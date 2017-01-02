package com.quascenta.petersroad.interfaces;

import rx.Scheduler;

/**
 * Created by AKSHAY on 11/10/2016.
 */
public interface IScheduler {
    public Scheduler mainThread();
    public Scheduler backgroundThread();
}
