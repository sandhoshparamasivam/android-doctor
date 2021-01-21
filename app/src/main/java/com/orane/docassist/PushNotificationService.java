/*
package com.orane.docassist;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;

import com.google.android.gms.gcm.GcmListenerService;
import com.kissmetrics.sdk.KISSmetricsAPI;
import com.orane.docassist.Model.Model;

import java.text.DateFormat;
import java.util.Date;
import java.util.HashMap;

import static com.zipow.videobox.confapp.ConfMgr.getApplicationContext;

public class PushNotificationService extends GcmListenerService {

    Intent intent;
    PendingIntent pIntent;
    String ptype, qases_id;

    SharedPreferences sharedpreferences;
    public static final String MyPREFERENCES = "MyPrefs";
    public static final String Login_Status = "Login_Status_key";
    public static final String noti_status = "noti_status_key";
    public static final String noti_sound = "noti_sound_key";
    public String noti_sound_val, stop_noti_val;

    String open_url;
    Bundle bundle;

    @Override
    public void onMessageReceived(String from, Bundle data) {

        //Model.kiss = KISSmetricsAPI.sharedAPI(Model.kissmetric_apikey, getApplicationContext());
        System.out.println("Push Bundle Data-----" + bundle2string(data));

        Model.push_msg = data.getString("message");
        Model.push_type = data.getString("msg_type");
        Model.push_title = data.getString("title");
        Model.fcode = "";

        try {
            if ((Model.push_type).equals("22")) {
                Model.followup_code = data.getString("followup_code");
                Model.from_name = data.getString("from_name");
            } else if ((Model.push_type).equals("29")) {

                Model.fcode = data.getString("fcode");
                Model.qid = data.getString("id");

                Model.push_url = data.containsKey("url") ? data.getString("url") : "nourl";
                System.out.println("Model.push_url ------------" + Model.push_url);
                //Model.push_url = data.getString("url");

                Model.push_title = "Hotline message received";
                System.out.println("Model.fcode-----" + Model.fcode);
                System.out.println("Model.qid-----" + Model.qid);

            } else if ((Model.push_type).equals("23")) {
                Model.push_cons_id = data.getString("id");
                Model.push_cons_from_name = data.getString("from_name");
                Model.push_cons_startdate = data.getString("startDate");
                Model.push_cons_tz = data.getString("tz");
            } else if ((Model.push_type).equals("24")) {
                Model.push_cons_id = data.getString("id");
                Model.push_cons_from_name = data.getString("from_name");
                Model.push_cons_startdate = data.getString("startDate");
                Model.push_cons_tz = data.getString("tz");
            } else if ((Model.push_type).equals("26")) {
                //Model.followup_code = data.getString("followup_code");
                Model.from_name = data.getString("from_name");
            } else if ((Model.push_type).equals("30")) {

                Model.appt_id = data.getString("id");
                Model.meeting_id = data.getString("meeting_id");
                Model.push_title = "Video Call Notification Received";
                System.out.println("Doc Side appt_id-----" + Model.appt_id);
                System.out.println("Doc Side meeting_id-----" + Model.meeting_id);

                open_url = bundle.containsKey("open_url") ? bundle.getString("open_url") : "";

            } else if ((Model.push_type).equals("31")) {
                System.out.println("Model.push_msg-----" + Model.push_msg);
                System.out.println("Model.push_type-----" + Model.push_type);
                System.out.println("Model.push_title-----" + Model.push_title);

            } else if ((Model.push_type).equals("32")) {
                qases_id = data.getString("id");

                System.out.println("Model.push_msg-----" + Model.push_msg);
                System.out.println("Model.push_type-----" + Model.push_type);
                System.out.println("Model.push_title-----" + Model.push_title);
                System.out.println("qases_id-----" + qases_id);

            } else {
                //Model.followup_code = data.getString("followup_code");
                Model.from_name = data.getString("from_name");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            if ((Model.fcode) != null && !(Model.fcode).isEmpty() && !(Model.fcode).equals("null") && !(Model.fcode).equals("")) {
                if ((Model.screen_status) != null && !(Model.screen_status).isEmpty() && !(Model.screen_status).equals("null") && !(Model.screen_status).equals("")) {
                    if ((Model.screen_status).equals("true")) {
                        //Model.fcode = Model.push_msg;
                        System.out.println("Push Model.screen_status-----" + Model.screen_status);
                        System.out.println("Push Blocked-sent to View-----");
                    } else {
                        System.out.println("Push Model.screen_status-----" + Model.screen_status);
                        System.out.println("Push one Exception------");
                        Notification();
                    }
                } else {
                    System.out.println("screen_status Exception------");
                    System.out.println("Push screen_status-----" + Model.screen_status);
                    Notification();
                }
            } else {
                System.out.println("Push Two Exception------");
                System.out.println("Push Model.screen_status-----" + Model.screen_status);
                Notification();
            }
        } catch (Exception e) {
            System.out.println("Push Exception------" + e.toString());
        }
    }


    public void Notification() {

*/
/*
        String strtitle = "Title";
        String strtext = "This is Smaple msg";
*//*

        // Open NotificationView Class on Notification Click

        if ((Model.push_type).equals("22")) {
            intent = new Intent(this, NewQueriesActivity.class);
            intent.putExtra("title", Model.push_title);
            intent.putExtra("text", Model.push_msg);
            Model.launch = "PushNotificationService";

        } else if ((Model.push_type).equals("30")) {

            if (open_url != null && !open_url.isEmpty() && !open_url.equals("null") && !open_url.equals("")) {
                intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(open_url));
            } else {
                intent = new Intent(this, UpcomingConsView.class);
                Bundle to_consview = new Bundle();
                to_consview.putString("consid", (Model.appt_id));
                to_consview.putString("cons_view_type", "Upcoming");
                intent.putExtras(to_consview);
                Model.launch = "PushNotificationService";
            }
        } else if ((Model.push_type).equals("23")) {
            intent = new Intent(this, ConsultationsActivity.class);
            intent.putExtra("title", Model.push_title);
            intent.putExtra("text", Model.push_msg);
            Model.launch = "PushNotificationService";
        } else if ((Model.push_type).equals("24")) {
            intent = new Intent(this, ConsultationsActivity.class);
            intent.putExtra("title", Model.push_title);
            intent.putExtra("text", Model.push_msg);
            Model.launch = "PushNotificationService";
        } else if ((Model.push_type).equals("29")) {
            intent = new Intent(this, HotlineChatViewActivity.class);
            intent.putExtra("follouwupcode", Model.fcode);
            Model.launch = "PushNotificationService";
        } else if ((Model.push_type).equals("26")) {
            intent = new Intent(this, NewQueriesActivity.class);
            intent.putExtra("title", Model.push_title);
            intent.putExtra("text", Model.push_msg);
            Model.launch = "PushNotificationService";
        } else if ((Model.push_type).equals("31")) {
            intent = new Intent(this, NotificationViewActivity.class);
            intent.putExtra("push_msg", Model.push_msg);
            intent.putExtra("push_type", Model.push_type);
            intent.putExtra("push_title", Model.push_title);
        } else if ((Model.push_type).equals("32")) {
            intent = new Intent(this, Qases_View_Activity.class);
            intent.putExtra("id", qases_id);
            Model.launch = "PushNotificationService";
        } else {
            intent = new Intent(this, NewQueriesActivity.class);
            intent.putExtra("title", Model.push_title);
            intent.putExtra("text", Model.push_msg);
            Model.launch = "PushNotificationService";
        }

        try {

            String currentDateTimeString = DateFormat.getDateTimeInstance().format(new Date());

            HashMap<String, String> properties = new HashMap<String, String>();
            Model.kiss.record("android.doc.pushnotification_received");
            properties.put("android.doc.push.title", Model.push_title);
            properties.put("android.doc.push.msg", Model.push_msg);
            properties.put("android.doc.push.type", Model.push_type);
            properties.put("android.doc.Receive_Time", currentDateTimeString);
            Model.kiss.set(properties);

        } catch (Exception e) {
            e.printStackTrace();
        }


       */
