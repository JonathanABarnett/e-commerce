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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class RegisterActivity extends AppCompatActivity {

    private Button createAccountBtn;
    private EditText usernameET, phoneNumberET, passwordET;
    private ProgressDialog loadingBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        createAccountBtn = findViewById(R.id.register_btn);
        usernameET = findViewById(R.id.register_username_edit_text);
        phoneNumberET = findViewById(R.id.register_phone_number_edit_text);
        passwordET = findViewById(R.id.register_password_edit_text);
        loadingBar = new ProgressDialog(this);

        createAccountBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createAccount();
            }
        });
    }

    private void createAccount() {
        String username = usernameET.getText().toString().trim();
        String phone = phoneNumberET.getText().toString().trim();
        String password = passwordET.getText().toString().trim();

        if (TextUtils.isEmpty(username)) {
            Toast.makeText(this, "Please enter your name", Toast.LENGTH_SHORT).show();
            return;
        } else if (phone.length() < 10) {
            Toast.makeText(this, "Please enter a valid phone number", Toast.LENGTH_SHORT).show();
            return;
        } else if (password.length() < 6) {
            Toast.makeText(this, "Please enter a password greater than 6 characters", Toast.LENGTH_SHORT).show();
            return;
        } 
        
        loadingBar.setTitle("Create Account");
        loadingBar.setMessage("Please wait, we are loading you into the system");
        loadingBar.setCanceledOnTouchOutside(false);
        loadingBar.show();
        
        validatePhoneNumber(username, phone, password);
    }

    private void validatePhoneNumber(final String username, final String phone, final String password) {
    
        final DatabaseReference rootRef;
        rootRef = FirebaseDatabase.getInstance().getReference();
        
        rootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (!dataSnapshot.child("Users").child(phone).exists()) {
                    HashMap<String, Object> userDataHashMap = new HashMap<>();
                    userDataHashMap.put("phone", phone);
                    userDataHashMap.put("username", username);
                    userDataHashMap.put("password", password);

                    rootRef.child("Users").child(phone).updateChildren(userDataHashMap)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(RegisterActivity.this, "You have been added to the system", Toast.LENGTH_SHORT).show();
                                        loadingBar.dismiss();

                                        Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                                        intent.putExtra("phone", phone);
                                        startActivity(intent);
                                        finish();
                                    } else {
                                        Toast.makeText(RegisterActivity.this, "Error! You broke it", Toast.LENGTH_SHORT).show();
                                        loadingBar.dismiss();
                                    }
                                }
                            });
                } else {
                    Toast.makeText(RegisterActivity.this, "" + phone + " already exists", Toast.LENGTH_SHORT).show();
                    loadingBar.dismiss();
                    Toast.makeText(RegisterActivity.this, "Please try again using a different number", Toast.LENGTH_SHORT).show();
                    phoneNumberET.setText("");
                    passwordET.setText("");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
