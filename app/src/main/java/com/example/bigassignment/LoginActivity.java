package com.example.bigassignment;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
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

        this.dbManager = new DBManager(this);
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
        String inp_pwd = this.pwd.getText().toString().trim();
        System.out.println(inp_email + " " + inp_pwd);
        if (inp_email.equals("") || inp_pwd.equals("")) {
            Toast.makeText(getApplicationContext(),"Chưa nhập đủ thông tin!",Toast.LENGTH_SHORT).show();
        }
        else {
            // Get a specified account
            String[] SelectionArgs = {inp_email};
            Cursor cursor = dbManager.query("Account", null, "Email=?", SelectionArgs, null);
            if ((cursor != null) && (cursor.moveToFirst())) {
                int id = cursor.getInt(0);
                String fname = cursor.getString(1);
                String email = cursor.getString(2);
                String password = cursor.getString(3);

                UserModel user = new UserModel(id, fname, email, password);
                System.out.println(user.getUser_id() + " " + user.getFull_name() + " "
                        + user.getEmail() + " " + user.getPassword());

                if (!user.getPassword().equals(inp_pwd)) {
                    Toast.makeText(getApplicationContext(), "Sai mật khẩu!", Toast.LENGTH_SHORT).show();
                } else {
                    Intent intent = new Intent();
                    intent.setClass(LoginActivity.this, AllNotesActivity.class);
                    intent.putExtra("user_id", ""+user.getUser_id());
                    startActivity(intent);
                }
            } else {
                Toast.makeText(getApplicationContext(), "Email không tồn tại!", Toast.LENGTH_SHORT).show();
            }
        }
    }
}