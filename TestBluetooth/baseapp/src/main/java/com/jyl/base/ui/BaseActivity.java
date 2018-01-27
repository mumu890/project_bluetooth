package com.jyl.base.ui;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;

import com.jyl.base.baseapp.R;
import com.jyl.base.dailog.LoadingDialog;

import org.xutils.common.util.LogUtil;
import org.xutils.x;

/**
 * desc:Actitiv基础继承类
 * last modified time:2017/5/19 14:42
 *
 * @author yulin.jing
 * @since 2017/5/19
 */
public abstract class BaseActivity extends AppCompatActivity {
    public Context mContext;

    public ToolbarView mToolbarView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LogUtil.d("onCreate");
        x.view().inject(this);
        mContext = this;
        mToolbarView = (ToolbarView)findViewById(R.id.toolbarView);
        if (mProgressDialog == null) {
            mProgressDialog = new LoadingDialog(this);
        }
    }

    public void initToolBar(){
        if(null != mToolbarView){
            LogUtil.d("initToolBar not null");
            mToolbarView.setLeftIconClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onBackPressed();
                }
            });
        } else {
            LogUtil.d("initToolBar is null");
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    protected LoadingDialog mProgressDialog;
    protected void dismissProgress() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }
    protected void showProgress(@StringRes int resId) {
        LogUtil.d("showProgress resId:"+resId);
        showProgress(mContext.getResources().getString(resId));
    }

    protected void showProgress() {
        LogUtil.d("showProgress");
        showProgress(R.string.progress_tip_default);
    }

    protected void showProgress(String msg) {
        if (TextUtils.isEmpty(msg)) {
            showProgress();
        } else {
            if (mProgressDialog != null && !mProgressDialog.isShowing()) {
                mProgressDialog.show();
                mProgressDialog.setMessage(msg);
                LogUtil.d("showProgress msg:"+msg);
            }
        }
    }
}
