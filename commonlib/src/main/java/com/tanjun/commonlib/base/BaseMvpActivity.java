package com.tanjun.commonlib.base;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.CallSuper;
import androidx.annotation.CheckResult;
import androidx.appcompat.app.AppCompatActivity;

import com.example.cheers.logincomponent.R;
import com.gyf.barlibrary.ImmersionBar;
import com.tanjun.commonlib.model.BaseModel;
import com.tanjun.commonlib.presenter.BasePresenter;
import com.tanjun.commonlib.view.BaseView;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.trello.rxlifecycle2.LifecycleProvider;
import com.trello.rxlifecycle2.LifecycleTransformer;
import com.trello.rxlifecycle2.RxLifecycle;
import com.trello.rxlifecycle2.android.ActivityEvent;
import com.trello.rxlifecycle2.android.RxLifecycleAndroid;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.reactivex.Observable;
import io.reactivex.subjects.BehaviorSubject;

/**
 * Created by tanjun
 * mvp Activity 基类
 */

public abstract class BaseMvpActivity<M extends BaseModel, V extends BaseView, P extends BasePresenter> extends AppCompatActivity implements LifecycleProvider<ActivityEvent>, BaseMvp<M, V, P> {

    //RxJava 生命周期控制 自动解绑回收
    private final BehaviorSubject<ActivityEvent> lifecycleSubject = BehaviorSubject.create();
    //动态权限申请
    public RxPermissions rxPermissions;
    //解除绑定
    public Unbinder unbinder;
    //沉浸式框架
    protected ImmersionBar mImmersionBar;
    protected P presenter;

    @SuppressWarnings("unchecked")
    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        lifecycleSubject.onNext(ActivityEvent.CREATE);
        rxPermissions = new RxPermissions(this);
        initContentView(savedInstanceState);
        //沉浸式
        initImmersionBar();
        unbinder = ButterKnife.bind(this);
        presenter = createPresenter();
        if (presenter != null) {
            //将Model层注册到Presenter中
            presenter.registerModel(createModel());
            //将View层注册到Presenter中
            presenter.registerView(createView());
        }
        init();
    }

    /**
     * 设置布局文件
     */
    protected abstract void initContentView(Bundle savedInstanceState);

    /**
     * 初始化组件
     */
    public abstract void init();

    /**
     * 实例化沉浸式布局
     */
    protected void initImmersionBar() {
        //在BaseActivity里初始化
        mImmersionBar = ImmersionBar.with(this);
        mImmersionBar.statusBarColor(R.color.colorPrimary);
        mImmersionBar.init();
    }

    /**
     * 无参数跳转
     *
     * @param cls 跳转的目的activity
     */
    public void openActivity(Class cls) {
        Intent intent = new Intent(this, cls);
        startActivity(intent);
    }

    /**
     * 带参数跳转
     *
     * @param cls    跳转的目的activity
     * @param bundle 参数集
     */
    public void openActivity(Class cls, Bundle bundle) {
        Intent intent = new Intent(this, cls);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    /**
     * 带返回结果跳转
     *
     * @param cls 跳转的目的activity
     */
    public void openForResultActivity(Class cls) {
        Intent intent = new Intent(this, cls);
        startActivityForResult(intent, 100);
    }

    /**
     * 带参数跳转并且返回结果
     *
     * @param cls    跳转的目的activity
     * @param bundle 参数集
     */
    public void openForResultActivity(Class cls, Bundle bundle) {
        Intent intent = new Intent(this, cls);
        intent.putExtras(bundle);
        startActivityForResult(intent, 100);
    }

    @Override
    @CheckResult
    public final Observable<ActivityEvent> lifecycle() {
        return lifecycleSubject.hide();
    }

    @Override
    @CheckResult
    public final <T> LifecycleTransformer<T> bindUntilEvent(ActivityEvent event) {
        return RxLifecycle.bindUntilEvent(lifecycleSubject, event);
    }

    @Override
    @CheckResult
    public final <T> LifecycleTransformer<T> bindToLifecycle() {
        return RxLifecycleAndroid.bindActivity(lifecycleSubject);
    }

    @Override
    @CallSuper
    protected void onStart() {
        super.onStart();
        lifecycleSubject.onNext(ActivityEvent.START);
    }

    @Override
    @CallSuper
    protected void onResume() {
        super.onResume();
        lifecycleSubject.onNext(ActivityEvent.RESUME);
    }

    @Override
    @CallSuper
    protected void onPause() {
        lifecycleSubject.onNext(ActivityEvent.PAUSE);
        super.onPause();
    }

    @Override
    @CallSuper
    protected void onStop() {
        lifecycleSubject.onNext(ActivityEvent.STOP);
        super.onStop();
    }


    public void beforeDestroy() {

    }

    @Override
    @CallSuper
    protected void onDestroy() {
        beforeDestroy();
        if (mImmersionBar != null)
            mImmersionBar.destroy();  //在BaseActivity里销毁
        lifecycleSubject.onNext(ActivityEvent.DESTROY);
        super.onDestroy();
        if (unbinder != null)
            unbinder.unbind();
        if (presenter != null) {
            //Activity销毁时的调用，让具体实现BasePresenter中onViewDestroy()方法做出决定
            presenter.destroy();
        }
    }

    /**
     * 重写Activity进场动画
     */
    @Override
    public void startActivity(Intent intent) {
        super.startActivity(intent);
        overridePendingTransition(R.anim.anim_enter, R.anim.anim_exit);
    }

    /**
     * 重写Activity退场动画
     */
    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.back_enter, R.anim.back_exit);
    }
}
