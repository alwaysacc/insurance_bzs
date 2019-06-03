package com.bzs.utils;

import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.util.ByteSource;
import sun.security.provider.MD5;

import java.security.MessageDigest;

public class MD5Utils {

	protected MD5Utils(){

	}

	private static final String SALT = "bzs";

	private static final String ALGORITH_NAME = "md5";

	private static final int HASH_ITERATIONS = 2;

	public static String encrypt(String pswd) {
		return new SimpleHash(ALGORITH_NAME, pswd, ByteSource.Util.bytes(SALT), HASH_ITERATIONS).toHex();
	}

	public static String encrypt(String username, String pswd) {
		return new SimpleHash(ALGORITH_NAME, pswd, ByteSource.Util.bytes(username.toLowerCase() + SALT),
				HASH_ITERATIONS).toHex();
	}
	public static String md5(String str){
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			// md.update(str.getBytes());
			//注意要保证在不同环境下MD5结果相同，则必须使用相同的字符编码
			//必须加上为digest()方法添加参数，str.getBytes("UTF-8")，否则加密会由于环境的不同产生不同的值
			byte b[] = md.digest(str.getBytes("UTF-8"));
			int i;
			StringBuffer buf = new StringBuffer("");
			for (int offset = 0; offset < b.length; offset++) {
				i = b[offset];
				if (i < 0)
					i += 256;
				if (i < 16)
					buf.append("0");
				buf.append(Integer.toHexString(i));
			}
			//32位加密
			System.out.println("MD5中加密项："+str);
			System.out.println("MD5中加密结果："+buf.toString());
			return buf.toString();
			// 16位的加密
			//return buf.toString().substring(8, 24);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
}
