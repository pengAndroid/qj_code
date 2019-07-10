package com.demo.li.vc_room_out;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.RemoteException;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;


import com.demo.li.vc_room_out.library.ClientSettingHelper;
import com.demo.li.vc_room_out.library.Global;
import com.demo.li.vc_room_out.library.entity.CallOff;
import com.demo.li.vc_room_out.library.entity.CallRequestResult;
import com.demo.li.vc_room_out.library.entity.WarningInfo;
import com.demo.li.vc_room_out.library.tools.Common;
import com.demo.li.vc_room_out.library.tools.Utils;
import com.demo.li.vc_room_out.library.ui.Base1CallActivity;
import com.demo.li.vc_room_out.service.MyService;




import java.util.Timer;
import java.util.TimerTask;

//新思路处理：将本页面独立出来一个新的进程 Socket的逻辑放到MainActivity中，然后回调也在mainActivity中再交由广播通信到VisitCallActivity做对应的操作。
//考虑到MainActivity中页面会跑onpause生命周期，所以使用AIDL来处理吧。本页面跟MainActivity的通信吧。
@SuppressLint("HandlerLeak")
public class VisitCallActivity extends Base1CallActivity {
    private static final int MSG_WHAT_ACO = 1;// 自动挂断通话
    private static final int ONCREATE_WHAT_TO_SENDVISIT = 3;//发送视频指令
    private static final int ONCREATE_WHAT_TO_GETVISIT = 4;//接受视频指令
    private static final String TAG = "VisitCallActivity";
    public CallOff callOff = new CallOff();//视频挂掉指令
    String theurl = "";
    Boolean isToCall = false;
    String mSessionGetVisit = "";

    public IOutRoomAidlInterface iVisitAidlService;
    //定义一个广播 ：用于接收Socket通信然后进行 广播在onResume跟onPause来定义取消
    private ServiceConnection mServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            iVisitAidlService = IOutRoomAidlInterface.Stub.asInterface(service);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            iVisitAidlService = null;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //注册AIDLService
        Bundle args = new Bundle();
        Intent intent = new Intent(VisitCallActivity.this, MyService.class);
        intent.putExtras(args);
        bindService(intent, mServiceConnection, Context.BIND_AUTO_CREATE);//最后一个参数是自动连接的意思