/* // Open NotificationView.java Activity
        pIntent = PendingIntent.getActivity(getApplicationContext(), 0, intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_ONE_SHOT);
        //Create Notification using NotificationCompat.Builder
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                // Set Icon
                .setSmallIcon(R.mipmap.logo_fordoctors)
                // Set Ticker Message
                .setTicker(Model.push_title)
                // Set Title
                .setContentTitle(Model.push_title)

                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.logo_fordoctors))
                .setDefaults(new NotificationCompat().DEFAULT_SOUND)
                //.setDefaults(Notification.DEFAULT_SOUND)
                // Set Text
                .setContentText(Model.push_msg)

                // Add an Action Button below Notification
                //.addAction(R.mipmap.logo, "Action Button", pIntent)
                // Set PendingIntent into Notification
                .setContentIntent(pIntent)
                // Dismiss Notification
                .setAutoCancel(true);
        // Create Notification Manager
        NotificationManager notificationmanager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        // Build Notification with Notification Manager
        notificationmanager.notify(0, builder.build());*//*



        //================ Initialize ======================---------------
        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        stop_noti_val = sharedpreferences.getString(noti_status, "off");
        noti_sound_val = sharedpreferences.getString(noti_sound, "off");
        //================ Initialize ======================---------------


        System.out.println("Sounds is ON");
        Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        Ringtone r = RingtoneManager.getRingtone(getApplicationContext(), notification);
        r.play();

        //---------------- Normal Notify -----------------------------------------
        pIntent = PendingIntent.getActivity(getApplicationContext(), 0, intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_ONE_SHOT);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.logo_fordoctors)
                .setTicker(Model.push_title)
                .setContentTitle(Model.push_title)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.logo))
                //.setDefaults(new NotificationCompat().DEFAULT_SOUND)
                //.setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                .setContentText(Model.push_msg)
                .setContentIntent(pIntent)
                .setAutoCancel(true);

        NotificationManager notificationmanager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notificationmanager.notify(0, builder.build());
        //---------------- Normal Notify -----------------------------------------



    */
