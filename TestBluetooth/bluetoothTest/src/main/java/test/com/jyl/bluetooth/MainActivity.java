package test.com.jyl.bluetooth;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import com.jyl.base.util.ToastUtil;

import org.xutils.common.util.LogUtil;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;

import test.com.jyl.bluetooth.holde.ChatAdapte;
import test.com.jyl.bluetooth.holde.HoldeBeanServiceMsg;
import test.com.jyl.bluetooth.holde.HoldeBeanUserMsg;

public class MainActivity extends AppCompatActivity {
    private final String MAC_ADDRESS = "00:1B:10:00:2A:EC";
//    private final String MAC_ADDRESS = "74:AC:5F:E2:7E:5B";
    private final UUID MY_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    private BluetoothAdapter bluetoothAdapter = null;

    private BluetoothServerSocket bluetoothServerSocket = null;
    private BluetoothSocket socket = null;

    private BluetoothDevice device = null;
    private BluetoothSocket btSocket = null;

    private RecyclerView recyclerView;
    private EditText editText;

    private ChatAdapte adapte;

    private Handler mHandler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(msg.what == 0) {
                ToastUtil.showShort(msg.obj.toString());
            } else if (msg.what == 1){
                addServiceMsg(msg.obj.toString());
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LogUtil.d("onCreate");
        setContentView(R.layout.activity_main);
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if(bluetoothAdapter == null) {
            ToastUtil.showShort("此设备不支持蓝牙");
            finish();
            return;
        } else {
            LogUtil.d("此设备支持蓝牙");

            // 申请蓝牙相关的权限
            if(RequestPermissionsActivity.startPermissionActivity(this)){
                finish();
            }
        }

        recyclerView = (RecyclerView) findViewById(R.id.rcy);
        editText = (EditText) findViewById(R.id.edt);
        adapte = new ChatAdapte(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapte);

        registBroadcast();

        connetDevice();
    }

    private void connetDevice(){
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
        bluetoothAdapter.startDiscovery();      // 扫描设备
//        bluetoothAdapter.getBondedDevices();    //已经连接的蓝牙设配
//        bluetoothAdapter.cancelDiscovery(); // 取消扫描
//        bluetoothAdapter.getState();           // 获取本地蓝牙适配器当前状态（感觉可能调试的时候更需要）
//        bluetoothAdapter.listenUsingRfcommWithServiceRecord(String name,UUID uuid);  //根据名称，UUID创建并返回BluetoothServerSocket，这是创建BluetoothSocket服务器端的第一步

        /* 	功能：以给定的MAC地址去创建一个 BluetoothDevice 类实例(代表远程蓝牙实例)。即使该蓝牙地址不可见，也会产生
        	一个BluetoothDevice 类实例。
    		返回：BluetoothDevice 类实例 。注意，如果该蓝牙设备MAC地址不能被识别，其蓝牙Name为null。
        	异常：如果MAC  address无效，抛出IllegalArgumentException。*/
//        device = bluetoothAdapter.getRemoteDevice(MAC_ADDRESS);  // 根据蓝牙地址获取远程蓝牙设备
//        if(device != null)
//            new ClientThread().start();
    }

    public void addServiceMsg(String msg){
        if(TextUtils.isEmpty(msg))
            return;
        HoldeBeanServiceMsg beanRobotVoice = new HoldeBeanServiceMsg();
        beanRobotVoice.setMsg(msg);
        adapte.addVoiceRobot(beanRobotVoice);
        recyclerView.scrollToPosition(adapte.getItemCount() -1);
    }

    public void addUserMsg(String msg){
        if(TextUtils.isEmpty(msg))
            return;
        HoldeBeanUserMsg beanUserVoice = new HoldeBeanUserMsg();
        beanUserVoice.setMsg(msg);
        adapte.addVoiceUser(beanUserVoice);
        recyclerView.scrollToPosition(adapte.getItemCount() -1);
    }


