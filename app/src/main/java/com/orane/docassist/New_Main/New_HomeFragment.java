package com.orane.docassist.New_Main;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.Fragment;
import androidx.appcompat.widget.Toolbar;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.orane.docassist.Consultation_Activity_New;
import com.orane.docassist.LoginActivity;
import com.orane.docassist.Model.Model;
import com.orane.docassist.MyClinicActivity;
import com.orane.docassist.MyPatientActivity;
import com.orane.docassist.MyWalletActivity;
import com.orane.docassist.Network.Detector;
import com.orane.docassist.Network.JSONParser;
import com.orane.docassist.Network.ShareIntent;
import com.orane.docassist.QR_Code_Activity;
import com.orane.docassist.Queries_Activity_New;
import com.orane.docassist.R;
import com.orane.docassist.Video_Description_Activity;
import com.orane.docassist.WebViewActivity;
import com.skyfishjy.library.RippleBackground;

import org.json.JSONObject;

import java.util.List;

import me.drakeet.materialdialog.MaterialDialog;


public class New_HomeFragment extends Fragment {

    public New_HomeFragment() {
    }

    LinearLayout show_layout, drop_down_layout, qases_layout, gl_layout, home_row2, home_row1, cons_layout, feedback_layout, bookings_layout, post_videos_layout, queries_layout;
    View rootView;
    RelativeLayout show_wallet_layout;
    Typeface font_reg, font_bold;
    CardView card_educations;
    ImageView show_down_arrow, post_vid_icon, img_search_logo, img_share_icon;
    String sensappt_home_flag_val, surveyFormUrl_val, str_response, isShowSurvey_val;
    JSONObject json_count;
    int qc = 0;
    int cc = 0;
    int bc = 0;

    SharedPreferences sharedpreferences;
    public static final String MyPREFERENCES = "MyPrefs";
    public static final String Login_Status = "Login_Status_key";
    public static final String sensappt_home_flag = "sensappt_home_flag_key";
    public String qcount, ccount, bcount, uname, pass, track_id_val, url;

    TextView tvqnotify, tv_sub1, tv_feedback, tvcons, tv_wallet_balance;
    JSONObject json, jsonobj;
    ImageView img_feedback;
    TranslateAnimation anim;
    ObjectAnimator objectanimator_close, objectanimator;
    RippleBackground rippleBackground;
    ScrollView scroll_view;

