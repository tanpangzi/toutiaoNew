package com.tanjun.commonlib.presenter;


import com.tanjun.commonlib.model.BaseModel;
import com.tanjun.commonlib.view.BaseView;

import java.lang.ref.WeakReference;

/**
 * Created by tanjun
 */
public abstract class BasePresenter<M extends BaseModel, V extends BaseView> {
    protected M model;
    /**
     * 使用弱引用来防止内存泄漏
     */
    private WeakReference<V> wrf;

    public void registerModel(M model) {
        this.model = model;
    }

    public void registerView(V view) {
        wrf = new WeakReference<>(view);
    }

    public V getView() {
        return wrf == null ? null : wrf.get();
    }

    /**
     * 在Activity或Fragment卸载时调用View结束的方法
     */
    public void destroy() {
        if (wrf != null) {
            wrf.clear();
            wrf = null;
            model = null;
        }
        onViewDestroy();
    }

    protected abstract void onViewDestroy();
}
