package com.example.bigassignment;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import androidx.fragment.app.DialogFragment;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class PopReminder extends DialogFragment{
    View view;
    TextView time_df,date_df;
    ImageButton back_rem, time_pick, date_pic;
    Button delete_rem, save_rem;
    String time_get, date_get;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        view=inflater.inflate(R.layout.activity_pop_reminder, container, false);
        back_rem=(ImageButton)view.findViewById(R.id.back_rem);
        time_pick=(ImageButton)view.findViewById(R.id.time_pick);
        date_pic=(ImageButton)view.findViewById(R.id.date_pic);
        delete_rem=(Button) view.findViewById(R.id.delete_rem);
        save_rem=(Button) view.findViewById(R.id.save_rem);
        time_df=(TextView)view.findViewById(R.id.time_df);
        date_df=(TextView)view.findViewById(R.id.date_df);


        if (getArguments() != null && !TextUtils.isEmpty(getArguments().getString("time_value")))
            time_get=getArguments().getString("time_value");
        if (getArguments() != null && !TextUtils.isEmpty(getArguments().getString("date_value")))
            date_get=getArguments().getString("date_value");


        SimpleDateFormat sdf1 = new SimpleDateFormat("HH:mm", Locale.getDefault());
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());

        String currentTime1 = sdf1.format(new Date());
        String currentDate1 = sdf.format(new Date());

        if(time_get.equalsIgnoreCase("ignore")) {
            time_df.setText(currentTime1);
        }
        else{
            time_df.setText(time_get);
        }


        if(date_get.equalsIgnoreCase("ignore")) {
            date_df.setText(currentDate1);
        }
        else{
            date_df.setText(date_get);
        }


        back_rem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        time_pick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                androidx.fragment.app.FragmentManager fragmentManager1=getFragmentManager();
                PopTime poptime=new PopTime();
                Bundle bundle1_time = new Bundle();
                bundle1_time.putString("time_value1", time_get);
                bundle1_time.putString("date_value1", date_get);

                poptime.setArguments(bundle1_time);

                poptime.show(fragmentManager1,"Time Picker Fragment Show");
                dismiss();
            }
        });
        date_pic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                androidx.fragment.app.FragmentManager fragmentManager2=getFragmentManager();
                PopDate popdate=new PopDate();
                Bundle bundle1_date = new Bundle();
                bundle1_date.putString("time_value2", time_get);
                bundle1_date.putString("date_value2", date_get);

                popdate.setArguments(bundle1_date);

                popdate.show(fragmentManager2,"Date Picker Fragment Show");
                dismiss();
            }
        });
        delete_rem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(),"Xóa nhắc nhở thành công!",Toast.LENGTH_SHORT).show();
                NewNoteActivity a = (NewNoteActivity)getActivity();
                a.deleteRem();
                dismiss();
            }
        });
        save_rem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(),"Đặt nhắc nhở thành công!",Toast.LENGTH_SHORT).show();
                NewNoteActivity a = (NewNoteActivity)getActivity();
                a.setDateTime(time_df.getText().toString(),date_df.getText().toString());
                dismiss();
            }
        });
        return view;
    }
}
