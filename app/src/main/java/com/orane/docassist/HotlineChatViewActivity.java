package com.orane.docassist;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.flurry.android.FlurryAgent;

import com.google.firebase.analytics.FirebaseAnalytics;

import com.orane.docassist.Model.Model;
import com.orane.docassist.Model.MultipartEntity2;
import com.orane.docassist.Network.Detector;
import com.orane.docassist.Network.JSONParser;
import com.orane.docassist.R;
import com.orane.docassist.fileattach_library.DefaultCallback;
import com.orane.docassist.fileattach_library.EasyImage;
import com.squareup.picasso.Picasso;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.InputStream;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;


import de.hdodenhof.circleimageview.CircleImageView;
import me.drakeet.materialdialog.MaterialDialog;
import pl.tajchert.nammu.Nammu;
import pl.tajchert.nammu.PermissionCallback;

public class HotlineChatViewActivity extends AppCompatActivity {

    final Handler handler = new Handler();
    Timer timer;
    TimerTask timerTask;
    Thread thread;
    private Timer myTimer;
    Uri selectedImageUri;
    ProgressBar progressBar;
    Bitmap bitmap;
    CircleImageView doc_photo;
    LinearLayout main_data_layout,ans_extra_layout, quest_extra_layout, layout_file_open, layout_file_down, files_layout, layout_attachfile, ans_layout, myLayout, extra_hw_details,query_layout, send_message_layout;
    public String title_text,fields_text,age_gender_txt,str_drug_dets, has_upload_file_val,enable_prescription_val, has_prescription_val, p_status_val, prescMsg_text, local_url, last_upload_file, attach_qid, attach_status, attach_file_url, attach_filename, attach_id, upload_response, answering_status, selectedfilename, doctor_id, str_response, track_id_val, complaint_more, p_history, c_medications, p_medications, tests;
    public String age_txt,gender_txt,file_direct_url, pb_cause_text, lab_t_text, ddx_text, pdx_text, treatment_plan_text, followup_text, p_tips_text;
    View vi_ans, vi, vi_files;
    public JSONObject jsonon_titem,json_fields,jsonobj_hwextra,json_gender,jsonobj_canisnaswer, jsonobj_files, jsonobj_items, chat_post_jsonobj, json, jsonobj, jsonobj1;
    public JSONArray jsonarray;
    ScrollView scrollview;
    EditText edt_chat_msg;
    View vi_hw_full,vi_hw;
    Button btn_send, btn_ansquery, btn_prescription;
    CircleImageView imageview_poster;
    ImageView file_image, take_photo_image, thumb_img;

    TextView tv_furl, tv_ext, tv_answer_query_id,tv_etitle, tv_attach_url, tv_attach_id, tv_userid, tv_filename, tv_pat_name, tv_pat_place, tv_query, tvt_morecomp, tv_morecomp, tvt_prevhist, tv_prevhist, tvt_curmedi, tv_curmedi, tvt_pastmedi, tv_pastmedi, tvt_labtest, tv_labtest, tv_age,tv_gender,tv_datetime;
    TextView tv_answer, tvt_probcause, tv_probcause, tvt_invdone, tv_invdone, tvt_diffdiag, tv_diffdiag, tvt_probdiag, tv_probdiag, tvt_tratplan, tv_tratplan, tvt_prevmeasure, tv_prevmeasure, tvt_follup, tv_follup, tv_datetimeans;

    public String regards, file_user_id, file_doctype, file_file, file_ext, html_file_str, msg_text, files_text, time_text, class_text, qansby, qcanianswer, current_qid, qitems, follouwupcode, pat_name, pat_id, docurl, Doctor_id, selqid, fcode, chat_msg, prep_inv_id, prep_inv_fee, prep_inv_strfee, feedback_status, docname, answer, answerval, status, created_at, question, ratting_val, feedback_text, query_id_val, cur_answer_id, answerval_id, pass, uname, str_status, unpaid_fee, unpaid_invid, unpaid_json_text, str_follow_fee, ftrack_str_status_val, ftrack_str_status, ftrack_fee, ftrack_str, selquery_id;
    public String selectedPath, filename, files_List;

    SharedPreferences sharedpreferences;
    public static final String MyPREFERENCES = "MyPrefs";
    public static final String Login_Status = "Login_Status_key";
    public static final String sp_km_id = "sp_km_id_key";
    public static final String user_name = "user_name_key";
    public static final String Name = "Name_key";
    public static final String password = "password_key";
    public static final String bcountry = "bcountry_key";
    public static final String isValid = "isValid";
    public static final String id = "id";
    public static final String browser_country = "browser_country";
    public static final String email = "email";
    public static final String fee_q = "fee_q";
    public static final String fee_consult = "fee_consult";
    public static final String fee_q_inr = "fee_q_inr";
    public static final String fee_consult_inr = "fee_consult_inr";
    public static final String currency_symbol = "currency_symbol";
    public static final String currency_label = "currency_label";
    public static final String have_free_credit = "have_free_credit";
    public static final String photo_url = "photo_url";
    public static final String sp_mcode = "sp_mcode_key";
    public static final String sp_mnum = "sp_mnum_key";
    public static final String sp_qid = "sp_qid_key";
    public static final String token = "token_key";

