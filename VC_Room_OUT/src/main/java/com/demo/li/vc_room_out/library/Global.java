package com.demo.li.vc_room_out.library;

import android.app.Activity;
import android.widget.Toast;

public class Global
{
    public static final int STATION_PORT = 8888;
    public static final int PUSH_PORT = 1935;

    public static final int CLIENT_RESTART_SLEEP_TIME = 5000;
    public static final int CLIENT_HEARTBEAT_INTERVAL = 5000;
    
    public static final String DEVICE_NUMBER_STATION = "000";
    public static final int DEVICE_TYPE_STATION = 0;// 室内
    public static final int DEVICE_TYPE_OUT_ROOM = 2;// 室外


    /**
     * 当前的Activity界面
     */
    public static Activity currentActivity;

    /**
     * 适配多进程的问题
     */

    public static boolean isVisitCallMultiProcessToShowActivity = false;
    public static boolean isCalling = false;// 是否正在通话中

    public static void showToast(final String msg, final boolean showLong)
    {
	currentActivity.runOnUiThread(new Runnable()
	{
	    
	    @Override
	    public void run()
	    {
		Toast.makeText(currentActivity, msg, showLong ? Toast.LENGTH_LONG : Toast.LENGTH_SHORT).show();
	    }
	});
    }
    
    /**
     * 创建直播地址
     * */
    public static String createUrl(String session, int deviceType, String deviceNumber)
    {
	return "rtmp://" + SettingHepler.getServerIp() + ":" + PUSH_PORT + "/" + session + "/" + deviceType + "-" + deviceNumber;
    }

}
