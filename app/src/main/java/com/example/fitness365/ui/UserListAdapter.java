package com.example.fitness365.ui;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.fitness365.R;
import com.example.fitness365.models.Member;
import com.example.fitness365.models.UserType;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;

import java.util.List;

public class UserListAdapter extends ArrayAdapter<Member> implements ChildEventListener {

    private final Activity mActivity;
    private final LayoutInflater mInflater;
    private List<Member> memberList;

    private class UserEmailHolder {
        Button emailButton;

        public UserEmailHolder(Button button) {
            emailButton = button;
            emailButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String email = emailButton.getText().toString();
                    Toast.makeText(mActivity, "Button pressed with " + email, Toast.LENGTH_LONG).show();

                    for(Member member: memberList) {
                        if(member.getEmail().equals(email)) {
                            Intent activityIntent = new Intent(mActivity, UserInfoActivity.class);
                            activityIntent.putExtra("uid", member.getUid());
                            mActivity.startActivity(activityIntent);
                        }
                    }
                }
            });
        }
    }

    public UserListAdapter(@NonNull Activity mActivity, int resource, @NonNull List<Member> memberList) {
        super(mActivity, resource, memberList);
        this.mActivity = mActivity;
        this.mInflater = mActivity.getLayoutInflater();
        this.memberList = memberList;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Log.v("UserList", "GetView is called");
        UserEmailHolder holder;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.user_list_layout, parent, false);
            holder = new UserEmailHolder((Button) convertView.findViewById(R.id.user_email_btn));
            convertView.setTag(holder);
        } else {
            holder = (UserEmailHolder) convertView.getTag();
        }

        String userEmail = memberList.get(position).getEmail();
        holder.emailButton.setText(userEmail);

        return convertView;
    }

    @Override
    public int getCount() {
        return memberList.size();
    }

    @Override
    public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
        Log.v("UserList", "onChildAdded is called " + snapshot);
        for(Member member: memberList) {
            if(member.getUid().equals(snapshot.getKey())) return;
        }
        Member member = snapshot.getValue(Member.class);
        if (member.getType().equals(UserType.USER)) {
            memberList.add(member);
            notifyDataSetChanged();
        }
    }

    @Override
    public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
        Log.v("UserList", "onChildChanged is called " + snapshot);
    }

    @Override
    public void onChildRemoved(@NonNull DataSnapshot snapshot) {
        Log.v("UserList", "onChildRemoved is called " + snapshot);
        for(Member member: memberList) {
            if(member.getEmail().equals(snapshot.getKey())) {
                memberList.remove(member);
            }
        }
        notifyDataSetChanged();
    }

    @Override
    public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
        Log.v("UserList", "onChildMoved is called " + snapshot);
    }

    @Override
    public void onCancelled(@NonNull DatabaseError error) {
        Log.v("UserList", "onCancelled is called " + error);
    }
}