    FrameLayout ad_layout;
    ImageView home_ad, ad_close;
    JSONObject jsonobj_ad;
    RelativeLayout ad_close_layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.hotline_chat_view);

        //Model.id = "437302";

        btn_ansquery = (Button) findViewById(R.id.btn_ansquery);

        ad_layout = (FrameLayout) findViewById(R.id.ad_layout);
        home_ad = (ImageView) findViewById(R.id.home_ad);
        ad_close = (ImageView) findViewById(R.id.ad_close);
        ad_close_layout = (RelativeLayout) findViewById(R.id.ad_close_layout);

        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);

        FlurryAgent.onPageView();
        //------------ Object Creations -------------------------------

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Model.screen_status = "true;";

        //================ Shared Pref ======================
        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        String Log_Status = sharedpreferences.getString(Login_Status, "");
        uname = sharedpreferences.getString(user_name, "");
        docname = sharedpreferences.getString(Name, "");
        Model.name = sharedpreferences.getString(Name, "");
        pass = sharedpreferences.getString(password, "");
        Model.id = sharedpreferences.getString(id, "");
        Model.email = sharedpreferences.getString(email, "");
        Model.browser_country = sharedpreferences.getString(browser_country, "");
        Model.fee_q = sharedpreferences.getString(fee_q, "");
        Model.fee_consult = sharedpreferences.getString(fee_consult, "");
        Model.currency_label = sharedpreferences.getString(currency_label, "");
        Model.currency_symbol = sharedpreferences.getString(currency_symbol, "");
        Model.have_free_credit = sharedpreferences.getString(have_free_credit, "");
        Model.photo_url = sharedpreferences.getString(photo_url, "");
        Model.kmid = sharedpreferences.getString(sp_km_id, "");
        //=======================================================

        //================ Shared Pref ===========================
        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        Model.token = sharedpreferences.getString(token, "");
        System.out.println("Model.token----------------------" + Model.token);
        //================ Shared Pref =======================

        ad_close_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ad_layout.setVisibility(View.GONE);

                //--------------------Ad- Close--------
                String ad_url = Model.BASE_URL + "/sapp/closeAd?user_id=" + Model.id + "&track_id=" + track_id_val + "&token=" + Model.token + "&enc=1";
                System.out.println("ad_url----------" + ad_url);
                new JSON_Close().execute(ad_url);
                //---------------------Ad- Close-------
            }
        });

        btn_prescription = (Button) findViewById(R.id.btn_prescription);
        take_photo_image = (ImageView) findViewById(R.id.take_photo_image);
        scrollview = (ScrollView) findViewById(R.id.scrollview);
        myLayout = (LinearLayout) findViewById(R.id.parent_qalayout);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        send_message_layout = (LinearLayout) findViewById(R.id.send_message_layout);
        edt_chat_msg = (EditText) findViewById(R.id.edt_chat_msg);
        btn_send = (Button) findViewById(R.id.btn_send);


        if (new Detector().isTablet(getApplicationContext())) {
            edt_chat_msg.setTextSize(25);
            btn_send.setWidth(45);
            btn_send.setHeight(45);
        }

        try {
            Intent intent = getIntent();
            pat_id = intent.getStringExtra("pat_id");
            pat_name = intent.getStringExtra("pat_name");
            selqid = intent.getStringExtra("selqid");
            follouwupcode = intent.getStringExtra("follouwupcode");

            //----------------------------------------
            if (intent.hasExtra("doctor_id")) {
                doctor_id = intent.getStringExtra("doctor_id");
            } else {
                doctor_id = "0";
            }
            //----------------------------------------

            Model.fcode = "";

            if (getSupportActionBar() != null) {
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                getSupportActionBar().setDisplayShowHomeEnabled(true);
                getSupportActionBar().setTitle(pat_name);
            }

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                getWindow().setStatusBarColor(getResources().getColor(R.color.app_color));
            }

            System.out.println("selecting_pat_id---------" + pat_id);
            System.out.println("pat_name---------" + pat_name);
            System.out.println("selqid---------" + selqid);

        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            Intent intent = getIntent();
            follouwupcode = intent.getStringExtra("follouwupcode");
            System.out.println("follouwupcode---------" + follouwupcode);
        } catch (Exception e) {
            e.printStackTrace();
        }


        btn_ansquery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if (current_qid != null && !current_qid.isEmpty() && !current_qid.equals("null") && !current_qid.equals("")) {
                        if ((Model.id) != null && !(Model.id).isEmpty() && !(Model.id).equals("null") && !(Model.id).equals("")) {
                            try {
                                //------------------------ Can I answer ----------------
                                String url = Model.BASE_URL + "sapp/canIAnswer?user_id=" + (Model.id) + "&qid=" + current_qid + "&token=" + Model.token;
                                System.out.println("canIAnswer url------" + url);
                                new JSON_canianswer().execute(url);
                                //------------------------ Can I answer ----------------

                            } catch (Exception e) {
                                e.printStackTrace();

                                go_back_msg();
                            }
                        } else {
                            go_back_msg();
                        }
                    } else {

                        go_back_msg();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });


        try {

            //----------- Flurry ---------------------------------------------
            Map<String, String> articleParams = new HashMap<String, String>();
            articleParams.put("android.doc.qid:", selqid);
            articleParams.put("android.doc.follouwupcode:", follouwupcode);
            articleParams.put("android.doc.Patient_Id:", pat_id);
            articleParams.put("android.doc.Patient_Name:", pat_name);
            FlurryAgent.logEvent("android.doc.Hotline_ChatView", articleParams);
            //----------- Flurry -------------------------------------------------

        } catch (Exception e) {
            e.printStackTrace();
        }

        System.out.println("Model.id-----------" + Model.id);
        System.out.println("follouwupcode----------" + follouwupcode);


        if ((Model.id) != null && !(Model.id).isEmpty() && !(Model.id).equals("null") && !(Model.id).equals("")) {
            if (follouwupcode != null && !follouwupcode.isEmpty() && !follouwupcode.equals("null") && !follouwupcode.equals("")) {
                fullprocess();
            }
        }

        btn_send.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                try {

                    chat_msg = edt_chat_msg.getText().toString();
                    add_chat_text(chat_msg);

                    edt_chat_msg.setText("");

                    System.out.println("Model.id-----------" + Model.id);
                    System.out.println("follouwupcode----------" + follouwupcode);


                    if (!chat_msg.equals("")) {

                        if ((Model.id) != null && !(Model.id).isEmpty() && !(Model.id).equals("null") && !(Model.id).equals("")) {
                            if (follouwupcode != null && !follouwupcode.isEmpty() && !follouwupcode.equals("null") && !follouwupcode.equals("")) {
                                try {
                                    json = new JSONObject();
                                    json.put("txt", chat_msg);
                                    json.put("fcode", follouwupcode);
                                    json.put("ses_uid", (Model.id));

                                    System.out.println("Chat post json------" + json);

                                    new JSONPostQuery().execute(json);


                                    //------------ Google firebase Analitics--------------------
                                    Model.mFirebaseAnalytics = FirebaseAnalytics.getInstance(getApplicationContext());
                                    Bundle params = new Bundle();
                                    params.putString("User", Model.id + "/" + Model.name);
                                    params.putString("Details", json.toString());
                                    Model.mFirebaseAnalytics.logEvent("Hotline_Chat", params);
                                    //------------ Google firebase Analitics--------------------

                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        btn_prescription.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HotlineChatViewActivity.this, Chat_Prescription_Home.class);
                intent.putExtra("add_type", "new");
                intent.putExtra("q_id", current_qid);
                intent.putExtra("p_type", "chat");
                intent.putExtra("pres_status", p_status_val);
                intent.putExtra("enable_prescription_val", enable_prescription_val);
                intent.putExtra("has_upload_file_val", has_upload_file_val);

                startActivity(intent);
            }
        });
    }


    public void fullprocess() {

        try {

            if (!follouwupcode.equals("")) {
                //----------------------------------------
                String full_url = Model.BASE_URL + "sapp/jsonviewquery4doc?followupcode=" + follouwupcode + "&user_id=" + (Model.id) + "&token=" + Model.token + "&enc=1&isAFiles=1";
                //String full_url = Model.BASE_URL + "sapp/jsonviewquery4doc?followupcode=" + follouwupcode + "&user_id=410443&token=" + Model.token + "&enc=1&isAFiles=1";
                System.out.println("full_url-------------" + full_url);
                Model.chat_cur_fcode = follouwupcode;

                new JSONAsyncTask().execute(full_url);
                //----------------------------------------
            }
        } catch (Exception e) {
            e.printStackTrace();
            force_logout();
        }
    }


    private class JSONPostQuery extends AsyncTask<JSONObject, Void, Boolean> {

        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {

            /*    progressBar.setVisibility(View.VISIBLE);
                  scrollview.setVisibility(View.GONE);      */
        }

        @Override
        protected Boolean doInBackground(JSONObject... urls) {
            try {
                JSONParser jParser = new JSONParser();
                chat_post_jsonobj = jParser.JSON_POST(urls[0], "sendTextAns");

                System.out.println("urls[0]---------------" + urls[0]);
                System.out.println("chat_post_jsonobj---------------" + chat_post_jsonobj.toString());

                return true;

            } catch (Exception e) {
                e.printStackTrace();
            }

            return false;
        }

        protected void onPostExecute(Boolean result) {
            try {

                String status_txt = chat_post_jsonobj.getString("status");
                System.out.println("Chat post Status----- " + status_txt);


                //------------ Google firebase Analitics--------------------
                Model.mFirebaseAnalytics = FirebaseAnalytics.getInstance(getApplicationContext());
                Bundle params = new Bundle();
                params.putString("User", Model.id + "/" + Model.name);
                Model.mFirebaseAnalytics.logEvent("chat_msg_post", params);
                //------------ Google firebase Analitics--------------------

                //----------- Flurry -------------------------------------------------
                Map<String, String> articleParams = new HashMap<String, String>();
                articleParams.put("android.doc.Qid:", selqid);
                articleParams.put("android.doc.Doctor_id:", Doctor_id);
                articleParams.put("android.doc.follouwupcode:", follouwupcode);
                articleParams.put("android.doc.Msg:", chat_msg);
                articleParams.put("android.doc.status:", status_txt);
                FlurryAgent.logEvent("android.doc.Hotline_Chat_PostMsg", articleParams);
                //----------- Flurry -------------------------------------------------

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        //getMenuInflater().inflate(R.menu.chat_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == android.R.id.home) {
            finish();
            return true;
        }

       /* if (id == R.id.nav_attach) {

            Intent intent = new Intent(HotlineChatViewActivity.this, HotlineFileChooserActivity.class);
            intent.putExtra("selqid", selqid);
            startActivity(intent);
            return true;
        }*/

        return super.onOptionsItemSelected(item);
    }


    private class JSONAsyncTask extends AsyncTask<String, Void, Boolean> {

        AlertDialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBar.setVisibility(View.VISIBLE);
            scrollview.setVisibility(View.GONE);
        }

        @Override
        protected Boolean doInBackground(String... urls) {

            try {
                str_response = new JSONParser().getJSONString(urls[0]);
                System.out.println("str_response--------------" + str_response);
                return true;
            } catch (Exception e) {
                e.printStackTrace();
            }

            return false;
        }

        protected void onPostExecute(Boolean result) {

            myLayout.removeAllViews();

            try {

                jsonobj = new JSONObject(str_response);

                if (jsonobj.has("token_status")) {
                    String token_status = jsonobj.getString("token_status");

                    if (token_status.equals("0")) {

                        //============================================================
                        SharedPreferences.Editor editor = sharedpreferences.edit();
                        editor.putString(Login_Status, "0");
                        editor.apply();
                        //============================================================

                        finishAffinity();
                        Intent intent = new Intent(HotlineChatViewActivity.this, LoginActivity.class);
                        startActivity(intent);
                        finish();
                    }
                } else {

                    if (jsonobj.has("askedby_name")) {
                        qansby = jsonobj.getString("askedby_name");
                    } else {
                        qansby = "";
                    }
                    qcanianswer = jsonobj.getString("cananswer");
                    current_qid = jsonobj.getString("qid");
                    qitems = jsonobj.getString("items");
                    fcode = jsonobj.getString("fcode");

                    if (getSupportActionBar() != null) {
                        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                        getSupportActionBar().setDisplayShowHomeEnabled(true);
                        getSupportActionBar().setTitle(qansby);
                    }

                    System.out.println("qansby------" + qansby);
                    System.out.println("qcanianswer------" + qcanianswer);
                    System.out.println("current_qid------" + current_qid);
                    System.out.println("qitems------" + qitems);
                    System.out.println("fcode------" + fcode);


                    if (qitems.length() > 2) {

                        try {
                            JSONArray jarray = jsonobj.getJSONArray("items");
                            System.out.println("Items jarray.length------" + jarray.length());

                            for (int i = 0; i < jarray.length(); i++) {
                                jsonobj_items = jarray.getJSONObject(i);
                                System.out.println("jsonobj_Items------" + jsonobj_items.toString());
                                class_text = jsonobj_items.getString("class");
                                System.out.println("class_text------" + class_text);

                                if (class_text.equals("bubbledRight")) {

                                    vi = getLayoutInflater().inflate(R.layout.chat_view_question, null);
                                    quest_extra_layout = (LinearLayout) vi.findViewById(R.id.quest_extra_layout);

                                    query_layout = (LinearLayout) vi.findViewById(R.id.quest_layout);
                                    extra_hw_details = (LinearLayout) vi.findViewById(R.id.extra_hw_details);

                                    tv_query = (TextView) vi.findViewById(R.id.tv_query);
                                    tv_datetime = (TextView) vi.findViewById(R.id.tv_datetime);
                                    files_layout = (LinearLayout) vi.findViewById(R.id.files_layout);
                                    layout_attachfile = (LinearLayout) vi.findViewById(R.id.layout_attachfile);
                                    tvt_morecomp = (TextView) vi.findViewById(R.id.tvt_morecomp);
                                    tv_morecomp = (TextView) vi.findViewById(R.id.tv_morecomp);
                                    tvt_prevhist = (TextView) vi.findViewById(R.id.tvt_prevhist);
                                    tv_prevhist = (TextView) vi.findViewById(R.id.tv_prevhist);
                                    tvt_curmedi = (TextView) vi.findViewById(R.id.tvt_curmedi);
                                    tv_curmedi = (TextView) vi.findViewById(R.id.tv_curmedi);
                                    tvt_pastmedi = (TextView) vi.findViewById(R.id.tvt_pastmedi);
                                    tv_pastmedi = (TextView) vi.findViewById(R.id.tv_pastmedi);
                                    tvt_labtest = (TextView) vi.findViewById(R.id.tvt_labtest);
                                    tv_labtest = (TextView) vi.findViewById(R.id.tv_labtest);
                                    tv_datetime = (TextView) vi.findViewById(R.id.tv_datetime);
                                    tv_gender = (TextView) vi.findViewById(R.id.tv_gender);

                                    if (new Detector().isTablet(getApplicationContext())) {
                                        tv_query.setTextSize(20);
                                        tvt_morecomp.setTextSize(20);
                                        tv_morecomp.setTextSize(20);
                                        tvt_prevhist.setTextSize(20);
                                        tv_prevhist.setTextSize(20);
                                        tvt_curmedi.setTextSize(20);
                                        tv_curmedi.setTextSize(20);
                                        tvt_pastmedi.setTextSize(20);
                                        tv_pastmedi.setTextSize(20);
                                        tvt_labtest.setTextSize(20);
                                        tv_labtest.setTextSize(20);
                                        tv_datetime.setTextSize(18);
                                    }

                                    Typeface font_reg = Typeface.createFromAsset(getAssets(), Model.font_name);
                                    Typeface font_bold = Typeface.createFromAsset(getAssets(), Model.font_name_bold);

                                    tv_query.setTypeface(font_reg);
                                    tvt_morecomp.setTypeface(font_bold);
                                    tv_morecomp.setTypeface(font_reg);
                                    tvt_prevhist.setTypeface(font_bold);
                                    tv_prevhist.setTypeface(font_reg);
                                    tvt_curmedi.setTypeface(font_bold);
                                    tv_curmedi.setTypeface(font_reg);
                                    tvt_pastmedi.setTypeface(font_bold);
                                    tv_pastmedi.setTypeface(font_reg);
                                    tvt_labtest.setTypeface(font_bold);
                                    tv_labtest.setTypeface(font_reg);
                                    tv_datetime.setTypeface(font_reg);


                                    layout_attachfile.setVisibility(View.GONE);

                                    msg_text = jsonobj_items.getString("msg");
                                    String msg_ext_text = jsonobj_items.getString("msg_ext");
                                    files_text = jsonobj_items.getString("files");
                                    class_text = jsonobj_items.getString("class");
                                    time_text = jsonobj_items.getString("time");
                                    enable_prescription_val = jsonobj_items.getString("enable_prescription");
                                    has_upload_file_val = jsonobj_items.getString("has_upload_file");


                                    //-------------------------- Age Gender ------------------------------------------------
                                    if (jsonobj_items.has("age_gender")) {

                                        age_gender_txt = jsonobj_items.getString("age_gender");

                                        if (age_gender_txt.length() > 2) {
                                            json_gender = new JSONObject(age_gender_txt);

                                            if (json_gender.has("age")) {
                                                age_txt = json_gender.getString("age");
                                            }
                                            if (json_gender.has("gender")) {
                                                gender_txt = json_gender.getString("gender");
                                            }

                                            System.out.println("age_txt------" + age_txt);
                                            System.out.println("gender_txt------" + gender_txt);

                                            if (age_txt != null && !age_txt.isEmpty() && !age_txt.equals("null") && !age_txt.equals("")) {
                                                tv_gender.setText(Html.fromHtml("<b>" + qansby + "</b> ," + age_txt + ", " + gender_txt));
                                            } else {
                                                tv_gender.setVisibility(View.GONE);
                                            }

                                            //-----------------------Extra HW --------------------------------------
                                            if (json_gender.has("extra")) {
                                                //extra_txt = json_gender.getString("extra");

                                                JSONArray jarr = json_gender.getJSONArray("extra");
                                                System.out.println("jarr Length------" + jarr.length());
                                                System.out.println("jarr------" + jarr.toString());

                                                for (int j = 0; j < jarr.length(); j++) {
                                                    jsonobj_hwextra = jarr.getJSONObject(j);
                                                    System.out.println("jsonobj_hwextra------" + j + " ----" + jsonobj_hwextra.toString());


                                                    title_text = jsonobj_hwextra.getString("title");
                                                    fields_text = jsonobj_hwextra.getString("fields");

                                                    //--------------------- Title--------------------------------
                                                    if (new Detector().isTablet(getApplicationContext())) {
                                                        vi_hw_full = getLayoutInflater().inflate(R.layout.tab_query_view_extra_full, null);
                                                    } else {
                                                        vi_hw_full = getLayoutInflater().inflate(R.layout.query_view_extra_full, null);
                                                    }

                                                    tv_etitle = vi_hw_full.findViewById(R.id.tv_etitle);
                                                    main_data_layout = vi_hw_full.findViewById(R.id.main_data_layout);
                                                    tv_etitle.setText(Html.fromHtml("<b>" + title_text + "</b>"));
                                                    System.out.println("title_text-------------------->" + title_text);
                                                    //extra_hw_title.addView(vi_hw_tit);
                                                    //--------------------- Title--------------------------------

                                                    tv_etitle.setText(Html.fromHtml("<b>" + title_text + "</b>"));


                                                    json_fields = new JSONObject(fields_text);

                                                    for (int f = 1; f <= 10; f++) {

                                                        String s = "" + f;
                                                        if (json_fields.has("" + s)) {

                                                            String thread = json_fields.getString("" + s);
                                                            System.out.println("thread-----" + thread);

                                                            jsonon_titem = new JSONObject(thread);
                                                            System.out.println("jsonon_titem------" + jsonon_titem.toString());
                                                            //String jsonon_titem.getString("");

                                                            Iterator<String> iter = jsonon_titem.keys();
                                                            while (iter.hasNext()) {
                                                                String key = iter.next();
                                                                System.out.println("key-----" + key);

                                                                try {
                                                                    Object value = jsonon_titem.get(key);
                                                                    System.out.println("key_values=======" + value.toString());

                                                                    if (new Detector().isTablet(getApplicationContext())) {
                                                                        vi_hw = getLayoutInflater().inflate(R.layout.tab_query_view_extra_details, null);
                                                                    } else {
                                                                        vi_hw = getLayoutInflater().inflate(R.layout.query_view_extra_details, null);
                                                                    }

                                                                    //main_data_layout = (LinearLayout) vi_hw.findViewById(R.id.main_data_layout);
                                                                    //tv_etitle = (TextView) vi_hw.findViewById(R.id.tv_etitle);
                                                                    TextView tv_keytext = vi_hw.findViewById(R.id.tv_keytext);
                                                                    TextView tv_valuetext = vi_hw.findViewById(R.id.tv_valuetext);

                                                                    //tv_keytext.setText(Html.fromHtml("<b>" + key + "</b>"));
                                                                    tv_keytext.setText(key + ": ");
                                                                    tv_valuetext.setText(value.toString());

                                                                    main_data_layout.addView(vi_hw);


                                                                } catch (JSONException e) {
                                                                    System.out.println("Exep----" + e.toString());
                                                                }
                                                            }
                                                        }
                                                    }

                                                    extra_hw_details.addView(vi_hw_full);
                                                }
                                            }
                                            //-----------------------Extra  --------------------------------------
                                        }
                                        else{
                                            tv_gender.setVisibility(View.GONE);
                                        }
                                    }
                                    //-------------------------- Age Gender ---------------------------------------------

                                    System.out.println("msg_text------" + msg_text);
                                    System.out.println("msg_ext_text------" + msg_ext_text);
                                    System.out.println("files_text------" + files_text);
                                    System.out.println("class_text------" + class_text);
                                    System.out.println("time_text------" + time_text);

                                    //msg_text = msg_text.replaceAll("\\\n", "<br/>");
                                    tv_query.setText(Html.fromHtml(msg_text));
                                    tv_datetime.setText(time_text);

                                    //---------------- Show PRescription-----------------------------------
                                    if (enable_prescription_val.equals("1")) {
                                        btn_prescription.setVisibility(View.VISIBLE);
                                    } else {
                                        btn_prescription.setVisibility(View.GONE);
                                    }
                                    //---------------- Show Prescription-----------------------------------


                                    //---------------- Msg Extra ------------------------------------
                                    if ((msg_ext_text.length()) > 5) {

                                        quest_extra_layout.setVisibility(View.VISIBLE);

                                        JSONObject msg_ext_jsonobj = new JSONObject(msg_ext_text);

                                        if (msg_ext_jsonobj.has("complaint_more")) {
                                            complaint_more = msg_ext_jsonobj.getString("complaint_more");
                                        }
                                        if (msg_ext_jsonobj.has("p_history")) {
                                            p_history = msg_ext_jsonobj.getString("p_history");
                                        }
                                        if (msg_ext_jsonobj.has("c_medications")) {
                                            c_medications = msg_ext_jsonobj.getString("c_medications");
                                        }
                                        if (msg_ext_jsonobj.has("p_medications")) {
                                            p_medications = msg_ext_jsonobj.getString("p_medications");
                                        }
                                        if (msg_ext_jsonobj.has("tests")) {
                                            tests = msg_ext_jsonobj.getString("tests");
                                        }

                                        System.out.println("complaint_more--------" + complaint_more);
                                        System.out.println("p_history--------" + p_history);
                                        System.out.println("c_medications--------" + c_medications);
                                        System.out.println("p_medications--------" + p_medications);
                                        System.out.println("tests--------" + tests);
                                    } else {
                                        quest_extra_layout.setVisibility(View.GONE);
                                    }
                                    //---------------- Msg Extra ---------------------------------------


                                    //-------------------- More comp -----------------------------------------------
                                    if (complaint_more != null && !complaint_more.isEmpty() && !complaint_more.equals("")) {
                                        tv_morecomp.setText(Html.fromHtml(complaint_more));
                                        tv_morecomp.setVisibility(View.VISIBLE);
                                        tvt_morecomp.setVisibility(View.VISIBLE);
                                    } else {
                                        tvt_morecomp.setVisibility(View.GONE);
                                        tv_morecomp.setVisibility(View.GONE);
                                    }
                                    //-------------------- More comp -----------------------------------------------
                                    //--------------------Prev Hist -----------------------------------------------
                                    if (p_history != null && !p_history.isEmpty() && !p_history.equals("")) {
                                        tv_prevhist.setVisibility(View.VISIBLE);
                                        tvt_prevhist.setVisibility(View.VISIBLE);
                                        tv_prevhist.setText(Html.fromHtml(p_history));
                                    } else {
                                        tv_prevhist.setVisibility(View.GONE);
                                        tvt_prevhist.setVisibility(View.GONE);
                                    }
                                    //---------------------Prev Hist ------------------------------------------------

                                    //--------------------Cur Medi--------------------------------------------
                                    if (c_medications != null && !c_medications.isEmpty() && !c_medications.equals("")) {
                                        tvt_curmedi.setVisibility(View.VISIBLE);
                                        tv_curmedi.setVisibility(View.VISIBLE);
                                        tv_curmedi.setText(Html.fromHtml(c_medications));
                                    } else {
                                        tv_curmedi.setVisibility(View.GONE);
                                        tvt_curmedi.setVisibility(View.GONE);
                                    }
                                    //----------------------Cur Medi---------------------------------------------------

                                    //--------------------past Medi--------------------------------------------
                                    if (p_medications != null && !p_medications.isEmpty() && !p_medications.equals("")) {
                                        tvt_pastmedi.setVisibility(View.VISIBLE);
                                        tv_pastmedi.setVisibility(View.VISIBLE);
                                        tv_pastmedi.setText(Html.fromHtml(p_medications));
                                    } else {
                                        tv_pastmedi.setVisibility(View.GONE);
                                        tvt_pastmedi.setVisibility(View.GONE);
                                    }
                                    //---------------------past Medi---------------------------------------------------

                                    //--------------------lab test-------------------------------------------
                                    if (tests != null && !tests.isEmpty() && !tests.equals("")) {
                                        tvt_labtest.setVisibility(View.VISIBLE);
                                        tv_labtest.setVisibility(View.VISIBLE);
                                        tv_labtest.setText(Html.fromHtml(tests));
                                    } else {
                                        tv_labtest.setVisibility(View.GONE);
                                        tvt_labtest.setVisibility(View.GONE);
                                    }
                                    //---------------------lab test---------------------------------------------------


                                    //---------------- Files ---------------------------------------
                                    if ((files_text.length()) > 2) {

                                        layout_attachfile.setVisibility(View.VISIBLE);

                                        System.out.println("files_text------" + files_text);
                                        JSONArray jarray_files = jsonobj_items.getJSONArray("files");
                                        System.out.println("jsonobj_items------" + jarray_files.toString());
                                        System.out.println("jarray_files.length()------" + jarray_files.length());

                                        //tvattached.setText("Attached " + jarray_files.length() + " File(s)");

                                        for (int j = 0; j < jarray_files.length(); j++) {
                                            jsonobj_files = jarray_files.getJSONObject(j);

                                            System.out.println("jsonobj_files------" + j + " ----" + jsonobj_files.toString());

                                            file_user_id = jsonobj_files.getString("user_id");
                                            file_doctype = jsonobj_files.getString("doctype");
                                            file_file = jsonobj_files.getString("file");
                                            file_ext = jsonobj_files.getString("ext");
                                            file_direct_url = jsonobj_files.getString("url");

                                            System.out.println("file_user_id--------" + file_user_id);
                                            System.out.println("file_doctype--------" + file_doctype);
                                            System.out.println("filename--------" + file_file);
                                            System.out.println("file_ext--------" + file_ext);
                                            System.out.println("file_direct_url--------" + file_direct_url);

                                            if (new Detector().isTablet(getApplicationContext())) {
                                                vi_files = getLayoutInflater().inflate(R.layout.tab_attached_file_list, null);
                                            } else {
                                                vi_files = getLayoutInflater().inflate(R.layout.attached_file_list, null);
                                            }


                                            file_image = (ImageView) vi_files.findViewById(R.id.file_image);
                                            tv_filename = (TextView) vi_files.findViewById(R.id.tv_filename);
                                            tv_ext = (TextView) vi_files.findViewById(R.id.tv_ext);
                                            tv_userid = (TextView) vi_files.findViewById(R.id.tv_userid);
                                            layout_file_open = (LinearLayout) vi_files.findViewById(R.id.layout_file_open);
                                            layout_file_down = (LinearLayout) vi_files.findViewById(R.id.layout_file_down);

                                            tv_filename.setText(Html.fromHtml(file_file));
                                            //tv_ext.setText(file_ext);
                                            tv_ext.setText(file_direct_url);
                                            tv_userid.setText(file_user_id);


                                            layout_file_open.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View view) {

                                                    String file_full__url = "";

                                                    if ("?".contains(file_direct_url)) {
                                                        file_full__url = file_direct_url + "&token=" + Model.token + "&viewer_doctor_id=" + Model.id;
                                                    } else {
                                                        file_full__url = file_direct_url + "?token=" + Model.token + "&viewer_doctor_id=" + Model.id;
                                                    }

                                                    System.out.println("file_full__url-------------" + file_full__url);

                                                    Intent i = new Intent(Intent.ACTION_VIEW);
                                                    i.setData(Uri.parse(file_full__url));
                                                    startActivity(i);



                                                  /*  String url = Model.BASE_URL + "tools/downloadFile/user_id/" + (file_user_id) + "/doctype/query_attachment?file=" + file_file + "&ext=" + file_ext + "&isapp=1";
                                                    System.out.println("File Open url-------------" + url);

                                                    Intent i = new Intent(Intent.ACTION_VIEW);
                                                    i.setData(Uri.parse(url));
                                                    startActivity(i);*/

                                                }
                                            });

                                            layout_file_down.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View view) {

                                                    String file_full__url = "";

                                                    if ("?".contains(file_direct_url)) {
                                                        file_full__url = file_direct_url + "&token=" + Model.token + "&viewer_doctor_id=" + Model.id;
                                                    } else {
                                                        file_full__url = file_direct_url + "?token=" + Model.token + "&viewer_doctor_id=" + Model.id;
                                                    }

                                                    System.out.println("file_full__url-------------" + file_full__url);

                                                    Intent i = new Intent(Intent.ACTION_VIEW);
                                                    i.setData(Uri.parse(file_full__url));
                                                    startActivity(i);


                                                   /* String url = Model.BASE_URL + "tools/downloadFile/user_id/" + (file_user_id) + "/doctype/query_attachment?file=" + file_file + "&ext=" + file_ext + "&isapp=1";
                                                    System.out.println("File Open url-------------" + url);

                                                    Intent i = new Intent(Intent.ACTION_VIEW);
                                                    i.setData(Uri.parse(url));
                                                    startActivity(i);*/

                                                }
                                            });


                                            files_layout.addView(vi_files);
                                        }

                                    } else {
                                        layout_attachfile.setVisibility(View.GONE);
                                    }
                                    //---------------- Files---------------------------------------

                                    myLayout.addView(vi);

                                }

                                //-------------------------- Bubble Left Answer --------------------------------------------------
                                if (class_text.equals("bubbledLeft")) {

                                    vi_ans = getLayoutInflater().inflate(R.layout.chat_view_answer, null);

                                    ans_extra_layout = (LinearLayout) vi_ans.findViewById(R.id.ans_extra_layout);

                                    tv_answer = (TextView) vi_ans.findViewById(R.id.tvanswer);
                                    tvt_probcause = (TextView) vi_ans.findViewById(R.id.tvt_probcause);
                                    tv_probcause = (TextView) vi_ans.findViewById(R.id.tv_probcause);
                                    tvt_invdone = (TextView) vi_ans.findViewById(R.id.tvt_invdone);
                                    tv_invdone = (TextView) vi_ans.findViewById(R.id.tv_invdone);
                                    tvt_diffdiag = (TextView) vi_ans.findViewById(R.id.tvt_diffdiag);
                                    tv_diffdiag = (TextView) vi_ans.findViewById(R.id.tv_diffdiag);
                                    tvt_probdiag = (TextView) vi_ans.findViewById(R.id.tvt_probdiag);
                                    tv_probdiag = (TextView) vi_ans.findViewById(R.id.tv_probdiag);
                                    tvt_tratplan = (TextView) vi_ans.findViewById(R.id.tvt_tratplan);
                                    tv_tratplan = (TextView) vi_ans.findViewById(R.id.tv_tratplan);
                                    tvt_prevmeasure = (TextView) vi_ans.findViewById(R.id.tvt_prevmeasure);
                                    tv_prevmeasure = (TextView) vi_ans.findViewById(R.id.tv_prevmeasure);
                                    tvt_follup = (TextView) vi_ans.findViewById(R.id.tvt_follup);
                                    tv_follup = (TextView) vi_ans.findViewById(R.id.tv_follup);
                                    tv_datetimeans = (TextView) vi_ans.findViewById(R.id.tv_datetime);
                                    tv_answer_query_id = (TextView) vi_ans.findViewById(R.id.tv_answer_query_id);

                                    Button btn_view_prescription = (Button) vi_ans.findViewById(R.id.btn_view_prescription);
                                    Button btn_write_pres_answer = (Button) vi_ans.findViewById(R.id.btn_write_pres_answer);
                                    TextView tv_pres_comment = (TextView) vi_ans.findViewById(R.id.tv_pres_comment);

                                    if (new Detector().isTablet(getApplicationContext())) {
                                        tv_answer.setTextSize(20);
                                        tvt_probcause.setTextSize(20);
                                        tv_probcause.setTextSize(20);
                                        tvt_invdone.setTextSize(20);
                                        tv_invdone.setTextSize(20);
                                        tvt_diffdiag.setTextSize(20);
                                        tv_diffdiag.setTextSize(20);
                                        tvt_probdiag.setTextSize(20);
                                        tv_probdiag.setTextSize(20);
                                        tvt_tratplan.setTextSize(20);
                                        tv_tratplan.setTextSize(20);
                                        tvt_prevmeasure.setTextSize(20);
                                        tv_prevmeasure.setTextSize(20);
                                        tvt_follup.setTextSize(20);
                                        tv_follup.setTextSize(20);
                                        tv_datetimeans.setTextSize(18);

                                    }

                                    Typeface font_reg = Typeface.createFromAsset(getAssets(), Model.font_name);
                                    Typeface font_bold = Typeface.createFromAsset(getAssets(), Model.font_name_bold);

                                    tv_answer.setTypeface(font_reg);
                                    tvt_probcause.setTypeface(font_bold);

                                    tvt_invdone.setTypeface(font_bold);
                                    tv_invdone.setTypeface(font_reg);
                                    tvt_diffdiag.setTypeface(font_bold);
                                    tv_diffdiag.setTypeface(font_reg);
                                    tvt_probdiag.setTypeface(font_bold);
                                    tv_probdiag.setTypeface(font_reg);
                                    tvt_tratplan.setTypeface(font_bold);
                                    tv_tratplan.setTypeface(font_reg);
                                    tvt_prevmeasure.setTypeface(font_bold);
                                    tv_prevmeasure.setTypeface(font_reg);
                                    tvt_follup.setTypeface(font_bold);
                                    tv_follup.setTypeface(font_reg);
                                    tv_datetimeans.setTypeface(font_reg);


                                    String q_id_val = jsonobj_items.getString("q_id");
                                    msg_text = jsonobj_items.getString("msg");
                                    String msg_ext_text = jsonobj_items.getString("msg_ext");
                                    class_text = jsonobj_items.getString("class");
                                    regards = jsonobj_items.getString("regards");
                                    time_text = jsonobj_items.getString("time");

                                    tv_answer_query_id.setText(q_id_val);

                                    //---------------------------------------------
                                    if (jsonobj_items.has("has_prescription")) {
                                        has_prescription_val = jsonobj_items.getString("has_prescription");
                                        System.out.println("has_prescription_val----------" + has_prescription_val);

                                        if ((jsonobj_items.getString("has_prescription")).equals("1")) {
                                            btn_view_prescription.setVisibility(View.VISIBLE);
                                        } else {
                                            btn_view_prescription.setVisibility(View.GONE);
                                        }
                                    } else {
                                        btn_view_prescription.setVisibility(View.GONE);
                                    }
                                    //----------------------------------------------

                                    //-------------------------------------------------------------------------------
                                    if (jsonobj_items.has("p_status")) {
                                        p_status_val = jsonobj_items.getString("p_status");
                                    } else {
                                        p_status_val = "";
                                    }

                                    if (jsonobj_items.has("prescMsg")) {
                                        prescMsg_text = jsonobj_items.getString("prescMsg");
                                    } else {
                                        prescMsg_text = "";
                                    }

                                    if (p_status_val.equals("pending_review")) {
                                        btn_write_pres_answer.setVisibility(View.GONE);
                                        tv_pres_comment.setVisibility(View.VISIBLE);
                                        tv_pres_comment.setText(Html.fromHtml(prescMsg_text));

                                    } else if (p_status_val.equals("rejected")) {
                                        btn_write_pres_answer.setVisibility(View.VISIBLE);
                                        btn_write_pres_answer.setText("Edit Prescription");
                                        tv_pres_comment.setVisibility(View.VISIBLE);
                                        tv_pres_comment.setText(Html.fromHtml(prescMsg_text));
                                    } else {
                                        btn_write_pres_answer.setVisibility(View.GONE);
                                        tv_pres_comment.setVisibility(View.GONE);
                                    }
                                    //-------------------------------------------------------------------------------

                                    System.out.println("p_status_val----------" + p_status_val);
                                    System.out.println("prescMsg_text----------" + prescMsg_text);
                                    System.out.println("has_prescription_val----------" + has_prescription_val);


                                    btn_write_pres_answer.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {

                                            View parent = (View) v.getParent();
                                            TextView tvqid = (TextView) parent.findViewById(R.id.tv_answer_query_id);
                                            String qid_val = tvqid.getText().toString();

                                            System.out.println("qid_val---------" + qid_val);

                                            Intent intent = new Intent(HotlineChatViewActivity.this, Prescription_home.class);
                                            intent.putExtra("add_type", "edit");
                                            intent.putExtra("cur_qid", q_id_val);
                                            intent.putExtra("p_type", "chat");
                                            startActivity(intent);

                                        }
                                    });


                                    tv_answer.setText(Html.fromHtml(msg_text));
                                    tv_datetimeans.setText(time_text);

                                    System.out.println("msg_text------" + msg_text);
                                    System.out.println("msg_ext_text------" + msg_ext_text);
                                    System.out.println("class_text------" + class_text);
                                    System.out.println("regards------" + regards);
                                    System.out.println("time_text------" + time_text);

                                    //---------------- Msg Extra ---------------------------------------
                                    if ((msg_ext_text.length()) > 4) {

                                        ans_extra_layout.setVisibility(View.VISIBLE);

                                        JSONObject msg_ext_jsonobj = new JSONObject(msg_ext_text);

                                        if (msg_ext_jsonobj.has("pb_cause")) {
                                            pb_cause_text = msg_ext_jsonobj.getString("pb_cause");
                                        }
                                        if (msg_ext_jsonobj.has("lab_t")) {
                                            lab_t_text = msg_ext_jsonobj.getString("lab_t");
                                        }
                                        if (msg_ext_jsonobj.has("ddx")) {
                                            ddx_text = msg_ext_jsonobj.getString("ddx");
                                        }
                                        if (msg_ext_jsonobj.has("pdx")) {
                                            pdx_text = msg_ext_jsonobj.getString("pdx");
                                        }
                                        if (msg_ext_jsonobj.has("treatment_plan")) {
                                            treatment_plan_text = msg_ext_jsonobj.getString("treatment_plan");
                                        }
                                        if (msg_ext_jsonobj.has("followup")) {
                                            followup_text = msg_ext_jsonobj.getString("followup");
                                        }
                                        if (msg_ext_jsonobj.has("p_tips")) {
                                            p_tips_text = msg_ext_jsonobj.getString("p_tips");
                                        }
                                    } else {

                                        ans_extra_layout.setVisibility(View.GONE);
                                    }
                                    //---------------- Msg Extra ---------------------------------------
                                    //-------------------- Prob Cause -----------------------------------------------
                                    if (pb_cause_text != null && !pb_cause_text.isEmpty() && !pb_cause_text.equals("")) {
                                        tv_probcause.setText(Html.fromHtml(pb_cause_text));
                                        tv_probcause.setVisibility(View.VISIBLE);
                                        tvt_probcause.setVisibility(View.VISIBLE);
                                    } else {
                                        tv_probcause.setVisibility(View.GONE);
                                        tvt_probcause.setVisibility(View.GONE);
                                    }
                                    //-------------------- Prob cause-----------------------------------------------

                                    //-------------------- Inv Done -----------------------------------------------
                                    if (lab_t_text != null && !lab_t_text.isEmpty() && !lab_t_text.equals("")) {
                                        tv_invdone.setText(Html.fromHtml(lab_t_text));
                                        tvt_invdone.setVisibility(View.VISIBLE);
                                        tvt_invdone.setVisibility(View.VISIBLE);
                                    } else {
                                        tv_invdone.setVisibility(View.GONE);
                                        tvt_invdone.setVisibility(View.GONE);
                                    }
                                    //-------------------- Inv Done---------------------------------------------
                                    //-------------------- Diff Diag-----------------------------------------------
                                    if (ddx_text != null && !ddx_text.isEmpty() && !ddx_text.equals("")) {
                                        tv_diffdiag.setText(Html.fromHtml(ddx_text));
                                        tv_diffdiag.setVisibility(View.VISIBLE);
                                        tvt_diffdiag.setVisibility(View.VISIBLE);
                                    } else {
                                        tv_diffdiag.setVisibility(View.GONE);
                                        tvt_diffdiag.setVisibility(View.GONE);
                                    }
                                    //--------------------Diff Diag---------------------------------------------
                                    //-------------------- Prob Diag-----------------------------------------------
                                    if (pdx_text != null && !pdx_text.isEmpty() && !pdx_text.equals("")) {
                                        tv_probdiag.setText(Html.fromHtml(pdx_text));
                                        tv_probdiag.setVisibility(View.VISIBLE);
                                        tvt_probdiag.setVisibility(View.VISIBLE);
                                    } else {
                                        tv_probdiag.setVisibility(View.GONE);
                                        tvt_probdiag.setVisibility(View.GONE);
                                    }
                                    //--------------------Prob Diag---------------------------------------------
                                    //--------------------Treat Plan----------------------------------------------
                                    if (treatment_plan_text != null && !treatment_plan_text.isEmpty() && !treatment_plan_text.equals("")) {
                                        tv_tratplan.setText(Html.fromHtml(treatment_plan_text));
                                        tv_tratplan.setVisibility(View.VISIBLE);
                                        tvt_tratplan.setVisibility(View.VISIBLE);
                                    } else {
                                        tv_tratplan.setVisibility(View.GONE);
                                        tvt_tratplan.setVisibility(View.GONE);
                                    }
                                    //--------------------Treat Plan---------------------------------------------
                                    //--------------------pREV mEASURE--------------------------------------------
                                    if (followup_text != null && !followup_text.isEmpty() && !followup_text.equals("")) {
                                        tv_prevmeasure.setText(Html.fromHtml(followup_text));
                                        tv_prevmeasure.setVisibility(View.VISIBLE);
                                        tvt_prevmeasure.setVisibility(View.VISIBLE);
                                    } else {
                                        tv_prevmeasure.setVisibility(View.GONE);
                                        tvt_prevmeasure.setVisibility(View.GONE);
                                    }
                                    //--------------------pREV mEASURE--------------------------------------------
                                    //-------------------Follow up-------------------------------------------
                                    if (p_tips_text != null && !p_tips_text.isEmpty() && !p_tips_text.equals("")) {
                                        tv_follup.setText(Html.fromHtml(p_tips_text));
                                        tv_follup.setVisibility(View.VISIBLE);
                                        tvt_follup.setVisibility(View.VISIBLE);
                                    } else {
                                        tv_follup.setVisibility(View.GONE);
                                        tvt_follup.setVisibility(View.GONE);
                                    }
                                    //--------------------Follow up--------------------------------------------

                                    myLayout.addView(vi_ans);
                                }
                                //---------------- Files-------------------------------------------------------------
                            }

                        } catch (Exception e) {
                            System.out.println("Exception---1---" + e.toString());
                            e.printStackTrace();
                        }
                    }
                    //=============================================================================

                    //-----------------------------------------------------
                    if (doctor_id.equals("0")) {
                        btn_ansquery.setVisibility(View.VISIBLE);
                        send_message_layout.setVisibility(View.GONE);
                    } else {
                        btn_ansquery.setVisibility(View.GONE);
                        send_message_layout.setVisibility(View.VISIBLE);
                    }
                    //-----------------------------------------------------

                    //--------------------Ad---------
                    String ad_url = Model.BASE_URL + "/sapp/fetchAd?user_id=" + Model.id + "&browser_country=" + Model.browser_country + "&qid=" + current_qid + "page_src=6&token=" + Model.token;
                    System.out.println("ad_url----------" + ad_url);
                    new JSON_Ad().execute(ad_url);
                    //---------------------Ad--------

                    //-------- Auto Scroll to Bottom------------------
                    scrollview.post(new Runnable() {
                        @Override
                        public void run() {
                            scrollview.fullScroll(ScrollView.FOCUS_DOWN);
                        }
                    });
                    //-------- Auto Scroll to Bottom------------------
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

            progressBar.setVisibility(View.GONE);
            scrollview.setVisibility(View.VISIBLE);
        }
    }

    private class DownloadImageWithURLTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public DownloadImageWithURLTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String pathToFile = urls[0];
            Bitmap bitmap = null;
            try {
                InputStream in = new URL(pathToFile).openStream();
                bitmap = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                //Log.e("Error", e.getMessage());
                //e.printStackTrace();
            }
            return bitmap;
        }

        protected void onPostExecute(Bitmap result) {

            try {

                bmImage.setImageBitmap(result);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private class LoadImage extends AsyncTask<String, String, Bitmap> {

        ProgressDialog pDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(HotlineChatViewActivity.this);
            pDialog.setMessage("Loading Image ....");
            pDialog.show();

        }

        protected Bitmap doInBackground(String... args) {
            try {
                bitmap = BitmapFactory.decodeStream((InputStream) new URL(args[0]).getContent());

            } catch (Exception e) {
                e.printStackTrace();
            }
            return bitmap;
        }

        protected void onPostExecute(Bitmap image) {

            try {
                if (image != null) {
                    doc_photo.setImageBitmap(image);
                    pDialog.dismiss();

                } else {

                    pDialog.dismiss();
                    Toast.makeText(HotlineChatViewActivity.this, "", Toast.LENGTH_SHORT).show();

                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    private CharSequence noTrailingwhiteLines(CharSequence text) {

        while (text.charAt(text.length() - 1) == '\n') {
            text = text.subSequence(0, text.length() - 1);
        }
        return text;
    }

    public static CharSequence trim(CharSequence s, int start, int end) {
        while (start < end && Character.isWhitespace(s.charAt(start))) {
            start++;
        }

        while (end > start && Character.isWhitespace(s.charAt(end - 1))) {
            end--;
        }

        return s.subSequence(start, end);
    }


    public void add_chat_text(String chat_msg_text) {

        try {


            System.out.println("chat_msg_text--inside function----" + chat_msg_text);

            if (!chat_msg_text.equals("")) {

                vi_ans = getLayoutInflater().inflate(R.layout.chat_view_answer, null);

                tv_answer = (TextView) vi_ans.findViewById(R.id.tvanswer);
                tv_datetimeans = (TextView) vi_ans.findViewById(R.id.tv_datetime);


                tvt_probcause = (TextView) vi_ans.findViewById(R.id.tvt_probcause);
                tv_probcause = (TextView) vi_ans.findViewById(R.id.tv_probcause);
                tvt_invdone = (TextView) vi_ans.findViewById(R.id.tvt_invdone);
                tv_invdone = (TextView) vi_ans.findViewById(R.id.tv_invdone);
                tvt_diffdiag = (TextView) vi_ans.findViewById(R.id.tvt_diffdiag);
                tv_diffdiag = (TextView) vi_ans.findViewById(R.id.tv_diffdiag);
                tvt_probdiag = (TextView) vi_ans.findViewById(R.id.tvt_probdiag);
                tv_probdiag = (TextView) vi_ans.findViewById(R.id.tv_probdiag);
                tvt_tratplan = (TextView) vi_ans.findViewById(R.id.tvt_tratplan);
                tv_tratplan = (TextView) vi_ans.findViewById(R.id.tv_tratplan);
                tvt_prevmeasure = (TextView) vi_ans.findViewById(R.id.tvt_prevmeasure);
                tv_prevmeasure = (TextView) vi_ans.findViewById(R.id.tv_prevmeasure);
                tvt_follup = (TextView) vi_ans.findViewById(R.id.tvt_follup);
                tv_follup = (TextView) vi_ans.findViewById(R.id.tv_follup);

                tvt_probcause.setVisibility(View.GONE);
                tv_probcause.setVisibility(View.GONE);
                tvt_invdone.setVisibility(View.GONE);
                tv_invdone.setVisibility(View.GONE);
                tvt_diffdiag.setVisibility(View.GONE);
                tv_diffdiag.setVisibility(View.GONE);
                tvt_probdiag.setVisibility(View.GONE);
                tv_probdiag.setVisibility(View.GONE);
                tvt_tratplan.setVisibility(View.GONE);
                tv_tratplan.setVisibility(View.GONE);
                tvt_prevmeasure.setVisibility(View.GONE);
                tv_prevmeasure.setVisibility(View.GONE);
                tvt_follup.setVisibility(View.GONE);
                tv_follup.setVisibility(View.GONE);

                //chat_msg = chat_msg.replace("\\n", System.getProperty("line.separator"));
                //tv_query.setText(Html.fromHtml(chat_msg.replace("\\n", "\n")));

                tv_answer.setText(Html.fromHtml(chat_msg_text));


                //------------ Google firebase Analitics--------------------
                Model.mFirebaseAnalytics = FirebaseAnalytics.getInstance(getApplicationContext());
                Bundle params = new Bundle();
                params.putString("User", Model.id + "/" + Model.name);
                Model.mFirebaseAnalytics.logEvent("Hotline_send_msg", params);
                //------------ Google firebase Analitics--------------------


                if (new Detector().isTablet(getApplicationContext())) {
                    tv_query.setTextSize(20);
                    tvt_morecomp.setTextSize(20);
                    tv_morecomp.setTextSize(20);
                    tvt_prevhist.setTextSize(20);
                    tv_prevhist.setTextSize(20);
                    tvt_curmedi.setTextSize(20);
                    tv_curmedi.setTextSize(20);
                    tvt_pastmedi.setTextSize(20);
                    tv_pastmedi.setTextSize(20);
                    tvt_labtest.setTextSize(20);
                    tv_labtest.setTextSize(20);
                    tv_datetime.setTextSize(18);
                }

                SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
                String currentTimeStamp = dateFormat.format(new Date());
                tv_datetimeans.setText(currentTimeStamp);

                myLayout.addView(vi_ans);

                //-------- Auto Scroll to Bottom------------------
                scrollview.post(new Runnable() {
                    @Override
                    public void run() {
                        scrollview.fullScroll(ScrollView.FOCUS_DOWN);
                    }
                });
                //-------- Auto Scroll to Bottom------------------
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void add_receive_text(String chat_msg_text) {

        try {
            chat_msg = chat_msg_text;
            System.out.println("Received msg-inside func---" + chat_msg);

            if (!chat_msg.equals("")) {

                vi = getLayoutInflater().inflate(R.layout.chat_view_question, null);

                query_layout = (LinearLayout) vi.findViewById(R.id.quest_layout);
                tv_query = (TextView) vi.findViewById(R.id.tv_query);
                tv_datetime = (TextView) vi.findViewById(R.id.tv_datetime);

                files_layout = (LinearLayout) vi.findViewById(R.id.files_layout);
                layout_attachfile = (LinearLayout) vi.findViewById(R.id.layout_attachfile);
                tvt_morecomp = (TextView) vi.findViewById(R.id.tvt_morecomp);
                tv_morecomp = (TextView) vi.findViewById(R.id.tv_morecomp);
                tvt_prevhist = (TextView) vi.findViewById(R.id.tvt_prevhist);
                tv_prevhist = (TextView) vi.findViewById(R.id.tv_prevhist);
                tvt_curmedi = (TextView) vi.findViewById(R.id.tvt_curmedi);
                tv_curmedi = (TextView) vi.findViewById(R.id.tv_curmedi);
                tvt_pastmedi = (TextView) vi.findViewById(R.id.tvt_pastmedi);
                tv_pastmedi = (TextView) vi.findViewById(R.id.tv_pastmedi);
                tvt_labtest = (TextView) vi.findViewById(R.id.tvt_labtest);
                tv_labtest = (TextView) vi.findViewById(R.id.tv_labtest);

                tv_curmedi.setVisibility(View.GONE);
                tvt_curmedi.setVisibility(View.GONE);
                tv_labtest.setVisibility(View.GONE);
                tvt_labtest.setVisibility(View.GONE);
                tv_pastmedi.setVisibility(View.GONE);
                tvt_pastmedi.setVisibility(View.GONE);
                tv_prevhist.setVisibility(View.GONE);
                tvt_prevhist.setVisibility(View.GONE);
                tvt_morecomp.setVisibility(View.GONE);
                tv_morecomp.setVisibility(View.GONE);
                layout_attachfile.setVisibility(View.GONE);


                if (new Detector().isTablet(getApplicationContext())) {
                    tv_answer.setTextSize(20);
                    tvt_probcause.setTextSize(20);
                    tv_probcause.setTextSize(20);
                    tvt_invdone.setTextSize(20);
                    tv_invdone.setTextSize(20);
                    tvt_diffdiag.setTextSize(20);
                    tv_diffdiag.setTextSize(20);
                    tvt_probdiag.setTextSize(20);
                    tv_probdiag.setTextSize(20);
                    tvt_tratplan.setTextSize(20);
                    tv_tratplan.setTextSize(20);
                    tvt_prevmeasure.setTextSize(20);
                    tv_prevmeasure.setTextSize(20);
                    tvt_follup.setTextSize(20);
                    tv_follup.setTextSize(20);
                    tv_datetimeans.setTextSize(18);

                }
                //------------------------------------------------------------------
                if ((Model.push_url) != null && !(Model.push_url).isEmpty() && !(Model.push_url).equals("null") && !(Model.push_url).equals("nourl") && !(Model.push_url).equals("")) {
                    chat_msg = (Model.push_url) + "&isapp=1";
                    System.out.println("files_text url --------- " + chat_msg);
                    //---------------- Files ---------------------------------------
                    layout_attachfile.setVisibility(View.VISIBLE);
                    files_layout.setVisibility(View.VISIBLE);

                    vi_files = getLayoutInflater().inflate(R.layout.attached_rec_file, null);
                    file_image = (ImageView) vi_files.findViewById(R.id.file_image);
                    tv_filename = (TextView) vi_files.findViewById(R.id.tv_filename);
                    tv_ext = (TextView) vi_files.findViewById(R.id.tv_ext);
                    tv_userid = (TextView) vi_files.findViewById(R.id.tv_userid);
                    tv_furl = (TextView) vi_files.findViewById(R.id.tv_furl);

                    tv_filename.setText(Html.fromHtml(chat_msg));
                    tv_furl.setText(Html.fromHtml(chat_msg));
                    files_layout.addView(vi_files);
                    //---------------- Files---------------------------------------

                    tv_query.setText("Uploaded File: ");

                } else {
                    layout_attachfile.setVisibility(View.GONE);
                    files_layout.setVisibility(View.GONE);

                    tv_query.setText(Html.fromHtml(chat_msg));

                }
                //------------------------------------------------------------------

                SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy HH:mm:ss");
                String currentTimeStamp = dateFormat.format(new Date());
                tv_datetime.setText(currentTimeStamp);


                //------------ Google firebase Analitics--------------------
                Model.mFirebaseAnalytics = FirebaseAnalytics.getInstance(getApplicationContext());
                Bundle params = new Bundle();
                params.putString("User", Model.id + "/" + Model.name);
                params.putString("Details", chat_msg);
                Model.mFirebaseAnalytics.logEvent("Hotline_receive_msg", params);
                //------------ Google firebase Analitics--------------------

                //----------- Flurry -------------------------------------------------
                Map<String, String> articleParams = new HashMap<String, String>();
                articleParams.put("android.doc.Qid:", selqid);
                articleParams.put("android.doc.Doctor_id:", Doctor_id);
                articleParams.put("android.doc.follouwupcode:", follouwupcode);
                articleParams.put("android.doc.Msg:", chat_msg);
                FlurryAgent.logEvent("android.doc.Hotline_Chat_MsgReceive", articleParams);
                //----------- Flurry -------------------------------------------------

                myLayout.addView(vi);

                //--------------- Alert Service -------------------------------------------------------
                if (checkRingerIsOn(getApplicationContext())) {
                    MediaPlayer mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.click_sound);
                    mediaPlayer.start();
                } else {
                    Vibrator v = (Vibrator) getApplicationContext().getSystemService(Context.VIBRATOR_SERVICE);
                    // Vibrate for 500 milliseconds
                    v.vibrate(500);
                }
                //--------------- Alert Service -------------------------------------------------------

                Model.fcode = "";

                System.out.println("Timer fcode-Success-----");
                //-------- Auto Scroll to Bottom------------------
                scrollview.post(new Runnable() {
                    @Override
                    public void run() {
                        scrollview.fullScroll(ScrollView.FOCUS_DOWN);
                    }
                });
                //-------- Auto Scroll to Bottom------------------
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void onClick(View v) {

        try {
            TextView tv_filename = (TextView) v.findViewById(R.id.tv_filename);
            TextView tv_ext = (TextView) v.findViewById(R.id.tv_ext);

            String file_name = tv_filename.getText().toString();
            String file_ext = tv_ext.getText().toString();
            String file_userid = tv_userid.getText().toString();

            System.out.println("str_filename-------" + file_name);
            System.out.println("file_ext-------" + file_ext);

            //String url = Model.BASE_URL + "tools/downloadFile/user_id/" + (file_userid) + "/doctype/query_attachment?file=" + file_name + "&ext=" + file_ext + "&isapp=1";
            String url = file_ext;
            System.out.println("File url-------------" + url);

            //------------ Google firebase Analitics--------------------
            Model.mFirebaseAnalytics = FirebaseAnalytics.getInstance(getApplicationContext());
            Bundle params = new Bundle();
            params.putString("User", Model.id + "/" + Model.name);
            Model.mFirebaseAnalytics.logEvent("Hotline_Attach_File", params);
            //------------ Google firebase Analitics--------------------

            String file_full__url = "";

            if ("?".contains(url)) {
                file_full__url = url + "&token=" + Model.token + "&viewer_doctor_id=" + Model.id;
            } else {
                file_full__url = url + "?token=" + Model.token + "&viewer_doctor_id=" + Model.id;
            }

            System.out.println("file_full__url-------------" + file_full__url);

            Intent i = new Intent(Intent.ACTION_VIEW);
            i.setData(Uri.parse(file_full__url));
            startActivity(i);

          /*  Intent i = new Intent(getApplicationContext(), WebViewActivity.class);
            i.putExtra("url", url);
            i.putExtra("type", "Attachment View");
            startActivity(i);
            overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);*/

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void onClick_file(View v) {

        try {
            TextView tv_filename = (TextView) v.findViewById(R.id.tv_filename);
            String file_name = tv_filename.getText().toString();
            String file_ext = tv_ext.getText().toString();
            String file_userid = tv_userid.getText().toString();
            String file_furl = tv_furl.getText().toString();

            System.out.println("str_filename-------" + file_name);
            System.out.println("file_furll-------------" + file_furl);

            //------------ Google firebase Analitics--------------------
            Model.mFirebaseAnalytics = FirebaseAnalytics.getInstance(getApplicationContext());
            Bundle params = new Bundle();
            params.putString("User", Model.id + "/" + Model.name);
            Model.mFirebaseAnalytics.logEvent("Attach_File", params);
            //------------ Google firebase Analitics--------------------


            String file_full__url = "";

            if ("?".contains(file_furl)) {
                file_full__url = file_furl + "&token=" + Model.token + "&viewer_doctor_id=" + Model.id;
            } else {
                file_full__url = file_furl + "?token=" + Model.token + "&viewer_doctor_id=" + Model.id;
            }

            System.out.println("file_full__url-------------" + file_full__url);

            Intent i = new Intent(Intent.ACTION_VIEW);
            i.setData(Uri.parse(file_full__url));
            startActivity(i);

/*          Intent i = new Intent(Intent.ACTION_VIEW);
            i.setData(Uri.parse(file_furl));
            startActivity(i);*/

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onPause() {
        super.onPause();
        Model.screen_status = "false";

        if (timer != null) {
            timer.cancel();
            timer = null;
        }

        System.out.println("Model.screen_status- onPause------------" + (Model.screen_status));
    }

    @Override
    protected void onResume() {
        super.onResume();
        startTimer();
        Model.screen_status = "true";
        System.out.println("Model.screen_status--onResume-----------" + Model.screen_status);

        fullprocess();
    }

    public void startTimer() {
        timer = new Timer();
        initializeTimerTask();
        timer.schedule(timerTask, 3000, 3000);
    }

    public void stoptimertask(View v) {
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
    }

    public void initializeTimerTask() {

        timerTask = new TimerTask() {
            public void run() {

                handler.post(new Runnable() {
                    public void run() {
                        if ((Model.fcode) != null && !(Model.fcode).isEmpty() && !(Model.fcode).equals("null") && !(Model.fcode).equals("")) {
                            System.out.println("Timer fcode-Received-----" + Model.fcode);
                            add_receive_text("" + (Model.push_msg));
                        } else {
                            System.out.println("Timer fcode------" + Model.fcode);
                        }
                    }
                });
            }
        };
    }


    public static boolean checkRingerIsOn(Context context) {
        AudioManager am = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        return am.getRingerMode() == AudioManager.RINGER_MODE_NORMAL;
    }

    public void force_logout() {

        //---------------- Dialog------------------------------------------------------------------
        final MaterialDialog alert = new MaterialDialog(HotlineChatViewActivity.this);
        alert.setTitle("Oops!");
        alert.setMessage("Something went wrong. You need to logout and login again to proceed.");
        alert.setCanceledOnTouchOutside(false);
        alert.setPositiveButton("Yes,logout!", new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //============================================================
                sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedpreferences.edit();
                editor.putString(Login_Status, "0");
                editor.apply();
                //============================================================

                finishAffinity();
                Intent i = new Intent(HotlineChatViewActivity.this, LoginActivity.class);
                startActivity(i);
                alert.dismiss();
                finish();
            }
        });

        alert.show();
        //-----------------Dialog-----------------------------------------------------------------

    }


    private class JSON_Ad extends AsyncTask<String, Void, Boolean> {

        ProgressDialog pd;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

/*            pd = new ProgressDialog(NewQueryViewActivity.this);
            pd.setMessage("Loading Ad. Please wait...");
            pd.setCancelable(true);
            pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            pd.show();*/

        }

        @Override
        protected Boolean doInBackground(String... urls) {

            try {
                JSONParser jParser = new JSONParser();
                jsonobj_ad = jParser.getJSONFromUrl(urls[0]);
                System.out.println("jsonobj_ad---------- " + jsonobj_ad.toString());

                return true;
            } catch (Exception e) {
                e.printStackTrace();
            }

            return false;
        }

        protected void onPostExecute(Boolean result) {

            try {

                if (jsonobj_ad.has("status")) {
                    String str_status = jsonobj_ad.getString("status");
                    if (str_status.equals("1")) {
                        ad_layout.setVisibility(View.VISIBLE);
                        String ad_img_path = jsonobj_ad.getString("img");
                        track_id_val = jsonobj_ad.getString("track_id");
                        Picasso.with(HotlineChatViewActivity.this).load(ad_img_path).placeholder(R.mipmap.ad_placeholder).error(R.mipmap.ad_placeholder).into(home_ad);
                    } else {
                        ad_layout.setVisibility(View.GONE);
                    }
                } else {
                    ad_layout.setVisibility(View.GONE);
                }

                // pd.dismiss();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    private class JSON_Close extends AsyncTask<String, Void, Boolean> {

        @Override
        protected Boolean doInBackground(String... urls) {

            try {
                JSONParser jParser = new JSONParser();
                jsonobj_ad = jParser.getJSONFromUrl(urls[0]);
                System.out.println("jsonobj_ad---------- " + jsonobj_ad.toString());

                return true;
            } catch (Exception e) {
                e.printStackTrace();
            }

            return false;
        }
    }

    private class JSON_canianswer extends AsyncTask<String, Void, Boolean> {

        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();


            dialog = new ProgressDialog(HotlineChatViewActivity.this);
            dialog.setTitle("Picking this Query., please wait");
            dialog.show();
            dialog.setCancelable(false);
        }

        @Override
        protected Boolean doInBackground(String... urls) {
            try {
                str_response = new JSONParser().getJSONString(urls[0]);
                System.out.println("str_response--------------" + str_response);
                return true;
            } catch (Exception e) {
                e.printStackTrace();
            }
            return false;
        }

        protected void onPostExecute(Boolean result) {

            try {

                jsonobj_canisnaswer = new JSONObject(str_response);
 /*
                //------------ Tracker ------------------------
                MyApp.tracker().send(new HitBuilders.EventBuilder()
                        .setCategory("Query_View")
                        .setAction("Answering_Status=" + str_response)
                        .build());
                //------------ Tracker ------------------------*/

                if (jsonobj_canisnaswer.has("token_status")) {
                    String token_status = jsonobj_canisnaswer.getString("token_status");
                    if (token_status.equals("0")) {

                        //============================================================
                        SharedPreferences.Editor editor = sharedpreferences.edit();
                        editor.putString(Login_Status, "0");
                        editor.apply();
                        //============================================================

                        finishAffinity();
                        Intent intent = new Intent(HotlineChatViewActivity.this, LoginActivity.class);
                        startActivity(intent);
                        finish();
                    }
                } else {

                    answering_status = jsonobj_canisnaswer.getString("status");

                    //----------------------------------------------
                    if ((answering_status).equals("0")) {
                        Toast.makeText(getApplicationContext(), "Sorry! Another doctor has already picked this query.", Toast.LENGTH_LONG).show();
                    } else {
                        btn_ansquery.setVisibility(View.GONE);
                        send_message_layout.setVisibility(View.VISIBLE);
                    }

                    dialog.cancel();
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void go_back_msg() {

        try {
            //---------------- Dialog------------------------------------------------------------------
            final MaterialDialog alert = new MaterialDialog(HotlineChatViewActivity.this);
            alert.setTitle("Oops.!");
            alert.setMessage("Something went wrong; please try again.");
            alert.setCanceledOnTouchOutside(false);
            alert.setPositiveButton("OK", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    alert.dismiss();
                    finish();
                }
            });
                          /*  alert.setNegativeButton("No", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    alert.dismiss();
                                }
                            });*/
            alert.show();
            //-----------------Dialog-----------------------------------------------------------------

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void onclick_viewpres(View v) {

        try {
            View parent = (View) v.getParent();

            TextView cqid = (TextView) parent.findViewById(R.id.tv_answer_query_id);
            String cqid_val = cqid.getText().toString();
            System.out.println("cqid_val---------" + cqid_val);

            String params = Model.BASE_URL + "sapp/previewPrescription?user_id=" + (Model.id) + "&token=" + Model.token + "&os_type=android&item_type=chat&item_id=" + cqid_val;
            System.out.println("Pressed Prescription-----------" + params);
            new list_drugs().execute(params);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private class list_drugs extends AsyncTask<String, Void, Void> {

        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            dialog = new ProgressDialog(HotlineChatViewActivity.this);
            dialog.setMessage("Please wait..");
            dialog.show();
            dialog.setCancelable(false);
        }

        @Override
        protected Void doInBackground(String... params) {
            try {
                str_drug_dets = new JSONParser().getJSONString(params[0]);
                System.out.println("str_drug_dets--------------" + str_drug_dets);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);

            try {

                if (str_drug_dets != null && !str_drug_dets.isEmpty() && !str_drug_dets.equals("null") && !str_drug_dets.equals("")) {

                    JSONObject jobj = new JSONObject(str_drug_dets);

                    String status_text = jobj.getString("status");

                    if (status_text.equals("1")) {

                        if (jobj.has("strHtmlData")) {

                            String strHtmlData = jobj.getString("strHtmlData");
                            String prescPdfUrl_text = jobj.getString("prescPdfUrl");

                            System.out.println("Final_strHtmlData-----" + strHtmlData);

                            try {
                                Intent i = new Intent(getApplicationContext(), Prescription_WebViewActivity.class);
                                i.putExtra("str_html", strHtmlData);
                                i.putExtra("pdf_url", prescPdfUrl_text);
                                startActivity(i);
                                overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }


                        } else {
                            String msg_text = jobj.getString("msg");
                            Toast.makeText(HotlineChatViewActivity.this, msg_text, Toast.LENGTH_SHORT).show();
                        }
                    }
                    dialog.dismiss();
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}
