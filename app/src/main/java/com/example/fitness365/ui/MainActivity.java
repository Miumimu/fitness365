package com.example.fitness365.ui;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.fitness365.R;
import com.example.fitness365.models.Member;
import com.example.fitness365.models.UserType;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {

    Button singup, login;
    EditText email, pw;

    AppCompatActivity activity;

    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;

    Member member;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar();

        activity = this;
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();

        if(firebaseUser != null) {
            handleSignIn();
        }

        email = findViewById(R.id.lmail);
        pw = findViewById(R.id.lpw);
        login = findViewById(R.id.LogIn);
        member = new Member();
        singup = findViewById(R.id.SignUp);
        singup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String mail,pass;
                mail = email.getText().toString();
                pass = pw.getText().toString();
                if (mail.equals("")){
                    Toast.makeText(MainActivity.this,"This Email is invalid",Toast.LENGTH_LONG).show();
                } else if (pass.equals("")){
                    Toast.makeText(MainActivity.this,"This Password is invalid",Toast.LENGTH_LONG).show();
                } else {
                    firebaseAuth.createUserWithEmailAndPassword(mail, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(activity, "Successful", Toast.LENGTH_LONG).show();
                                startActivity(new Intent(getApplicationContext(), ProfileActivity.class));
                            } else {
                                Toast.makeText(MainActivity.this, "Wrong password or Email", Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                }
            }
        });
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String mail,pass;
                mail = email.getText().toString();
                pass = pw.getText().toString();
                if (mail.equals("")){
                    Toast.makeText(MainActivity.this,"This Email is invalid",Toast.LENGTH_LONG).show();
                }
                else if (pass.equals("")){
                    Toast.makeText(MainActivity.this,"This Password is invalid",Toast.LENGTH_LONG).show();
                }
                else {
                    firebaseAuth.signInWithEmailAndPassword(mail, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                firebaseUser = firebaseAuth.getCurrentUser();
                                handleSignIn();
                            } else {
                                Toast.makeText(MainActivity.this, "Wrong password or Email", Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                }
            }
        });
    }

    void handleSignIn() {
        Toast.makeText(activity, "Successful", Toast.LENGTH_LONG).show();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Smember");
        reference.child(firebaseUser.getUid()).child("type").addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        Log.v("MainActivity", snapshot.toString());
                        String type = snapshot.getValue(String.class);
                        if(type.equals(UserType.USER.name())) {
                            startActivity(new Intent(getApplicationContext(), UserActivity.class));
                            finish();
                        } else if(type.equals(UserType.ADMIN.name())) {
                            startActivity(new Intent(getApplicationContext(), AdminActivity.class));
                            finish();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }
}