package com.example.luan.adoptyourstreet.adapters;

import android.content.Context;
import android.content.Intent;
import android.database.Observable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.luan.adoptyourstreet.R;
import com.example.luan.adoptyourstreet.Util.Util;
import com.example.luan.adoptyourstreet.activities.EventShowCaseActivity;
import com.example.luan.adoptyourstreet.models.Event;
import com.example.luan.adoptyourstreet.models.ObjDemo;

import java.util.ArrayList;

/**
 * Created by Hai on 07/07/2016.
 */
public abstract class Event_Adapter extends ArrayAdapter<Event> {
    Context context;
    ArrayList<Event> list;
    public Event_Adapter(Context context, int resource, ArrayList<Event> list) {
        super(context, resource, list);
        this.list=list;
        this.context=context;
    }

    @Override
    public View getView(int position, final View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View rowView= inflater.inflate(R.layout.item_list_event, null, true);
        TextView tv=(TextView) rowView.findViewById(R.id.tvNameItemEvent) ;
        tv.setText(list.get(position).name);

        TextView tvTimeEnd=(TextView) rowView.findViewById(R.id.tvTimeEndImtemEvent) ;
        tvTimeEnd.setText(list.get(position).getStringTimeFire());

        TextView tvCountUser=(TextView) rowView.findViewById(R.id.tvItemEventCountUser);
        tvCountUser.setText(list.get(position).getCountUserOfEven()+ " people");
        ImageView bt=(ImageView) rowView.findViewById(R.id.btnItemEvent);
        bt.setTag(new Integer(position));

        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
             //  context.startActivity(new Intent(context, EventShowCaseActivity.class));
                ClickBuy();
            }
        });
        return rowView;
    }
    public abstract void ClickBuy();
    public abstract int demo(int a);
}

