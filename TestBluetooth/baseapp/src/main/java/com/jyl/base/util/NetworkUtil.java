package com.jyl.base.util;

import android.content.Context;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;

import static java.security.AccessController.getContext;

public class NetworkUtil {

	// 上网类型
	/** 没有网络 */
	public static final byte NETWORK_TYPE_INVALID = 0x0;
	/** wifi网络 */
	public static final byte NETWORK_TYPE_WIFI = 0x1;
	/** 3G和3G以上网络，或统称为快速网络 */
	public static final byte NETWORK_TYPE_3G = 0x2;
	/** 2G网络 */
	public static final byte NETWORK_TYPE_2G = 0x3;
	/**
	 * 从原快速网络剥离 4G网络
	 */
	public static final byte NETWORK_TYPE_4G = 0x4;
	public static final byte XIAO_MI_SHARE_PC_NETWORK = 9;

	/**
	 * 获取网络状态，wifi,3g,2g,无网络。
	 * 
	 * @param context
	 *            上下文
	 * @return byte 网络状态 {@link #NETWORK_TYPE_WIFI}, {@link #NETWORK_TYPE_3G},
	 *         {@link #NETWORK_TYPE_2G}, {@link #NETWORK_TYPE_INVALID}
	 */
	public static byte getNetWorkType(Context context) {
		byte mNetWorkType = NETWORK_TYPE_INVALID;
		if (context == null)
			return mNetWorkType;
		ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = manager.getActiveNetworkInfo();

		if (networkInfo != null && networkInfo.isConnected()) {
			int nType = networkInfo.getType();
			Log.e("NetworkUtil", nType + "");
			if (nType == ConnectivityManager.TYPE_WIFI) {
				mNetWorkType = NETWORK_TYPE_WIFI;
			} else if (nType == ConnectivityManager.TYPE_MOBILE) {
				// String proxyHost =
				// android.net.Proxy.getDefaultHost();//TextUtils.isEmpty(proxyHost)=false为wap网络
//				mNetWorkType = (isFastMobileNetwork(context) ? NETWORK_TYPE_3G : NETWORK_TYPE_2G);
				//modify by @freeman 07-28
				mNetWorkType = getMobileNetworkType(context);
			} else if (nType == XIAO_MI_SHARE_PC_NETWORK) {
				mNetWorkType = NETWORK_TYPE_WIFI;
			}

		} else {
			mNetWorkType = NETWORK_TYPE_INVALID;

		}

		return mNetWorkType;
	}


	/**
	 * 检测网络是否可用
	 * 
	 * @return
	 */
	public static boolean isNetworkConnected(Context context) {
		ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo ni = cm.getActiveNetworkInfo();
		return ni != null && ni.isConnectedOrConnecting();
	}

	/**
	 * 判断是2G网络还是3G以上网络 false:2G网络;true:3G以上网络
	 */
	private static boolean isFastMobileNetwork(Context context) {
		TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
		switch (telephonyManager.getNetworkType()) {
		case TelephonyManager.NETWORK_TYPE_UNKNOWN:// 0
			return false;
		case TelephonyManager.NETWORK_TYPE_GPRS:// 1
			return false; // ~ 100 kbps
		case TelephonyManager.NETWORK_TYPE_EDGE:// 2
			return false; // ~ 50-100 kbps
		case TelephonyManager.NETWORK_TYPE_UMTS:// 3
			return true; // ~ 400-7000 kbps
		case TelephonyManager.NETWORK_TYPE_CDMA:// 4
			return false; // ~ 14-64 kbps
		case TelephonyManager.NETWORK_TYPE_EVDO_0:// 5
			return true; // ~ 400-1000 kbps
		case TelephonyManager.NETWORK_TYPE_EVDO_A:// 6
			return true; // ~ 600-1400 kbps
		case TelephonyManager.NETWORK_TYPE_1xRTT:// 7
			return false; // ~ 50-100 kbps
		case TelephonyManager.NETWORK_TYPE_HSDPA:// 8
			return true; // ~ 2-14 Mbps
		case TelephonyManager.NETWORK_TYPE_HSUPA:// 9
			return true; // ~ 1-23 Mbps
		case TelephonyManager.NETWORK_TYPE_HSPA:// 10
			return true; // ~ 700-1700 kbps
		case TelephonyManager.NETWORK_TYPE_IDEN:// 11
			return false; // ~25 kbps
			// SDK4.0才支持以下接口
		case 12:// TelephonyManager.NETWORK_TYPE_EVDO_B://12
			return true; // ~ 5 Mbps
		case 13:// TelephonyManager.NETWORK_TYPE_LTE://13
			return true; // ~ 10+ Mbps
		case 14:// TelephonyManager.NETWORK_TYPE_EHRPD://14
			return true; // ~ 1-2 Mbps
		case 15:// TelephonyManager.NETWORK_TYPE_HSPAP://15
			return true; // ~ 10-20 Mbps
		default:
			return false;
		}
	}

