package com.tanjun.commonlib.base;

import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.CallSuper;
import androidx.annotation.CheckResult;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Lifecycle;

import com.afollestad.materialdialogs.color.CircleView;
import com.gyf.barlibrary.ImmersionBar;
import com.r0adkll.slidr.Slidr;
import com.r0adkll.slidr.model.SlidrConfig;
import com.r0adkll.slidr.model.SlidrInterface;
import com.tanjun.commonlib.Constant;
import com.tanjun.commonlib.R;
import com.tanjun.commonlib.model.BaseModel;
import com.tanjun.commonlib.presenter.BasePresenter;
import com.tanjun.commonlib.util.SettingUtil;
import com.tanjun.commonlib.view.BaseView;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.trello.rxlifecycle2.LifecycleProvider;
import com.trello.rxlifecycle2.LifecycleTransformer;
import com.trello.rxlifecycle2.RxLifecycle;
import com.trello.rxlifecycle2.android.ActivityEvent;
import com.trello.rxlifecycle2.android.RxLifecycleAndroid;
import com.uber.autodispose.AutoDispose;
import com.uber.autodispose.AutoDisposeConverter;
import com.uber.autodispose.android.lifecycle.AndroidLifecycleScopeProvider;

import io.reactivex.Observable;
import io.reactivex.subjects.BehaviorSubject;

/**
 * Created by tanjun
 * mvp Activity 基类
 */

public abstract class BaseMvpActivity<M extends BaseModel, V extends BaseView, P extends BasePresenter> extends AppCompatActivity implements LifecycleProvider<ActivityEvent>, BaseMvp<M, V, P> {

    protected SlidrInterface slidrInterface;
    protected Context mContext;
    private int iconType = -1;

    //RxJava 生命周期控制 自动解绑回收
    private final BehaviorSubject<ActivityEvent> lifecycleSubject = BehaviorSubject.create();
    //动态权限申请
    public RxPermissions rxPermissions;
    //沉浸式框架
    protected ImmersionBar mImmersionBar;
    protected P presenter;

    @SuppressWarnings("unchecked")
    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.iconType = SettingUtil.getInstance().getCustomIconValue();
        this.mContext = this;
        lifecycleSubject.onNext(ActivityEvent.CREATE);
        rxPermissions = new RxPermissions(this);
        initContentView(savedInstanceState);
        //沉浸式
        initImmersionBar();
        presenter = createPresenter();
        if (presenter != null) {
            //将Model层注册到Presenter中
            presenter.registerModel(createModel());
            //将View层注册到Presenter中
            presenter.registerView(createView());
        }
        init();
        initSlidable();
    }

    protected void initToolBar(Toolbar toolbar, boolean homeAsUpEnable, String title){
        toolbar.setTitle(title);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(homeAsUpEnable);
    }

    protected void initSlidable(){
        int isSlidable = SettingUtil.getInstance().getSlidable();
        if (isSlidable != Constant.SLIDABLE_DISABLE){
            SlidrConfig config = new SlidrConfig.Builder()
                    .edge(isSlidable == Constant.SLIDABLE_EDGE)
                    .build();
            slidrInterface = Slidr.attach(this, config);
        }
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

        int color = SettingUtil.getInstance().getColor();
        /** 图标替换 */
        int drawable = Constant.ICONS_DRAWABLES[SettingUtil.getInstance().getCustomIconValue()];
        if (getSupportActionBar() != null) {
            getSupportActionBar().setBackgroundDrawable(new ColorDrawable(color));
        }

        /** 修改主题颜色 */
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            getWindow().setStatusBarColor(CircleView.shiftColorDown(color));

            /** 最近任务栏上色 */
            ActivityManager.TaskDescription description = new ActivityManager.TaskDescription(
                    "头条", BitmapFactory.decodeResource(getResources(), drawable)
                    ,color);
            setTaskDescription(description);
            if (SettingUtil.getInstance().getNavBar()){
                getWindow().setStatusBarColor(CircleView.shiftColorDown(color));
            }else {
                getWindow().setNavigationBarColor(Color.BLACK);
            }
        }
    }

    @Override
    @CallSuper
    protected void onPause() {
        lifecycleSubject.onNext(ActivityEvent.PAUSE);
        super.onPause();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home){
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        int count = getSupportFragmentManager().getBackStackEntryCount();
        if (count == 0){
            super.onBackPressed();
        }else {
            getSupportFragmentManager().popBackStack();
        }
    }

    @Override
    @CallSuper
    protected void onStop() {
        lifecycleSubject.onNext(ActivityEvent.STOP);
        super.onStop();
        if (iconType != SettingUtil.getInstance().getCustomIconValue()){
            new Thread(new Runnable() {
                @Override
                public void run() {
                    String act = ".SplashActivity_";
                    for (String s : Constant.ICONS_TYPE) {
                        BaseMvpActivity.this.getPackageManager().setComponentEnabledSetting(
                                new ComponentName(BaseMvpActivity.this, BaseMvpActivity.this.getPackageName() + act + s),
                                PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                                PackageManager.DONT_KILL_APP);
                    }
                    act += Constant.ICONS_TYPE[SettingUtil.getInstance().getCustomIconValue()];

                    BaseMvpActivity.this.getPackageManager().setComponentEnabledSetting(new ComponentName(BaseMvpActivity.this,
                                    BaseMvpActivity.this.getPackageName() + act),
                            PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                            PackageManager.DONT_KILL_APP);
                }
            }).start();

        }
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

    public <X> AutoDisposeConverter<X> bindAutoDispos(){
        return AutoDispose.autoDisposable(AndroidLifecycleScopeProvider
                .from(this, Lifecycle.Event.ON_DESTROY));
    }
}
