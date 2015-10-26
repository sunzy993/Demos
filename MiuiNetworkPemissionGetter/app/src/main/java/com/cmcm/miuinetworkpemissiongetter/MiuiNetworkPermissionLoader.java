package com.cmcm.miuinetworkpemissiongetter;

import android.app.ActivityManager;
import android.content.AsyncTaskLoader;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.net.LocalSocket;
import android.net.LocalSocketAddress;
import android.text.TextUtils;
import android.util.Log;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MiuiNetworkPermissionLoader extends AsyncTaskLoader<List<AppNetworkPermission>> {

    private static final String TAG = "sunzy";
    private Context context;
    private boolean isWifiOn;
    private ConnectivityController connectivityController;

    public MiuiNetworkPermissionLoader(Context context) {
        super(context);
        this.context = context.getApplicationContext();
        connectivityController = new ConnectivityController(context);
    }

    @Override
    public List<AppNetworkPermission> loadInBackground() {
        Log.v(TAG, "load in background...");
        // to calculate the loading time
        long startTime = System.currentTimeMillis();
        HashMap<String, AppNetworkPermission> pnPermMap = new HashMap<>();
        PackageManager packageManager = context.getPackageManager();
        //to be clear, show user app only, you can filter apps here
        List<ApplicationInfo> installedApp = packageManager.getInstalledApplications(0);
        HashMap<String, ApplicationInfo> userApps = new HashMap<>();
        for (ApplicationInfo info : installedApp){
            if((info.flags & ApplicationInfo.FLAG_SYSTEM) == 0 && (info.flags & ApplicationInfo.FLAG_UPDATED_SYSTEM_APP) == 0){
                userApps.put(info.packageName, info);
            }
        }
        // recode the current state
        isWifiOn = connectivityController.isWifiEnabled();
        // scan the permission
        queryPermissions(userApps, pnPermMap, false);
        // toggle, and do it again
        connectivityController.changeWifiState(!isWifiOn);
        // give XimiFirewall some time to react
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        // and do it again
        queryPermissions(userApps, pnPermMap, true);
        // do some recovery
        connectivityController.changeWifiState(isWifiOn);
        long endTime = System.currentTimeMillis();
        Log.v(TAG, "time consumed = " + (endTime - startTime));
        //TODO You may sort the data here...
        return new ArrayList<>(pnPermMap.values());
    }

    private void queryPermissions(HashMap<String, ApplicationInfo> userApps, HashMap<String, AppNetworkPermission> pnPermMap, boolean isData){
        ActivityManager activityManager = (ActivityManager)context.getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningAppProcessInfo appProcess : activityManager.getRunningAppProcesses()) {
            String packageName = appProcess.processName;
            if(TextUtils.isEmpty(packageName)) continue;
            if(packageName.contains(":")){
                packageName = packageName.substring(0, packageName.lastIndexOf(':'));
            }
            if(!userApps.containsKey(packageName)){
                continue;
            }
            if(!pnPermMap.containsKey(packageName)){
                pnPermMap.put(packageName, new AppNetworkPermission(context.getPackageManager().getApplicationLabel(userApps.get(packageName)).toString(), packageName));
            }
            AppNetworkPermission appNetworkPermission = pnPermMap.get(packageName);
            if(isData){
                appNetworkPermission.setPermissionData(queryPermissionByPid(appProcess.pid));
            } else {
                appNetworkPermission.setPermissionWifi(queryPermissionByPid(appProcess.pid));
            }
            pnPermMap.put(packageName, appNetworkPermission);
            //Log.v(TAG, "querying pid = " + appProcess.pid + ", process name = " + appProcess.processName + ", package name = " + packageName);
        }
    }

    public static final int MODE_ALLOWED = 0;
    public static final int MODE_DENIED = 1;
    public static final int MODE_UNKNOWN = -1;
    private static final String MIUI_NETWORK_PERMISSION_SOCKET_NAME = "mfwd";

    private static int queryPermissionByPid(int pid) {
        int result = MODE_UNKNOWN;
        LocalSocket localSocket = new LocalSocket();
        try {
            localSocket.connect(new LocalSocketAddress(MIUI_NETWORK_PERMISSION_SOCKET_NAME));
            OutputStream outputStream = localSocket.getOutputStream();
            outputStream.write(intToBytes(pid));
            byte[] buffer = new byte[4];
            while (true) {
                try {
                    int count = localSocket.getInputStream().read(buffer);
                    if (count > 0) {
                        result = bytesToInt(buffer);
                        break;
                    }
                } catch (IOException e1) {
                    Log.wtf(MainActivity.TAG, e1);
                    break;
                }
            }
        } catch (IOException e2) {
            Log.wtf(MainActivity.TAG, e2);
        }
        try {
            localSocket.shutdownInput();
            localSocket.shutdownOutput();
            localSocket.close();
        } catch (IOException e) {
            Log.wtf(TAG, e);
        }
        return result;
    }

    private static byte[] intToBytes(int paramInt) {
        byte[] arrayOfByte = new byte[4];
        arrayOfByte[0] = ((byte)(0xFF & paramInt >>> 24));
        arrayOfByte[1] = ((byte)(0xFF & paramInt >>> 16));
        arrayOfByte[2] = ((byte)(0xFF & paramInt >>> 8));
        arrayOfByte[3] = ((byte)(paramInt & 0xFF));
        return arrayOfByte;
    }

    private static int bytesToInt(byte[] paramArrayOfByte) {
        int i = 0;
        for (int j = 0; j < 4; j++) {
            i = i << 8 | 0xFF & paramArrayOfByte[j];
        }
        return i;
    }

}
