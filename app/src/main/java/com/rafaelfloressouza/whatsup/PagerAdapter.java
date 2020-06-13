package com.rafaelfloressouza.whatsup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import java.util.Map;

public class PagerAdapter extends FragmentPagerAdapter {

    private int mTabsNumber;
    private Map<String, String> userMap;
    private Map<String, String> groupMap;

    public PagerAdapter(@NonNull FragmentManager fm, int behavior, int tabNumber, Map<String, String> userMap, Map<String, String> groupMap) {
        super(fm, behavior);
        this.mTabsNumber = tabNumber;
        this.userMap = userMap;
        this.groupMap = groupMap;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {

        // Selection the right fragment to display
        switch (position) {
            case 0:
                return new ChatsFragment(userMap);
            case 1:
                return new GroupsFragment(groupMap);
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
