package com.androidex.indoorlock.ui.activity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Message;
import android.view.SurfaceView;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.androidex.indoorlock.R;
import com.androidex.indoorlock.base.BaseActivity;
import com.androidex.indoorlock.bean.Event;
import com.androidex.indoorlock.net.UrlTool;
import com.androidex.indoorlock.utils.RTCTools;

import org.greenrobot.eventbus.EventBus;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import rtc.sdk.common.RtcConst;

/**
 * Created by Administrator on 2018/3/1.
 */

public class TelephoneActivity extends BaseActivity {
    public static boolean isCall = false;
    private MediaPlayer mediaPlayer;
    private int callMode = -1;
    private SurfaceView remoteView = null;

    //来电Ui
    private RelativeLayout callLayout;
    private ImageView callHeadImage;
    private TextView callName;
    private LinearLayout callRejectLayout,callOpendoorLayout,callVoiceLayout,callVideoLayout;


    //通话UI
    private RelativeLayout offhookLayout;
    private ImageView offhookImage;
    private FrameLayout videoLayout;
    private LinearLayout offhootRejectLayout,offhootOpendoorLayout;


    @Override
    public void initParms(Bundle parms) {
        isCall = true;
        initRingPlayer();
    }

    @Override
    public int bindView() {
        return R.layout.activity_telphone;
    }

