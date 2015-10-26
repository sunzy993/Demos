package com.cmcm.socketdemo;

import android.app.Service;
import android.content.Intent;
import android.net.LocalServerSocket;
import android.net.LocalSocket;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by i on 2015/10/15.
 */
public class SocketServerService extends Service {

    private static final String TAG = "sunzy";
    private LocalServerSocket localServerSocket;

    @Override
    public void onCreate() {
        Log.v(TAG, "SocketServerService onCreate!");

        new Thread(){
            @Override
            public void run(){
                try {
                    localServerSocket = new LocalServerSocket("mysocket");
                    Log.v(TAG,"mysocket created!");
                    while (true) {
                        LocalSocket receiver = localServerSocket.accept();
                        InputStream is = receiver.getInputStream();
                        if(is != null && is.available() > 0){
                            byte[] bytes = new byte[is.available()];
                            is.read(bytes);
                            Log.v(TAG, "receive message = " + new String(bytes));
                            Log.v(TAG, "file descriptor = " + receiver.getFileDescriptor());
                            //java.lang.UnsupportedOperationException
                            //Log.v(TAG, "file descriptor = " + receiver.getRemoteSocketAddress());
                            Log.v(TAG, "local socket address = " + receiver.getLocalSocketAddress());
                            Log.v(TAG, "local socket timeout = " + receiver.getSoTimeout());
                            String responseMessage = "fuck off";
                            receiver.getOutputStream().write(responseMessage.getBytes());
                            receiver.getOutputStream().close();
                        }
                    }
                } catch (IOException e) {
                    Log.wtf(TAG, e);
                }
            }
        }.start();
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.v(TAG, "SocketServerService onStartCommand!");
        return START_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
