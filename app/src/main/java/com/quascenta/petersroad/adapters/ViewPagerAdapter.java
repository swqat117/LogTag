package com.quascenta.petersroad.adapters;

import android.support.v4.app.FragmentManager;
import android.support.v4.view.PagerAdapter;
import android.view.View;

/**
 * Created by AKSHAY on 12/22/2016.
 */
public class ViewPagerAdapter extends PagerAdapter {
    public ViewPagerAdapter(FragmentManager supportFragmentManager, CharSequence[] titles, int numbofTabs) {
    }

    @Override
    public int getCount() {
        return 0;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return false;
    }
}
