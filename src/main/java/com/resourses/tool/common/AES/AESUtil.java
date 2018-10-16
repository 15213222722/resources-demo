package com.resourses.tool.common.AES;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;


/**
 * 加密和解密算法
 *
 * @author dgh
 */
public class AESUtil {

    private static final String PASSWORD = "1234567890";


    /**
     * 获取解密后的字符串
     *
     * @param content
     * @return
     * @throws BadPaddingException
     * @throws IllegalBlockSizeException
     * @throws NoSuchPaddingException
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeyException
     * @throws UnsupportedEncodingException 
     */
    public static String RevertAESCode(String content) throws InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException, UnsupportedEncodingException {
        byte[] decryptFrom = parseHexStr2Byte(content);
        byte[] decryptResult = decrypt(decryptFrom, PASSWORD);
        String decryptString = new String(decryptResult,"UTF-8");
        return decryptString;
    }

    /**
     * 获取解密后的字符串
     *
     * @param content
     * @param passcode
     * @return
     * @throws BadPaddingException
     * @throws IllegalBlockSizeException
     * @throws NoSuchPaddingException
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeyException
     */
    public static String RevertAESCode(String content, String passcode) throws InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException {
        byte[] decryptFrom = parseHexStr2Byte(content);
        byte[] decryptResult = decrypt(decryptFrom, passcode);
        String decryptString = new String(decryptResult);
        return decryptString;
    }

    /**
     * 获取加密后的字符串
     *
     * @param content
     * @return
     * @throws NoSuchPaddingException
     * @throws NoSuchAlgorithmException
     * @throws BadPaddingException
     * @throws IllegalBlockSizeException
     * @throws UnsupportedEncodingException
     * @throws InvalidKeyException
     */
    public static String GetAESCode(String content) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, UnsupportedEncodingException, IllegalBlockSizeException, BadPaddingException {
        byte[] encryptResult = encrypt(content, PASSWORD);
        String encryptResultStr = parseByte2HexStr(encryptResult);
        return encryptResultStr;
    }

    /**
     * 获取加密后的字符串
     *
     * @param content
     * @param passcode
     * @return
     * @throws NoSuchPaddingException
     * @throws NoSuchAlgorithmException
     * @throws BadPaddingException
     * @throws IllegalBlockSizeException
     * @throws UnsupportedEncodingException
     * @throws InvalidKeyException
     */
    public static String GetAESCode(String content, String passcode) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, UnsupportedEncodingException, IllegalBlockSizeException, BadPaddingException {
        byte[] encryptResult = encrypt(content, passcode);
        String encryptResultStr = parseByte2HexStr(encryptResult);
        return encryptResultStr;
    }


    /**
     * 加密
     *
     * @param content
     * @param password
     * @return
     * @throws NoSuchAlgorithmException
     * @throws NoSuchPaddingException
     * @throws UnsupportedEncodingException
     * @throws InvalidKeyException
     * @throws BadPaddingException
     * @throws IllegalBlockSizeException
     */
    private static byte[] encrypt(String content, String password) throws NoSuchAlgorithmException, NoSuchPaddingException, UnsupportedEncodingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        KeyGenerator kgen = KeyGenerator.getInstance("AES");
        //防止linux下 随机生成key
        SecureRandom secureRandom = SecureRandom.getInstance("SHA1PRNG");
        secureRandom.setSeed(password.getBytes());
        kgen.init(128, secureRandom);
        SecretKey secretKey = kgen.generateKey();
        byte[] enCodeFormat = secretKey.getEncoded();
        SecretKeySpec key = new SecretKeySpec(enCodeFormat, "AES");
        /**创建密码器**/
        Cipher cipher = Cipher.getInstance("AES");
        byte[] byteContent = content.getBytes("UTF-8");
        /**初始化密码器**/
        cipher.init(Cipher.ENCRYPT_MODE, key);
        byte[] result = cipher.doFinal(byteContent);
        return result;
    }

    /**
     * 解密
     *
     * @param content
     * @param password
     * @return
     * @throws NoSuchAlgorithmException
     * @throws NoSuchPaddingException
     * @throws InvalidKeyException
     * @throws BadPaddingException
     * @throws IllegalBlockSizeException
     */
    private static byte[] decrypt(byte[] content, String password) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        KeyGenerator kgen = KeyGenerator.
                getInstance("AES");
        //防止linux下 随机生成key
        SecureRandom secureRandom = SecureRandom.getInstance("SHA1PRNG");
        secureRandom.setSeed(password.getBytes());
        kgen.init(128, secureRandom);
        SecretKey secretKey = kgen.generateKey();
        byte[] enCodeFormat = secretKey.getEncoded();
        SecretKeySpec key = new SecretKeySpec(enCodeFormat, "AES");
        /**创建密码器**/
        Cipher cipher = Cipher.getInstance("AES");
        /**初始化密码器**/
        cipher.init(Cipher.DECRYPT_MODE, key);
        byte[] result = cipher.doFinal(content);
        return result;
    }

    /**
     * 将二进制转换成十六进制
     *
     * @param buf
     * @return
     */
    private static String parseByte2HexStr(byte buf[]) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < buf.length; i++) {
            String hex = Integer.toHexString(buf[i] & 0xFF);
            if (hex.length() == 1) {
                hex = '0' + hex;
            }
            sb.append(hex.toUpperCase());
        }
        return sb.toString();
    }

    /**
     * 将十六进制转换为二进制
     *
     * @param hexStr
     * @return
     */
    private static byte[] parseHexStr2Byte(String hexStr) {
        if (hexStr.length() < 1) {
            return null;
        } else {
            byte[] result = new byte[hexStr.length() / 2];
            for (int i = 0; i < hexStr.length() / 2; i++) {
                int high = Integer.parseInt(hexStr.substring(i * 2, i * 2 + 1), 16);
                int low = Integer.parseInt(hexStr.substring(i * 2 + 1, i * 2 + 2), 16);
                result[i] = (byte) (high * 16 + low);
            }
            return result;
        }
    }

