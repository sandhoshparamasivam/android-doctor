package com.orane.docassist;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.text.TextUtils;
import android.widget.Toast;


public class SmsBroadcastReceiver extends BroadcastReceiver {

    public static final String SMS_BUNDLE = "pdus";
    public String smsBody, address;

    public void onReceive(Context context, Intent intent) {
        Bundle intentExtras = intent.getExtras();

        System.out.println("Push Bundle Data-----" + bundle2string(intentExtras));

        try {
            if (intentExtras != null) {
                Object[] sms = (Object[]) intentExtras.get(SMS_BUNDLE);
                String smsMessageStr = "";
                for (int i = 0; i < sms.length; ++i) {
                    SmsMessage smsMessage = SmsMessage.createFromPdu((byte[]) sms[i]);

                    smsBody = smsMessage.getMessageBody();
                    address = smsMessage.getOriginatingAddress();

                    smsMessageStr += "SMS From: " + address + "\n";
                    smsMessageStr += smsBody + "\n";
                }
                System.out.println("smsMessageStr-----" + smsMessageStr);
                System.out.println("smsBody----" + smsBody);
                System.out.println("address----" + address);

                String sub_str_title = "ICLNIQ";
                String sub_str_msg = "Your iCliniq OTP is:";

                boolean title_check = address.toLowerCase().contains(sub_str_title.toLowerCase());
                boolean msg_check = smsBody.toLowerCase().contains(sub_str_msg.toLowerCase());

                System.out.println("Boolean title_check----" + title_check);
                System.out.println("Boolean msg_check----" + msg_check);


                if (title_check && msg_check) {

                    //String otp_value = smsBody.substring(smsBody.indexOf("Your iCliniq OTP is: ") + 4);
                    //String otp_value = smsBody.substring(21, 4);
                    String otp_value = TextUtils.substring(smsBody, 21, 25);

                    System.out.println("Broadcast_otp_value----" + otp_value);

                    OTPActivity isms = OTPActivity.instance();
                    isms.getSMS(otp_value);

                } else if (!title_check && !msg_check) {
                    System.out.println("Boolean check--Failed--");
                }

                Toast.makeText(context, smsMessageStr, Toast.LENGTH_SHORT).show();

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static String bundle2string(Bundle bundle) {
        if (bundle == null) {
            return null;
        }
        String string = "Bundle{";
        for (String key : bundle.keySet()) {
            string += " " + key + " => " + bundle.get(key) + ";";
        }
        string += " }Bundle";
        return string;
    }

}