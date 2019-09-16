package com.tanjun.commonlib.util;


import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import io.reactivex.Observable;
import io.reactivex.subjects.PublishSubject;
import io.reactivex.subjects.Subject;

/**
 * https://juejin.im/entry/58ff2e26a0bb9f0065d2c5f2
 */

public class RxBus {

    private ConcurrentHashMap<Object, List<Subject>> subjectMapper = new ConcurrentHashMap<>();

    private RxBus() {

    }

    public static RxBus getInstance() {
        return Holder.instance;
    }

    public <T> Observable<T> register(Class<T> clz) {
        return register(clz.getName());
    }

    public <T> Observable<T> register(Object tag) {
        List<Subject> subjectList = subjectMapper.get(tag);
        if (null == subjectList) {
            subjectList = new ArrayList<>();
            subjectMapper.put(tag, subjectList);
        }

        Subject<T> subject = PublishSubject.create();
        subjectList.add(subject);

        //System.out.println("注册到rxbus");
        return subject;
    }

    public <T> void unregister(Class<T> clz, Observable observable) {
        unregister(clz.getName(), observable);
    }

    public void unregister(Object tag, Observable observable) {
        List<Subject> subjects = subjectMapper.get(tag);
        if (null != subjects) {
            subjects.remove(observable);
            if (subjects.isEmpty()) {
                subjectMapper.remove(tag);
                //System.out.println("从rxbus取消注册");
            }
        }
    }

    public void post(Object content) {
        post(content.getClass().getName(), content);
    }

    public void post(Object tag, Object content) {
        List<Subject> subjects = subjectMapper.get(tag);
        if (!subjects.isEmpty()) {
            for (Subject subject : subjects) {
                subject.onNext(content);
            }
        }
    }

    private static class Holder {
        private static RxBus instance = new RxBus();
    }
}
