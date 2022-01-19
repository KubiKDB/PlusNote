package com.example.plusnote.adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.plusnote.activities.PagerFragment2;

public class MyAdapter2 extends FragmentStateAdapter {
    public MyAdapter2(FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        return(PagerFragment2.newInstance(position));
    }

    @Override
    public int getItemCount() {
        return 262;
    }
}
