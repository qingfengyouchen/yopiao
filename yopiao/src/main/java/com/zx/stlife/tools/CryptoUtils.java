package com.zx.stlife.tools;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * 
 * @ClassName: CryptoUtils
 * @Description: TODO(加密方法工具类)
 * @date 2015-4-7 下午2:03:04
 * 
 */
public final class CryptoUtils {

	private static final CryptoUtils singletonObject = new CryptoUtils();

	private CryptoUtils() {

	}

	public static CryptoUtils getInstance() {
		return singletonObject;
	}

	/**
	 * 对目标进行AES-128方式加密
	 * 
	 * @param value
	 * @param privateKey
	 *            长度必须为16字节
	 * @return
	 * @throws NoSuchPaddingException
	 * @throws NoSuchAlgorithmException
	 * @throws InvalidKeyException
	 * @throws BadPaddingException
	 * @throws IllegalBlockSizeException
	 * @throws Exception
	 */
	public static String encryptAES(String value, String privateKey) throws NoSuchAlgorithmException,
			NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
		byte[] raw = privateKey.getBytes();
		SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
		Cipher cipher = Cipher.getInstance("AES");
		cipher.init(Cipher.ENCRYPT_MODE, skeySpec);
		return CryptoUtils.byteToHexString(cipher.doFinal(value.getBytes()));
	}

	/**
	 * 对目标进行AES-128方式解密
	 * 
	 * @param value
	 * @param privateKey
	 *            长度必须为16字节
	 * @return
	 * @throws NoSuchPaddingException
	 * @throws NoSuchAlgorithmException
	 * @throws InvalidKeyException
	 * @throws BadPaddingException
	 * @throws IllegalBlockSizeException
	 * @throws Exception
	 */
	public static String decryptAES(String value, String privateKey) throws NoSuchAlgorithmException,
			NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
		byte[] raw = privateKey.getBytes();
		SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
		Cipher cipher = Cipher.getInstance("AES");
		cipher.init(Cipher.DECRYPT_MODE, skeySpec);
		return new String(cipher.doFinal(CryptoUtils.hexStringToByte(value)));
	}

	/**
	 * MD5散列码 散列结果被转化成16进制的字符串
	 * 
	 * @param value
	 * @return
	 * @throws NoSuchAlgorithmException
	 * @throws UnsupportedEncodingException
	 * @throws Exception
	 */
	public static String hexMD5(String value) throws NoSuchAlgorithmException, UnsupportedEncodingException {
		MessageDigest messageDigest = MessageDigest.getInstance("MD5");
		messageDigest.reset();
		messageDigest.update(value.getBytes("utf-8"));
		byte[] digest = messageDigest.digest();
		return byteToHexString(digest);
	}

	/**
	 * SHA-1散列码
	 * 
	 * @param value
	 * @return
	 * @throws NoSuchAlgorithmException
	 * @throws UnsupportedEncodingException
	 * @throws Exception
	 */
	public static String hexSHA1(String value) throws NoSuchAlgorithmException, UnsupportedEncodingException {
		MessageDigest md;
		md = MessageDigest.getInstance("SHA-1");
		byte[] digest = new byte[40];
		md.update(value.getBytes("utf-8"));
		digest = md.digest();
		return byteToHexString(digest);

	}

	/**
	 * 字节转为16进制串
	 * 
	 * @param bytes
	 * @return
	 */
	public static String byteToHexString(byte[] bytes) {
		StringBuilder builder = new StringBuilder();
		for (int i = 0; i < bytes.length; ++i) {
			int v = bytes[i];
			if (v < 0) {
				v += 256;
			}
			String n = Integer.toHexString(v);
			if (n.length() == 1)
				n = "0" + n;
			builder.append(n);
		}

		return builder.toString();
	}

	/**
	 * 16进制串转为字节
	 * 
	 * @param hexString
	 * @return
	 */
	public static byte[] hexStringToByte(String hexString) {
		if (hexString == null || hexString.equals("")) {
			return null;
		}
		hexString = hexString.toUpperCase();
		int length = hexString.length() / 2;
		char[] hexChars = hexString.toCharArray();
		byte[] d = new byte[length];
		for (int i = 0; i < length; i++) {
			int pos = i * 2;
			d[i] = (byte) (charToByte(hexChars[pos]) << 4 | charToByte(hexChars[pos + 1]));
		}
		return d;
	}

	/**
	 * Convert char to byte
	 */
	private static byte charToByte(char c) {
		return (byte) "0123456789ABCDEF".indexOf(c);
	}

}
