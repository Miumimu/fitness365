package com.example.fitness365.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.fitness365.R;
import com.example.fitness365.models.Member;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class UserInfoActivity extends AppCompatActivity {

    private Activity mActivity;
    private DatabaseReference databaseReference;

    private TextView mNameTv;
    private TextView mGenderTv;
    private TextView mHeightTv;
    private TextView mWeightTv;
    private TextView mAddressTv;
    private TextView mPhoneTv;
    private Button mDietChartBtn;
    private Button mWorkOutBtn;

    private Member userInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);

        mActivity = this;

        String uid = getIntent().getStringExtra("uid");

        mNameTv = findViewById(R.id.user_info_name);
        mGenderTv = findViewById(R.id.user_info_gender);
        mHeightTv = findViewById(R.id.user_info_height);
        mWeightTv = findViewById(R.id.user_info_weight);
        mAddressTv = findViewById(R.id.user_info_address);
        mPhoneTv = findViewById(R.id.user_info_phone);
        mDietChartBtn = findViewById(R.id.user_info_diet_chart);
        mWorkOutBtn = findViewById(R.id.user_info_work_out);

        if(uid == null) {
            Toast.makeText(mActivity, "Unable to retrieve uid", Toast.LENGTH_LONG).show();
        } else {
            databaseReference = FirebaseDatabase.getInstance().getReference().child("Smember").child(uid);
            databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    userInfo = snapshot.getValue(Member.class);
                    if(userInfo == null) {
                        Toast.makeText(mActivity, "Something went wrong!!!", Toast.LENGTH_LONG).show();
                        return;
                    }

                    mNameTv.setText(String.format("Name: %s %s", userInfo.getFirstName(), userInfo.getLastName()));
                    mGenderTv.setText(String.format("Gender: %s", userInfo.getGender().toString()));
                    mHeightTv.setText(String.format("Height: %s", userInfo.getHeight().toString()));
                    mWeightTv.setText(String.format("Weight: %s", userInfo.getWeight().toString()));
                    mAddressTv.setText(String.format("Address: %s", userInfo.getAddress()));
                    mPhoneTv.setText(String.format("Contact No. %s", userInfo.getPhone()));
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(mActivity, "Something went wrong!!!", Toast.LENGTH_LONG).show();
                }
            });

            mDietChartBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(userInfo != null) {
                        Intent intent = new Intent(mActivity, DietChartActivity.class);
                        intent.putExtra("uid", userInfo.getUid());
                        startActivity(intent);
                    } else {
                        Log.e("UserInfoActivity", "User info is not loaded yet");
                    }
                }
            });

            mWorkOutBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(userInfo != null) {
                        Intent intent = new Intent(mActivity, WorkOutActivity.class);
                        intent.putExtra("uid", userInfo.getUid());
                        startActivity(intent);
                    } else {
                        Log.e("UserInfoActivity", "User info is not loaded yet");
                    }
                }
            });
        }
    }
}