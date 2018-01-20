package com.jyl.base.util;

import android.annotation.SuppressLint;

import org.xutils.common.util.LogUtil;

import java.io.FileInputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MD5 {
	private static final char HEX_DIGITS[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
        'A', 'B', 'C', 'D', 'E', 'F' };

	public static String toHexString(byte[] b) {
		 StringBuilder sb = new StringBuilder(b.length * 2);
		 for (int i = 0; i < b.length; i++) {  
		     sb.append(HEX_DIGITS[(b[i] & 0xf0) >>> 4]);  
		     sb.append(HEX_DIGITS[b[i] & 0x0f]);  
		 }  
		 return sb.toString();  
	}


	/**
	 * 获取文件的MD5值
	 * @param filename
	 * @return
	 */
	@SuppressLint("DefaultLocale")
	public static String getFileMD5(String filename) {
		LogUtil.d("getFileMD5 filename:"+filename);
		InputStream fis;
		byte[] buffer = new byte[1024];
		int numRead = 0;
		MessageDigest md5;
		try{
			fis = new FileInputStream(filename);
			md5 = MessageDigest.getInstance("MD5");
			while((numRead=fis.read(buffer)) > 0) {
				md5.update(buffer,0,numRead);
			}
			fis.close();
			return (toHexString(md5.digest())).toLowerCase();	
		} catch (Exception e) {
			System.out.println("error");
			return null;
		}
	}

	/**
	 * 将字符串转成MD5值
	 *
	 * @param string
	 * @return
	 */
	public static String getStringMD5(String string) {
		byte[] hash;

		try {
			hash = MessageDigest.getInstance("MD5").digest(string.getBytes("UTF-8"));
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			return null;
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return null;
		}

		StringBuilder hex = new StringBuilder(hash.length * 2);
		for (byte b : hash) {
			if ((b & 0xFF) < 0x10)
				hex.append("0");
			hex.append(Integer.toHexString(b & 0xFF));
		}

		return hex.toString();
	}
}

