package com.quascenta.QBlueLogger;

/**
 * Created by AKSHAY on 1/16/2017.
 */

public class Device {
    private int id;

    private int deviceServices;
    private String deviceName;
    private String deviceMac;

    public Device(int id, String deviceName, String deviceMac, int deviceServices) {
        this.id = id;
        this.deviceName = deviceName;
        this.deviceMac = deviceMac;
        this.deviceServices = deviceServices;
    }

    public int getDeviceServices() {
        return deviceServices;
    }

    public void setDeviceServices(int deviceServices) {
        this.deviceServices = deviceServices;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public String getDeviceMac() {
        return deviceMac;
    }

    public void setDeviceMac(String deviceMac) {
        this.deviceMac = deviceMac;
    }
}
