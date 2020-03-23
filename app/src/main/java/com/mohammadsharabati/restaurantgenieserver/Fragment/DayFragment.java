package com.mohammadsharabati.restaurantgenieserver.Fragment;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mohammadsharabati.restaurantgenieserver.Common.Common;
import com.mohammadsharabati.restaurantgenieserver.FoodList;
import com.mohammadsharabati.restaurantgenieserver.Home;
import com.mohammadsharabati.restaurantgenieserver.Interface.ItemClickListener;
import com.mohammadsharabati.restaurantgenieserver.Model.TimeWorker;
import com.mohammadsharabati.restaurantgenieserver.R;
import com.mohammadsharabati.restaurantgenieserver.TimeTable;
import com.mohammadsharabati.restaurantgenieserver.ViewHolder.TimeWorkerViewHolder;
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
    private FirebaseRecyclerOptions<TimeWorker> options;
    private FirebaseRecyclerAdapter<TimeWorker, TimeWorkerViewHolder> adapterFirebase;
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
        View rootView = inflater.inflate(R.layout.fragment_day, container, false);

        recycler_fragment_day = rootView.findViewById(R.id.recycler_fragment_day);
        recycler_fragment_day.setHasFixedSize(true);

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
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mActivity = getActivity();

        // Set up Layout Manager, reverse layout
        LinearLayoutManager mManager = new LinearLayoutManager(mActivity);
        mManager.setReverseLayout(true);
        mManager.setStackFromEnd(true);
        recycler_fragment_day.setLayoutManager(mManager);



//        days.keepSynced(true);
//        recycler_fragment_day.setLayoutManager(new LinearLayoutManager(getActivity()));

        // keyQuery - the Firebase location containing the list of keys to be found in dataRef
        Log.v("TAG", ">>>>> days is: " + days);
        options = new FirebaseRecyclerOptions.Builder<TimeWorker>()
                .setQuery(days, TimeWorker.class)
                .build();


        adapterFirebase = new FirebaseRecyclerAdapter<TimeWorker, TimeWorkerViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull final TimeWorkerViewHolder holder, int position, @NonNull final TimeWorker model) {
                Log.v("TAG", ">>>>> options:");
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
//                                holder.name.setText(name);
//                                holder.startTime.setText(startTime);
//                                holder.endTime.setText(endTime);
//                            }
//                        }
//
//                        @Override
//                        public void onCancelled(@NonNull DatabaseError databaseError) {
//
//                        }
//                    });
                Log.v("TAG", "mohammad sharabati: >>>>> loadTimeTable name:" + model.getName());
                Log.v("TAG", "mohammad sharabati: >>>>> loadTimeTable startTime:" + model.getStartTime());
                Log.v("TAG", "mohammad sharabati: >>>>> loadTimeTable endTime: " + model.getEndTime());

                holder.name.setText(model.getName());
                holder.startTime.setText(model.getStartTime());
                holder.endTime.setText(model.getEndTime());

                holder.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void onClick(View view, int position, boolean isLongClick) {


//                        // send TimeTable and start new activity TimeTable
//                        Intent timeTable = new Intent(getActivity(), TimeTable.class);
//                        timeTable.putExtra("TimeTable", adapterFirebase.getRef(position).getKey());
//                        startActivity(timeTable);

                    }
                });
            }

            @NonNull
            @Override
            public TimeWorkerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.day_item, parent, false);
                return new TimeWorkerViewHolder(view);
            }
        };

//        recycler_fragment_day.setAdapter(adapterFirebase);
//            recycler_fragment_day.setAdapter(adapterFirebase);
//            adapterFirebase.startListening();

//        adapterFirebase.startListening();
//        adapterFirebase.notifyDataSetChanged(); // Refresh data if have data changed
        recycler_fragment_day.setAdapter(adapterFirebase);

    }

