package com.demo.li.vc_room_out.library;

import android.content.SharedPreferences.Editor;
import android.text.TextUtils;

import java.util.UUID;


//Sp存储数据
public class ClientSettingHelper extends SettingHepler
{
    /**
     * 设置设备唯一ID
     */
    private static void setDeviceUUID(String uuid)
    {
	Editor editor = createSharedPreferences().edit();
	editor.putString("deviceUUID", uuid);
	editor.commit();
    }

    /**
     * 获取设备唯一ID
     */
    public static String getDeviceUUID()
    {
	String uuid = createSharedPreferences().getString("deviceUUID", "");
	if(TextUtils.isEmpty(uuid))
	{
	    uuid = UUID.randomUUID().toString();
	    setDeviceUUID(uuid);
	}
	return uuid;
    }
}
