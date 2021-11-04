package com.example.fitness365.ui;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.example.fitness365.R;
import com.example.fitness365.models.Member;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class AdminActivity extends AppCompatActivity {

    private Activity mActivity;
    private UserListAdapter adapter;
    private DatabaseReference reference;

    private ListView userListView;

    private Button mLogOutBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        mActivity = this;

        adapter = new UserListAdapter(mActivity, R.id.user_list_view, new ArrayList<Member>());

        userListView = findViewById(R.id.user_list_view);
        userListView.setAdapter(adapter);

        reference = FirebaseDatabase.getInstance().getReference().child("Smember");
        reference.addChildEventListener(adapter);

        mLogOutBtn = findViewById(R.id.admin_log_out);
        mLogOutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(mActivity, MainActivity.class));
            }
        });
    }
}