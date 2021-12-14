package io.dubai.common.utils;

import java.util.Calendar;
import java.util.Date;

public class DateUtil {

    public static String dayForWeek() {
        Calendar cal = Calendar.getInstance();
        String[] weekDays = {"7", "1", "2", "3", "4", "5", "6"};
        try {
            cal.setTime(new Date());
        } catch (Exception e) {
            e.printStackTrace();
        }
        int w = cal.get(Calendar.DAY_OF_WEEK) - 1;
        if (w < 0) w = 0;
        return weekDays[w];
    }

    public static void main(String[] args) {
        System.out.println(dayForWeek());
    }

}
