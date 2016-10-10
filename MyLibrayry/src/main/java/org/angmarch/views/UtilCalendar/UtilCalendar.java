package org.angmarch.views.UtilCalendar;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Created by Hai on 11/08/2016.
 */
public class UtilCalendar {

    public static boolean compareDateByLongDate(Long d1,Long d2){
        Date a1=new Date(d1);
        Date a2=new Date(d2);
        Calendar c1=Calendar.getInstance(),c2=Calendar.getInstance();
        c1.setTime(a1);c1.set(Calendar.MILLISECOND,0);
        c2.setTime(a2);c1.set(Calendar.MILLISECOND,0);
        if((a1.getDate()==a2.getDate())&&(a1.getMonth()==a2.getMonth())&&(a1.getYear()==a2.getYear())){
            return true;
        }
        return false;
    }

     public static String toStringFromDate(Date c){
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        return format.format(c);
    }

     public static Date getMondayOfWeek(int year, int monthOfYear, int dayOfMonth){
         Date temp[]=getAllDateOfWeeoToCalen(year,monthOfYear,dayOfMonth);
        temp[0].setHours(0);
        temp[0].setMinutes(0);
        temp[0].setSeconds(0);
        return temp[0];
    }

     public static String[] getAllDateOfWeek(int year, int monthOfYear, int dayOfMonth) {
        Calendar now = Calendar.getInstance();
        if ((year != -1) && (dayOfMonth != -1) && (dayOfMonth != -1))
            now.set(year, monthOfYear, dayOfMonth);
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        String[] days = new String[7];
        int delta = -now.get(GregorianCalendar.DAY_OF_WEEK) + 2; //add 2 if your week start on monday
        now.add(Calendar.DAY_OF_MONTH, delta);
        for (int i = 0; i < 7; i++) {
            days[i] = format.format(now.getTime());
            now.add(Calendar.DAY_OF_MONTH, 1);
        }
        return days;
    }

     public static Date getMondayOfWeek(Date date){
        Date temp[]=getAllDateOfWeeoToCalen(date.getYear(),date.getMonth(),date.getDate());
        return temp[0];
    }

     public static Date[] getAllDateOfWeeoToCalen(int year, int monthOfYear, int dayOfMonth) {
        Calendar now = Calendar.getInstance();
        Date arr[] = new Date[7];
        if ((year != -1) && (dayOfMonth != -1) && (dayOfMonth != -1))
            now.set(year, monthOfYear, dayOfMonth);
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        String[] days = new String[7];
        int delta = -now.get(GregorianCalendar.DAY_OF_WEEK) + 2; //add 2 if your week start on monday
        now.add(Calendar.DAY_OF_MONTH, delta);
        for (int i = 0; i < 7; i++) {
            arr[i] = now.getTime();
            now.add(Calendar.DAY_OF_MONTH, 1);
        }

        return arr;
    }

}
