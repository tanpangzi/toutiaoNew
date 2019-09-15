package com.tanjun.commonlib.base;

import com.tanjun.commonlib.model.BaseModel;
import com.tanjun.commonlib.presenter.BasePresenter;
import com.tanjun.commonlib.view.BaseView;

public interface BaseMvp<M, V extends BaseView, P extends BasePresenter> {
    BaseModel createModel();

    V createView();

    P createPresenter();
}
