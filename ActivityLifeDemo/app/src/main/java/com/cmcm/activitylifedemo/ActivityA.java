package com.cmcm.activitylifedemo;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

public class ActivityA extends Activity implements View.OnClickListener{

    private static final String TAG = "sunzy";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.tv).setOnClickListener(this);
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
        Log.v(TAG, t.getStackTrace()[0].getClassName().toString() + " : " + t.getStackTrace()[1].getMethodName());
        Log.v(TAG, "object = " + this.toString());
    }

    @Override
    public void onClick(View v) {
        //new AlertDialog.Builder(this).setMessage("hahahahha").setTitle("hhhh").create().show();
        Intent i = new Intent(this, ActivityB.class);
        startActivity(i);
    }
}
