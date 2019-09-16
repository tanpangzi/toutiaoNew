package com.tanjun.toutiao.activity;

import android.app.Activity;
import android.os.Bundle;

import androidx.annotation.Nullable;

import com.tanjun.toutiao.R;

public class MainActivity extends Activity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }
}
