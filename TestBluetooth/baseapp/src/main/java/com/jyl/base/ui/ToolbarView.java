package com.jyl.base.ui;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jyl.base.baseapp.R;

import org.xutils.common.util.LogUtil;


/**
 * desc:
 * last modified time:2017/6/5 10:42
 *
 * @author yulin.jing
 * @since 2017/6/5
 */
public class ToolbarView extends FrameLayout {
    private Context mContext;
    RelativeLayout rlToolbarBg;
    ImageView ivLeft;
    ImageView ivRight0;
    ImageView ivRight1;
    TextView tvLeft;
    TextView tvCenter;
    TextView tvRight;
    TextView btnLeft;
    TextView btnRight;

    public ToolbarView(@NonNull Context context) {
        super(context);
        initView(context);
    }
    public ToolbarView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    private void initView(Context context) {
        mContext = context;
        View v = inflate(mContext, R.layout.view_toolbar,this);
        rlToolbarBg = v.findViewById(R.id.rl_toobar_bg);
        ivLeft= v.findViewById(R.id.iv_left);
        ivRight0= v.findViewById(R.id.iv_right0);
        ivRight1= v.findViewById(R.id.iv_right1);
        tvLeft= v.findViewById(R.id.tv_left);
        tvCenter= v.findViewById(R.id.tv_center);
        tvRight= v.findViewById(R.id.tv_right);
        btnLeft= v.findViewById(R.id.btn_left);
        btnRight= v.findViewById(R.id.btn_right);
    }

    /**
     * 设置toolbar左侧标题
     * @param titleResource
     */
    public ToolbarView setLeftBtnTitle(int titleResource){
        btnLeft.setVisibility(View.VISIBLE);
        btnLeft.setText(titleResource);
        return this;
    }

    /**
     * 设置toolbar左侧标题
     * @param title
     */
    public ToolbarView setLeftBtnTitle(String title){
        if(TextUtils.isEmpty(title)) {
            btnLeft.setVisibility(GONE);
        } else {
            btnLeft.setText(title);
            btnLeft.setVisibility(View.VISIBLE);
        }
        return this;
    }
    /**
     * 设置toolbar右侧标题
     * @param title
     */
    public ToolbarView setRightBtnTitle(String title){
        if(TextUtils.isEmpty(title)) {
            btnRight.setVisibility(GONE);
        } else {
            btnRight.setText(title);
            btnRight.setVisibility(View.VISIBLE);
        }
        return this;
    }

    /**
     * 设置toolbar左侧标题监听
     * @param listener
     */
    public ToolbarView setLeftBtnClickListener(OnClickListener listener){
        btnLeft.setOnClickListener(listener);
        return this;
    }
    /**
     * 设置toolbar右侧侧标题监听
     * @param listener
     */
    public ToolbarView setRightBtnClickListener(OnClickListener listener){
        btnRight.setOnClickListener(listener);
        return this;
    }
    /**
     * 设置toolbar左侧标题字体大小
     * @param size
     */
    public ToolbarView setLeftBtnTitleSize(int size){
        btnLeft.setVisibility(View.VISIBLE);
        btnLeft.setTextSize(size);
        return this;
    }
    /**
     * 设置toolbaryou侧标题
     * @param titleResource
     */
    public ToolbarView setRightBtnTitle(int titleResource){
        LogUtil.d("setToolBarLeftTitle");
        btnRight.setVisibility(View.VISIBLE);
        btnRight.setText(titleResource);
        return this;
    }
    /**
     * 设置toolbar左侧标题字体大小
     * @param size
     */
    public ToolbarView setRightBtnTitleSize(int size){
        LogUtil.d("setToolBarLeftTitle");
        btnRight.setVisibility(View.VISIBLE);
        btnRight.setTextSize(size);
        return this;
    }

    /**
     * 设置toolbar左侧标题
     * @param titleResource
     */
    public ToolbarView setLeftTitle(int titleResource){
        LogUtil.d("setToolBarLeftTitle");
        tvLeft.setVisibility(View.VISIBLE);
        tvLeft.setText(titleResource);
        return this;
    }

    /**
     * 设置toolbar中间标题
     * @param titleResource
     */
    public ToolbarView setCenterTitle(int titleResource){
        LogUtil.d("setCenterTitle");
        tvCenter.setText(titleResource);
        tvCenter.setVisibility(View.VISIBLE);
        return this;
    }
    /**
     * 设置toolbar中间标题
     * @param title
     */
    public ToolbarView setCenterTitle(String title){
        tvCenter.setText(title);
        tvCenter.setVisibility(View.VISIBLE);
        return this;
    }

