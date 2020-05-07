package com.alaythiaproductions.e_commerce;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.alaythiaproductions.e_commerce.models.User;
import com.alaythiaproductions.e_commerce.persistence.Persistence;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import io.paperdb.Paper;

public class MainActivity extends AppCompatActivity {

    private Button signUpBtn, loginBtn;

    private ProgressDialog loadingBar;

    private String rootUserDB = "Users";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        signUpBtn = findViewById(R.id.main_sign_up_btn);
        loginBtn = findViewById(R.id.main_login_btn);

        loadingBar = new ProgressDialog(this);

        Paper.init(this);
        String UserPhoneKey = Paper.book().read(Persistence.UserPhoneKey);
        String UserPasswordKey = Paper.book().read(Persistence.UserPasswordKey);
        if (UserPhoneKey != null && UserPasswordKey != null) {
            if (!TextUtils.isEmpty(UserPhoneKey) && !TextUtils.isEmpty(UserPasswordKey)) {

                loadingBar.setTitle("Logging in");
                loadingBar.setMessage("Please wait, we are logging you into the system");
                loadingBar.setCanceledOnTouchOutside(false);
                loadingBar.show();

                allowAccess(UserPhoneKey, UserPasswordKey);
            }
        }

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });

        signUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });
    }

    private void allowAccess(final String phone, final String password) {
        final DatabaseReference ref = FirebaseDatabase.getInstance().getReference();

        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.child(rootUserDB).child(phone).exists()) {
                    User user = dataSnapshot.child(rootUserDB).child(phone).getValue(User.class);

                    if (user.getPhone().equals(phone)) {
                        if (user.getPassword().equals(password)) {
                            Toast.makeText(MainActivity.this, "Logged in successfully", Toast.LENGTH_SHORT).show();
                            loadingBar.dismiss();

                            Intent intent = new Intent(MainActivity.this, HomeActivity.class);
                            startActivity(intent);
                        } else {
                            Toast.makeText(MainActivity.this, "Incorrect Password you Hacker!", Toast.LENGTH_SHORT).show();
                            loadingBar.dismiss();
                        }
                    }
                } else {
                    Toast.makeText(MainActivity.this, "Account with phone: " + phone + " doesn't exist", Toast.LENGTH_SHORT).show();
                    loadingBar.dismiss();
                    Toast.makeText(MainActivity.this, "Go make an account fool", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
