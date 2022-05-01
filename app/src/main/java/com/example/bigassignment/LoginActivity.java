package com.example.bigassignment;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {

    private DBManager dbManager;
    private EditText email;
    private EditText pwd;
    private Button btn_login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Init();
        AddAction();
    }

    private void Init() {
        this.email = findViewById(R.id.inp_email_login);
        this.pwd = findViewById(R.id.inp_pwd_login);
        this.btn_login = findViewById(R.id.btn_sub_login);

        dbManager = new DBManager(this);
    }

    public void AddAction() {
        this.btn_login.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                login();
            }
        });
    }

    public void login() {
        String inp_email = this.email.getText().toString().trim();
        String inp_pwd = this.pwd.getText().toString();
        System.out.println(inp_email + " " + inp_pwd);

        Cursor cursor = dbManager.query("Account", null, null, null, null);
        while ((cursor != null) && (cursor.moveToNext())) {
            int id = cursor.getInt(0);
            String fname = cursor.getString(1);
            String email = cursor.getString(2);
            String password = cursor.getString(3);
            System.out.println(id + " " + fname + " " + email + " " + password);
        }

        String[] SelectionArgs = {inp_email};
        cursor = dbManager.query("Account", null, "Email=?", SelectionArgs, null);
        if ((cursor != null) && (cursor.moveToFirst())){
            int id = cursor.getInt(0);
            String fname = cursor.getString(1);
            String email = cursor.getString(2);
            String password = cursor.getString(3);
            System.out.println(id + " " + fname + " " + email + " " + password);
        }
        else {
            Toast.makeText(getApplicationContext(),"Email không tồn tại!",Toast.LENGTH_SHORT).show();
        }
//
    }
}