    String pressFlagg = "false";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.new_fragment_home, container, false);
        //================ Shared Pref ======================
        sharedpreferences = getActivity().getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        sensappt_home_flag_val = sharedpreferences.getString(sensappt_home_flag, "");
        //------------ Object Creations -------------------------------

        show_wallet_layout = (RelativeLayout) rootView.findViewById(R.id.show_wallet_layout);
        post_vid_icon = (ImageView) rootView.findViewById(R.id.post_vid_icon);
        show_down_arrow = (ImageView) rootView.findViewById(R.id.show_down_arrow);

        post_videos_layout = (LinearLayout) rootView.findViewById(R.id.post_videos_layout);
        drop_down_layout = (LinearLayout) rootView.findViewById(R.id.drop_down_layout);
        show_layout = (LinearLayout) rootView.findViewById(R.id.show_layout);

        tv_sub1 = (TextView) rootView.findViewById(R.id.tv_sub1);
        scroll_view = (ScrollView) rootView.findViewById(R.id.scroll_view);
        rippleBackground = (RippleBackground) rootView.findViewById(R.id.content);
        tv_feedback = (TextView) rootView.findViewById(R.id.tv_feedback);
        feedback_layout = (LinearLayout) rootView.findViewById(R.id.feedback_layout);
        bookings_layout = (LinearLayout) rootView.findViewById(R.id.bookings_layout);
        queries_layout = (LinearLayout) rootView.findViewById(R.id.queries_layout);
        cons_layout = (LinearLayout) rootView.findViewById(R.id.cons_layout);
        qases_layout = (LinearLayout) rootView.findViewById(R.id.qases_layout);
        home_row1 = (LinearLayout) rootView.findViewById(R.id.home_row1);
        home_row2 = (LinearLayout) rootView.findViewById(R.id.home_row2);
        card_educations = (CardView) rootView.findViewById(R.id.card_educations);
        gl_layout = (LinearLayout) rootView.findViewById(R.id.gl_layout);
        img_share_icon = (ImageView) rootView.findViewById(R.id.img_share_icon);
        img_search_logo = (ImageView) rootView.findViewById(R.id.img_search_logo);
        tvqnotify = (TextView) rootView.findViewById(R.id.tvqnotify);
        tvcons = (TextView) rootView.findViewById(R.id.tvcons);
        tv_wallet_balance = (TextView) rootView.findViewById(R.id.tv_wallet_balance);
        img_feedback = (ImageView) rootView.findViewById(R.id.img_feedback);

        font_reg = Typeface.createFromAsset(getActivity().getAssets(), Model.font_name);
        font_bold = Typeface.createFromAsset(getActivity().getAssets(), Model.font_name_bold);


        if ((Model.id) != null && !(Model.id).isEmpty() && !(Model.id).equals("null") && !(Model.id).equals("")) {
            //--------------------New Query Notify-----------------------------------------------------
            String url2 = Model.BASE_URL + "sapp/getNewCount?user_id=" + (Model.id) + "&token=" + Model.token;
            System.out.println("Counts URL----------" + url2);
            new JSON_Counts().execute(url2);
            //-------------------------------------------------------------------------
        } else {
            Toast.makeText(getActivity(), "Something went wrong. Please try again.", Toast.LENGTH_LONG).show();
            //force_logout();
        }


      /*  rope = YoYo.with(Techniques.Pulse)
                .duration(1000)
                .repeat(YoYo.INFINITE)
                .pivot(YoYo.CENTER_PIVOT, YoYo.CENTER_PIVOT)
                .interpolate(new AccelerateDecelerateInterpolator())
                .withListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {
                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {

                    }

                    @Override
                    public void onAnimationRepeat(Animator animation) {

                    }
                })
                .playOn(post_vid_icon);

*/
        scroll_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                System.out.println("Touching Outside---------------------------");

                if (pressFlagg.equals("true")) {
                    System.out.println("Moving Outside---------------------------");
                    pressFlagg = "false";
                    objectanimator_close.setDuration(500);
                    objectanimator_close.start();
                }
            }
        });


        String aaa = "<small>Post you own</small> Videos";
        tv_sub1.setText(Html.fromHtml(aaa));


    /*    Animation animSlideDown1 = AnimationUtils.loadAnimation(getActivity(), R.anim.bounce);
        animSlideDown1.setStartOffset(100);
        home_row1.startAnimation(animSlideDown1);

        Animation animSlideDown2 = AnimationUtils.loadAnimation(getActivity(), R.anim.slide_right_to_left1);
        animSlideDown2.setStartOffset(300);
        home_row2.startAnimation(animSlideDown2);

        Animation animSlideDown3 = AnimationUtils.loadAnimation(getActivity(), R.anim.bounce3);
        animSlideDown3.setStartOffset(500);
        card_educations.startAnimation(animSlideDown3);




        Animation animSlideDown4 = AnimationUtils.loadAnimation(getActivity(), R.anim.slide_right_to_left2);
        animSlideDown4.setStartOffset(700);
        gl_layout.startAnimation(animSlideDown4);
*/

        Animation animSlideDown1 = AnimationUtils.loadAnimation(getActivity(), R.anim.bounce);
        animSlideDown1.setStartOffset(100);
        home_row1.startAnimation(animSlideDown1);

        Animation animSlideDown2 = AnimationUtils.loadAnimation(getActivity(), R.anim.bounce2);
        animSlideDown2.setStartOffset(300);
        home_row2.startAnimation(animSlideDown2);

        Animation animSlideDown3 = AnimationUtils.loadAnimation(getActivity(), R.anim.bounce3);
        animSlideDown3.setStartOffset(500);
        card_educations.startAnimation(animSlideDown3);

        Animation animSlideDown4 = AnimationUtils.loadAnimation(getActivity(), R.anim.bounce4);
        animSlideDown4.setStartOffset(700);
        gl_layout.startAnimation(animSlideDown4);


