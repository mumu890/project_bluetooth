package com.example.bledemo;

import android.bluetooth.BluetoothDevice;

import java.util.ArrayList;

public interface BluetoothCallBack {
	void startScan();//开始扫描
	void findDevice(ArrayList<BluetoothDevice> list);
	void finishFind(ArrayList<BluetoothDevice> list);
	
	void bluOpen();
	void bluClose();
	
	void connectionSucc(BluetoothDevice device);
	void connectionFail();
	void startConn();
	
	void BondFail();
	void BondSuccess();
	void Bonding();
}
