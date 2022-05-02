package com.example.bigassignment;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;

import androidx.fragment.app.DialogFragment;

public class PopDate extends DialogFragment{
    View view;
    Button save_datepick,cancel_datepick;
    DatePicker dp;
    String time_get_date,date_get_date;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        view=inflater.inflate(R.layout.activity_pop_date, container, false);
        save_datepick=(Button)view.findViewById(R.id.date_save_picker);
        cancel_datepick=(Button)view.findViewById(R.id.date_cancel_picker);
        dp=(DatePicker)view.findViewById(R.id.dp);
        if (getArguments() != null && !TextUtils.isEmpty(getArguments().getString("time_value2")))
            time_get_date=getArguments().getString("time_value2");
        if (getArguments() != null && !TextUtils.isEmpty(getArguments().getString("date_value2")))
            date_get_date=getArguments().getString("date_value2");


        if(!date_get_date.equalsIgnoreCase("ignore")) {
            String[] date_arr = date_get_date.split("-", 3);

            int Year = Integer.parseInt(date_arr[2]);
            int Month = Integer.parseInt(date_arr[1]);
            int Day = Integer.parseInt(date_arr[0]);

            dp.updateDate(Year - 1 + 1, Month - 1, Day - 1 + 1);
        }
        save_datepick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Integer m=Integer.parseInt(String.valueOf(dp.getMonth()))+1;
                String dateOn=dp.getDayOfMonth()+"-"+ m +"-"+dp.getYear();

                androidx.fragment.app.FragmentManager fragmentManager7=getFragmentManager();

                PopReminder pop_reminder = new PopReminder();
                Bundle bundleDate_back = new Bundle();
                bundleDate_back.putString("time_value",time_get_date);
                bundleDate_back.putString("date_value", dateOn);

                pop_reminder.setArguments(bundleDate_back);

                assert fragmentManager7 != null;
                pop_reminder.show(fragmentManager7,"Back to dialog save date");

                dismiss();
            }
        });
        cancel_datepick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                androidx.fragment.app.FragmentManager fragmentManager8=getFragmentManager();
                PopReminder pop_reminder = new PopReminder();
                Bundle bundleTime_back = new Bundle();
                bundleTime_back.putString("time_value",time_get_date);
                bundleTime_back.putString("date_value", date_get_date);

                pop_reminder.setArguments(bundleTime_back);

                assert fragmentManager8 != null;
                pop_reminder.show(fragmentManager8,"Back to dialog cancel date");

                dismiss();
            }
        });

        return view;
    }
}
