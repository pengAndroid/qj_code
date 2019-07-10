package com.demo.li.vc_room_out.library.entity;

import java.io.Serializable;

public class DeviceInfo implements Serializable
{
    public int deviceType;// 设备类型0室内  2室外端
    public String deviceName;// 设备名称
    public String deviceNumber;// 设备编号
    public String deviceUUID;// 设备编号
    public String deviceIP;// 设备端Ip地址
    public long lastHeartBeatTime; // 最后心跳时间
}
