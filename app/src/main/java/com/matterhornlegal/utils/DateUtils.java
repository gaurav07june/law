package com.matterhornlegal.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by karan.kalsi on 10/31/2017.
 */

public  class DateUtils {
    private DateUtils()
    {

    }
    public static final long getRemainingMillis(String dateString)
    {
        long remaining_time=0;
        try {
            SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
            Date date = format.parse(dateString);
            remaining_time=date.getTime()-System.currentTimeMillis();
            if (remaining_time<0)return 0;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return remaining_time;

    }
}
