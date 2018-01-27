package com.jyl.base.util;

import android.content.Context;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

public class InputUtil {
	static final String TAG = "InputUtil";

	/**
	 * 切换输入键盘,把光标放到文字最后面
	 * 
	 * @param editText
	 */
	public static void getInputManager(EditText editText, Window window) {
		editText.requestFocus();// 获取焦点
		String inputContent = editText.getText().toString();
		if (inputContent != null && inputContent.length() > 0) {
			editText.setSelection(inputContent.length());
		} else {
			editText.setSelection(0);
		}
		InputMethodManager imm = (InputMethodManager) editText.getContext()
				.getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.toggleSoftInput(0, InputMethodManager.SHOW_FORCED);
		window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
	}

	/**
	 * 关闭软键盘
	 */
	public static void hideSoftInput(EditText editText) {

		InputMethodManager imm = (InputMethodManager) editText.getContext()
				.getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(editText.getWindowToken(),
				InputMethodManager.HIDE_NOT_ALWAYS);
	}

	/**
	 * 切换输入键盘 add 2014.06.07
	 * 
	 * @param editText
	 */
	public static void getInputManagers(EditText editText) {
		editText.requestFocus();
		InputMethodManager imm = (InputMethodManager) editText.getContext()
				.getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.toggleSoftInput(0, InputMethodManager.SHOW_FORCED);
	}

	/**
	 * 弹出软键盘
	 * @param editText
	 */
	public static void showSoftInput(EditText editText) {
		InputMethodManager inputManager =
		(InputMethodManager) editText.getContext().getSystemService(
				Context.INPUT_METHOD_SERVICE);
		inputManager.showSoftInput(editText, 0);
	}	/**
	 * 弹出软键盘
	 * @param v
	 */
	public static void showSoftInput(View v) {
		InputMethodManager imm = (InputMethodManager) v.getContext( ).getSystemService( Context.INPUT_METHOD_SERVICE );
		imm.showSoftInput(v, InputMethodManager.SHOW_FORCED);
	}

}
