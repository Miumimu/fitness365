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
import com.example.fitness365.models.UserType;
import com.example.fitness365.models.WorkOut;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class WorkOutActivity extends AppCompatActivity {

    private Activity mActivity;
    private WorkOutAdapter adapter;
    private ListView workOutListView;

    private EditText mExerciseEt;
    private EditText mDayEt;
    private EditText mTimeOfDayEt;
    private Button mAddBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_work_out);

        mActivity = this;
        String uid = getIntent().getStringExtra("uid");

        final DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Smember")
                .child(uid).child("workouts");

        adapter = new WorkOutAdapter(mActivity, R.id.work_out_list_view, reference);

        workOutListView = findViewById(R.id.work_out_list_view);
        workOutListView.setAdapter(adapter);

        reference.addChildEventListener(adapter);

        mExerciseEt = findViewById(R.id.work_out_exercise_et);
        mDayEt = findViewById(R.id.work_out_day_et);
        mTimeOfDayEt = findViewById(R.id.work_out_time_of_day_et);
        mAddBtn = findViewById(R.id.add_work_out);

        if(uid.equals(FirebaseAuth.getInstance().getUid())) {
            mExerciseEt.setVisibility(View.GONE);
            mDayEt.setVisibility(View.GONE);
            mTimeOfDayEt.setVisibility(View.GONE);
            mAddBtn.setVisibility(View.GONE);
            adapter.setViewer(UserType.USER);
        } else {
            adapter.setViewer(UserType.ADMIN);
        }

        mAddBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WorkOut workOut = new WorkOut();
                workOut.setExercise(mExerciseEt.getText().toString());
                workOut.setDay(mDayEt.getText().toString());
                workOut.setTimeOfDay(mTimeOfDayEt.getText().toString());

                reference.push().setValue(workOut).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()) {
                            Log.v("WorkOutActivity", "Data inserted successfully");
                        } else {
                            Log.v("WorkOutActivity", "Something went wrong!!!");
                        }
                        mExerciseEt.setText("");
                        mDayEt.setText("");
                        mTimeOfDayEt.setText("");
                    }
                });
            }
        });
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}