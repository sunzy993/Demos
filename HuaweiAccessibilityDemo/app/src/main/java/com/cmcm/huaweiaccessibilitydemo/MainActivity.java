package com.cmcm.huaweiaccessibilitydemo;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
       // gotoHuaweiSettings();
        //gotoHuaweiDataTrafficManagement();
        Log.v("sunzy", "activity start!!!");
        HuaweiAccessibilityHelper.appNames = new ArrayList<>();
        HuaweiAccessibilityHelper.appNames.add("Facebook");
        HuaweiAccessibilityHelper.appNames.add("金山手机助手");
        HuaweiAccessibilityHelper.appNames.add("QQ浏览器");
        gotoHuaweiNetworkSettings();
    }

    private void gotoHuaweiNetworkSettings(){
        Intent intent = new Intent();
        intent.setAction("huawei.intent.action.NETWORK_SETTING");
        startActivity(intent);
    }
}
