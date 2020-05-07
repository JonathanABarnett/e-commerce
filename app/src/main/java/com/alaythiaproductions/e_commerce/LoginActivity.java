package com.alaythiaproductions.e_commerce;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.alaythiaproductions.e_commerce.models.User;
import com.alaythiaproductions.e_commerce.persistence.Persistence;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rey.material.widget.CheckBox;

import io.paperdb.Paper;

public class LoginActivity extends AppCompatActivity {

    private Button loginBtn;
    private EditText phoneNumberET, passwordET;
    private ProgressDialog loadingBar;

    private CheckBox rememberMeCB;

    private String rootUserDB = "Users";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        loginBtn = findViewById(R.id.login_btn);
        phoneNumberET = findViewById(R.id.login_phone_number_edit_text);
        passwordET = findViewById(R.id.login_password_edit_text);
        loadingBar = new ProgressDialog(this);

        rememberMeCB = findViewById(R.id.login_remember_me_checkbox);
        Paper.init(this);

        Intent intent = getIntent();
        phoneNumberET.setText(intent.getStringExtra("phone"));

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginUser();
            }
        });
    }

    private void loginUser() {

        String phone = phoneNumberET.getText().toString().trim();
        String password = passwordET.getText().toString().trim();

        if (phone.length() < 10) {
            Toast.makeText(this, "Please enter a valid phone number", Toast.LENGTH_SHORT).show();
            return;
        } else if (password.length() < 6) {
            Toast.makeText(this, "Please enter a password greater than 6 characters", Toast.LENGTH_SHORT).show();
            return;
        }

        loadingBar.setTitle("Logging in");
        loadingBar.setMessage("Please wait, we are logging you into the system");
        loadingBar.setCanceledOnTouchOutside(false);
        loadingBar.show();

        loginToAccount(phone, password);
    }

    private void loginToAccount(final String phone, final String password) {
        if (rememberMeCB.isChecked()) {
            Paper.book().write(Persistence.UserPhoneKey, phone);
            Paper.book().write(Persistence.UserPasswordKey, password);
        }

        final DatabaseReference ref = FirebaseDatabase.getInstance().getReference();

        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.child(rootUserDB).child(phone).exists()) {
                    User user = dataSnapshot.child(rootUserDB).child(phone).getValue(User.class);
                    
                    if (user.getPhone().equals(phone)) {
                        if (user.getPassword().equals(password)) {
                            Toast.makeText(LoginActivity.this, "Logged in successfully", Toast.LENGTH_SHORT).show();
                            loadingBar.dismiss();

                            Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                            startActivity(intent);
                        } else {
                            Toast.makeText(LoginActivity.this, "Incorrect Password you Hacker!", Toast.LENGTH_SHORT).show();
                            loadingBar.dismiss();
                        }
                    }
                } else {
                    Toast.makeText(LoginActivity.this, "Account with phone: " + phone + " doesn't exist", Toast.LENGTH_SHORT).show();
                    loadingBar.dismiss();
                    Toast.makeText(LoginActivity.this, "Go make an account fool", Toast.LENGTH_SHORT).show();
                }
                
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
