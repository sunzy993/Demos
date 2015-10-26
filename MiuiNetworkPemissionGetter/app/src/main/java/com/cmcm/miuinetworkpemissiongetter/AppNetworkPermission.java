package com.cmcm.miuinetworkpemissiongetter;

public class AppNetworkPermission{
    private String label;
    private String packageName;
    private int permissionData = -1;
    private int permissionWifi = -1;

    public AppNetworkPermission(String label, String packageName){
        this.label = label;
        this.packageName = packageName;
    }

    public String getLabel() {
        return label;
    }

    public String getPackageName() {
        return packageName;
    }

    public int getPermissionData() {
        return permissionData;
    }

    public void setPermissionData(int permissionData) {
        this.permissionData = permissionData;
    }

    public int getPermissionWifi() {
        return permissionWifi;
    }

    public void setPermissionWifi(int permissionWifi) {
        this.permissionWifi = permissionWifi;
    }

    @Override
    public String toString(){
        return label + " : " + packageName + " : " + permissionData + " : " + permissionWifi;
    }
}