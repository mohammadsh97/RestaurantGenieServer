package com.mohammadsharabati.restaurantgenieserver;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import com.google.android.material.tabs.TabLayout;
import com.mohammadsharabati.restaurantgenieserver.Adapter.PagerAdapter;
import com.mohammadsharabati.restaurantgenieserver.Fragment.DayFragment;
import com.mohammadsharabati.restaurantgenieserver.Model.TabDays;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

public class TimeTable extends AppCompatActivity {

    private static final int[] TAB_TITLES = new int[]{R.string.Sunday, R.string.Monday, R.string.Tuesday,
            R.string.Wednesday, R.string.Thursday, R.string.Friday, R.string.Saturday};

    TabLayout tabLayout;
    ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_time_table);
        Toolbar toolbar = findViewById(R.id.toolbar);
        if(getSupportActionBar() == null)
            setSupportActionBar(toolbar);
        else toolbar.setVisibility(View.GONE);
        getSupportActionBar().setTitle("Time table");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        viewPager = findViewById(R.id.pager);
        viewPager.setOffscreenPageLimit(TAB_TITLES.length);
        tabLayout = findViewById(R.id.tabs);
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);


        PagerAdapter pagerAdapter = new PagerAdapter(getSupportFragmentManager());
        for (int position = 0; position < TAB_TITLES.length; position++) {

            String nameTab = this.getResources().getString(TAB_TITLES[position]);

            pagerAdapter.addDays(new TabDays(nameTab, DayFragment.newInstance((position),
                    nameTab)));
        }

        viewPager.setAdapter(pagerAdapter);
        tabLayout.setupWithViewPager(viewPager);


        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
                Toast.makeText(TimeTable.this, "my click", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

    }
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}