/*
        //--- Set Marging Programitically --------------------------------------
        LinearLayout layout = new LinearLayout(getActivity());
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        params.setMargins(100, 0, 0, 0);
        layout.addView(feedback_layout, params);
        //--- Set Marging Programitically --------------------------------------
*/

        objectanimator = ObjectAnimator.ofFloat(feedback_layout, "x", 0);
        objectanimator_close = ObjectAnimator.ofFloat(feedback_layout, "x", -280);


        show_wallet_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //------------ Google firebase Analitics--------------------
                Model.mFirebaseAnalytics = FirebaseAnalytics.getInstance(getActivity());
                Bundle params = new Bundle();
                params.putString("User", Model.id + "/" + Model.name);
                Model.mFirebaseAnalytics.logEvent("Home_MyWallet", params);
                //------------ Google firebase Analitics--------------------

                Intent i = new Intent(getActivity(), MyWalletActivity.class);
                startActivity(i);
            }
        });

        post_videos_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //------------ Google firebase Analitics--------------------
                Model.mFirebaseAnalytics = FirebaseAnalytics.getInstance(getActivity());
                Bundle params = new Bundle();
                params.putString("User", Model.id + "/" + Model.name);
                Model.mFirebaseAnalytics.logEvent("Home_post_videos", params);
                //------------ Google firebase Analitics--------------------

                Intent i = new Intent(getActivity(), Video_Description_Activity.class);
                startActivity(i);
            }
        });

        show_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (drop_down_layout.getVisibility() == View.VISIBLE) {

                    drop_down_layout.setVisibility(View.GONE);
                    show_down_arrow.setImageResource(R.mipmap.down_arrow);

                } else if (drop_down_layout.getVisibility() == View.GONE) {

                    drop_down_layout.setVisibility(View.VISIBLE);
                    show_down_arrow.setImageResource(R.mipmap.up_arrow);
                }
            }
        });


        tv_feedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                pressFlagg = "false";
                objectanimator_close.setDuration(500);
                objectanimator_close.start();
                //img_feedback.setRotation((float) 170.0);


                String url = Model.surveyFormUrl;
                System.out.println("Survey url-------------------" + url);

                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);

                // feedback_layout.setVisibility(View.GONE);
            }
        });


        img_feedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (pressFlagg.equals("false")) {


                   /* Animation animSlideDown3 = AnimationUtils.loadAnimation(getActivity(), R.anim.rotation);
                    animSlideDown3.setStartOffset(300);
                    img_feedback.startAnimation(animSlideDown3);*/

                    pressFlagg = "true";
                    objectanimator.setDuration(500);
                    objectanimator.start();


                } else if (pressFlagg.equals("true")) {

                    pressFlagg = "false";
                    objectanimator_close.setDuration(500);
                    objectanimator_close.start();

                   /* Animation animSlideDown3 = AnimationUtils.loadAnimation(getActivity(), R.anim.rotation);
                    animSlideDown3.setStartOffset(0);
                    img_feedback.startAnimation(animSlideDown3);*/

                }
            }
        });


       /* rope = YoYo.with(Techniques.Pulse)
                .duration(600)
                .repeat(YoYo.INFINITE)
                .pivot(YoYo.CENTER_PIVOT, YoYo.CENTER_PIVOT)
                .interpolate(new AccelerateDecelerateInterpolator())
                .withListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {
                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {
                        Toast.makeText(getActivity(), "cancelled previous animation", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onAnimationRepeat(Animator animation) {

                    }
                })
                .playOn(img_feedback);
*/

        try {

            //--------------- Show Wallet Amount --------------------------
            json = new JSONObject();
            json.put("user_id", (Model.id));
            json.put("browser_country", (Model.browser_country));

            System.out.println("json------:" + json.toString());

            new JSON_WalletBal().execute(json);
            //--------------- Show Wallet Amount --------------------------

        } catch (Exception e) {
            e.printStackTrace();
        }

        queries_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Intent i = new Intent(getActivity(), NewQueriesActivity.class);
                Intent i = new Intent(getActivity(), Queries_Activity_New.class);
                startActivity(i);

                //------------ Google firebase Analitics--------------------
                Model.mFirebaseAnalytics = FirebaseAnalytics.getInstance(getActivity());
                Bundle params = new Bundle();
                params.putString("User", Model.id + "/" + Model.name);
                Model.mFirebaseAnalytics.logEvent("Home_Queries", params);
                //------------ Google firebase Analitics--------------------
            }
        });

        bookings_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(getActivity(), Consultation_Activity_New.class);
                startActivity(i);

                //------------ Google firebase Analitics--------------------
                Model.mFirebaseAnalytics = FirebaseAnalytics.getInstance(getActivity());
                Bundle params = new Bundle();
                params.putString("User", Model.id + "/" + Model.name);
                Model.mFirebaseAnalytics.logEvent("Home_Bookings", params);
                //------------ Google firebase Analitics--------------------
            }
        });

        cons_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(getActivity(), Consultation_Activity_New.class);
                startActivity(i);

                //------------ Google firebase Analitics--------------------
                Model.mFirebaseAnalytics = FirebaseAnalytics.getInstance(getActivity());
                Bundle params = new Bundle();
                params.putString("User", Model.id + "/" + Model.name);
                Model.mFirebaseAnalytics.logEvent("Home_Consultation", params);
                //------------ Google firebase Analitics--------------------

            }
        });

        qases_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(getActivity(), WebViewActivity.class);
                i.putExtra("url", Model.BASE_URL + "/analytics/index?user_id=" + Model.id + "&token=" + Model.token + "&web_view=true");
                i.putExtra("type", "Analytics");
                startActivity(i);

                //------------ Google firebase Analitics--------------------
                Model.mFirebaseAnalytics = FirebaseAnalytics.getInstance(getActivity());
                Bundle params = new Bundle();
                params.putString("User", Model.id + "/" + Model.name);
                Model.mFirebaseAnalytics.logEvent("Home_Cases", params);
                //------------ Google firebase Analitics--------------------

            }
        });

        home_row1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //------------ Google firebase Analitics--------------------
                Model.mFirebaseAnalytics = FirebaseAnalytics.getInstance(getActivity());
                Bundle params = new Bundle();
                params.putString("User", Model.id + "/" + Model.name);
                Model.mFirebaseAnalytics.logEvent("Home_MyClinics", params);
                //------------ Google firebase Analitics--------------------

                Intent i = new Intent(getActivity(), MyClinicActivity.class);
                startActivity(i);

               /* //---------------- Normal Notify -----------------------------------------
                Intent intent = new Intent(getActivity(), AddNotesActivity.class);
                PendingIntent pIntent = PendingIntent.getActivity(getActivity(), 0, intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_ONE_SHOT);
                NotificationCompat.Builder builder = new NotificationCompat.Builder(getActivity())
                        .setSmallIcon(R.mipmap.logo)
                        .setTicker("Sample")
                        .setContentTitle("Sampele Title")
                        .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.logo))
                        //.setDefaults(new NotificationCompat().DEFAULT_SOUND)
                        //.setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                        .setContentText("Sample Text")
                        .setContentIntent(pIntent)
                        .setAutoCancel(true);

                NotificationManager notificationmanager = (NotificationManager)  getActivity().getSystemService(NOTIFICATION_SERVICE);
                notificationmanager.notify(0, builder.build());
                //---------------- Normal Notify -----------------------------------------*/

            }
        });

        home_row2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                //------------ Google firebase Analitics--------------------
                Model.mFirebaseAnalytics = FirebaseAnalytics.getInstance(getActivity());
                Bundle params = new Bundle();
                params.putString("User", Model.id + "/" + Model.name);
                Model.mFirebaseAnalytics.logEvent("Home_MyPatients", params);
                //------------ Google firebase Analitics--------------------

                Intent i = new Intent(getActivity(), MyPatientActivity.class);
                startActivity(i);
            }
        });

        card_educations.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


            }
        });


        gl_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                //------------ Google firebase Analitics--------------------
                Model.mFirebaseAnalytics = FirebaseAnalytics.getInstance(getActivity());
                Bundle params = new Bundle();
                params.putString("User", Model.id + "/" + Model.name);
                Model.mFirebaseAnalytics.logEvent("Home_Guidelines", params);
                //------------ Google firebase Analitics--------------------

                System.out.println("Offer1....1");
                show_guidelines();

            }
        });


        img_share_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //------------ Google firebase Analitics--------------------
                Model.mFirebaseAnalytics = FirebaseAnalytics.getInstance(getActivity());
                Bundle params = new Bundle();
                params.putString("User", Model.id + "/" + Model.name);
                Model.mFirebaseAnalytics.logEvent("Home_AppShare", params);
                //------------ Google firebase Analitics--------------------

                try {
                    ShareIntent sintent = new ShareIntent();
                    sintent.ShareApp(getActivity(), "MainActivity");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        img_search_logo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //------------ Google firebase Analitics--------------------
                Model.mFirebaseAnalytics = FirebaseAnalytics.getInstance(getActivity());
                Bundle params = new Bundle();
                params.putString("User", Model.id + "/" + Model.name);
                Model.mFirebaseAnalytics.logEvent("Home_QR_Code", params);
                //------------ Google firebase Analitics--------------------

                try {
                    Intent intent = new Intent(getActivity(), QR_Code_Activity.class);
                    intent.putExtra("type", "qr_layout");
                    startActivity(intent);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });

        return rootView;
    }

    public void show_guidelines() {

        try {

            final MaterialDialog alert = new MaterialDialog(getActivity());
            View view = LayoutInflater.from(getActivity()).inflate(R.layout.answeringgudlines, null);
            alert.setView(view);
            alert.setTitle("Answering Guidelines");
            alert.setCanceledOnTouchOutside(false);

            Toolbar toolBar = (Toolbar) view.findViewById(R.id.toolBar);
            ImageView imgapp = (ImageView) view.findViewById(R.id.imgapp);
            final TextView tvguidline = (TextView) view.findViewById(R.id.tvguidline);
            final TextView tvappverview = (TextView) view.findViewById(R.id.tvappverview);

            if (new Detector().isTablet(getActivity())) {
                tvguidline.setTextSize(18);
                tvappverview.setTextSize(20);
            }

            toolBar.setVisibility(View.GONE);
            imgapp.setVisibility(View.GONE);

            tvguidline.setText(Html.fromHtml(getString(R.string.guidline)));

            alert.setPositiveButton("OK", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    alert.dismiss();
                }
            });
            alert.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private class JSON_Counts extends AsyncTask<String, Void, Boolean> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected Boolean doInBackground(String... urls) {

            try {

                str_response = new JSONParser().getJSONString(urls[0]);
                System.out.println("str_response--------------" + str_response);

/*              JSONParser jParser = new JSONParser();
                JSONObject jsonoj = jParser.getJSON_URL(urls[0]);

                qcount = jsonoj.getString("new_query_count");
                ccount = jsonoj.getString("new_consult_count");
                bcount = jsonoj.getString("new_booking_count");*/

                return true;
            } catch (Exception e) {
                e.printStackTrace();
            }

            return false;
        }

        protected void onPostExecute(Boolean result) {

            try {

                json_count = new JSONObject(str_response);
                System.out.println("json_count-------" + json_count.toString());

                if (json_count.has("token_status")) {
                    String token_status = json_count.getString("token_status");
                    if (token_status.equals("0")) {
                        //============================================================
                        SharedPreferences.Editor editor = sharedpreferences.edit();
                        editor.putString(Login_Status, "0");
                        editor.apply();
                        //============================================================
                        getActivity().finishAffinity();
                        Intent intent = new Intent(getActivity(), LoginActivity.class);
                        startActivity(intent);
                        getActivity().finish();
                    }
                } else {

                    qcount = json_count.getString("new_query_count");
                    ccount = json_count.getString("new_consult_count");
                    bcount = json_count.getString("new_booking_count");

                    //---------------------------------------------------------
                    if (json_count.has("isShowSurvey")) {
                        isShowSurvey_val = json_count.getString("isShowSurvey");
                        if (isShowSurvey_val.equals("1")) {
                            feedback_layout.setVisibility(View.VISIBLE);
                        } else {
                            feedback_layout.setVisibility(View.GONE);
                        }
                    } else {
                        feedback_layout.setVisibility(View.GONE);
                    }
                    //---------------------------------------------------------

                    //---------------------------------------------------------
                    if (json_count.has("surveyFormUrl")) {
                        surveyFormUrl_val = json_count.getString("surveyFormUrl");
                        Model.surveyFormUrl = surveyFormUrl_val;
                    } else {
                        feedback_layout.setVisibility(View.GONE);
                    }
                    //---------------------------------------------------------

                    if (qcount != null && !qcount.isEmpty() && !qcount.equals("null")) {
                        if (ccount != null && !ccount.isEmpty() && !ccount.equals("null")) {
                            if (bcount != null && !bcount.isEmpty() && !bcount.equals("null")) {

                                if (result) {

                                    System.out.println("qcount-----" + qcount);
                                    System.out.println("ccount-----" + ccount);
                                    System.out.println("bcount-----" + bcount);

                                    qc = Integer.parseInt(qcount);
                                    cc = Integer.parseInt(ccount);
                                    bc = Integer.parseInt(bcount);

                                    setBadge(getActivity(), (qc + cc + bc));

                                    System.out.println("(qc + cc + bc)-----" + (qc + cc + bc));

                                    //------------------------------------------
                                    if (qc > 0) {
                                        tvqnotify.setVisibility(View.VISIBLE);
                                        tvqnotify.setText("" + qc);
                                    } else {
                                        tvqnotify.setVisibility(View.GONE);
                                    }
                                    //-----------------------------------
                                    if ((cc > 0) || (bc > 0)) {
                                        tvcons.setVisibility(View.VISIBLE);
                                        tvcons.setText("" + (cc + bc));
                                    } else {
                                        tvcons.setVisibility(View.GONE);
                                    }


/*                                //-----------------------------------------------
                                if (bc > 0) {
                                    tvnewbookings.setVisibility(View.VISIBLE);
                                    tvnewbookings.setText("" + bc);
                                } else {
                                    tvnewbookings.setVisibility(View.GONE);
                                }
                                //-----------------------------------------------*/

                                }
                            }
                        }
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    public static void setBadge(Context context, int count) {
        String launcherClassName = getLauncherClassName(context);
        if (launcherClassName == null) {
            return;
        }
        Intent intent = new Intent("android.intent.action.BADGE_COUNT_UPDATE");
        intent.putExtra("badge_count", count);
        intent.putExtra("badge_count_package_name", context.getPackageName());
        intent.putExtra("badge_count_class_name", launcherClassName);
        context.sendBroadcast(intent);
    }

    public static String getLauncherClassName(Context context) {

        PackageManager pm = context.getPackageManager();

        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);

        List<ResolveInfo> resolveInfos = pm.queryIntentActivities(intent, 0);
        for (ResolveInfo resolveInfo : resolveInfos) {
            String pkgName = resolveInfo.activityInfo.applicationInfo.packageName;
            if (pkgName.equalsIgnoreCase(context.getPackageName())) {
                return resolveInfo.activityInfo.name;
            }
        }

        return null;
    }

    private class JSON_WalletBal extends AsyncTask<JSONObject, Void, Boolean> {

        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

           /* dialog = new ProgressDialog(getActivity());
            dialog.setMessage("Loading...Please wait.");
            dialog.show();
            dialog.setCancelable(false);*/
        }

        @Override
        protected Boolean doInBackground(JSONObject... urls) {
            try {
                JSONParser jParser = new JSONParser();
                jsonobj = jParser.JSON_POST(urls[0], "mobileWallet");

                return true;
            } catch (Exception e) {
                e.printStackTrace();
            }

            return false;
        }

        protected void onPostExecute(Boolean result) {

            try {
                System.out.println("jsonobj---------" + jsonobj.toString());

                String currency_symbol = jsonobj.getString("currency_symbol");
                String wallet_bal = jsonobj.getString("wallet_balance");
                String payout_bal = jsonobj.getString("payout_sum");

                System.out.println("currency_symbol---" + currency_symbol);
                System.out.println("wallet_bal---" + wallet_bal);
                System.out.println("payout_bal---" + payout_bal);

                tv_wallet_balance.setText(currency_symbol + " " + wallet_bal);


                // dialog.cancel();

            } catch (Exception e) {
                e.printStackTrace();
            }

            // dialog.cancel();

        }
    }


}
