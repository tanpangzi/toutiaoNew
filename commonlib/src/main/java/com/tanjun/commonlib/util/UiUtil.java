package com.tanjun.commonlib.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import java.io.File;

public class UiUtil {

    public static void showToast(Context context, int resId) {
        showToast(context, context.getString(resId));
    }

    public static void showToast(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

    /**
     * 安装APK包
     *
     * @param context
     * @param apkPath
     */
    public static void installApk(Context context, String apkPath) {
        File apkFile = new File(apkPath);
        if (!apkFile.exists()) {
            return;
        }
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        i.setDataAndType(Uri.parse("file://" + apkFile.toString()), "application/vnd.android.package-archive");
        context.startActivity(i);
        android.os.Process.killProcess(android.os.Process.myPid());
    }


    public static void finish(Activity activity) {
        activity.finish();
    }

    public static void startActivity(Context context, Class clazz) {
        startActivity(context, clazz, null);
    }

    public static void startActivity(Context context, Class clazz, Bundle bundle) {
        Intent intent = new Intent(context, clazz);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        context.startActivity(intent);
    }


    public static void startActivityForResult(Activity activity, Class clazz, int requestCode, Bundle bundle) {
        Intent intent = new Intent(activity, clazz);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        activity.startActivityForResult(intent, requestCode);
    }

    public static void dial(Context context, String phoneNumber) {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:" + phoneNumber));
        context.startActivity(intent);
    }

    /**
     * 隐藏键盘
     */
    public static void hideSoftKeyboard(Activity activity) {
        InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (inputMethodManager != null) {
            View localView = activity.getCurrentFocus();
            if (localView != null && localView.getWindowToken() != null) {
                IBinder windowToken = localView.getWindowToken();
                inputMethodManager.hideSoftInputFromWindow(windowToken, 0);
            }
        }
    }

}
