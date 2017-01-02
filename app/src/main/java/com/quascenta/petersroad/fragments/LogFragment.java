package com.quascenta.petersroad.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.quascenta.petersroad.adapters.ExpandableListAdapter;
import com.quascenta.petersroad.broadway.R;
import com.quascenta.petersroad.model.device_log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by AKSHAY on 11/7/2016.
 */

public class LogFragment extends Fragment {
    private Subscription mSubscription;
    @Bind(R.id.recyclerview_loadSensorsList)
    RecyclerView recyclerView;
    ExpandableListAdapter.Item sensors1  = new ExpandableListAdapter.Item(ExpandableListAdapter.HEADER, "Sensor1");
    ExpandableListAdapter.Item sensors2  = new ExpandableListAdapter.Item(ExpandableListAdapter.HEADER, "Sensor2");
    ExpandableListAdapter.Item sensors3  = new ExpandableListAdapter.Item(ExpandableListAdapter.HEADER, "Sensor3");
    ExpandableListAdapter.Item sensors4  = new ExpandableListAdapter.Item(ExpandableListAdapter.HEADER, "Sensor4");
    ExpandableListAdapter.Item sensors5  = new ExpandableListAdapter.Item(ExpandableListAdapter.HEADER, "Sensor5");
    ExpandableListAdapter.Item sensors6  = new ExpandableListAdapter.Item(ExpandableListAdapter.HEADER, "Sensor6");
    ExpandableListAdapter.Item sensors7  = new ExpandableListAdapter.Item(ExpandableListAdapter.HEADER, "Sensor7");
    ExpandableListAdapter.Item sensors8  = new ExpandableListAdapter.Item(ExpandableListAdapter.HEADER, "Sensor8");
    List<ExpandableListAdapter.Item> data = new ArrayList<>();

    OnHeadlineSelectedListener mCallback;

    public interface OnHeadlineSelectedListener {
        public void onArticleSelected(int position);
    }

