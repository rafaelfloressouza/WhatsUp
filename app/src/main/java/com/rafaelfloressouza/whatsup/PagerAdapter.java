package com.rafaelfloressouza.whatsup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class PagerAdapter extends FragmentPagerAdapter {

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
    }

    @Override
    public int getCount() {
        return mTabsNumber;
    }
}
