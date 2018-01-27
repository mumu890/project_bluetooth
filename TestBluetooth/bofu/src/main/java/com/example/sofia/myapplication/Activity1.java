package com.example.sofia.myapplication;

import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.SeekBar;
import android.widget.Toast;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class Activity1 extends AppCompatActivity {
    ScrollView scrollView;
    SeekBar seekBar;
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
            if(progerss ==  0) {
                showToast("窗帘已经关闭");
            } else if(progerss ==  100){
                showToast("窗帘已经全开");
            } else {
                showToast("窗帘已经开启"+progerss+"%");
            }
        }
    };
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_1);
        scrollView = (ScrollView) findViewById(R.id.scrollView);
        scrollView.setOnScrollChangeListener(new View.OnScrollChangeListener() {
            @Override
            public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                Log.d("","=======scrollViewscrollY="+scrollY);
                if(!trackingTouchSeekBar) {
                    progerss =  (scrollY)*100/900;
                    seekBar.setProgress(progerss);
                    sendTo();
                }
            }
        });

        seekBar = (SeekBar) findViewById(R.id.seek_bar);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener(){

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                Log.d("","onProgressChanged progress="+progress);
                Log.d("","onProgressChanged progress2="+(900 - (900*progress/100)));
                if(trackingTouchSeekBar)
                    scrollView.scrollTo(0,(900*progress/100));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                trackingTouchSeekBar = true;
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                trackingTouchSeekBar = false;
                progerss =seekBar.getProgress();
                sendTo();
            }
        });
    }

    private void sendTo(){
        mHandler.removeCallbacks(runnable);
        mHandler.postDelayed(runnable,500);
    }

    public void open(View view){
        scrollView.scrollTo(0,900);
        seekBar.setProgress(100);
    }

    public void close(View view){
        scrollView.scrollTo(0,0);
        seekBar.setProgress(0);
    }

    private void showToast(String msg){
        Toast.makeText(this,msg,Toast.LENGTH_SHORT).show();
    }
}
