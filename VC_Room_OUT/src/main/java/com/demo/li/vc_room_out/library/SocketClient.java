package com.demo.li.vc_room_out.library;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class SocketClient
{
    private Thread thread;
    private Socket socket;
    private PrintWriter printWriter;
    private OnSocketClientListener listener;

    public SocketClient(Socket socket)
    {
	this.socket = socket;
    }

    public void start()
    {
	if (thread == null)
	{
	    thread = new Thread(new SocketClientRunnable());
	    thread.start();
	}
    }

    public void stop()
    {
	try
	{
	    if (thread != null)
	    {
		thread.interrupt();
		socket.close();
		socket = null;
		thread = null;
	    }
	}
	catch (Exception e)
	{
	    e.printStackTrace();
	}
    }

    private class SocketClientRunnable implements Runnable
    {
	public void run()
	{
	    InputStreamReader isr = null;
	    BufferedReader br = null;
	    try
	    {
		printWriter = new PrintWriter(socket.getOutputStream());
		isr = new InputStreamReader(socket.getInputStream());
		br = new BufferedReader(isr);
		while (!thread.isInterrupted())
		{
		    String content = br.readLine();
		    if (listener != null)
		    {
			listener.onReceived(content);
		    }
		}
	    }
	    catch (Exception e)
	    {
		e.printStackTrace();
	    }
	    finally
	    {
		try
		{
		    br.close();
		    isr.close();
		    printWriter.close();
		    printWriter = null;
		}
		catch (IOException e)
		{
		    e.printStackTrace();
		}
	    }
	}
    }

    public boolean send(String content)
    {
	boolean isSuccess = false;
	try
	{
	    if (socket == null || printWriter == null)
	    {
		return isSuccess;
	    }
	    printWriter.println(content);
	    printWriter.flush();
	    isSuccess = true;
	}
	catch (Exception e)
	{
	    e.printStackTrace();
	}
	return isSuccess;
    }

    public interface OnSocketClientListener
    {
	void onReceived(String content);
    }
}
