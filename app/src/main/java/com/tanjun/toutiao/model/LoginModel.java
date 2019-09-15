package com.tanjun.toutiao.model;

import com.tanjun.commonlib.model.BaseModel;
import com.tanjun.toutiao.bean.LoginBean;
import com.tanjun.toutiao.rxjava.BaseEntity;

import io.reactivex.Observable;

public interface LoginModel extends BaseModel {
    /**
     * 登录
     */
    Observable<BaseEntity<LoginBean>> login(String account, String psw);


}
