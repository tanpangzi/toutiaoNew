package com.tanjun.toutiao.implement;

import io.reactivex.Observable;

import com.tanjun.toutiao.bean.LoginBean;
import com.tanjun.toutiao.model.LoginModel;
import com.tanjun.toutiao.retrofit.RetrofitFactory;
import com.tanjun.toutiao.rxjava.BaseEntity;

/**
 * Created by tanjun .
 * 在这里只负责处理或获取数据，不直接与View交互
 */
public class LoginModelImple implements LoginModel {

    @Override
    public Observable<BaseEntity<LoginBean>> login(String account, String psw) {
        return RetrofitFactory.getInstance().login(account, psw);
    }

}
