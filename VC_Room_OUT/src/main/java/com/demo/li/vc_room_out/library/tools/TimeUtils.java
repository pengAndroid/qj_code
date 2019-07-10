package com.demo.li.vc_room_out.library.tools;

import android.os.SystemClock;

import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class TimeUtils
{

    static final String TAG = "SystemDateTime";

    /** 将当前系统时间设定为给定值 */
    public static void setDateTime(int year, int month, int day, int hour, int minute, int seconds) throws IOException, InterruptedException
    {
        requestPermission();

        Calendar c = Calendar.getInstance();

        c.set(Calendar.YEAR, year);
        c.set(Calendar.MONTH, month - 1);
        c.set(Calendar.DAY_OF_MONTH, day);
        c.set(Calendar.HOUR_OF_DAY, hour);
        c.set(Calendar.MINUTE, minute);
        c.set(Calendar.SECOND, seconds);

        long when = c.getTimeInMillis();

        if (when / 1000 < Integer.MAX_VALUE)
        {
            SystemClock.setCurrentTimeMillis(when);
        }

        long now = Calendar.getInstance().getTimeInMillis();
        //Log.d(TAG, "set tm="+when + ", now tm="+now);

        if (now - when > 1000)
            throw new IOException("failed to set Date.");
    }

    /**
     * 将当前时间设置为指定的tick值。
     *
     * @param milliseconds
     */
    public static boolean setDateTime(long milliseconds)
    {
        try
        {
//            requestPermission();

            if (milliseconds / 1000 < Integer.MAX_VALUE)
            {
                SystemClock.setCurrentTimeMillis(milliseconds);
            }
            return Math.abs(new Date().getTime() - milliseconds) < 1000;
        }
        catch (Exception e)
        {
//            L.e("设置系统时间为:" + milliseconds + "异常", e);
            return false;
        }
    }

    /**
     * 将当前时间设置为yyyy-MM-dd HH:mm:ss的日期字符串对应的时间
     *
     * @param now
     */
    public static boolean setDateTime(String now)
    {
        try
        {
            requestPermission();

            long time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(now).getTime(); //DateTimeUtils.tryParseDate(now, "yyyy-MM-dd HH:mm:ss").getTime();
            if (time / 1000 < Integer.MAX_VALUE)
            {
                SystemClock.setCurrentTimeMillis(time);
            }
            return Math.abs(new Date().getTime() - time) < 1000;
        }
        catch (Exception e)
        {
//            L.e("设置系统时间为:" + now + "异常", e);
            return false;
        }
    }

    /** 将当前系统时间设定为给定的年月日 */
    public static void setDate(int year, int month, int day) throws IOException, InterruptedException
    {

        requestPermission();

        Calendar c = Calendar.getInstance();

        c.set(Calendar.YEAR, year);
        c.set(Calendar.MONTH, month);
        c.set(Calendar.DAY_OF_MONTH, day);
        long when = c.getTimeInMillis();

        if (when / 1000 < Integer.MAX_VALUE)
        {
            SystemClock.setCurrentTimeMillis(when);
        }

        long now = Calendar.getInstance().getTimeInMillis();
        //Log.d(TAG, "set tm="+when + ", now tm="+now);

        if (now - when > 1000)
            throw new IOException("failed to set Date.");
    }

    public static void setTime(int hour, int minute) throws IOException, InterruptedException
    {

        requestPermission();

        Calendar c = Calendar.getInstance();

        c.set(Calendar.HOUR_OF_DAY, hour);
        c.set(Calendar.MINUTE, minute);
        long when = c.getTimeInMillis();

        if (when / 1000 < Integer.MAX_VALUE)
        {
            SystemClock.setCurrentTimeMillis(when);
        }

        long now = Calendar.getInstance().getTimeInMillis();
        //Log.d(TAG, "set tm="+when + ", now tm="+now);

        if (now - when > 1000)
            throw new IOException("failed to set Time.");
    }

    static void requestPermission() throws InterruptedException, IOException
    {
        createSuProcess("chmod 666 /dev/alarm").waitFor();
    }

    static Process createSuProcess() throws IOException
    {
        File rootUser = new File("/system/xbin/ru");
        if (rootUser.exists())
        {
            return Runtime.getRuntime().exec(rootUser.getAbsolutePath());
        }
        else
        {
            return Runtime.getRuntime().exec("su");
        }
    }

    static Process createSuProcess(String cmd) throws IOException
    {

        DataOutputStream os = null;
        Process process = createSuProcess();

        try
        {
            os = new DataOutputStream(process.getOutputStream());
            os.writeBytes(cmd + "\n");
            os.writeBytes("exit $?\n");
        }
        finally
        {
            if (os != null)
            {
                try
                {
                    os.close();
                }
                catch (IOException e)
                {
                }
            }
        }

        return process;
    }
}
