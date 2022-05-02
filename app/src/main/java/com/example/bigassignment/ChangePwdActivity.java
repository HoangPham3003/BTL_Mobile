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

public class ChangePwdActivity extends AppCompatActivity {

    private DBManager dbManager;

    private EditText old_pwd;
    private EditText new_pwd;
    private EditText new_pwd_retyped;
    private Button btn_sub_update_pwd;
    private TextView txt_name;

    private String user_id;
    private String fname;
    private String email;
    private String password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_pwd);
        Init();
        AddAction();
    }

    private void Init() {
        this.dbManager = new DBManager(this);

        this.old_pwd = findViewById(R.id.old_pwd);
        this.new_pwd = findViewById(R.id.new_pwd);
        this.new_pwd_retyped = findViewById(R.id.new_pwd_retyped);
        this.btn_sub_update_pwd = findViewById(R.id.btnUpdatePwd);
        this.txt_name = findViewById(R.id.txt_name);

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
        } else {
            Toast.makeText(getApplicationContext(), "Tài khoản không tồn tại!", Toast.LENGTH_SHORT).show();
        }
    }

    public void AddAction() {
        this.btn_sub_update_pwd.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                String inp_old_pwd = old_pwd.getText().toString().trim();
                String inp_new_pwd = new_pwd.getText().toString().trim();
                String inp_new_pwd_retyped = new_pwd_retyped.getText().toString().trim();

                if (inp_old_pwd.equals("") || inp_new_pwd.equals("") || inp_new_pwd_retyped.equals("")) {
                    Toast.makeText(getApplicationContext(), "Chưa nhập đủ thông tin!", Toast.LENGTH_SHORT).show();
                }
                else if (!inp_old_pwd.equals(password)){
                    Toast.makeText(getApplicationContext(), "Sai mật khẩu cũ!", Toast.LENGTH_SHORT).show();
                }
                else if (inp_new_pwd.equals(password)){
                    Toast.makeText(getApplicationContext(), "Mật khẩu mới phải khác mật khẩu cũ!", Toast.LENGTH_SHORT).show();
                }
                else if (!inp_new_pwd.equals(inp_new_pwd_retyped)){
                    Toast.makeText(getApplicationContext(), "Mật khẩu mới và mật khẩu nhập lại phải giống nhau!", Toast.LENGTH_SHORT).show();
                }
                else {
                    ContentValues values = new ContentValues();

                    values.put(DBManager.Account_ColFullName, fname);
                    values.put(DBManager.Account_ColEmail, email);
                    values.put(DBManager.Account_ColPassword, inp_new_pwd_retyped);
                    values.put(DBManager.Account_ColID, user_id);


                    String[] SelectionArgs = {user_id};
                    int count = dbManager.Update("Account", values, "ID=?", SelectionArgs);

                    if (count > 0) {
                        Toast.makeText(ChangePwdActivity.this, "Cập nhật mật khẩu thành công!", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent();
                        intent.setClass(ChangePwdActivity.this, LoginActivity.class);
                        startActivity(intent);
                    } else
                        Toast.makeText(getApplicationContext(), "Lỗi cập nhật!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void turn_back(View view) {
        Intent intent = new Intent();
        intent.setClass(ChangePwdActivity.this, AccountActivity.class);
        intent.putExtra("user_id", user_id);
        startActivity(intent);
    }
}