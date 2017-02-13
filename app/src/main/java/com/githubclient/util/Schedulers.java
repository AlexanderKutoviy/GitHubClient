package com.githubclient.util;

import java.util.concurrent.Executors;

import rx.Scheduler;
import rx.android.schedulers.AndroidSchedulers;

public class Schedulers {

    private static final Scheduler ioScheduler = rx.schedulers.Schedulers
            .from(Executors.newSingleThreadExecutor());

    private static final Scheduler mainScheduler = AndroidSchedulers.mainThread();

    public static Scheduler main() {
        return mainScheduler;
    }

    public static Scheduler io() {
        return ioScheduler;
    }
}