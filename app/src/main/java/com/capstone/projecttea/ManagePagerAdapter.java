package com.capstone.projecttea;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

public class ManagePagerAdapter extends FragmentStatePagerAdapter {
    int numOfTabs;
    public ManagePagerAdapter(FragmentManager fragmentManager,int numOfTabs) {
        super(fragmentManager);

        this.numOfTabs = numOfTabs;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new ManageItem();
            case 1:
                return new FragmentAddons();
            case 2:
                return new FragmentSeries();
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return numOfTabs;
    }
}
