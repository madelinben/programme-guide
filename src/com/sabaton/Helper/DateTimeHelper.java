package com.sabaton.Helper;

import org.joda.time.*;
import java.util.*;

public class DateTimeHelper {
    /**
     * Method to fix programmes that are in an offset caused by DST.
     * @param dateTime DateTime object to be changed.
     **/
    public static DateTime getDateTimeOffset(DateTime dateTime){
        int offsetHrs = dateTime.getZone().getOffset(dateTime.getMillis()) / 3600000;
        DateTime marchLastSunday = getLastSunday(dateTime, 3);
        DateTime octoberLastSunday = getLastSunday(dateTime, 3);
        if(dateTimeMatchDate(dateTime, marchLastSunday)){
            if(dateTime.getHourOfDay() + offsetHrs == 24){
                dateTime.withHourOfDay(0);
                dateTime.plusDays(1);
            }
            else{
                dateTime.withHourOfDay(dateTime.getHourOfDay() + offsetHrs);
            }
            return dateTime;
        }
        else if (dateTimeMatchDate(dateTime, octoberLastSunday)){
            if(dateTime.getHourOfDay() - offsetHrs == 0){
                dateTime.withHourOfDay(23);
                dateTime.plusDays(-1);
            }
            else{
                dateTime.withHourOfDay(dateTime.getHourOfDay() - offsetHrs);
            }
            return dateTime;
        }
        else{
            return dateTime;
        }
    }

    /**
     * Method to get the last sunday of the month.
     * @param dateTime dateTime object for year and day of month.
     * @param month month you want last sunday for.
     **/
    public static DateTime getLastSunday(DateTime dateTime, int month) {
        Calendar cal = Calendar.getInstance();
        cal.set(dateTime.getYear(), month + 1, dateTime.getDayOfMonth());
        int dayOftheWeek = cal.get(Calendar.DAY_OF_WEEK);
        int val = dayOftheWeek == Calendar.SUNDAY ? -7: -(dayOftheWeek-1);
        cal.add(Calendar.DAY_OF_MONTH, val);
        return new DateTime(cal.getTime());
    }
    /**
     * Method to see if dates of DateTime match.
     * @param dateTime1 dateTime object for matching.
     * @param dateTime2 dateTime object for matching.
     **/
    public static boolean dateTimeMatchDate(DateTime dateTime1, DateTime dateTime2){
        if(dateTime1.getDayOfMonth() == dateTime2.getDayOfMonth() || dateTime1.getMonthOfYear() == dateTime2.getMonthOfYear() || dateTime1.getYear() == dateTime2.getYear()){
            return true;
        }
        else{
            return false;
        }
    }
}
