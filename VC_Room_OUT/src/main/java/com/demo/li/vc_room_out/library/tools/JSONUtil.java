package com.demo.li.vc_room_out.library.tools;

import com.google.gson.Gson;

import java.util.List;
import java.util.Map;

public class JSONUtil
{

    private static Gson gson = new Gson();

    /**
     * 将json转换为指定实体对象
     * 
     * @param json
     *            源json数据
     * @param cls
     *            要转换的对象class
     * @return 转换后的对象
     */
    public static <T> T parseInfo(String json, Class<T> cls)
    {
	T t = null;
	try
	{
	    t = gson.fromJson(json, cls);
	}
	catch (IllegalStateException ignored)
	{
	}
	if (t == null)
	{
	    try
	    {
		t = cls.newInstance();
	    }
	    catch (InstantiationException e)
	    {
		e.printStackTrace();
	    }
	    catch (IllegalAccessException e)
	    {
		e.printStackTrace();
	    }
	}
	return t;
    }

    /** 将指定实体对象转换为json */
    public static <T> String toJson(T t)
    {
	return gson.toJson(t);
    }

    /** 将指定实体对象数组转换为json */
    public static <T> String toListJson(List<T> list)
    {
	return gson.toJson(list);
    }

    /** 将Map转换为json */
    public static <T> String toMapJson(Map<T, T> map)
    {
	return gson.toJson(map);
    }
}
