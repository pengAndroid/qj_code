package com.demo.li.vc_room_out.sockethelper;



import android.content.Intent;
import android.widget.Toast;

import com.demo.li.vc_room_out.InTeleDialog;
import com.demo.li.vc_room_out.library.ClientSettingHelper;
import com.demo.li.vc_room_out.library.Global;
import com.demo.li.vc_room_out.library.entity.CallOff;
import com.demo.li.vc_room_out.library.entity.CallRequest;
import com.demo.li.vc_room_out.library.entity.CallRequestResult;
import com.demo.li.vc_room_out.library.entity.ServerInfo;
import com.demo.li.vc_room_out.library.entity.WarningInfo;
import com.demo.li.vc_room_out.library.tools.Common;
import com.demo.li.vc_room_out.library.tools.RingtonePlayer;
import com.demo.li.vc_room_out.library.tools.TimeUtils;


public class VisitReceiver
{

	static InTeleDialog mTeleDialog;
    public static void receivedServerInfo(ServerInfo serverInfo)
    {
	System.out.println("服务器信息" + serverInfo.serverTime);
	ClientSettingHelper.setPassword(serverInfo.password);
	ClientSettingHelper.setServerIp(serverInfo.serverIP);
//	TimeUtils.setDateTime(serverInfo.serverTime);
    }

	/**
	 * 收到视频请求
	 */
	public static void receivedCallRequest(CallRequest callRequest){
		if (visitListener != null)
		{
			mTeleDialog = new InTeleDialog(Global.currentActivity,callRequest);
			mTeleDialog.show();
			RingtonePlayer.playRingtone(Global.currentActivity);
			visitListener.onCallRequest(callRequest);
		}
	}
	/**
     * 收到视频通话请求结果
     */
    public static void receivedCallRequestResult(CallRequestResult callRequestResult)
    {
	if (visitListener != null)
	{
		if ( mTeleDialog!= null && mTeleDialog.isShowing() ==true &&callRequestResult.status == 3) {
			mTeleDialog.cancel();
			Toast.makeText(Global.currentActivity,callRequestResult.message,Toast.LENGTH_SHORT).show();
		}
		else {
			Intent intent = new Intent(Common.BROADCAST_VISIT);
			intent.putExtra(Common.TAG, Common.BROADCAST_VISIT_TAG_CAll_REQUEST_RESULT);
			intent.putExtra(Common.BROADCAST_VISIT_TAG_CAll_REQUEST_RESULT, callRequestResult);
			Global.currentActivity.sendBroadcast(intent);
		}
	    visitListener.onCallRequestResult(callRequestResult);
	}
    }

    /**
     * 收到视频通话挂断指令
     */
    public static void receivedCallOff(CallOff callOff)
    {
	if (visitListener != null)
	{
		Intent intent = new Intent(Common.BROADCAST_VISIT);
		intent.putExtra(Common.TAG, Common.BROADCAST_VISIT_TAG_CAll_Off);
		intent.putExtra(Common.BROADCAST_VISIT_TAG_CAll_Off, callOff);
		Global.currentActivity.sendBroadcast(intent);
		Toast.makeText(Global.currentActivity,"对方主动挂断通话",Toast.LENGTH_SHORT).show();
	    visitListener.onCallOff(callOff);
	}
    }


    /**
     * 收到提示信息
     */
    public static void receivedWarningInfo(WarningInfo warningInfo)
    {
	if (warningInfo.level == 0)
	{
	    Global.showToast(warningInfo.message, true);
	}
	else if (visitListener != null)
	{
	    visitListener.receivedWarningInfo(warningInfo);
		Intent intent = new Intent(Common.BROADCAST_VISIT);
		intent.putExtra(Common.TAG,Common.BROADCAST_VISIT_TAG_WARNING_INFO);
		intent.putExtra(Common.BROADCAST_VISIT_TAG_WARNING_INFO,warningInfo);
		Global.currentActivity.sendBroadcast(intent);
	}
    }

    private static VisitListener visitListener;

    public static void setListener(VisitListener visitListener)
    {
	VisitReceiver.visitListener = visitListener;

    }

    public interface VisitListener
    {

	void receivedWarningInfo(WarningInfo warningInfo);//收到提示信息

	void onCallRequestResult(CallRequestResult callRequestResult);//收到视频通话请求结果

	void onCallRequest(CallRequest callRequest);//收到视频通话请求

	void onCallOff(CallOff callOff);


    }
}
