package com.alaythiaproductions.e_commerce;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

public class RegisterActivity extends AppCompatActivity {

    private Button createAccountBtn;
    private EditText usernameET, phoneNumberET, passwordET;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        createAccountBtn = findViewById(R.id.register_btn);
        usernameET = findViewById(R.id.register_username_edit_text);
        phoneNumberET = findViewById(R.id.register_phone_number_edit_text);
        passwordET = findViewById(R.id.register_password_edit_text);
    }
}
