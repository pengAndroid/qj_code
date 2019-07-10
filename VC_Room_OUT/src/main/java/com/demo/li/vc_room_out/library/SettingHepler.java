package com.demo.li.vc_room_out.library;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.demo.li.vc_room_out.R;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;


@SuppressLint("DefaultLocale")
public class SettingHepler
{
    private static SharedPreferences sharedPreferences;

    /**
     * 创建SharedPreferences
     */
    protected static SharedPreferences createSharedPreferences()
    {
	// 获取设置文件名称
	if (sharedPreferences == null)
	{
	    String configName = Global.currentActivity.getResources().getString(R.string.Config);
	    sharedPreferences = Global.currentActivity.getSharedPreferences(configName, Context.MODE_MULTI_PROCESS);
	}
	return sharedPreferences;
    }

    /**
     * 设置推流服务器IP地址
     */
    public static void setServerIp(String ip)
    {
	Editor editor = createSharedPreferences().edit();
	editor.putString("ServerIp", ip);
	editor.commit();
    }

    /**
     * 获取推流服务器IP地址
     */
    public static String getServerIp()
    {
	return createSharedPreferences().getString("ServerIp", "192.168.1.185");
    }

    /**
     * 设置室内IP地址
     */
    public static void setStationIp(String ip)
    {
	Editor editor = createSharedPreferences().edit();
	editor.putString("StationIp", ip);
	editor.commit();
    }

    /**
     * 获取室内IP地址
     */
    public static String getStationIp()
    {
	return createSharedPreferences().getString("StationIp", "192.168.1.150");
    }

    public static String getLocalIP()
    {
	try
	{
	    for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements();)
	    {
		NetworkInterface intf = en.nextElement();
		if (intf.getName().toLowerCase().equals("eth0") || intf.getName().toLowerCase().equals("wlan0"))
		{
		    for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements();)
		    {
			InetAddress inetAddress = enumIpAddr.nextElement();
			if (!inetAddress.isLoopbackAddress())
			{
			    String ipaddress = inetAddress.getHostAddress().toString();
			    if (!ipaddress.contains("::"))
			    {
				// return inetAddress;
				return ipaddress;
			    }
			}
		    }
		}
	    }
	}
	catch (Exception ex)
	{
//	    L.e(ex.toString());
	}
	return null;
    }

    public static void setDeviceName(String deviceName)
    {
	Editor editor = createSharedPreferences().edit();
	editor.putString("DeviceName", deviceName);
	editor.commit();
    }

    public static String getDeviceName()
    {
	return createSharedPreferences().getString("DeviceName", "设备001");
    }

    public static void setDeviceNumber(String deviceNumber)
    {
	Editor editor = createSharedPreferences().edit();
	editor.putString("DeviceNumber", deviceNumber);
	editor.commit();
    }

    public static String getDeviceNumber()
    {
	return createSharedPreferences().getString("DeviceNumber", "001");
    }

    public static void setPassword(String password)
    {
	Editor editor = createSharedPreferences().edit();
	editor.putString("Password", password);
	editor.commit();
    }

    public static String getPassword()
    {
	return createSharedPreferences().getString("Password", "123456");
    }

	/**
	 * 设置分辨率的大小
	 */
	public static void setVideoMode(int i){
		Editor editor = createSharedPreferences().edit();
		editor.putInt("VideoMode",i);
		editor.commit();
	}

	/**
	 * 获取分辨率的大小
	 * @return
	 */
	public static int getVideoMode(){
		return createSharedPreferences().getInt("VideoMode", 0);
	}

}
