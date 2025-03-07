package com.faithyapp.capacitor.plugins.imessage;

import android.content.Intent;
import android.net.Uri;
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
        
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse("sms:"));  // Opens default messaging app
        intent.putExtra("sms_body", text);  // Pre-fills the text message

        if (imageUrl != null && !imageUrl.isEmpty()) {
            Uri uri = Uri.parse(imageUrl);
            intent.setType("image/*");
            intent.putExtra(Intent.EXTRA_STREAM, uri);
        }

        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        try {
            getContext().startActivity(intent);

            JSObject result = new JSObject();
            result.put("status", "sent");
            call.resolve(result);
        } catch (Exception e) {
            call.reject("Failed to open messaging app.");
        }
    }
}