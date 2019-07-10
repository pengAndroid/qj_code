package com.demo.li.vc_room_out.sockethelper;


import com.demo.li.vc_room_out.library.ClientSettingHelper;
import com.demo.li.vc_room_out.library.Global;
import com.demo.li.vc_room_out.library.entity.CallOff;
import com.demo.li.vc_room_out.library.entity.CallRequest;
import com.demo.li.vc_room_out.library.entity.CallRequestResult;
import com.demo.li.vc_room_out.library.entity.CmdType;
import com.demo.li.vc_room_out.library.entity.DeviceInfo;
import com.demo.li.vc_room_out.library.entity.Heartbeat;

public class VisitSender
{
    public static boolean sendDeviceInfo()
    {
	DeviceInfo deviceInfo = new DeviceInfo();
	deviceInfo.deviceName = ClientSettingHelper.getDeviceName();
	deviceInfo.deviceType = Global.DEVICE_TYPE_OUT_ROOM;
	deviceInfo.deviceNumber = ClientSettingHelper.getDeviceNumber();
	deviceInfo.deviceUUID = ClientSettingHelper.getDeviceUUID();
	boolean val = SocketVisitClient.getInstance().send(CmdType.DEVICE_INFO, deviceInfo);
	return val;
    }

    public static boolean sendHeartBeat()
    {
	Heartbeat heartBeat = new Heartbeat(ClientSettingHelper.getDeviceName(), ClientSettingHelper.getDeviceNumber());
	boolean val = SocketVisitClient.getInstance().send(CmdType.HEARTBEAT, heartBeat);
	return val;
    }

    /**
     * 发送视频通话请求
     */
    public static boolean sendCallRequest(CallRequest callRequest)
    {
	boolean val = SocketVisitClient.getInstance().send(CmdType.CALL_REQUEST, callRequest);
	return val;
    }

    /**
     * 发送视频通话挂断指令
     */
    public static boolean sendCallOff(CallOff callOff)
    {
	boolean val = SocketVisitClient.getInstance().send(CmdType.CALL_OFF, callOff);
	return val;
    }
    /**
     * 发送视频通话请求的结果
     */
    public static boolean sendCallRequestResult(CallRequestResult callRequestResult)
    {
        boolean val = SocketVisitClient.getInstance().send(CmdType.CALL_REQUEST_RESULT, callRequestResult);
        return val;
    }


}
