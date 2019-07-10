package com.demo.li.vc_room_out.library.tools;

import android.content.Context;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Handler;

public class RingtonePlayer
{
    private static final int RINGTONE = 1;
    private static final int ALARM = 2;
    private static final int NOTIFICATION = 3;
    private static Ringtone ringtone;


    // 来电
    public static void playRingtone(Context context)
    {
	if (ringtone == null || !ringtone.isPlaying())
	{
	    Uri uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE);
	    ringtone = RingtoneManager.getRingtone(context, uri);
	    handler.sendEmptyMessage(RINGTONE);
	}
    }

    public static void stopRingtone()
    {
	handler.removeMessages(RINGTONE);
	if (ringtone != null && ringtone.isPlaying())
	{
	    ringtone.stop();
	    ringtone = null;
	}
    }


    private static Handler handler = new Handler()
    {
	public void handleMessage(android.os.Message msg)
	{
	    switch (msg.what)
	    {
		case RINGTONE:
		    if (!ringtone.isPlaying())
		    {
			ringtone.play();
		    }
		    //sendEmptyMessageDelayed(RINGTONE, 1000);
		    break;

		default:
		    break;
	    }
	}
    };
}