	private static byte getMobileNetworkType (Context context) {
		TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
		switch (telephonyManager.getNetworkType()) {
			case TelephonyManager.NETWORK_TYPE_UNKNOWN:// 0
			case TelephonyManager.NETWORK_TYPE_GPRS:// 1
			// ~ 100 kbps
			case TelephonyManager.NETWORK_TYPE_EDGE:// 2
			// ~ 50-100 kbps
			case TelephonyManager.NETWORK_TYPE_CDMA:// 4
				 // ~ 14-64 kbps
			case TelephonyManager.NETWORK_TYPE_1xRTT:// 7
				// ~ 50-100 kbps
			case TelephonyManager.NETWORK_TYPE_IDEN:// 11
				 // ~25 kbps
			return NETWORK_TYPE_2G;

			case TelephonyManager.NETWORK_TYPE_UMTS:// 3
				 // ~ 400-7000 kbps
			case TelephonyManager.NETWORK_TYPE_EVDO_0:// 5
				 // ~ 400-1000 kbps
			case TelephonyManager.NETWORK_TYPE_EVDO_A:// 6
				// ~ 600-1400 kbps
			case TelephonyManager.NETWORK_TYPE_HSDPA:// 8
				// ~ 2-14 Mbps
			case TelephonyManager.NETWORK_TYPE_HSUPA:// 9
				// ~ 1-23 Mbps
			case TelephonyManager.NETWORK_TYPE_HSPA:// 10
				// ~ 700-1700 kbps
			case 12:// TelephonyManager.NETWORK_TYPE_EVDO_B://12
				 // ~ 5 Mbps
			case 14:// TelephonyManager.NETWORK_TYPE_EHRPD://14
				// ~ 1-2 Mbps
			case 15:// TelephonyManager.NETWORK_TYPE_HSPAP://15
				 // ~ 10-20 Mbps
			return NETWORK_TYPE_3G;

			// SDK4.0才支持以下接口
			case 13:// TelephonyManager.NETWORK_TYPE_LTE://13
				return NETWORK_TYPE_4G; // ~ 10+ Mbps
			default:
				return NETWORK_TYPE_2G;
		}
	}

	public static final String CTWAP = "ctwap";
	public static final String CMWAP = "cmwap";
	public static final String WAP_3G = "3gwap";
	public static final String UNIWAP = "uniwap";
	/** @Fields TYPE_NET_WORK_DISABLED : 网络不可用 */
	public static final int TYPE_NET_WORK_DISABLED = 0;
	/** @Fields TYPE_CM_CU_WAP : 移动联通wap10.0.0.172 */
	public static final int TYPE_CM_CU_WAP = 4;
	/** @Fields TYPE_CT_WAP : 电信wap 10.0.0.200 */
	public static final int TYPE_CT_WAP = 5;
	/** @Fields TYPE_OTHER_NET : 电信,移动,联通,wifi 等net网络 */
	public static final int TYPE_OTHER_NET = 6;
	public static Uri PREFERRED_APN_URI = Uri.parse("content://telephony/carriers/preferapn");

	/**
	 * 判断Network具体类型（联通移动wap，电信wap，其他net）
	 * 
	 * @param mContext
	 */
	public static int checkNetworkType(Context mContext) {
		try {
			final ConnectivityManager connectivityManager = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
			final NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
			if (networkInfo == null || !networkInfo.isAvailable()) {
				// 注意一：
				// NetworkInfo 为空或者不可以用的时候正常情况应该是当前没有可用网络，
				// 但是有些电信机器，仍可以正常联网，
				// 所以当成net网络处理依然尝试连接网络。
				// （然后在socket中捕捉异常，进行二次判断与用户提示）。
				Log.i("", "=====================>无网络");
				return TYPE_NET_WORK_DISABLED;
			} else {
				// NetworkInfo不为null开始判断是网络类型
				int netType = networkInfo.getType();
				if (netType == ConnectivityManager.TYPE_WIFI) {
					// wifi net处理
					Log.i("", "=====================>wifi网络");
					return TYPE_OTHER_NET;
				} else if (netType == ConnectivityManager.TYPE_MOBILE) {
					// 注意二：
					// 判断是否电信wap:
					// 不要通过getExtraInfo获取接入点名称来判断类型，
					// 因为通过目前电信多种机型测试发现接入点名称大都为#777或者null，
					// 电信机器wap接入点中要比移动联通wap接入点多设置一个用户名和密码,
					// 所以可以通过这个进行判断！
					final Cursor c = mContext.getContentResolver().query(PREFERRED_APN_URI, null, null, null, null);
					if (c != null) {
						c.moveToFirst();
						final String user = c.getString(c.getColumnIndex("com/chushang/store/user"));
						if (!TextUtils.isEmpty(user)) {
							Log.i("", "=====================>代理：" + c.getString(c.getColumnIndex("proxy")));
							if (user.startsWith(CTWAP)) {
								Log.i("", "=====================>电信wap网络");
								return TYPE_CT_WAP;
							}
						}
					}
					c.close();

					// 注意三：
					// 判断是移动联通wap:
					// 其实还有一种方法通过getString(c.getColumnIndex("proxy")获取代理ip
					// 来判断接入点，10.0.0.172就是移动联通wap，10.0.0.200就是电信wap，但在
					// 实际开发中并不是所有机器都能获取到接入点代理信息，例如魅族M9 （2.2）等...
					// 所以采用getExtraInfo获取接入点名字进行判断
					String netMode = networkInfo.getExtraInfo();
					Log.i("", "netMode ================== " + netMode);
					if (netMode != null) {
						// 通过apn名称判断是否是联通和移动wap
						netMode = netMode.toLowerCase();
						if (netMode.equals(CMWAP) || netMode.equals(WAP_3G) || netMode.equals(UNIWAP)) {
							Log.i("", "=====================>移动联通wap网络");
							return TYPE_CM_CU_WAP;
						}
					}
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			return TYPE_OTHER_NET;
		}
		return TYPE_OTHER_NET;
	}

	public static void setWifiEnable(Context context,boolean state){
		//首先，用Context通过getSystemService获取wifimanager
		WifiManager mWifiManager = (WifiManager)
				context.getSystemService(Context.WIFI_SERVICE);
		//调用WifiManager的setWifiEnabled方法设置wifi的打开或者关闭，只需把下面的state改为布尔值即可（true:打开 false:关闭）
		mWifiManager.setWifiEnabled(state);
	}
}
