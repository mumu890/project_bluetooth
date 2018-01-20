package test.com.jyl.bluetooth;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
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
//    private final String MAC_ADDRESS = "00:1B:10:00:2A:EC";
    private final String MAC_ADDRESS = "74:AC:5F:E2:7E:5B";
    private final UUID MY_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    private BluetoothAdapter bluetoothAdapter = null;
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
//        bluetoothAdapter.startDiscovery();  // 扫描设备
//        bluetoothAdapter.cancelDiscovery(); // 取消扫描
//        bluetoothAdapter.getState();           // 获取本地蓝牙适配器当前状态（感觉可能调试的时候更需要）
//        bluetoothAdapter.listenUsingRfcommWithServiceRecord(String name,UUID uuid);  //根据名称，UUID创建并返回BluetoothServerSocket，这是创建BluetoothSocket服务器端的第一步

        /* 	功能：以给定的MAC地址去创建一个 BluetoothDevice 类实例(代表远程蓝牙实例)。即使该蓝牙地址不可见，也会产生
        	一个BluetoothDevice 类实例。
    		返回：BluetoothDevice 类实例 。注意，如果该蓝牙设备MAC地址不能被识别，其蓝牙Name为null。
        	异常：如果MAC  address无效，抛出IllegalArgumentException。*/
        device = bluetoothAdapter.getRemoteDevice(MAC_ADDRESS);  // 根据蓝牙地址获取远程蓝牙设备
        if(device != null)
            new ClientThread().start();
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

//    /**
//     　　　* 服务端，接收连接的线程
//     * @author Administrator
//     *
//     */
//    class ServiceThread extends Thread{
//        @Override
//        public void run(){
//            try {
//                //先用本地蓝牙适配器创建一个serversocket *
//                serSocket= bluetoothAdapter.listenUsingRfcommWithServiceRecord(bluetoothAdapter.getName(), UUID.fromString("00001101-0000-1000-8000-00805F9B34FB"));
//                Utils.sonUiStateMsg("正在等待连接");
//                if(socket!=null){Utils.sonUiStateMsg("连接成功");}
//                //等待连接，该方法阻塞*
//                socket=serSocket.accept();
//                Utils.sonUiStateMsg("连接成功");
//                new ReadMsg().start();
//            } catch (IOException e) {
//                // TODO Auto-generated catch block
////                Toast.makeText(getApplicationContext(), "IOExeption", 1).show();
//                System.out.println("IOExeption");
//                Utils.sonUiStateMsg("连接失败");
//                e.printStackTrace();
//            }
//
//        }
//    }

//    //2、10、16进制转换
//    public String ConvertRadix(String value,String radixMode) {
//        if (TextUtils.isEmpty(value) || value == "0" || value == "1")
//            return value;
//        String rs ="";
//        switch (radixMode){
//            case "210": rs = Convert.ToInt32(value, 2).ToString();
//                break;
//            case "216": rs = Convert.ToString(Convert.ToInt32(value, 2), 16);
//                break;
//            case "102": rs = Convert.ToString(int.Parse(value), 2).ToString();
//                break;
//            case "1016": rs = Convert.ToString(int.Parse(value), 16);
//                break;
//            case "162": rs = Convert.ToString(Convert.ToInt32(value, 16), 2);
//                break;
//            case "1610": rs = Convert.ToInt32(value, 16).ToString();
//                break;
//        }
//        return rs;
//    }
//
//    //字符转为16进制ASCII码 eg:门锁S转为53
//    public static String StrToHexAscii(String str) {
//        StringBuilder StringBuilder = new StringBuilder(str.length() * 2);
//        for (int i = 0; i < str.length(); i++)
//            StringBuilder.append(((int)str[i]).ToString("X2"));
//        return StringBuilder.toString();
//    }
//
//    /// <summary>
//    /// 字符串转16进制字节数组
//    /// </summary>
//    /// <param name="src">十六进制字符串</param>
//    /// <returns></returns>
//    public static byte[] HexString2Bytes(String src) {
//        if (null == src || 0 == src.length()) return null;
//        src = src.Replace(" ", "").ToUpper();
//        byte[] ret = new byte[src.Length / 2];
//        for (int i = 0; i < (src.Length / 2); i++)
//            ret[i] = Convert.ToByte(src.SubString(i * 2, 2), 16); //(byte)Int32.Parse(src.SubString(i * 2, 2), System.Globalization.NumberStyles.HexNumber);
//        return ret;
//    }
}
