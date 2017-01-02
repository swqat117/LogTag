package com.quascenta.petersroad.model;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT. Enable "keep" sections if you want to edit. 

import com.opencsv.bean.CsvBindByName;
import com.quascenta.petersroad.Utils.CSVAnnotation;

import java.util.ArrayList;

/**
 * Entity mapped to table DEVICE_LOG.
 */
public class device_log {
    @CsvBindByName(column = "Data ID")
    private String data_id;

    @CsvBindByName(column = "Config ID")
    private String config_id;

    @CsvBindByName(column = "DateTime")
    private String datetime;

    @CsvBindByName(column = "SEN 1")
    private String sen1;

    @CsvBindByName(column = "SEN 2")
    private String sen2;

    @CsvBindByName(column = "SEN 3")
    private String sen3;

    @CsvBindByName(column = "SEN 4")
    private String sen4;

    @CsvBindByName (column = "SEN 5")
    private String sen5;

    @CsvBindByName(column = "SEN 6")
    private String sen6;

    @CsvBindByName(column = "SEN 7")
    private String sen7;

    @CsvBindByName(column = "SEN 8")
    private String sen8;

    private String time;

    public device_log(String data_id,String config_id,String datetime, String sen1, String sen2, String sen3, String sen4) {

        this.data_id = data_id;
        this.config_id = config_id;
        this.datetime = datetime;
        this.sen1 = sen1;
        this.sen2 = sen2;
        this.sen3 = sen3;
        this.sen4 = sen4;
    }


    public Boolean getLoaded() {
        return isLoaded;
    }




    public void setLoaded(Boolean loaded) {
        isLoaded = loaded;
    }

    private Boolean isLoaded = false;

    public device_log(String id,String senid,String datetime, String sen1, String sen2){

        this.data_id = id;
        this.config_id = senid;
        this.datetime = datetime;
        this.sen1 = sen1;
        this.sen2 = sen2;

    }


    public device_log(String data_id, String config_id, String datetime, String sen1, String sen2, String sen3, String sen4, String sen5, String sen6, String sen7, String sen8) {
        this.data_id = data_id;
        this.config_id = config_id;
        this.datetime = datetime;
        this.sen1 = sen1;
        this.sen2 = sen2;
        this.sen3 = sen3;
        this.sen4 = sen4;
        this.sen5 = sen5;
        this.sen6 = sen6;
        this.sen7 = sen7;
        this.sen8 = sen8;
    }
    public void add(ArrayList<String> data){
        this.data_id = data.get(0);
        this.config_id = data.get(1);
        this.datetime = data.get(2);
        this.sen1 = data.get(3);
        this.sen2 = data.get(4);
        this.sen3 = data.get(5);
        this.sen4 = data.get(6);
        this.sen5 = data.get(7);
        this.sen6 = data.get(8);
        this.sen7 = data.get(9);
        this.sen8 = data.get(10);
        this.time = alterDate(data.get(2));

    }
    public String alterDate(String datetime){
        String x = datetime.substring(9);
        return x;
    }
    public String getTime(){
        return time;
    }

    public String getData_id() {
        return data_id;
    }

    public void setData_id(String data_id) {
        this.data_id = data_id;
    }

    public String getConfig_id() {
        return config_id;
    }

    public void setConfig_id(String config_id) {
        this.config_id = config_id;
    }

    public String getDatetime() {
        return datetime;
    }

    public void setDatetime(String datetime) {
        this.datetime = datetime;
    }

    public String getSen1() {
        return sen1;
    }

    public void setSen1(String sen1) {
        this.sen1 = sen1;
    }

    public String getSen2() {
        return sen2;
    }
     void setSen2(String sen2) {
        this.sen2 = sen2;
    }

    public String getSen3() {
        return sen3;
    }

    public void setSen3(String sen3) {
        this.sen3 = sen3;
    }

    public String getSen4() {
        return sen4;
    }

    public void setSen4(String sen4) {
        this.sen4 = sen4;
    }

    public String getSen5() {
        return sen5;
    }

    public void setSen5(String sen5) {
        this.sen5 = sen5;
    }

    public String getSen6() {
        return sen6;
    }

    public void setSen6(String sen6) {
        this.sen6 = sen6;
    }

    public String getSen7() {
        return sen7;
    }

    public void setSen7(String sen7) {
        this.sen7 = sen7;
    }

    public String getSen8() {
        return sen8;
    }

    public void setSen8(String sen8) {
        this.sen8 = sen8;
    }

}