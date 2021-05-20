package com.orane.docassist;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.flurry.android.FlurryAgent;

import com.google.firebase.analytics.FirebaseAnalytics;

import com.orane.docassist.Model.Model;
import com.orane.docassist.Network.JSONParser;
import com.orane.docassist.Network.ShareIntent;
import com.orane.docassist.R;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import me.drakeet.materialdialog.MaterialDialog;

public class CommonActivity extends AppCompatActivity {

    LinearLayout report_layout, support_layout, contactus_layout, aboutus_layout, settings_layout, aboutApp_layout, terms_layout;
    LinearLayout terms, secret_layout, check_update;
    TextView tv_appver, tv_app_rel, tvaddress_us;
    Button btn_save;
    EditText edt_id;
    ImageView logo;
    String type_text;
    TextView mTitle;

    ImageView img, facebook, twitter, youtube, goolglep, pinterest;
    TextView phno;

    Button btn_submit;
    EditText edt_feedback;
    public String feedback_val, report_response;
    JSONObject json_feedback, json_response_obj;

    LinearLayout policy_layout, reportissue_layout, rate_layout, share_layout, aredoctor_layout, pv_consult_layout, signout_layout;
    Switch switch_notisound, switch_stopnoti;
    TextView tvaddress, tvaddress2, tvcallcenter, tvemail;
    public String noti_sound_val, stop_noti_val;

    SharedPreferences sharedpreferences;
    public static final String noti_status = "noti_status_key";
    public static final String noti_sound = "noti_sound_key";
    public static final String MyPREFERENCES = "MyPrefs";
    public static final String Login_Status = "Login_Status_key";

    TableRow table_mailid;

    Typeface font_reg, font_bold;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.common_activity);

        FlurryAgent.onPageView();

        //------------ Object Creations -------------------------------
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setTitle("");

            mTitle = (TextView) toolbar.findViewById(R.id.toolbar_title);
            Typeface khandBold = Typeface.createFromAsset(getApplicationContext().getAssets(), Model.font_name_bold);
            mTitle.setTypeface(khandBold);
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.app_color));
        }
        //------------ Object Creations -------------------------------

        try {

            Intent intent = getIntent();
            type_text = intent.getStringExtra("type");
            System.out.println("type_text---------" + type_text);

        } catch (Exception e) {
            e.printStackTrace();
        }

        table_mailid = (TableRow) findViewById(R.id.table_mailid);
        aboutApp_layout = (LinearLayout) findViewById(R.id.aboutApp_layout);
        report_layout = (LinearLayout) findViewById(R.id.report_layout);
        settings_layout = (LinearLayout) findViewById(R.id.settings_layout);
        contactus_layout = (LinearLayout) findViewById(R.id.contactus_layout);
        support_layout = (LinearLayout) findViewById(R.id.support_layout);

        //--------- About the Activity ---------------------------
        check_update = (LinearLayout) findViewById(R.id.check_update);
        logo = (ImageView) findViewById(R.id.logo);
        edt_id = (EditText) findViewById(R.id.edt_id);
        btn_save = (Button) findViewById(R.id.btn_save);
        tv_appver = (TextView) findViewById(R.id.tv_appver);
        tv_app_rel = (TextView) findViewById(R.id.tv_app_rel);
        secret_layout = (LinearLayout) findViewById(R.id.secret_layout);
        terms = (LinearLayout) findViewById(R.id.terms);

        tv_appver.setText("App ver: " + (Model.app_ver));
        tv_app_rel.setText("Released on: " + (Model.app_rel));

        edt_feedback = (EditText) findViewById(R.id.edt_feedback);
        btn_submit = (Button) findViewById(R.id.btn_submit);

        secret_layout.setVisibility(View.GONE);

        //--------------------------------------------------------------------------
        logo.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                secret_layout.setVisibility(View.VISIBLE);
                return false;
            }
        });

        table_mailid.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                try {

                    Intent email = new Intent(Intent.ACTION_SEND);
                    email.putExtra(Intent.EXTRA_EMAIL, "icliniq@icliniq.com");
                    email.putExtra(Intent.EXTRA_SUBJECT, "Need Support");
                    email.putExtra(Intent.EXTRA_TEXT, "I would you like to chat with you");
                    email.setType("message/rfc822");

                    startActivity(Intent.createChooser(email, "Choose an Email client :"));

                    //------------ Google firebase Analitics--------------------
                    Model.mFirebaseAnalytics = FirebaseAnalytics.getInstance(getApplicationContext());
                    Bundle params = new Bundle();
                    params.putString("user_id", Model.id);
                    params.putString("name", Model.name);
                    Model.mFirebaseAnalytics.logEvent("Whatsapp_support_chat", params);
                    //------------ Google firebase Analitics--------------------


                } catch (Exception e) {
                    e.printStackTrace();
                }

                return false;
            }
        });

        check_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

