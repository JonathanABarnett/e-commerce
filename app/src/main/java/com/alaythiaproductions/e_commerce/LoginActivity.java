package com.alaythiaproductions.e_commerce;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class LoginActivity extends AppCompatActivity {

    private Button loginBtn;
    private EditText phoneNumberET, passwordET;
    private ProgressDialog loadingBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        loginBtn = findViewById(R.id.login_btn);
        phoneNumberET = findViewById(R.id.login_phone_number_edit_text);
        passwordET = findViewById(R.id.login_password_edit_text);
        loadingBar = new ProgressDialog(this);

        Intent intent = getIntent();
        phoneNumberET.setText(intent.getStringExtra("phone"));

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }
}
