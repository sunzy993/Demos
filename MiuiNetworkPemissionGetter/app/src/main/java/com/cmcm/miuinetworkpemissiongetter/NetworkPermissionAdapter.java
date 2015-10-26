package com.cmcm.miuinetworkpemissiongetter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.List;

public class NetworkPermissionAdapter extends BaseAdapter {

    private Context context;
    private List<AppNetworkPermission> data;

    public NetworkPermissionAdapter(Context context){
        this.context = context.getApplicationContext();
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ItemView item;
        if(convertView == null){
            item = new ItemView.Builder(context).build();
        } else {
            item = (ItemView)convertView;
        }
        AppNetworkPermission anp = data.get(position);
        item.setLabel("应用：" + anp.getLabel());
        item.setPackageName("包名：" + anp.getPackageName());
        if(anp.getPermissionData() == 0){
            item.setPermissionData("数据：Allowed");
        } else if(anp.getPermissionData() == 1){
            item.setPermissionData("数据：Denied");
        } else {
            item.setPermissionData("数据：Unkown");
        }
        if(anp.getPermissionWifi() == 0){
            item.setPermissionWifi("WIFI：Allowed");
        } else if(anp.getPermissionWifi() == 1){
            item.setPermissionWifi("WIFI：Denied");
        } else {
            item.setPermissionWifi("WIFI：Unkown");
        }
        return item;
    }

    public void setData(List<AppNetworkPermission> data){
        if(data != null && data.size() > 0){
            this.data = data;
        }
    }
}