package com.example.sofia.myapplication;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;

import org.xutils.common.util.LogUtil;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;

public class BluetoothUtil {
	BluetoothAdapter mBluetoothAdapter;
	BluetoothSocket mSocket;
	Timer timer;// 扫描时间定时器
	boolean isOpenBle = false;// 是否打开蓝牙
	boolean isConn = false;// 是否连接设备
	boolean isPaired = false;

	private final UUID MY_UUID = UUID.fromString("00001101-0000-1000-8000-00805f9b34fb");
	Context context = null;
	BluetoothCallBack callBack = null;

	ArrayList<BluetoothDevice> list_device = new ArrayList();			// 扫描到的

	BluetoothUtil(Context context, BluetoothCallBack callBack) {
		this.callBack = callBack;
		this.context = context;
		initBleAdapter();
		registerBoard();
	}

	// 初始化蓝牙适配器
	public void initBleAdapter() {
		if (mBluetoothAdapter == null) {
			mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
			LogUtil.d("BluetoothUtil  initBleAdapter ");
		}
	}

	/**
	 * 得到本机蓝牙名称
	 * 
	 * @return 本机蓝牙名称
	 */
	public String getSelfName() {
		initBleAdapter();
		String name = mBluetoothAdapter.getName();
		return name;
	}

	/**
	 * 打开蓝牙
	 * 
	 * @return 是否打开蓝牙
	 */
	public boolean openBle() {
		initBleAdapter();
		boolean openResult = mBluetoothAdapter.enable();
		if (openResult) {
			LogUtil.d("BluetoothUtil  openBle");
		} else {
			Log.e("BluetoothUtil", "openBle fail");
		}

		return openResult;
	}

	/**
	 * 关闭蓝牙
	 * 
	 * @return 是否关闭蓝牙
	 */
	public boolean closeBle() {
		initBleAdapter();
		boolean closeResult = mBluetoothAdapter.disable();
		if (closeResult) {
			LogUtil.d("BluetoothUtil  closeBle");
		} else {
			Log.e("BluetoothUtil", "closeBle fail");
		}
		return closeResult;

	}

	/**
	 * 扫描蓝牙
	 * 
	 * @param scanTime 超时时间
	 * @return 扫描是否成功
	 */
	public boolean startScan(int scanTime) {
		initBleAdapter();
		boolean scanResult = false;
		scanTime = scanTime * 1000;
		timer = new Timer(true);
		timer.schedule(new TimerTask() {
			@Override
			public void run() {
				mBluetoothAdapter.cancelDiscovery();
				if (timer != null) {
					timer.cancel();
					timer = null;
				}
			}
		}, scanTime);
		callBack.startScan();
		if (list_device.size() > 0) {
			list_device.clear();
		}
		scanResult = mBluetoothAdapter.startDiscovery();
		if (scanResult) {
			LogUtil.d("BluetoothUtil  startScan");
		} else {
			Log.e("BluetoothUtil", "startScan fail");
		}

		return scanResult;
	}

	/**
	 * 开始连接设备
	 * 
	 * @param device
	 *            连接的设备
	 */
	public void startConn(BluetoothDevice device) {
		initBleAdapter();
		if (device == null) {
			Log.e("BluetoothUtil", "device is null");
		} else {
			new ConnectionThread(device).start();
			LogUtil.d("BluetoothUtil  startConn");
		}
	}

