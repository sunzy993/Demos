package com.cmcm.boundservicedemo;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import android.util.Log;

import java.io.FileDescriptor;

public class MyService extends Service {

    private static final String TAG = "sunzy";
    private LocalBinder binder = new LocalBinder();
    public class LocalBinder extends Binder{
        public MyService getService(){
            return MyService.this;
        }
    }

    public MyService() {
        Log.v(TAG, "my service constructor!");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.v(TAG, "my service created!");
    }

    @Override
    public void onDestroy() {
        Log.v(TAG, "my service destroyed!");
        super.onDestroy();
    }

    @Override
    public IBinder onBind(Intent intent) {
        Log.v(TAG, "my service onbind!");
        return binder;
    }

    public int getCount(){
        return 10;
    }

}
