package com.tanjun.commonlib;

import androidx.multidex.BuildConfig;

import io.reactivex.functions.Consumer;

/**
 * Created by Meiji on 2017/6/18.
 */

public class ErrorAction {

    public static Consumer<Throwable> error() {
        return new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Exception {
                if (BuildConfig.DEBUG) {
                    throwable.printStackTrace();
                }
            }
        };
    }

    public static void print(Throwable throwable) {
        if (BuildConfig.DEBUG) {
            throwable.printStackTrace();
        }
    }
}