/*    if (stop_noti_val.equals("off")) {
            System.out.println("Noti is ON");


            if (noti_sound_val.equals("on")) {
                System.out.println("Sounds is ON");
                Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                Ringtone r = RingtoneManager.getRingtone(getApplicationContext(), notification);
                r.play();
            } else {
                ((Vibrator) getSystemService(VIBRATOR_SERVICE)).vibrate(1000);
                System.out.println("Sounds is OFF");
            }


            //---------------- Normal Notify -----------------------------------------
            pIntent = PendingIntent.getActivity(getApplicationContext(), 0, intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_ONE_SHOT);
            NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                    .setSmallIcon(R.mipmap.logo_fordoctors)
                    .setTicker(Model.push_title)
                    .setContentTitle(Model.push_title)
                    .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.logo))
                    //.setDefaults(new NotificationCompat().DEFAULT_SOUND)
                    //.setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                    .setContentText(Model.push_msg)
                    .setContentIntent(pIntent)
                    .setAutoCancel(true);


            NotificationManager notificationmanager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            notificationmanager.notify(0, builder.build());
            //---------------- Normal Notify -----------------------------------------
        } else {
            System.out.println("Noti is OFF");
        }*//*

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



    public void Notification() {

*/
/*
        String strtitle = "Title";
        String strtext = "This is Smaple msg";
*//*

        // Open NotificationView Class on Notification Click

        if ((Model.push_type).equals("22")) {
            intent = new Intent(this, NewQueriesActivity.class);
            intent.putExtra("title", Model.push_title);
            intent.putExtra("text", Model.push_msg);
            Model.launch = "PushNotificationService";

        } else if ((Model.push_type).equals("30")) {

            if (open_url != null && !open_url.isEmpty() && !open_url.equals("null") && !open_url.equals("")) {
                intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(open_url));
            } else {
                intent = new Intent(this, UpcomingConsView.class);
                Bundle to_consview = new Bundle();
                to_consview.putString("consid", (Model.appt_id));
                to_consview.putString("cons_view_type", "Upcoming");
                intent.putExtras(to_consview);
                Model.launch = "PushNotificationService";
            }
        } else if ((Model.push_type).equals("23")) {
            intent = new Intent(this, ConsultationsActivity.class);
            intent.putExtra("title", Model.push_title);
            intent.putExtra("text", Model.push_msg);
            Model.launch = "PushNotificationService";
        } else if ((Model.push_type).equals("24")) {
            intent = new Intent(this, ConsultationsActivity.class);
            intent.putExtra("title", Model.push_title);
            intent.putExtra("text", Model.push_msg);
            Model.launch = "PushNotificationService";
        } else if ((Model.push_type).equals("29")) {
            intent = new Intent(this, HotlineChatViewActivity.class);
            intent.putExtra("follouwupcode", Model.fcode);
            Model.launch = "PushNotificationService";
        } else if ((Model.push_type).equals("26")) {
            intent = new Intent(this, NewQueriesActivity.class);
            intent.putExtra("title", Model.push_title);
            intent.putExtra("text", Model.push_msg);
            Model.launch = "PushNotificationService";
        } else if ((Model.push_type).equals("31")) {
            intent = new Intent(this, NotificationViewActivity.class);
            intent.putExtra("push_msg", Model.push_msg);
            intent.putExtra("push_type", Model.push_type);
            intent.putExtra("push_title", Model.push_title);
        } else if ((Model.push_type).equals("32")) {
            intent = new Intent(this, Qases_View_Activity.class);
            intent.putExtra("id", qases_id);
            Model.launch = "PushNotificationService";
        } else {
            intent = new Intent(this, NewQueriesActivity.class);
            intent.putExtra("title", Model.push_title);
            intent.putExtra("text", Model.push_msg);
            Model.launch = "PushNotificationService";
        }

        try {

            String currentDateTimeString = DateFormat.getDateTimeInstance().format(new Date());

            HashMap<String, String> properties = new HashMap<String, String>();
            Model.kiss.record("android.doc.pushnotification_received");
            properties.put("android.doc.push.title", Model.push_title);
            properties.put("android.doc.push.msg", Model.push_msg);
            properties.put("android.doc.push.type", Model.push_type);
            properties.put("android.doc.Receive_Time", currentDateTimeString);
            Model.kiss.set(properties);

        } catch (Exception e) {
            e.printStackTrace();
        }


       */
