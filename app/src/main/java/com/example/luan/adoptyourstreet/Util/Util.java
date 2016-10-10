package com.example.luan.adoptyourstreet.Util;

        import android.util.Log;
        import android.widget.ListView;

        import com.example.luan.adoptyourstreet.models.WeekActivitie;

        import org.angmarch.views.UtilCalendar.UtilCalendar;

        import java.sql.Time;
        import java.text.SimpleDateFormat;
        import java.util.ArrayList;
        import java.util.Calendar;
        import java.util.Date;

/**
 * Created by Hai on 25/08/2016.
 */
public class Util {

  // get time reset 0 hour,0 mintue, 0 s..
    public static Calendar getInstaceResetDate(Calendar c){
        c.set(Calendar.HOUR_OF_DAY,0);
        c.set(Calendar.SECOND,0);
        c.set(Calendar.MINUTE,0);
        c.set(Calendar.MILLISECOND,0);
        return c;
    }

    public static int checkHasWeekActivitie(ArrayList<WeekActivitie> listWeek, Calendar time){
        Date date= UtilCalendar.getMondayOfWeek(time.get(Calendar.YEAR),time.get(Calendar.MONTH),time.get(Calendar.DAY_OF_MONTH));
        time.setTime(date);
        time.set(Calendar.MILLISECOND,0);
        int re=-1;
        if(listWeek.size()==0) return  re;
        for (int i=0;i<listWeek.size();i++){
            Calendar c=Calendar.getInstance();
            if((-listWeek.get(i).startDate)==time.getTimeInMillis()){
                re=i;
                break;
            }
        }
        return re;
    }

    public static String toStringHeaderWeeek(long time){
        Calendar c=Calendar.getInstance();
        c.setTimeInMillis(time);
        Date[] arrWeek=UtilCalendar.getAllDateOfWeeoToCalen(c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH));
        SimpleDateFormat fr=new SimpleDateFormat("dd/MM");
        return fr.format(arrWeek[0])+" - "+fr.format(arrWeek[6]);
    }

    public static boolean DayInWeek(Calendar curentTime,WeekActivitie week){
        Date date= UtilCalendar.getMondayOfWeek(curentTime.get(Calendar.YEAR),curentTime.get(Calendar.MONTH),curentTime.get(Calendar.DAY_OF_MONTH));
        curentTime.setTime(date);
        curentTime.set(Calendar.MILLISECOND,0);
        curentTime.set(Calendar.HOUR_OF_DAY,0);
        curentTime.set(Calendar.MINUTE,0);
        curentTime.set(Calendar.SECOND,0);
        if (curentTime.getTimeInMillis()==week.startDate)return true;
        return false;
    }
    public static String toStringDateActivity(long longdate){
        Date date=new Date();
        date.setTime(longdate);
        Date now=Calendar.getInstance().getTime();
        String print="";
        if((now.getMonth()==date.getMonth())&&(now.getYear()==date.getYear())){
            if(now.getDate()==date.getDate()){
                return  "Today";
            }
            if((now.getDate())==(date.getDate()+1)){
                return "Yesterday";
            }
        }
        SimpleDateFormat ft = new SimpleDateFormat ("dd/MM/yyyy");

        return ft.format(date);
    }


}
