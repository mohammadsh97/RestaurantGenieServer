package com.mohammadsharabati.restaurantgenieserver.Adapter;


import android.view.View;
import android.view.ViewGroup;

import com.mohammadsharabati.restaurantgenieserver.Model.TabDays;
import com.mohammadsharabati.restaurantgenieserver.R;

import java.util.ArrayList;
import java.util.List;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.recyclerview.widget.RecyclerView;


public class PagerAdapter extends FragmentStatePagerAdapter {

    private List<TabDays> tabDaysList = new ArrayList<>();


    public PagerAdapter(@NonNull FragmentManager fm) {
        super(fm);
//        this.mContext = mContext;
    }

    public void addDays(TabDays tabDay) {
        tabDaysList.add(tabDay);
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
//        return mContext.getResources().getString(TAB_TITLES[position]);
        return tabDaysList.get(position).getName();
    }

    @Override
    public Fragment getItem(int i) {
        // getItem is called to instantiate the fragment for the given page.
        // Return a PlaceholderFragment (defined as a static inner class below).
//        return DayFragment.newInstance(i + 1);
        return tabDaysList.get(i).getFragment();
    }

    @Override
    public int getCount() {
        return tabDaysList.size();
    }

    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    // Removes the page from the container for the given position.
    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }


}






//
//    Context context;
//    ArrayList<TabDay> tabDays = new ArrayList<>();
//    LayoutInflater inflater;
//
//    public PagerAdapter(FragmentManager fm) {
//        super(fm);
//    }
//
//        public void addTabDays(TabDay tab){
//            tabDays.add(tab);
//    }
//
//    public Object instantiateItem(ViewGroup view, int position) {
//        View itemView = inflater.inflate(R.layout.day_item, view, false);
//
//
//        TextView startTime = view.findViewById(R.id.startTime);
//        TextView endTime = view.findViewById(R.id.endTime);
//        TextView nameWorker = view.findViewById(R.id.nameWorker);
//
//        startTime.setText(tabDays.get(position).getTimeWorker().getStartTime());
//        endTime.setText(tabDays.get(position).getTimeWorker().getEndTime());
//        nameWorker.setText(tabDays.get(position).getTimeWorker().getName());
//
//        view.addView(itemView);
//
//
//        return itemView;
//    }
//
//
//    @NonNull
//    @Override
//    public Fragment getItem(int position) {
//        return tabDays.get(position).getFragment();
//    }
//
//        @Override
//    public CharSequence getPageTitle(int position) {
//
//        return tabDays.get(position).getTabName();
//
//            switch (position) {
//                case 0:
//                    return "Sunday";
//                case 1:
//                    return "Monday";
//                case 2:
//                    return "Tuesday";
//                case 3:
//                    return "Wednesday";
//                case 4:
//                    return "Thursday";
//                case 5:
//                    return "Friday";
//                case 6:
//                    return "Saturday";
//            }
//            return null;
//        }
//
//    public int getCount() {
//        return tabDays.size();
//    }
//
//    public void destroyItem(ViewGroup container, int position, Object object) {
//        container.removeView((LinearLayout) object);
//    }
//
//    public boolean isViewFromObject(View view, Object object) {
//        return view == object;
//    }
//}
//
////
////    Context context;
////    List<TabDay> tabDays;
////    LayoutInflater inflater;
////
////    public PagerAdapter(@NonNull FragmentManager fm, Context context, List<TabDay> tabDays) {
////        super(fm);
////        this.context = context;
////        this.tabDays = tabDays;
////    }
////
////    public PagerAdapter(@NonNull FragmentManager fm) {
////        super(fm);
////    }
////
////
////
////    @NonNull
////    @Override
////    public Fragment getItem(int position) {
////        return tabDays.get(position).getFragment();
////    }
////
////    @Override
////    public int getCount() {
////        return tabDays.size();
////    }
////
////
////    @Override
////    public CharSequence getPageTitle(int position) {
////        switch (position) {
////            case 0:
////                return "Sunday";
////            case 1:
////                return "Monday";
////            case 2:
////                return "Tuesday";
////            case 3:
////                return "Wednesday";
////            case 4:
////                return "Thursday";
////            case 5:
////                return "Friday";
////            case 6:
////                return "Saturday";
////        }
////        return null;
////    }
////
////    Context context;
////    List<TabDay> tabDays;
////    LayoutInflater inflater;
////
////    public PagerAdapter(@NonNull FragmentManager fm, Context context, List<TabDay> tabDays) {
////        super(fm);
////        this.context = context;
////        this.tabDays = tabDays;
////    }
////
////
////    @NonNull
////    @Override
////    public Fragment getItem(int position) {
////        return tabDays.get(position).getFragment();
////    }
////
////    @Override
////    public int getCount() {
////        return tabDays.size();
////    }
////
////    @NonNull
////    @Override
////    public Object instantiateItem(@NonNull ViewGroup container, int position) {
////        View view = inflater.inflate(R.layout.day_item,container,false);
////
////
////        RecyclerView recycler_fragment_day;
////        RecyclerView.LayoutManager layoutManager;
////        DatabasePagingOptions<TimeWorker> options;
////        FirebaseRecyclerPagingAdapter<TimeWorker, TimeWorkerViewHolder> adapter;
////        PagedList.Config config;
////        ArrayList<TabDay> days = new ArrayList<>();
////    PagerAdapter adapters = new PagerAdapter(getSupportFragmentManager());
////
////
////
////
////        TextView startTime = view.findViewById(R.id.startTime);
////        TextView endTime = view.findViewById(R.id.endTime);
////        TextView nameWorker = view.findViewById(R.id.nameWorker);
////
////        startTime.setText(tabDays.get(position).getTimeWorker().getStartTime());
////        endTime.setText(tabDays.get(position).getTimeWorker().getEndTime());
////        nameWorker.setText(tabDays.get(position).getTimeWorker().getName());
////
////
////
////                config = new PagedList.Config.Builder()
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
//////                // Increase item height for longer classes
//////                if (model.getLength() > 1) {
//////                    holder..view.layoutParams.height =
//////                            (holder.view.context.resources.getDimension(R.dimen.class_item_height) * 2).toInt();
//////                }
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
////
////
////
////
////
////
////
////        view.setOnClickListener(new View.OnClickListener() {
////            @Override
////            public void onClick(View v) {
////                Toast.makeText(context, "cliked", Toast.LENGTH_SHORT).show();
////            }
////        });
////        container.addView(view);
////        return view;
////    }
//
//
//    //    ArrayList<TabDay> tabs = new ArrayList<>();
////
////    public PagerAdapter(@NonNull FragmentManager fm) {
////        super(fm);
////    }
////
////    public void addTab(TabDay tab){
////        tabs.add(tab);
////    }
////
////    @NonNull
////    @Override
////    public Fragment getItem(int position) {
////        return tabs.get(position).getFragment();
////    }
////
////    @Nullable
////    @Override
////    public CharSequence getPageTitle(int position) {
////        return tabs.get(position).getTabName();
////    }
////
////    @Override
////    public int getCount() {
////        return tabs.size();
////    }
//
//
