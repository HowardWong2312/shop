package io.dubai.common.utils;

import org.apache.commons.codec.binary.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.security.Key;

/**
 * php与java通用AES加密解密算法
 * AES指高级加密标准（Advanced Encryption Standard）,是当前最流行的一种密码算法，在web应用开发，特别是对外提供接口时经常会用到，
 * 下面是整理的一套php与java通用的AES加密解密算法。
 * @author Administrator
 *
 */
public class CryptAES {
	
	private static final String AESTYPE ="AES/ECB/PKCS5Padding";
	private static final  String keyStr = "UITN25LMUQC436IM";
	 
	/**
	 * 加密方法
	 * @param plainText
	 * @return
	 */
    public static String AES_Encrypt(String plainText) {
        byte[] encrypt = null;
        try{
            Key key = generateKey(keyStr);
            Cipher cipher = Cipher.getInstance(AESTYPE);
            cipher.init(Cipher.ENCRYPT_MODE, key);
            encrypt = cipher.doFinal(plainText.getBytes());    
        }catch(Exception e){
            e.printStackTrace();
        }
        return new String(Base64.encodeBase64(encrypt));
    }
 
    /**
     * 解密方法
     * @param encryptData
     * @return
     */
    public static String AES_Decrypt(String encryptData) {
        byte[] decrypt = null;
        try{
            Key key = generateKey(keyStr);
            Cipher cipher = Cipher.getInstance(AESTYPE);
            cipher.init(Cipher.DECRYPT_MODE, key);
            decrypt = cipher.doFinal(Base64.decodeBase64(encryptData));
        }catch(Exception e){
            e.printStackTrace();
        }
        return new String(decrypt).trim();
    }
 
    private static Key generateKey(String key)throws Exception{
        try{           
            SecretKeySpec keySpec = new SecretKeySpec(key.getBytes(), "AES");
            return keySpec;
        }catch(Exception e){
            e.printStackTrace();
            throw e;
        }
 
    }


    public static void main(String[] args) throws Exception {
 
        String plainText = "hD8+mVGTHHkfpZapaLwhhw==";
////
        String encText = AES_Encrypt(plainText);
        String decString = AES_Decrypt(plainText);
////
        System.out.println(encText);
        System.out.println(decString);
//
//        BigDecimal bigDecimal = new BigDecimal(33);
//        BigDecimal bigDecimal1 = new BigDecimal(8);
//
//        System.out.println(bigDecimal);
//        System.out.println(bigDecimal1);
//        BigDecimal a = new BigDecimal("8").divide(new BigDecimal("33"),2, BigDecimal.ROUND_HALF_DOWN).multiply(new BigDecimal(100)) ;
//        System.out.println(a);




    } 

}
