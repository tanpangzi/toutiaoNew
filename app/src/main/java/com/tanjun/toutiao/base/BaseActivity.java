package com.tanjun.toutiao.base;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.r0adkll.slidr.Slidr;
import com.r0adkll.slidr.model.SlidrConfig;
import com.r0adkll.slidr.model.SlidrInterface;
import com.tanjun.toutiao.Constant;
import com.tanjun.toutiao.util.SettingUtil;

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

    @Override
    protected void onResume() {
        super.onResume();
        int color = SettingUtil.getInstance().getColor();
        int drawable = Constant.ICONS_DRAWABLES[SettingUtil.getInstance().getCustomIconValue()];

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

}
