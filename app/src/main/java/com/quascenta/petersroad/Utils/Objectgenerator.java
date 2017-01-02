package com.quascenta.petersroad.Utils;

import java.util.Date;

/**
 * Created by AKSHAY on 12/19/2016.
 */

public class Objectgenerator {


    private String customer_tracking_id;
    private String device_serial_number;
    private String device_source_location;
    private String device_source_company;
    private String device_destination_location;
    private String device_destination_company;
    private Date device_start_date;
    private Date device_end_date;
    private int flag;


    public Objectgenerator(String customer_tracking_id, String device_serial_number, String device_source_location, String device_source_company, String device_destination_location, String device_destination_company, int x){
        this.customer_tracking_id = customer_tracking_id;
        this.device_serial_number = device_serial_number;
        this.device_source_location = device_source_location;
        this.device_source_company = device_source_company;
        this.device_destination_company = device_destination_company;
        this.device_destination_location = device_destination_location;

        flag = x;
    }

    public Objectgenerator() {

    }

    public String getCustomer_tracking_id() {
        return customer_tracking_id;
    }

    public void setCustomer_tracking_id(String customer_tracking_id) {
        this.customer_tracking_id = customer_tracking_id;
    }

    public String getDevice_destination_company() {
        return device_destination_company;
    }

    public void setDevice_destination_company(String device_destination_company) {
        this.device_destination_company = device_destination_company;
    }

    public String getDevice_destination_location() {
        return device_destination_location;
    }

    public void setDevice_destination_location(String device_destination_location) {
        this.device_destination_location = device_destination_location;
    }

    public Date getDevice_end_date() {
        return device_end_date;
    }

    public void setDevice_end_date(Date device_end_date) {
        this.device_end_date = device_end_date;
    }

    public String getDevice_serial_number() {
        return device_serial_number;
    }

    public void setDevice_serial_number(String device_serial_number) {
        this.device_serial_number = device_serial_number;
    }

    public String getDevice_source_company() {
        return device_source_company;
    }

    public void setDevice_source_company(String device_source_company) {
        this.device_source_company = device_source_company;
    }

    public String getDevice_source_location() {
        return device_source_location;
    }

    public void setDevice_source_location(String device_source_location) {
        this.device_source_location = device_source_location;
    }

    public Date getDevice_start_date() {
        return device_start_date;
    }

    public void setDevice_start_date(Date device_start_date) {
        this.device_start_date = device_start_date;
    }

    public int getFlag() {
        return flag;
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }
}
