package com.cmcm.activitylifedemo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

public class ActivityB extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        logClassAndMethod();
    }

    @Override
    public void onCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
        logClassAndMethod();
    }

    @Override
    protected void onStart() {
        super.onStart();
        logClassAndMethod();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        logClassAndMethod();
    }

    @Override
    protected void onResume() {
        super.onResume();
        logClassAndMethod();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        logClassAndMethod();
    }

    @Override
    protected void onPause() {
        super.onPause();
        logClassAndMethod();
    }

    @Override
    protected void onStop() {
        super.onStop();
        logClassAndMethod();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        logClassAndMethod();
    }

    public void logClassAndMethod(){
        Throwable t = new Throwable();
        Log.v("sunzy", t.getStackTrace()[0].getClassName().toString() + " : " + t.getStackTrace()[1].getMethodName());
        Log.v("sunzy", "object = " + this.toString());
    }

}