    public void send(View view){
        String msg = editText.getText().toString();
        LogUtil.d("send msg:"+msg);
        if(TextUtils.isEmpty(msg)) {
            ToastUtil.showShort("请输入内容");
        } else {
            sendMessage(msg);
        }
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
            addUserMsg(msg);
            editText.setText("");
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
     * 服务端，接收连接的线程
     *
     */
    class MatchThread extends Thread{
        @Override
        public void run() {
            //服务器端的bltsocket需要传入uuid和一个独立存在的字符串，以便验证，通常使用包名的形式
            try {
                bluetoothServerSocket = bluetoothAdapter.listenUsingRfcommWithServiceRecord("com.bluetooth.demo", MY_UUID);
            } catch (IOException e) {
                e.printStackTrace();
            }
            while (true) {
                try {
                    //注意，当accept()返回BluetoothSocket时，socket已经连接了，因此不应该调用connect方法。
                    //这里会线程阻塞，直到有蓝牙设备链接进来才会往下走
                    socket = bluetoothServerSocket.accept();
                    if (socket != null) {
                        //回调结果通知
                        BluetoothDevice device = socket.getRemoteDevice();

                        LogUtil.d("name:"+device.getName()+ " mac:"+device.getAddress());

                        //如果你的蓝牙设备只是一对一的连接，则执行以下代码
                        bluetoothServerSocket.close();
                        //如果你的蓝牙设备是一对多的，则应该调用break；跳出循环
                        //break;
                    }
                    try {
                        Thread.sleep(50);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                } catch (IOException e) {
                    try {
                        bluetoothServerSocket.close();
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                    break;
                }
            }
        }
    }

    /**
     　　　* 服务端，接收连接的线程
     * @author Administrator
     *
     */
    class ServiceThread extends Thread{
        @Override
        public void run(){
            try {
                //先用本地蓝牙适配器创建一个serversocket *
                bluetoothServerSocket = bluetoothAdapter.listenUsingRfcommWithServiceRecord(bluetoothAdapter.getName(), MY_UUID);
                LogUtil.d("正在等待连接");
                if(socket!=null){
                    LogUtil.d("连接成功");}
                //等待连接，该方法阻塞*
                socket = bluetoothServerSocket.accept();
                LogUtil.d("连接成功");
                new ReadMsg().start();
            } catch (IOException e) {
                // TODO Auto-generated catch block
//                Toast.makeText(getApplicationContext(), "IOExeption", 1).show();
                System.out.println("IOExeption");
                LogUtil.d("连接失败");
                e.printStackTrace();
            }

        }
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
                    LogUtil.d("BT connection established, data transfer link open.");
                } catch (IOException e) {
                    LogUtil.d("Unable to connect socket."+e);
                    btSocket=null;
                    tostMsg("设备连接失败");
                    return;
                }
                LogUtil.d("连接已经建立");
                tostMsg("设备连接成功");

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

    private void registBroadcast(){
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(BluetoothAdapter.ACTION_STATE_CHANGED);
        intentFilter.addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED);
        intentFilter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        intentFilter.addAction(BluetoothAdapter.ACTION_CONNECTION_STATE_CHANGED);
        intentFilter.addAction(BluetoothAdapter.ACTION_LOCAL_NAME_CHANGED);

        intentFilter.addAction(BluetoothDevice.ACTION_NAME_CHANGED);
        intentFilter.addAction(BluetoothDevice.ACTION_FOUND);
        intentFilter.addAction(BluetoothDevice.ACTION_UUID);
        intentFilter.addAction(BluetoothDevice.ACTION_PAIRING_REQUEST);
        intentFilter.addAction(BluetoothDevice.ACTION_PAIRING_REQUEST);

        registerReceiver(broadcastReceiver,intentFilter);
    }

    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            // 蓝牙开关状态的广播
            if (action.equals(BluetoothAdapter.ACTION_STATE_CHANGED)) {
                LogUtil.d("BluetoothAdapter.ACTION_STATE_CHANGED");
                // int STATE_OFF = 10;          //蓝牙关闭
                // int STATE_ON = 12;           //蓝牙打开
                // int STATE_TURNING_OFF = 13;  //蓝牙正在关闭
                // int STATE_TURNING_ON = 11;   //蓝牙正在打开
                int state = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, BluetoothAdapter.STATE_OFF);
                LogUtil.d("state:" + state);
             // 扫描开始
            } else if (action.equals(BluetoothAdapter.ACTION_DISCOVERY_STARTED)) {
                LogUtil.d("BluetoothAdapter.ACTION_DISCOVERY_STARTED");
            // 扫描结束
            } else if (action.equals(BluetoothAdapter.ACTION_DISCOVERY_FINISHED)) {
                LogUtil.d("BluetoothAdapter.ACTION_DISCOVERY_FINISHED");
            // 蓝牙连接状态的广播
            } else if (action.equals(BluetoothAdapter.ACTION_CONNECTION_STATE_CHANGED)) {
                LogUtil.d("BluetoothAdapter.ACTION_CONNECTION_STATE_CHANGED ");
                int stateAfte = intent.getIntExtra(BluetoothAdapter.EXTRA_PREVIOUS_CONNECTION_STATE, BluetoothAdapter.ERROR);   //前一次的连接状态
                int state = intent.getIntExtra(BluetoothAdapter.EXTRA_CONNECTION_STATE, BluetoothAdapter.ERROR);                //当前的连接状态
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);                               //连接的设备信息
                if (device == null) return;
                String name = device.getName();
                String addr = device.getAddress();
                LogUtil.d("stateAfte:" + stateAfte + "," + "state:" + state + "," + "name:" + name + "," + "addr:" + addr);
            // 蓝牙设备的名字变化的广播(当前设备)
            } else if (action.equals(BluetoothAdapter.ACTION_LOCAL_NAME_CHANGED)) {
                LogUtil.d("BluetoothAdapter.ACTION_LOCAL_NAME_CHANGED ");
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                if (device == null)
                    return;
                String name = device.getName();
                String addr = device.getAddress();
                LogUtil.d("name:" + name + "," + "address:" + addr);
            // 蓝牙设备的名字变化的广播（远程设备）
            } else if (action.equals(BluetoothDevice.ACTION_NAME_CHANGED)) {
                LogUtil.d("BluetoothDevice.ACTION_NAME_CHANGED  ");
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                if (device == null) return;
                String name = device.getName();
                String addr = device.getAddress();
                LogUtil.d("name:" + name + "," + "address:" + addr);
            // 扫描到设备
            } else if (action.equals(BluetoothDevice.ACTION_FOUND)) {
                // 获取设备相关的信息
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                //信号强度
                short rssi = intent.getExtras().getShort(BluetoothDevice.EXTRA_RSSI);
                if (device == null) return;
                String name = device.getName();
                String addr = device.getAddress();
                LogUtil.d("BluetoothDevice.ACTION_FOUND name:" + name + "," + "address:" + addr +" rssi:"+rssi);

                if(device.getName()!= null && device.getName().contains("bofu")) {
                    if (device.getBondState() == BluetoothDevice.BOND_NONE) {     // 判断是否配对
                        LogUtil.d("开始配对 "+"["+device.getName()+"]");
                        try {
                            //通过工具类ClsUtils,调用createBond方法
                            ClsUtils.createBond(device.getClass(), device);
                        } catch (Exception e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                    } else if (device.getBondState() == BluetoothDevice.BOND_BONDED){
                        LogUtil.d("已经配对");
                        MainActivity.this.device = device;
                        new ClientThread().start();
                    } else if (device.getBondState() == BluetoothDevice.BOND_BONDING) {
                        LogUtil.d("正在配对");
                    }
                }

            // 收到设备的UUID广播
            } else if (action.equals(BluetoothDevice.ACTION_UUID)) {
                LogUtil.d("BluetoothDevice.ACTION_UUID");
                // 获取设备的UUID
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                Parcelable[] uuidExtra = intent.getParcelableArrayExtra(BluetoothDevice.EXTRA_UUID);
                for (int i = 0; i < uuidExtra.length; i++) {
                    String uuid = uuidExtra[i].toString();
                    LogUtil.d("uuid:" + uuid);
                }

                if(device.getName() != null && device.getName().contains("bofu") && device.getBondState() == BluetoothDevice.BOND_BONDED) {
                    MainActivity.this.device = device;
                    new ClientThread().start();
                }

            // 有蓝牙设备的配对请求时，会监听到这个广播
            } else if (action.equals(BluetoothDevice.ACTION_PAIRING_REQUEST)) {
                LogUtil.d("BluetoothDevice.ACTION_PAIRING_REQUEST");
                // 获取设备相关的信息
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                if (device.getName() != null && device.getName().contains("bofu")) {
                    LogUtil.d("OKOKOK");
                    try {
                        //1.确认配对
                        ClsUtils.setPairingConfirmation(device.getClass(), device, true);
                        //2.终止有序广播
                        LogUtil.d("isOrderedBroadcast:" + isOrderedBroadcast() + ",isInitialStickyBroadcast:" + isInitialStickyBroadcast());
                        abortBroadcast();//如果没有将广播终止，则会出现一个一闪而过的配对框。
                        //3.调用setPin方法进行配对...
                        boolean ret = ClsUtils.setPin(device.getClass(), device, "0000");
                        LogUtil.d("OKOKOK ret："+ret);
                    } catch (Exception e) {
                        LogUtil.d("OKOKOK Exception:"+e.getMessage());
                        e.printStackTrace();
                    }
                } else {
                    LogUtil.d("这个设备不是目标蓝牙设备");
                }

            // 蓝牙配对状态的广播
            } else if (action.equals(BluetoothDevice.ACTION_BOND_STATE_CHANGED)) {
                LogUtil.d("BluetoothDevice.ACTION_BOND_STATE_CHANGED");
                // int BOND_NONE = 10;      //配对没有成功
                // int BOND_BONDING = 11;   //配对中
                // int BOND_BONDED = 12;    //配对成功
                int stateAfte = intent.getIntExtra(BluetoothDevice.EXTRA_PREVIOUS_BOND_STATE, BluetoothDevice.BOND_NONE);   //前一次的配对状态
                int state = intent.getIntExtra(BluetoothDevice.EXTRA_BOND_STATE, BluetoothDevice.BOND_NONE);                //当前的配对的状态
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);                           //配对的设备信息
                if (device == null) return;
                String name = device.getName();
                String addr = device.getAddress();
                LogUtil.d("stateAfte:" + stateAfte + "," + "state:" + state + "," + "name:" + name + "," + "addr:" + addr);
            }
        }
    };
}
