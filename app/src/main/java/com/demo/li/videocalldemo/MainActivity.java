package com.demo.li.videocalldemo;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.demo.li.vc_room_out.BaseUtils;
import com.demo.li.vc_room_out.ClientSettingActivity;
import com.demo.li.vc_room_out.VisitCallActivity;
import com.demo.li.vc_room_out.library.Global;
import com.demo.li.vc_room_out.library.entity.CallOff;
import com.demo.li.vc_room_out.library.entity.CallRequest;
import com.demo.li.vc_room_out.library.entity.CallRequestResult;
import com.demo.li.vc_room_out.library.entity.WarningInfo;
import com.demo.li.vc_room_out.sockethelper.HeartbeatThread;
import com.demo.li.vc_room_out.sockethelper.SocketVisitClient;
import com.demo.li.vc_room_out.sockethelper.VisitReceiver;


public class MainActivity extends AppCompatActivity implements VisitReceiver.VisitListener {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Utils.printResolution(this);

        ////这三句代码必须得在主页面Activity中写///////
        Global.currentActivity = this;
        SocketVisitClient.getInstance().start();//连接室内端的socket并且开始监听回来的数据
        HeartbeatThread.getInstance().start();// 心跳线程
        VisitReceiver.setListener(this);//监听电话的几个状态----
        //////////////////////////////////////////

        findViewById(R.id.button1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, VisitCallActivity.class);
                startActivity(intent);
            }
        });
        findViewById(R.id.button2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ClientSettingActivity.class);
                startActivity(intent);
            }
        });
    }


    @Override
    protected void onResume() {
        super.onResume();
        if (Utils.isLandscape(this) && this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT)
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
    }

    @Override
    public void receivedWarningInfo(WarningInfo warningInfo) {

    }

    @Override
    public void onCallRequestResult(CallRequestResult callRequestResult) {

    }

    @Override
    public void onCallRequest(CallRequest callRequest) {

    }

    @Override
    public void onCallOff(CallOff callOff) {
    }

}
