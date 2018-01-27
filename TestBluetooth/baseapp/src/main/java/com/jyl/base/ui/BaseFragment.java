package com.jyl.base.ui;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;

import com.jyl.base.baseapp.R;
import com.jyl.base.dailog.LoadingDialog;

import org.xutils.common.util.LogUtil;
import org.xutils.x;

/**
 * desc:Fragment基础继承类
 * last modified time:2017/5/19 14:42
 *
 * @author yulin.jing
 * @since 2017/5/19
 */

    public abstract class BaseFragment extends Fragment {
    private String TAG = BaseFragment.class.getSimpleName();

    public Context mContext;
    public View mRootView;
    private boolean injected = false;
    private int mToolbarHieght;

//    @ViewInject(R.id.toolbarview)
    public ToolbarView mToolbarView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LogUtil.d("onCreate");
        mContext = getActivity();
        if (mProgressDialog == null) {
            mProgressDialog = new LoadingDialog(mContext);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        LogUtil.d("onCreateView");
        if(null == mRootView) {
            mRootView = x.view().inject(this, inflater, container);
            mToolbarView = (ToolbarView) mRootView.findViewById(R.id.toolbarView);
        }
        return mRootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        LogUtil.d("onViewCreated");
        if(!injected) {
            injected = true;
            initToolBar();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
//        if(!NetworkUtil.isNetworkConnected(mContext)) {
//            LogUtil.d("onResume netWork not connect");
//            SnackbarUtil.showLong(mRootView, R.string.net_work_eeror);
//        } else {
//            LogUtil.d("onResume netWork connect");
//            SnackbarUtil.dismiss();
//        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        LogUtil.d("onActivityCreated");
    }

    private void initToolBar(){
        if(null != mToolbarView){
            LogUtil.d("initToolBar not null");
            mToolbarView.setLeftIconClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getActivity().onBackPressed();
                }
            });
            mToolbarView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    mToolbarHieght = mToolbarView.getHeight();
                    LogUtil.d("initToolBar mToolbarHieght:"+mToolbarHieght);
                }
            });
        } else {
            LogUtil.d("initToolBar is null");
        }
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