//    public static void main(String[] args) {
//        String passcode = "ebb808789ac74491accc28c86ad39679";
//        String content = "7DDB9A4980EF58FDD581E769CA361EEDFCBC5839D76E1E04A68A710FDA5907E19E5E80F5484D5B24759F9DBF2C1091B49537755C876F995CF8FAE26726CA37478A614F08CEB87851B3AF675837391BA45D568ADB4E89099124E38CBF2960E5DD9E59B88A07B90E66FCECCF8E04D263BE2FBF13BD8569FA16537F7F782610EB00469FF2EBE6EA2E67AC6C52713A1B81308AA2EF7455185501B007EC67C623BDBCC47D47F85CEF61E05FC28724021D721390C23073C2A38AA224A5EA73D5045220DEF4C1F44EDB2E577F15BF96DF7CF523885522DC8B1BEF6DA833D2C332C7BB16";
//        try {
//            System.out.println(RevertAESCode(content, passcode));
//        } catch (InvalidKeyException | NoSuchAlgorithmException | NoSuchPaddingException
//                | IllegalBlockSizeException | BadPaddingException e) {
//            e.printStackTrace();
//        }
//    }
    
    public static void main(String[] args) throws Exception {
		// 跳转
		String a = AESUtil.GetAESCode(
				"{\"payOrderNo\":\"20191131105809706823\",\"orderPrice\":\"1\",\"CustomParam\":\"56556566\","
				+ "\"productName\":\"我是李勇\",\"productDesc\":\"我是李勇\",\"productURL\":\"http://baiddu.com\","
				+ "\"returnUrl\":\"http://baidu.com\","
				+ "\"payment\":\"222\",\"signValidityPeriod\":\"2d\",\"OriOrderDate\":\"20180926\","
				+ "\"loginID\":\"123\",\"requestFromUrl\":\"http://baidu.com\",\"ContractCode\":\"00852018092610320001100018000000\"}",
				"ebb808789ac74491accc28c86ad39679");
		//退款
		String b = AESUtil.GetAESCode("{" + 
				"	\"payOrderNo\":\"20191131105809706818\"," + 
				"	\"refundFee\":\"1\"," + 
				"	\"refundOrderNo\":\"56585987458515845695215658595497\"," + 
				"	\"PayTrans\":\"00852018092515253071860533056655\"," + 
				"	\"refundReason\":\"退款\"" + 
				"}","ebb808789ac74491accc28c86ad39679");
		//解约
		String c = AESUtil.GetAESCode(
				"{\"payOrderNo\":\"20191131105809706808\",\"orderPrice\":\"1\",\"CustomParam\":\"56556566\","
				+ "\"productName\":\"我是李勇\",\"productDesc\":\"我是李勇\",\"productURL\":\"http://baiddu.com\","
				+ "\"returnUrl\":\"https://stageandmu.reservehemu.com:8143/order/app/cloudPackage/cde010000003/b4b9e6f9e82f3fa814b5351129f78be1\","
				+ "\"ContractTerminationRemark\":\"你长得太丑\",\"signValidityPeriod\":\"2d\",\"OriOrderDate\":\"20180925\","
				+ "\"loginID\":\"123\",\"ContractCode\":\"00852018092515220001800018000000\"}",
				"ebb808789ac74491accc28c86ad39679");
		
		System.out.println("加密ssis：--" + a);
		System.out.println("加密ssis：--" + b);
		System.out.println("加密ssis：--" + c);

	}
}