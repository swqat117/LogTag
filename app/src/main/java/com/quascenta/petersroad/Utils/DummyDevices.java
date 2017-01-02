package com.quascenta.petersroad.Utils;

import com.quascenta.petersroad.broadway.R;

import java.util.Random;

/**
 * Created by AKSHAY on 12/8/2016.
 */

public class DummyDevices {

    private static final Random RANDOM = new Random();

    public static int getRandomCheeseDrawable() {
        switch (RANDOM.nextInt(5)) {
            default:
            case 0:
                return R.drawable.message_alert;
            case 1:
                return R.drawable.ic_error_black_24dp;
            case 2:
                return R.drawable.cheese_4;
            case 3:
                return R.drawable.cheese_4;
            case 4:
                return R.drawable.cheese_5;
        }
    }

    public static final String[] sLocationStrings = {"MAA","DXB","SFO","AFG","ALB","CAY","CGO","BAN"};
    public static final String[] sLocationCompanies = {"Easy Solutions","Tata Consultancy Services","Cognizant Technology services","Quascenta","Convergence Tech","LoremIpsom Ltd","Hindustan Unilever","Arial"};
    public static final String[] scust_track_id = {"#83121","#21321","#23332","#12121","#858585","#22212","#58124","#12345","#654321","#212122","#44545","#78787"};
    public static final String[] sid = {"11111111111","22222222222","3333333333","44444444444","12345678998","123456789989","21212121211","35456654454","88888888888","83183183183","83116516516","831138348355"};

    };



