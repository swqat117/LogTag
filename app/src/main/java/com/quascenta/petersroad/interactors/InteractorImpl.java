package com.quascenta.petersroad.interactors;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.RequestFuture;
import com.android.volley.toolbox.StringRequest;
import com.quascenta.petersroad.Utils.ToStringConverterFactory;
import com.quascenta.petersroad.Utils.Utils;
import com.quascenta.petersroad.MyApp;
import com.quascenta.petersroad.model.DaoSession;
import com.quascenta.petersroad.model.device_log;
import com.quascenta.petersroad.services.MyVolley;
import com.quascenta.petersroad.services.RetryWithDelay;
import com.quascenta.petersroad.interfaces.Interactor;


import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.http.GET;
import retrofit2.http.Url;
import rx.Observable;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;
import timber.log.Timber;

/**
 * Created by AKSHAY on 11/10/2016.
 */

public class InteractorImpl implements Interactor {
    private String url;
    private static final String TAG = "VolleyIntentService";
    private ArrayList<ArrayList<String>> crunchifyResult;
    private CompositeSubscription compositeSubscription = new CompositeSubscription();
    private Context context;
    private DaoSession daoSession;
    private device_log[] device_log;


    public InteractorImpl(Context context){
            this.context = context;
             daoSession  = ((MyApp) context).getDaoSession();

    }

    @Override
    public void RetrieveCsv(String x,OnFinishedListener listener) {
        url = x;
       // startVolleyRequest(listener);
       // listener.onSuccess(startVolleyRequest());
        new LongOperation(listener).execute(url);


    }



    private Observable<String> newGetRouteData() {
        return Observable.defer(() -> {
            try {
                return Observable.just(getRouteData());
            } catch (InterruptedException | ExecutionException e) {
                Log.e("routes", e.getMessage());
                return Observable.error(e);
            }
        }).retryWhen(new RetryWithDelay(3,10));
    }


    private void startVolleyRequest(OnFinishedListener listener) {
        compositeSubscription.add(newGetRouteData().subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<String>() {
                    @Override
                    public void onCompleted() {
                        Log.e(TAG, "onCompleted");
                        Timber.d("----- onCompleted");
                        listener.onSuccess(RipSensorData(device_log));

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
                    public void onNext(String jsonObject) {

                        try {
                            crunchifyResult  = Utils.creater(MakeFile(jsonObject));
                            addtoDB(crunchifyResult);


                        } catch (IOException e) {
                            e.printStackTrace();
                        }


                    }
                }));

    }

    private String getRouteData() throws ExecutionException, InterruptedException {

        RequestFuture<String> future = RequestFuture.newFuture();
        String url1 = setUrl(url);
        StringRequest req = new StringRequest(Request.Method.GET, url1, future, future);
        MyVolley.init(context);
        MyVolley.getRequestQueue().add(req);
        req.setRetryPolicy(new DefaultRetryPolicy(5000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        return future.get();
    }


    private String setUrl(String x) {
        return "http://192.168.1.249"+x+".csv/";
    }

    private File MakeFile(String x) throws IOException {
        final File file = new File(context.getCacheDir(), "test_image.csv");

               try {
                FileWriter fileWriter = new FileWriter(file);
                   BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
                   bufferedWriter.write(x);
               } catch (IOException e) {
                   e.printStackTrace();
               }



        return file;
    }

    /*public  void checkFile(File file) throws IOException{
        BufferedReader in = new BufferedReader(new FileReader(file));
        String line;
        while((line = in.readLine()) != null)
        {
            System.out.println(line);
        }
        in.close();
    }

    public void readCsvFile(File file) throws IOException{
        String[] row;
        CSVReader csvReader = new CSVReader(new FileReader(file));
        List content = csvReader.readAll();

        for (Object object : content) {
            row = (String[]) object;

            System.out.println(row[0]
                    + " # " + row[1]
                    + " #  " + row[2]);
        }
//...
        csvReader.close();
    }*/

    private void addtoDB(ArrayList<ArrayList<String>> data){
        device_log = new device_log[data.size()-1];


                ArrayList<String> data1;
                for (int i = 0; i< data.size()-1; i++){
                    data1 = data.get(i);
                    device_log[i] = new device_log(data1.get(0),data1.get(1), data1.get(2),data1.get(3),data1.get(4),data1.get(5),data1.get(6),data1.get(7),data1.get(8),data1.get(9),data1.get(10));
                    device_log[i].setLoaded(true);
                //    device_logDao device_logDao = daoSession.getDevice_logDao();
                //    device_logDao.insertOrReplace(device_log[i]);
                }


        }

    private void offlineaddtoDB(ArrayList<ArrayList<String>> data){
        device_log = new device_log[data.size()];
        ArrayList<String> data1;
        for (int i = 0; i< data.size()-1; i++) {
            data1 = data.get(i);
            device_log[i] = new device_log(data1.get(0), data1.get(1), data1.get(2), data1.get(3), data1.get(4), data1.get(5), data1.get(6));
            device_log[i].setLoaded(true);
        }
    }

    interface Service {
        @GET()
        Call<String> getBody(@Url String anEmptyString);
    }

    private String StringRetrofitRequest(String x) throws IOException {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(setUrl(x))
                .addConverterFactory(new ToStringConverterFactory())
                .build();

        String ur = x.substring(0)+".csv";
        System.out.println("--------------------"+ur);
        Service example = retrofit.create(Service.class);
        Response response = example.getBody("").execute();

            return response.body().toString();


    }
    @SuppressWarnings("deprecation")
    private String  RipCsv() throws IOException {
        StringBuilder buf=new StringBuilder();
        InputStream json= context.getResources().getAssets().open("161111.CSV");
        BufferedReader in=
                new BufferedReader(new InputStreamReader(json, "UTF-8"));
        String str;

        while ((str=in.readLine()) != null) {
            buf.append(in.readLine());

        }
        String x = buf.toString();
        in.close();
        return x;

    }

    private ArrayList<device_log> RipSensorData( device_log device_log[]){
        ArrayList<device_log> sensors = new ArrayList<>();
                //Taking logs everyhour from csv
               for(int i  = 1;i<device_log.length; i++){
                   if(device_log[i].getDatetime().endsWith("00:00") || device_log[i].getDatetime().substring(12).equals("00:00"))
                   sensors.add(device_log[i]);
                }
           System.out.println("Size of array"+sensors.size());
           System.out.println("ist value"+ sensors.get(0).getDatetime() + sensors.get(1).getDatetime());

        return sensors;
        }


    private class LongOperation extends AsyncTask<String, Void, ArrayList<device_log> >{
        private OnFinishedListener listener = null;
        public LongOperation(OnFinishedListener listener) {
            this.listener = listener;
        }

        @Override
        protected ArrayList<device_log> doInBackground(String... strings) {
          String body = strings[0];

            try {
               // crunchifyResult  = Utils.creater(MakeFile(StringRetrofitRequest(body)));
                crunchifyResult  = Utils.creater(new File(String.valueOf(context.getResources().getAssets().open("161111.CSV"))));
                //addtoDB(crunchifyResult);
                offlineaddtoDB(crunchifyResult);


            }
            catch (IOException e) {
                e.printStackTrace();
            }
        return RipSensorData(device_log);
        }

        @Override
        protected void onPostExecute(ArrayList<device_log> arrayList) {
            super.onPostExecute(arrayList);
            listener.onSuccess(arrayList);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }
    }



  }
