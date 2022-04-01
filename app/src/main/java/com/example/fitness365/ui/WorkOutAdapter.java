package com.example.fitness365.ui;

import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.fitness365.R;
import com.example.fitness365.models.Diet;
import com.example.fitness365.models.UserType;
import com.example.fitness365.models.WorkOut;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class WorkOutAdapter extends BaseAdapter implements ChildEventListener {
    private final Activity mActivity;
    private final LayoutInflater mInflater;
    private final Map<String, WorkOut> workOutMap;
    private final DatabaseReference databaseReference;
    private UserType userType;

    public WorkOutAdapter(@NonNull Activity activity, int resource, DatabaseReference reference) {
        this.mActivity = activity;
        this.mInflater = activity.getLayoutInflater();
        this.workOutMap = new TreeMap<>();
        this.databaseReference = reference;
    }

    public void setViewer(UserType userType) {
        this.userType = userType;
    }

    private class WorkOutHolder {
        private TextView mExerciseTv;
        private TextView mDayTv;
        private TextView mTimeOfDayTv;
        private Button mRemoveBtn;
        private int index;

        public WorkOutHolder(TextView exerciseTv, TextView dayTv, TextView timeOfDayTv, Button removeBtn) {
            mExerciseTv = exerciseTv;
            mDayTv = dayTv;
            mTimeOfDayTv = timeOfDayTv;
            this.mRemoveBtn = removeBtn;

            if(userType.equals(UserType.USER)) {
                removeBtn.setVisibility(View.GONE);
            }

            mRemoveBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    databaseReference.child((String) workOutMap.keySet().toArray()[index]).removeValue();
                }
            });
        }

        void setIndex(int position) {
            index = position;
        }
    }

    @Override
    public int getCount() {
        return workOutMap.size();
    }

    @Override
    public Object getItem(int position) {
        return workOutMap.get(workOutMap.keySet().toArray()[position]);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Log.v("UserList", "GetView is called");
        WorkOutHolder holder;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.work_out_table_row, parent, false);

            TextView exercise = convertView.findViewById(R.id.work_out_exercise);
            TextView day = convertView.findViewById(R.id.work_out_day);
            TextView timeOfDay = convertView.findViewById(R.id.work_out_time_of_day);
            Button removeBtn = convertView.findViewById(R.id.work_out_remove);

            holder = new WorkOutHolder(exercise, day, timeOfDay, removeBtn);
            convertView.setTag(holder);
        } else {
            holder = (WorkOutHolder) convertView.getTag();
        }
        holder.setIndex(position);

        List<WorkOut> workOutList = new ArrayList<>(workOutMap.values());
        holder.mExerciseTv.setText(workOutList.get(position).getExercise());
        holder.mDayTv.setText(workOutList.get(position).getDay());
        holder.mTimeOfDayTv.setText(workOutList.get(position).getTimeOfDay());

        return convertView;
    }

    @Override
    public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
        Log.v("WorkOutActivity", "onChildAdded is called " + snapshot);
        if(workOutMap.containsKey(snapshot.getKey())) return;

        workOutMap.put(snapshot.getKey(), snapshot.getValue(WorkOut.class));
        notifyDataSetChanged();
    }

    @Override
    public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

    }

    @Override
    public void onChildRemoved(@NonNull DataSnapshot snapshot) {
        Log.v("WorkOutActivity", "onChildAdded is called " + snapshot);
        workOutMap.remove(snapshot.getKey());
        notifyDataSetChanged();
    }

    @Override
    public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

    }

    @Override
    public void onCancelled(@NonNull DatabaseError error) {

    }
}
