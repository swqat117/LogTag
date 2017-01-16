package com.quascenta.QBlueLogger;

import android.content.Context;

import java.util.List;

/**
 * Created by AKSHAY on 1/16/2017.
 */
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.quascenta.petersroad.broadway.R;

public class MyDevicesAdapter extends BaseAdapter {
    private LayoutInflater inflater=null;
    private List<Device> devices;
    private int connectedDeviceId = 0;

    public MyDevicesAdapter(Context c, List<Device> devices, int id) {
        this.devices = devices;
        this.connectedDeviceId = id;

        inflater = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public int getCount() {
        return devices.size();
    }

    public Object getItem(int position) {
        return position;
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(final int position, View convertView, ViewGroup parent) {
        View vi = convertView;

        if(vi == null)
            vi = inflater.inflate(R.layout.list_device_entry, null);

        TextView name = (TextView) vi.findViewById(R.id.device_name);
        TextView mac = (TextView) vi.findViewById(R.id.device_mac);
        TextView services = (TextView) vi.findViewById(R.id.device_services);

        name.setText(devices.get(position).getDeviceName());
        mac.setText(devices.get(position).getDeviceMac());
        services.setText(String.valueOf(devices.get(position).getDeviceServices()));

        if(devices.get(position).getId() == connectedDeviceId)
            vi.setBackgroundColor(Color.rgb(221,255,211));
        else
            vi.setBackgroundColor(0);

        return vi;
    }

    /**
     * Method used to refresh the listView
     */
    public void updateDevices(List<Device> devices, int id) {
        this.devices = devices;
        this.connectedDeviceId = id;
    }

    public List<Device> getList(){
        return this.devices;
    }
}