//    @Override
//    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
//        super.onViewCreated(view, savedInstanceState);
//
//
//
//    }

    @Override
    public void onStart() {
        super.onStart();
        if (adapterFirebase != null) {
            adapterFirebase.startListening();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        //Fix click back on FoodDetail and get no item in FoodList
        if (adapterFirebase != null)
            adapterFirebase.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        if (adapterFirebase != null)
            adapterFirebase.stopListening();
    }
}




//
//
//
//
//
//
//
//    int pageNumber = -1;
//    private final List<Fragment> mFragmentList = new ArrayList<>();
//    private final List<String> mFragmentTitleList = new ArrayList<>();
//
//    private static final String ARG_SECTION_NUMBER = "section_number";
//    private PageViewModel pageViewModel;
//    private View v;
//
//    //My Added
//    private RecyclerView recycler_fragment_day;
//    private RecyclerView.LayoutManager layoutManager;
////    private FirebaseRecyclerOptions<TimeWorker> options;
////    private FirebaseRecyclerAdapter<TimeWorker, TimeWorkerViewHolder> adapterFirebase;
//
//    private DatabasePagingOptions<TimeWorker> options;
//    private FirebaseRecyclerPagingAdapter<TimeWorker, TimeWorkerViewHolder> adapterFirebase;
//
//    private FirebaseDatabase database;
//    private DatabaseReference days;
//    private PagedList.Config config;
//
//    public DayFragment() {
//        // Required empty public constructor
//    }
//
//    public static DayFragment newInstance(int index) {
//        DayFragment fragment = new DayFragment();
//        Bundle bundle = new Bundle();
//        bundle.putInt(ARG_SECTION_NUMBER, index);
//        fragment.setArguments(bundle);
//        return fragment;
//    }
//
//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//
//
//        pageViewModel = ViewModelProviders.of(this).get(PageViewModel.class);
//        int index = 1;
//        if (getArguments() != null) {
//            index = getArguments().getInt(ARG_SECTION_NUMBER);
//        }
//        pageViewModel.setIndex(index);
//
//        pageNumber = getArguments().getInt(ARG_SECTION_NUMBER);
//    }
//
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        // Inflate the layout for this fragment
//        v = inflater.inflate(R.layout.fragment_day, container, false);
//        return v;
//    }
//
//
//    @Override
//    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
//        super.onViewCreated(view, savedInstanceState);
//
////
//////        recycler_fragment_day = view.findViewById(R.id.recycler_fragment_day);
//////        recycler_fragment_day.setHasFixedSize(true);
//////        layoutManager = new LinearLayoutManager(getActivity());
//////        recycler_fragment_day.setLayoutManager(layoutManager);
////
//        //Init firebase
//        database = FirebaseDatabase.getInstance();
////        days = database.getReference().child("RestaurantGenie").child(Common.currentUser.getBusinessNumber()).child("Days").child("Sunday");
//
////        System.out.println("mohammad sharabati: " + view.getTag().toString());
//
////        switch (pageViewModel.getmIndex()) {
////            case "Sunday":
////                days = database.getReference().child("RestaurantGenie").child(Common.currentUser.getBusinessNumber()).child("Days").child("Sunday");
////            case "Monday":
////                days = database.getReference().child("RestaurantGenie").child(Common.currentUser.getBusinessNumber()).child("Days").child("Monday");
////            case "Tuesday":
////                days = database.getReference().child("RestaurantGenie").child(Common.currentUser.getBusinessNumber()).child("Days").child("Tuesday");
////            case "Wednesday":
////                days = database.getReference().child("RestaurantGenie").child(Common.currentUser.getBusinessNumber()).child("Days").child("Wednesday");
////            case "Thursday":
////                days = database.getReference().child("RestaurantGenie").child(Common.currentUser.getBusinessNumber()).child("Days").child("Thursday");
////            case "Friday":
////                days = database.getReference().child("RestaurantGenie").child(Common.currentUser.getBusinessNumber()).child("Days").child("Friday");
////            case "Saturday":
////                days = database.getReference().child("RestaurantGenie").child(Common.currentUser.getBusinessNumber()).child("Days").child("Saturday");
//
//        int caseNumber = pageViewModel.getmIndexValue() - 1;
//        System.out.println("mohammad sharabati: pageNumber " + pageNumber + ">>>>>" + "hg");
//        System.out.println("mohammad sharabati: " + (caseNumber) + ">>>>>" + "hg");
////        System.out.println("mohammad sharabati: " + (pageViewModel.getmIndexValue()-1));
//
//        switch (caseNumber) {
//            case 0:
//                days = database.getReference().child("RestaurantGenie").child(Common.currentUser.getBusinessNumber()).child("Day").child("Sunday");
//                break;
//            case 1:
//                days = database.getReference().child("RestaurantGenie").child(Common.currentUser.getBusinessNumber()).child("Day").child("Monday");
//                break;
//            case 2:
//                days = database.getReference().child("RestaurantGenie").child(Common.currentUser.getBusinessNumber()).child("Day").child("Tuesday");
//                break;
//            case 3:
//                days = database.getReference().child("RestaurantGenie").child(Common.currentUser.getBusinessNumber()).child("Day").child("Wednesday");
//                break;
//            case 4:
//                days = database.getReference().child("RestaurantGenie").child(Common.currentUser.getBusinessNumber()).child("Day").child("Thursday");
//                break;
//            case 5:
//                days = database.getReference().child("RestaurantGenie").child(Common.currentUser.getBusinessNumber()).child("Day").child("Friday");
//                break;
//            case 6:
//                days = database.getReference().child("RestaurantGenie").child(Common.currentUser.getBusinessNumber()).child("Day").child("Saturday");
//                break;
//        }
//
////        //load menu
//        recycler_fragment_day = view.findViewById(R.id.recycler_fragment_day);
//        recycler_fragment_day.setHasFixedSize(true);
//        layoutManager = new LinearLayoutManager(getActivity());
//        recycler_fragment_day.setLayoutManager(layoutManager);
//        loadTimeTable();
//
//
//    }
//
//    private void loadTimeTable() {
//
//
//        System.out.println("mohammad sharabati: >>>>> loadTimeTable");
//        System.out.println("mohammad sharabati: >>>>> days" + days);
//        config = new PagedList.Config.Builder()
//                .setEnablePlaceholders(false)
//                .setPrefetchDistance(10)
//                .setPageSize(20)
//                .build();
//        options = new DatabasePagingOptions.Builder<TimeWorker>()
//                .setLifecycleOwner(this)
//                .setQuery(days, config, TimeWorker.class)
//                .build();
//        adapterFirebase = new FirebaseRecyclerPagingAdapter<TimeWorker, TimeWorkerViewHolder>(options) {
//            @Override
//            protected void onBindViewHolder(@NonNull TimeWorkerViewHolder holder, int position, @NonNull TimeWorker model) {
//
//                Log.d("TAG", "mohammad sharabati: >>>>> loadTimeTable name:" + model.getName());
//                System.out.println("mohammad sharabati: >>>>> loadTimeTable name:" + model.getName());
//                System.out.println("mohammad sharabati: >>>>> loadTimeTable startTime:" + model.getStartTime());
//                System.out.println("mohammad sharabati: >>>>> loadTimeTable endTime:" + model.getEndTime());
//
//                holder.endTime.setText(model.getEndTime());
//                holder.name.setText(model.getName());
//                holder.startTime.setText(model.getEndTime());
//
////                // Increase item height for longer classes
////                if (model.getLength() > 1) {
////                    holder.view.layoutParams.height =
////                            (holder.view.context.resources.getDimension(R.dimen.class_item_height) * 2).toInt();
////                }
//                holder.setItemClickListener(new ItemClickListener() {
//                    @Override
//                    public void onClick(View view, int position, boolean isLongClick) {
//                    }
//                });
//            }
//
//            @Override
//            protected void onLoadingStateChanged(@NonNull LoadingState state) {
//
//            }
//
//            @NonNull
//            @Override
//            public TimeWorkerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//                View view = LayoutInflater.from(parent.getContext())
//                        .inflate(R.layout.day_item, parent, false);
//                return new TimeWorkerViewHolder(view);
//            }
//
//        };
//        adapterFirebase.startListening();
//        recycler_fragment_day.setAdapter(adapterFirebase);
//
//
////        options = new FirebaseRecyclerOptions.Builder<TimeWorker>()
////                .setQuery(days, TimeWorker.class)
////                .build();
////
////        adapterFirebase = new FirebaseRecyclerAdapter<TimeWorker, TimeWorkerViewHolder>(options) {
////
////            @Override
////            protected void onBindViewHolder(@NonNull TimeWorkerViewHolder holder, int position, @NonNull final TimeWorker model) {
//////                Picasso.with(getBaseContext())
//////                        .load(model.getImage())
//////                        .into(holder.imageView);
////                System.out.println("mohammad sharabati: >>>>> loadTimeTable name:"+model.getName());
////                System.out.println("mohammad sharabati: >>>>> loadTimeTable startTime:"+model.getStartTime());
////                System.out.println("mohammad sharabati: >>>>> loadTimeTable endTime:"+model.getEndTime());
////                holder.name.setText(model.getName());
////                holder.startTime.setText(model.getStartTime());
////                holder.endTime.setText(model.getEndTime());
////                holder.setItemClickListener(new ItemClickListener() {
////                    @Override
////                    public void onClick(View view, int position, boolean isLongClick) {
////                        // send categoryId and start new activity
//////                        Intent foodList = new Intent(Home.this, FoodList.class);
//////                        foodList.putExtra("CategoryId", adapter.getRef(position).getKey());
//////                        startActivity(foodList);
////                    }
////                });
////            }
////            @NonNull
////            @Override
////            public TimeWorkerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
////                View view = LayoutInflater.from(parent.getContext())
////                        .inflate(R.layout.day_item, parent, false);
////                return new TimeWorkerViewHolder(view);
////            }
////        };
////        adapterFirebase.startListening();
////        recycler_fragment_day.setAdapter(adapterFirebase);
////        adapterFirebase.startListening();
////        adapterFirebase.notifyDataSetChanged(); // Refresh data if have data changed
////        recycler_fragment_day.setAdapter(adapterFirebase);
//    }
//
////    @Override
////    public void onStop() {
////        super.onStop();
////        if (adapterFirebase != null)
////            adapterFirebase.stopListening();
////    }
//}
//
//
////    private RecyclerView recycler_fragment_day;
////    private  DatabasePagingOptions<TimeWorker> options;
////    private FirebaseRecyclerPagingAdapter<TimeWorker, TimeWorkerViewHolder> adapter;
////    private FirebaseDatabase database;
////    private DatabaseReference timeWorkers;
////    PagedList.Config config;
////
////    /**
////     * The fragment argument representing the section number for this
////     * fragment.
////     */
////    public static final String ARG_Day = "day";
////
////    /**
////     * Returns a new instance of this fragment for the given section
////     * number.
////     */
////    public static DayFragment newInstance(int tabday) {
////        DayFragment fragment = new DayFragment();
////        Bundle args = new Bundle();
////        args.putInt(ARG_Day, tabday);
////        fragment.setArguments(args);
////        return fragment;
////    }
////
////    public DayFragment() {
////
////                //Init firebase
////        database = FirebaseDatabase.getInstance();
////        timeWorkers = database.getReference().child("RestaurantGenie").child(Common.currentUser.getBusinessNumber()).child("Days").child("Sunday");
////    }
////
////    @Override
////    public View onCreateView(LayoutInflater inflater, ViewGroup container,
////                             Bundle savedInstanceState) {
////        View rootView = inflater.inflate(R.layout.activity_time_table, container, false);
////
////        recycler_fragment_day = (RecyclerView) rootView.findViewById(R.id.recycler_fragment_day);
////        setupAdapter(getArguments().getInt(ARG_Day) - 1);
////        recycler_fragment_day.setLayoutManager(new LinearLayoutManager(getContext()));
////        recycler_fragment_day.setAdapter(adapter);
////        return rootView;
////    }
////
////    private void setupAdapter(final int n) {
////
////
////        config = new PagedList.Config.Builder()
////                .setEnablePlaceholders(false)
////                .setPrefetchDistance(10)
////                .setPageSize(20)
////                .build();
////
////
////        options = new DatabasePagingOptions.Builder<TimeWorker>()
////                .setLifecycleOwner(this)
////                .setQuery(timeWorkers, config, TimeWorker.class)
////                .build();
////
////        adapter = new FirebaseRecyclerPagingAdapter<TimeWorker, TimeWorkerViewHolder>(options) {
////            @Override
////            protected void onBindViewHolder(@NonNull TimeWorkerViewHolder holder, int position, @NonNull TimeWorker model) {
////                holder.endTime.setText(model.getEndTime());
////                holder.name.setText(model.getName());
////                holder.startTime.setText(model.getEndTime());
////
////                // Increase item height for longer classes
////                if (model.getLength() > 1) {
////                    holder..view.layoutParams.height =
////                            (holder.view.context.resources.getDimension(R.dimen.class_item_height) * 2).toInt();
////                }
////                holder.setItemClickListener(new ItemClickListener() {
////                    @Override
////                    public void onClick(View view, int position, boolean isLongClick) {
////                    }
////                });
////            }
////
////            @Override
////            protected void onLoadingStateChanged(@NonNull LoadingState state) {
////
////            }
////
////            @NonNull
////            @Override
////            public TimeWorkerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
////                View view = LayoutInflater.from(parent.getContext())
////                        .inflate(R.layout.day_item, parent, false);
////                return new TimeWorkerViewHolder(view);
////            }
////
////        };
////
////        adapter.startListening();
////        recycler_fragment_day.setAdapter(adapter);
////
//        //Instead of choosing a different branch, the same thing could be done, be sorting by a child
//        //or only choosing by some child or variable of the model. This could act as a tag or sorts
////
////        DatabaseReference tempRef = mRef.child(Constants.PAGE + "_" + n);
////        Log.v("PlaceHolder", tempRef.toString());
////        adapter = new FirebaseRecyclerAdapter<String, TimeWorkerViewHolder>(
////                String.class,
////                R.layout.item_card,
////                ViewPagerViewHolder.class,
////                tempRef) {
////            @Override
////            protected void populateViewHolder(ViewPagerViewHolder viewHolder, String model, int position) {
////                viewHolder.setPositionText(n, position);
////            }
////        };
////    }
////
////    public static class ViewPagerViewHolder extends RecyclerView.ViewHolder {
////        private TextView positionTV;
////
////        public ViewPagerViewHolder(View itemView) {
////            super(itemView);
////            positionTV = (TextView) itemView.findViewById(R.id.position_tv);
////        }
////
////        public void setPositionText(int page, int n) {
////            positionTV.setText("PAGE: " + page + ", " + n);
////        }
////    }
////
////
////
////
////
////    private static final String ARG_ID = "id";
////    private static final String ARG_NAME = "name";
////
////    private int id;
////    private String name;
////
////    public DayFragment() {
////        // Required empty public constructor
////    }
////
////    public static DayFragment newInstance(int id, String name) {
////        DayFragment fragment = new DayFragment();
////        Bundle args = new Bundle();
////        args.putInt(ARG_ID, id);
////        args.putString(ARG_NAME, name);
////        fragment.setArguments(args);
////        return fragment;
////    }
////
////    @Override
////    public void onCreate(Bundle savedInstanceState) {
////        super.onCreate(savedInstanceState);
////        if (getArguments() != null) {
////            id = getArguments().getInt(ARG_ID);
////            name = getArguments().getString(ARG_NAME);
////        }
////    }
////
////    @Override
////    public View onCreateView(LayoutInflater inflater, ViewGroup container,
////                             Bundle savedInstanceState) {
////        // Inflate the layout for this fragment
////        return inflater.inflate(R.layout.activity_time_table, container, false);
////    }
////
////    @Override
////    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
////        super.onViewCreated(view, savedInstanceState);
////
////
////
////    }
////}
