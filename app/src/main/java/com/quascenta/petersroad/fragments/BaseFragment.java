package com.quascenta.petersroad.fragments;

import android.support.v4.app.Fragment;

import com.quascenta.petersroad.MyApp;
import com.squareup.leakcanary.RefWatcher;

/**
 * Created by AKSHAY on 10/27/2016.
 */


    public class BaseFragment
            extends Fragment {

        @Override
        public void onDestroy() {
            super.onDestroy();
            RefWatcher refWatcher = MyApp.getRefWatcher();
            refWatcher.watch(this);
        }


    }