	/**
	 * 断开当前连接
	 */
	public void stopConn() {
		try {
			isConn = false;
			this.mSocket.close();
			LogUtil.d("BluetoothUtil  stopConn");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 注册广播
	 */
	public void registerBoard() {
		IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction("android.bluetooth.device.action.FOUND");
		intentFilter.addAction("android.bluetooth.adapter.action.DISCOVERY_FINISHED");
		intentFilter.addAction("android.bluetooth.adapter.action.STATE_CHANGED");
		intentFilter.addAction("android.bluetooth.device.action.BOND_STATE_CHANGED");
		intentFilter.addAction("android.bluetooth.device.action.PAIRING_REQUEST");
		intentFilter.addAction("android.bluetooth.device.action.PAIRING_REQUEST");
		this.context.registerReceiver(mReceiver, intentFilter);
		LogUtil.d("BluetoothUtil  registerBoard");
	}

	/**
	 * 处理广播
	 */
	private BroadcastReceiver mReceiver = new BroadcastReceiver() {

		public void onReceive(Context context, Intent intent) {
			String exceptionMessage = null;
			String action = intent.getAction();
			Bundle bundle = intent.getExtras();

			if (bundle != null) {
				exceptionMessage = bundle.getString("exception");
			}

			LogUtil.d("BluetoothUtil  broadcast message:" + action.toString());

			// 开始搜索广播
			if (action.equals("android.bluetooth.device.action.FOUND")) {
				BluetoothDevice device = (BluetoothDevice) intent.getParcelableExtra("android.bluetooth.device.extra.DEVICE");
				LogUtil.d("BluetoothUtil "+device.getName());
				list_device.add(device);
				if (callBack != null) {
					callBack.findDevice(list_device);
					LogUtil.d("BluetoothUtil  call.findDevice");
				}
			}
			// 停止搜索广播
			if (action.equals("android.bluetooth.adapter.action.DISCOVERY_FINISHED")) {
				if (callBack != null) {
					callBack.finishFind(list_device);
					LogUtil.d("BluetoothUtil  call.finishFind");
				}

			}
			// 蓝牙适配器改变广播
			if (action.equals("android.bluetooth.adapter.action.STATE_CHANGED")) {
				// 12表明蓝牙适配器是打开的
				if (intent.getIntExtra("android.bluetooth.adapter.extra.STATE", -1) == 12) {
					LogUtil.d("BluetoothUtil  mBluetoothAdapter 12");
					isOpenBle = true;
					if (callBack != null) {
						callBack.bluOpen();
						LogUtil.d("BluetoothUtil  call.bluOpen");
					}
				}
				// 10表示蓝牙适配器是关闭的
				if (intent.getIntExtra("android.bluetooth.adapter.extra.STATE", -1) == 10) {
					LogUtil.d("BluetoothUtil  mBluetoothAdapter 10");
					isOpenBle = false;
					if (callBack != null) {
						callBack.bluClose();
						LogUtil.d("BluetoothUtil  call.bluClose");
					}
				}
			}

			// 蓝牙绑定状态广播
			if (action.equals("android.bluetooth.device.action.BOND_STATE_CHANGED")) {
				BluetoothDevice dev = (BluetoothDevice) intent
						.getParcelableExtra("android.bluetooth.device.extra.DEVICE");
				int bondState = dev.getBondState();
				switch (bondState) {
				case 10:
					callBack.BondFail();
					break;
				case 11:
					callBack.Bonding();
					break;
				case 12:
					new ConnectionThread(dev).start();
					callBack.BondSuccess();
					break;

				default:
					break;
				}
			}

			if (action.equals("android.bluetooth.device.action.PAIRING_REQUEST")) {
			}
		}
	};

	/**
	 * 开启线程连接设备
	 * 
	 * @author wt
	 *
	 */
	private class ConnectionThread extends Thread {
		private BluetoothDevice device;

		public ConnectionThread(BluetoothDevice device) {
			if (callBack != null) {
				callBack.startConn();
			}
			this.device = device;
		}

		@Override
		public void run() {
			if (device.getBondState()==10) {
				doBondProcess();
			}else{
				boolean result = connectRfcommSocket();
				if (!result) {
					callBack.connectionFail();
					isConn = false;
				} else {
					isConn = true;
					callBack.connectionSucc(this.device);
					LogUtil.d("BluetoothUtil  connectRfcommSocket connectionSucc");
					return;
				}
			}
		}
		
		//绑定的过程
	    private void doBondProcess() {
	      int during = 0;
	      Log.i("ConnManager", "doBondProcess...");
	      while (!isPaired && (during < 20)) {
	        if (this.device.getBondState() == 12) {
	          Log.i("ConnManager", "bond status is bonded");
	          callBack.BondSuccess();
	          isPaired = true;
	        }if (device.getBondState() == 11) {
	          Log.i("ConnManager", "bond status is bonding");
	        } else if (device.getBondState() == 10) {
	          Log.i("ConnManager", "bond status is none");
	          try {
	              Log.i("ConnManager", "start bond device");
	  	    	  device.createBond();
	          } catch (Exception e) {
	            e.printStackTrace();
	          }
	        }
	        during++;
	      }
	    }

		// 创建BluetoothSocket，连接Socket通道
		private boolean connectRfcommSocket() {
			callBack.startConn();
			LogUtil.d("BluetoothUtil  connectRfcommSocket...");

			if ((Build.VERSION.SDK_INT >= 10)) {
				Class cls = BluetoothDevice.class;
				Method m = null;
				try {
					m = cls.getMethod("createInsecureRfcommSocketToServiceRecord", new Class[] { UUID.class });
				} catch (NoSuchMethodException e) {
					e.printStackTrace();
				}
				if (m != null)
					try {
						mSocket = (BluetoothSocket) m.invoke(this.device, new Object[] { MY_UUID });
					} catch (IllegalArgumentException e) {
						e.printStackTrace();
					} catch (IllegalAccessException e) {
						e.printStackTrace();
					} catch (InvocationTargetException e) {
						e.printStackTrace();
					}
			} else {
				try {
					mSocket = this.device.createRfcommSocketToServiceRecord(MY_UUID);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

			if (!mSocket.isConnected()) {
				try {
					if (mSocket != null) {
						mSocket.connect();
						LogUtil.d("BluetoothUtil  socket connect");
						return true;
					} else {
						Log.e("BluetoothUtil", "socket is null");
					}
				} catch (IOException e) {
					Log.e("BluetoothUtil", e.getMessage());
					return false;
				}
			}
			return false;
		}
	}
}
