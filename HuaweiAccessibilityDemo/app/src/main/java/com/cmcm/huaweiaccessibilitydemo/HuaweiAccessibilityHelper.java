package com.cmcm.huaweiaccessibilitydemo;

import android.graphics.Rect;
import android.os.Build;
import android.text.TextUtils;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

public class HuaweiAccessibilityHelper {

    private static final String TAG = "sunzy";
    private static boolean isHuawei = TextUtils.equals("HUAWEI", Build.MANUFACTURER);
    public static Collection<String> appNames;
    private static int expectingEventType = AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED;

    public static void handle(AccessibilityEvent event, AccessibilityNodeInfo rootNodeInfo) {
        if(!shouldHandle(event)){
            return;
        }
        switch (event.getEventType()){
            case AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED:
                if (TextUtils.equals(event.getClassName(), "com.huawei.netmanager.ui.NetWorkSettingActivity")) {
                    Log.v(TAG, "entering Huawei NetWorkSettingActivity...");
                    expectingEventType = AccessibilityEvent.TYPE_VIEW_SCROLLED;
                } else {
                    Log.v(TAG, "leaving Huawei NetWorkSettingActivity...");
                    expectingEventType = AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED;
                }
                break;
            case AccessibilityEvent.TYPE_VIEW_SCROLLED:
                AccessibilityNodeInfo listNodeInfo = null;
                if(TextUtils.equals(event.getClassName(), "android.widget.ListView")){
                    AccessibilityNodeInfo temp = event.getSource();
                    if(temp != null){
                        Rect rect = new Rect();
                        temp.getBoundsInParent(rect);
                        if(rect.bottom > 0){
                            listNodeInfo = temp;
                        }
                    }
                }
                if(listNodeInfo != null){
                    HashSet<String> appRemoved = new HashSet<>();
                    for (String appName : appNames){
                        List<AccessibilityNodeInfo> list = rootNodeInfo.findAccessibilityNodeInfosByText(appName);
                        if(list != null && list.size() > 0){
                            AccessibilityNodeInfo labelNodeInfo = list.get(0);
                            if(labelNodeInfo != null){
                                labelNodeInfo.getParent().getChild(3).getChild(0).performAction(AccessibilityNodeInfo.ACTION_CLICK);
                                appRemoved.add(appName);
                                //TODO one app check state changed, invoke callback here
                                Log.v(TAG, "removing  " + appName);
                            } else {
                                Log.v(TAG, "not found " + appName);
                            }
                        } else {
                            Log.v(TAG, "not found " + appName);
                        }
                    }
                    appNames.removeAll(appRemoved);
                    if(appNames.size() > 0) {
                        Log.v(TAG, "still have, scrolling...  " + appNames.toString());
                        listNodeInfo.performAction(AccessibilityNodeInfo.ACTION_SCROLL_FORWARD);
                        expectingEventType = AccessibilityEvent.TYPE_VIEW_SCROLLED;
                    } else {
                        expectingEventType = AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED;
                        Log.v(TAG, "over!");
                        //TODO all app check state changed, invoke callback here
                    }
                }
                break;
            case AccessibilityEvent.TYPE_VIEW_CLICKED:
                // 0. will get wrong label while list is scrolling by program(by accessibility AccessibilityNodeInfo.ACTION_SCROLL_FORWARD action), so will not observe the click event while actions is performing
                // 1. appNames != null means is performing actions, so return
                // 2. appNames.size() == 0 means just finish the action, so this single click event will not be handled since it's caused by program(by accessibility AccessibilityNodeInfo.ACTION_CLICK)
                if(appNames != null){
                    if(appNames.size() == 0){
                        appNames = null;
                    }
                    return;
                }
                AccessibilityNodeInfo clickedNodeInfo = event.getSource();
                if(clickedNodeInfo != null && TextUtils.equals(clickedNodeInfo.getClassName(), "android.widget.CheckBox")) {
                    //TODO check state changed by user, invoke callback here
                    Log.v(TAG, clickedNodeInfo.getParent().getParent().getChild(0).getText().toString() + " was clicked!");
                }
                break;
        }
    }

    private static boolean shouldHandle(AccessibilityEvent event){
        //Log.v(TAG, "got an event, type = " + type + ", class = " + event.getClassName());
        // the click event will always be handled
        if(event.getEventType() == AccessibilityEvent.TYPE_VIEW_CLICKED){
            return true;
        }
        if(event == null || appNames == null || appNames.size() == 0){
            return false;
        }
        if (!isHuawei) {
            return false;
        }
        if(expectingEventType != event.getEventType()){
            return false;
        }
        return true;
    }
}
