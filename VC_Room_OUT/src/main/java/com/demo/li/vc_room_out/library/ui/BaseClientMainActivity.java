package com.demo.li.vc_room_out.library.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextClock;

import com.demo.li.vc_room_out.R;


public abstract class BaseClientMainActivity extends BaseActivity
{

    public TextClock tvTime;
    public Button btnCall;

    protected static int REQUEST_CODE_SETTING = 11;



    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
	super.onCreate(savedInstanceState);
	getWindow().addFlags(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
	setContentView(R.layout.activity_client_main);

	tvTime = (TextClock) findViewById(R.id.main_time_tv);
	btnCall = (Button) findViewById(R.id.main_call_btn);

	btnCall.setOnClickListener(new OnClickListener()
	{
	    public void onClick(View button)
	    {
		onCallClick();
	    }
	});


    }

    public abstract void onPasswordVaild();

    public abstract String getPassword();

    public abstract void onCallClick();

    public abstract void onServerIpChanged();

    public abstract void onSettingInfoChanged();

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
	super.onActivityResult(requestCode, resultCode, data);

    }

    @Override
    protected void onDestroy()
    {
	super.onDestroy();
	System.exit(0);
	android.os.Process.killProcess(android.os.Process.myPid());
    }
}
