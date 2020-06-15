package com.mohammadsharabati.restaurantgenieserver.Fragment;


import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mohammadsharabati.restaurantgenieserver.Adapter.TimeWorkerAdapter;
import com.mohammadsharabati.restaurantgenieserver.Common.Common;
import com.mohammadsharabati.restaurantgenieserver.Model.TimeWorker;
import com.mohammadsharabati.restaurantgenieserver.R;

import java.util.ArrayList;
import java.util.List;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class DayFragment extends Fragment {


    //    private View containerView;
    //My Added
    private RecyclerView recycler_fragment_day;
//    private FirebaseRecyclerOptions<TimeWorker> options;
//    private FirebaseRecyclerAdapter<TimeWorker, StaffTimeWorkerItemViewHolder> adapterFirebase;
    TimeWorkerAdapter adapter;
    List<TimeWorker> timeWorkerList = new ArrayList<>();
    private FirebaseDatabase database;
    private DatabaseReference days;
    private Activity mActivity;



    public DayFragment() {
    }

    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    public static final String ARG_ID = "id";
    public static final String ARG_Day = "day";

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */

    private int id;
    private String day;

    public static DayFragment newInstance(int id, String day) {
        DayFragment fragment = new DayFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_ID, id);
        args.putString(ARG_Day, day);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            id = getArguments().getInt(ARG_ID);
            day = getArguments().getString(ARG_Day);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_days, container, false);

        recycler_fragment_day = rootView.findViewById(R.id.recycler_fragment_day);
//        recycler_fragment_day.setHasFixedSize(true);

        //Init firebase
        database = FirebaseDatabase.getInstance();
        Bundle bundle = getArguments();

        Log.v("TAG", ">>>>> loadTimeTable Tab number:" + bundle.getInt(ARG_ID));

        switch (bundle.getInt(ARG_ID)) {
            case 0:
                days = database.getReference().child("RestaurantGenie").child(Common.currentUser.getBusinessNumber()).child("Day").child("Sunday");
                break;
            case 1:
                days = database.getReference().child("RestaurantGenie").child(Common.currentUser.getBusinessNumber()).child("Day").child("Monday");
                break;
            case 2:
                days = database.getReference().child("RestaurantGenie").child(Common.currentUser.getBusinessNumber()).child("Day").child("Tuesday");
                break;
            case 3:
                days = database.getReference().child("RestaurantGenie").child(Common.currentUser.getBusinessNumber()).child("Day").child("Wednesday");
                break;
            case 4:
                days = database.getReference().child("RestaurantGenie").child(Common.currentUser.getBusinessNumber()).child("Day").child("Thursday");
                break;
            case 5:
                days = database.getReference().child("RestaurantGenie").child(Common.currentUser.getBusinessNumber()).child("Day").child("Friday");
                break;
            case 6:
                days = database.getReference().child("RestaurantGenie").child(Common.currentUser.getBusinessNumber()).child("Day").child("Saturday");
                break;
        }
        mActivity = getActivity();

        // Set up Layout Manager, reverse layout
        LinearLayoutManager mManager = new LinearLayoutManager(mActivity);

        recycler_fragment_day.setLayoutManager(mManager);

        days.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Log.v("TAG", ">>>>> snapshot: " + snapshot.getValue(TimeWorker.class).getName());
                    timeWorkerList.add(snapshot.getValue(TimeWorker.class));
                }
                adapter = new TimeWorkerAdapter(timeWorkerList);
                adapter.notifyDataSetChanged();
                recycler_fragment_day.setAdapter(adapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        // keyQuery - the Firebase location containing the list of keys to be found in dataRef
//        Log.v("TAGDAYS", ">>>>> days is: " + days.child("01"));
//        days = days.child("01");
//
//
//        options = new FirebaseRecyclerOptions.Builder<TimeWorker>()
//                .setQuery(days, TimeWorker.class)
//                .build();
//
//
//
//        adapterFirebase = new FirebaseRecyclerAdapter<TimeWorker, StaffTimeWorkerItemViewHolder>(options) {
//
//
//            @Override
//            protected void onBindViewHolder(@NonNull final StaffTimeWorkerItemViewHolder holder, int position, @NonNull final TimeWorker model) {
//                Log.v("TAG", ">>>>> options:");
//                holder.name.setText(model.getName());
//                holder.startTime.setText(model.getStartTime());
//                holder.endTime.setText(model.getEndTime());
//                    days.addListenerForSingleValueEvent(new ValueEventListener() {
//                        List<TimeWorker> timeWorkerList = new ArrayList<>();
//
//                        @Override
//                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                            for (DataSnapshot timeWorkerSnapshot : dataSnapshot.getChildren()) {
//                                timeWorkerList.add(timeWorkerSnapshot.getValue(TimeWorker.class));
//
//                                String endTime = dataSnapshot.child("endTime").getValue().toString();
//                                String name = dataSnapshot.child("name").getValue().toString();
//                                String startTime = dataSnapshot.child("startTime").getValue().toString();
//
//                                Log.v("TAG", "mohammad sharabati: >>>>> loadTimeTable name:" + endTime);
//                                Log.v("TAG", "mohammad sharabati: >>>>> loadTimeTable startTime:" + name);
//                                Log.v("TAG", "mohammad sharabati: >>>>> loadTimeTable endTime: " + startTime);
//
//                            }
//                        }
//
//                        @Override
//                        public void onCancelled(@NonNull DatabaseError databaseError) {
//
//                        }
//                    });
//            }
//
//            @NonNull
//            @Override
//            public StaffTimeWorkerItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//                View view = LayoutInflater.from(parent.getContext())
//                        .inflate(R.layout.staff_time_table_item, parent, false);
//                return new StaffTimeWorkerItemViewHolder(view);
//            }
//        };


//        adapterFirebase.startListening();
//        adapterFirebase.notifyDataSetChanged(); // Refresh data if have data changed
//        recycler_fragment_day.setAdapter(adapterFirebase);


        return rootView;
    }


//    @Override
//    public void onStart() {
//        super.onStart();
//        if (adapterFirebase != null) {
//            adapterFirebase.startListening();
//        }
//    }
//
//    @Override
//    public void onResume() {
//        super.onResume();
//        //Fix click back on FoodDetail and get no item in FoodList
//        if (adapterFirebase != null) {
//            adapterFirebase.startListening();
//            Log.v("TAG","onResume");
//        }
//    }
////
//    @Override
//    public void onStop() {
//        super.onStop();
//        if (adapterFirebase != null)
//            adapterFirebase.stopListening();
//    }
}
