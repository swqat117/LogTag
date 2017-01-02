package com.quascenta.petersroad.services;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.RequestFuture;

import org.json.JSONObject;

import java.nio.charset.Charset;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;
import timber.log.Timber;

/**
 * Created by AKSHAY on 11/4/2016.
 */

public class SensorService extends IntentService {


     private static final String TAG = "VolleyIntentService";
    private static final String TAG1 = "JsonObjectRequest";
    public  static final String RESPONSE_STRING ="response_string" ;
    private List<String> Sensordata;
    private CompositeSubscription compositeSubscription = new CompositeSubscription();

    public SensorService(){
        super("Sensor service");

    }

    @Override
    protected void onHandleIntent(Intent intent) {
        //Adding bri
        System.out.println("pass ser1");
        startVolleyRequest();
    }




    public Observable<JSONObject> newGetRouteData() {
        return Observable.defer(() -> {
            try {
                return Observable.just(getRouteData());
            } catch (InterruptedException | ExecutionException e) {
                Log.e("routes", e.getMessage());
                return Observable.error(e);
            }
        }).retryWhen(new RetryWithDelay(3,10)).repeatWhen(completed -> completed.delay(30, TimeUnit.SECONDS));
    }
    private void startVolleyRequest() {
        compositeSubscription.add(newGetRouteData().subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<JSONObject>() {
                    @Override
                    public void onCompleted() {
                        Log.e(TAG, "onCompleted");
                        Timber.d("----- onCompleted");

                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        VolleyError cause = (VolleyError) e.getCause();
                        String s = new String(cause.networkResponse.data, Charset.forName("UTF-8"));
                        Log.e(TAG, s);
                        Log.e(TAG, cause.toString());


                    }

                    @Override
                    public void onNext(JSONObject jsonObject) {
                        Log.e(TAG, "onNext " + jsonObject.toString());
                        System.out.println("JSONOBJECT ==="+jsonObject.toString());


                    }
                }));
    }

    private JSONObject getRouteData() throws ExecutionException, InterruptedException {
        RequestFuture<JSONObject> future = RequestFuture.newFuture();
        String url = "http://192.168.1.249/cgi-bin/data";
        JsonObjectRequest req = new JsonObjectRequest(Request.Method.GET, url, future, future);
        MyVolley.init(this);
        MyVolley.getRequestQueue().add(req);

        return future.get();
    }
}
