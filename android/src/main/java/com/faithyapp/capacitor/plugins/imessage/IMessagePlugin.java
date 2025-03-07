package com.faithyapp.capacitor.plugins.imessage;

import android.content.Intent;
import android.net.Uri;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.util.Log;
import com.getcapacitor.*;
import com.getcapacitor.annotation.CapacitorPlugin;

import java.util.List;

@CapacitorPlugin(name = "IMessage")
public class IMessagePlugin extends Plugin {

    @PluginMethod
    public void isMessagingAvailable(PluginCall call) {
        boolean available = getContext().getPackageManager().hasSystemFeature("android.hardware.telephony");

        JSObject result = new JSObject();
        result.put("available", available);
        call.resolve(result);
    }

    @PluginMethod
    public void sendMessage(PluginCall call) {
        String text = call.getString("text", "");
        String imageUrl = call.getString("imageUrl");

        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse("sms:")); // Opens messaging app
        intent.putExtra("sms_body", text);

        if (imageUrl != null && !imageUrl.isEmpty()) {
            Uri uri = Uri.parse(imageUrl);
            intent.putExtra(Intent.EXTRA_STREAM, uri);
            intent.setType("image/*");
        }

        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        String googleMessagesPackage = "com.google.android.apps.messaging";
        if (isPackageInstalled(googleMessagesPackage)) {
            intent.setPackage(googleMessagesPackage);
        } else {
            call.reject("Google Messages is not installed.");
            return;
        }

        try {
            getContext().startActivity(intent);
            JSObject result = new JSObject();
            result.put("status", "sent");
            call.resolve(result);
        } catch (Exception e) {
            call.reject("Failed to open Google Messages.");
        }
    }

    private boolean isPackageInstalled(String packageName) {
        PackageManager pm = getContext().getPackageManager();
        try {
            pm.getPackageInfo(packageName, PackageManager.GET_ACTIVITIES);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }
}