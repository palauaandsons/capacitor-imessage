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

        // ✅ Use ACTION_SENDTO to ensure the correct MMS app is used
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("mmsto:"));  // Ensures MMS (not SMS)
        intent.putExtra("sms_body", text);

        if (imageUrl != null && !imageUrl.isEmpty()) {
            try {
                Uri uri = Uri.parse(imageUrl);
                intent.putExtra(Intent.EXTRA_STREAM, uri);
                intent.setType("image/*");
            } catch (Exception e) {
                call.reject("Invalid image URL.");
                return;
            }
        }

        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        try {
            getContext().startActivity(intent);
            JSObject result = new JSObject();
            result.put("status", "sent");
            call.resolve(result);
        } catch (Exception e) {
            Log.e("IMessagePlugin", "Error launching MMS app", e);
            call.reject("Failed to open MMS app. Error: " + e.getMessage());
        }
    }
}