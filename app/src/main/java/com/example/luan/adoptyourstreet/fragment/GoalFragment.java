package com.example.luan.adoptyourstreet.fragment;

import android.annotation.TargetApi;
import android.app.Dialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.luan.adoptyourstreet.R;
import com.google.android.gms.vision.text.Text;
import com.p_v.flexiblecalendar.FlexibleCalendarView;
import com.p_v.flexiblecalendar.entity.Event;
import com.p_v.flexiblecalendar.view.BaseCellView;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog.OnDateChangedListener;
import com.wdullaer.materialdatetimepicker.time.RadialPickerLayout;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;


import org.angmarch.views.UtilCalendar.UtilCalendar;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

/**
 * Created by Hai on 05/07/2016.
 */

public class GoalFragment extends Fragment {
    private TextView tvSelectWeek;

    public GoalFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_goal, container, false);
        tvSelectWeek = (TextView) rootView.findViewById(R.id.tvSelectWeek);
        final String date[] = UtilCalendar.getAllDateOfWeek(-1, -1, -1);
        tvSelectWeek.setText(date[0] + " - " + date[6]);
        tvSelectWeek.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupDate();
            }
        });

        return rootView;
    }
    private void setTextDate(){
        final String date[] = UtilCalendar.getAllDateOfWeek(yearClick, monthClick, dayClick);
        tvSelectWeek.setText(date[0] + " - " + date[6]);
    }
    int dayClick, monthClick, yearClick;
    Date[] arrDayofWeek=new Date[7];
    public void popupDate() {
        final Dialog dialog = new Dialog(getActivity());
        // Include dialog.xml file
        dialog.setContentView(R.layout.pick_date_layout);
        // Set dialog title
        dialog.setTitle("Custom Dialog");
        final FlexibleCalendarView calendarView = (FlexibleCalendarView) dialog.findViewById(R.id.month_view);
        final TextView tv=(TextView) dialog.findViewById(R.id.tvDialogDate);
        Calendar c=Calendar.getInstance();
        Date d=c.getTime();
        final String date[] = UtilCalendar.getAllDateOfWeek(d.getYear(), d.getMonth(), d.getDate());
        tv.setText(date[0] + " - " + date[6]);
        final Button bt=(Button) dialog.findViewById(R.id.btDialogDate);
        dialog.show();
        calendarView.setEventDataProvider(new FlexibleCalendarView.EventDataProvider() {
            @Override
            public List<? extends Event> getEventsForTheDay(int year, int month, int day) {
                Date[]   arrDayofWeek=  UtilCalendar.getAllDateOfWeeoToCalen(yearClick,monthClick,dayClick);
                for(int i=0;i<7;i++) {
                    if ((day ==arrDayofWeek[i].getDate())&&(month==arrDayofWeek[i].getMonth())) {
                        Log.d("ffff:",arrDayofWeek[i].getDate()+"");
                        List<CustomEvent> colorLst1 = new ArrayList<>();
                        colorLst1.add(new CustomEvent(android.R.color.holo_green_dark));
                        colorLst1.add(new CustomEvent(android.R.color.holo_blue_light));
                        colorLst1.add(new CustomEvent(android.R.color.holo_purple));
                        return colorLst1;
                    }
                }
                return null;
            }
        });
        calendarView.setOnDateClickListener(new FlexibleCalendarView.OnDateClickListener() {
            @Override
            public void onDateClick(int year, int month, int day) {

       //         Toast.makeText(dialog.getContext(),day+"-"+month+"-"+year,Toast.LENGTH_SHORT).show();
                dayClick = day;
                monthClick = month;
                yearClick=year;

                arrDayofWeek=UtilCalendar.getAllDateOfWeeoToCalen(yearClick,monthClick,dayClick);

                tv.setText(UtilCalendar.toStringFromDate(arrDayofWeek[0])+" - " +UtilCalendar.toStringFromDate(arrDayofWeek[6]));
                calendarView.refresh();
                calendarView.setEventDataProvider(new FlexibleCalendarView.EventDataProvider() {
                    @Override
                    public List<? extends Event> getEventsForTheDay(int year, int month, int day) {
                        Date[]   arrDayofWeek=  UtilCalendar.getAllDateOfWeeoToCalen(yearClick,monthClick,dayClick);
                        for(int i=0;i<7;i++) {
                            if ((day ==arrDayofWeek[i].getDate())&&(month==arrDayofWeek[i].getMonth())) {
                                Log.d("ffff:",arrDayofWeek[i].getDate()+"");
                                List<CustomEvent> colorLst1 = new ArrayList<>();
                                colorLst1.add(new CustomEvent(android.R.color.holo_green_dark));
                                colorLst1.add(new CustomEvent(android.R.color.holo_blue_light));
                                colorLst1.add(new CustomEvent(android.R.color.holo_purple));
                                return colorLst1;
                            }
                        }
                        return null;
                    }
                });
            }
        });
        calendarView.setCalendarView(new FlexibleCalendarView.CalendarView() {
            @Override
            public BaseCellView getCellView(int position, View convertView, ViewGroup parent, int cellType) {
                BaseCellView cellView = (BaseCellView) convertView;
                if (cellView == null) {
                    LayoutInflater inflater = LayoutInflater.from(dialog.getContext());
                    cellView = (BaseCellView) inflater.inflate(R.layout.calendar2_date_cell_view, null);
                }
                if (cellType == BaseCellView.TODAY) {
                    cellView.setTextColor(getResources().getColor(android.R.color.holo_red_dark));
                    cellView.setTextSize(15);
                } else {
                    cellView.setTextColor(getResources().getColor(android.R.color.white));
                    cellView.setTextSize(12);
                }
                return cellView;
            }

            @Override
            public BaseCellView getWeekdayCellView(int position, View convertView, ViewGroup parent) {
                BaseCellView cellView = (BaseCellView) convertView;
                if (cellView == null) {
                    LayoutInflater inflater = LayoutInflater.from(dialog.getContext());
                    cellView = (BaseCellView) inflater.inflate(R.layout.calendar2_week_cell_view, null);
                }
                return cellView;
            }

            @Override
            public String getDayOfWeekDisplayValue(int dayOfWeek, String defaultValue) {
                return null;
            }
        });

        calendarView.setEventDataProvider(new FlexibleCalendarView.EventDataProvider() {
            @Override
            public List<? extends Event> getEventsForTheDay(int year, int month, int day) {
                Date[]   arrDayofWeek=  UtilCalendar.getAllDateOfWeeoToCalen(yearClick,monthClick,dayClick);
                for(int i=0;i<7;i++) {
                    if ((day ==arrDayofWeek[i].getDate())&&(month==arrDayofWeek[i].getMonth())) {
                        Log.d("ffff:",arrDayofWeek[i].getDate()+"");
                        List<CustomEvent> colorLst1 = new ArrayList<>();
                        colorLst1.add(new CustomEvent(android.R.color.holo_green_dark));
                        colorLst1.add(new CustomEvent(android.R.color.holo_blue_light));
                        colorLst1.add(new CustomEvent(android.R.color.holo_purple));
                        return colorLst1;
                    }
                }
                return null;
            }
        });
        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setTextDate();
                dialog.hide();
            }
        });

    }

    public class CustomEvent implements Event {

        private int color;

        public CustomEvent(int color) {
            this.color = color;
        }

        @Override
        public int getColor() {
            return color;
        }
    }

    //    DatePickerDialog.OnDateSetListener datePickSetData=new DatePickerDialog.OnDateSetListener() {
//        @Override
//        public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
//            Calendar date = Calendar.getInstance();
//            date.set(Calendar.HOUR_OF_DAY, 9);
//            date.set(Calendar.MINUTE, 0);
//            date.set(Calendar.YEAR, year);
//            date.set(Calendar.MONTH, monthOfYear);
//            date.set(Calendar.DAY_OF_MONTH, dayOfMonth);
//            view.setSelectableDays(getAllDateOfWeeoToCalen(year,monthOfYear,dayOfMonth));
//        }
//    };
//    DatePickerDialog.OnDateSetListener datePickGetData=new DatePickerDialog.OnDateSetListener() {
//        @Override
//        public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
//            String date[] = getAllDateOfWeek(year,monthOfYear,dayOfMonth);
//            tvSelectWeek.setText(date[0]+" - " +date[6]);
//
//        }
//    };



//    @Override
//    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
//        String date = ""+dayOfMonth+"/"+(++monthOfYear)+"/"+year;
//        tvSelectWeek.setText(date);
//    }


}
