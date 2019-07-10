package com.demo.li.vc_room_out.library.entity;

import java.io.Serializable;

/**
 * 视频请求返回结果
 */
public class CallRequestResult implements Serializable
{
    /**
     * 返回状态 0为请求不通过(室内正在通话中或正忙)，1为请求通过(室内未通话)，2为已接听 ，3为挂断
     */
    public int status;

    /**
     * 通话唯一ID
     */
    public String session;
    
    /**
     * 请求返回的消息
     */
    public String message;
}
