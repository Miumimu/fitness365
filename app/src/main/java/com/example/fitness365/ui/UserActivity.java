package com.example.fitness365.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import com.example.fitness365.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class UserActivity extends AppCompatActivity {

    private Activity mActivity;

    private FirebaseUser firebaseUser;

    private ImageButton mProfileIB;
    private ImageButton mViewDietChartIB;
    private ImageButton mViewWorkOutIB;
    private Button mLogOutBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        mActivity = this;

        mProfileIB = findViewById(R.id.MyProfile);
        mViewDietChartIB = findViewById(R.id.viewDietchart);
        mViewWorkOutIB = findViewById(R.id.viewWorkOut);
        mLogOutBtn = findViewById(R.id.user_log_out);

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        mProfileIB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(mActivity, ProfileActivity.class));
            }
        });

        mViewWorkOutIB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mActivity, WorkOutActivity.class);
                intent.putExtra("uid", firebaseUser.getUid());
                startActivity(intent);
            }
        });

        mViewDietChartIB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mActivity, DietChartActivity.class);
                intent.putExtra("uid", firebaseUser.getUid());
                startActivity(intent);
            }
        });

        mLogOutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(mActivity, MainActivity.class));
                finish();
            }
        });
    }
}
