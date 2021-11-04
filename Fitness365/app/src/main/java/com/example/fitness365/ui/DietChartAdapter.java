package com.example.fitness365.ui;

import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.fitness365.R;
import com.example.fitness365.models.Diet;
import com.example.fitness365.models.Member;
import com.example.fitness365.models.UserType;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class DietChartAdapter extends BaseAdapter implements ChildEventListener {

    private final Activity mActivity;
    private final LayoutInflater mInflater;
    private Map<String, Diet> dietMap;

    private DatabaseReference databaseReference;
    private UserType userType;

    public DietChartAdapter(@NonNull Activity activity, int resource, DatabaseReference reference) {
        this.mActivity = activity;
        this.mInflater = activity.getLayoutInflater();
        this.dietMap = new TreeMap<>();
        databaseReference = reference;
    }

    public void setViewer(UserType userType) {
        this.userType = userType;
    }

    private class DietHolder {
        private TextView mFoodTv;
        private TextView mTimeTv;
        private Button mRemoveBtn;
        private int index;

        public DietHolder(TextView foodTv, TextView timeTv, Button removeBtn) {
            this.mFoodTv = foodTv;
            this.mTimeTv = timeTv;
            this.mRemoveBtn = removeBtn;

            if(userType.equals(UserType.USER)) {
                removeBtn.setVisibility(View.GONE);
            }

            mRemoveBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    databaseReference.child((String) dietMap.keySet().toArray()[index]).removeValue();
                }
            });
        }

        void setIndex(int position) {
            index = position;
        }
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Log.v("UserList", "GetView is called");
        DietHolder holder;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.diet_table_row, parent, false);

            TextView food = convertView.findViewById(R.id.set_diet_food);
            TextView time = convertView.findViewById(R.id.set_diet_time);
            Button removeBtn = convertView.findViewById(R.id.diet_remove);

            holder = new DietHolder(food, time, removeBtn);
            convertView.setTag(holder);
        } else {
            holder = (DietHolder) convertView.getTag();
        }
        holder.setIndex(position);

        List<Diet> dietList = new ArrayList<>(dietMap.values());
        holder.mFoodTv.setText(dietList.get(position).getFood());
        holder.mTimeTv.setText(dietList.get(position).getTimeOfTheDay());

        return convertView;
    }

    @Override
    public int getCount() {
        return dietMap.size();
    }

    @Override
    public Object getItem(int position) {
        return dietMap.get(dietMap.keySet().toArray()[position]);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
        Log.v("DietChartActivity", "onChildAdded is called " + snapshot);
        if(dietMap.containsKey(snapshot.getKey())) return;

        dietMap.put(snapshot.getKey(), snapshot.getValue(Diet.class));
        notifyDataSetChanged();
    }

    @Override
    public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

    }

    @Override
    public void onChildRemoved(@NonNull DataSnapshot snapshot) {
        Log.v("DietChartActivity", "onChildAdded is called " + snapshot);
        dietMap.remove(snapshot.getKey());
        notifyDataSetChanged();
    }

    @Override
    public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

    }

    @Override
    public void onCancelled(@NonNull DatabaseError error) {

    }
}