/*              String response = SendNetworkUpdateAppRequest();
                if(response.equals("YES")) //n Start Intent to download the app user has to manually install it by clicking on the notification
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("URL TO LATEST APK"))); */
            }
        });

        terms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    String url = Model.BASE_URL + "p/terms?nolayout=1";
                    Intent i = new Intent(Intent.ACTION_VIEW);
                    i.setData(Uri.parse(url));
                    startActivity(i);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Model.id = edt_id.getText().toString();
                    Toast.makeText(getApplicationContext(), "Saved", Toast.LENGTH_LONG).show();
                    secret_layout.setVisibility(View.GONE);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        //--------- About the Activity ---------------------------


        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                feedback_val = edt_feedback.getText().toString();

                if (feedback_val.equals("")) {
                    edt_feedback.setError("Please enter your valuable feedback");

                } else {

                    try {

                        json_feedback = new JSONObject();
                        json_feedback.put("user_id", (Model.id));
                        json_feedback.put("text", feedback_val);

                        System.out.println("json_feedback---" + json_feedback.toString());

                        new JSON_Feedback().execute(json_feedback);

                        //say_success();

                        //------------ Google firebase Analitics--------------------
                        Model.mFirebaseAnalytics = FirebaseAnalytics.getInstance(getApplicationContext());
                        Bundle params = new Bundle();
                        params.putString("User", Model.id + "/" + Model.name);
                        params.putString("Details", json_feedback.toString());
                        Model.mFirebaseAnalytics.logEvent("Feedback", params);
                        //------------ Google firebase Analitics--------------------

                        //----------- Flurry -------------------------------------------------
                        Map<String, String> articleParams = new HashMap<String, String>();
                        articleParams.put("android.doc.Report_Issue_text:", feedback_val);
                        FlurryAgent.logEvent("android.doc.Report_Issue", articleParams);
                        //----------- Flurry -------------------------------------------------

                    } catch (Exception e2) {
                        e2.printStackTrace();
                    }
                }
            }
        });
        //---------- Feedback Layout ----------------------------------------------


        //-------------- Settings Layout -------------------------------------
        signout_layout = (LinearLayout) findViewById(R.id.signout_layout);
        terms_layout = (LinearLayout) findViewById(R.id.terms_layout);
        policy_layout = (LinearLayout) findViewById(R.id.policy_layout);
        reportissue_layout = (LinearLayout) findViewById(R.id.reportissue_layout);
        rate_layout = (LinearLayout) findViewById(R.id.rate_layout);
        share_layout = (LinearLayout) findViewById(R.id.share_layout);
        aredoctor_layout = (LinearLayout) findViewById(R.id.aredoctor_layout);
        pv_consult_layout = (LinearLayout) findViewById(R.id.pv_consult_layout);
        switch_notisound = (Switch) findViewById(R.id.switch_notisound);
        switch_stopnoti = (Switch) findViewById(R.id.switch_stopnoti);

        font_reg = Typeface.createFromAsset(getApplicationContext().getAssets(), Model.font_name);
        font_bold = Typeface.createFromAsset(getApplicationContext().getAssets(), Model.font_name_bold);

        ((TextView) findViewById(R.id.tv_share_tit)).setTypeface(font_bold);
        ((TextView) findViewById(R.id.tv_rate_app)).setTypeface(font_reg);
        ((TextView) findViewById(R.id.tv_share_friends)).setTypeface(font_reg);
        ((TextView) findViewById(R.id.tv_app_sett)).setTypeface(font_bold);
        ((TextView) findViewById(R.id.tv_noti_sound)).setTypeface(font_reg);
        ((TextView) findViewById(R.id.tv_noti_stat)).setTypeface(font_reg);
        ((TextView) findViewById(R.id.tv_about)).setTypeface(font_bold);
        ((TextView) findViewById(R.id.tv_terms)).setTypeface(font_reg);
        ((TextView) findViewById(R.id.tv_privatepolicy)).setTypeface(font_reg);
        ((TextView) findViewById(R.id.tv_reportissue)).setTypeface(font_reg);
        ((TextView) findViewById(R.id.tv_rupat)).setTypeface(font_reg);
        ((TextView) findViewById(R.id.tv_signout)).setTypeface(font_bold);

        //================ Initialize ======================---------------
        sharedpreferences = getApplicationContext().getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        stop_noti_val = sharedpreferences.getString(noti_status, "off");
        noti_sound_val = sharedpreferences.getString(noti_sound, "on");

        switch_stopnoti.setChecked(stop_noti_val.equals("on"));

        switch_notisound.setChecked(noti_sound_val.equals("on"));
        //================ Initialize ======================

        switch_notisound.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                try {

                    if (b) noti_sound_val = "on";
                    else noti_sound_val = "off";

                    //===============Apply Noti Settings Values=====================================
                    SharedPreferences.Editor editor = sharedpreferences.edit();
                    editor.putString(noti_sound, noti_sound_val);
                    editor.apply();
                    //===============Apply Noti Settings Values=================================

                    System.out.println("noti_sound_val-------" + noti_sound_val);

                } catch (Exception ee) {
                    ee.printStackTrace();
                }
            }
        });

        switch_stopnoti.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                try {

                    if (b) stop_noti_val = "on";
                    else stop_noti_val = "off";

                    //===============Apply Noti Settings Values=============================================
                    SharedPreferences.Editor editor = sharedpreferences.edit();
                    editor.putString(noti_status, stop_noti_val);
                    editor.apply();
                    //===============Apply Noti Settings Values=============================================

                    System.out.println("stop_noti_val-------" + stop_noti_val);


                } catch (Exception ee) {
                    ee.printStackTrace();
                }
            }
        });


        terms_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    Intent i = new Intent(getApplicationContext(), WebViewActivity.class);
                    i.putExtra("url", Model.BASE_URL + "/p/terms?nolayout=1");
                    i.putExtra("type", "Terms");
                    startActivity(i);
                    overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        policy_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                try {
                    Intent i = new Intent(getApplicationContext(), WebViewActivity.class);
                    i.putExtra("url", Model.BASE_URL + "/p/privacy?nolayout=1");
                    i.putExtra("type", "Privacy Policy");
                    startActivity(i);
                    overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        reportissue_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(getApplicationContext(), CommonActivity.class);
                i.putExtra("type", "feedback");
                startActivity(i);
                overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);
            }
        });

        rate_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String url = "https://goo.gl/vu2SdY";
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);

            }
        });

        share_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                try {
                    ShareIntent sintent = new ShareIntent();
                    sintent.ShareApp(CommonActivity.this, "MainActivity");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        aredoctor_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                String url = "https://play.google.com/store/apps/details?id=com.orane.icliniq";
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
            }
        });

        pv_consult_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