    int i = 0;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_content_list2, container, false);;
        ButterKnife.bind(this, view);


        return view;
    }


    @SuppressWarnings("unchecked")
    List<ExpandableListAdapter.Item> add1(ArrayList<device_log> a) {
        ArrayList<HashMap<String, String>> bundle = new ArrayList<>();
        Iterator<device_log> iterator = a.iterator();
        List<ExpandableListAdapter.Item> data = new ArrayList<>();
        sensors1  = new ExpandableListAdapter.Item(ExpandableListAdapter.HEADER, "Sensor1");
        sensors2  = new ExpandableListAdapter.Item(ExpandableListAdapter.HEADER, "Sensor2");
        sensors3  = new ExpandableListAdapter.Item(ExpandableListAdapter.HEADER, "Sensor3");
        sensors4  = new ExpandableListAdapter.Item(ExpandableListAdapter.HEADER, "Sensor4");
        sensors5  = new ExpandableListAdapter.Item(ExpandableListAdapter.HEADER, "Sensor5");
        sensors6  = new ExpandableListAdapter.Item(ExpandableListAdapter.HEADER, "Sensor6");
        sensors7  = new ExpandableListAdapter.Item(ExpandableListAdapter.HEADER, "Sensor7");
        sensors8  = new ExpandableListAdapter.Item(ExpandableListAdapter.HEADER, "Sensor8");
        sensors1.invisibleChildren = new ArrayList<>();
        sensors2.invisibleChildren = new ArrayList<>();
        sensors3.invisibleChildren = new ArrayList<>();
        sensors4.invisibleChildren = new ArrayList<>();
        sensors5.invisibleChildren = new ArrayList<>();
        sensors6.invisibleChildren = new ArrayList<>();
        sensors7.invisibleChildren = new ArrayList<>();
        sensors8.invisibleChildren = new ArrayList<>();
        sensors1.invisibleChildren.clear();
        sensors2.invisibleChildren.clear();
        sensors3.invisibleChildren.clear();
        sensors4.invisibleChildren.clear();
        sensors5.invisibleChildren.clear();
        sensors6.invisibleChildren.clear();
        sensors7.invisibleChildren.clear();
        sensors8.invisibleChildren.clear();
        //After second call just to clear the values out and start fresh
        //Starting while loop to assign point values to the line
        int i = 0;
        while (iterator.hasNext()) {
            i++;
            device_log device_log = iterator.next();
            switch (device_log.getSen1()) {
                case "-":
                    sensors1.invisibleChildren.add(new ExpandableListAdapter.Item(ExpandableListAdapter.CHILD, device_log.getDatetime()+" WAS INVALID"));
                    break;
                case "8989.0":
                    sensors1.invisibleChildren.add(new ExpandableListAdapter.Item(ExpandableListAdapter.CHILD, device_log.getDatetime()+" was Disconnected"));
                    break;
                default:
                    sensors1.invisibleChildren.add(new ExpandableListAdapter.Item(ExpandableListAdapter.CHILD, device_log.getDatetime()+" was valid "+device_log.getSen1()+"C"));
                    break;
            }

            switch (device_log.getSen2()) {
                case "-":
                    sensors2.invisibleChildren.add(new ExpandableListAdapter.Item(ExpandableListAdapter.CHILD, device_log.getDatetime()+" WAS INVALID"));
                    break;
                case "8989.0":
                    sensors2.invisibleChildren.add(new ExpandableListAdapter.Item(ExpandableListAdapter.CHILD, device_log.getDatetime()+" was Disconnected"));
                    break;
                default:
                    sensors2.invisibleChildren.add(new ExpandableListAdapter.Item(ExpandableListAdapter.CHILD, device_log.getDatetime()+" was valid "+device_log.getSen2()+"C"));
                    break;
            }

            switch (device_log.getSen3()) {
                case "-":
                    sensors3.invisibleChildren.add(new ExpandableListAdapter.Item(ExpandableListAdapter.CHILD, device_log.getDatetime()+" WAS INVALID"));
                    break;
                case "8989.0":
                    sensors3.invisibleChildren.add(new ExpandableListAdapter.Item(ExpandableListAdapter.CHILD, device_log.getDatetime()+" was Disconnected"));
                    break;
                default:
                    sensors3.invisibleChildren.add(new ExpandableListAdapter.Item(ExpandableListAdapter.CHILD, device_log.getDatetime()+" was valid "+device_log.getSen3()+"C"));
                    break;
            }

            switch (device_log.getSen4()) {
                case "-":
                    sensors4.invisibleChildren.add(new ExpandableListAdapter.Item(ExpandableListAdapter.CHILD, device_log.getDatetime()+" WAS INVALID"));
                    break;
                case "8989.0":
                    sensors4.invisibleChildren.add(new ExpandableListAdapter.Item(ExpandableListAdapter.CHILD, device_log.getDatetime()+" was Disconnected"));
                    break;
                default:
                    sensors4.invisibleChildren.add(new ExpandableListAdapter.Item(ExpandableListAdapter.CHILD, device_log.getDatetime()+" was valid "+device_log.getSen4()+"C"));
                    break;
            }

            switch (device_log.getSen5()) {
                case "-":
                    sensors5.invisibleChildren.add(new ExpandableListAdapter.Item(ExpandableListAdapter.CHILD, device_log.getDatetime()+" WAS INVALID"));
                    break;
                case "8989.0":
                    sensors5.invisibleChildren.add(new ExpandableListAdapter.Item(ExpandableListAdapter.CHILD, device_log.getDatetime()+" was Disconnected"));
                    break;
                default:
                    sensors5.invisibleChildren.add(new ExpandableListAdapter.Item(ExpandableListAdapter.CHILD, device_log.getDatetime()+" was valid "+device_log.getSen5()+"C"));
                    break;
            }


            switch (device_log.getSen6()) {
                case "-":
                    sensors6.invisibleChildren.add(new ExpandableListAdapter.Item(ExpandableListAdapter.CHILD, device_log.getDatetime()+" WAS INVALID"));
                    break;
                case "8989.0":
                    sensors6.invisibleChildren.add(new ExpandableListAdapter.Item(ExpandableListAdapter.CHILD, device_log.getDatetime()+" was Disconnected"));
                    break;
                default:
                    sensors6.invisibleChildren.add(new ExpandableListAdapter.Item(ExpandableListAdapter.CHILD, device_log.getDatetime()+" was valid "+device_log.getSen6()+"C"));
                    break;
            }
            switch (device_log.getSen7()) {
                case "-":
                    sensors7.invisibleChildren.add(new ExpandableListAdapter.Item(ExpandableListAdapter.CHILD, device_log.getDatetime()+" WAS INVALID"));
                    break;
                case "8989.0":
                    sensors7.invisibleChildren.add(new ExpandableListAdapter.Item(ExpandableListAdapter.CHILD, device_log.getDatetime()+" was Disconnected"));
                    break;
                default:
                    sensors7.invisibleChildren.add(new ExpandableListAdapter.Item(ExpandableListAdapter.CHILD, device_log.getDatetime()+" was valid "+device_log.getSen7()+"C"));
                    break;
            }

            switch (device_log.getSen8()) {
                case "-":
                    sensors8.invisibleChildren.add(new ExpandableListAdapter.Item(ExpandableListAdapter.CHILD, device_log.getDatetime()+" WAS INVALID"));
                    break;
                case "8989.0":
                    sensors8.invisibleChildren.add(new ExpandableListAdapter.Item(ExpandableListAdapter.CHILD, device_log.getDatetime()+" was Disconnected"));
                    break;
                default:
                    sensors8.invisibleChildren.add(new ExpandableListAdapter.Item(ExpandableListAdapter.CHILD, device_log.getDatetime()+" was valid "+device_log.getSen8()+"C"));
                    break;
            }
        }
        data.clear();
        data.add(sensors1);
        data.add(sensors2);
        data.add(sensors3);
        data.add(sensors4);
        data.add(sensors5);
        data.add(sensors6);
        data.add(sensors7);
        data.add(sensors8);

        return data;

    }

        @Override
        public void onAttach (Context context){
            super.onAttach(context);
            try {
                mCallback = (OnHeadlineSelectedListener) context;
            } catch (ClassCastException e) {
                throw new ClassCastException(context.toString()
                        + " must implement OnHeadlineSelectedListener");
            }
        }

    public void updateUI(ArrayList<device_log> device_logs) {
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(new ExpandableListAdapter(data));
        mSubscription = Observable.just(device_logs).map( s -> add1(s)).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribe( s -> recyclerView.setAdapter(new ExpandableListAdapter(s)));






    }


}



