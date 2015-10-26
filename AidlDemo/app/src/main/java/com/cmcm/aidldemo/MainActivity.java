package com.cmcm.aidldemo;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        PackageManager pm = getPackageManager();
        List<PackageInfo> infos = pm.getInstalledPackages(0);
        for (PackageInfo info : infos){
            if(TextUtils.equals("com.autonavi.minimap", info.packageName)){
                Log.v("sunzy", info.applicationInfo.packageName + " : " + pm.getApplicationLabel(info.applicationInfo));
                Log.v("sunzy", info.applicationInfo.packageName + " : " + trim(pm.getApplicationLabel(info.applicationInfo).toString()));
            }
        }
        Log.v("sunzy", trim("  " + "     高 德 地 图     "));
    }

    public static String trim(String source) {
        if(TextUtils.isEmpty(source)){
            return "";
        }
        int start = 0, last = source.length() - 1;
        int end = last;
        while ((start <= end) && (source.charAt(start) <= 32 || source.charAt(start) == 160)) {
            start++;
        }
        while ((start <= end) && (source.charAt(end) <= 32 || source.charAt(end) == 160)) {
            end--;
        }
        if (start == 0 && end == last) {
            return source;
        }
        return source.substring(start, end + 1);
    }
}
