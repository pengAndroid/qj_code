package com.demo.li.vc_room_out.sockethelper;

import android.text.TextUtils;


import com.demo.li.vc_room_out.library.ClientSettingHelper;
import com.demo.li.vc_room_out.library.Global;
import com.demo.li.vc_room_out.library.entity.CallOff;
import com.demo.li.vc_room_out.library.entity.CallRequest;
import com.demo.li.vc_room_out.library.entity.CallRequestResult;
import com.demo.li.vc_room_out.library.entity.CmdData;
import com.demo.li.vc_room_out.library.entity.CmdType;
import com.demo.li.vc_room_out.library.entity.ServerInfo;
import com.demo.li.vc_room_out.library.entity.WarningInfo;
import com.demo.li.vc_room_out.library.tools.JSONUtil;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * author
 * */
public class SocketVisitClient
{
    public Socket socket;

    private BufferedReader bufferedReader;
    private InputStream inputStream;
    private InputStreamReader inputStreamReader;
    private PrintWriter printWriter;
    private OutputStream outputStream;
    private Thread socketClientThread;

    private static SocketVisitClient instance;

    public static SocketVisitClient getInstance()
    {
	if (instance == null) instance = new SocketVisitClient();
	return instance;
    }

    public void start()
    {
	socketClientThread = new Thread(new SocketClientRunnable());
	socketClientThread.start();
    }

    private class SocketClientRunnable implements Runnable
    {
	public void run()
	{
	    try
	    {
		socket = new Socket(ClientSettingHelper.getStationIp(), Global.STATION_PORT);

		inputStream = socket.getInputStream();
		inputStreamReader = new InputStreamReader(inputStream);
		bufferedReader = new BufferedReader(inputStreamReader);

		outputStream = socket.getOutputStream();
		printWriter = new PrintWriter(outputStream);

		VisitSender.sendDeviceInfo();//连接后发送自己的IP等信息给室内端

		String content = null;
		while (bufferedReader != null && (content = bufferedReader.readLine()) != null)
		{
		    String error = received(content);
		    if (!TextUtils.isEmpty(error))
		    {
			Global.showToast(error, false);
		    }
		}
		socket = null;
		restart();
	    }
	    catch (Exception e)
	    {
		e.printStackTrace();
		Global.showToast("室内端断开连接", false);
		socket = null;
		restart();
	    }
	}
    }

    private String received(String content)
    {
	String error = null;
	if (TextUtils.isEmpty(content))
	{
	    error = "读取数据出错";
	    return error;
	}

	final CmdData cmdData = JSONUtil.parseInfo(content, CmdData.class);

	if (cmdData == null)
	{
	    error = "指令解析错误";
	    return error;
	}
	Global.currentActivity.runOnUiThread(new Runnable()
	{
	    @Override
	    public void run()
	    {
		switch (cmdData.cmdType)
		{
		    case SERVER_INFO:
			ServerInfo serverInfo = JSONUtil.parseInfo((String) cmdData.data, ServerInfo.class);
			VisitReceiver.receivedServerInfo(serverInfo);
			break;
			case CALL_REQUEST:
				CallRequest callRequest = JSONUtil.parseInfo((String) cmdData.data, CallRequest.class);
				VisitReceiver.receivedCallRequest(callRequest);
			break;
		    case CALL_REQUEST_RESULT:
			CallRequestResult callRequestResult = JSONUtil.parseInfo((String) cmdData.data, CallRequestResult.class);
			VisitReceiver.receivedCallRequestResult(callRequestResult);
			break;
		    case CALL_OFF:
			CallOff callOff = JSONUtil.parseInfo((String) cmdData.data, CallOff.class);
			VisitReceiver.receivedCallOff(callOff);
			break;
		    case WARNIMG_INFO:
			WarningInfo warningInfo = JSONUtil.parseInfo((String) cmdData.data, WarningInfo.class);
			VisitReceiver.receivedWarningInfo(warningInfo);
			break;
		    default:
			break;
		}
	    }
	});
	return error;
    }

    /**
     * 重启SocketClient
     */
    public void restart()
    {
	new Thread(new Runnable()
	{

	    @Override
	    public void run()
	    {
		try
		{
		    if (socketClientThread != null)
		    {
			socketClientThread.interrupt();//中断掉这个线程
			socketClientThread = null;//并且设置为空
			close();//释放掉所有临时资源
		    }
		    Thread.sleep(Global.CLIENT_RESTART_SLEEP_TIME);
		    start();
		}
		catch (Exception e)
		{
		    e.printStackTrace();
		}
	    }
	}).start();
    }

    public void stop()
    {
	new Thread(new Runnable()
	{

	    @Override
	    public void run()
	    {
		try
		{
		    if (socketClientThread != null)
		    {
			socketClientThread.interrupt();
			socketClientThread = null;
			close();
		    }
		}
		catch (Exception e)
		{
		    e.printStackTrace();
		}
	    }
	}).start();
    }

    public void close()
    {
	try
	{
	    if (socket != null)
	    {
		socket.close();
		socket = null;
	    }

	    if (bufferedReader != null)
	    {
		bufferedReader.close();
		bufferedReader = null;
	    }
	    if (inputStreamReader != null)
	    {
		inputStreamReader.close();
		inputStreamReader = null;
	    }

	    if (inputStream != null)
	    {
		inputStream.close();
		inputStream = null;
	    }

	    if (socket != null)
	    {
		socket.close();
		socket = null;
	    }

	    if (outputStream != null)
	    {
		outputStream.close();
		outputStream = null;
	    }

	    if (printWriter != null)
	    {
		printWriter.close();
		printWriter = null;
	    }
	}
	catch (Exception e)
	{
	    e.printStackTrace();
	}
    }

    public <T> boolean send(CmdType cmdType, T t)
    {
	boolean isSuccess = false;
	if (socket == null && cmdType != CmdType.HEARTBEAT)
	{
	    // Global.showToast("与室内端断开连接", false);
	    return isSuccess;
	}
	try
	{
	    CmdData instructionsData = new CmdData();
	    instructionsData.cmdType = cmdType;
	    instructionsData.data = JSONUtil.toJson(t);
	    final String jsonData = JSONUtil.toJson(instructionsData);
	    new Thread()
	    {
		public void run()
		{
		    try
		    {
			printWriter.write(jsonData + "\n");
			printWriter.flush();
		    }
		    catch (Exception e)
		    {
//			e.printStackTrace();
			Global.showToast("发送数据出错", false);
		    }
		}
	    }.start();
	    isSuccess = true;
	}
	catch (Exception e)
	{
//	    e.printStackTrace();
	    Global.showToast("发送数据出错", false);
	}
	return isSuccess;
    }
}
