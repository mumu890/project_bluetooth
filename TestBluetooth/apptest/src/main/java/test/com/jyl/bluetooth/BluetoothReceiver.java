package test.com.jyl.bluetooth;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Parcelable;

import org.xutils.common.util.LogUtil;

/**
 * desc:
 * last modified time:2018/1/20 11:40
 *
 * @author yulin.jing
 * @since 2018/1/20
 */
public class BluetoothReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        // 蓝牙开关状态的广播
        if(action.equals(BluetoothAdapter.ACTION_STATE_CHANGED)) {
            LogUtil.d("BluetoothAdapter.ACTION_STATE_CHANGED");
            // int STATE_OFF = 10;          //蓝牙关闭
            // int STATE_ON = 12;           //蓝牙打开
            // int STATE_TURNING_OFF = 13;  //蓝牙正在关闭
            // int STATE_TURNING_ON = 11;   //蓝牙正在打开
            int state = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE,BluetoothAdapter.STATE_OFF);
            LogUtil.d("state:"+state);
        // 扫描开始
        } else if (action.equals(BluetoothAdapter.ACTION_DISCOVERY_STARTED)){
            LogUtil.d("BluetoothAdapter.ACTION_DISCOVERY_STARTED");
        // 扫描结束
        } else if (action.equals(BluetoothAdapter.ACTION_DISCOVERY_FINISHED)){
            LogUtil.d("BluetoothAdapter.ACTION_DISCOVERY_FINISHED");
        // 蓝牙连接状态的广播
        } else if (action.equals(BluetoothAdapter.ACTION_CONNECTION_STATE_CHANGED)){
            LogUtil.d("BluetoothAdapter.ACTION_CONNECTION_STATE_CHANGED ");
            int stateAfte = intent.getIntExtra(BluetoothAdapter.EXTRA_PREVIOUS_CONNECTION_STATE, BluetoothAdapter.ERROR);   //前一次的连接状态
            int state = intent.getIntExtra(BluetoothAdapter.EXTRA_CONNECTION_STATE, BluetoothAdapter.ERROR);                //当前的连接状态
            BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);                               //连接的设备信息
            if(device == null)return;
            String name = device.getName();
            String addr = device.getAddress();
            LogUtil.d("stateAfte:"+stateAfte + ","+ "state:"+state + ","+"name:"+ name +","+"addr:"+addr);
        // 蓝牙设备的名字变化的广播(当前设备)
        } else if (action.equals(BluetoothAdapter.ACTION_LOCAL_NAME_CHANGED)){
            LogUtil.d("BluetoothAdapter.ACTION_LOCAL_NAME_CHANGED ");
            BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
            if(device == null)return;
            String name = device.getName();
            String addr = device.getAddress();
            LogUtil.d("name:"+name + ","+ "address:"+addr);
        // 蓝牙设备的名字变化的广播（远程设备）
        } else if (action.equals(BluetoothDevice.ACTION_NAME_CHANGED)){
            LogUtil.d("BluetoothDevice.ACTION_NAME_CHANGED  ");
            BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
            if(device == null)return;
            String name = device.getName();
            String addr = device.getAddress();
            LogUtil.d("name:"+name + ","+ "address:"+addr);
        // 扫描到设备
        } else if (action.equals(BluetoothDevice.ACTION_FOUND)){
            LogUtil.d("BluetoothDevice.ACTION_FOUND");
            // 获取设备相关的信息
            BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
            if(device == null)return;
            String name = device.getName();
            String addr = device.getAddress();
            LogUtil.d("name:"+name + ","+ "address:"+addr);
        // 收到设备地UUID广播
        } else if (action.equals(BluetoothDevice.ACTION_UUID)){
            LogUtil.d("BluetoothDevice.ACTION_UUID");
            // 获取设备的UUID
            Parcelable[] uuidExtra = intent.getParcelableArrayExtra(BluetoothDevice.EXTRA_UUID);
            for (int i=0; i<uuidExtra.length; i++) {
                String uuid = uuidExtra[i].toString();
                LogUtil.d("uuid:"+uuid);
            }
        // 有蓝牙设备的配对请求时，会监听到这个广播
        } else if (action.equals(BluetoothDevice.ACTION_PAIRING_REQUEST )){
            LogUtil.d("BluetoothDevice.ACTION_PAIRING_REQUEST");
            // 获取设备的UUID

        // 蓝牙配对状态的广播
        } else if (action.equals(BluetoothDevice.ACTION_BOND_STATE_CHANGED)) {
            LogUtil.d("BluetoothDevice.ACTION_BOND_STATE_CHANGED");
            // int BOND_NONE = 10;      //配对没有成功
            // int BOND_BONDING = 11;   //配对中
            // int BOND_BONDED = 12;    //配对成功
            int stateAfte = intent.getIntExtra(BluetoothDevice.EXTRA_PREVIOUS_BOND_STATE, BluetoothDevice.BOND_NONE);   //前一次的配对状态
            int state = intent.getIntExtra(BluetoothDevice.EXTRA_BOND_STATE, BluetoothDevice.BOND_NONE);                //当前的配对的状态
            BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);                           //配对的设备信息
            if(device == null)return;
            String name = device.getName();
            String addr = device.getAddress();
            LogUtil.d("stateAfte:" + stateAfte + "," + "state:" + state + "," + "name:" + name + "," + "addr:" + addr);
        }
    }
}
