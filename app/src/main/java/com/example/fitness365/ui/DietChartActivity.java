package com.example.fitness365.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.example.fitness365.R;
import com.example.fitness365.models.Diet;
import com.example.fitness365.models.UserType;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class DietChartActivity extends AppCompatActivity {

    private Activity mActivity;

    private DietChartAdapter adapter;
    private ListView dietListView;

    private EditText mFoodEt;
    private EditText mTimeEt;
    private Button mAddBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diet_chart);

        mActivity = this;
        String uid = getIntent().getStringExtra("uid");

        final DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Smember")
                .child(uid).child("diets");

        adapter = new DietChartAdapter(mActivity, R.id.diet_list_view, reference);

        dietListView = findViewById(R.id.diet_list_view);
        dietListView.setAdapter(adapter);

        reference.addChildEventListener(adapter);

        mFoodEt = findViewById(R.id.diet_food_et);
        mTimeEt = findViewById(R.id.diet_time_et);
        mAddBtn = findViewById(R.id.add_diet_chart);

        if(uid.equals(FirebaseAuth.getInstance().getUid())) {
            mFoodEt.setVisibility(View.GONE);
            mTimeEt.setVisibility(View.GONE);
            mAddBtn.setVisibility(View.GONE);
            adapter.setViewer(UserType.USER);
        } else {
            adapter.setViewer(UserType.ADMIN);
        }

        mAddBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Diet diet = new Diet();
                diet.setFood(mFoodEt.getText().toString());
                diet.setTimeOfTheDay(mTimeEt.getText().toString());

                reference.push().setValue(diet).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()) {
                            Log.v("DietChartActivity", "Data inserted successfully");
                        } else {
                            Log.v("DietChartActivity", "Something went wrong!!!");
                        }
                        mFoodEt.setText("");
                        mTimeEt.setText("");
                    }
                });
            }
        });
    }
}