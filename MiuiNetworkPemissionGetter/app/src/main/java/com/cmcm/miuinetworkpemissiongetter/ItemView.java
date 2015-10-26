package com.cmcm.miuinetworkpemissiongetter;

import android.content.Context;
import android.graphics.Color;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ItemView extends LinearLayout {

    private ListItemParams params;

    private ItemView(Context context, ListItemParams params) {
        super(context);
        this.params = params;
    }

    public void setLabel(String label){
        params.tvLabel.setText(label);
    }

    public void setPackageName(String packageName){
        params.tvPackageName.setText(packageName);
    }

    public void setPermissionWifi(String perm){
        params.tvPermissionWifi.setText(perm);
    }

    public void setPermissionData(String perm){
        params.tvPermissionData.setText(perm);
    }

    private static class ListItemParams{
        TextView tvLabel;
        TextView tvPackageName;
        TextView tvPermissionWifi;
        TextView tvPermissionData;
    }

    public static class Builder{

        private final Context context;

        public Builder(Context context){
            this.context = context;
        }

        public ItemView build(){
            ListItemParams p = new ListItemParams();
            ItemView item =  new ItemView(context, p);
            item.setOrientation(ItemView.VERTICAL);
            p.tvLabel = new TextView(context);
            p.tvLabel.setTextColor(Color.parseColor("#000000"));
            item.addView(p.tvLabel);
            p.tvPackageName = new TextView(context);
            p.tvPackageName.setTextColor(Color.parseColor("#000000"));
            item.addView(p.tvPackageName);
            p.tvPermissionWifi = new TextView(context);
            p.tvPermissionWifi.setTextColor(Color.parseColor("#000000"));
            item.addView(p.tvPermissionWifi);
            p.tvPermissionData = new TextView(context);
            p.tvPermissionData.setTextColor(Color.parseColor("#000000"));
            item.addView(p.tvPermissionData);
            return item;
        }
    }
}
