package com.example.bigassignment;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class NewNoteActivity extends AppCompatActivity {
    private DBManager dbManager;

    private EditText title_value;
    private EditText desc_value;
    private TextView RemTime;
    private TextView RemDate;

    private String RecordID_received1;
    private Integer RecordID_received;
    private String add_this;
    private String title_received1;
    private String desc_received1;
    private String RemDate_received,RemTime_received;

    private String user_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_note);
        Init();
    }

    private void Init() {
        title_value = findViewById(R.id.title_value);
        desc_value = findViewById(R.id.desc_value);
        RemTime = findViewById(R.id.rem_time1);
        RemDate = findViewById(R.id.rem_date1);

        RemTime.setVisibility(View.GONE);
        RemDate.setVisibility(View.GONE);
        String yo1 = "ignore";
        RemTime.setText(yo1);
        RemDate.setText(yo1);

        dbManager = new DBManager(this);

        Bundle b1=getIntent().getExtras();
        user_id = b1.getString("user_id");
        title_received1 = b1.getString("titlefrom");
        desc_received1 = b1.getString("descriptionfrom");
        add_this = b1.getString("add_or_update");
        RecordID_received1 = b1.getString("recordno");
        RemTime_received = b1.getString("rem_time");
        RemDate_received = b1.getString("rem_date");
        RecordID_received = Integer.parseInt(RecordID_received1);

        if(add_this.equals("UPDATE")){
            title_value.setText(title_received1);
            desc_value.setText(desc_received1);
            if(!RemTime_received.equalsIgnoreCase("notset")) {
                RemTime.setVisibility(View.VISIBLE);
                RemDate.setVisibility(View.VISIBLE);
                RemTime.setText(RemTime_received);
                RemDate.setText(RemDate_received);
            }
        }
    }

    public void save_button_add_update(View view) {
        if(!add_this.equals("UPDATE")){
//             For adding to database
            push_values_database();
        }
        else{
            // For updating in database
            update_database();
        }
    }

    public void push_values_database() {
        if (!title_value.getText().toString().equals("") && !desc_value.getText().toString().equals("")) {
            System.out.println(title_value.getText().toString() + " " + desc_value.getText().toString());

            ContentValues values = new ContentValues();

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
            String currentDateandTime = sdf.format(new Date());

            values.put(DBManager.Note_ColDateTime, currentDateandTime);
            values.put(DBManager.Note_ColTitle, title_value.getText().toString().trim());
            values.put(DBManager.Note_ColDescription, desc_value.getText().toString().trim());

            if(!RemTime.getText().toString().equalsIgnoreCase("ignore")) {
                values.put(DBManager.Note_ColRemTime, RemTime.getText().toString());
                values.put(DBManager.Note_ColRemDate, RemDate.getText().toString());
                SetAlarm(RemTime.getText().toString(), RemDate.getText().toString(),title_value.getText().toString(),desc_value.getText().toString());
                System.out.println("You are here push_values_database function !!!");
            }
            else{
                values.put(DBManager.Note_ColRemTime, "notset");
                values.put(DBManager.Note_ColRemDate, "notset");
            }
            values.put(DBManager.Note_Account_ID, user_id);

            String table_name = DBManager.Note_TableName;
            long id = dbManager.Insert(values, table_name);
            if (id > 0) {
                Toast.makeText(this, "Tạo ghi chú thành công!", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent();
                intent.setClass(NewNoteActivity.this, AllNotesActivity.class);
                intent.putExtra("user_id", user_id);
                startActivity(intent);
            } else
                Toast.makeText(this, "Tạo ghi chú thất bại!", Toast.LENGTH_SHORT).show();
        }
        else{
            Toast.makeText(this, "Chưa nhập đủ dữ liệu!", Toast.LENGTH_SHORT).show();
        }
    }

    public void update_database(){
        if (!title_value.getText().toString().equals("") && !desc_value.getText().toString().equals("")) {
            ContentValues values = new ContentValues();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
            String currentDateandTime = sdf.format(new Date());

            values.put(DBManager.Note_ColDateTime, currentDateandTime);
            values.put(DBManager.Note_ColTitle, title_value.getText().toString());
            values.put(DBManager.Note_ColDescription, desc_value.getText().toString());
            values.put(DBManager.Note_ColID, RecordID_received);

            if(!RemTime.getText().toString().equalsIgnoreCase("ignore")) {
                values.put(DBManager.Note_ColRemTime, RemTime.getText().toString());
                values.put(DBManager.Note_ColRemDate, RemDate.getText().toString());
                SetAlarm(RemTime.getText().toString(), RemDate.getText().toString(),title_value.getText().toString(),desc_value.getText().toString());
                System.out.println("You are here update_database function !!!");
            }
            else{
                values.put(DBManager.Note_ColRemTime, "notset");
                values.put(DBManager.Note_ColRemDate, "notset");
                CancelAlarm();
            }
            values.put(DBManager.Note_Account_ID, user_id);

            String[] SelectionArgs = {String.valueOf(RecordID_received)};
            int count2 = dbManager.Update("Note", values, "ID=?", SelectionArgs);

            if (count2 > 0) {
                Toast.makeText(this, "Cập nhật ghi chú thành công!", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent();
                intent.setClass(NewNoteActivity.this, AllNotesActivity.class);
                intent.putExtra("user_id", user_id);
                startActivity(intent);
            } else
                Toast.makeText(getApplicationContext(), "Chọn 1 ghi chú để cập nhật!", Toast.LENGTH_SHORT).show();
        }
        else{
            Toast.makeText(getApplicationContext(), "Chưa nhập đủ thông tin!", Toast.LENGTH_SHORT).show();
        }
    }

    public void set_reminder(View view) {
        System.out.println("Start set reminder!!!");
        androidx.fragment.app.FragmentManager fm=getSupportFragmentManager();
        PopReminder pop_reminder =new PopReminder();

        Bundle bundle1 = new Bundle();
        bundle1.putString("time_value", RemTime.getText().toString());
        bundle1.putString("date_value", RemDate.getText().toString());

        pop_reminder.setArguments(bundle1);

        pop_reminder.show(fm,"Show Fragment");
    }

    public void SetAlarm(String TimeAlarm, String DateAlarm, String Title_Received, String Desc_Received){

        String[] time_arr = TimeAlarm.split(":",2);
        String[] date_arr = DateAlarm.split("-",3);

        int Hour=Integer.parseInt(time_arr[0]);
        int Minute=Integer.parseInt(time_arr[1]);
        int Year=Integer.parseInt(date_arr[2]);
        int Month=Integer.parseInt(date_arr[1])-1;
        int Day=Integer.parseInt(date_arr[0]);

        System.out.println("Setting alarm for "+RecordID_received+" at "+Hour+":"+Minute+" and "+Day+"-"+String.valueOf(Integer.valueOf(Month+1))+"-"+Year);

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, Year);
        calendar.set(Calendar.MONTH, Month);
        calendar.set(Calendar.DAY_OF_MONTH, Day);
        calendar.set(Calendar.HOUR_OF_DAY, Hour);
        calendar.set(Calendar.MINUTE, Minute);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        AlarmManager am = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        System.out.println(am);

        Intent intent = new Intent(NewNoteActivity.this, MyReceiver.class);
//        intent.setAction("com.akashmanna.rem");
        intent.setAction("com.me.rem");

        String msg1="Hello from Note!!!";
        intent.putExtra("AlarmMessage",msg1);
        intent.putExtra("NotiID",RecordID_received1);
        intent.putExtra("Noti_Title",Title_Received);
        intent.putExtra("Noti_Desc",Desc_Received);
        intent.putExtra("Rem_Time",TimeAlarm);
        intent.putExtra("Rem_Date",DateAlarm);
        intent.putExtra("SetNotify","SetNotification");

        PendingIntent pi = PendingIntent.getBroadcast(NewNoteActivity.this, RecordID_received, intent,0);
        System.out.println(calendar.getTimeInMillis());
        am.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pi);
        System.out.println("====== Hello ======");
    }

    public void CancelAlarm(){
        AlarmManager am = (AlarmManager)getSystemService (Context.ALARM_SERVICE);

        Intent intent = new Intent(this, MyReceiver.class);
        intent.setAction("com.akashmanna.rem");
        String msg1="Hello from Note";
        intent.putExtra("AlarmMessage",msg1);
        intent.putExtra("NotiID",RecordID_received1);
        intent.putExtra("Noti_Title","ignore");
        intent.putExtra("Noti_Desc","ignore");
        intent.putExtra("Rem_Time","ignore");
        intent.putExtra("Rem_Date","ignore");
        intent.putExtra("SetNotify","SetNotificationNot");

        PendingIntent pi = PendingIntent.getBroadcast(this, RecordID_received, intent,0);
        assert am != null;
        am.cancel(pi);

        System.out.println("Canceling alarm for "+RecordID_received);
    }

    public void delete_note(View view) {
        if(add_this.equals("UPDATE")) {
            AlertDialog.Builder info1=new AlertDialog.Builder(this);
            info1.setMessage("Bạn có chắc khi xóa ghi chú này?")
                    .setTitle("Warning")
                    .setNeutralButton("Không", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    })
                    .setPositiveButton("Có", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            delete_element(RecordID_received1);
                        }
                    })
                    .show();
        }
        else{
            Toast.makeText(getApplicationContext(),"Ghi chú không tồn tại!",Toast.LENGTH_SHORT).show();
        }
    }

    public void delete_element(String ID1){
        String[] SelectionArgs={ID1};
        int count=dbManager.Delete("Note", "ID=?", SelectionArgs);
        if (count>0){
            CancelAlarm();
            Toast.makeText(getApplicationContext(),"Xóa ghi chú thành công!",Toast.LENGTH_SHORT).show();
            Intent intent = new Intent();
            intent.setClass(NewNoteActivity.this, AllNotesActivity.class);
            intent.putExtra("user_id", user_id);
            startActivity(intent);
        }
        else{
            Toast.makeText(getApplicationContext(),"Xóa ghi chú thất bại!",Toast.LENGTH_SHORT).show();
        }
    }

    public void close_act(View view) {
        Intent intent = new Intent();
        intent.setClass(NewNoteActivity.this, AllNotesActivity.class);
        intent.putExtra("user_id", user_id);
        startActivity(intent);
    }

    public void setDateTime(String time_received_reminder,String date_received_reminder){
        // Will be called from save button click of Dialog Frag
        RemTime.setText(time_received_reminder);
        RemDate.setText(date_received_reminder);
        RemTime.setVisibility(View.VISIBLE);
        RemDate.setVisibility(View.VISIBLE);
    }

    public void deleteRem(){
        RemTime.setVisibility(View.GONE);
        RemDate.setVisibility(View.GONE);
        String yo="ignore";
        RemTime.setText(yo);
        RemDate.setText(yo);
    }
}