//public class TimeTable extends AppCompatActivity {
//
//
//    private static final int[] TAB_TITLES = new int[]{R.string.Sunday, R.string.Monday, R.string.Tuesday,
//            R.string.Wednesday, R.string.Thursday, R.string.Friday, R.string.Saturday};
//
//    TabLayout tabLayout;
//    ViewPager viewPager;
//
//    FirebaseDatabase database;
//    DatabaseReference timeWorkers;
//    RecyclerView recycler_fragment_day;
//    RecyclerView.LayoutManager layoutManager;
//    DatabasePagingOptions<TimeWorker> options;
//    FirebaseRecyclerPagingAdapter<TimeWorker, TimeWorkerViewHolder> adapter;
//    PagedList.Config config;
////    ArrayList<TabDay> days = new ArrayList<>();
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_time_table);
//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
//
//        viewPager =  findViewById(R.id.pager);
//        tabLayout =  findViewById(R.id.tabs);
//
//        PagerAdapter PagerAdapter = new PagerAdapter(getSupportFragmentManager(),this);
//
//
//        viewPager.setAdapter(PagerAdapter);
//        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
//
//        // give the Tablayout the viewPager
//        tabLayout.setupWithViewPager(viewPager);
//
//
//        FloatingActionButton fab = findViewById(R.id.fab);
//
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });
//
//
//
////        PagerAdapter adapter = new PagerAdapter(getSupportFragmentManager());
////
////        adapter.addTabDays(new TabDay("Sunday",DayFragment.newInstance(1,"mohammad")));
////        adapter.addTabDays(new TabDay("Monday",DayFragment.newInstance(2,"mohammad")));
////        adapter.addTabDays(new TabDay("Tuesday",DayFragment.newInstance(3,"mohammad")));
////        adapter.addTabDays(new TabDay("Wednesday",DayFragment.newInstance(4,"mohammad")));
////        adapter.addTabDays(new TabDay("Thursday",DayFragment.newInstance(5,"mohammad")));
////        adapter.addTabDays(new TabDay("Friday",DayFragment.newInstance(6,"mohammad")));
////        adapter.addTabDays(new TabDay("Saturday",DayFragment.newInstance(6,"mohammad")));
////
////        viewPager.setAdapter(adapter);
////        tabLayout.setupWithViewPager(viewPager);
//    }
//}
////
////    private void addTabs(ViewPager viewPager) {
////        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
////        adapter.addFrag(new DayFragment(), "APPLE");
////        adapter.addFrag(new OrangeFragment(), "ORANGE");
////        adapter.addFrag(new GrapesFragment(), "GRAPES");
////        adapter.addFrag(new BananaFragment(), "Banana");
////        viewPager.setAdapter(adapter);
////
////
//        //        adapters.addTab(new TabDay("Monday" , DayFragment.newInstance(1,"mohammad")));
////        adapters.addTab(new TabDay("Tuesday" , DayFragment.newInstance(2,"lolo")));
////        adapters.addTab(new TabDay("Wednesday" , DayFragment.newInstance(3,"lolo")));
////        adapters.addTab(new TabDay("Thursday" , DayFragment.newInstance(4,"lolo")));
////        adapters.addTab(new TabDay("Friday" , DayFragment.newInstance(5,"lolo")));
////        adapters.addTab(new TabDay("Saturday" , DayFragment.newInstance(6,"lolo")));
////
////
////    }
////
////    class ViewPagerAdapter extends FragmentPagerAdapter {
////        private final List<Fragment> mFragmentList = new ArrayList<>();
////        private final List<String> mFragmentTitleList = new ArrayList<>();
////
////        public ViewPagerAdapter(FragmentManager manager) {
////            super(manager);
////        }
////
////        @Override
////        public Fragment getItem(int position) {
////            return mFragmentList.get(position);
////        }
////
////        @Override
////        public int getCount() {
////            return mFragmentList.size();
////        }
////
////        public void addFrag(Fragment fragment, String title) {
////            mFragmentList.add(fragment);
////            mFragmentTitleList.add(title);
////        }
////
////        @Override
////        public CharSequence getPageTitle(int position) {
////            return mFragmentTitleList.get(position);
////        }
////    }
////
////
////
////
////}
////
////
////
////
////
////
////
////
////
////
////
////
////
////
////
////
////
////
////    PagerAdapter adapters = new PagerAdapter(getSupportFragmentManager());
////
////
////    @Override
////    protected void onCreate(Bundle savedInstanceState) {
////        super.onCreate(savedInstanceState);
////        setContentView(R.layout.activity_time_table);
////        Toolbar toolbar = findViewById(R.id.toolbar);
////        toolbar.setTitle("Time Table");
////        setSupportActionBar(toolbar);
////
////        tabLayout = findViewById(R.id.tabs);
////        viewPager = findViewById(R.id.pager);
////
////        //Init firebase
////        database = FirebaseDatabase.getInstance();
////        timeWorkers = FirebaseDatabase.getInstance().getReference().child("RestaurantGenie").child(Common.currentUser.getBusinessNumber()).child("Days").child("Sunday");
////
////
////        timeWorkers.addListenerForSingleValueEvent(new ValueEventListener() {
////
////            List<TimeWorker> timeWorkers = new ArrayList<>();
////            @Override
////            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
////                for(DataSnapshot timeWorkersDataSnapshot:dataSnapshot.getChildren())
////                    timeWorkers.add(timeWorkersDataSnapshot.getValue(TimeWorker.class));
////
////            }
////        });
////        viewPager.setAdapter(adapter);
////        tabLayout.setupWithViewPager(viewPager);
////
////
////        //Adding the tabs using addTab() method
////        tabLayout.addTab(tabLayout.newTab().setText(R.string.Sunday));
////        tabLayout.addTab(tabLayout.newTab().setText(R.string.Monday));
////        tabLayout.addTab(tabLayout.newTab().setText(R.string.Tuesday));
////        tabLayout.addTab(tabLayout.newTab().setText(R.string.Wednesday));
////        tabLayout.addTab(tabLayout.newTab().setText(R.string.Thursday));
////        tabLayout.addTab(tabLayout.newTab().setText(R.string.Friday));
////        tabLayout.addTab(tabLayout.newTab().setText(R.string.Saturday));
////
//////        Adding the tabs using addTab() method
////
////        adapters.addTab(new TabDay("Monday" , DayFragment.newInstance(1,"mohammad")));
////        adapters.addTab(new TabDay("Tuesday" , DayFragment.newInstance(2,"lolo")));
////        adapters.addTab(new TabDay("Wednesday" , DayFragment.newInstance(3,"lolo")));
////        adapters.addTab(new TabDay("Thursday" , DayFragment.newInstance(4,"lolo")));
////        adapters.addTab(new TabDay("Friday" , DayFragment.newInstance(5,"lolo")));
////        adapters.addTab(new TabDay("Saturday" , DayFragment.newInstance(6,"lolo")));
////        tabLayout.addTab(tabLayout.newTab().setText(R.string.Monday));
////        tabLayout.addTab(tabLayout.newTab().setText(R.string.Tuesday));
////        tabLayout.addTab(tabLayout.newTab().setText(R.string.Wednesday));
////        tabLayout.addTab(tabLayout.newTab().setText(R.string.Thursday));
////        tabLayout.addTab(tabLayout.newTab().setText(R.string.Friday));
////        tabLayout.addTab(tabLayout.newTab().setText(R.string.Saturday));
////        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
////
////        viewPager.setAdapter(adapter);
////        tabLayout.setupWithViewPager(viewPager);
////
////
////        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
////            @Override
////            public void onTabSelected(TabLayout.Tab tab) {
////
////            }
////
////            @Override
////            public void onTabUnselected(TabLayout.Tab tab) {
////
////            }
////
////            @Override
////            public void onTabReselected(TabLayout.Tab tab) {
////
////            }
////        });
////
////        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
////            @Override
////            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
////
////            }
////
////            @Override
////            public void onPageSelected(int position) {
////
////            }
////
////            @Override
////            public void onPageScrollStateChanged(int state) {
////
////            }
////        });
////
////
////
////
////        //Init firebase
////        database = FirebaseDatabase.getInstance();
////        timeWorkers = database.getReference().child("RestaurantGenie").child(Common.currentUser.getBusinessNumber()).child("Days").child("Sunday");
////
////
////        FloatingActionButton fab = findViewById(R.id.fab);
////        fab.setOnClickListener(new View.OnClickListener() {
////            @Override
////            public void onClick(View view) {
////            }
////        });
////
//////        //load menu
////        recycler_fragment_day = (RecyclerView) findViewById(R.id.recycler_fragment_day);
////        recycler_fragment_day.setHasFixedSize(true);
////        layoutManager = new LinearLayoutManager(this);
////        recycler_fragment_day.setLayoutManager(layoutManager);
////        loadTimeTable();
////
////    }
////
////    private void loadTimeTable() {
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
////                .setQuery(timeWorkers,config, TimeWorker.class)
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
////            @NonNull
////            @Override
////            public TimeWorkerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
////                View view = LayoutInflater.from(parent.getContext())
////                        .inflate(R.layout.day_item, parent, false);
////                return new TimeWorkerViewHolder(view);
////            }
////
////            @Override
////            protected void onLoadingStateChanged(@NonNull LoadingState state) {
////                switch (state) {
////                    case LOADING_INITIAL:
////                        // The initial load has begun
////                        // ...
////                    case LOADING_MORE:
////                        // The adapter has started to load an additional page
////                        // ...
////                    case LOADED:
////                        // The previous load (either initial or additional) completed
////                        // ...
////                    case ERROR:
////                        // The previous load (either initial or additional) failed. Call
////                        // the retry() method in order to retry the load operation.
////                        // ...
////                }
////            }
////        };
////
////        adapter.startListening();
////        recycler_fragment_day.setAdapter(adapter);
////
////        //Set Adapter to RecyclerView
////        recycler_fragment_day.setAdapter(adapter);
////    }
////
////}
