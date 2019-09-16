package com.tanjun.toutiao.activity;

import android.os.Bundle;

import com.tanjun.commonlib.model.BaseModel;
import com.tanjun.commonlib.presenter.BasePresenter;
import com.tanjun.commonlib.view.BaseView;
import com.tanjun.toutiao.R;
import com.tanjun.toutiao.base.BaseMainActivity;
import com.trello.rxlifecycle2.LifecycleTransformer;

import javax.annotation.Nonnull;

public class MainActivity extends BaseMainActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }

    @Override
    protected void initContentView(Bundle savedInstanceState) {

    }

    @Override
    public void init() {

    }

    @Override
    public BaseModel createModel() {
        return null;
    }

    @Override
    public BaseView createView() {
        return null;
    }

    @Override
    public BasePresenter createPresenter() {
        return null;
    }

    @Nonnull
    @Override
    public LifecycleTransformer bindUntilEvent(@Nonnull Object event) {
        return null;
    }
}
