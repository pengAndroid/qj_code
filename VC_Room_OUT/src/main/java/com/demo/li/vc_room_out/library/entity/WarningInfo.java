package com.demo.li.vc_room_out.library.entity;

import java.io.Serializable;

/**
 * 室内给室外发送警告信息
 */
public class WarningInfo implements Serializable
{
    /**
     * 警告信息内容
     */
    public String message;

    /**
     * 提示等级：0最高代表错误，气泡显示。其他label显示
     */
    public int level;
}
