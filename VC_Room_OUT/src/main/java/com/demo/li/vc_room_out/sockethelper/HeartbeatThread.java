package com.demo.li.vc_room_out.sockethelper;


import com.demo.li.vc_room_out.library.Global;

public class HeartbeatThread
{
    private static HeartbeatThread sendHeartBeatThread;

    public static HeartbeatThread getInstance()
    {
	if (sendHeartBeatThread == null) sendHeartBeatThread = new HeartbeatThread();
	return sendHeartBeatThread;
    }

    public void start()
    {
	Thread heartBeatThread = new Thread(new HeartbeatRunnable());
	heartBeatThread.start();
    }

    private class HeartbeatRunnable implements Runnable
    {
	public void run()
	{
	    while (true)
	    {
		try
		{
		    Thread.sleep(Global.CLIENT_HEARTBEAT_INTERVAL);
		    VisitSender.sendHeartBeat();
		}
		catch (Exception e)
		{
		    e.printStackTrace();
		}
	    }
	}
    }
}
