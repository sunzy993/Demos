package com.cmcm.huaweiaccessibilitydemo;

import android.accessibilityservice.AccessibilityService;
import android.text.TextUtils;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;

import java.util.List;

public class HuaweiAccessibilityService extends AccessibilityService{

    private static final String TAG = "sunzy";

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        HuaweiAccessibilityHelper.handle(event, getRootInActiveWindow());
    }

    @Override
    public void onInterrupt() {
        Log.v(TAG, "on interrupt!");
    }
}
