package com.example.fitness365.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.fitness365.R;
import com.example.fitness365.models.Gender;
import com.example.fitness365.models.Member;
import com.example.fitness365.models.UserType;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ProfileActivity extends AppCompatActivity {
    EditText fname,lname,height,weight,address,phn;
    RadioButton admin,user,female,male;
    Button save;
    DatabaseReference reference;
    String gender ="";
    String type = "";
    Member member;
    FirebaseDatabase firebaseDatabase;
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile);
        getSupportActionBar();
        fname = findViewById(R.id.fname);
        lname = findViewById((R.id.lname));
        height = findViewById(R.id.height);
        weight = findViewById(R.id.weight);
        address = findViewById(R.id.address);
        phn = findViewById(R.id.phone);
        admin = findViewById(R.id.admin);
        user = findViewById(R.id.user);
        male = findViewById(R.id.male);
        female = findViewById(R.id.female);
        save = findViewById(R.id.save);
        member = new Member();
        reference = FirebaseDatabase.getInstance().getReference().child("Smember");
        firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        setCurrentValues();

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String fn = fname.getText().toString();
                String ln = lname.getText().toString();
                String a = address.getText().toString();
                String ph = phn.getText().toString();

                float h = Float.parseFloat(height.getText().toString());
                float w = Float.parseFloat(weight.getText().toString());

                if (fn.equals("")){
                    Toast.makeText(ProfileActivity.this,"First name is Blank",Toast.LENGTH_LONG).show();
                } else if (ln.equals("")){
                    Toast.makeText(ProfileActivity.this,"last name is Blank",Toast.LENGTH_LONG).show();
                } else if (a.equals("")){
                    Toast.makeText(ProfileActivity.this,"Address is Blank",Toast.LENGTH_LONG).show();
                } else if (ph.equals("")){
                    Toast.makeText(ProfileActivity.this,"Phone number is Blank",Toast.LENGTH_LONG).show();
                } else {
                    member.setFirstName(fn);
                    member.setLastName(ln);
                    member.setHeight(h);
                    member.setWeight(w);
                    member.setAddress(a);
                    member.setPhone(ph);
                    if (male.isChecked()){
                        member.setGender(Gender.MALE);
                    }
                    if (female.isChecked()){
                        member.setGender(Gender.FEMALE);
                    }
                    if (user.isChecked()){
                        member.setType(UserType.USER);
                    }
                    if (admin.isChecked()){
                        member.setType(UserType.ADMIN);
                    }
                    member.setEmail(firebaseUser.getEmail());
                    member.setUid(firebaseUser.getUid());
                    reference.child(member.getUid()).setValue(member).
                            addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isCanceled()) {
                                Toast.makeText(ProfileActivity.this,"Something went wrong, " +
                                        "try again!!!",Toast.LENGTH_LONG).show();
                            } else if(task.isSuccessful()) {
                                Toast.makeText(ProfileActivity.this,"Data inserted " +
                                        "Successfully",Toast.LENGTH_LONG).show();
                                if(member.getType().equals(UserType.ADMIN))
                                    startActivity(new Intent(ProfileActivity.this, AdminActivity.class));
                                else
                                    startActivity(new Intent(ProfileActivity.this, UserActivity.class));
                            }
                        }
                    });
                }
            }
        });
    }

    private void setCurrentValues() {
        reference.child(firebaseUser.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Member member = snapshot.getValue(Member.class);
                if(member == null) return;
                fname.setText(member.getFirstName());
                lname.setText(member.getLastName());
                height.setText(member.getHeight().toString());
                weight.setText(member.getWeight().toString());
                address.setText(member.getAddress());
                phn.setText(member.getPhone());
                if(member.getGender().equals(Gender.MALE)) {
                    male.setChecked(true);
                    female.setChecked(false);
                } else {
                    male.setChecked(false);
                    female.setChecked(true);
                }
                if (member.getType().equals(UserType.USER)) {
                    user.setChecked(true);
                    admin.setChecked(false);
                } else {
                    user.setChecked(false);
                    admin.setChecked(true);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
