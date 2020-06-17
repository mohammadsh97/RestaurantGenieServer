package com.mohammadsharabati.restaurantgenieserver;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.mohammadsharabati.restaurantgenieserver.Common.Common;
import com.mohammadsharabati.restaurantgenieserver.Interface.ItemClickListener;
import com.mohammadsharabati.restaurantgenieserver.Model.Day;
import com.mohammadsharabati.restaurantgenieserver.ViewHolder.WeekTableViewHolder;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class TimeTable extends AppCompatActivity {

    private FirebaseDatabase database;
    private DatabaseReference weeks;

    private RecyclerView recycler_menu;
    private RecyclerView.LayoutManager mLayoutManager;
    private FirebaseRecyclerOptions<Day> options;
    private FirebaseRecyclerAdapter<Day, WeekTableViewHolder> adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_time_worker);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Time Table");

        //Init firebase
        database = FirebaseDatabase.getInstance();
        weeks = database.getReference().child("RestaurantGenie").child(Common.currentUser.getBusinessNumber()).child("Week");

        //load menu
        recycler_menu = (RecyclerView) findViewById(R.id.recycler_week);
        recycler_menu.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        recycler_menu.setLayoutManager(mLayoutManager);
        loadWeek();
    }

    private void loadWeek() {


        options = new FirebaseRecyclerOptions.Builder<Day>()
                .setQuery(weeks, Day.class)
                .build();


        adapter = new FirebaseRecyclerAdapter<Day, WeekTableViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull WeekTableViewHolder holder, int i, @NonNull Day model) {
                holder.tvWeek.setText(model.getName());
                holder.ivLetter.setOval(true);
                holder.ivLetter.setLetter(model.getName().charAt(0));
                holder.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void onClick(View view, int position, boolean isLongClick) {
                        // send categoryId and start new activity
                        Intent workSchedule = new Intent(TimeTable.this, WorkSchedule.class);
                        Log.v("msm" , ""+adapter.getRef(position).getKey());
                        workSchedule.putExtra("DayId", adapter.getRef(position).getKey());
                        startActivity(workSchedule);
                    }
                });
            }

            @NonNull
            @Override
            public WeekTableViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.week_single_item, parent, false);
                return new WeekTableViewHolder(view);
            }
        };
        adapter.startListening();
        adapter.notifyDataSetChanged(); // Refresh data if have data changed
        recycler_menu.setAdapter(adapter);
    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }

    @Override
    protected void onResume() {
        super.onResume();
        //Fix click back on FoodDetail and get no item in FoodList
        if (adapter != null)
            adapter.startListening();
    }
}