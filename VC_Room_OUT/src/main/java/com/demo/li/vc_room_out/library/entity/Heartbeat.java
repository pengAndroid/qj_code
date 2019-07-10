package com.demo.li.vc_room_out.library.entity;

/**
 * 室外端发送心跳
 */
public class Heartbeat
{
    public String deviceName;// 设备名称
    public String deviceNumber;// 设备编号

    public Heartbeat(String deviceName, String deviceNumber)
    {
	super();
	this.deviceName = deviceName;
	this.deviceNumber = deviceNumber;
    }
}