/*                Intent intent = new Intent(getActivity(), Invite_doctors.class);
                startActivity(intent);*/
            }
        });

        signout_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ask_logout();
            }
        });
        //-------------- Settings Layout -------------------------------------

        //------------------ About us -------------------------------------------
        aboutus_layout = (LinearLayout) findViewById(R.id.aboutus_layout);
        TextView tv_title = (TextView) findViewById(R.id.tv_title);
        TextView tv_desc = (TextView) findViewById(R.id.tv_desc);
        tv_desc.setText(Html.fromHtml(getResources().getString(R.string.large_text)));
        //------------------ About us -------------------------------------------

        //--------------- Contact us ------------------------------------------------
        tvaddress = (TextView) findViewById(R.id.tvaddress);
        tvaddress2 = (TextView) findViewById(R.id.tvaddress2);
        tvaddress_us = (TextView) findViewById(R.id.tvaddress_us);
        tvcallcenter = (TextView) findViewById(R.id.tvcallcenter);
        tvemail = (TextView) findViewById(R.id.tvemail);

        tvemail.setText(Html.fromHtml("<a href=\"mailto:icliniq@icliniq.com\">Email : icliniq@icliniq.com</a>"));
        tvemail.setMovementMethod(LinkMovementMethod.getInstance());

        tvaddress.setText(Html.fromHtml("<br>No.174, Rangasamy Goundan Pudur,<br>" +
                "Chinniyampalayam (P.O),<br>" +
                "Avinashi Road,<br>" +
                "Coimbatore - 641062.<br>" +
                "Tamil Nadu, India.<br>" +
                "Phone: +91-422-2626663"));

        tvaddress2.setText(Html.fromHtml("N0.117/A, 18th Main, 24th Cross,<br>" +
                "Sector 3, HSR Layout,<br>" +
                "Bengaluru - 560 102,<br>" +
                "Karnataka, India"));

        tvaddress_us.setText(Html.fromHtml(
                "icliniq Inc" +
                        "340 S Lemon AVE #8199<br>" +
                        "Walnut, CA, 91789, USA"));


