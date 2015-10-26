package com.cmcm.miuinetworkpemissiongetter;

import android.app.ProgressDialog;
import android.database.ContentObserver;
import android.net.Uri;
import android.os.*;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.util.Log;
import android.widget.ListView;

import java.util.List;

public class MainActivity extends FragmentActivity {

    public static final String TAG = "sunzy";
    private static final Uri mFirewallUri = Uri.parse(String.format("content://%s/%s", "com.miui.networkassistant.provider", "firewall/*" ));
    private static final int MIUI_NETWORK_PERMISSION_LOADER_ID = 1;
    private ListView listView;
    private NetworkPermissionAdapter adapter;
    private ProgressDialog progressDialog;

    private ContentObserver observer = new ContentObserver(new Handler()) {
        @Override
        public void onChange(boolean selfChange) {
            super.onChange(selfChange);
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Log.v("sunzy", "on change");
                    if(!getLoaderManager().getLoader(MIUI_NETWORK_PERMISSION_LOADER_ID).isStarted() && !MainActivity.this.isFinishing()){
                        Log.v("sunzy", "force load after data change!");
                        MainActivity.this.getLoaderManager().initLoader(MIUI_NETWORK_PERMISSION_LOADER_ID, null, new MyLoaderCallback()).forceLoad();
                    }
                }
            });

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getContentResolver().registerContentObserver(mFirewallUri, true, observer);
        getLoaderManager().initLoader(MIUI_NETWORK_PERMISSION_LOADER_ID, null, new MyLoaderCallback()).forceLoad();
        listView = (ListView)findViewById(R.id.lv_permission);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading...");
        progressDialog.show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        getContentResolver().unregisterContentObserver(observer);
    }

    private class MyLoaderCallback implements android.app.LoaderManager.LoaderCallbacks<List<AppNetworkPermission>>{

        @Override
        public android.content.Loader<List<AppNetworkPermission>> onCreateLoader(int id, Bundle args) {
            return new MiuiNetworkPermissionLoader(MainActivity.this);
        }

        @Override
        public void onLoadFinished(android.content.Loader<List<AppNetworkPermission>> loader, List<AppNetworkPermission> data) {
            Log.v(TAG, "is finishing = " + isFinishing());
            if(!isFinishing()){
                Log.v(TAG, "11111111");
                if(adapter == null){
                    Log.v(TAG, "222222222");
                    adapter = new NetworkPermissionAdapter(MainActivity.this);
                    adapter.setData(data);
                    listView.setAdapter(adapter);
                } else {
                    Log.v(TAG, "3333333");
                    Log.v(TAG, "on load finished");
                    for (AppNetworkPermission perm : data){
                        Log.v(TAG, perm.toString());
                    }
                    adapter.setData(data);
                    adapter.notifyDataSetChanged();
                }
                Log.v(TAG, "4444444");
            }
            Log.v(TAG, "555555555555");
            if(progressDialog != null){
                progressDialog.dismiss();
            }
        }

        @Override
        public void onLoaderReset(android.content.Loader<List<AppNetworkPermission>> loader) {

        }
    }
}








