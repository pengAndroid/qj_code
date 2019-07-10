package com.demo.li.vc_room_out;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.demo.li.vc_room_out.library.Global;
import com.demo.li.vc_room_out.library.entity.CallRequest;
import com.demo.li.vc_room_out.library.entity.CallRequestResult;
import com.demo.li.vc_room_out.library.tools.RingtonePlayer;
import com.demo.li.vc_room_out.library.view.BaseDialog;
import com.demo.li.vc_room_out.sockethelper.VisitSender;



import java.util.Timer;
import java.util.TimerTask;

/**
 * 子进程下的对话框也有可能是主进程的对话框 要记得处理~~ AIDL跨进程通信
 */
public class InTeleDialog extends BaseDialog {
    private ImageView header;
    private TextView name;
    private Timer timer;
    private Button mOk,mCancel;

    private CallRequest callRequest;

    public InTeleDialog(Context context, CallRequest callRequest) {
        super(context, R.style.dialog_in_tele_style);

        setContentView(R.layout.dialog_in_tele);

        this.callRequest = callRequest;

//        header = (ImageView) findViewById(R.id.dialog_in_tele_header);
        name = (TextView) findViewById(R.id.dialog_in_tele_name);
        name.setText("室内机呼叫中……");
        mOk =  findViewById(R.id.dialog_in_tele_connect);
        mOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onConnect();
            }
        });
       mCancel = findViewById(R.id.dialog_in_tele_cut);
        mCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onCut();
            }
        });

//        header.setImageResource(R.drawable.visit_header);

        setCanceledOnTouchOutside(false);

    }

    @Override
    public void show() {
        super.show();
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                InTeleDialog.this.cancel();
                CallRequestResult callRequestResult = new CallRequestResult();
                callRequestResult.message = "室内无人接听，请稍候再拨";
                callRequestResult.status = 0;
                VisitSender.sendCallRequestResult(callRequestResult);
                cancel();
            }
        }, 60000);
    }

    @Override
    public void cancel() {
        Global.isCalling = false;
        RingtonePlayer.stopRingtone();
        if (timer != null) {
            timer.cancel();
        }
        super.cancel();
    }

    public void setHeader(Bitmap bitmap) {
        header.setImageBitmap(bitmap);
    }



    private void onConnect() {
        CallRequestResult callRequestResult = new CallRequestResult();
        callRequestResult.message = "";
        callRequestResult.status = 2;
        callRequestResult.session = callRequest.session;
        VisitSender.sendCallRequestResult(callRequestResult);
        cancel();
        Global.isVisitCallMultiProcessToShowActivity = true;
        Intent intent = new Intent(getContext(), VisitCallActivity.class);
        intent.putExtra("session", callRequest.session);
        intent.putExtra("isToCall", false);
        getContext().startActivity(intent);
    }

    private void onCut() {
        Global.isCalling = false;
        CallRequestResult callRequestResult = new CallRequestResult();
        callRequestResult.message = "室外机已拒绝您的通话请求";
        callRequestResult.status = 3;
        VisitSender.sendCallRequestResult(callRequestResult);
        cancel();
    }

}
