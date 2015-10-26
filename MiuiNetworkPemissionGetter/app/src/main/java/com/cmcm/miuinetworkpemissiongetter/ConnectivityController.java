package com.cmcm.miuinetworkpemissiongetter;

import java.lang.reflect.Method;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.wifi.WifiManager;
import android.os.Build;

import android.os.Build.VERSION;
import android.provider.Settings;

public final class ConnectivityController {

	private static Method mSetMobileDataEnabled;
	private static Method mGetMobileDataEnabled;
	private static Method mEnableDataConnectivity;
	private static Method mDisableDataConnectivity;
	private static Method mIsDataConnectivityPossible;
	private static boolean mSupportGingerbreadAPI;
	private static boolean mSupportFroyoAPI;
	private static boolean sVerified;

	static {
		if (VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
			try {
				Class<ConnectivityManager> cls = ConnectivityManager.class;
				mSetMobileDataEnabled = cls.getDeclaredMethod("setMobileDataEnabled", boolean.class);
				mSetMobileDataEnabled.setAccessible(true);
				mGetMobileDataEnabled = cls.getDeclaredMethod("getMobileDataEnabled");
				mGetMobileDataEnabled.setAccessible(true);
				mSupportGingerbreadAPI = true;
			} catch (Exception e) {
				mSupportGingerbreadAPI = false;
			}
		}
		
		try {
			Class<?> cls = Class.forName("com.android.internal.telephony.ITelephony");
			mEnableDataConnectivity = cls.getDeclaredMethod("enableDataConnectivity");
			mEnableDataConnectivity.setAccessible(true);
			mDisableDataConnectivity = cls.getDeclaredMethod("disableDataConnectivity");
			mDisableDataConnectivity.setAccessible(true);
			mIsDataConnectivityPossible = cls.getDeclaredMethod("isDataConnectivityPossible");
			mIsDataConnectivityPossible.setAccessible(true);
			mSupportFroyoAPI = true;
		} catch (Exception e) {
			mSupportFroyoAPI = false;
		}
	}

	private Context mContext;
	private ConnectivityManager cm;

	public ConnectivityController(Context context) {
		this.mContext = context;
		this.cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
	}
	
	
	private void validateAPISupportIfNeeded(){
		synchronized (ConnectivityController.class) {
			if (!sVerified) {
				validateAPISupport();
				sVerified = true;
			}
		}
	}

	private void validateAPISupport() {
		if (mSupportGingerbreadAPI) {
			try {
				boolean tmpStatus = (Boolean) mGetMobileDataEnabled.invoke(cm);
				mSetMobileDataEnabled.invoke(cm, tmpStatus);
			} catch (Exception e) {
				mSupportGingerbreadAPI = false;
			}
		}
		
		if (mSupportFroyoAPI) {
			if (mContext.checkPermission(android.Manifest.permission.MODIFY_PHONE_STATE, android.os.Process.myPid(), android.os.Process.myUid()) == PackageManager.PERMISSION_DENIED) {
				mSupportFroyoAPI = false;
			}
		}
	}

	public boolean isDataEnable() {
//		validateAPISupportIfNeeded();
		if (mSupportGingerbreadAPI) {
			try {
				return (Boolean)mGetMobileDataEnabled.invoke(cm);
			} catch (Exception e) {
				return false;
			}
		} else if (mSupportFroyoAPI) {
			try {
				return (Boolean) mIsDataConnectivityPossible.invoke(null);
			} catch (Exception e) {
				return false;
			}
		} else {
			return APNUtils.getApnState(mContext);
		}
	}

	public void changeDataState(boolean enable){
		if(enable){
			enableConnectivity();
		} else {
			disableConnectivity();
		}
	}

	public void disableConnectivity() {
		validateAPISupportIfNeeded();
		if(VERSION.SDK_INT >= 21){
			Intent intent = new Intent(Settings.ACTION_DATA_ROAMING_SETTINGS);
			mContext.startActivity(intent);
			return;
		}

		if (mSupportGingerbreadAPI) {
			try {
				mSetMobileDataEnabled.invoke(cm, false);
				return;
			} catch (Exception e) {
			}
		}
		
		if (mSupportFroyoAPI) {
			try {
				mDisableDataConnectivity.invoke(null);
				return;
			} catch (Exception e) {
			}
		}
		
		if (APNUtils.getApnState(mContext)) {
			APNUtils.setApnState(mContext, false);
		}
	}

	public void enableConnectivity() {
		validateAPISupportIfNeeded();
		if(VERSION.SDK_INT >= 21){
			Intent intent = new Intent(Settings.ACTION_DATA_ROAMING_SETTINGS);
			mContext.startActivity(intent);
			return;
		}
		if (mSupportGingerbreadAPI) {
			try {
				mSetMobileDataEnabled.invoke(cm, true);
				return;
			} catch (Exception e) {
			}
		}
		
		if (mSupportFroyoAPI) {
			try {
				mEnableDataConnectivity.invoke(null);
				return;
			} catch (Exception e) {
			}
		}
		
		if (!APNUtils.getApnState(mContext)) {
			APNUtils.setApnState(mContext, true);
		}
	}

	public void changeWifiState(boolean enable){
		WifiManager wifiManager = (WifiManager) mContext.getSystemService(Context.WIFI_SERVICE);
		wifiManager.setWifiEnabled(enable);
	}

	public boolean isWifiEnabled(){
		WifiManager wifiManager = (WifiManager) mContext.getSystemService(Context.WIFI_SERVICE);
		return wifiManager.isWifiEnabled();
	}
}
