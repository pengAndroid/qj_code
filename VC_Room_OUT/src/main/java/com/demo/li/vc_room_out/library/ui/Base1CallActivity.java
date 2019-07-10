package com.demo.li.vc_room_out.library.ui;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.demo.li.vc_room_out.R;
import com.demo.li.vc_room_out.library.tencent.player.TencentPlayer;
import com.demo.li.vc_room_out.library.tencent.publisher.TencentPublisher;

import com.tencent.rtmp.TXLivePlayer;
import com.tencent.rtmp.TXLivePusher;
import com.tencent.rtmp.ui.TXCloudVideoView;

/**
 * 室外机子最新版接入腾讯直播云SDK
 */
public class Base1CallActivity extends BaseActivity {

    public TencentPublisher mTencentPublisher;
    public TXLivePlayer mLivePlayer;//TXLivePlayer接流工作
    public TencentPlayer mTencentPlayer;
    public TextView tvToast;
    public TXCloudVideoView remoteSurface;
    public TXCloudVideoView localSurface;
    public TXLivePusher mLivePusher;//TXLivePush推流工作
    public Button btnCut;

//    LinearLayout mBigLayout;//放大布局跟放小布局
    RelativeLayout mSmaillLayout,mBigLayout;
    private boolean isViewShow = true;
    public boolean isNomal = true;//本地的镜头页面大小

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_call1);

        mBigLayout = findViewById(R.id.big_layout);
        mSmaillLayout = findViewById(R.id.smaill_layout);

        tvToast = (TextView) findViewById(R.id.call_toast_tv);
        btnCut = (Button) findViewById(R.id.call_cut_btn);

        remoteSurface = (TXCloudVideoView) findViewById(R.id.call_remote_surface);
        localSurface = (TXCloudVideoView) findViewById(R.id.call_local_surface);


        mTencentPublisher = new TencentPublisher();
        mLivePusher = mTencentPublisher.getInstance(this, false);

        mTencentPlayer = new TencentPlayer();
        mLivePlayer = mTencentPlayer.getInstance(this);
        handler.sendEmptyMessageDelayed(HANDLER_WHAT, 5000);

    }

    @Override
    public boolean onTouchEvent(MotionEvent e) {
        switch (e.getAction()) {
            case MotionEvent.ACTION_UP:
                handler.removeMessages(HANDLER_WHAT);
                setViewShow(!isViewShow);
                if (isViewShow) handler.sendEmptyMessageDelayed(HANDLER_WHAT, 5000);
                break;
        }
        return super.onTouchEvent(e);
    }

    private final int HANDLER_WHAT = 0;
    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            setViewShow(false);
        }
    };

    private void setViewShow(boolean isViewShow) {
        btnCut.setVisibility(isViewShow ? View.VISIBLE : View.INVISIBLE);
        this.isViewShow = isViewShow;
        btnCut.bringToFront();
    }

    @Override
    protected void onDestroy() {
        if (null != mTencentPublisher)
            mTencentPublisher.stop();
        if (null != mTencentPlayer)
            mTencentPlayer.stop(remoteSurface);
        super.onDestroy();
    }

}
