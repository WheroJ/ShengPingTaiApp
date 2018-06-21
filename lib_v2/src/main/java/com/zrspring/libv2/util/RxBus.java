package com.zrspring.libv2.util;

import com.zrspring.libv2.network.Event;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.PublishSubject;

/**
 * Created by shopping on 2017/12/26 14:13.
 * https://github.com/wheroj
 */

public class RxBus {

    public static <T> void just(T t, Consumer Consumer) {
        Observable.just(t)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(Consumer);

//        new ();
        PublishSubject<Event<?>> publishSubject = PublishSubject.create();
    }
}
