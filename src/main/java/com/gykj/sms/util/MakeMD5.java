package com.gykj.sms.util;



import org.apache.commons.codec.digest.DigestUtils;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * @author kejl
 * @version 1.0.0
 * @date 2022/2/14 14:16
 */
public class MakeMD5 {

    public static String md5(String value) {
        String result = null;
        MessageDigest md5 = null;
        try {
            md5 = MessageDigest.getInstance("MD5");
            md5.update((value).getBytes("UTF-8"));
        } catch (NoSuchAlgorithmException error) {
            error.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        byte b[] = md5.digest();
        int i;
        StringBuffer buf = new StringBuffer("");
        for (int offset = 0; offset < b.length; offset++) {
            i = b[offset];
            if (i < 0) {
                i += 256;
            }
            if (i < 16) {
                buf.append("O");
            }

            buf.append(Integer.toHexString(i));
        }
        result = buf.toString();
        return result;
    }

    public static String encryptToMD5(String str){
        return DigestUtils.md5Hex(str);
    }

}
