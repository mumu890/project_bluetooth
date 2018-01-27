package com.example.sofia.myapplication;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.os.Handler;
import android.os.Message;

import com.jyl.base.util.ToastUtil;

import org.xutils.common.util.LogUtil;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;

/**
 * desc:
 * last modified time:2018/1/24 20:59
 *
 * @author yulin.jing
 * @since 2018/1/24
 */
public class CollectManager {

    private String MAC_ADDRESS = "00:1B:10:00:2A:EC";
    private final UUID MY_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    private BluetoothAdapter bluetoothAdapter = null;
    private BluetoothDevice device = null;
    private BluetoothSocket btSocket = null;

    private static final byte[] slnstanceLock = new byte[0];
    private static CollectManager mInstance;

    private Handler mHandler = null;

    private CollectManager(){
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
    }

    public static CollectManager getInstance(){
        if(mInstance == null){
            synchronized (slnstanceLock) {
                mInstance = new CollectManager();
            }
        }
        return mInstance;
    }

    public void disCoverDevice(){
        if(bluetoothAdapter == null) {
            bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        }
        if(!bluetoothAdapter.isEnabled()) { // 判断蓝牙是否打开
            LogUtil.d("蓝牙未打开");
            // 方式1 直接打开
            bluetoothAdapter.enable();      // 打开蓝牙
//            // 方式1 调用系统蓝牙界面打开
//            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
//            startActivityForResult(enableBtIntent, 0);
        } else {
            LogUtil.d("蓝牙已经打开");
        }
        if(!bluetoothAdapter.isDiscovering()) {
            bluetoothAdapter.startDiscovery();  // 扫描设备
        }
//        bluetoothAdapter.isDiscovering();   // 是否在扫描中
//        bluetoothAdapter.startDiscovery();  // 扫描设备
//        bluetoothAdapter.cancelDiscovery(); // 取消扫描
//        bluetoothAdapter.getState();           // 获取本地蓝牙适配器当前状态（感觉可能调试的时候更需要）
//        bluetoothAdapter.listenUsingRfcommWithServiceRecord(String name,UUID uuid);  //根据名称，UUID创建并返回BluetoothServerSocket，这是创建BluetoothSocket服务器端的第一步
    }


    public void connetDevice(Handler handler,String mac){
        MAC_ADDRESS = mac;
        mHandler = handler;
        if(bluetoothAdapter == null) {
            bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        }
        if(!bluetoothAdapter.isEnabled()) { // 判断蓝牙是否打开
            LogUtil.d("蓝牙未打开");
            // 方式1 直接打开
            bluetoothAdapter.enable();      // 打开蓝牙
//            // 方式1 调用系统蓝牙界面打开
//            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
//            startActivityForResult(enableBtIntent, 0);
        } else {
            LogUtil.d("蓝牙已经打开");
        }
//        bluetoothAdapter.isDiscovering();   // 是否在扫描中
//        bluetoothAdapter.startDiscovery();  // 扫描设备
//        bluetoothAdapter.cancelDiscovery(); // 取消扫描
//        bluetoothAdapter.getState();           // 获取本地蓝牙适配器当前状态（感觉可能调试的时候更需要）
//        bluetoothAdapter.listenUsingRfcommWithServiceRecord(String name,UUID uuid);  //根据名称，UUID创建并返回BluetoothServerSocket，这是创建BluetoothSocket服务器端的第一步

        /* 	功能：以给定的MAC地址去创建一个 BluetoothDevice 类实例(代表远程蓝牙实例)。即使该蓝牙地址不可见，也会产生
        	一个BluetoothDevice 类实例。
    		返回：BluetoothDevice 类实例 。注意，如果该蓝牙设备MAC地址不能被识别，其蓝牙Name为null。
        	异常：如果MAC  address无效，抛出IllegalArgumentException。*/
        try {
            device = bluetoothAdapter.getRemoteDevice(MAC_ADDRESS);  // 根据蓝牙地址获取远程蓝牙设备
        } catch (IllegalArgumentException e) {
            LogUtil.d("连接失败，请检查mac地址 "+e.getMessage());
            tostMsg("连接失败，请检查mac地址");
            return;
        }

        if(device != null)
            new ClientThread().start();
    }

    public void send(String msg){
        LogUtil.d("send msg:"+msg);
        sendMessage(msg);
    }

    public void setBtSocket(BluetoothSocket btSocket) {
        this.btSocket = btSocket;
        if(btSocket != null)
            //进行接收线程
            new ReadMsg().start();
    }

    /**
     * 发送消息
     * @param msg：发送的消息
     */
    private void sendMessage(String msg) {
        if (btSocket != null) {
            try {
                OutputStream out = btSocket.getOutputStream();
                out.write(msg.getBytes());      //将消息字节发出
                out.flush();                    //确保所有数据已经被写出，否则抛出异常
            } catch (IOException e) {
                e.printStackTrace();
                LogUtil.d("Exception during write." + e);
                ToastUtil.showShort("连接异常");
                return;
            }
        } else {
            ToastUtil.showShort("未建立连接");   //防止未连接就发送信息
        }

    }

    private void tostMsg(String msg){
        Message message = mHandler.obtainMessage();
        message.what = 0;
        message.obj = msg;
        mHandler.sendMessage(message);
    }
    /**
     * 客户端，进行连接的线程
     * @author Administrator
     *
     */
    class ClientThread extends Thread{
        @Override
        public void run(){
            try {
                btSocket = device.createRfcommSocketToServiceRecord(MY_UUID);        // 根据UUID创建并返回一个BluetoothSocket
            } catch (IOException e) {
                LogUtil.d("Socket creation failed.");
                tostMsg("设备连接失败");
                return;
            }

            try {
                tostMsg("正在连接，请稍后......");
                btSocket.connect();
                LogUtil.d("BT connection established, data transfer link autoOpen.");
            } catch (IOException e) {
                LogUtil.d("Unable to connect socket."+e);
                btSocket=null;
                tostMsg("设备连接失败");
                return;
            }
            LogUtil.d("连接已经建立");
            tostMsg("设备连接成功");
            mHandler.sendEmptyMessage(1);
            //进行接收线程
            new ReadMsg().start();
        }
    }

    /**
     * 循环读取信息的线程
     * @author Administrator
     *
     */
    class ReadMsg extends Thread{
        @Override
        public void run(){
            byte[] buffer = new byte[1024];//定义字节数组装载信息
            int bytes;//定义长度变量
            InputStream in=null;
            try {
                //使用socket获得输入流*
                in = btSocket.getInputStream();
                //一直循环接收处理消息*
                while(true){
                    if((bytes=in.read(buffer))!=0){
                        byte[] buf_data = new byte[bytes];
                        for (int i = 0; i < bytes; i++){
                            buf_data[i]=buffer[i];
                        }
                        String msg=new String(buf_data);                //最后得到String类型消息
                        Message message = mHandler.obtainMessage();
                        message.what = 1;
                        message.obj = msg;
                        mHandler.sendMessage(message);
                        LogUtil.d("recervice msg:"+msg);
                    }
                    try {
                        Thread.sleep(50);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                tostMsg("连接已断开");
            }finally{
                try {
                    in.close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }
    }
}
