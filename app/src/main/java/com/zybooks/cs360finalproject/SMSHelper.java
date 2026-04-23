package com.zybooks.cs360finalproject;

import android.content.Context;
import android.telephony.SmsManager;

public class SMSHelper {
    // Simple helper for sending SMS messages to the user when quantity is low
    public static void sendSMS(Context context, String phoneNumber, String message) {
        SmsManager smsManager = context.getSystemService(SmsManager.class);
        smsManager.sendTextMessage(phoneNumber, null, message, null, null);
    }
}
