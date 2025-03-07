package com.faithyapp.capacitor.plugins.imessage;

import android.content.Intent;
import android.net.Uri;
import android.content.pm.PackageManager;
import android.util.Log;
import com.getcapacitor.*;
import com.getcapacitor.annotation.CapacitorPlugin;

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

        Intent intent;

        if (imageUrl != null && !imageUrl.isEmpty()) {
            intent = new Intent(Intent.ACTION_SEND);
            intent.setType("image/*");
            intent.putExtra(Intent.EXTRA_TEXT, text);

            try {
                Uri uri = Uri.parse(imageUrl);
                intent.putExtra(Intent.EXTRA_STREAM, uri);
            } catch (Exception e) {
                call.reject("Invalid image URL.");
                return;
            }

        } else {
            intent = new Intent(Intent.ACTION_SENDTO);
            intent.setData(Uri.parse("smsto:"));  // Ensures it opens an SMS/MMS app
            intent.putExtra("sms_body", text);
        }

        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        try {
            getContext().startActivity(intent);
            JSObject result = new JSObject();
            result.put("status", "sent");
            call.resolve(result);
        } catch (Exception e) {
            Log.e("IMessagePlugin", "Error launching SMS/MMS app", e);
            call.reject("Failed to open SMS/MMS app. Error: " + e.getMessage());
        }
    }
}