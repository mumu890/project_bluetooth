package com.example.sofia.myapplication;

import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.widget.Toast;

import com.jyl.base.util.DisplayUtil;
import com.jyl.base.util.ToastUtil;

import org.xutils.common.util.LogUtil;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class ActivityVertical extends AppCompatActivity implements View.OnTouchListener,View.OnLongClickListener{
    ImageView img;
    ScrollView scrollView;
    SeekBar seekBar;
    VerticalSeekBar verticalSeekBar;
    ImageView k6,k7;
    TextView tv;
    boolean touchSeekBar = false;
    boolean touchSeekBarV = false;
    boolean touchScroll = false;
    int progerss = 0;
    int initHight = 0;
    String kType = "";
    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(msg.what == 0) {
                ToastUtil.showShort(msg.obj.toString());
            }
        }
    };

    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
//            if(progerss ==  0) {
//                showToast("Scrol窗帘已经关闭");
//            } else if(progerss ==  100){
//                showToast("窗帘已经全开");
//            } else {
//                showToast("窗帘已经开启"+progerss+"%");
//            }

            tv.setText(progerss+"%");
            sendOrder(kType);
        }
    };
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vertical);
        img = (ImageView) findViewById(R.id.img);
        tv = (TextView) findViewById(R.id.tv);

        initHight = DisplayUtil.dip2px(this,240);
        LogUtil.d("initHight:"+initHight);

        scrollView = (ScrollView) findViewById(R.id.scrollView);
        scrollView.setOnTouchListener(this);
        scrollView.setOnScrollChangeListener(new View.OnScrollChangeListener() {
            @Override
            public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                LogUtil.d("=======scrollViews scrollY=" + scrollY); // 0 ~ 720
                setImg(initHight - scrollY);
                if(!touchSeekBar) {
                    progerss = (scrollY)*100/initHight;
                    if(progerss >= 100) progerss = 100;
                        verticalSeekBar.setProgress(progerss);
                    if(touchScroll) {
                        kType = "k4";
                        sendTo();
//                        sendOrder("k4");
                    }
                }
            }
        });

        seekBar = (SeekBar) findViewById(R.id.seek_bar);
        seekBar.setOnTouchListener(this);
        seekBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener(){

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                LogUtil.d("SeekBar onProgressChanged");
                if(touchSeekBar) {
                    kType = "k8";
                    sendTo();
//                    sendOrder("k8");
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                LogUtil.d("SeekBar onStartTrackingTouch");
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                LogUtil.d("SeekBar onStopTrackingTouch");
            }
        });

        verticalSeekBar = (VerticalSeekBar) findViewById(R.id.seek_bar_v);
        verticalSeekBar.setOnTouchListener(this);
        verticalSeekBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener(){

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                LogUtil.d("VerticalSeekBar onProgressChanged");
                if(touchSeekBarV) {
                    scrollView.scrollTo( 0,(initHight * (progress) / 100));
                    kType = "k3";
                    sendTo();
//                    sendOrder("k3");
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                LogUtil.d("VerticalSeekBar onStartTrackingTouch");
                touchSeekBarV = true;
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                LogUtil.d("VerticalSeekBar onStopTrackingTouch");
                touchSeekBarV = false;
                progerss = 100 - seekBar.getProgress();
                sendTo();
            }
        });
        k6 = (ImageView) findViewById(R.id.img_k6);
        k7 = (ImageView) findViewById(R.id.img_k7);
        k6.setLongClickable(true);
        k7.setLongClickable(true);
        k6.setOnLongClickListener(this);
        k7.setOnLongClickListener(this);

    }

    private void setImg(int w){
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams)img.getLayoutParams();
        params.height = w;
        img.setLayoutParams(params);
    }

    private void showToast(String msg){
        Toast.makeText(this,msg,Toast.LENGTH_SHORT).show();
    }


    private void sendTo(){
        mHandler.removeCallbacks(runnable);
        mHandler.postDelayed(runnable,500);
    }

    public void jian(View view){
        stop(null);
        touchScroll = false;
        touchSeekBar = false;
        touchSeekBarV = false;
        scrollView.scrollTo(0,scrollView.getScrollY()+30);
        sendOrder("k1");
    }

    public void add(View view){
        stop(null);
        touchScroll = false;
        touchSeekBar = false;
        touchSeekBarV = false;
        scrollView.scrollTo(0,scrollView.getScrollY()-30);
        sendOrder("k2");
    }

    Runnable runnableClose = new Runnable() {
        @Override
        public void run() {
            if(autoClose) {
                scrollView.scrollTo(0,scrollView.getScrollY()-10);
            } else {
                return;
            }
            if(scrollView.getScrollY() > 0) {
                mHandler.postDelayed(runnableClose, 50);
            } else {
                autoClose = false;
            }
        }
    };
    private boolean autoClose = false;
    public void autoClose(View view){
        if(autoClose)
            return;
        touchScroll = false;
        touchSeekBar = false;
        touchSeekBarV = false;
        autoOpen = false;
        autoClose = true;
        scrollView.scrollTo(0,scrollView.getScrollY()-10);
        mHandler.postDelayed(runnableClose,50);
        sendOrder("k11");
    }

    public void stop(View view){
        if(autoClose || autoOpen) {
            sendOrder("k10");
        }
        autoClose = false;
        autoOpen = false;
    }

    Runnable runnableOpen = new Runnable() {
        @Override
        public void run() {
            if(autoOpen) {
                scrollView.scrollTo(0,scrollView.getScrollY()+10);
            } else {
                return;
            }
            if(scrollView.getScrollY() <= initHight) {
                mHandler.postDelayed(runnableOpen, 50);
            } else {
                autoOpen = false;
            }
        }
    };
    private boolean autoOpen = false;
    public void autoOpen(View view){
        if(autoOpen)
            return;
        touchScroll = false;
        touchSeekBar = false;
        touchSeekBarV = false;
        autoClose = false;
        autoOpen = true;
        scrollView.scrollTo(0,scrollView.getScrollY()+10);
        mHandler.postDelayed(runnableOpen,50);
        sendOrder("k9");
    }

    public void love(View view){
        sendOrder("k5");
    }

    public void k6(View view){  // 减少速度
        touchScroll = false;
        touchSeekBar = false;
        touchSeekBarV = false;
        int p = seekBar.getProgress();
        if(p == 0) {
            return;
        } else {
            p = p -5;
            if(p <= 0)
                p = 0;
            seekBar.setProgress(p);
            sendOrder("k6");
        }
    }

    public void k7(View view){  // 加大速度
        touchScroll = false;
        touchSeekBar = false;
        touchSeekBarV = false;
        int p = seekBar.getProgress();
        if(p == 100) {
            return;
        } else {
            p = p + 5;
            if(p >= 100)
                p = 100;
            seekBar.setProgress(p);
            sendOrder("k7");
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        stop(null);
        if(v.getId() == R.id.scrollView) {
            LogUtil.d("onTouch scrollView");
            touchScroll = true;
            touchSeekBar = false;
            touchSeekBarV = false;
        } else if (v.getId() == R.id.seek_bar) {
            LogUtil.d("onTouch seek_bar");
            touchSeekBar = true;
            touchSeekBarV = false;
            touchScroll = false;
        } else if(v.getId() == R.id.seek_bar_v) {
            LogUtil.d("onTouch seek_bar_v");
            touchSeekBarV = true;
            touchSeekBar = false;
            touchScroll = false;
        }
        return false;
    }

    private void sendOrder(String key){
        switch (key) {
            case "k1":
                LogUtil.d("k1");
                break;
            case "k2":
                LogUtil.d("k2");
                break;
            case "k3":
                LogUtil.d("k3");
                break;
            case "k4":
                LogUtil.d("k4");
                break;
            case "k5":
                LogUtil.d("k5");
                break;
            case "k6":
                LogUtil.d("k6");
                break;
            case "k66":
                LogUtil.d("k66");
                break;
            case "k7":
                LogUtil.d("k7");
                break;
            case "k77":
                LogUtil.d("k77");
                break;
            case "k8":
                LogUtil.d("k8");
                break;
            case "k9":
                LogUtil.d("k9");
                break;
            case "k10":
                LogUtil.d("k10");
                break;
            case "k11":
                LogUtil.d("k11");
                break;
        }
        CollectManager.getInstance().send(key);
    }

    @Override
    public boolean onLongClick(View v) {
        if (v.getId() == R.id.img_k6) {
            sendOrder("k66");
        } else if(v.getId() == R.id.img_k7){
            sendOrder("k77");
        }
        return true;
    }
}
