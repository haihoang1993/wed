package com.example.luan.adoptyourstreet.adapters;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.TextView;

import com.example.luan.adoptyourstreet.R;
import com.example.luan.adoptyourstreet.Util.Util;
import com.example.luan.adoptyourstreet.models.DayActivitive;
import com.example.luan.adoptyourstreet.models.WeekActivitie;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Hai on 07/07/2016.
 */
public class ExpandableListAdapterActivity extends BaseExpandableListAdapter {
    private Context _context;
    private List<WeekActivitie> _listDataHeader; // header titles
    // child data in format of header title, child title
    private HashMap<String, List<DayActivitive>> _listDataChild;


    public ExpandableListAdapterActivity(Context context,List<WeekActivitie>  listWeek){
        _listDataChild=new HashMap<>();
        this._context = context;

        _listDataHeader=listWeek;
        if(_listDataHeader.size()>0)
        for(WeekActivitie arrW:_listDataHeader){
            _listDataChild.put(arrW.weekId, arrW.listDateOfWeek);
        }
    }

    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
        if(_listDataHeader.size()>0)
            for(WeekActivitie arrW:_listDataHeader){
                _listDataChild.put(arrW.weekId, arrW.listDateOfWeek);
            }
    }

    @Override
    public Object getChild(int groupPosition, int childPosititon) {
        return this._listDataChild.get(this._listDataHeader.get(groupPosition).weekId)
                .get(childPosititon);
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public View getChildView(int groupPosition, final int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {

        final DayActivitive childText = (DayActivitive) getChild(groupPosition, childPosition);

        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this._context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = infalInflater.inflate(R.layout.list_item_activity, null);

        }



        TextView txtListChild = (TextView) convertView
                .findViewById(R.id.tvItemDateActivity);
        txtListChild.setText(Util.toStringDateActivity(-childText.startDate)+"");
        TextView tvDuration= (TextView) convertView
                .findViewById(R.id.tvItemDurationActivity);
        float duration= ((float)childText.sumDuration()/60/60);
        duration=(float) Math.round(duration*100)/100;
        tvDuration.setText(duration+" h");
 TextView tvDistance= (TextView) convertView
                .findViewById(R.id.tvItemDitanceActivi);
        float dis= ((float)childText.sumDistance());
        dis=(float) Math.round(dis*100)/100;
        tvDistance.setText(dis+" m");
        return convertView;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return this._listDataChild.get(this._listDataHeader.get(groupPosition).weekId)
                .size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return this._listDataHeader.get(groupPosition);
    }

    @Override
    public int getGroupCount() {
        return this._listDataHeader.size();
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {

        ExpandableListView mExpandableListView = (ExpandableListView) parent;
        mExpandableListView.expandGroup(groupPosition);
        WeekActivitie  week= (WeekActivitie) getGroup(groupPosition);
        String headerTitle =week.weekId ;
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this._context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.item_group_activity, null);
        }

        TextView  tvHeadWeek= (TextView) convertView
                .findViewById(R.id.tvItemHeaderWeekActivity);
        tvHeadWeek.setText(Util.toStringHeaderWeeek(-week.startDate));


          TextView tvDuratio = (TextView) convertView
                        .findViewById(R.id.tvItemSumDurationActivi);
        float duration=((float)(week.sumDuration()))/60/60;
            duration=(float)Math.round(duration*100)/100;
            tvDuratio.setText(duration+" ");
        TextView tvMe = (TextView) convertView
                .findViewById(R.id.tvItemSumDistanceActivity);
        float distance=((float)week.sumDistance())/1000;
        distance=(float)Math.round(distance*100)/100;
        tvMe.setText(distance+" km");
        return convertView;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}

