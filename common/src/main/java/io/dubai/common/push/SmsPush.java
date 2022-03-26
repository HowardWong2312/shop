package io.dubai.common.push;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import io.dubai.common.utils.HttpUtils;
import io.dubai.common.utils.Tools;
import org.apache.commons.codec.digest.DigestUtils;
import org.json.XML;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

/**
 * Created by Howard on 2021/06/25
 */
public class SmsPush {

    private SmsPush(){

    }

    public static class SmsPushClass{
        private final static SmsPush instance = new SmsPush();
    }

    public static SmsPush getInstance(){
        return SmsPushClass.instance;
    }
    private Logger log = LoggerFactory.getLogger(getClass());

    private static final String msgEn = "The AShop verification code is {0} Valid for 5 mins";


    public boolean SendVodafoneSMS(String mobile,String msg) throws Exception {
        String x = String.valueOf(Math.random()*900000+100000) ;

        String url = "https://e3len.vodafone.com.eg/web2sms/sms/submit/";
        String key = "6D1D5CB8983D42DD81066C5B7031FA99";
        StringBuilder parameters = new StringBuilder();
        parameters.append("AccountId="+"200003281");
        parameters.append("&Password="+"Vodafone.1");
        parameters.append("&SenderName="+"TGC");
        parameters.append("&ReceiverMSISDN="+mobile);
        parameters.append("&SMSText="+msg);
        JSONObject json = new JSONObject();
        JSONObject sms = new JSONObject();
        JSONArray SMSList = new JSONArray();
        json.put("AccountId","200003281");
        json.put("Password","Vodafone.1");
        json.put("SecureHash",Tools.HMACSHA256(parameters.toString(),key));
        sms.put("SenderName","TGC");
        sms.put("ReceiverMSISDN",mobile);
        sms.put("SMSText",msg);
        SMSList.add(sms);
        json.put("SMSList",SMSList);
        StringBuilder xmlStr = new StringBuilder();
        xmlStr.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
        xmlStr.append("<SubmitSMSRequest xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:=\"http://www.edafa.com/web2sms/sms/model/\" xsi:schemaLocation=\"http://www.edafa.com/web2sms/sms/model/SMSAPI.xsd \" xsi:type=\"SubmitSMSRequest\">\n");
        xmlStr.append(Tools.jsontoxml(json,""));
        xmlStr.append("</SubmitSMSRequest>");
        String result = HttpUtils.sendPostXml(url,xmlStr.toString());
        log.info("vodafone短信发送,发送号码->{},返回结果->{}",mobile,result);
        if(result != null){
            String status = XML.toJSONObject(result).getJSONObject("SubmitSMSResponse").getString("ResultStatus");
            if("SUCCESS".equalsIgnoreCase(status)){
                return true;
            }
        }
        return false;
    }

    public String send(String countryCode,String phone) {
        try {
            String code = getRandNum(6);
            String msg = MessageFormat.format(msgEn, code);
            boolean flag = SendVodafoneSMS(phone,msg);
            if(flag){
                return code;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    public String getRandNum(int charCount) {
        String charValue = "";
        for (int i = 0; i < charCount; i++) {
            char c = (char) (randomInt(0, 10) + '0');
            charValue += String.valueOf(c);
        }
        return charValue;
    }

    public int randomInt(int from, int to) {
        Random r = new Random();
        return from + r.nextInt(to - from);
    }

    public static void main(String[] args) {
        System.out.println(SmsPush.getInstance().send("+971","0525363459"));
    }

}
