package com.example.bigassignment;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;

public class AllNotesActivity extends AppCompatActivity {

    private DBManager dbManager;
    private long RecordID;
    private String RecordTitle;
    private String RecordDesc;
    private String RecordDateRem;
    private String RecordTimeRem;
    private String user_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_notes);
        Init();
        getdatabaseinfo(1,"ignore");
//        createNotificationChannel();
    }

    private void Init() {
        Intent intent = getIntent();
        this.user_id = intent.getStringExtra("user_id");
        System.out.println("User id: " + user_id);

        Toast.makeText(getApplicationContext(),"Xin chào!",Toast.LENGTH_SHORT).show();
        dbManager=new DBManager(this);

        set_search_engine();
    }

    public void set_search_engine() {
        SearchView sv = (SearchView) findViewById(R.id.note_search);
        SearchManager sm = (SearchManager)getSystemService(Context.SEARCH_SERVICE);
        sv.setSearchableInfo(sm.getSearchableInfo(getComponentName()));
        sv.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                getdatabaseinfo(2,query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                getdatabaseinfo(2,newText);
                return true;
            }
        });
    }

    public void btn_add_activity(View view) {
        // For adding new notes
        // Setting recordid to maximum exact size of the row+1
        RecordID=dbManager.RowCount("Note")+1;

        String RecordID_string = String.valueOf(RecordID);
        Intent add_edit_act_intent1 = new Intent(getApplicationContext(),NewNoteActivity.class);
        add_edit_act_intent1.putExtra("user_id", user_id);
        add_edit_act_intent1.putExtra("titlefrom","ignore");
        add_edit_act_intent1.putExtra("descriptionfrom","ignore");
        add_edit_act_intent1.putExtra("add_or_update","ADD");
        add_edit_act_intent1.putExtra("recordno",RecordID_string);
        add_edit_act_intent1.putExtra("rem_time","ignore");
        add_edit_act_intent1.putExtra("rem_date","ignore");
        startActivityForResult(add_edit_act_intent1,3);
    }

    private void createNotificationChannel() {
        String CHANNEL_ID="ReminderID";
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = getString(R.string.channel_name);
            String description = getString(R.string.channel_description);
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            channel.enableVibration(true);
            channel.setLightColor(Color.RED);
            channel.setVibrationPattern(new long[]{0});
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    ArrayList<AdapterItems> listnewsData = new ArrayList<AdapterItems>();
    MyCustomAdapter myadapter;

    @SuppressLint("Range")
    public void getdatabaseinfo(int count1, String to_search){

        //add data and view it
        listnewsData.clear();
        //String[] projection={"","",""};
        Cursor cursor;
        if (count1 == 1) {
            String[] SelectionsArgs={user_id};
            cursor = dbManager.query("Note",null, "Account_ID=?", SelectionsArgs, DBManager.Note_ColDateTime+" DESC");
        }
        else {
            String[] SelectionsArgs={"%"+to_search+"%","%"+to_search+"%", user_id};
            cursor = dbManager.query("Note",null, "(Title like ? or Description like ?) and Account_ID=?", SelectionsArgs, DBManager.Note_ColDateTime+" DESC");
        }
        //If wanted to select all data then give null in selection
        if(cursor.moveToFirst()){
            do{
                listnewsData.add(new AdapterItems(cursor.getLong(cursor.getColumnIndex(DBManager.Note_ColID)),
                        cursor.getString(cursor.getColumnIndex(DBManager.Note_ColDateTime)),
                        cursor.getString(cursor.getColumnIndex(DBManager.Note_ColTitle)),
                        cursor.getString(cursor.getColumnIndex(DBManager.Note_ColDescription)),
                        cursor.getString(cursor.getColumnIndex(DBManager.Note_ColRemTime)),
                        cursor.getString(cursor.getColumnIndex(DBManager.Note_ColRemDate)),
                        cursor.getLong(cursor.getColumnIndex(DBManager.Note_Account_ID))));

            }while (cursor.moveToNext());
        }

        myadapter = new MyCustomAdapter(listnewsData);

        final ListView lsNews = (ListView)findViewById(R.id.lv_all);
        lsNews.setAdapter(myadapter); //intisal with data

    }

    private class MyCustomAdapter extends BaseAdapter {
        public ArrayList<AdapterItems> listnewsDataAdpater ;

        public MyCustomAdapter(ArrayList<AdapterItems> listnewsDataAdpater) {
            this.listnewsDataAdpater = listnewsDataAdpater;
        }

        @Override
        public int getCount() {
            return listnewsDataAdpater.size();
        }

        @Override
        public String getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }
        TextView txt_datetime;
        TextView txt_datetime_rem, txt_title, txt_desc;
        @Override
        public View getView(int position, View convertView, ViewGroup parent)
        {
            LayoutInflater mInflater = getLayoutInflater();
            View myView = mInflater.inflate(R.layout.layout_ticket, null);

            final AdapterItems s = listnewsDataAdpater.get(position);

            String rem_DateTime=s.Time+" "+s.Date;
            txt_datetime_rem=(TextView)myView.findViewById(R.id.date_time_id_rem);

            if(s.Time.equalsIgnoreCase("notset")) {
                txt_datetime_rem.setVisibility(View.GONE);
            }
            else {
                txt_datetime_rem.setVisibility(View.VISIBLE);
                txt_datetime_rem.setText(rem_DateTime);
            }
            txt_title=(TextView)myView.findViewById(R.id.title_tv2);
            txt_title.setText(s.Title);
            txt_title.setSelected(true);

            txt_desc=(TextView)myView.findViewById(R.id.desc_tv2);
            txt_desc.setText(s.Description);

            txt_desc.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    RecordID=s.ID;
                    RecordTitle=s.Title;
                    RecordDesc=s.Description;
                    RecordDateRem=s.Date;
                    RecordTimeRem=s.Time;
                    update_element_new();
                }
            });
            return myView;
        }
    }

    public void update_element_new(){
        // For updating notes
        String title_received = RecordTitle;
        String description_received = RecordDesc;
        String RecordID_string = String.valueOf(RecordID);
        String time_rem_received = RecordTimeRem;
        String date_rem_received = RecordDateRem;
        Intent add_edit_act_intent = new Intent(getApplicationContext(),NewNoteActivity.class);
        add_edit_act_intent.putExtra("user_id", user_id);
        add_edit_act_intent.putExtra("titlefrom",title_received);
        add_edit_act_intent.putExtra("descriptionfrom",description_received);
        add_edit_act_intent.putExtra("add_or_update","UPDATE");
        add_edit_act_intent.putExtra("recordno",RecordID_string);
        add_edit_act_intent.putExtra("rem_time",time_rem_received);
        add_edit_act_intent.putExtra("rem_date",date_rem_received);
        startActivityForResult(add_edit_act_intent,4);
    }

    public void show_account(View view) {
        Intent intent = new Intent();
        intent.setClass(AllNotesActivity.this, AccountActivity.class);
        intent.putExtra("user_id", this.user_id);
        startActivity(intent);
    }
}