package com.quascenta.petersroad.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.quascenta.petersroad.broadway.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by AKSHAY on 11/17/2016.
 */

public class ExpandableListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    public static final int HEADER = 0;
    public static final int CHILD = 1;
    OnSensorSelected l;

    private List<Item> data;

    public ExpandableListAdapter(List<Item> data) {
        this.data = data;
    }






    public interface OnSensorSelected {
        public void sensorSelected(int position,String x);
    }
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int type) {
        View view = null;

        Context context = parent.getContext();
        try {
            l = (ExpandableListAdapter.OnSensorSelected) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement OnHeadlineSelectedListener");
        }
        float dp = context.getResources().getDisplayMetrics().density;
        int subItemPaddingLeft = (int) (18 * dp);
        int subItemPaddingTopAndBottom = (int) (5 * dp);
        switch (type) {
            case HEADER:
                LayoutInflater inflater = (LayoutInflater) parent.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                view = inflater.inflate(R.layout.list_header, parent, false);
                ListHeaderViewHolder header = new ListHeaderViewHolder(view);
                return header;
            case CHILD:
                TextView itemTextView = new TextView(context);
                itemTextView.setPadding(subItemPaddingLeft, subItemPaddingTopAndBottom, 0, subItemPaddingTopAndBottom);
                itemTextView.setTextColor(0x88000000);
                itemTextView.setLayoutParams(
                        new ViewGroup.LayoutParams(
                                ViewGroup.LayoutParams.MATCH_PARENT,
                                ViewGroup.LayoutParams.WRAP_CONTENT));
                return new RecyclerView.ViewHolder(itemTextView) {
                };
        }
        return null;
    }

    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final Item item = data.get(position);
        switch (item.type) {
            case HEADER:
                final ListHeaderViewHolder itemController = (ListHeaderViewHolder) holder;
                itemController.refferalItem = item;
                itemController.header_title.setText(item.text);
                if (item.invisibleChildren == null) {
                    itemController.btn_expand_toggle.setImageResource(R.drawable.circle_minus);
                } else {
                    itemController.btn_expand_toggle.setImageResource(R.drawable.circle_plus);
                }
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Click(item,itemController);
                        LoadGraph(item,itemController,l);
                    }
                });
                itemController.btn_expand_toggle.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                            Click(item,itemController);
                    }
                });


                break;
            case CHILD:
                TextView itemTextView = (TextView) holder.itemView;
                itemTextView.setText(data.get(position).text);
                break;
        }
    }

    @Override
    public int getItemViewType(int position) {
        return data.get(position).type;
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    private static class ListHeaderViewHolder extends RecyclerView.ViewHolder {
        TextView header_title;
        ImageView btn_expand_toggle;
        public Item refferalItem;

        public ListHeaderViewHolder(View itemView) {
            super(itemView);
            header_title = (TextView) itemView.findViewById(R.id.header_title);
            btn_expand_toggle = (ImageView) itemView.findViewById(R.id.btn_expand_toggle);
        }
    }
    void Click(Item item,ListHeaderViewHolder itemController){

        if (item.invisibleChildren == null) {
            item.invisibleChildren = new ArrayList<Item>();
            int count = 0;
            int pos = data.indexOf(itemController.refferalItem);
            while (data.size() > pos + 1 && data.get(pos + 1).type == CHILD) {
                item.invisibleChildren.add(data.remove(pos + 1));
                count++;
            }
            notifyItemRangeRemoved(pos + 1, count);
            itemController.btn_expand_toggle.setImageResource(R.drawable.circle_plus);
        } else {
            int pos = data.indexOf(itemController.refferalItem);
            int index = pos + 1;
            for (Item i : item.invisibleChildren) {
                data.add(index, i);
                index++;
            }
            notifyItemRangeInserted(pos + 1, index - pos - 1);
            itemController.btn_expand_toggle.setImageResource(R.drawable.circle_minus);
            item.invisibleChildren = null;
        }

            }
    void LoadGraph(Item item,ListHeaderViewHolder itemController,OnSensorSelected listener){
            switch (item.type){
            case HEADER:
               String x = itemController.header_title.getText().toString();
                listener.sensorSelected(item.type,x);

        }

    }

    public static class Item {
        public int type;
        public String text;
        public List<Item> invisibleChildren;

        public Item() {
        }

        public Item(int type, String text) {
            this.type = type;
            this.text = text;
        }
    }
}