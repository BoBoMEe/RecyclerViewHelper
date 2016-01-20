package com.yaodu.drug.util;

import java.net.URLEncoder;

/**
 * Created by bobomee on 16/1/14.
 */
public class EncodeUtil {

    public static String urlEnodeUTF8(String str) {
        String result = str;
        try {
            result = URLEncoder.encode(str, "UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
}
