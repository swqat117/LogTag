package com.quascenta.petersroad.services;

import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.functions.Func1;

/**
 * Created by AKSHAY on 11/4/2016.
 */

public class RetryWithDelay implements Func1<Observable<? extends Throwable>, Observable<?>> {

    private final int maxRetries;
    private final int retryDelayMillis;
    private int retrycount;

    public RetryWithDelay(final int maxRetries, final int retryDelayMillis){
        this.maxRetries = maxRetries;
        this.retryDelayMillis = retryDelayMillis;
        this.retrycount = 0;
    }



    @Override
    public Observable<?> call(Observable<? extends Throwable> observable) {
        return observable.flatMap(new Func1<Throwable, Observable<?>>() {
            @Override
            public Observable<?> call(Throwable throwable) {
                if(++retrycount < maxRetries){
                    return Observable.timer(retryDelayMillis, TimeUnit.SECONDS);
                }
                return Observable.error(throwable);
            }
        });
    }
}
