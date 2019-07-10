package com.demo.li.vc_room_out.library.tencent.publisher;

import android.content.Context;
import android.util.Log;
import android.view.View;

import com.tencent.rtmp.TXLiveConstants;
import com.tencent.rtmp.TXLivePushConfig;
import com.tencent.rtmp.TXLivePusher;
import com.tencent.rtmp.ui.TXCloudVideoView;

import static com.tencent.rtmp.TXLiveConstants.VIDEO_RESOLUTION_TYPE_320_180;
import static com.tencent.rtmp.TXLiveConstants.VIDEO_RESOLUTION_TYPE_320_240;
import static com.tencent.rtmp.TXLiveConstants.VIDEO_RESOLUTION_TYPE_640_480;

/**
 *
 * on 2018/8/10 16:23.
 */
public class TencentPublisher {
    private static final String TAG = "TencentPublisher";
    Context mContext;
    TXLivePusher mLivePusher;
    TXLivePushConfig mLivePushConfig; //配置推流工作


    String mUrl;

    public TXLivePusher getInstance(Context activity,boolean isRoomIn) {
        mContext = activity;
        mLivePusher = new TXLivePusher(activity);
        /**
         * SDK 不绑定腾讯云，如果要推流到非腾讯云地址，请在推流前设置 TXLivePushConfig 中的 enableNearestIP 设置为 false。但如果您要推流的地址为腾讯云地址，
         * 请务必在推流前将其设置为 true，否则推流质量可能会因为运营商 DNS 不准确而受到影响。
         */
        mLivePushConfig = new TXLivePushConfig();
        mLivePushConfig.enableNearestIP(false);
        mLivePushConfig.setFrontCamera(true);
        mLivePushConfig.enableAEC(true);
        if (isRoomIn){
            mLivePushConfig.setHomeOrientation(TXLiveConstants.VIDEO_ANGLE_HOME_RIGHT);//配置竖屏  横屏：VIDEO_ANGLE_HOME_DOWN

        }else{
            mLivePushConfig.setHomeOrientation(TXLiveConstants.VIDEO_ANGLE_HOME_DOWN);//配置横屏  竖屏：VIDEO_ANGLE_HOME_RIGHT
           }

        mLivePushConfig.enableHighResolutionCaptureMode(false);//开启高清 true:开启高清模式，采用1280*720分辨率 false:关闭高清模式，使用编码分辨率
        mLivePushConfig.setTouchFocus(false);//是否手动对焦
        mLivePushConfig.setVideoResolution(VIDEO_RESOLUTION_TYPE_320_240);//VIDEO_RESOLUTION_TYPE_640_480
        mLivePushConfig.setVideoFPS(16);//帧率 默认20
        mLivePushConfig.setVideoEncodeGop(2);

        mLivePushConfig.setHardwareAcceleration(TXLiveConstants.ENCODE_VIDEO_HARDWARE);//开启硬件编码 //ENCODE_VIDEO_AUTO
//        mLivePusher.setVideoQuality(TXLiveConstants.VIDEO_QUALITY_LINKMIC_MAIN_PUBLISHER, true, true);//设置视频质量
        mLivePusher.setConfig(mLivePushConfig);
        return mLivePusher;
    }


    public void start(String url, TXCloudVideoView txCloudVideoView,int videomode) {
        mLivePushConfig.setVideoResolution(videomode == 0 ? VIDEO_RESOLUTION_TYPE_320_240:VIDEO_RESOLUTION_TYPE_640_480);
        mLivePushConfig.setVideoFPS(videomode == 0 ? 10:16);//帧率
        Log.i(TAG, "推流的帧数跟模式: "+ videomode );
        mLivePusher.startPusher(url);
        mLivePusher.startCameraPreview(txCloudVideoView);
        txCloudVideoView.setVisibility(View.VISIBLE);//这个是大坑 有时候切换页面但是没有预览画面出来就是因为它没有显示出来！！！！
        mUrl = url;
    }

    public void start(TXCloudVideoView txCloudVideoView) {
        int i = mLivePusher.startPusher(mUrl);
        mLivePusher.startCameraPreview(txCloudVideoView);
        txCloudVideoView.setVisibility(View.VISIBLE);//这个是大坑 有时候切换页面但是没有预览画面出来就是因为它没有显示出来！！！！
        Log.i(TAG, "start: " + i);
    }

    public void stop() {
        mLivePusher.stopCameraPreview(true); //停止摄像头预览
        mLivePusher.stopPusher();            //停止推流
        mLivePusher.setPushListener(null);   //解绑 listener
    }



}
