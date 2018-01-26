package lzlz.boardgame.util;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory; 
import javax.crypto.spec.DESKeySpec; 
import javax.crypto.spec.IvParameterSpec;
/**
 * 字符串加密解密
 * createBy lzlz at 2018/1/26 9:32
 * @author : lzlz
 */
public class Encryption {
	private static final String KEY ="a3f3bc6d";
	/**
	 * 字符串解密
	 * @throws Exception 解密失败
	 */
	public static String decode(String message) throws Exception {
		try{
			return decrypt(message,KEY);
		}catch(Exception ex){
			throw new Exception("\""+message+"\"解密失败");
		}
	}
	/**
	 * 字符串加密
	 * @throws Exception 加密失败
	 */
	public static String encode(String message) throws Exception {
		try {
			byte[] tmp = encrypt(message,KEY);
			StringBuilder stringBuilder = new StringBuilder();
			for(byte p : tmp){
				StringBuilder s = new StringBuilder(Integer.toHexString(p));//生成的16进制字符串有可能为1位或者负数
				for(int i=s.length();i<2;i++)//在少于2位的字符串前面补充0
					s.insert(0, "0");
				s = new StringBuilder(s.substring(s.length() - 2));//多于2位的字符串意味着前面有负号 去除
				stringBuilder.append(s);
			}
			return stringBuilder.toString().toUpperCase();
		} catch (Exception e) {
			throw new Exception("\""+message+"\"加密失败");
		}
	}
	
	private static String decrypt(String message,String key) throws Exception {
		byte[] bytesrc = convert2HexString(message);
		Cipher cipher = Cipher.getInstance("DES/CBC/PKCS5Padding"); 
		DESKeySpec desKeySpec = new DESKeySpec(key.getBytes("UTF-8")); 
		SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES"); 
		SecretKey secretKey = keyFactory.generateSecret(desKeySpec); 
		IvParameterSpec iv = new IvParameterSpec(key.getBytes("UTF-8"));
		cipher.init(Cipher.DECRYPT_MODE, secretKey, iv);
		byte[] retByte = cipher.doFinal(bytesrc); 
		return new String(retByte); 
	}

	private static byte[] encrypt(String message, String key) 
			throws Exception { 
		Cipher cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
		DESKeySpec desKeySpec = new DESKeySpec(key.getBytes("UTF-8"));
		SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES"); 
		SecretKey secretKey = keyFactory.generateSecret(desKeySpec); 
		IvParameterSpec iv = new IvParameterSpec(key.getBytes("UTF-8")); 
		cipher.init(Cipher.ENCRYPT_MODE, secretKey, iv);
		return cipher.doFinal(message.getBytes("UTF-8")); 
	}

	//把加密byte数组转换为16进制字符串
	private static byte[] convert2HexString(String ss)
	{ 
		byte digest[] = new byte[ss.length() / 2]; 
		for(int i = 0; i < digest.length; i++) 
		{ 
			String byteString = ss.substring(2 * i, 2 * i + 2); 
			int byteValue = Integer.parseInt(byteString, 16); 
			digest[i] = (byte)byteValue; 
		}
		return digest; 
	}
}