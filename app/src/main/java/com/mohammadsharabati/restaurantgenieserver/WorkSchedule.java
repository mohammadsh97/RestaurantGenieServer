package com.mohammadsharabati.restaurantgenieserver;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.firebase.ui.database.ObservableSnapshotArray;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.mohammadsharabati.restaurantgenieserver.Common.Common;
import com.mohammadsharabati.restaurantgenieserver.Interface.ItemClickListener;
import com.mohammadsharabati.restaurantgenieserver.Model.TimeWorker;
import com.mohammadsharabati.restaurantgenieserver.ViewHolder.StaffTimeWorkerItemViewHolder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class WorkSchedule extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private FloatingActionButton fab;
    private String DayId = "";

    //Firebase
    private FirebaseDatabase db;
    private DatabaseReference days;
    private FirebaseRecyclerAdapter<TimeWorker, StaffTimeWorkerItemViewHolder> adapter;

    EditText edtName , edtFromTime, edtUntilTime,edtUpdateName , edtUpdateFromTime, edtUpdateUntilTime;

    TimeWorker newTimeWorker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_work_schedule);

        // Firebase
        db = FirebaseDatabase.getInstance();
        days = db.getReference().child("RestaurantGenie").child(Common.currentUser.getBusinessNumber()).child("Days");

        //Init View
        recyclerView = (RecyclerView) findViewById(R.id.recycler_work_schedule);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog();
            }
        });

        if (getIntent() != null)
            DayId = getIntent().getStringExtra("DayId");
        if (!DayId.isEmpty())
            loadListWorkSchedule(DayId);
    }

    private void loadListWorkSchedule(String dayId) {

        Query loadListWorkScheduleId = days.orderByChild("dayId").equalTo(dayId);

        FirebaseRecyclerOptions<TimeWorker> options = new FirebaseRecyclerOptions.Builder<TimeWorker>()
                .setQuery(loadListWorkScheduleId, TimeWorker.class)
                .build();

        adapter = new FirebaseRecyclerAdapter<TimeWorker, StaffTimeWorkerItemViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull StaffTimeWorkerItemViewHolder holder, int position, @NonNull TimeWorker model) {
                holder.endTime.setText(model.getEndTime());
                holder.name.setText(model.getName());
                holder.startTime.setText(model.getStartTime());
                holder.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void onClick(View view, int position, boolean isLongClick) {
                    }
                });
            }

            @Override
            public StaffTimeWorkerItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                View itemView = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.staff_time_table_item, parent, false);

                return new StaffTimeWorkerItemViewHolder(itemView);
            }
        };
        adapter.startListening();
        adapter.notifyDataSetChanged(); // Refresh data if have data changed
        recyclerView.setAdapter(adapter);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        if (item.getTitle().equals(Common.UPDATE)) {
            showUpdateWorkSchedule(adapter.getRef(item.getOrder()).getKey(), adapter.getItem(item.getOrder()));
        } else if (item.getTitle().equals(Common.DELETE)) {
            deleteWorkSchedule(adapter.getRef(item.getOrder()).getKey());
        }
        return super.onContextItemSelected(item);
    }


    private void showUpdateWorkSchedule(String key, TimeWorker item) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(WorkSchedule.this, R.style.DialogTheme);
        alertDialog.setTitle("Edit Work Schedule");
        alertDialog.setMessage("Please fill full information");

        LayoutInflater inflater = this.getLayoutInflater();
        View update_work_schedule = inflater.inflate(R.layout.update_work_schedule_layout, null);

        edtUpdateName = update_work_schedule.findViewById(R.id.edtUpdateName);
        edtUpdateFromTime = update_work_schedule.findViewById(R.id.edtUpdateFromTime);
        edtUpdateUntilTime = update_work_schedule.findViewById(R.id.edtUpdateUntilTime);

        //Set default value for view
        edtUpdateName.setText(item.getName());
        edtUpdateFromTime.setText(item.getStartTime());
        edtUpdateUntilTime.setText(item.getEndTime());

        edtUpdateFromTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTimeDialog(edtUpdateFromTime);
            }
        });
        edtUpdateUntilTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTimeDialog(edtUpdateUntilTime);
            }
        });

        alertDialog.setView(update_work_schedule);
        alertDialog.setIcon(R.drawable.ic_shopping_cart_black_24dp);

        // Set Button
        alertDialog.setPositiveButton("Update", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();

                //update Information
                item.setName(edtUpdateName.getText().toString());
                item.setStartTime(edtUpdateFromTime.getText().toString());
                item.setEndTime(edtUpdateUntilTime.getText().toString());

                days.child(key).setValue(item);

            }
        });
        alertDialog.setNegativeButton("Exit", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        alertDialog.show();

    }

    private void deleteWorkSchedule(String key) {
        days.child(key).removeValue();

    }

    /**
     * Adding new Work Schedule
     */
    private void showDialog() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(WorkSchedule.this, R.style.DialogTheme);
        alertDialog.setTitle("Add new Schedule");
        alertDialog.setMessage("Please fill full information");

        LayoutInflater inflater = this.getLayoutInflater();
        View add_new_work_schedule = inflater.inflate(R.layout.add_new_work_schedule_layout, null);

        edtName = add_new_work_schedule.findViewById(R.id.edtName);
        edtFromTime = add_new_work_schedule.findViewById(R.id.edtFromTime);
        edtUntilTime = add_new_work_schedule.findViewById(R.id.edtUntilTime);

        edtFromTime.setInputType(InputType.TYPE_NULL);
        edtUntilTime.setInputType(InputType.TYPE_NULL);

        edtFromTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTimeDialog(edtFromTime);
            }
        });
        edtUntilTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTimeDialog(edtUntilTime);
            }
        });

        alertDialog.setView(add_new_work_schedule);
        alertDialog.setIcon(R.drawable.ic_shopping_cart_black_24dp);

        // set button
        alertDialog.setPositiveButton("ADD", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();

                if (!edtName.getText().toString().trim().isEmpty() &&
                        !edtFromTime.getText().toString().trim().isEmpty() &&
                        !edtUntilTime.getText().toString().trim().isEmpty()){

                    newTimeWorker = new TimeWorker(edtName.getText().toString() ,
                            edtFromTime.getText().toString()
                    ,edtUntilTime.getText().toString() ,DayId);
                }
                else {
                    Toast.makeText(WorkSchedule.this, "Please fill full information", Toast.LENGTH_SHORT).show();
                    showDialog();
                }

                // Here, pushing new category to Firebase Database
                if (newTimeWorker != null) {
                    days.push().setValue(newTimeWorker);
                }

            }
        });
        alertDialog.setNegativeButton("Exit", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        alertDialog.show();
    }

    private void showTimeDialog(final EditText time_in) {
        final Calendar calendar=Calendar.getInstance();

        TimePickerDialog.OnTimeSetListener timeSetListener=new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                calendar.set(Calendar.HOUR_OF_DAY,hourOfDay);
                calendar.set(Calendar.MINUTE,minute);
                SimpleDateFormat simpleDateFormat=new SimpleDateFormat("HH:mm");
                time_in.setText(simpleDateFormat.format(calendar.getTime()));
            }
        };

        new TimePickerDialog(WorkSchedule.this,timeSetListener,calendar.get(Calendar.HOUR_OF_DAY),calendar.get(Calendar.MINUTE),false).show();
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