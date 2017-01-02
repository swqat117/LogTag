package com.quascenta.petersroad.fragments;

/**
 * Created by AKSHAY on 11/3/2016.
 */

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.OvershootInterpolator;
import android.widget.ImageView;
import android.widget.TextView;

import com.quascenta.petersroad.MyApp;
import com.quascenta.petersroad.broadway.R;
import com.quascenta.petersroad.activities.DetailSensorsActivity;
import com.quascenta.petersroad.Utils.Objectgenerator;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import jp.wasabeef.recyclerview.adapters.SlideInLeftAnimationAdapter;
import jp.wasabeef.recyclerview.animators.SlideInLeftAnimator;

public class CheeseListFragment extends Fragment {
    SimpleStringRecyclerViewAdapter simpleStringRecyclerViewAdapter ;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        RecyclerView rv = (RecyclerView) inflater.inflate(
                R.layout.test_cheeselist, container, false);
        setupRecyclerView(rv);



        return rv;
    }

    private void setupRecyclerView(RecyclerView recyclerView) {
        simpleStringRecyclerViewAdapter =  new SimpleStringRecyclerViewAdapter(getActivity(), MyApp.devices);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        animate(recyclerView);
    }

    void animate(RecyclerView recyclerView){
        SlideInLeftAnimationAdapter slideInLeftAnimationAdapter = new SlideInLeftAnimationAdapter(simpleStringRecyclerViewAdapter);
        slideInLeftAnimationAdapter.setDuration(1343);
        slideInLeftAnimationAdapter.setFirstOnly(false);
        recyclerView.setItemAnimator(new SlideInLeftAnimator());
        recyclerView.setAdapter(new SlideInLeftAnimationAdapter(simpleStringRecyclerViewAdapter));
        slideInLeftAnimationAdapter.setInterpolator(new OvershootInterpolator());
        recyclerView.getItemAnimator().setAddDuration(1000);
        recyclerView.getItemAnimator().setRemoveDuration(1000);
        recyclerView.getItemAnimator().setMoveDuration(1000);
        recyclerView.getItemAnimator().setChangeDuration(1000);
    }

    private List<String> getRandomSublist(String[] array, int amount) {
        ArrayList<String> list = new ArrayList<>(amount);
        Random random = new Random();
        while (list.size() < amount) {
            list.add(array[random.nextInt(array.length)]);
        }
        return list;
    }

    public static class SimpleStringRecyclerViewAdapter
            extends RecyclerView.Adapter<SimpleStringRecyclerViewAdapter.ViewHolder> {
        private Context context;
        private final TypedValue mTypedValue = new TypedValue();
        private int mBackground;
        private ArrayList<Objectgenerator> devices;
        private List<String> mValues;
        private List<String> mValues1;
        private List<String> mValues3;

         static class ViewHolder extends RecyclerView.ViewHolder {
            String mBoundString;

            final View mView;
            final ImageView iv_status,iv2_status;
            final TextView mSource_company_name,mSource_location;
            final TextView mDestination_company_name,mDestination_location;
            final TextView customer_tracking_id,status_state;

             ViewHolder(View view) {
                super(view);
                mView = view;
                 iv_status = (ImageView)mView.findViewById(R.id.iv_icon_alert);
                iv2_status = (ImageView)mView.findViewById(R.id.iv2_icon_alert);
                mSource_company_name = (TextView)mView.findViewById(R.id.source_company_name);
                mSource_location = (TextView)mView.findViewById(R.id.source_location);
                mDestination_company_name = (TextView)mView.findViewById(R.id.destination_company_name);
                mDestination_location = (TextView)mView.findViewById(R.id.destination_location);
                customer_tracking_id = (TextView)mView.findViewById(R.id.customer_tracking_id);
                status_state = (TextView)mView.findViewById(R.id.status_state);




            }

            @Override
            public String toString() {
                return super.toString() + " '" + status_state.getText();
            }
        }

        public String getValueAt(int position) {
            return devices.get(position).toString();
        }

         SimpleStringRecyclerViewAdapter(Context context, ArrayList<Objectgenerator> devices) {

             this.context = context;
             mBackground = mTypedValue.resourceId;
             this.devices = devices;

        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.list_item6, parent, false);
            view.setBackgroundResource(mBackground);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, int position) {
            holder.mBoundString = devices.get(position).toString();
            holder.mSource_company_name.setText(devices.get(position).getDevice_source_company());
            holder.mDestination_company_name.setText(devices.get(position).getDevice_destination_location());
            holder.mSource_location.setText(devices.get(position).getDevice_source_location());
            holder.mDestination_location.setText(devices.get(position).getDevice_destination_company());
            holder.customer_tracking_id.setText(devices.get(position).getCustomer_tracking_id());
            holder.status_state.setText("Data not Uploaded");
            holder.mView.setOnClickListener(v -> {
                Intent intent = new Intent(context, DetailSensorsActivity.class);
                System.out.println("pass on click");
                intent.putExtra(DetailSensorsActivity.EXTRA_NAME, holder.mBoundString);
                 context.startActivity(intent);
            });


        }

        @Override
        public int getItemCount() {
            return devices.size();
        }
    }
}
