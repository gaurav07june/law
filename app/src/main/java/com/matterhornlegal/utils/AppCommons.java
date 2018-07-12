package com.matterhornlegal.utils;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;
import android.provider.CalendarContract;

import com.bumptech.glide.util.Util;
import com.matterhornlegal.R;
import com.matterhornlegal.models.EventData;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Created by karan.kalsi on 5/28/2018.
 */

public class AppCommons {
    private AppCommons()
    {

    }

    public static final String getMonthFullDayYear(Context context,String webDateS)
    {


        try {
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);
            Date webDate = df.parse(webDateS);
            Calendar webCalendar = Calendar.getInstance(Locale.US);
            webCalendar.setTime(webDate);

            return getMonthNameFull(context,webCalendar.get(Calendar.MONTH))+" "+
                    webCalendar.get(Calendar.DAY_OF_MONTH)+","+
                    webCalendar.get(Calendar.YEAR);


        } catch (Exception e) {

        }
        return "";
    }


    public static final String getMonthName(Context context, int monthIndex) {
        switch (monthIndex) {
            case 0:
                return context.getString(R.string.jan);
            case 1:
                return context.getString(R.string.feb);
            case 2:
                return context.getString(R.string.mar);
            case 3:
                return context.getString(R.string.apr);
            case 4:
                return context.getString(R.string.may);
            case 5:
                return context.getString(R.string.jun);
            case 6:
                return context.getString(R.string.jul);
            case 7:
                return context.getString(R.string.aug);
            case 8:
                return context.getString(R.string.sep);
            case 9:
                return context.getString(R.string.oct);
            case 10:
                return context.getString(R.string.nov);
            case 11:
                return context.getString(R.string.dec);
            default:
                return "";
        }
    }

    public static final String getMonthNameFull(Context context, int monthIndex) {
        switch (monthIndex) {
            case 0:
                return context.getString(R.string.jan_full);
            case 1:
                return context.getString(R.string.feb_full);
            case 2:
                return context.getString(R.string.mar_full);
            case 3:
                return context.getString(R.string.apr_full);
            case 4:
                return context.getString(R.string.may_full);
            case 5:
                return context.getString(R.string.jun_full);
            case 6:
                return context.getString(R.string.jul_full);
            case 7:
                return context.getString(R.string.aug_full);
            case 8:
                return context.getString(R.string.sep_full);
            case 9:
                return context.getString(R.string.oct_full);
            case 10:
                return context.getString(R.string.nov_full);
            case 11:
                return context.getString(R.string.dec_full);
            default:
                return "";
        }
    }

    public static final void addEventToDefaultCalendar(Context context, EventData eventData)
    {
        ContentResolver cr = context.getContentResolver();
        ContentValues values = new ContentValues();

        values.put(CalendarContract.Events.DTSTART, Utils.getDate(eventData.getEvent_start_date()).getTime());
        values.put(CalendarContract.Events.DTEND, Utils.getDate(eventData.getEvent_end_date()).getTime());
        values.put(CalendarContract.Events.TITLE, eventData.getEvent_title());
        values.put(CalendarContract.Events.DESCRIPTION, eventData.getEvent_venue());
        TimeZone timeZone = TimeZone.getDefault();
        values.put(CalendarContract.Events.EVENT_TIMEZONE, timeZone.getID());
        values.put(CalendarContract.Events.CALENDAR_ID, 1);

        values.put(CalendarContract.Events.RRULE, "FREQ=DAILY;");
        values.put(CalendarContract.Events.HAS_ALARM, 1);

// Insert event to calendar
        Uri uri = cr.insert(CalendarContract.Events.CONTENT_URI, values);
    }
}
