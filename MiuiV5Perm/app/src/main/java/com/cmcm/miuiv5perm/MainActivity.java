package com.cmcm.miuiv5perm;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        gotoAutoStartSetting(this, 0);
    }

    public static boolean gotoAutoStartSetting(final Context context, int source){

            Log.v("sunzy", "miui v5");
//            final String packageName = context.getPackageName();
//            try {
//                Intent intent = new Intent("miui.intent.action.APP_PERM_EDITOR");
//                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                intent.setClassName("com.android.settings", "com.miui.securitycenter.permission.AppPermissionsEditor");
//                intent.putExtra("extra_package_uid", context.getApplicationInfo().uid);
//                context.startActivity(intent);
//            } catch(Exception e) {
//                Log.wtf("sunzy", e);
//            }
        Intent intent = new Intent();
        ComponentName comp = new ComponentName("com.android.settings", "com.android.settings.BackgroundApplicationsManager");
        intent.setComponent(comp);
        context.startActivity(intent);
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