/* // Open NotificationView.java Activity
        pIntent = PendingIntent.getActivity(getApplicationContext(), 0, intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_ONE_SHOT);
        //Create Notification using NotificationCompat.Builder
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                // Set Icon
                .setSmallIcon(R.mipmap.logo_fordoctors)
                // Set Ticker Message
                .setTicker(Model.push_title)
                // Set Title
                .setContentTitle(Model.push_title)

                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.logo_fordoctors))
                .setDefaults(new NotificationCompat().DEFAULT_SOUND)
                //.setDefaults(Notification.DEFAULT_SOUND)
                // Set Text
                .setContentText(Model.push_msg)

                // Add an Action Button below Notification
                //.addAction(R.mipmap.logo, "Action Button", pIntent)
                // Set PendingIntent into Notification
                .setContentIntent(pIntent)
                // Dismiss Notification
                .setAutoCancel(true);
        // Create Notification Manager
        NotificationManager notificationmanager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        // Build Notification with Notification Manager
        notificationmanager.notify(0, builder.build());*//*



        //================ Initialize ======================---------------
        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        stop_noti_val = sharedpreferences.getString(noti_status, "off");
        noti_sound_val = sharedpreferences.getString(noti_sound, "off");
        //================ Initialize ======================---------------


        System.out.println("Sounds is ON");
        Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        Ringtone r = RingtoneManager.getRingtone(getApplicationContext(), notification);
        r.play();

        //---------------- Normal Notify -----------------------------------------
        pIntent = PendingIntent.getActivity(getApplicationContext(), 0, intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_ONE_SHOT);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.logo_fordoctors)
                .setTicker(Model.push_title)
                .setContentTitle(Model.push_title)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.logo))
                //.setDefaults(new NotificationCompat().DEFAULT_SOUND)
                //.setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                .setContentText(Model.push_msg)
                .setContentIntent(pIntent)
                .setAutoCancel(true);

        NotificationManager notificationmanager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notificationmanager.notify(0, builder.build());
        //---------------- Normal Notify -----------------------------------------



    */
/*    if (stop_noti_val.equals("off")) {
            System.out.println("Noti is ON");


            if (noti_sound_val.equals("on")) {
                System.out.println("Sounds is ON");
                Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                Ringtone r = RingtoneManager.getRingtone(getApplicationContext(), notification);
                r.play();
            } else {
                ((Vibrator) getSystemService(VIBRATOR_SERVICE)).vibrate(1000);
                System.out.println("Sounds is OFF");
            }


            //---------------- Normal Notify -----------------------------------------
            pIntent = PendingIntent.getActivity(getApplicationContext(), 0, intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_ONE_SHOT);
            NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                    .setSmallIcon(R.mipmap.logo_fordoctors)
                    .setTicker(Model.push_title)
                    .setContentTitle(Model.push_title)
                    .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.logo))
                    //.setDefaults(new NotificationCompat().DEFAULT_SOUND)
                    //.setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                    .setContentText(Model.push_msg)
                    .setContentIntent(pIntent)
                    .setAutoCancel(true);


            NotificationManager notificationmanager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            notificationmanager.notify(0, builder.build());
            //---------------- Normal Notify -----------------------------------------
        } else {
            System.out.println("Noti is OFF");
        }*//*

    }


}
*/
