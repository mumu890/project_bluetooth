package com.example.sofia.myapplication;

import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.widget.Toast;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class ActivityHorizontal extends AppCompatActivity implements View.OnTouchListener{
    ImageView img;
    HorizontalScrollView scrollView;
    SeekBar seekBar;
    TextView tv;
    boolean trackingTouchSeekBar = false;
    int progerss = 0;
    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
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
        }
    };
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_horizontal);
        img = (ImageView) findViewById(R.id.img);
        tv = (TextView) findViewById(R.id.tv);
        scrollView = (HorizontalScrollView) findViewById(R.id.scrollView);
        scrollView.setOnTouchListener(this);
        scrollView.setOnScrollChangeListener(new View.OnScrollChangeListener() {
            @Override
            public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                Log.d("","=======scrollViewscrollX="+scrollX);
                setImg(750 - scrollX);
                if(!trackingTouchSeekBar) {
                    progerss =  100 - (scrollX-60)*100/630;
                    if(progerss >= 100) progerss = 100;
                    seekBar.setProgress(progerss);
                    sendTo();
                }
            }
        });

        seekBar = (SeekBar) findViewById(R.id.seek_bar);
        seekBar.setOnTouchListener(this);
        seekBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener(){

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if(trackingTouchSeekBar)
                    scrollView.scrollTo((630*(100-progress)/100),0);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                trackingTouchSeekBar = true;
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                trackingTouchSeekBar = false;
                progerss = 100 - seekBar.getProgress();
                sendTo();
            }
        });
    }

    private void sendTo(){
        mHandler.removeCallbacks(runnable);
        mHandler.postDelayed(runnable,500);
    }

    public void jian(View view){
        stop(null);
        if(scrollView.getScrollX() < 60) {
            scrollView.scrollTo(90,0);
        } else {
            scrollView.scrollTo(scrollView.getScrollX()+30,0);
        }
    }

    public void add(View view){
        stop(null);
        scrollView.scrollTo(scrollView.getScrollX()-30,0);
    }

    Runnable runnableClose = new Runnable() {
        @Override
        public void run() {
            if(close) {
                scrollView.scrollTo(scrollView.getScrollX()-10,0);
            } else {
                return;
            }
            if(scrollView.getScrollX() > 0) {
                mHandler.postDelayed(runnableClose, 50);
            } else {
                close = false;
            }
        }
    };
    private boolean close = false;
    public void close(View view){
        if(close)
            return;
        open = false;
        close = true;
        scrollView.scrollTo(scrollView.getScrollX()-10,0);
        mHandler.postDelayed(runnableClose,50);
    }

    public void stop(View view){
        close = false;
        open = false;
    }

    Runnable runnableOpen = new Runnable() {
        @Override
        public void run() {
            if(open) {
                scrollView.scrollTo(scrollView.getScrollX()+10,0);
            } else {
                return;
            }
            if(scrollView.getScrollX() <= 690) {
                mHandler.postDelayed(runnableOpen, 50);
            } else {
                open = false;
            }
        }
    };
    private boolean open = false;
    public void open(View view){
        if(open)
            return;
        close = false;
        open = true;
        scrollView.scrollTo(scrollView.getScrollX()+10,0);
        mHandler.postDelayed(runnableOpen,50);
    }

    private void setImg(int w){
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams)img.getLayoutParams();
        params.width = w;
        img.setLayoutParams(params);
    }

    private void showToast(String msg){
        Toast.makeText(this,msg,Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        stop(null);
        return false;
    }
}