    /**
     * 设置toolbar右侧标题
     * @param titleResource
     */
    public ToolbarView setRightTitle(int titleResource){
        tvRight.setText(titleResource);
        tvRight.setVisibility(View.VISIBLE);
        return this;
    }
    /**
     * 设置toolbar右侧标题
     * @param title
     */
    public ToolbarView setRightTitle(String title){
        tvRight.setText(title);
        tvRight.setVisibility(View.VISIBLE);
        return this;
    }

    /**
     * 设置toolbar右侧标题点击监听
     * @param listener
     */
    public ToolbarView setRightTitleClickListener(OnClickListener listener){
        tvRight.setOnClickListener(listener);
        return this;
    }

    /**
     * 设置toolbar左侧标题颜色
     * @param color
     */
    public ToolbarView setLeftTitleColor(int color){
        tvLeft.setTextColor(color);
        return this;
    }

    /**
     * 设置toolbar中间标题颜色
     * @param color
     */
    public ToolbarView setCenterTitleColor(int color){
        tvCenter.setTextColor(color);
        return this;
    }

    /**
     * 设置toolbar右侧标题颜色
     * @param color
     */
    public ToolbarView setRightTitleColor(int color){
        tvRight.setTextColor(color);
        return this;
    }

    /**
     * 设置toolbar左侧标题大小
     * @param size
     */
    public ToolbarView setLeftTitleSize(float size){
        tvLeft.setTextSize(size);
        return this;
    }

    /**
     * 设置toolbar中间标题大小
     * @param size
     */
    public ToolbarView setCenterTitleSize(float size){
        tvCenter.setTextSize(size);
        return this;
    }

    /**
     * 设置toolbar右侧标题大小
     * @param size
     */
    public ToolbarView setRightTitleSize(float size){
        tvRight.setTextSize(size);
        return this;
    }

    /**
     * 设置左图标是否显示
     * @param show
     */
    public ToolbarView showLeftDefaultBackIcon(boolean show){
        ivLeft.setVisibility(show ? View.VISIBLE : View.INVISIBLE);
        return this;
    }

    /**
     * 设置左图标图片
     * @param drawable
     */
    public ToolbarView setLeftIconDrawble(int drawable){
        ivLeft.setImageResource(drawable);
        ivLeft.setVisibility(View.VISIBLE);
        return this;
    }

    /**
     * 设置左图标点击监听
     * @param listener
     */
    public ToolbarView setLeftIconClickListener(OnClickListener listener){
        LogUtil.d("setLeftIconClickListener");
        ivLeft.setOnClickListener(listener);
        return this;
    }

    /**
     * 设置右侧0位置标图片
     * @param drawable
     */
    public ToolbarView setRight0IconDrawble(int drawable){
        ivRight0.setImageResource(drawable);
        ivRight0.setVisibility(View.VISIBLE);
        return this;
    }

    /**
     * 设置右侧0图标击监听
     * @param listener
     */
    public ToolbarView setRight0IconClickListener(OnClickListener listener){
        ivRight0.setOnClickListener(listener);
        return this;
    }

    /**
     * 设置右侧1位置图标图片
     * @param drawable
     */
    public ToolbarView setRight1IconDrawble(int drawable){
        ivRight1.setImageResource(drawable);
        ivRight1.setVisibility(View.VISIBLE);
        return this;
    }

    /**
     * 设置右侧1图标点击监听
     * @param listener
     */
    public ToolbarView setRight1IconClickListener(OnClickListener listener){
        ivRight1.setOnClickListener(listener);
        return this;
    }

    /**
     * 设置toolbar背景色
     * @param color
     */
    public ToolbarView setToolBarBackColor(int color){
        this.setBackgroundColor(color);
        return this;
    }

    /**
     * 设置toolbar背景图片
     * @param drawable
     */
    public ToolbarView setToolBarBackDrawble(int drawable){
        this.setBackgroundResource(drawable);
        return this;
    }


    public static int SCROLL_MAX = 1;

    private ToolbarView applyScrollBgAlpha(float alpha) {
        LogUtil.d("applyScrollBgAlpha alpha:"+alpha);
        rlToolbarBg.getBackground().setAlpha((int) (alpha * 255));
        return this;
    }

    public ImageView getIvLeft() {
        return ivLeft;
    }

    public ImageView getIvRight0() {
        return ivRight0;
    }

    public ImageView getIvRight1() {
        return ivRight1;
    }

    public TextView getTvLeft() {
        return tvLeft;
    }

    public TextView getTvCenter() {
        return tvCenter;
    }

    public TextView getTvRight() {
        return tvRight;
    }
}
