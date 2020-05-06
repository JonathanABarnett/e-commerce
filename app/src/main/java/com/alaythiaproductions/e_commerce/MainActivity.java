package com.alaythiaproductions.e_commerce;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    private Button signUpButton, loginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        signUpButton = findViewById(R.id.main_sign_up_btn);
        loginButton = findViewById(R.id.main_login_btn);
    }
}
