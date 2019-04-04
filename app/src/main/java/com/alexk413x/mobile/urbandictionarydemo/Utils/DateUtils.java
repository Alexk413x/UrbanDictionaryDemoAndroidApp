package com.alexk413x.mobile.urbandictionarydemo.Utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class DateUtils {

    public Calendar createDateFromString(String dateString){
        //Assuming UrbanDictionary converts all their dates to the same timezone and its this one
        //Assuming date is required and verified on input
        SimpleDateFormat sdf = new SimpleDateFormat("YYYY-MM-DD'T'HH:mm:ss.SSS'Z'", Locale.ENGLISH);
        Calendar calendar = Calendar.getInstance();
        try {
            Date date = sdf.parse(dateString);
            calendar.setTime(date);

        } catch (ParseException e) {
            e.printStackTrace();
            calendar.setTime(new Date(0));
        }

        return calendar;
    }

}
