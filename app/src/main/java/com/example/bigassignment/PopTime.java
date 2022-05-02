package com.example.bigassignment;

import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TimePicker;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.DialogFragment;


public class PopTime extends DialogFragment{
    View view;
    Button save_timepick,cancel_timepick;
    TimePicker tp;
    String time_get_time,date_get_time;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        view=inflater.inflate(R.layout.activity_pop_time, container, false);
        save_timepick=(Button)view.findViewById(R.id.time_save_picker);
        cancel_timepick=(Button)view.findViewById(R.id.time_cancel_picker);
        tp = (TimePicker)view.findViewById(R.id.tp);


        if (getArguments() != null && !TextUtils.isEmpty(getArguments().getString("time_value1")))
            time_get_time=getArguments().getString("time_value1");
        if (getArguments() != null && !TextUtils.isEmpty(getArguments().getString("date_value1")))
            date_get_time=getArguments().getString("date_value1");

        if(!time_get_time.equalsIgnoreCase("ignore")) {
            String[] time_arr = time_get_time.split(":", 2);
            for (String a : time_arr)
                System.out.println("Holathis1" + a);

            int Hour = Integer.parseInt(time_arr[0]);
            int Minute = Integer.parseInt(time_arr[1]);

            tp.setHour(Hour);
            tp.setMinute(Minute);
        }

        save_timepick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String timeOn = tp.getHour()+":"+tp.getMinute();

                androidx.fragment.app.FragmentManager fragmentManager5=getFragmentManager();

                PopReminder pop_reminder = new PopReminder();
                Bundle bundleTime_back = new Bundle();
                bundleTime_back.putString("time_value",timeOn);
                bundleTime_back.putString("date_value", date_get_time);

                pop_reminder.setArguments(bundleTime_back);

                assert fragmentManager5 != null;
                pop_reminder.show(fragmentManager5,"Back to dialog save time");

                dismiss();
            }
        });
        cancel_timepick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                androidx.fragment.app.FragmentManager fragmentManager6=getFragmentManager();


                PopReminder pop_reminder=new PopReminder();
                Bundle bundleTime_back = new Bundle();
                bundleTime_back.putString("time_value",time_get_time);
                bundleTime_back.putString("date_value", date_get_time);

                pop_reminder.setArguments(bundleTime_back);

                assert fragmentManager6 != null;
                pop_reminder.show(fragmentManager6,"Back to dialog cancel time");

                dismiss();
            }
        });
        return view;
    }
}
