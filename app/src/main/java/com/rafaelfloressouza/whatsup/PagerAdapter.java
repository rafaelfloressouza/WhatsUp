package com.rafaelfloressouza.whatsup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.ArrayList;

public class PagerAdapter extends FragmentPagerAdapter {

//    private final ArrayList<Fragment> lastFragment = new ArrayList<>();
//    private final ArrayList<String> lastTitles = new ArrayList<>();


    private int mTabsNumber;

    public PagerAdapter(@NonNull FragmentManager fm, int behavior, int tabNumber) {
        super(fm, behavior);
        this.mTabsNumber = tabNumber;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {

        // Selection the right fragment to display
        switch (position) {
            case 0:
                return new ChatsFragment();
            case 1:
                return new GroupsFragment();
            case 2:
                return new CallsFragment();
            default:
                return null;
        }

//      return lastFragment.get(position);
    }

    @Override
    public int getCount() {
        return mTabsNumber;
//        return lastFragment.size();
    }

//    @Nullable
//    @Override
//    public CharSequence getPageTitle(int position) {
//        return lastTitles.get(position);
//    }


//    public void AddFragment (Fragment fragment, String title)
//
//
//
//


}
