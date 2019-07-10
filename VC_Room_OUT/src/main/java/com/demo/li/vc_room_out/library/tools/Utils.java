package com.demo.li.vc_room_out.library.tools;

public class Utils
{
    public static String createSession()
    {
	// 10^8 至 2*10^8之间
	int s = 100000001;
	int e = 200000000;
	int id = (int) (s + Math.random() * (e - s));
	return id + "";
    }
}
