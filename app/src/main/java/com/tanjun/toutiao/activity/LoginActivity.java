//package com.tanjun.toutiao.activity;
//
//import android.os.Bundle;
//import android.widget.EditText;
//
//import com.tanjun.commonlib.base.BaseMvpActivity;
//import com.tanjun.commonlib.util.MyUtils;
//import com.tanjun.toutiao.R;
//import com.tanjun.toutiao.bean.LoginBean;
//import com.tanjun.toutiao.implement.LoginModelImple;
//import com.tanjun.toutiao.model.LoginModel;
//import com.tanjun.toutiao.presenter.LoginPresenter;
//import com.tanjun.toutiao.view.LoginView;
//
//import butterknife.BindView;
//import butterknife.OnClick;
//
///**
// * Created by tanjun
// */
//public class LoginActivity extends BaseMvpActivity<LoginModel, LoginView, LoginPresenter> implements LoginView {
//
//
//    @BindView(R.id.edt_account)
//    EditText edtAccount;
//    @BindView(R.id.edt_psw)
//    EditText edtPsw;
//
//
//    @Override
//    protected void initContentView(Bundle savedInstanceState) {
//        setContentView(R.layout.activity_login);
//    }
//
//    @Override
//    public void init() {
//
//    }
//
//
//    @Override
//    public LoginModel createModel() {
//        return new LoginModelImple();
//    }
//
//    @Override
//    public LoginView createView() {
//        return this;
//    }
//
//    @Override
//    public LoginPresenter createPresenter() {
//        return new LoginPresenter();
//    }
//
//
//    @Override
//    public String getAccount() {
//        return edtAccount.getText().toString();
//    }
//
//    @Override
//    public String getPassword() {
//        return edtPsw.getText().toString();
//    }
//
//    @OnClick(R.id.btn_login)
//    public void clickLoginBtn() {
//        presenter.login(this);
//    }
//
//    @Override
//    public void loginSuccess(LoginBean t) {
//        MyUtils.showToast(this, "登录成功!" + "token:" + t.getToken());
//    }
//
//    @Override
//    public void loginFail(String msg) {
//        MyUtils.showToast(this, msg);
//    }
//
//
//}
