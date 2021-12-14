package io.dubai.common.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;
import java.util.regex.Pattern;

public class StringUtils extends org.springframework.util.StringUtils {

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

    public static String getRandomString(int length){
        String str="abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        Random random=new Random();
        StringBuffer sb=new StringBuffer();
        for(int i=0;i<length;i++){
            int number=random.nextInt(62);
            sb.append(str.charAt(number));
        }
        return sb.toString();
    }

    public static boolean isNumeric(String str){

        Pattern pattern = Pattern.compile("(?!^0+\\.0{0,2}$)(^\\d+$|^\\d+\\.\\d{2}$)");

        return null != str && pattern.matcher(str).matches();

    }

    public static void main(String[] args) {
        for (int i = 0; i <100; i++) {
            System.out.println(createOrderNum());
        }
    }
}