/*        tvcallcenter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("tel://" + tvcallcenter.getText().toString().trim())));
            }
        });*/
        //--------------- Contact us ------------------------------------------------

        //-------------- Support ----------------------------------------------
        img = (ImageView) findViewById(R.id.img);
        facebook = (ImageView) findViewById(R.id.facebook);
        twitter = (ImageView) findViewById(R.id.twitter);
        youtube = (ImageView) findViewById(R.id.youtube);
        goolglep = (ImageView) findViewById(R.id.goolglep);
        pinterest = (ImageView) findViewById(R.id.pinterest);
        phno = (TextView) findViewById(R.id.phno);

        img.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

            }
        });

        facebook.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_VIEW);
                intent.addCategory(Intent.CATEGORY_BROWSABLE);
                intent.setData(Uri.parse("https://www.facebook.com/icliniq"));
                startActivity(intent);
            }
        });
        twitter.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_VIEW);
                intent.addCategory(Intent.CATEGORY_BROWSABLE);
                intent.setData(Uri.parse("https://www.twitter.com/icliniq"));
                startActivity(intent);
            }
        });
        youtube.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_VIEW);
                intent.addCategory(Intent.CATEGORY_BROWSABLE);
                intent.setData(Uri.parse("https://www.youtube.com/icliniq"));
                startActivity(intent);
            }
        });
        goolglep.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_VIEW);
                intent.addCategory(Intent.CATEGORY_BROWSABLE);
                intent.setData(Uri.parse("https://plus.google.com/110128099624226749397"));
                startActivity(intent);
            }
        });
        pinterest.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_VIEW);
                intent.addCategory(Intent.CATEGORY_BROWSABLE);
                intent.setData(Uri.parse("https://www.pinterest.com/icliniq"));
                startActivity(intent);
            }
        });
