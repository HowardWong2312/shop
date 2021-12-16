package io.dubai.common.utils;


import com.alibaba.fastjson.JSONObject;

import java.net.URLEncoder;
import java.security.MessageDigest;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Pattern;

/**
 * Created bymother fucker on 2018/11/23
 */

public class Utils {

    public static String getRandomString(int length) {
        String base = "abcdefghijklmnopqrstuvwxyz0123456789";
        Random random = new Random();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < length; i++) {
            int number = random.nextInt(base.length());
            sb.append(base.charAt(number));
        }
        return sb.toString();
    }

    /**
     * md5加密
     */
    public final static String MD5(String s) {
        char hexDigits[]={'0','1','2','3','4','5','6','7','8','9','A','B','C','D','E','F'};
        try {
            byte[] btInput = s.getBytes("UTF-8");
            MessageDigest mdInst = MessageDigest.getInstance("MD5");
            mdInst.update(btInput);
            byte[] md = mdInst.digest();
            int j = md.length;
            char str[] = new char[j * 2];
            int k = 0;
            for (int i = 0; i < j; i++) {
                byte byte0 = md[i];
                str[k++] = hexDigits[byte0 >>> 4 & 0xf];
                str[k++] = hexDigits[byte0 & 0xf];
            }
            return new String(str);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 转为XML格式
     */
    public static String ArrayToXml(Map<String, String> arr) {
        String xml = "<xml>";
        Iterator<Map.Entry<String, String>> iter = arr.entrySet().iterator();
        while (iter.hasNext()) {
            Map.Entry<String, String> entry = iter.next();
            String key = entry.getKey();
            String val = entry.getValue();
            if (IsNumeric(val)) {
                xml += "<" + key + "><![CDATA[" + val + "]]></" + key + ">";
            } else
                xml += "<" + key + "><![CDATA[" + val + "]]></" + key + ">";
        }
        xml += "</xml>";
        return xml;
    }


    public static boolean IsNumeric(String str) {
        if (str.matches("\\d *")) {
            return true;
        } else {
            return false;
        }
    }

    private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");

    public static String createOrderNum() {
        StringBuffer sb = new StringBuffer();
        sb.append(sdf.format(new Date()));
        Random random = new Random();
        for (int i = 0; i < 10; i++) {
            sb.append(random.nextInt(10));
        }
        return sb.toString();
    }

    public static boolean isNumeric(String str){

        Pattern pattern = Pattern.compile("(?!^0+\\.0{0,2}$)(^\\d+$|^\\d+\\.\\d{2}$)");

        return null != str && pattern.matcher(str).matches();

    }

    public static boolean isJSONObject(String str){
        try{
            JSONObject.parseObject(str);
            return true;
        }catch (Exception e){
            return false;
        }
    }
    public static boolean isJSONArray(String str){
        try{
            JSONObject.parseObject(str);
            return true;
        }catch (Exception e){
            return false;
        }
    }

    public static String getPayCustomSign(Map<String, String> bizObj,String key) throws Exception {
        String bizString = FormatBizQueryParaMap(bizObj, false);
        return sign(bizString, key);
    }

    public static String FormatBizQueryParaMap(Map<String, String> paraMap,
                                               boolean urlencode) throws Exception {
        String buff = "";
        try {
            List<Map.Entry<String, String>> infoIds = new ArrayList<Map.Entry<String, String>>(paraMap.entrySet());
            Collections.sort(infoIds,
                    (o1, o2) -> (o1.getKey()).toString().compareTo(
                            o2.getKey()));
            for (int i = 0; i < infoIds.size(); i++) {
                Map.Entry<String, String> item = infoIds.get(i);
                if (item.getKey() != "") {
                    String key = item.getKey();
                    String val = item.getValue();
                    if (urlencode) {
                        val = URLEncoder.encode(val, "utf-8");
                    }
                    buff += key + "=" + val + "&";
                }
            }
            if (buff.isEmpty() == false) {
                buff = buff.substring(0, buff.length() - 1);
            }
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
        return buff;
    }

    public static String sign(String content, String key){
        String signStr = content + "&key=" + key;
        System.out.println("签名数据---"+signStr);
        return MD5(signStr).toUpperCase();
    }

}
