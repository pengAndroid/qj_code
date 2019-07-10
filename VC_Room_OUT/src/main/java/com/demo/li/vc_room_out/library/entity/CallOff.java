package com.demo.li.vc_room_out.library.entity;

import java.io.Serializable;

/**
 * 视频挂掉指令
 */
public class CallOff implements Serializable
{
    /**
     * 通话唯一ID
     */
    public String session;
    
    /**
     * 需要挂断的ip
     */
    public String deviceIP;

    /**
     * 需要挂断的设备编号
     */
    public String deviceNumber;
    
    /**
     * 是否自动挂断（室内设置的自动断开时间到达后由探视端发送自动挂断信息给室内更新状态）
     */
    public boolean isAutoCallOff = false;
}
