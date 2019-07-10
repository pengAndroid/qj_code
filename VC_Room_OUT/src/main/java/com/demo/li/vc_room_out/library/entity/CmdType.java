package com.demo.li.vc_room_out.library.entity;

public enum CmdType
{
    /**
     * 发送或接收客户端设备信息
     */
    DEVICE_INFO,
    
    /**
     * 发送或接收服务端信息
     */
    SERVER_INFO,
    
    /**
     * 发送或接收心跳
     */
    HEARTBEAT,
    
    /**
     * 发送或接收视频通话请求
     */
    CALL_REQUEST,
    
    /**
     * 发送或接收视频通话请求结果，该指令用于（拨通）过程中挂断
     */
    CALL_REQUEST_RESULT,
    
    /**
     * 发送或接收视频挂掉指令，该指令用于（通话）过程中挂断
     */
    CALL_OFF,

    /**
     * 发送或接收提示警告信息
     */
    WARNIMG_INFO,

}
