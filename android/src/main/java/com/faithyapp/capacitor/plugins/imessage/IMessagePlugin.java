package com.faithyapp.capacitor.plugins.imessage;

import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import com.getcapacitor.*;
import com.getcapacitor.annotation.CapacitorPlugin;

/**
 * IMessagePlugin: Opens the user's default SMS/MMS app to send messages.
 * - If only text is provided, it uses ACTION_SENDTO (SMS).
 * - If an image is also provided, it uses ACTION_SEND (MMS).
 */
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
        // Get parameters
        String text = call.getString("text", "");        // The text message
        String imageUrl = call.getString("imageUrl");    // Optional image URL

        Intent intent;

        // CASE 1: If there's an image, we treat it as MMS
        if (imageUrl != null && !imageUrl.isEmpty()) {
            // Use ACTION_SEND for MMS
            intent = new Intent(Intent.ACTION_SEND);
            intent.setType("image/*");
            intent.putExtra(Intent.EXTRA_TEXT, text);

            // Attach the image (must be a valid URI)
            try {
                Uri uri = Uri.parse(imageUrl);
                intent.putExtra(Intent.EXTRA_STREAM, uri);
            } catch (Exception e) {
                call.reject("Invalid image URL.");
                return;
            }
        }
        // CASE 2: Otherwise, SMS text only
        else {
            // Use ACTION_SENDTO for SMS
            intent = new Intent(Intent.ACTION_SENDTO);
            // "smsto:" ensures only SMS apps will handle this intent
            intent.setData(Uri.parse("smsto:"));
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