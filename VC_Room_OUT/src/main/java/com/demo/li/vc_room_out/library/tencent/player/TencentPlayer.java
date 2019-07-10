package com.demo.li.vc_room_out.library.tencent.player;

import android.content.Context;
import android.view.View;

import com.tencent.rtmp.TXLiveConstants;
import com.tencent.rtmp.TXLivePlayConfig;
import com.tencent.rtmp.TXLivePlayer;
import com.tencent.rtmp.ui.TXCloudVideoView;

/**
 *
 * on 2018/8/10 16:22.
 */
public class TencentPlayer {
    Context mContext;
    TXLivePlayer mLivePlayer;
    String mUrl;
    TXLivePlayConfig mPlayConfig;

    public TXLivePlayer getInstance(Context activity) {
        mContext = activity;
        mLivePlayer = new TXLivePlayer(activity);

        mLivePlayer.setRenderRotation(TXLiveConstants.RENDER_ROTATION_PORTRAIT);//设置横屏
        mLivePlayer.setRenderMode(TXLiveConstants.RENDER_MODE_FULL_FILL_SCREEN);//填充模式 适应
        mLivePlayer.enableHardwareDecode(true);//启用视频硬解码
        mPlayConfig = new TXLivePlayConfig();
        mPlayConfig.enableAEC(true);//消除回声


        mPlayConfig.setAutoAdjustCacheTime(true);
        mPlayConfig.setMaxAutoAdjustCacheTime(1.0f);
        mPlayConfig.setMinAutoAdjustCacheTime(1.0f);
        mLivePlayer.setConfig(mPlayConfig);
        return mLivePlayer;
    }

    public void start(String url, TXCloudVideoView mView) {
        mLivePlayer.setPlayerView(mView);
        mLivePlayer.startPlay(url, TXLivePlayer.PLAY_TYPE_LIVE_RTMP);
        mView.setVisibility(View.VISIBLE);
        mUrl = url;

    }

    public void start(TXCloudVideoView mView) {
        mLivePlayer.setPlayerView(mView);
        mLivePlayer.startPlay(mUrl, TXLivePlayer.PLAY_TYPE_LIVE_RTMP);
        mView.setVisibility(View.VISIBLE);
    }


    /**
     * 转化url
     *
     * @param url
     * @param mView
     */
    public void switchUrl(String url, TXCloudVideoView mView) {
        if (!mLivePlayer.isPlaying()) {
            mUrl = url;
            mLivePlayer.stopPlay(true); // true 代表清除最后一帧画面
            start(mView);
        }else{
//            mLivePlayer.stopPlay(true); // true 代表清除最后一帧画面 这个加上去会出现 有时候播放不了的问题 不能加 毕竟他也没有开始播放 你就调用停止播放
            start(url,mView);
            mUrl = url;
        }
    }

    public void stop(TXCloudVideoView mView){
        mLivePlayer.stopPlay(true);// true 代表清除最后一帧画面
//        mLivePlayer.setPlayerView(null);
        mView.onDestroy();
    }

}
