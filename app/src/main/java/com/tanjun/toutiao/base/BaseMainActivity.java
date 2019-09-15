package com.tanjun.toutiao.base;

import android.content.Intent;
import android.content.res.Configuration;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toolbar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.drawerlayout.widget.DrawerLayout;

import com.tanjun.commonlib.util.SettingUtil;
import com.google.android.material.navigation.NavigationView;
import com.tanjun.toutiao.R;

public abstract class BaseMainActivity extends BaseActivity implements NavigationView.OnNavigationItemSelectedListener {
    private long exit = 0;

    protected Toolbar toolbar;
    protected DrawerLayout drawer_layout;

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
}
