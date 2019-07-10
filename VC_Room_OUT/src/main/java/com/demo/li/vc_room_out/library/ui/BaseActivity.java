package com.demo.li.vc_room_out.library.ui;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.demo.li.vc_room_out.BaseUtils;
import com.demo.li.vc_room_out.R;
import com.demo.li.vc_room_out.library.Global;
import com.demo.li.vc_room_out.library.tools.Utils;


import java.util.Timer;
import java.util.TimerTask;

@SuppressLint("ShowToast")
public class BaseActivity extends Activity
{
    // public BaseActivity self;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
	super.onCreate(savedInstanceState);
	// 全屏
//	requestWindowFeature(Window.FEATURE_NO_TITLE);
//	getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

	getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);// 屏幕常亮

	Global.currentActivity = this;
    }

    // 隐藏虚拟按钮
    @Override
    public void onWindowFocusChanged(boolean hasFocus)
    {
	super.onWindowFocusChanged(hasFocus);
//	if (hasFocus && Build.VERSION.SDK_INT >= 19)
//	{
//	    View decorView = getWindow().getDecorView();
//	    decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
//		    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
//	}
    }

    public void showToast(String msg, boolean showLong)
    {
	Toast.makeText(this, msg, showLong ? Toast.LENGTH_LONG : Toast.LENGTH_SHORT).show();
    }

    public void showToast(String msg, int sec)
    {
	Toast toast = Toast.makeText(this, msg, Toast.LENGTH_LONG);
	View view = LayoutInflater.from(this).inflate(R.layout.toast, null);
	toast.setView(view);
	toast.setGravity(Gravity.TOP | Gravity.CENTER_HORIZONTAL, 0, getResources().getDimensionPixelSize(R.dimen.x120));

	TextView text = (TextView) view.findViewWithTag("text");
	text.setText(msg);
	execToast(toast, sec * 1000);
    }

    private static void execToast(final Toast toast, int time)
    {
	final Timer timer = new Timer();
	timer.schedule(new TimerTask()
	{
	    @Override
	    public void run()
	    {
		toast.show();
	    }
	}, 0, 3000);
	new Timer().schedule(new TimerTask()
	{
	    @Override
	    public void run()
	    {
		toast.cancel();
		timer.cancel();
		cancel();
	    }
	}, time);
    }

    @Override
    public void onResume()
    {
	super.onResume();
	Global.currentActivity = this;
//		if (BaseUtils.isLandscape(this) && this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT)
//			setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event)
    {
	// 屏蔽按键事件
	if (event.getKeyCode() == KeyEvent.KEYCODE_BACK || event.getKeyCode() == KeyEvent.KEYCODE_HOME || event.getKeyCode() == KeyEvent.KEYCODE_MENU)//KeyEvent.KEYCODE_HOME
	{
	    return false;
	}
	return super.dispatchKeyEvent(event);
    }
}