    @Override
    public void initView(View v) {
        //来电
        callLayout = findViewById(R.id.call_layout);
        callLayout.setVisibility(View.VISIBLE);
        callHeadImage = findViewById(R.id.call_portrait_image);
        callName = findViewById(R.id.call_name_text);
        callName.setText(RTCTools.callModel.communityName+RTCTools.callModel.lockName);
        callRejectLayout = findViewById(R.id.call_reject_layout);
        callRejectLayout.setOnClickListener(this);
        callOpendoorLayout = findViewById(R.id.call_opendoor_layout);
        callOpendoorLayout.setOnClickListener(this);
        callVoiceLayout = findViewById(R.id.call_voice_layout);
        callVoiceLayout.setOnClickListener(this);
        callVideoLayout = findViewById(R.id.call_video_layout);
        callVideoLayout.setOnClickListener(this);

        //通话
        offhookLayout = findViewById(R.id.offhook_layout);
        offhookImage = findViewById(R.id.offhook_portrait_image);
        videoLayout = findViewById(R.id.offhook_video_layout);
        offhootRejectLayout = findViewById(R.id.offhoot_reject_layout);
        offhootRejectLayout.setOnClickListener(this);
        offhootOpendoorLayout = findViewById(R.id.offhoot_opendoor_layout);
        offhootOpendoorLayout.setOnClickListener(this);
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.call_reject_layout:
                //来电挂断
                this.finish();
                break;
            case R.id.call_opendoor_layout:
                //直接开门
                postEvent(EVENT_WHAT_OPEN_DOOR);
                this.finish();
                break;
            case R.id.call_voice_layout:
                openVoice();
                break;
            case R.id.call_video_layout:
                openVideo();
                break;
            case R.id.offhoot_reject_layout:
                postEvent(EVENT_WHAT_OFFHOOK_REJECT);
                this.finish();
                break;
            case R.id.offhoot_opendoor_layout:
                postEvent(EVENT_WHAT_OFFHOOK_OPEN_DOOR);
                this.finish();
                break;
        }
    }

    @Override
    public void onMessage(Message msg) {

    }

    @Override
    public void onEvent(Event event) {
        switch (event.what){
            case EVENT_WHAT_ON_DISCONNECTED:
                onDisconnected((Integer) event.msg);
                this.finish();
                break;
            case EVENT_WHAT_ON_VIDEO:
                initVideoView();
                if(callMode == 0){
                    offhookImage.setVisibility(View.VISIBLE);
                    videoLayout.setVisibility(View.GONE);
                }else if(callMode == 1){
                    offhookImage.setVisibility(View.GONE);
                    videoLayout.setVisibility(View.VISIBLE);
                }
                RTCTools.callConnection.buildVideo(remoteView);
                break;
            case EVENT_WHAT_APPEND_IMAGE:
                String imageUrl = (String) event.msg;
                if(imageUrl!=null && imageUrl.length()>0){
                    setPortraitImage(imageUrl);
                }
                break;
            case EVENT_WHAT_PORTRAIT_BITMAP:
                callHeadImage.setImageBitmap((Bitmap)event.msg);
                offhookImage.setImageBitmap((Bitmap)event.msg);
                break;
            case EVENT_WHAT_CANCEL_CALL:
                this.finish();
                break;
        }
    }

    @Override
    public void mainThread() {
        startRing(); // 播放来电铃声
    }

    @Override
    protected void onDestroy() {
        isCall = false;
        if(mediaPlayer!=null){
            stopRing();
        }
        super.onDestroy();
    }
    private void setPortraitImage(final String url) {
        new Thread() {
            public void run() {
                Bitmap bitmap = returnBitmap(url);
                if (bitmap != null) {
                    postEvent(EVENT_WHAT_PORTRAIT_BITMAP,bitmap);
                }
            }
        }.start();
    }

    private Bitmap returnBitmap(String url) {
        URL fileUrl = null;
        Bitmap bitmap = null;
        try {
            fileUrl = new URL(convertImageUrl(url));
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        try {
            HttpURLConnection conn = (HttpURLConnection) fileUrl
                    .openConnection();
            conn.setDoInput(true);
            conn.connect();
            InputStream is = conn.getInputStream();
            bitmap = BitmapFactory.decodeStream(is);
            is.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bitmap;
    }

    private String convertImageUrl(String url) {
        if (url != null && url.length() > 4 && (!url.substring(0, 4).toLowerCase().equals("http"))) {
            url = UrlTool.BASE_HEAD+url;
        }
        return url;
    }


    private void initVideoView(){
        if(remoteView !=null){
            return;
        }
        if (RTCTools.callConnection != null) {
            remoteView = (SurfaceView) RTCTools.callConnection.createVideoView(false, this, true);
        }
        remoteView.setVisibility(View.VISIBLE);
        videoLayout.addView(remoteView);
        remoteView.setKeepScreenOn(true);
        remoteView.setZOrderMediaOverlay(true);
        remoteView.setZOrderOnTop(true);
    }

    private void onDisconnected(int code){
        String msg = "";
        switch (code){
            case RtcConst.CallCode_Timeout:
                showL("连接超时");
                msg = "连接超时";
                break;
            case RtcConst.CallCode_Fail:
                showL("对方不在线");
                msg = "对方不在线";
                break;
            case RtcConst.CallCode_Forbidden:
                showL("暂时无法接通");
                msg = "暂时无法接通";
                break;
            case RtcConst. CallCode_NotFound:
                showL("呼叫的号码不存在");
                msg = "呼叫的号码不存在";
                break;
            case RtcConst.CallCode_Network:
                showL("服务器错误或网络不可用");
                msg = "服务器错误或网络不可用";
                break;
            case RtcConst. CallCode_Reject:
                showL("对方正忙");
                msg = "对方正忙";
                break;
            case RtcConst. CallCode_Busy:
                showL("对方正忙");
                msg = "对方正忙";
                break;
        }
        showToast(false,msg);
    }

    private void openVoice(){
        stopRing();
        callMode = 0;
        postEvent(EVENT_WHAT_ANSWER,"voice");
        callLayout.setVisibility(View.GONE);
        offhookLayout.setVisibility(View.VISIBLE);
        offhookImage.setVisibility(View.VISIBLE);
        videoLayout.setVisibility(View.GONE);
    }
    private void openVideo(){
        stopRing();
        callMode = 1;
        postEvent(EVENT_WHAT_ANSWER,"video");
        callLayout.setVisibility(View.GONE);
        offhookLayout.setVisibility(View.VISIBLE);
        offhookImage.setVisibility(View.VISIBLE);
    }

    protected void initRingPlayer() {
        try {
            mediaPlayer = MediaPlayer.create(this, R.raw.ring);
            //ringingPlayer.prepare();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected void startRing() {
        mediaPlayer.setLooping(true);
        mediaPlayer.start();
    }
    protected void stopRing() {
        if(mediaPlayer!=null){
            mediaPlayer.pause();
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }
}
