package com.example.luan.adoptyourstreet.adapters;

import android.content.Context;
import android.text.Html;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.luan.adoptyourstreet.R;
import com.example.luan.adoptyourstreet.models.ObjDemo;

import java.util.ArrayList;

/**
 * Created by Hai on 07/07/2016.
 */
public class Comment_Adapter extends ArrayAdapter<ObjDemo> {
    Context context;
    ArrayList<ObjDemo> list;
    public Comment_Adapter(Context context, int resource, ArrayList<ObjDemo> list) {
        super(context, resource, list);
        this.list=list;
        this.context=context;
    }

    @Override
    public View getView(int position, final View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View rowView= inflater.inflate(R.layout.item_coment, null, true);
        TextView tv=(TextView) rowView.findViewById(R.id.tvComment) ;
        Spanned htmlAsSpanned = Html.fromHtml("<b> Funkoi </b> "+"feeeeeeeeeeeeeeeeeeeeejdffffjeduuuuuuuuueuuuuuuuuuuu");

        tv.setText(htmlAsSpanned);

        return rowView;
    }
}

