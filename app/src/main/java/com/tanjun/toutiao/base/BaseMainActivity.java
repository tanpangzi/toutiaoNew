package com.tanjun.toutiao.base;

import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Rect;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toolbar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.drawerlayout.widget.DrawerLayout;

import com.getkeepsafe.taptargetview.TapTarget;
import com.getkeepsafe.taptargetview.TapTargetSequence;
import com.google.android.material.navigation.NavigationView;
import com.tanjun.commonlib.base.BaseMvpActivity;
import com.tanjun.commonlib.util.SettingUtil;
import com.tanjun.commonlib.util.UiUtil;
import com.tanjun.toutiao.R;

public abstract class BaseMainActivity extends BaseMvpActivity implements NavigationView.OnNavigationItemSelectedListener {
    private long exitTime = 0;

    protected Toolbar toolbar;
    protected DrawerLayout drawer_layout;

    protected static final String POSITION = "position";
    protected static final String select_item = "bottomNavigationSelectItem";

    protected static final int FRAGMENT_NEWS = 0;
    protected static final int FRAGMENT_PHOTO = 1;
    protected static final int FRAGMENT_VIDEO = 2;
    protected static final int FRAGMENT_MEDIA = 3;

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()){
            case R.id.nav_switch_night_mode:
                setNightMode();
                return false;

            case R.id.nav_setting:
                //startActivity(new Intent(this, ));
                drawer_layout.closeDrawers();
                return false;

            case R.id.nav_share:
                Intent intent = new Intent()
                        .setAction(Intent.ACTION_SEND)
                        .setType("text/plain")
                        .putExtra(Intent.EXTRA_TEXT, getString(R.string.share_app_text)+getString(R.string.source_code_url));
                startActivity(intent);
                drawer_layout.closeDrawers();
                return false;
        }
        return false;
    }

    /** 主题设置 */
    private void setNightMode() {
        /** 切换主题 */
        int mode = getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;
        /** 夜间转白天 */
        if (mode == Configuration.UI_MODE_NIGHT_YES){
            SettingUtil.getInstance().setIsNightMode(false);
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }else {
            /** 白天转夜间 */
            SettingUtil.getInstance().setIsNightMode(true);
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        }

        getWindow().setWindowAnimations(R.style.WindowAnimationFadeInOut);
        recreate();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_activity_main, menu);
        return true;
    }

    /** 搜索按钮 */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_search){

        }
        return super.onOptionsItemSelected(item);
    }

    protected void showTapTarget(){
        final Display display = getWindowManager().getDefaultDisplay();
        final Rect target = new Rect(
                0,display.getHeight(),
                0, display.getHeight());
        target.offset(display.getWidth()/8, -56);

        /** 用户引导页 */
        TapTargetSequence sequence = new TapTargetSequence(this)
                .targets(
                        TapTarget.forToolbarMenuItem(toolbar, R.id.action_search, "点击这里进行搜索")
                                .dimColor(android.R.color.black)
                                .outerCircleColor(R.color.colorPrimary)
                                .drawShadow(true)
                                .id(1),
                        TapTarget.forToolbarNavigationIcon(toolbar, "点击这里展开侧栏")
                                .dimColor(android.R.color.black)
                                .outerCircleColor(R.color.colorPrimary)
                                .drawShadow(true)
                                .id(2),
                        TapTarget.forBounds(target, "点击这里切换新闻", "双击返回顶部\n再次双击刷新当前页面")
                                .dimColor(android.R.color.black)
                                .outerCircleColor(R.color.colorPrimary)
                                .targetRadius(60)
                                .transparentTarget(true)
                                .drawShadow(true)
                                .id(3)
                ).listener(new TapTargetSequence.Listener() {
                    @Override
                    public void onSequenceFinish() {
                        SettingUtil.getInstance().setIsFirstTime(false);
                    }

                    @Override
                    public void onSequenceStep(TapTarget lastTarget, boolean targetClicked) {

                    }

                    @Override
                    public void onSequenceCanceled(TapTarget lastTarget) {
                        SettingUtil.getInstance().setIsFirstTime(false);
                    }
                });
        sequence.start();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        long currentTime = System.currentTimeMillis();
        if (currentTime - exitTime < 2000){
            super.onBackPressed();
        }else {
            UiUtil.showToast(mContext, getString(R.string.double_click_exit));
            exitTime = currentTime;
        }
    }
}
