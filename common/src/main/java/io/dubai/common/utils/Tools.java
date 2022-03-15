package io.dubai.common.utils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.Iterator;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * Created bymother fucker on 2017/10/24
 */

public class Tools {

    private static Logger logger = LoggerFactory.getLogger(Tools.class);

    public static boolean isPhone(String phone) {
        String regex = "^((13[0-9])|(14[5,7,9])|(15([0-3]|[5-9]))|(166)|(17[0,1,3,5,6,7,8])|(18[0-9])|(19[8|9]))\\d{8}$";
        if (phone == null || phone.length() != 11) {
            return false;
        } else {
            Pattern p = Pattern.compile(regex);
            return p.matcher(phone).matches();
        }
    }

    public static String getIpAddr(HttpServletRequest request) {
        String ip = null;
        try {
            ip = request.getHeader("x-forwarded-for");
            if (org.apache.commons.lang.StringUtils.isEmpty(ip) || "unknown".equalsIgnoreCase(ip)) {
                ip = request.getHeader("Proxy-Client-IP");
            }
            if (org.apache.commons.lang.StringUtils.isEmpty(ip) || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
                ip = request.getHeader("WL-Proxy-Client-IP");
            }
            if (org.apache.commons.lang.StringUtils.isEmpty(ip) || "unknown".equalsIgnoreCase(ip)) {
                ip = request.getHeader("HTTP_CLIENT_IP");
            }
            if (org.apache.commons.lang.StringUtils.isEmpty(ip) || "unknown".equalsIgnoreCase(ip)) {
                ip = request.getHeader("HTTP_X_FORWARDED_FOR");
            }
            if (StringUtils.isEmpty(ip) || "unknown".equalsIgnoreCase(ip)) {
                ip = request.getRemoteAddr();
            }
        } catch (Exception e) {
            logger.error("IPUtils ERROR ", e);
        }

//        //使用代理，则获取第一个IP地址
//        if(StringUtils.isEmpty(ip) && ip.length() > 15) {
//			if(ip.indexOf(",") > 0) {
//				ip = ip.substring(0, ip.indexOf(","));
//			}
//		}

        return ip;
    }

    public static boolean isZero(BigDecimal amount) {
        try{
            if(amount == null){
                return true;
            }
            if(amount.compareTo(BigDecimal.ZERO) < 1){
                return true;
            }
            return false;
        }catch (Exception e){
            return true;
        }
    }

    public static boolean isJson(String str) {
        try{
            if(StringUtils.isEmpty(str)){
               return false;
            }
            JSONObject json = JSONObject.parseObject(str);
            if(json == null){
                return false;
            }
            return true;
        }catch (Exception e){
            return false;
        }
    }


    /**
     * json转xml
     * 	自动添加一个根元素 <info></info>
     *
     * @param jo JSONObject对象
     * @param root 转换的xml根元素 默认info   传入 null  or String 字符串
     *
     * @return xml Str字符串
     */
    public String jsonObjToXml(JSONObject jo,String root){
        JSONObject jo0 = new JSONObject();
        jo0.put( root == null ? "info": root, jo);
        String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"+ jsontoxml(jo0, "");
        return xml;
    }

    /**
     * json对象转xml
     *
     * @param jo JSONObject
     * @param gt "\n" 位移符
     *
     * @return XML Str字符串
     *
     */
    @SuppressWarnings("rawtypes")
    public static String jsontoxml(JSONObject jo, String gt) {
        StringBuilder xmlStr = new StringBuilder();
        try {
            Iterator iter = jo.entrySet().iterator();
            while (iter.hasNext()) {
                Map.Entry entry = (Map.Entry) iter.next();
                String key = entry.getKey().toString();
                String val = entry.getValue().toString();
                if (val.substring(0, 1).equals("{")) {
                    xmlStr.append(gt);
                    xmlStr.append("<");
                    xmlStr.append(key);
                    xmlStr.append(">\n");
                    xmlStr.append(jsontoxml(JSONObject.parseObject(val), gt + "\t"));
                    xmlStr.append(gt);
                    xmlStr.append("</");
                    xmlStr.append(key);
                    xmlStr.append(">\n");
                } else if (val.substring(0, 1).equals("[")) {
                    xmlStr.append(gt);
                    xmlStr.append("<");
                    xmlStr.append(key);
                    xmlStr.append(">\n");
                    JSONArray ja = JSONArray.parseArray(val);
                    for (int i = 0; i < ja.size(); i++) {
                        xmlStr.append(jsontoxml(ja.getJSONObject(i), gt + "\t"));
                    }
                    xmlStr.append(gt);
                    xmlStr.append("</");
                    xmlStr.append(key);
                    xmlStr.append(">\n");
                } else {
                    xmlStr.append(gt);
                    xmlStr.append("<");
                    xmlStr.append(key);
                    xmlStr.append(">");
                    xmlStr.append(val);
                    xmlStr.append("</");
                    xmlStr.append(key);
                    xmlStr.append(">\n");
                }
            }
        } catch (Exception e) {
            logger.info("jo=" + jo);
            e.printStackTrace();
            return "<error>1</error>";
        }

        return xmlStr.toString();
    }




    public static String HMACSHA256(String data, String key) throws Exception {

        Mac sha256_HMAC = Mac.getInstance("HmacSHA256");

        SecretKeySpec secret_key = new SecretKeySpec(key.getBytes("UTF-8"), "HmacSHA256");

        sha256_HMAC.init(secret_key);

        byte[] array = sha256_HMAC.doFinal(data.getBytes("UTF-8"));

        StringBuilder sb = new StringBuilder();

        for (byte item : array) {

            sb.append(Integer.toHexString((item & 0xFF) | 0x100).substring(1, 3));

        }

        return sb.toString().toUpperCase();

    }

}
