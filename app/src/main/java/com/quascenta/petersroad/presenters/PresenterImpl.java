package com.quascenta.petersroad.presenters;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;


import com.quascenta.petersroad.interactors.InteractorImpl;
import com.quascenta.petersroad.interfaces.IView;
import com.quascenta.petersroad.interfaces.Interactor;
import com.quascenta.petersroad.interfaces.Presenter;
import com.quascenta.petersroad.model.device_log;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import lecho.lib.hellocharts.model.Line;
import lecho.lib.hellocharts.model.PointValue;
import rx.Observable;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by AKSHAY on 11/7/2016.
 */

public class PresenterImpl implements Presenter,Interactor.OnFinishedListener {
     private IView iView;

    private ArrayList<Line> line;
    private Interactor interactor ;
    private Context context;
    private CompositeSubscription compositeSubscription;
    public PresenterImpl(IView view, Context c ){
        this.context = c;
        this.iView = view;
        this.interactor = new InteractorImpl(context);
        compositeSubscription = new CompositeSubscription();
    }
    @Override
    public void OnMonthSelected(String month) {
       ArrayList<Line> lines = new ArrayList<>();
        switch (month){


        }

    }

    @Override
    public void CallBack(ArrayList<device_log> data) {
                }



    @Override
    public void onDaySelected(int day, String month, int year) {
        Observable.just(month).map(s -> String.valueOf(year) + convertoValue(s) + String.format("%02d", day)).map(s -> "/"+s.substring(0,6)+"/"+s.substring(2)).subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread()).subscribe(s -> interactor.RetrieveCsv(s,this));
    }


    //Might need to be done at the interactor level but for now this.
    private String convertoValue(String x){
        switch (x){
            case "Jan":
                return "01";

            case "Feb":
                return "02";

            case "Mar":
                return "03";

            case "Apr":
                return "04";

            case "May":
                return "05";

            case "Jun":
                return "06";

            case "Jul":
                return  "07";

            case "Aug":
                return "08";

            case "Sep":
                return "09";

            case "Oct":
                return "10";

            case "Nov":
                return "11";

            case "Dec":
                return "12";

            default:
                return x;
        }
    }

    @Override
    public void updatechart() {

    }

    @Override
    public void updateLog() {

    }

    public void loadProgressBar( ProgressDialog progressDialog) {

            progressDialog.setCancelable(false);
            progressDialog.setMessage("Loading ...");
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.setIndeterminate(true);
            progressDialog.show();


        }




    private ArrayList<Line> change(ArrayList<device_log> s){
        Iterator<device_log> iterator = s.iterator();
        List<PointValue> values0 = new ArrayList<>();
        List<PointValue> values1 = new ArrayList<>();
        List<PointValue> values2 = new ArrayList<>();
        List<PointValue> values3 = new ArrayList<>();
        List<PointValue> values4 = new ArrayList<>();
        List<PointValue> values5 = new ArrayList<>();
        List<PointValue> values6 = new ArrayList<>();
        List<PointValue> values7 = new ArrayList<>();
        ArrayList<Line> lines = new ArrayList<>();
        int i = 0;
        while(iterator.hasNext()) {
            i++;
            device_log device_log = iterator.next();

            switch (device_log.getSen1()) {
                case "-":
                    values0.add(new PointValue(i, 0));
                    break;
                case "8989.0":
                    values0.add(new PointValue(i, 100));
                    break;
                default:
                    values0.add(new PointValue(i, Float.parseFloat(device_log.getSen1())));

                    break;
            }

            switch (device_log.getSen2()) {
                case "-":

                    values1.add(new PointValue(i, 0));
                    break;
                case "8989.0":
                    values1.add(new PointValue(i, 100));
                    break;
                default:
                    values1.add(new PointValue(i, Float.parseFloat(device_log.getSen2())));
                    break;
            }


            switch (device_log.getSen3()) {
                case "-":
                    values2.add(new PointValue(i, 0));

                    break;
                case "8989.0":
                    values2.add(new PointValue(i, 100));
                    break;
                default:
                    values2.add(new PointValue(i, Float.parseFloat(device_log.getSen3())));
                    break;
            }

            switch (device_log.getSen4()) {
                case "-":
                    values3.add(new PointValue(i, 0));
                    break;
                case "8989.0":
                    values3.add(new PointValue(i, 100));
                    break;
                default:
                    values3.add(new PointValue(i, Float.parseFloat(device_log.getSen4())));
                    break;
            }
            switch (device_log.getSen5()) {
                case "-":
                    values4.add(new PointValue(i, 0));

                    break;
                case "8989.0":
                    values4.add(new PointValue(i, 100));
                    break;
                default:
                    values4.add(new PointValue(i, Float.parseFloat(device_log.getSen5())));
                    break;
            }
            switch (device_log.getSen6()) {
                case "-":
                    values5.add(new PointValue(i, 0));

                    break;
                case "8989.0":
                    values5.add(new PointValue(i, 100));
                    break;
                default:
                    values5.add(new PointValue(i, Float.parseFloat(device_log.getSen6())));
                    break;
            }
            switch (device_log.getSen7()) {
                case "-":
                    values6.add(new PointValue(i, 0));

                    break;
                case "8989.0":
                    values6.add(new PointValue(i, 100));
                    break;
                default:
                    values6.add(new PointValue(i, Float.parseFloat(device_log.getSen7())));
                    break;
            }
            switch (device_log.getSen8()) {
                case "-":
                    values7.add(new PointValue(i, 0));

                    break;
                case "8989.0":
                    values7.add(new PointValue(i, 100));
                    break;
                default:
                    values7.add(new PointValue(i, Float.parseFloat(device_log.getSen8())));
                    break;
            }
        }


        Line line0 = new Line(values0);
        Line line1 = new Line(values1);
        Line line2 = new Line(values2);
        Line line3 = new Line(values3);
        Line line4 = new Line(values4);
        Line line5 = new Line(values5);
        Line line6 = new Line(values6);
        Line line7 = new Line(values7);
        line0.setHasLabels(true);
        line0.setHasLabelsOnlyForSelected(true);
        line1.setHasLabels(true);
        line1.setHasLabelsOnlyForSelected(true);
        line2.setHasLabels(true);
        line2.setHasLabelsOnlyForSelected(true);
        line3.setHasLabels(true);
        line3.setHasLabelsOnlyForSelected(true);
        line4.setHasLabels(true);
        line4.setHasLabelsOnlyForSelected(true);
        line5.setHasLabels(true);
        line5.setHasLabelsOnlyForSelected(true);
        line6.setHasLabels(true);
        line6.setHasLabelsOnlyForSelected(true);
        line7.setHasLabels(true);
        line7.setHasLabelsOnlyForSelected(true);
        line0.setHasPoints(true);
        line1.setHasPoints(true);
        line2.setHasPoints(true);
        line3.setHasPoints(true);
        line4.setHasPoints(true);
        line5.setHasPoints(true);
        line6.setHasPoints(true);
        line7.setHasPoints(true);
           lines.clear();
           lines.add(line0);
           lines.add(line1);
           lines.add(line2);
           lines.add(line3);
           lines.add(line4);
           lines.add(line5);
           lines.add(line6);
           lines.add(line7);




        return lines;
    }

     private void change(ArrayList<device_log> s,int a){
        new LongOperation().execute(s);
    }



    @Override
    public void onError() {

    }

    @Override
    public void onSuccess(ArrayList<device_log> device_logs) {
        //Using AsyncTask
         change(device_logs,0);
         iView.onSuccess(device_logs);
        //Using rx java
        //changeling(device_logs);
    }

    private void changeling(ArrayList<device_log> device_logs) {
        compositeSubscription.add(CreateLine(device_logs).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ArrayList<Line>>() {

                    @Override
                    public void onCompleted() {
                        iView.Plot(line);
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onNext(ArrayList<Line> arrayList) {
                        line = arrayList;
                    }
                }));
    }



        private Observable<ArrayList<Line>> CreateLine(ArrayList<device_log> a){
        return Observable.defer(() -> {
            try{
                return Observable.just(change(a));
            }   catch (Exception e){
                System.out.println(e.getMessage());
                return Observable.error(e);
            }
        });
        }



    private  class LongOperation extends AsyncTask<ArrayList<device_log>, Void, ArrayList<Line>>{

        @Override
        protected ArrayList<Line> doInBackground(ArrayList<device_log>... s) {
            ArrayList<device_log> device_logs = s[0];
            Iterator<device_log> iterator = device_logs.iterator();
            List<PointValue> values0 = new ArrayList<>();
            List<PointValue> values1 = new ArrayList<>();
            List<PointValue> values2 = new ArrayList<>();
            List<PointValue> values3 = new ArrayList<>();
            List<PointValue> values4 = new ArrayList<>();
            List<PointValue> values5 = new ArrayList<>();
            List<PointValue> values6 = new ArrayList<>();
            List<PointValue> values7 = new ArrayList<>();
            ArrayList<Line> lines = new ArrayList<>();
            int i = 0;
            while(iterator.hasNext()) {
                i++;
                device_log device_log = iterator.next();

                switch (device_log.getSen1()) {
                    case "-":
                        values0.add(new PointValue(i, 0));
                        break;
                    case "8989.0":
                        values0.add(new PointValue(i, 100));
                        break;
                    default:
                        values0.add(new PointValue(i, Float.parseFloat(device_log.getSen1())));

                        break;
                }

                switch (device_log.getSen2()) {
                    case "-":

                        values1.add(new PointValue(i, 0));
                        break;
                    case "8989.0":
                        values1.add(new PointValue(i, 100));
                        break;
                    default:
                        values1.add(new PointValue(i, Float.parseFloat(device_log.getSen2())));
                        break;
                }


                switch (device_log.getSen3()) {
                    case "-":
                        values2.add(new PointValue(i, 0));

                        break;
                    case "8989.0":
                        values2.add(new PointValue(i, 100));
                        break;
                    default:
                        values2.add(new PointValue(i, Float.parseFloat(device_log.getSen3())));
                        break;
                }

                switch (device_log.getSen4()) {
                    case "-":
                        values3.add(new PointValue(i, 0));
                        break;
                    case "8989.0":
                        values3.add(new PointValue(i, 100));
                        break;
                    default:
                        values3.add(new PointValue(i, Float.parseFloat(device_log.getSen4())));
                        break;
                }
                switch (device_log.getSen5()) {
                    case "-":
                        values4.add(new PointValue(i, 0));

                        break;
                    case "8989.0":
                        values4.add(new PointValue(i, 100));
                        break;
                    default:
                        values4.add(new PointValue(i, Float.parseFloat(device_log.getSen5())));
                        break;
                }
                switch (device_log.getSen6()) {
                    case "-":
                        values5.add(new PointValue(i, 0));

                        break;
                    case "8989.0":
                        values5.add(new PointValue(i, 100));
                        break;
                    default:
                        values5.add(new PointValue(i, Float.parseFloat(device_log.getSen6())));
                        break;
                }
                switch (device_log.getSen7()) {
                    case "-":
                        values6.add(new PointValue(i, 0));

                        break;
                    case "8989.0":
                        values6.add(new PointValue(i, 100));
                        break;
                    default:
                        values6.add(new PointValue(i, Float.parseFloat(device_log.getSen7())));
                        break;
                }
                switch (device_log.getSen8()) {
                    case "-":
                        values7.add(new PointValue(i, 0));

                        break;
                    case "8989.0":
                        values7.add(new PointValue(i, 100));
                        break;
                    default:
                        values7.add(new PointValue(i, Float.parseFloat(device_log.getSen8())));
                        break;
                }
            }
            Line line0 = new Line(values0);Line line1 = new Line(values1);
            Line line2 = new Line(values2);Line line3 = new Line(values3);
            Line line4 = new Line(values4);Line line5 = new Line(values5);
            Line line6 = new Line(values6);Line line7 = new Line(values7);
            line0.setHasLabels(true);line0.setHasLabelsOnlyForSelected(true);
            line1.setHasLabels(true);line1.setHasLabelsOnlyForSelected(true);
            line2.setHasLabels(true);
            line2.setHasLabelsOnlyForSelected(true);
            line3.setHasLabels(true);
            line3.setHasLabelsOnlyForSelected(true);
            line4.setHasLabels(true);
            line4.setHasLabelsOnlyForSelected(true);
            line5.setHasLabels(true);
            line5.setHasLabelsOnlyForSelected(true);
            line6.setHasLabels(true);
            line6.setHasLabelsOnlyForSelected(true);
            line7.setHasLabels(true);
            line7.setHasLabelsOnlyForSelected(true);
            line0.setHasPoints(true);
            line0.setFilled(true);line4.setFilled(true);
            line1.setFilled(true);line5.setFilled(true);
            line2.setFilled(true);line6.setFilled(true);
            line3.setFilled(true);line0.setFilled(true);
            line1.setHasPoints(true);line2.setHasPoints(true);
            line3.setHasPoints(true);line4.setHasPoints(true);
            line5.setHasPoints(true);line6.setHasPoints(true);
            line7.setHasPoints(true);
            lines.clear();
            lines.add(line0);
            lines.add(line1);
            lines.add(line2);
            lines.add(line3);
            lines.add(line4);
            lines.add(line5);
            lines.add(line6);
            lines.add(line7);

            return lines;

        }

              @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(ArrayList<Line> s) {
            super.onPostExecute(s);
            iView.Plot(s);
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }
    }
}