/*        phno.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent callIntent = new Intent(Intent.ACTION_DIAL);
                callIntent.setData(Uri.parse("tel:+" + phno.getText().toString().trim()));
                startActivity(callIntent);
            }
        });*/


        //-------------- Support ----------------------------------------------
        if (type_text.equals("about_app")) {

            aboutApp_layout.setVisibility(View.VISIBLE);
            report_layout.setVisibility(View.GONE);
            settings_layout.setVisibility(View.GONE);
            aboutus_layout.setVisibility(View.GONE);
            contactus_layout.setVisibility(View.GONE);
            support_layout.setVisibility(View.GONE);

            mTitle.setText("About the App");

        } else if (type_text.equals("feedback")) {
            aboutApp_layout.setVisibility(View.GONE);
            report_layout.setVisibility(View.VISIBLE);
            settings_layout.setVisibility(View.GONE);
            aboutus_layout.setVisibility(View.GONE);
            contactus_layout.setVisibility(View.GONE);
            support_layout.setVisibility(View.GONE);

            mTitle.setText("Feedback");
        } else if (type_text.equals("settings")) {
            aboutApp_layout.setVisibility(View.GONE);
            report_layout.setVisibility(View.GONE);
            settings_layout.setVisibility(View.VISIBLE);
            aboutus_layout.setVisibility(View.GONE);
            contactus_layout.setVisibility(View.GONE);
            support_layout.setVisibility(View.GONE);

            mTitle.setText("Settings");
        } else if (type_text.equals("aboutus")) {
            aboutApp_layout.setVisibility(View.GONE);
            report_layout.setVisibility(View.GONE);
            settings_layout.setVisibility(View.GONE);
            aboutus_layout.setVisibility(View.VISIBLE);
            contactus_layout.setVisibility(View.GONE);
            support_layout.setVisibility(View.GONE);

            mTitle.setText("About us");
        } else if (type_text.equals("contactus")) {
            aboutApp_layout.setVisibility(View.GONE);
            report_layout.setVisibility(View.GONE);
            settings_layout.setVisibility(View.GONE);
            aboutus_layout.setVisibility(View.GONE);
            contactus_layout.setVisibility(View.VISIBLE);
            support_layout.setVisibility(View.GONE);

            mTitle.setText("Contact us");
        } else if (type_text.equals("support")) {
            aboutApp_layout.setVisibility(View.GONE);
            report_layout.setVisibility(View.GONE);
            settings_layout.setVisibility(View.GONE);
            aboutus_layout.setVisibility(View.GONE);
            contactus_layout.setVisibility(View.GONE);
            support_layout.setVisibility(View.VISIBLE);

            mTitle.setText("Customer Support");
        }


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == android.R.id.home) {
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private class JSON_Feedback extends AsyncTask<JSONObject, Void, Boolean> {

        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(CommonActivity.this);
            dialog.setMessage("Please wait..");
            dialog.show();
            dialog.setCancelable(false);
        }

        @Override
        protected Boolean doInBackground(JSONObject... urls) {
            try {
                JSONParser jParser = new JSONParser();
                json_response_obj = jParser.JSON_POST(urls[0], "feedback");

                System.out.println("Feedback URL---------------" + urls[0]);
                System.out.println("json_response_obj-----------" + json_response_obj.toString());

                return true;
            } catch (Exception e) {
                e.printStackTrace();
            }

            return false;
        }

        protected void onPostExecute(Boolean result) {

            try {

                report_response = json_response_obj.getString("status");
                System.out.println("report_response--------------" + report_response);

                if (report_response.equals("1")) {
                    say_success();
                } else {
                    Toast.makeText(getApplicationContext(), report_response, Toast.LENGTH_LONG).show();
                }

                dialog.cancel();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void say_success() {

        try {

            final MaterialDialog alert = new MaterialDialog(CommonActivity.this);
            alert.setTitle("Thank you.!");
            alert.setMessage("Would you like to post your review on play store. This will help and motivate us a lot");
            alert.setCanceledOnTouchOutside(false);
            alert.setPositiveButton("Sure", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    alert.dismiss();
                    finish();

                    try {
                        final String appPackageName = getPackageName();
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            });
            alert.setNegativeButton("No, thanks", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    alert.dismiss();
                    finish();
                }
            });
            alert.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void ask_logout() {

        try {
            final MaterialDialog alert = new MaterialDialog(CommonActivity.this);
            alert.setTitle("Logout.!");
            alert.setMessage("Are you sure you want to logout?");
            alert.setCanceledOnTouchOutside(false);
            alert.setPositiveButton("Yes", new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    //============================================================
                    SharedPreferences.Editor editor = sharedpreferences.edit();
                    editor.putString(Login_Status, "0");
                    editor.apply();
                    //============================================================
                    finishAffinity();
                    Intent intent = new Intent(CommonActivity.this, LoginActivity.class);
                    startActivity(intent);
                    finish();
                }
            });

            alert.setNegativeButton("No", new View.OnClickListener() {
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


}
