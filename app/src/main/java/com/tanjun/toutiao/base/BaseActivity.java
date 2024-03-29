package com.tanjun.toutiao.base;

import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Lifecycle;

import com.afollestad.materialdialogs.color.CircleView;
import com.tanjun.commonlib.util.SettingUtil;
import com.r0adkll.slidr.Slidr;
import com.r0adkll.slidr.model.SlidrConfig;
import com.r0adkll.slidr.model.SlidrInterface;
import com.tanjun.toutiao.Constant;
import com.uber.autodispose.AutoDispose;
import com.uber.autodispose.AutoDisposeConverter;
import com.uber.autodispose.android.lifecycle.AndroidLifecycleScopeProvider;

public abstract class BaseActivity extends AppCompatActivity {

    protected SlidrInterface slidrInterface;
    protected Context mContext;
    private int iconType = -1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.iconType = SettingUtil.getInstance().getCustomIconValue();
        this.mContext = this;
        initSlidable();
    }

    /** toolbar */
    protected void initToolBar(Toolbar toolbar, boolean homeAsUpEnable, String title){
        toolbar.setTitle(title);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(homeAsUpEnable);
    }

    /** 初始化滑动返回 */
    protected void initSlidable(){
        int isSlidable = SettingUtil.getInstance().getSlidable();
        if (isSlidable != Constant.SLIDABLE_DISABLE){
            SlidrConfig config = new SlidrConfig.Builder()
                    .edge(isSlidable == Constant.SLIDABLE_EDGE)
                    .build();
            slidrInterface = Slidr.attach(this, config);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

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
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home){
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        /** fragment逐个退出 */
        int count = getSupportFragmentManager().getBackStackEntryCount();
        if (count == 0){
            super.onBackPressed();
        }else {
            getSupportFragmentManager().popBackStack();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (iconType != SettingUtil.getInstance().getCustomIconValue()){
            new Thread(() -> {
                String act = ".SplashActivity_";
                for (String s: Constant.ICONS_TYPE) {
                    getPackageManager().setComponentEnabledSetting(
                            new ComponentName(BaseActivity.this, getPackageName()+act +s),
                            PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                            PackageManager.DONT_KILL_APP);
                }
                act +=Constant.ICONS_TYPE[SettingUtil.getInstance().getCustomIconValue()];

                getPackageManager().setComponentEnabledSetting(new ComponentName(BaseActivity.this,
                                getPackageName()+act),
                        PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                        PackageManager.DONT_KILL_APP);
            }).start();

        }
    }

    public <X> AutoDisposeConverter<X> bindAutoDispos(){
        return AutoDispose.autoDisposable(AndroidLifecycleScopeProvider
        .from(this, Lifecycle.Event.ON_DESTROY));
    }
}
