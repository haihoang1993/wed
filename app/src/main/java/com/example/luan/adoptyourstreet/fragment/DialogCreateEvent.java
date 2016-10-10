package com.example.luan.adoptyourstreet.fragment;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;

import com.example.luan.adoptyourstreet.R;

/**
 * Created by Hai on 12/08/2016.
 */
public abstract class DialogCreateEvent   {
    Dialog dialog;
    public void showDialog(Activity context){
        dialog=new Dialog(context);
        dialog.setContentView(R.layout.popup_event);
        ImageView img=(ImageView) dialog.findViewById(R.id.btnExitPopupEvent);
        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        Button btnClick=(Button) dialog.findViewById(R.id.btnClickOfPopupEvent);
        Button btnClick1=(Button) dialog.findViewById(R.id.btnOpenShoPopupEvent1);
        Button btnClick2=(Button) dialog.findViewById(R.id.btnOpenShoPopupEvent2);
        Button btnClick3=(Button) dialog.findViewById(R.id.btnOpenShoPopupEvent3);
        btnClick1.setOnClickListener(clickButonOpenShop);
        btnClick2.setOnClickListener(clickButonOpenShop);
        btnClick3.setOnClickListener(clickButonOpenShop);
        btnClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onclickCreateVent();
                dialog.dismiss();
            }
        });
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = (int) (context.getResources().getDisplayMetrics().widthPixels*0.90);
        lp.height = (int) (context.getResources().getDisplayMetrics().heightPixels*0.6);
        lp.gravity = Gravity.CENTER;
        dialog.getWindow().setAttributes(lp);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.show();
    }
    View.OnClickListener clickButonOpenShop=new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            dialog.dismiss();
            onclickOpenShop();
        }
    };
    public abstract void onclickOpenShop();
    public abstract void onclickCreateVent();

}