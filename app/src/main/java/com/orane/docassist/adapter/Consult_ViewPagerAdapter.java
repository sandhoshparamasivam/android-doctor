package com.orane.docassist.adapter;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.orane.docassist.fragment.Consult_HistoryFragment;
import com.orane.docassist.fragment.Unconfirmed_Consult_Fragment;
import com.orane.docassist.fragment.Upcoming_Consult_Fragment;


public class Consult_ViewPagerAdapter extends FragmentPagerAdapter {

    public Consult_ViewPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment = null;
        if (position == 0) {
            fragment = new Upcoming_Consult_Fragment();
        } else if (position == 1) {
            fragment = new Unconfirmed_Consult_Fragment();
        } else if (position == 2) {
            fragment = new Consult_HistoryFragment();
        }
        return fragment;
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        String title = null;
        if (position == 0) {
            title = "UpComing";
        } else if (position == 1) {
            title = "Bookings";
        } else if (position == 2) {
            title = "History";
        }
        return title;
    }
}
