package com.cmcm.huaweiautostartdemo;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class MainActivity extends AppCompatActivity {

    private ContentObserver observer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.v("sunzy", "before register");
        Log.v("sunzy", Build.MANUFACTURER);
        Log.v("sunzy", Build.FINGERPRINT);
        Log.v("sunzy", Build.BOARD);
        Log.v("sunzy", Build.FINGERPRINT);
//        int uid = 0;
//        try {
//            uid = getPackageManager().getPackageInfo(getPackageName(), 0).applicationInfo.uid;
//        } catch (PackageManager.NameNotFoundException e) {
//            e.printStackTrace();
//        }
//        invokeCheckOpMethod(this, uid, getPackageName());
    }

    public static boolean invokeCheckOpMethod(Context context, int uid, String pkgName) {
        boolean isClosedByMiuiV6 = false;
        try {
            Class clz = Class.forName("android.content.Context");
            Field fd = clz.getDeclaredField("APP_OPS_SERVICE");
            fd.setAccessible(true);
            Object obj = fd.get(clz);
            String ops = "";
            if(obj instanceof String) {
                ops = (String) obj;
                Method method1 = clz.getMethod("getSystemService", String.class);
                Object appOpsManager = method1.invoke(context, ops);
                Class<?> cls = Class.forName("android.app.AppOpsManager");
                fd = cls.getDeclaredField("MODE_ALLOWED");
                fd.setAccessible(true);
                int allowMode = fd.getInt(cls);

                Method method = cls.getMethod("checkOp", int.class, int.class, String.class);
                int opMode = (Integer) method.invoke(appOpsManager, 50, uid, pkgName);
                isClosedByMiuiV6 = opMode != allowMode;
                Log.i("sunzy", "isClosedByMiuiV6 = " + isClosedByMiuiV6 + " allowMode = " + allowMode + " opMode = " + opMode);
            }
        } catch(Exception e) {
        }
        return isClosedByMiuiV6;
    }
}
