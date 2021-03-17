package com.bytecub.utils;

import sun.misc.BASE64Encoder;

import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Map;

/**
 * 生成摘要工具类
 */
public class DigestUtil {
	public static final String GBK="GBK";
	public static final String UTF8="UTF-8";

	/**
	 * base64
	 * @param md5
	 * @return
	 * @throws Exception
	 */
	public static String encryptBASE64(byte[] md5) throws Exception {
		return (new BASE64Encoder()).encodeBuffer(md5);
	}

	/**
	 * MD5
	 * @param data
	 * @return
	 * @throws Exception
	 */
	public static byte[] encryptMD5(byte[] data) throws Exception {
		MessageDigest md5 = MessageDigest.getInstance("MD5");
		md5.update(data);
		return md5.digest();
	}

	/**
	 * 摘要生成
	 * @param data 请求数据
	 * @param sign 签名秘钥(key或者parternID)
	 * @param charset 编码格式
	 * @return 摘要
	 */
	public static String digest(String data,String sign,String charset) {
		try {
			String t = encryptBASE64(encryptMD5((data + sign).getBytes(charset)));
			return t.trim();
		}catch (Exception ex){
			ex.getMessage();
		}
		return null;
	}

	/**
	 * 日志系统加密方式
	 * @param params
	 * @param secret
	 * @return
	 * @throws IOException
	 * @throws NoSuchAlgorithmException
	 */
	public static String signTopRequestNew(Map<String, String> params, String secret) throws IOException, NoSuchAlgorithmException {
		// 第一步：检查参数是否已经排序
		String[] keys = params.keySet().toArray(new String[0]);
		Arrays.sort(keys);

		// 第二步：把所有参数名和参数值串在一起
		StringBuilder query = new StringBuilder(secret);
		for (String key : keys) {
			query.append(key).append(params.get(key));
		}
		query.append(secret);

		// 第三步：进行MD5
		MessageDigest mdTemp = MessageDigest.getInstance("MD5");
		byte[] md5Bytes = mdTemp.digest(query.toString().getBytes("UTF-8"));

		// 第四步：转换成16进制
		return bytesToHexString(md5Bytes);
	}

	private static String bytesToHexString(byte[] src) {
		try {
			StringBuilder stringBuilder = new StringBuilder("");
			if (src == null || src.length <= 0) {
				return null;
			}
			for (int i = 0; i < src.length; i++) {
				int v = src[i] & 0xFF;
				String hv = Integer.toHexString(v);
				if (hv.length() < 2) {
					stringBuilder.append(0);
				}
				stringBuilder.append(hv);
			}
			return stringBuilder.toString();
		} catch (Exception e) {
			return null;
		}
	}



}