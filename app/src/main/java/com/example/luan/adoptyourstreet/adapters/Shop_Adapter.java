package com.example.luan.adoptyourstreet.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.example.luan.adoptyourstreet.R;
import com.example.luan.adoptyourstreet.activities.EventShowCaseActivity;
import com.example.luan.adoptyourstreet.models.ObjDemo;
import com.example.luan.adoptyourstreet.models.ShopObj;

import java.util.ArrayList;

/**
 * Created by Hai on 07/07/2016.
 */
public class Shop_Adapter extends ArrayAdapter<ShopObj> {
    Context context;
    ArrayList<ShopObj> list;
    public Shop_Adapter(Context context, int resource, ArrayList<ShopObj> list) {
        super(context, resource, list);
        this.list=list;
        this.context=context;
    }

    @Override
    public View getView(int position, final View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View rowView= inflater.inflate(R.layout.item_shop, null, true);
        TextView tv=(TextView) rowView.findViewById(R.id.tvNameItemShop) ;
        tv.setText(list.get(position).getName());
        TextView tv2=(TextView) rowView.findViewById(R.id.tvDesItemShop) ;
        tv2.setText("$"+list.get(position).getMoney());
        return rowView;
    }
}

