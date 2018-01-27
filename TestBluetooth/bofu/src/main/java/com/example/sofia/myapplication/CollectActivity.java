package com.example.sofia.myapplication;

import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;

import com.jyl.base.util.SPUtils;
import com.jyl.base.util.ToastUtil;

import org.xutils.common.util.LogUtil;

public class CollectActivity extends AppCompatActivity {
    private String MAC_ADDRESS = "00:1B:10:00:2A:EC";

    EditText editText0;
    EditText editText1;
    EditText editText2;
    EditText editText3;
    EditText editText4;
    EditText editText5;
    CheckBox checkBox;

    private Handler mHandler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(msg.what == 0) {
                ToastUtil.showShort(msg.obj.toString());
            } else if (msg.what == 1){
                if(checkBox.isChecked()) {
                    SPUtils.putString(CollectActivity.this,"bofu",MAC_ADDRESS);
                }
                startActivity(new Intent(CollectActivity.this,ActivityVertical.class));
                finish();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(BluetoothAdapter.getDefaultAdapter() == null) {
            ToastUtil.showShort("此设备不支持蓝牙");
            return;
        } else {
            LogUtil.d("此设备支持蓝牙");
            // 申请蓝牙相关的权限
            if(RequestPermissionsActivity.startPermissionActivity(this)){
                finish();
            }
        }

        setContentView(R.layout.activity_collect);
        editText0 = (EditText) findViewById(R.id.edit_mac_0);
        editText1 = (EditText) findViewById(R.id.edit_mac_1);
        editText2 = (EditText) findViewById(R.id.edit_mac_2);
        editText3 = (EditText) findViewById(R.id.edit_mac_3);
        editText4 = (EditText) findViewById(R.id.edit_mac_4);
        editText5 = (EditText) findViewById(R.id.edit_mac_5);
        checkBox = (CheckBox) findViewById(R.id.checkBox);

        String mac = SPUtils.getString(CollectActivity.this,"bofu","");
        if(!TextUtils.isEmpty(mac)){
            String macs[] =  mac.split(":");
            for(int i = 0; i<macs.length ; i ++){
                switch (i) {
                    case 0:
                        editText0.setText(macs[i]);
                        break;
                    case 1:
                        editText1.setText(macs[i]);
                        break;
                    case 2:
                        editText2.setText(macs[i]);
                        break;
                    case 3:
                        editText3.setText(macs[i]);
                        break;
                    case 4:
                        editText4.setText(macs[i]);
                        break;
                    case 5:
                        editText5.setText(macs[i]);
                        break;
                }
            }
        }

    }

    public void collect(View view){
        MAC_ADDRESS = editText0.getText().toString() +
                ":" + editText1.getText().toString() +
                ":" + editText2.getText().toString() +
                ":" + editText3.getText().toString() +
                ":" + editText4.getText().toString() +
                ":" + editText5.getText().toString();
        LogUtil.d("mac:"+MAC_ADDRESS);
        if(TextUtils.isEmpty(MAC_ADDRESS)) {
            ToastUtil.showShort("请输入Mac地址");
        } else {
            CollectManager.getInstance().connetDevice(mHandler,MAC_ADDRESS);
        }
    }
}
