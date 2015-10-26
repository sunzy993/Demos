package com.cmcm.socketdemo;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Intent;
import android.net.LocalSocket;
import android.net.LocalSocketAddress;
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
import android.widget.Toast;

import org.apache.http.params.HttpParams;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends Activity implements View.OnClickListener{

    private String TAG = "sunzy";
    private int offset = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Intent intent = new Intent(this, SocketServerService.class);
        Log.v(TAG, "MainActivity onCreate!");
        startService(intent);
        findViewById(R.id.fab).setOnClickListener(this);
        new Thread(new Runnable() {
            @Override
            public void run() {
                downloadPage();
            }
        }).start();

    }

    @Override
    public void onClick(View v) {
        LocalSocket localSocket = new LocalSocket();
        try {
            localSocket.connect(new LocalSocketAddress("mfwd"));
            //String str = "hello, beautiful girl.";
            ActivityManager mActivityManager = (ActivityManager)getSystemService(ACTIVITY_SERVICE);
            int id = 0;
            for (ActivityManager.RunningAppProcessInfo appProcess : mActivityManager.getRunningAppProcesses()) {
                if(TextUtils.equals(appProcess.processName, "com.cleanmaster.mguard_cn")){
                    Log.v(TAG, "got id = " + id);
                    id = appProcess.pid;
                }
            }
            localSocket.getOutputStream().write(intToBytes(id));
            //cannot close��or else broken pipe
            //localSocket.getOutputStream().close();
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                Log.wtf(TAG, e);
            }
            InputStream is = localSocket.getInputStream();
            byte[] bytes = new byte[is.available()];
            Log.v(TAG, "bytes size = " + bytes.length);
            is.read(bytes);
            Toast.makeText(this, "result = " + bytesToInt(bytes), Toast.LENGTH_LONG).show();
        } catch (IOException e) {
            Log.wtf(TAG, e);
            Log.wtf(TAG, e.getCause());
        }

    }


   public byte[] intToBytes(int paramInt)
    {
      byte[] arrayOfByte = new byte[4];
      arrayOfByte[0] = ((byte)(0xFF & paramInt >>> 24));
      arrayOfByte[1] = ((byte)(0xFF & paramInt >>> 16));
      arrayOfByte[2] = ((byte)(0xFF & paramInt >>> 8));
      arrayOfByte[3] = ((byte)(paramInt & 0xFF));
      return arrayOfByte;
    }

    public int bytesToInt(byte[] paramArrayOfByte)
    {
        int i = 0;
        for (int j = 0; j < 4; j++) {
            i = i << 8 | 0xFF & paramArrayOfByte[j];
        }
        return i;
    }

    public void downloadPage(){
        String validateURL="http://www.baidu.com/";

        try {
            URL url = new URL(validateURL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setConnectTimeout(5000);
            conn.setRequestMethod("GET");
            conn.connect();
            DataInputStream dis = new DataInputStream(conn.getInputStream());
            byte[] bytes = new byte[9000];
            dis.read(bytes);
            Log.v(TAG, new String(bytes));

            if (conn.getResponseCode() != HttpURLConnection.HTTP_OK) {

            }

        } catch (Exception e) {
            Log.wtf(TAG, e);
        }
    }






}
