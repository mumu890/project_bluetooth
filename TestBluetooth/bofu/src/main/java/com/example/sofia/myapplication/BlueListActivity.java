package com.example.sofia.myapplication;

import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.jyl.base.ui.BaseActivity;
import com.jyl.base.util.ToastUtil;

import org.xutils.common.util.LogUtil;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import java.lang.Thread.UncaughtExceptionHandler;
import java.util.ArrayList;

@ContentView(R.layout.activity_blulist)
public class BlueListActivity extends BaseActivity implements OnClickListener, BluetoothCallBack, UncaughtExceptionHandler {

	BluetoothUtil bluetoothUtil;
	Context context;

	// 蓝牙打开图片
	private static final int BLE_OPEN = 1;
	// 蓝牙关闭图片
	private static final int BLE_CLOSE = 2;
	// 信息
	private static final int TV_SHOW = 3;
	// 扫描到的设备
	private static final int SCAN_DEVICE = 4;

	@ViewInject(R.id.iv_openble)
	ImageView iv_openble;
	@ViewInject(R.id.btn_start)
	Button btn_start;
	@ViewInject(R.id.btn_uncon)
	Button btn_uncon;
	@ViewInject(R.id.lv_ble)
	ListView lv_ble;
	@ViewInject(R.id.tv_youself)
	TextView tv_youself;
	@ViewInject(R.id.tv_show)
	TextView tv_show;
	@ViewInject(R.id.tv_blue)
	TextView tv_blue;
	@ViewInject(R.id.lv_bond_ble)
	ListView lv_bond;
	ArrayList<BluetoothDevice> list_device = new ArrayList<BluetoothDevice>(); ;
	final ArrayList<BluetoothDevice> list_device_bond = new ArrayList<>();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if(RequestPermissionsActivity.startPermissionActivity(this)) {
			finish();
		}
		Thread.setDefaultUncaughtExceptionHandler(this);
		initView();
	}

	public void initView() {
		context = this;
		bluetoothUtil = new BluetoothUtil(context, this);
		bluetoothUtil.registerBoard();
		tv_youself.setText(bluetoothUtil.getSelfName());
		iv_openble.setOnClickListener(this);
		btn_start.setOnClickListener(this);
		btn_uncon.setOnClickListener(this);

		if(bluetoothUtil.mBluetoothAdapter.isEnabled()) {
			bluetoothUtil.isOpenBle = true;
			tv_blue.setText("蓝牙打开");
			handler.sendEmptyMessage(BLE_OPEN);
		} else {
			bluetoothUtil.isOpenBle = false;
			tv_blue.setText("蓝牙关闭");
			handler.sendEmptyMessage(BLE_CLOSE);
		}

		setBond();
	}

	private void setBond(){
		list_device_bond.clear();
		list_device_bond.addAll(bluetoothUtil.mBluetoothAdapter.getBondedDevices());
		LogUtil.d("set bond deviceSize:"+list_device_bond.size());
		if(list_device_bond.size() > 0) {
			lv_bond.setVisibility(View.VISIBLE);

			String[] items =new String[list_device_bond.size()]; // 设备名称的集合列表
			for (int i = 0; i < list_device_bond.size(); i++) {
				String name = list_device_bond.get(i).getName();
				if(TextUtils.isEmpty(name)) {
					name = "未知设备";
				}
				items[i] = name;
				LogUtil.d("set bond deviceName:"+items[i]);
			}

			ArrayAdapter<String> adapter = new ArrayAdapter<>(context, android.R.layout.simple_list_item_1, items);
			lv_bond.setAdapter(adapter);
			lv_bond.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
					if (bluetoothUtil.isOpenBle) {
						if (bluetoothUtil.isConn) {
							Toast.makeText(context, "请先断开连接", Toast.LENGTH_SHORT).show();
						}else{
							BluetoothDevice device = list_device_bond.get(position);
							bluetoothUtil.startConn(device);
						}
					}
				}
			});
		} else {
			lv_bond.setVisibility(View.GONE);
		}
	}

	@Event(value = R.id.ll_collect)
	private void collect(View view){
		if(tv_show.getText().equals("无")) {
			ToastUtil.showShort("没有连接的设备");
		} else {
			CollectManager.getInstance().setBtSocket(bluetoothUtil.mSocket);
			startActivity(new Intent(this,ActivityVertical.class));
		}
	}

	@Event(value = R.id.tv_bond)
	private void bondList(View view){
		if(list_device_bond.size() > 0 && lv_bond.getVisibility() == View.GONE) {
			lv_bond.setVisibility(View.VISIBLE);
		} else {
			lv_bond.setVisibility(View.GONE);
		}
	}

	@Event(value = R.id.tv_list)
	private void deviceList(View view){
		if(list_device.size() > 0 && lv_ble.getVisibility() == View.INVISIBLE) {
			lv_ble.setVisibility(View.VISIBLE);
		} else {
			lv_ble.setVisibility(View.INVISIBLE);
		}
	}


	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.iv_openble:
				if (bluetoothUtil.isOpenBle) {
					bluetoothUtil.closeBle();
					handler.sendEmptyMessage(BLE_CLOSE);
				}else {
					bluetoothUtil.openBle();
					handler.sendEmptyMessage(BLE_OPEN);
				}
			break;

		case R.id.btn_start:
			if (bluetoothUtil.isOpenBle) {
				bluetoothUtil.startScan(5);
			} else {
				Toast.makeText(context, "请先打开蓝牙", Toast.LENGTH_SHORT).show();
			}
			break;

		case R.id.btn_uncon:
			if (bluetoothUtil.isConn) {
				bluetoothUtil.stopConn();
				myHandMessage("无");
			}else{
				Toast.makeText(this, "还未连接", Toast.LENGTH_SHORT).show();
			}
			break;

		default:
			break;
		}
	}

	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case BLE_OPEN:
				tv_blue.setText("蓝牙打开");
				iv_openble.setImageResource(R.drawable.bleopen);
				break;

			case BLE_CLOSE:
				tv_blue.setText("蓝牙关闭");
				iv_openble.setImageResource(R.drawable.bleclose);
				break;

			case TV_SHOW:
				String info = (String) msg.obj;
				tv_show.setText(info);
				btn_uncon.setVisibility(msg.arg1 == 1 ? View.VISIBLE : View.GONE);
				setBond();
				break;

			case SCAN_DEVICE:
				list_device = (ArrayList<BluetoothDevice>) msg.obj;
				String[] items =new String[list_device.size()]; // 设备名称的集合列表

				for (int i = 0; i < list_device.size(); i++) {
					if(list_device.get(i).getBondState() != BluetoothDevice.BOND_BONDED) {
						String name = list_device.get(i).getName();
						items[i] = name;
					}
				}

				ArrayAdapter<String> adapter = new ArrayAdapter<String>(context, android.R.layout.simple_list_item_1, items);
				lv_ble.setAdapter(adapter);
				lv_ble.setOnItemClickListener(new OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
						if (bluetoothUtil.isOpenBle) {
							if (bluetoothUtil.isConn) {
								Toast.makeText(context, "请先断开连接", Toast.LENGTH_SHORT).show();
							}else{
								BluetoothDevice device = list_device.get(position);
								bluetoothUtil.startConn(device);
							}
						}else {
							Toast.makeText(context, "请先打开蓝牙", Toast.LENGTH_SHORT).show();
						}
					}
				});
				setBond();
				break;
	
			default:
				break;
			}
		};
	};

	@Override
	public void uncaughtException(Thread thread, Throwable ex) {
		Log.i("MainActivity", "uncaughtException  " + ex);
	}

	public void myHandMessage(String str) {
		Message message = new Message();
		message.what = TV_SHOW;
		message.obj = str;
		handler.sendMessage(message);
	}
	public void myHandMessage(String str,int collectStatus) {
		Message message = new Message();
		message.what = TV_SHOW;
		message.obj = str;
		message.arg1 = collectStatus;
		handler.sendMessage(message);
	}

	@Override
	public void bluOpen() {
		handler.sendEmptyMessage(BLE_OPEN);
	}

	@Override
	public void bluClose() {
		handler.sendEmptyMessage(BLE_CLOSE);
	}
	

	@Override
	public void startScan() {
		ProgressUtil.show(context, "正在搜索...");
	}

	@Override
	public void findDevice(ArrayList<BluetoothDevice> list) {
		if (list.size() == 0) {
			myHandMessage("未查找到设备");
		}else{
			Message message = new Message();
			message.what = SCAN_DEVICE;
			message.obj = list;
			handler.sendMessage(message);
		}
	}

	@Override
	public void finishFind(ArrayList<BluetoothDevice> list) {
		ProgressUtil.hide();
	}

	@Override
	public void connectionSucc(BluetoothDevice device) {
		ProgressUtil.hide();
		String name = device.getName();
		myHandMessage(name,1);
	}

	@Override
	public void connectionFail() {
		ProgressUtil.hide();
		myHandMessage("连接失败");
	}

	@Override
	public void startConn() {
		ProgressUtil.show(context,"正在连接设备...");
	}

	@Override
	public void BondFail() {
		myHandMessage("绑定失败");
		ProgressUtil.hide();
	}

	@Override
	public void BondSuccess() {
		myHandMessage("绑定成功");
	}

	@Override
	public void Bonding() {
		myHandMessage("正在绑定");
	}

}
