package com.orane.docassist;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.AudioAttributes;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Vibrator;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import com.flurry.android.FlurryAgent;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.orane.docassist.Model.Model;

import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import static com.zipow.videobox.confapp.ConfMgr.getApplicationContext;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = "MyFirebaseMsgService";
    public String open_url, qases_id, mtype, push_qid, push_msg, push_type, push_title, appt_id, meeting_id, doc_photo, doc_name, doc_sp, fb_url, title, icliniq_url, message, img_url;
    public String ticker_text, item_type, ContentTitle, ContentText, SummaryText;
    Bitmap bitmap_images;
    Intent intent;
    PendingIntent pIntent;
    public String noti_sound_val, stop_noti_val;

    Intent i;

    @Override
    public void onNewToken(@NotNull String token) {
        Log.e(TAG, "Refreshed token: " + token);

        System.out.println("New token--------------" + token);
        //sendRegistrationToServer(token);
        Model.device_token = token;
    }
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        System.out.println("Push Noti Msg Received------------------" + remoteMessage.getData().toString());
        System.out.println("Push Noti Msg Received------------------" + remoteMessage.toString());

        if (remoteMessage.getNotification() != null) {
            Log.d(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());
        }

        if (remoteMessage.getData().size() > 0) {
            System.out.println("Message data payload------- " + remoteMessage.getData());
        }


        try {

            Map<String, String> params = remoteMessage.getData();
            JSONObject object = new JSONObject(params);


            String title = object.getString("title");
            String body_text = object.getString("body");

            System.out.println("title-------------" + title);
            System.out.println("body_text-------------" + body_text);

            JSONObject data = new JSONObject(body_text);
            System.out.println("body_data-------------" + data);

            Model.push_msg = data.getString("message");
            Model.push_type = data.getString("msg_type");
            Model.push_title = data.getString("title");
            Model.fcode = "";

            push_title = Model.push_title;


        } catch (Exception e) {
            e.printStackTrace();
        }

        Notification();

/*        try {

            Map<String, String> params = remoteMessage.getData();
            JSONObject object = new JSONObject(params);


            String title = object.getString("title");
            String body_text = object.getString("body");

            System.out.println("title-------------" + title);
            System.out.println("body_text-------------" + body_text);

            JSONObject data = new JSONObject(body_text);
            System.out.println("body_data-------------" + data);

            Model.push_msg = data.getString("message");
            Model.push_type = data.getString("msg_type");
            Model.push_title = data.getString("title");
            Model.fcode = "";


            if ((Model.push_type).equals("22")) {
                Model.followup_code = data.getString("followup_code");
                Model.from_name = data.getString("from_name");
            } else if ((Model.push_type).equals("29")) {

                Model.fcode = data.getString("fcode");
                Model.qid = data.getString("id");

                Model.push_url = data.has("url") ? data.getString("url") : "nourl";
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

                open_url = data.has("open_url") ? data.getString("open_url") : "";

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
        }*/

    }




    public void Notification() {

/*

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
            intent = new Intent(this, Consultation_Activity_New.class);
            intent.putExtra("title", Model.push_title);
            intent.putExtra("text", Model.push_msg);
            Model.launch = "PushNotificationService";
        } else if ((Model.push_type).equals("24")) {
            intent = new Intent(this, Consultation_Activity_New.class);
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
*/


/*
        //================ Initialize ======================---------------
        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        stop_noti_val = sharedpreferences.getString(noti_status, "off");
        noti_sound_val = sharedpreferences.getString(noti_sound, "off");
        //================ Initialize ======================---------------*/


        System.out.println("Sounds is ON");
        Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        Ringtone r = RingtoneManager.getRingtone(getApplicationContext(), notification);
        r.play();
        //---------------- Normal Notify -----------------------------------------


        push_title = "Sampe Tirle";

        intent = new Intent(this, NewQueriesActivity.class);
        intent.putExtra("title", "");
        intent.putExtra("text", "");
        Model.launch = "PushNotificationService";

        NotificationManager mNotificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel("default",
                    "YOUR_CHANNEL_NAME",
                    NotificationManager.IMPORTANCE_DEFAULT);

            Uri soundUri = Uri.parse("android.resource://" + getApplicationContext().getPackageName() + "/" + R.raw.click_sound);

            AudioAttributes audioAttributes = new AudioAttributes.Builder()
                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                    .setUsage(AudioAttributes.USAGE_ALARM)
                    .build();


            channel.setDescription("YOUR_NOTIFICATION_CHANNEL_DISCRIPTION");
            channel.setSound(soundUri, audioAttributes);
            mNotificationManager.createNotificationChannel(channel);

            System.out.println("YOUR_NOTIFICATION_CHANNEL_DISCRIPTION-----------------");
        }


        NotificationCompat.InboxStyle inboxStyle = new NotificationCompat.InboxStyle();

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(MyFirebaseMessagingService.this, "default")
                .setSmallIcon(R.mipmap.logo)
                .setTicker(push_title)
                .setContentTitle(getResources().getString(R.string.app_name))
                .setContentText(push_title)
                //.setStyle(new NotificationCompat.BigPictureStyle().bigPicture(bitmap_images).setSummaryText(Html.fromHtml(push_title)))
                //.setStyle(inboxStyle)
                .setLargeIcon(BitmapFactory.decodeResource(MyFirebaseMessagingService.this.getResources(), R.mipmap.logo))
                //.setSound(alarmSound)
                .setAutoCancel(true);
        //Intent intent = new Intent(MyFirebaseMessagingService.this, AboutAppActivity.class);
        PendingIntent pi = PendingIntent.getActivity(MyFirebaseMessagingService.this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        mBuilder.setContentIntent(pi);
        mNotificationManager.notify((new Random().nextInt(9999 - 1000) + 1000), mBuilder.build());


    }

}
