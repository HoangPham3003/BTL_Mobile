package com.example.bigassignment;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class AccountActivity extends AppCompatActivity {

    private DBManager dbManager;

    private TextView txt_name;
    private EditText info_name;
    private EditText info_email;
    private Button btn_update_info;
    private Button btn_change_pwd_act;
    private Button btn_logout;

    private String user_id;
    private String fname;
    private String email;
    private String password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);
        Init();
        AddAction();
    }

    private void Init() {
        this.dbManager = new DBManager(this);

        this.txt_name = findViewById(R.id.txt_name);
        this.info_name = findViewById(R.id.info_name);
        this.info_email = findViewById(R.id.info_email);
        this.btn_update_info = findViewById(R.id.btnUpdateInfo);
        this.btn_change_pwd_act = findViewById(R.id.btnUpdatePwdAct);
        this.btn_logout = findViewById(R.id.btn_logout);

        Intent intent = getIntent();
        this.user_id = intent.getStringExtra("user_id");
        System.out.println("User id: " + user_id);

        // Get a specified account
        String[] SelectionArgs = {user_id};
        Cursor cursor = dbManager.query("Account", null, "ID=?", SelectionArgs, null);
        if ((cursor != null) && (cursor.moveToFirst())) {
            int id = cursor.getInt(0);
            this.fname = cursor.getString(1);
            this.email = cursor.getString(2);
            this.password = cursor.getString(3);
            System.out.println(id + " " + fname + " " + email + " " + password);

            this.txt_name.setText(fname);
            this.info_name.setText(fname);
            this.info_email.setText(email);

        } else {
            Toast.makeText(getApplicationContext(), "Tài khoản không tồn tại!", Toast.LENGTH_SHORT).show();
        }
    }

    public void turn_back(View view) {
        Intent intent = new Intent();
        intent.setClass(AccountActivity.this, AllNotesActivity.class);
        intent.putExtra("user_id", user_id);
        startActivity(intent);
    }

    public void AddAction() {
        this.btn_update_info.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                updateInfo();
            }
        });

        this.btn_change_pwd_act.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(AccountActivity.this, ChangePwdActivity.class);
                intent.putExtra("user_id", user_id);
                startActivity(intent);
            }
        });

        this.btn_logout.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(AccountActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }

    public void updateInfo() {
        String name_edited = info_name.getText().toString().trim();
        String email_edited = info_email.getText().toString().trim();

        if (name_edited.equals("") || email_edited.equals("")) {
            Toast.makeText(getApplicationContext(), "Chưa nhập đủ thông tin cá nhân!", Toast.LENGTH_SHORT).show();
        }
        else {
            ContentValues values = new ContentValues();

            values.put(DBManager.Account_ColFullName, name_edited);
            values.put(DBManager.Account_ColEmail, email_edited);
            values.put(DBManager.Account_ColPassword, password);
            values.put(DBManager.Account_ColID, user_id);


            String[] SelectionArgs = {user_id};
            int count = dbManager.Update("Account", values, "ID=?", SelectionArgs);

            if (count > 0) {
                txt_name.setText(name_edited);
                info_name.setText(name_edited);
                info_email.setText(email_edited);
                Toast.makeText(AccountActivity.this, "Cập nhật thông tin thành công!", Toast.LENGTH_SHORT).show();
            } else
                Toast.makeText(getApplicationContext(), "Lỗi cập nhật!", Toast.LENGTH_SHORT).show();
        }
    }
}