        btnCut.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                try {
                    if (isToCall) {
                        iVisitAidlService.sendCallRequestResult(3,callOff.session,"室外机已挂断通话");

                    }else{
                        iVisitAidlService.sendCallOf(mSessionGetVisit, callOff.deviceIP, callOff.deviceNumber, callOff.isAutoCallOff);
                    }
                    iVisitAidlService.setIsCalling(false);
                }
                catch (RemoteException e) {
                    e.printStackTrace();
                }
                finish();
            }
        });

        isToCall = getIntent().getBooleanExtra("isToCall", true);
        Log.i(TAG, "onCreate: isToCall"+ isToCall);
        Message msg = new Message();
        if (isToCall) {//自己呼叫室内端
            msg.what = ONCREATE_WHAT_TO_SENDVISIT;
        }
        else {//室内端呼叫室外
            tvToast.setVisibility(View.INVISIBLE);
            msg.what = ONCREATE_WHAT_TO_GETVISIT;
            mSessionGetVisit = getIntent().getStringExtra("session");
        }
        handler.sendMessageDelayed(msg, 500);
    }

    @Override
    public void onResume() {
        super.onResume();
        //注册广播
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Common.BROADCAST_VISIT);
        registerReceiver(receiver, intentFilter);
    }

    BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String extarName = intent.getStringExtra(Common.TAG);
            if (extarName.equals(Common.BROADCAST_VISIT_TAG_CAll_Off)) {//对方主动挂断通话
                onCallOff();
            }
            else if (extarName.equals(Common.BROADCAST_VISIT_TAG_WARNING_INFO)) {//收到警告信息
                WarningInfo warningInfo = (WarningInfo) intent.getSerializableExtra(extarName);
                receivedWarningInfo(warningInfo);
            }
            else if (extarName.equals(Common.BROADCAST_VISIT_TAG_CAll_REQUEST_RESULT)) {// 收到视频通话请求结果
                CallRequestResult callRequestResult = (CallRequestResult) intent.getSerializableExtra(extarName);
                onCallRequestResult(callRequestResult);

            }
        }
    };


    public void onCallOff() {
//        showToast("对方主动挂断通话", false);
        exit();

    }
    public void receivedWarningInfo(WarningInfo warningInfo) {
        if (warningInfo.level == 0) {
            showToast(warningInfo.message, true);
        }
        else {
            showToast(warningInfo.message, 10);
        }
    }
    // 0为请求不通过(室内正在通话中或正忙)，1为请求通过(室内未通话)，2为已接听 ，3为挂断
    public void onCallRequestResult(CallRequestResult callRequestResult) {
        if (callRequestResult == null) return;
        if (callRequestResult.status == 0 || callRequestResult.status == 3) {
            showToast(callRequestResult.message, false);
            btnCut.setEnabled(false);
            exit();
        }
        else if (callRequestResult.status == 2) {
            tvToast.setVisibility(View.INVISIBLE);
             mTencentPlayer.start(Global.createUrl(callRequestResult.session, Global.DEVICE_TYPE_STATION, Global.DEVICE_NUMBER_STATION), remoteSurface);
            isToCall = false;

        }
    }

    private void exit() {
        new Timer().schedule(new TimerTask() {

            @Override
            public void run() {
                try {
                    iVisitAidlService.setIsCalling(false);
                }
                catch (RemoteException e) {
                    e.printStackTrace();
                }
                VisitCallActivity.this.finish();
            }
        }, 1500);
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_WHAT_ACO:
                    Toast.makeText(VisitCallActivity.this, "您的通话时间已到达护士限制时间", Toast.LENGTH_LONG).show();
                    System.out.println(callOff.session);
                    callOff.isAutoCallOff = true;
                    try {
                        iVisitAidlService.sendCallOf(callOff.session, callOff.deviceIP, callOff.deviceNumber, callOff.isAutoCallOff);
                        iVisitAidlService.setIsCalling(false);
                    }
                    catch (RemoteException e) {
                        e.printStackTrace();
                    }

                    finish();
                    break;
                case ONCREATE_WHAT_TO_SENDVISIT:
                    //视频请求
                    String session = Utils.createSession();
                    int videoMode = 0;
                    // 发送视频通话请求
                    try {
                        iVisitAidlService.sendCallRequest(session);
                        videoMode = iVisitAidlService.getVideoMode();
                        Log.i(TAG, "发送视频通话请求: ------------------------------" + TAG);
                    }
                    catch (Exception e) {
                        e.printStackTrace();
                        Log.i(TAG, "发送视频通话请求RemoteException: ------------------------------" + TAG);
                    }
                    callOff.session = session;
                    callOff.deviceIP = ClientSettingHelper.getStationIp();

                    mTencentPublisher.start(Global.createUrl(session, Global.DEVICE_TYPE_OUT_ROOM, ClientSettingHelper.getDeviceNumber()),localSurface,videoMode);
                    theurl = Global.createUrl(session, Global.DEVICE_TYPE_OUT_ROOM, ClientSettingHelper.getDeviceNumber());
                    Log.i(TAG, "onCreate: -------------------------------------url:" + theurl);
                    break;
                case ONCREATE_WHAT_TO_GETVISIT:
                    int videoMode1 = 0;
                    try {
                        videoMode1 = iVisitAidlService.getVideoMode();
                    }
                    catch (RemoteException e) {
                        e.printStackTrace();
                    }
                    mTencentPublisher.start(Global.createUrl(mSessionGetVisit, Global.DEVICE_TYPE_OUT_ROOM, ClientSettingHelper.getDeviceNumber()),localSurface,videoMode1);

                    Log.i(TAG, "室外端推流地址: " + Global.createUrl(mSessionGetVisit, Global.DEVICE_TYPE_OUT_ROOM, ClientSettingHelper.getDeviceNumber()));
                    mTencentPlayer.start(Global.createUrl(mSessionGetVisit, Global.DEVICE_TYPE_STATION, Global.DEVICE_NUMBER_STATION), remoteSurface);
                    Log.i(TAG, "室外端播放地址: " + Global.createUrl(mSessionGetVisit, Global.DEVICE_TYPE_STATION, Global.DEVICE_NUMBER_STATION));
                    theurl = Global.createUrl(mSessionGetVisit, Global.DEVICE_TYPE_STATION, Global.DEVICE_NUMBER_STATION);

                    callOff.session = mSessionGetVisit;
                    callOff.deviceIP = ClientSettingHelper.getStationIp();
                    break;

                default:
                    break;
            }
        }
    };

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(receiver);
    }

    @Override
    public void onDestroy() {
        Log.i(TAG, "onDestroy: " + TAG);
        handler.removeMessages(MSG_WHAT_ACO);
        handler = null;
        unbindService(mServiceConnection);
        super.onDestroy();
        android.os.Process.killProcess(android.os.Process.myPid());
    }


}
