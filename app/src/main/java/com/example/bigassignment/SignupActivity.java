package com.example.bigassignment;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class SignupActivity extends AppCompatActivity {
    private DBManager dbManager;
    private EditText fname;
    private EditText email;
    private EditText pwd;
    private EditText pwd_retyped;
    private Button btn_signup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        Init();
        AddAction();
    }

    private void Init() {
        this.fname = findViewById(R.id.inp_fname);
        this.email = findViewById(R.id.inp_email);
        this.pwd = findViewById(R.id.inp_pwd);
        this.pwd_retyped = findViewById(R.id.inp_pwd_retyped);
        this.btn_signup = findViewById(R.id.btn_sub_signup);

        this.dbManager = new DBManager(this);
    }

    public void AddAction() {
        this.btn_signup.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                add_account();
            }
        });
    }

    public void add_account() {
        String inp_fname = fname.getText().toString().trim();
        String inp_email = email.getText().toString().trim();
        String inp_pwd = pwd.getText().toString().trim();
        String inp_pwd_retyped = pwd_retyped.getText().toString().trim();
        System.out.println(inp_fname + " " + inp_email + " " + inp_pwd + " " + inp_pwd_retyped);

        if (inp_fname.equals("") || inp_email.equals("") || inp_pwd.equals("") || inp_pwd_retyped.equals("")) {
            Toast.makeText(this, "Chưa nhập đủ thông tin!", Toast.LENGTH_SHORT).show();
        }
        else if (!inp_pwd.equals(inp_pwd_retyped)) {
            Toast.makeText(this, "Mật khẩu nhập lại phải giống với mật khẩu nhập vào!", Toast.LENGTH_SHORT).show();
        }
        else {
            // Get a specified account
            String[] SelectionArgs = {inp_email};
            Cursor cursor = dbManager.query("Account", null, "Email=?", SelectionArgs, null);
            if ((cursor != null) && (cursor.moveToFirst())) {
                Toast.makeText(getApplicationContext(), "Email đã tồn tại!", Toast.LENGTH_SHORT).show();
            } else {
                ContentValues values = new ContentValues();
                values.put(DBManager.Account_ColFullName, inp_fname);
                values.put(DBManager.Account_ColEmail, inp_email);
                values.put(DBManager.Account_ColPassword, inp_pwd_retyped);

                String table_name = "Account";
                long id = dbManager.Insert(values, table_name);
                if (id > 0) {
                    Toast.makeText(getApplicationContext(), "Tạo tài khoản thành công!", Toast.LENGTH_SHORT).show();
                    finish();
                } else
                    Toast.makeText(getApplicationContext(), "Tạo tài khoản thất bại!", Toast.LENGTH_SHORT).show();
            }
        }
    }
}