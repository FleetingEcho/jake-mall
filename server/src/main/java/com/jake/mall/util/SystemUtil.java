package com.jake.mall.util;

import java.math.BigInteger;
import java.security.MessageDigest;


public class SystemUtil {

    private SystemUtil() {
    }


    /**
     * After logging in or register, generate a user login status session token value
     *
     * @param src: When logging in for the user,regenerate token:  now()+user.id+random(4)
     * @return
     */
    public static String genToken(String src) {
        if (null == src || "".equals(src)) {
            return null;
        }
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(src.getBytes());
            String result = new BigInteger(1, md.digest()).toString(16);
            if (result.length() == 31) {
                result = result + "-";
            }
            System.out.println(result);
            return result;
        } catch (Exception e) {
            return null;
        }
    }

}
