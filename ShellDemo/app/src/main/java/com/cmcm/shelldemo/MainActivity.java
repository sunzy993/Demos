package com.cmcm.shelldemo;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import java.io.IOException;
import java.io.InputStream;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        executeCommand();

    }

    public void executeCommand(){
        try {
            Process process = Runtime.getRuntime().exec("ls");
            process.waitFor();

            InputStream is = process.getInputStream();
            byte[] bytes = new byte[is.available()];
            is.read(bytes);
            String str = new String(bytes);
            Log.v("sunzy", str);
        } catch (IOException e) {
            Log.wtf("sunzy", e);
        } catch (InterruptedException e) {
            Log.wtf("sunzy", e);
        }
    }
}
