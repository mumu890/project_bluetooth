package com.jyl.base.dailog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.view.Display;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.jyl.base.baseapp.R;


public class LoadingDialog extends Dialog {

    ImageView mIvLoading;
    TextView mTvMsg;

    private AnimationDrawable animationDrawable;

    public LoadingDialog(Context context) {
        super(context, R.style.dialogFullScreenNoBackground);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_loading);

        Window window = getWindow(); // 得到对话框
//        window.setWindowAnimations(R.style.dialog_anim);
        //window.setBackgroundDrawableResource(R.color.transparent); //设置对话框背景为透明
        WindowManager.LayoutParams wl = window.getAttributes();
        wl.gravity = Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL;
        WindowManager m = window.getWindowManager();
        Display d = m.getDefaultDisplay(); // 为获取屏幕宽、高

        //wl.type = WindowManager.LayoutParams.TYPE_APPLICATION;
        wl.width = d.getWidth();
        window.setAttributes(wl);

        setCanceledOnTouchOutside(true);

        initView(window);
    }

    @Override
    public void show() {
        super.show();
        animationDrawable.start();
    }

    @Override
    public void dismiss() {
        animationDrawable.stop();
        super.dismiss();
    }

    private void initView(Window w) {
        mIvLoading = w.findViewById(R.id.iv_loading);
        mTvMsg = w.findViewById(R.id.tv_msg);

        animationDrawable = (AnimationDrawable) mIvLoading.getDrawable();
        setCancelable(false);
        setCanceledOnTouchOutside(true);
    }

    public void setMessage(String msg){
        mTvMsg.setText(msg);
    }
}
