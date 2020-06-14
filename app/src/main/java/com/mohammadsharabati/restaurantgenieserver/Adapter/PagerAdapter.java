package com.mohammadsharabati.restaurantgenieserver.Adapter;


import android.view.View;
import android.view.ViewGroup;
import com.mohammadsharabati.restaurantgenieserver.Model.TabDays;
import java.util.ArrayList;
import java.util.List;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

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

//    // Removes the page from the container for the given position.
//    @Override
//    public void destroyItem(ViewGroup container, int position, Object object) {
//        container.removeView((View) object);
//    }
}