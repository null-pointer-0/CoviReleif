package com.example.covirelief.Adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import java.util.ArrayList;
import java.util.List;

public class FragmentAdapter extends FragmentStatePagerAdapter {
    List<Fragment> flist = new ArrayList();

    public FragmentAdapter(@NonNull FragmentManager fm) {
        super(fm);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        return flist.get(position);
    }
    @Override
    public CharSequence getPageTitle(int position){
        if(position == 0) return "Needs";
        else return "Availabilities";
    }
    public void add(Fragment frag){
        flist.add(frag);
    }
    @Override
    public int getCount() {
        return flist.size();
    }
}
