package com.orane.docassist;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.text.method.LinkMovementMethod;
import android.text.util.Linkify;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.webkit.WebSettings;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import com.flurry.android.FlurryAgent;
import com.github.ksoichiro.android.observablescrollview.ObservableWebView;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.orane.docassist.Model.Model;
import com.orane.docassist.Model.MultipartEntity2;
import com.orane.docassist.Network.Detector;
import com.orane.docassist.Network.JSONParser;
import com.orane.docassist.attachment_view.GridViewActivity;
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

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import me.drakeet.materialdialog.MaterialDialog;
import pl.tajchert.nammu.Nammu;
import pl.tajchert.nammu.PermissionCallback;

public class NewQueryViewActivity extends AppCompatActivity {

    public static final String MyPREFERENCES = "MyPrefs";
    public static final String Login_Status = "Login_Status_key";
    public static final String user_name = "user_name_key";
    public static final String password = "password_key";
    public static final String Name = "Name_key";
    public static final String bcountry = "bcountry_key";
    public static final String photo_url = "photo_url";
    public static final String token = "token_key";

    public String tv_qid_val, str_drug_dets, family_list, followup_txt, current_answer_id, selectedPath, selectedfilename, local_url, last_upload_file, upLoadServerUri, attach_qid, attach_status, attach_file_url, attach_filename, attach_id, upload_response, patient_id, jio_query, track_id_val, str_response, file_full_url, attach_file_text, extension, file_name_only, title_text, fields_text, qtype, query_price_text, pat_from_text, check_enable_ffollowup, answering_status, opt_freefollow, enable_freefollow, prescribe_status, isEnablefFollowup, qansby, qcanianswer, html_file_str, followupcode, qitems, regards, msg_text, msg_ext_text, files_text, class_text, time_text;
    public String has_upload_file_val, has_prescription_val, p_status_val, prescMsg_text, answer_id_val, file_user_id, feedback_text, file_doctype, file_file, file_ext, rating_text, pat_feedback_text, feedback_id_text, reply_text;
    public String serverResponseMessage, contentAsString, answer_files_text, diagnose_prediction_text, age_txt, report_response, arr_feedback_text, gender_txt, age_gender_txt, prescribe_value, enable_prescription_val, lab_t_text, ddx_text, pdx_text, treatment_plan_text, followup_text, p_tips_text;
    public View vi_ans, vi, vi_files;
    public JSONObject json_fields, jsonobj_hwextra, text_jsonobj, draft_json, json, jsonobj_ad;
    public JSONArray jarray_hw;
    public String type, ent_ans, current_qid, answer_txt, allow_answer, params, ans_isvalid, ans_status;
    Typeface noto_reg, noto_bold;
    Switch followup_switch;
    Button btn_notanswer, btn_write_pres_answer;
    View pred_vi;
    String strHtml_text;
    InputStream is = null;
    int serverResponseCode = 0;
    JSONObject jsonobj1, json_response_obj, feedback_json, jsonobj_postans, jsonon_titem, json_gender, jsonobj_canisnaswer, jsonobj, jsonobj_items, jsonobj_files;
    LinearLayout expand_layout, answer_display_layout, query_display_layout, main_data_layout, doctor_reply_section, feedback_section, extra_layout, ans_extra_layout, extra_hwlayout, extra_ans_layout, button_layout, answer_layout, netcheck_layout, nolayout, layout_attachfile, myLayout, ratting_layout, answer_files_layout, answer_layout_attachfile, files_layout;
    RelativeLayout pres_wrie_layout;
    TextView tv_attach_url, tv_pres_comment, tv_attach_id, tv_answer_filename, tv_pred_slno, tv_pred_name, tv_pred_profname, tv_pred_icd, tv_pred_rank, tv_etitle, tv_replytext, tv_valuetext, tv_keytext, tv_title, tv_extra, tv_ext, tv_userid, tv_gender, tvattached, tv_qid, tv_filename, tv_pat_name, tv_pat_place, tv_query, tvt_morecomp, tv_morecomp, tvt_prevhist, tv_prevhist, tvt_curmedi, tv_curmedi, tvt_pastmedi, tv_pastmedi, tvt_labtest, tv_labtest, tv_datetime;
    TextView tv_patfeedback, tvtips1, tvtips2, tvtips3, tv_ext_title, tv_ext_desc, tv_answer, tvt_probcause, tv_probcause, tvt_invdone, tv_invdone, tvt_diffdiag, tv_diffdiag, tvt_probdiag, tv_probdiag, tvt_tratplan, tv_tratplan, tvt_prevmeasure, tv_prevmeasure, tvt_follup, tv_follup, tv_datetimeans;
    ScrollView scrollview;
    StringBuilder total;
    Button btn_follow_submit, btn_write_pres, btn_feedbacksubmit;
    ImageView file_image, imgapp;
    ObservableWebView query_webview, webview_answer;
    ProgressBar progressBar;
    Button btn_reload, btn_ansquery;
    long startTime;
    ImageView thumb_img;
    View vi_hw, vi_hw_tit, vi_hw_full, vi_ans_ext;
    Typeface font_reg, font_bold;
    Map<String, String> extra_ans_map = new HashMap<String, String>();
    Map<String, String> extra_query_map = new HashMap<String, String>();
    View vi_ext;
    Button btn_done, btn_upload_pres;
    EditText edt_answer;
    LinearLayout days_layout, file_list, prediction_inner_layout, prediction_layout, extra_hw_title, extra_hw_details, show_more, ans_more_dets, show_less;
    TextView prescription_title, tvprivate, tvclear, tv_anstit, tv_write_query_id, tv_show;
    EditText prescribe_text, diagnosis, pb_cause, lab_t, ddx, pdx, treatment_plan, p_tips, followup;
    Switch reminder_switch;
    EditText edit_remind_days;
    SharedPreferences sharedpreferences;
    FrameLayout ad_layout;
    ImageView home_ad, ad_close;
    RelativeLayout ad_close_layout;
    RelativeLayout followup_layout;
    EditText edt_followup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.query_new_view_detail);

        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);

        //------------ Object Creations -------------------------------
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setTitle("");

            TextView mTitle = toolbar.findViewById(R.id.toolbar_title);
            Typeface khandBold = Typeface.createFromAsset(getApplicationContext().getAssets(), Model.font_name_bold);
            mTitle.setTypeface(khandBold);
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.app_color));
        }
        //-----------------------------------------------------------------------

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        allow_answer = "no";

        edt_followup = findViewById(R.id.edt_followup);
        btn_upload_pres = findViewById(R.id.btn_upload_pres);
        prescription_title = findViewById(R.id.prescription_title);

        btn_follow_submit = findViewById(R.id.btn_follow_submit);

        pres_wrie_layout = findViewById(R.id.pres_wrie_layout);
        file_list = findViewById(R.id.file_list);
        followup_layout = findViewById(R.id.followup_layout);
        ad_layout = findViewById(R.id.ad_layout);
        home_ad = findViewById(R.id.home_ad);
        ad_close = findViewById(R.id.ad_close);
        ad_close_layout = findViewById(R.id.ad_close_layout);

        tv_write_query_id = findViewById(R.id.tv_write_query_id);

        prescribe_text = findViewById(R.id.prescribe_text);
        edit_remind_days = findViewById(R.id.edit_remind_days);
        reminder_switch = findViewById(R.id.reminder_switch);
        days_layout = findViewById(R.id.days_layout);
        extra_ans_layout = findViewById(R.id.extra_ans_layout);
        btn_notanswer = findViewById(R.id.btn_notanswer);
        followup_switch = findViewById(R.id.followup_switch);
        btn_ansquery = findViewById(R.id.btn_ansquery);
        btn_reload = findViewById(R.id.btn_reload);
        button_layout = findViewById(R.id.button_layout);
        netcheck_layout = findViewById(R.id.netcheck_layout);
        nolayout = findViewById(R.id.nolayout);
        myLayout = findViewById(R.id.parent_qalayout);
        scrollview = findViewById(R.id.scrollview);
        progressBar = findViewById(R.id.progressBar);
        answer_layout = findViewById(R.id.answer_layout);
        show_more = findViewById(R.id.show_more);
        ans_more_dets = findViewById(R.id.ans_more_dets);
        diagnosis = findViewById(R.id.diagnosis);
        pb_cause = findViewById(R.id.pb_cause);
        lab_t = findViewById(R.id.lab_t);
        ddx = findViewById(R.id.ddx);
        pdx = findViewById(R.id.pdx);
        treatment_plan = findViewById(R.id.treatment_plan);
        p_tips = findViewById(R.id.p_tips);
        followup = findViewById(R.id.followup);
        btn_done = findViewById(R.id.btn_done);
        tvclear = findViewById(R.id.tvclear);
        tvprivate = findViewById(R.id.tvprivate);
        show_less = findViewById(R.id.show_less);
        edt_answer = findViewById(R.id.edt_answer);
        tv_anstit = findViewById(R.id.tv_anstit);
        tv_show = findViewById(R.id.tv_show);
        btn_write_pres = findViewById(R.id.btn_write_pres);

        if (new Detector().isTablet(getApplicationContext())) {
            tv_anstit.setTextSize(18);
            edt_answer.setTextSize(20);
            pb_cause.setTextSize(20);
            lab_t.setTextSize(20);
            ddx.setTextSize(20);
            pdx.setTextSize(20);
            treatment_plan.setTextSize(20);
            p_tips.setTextSize(20);
            followup.setTextSize(20);
            btn_done.setTextSize(20);
            btn_notanswer.setTextSize(20);
            tv_show.setTextSize(20);
            tvprivate.setTextSize(20);
            diagnosis.setTextSize(20);
        }

        //opt_ffollowup = (CheckBox) findViewById(R.id.opt_ffollowup);

        if (new Detector().isTablet(getApplicationContext())) {
            btn_ansquery.setHeight(75);
            btn_ansquery.setTextSize(25);
        }

        try {

            Intent intent = getIntent();
            followupcode = intent.getStringExtra("followupcode");
            query_price_text = intent.getStringExtra("str_price");
            pat_from_text = intent.getStringExtra("pat_location");
            qtype = intent.getStringExtra("qtype");

            System.out.println("Get intent followupcode----" + followupcode);
            System.out.println("Get intent query_price_text----" + query_price_text);
            System.out.println("Get intent pat_from_text----" + pat_from_text);

        } catch (Exception e) {
            e.printStackTrace();
        }

        edt_answer.setText(Model.query_typed);

        ad_close_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ad_layout.setVisibility(View.GONE);

                //--------------------Ad- Close--------
                String ad_url = Model.BASE_URL + "/sapp/closeAd?user_id=" + Model.id + "&track_id=" + track_id_val + "&token=" + Model.token;
                System.out.println("ad_url----------" + ad_url);
                new JSON_Close().execute(ad_url);
                //---------------------Ad- Close-------
            }
        });

        reminder_switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    days_layout.setVisibility(View.VISIBLE);
                } else {
                    days_layout.setVisibility(View.GONE);
                }
            }
        });


        btn_notanswer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showNotAnswerAlert();
            }
        });

        btn_upload_pres.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                attach_dialog();
            }
        });


        btn_write_pres.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

/*                Intent i = new Intent(NewQueryViewActivity.this, Prescriptions_Activity.class);
                i.putExtra("qid", current_qid);
                i.putExtra("patient_id", patient_id);
                startActivity(i);
                //finish();*/

                View parent = (View) v.getParent();
                TextView tvqid = (TextView) parent.findViewById(R.id.tv_write_query_id);
                String qid_val = tvqid.getText().toString();

                System.out.println("qid_val---------" + qid_val);

                Intent intent = new Intent(NewQueryViewActivity.this, Prescription_home.class);
                intent.putExtra("add_type", "new");
                intent.putExtra("cur_qid", qid_val);
                intent.putExtra("p_type", "query");
                startActivity(intent);

            }
        });


        btn_follow_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isInternetOn()) {

                    String ans_followup_text = edt_followup.getText().toString();

                    if (!ans_followup_text.equals("")) {
                        submit_followup();
                    } else {
                        edt_followup.setError("Please enter the follow-up message");
                        edt_followup.requestFocus();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Please check your Internet Connection and try again", Toast.LENGTH_SHORT).show();
                }
            }
        });


        btn_ansquery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (isInternetOn()) {
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

                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Please check your Internet Connection and try again!", Toast.LENGTH_SHORT).show();
                }
            }
        });


        btn_reload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (isInternetOn()) {
                    fullprocess();
                } else {
                    Toast.makeText(getApplicationContext(), "Please check your Internet Connection and try again!", Toast.LENGTH_SHORT).show();

                    progressBar.setVisibility(View.GONE);
                    scrollview.setVisibility(View.GONE);
                    nolayout.setVisibility(View.GONE);
                    netcheck_layout.setVisibility(View.VISIBLE);
                    button_layout.setVisibility(View.GONE);
                    btn_ansquery.setVisibility(View.GONE);
                }
            }
        });


        if (isInternetOn()) {
            fullprocess();
        } else {

            Toast.makeText(getApplicationContext(), "Please check your Internet Connection and try again!", Toast.LENGTH_SHORT).show();

            progressBar.setVisibility(View.GONE);
            scrollview.setVisibility(View.GONE);
            nolayout.setVisibility(View.GONE);
            netcheck_layout.setVisibility(View.VISIBLE);
            button_layout.setVisibility(View.GONE);
            btn_ansquery.setVisibility(View.GONE);
        }

        tvprivate.setText(Html.fromHtml("Private diagnosis (<b>Won't be displayed to the patient</b>)"));

        edt_answer.addTextChangedListener(edt_answerWatcher);


        btn_done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (isInternetOn()) {

                    ent_ans = edt_answer.getText().toString();
                    prescribe_value = prescribe_text.getText().toString();

                    if (!ent_ans.equals("")) {
                        submit_answer();

/*                        if (prescribe_status.equals("1")) {
                            if (!(prescribe_value.equals(""))) {
                                submit_answer();
                            } else {
                                prescribe_text.setError("Please enter the prescription");
                                prescribe_text.requestFocus();
                            }
                        } else {
                            submit_answer();
                        }*/
                    } else {
                        edt_answer.setError("Please enter the answer");
                        edt_answer.requestFocus();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Please check your Internet Connection and try again", Toast.LENGTH_SHORT).show();
                }
            }
        });


        int myNum = 0;
        try {
            myNum = Integer.parseInt((query_price_text));
            if (myNum >= 30) {
                ans_more_dets.setVisibility(View.VISIBLE);
                show_more.setVisibility(View.GONE);
            } else {
                ans_more_dets.setVisibility(View.GONE);
                show_more.setVisibility(View.VISIBLE);
            }
        } catch (NumberFormatException nfe) {

            //System.out.println("Could not parse " + nfe);
        }

        show_more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ans_more_dets.setVisibility(View.VISIBLE);
                show_more.setVisibility(View.GONE);
            }
        });

        show_less.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ans_more_dets.setVisibility(View.GONE);
                show_more.setVisibility(View.VISIBLE);
            }
        });


        tvclear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                edt_answer.setText("");
                Model.query_typed = "";
            }
        });

        System.out.println("isEnablefFollowup----------" + isEnablefFollowup);

        followup_switch.setVisibility(View.VISIBLE);
        followup_switch.setChecked(true);


        //------------------ Initialize File Attachment ---------------------------------
        Nammu.init(this);
        int permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            Nammu.askForPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE, new PermissionCallback() {
                @Override
                public void permissionGranted() {
                    //Nothing, this sample saves to Public gallery so it needs permission
                }

                @Override
                public void permissionRefused() {
                    finish();
                }
            });
        }

        EasyImage.configuration(this)
                .setImagesFolderName("Attachments")
                .setCopyTakenPhotosToPublicGalleryAppFolder(true)
                .setCopyPickedImagesToPublicGalleryAppFolder(true)
                .setAllowMultiplePickInGallery(true);

        //------------------ Initialize File Attachment ---------------------------------


    }

    public void onClick(View v) {

        if (isInternetOn()) {

            try {

                TextView tv_filename = v.findViewById(R.id.tv_filename);
                TextView tv_qid = v.findViewById(R.id.tv_qid);

                String file_name = tv_filename.getText().toString();
                String tv_qid_text = tv_qid.getText().toString();

                System.out.println("str_filename-------" + file_name);
                System.out.println("tv_qid_text-------" + tv_qid_text);

/*                Intent i = new Intent(getApplicationContext(), GridViewActivity.class);
                //Intent i = new Intent(getApplicationContext(), Attachment_List_Activity.class);
                //Intent i = new Intent(getApplicationContext(), Attached_List_Activity.class);
                //i.putExtra("filetxt", files_text);
                i.putExtra("filetxt", file_name);
                startActivity(i);
                overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);*/

                Intent i = new Intent(NewQueryViewActivity.this, ExpandableActivity.class);
                i.putExtra("item_id", tv_qid_text);
                i.putExtra("item_type", "query");
                startActivity(i);
                overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);


            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            Toast.makeText(getApplicationContext(), "Please check your Internet Connection and try again.", Toast.LENGTH_SHORT).show();
        }
    }

    public void onClickFileOpen(View v) {

        try {

            View parent = (View) v.getParent();

            TextView tv_filename = v.findViewById(R.id.tv_answer_filename);

            String file_name = tv_filename.getText().toString();

            System.out.println("Clicked str_filename-------" + file_name);

            Intent i = new Intent(getApplicationContext(), GridViewActivity.class);
            i.putExtra("filetxt", file_name);
            startActivity(i);
            overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);


/*            Intent i = new Intent(getApplicationContext(), ExpandableActivity.class);
            i.putExtra("item_id", current_answer_id);
            i.putExtra("item_type", "query");
            startActivity(i);
            overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);*/


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void fullprocess() {

        //------------ Object Creations -------------------------------
        try {
            if ((Model.id) != null && !(Model.id).isEmpty() && !(Model.id).equals("null") && !(Model.id).equals("")) {
                if (followupcode != null && !followupcode.isEmpty() && !followupcode.equals("null") && !followupcode.equals("")) {

                    //----------------------------------------------------
                    String full_url = Model.BASE_URL + "sapp/jsonviewquery4doc?followupcode=" + followupcode + "&user_id=" + (Model.id) + "&token=" + Model.token + "&enc=1&isAFiles=1";
                    System.out.println("New_Query_full_url-------------" + full_url);
                    new JSON_QueryView().execute(full_url);
                    //----------------------------------------------------

                    scrollview.fullScroll(ScrollView.FOCUS_DOWN);

                } else {
                    go_back_msg();
                }
            } else {
                go_back_msg();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        //------------ Object Creations -------------------------------
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.query_view_answer, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == android.R.id.home) {
            finish();
            return true;
        }

        if (id == R.id.nav_ans_guidelines) {
            show_guidelines();
            return true;
        }

        if (id == R.id.nav_earnmore) {
            show_tips();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public final boolean isInternetOn() {

        ConnectivityManager connec = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);

        if (connec.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.CONNECTED ||
                connec.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.CONNECTING ||
                connec.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.CONNECTING ||
                connec.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.CONNECTED) {

            return true;

        } else if (
                connec.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.DISCONNECTED ||
                        connec.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.DISCONNECTED) {

            return false;
        }
        return false;
    }

    public void alertdia(String diamsg) {
        final Context context = this;
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
        alertDialogBuilder
                .setMessage(diamsg)
                .setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        ((android.os.ResultReceiver) getIntent().getParcelableExtra("finisher")).send(1, new Bundle());

                        //Intent i = new Intent(NewQueryViewActivity.this, NewQueriesActivity.class);
                        //startActivity(i);
                        finish();
                    }
                });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    private void showNotAnswerAlert() {
        AlertDialog.Builder builder = new AlertDialog.Builder(NewQueryViewActivity.this);
        builder.setTitle("Confirmation");
        builder.setMessage("If you do not want to answer this, Query will be released to answer other doctors. \n\n Do you want to release this query?");

        String positiveText = "Release it";
        builder.setPositiveButton(positiveText,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        //---------------------------------------------------
                        String url = Model.BASE_URL + "sapp/releaseQuery?qid=" + current_qid;
                        System.out.println("Not Answering url-------------" + url);
                        new JSON_notanswer().execute(url);
                        //---------------------------------------------------
                    }
                });

        String negativeText = "Cancel";
        builder.setNegativeButton(negativeText,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

        AlertDialog dialog = builder.create();
        // display dialog
        dialog.show();
    }

    public void submit_answer() {

        try {

            if ((Model.id) != null && !(Model.id).isEmpty() && !(Model.id).equals("null") && !(Model.id).equals("")) {
                if (current_qid != null && !current_qid.isEmpty() && !current_qid.equals("null") && !current_qid.equals("")) {

                    try {
                        answer_txt = URLEncoder.encode((edt_answer.getText().toString()), "UTF-8");

                        Model.query_typed = "";

                        String diag_text = diagnosis.getText().toString();

                        String pb_cause_text = pb_cause.getText().toString();
                        String lab_t_text = lab_t.getText().toString();
                        String ddx_text = ddx.getText().toString();
                        String pdx_text = pdx.getText().toString();
                        String treatment_plan_text = treatment_plan.getText().toString();
                        String p_tips_text = p_tips.getText().toString();
                        String followup_text = followup.getText().toString();

                        String prescribe_text_text = prescribe_text.getText().toString();

                        //---------------Jio Query Check-----------------------------------
                        if (jio_query.equals("1")) {
                            if (pb_cause_text != null && !pb_cause_text.isEmpty() && !pb_cause_text.equals("null") && !pb_cause_text.equals("")) {
                                if (lab_t_text != null && !lab_t_text.isEmpty() && !lab_t_text.equals("null") && !lab_t_text.equals("")) {
                                    if (ddx_text != null && !ddx_text.isEmpty() && !ddx_text.equals("null") && !ddx_text.equals("")) {
                                        if (pdx_text != null && !pdx_text.isEmpty() && !pdx_text.equals("null") && !pdx_text.equals("")) {
                                            if (treatment_plan_text != null && !treatment_plan_text.isEmpty() && !treatment_plan_text.equals("null") && !treatment_plan_text.equals("")) {
                                                if (p_tips_text != null && !p_tips_text.isEmpty() && !p_tips_text.equals("null") && !p_tips_text.equals("")) {

                                                    send_answer();

                                                } else {
                                                    p_tips.setError("Please enter the preventive measures (Cannot be left blank)");
                                                    Toast.makeText(getApplicationContext(), "Please enter the preventive measures (Cannot be left blank)", Toast.LENGTH_SHORT).show();
                                                }
                                            } else {
                                                treatment_plan.setError("Please enter the treatment plan");
                                                Toast.makeText(getApplicationContext(), "Please enter the treatment plan", Toast.LENGTH_SHORT).show();
                                            }

                                        } else {
                                            pdx.setError("Please enter the probable diagnosis");
                                            Toast.makeText(getApplicationContext(), "Please enter the probable diagnosis", Toast.LENGTH_SHORT).show();
                                        }

                                    } else {
                                        ddx.setError("Please enter the differential diagnosis");
                                        Toast.makeText(getApplicationContext(), "Please enter the differential diagnosis", Toast.LENGTH_SHORT).show();
                                    }

                                } else {
                                    lab_t.setError("Please enter the investigations");
                                    Toast.makeText(getApplicationContext(), "Please enter the investigations", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                pb_cause.setError("Please enter the probable causes");
                                Toast.makeText(getApplicationContext(), "Please enter the probable causes", Toast.LENGTH_SHORT).show();
                            }
                        } else {

                            send_answer();
                        }

                        //---------------Jio Query Check-----------------------------------

                    } catch (Exception e) {

                        go_back_msg();

                        e.printStackTrace();
                    }

                } else {

                    go_back_msg();
                }
            } else {

                go_back_msg();
            }

        } catch (Exception e) {

            try {
                System.out.println("Exception-----3--" + e.toString());


            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
    }

    public void go_back_msg() {


        //---------------- Dialog------------------------------------------------------------------
        final MaterialDialog alert = new MaterialDialog(NewQueryViewActivity.this);
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
    }

    @Override
    public void onResume() {
        super.onResume();

        System.out.println("Resume current_qid---" + current_qid);
        System.out.println("Resume Model.id---" + Model.id);

        try {
            if (current_qid != null && !current_qid.isEmpty() && !current_qid.equals("null") && !current_qid.equals("")) {
                if ((Model.id) != null && !(Model.id).isEmpty() && !(Model.id).equals("null") && !(Model.id).equals("")) {
                    try {

                        String url = Model.BASE_URL + "sapp/canIAnswer?user_id=" + (Model.id) + "&qid=" + current_qid + "&token=" + Model.token;
                        System.out.println("canIAnswer url------" + url);

                        // new Resume_canianswer().execute(url);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("Resume Error---" + e.toString());
            e.printStackTrace();
        }


        try {
            if ((Model.prescribe_flag) != null && !(Model.prescribe_flag).isEmpty() && !(Model.prescribe_flag).equals("null") && !(Model.prescribe_flag).equals("")) {
                try {

                    if ((Model.prescribe_flag).equals("true")) {
                        prescription_title.setText("--- Prescription has been added ---");
                        //btn_write_pres.setText("Write more prescription");
                        fullprocess();
                    } else {
                        prescription_title.setText("");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        } catch (Exception e) {
            System.out.println("Resume Error---" + e.toString());
            e.printStackTrace();
        }
    }

    public void query_release_check() {


        //---------------- Dialog------------------------------------------------------------------
        final MaterialDialog alert = new MaterialDialog(NewQueryViewActivity.this);
        alert.setTitle("Oops!");
        alert.setMessage("This query has been released. Please check your dashboard for new query.");
        alert.setCanceledOnTouchOutside(false);
        alert.setPositiveButton("OK", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alert.dismiss();
/*                Intent i = new Intent(NewQueryViewActivity.this, Query.class);
                startActivity(i);*/
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


    }

    @Override
    public void onBackPressed() {
        finish();
    }

    public void show_guidelines() {

        final MaterialDialog alert = new MaterialDialog(NewQueryViewActivity.this);
        View view = LayoutInflater.from(NewQueryViewActivity.this).inflate(R.layout.answeringgudlines, null);
        alert.setView(view);
        alert.setTitle("Answering Guidelines");
        alert.setCanceledOnTouchOutside(false);

        Toolbar toolBar = view.findViewById(R.id.toolBar);
        imgapp = view.findViewById(R.id.imgapp);
        final TextView tvguidline = view.findViewById(R.id.tvguidline);

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
    }

    public void show_tips() {

        final MaterialDialog alert = new MaterialDialog(NewQueryViewActivity.this);
        View view = LayoutInflater.from(NewQueryViewActivity.this).inflate(R.layout.tipstoearning, null);
        alert.setView(view);
        alert.setTitle("Tips for Earning");
        alert.setCanceledOnTouchOutside(false);

        Toolbar toolBar = view.findViewById(R.id.toolBar);
        imgapp = view.findViewById(R.id.imgapp);
        tvtips1 = view.findViewById(R.id.tvtips1);
        tvtips2 = view.findViewById(R.id.tvtips2);
        tvtips3 = view.findViewById(R.id.tvtips3);

        toolBar.setVisibility(View.GONE);
        imgapp.setVisibility(View.GONE);
        tvtips1.setText(Html.fromHtml(getString(R.string.tips1)));
        tvtips2.setText(Html.fromHtml(getString(R.string.tips2)));
        tvtips3.setText(Html.fromHtml(getString(R.string.tips3)));

        alert.setPositiveButton("OK", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alert.dismiss();
            }
        });
        alert.show();
    }

    public void showFeedbackDialog() {

        try {
            final MaterialDialog alert = new MaterialDialog(NewQueryViewActivity.this);
            View view = LayoutInflater.from(NewQueryViewActivity.this).inflate(R.layout.ask_feedback, null);
            alert.setView(view);

            alert.setTitle("Reply to feedback");

            final EditText edt_coupon = view.findViewById(R.id.edt_coupon);

            alert.setCanceledOnTouchOutside(false);
            alert.setPositiveButton("OK", new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    feedback_text = edt_coupon.getText().toString();

                    if (!(feedback_text.equals(""))) {

                        try {
                            feedback_json = new JSONObject();
                            feedback_json.put("feedback_id", feedback_id_text);
                            feedback_json.put("user_id", (Model.id));
                            feedback_json.put("qid", current_qid);
                            feedback_json.put("reply", feedback_text);

                            System.out.println("feedback_json----------" + feedback_json.toString());

                            new JSON_post_feedback().execute(feedback_json);

                            alert.dismiss();

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else {
                        edt_coupon.setError("Please enter your feedback");
                    }
                }
            });

            alert.setNegativeButton("CANCEL", new View.OnClickListener() {
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

    public void send_answer() {

        try {
            json = new JSONObject();
            json.put("user_id", (Model.id));
            json.put("qid", current_qid);
            json.put("reply", answer_txt);
            json.put("diagnosis", (diagnosis.getText().toString()));
            json.put("pb_cause", (pb_cause.getText().toString()));
            json.put("lab_t", (lab_t.getText().toString()));
            json.put("ddx", (ddx.getText().toString()));
            json.put("pdx", (pdx.getText().toString()));
            json.put("treatment_plan", (treatment_plan.getText().toString()));
            json.put("followup", (followup.getText().toString()));
            json.put("p_tips", (p_tips.getText().toString()));
            json.put("prescription", (prescribe_text.getText().toString()));

            if (reminder_switch.isChecked()) {
                json.put("follow_remind_days", (edit_remind_days.getText().toString()));
            }

            System.out.println("json------------" + json.toString());

       /*     //------------ Tracker ------------------------
            MyApp.tracker().send(new HitBuilders.EventBuilder()
                    .setCategory("Query_View")
                    .setAction("Answering_Status=" + str_response)
                    .build());
            //------------ Tracker ------------------------
*/

            if (followup_switch.getVisibility() == View.VISIBLE) {
                //------------------------------------------------------
                if (followup_switch.isChecked()) {
                    json.put("enable_freefollow", "on");
                    System.out.println("parameter with-------on");
                } else {
                    json.put("enable_freefollow", "off");
                    System.out.println("parameter with-------off");
                }
                //------------------------------------------------------
            } else {

                json.put("enable_freefollow", enable_freefollow);

                System.out.println("enable_freefollow-------" + enable_freefollow);

 /*
                if ((enable_freefollow).equals("on")) {
                    json.put("enable_freefollow", "on");
                    System.out.println("parameter with-------on");
                    //------------------------------------------------------
                } else {
                    System.out.println("visi-else part------FALSE");
                    json.put("enable_freefollow", "off");
                    System.out.println("parameter with-------off");
                    //------------------------------------------------------
                }*/
            }

            new JSONPostAnswer().execute(json);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void attach_dialog() {
        List<String> mAnimals = new ArrayList<String>();
        mAnimals.add("Take Photo");
        mAnimals.add("Browse Files");

        final CharSequence[] Animals = mAnimals.toArray(new String[mAnimals.size()]);
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        dialogBuilder.setTitle("Attach Files/Images");
        dialogBuilder.setItems(Animals, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int item) {
                String selectedText = Animals[item].toString();
                System.out.println("selectedText---" + selectedText);

                if (selectedText.equals("Take Photo")) {

                    int permissionCheck = ContextCompat.checkSelfPermission(NewQueryViewActivity.this, Manifest.permission.CAMERA);
                    if (permissionCheck == PackageManager.PERMISSION_GRANTED) {
                        EasyImage.openCamera(NewQueryViewActivity.this, 0);
                    } else {
                        Nammu.askForPermission(NewQueryViewActivity.this, Manifest.permission.CAMERA, new PermissionCallback() {
                            @Override
                            public void permissionGranted() {
                                EasyImage.openCamera(NewQueryViewActivity.this, 0);
                            }

                            @Override
                            public void permissionRefused() {

                            }
                        });
                    }

                } else {
                    //showChooser();

                    int permissionCheck = ContextCompat.checkSelfPermission(NewQueryViewActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
                    if (permissionCheck == PackageManager.PERMISSION_GRANTED) {
                        EasyImage.openDocuments(NewQueryViewActivity.this, 0);
                    } else {
                        Nammu.askForPermission(NewQueryViewActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE, new PermissionCallback() {
                            @Override
                            public void permissionGranted() {
                                EasyImage.openDocuments(NewQueryViewActivity.this, 0);
                            }

                            @Override
                            public void permissionRefused() {

                            }
                        });
                    }

                }
            }
        });
        AlertDialog alertDialogObject = dialogBuilder.create();
        alertDialogObject.show();
    }

    /* public String upload_file(String fullpath) {

         String fpath_filename = fullpath.substring(fullpath.lastIndexOf("/") + 1);

         local_url = fullpath;

         System.out.println("fpath-------" + fullpath);
         System.out.println("fpath_filename---------" + fpath_filename);

         last_upload_file = fpath_filename;

         HttpURLConnection conn = null;
         DataOutputStream dos = null;

         String lineEnd = "\r\n";
         String twoHyphens = "--";
         String boundary = "*****";
         int bytesRead, bytesAvailable, bufferSize;
         byte[] buffer;
         int maxBufferSize = 1024 * 1024;
         File sourceFile = new File(fullpath);

         if (!sourceFile.isFile()) {
             System.out.println("Source File not exist :" + fullpath);
             return "";
         } else {

             try {
                 upLoadServerUri = Model.BASE_URL + "/sapp/ansUpload?user_id=" + (Model.id) + "&qid=" + (current_qid) + "&token=" + Model.token;
                 System.out.println("upLoadServerUri---------------------" + upLoadServerUri);

                 FileInputStream fileInputStream = new FileInputStream(fullpath);
                 System.out.println("fullpath---------------------------------" + fullpath);
                 URL url = new URL(upLoadServerUri);

                 conn = (HttpURLConnection) url.openConnection();
                 conn.setDoInput(true);
                 conn.setDoOutput(true);
                 conn.setUseCaches(false);
                 conn.setRequestMethod("POST");
                 conn.setRequestProperty("Connection", "Keep-Alive");
                 conn.setRequestProperty("ENCTYPE", "multipart/form-data");
                 conn.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);
                 conn.setRequestProperty("uploaded_file", fullpath);

                 dos = new DataOutputStream(conn.getOutputStream());
                 dos.writeBytes(twoHyphens + boundary + lineEnd);
                 dos.writeBytes("Content-Disposition: form-data; name=\"file\";filename=\"" + fullpath + "\"" + lineEnd);
                 dos.writeBytes(lineEnd);

                 bytesAvailable = fileInputStream.available();
                 bufferSize = Math.min(bytesAvailable, maxBufferSize);
                 buffer = new byte[bufferSize];
                 bytesRead = fileInputStream.read(buffer, 0, bufferSize);

                 while (bytesRead > 0) {
                     dos.write(buffer, 0, bufferSize);
                     bytesAvailable = fileInputStream.available();
                     bufferSize = Math.min(bytesAvailable, maxBufferSize);
                     bytesRead = fileInputStream.read(buffer, 0, bufferSize);
                 }

                 dos.writeBytes(lineEnd);
                 dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);

                 serverResponseCode = conn.getResponseCode();
                 serverResponseMessage = conn.getResponseMessage();

                 int response = conn.getResponseCode();
                 System.out.println("response-------" + response);
                 is = conn.getInputStream();
                 contentAsString = convertInputStreamToString(is);
                 System.out.println("Upload File Response-----------------" + contentAsString);

                 fileInputStream.close();
                 dos.flush();
                 dos.close();

             } catch (MalformedURLException ex) {
                 ex.printStackTrace();

                 runOnUiThread(new Runnable() {
                     public void run() {
                     }
                 });

             } catch (Exception e) {
                 e.printStackTrace();
             }

             return contentAsString;
         }
     }
 */
    public String convertInputStreamToString(InputStream stream) throws IOException {

        try {
            BufferedReader r = new BufferedReader(new InputStreamReader(stream, StandardCharsets.UTF_8));
            total = new StringBuilder();
            String line;
            while ((line = r.readLine()) != null) {
                total.append(line).append('\n');
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return total.toString();

    }

    public String getPath(Uri uri) {

        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor = managedQuery(uri, projection, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        Nammu.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        EasyImage.handleActivityResult(requestCode, resultCode, data, this, new DefaultCallback() {
            @Override
            public void onImagePickerError(Exception e, EasyImage.ImageSource source, int type) {
                e.printStackTrace();
            }

            @Override
            public void onImagesPicked(List<File> imageFiles, EasyImage.ImageSource source, int type) {
                onPhotosReturned(imageFiles);
                System.out.println("Selected file------------" + source.toString());

            }

            @Override
            public void onCanceled(EasyImage.ImageSource source, int type) {
                if (source == EasyImage.ImageSource.CAMERA) {
                    File photoFile = EasyImage.lastlyTakenButCanceledPhoto(NewQueryViewActivity.this);
                    if (photoFile != null) photoFile.delete();
                }
            }
        });
    }

    private void onPhotosReturned(List<File> returnedPhotos) {

        //photos.addAll(returnedPhotos);

        for (int i = 0; i < returnedPhotos.size(); i++) {
            System.out.println(returnedPhotos.get(i));

            System.out.println("File Name------------------" + (returnedPhotos.get(i)).getName());

            selectedPath = (returnedPhotos.get(i).toString());
            selectedfilename = (returnedPhotos.get(i)).getName();

            //----------- Flurry -------------------------------------------------
            Map<String, String> articleParams = new HashMap<String, String>();
            articleParams.put("android.patient.Qid", (attach_qid));
            articleParams.put("android.patient.attach_file_path", selectedPath);
            articleParams.put("android.patient.attach_filename", selectedfilename);
            FlurryAgent.logEvent("android.patient.Attach_Take_Photo", articleParams);
            //----------- Flurry -------------------------------------------------

            //------------ Google firebase Analitics--------------------
            Model.mFirebaseAnalytics = FirebaseAnalytics.getInstance(getApplicationContext());
            Bundle params = new Bundle();
            params.putString("User", Model.id);
            params.putString("Qid", attach_qid);
            params.putString("attach_file_path", selectedPath);
            params.putString("attach_filename", selectedfilename);
            Model.mFirebaseAnalytics.logEvent("Attach_Files", params);
            //------------ Google firebase Analitics--------------------

            System.out.println("selectedPath------------" + selectedPath);
            new AsyncTask_fileupload().execute(selectedPath);

        }
    }

    @Override
    protected void onDestroy() {
        //  EasyImage.clearConfiguration(this);
        super.onDestroy();
    }

    public void submit_followup() {

        try {

            if ((Model.id) != null && !(Model.id).isEmpty() && !(Model.id).equals("null") && !(Model.id).equals("")) {
                if (current_answer_id != null && !current_answer_id.isEmpty() && !current_answer_id.equals("null") && !current_answer_id.equals("")) {

                    try {
                        followup_txt = URLEncoder.encode((edt_followup.getText().toString()), "UTF-8");

                        Model.query_typed = "";

                        json = new JSONObject();
                        json.put("user_id", (Model.id));
                        json.put("answer_id", current_answer_id);
                        json.put("followup", followup_txt);

                        System.out.println("Followup_json------------" + json.toString());

                        new JSONPostFollowup().execute(json);


                        //------------ Google firebase Analitics--------------------
                        Model.mFirebaseAnalytics = FirebaseAnalytics.getInstance(getApplicationContext());
                        Bundle params = new Bundle();
                        params.putString("User", Model.id + "/" + Model.name);
                        params.putString("Details", followup_txt);
                        Model.mFirebaseAnalytics.logEvent("Followup_toPat", params);
                        //------------ Google firebase Analitics--------------------

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

   /* private String upload_file(String file_path) {

        last_upload_file = file_path;

        String ServerUploadPath = Model.BASE_URL + "/sapp/ansUpload?user_id=" + (Model.id) + "&qid=" + (current_qid) + "&token=" + Model.token;
        System.out.println("ServerUploadPath---------- " + ServerUploadPath);

        File file_value = new File(file_path);

        try {

            HttpClient client = new DefaultHttpClient();
            HttpPost post = new HttpPost(ServerUploadPath);
            MultipartEntity2 reqEntity = new MultipartEntity2();
            reqEntity.addPart("file", file_value);
            post.setEntity(reqEntity);

            HttpResponse response = client.execute(post);
            HttpEntity resEntity = response.getEntity();

            try {
                final String response_str = EntityUtils.toString(resEntity);

                if (resEntity != null) {
                    System.out.println("response_str-------" + response_str);
                    contentAsString = response_str;

                }
            } catch (Exception ex) {
                Log.e("Debug", "error: " + ex.getMessage(), ex);
            }
        } catch (Exception e) {
            Log.e("Upload Exception", "");
            e.printStackTrace();
        }

        return contentAsString;
    }
*/


    private String upload_file(String file_path) {

        last_upload_file = file_path;

        System.out.println("last_upload_file------------" + last_upload_file);

        String ServerUploadPath = Model.BASE_URL + "/sapp/ansUpload?user_id=" + (Model.id) + "&qid=" + (current_qid) + "&token=" + Model.token;
        //String ServerUploadPath = Model.BASE_URL + "/sapp/ansUpload?user_id=" + (Model.id) + "&os_type=android&appt_id=" + cons_id + "&token=" + Model.token;

        System.out.println("ServerUploadPath------------" + ServerUploadPath);

        File file_value = new File(file_path);

        try {

            HttpClient client = new DefaultHttpClient();
            HttpPost post = new HttpPost(ServerUploadPath);
            MultipartEntity2 reqEntity = new MultipartEntity2();
            reqEntity.addPart("file", file_value);
            post.setEntity(reqEntity);

            HttpResponse response = client.execute(post);
            HttpEntity resEntity = response.getEntity();

            try {
                final String response_str = EntityUtils.toString(resEntity);
                if (resEntity != null) {
                    System.out.println("response_str-------" + response_str);
                    contentAsString = response_str;

                }
            } catch (Exception ex) {
                Log.e("Debug", "error: " + ex.getMessage(), ex);
            }
        } catch (Exception e) {
            Log.e("Upload Exception", "");
            e.printStackTrace();
        }

        return contentAsString;
    }


    public void onclick_viewpres(View v) {

        try {
            View parent = (View) v.getParent();

            TextView cqid = (TextView) parent.findViewById(R.id.tv_answer_query_id);
            String cqid_val = cqid.getText().toString();
            System.out.println("cqid_val---------" + cqid_val);

            String params = Model.BASE_URL + "sapp/previewPrescription?user_id=" + (Model.id) + "&token=" + Model.token + "&os_type=android&item_type=query&item_id=" + cqid_val;
            System.out.println("Pressed Prescription-----------" + params);
            new list_drugs().execute(params);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private class JSON_canianswer extends AsyncTask<String, Void, Boolean> {

        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(NewQueryViewActivity.this);
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
                        Intent intent = new Intent(NewQueryViewActivity.this, LoginActivity.class);
                        startActivity(intent);
                        finish();
                    }

                } else {

                    answering_status = jsonobj_canisnaswer.getString("status");
                    opt_freefollow = jsonobj_canisnaswer.getString("opt_freefollow");
                    enable_freefollow = jsonobj_canisnaswer.getString("enable_freefollow");
                    Log.e("enable_freefollow",enable_freefollow+" ");
                    Log.e("opt_freefollow",opt_freefollow+" ");
                    //----------------------------------------------
//                  if (!enable_freefollow.isEmpty() && enable_freefollow.equalsIgnoreCase("off")){
//
//                  }
                    if ((opt_freefollow).equals("1")) {
                        check_enable_ffollowup = "Yes";
                        isEnablefFollowup = "Yes";
                        followup_switch.setVisibility(View.VISIBLE);
                        followup_switch.setChecked(true);
                        System.out.println("Opt_freefollowup_--------------1");
                    } else {

                        check_enable_ffollowup = "No";
                        isEnablefFollowup = "No";

                        followup_switch.setVisibility(View.GONE);
                        followup_switch.setChecked(false);
                    }
                    //----------------------------------------------

                    //----------------------------------------------
                    if ((answering_status).equals("0")) {
                        Toast.makeText(getApplicationContext(), "Sorry! Another doctor has already picked this query.", Toast.LENGTH_LONG).show();
                    } else {
                        button_layout.setVisibility(View.VISIBLE);
                        answer_layout.setVisibility(View.VISIBLE);
                        btn_ansquery.setVisibility(View.GONE);
                        scrollview.fullScroll(ScrollView.FOCUS_DOWN);
                    }
                    try {

                        //----------- Flurry -------------------------------------------------
                        Map<String, String> articleParams = new HashMap<String, String>();
                        articleParams.put("android.doc.qid", current_qid);
                        articleParams.put("android.doc.answering_status", answering_status);
                        articleParams.put("android.doc.check_enable_free_followup", check_enable_ffollowup);
                        FlurryAgent.logEvent("android.doc.Can_Answer_Status", articleParams);
                        //----------- Flurry -------------------------------------------------

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    dialog.cancel();
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    class JSON_QueryView extends AsyncTask<String, Void, Boolean> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressBar.setVisibility(View.VISIBLE);
            scrollview.setVisibility(View.GONE);
            nolayout.setVisibility(View.GONE);
            netcheck_layout.setVisibility(View.GONE);
            button_layout.setVisibility(View.GONE);
            btn_ansquery.setVisibility(View.GONE);

            startTime = System.currentTimeMillis();

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
                        Intent intent = new Intent(NewQueryViewActivity.this, LoginActivity.class);
                        startActivity(intent);
                        finish();
                    }

                } else {

                    patient_id = jsonobj.getString("patient_id");
                    qansby = jsonobj.getString("askedby_name");
                    qcanianswer = jsonobj.getString("cananswer");
                    current_qid = jsonobj.getString("qid");
                    String cur_qid = jsonobj.getString("qid");
                    qitems = jsonobj.getString("items");

                    //---------------------
                    tv_write_query_id.setText(cur_qid);

                    if (jsonobj.has("diagnose_prediction")) {
                        diagnose_prediction_text = jsonobj.getString("diagnose_prediction");
                        System.out.println("diagnose_prediction_text=============== " + diagnose_prediction_text);
                    } else {
                        diagnose_prediction_text = "";
                    }

                    System.out.println("diagnose_prediction_text=============== " + diagnose_prediction_text);
                    System.out.println("patient_id=============== " + patient_id);

                    if (jsonobj.has("jio_eo")) {
                        jio_query = jsonobj.getString("jio_eo");
                        System.out.println("This is Jio Query-----------------");

                        ans_more_dets.setVisibility(View.VISIBLE);
                        show_more.setVisibility(View.GONE);

                    } else {
                        jio_query = "0";
                        System.out.println("This is Not jio Query-----------------");
                    }


                    //------------ Get Array -----------------------------------
                    String array_text = jsonobj.getString("ans_ext_expansion");
                    String array_text_query = jsonobj.getString("query_ext_expansion");

                    System.out.println("array_text_ans-----" + array_text);
                    System.out.println("array_text_query-----" + array_text_query);

                    //-------------- query Key -----------------------------
                    JSONObject query_arrayJSONObj = new JSONObject(array_text_query);
                    Iterator<String> query_arrayiterator = query_arrayJSONObj.keys();
                    while (query_arrayiterator.hasNext()) {
                        String query_key = query_arrayiterator.next();
                        String value_of_key = query_arrayJSONObj.optString(query_key);

                        System.out.println("query_key------------------" + query_key);
                        System.out.println("value_of_key------------------" + value_of_key);

                        extra_query_map.put(query_key, value_of_key);
                    }
                    //-------------- query Key -----------------------------

                    //-------------- Ans Key -----------------------------
                    JSONObject ans_arrayJSONObj = new JSONObject(array_text);
                    Iterator<String> ans_arrayiterator = ans_arrayJSONObj.keys();
                    while (ans_arrayiterator.hasNext()) {
                        String ans_key = ans_arrayiterator.next();
                        String ans_value_of_key = ans_arrayJSONObj.optString(ans_key);

                        System.out.println("ans_key------------------" + ans_key);
                        System.out.println("ans_value_of_key------------------" + ans_value_of_key);

                        extra_ans_map.put(ans_key, ans_value_of_key);
                    }
                    //-------------- Ans Key -----------------------------

                    //------------ Get Array -----------------------------------
                    long elapsedTime = System.currentTimeMillis() - startTime;
                    System.out.println("Total elapsed http request/response time in milliseconds: " + elapsedTime);

                    try {

                        //----------- Flurry -------------------------------------------------
                        Map<String, String> articleParams = new HashMap<String, String>();
                        articleParams.put("android.doc.qid", "" + current_qid);
                        articleParams.put("android.doc.userid", "" + (Model.id));
                        articleParams.put("android.doc.milliseconds", "" + elapsedTime);
                        FlurryAgent.logEvent("android.doc.Query_View", articleParams);
                        //----------- Flurry -------------------------------------------------

                    } catch (Exception er) {
                        er.printStackTrace();
                    }


                  /*  if (jsonobj.has("prescribe")) {
                        prescribe_status = jsonobj.getString("prescribe");
                        if (prescribe_status.equals("1")) {
                            extra_ans_layout.setVisibility(View.GONE);
                            prescribe_layout.setVisibility(View.VISIBLE);
                        }
                        if (prescribe_status.equals("0")) {
                            extra_ans_layout.setVisibility(View.VISIBLE);
                            prescribe_layout.setVisibility(View.GONE);
                        }

                    } else {
                        prescribe_status = "0";
                    }*/


                    if (qitems.length() > 2) {

                        try {

                            JSONArray jarray = jsonobj.getJSONArray("items");
                            System.out.println("Items jarray.length------" + jarray.length());

                            for (int i = 0; i < jarray.length(); i++) {

                                jsonobj_items = jarray.getJSONObject(i);
                                System.out.println("jsonobj_Items------" + jsonobj_items.toString());

                                class_text = jsonobj_items.getString("class");


                                if (jsonobj_items.has("enable_prescription")) {
                                    enable_prescription_val = jsonobj_items.getString("enable_prescription");
                                    has_upload_file_val = jsonobj_items.getString("has_upload_file");

                                    System.out.println("enable_prescription_val------------" + enable_prescription_val);
                                    System.out.println("has_upload_file_val------------" + has_upload_file_val);

                                    //----------------------------------------------
                                    if (enable_prescription_val.equals("1")) {
                                        pres_wrie_layout.setVisibility(View.VISIBLE);
                                        prescription_title.setVisibility(View.VISIBLE);

                                        //--------------------------------------------------
                                        if (has_upload_file_val.equals("1")) {
                                            btn_upload_pres.setVisibility(View.VISIBLE);
                                        } else {
                                            btn_upload_pres.setVisibility(View.GONE);
                                        }
                                        //--------------------------------------------------

                                    } else {
                                        pres_wrie_layout.setVisibility(View.GONE);
                                        prescription_title.setVisibility(View.GONE);
                                    }
                                    //-------------------------------------------------------------------------------

                                } else {
                                    //enable_prescription_val = "0";
                                }

                                //-------------------------------------------------------------------------------
/*                              p_status_val = jsonobj.getString("p_status");
                                prescMsg_text = jsonobj.getString("prescMsg");*/
                                //-------------------------------------------------------------------------------

                                System.out.println("class_text------" + class_text);

                                if (class_text.equals("bubbledRight")) {

                                    if (new Detector().isTablet(getApplicationContext())) {
                                        vi = getLayoutInflater().inflate(R.layout.query_thread_view, null);
                                    } else {
                                        vi = getLayoutInflater().inflate(R.layout.query_thread_view, null);
                                    }

                                    query_display_layout = vi.findViewById(R.id.query_display_layout);
                                    query_webview = vi.findViewById(R.id.query_webview);
                                    expand_layout = vi.findViewById(R.id.expand_layout);

                                    extra_hwlayout = vi.findViewById(R.id.extra_hwlayout);
                                    extra_hw_details = vi.findViewById(R.id.extra_hw_details);
                                    extra_hw_title = vi.findViewById(R.id.extra_hw_title);
                                    prediction_layout = vi.findViewById(R.id.prediction_layout);
                                    prediction_inner_layout = vi.findViewById(R.id.prediction_inner_layout);

                                    extra_layout = vi.findViewById(R.id.extra_layout);
                                    tv_gender = vi.findViewById(R.id.tv_gender);
                                    ImageView img_preview = vi.findViewById(R.id.img_preview);
                                    tvattached = vi.findViewById(R.id.tvattached);
                                    files_layout = vi.findViewById(R.id.files_layout);
                                    layout_attachfile = vi.findViewById(R.id.layout_attachfile);
                                    tv_pat_name = vi.findViewById(R.id.tv_pat_name);
                                    tv_pat_place = vi.findViewById(R.id.tv_pat_place);
                                    tv_query = vi.findViewById(R.id.tvquery);
                                    tvt_morecomp = vi.findViewById(R.id.tvt_morecomp);
                                    tv_morecomp = vi.findViewById(R.id.tv_morecomp);
                                    tvt_prevhist = vi.findViewById(R.id.tvt_prevhist);
                                    tv_prevhist = vi.findViewById(R.id.tv_prevhist);
                                    tvt_curmedi = vi.findViewById(R.id.tvt_curmedi);
                                    tv_curmedi = vi.findViewById(R.id.tv_curmedi);
                                    tvt_pastmedi = vi.findViewById(R.id.tvt_pastmedi);
                                    tv_pastmedi = vi.findViewById(R.id.tv_pastmedi);
                                    tvt_labtest = vi.findViewById(R.id.tvt_labtest);
                                    tv_labtest = vi.findViewById(R.id.tv_labtest);
                                    tv_datetime = vi.findViewById(R.id.tv_datetime);
                                    tv_filename = vi.findViewById(R.id.tv_filename);
                                    tv_qid = vi.findViewById(R.id.tv_qid);

                                    font_reg = Typeface.createFromAsset(getAssets(), Model.font_name);
                                    font_bold = Typeface.createFromAsset(getAssets(), Model.font_name_bold);

                                    tv_pat_name.setTypeface(font_bold);
                                    tv_pat_place.setTypeface(font_reg);
                                    //tv_gender.setTypeface(font_reg);

                                    tv_query.setTypeface(font_reg);

                                    layout_attachfile.setVisibility(View.GONE);


                                    tv_qid_val = jsonobj_items.getString("qid");
                                    msg_text = jsonobj_items.getString("msg");
                                    msg_ext_text = jsonobj_items.getString("msg_ext");
                                    files_text = jsonobj_items.getString("files");
                                    class_text = jsonobj_items.getString("class");
                                    time_text = jsonobj_items.getString("time");

                                    if (jsonobj_items.has("strHtml")) {
                                        strHtml_text = jsonobj_items.getString("strHtml");

                                        query_display_layout.setVisibility(View.GONE);
                                        query_webview.setVisibility(View.VISIBLE);

                                        query_webview.getSettings().setJavaScriptEnabled(true);
                                        query_webview.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NORMAL);
                                        query_webview.loadDataWithBaseURL("", strHtml_text, "text/html", "UTF-8", "");
                                    } else {

                                        query_display_layout.setVisibility(View.VISIBLE);
                                        query_webview.setVisibility(View.GONE);
                                    }


                                    // strHtml_text = jsonobj_items.getString("strHtml");

                                  /*  //--------------------------------------------------------
                                    query_webview.getSettings().setJavaScriptEnabled(true);
                                    query_webview.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NORMAL);
                                    query_webview.loadData(strHtml_text, "text/html; charset=utf-8", "UTF-8");
                                    //--------------------------------------------------------
*/
                                    //----------------diagnose_prediction_text-----------------------------------------
                                    System.out.println("diagnose_prediction_text-------------" + diagnose_prediction_text);

                                    try {
                                        if (diagnose_prediction_text.length() > 5) {

                                            prediction_layout.setVisibility(View.VISIBLE);

                                            JSONArray jpred_array = new JSONArray(diagnose_prediction_text);
                                            System.out.println("Items jarray.length------" + jpred_array.length());

                                            for (int p = 0; p < jpred_array.length(); p++) {
                                                jsonobj_items = jpred_array.getJSONObject(p);
                                                System.out.println("Pred jsonobj_Items------" + jsonobj_items.toString());

                                                String pred_name_text = jsonobj_items.getString("name");
                                                String prof_name_text = jsonobj_items.getString("prof_name");
                                                String icd_text = jsonobj_items.getString("icd");
                                                String icd_name_text = jsonobj_items.getString("icd_name");
                                                String rank_text = jsonobj_items.getString("rank");

                                                pred_vi = getLayoutInflater().inflate(R.layout.prediction_row, null);
                                                tv_pred_slno = pred_vi.findViewById(R.id.tv_pred_slno);
                                                tv_pred_name = pred_vi.findViewById(R.id.tv_pred_name);
                                                tv_pred_profname = pred_vi.findViewById(R.id.tv_pred_profname);
                                                tv_pred_icd = pred_vi.findViewById(R.id.tv_pred_icd);
                                                tv_pred_rank = pred_vi.findViewById(R.id.tv_pred_rank);

                                                tv_pred_slno.setText("" + (p + 1) + ". ");
                                                tv_pred_name.setText(pred_name_text);
                                                tv_pred_profname.setText("(" + prof_name_text + ")");
                                                tv_pred_icd.setText(icd_text + " " + icd_name_text);
                                                tv_pred_rank.setText("Rank : " + rank_text);

                                                prediction_inner_layout.addView(pred_vi);

                                            }
                                        } else {
                                            prediction_layout.setVisibility(View.GONE);
                                            System.out.println("No Prediction--------------------");
                                        }

                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                    //----------------diagnose_prediction_text-----------------------------------------

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
                                                tv_gender.setText(Html.fromHtml("<b>Age: </b>" + age_txt + ", <b>Gender: </b>" + gender_txt));
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
                                                                    tv_keytext = vi_hw.findViewById(R.id.tv_keytext);
                                                                    tv_valuetext = vi_hw.findViewById(R.id.tv_valuetext);

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
                                    }
                                    //-------------------------- Age Gender ---------------------------------------------

                                    try {
                                        System.out.println("msg_text------" + msg_text);
                                        System.out.println("msg_ext_text------" + msg_ext_text);
                                        System.out.println("files_text------" + files_text);
                                        System.out.println("class_text------" + class_text);
                                        System.out.println("time_text------" + time_text);
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }

                                    noto_reg = Typeface.createFromAsset(getAssets(), Model.font_name);
                                    noto_bold = Typeface.createFromAsset(getAssets(), Model.font_name_bold);

                                    Typeface robo_reg = Typeface.createFromAsset(getAssets(), Model.font_name);
                                    Typeface noto_bold = Typeface.createFromAsset(getAssets(), Model.font_name_bold);

                                    tv_query.setTypeface(noto_reg);
                                    tv_pat_name.setTypeface(noto_bold);
                                    tv_pat_place.setTypeface(robo_reg);
                                    tv_datetime.setTypeface(noto_reg);

                                    tv_query.setAutoLinkMask(Linkify.ALL);
                                    tv_query.setText(Html.fromHtml(msg_text));
                                    tv_datetime.setText(time_text);

                                    if (qansby != null && !qansby.isEmpty() && !qansby.equals("") && !qansby.equals("null")) {
                                        tv_pat_name.setText(Html.fromHtml("<b>" + qansby + "</b>"));
                                    } else {
                                        tv_pat_name.setVisibility(View.GONE);
                                    }

                                    if (pat_from_text != null && !pat_from_text.isEmpty() && !pat_from_text.equals("") && !pat_from_text.equals("null")) {
                                        tv_pat_place.setText(pat_from_text);
                                    } else {
                                        tv_pat_place.setVisibility(View.GONE);
                                    }


                                    if ((msg_ext_text.length()) > 2) {

                                        //-------------- Dynamic Key -----------------------------
                                        JSONObject categoryJSONObj = new JSONObject(msg_ext_text);
                                        System.out.println("question_ext-----" + msg_ext_text);

                                        Iterator<String> iterator = categoryJSONObj.keys();
                                        while (iterator.hasNext()) {
                                            String key = iterator.next();
                                            String value_of_key = categoryJSONObj.optString(key);

                                            //Log.i("TAG", "key:" + key + "--Value::" + categoryJSONObj.optString(key));
                                            System.out.println("Key------------------" + key);
                                            System.out.println("Value------------------" + value_of_key);

                                            if (!value_of_key.equals("")) {

                                                vi_ext = getLayoutInflater().inflate(R.layout.extra_answer_row, null);
                                                tv_ext_title = vi_ext.findViewById(R.id.tv_ext_title);
                                                tv_ext_desc = vi_ext.findViewById(R.id.tv_ext_desc);

                                                String GET_KEY = extra_query_map.get(key);
                                                System.out.println("keykeykey---------------" + key);
                                                System.out.println("GET_KEY---------------" + GET_KEY);


                                                tv_ext_title.setText(GET_KEY);
                                                tv_ext_desc.setText(Html.fromHtml(categoryJSONObj.optString(key)));

                                                tv_ext_desc.setMovementMethod(LinkMovementMethod.getInstance());

                                                extra_layout.addView(vi_ext);
                                            }
                                        }
                                    }

                                   /* String query_id = jsonobj_items.getString("qid");
                                    System.out.println("query_id------------" + query_id);*/


                                    //---------------- Files ---------------------------------------
                                    if ((files_text.length()) > 2) {

                                        layout_attachfile.setVisibility(View.VISIBLE);

                                        System.out.println("files_text------" + files_text);
                                        JSONArray jarray_files = jsonobj_items.getJSONArray("files");
                                        System.out.println("jsonobj_items------" + jarray_files.toString());
                                        System.out.println("jarray_files.length()------" + jarray_files.length());

                                        tvattached.setText("Attachment:   " + jarray_files.length() + " File(s)");

                                        html_file_str = "";
                                        attach_file_text = "";

                                        for (int j = 0; j < jarray_files.length(); j++) {
                                            jsonobj_files = jarray_files.getJSONObject(j);

                                            System.out.println("jsonobj_files------" + j + " ----" + jsonobj_files.toString());

                                            file_user_id = jsonobj_files.getString("user_id");
                                            file_doctype = jsonobj_files.getString("doctype");
                                            file_file = jsonobj_files.getString("file");
                                            file_ext = jsonobj_files.getString("ext");
                                            file_full_url = jsonobj_files.getString("url");

                                            //------------------------ File Attached Text --------------------------------
                                            if (attach_file_text.equals("")) {
                                                attach_file_text = file_full_url;
                                                System.out.println("attach_file_text-------" + attach_file_text);
                                            } else {
                                                attach_file_text = attach_file_text + "###" + file_full_url;
                                                System.out.println("attach_file_text-------" + attach_file_text);
                                            }
                                            //------------------------ File Attached Text --------------------------------

                                            System.out.println("file_user_id--------" + file_user_id);
                                            System.out.println("file_doctype--------" + file_doctype);
                                            System.out.println("filename--------" + file_file);
                                            System.out.println("file_ext--------" + file_ext);

                                        }

                                        files_layout.setVisibility(View.GONE);
                                        //tv_filename.setText(attach_file_text);
                                        tv_filename.setText(files_text);
                                        tv_qid.setText(tv_qid_val);

                                    } else {
                                        layout_attachfile.setVisibility(View.GONE);
                                    }
                                    //---------------- Files---------------------------------------

                                    myLayout.addView(vi);
                                }

                                //-------------------------- Bubble Left --------------------------------------------------
                                if (class_text.equals("bubbledLeft")) {

                                    if (new Detector().isTablet(getApplicationContext())) {
                                        vi_ans = getLayoutInflater().inflate(R.layout.query_thread_view_answer, null);
                                    } else {
                                        vi_ans = getLayoutInflater().inflate(R.layout.query_thread_view_answer, null);
                                    }


                                    webview_answer = vi_ans.findViewById(R.id.webview_answer);
                                    answer_display_layout = vi_ans.findViewById(R.id.answer_display_layout);

                                    Button btn_view_prescription = (Button) vi_ans.findViewById(R.id.btn_view_prescription);
                                    TextView tv_cuurent_query_id = (TextView) vi_ans.findViewById(R.id.tv_cuurent_query_id);
                                    LinearLayout prescribe_view_layout = (LinearLayout) vi_ans.findViewById(R.id.prescribe_view_layout);


                                    final TextView tvattached = vi_ans.findViewById(R.id.tvattached);
                                    tv_answer_filename = vi_ans.findViewById(R.id.tv_answer_filename);
                                    ans_extra_layout = vi_ans.findViewById(R.id.ans_extra_layout);
                                    tv_answer = vi_ans.findViewById(R.id.tvanswer);
                                    tvt_probcause = vi_ans.findViewById(R.id.tvt_probcause);
                                    tv_probcause = vi_ans.findViewById(R.id.tv_probcause);
                                    tvt_invdone = vi_ans.findViewById(R.id.tvt_invdone);
                                    tv_invdone = vi_ans.findViewById(R.id.tv_invdone);
                                    tvt_diffdiag = vi_ans.findViewById(R.id.tvt_diffdiag);
                                    tv_diffdiag = vi_ans.findViewById(R.id.tv_diffdiag);
                                    tvt_probdiag = vi_ans.findViewById(R.id.tvt_probdiag);
                                    tv_probdiag = vi_ans.findViewById(R.id.tv_probdiag);
                                    tvt_tratplan = vi_ans.findViewById(R.id.tvt_tratplan);
                                    tv_tratplan = vi_ans.findViewById(R.id.tv_tratplan);
                                    tvt_prevmeasure = vi_ans.findViewById(R.id.tvt_prevmeasure);
                                    tv_prevmeasure = vi_ans.findViewById(R.id.tv_prevmeasure);
                                    tvt_follup = vi_ans.findViewById(R.id.tvt_follup);
                                    tv_follup = vi_ans.findViewById(R.id.tv_follup);
                                    tv_datetimeans = vi_ans.findViewById(R.id.tv_datetime);
                                    tv_patfeedback = vi_ans.findViewById(R.id.tv_patfeedback);
                                    feedback_section = vi_ans.findViewById(R.id.feedback_section);
                                    tv_replytext = vi_ans.findViewById(R.id.tv_replytext);
                                    doctor_reply_section = vi_ans.findViewById(R.id.doctor_reply_section);
                                    btn_feedbacksubmit = vi_ans.findViewById(R.id.btn_feedbacksubmit);
                                    tv_pres_comment = vi_ans.findViewById(R.id.tv_pres_comment);
                                    Button btn_write_pres_answer = vi_ans.findViewById(R.id.btn_write_pres_answer);
                                    TextView tv_answer_query_id = vi_ans.findViewById(R.id.tv_answer_query_id);

                                    answer_layout_attachfile = vi_ans.findViewById(R.id.answer_layout_attachfile);
                                    answer_files_layout = vi_ans.findViewById(R.id.answer_files_layout);
                                    ratting_layout = vi_ans.findViewById(R.id.ratting_layout);


                                    RatingBar ratingBar1 = vi_ans.findViewById(R.id.ratingBar1);
                                    RatingBar ratingBar2 = vi_ans.findViewById(R.id.ratingBar2);
                                    RatingBar ratingBar3 = vi_ans.findViewById(R.id.ratingBar3);
                                    RatingBar ratingBar4 = vi_ans.findViewById(R.id.ratingBar4);

                                    android.widget.TextView text_ratting1 = vi_ans.findViewById(R.id.text_ratting1);
                                    android.widget.TextView text_ratting2 = vi_ans.findViewById(R.id.text_ratting2);
                                    android.widget.TextView text_ratting3 = vi_ans.findViewById(R.id.text_ratting3);
                                    android.widget.TextView text_ratting4 = vi_ans.findViewById(R.id.text_ratting4);


                                    final String q_id_text = jsonobj_items.getString("q_id");
                                    msg_text = jsonobj_items.getString("msg");
                                    msg_ext_text = jsonobj_items.getString("msg_ext");
                                    class_text = jsonobj_items.getString("class");
                                    regards = jsonobj_items.getString("regards");
                                    time_text = jsonobj_items.getString("time");

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
                                    }
                                    else{
                                        btn_write_pres_answer.setVisibility(View.GONE);
                                    }
                                    //-------------------------------------------------------------------------------

                                    System.out.println("p_status_val----------" + p_status_val);
                                    System.out.println("prescMsg_text----------" + prescMsg_text);
                                    System.out.println("enable_prescription_val----------" + enable_prescription_val);


                                    /*if (enable_prescription_val.equals("1")) {
                                        //btn_write_pres_answer.setVisibility(View.VISIBLE);
                                        btn_write_pres_answer.setVisibility(View.GONE);
                                    } else {
                                        btn_write_pres_answer.setVisibility(View.GONE);
                                    }*/



                                    /*if (p_status_val.equals("pending_review")) {
                                        btn_write_pres_answer.setVisibility(View.GONE);
                                        tv_pres_comment.setVisibility(View.VISIBLE);
                                        tv_pres_comment.setText(prescMsg_text);

                                    } else if (p_status_val.equals("rejected")) {
                                        btn_write_pres_answer.setVisibility(View.VISIBLE);
                                        btn_write_pres_answer.setText("Edit Prescription");
                                        tv_pres_comment.setVisibility(View.VISIBLE);
                                        tv_pres_comment.setText(prescMsg_text);

                                    } else {
                                        //btn_write_pres_answer.setVisibility(View.GONE);
                                        tv_pres_comment.setVisibility(View.GONE);
                                        tv_pres_comment.setText("");

                                        if (enable_prescription_val.equals("1")) {
                                            btn_write_pres_answer.setVisibility(View.VISIBLE);
                                        } else {
                                            btn_write_pres_answer.setVisibility(View.GONE);
                                        }

                                    }
                                    //----------------------------------------------*/


                                    btn_write_pres_answer.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {

                                            View parent = (View) v.getParent();
                                            TextView tvqid = (TextView) parent.findViewById(R.id.tv_answer_query_id);
                                            String qid_val = tvqid.getText().toString();

                                            System.out.println("qid_val---------" + qid_val);

                                            Intent intent = new Intent(NewQueryViewActivity.this, Prescription_home.class);
                                            intent.putExtra("add_type", "new");
                                            intent.putExtra("cur_qid", qid_val);
                                            intent.putExtra("p_type", "query");
                                            startActivity(intent);

                                        }
                                    });


                                    //final String has_prescription_val = jsonobj_items.getString("has_prescription");

                                    tv_cuurent_query_id.setText(q_id_text);
                                    tv_answer_query_id.setText(q_id_text);


                                    /*//----------------------------------------------
                                    if (jsonobj_items.has("has_prescription")) {

                                        String has_prescription_val = jsonobj_items.getString("has_prescription");
                                        System.out.println("has_prescription_val---------" + has_prescription_val);

                                        if (has_prescription_val.equals("1")) {
                                            prescribe_view_layout.setVisibility(View.VISIBLE);
                                        } else {
                                            prescribe_view_layout.setVisibility(View.GONE);
                                        }
                                    } else {
                                        prescribe_view_layout.setVisibility(View.GONE);
                                    }
                                    //----------------------------------------------*/

                                  /*  if (has_prescription_val.equals("1")) {
                                        btn_view_prescription.setVisibility(View.VISIBLE);
                                    } else {
                                        btn_view_prescription.setVisibility(View.GONE);
                                    }*/

/*
                                    if (jsonobj_items.has("answer_id")) {
                                        answer_id_val = jsonobj_items.getString("answer_id");
                                    }
*/


                                    //-------------------------
                                    if (jsonobj_items.has("answer_id")) {
                                        current_answer_id = jsonobj_items.getString("answer_id");
                                    } else {
                                        current_answer_id = "0";
                                    }
                                    //-------------------------

                                    answer_files_text = jsonobj_items.getString("files");
                                    System.out.println("answer_files_text_Getting--------" + answer_files_text);

                                    if (jsonobj_items.has("strHtml")) {
                                        strHtml_text = jsonobj_items.getString("strHtml");

                                        answer_display_layout.setVisibility(View.GONE);
                                        webview_answer.setVisibility(View.VISIBLE);

                                        webview_answer.getSettings().setJavaScriptEnabled(true);
                                        webview_answer.setBackgroundColor(Color.TRANSPARENT);
                                        webview_answer.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NORMAL);
                                        webview_answer.loadDataWithBaseURL("", strHtml_text, "text/html", "UTF-8", "");

                                    } else {

                                        answer_display_layout.setVisibility(View.VISIBLE);
                                        webview_answer.setVisibility(View.GONE);
                                    }


                                    arr_feedback_text = jsonobj_items.getString("arr_feedback");

                                    System.out.println("arr_feedback_text---------" + arr_feedback_text);


                                    //--------------- patient Feedback ------------------------------
                                    if (jsonobj_items.has("arr_feedback")) {
                                        System.out.println("Yes JSOn Array--------------------------------");
                                        arr_feedback_text = jsonobj_items.getString("arr_feedback");

                                        if (arr_feedback_text.length() > 2) {

                                            feedback_section.setVisibility(View.VISIBLE);
                                            JSONObject jobject = new JSONObject(arr_feedback_text);
                                            System.out.println("arr_feedback_text---------" + arr_feedback_text);

                                            rating_text = jobject.getString("rating");
                                            pat_feedback_text = jobject.getString("feedback");
                                            feedback_id_text = jobject.getString("feedback_id");
                                            reply_text = jobject.getString("reply");


                                            if (jobject.has("is_star")) {

                                                String is_star_val = jobject.getString("is_star");

                                                if (is_star_val.equals("1")) {

                                                    ratting_layout.setVisibility(View.VISIBLE);

                                                    //---------- Ratting Star Text---------------------------
                                                    String star1_text = jobject.getString("star1");

                                                    if (star1_text != null && !star1_text.isEmpty() && !star1_text.equals("null") && !star1_text.equals("")) {

                                                        text_ratting1.setVisibility(View.VISIBLE);
                                                        ratingBar1.setVisibility(View.VISIBLE);

                                                        JSONObject json_star1 = new JSONObject(star1_text);
                                                        String star_title = json_star1.getString("title");
                                                        String star_value = json_star1.getString("value");

                                                        ratingBar1.setRating(Float.parseFloat(star_value));
                                                        text_ratting1.setText(star_title);
                                                    } else {
                                                        text_ratting1.setVisibility(View.GONE);
                                                        ratingBar1.setVisibility(View.GONE);
                                                    }
                                                    //---------------------------------------------------

                                                    //---------- Ratting Star Text---------------------------
                                                    String star2_text = jobject.getString("star2");

                                                    if (star2_text != null && !star2_text.isEmpty() && !star2_text.equals("null") && !star2_text.equals("")) {

                                                        text_ratting2.setVisibility(View.VISIBLE);
                                                        ratingBar2.setVisibility(View.VISIBLE);

                                                        JSONObject json_star2 = new JSONObject(star2_text);
                                                        String star_title = json_star2.getString("title");
                                                        String star_value = json_star2.getString("value");

                                                        ratingBar2.setRating(Float.parseFloat(star_value));
                                                        text_ratting2.setText(star_title);
                                                    } else {
                                                        text_ratting2.setVisibility(View.GONE);
                                                        ratingBar2.setVisibility(View.GONE);
                                                    }
                                                    //---------------------------------------------------

                                                    //---------- Ratting Star Text---------------------------
                                                    String star3_text = jobject.getString("star3");

                                                    if (star3_text != null && !star3_text.isEmpty() && !star3_text.equals("null") && !star3_text.equals("")) {

                                                        text_ratting3.setVisibility(View.VISIBLE);
                                                        ratingBar3.setVisibility(View.VISIBLE);

                                                        JSONObject json_star3 = new JSONObject(star3_text);
                                                        String star_title = json_star3.getString("title");
                                                        String star_value = json_star3.getString("value");

                                                        ratingBar3.setRating(Float.parseFloat(star_value));
                                                        text_ratting3.setText(star_title);
                                                    } else {
                                                        text_ratting3.setVisibility(View.GONE);
                                                        ratingBar3.setVisibility(View.GONE);
                                                    }
                                                    //---------------------------------------------------

                                                    //---------- Ratting Star Text---------------------------
                                                    String star4_text = jobject.getString("star4");

                                                    if (star4_text != null && !star4_text.isEmpty() && !star4_text.equals("null") && !star4_text.equals("") && star4_text.length() > 2) {

                                                        text_ratting4.setVisibility(View.VISIBLE);
                                                        ratingBar4.setVisibility(View.VISIBLE);

                                                        JSONObject json_star4 = new JSONObject(star4_text);
                                                        String star_title = json_star4.getString("title");
                                                        String star_value = json_star4.getString("value");

                                                        ratingBar4.setRating(Float.parseFloat(star_value));
                                                        text_ratting4.setText(star_title);
                                                    } else {
                                                        text_ratting4.setVisibility(View.GONE);
                                                        ratingBar4.setVisibility(View.GONE);
                                                    }
                                                    //---------------------------------------------------


                                                } else {
                                                    ratting_layout.setVisibility(View.GONE);
                                                }

                                            } else {
                                                ratting_layout.setVisibility(View.GONE);
                                            }


                                            tv_patfeedback.setText(pat_feedback_text);

                                            if (reply_text != null && !reply_text.isEmpty() && !reply_text.equals("null") && !reply_text.equals("")) {
                                                tv_replytext.setText(reply_text);
                                                doctor_reply_section.setVisibility(View.VISIBLE);
                                                btn_feedbacksubmit.setVisibility(View.GONE);
                                            } else {
                                                doctor_reply_section.setVisibility(View.GONE);
                                                btn_feedbacksubmit.setVisibility(View.VISIBLE);
                                            }

                                            feedback_section.setVisibility(View.VISIBLE);

                                        } else {
                                            feedback_section.setVisibility(View.GONE);
                                        }

                                    } else {
                                        System.out.println("No JSOn Array--------------------------------");
                                        feedback_section.setVisibility(View.GONE);
                                    }
                                    //--------------- patient Feedback ------------------------------


                                    btn_feedbacksubmit.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            showFeedbackDialog();
                                        }
                                    });


                                    font_reg = Typeface.createFromAsset(getAssets(), Model.font_name);
                                    font_bold = Typeface.createFromAsset(getAssets(), Model.font_name_bold);

                                    tv_answer.setTypeface(font_reg);

                                    tv_answer.setAutoLinkMask(Linkify.ALL);

                                    tv_answer.setText(Html.fromHtml(msg_text));
                                    tv_datetimeans.setText(time_text);

                                    System.out.println("msg_text------" + msg_text);
                                    System.out.println("msg_ext_text------" + msg_ext_text);
                                    System.out.println("class_text------" + class_text);
                                    System.out.println("class_text------" + class_text);
                                    System.out.println("time_text------" + time_text);

                                    //---------------- Msg Extra ---------------------------------------
                                    if ((msg_ext_text.length()) > 2) {


                                        ans_extra_layout.removeAllViews();
                                        //-------------- Dynamic Key -----------------------------
                                        JSONObject categoryJSONObj = new JSONObject(msg_ext_text);
                                        Iterator<String> iterator = categoryJSONObj.keys();
                                        while (iterator.hasNext()) {
                                            String key = iterator.next();
                                            String value_of_key = categoryJSONObj.optString(key);

                                            System.out.println("Key------------------" + key);
                                            System.out.println("Value------------------" + value_of_key);

                                            if (!value_of_key.equals("")) {

                                                vi_ans_ext = getLayoutInflater().inflate(R.layout.extra_answer_row, null);
                                                TextView tv_ext_title = vi_ans_ext.findViewById(R.id.tv_ext_title);
                                                tv_ext_desc = vi_ans_ext.findViewById(R.id.tv_ext_desc);

                                                tv_ext_title.setText(extra_ans_map.get(key));
                                                tv_ext_desc.setText(Html.fromHtml(categoryJSONObj.optString(key)));
                                                tv_ext_desc.setMovementMethod(LinkMovementMethod.getInstance());


                                                ans_extra_layout.addView(vi_ans_ext);
                                            }

                                        }
                                        //-------------- Dynamic Key -----------------------------

                                    }


                                    //---------------- Files ---------------------------------------
                                    System.out.println("answer_files_text$$$$$$$$$$$" + answer_files_text);

                                    if ((answer_files_text.length()) > 2) {

                                        answer_layout_attachfile.setVisibility(View.VISIBLE);

                                        System.out.println("answer_files_text------" + answer_files_text);

                                        JSONArray jarray_files = jsonobj_items.getJSONArray("files");

                                        System.out.println("jsonobj_items------" + jarray_files.toString());
                                        System.out.println("jarray_files.length()------" + jarray_files.length());

                                        tvattached.setText("Attachment:   " + jarray_files.length() + " File(s)");

                                        html_file_str = "";
                                        attach_file_text = "";

                                        for (int j = 0; j < jarray_files.length(); j++) {
                                            jsonobj_files = jarray_files.getJSONObject(j);

                                            System.out.println("jsonobj_files------" + j + " ----" + jsonobj_files.toString());

                                            file_user_id = jsonobj_files.getString("user_id");
                                            file_doctype = jsonobj_files.getString("doctype");
                                            file_file = jsonobj_files.getString("file");
                                            file_ext = jsonobj_files.getString("ext");
                                            file_full_url = jsonobj_files.getString("url");

                                            //------------------------ File Attached Text --------------------------------
                                            if (attach_file_text.equals("")) {
                                                attach_file_text = file_full_url;
                                                System.out.println("attach_file_text-------" + attach_file_text);
                                            } else {
                                                attach_file_text = attach_file_text + "###" + file_full_url;
                                                System.out.println("attach_file_text-------" + attach_file_text);
                                            }
                                            //------------------------ File Attached Text --------------------------------

                                            System.out.println("file_user_id--------" + file_user_id);
                                            System.out.println("file_doctype--------" + file_doctype);
                                            System.out.println("filename--------" + file_file);
                                            System.out.println("file_ext--------" + file_ext);

                                        }

                                        answer_files_layout.setVisibility(View.GONE);
                                        //tv_filename.setText(attach_file_text);

                                        tv_answer_filename.setText(answer_files_text);

                                    } else {
                                        answer_layout_attachfile.setVisibility(View.GONE);
                                    }
                                    //---------------- Files---------------------------------------

/*                                    btn_view_prescription.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            Intent i = new Intent(NewQueryViewActivity.this, Prescriptions_Activity.class);
                                            i.putExtra("qid", q_id_text);
                                            i.putExtra("patient_id", patient_id);
                                            i.putExtra("ptype", "view");
                                            startActivity(i);
                                        }
                                    });*/

                                    myLayout.addView(vi_ans);


                                }
                                //---------------- Files-------------------------------------------------------------
                            }

                          /*  if (enable_prescription_val.equals("1")) {
                                prescribe_layout.setVisibility(View.VISIBLE);
                            } else {
                                prescribe_layout.setVisibility(View.GONE);
                            }*/

                        } catch (Exception e) {

                            System.out.println("Exception---1---" + e.toString());
                            e.printStackTrace();

                            nolayout.setVisibility(View.VISIBLE);
                            scrollview.setVisibility(View.GONE);
                            progressBar.setVisibility(View.GONE);
                            netcheck_layout.setVisibility(View.GONE);
                            button_layout.setVisibility(View.GONE);
                            btn_ansquery.setVisibility(View.GONE);

                        }
                    } else {

                        nolayout.setVisibility(View.VISIBLE);
                        scrollview.setVisibility(View.GONE);
                        progressBar.setVisibility(View.GONE);
                        netcheck_layout.setVisibility(View.GONE);
                        button_layout.setVisibility(View.GONE);
                        btn_ansquery.setVisibility(View.GONE);
                    }


                    //------------------ Fetch Ad ----------------------------------------------
                    if ((Model.browser_country).equals("IN")) {
                        //--------------------Ad---------
                        Model.home_ad_flag = "false";
                        String ad_url = Model.BASE_URL + "/sapp/fetchAd?token=" + Model.token + "&user_id=" + Model.id + "&browser_country=" + Model.browser_country + "&qid=" + current_qid + "page_src=4";
                        System.out.println("ad_url----------" + ad_url);
                        new JSON_Ad().execute(ad_url);
                        //---------------------Ad--------
                    } else {
                        System.out.println("Model.browser_country--------------" + Model.browser_country);
                    }
                    //=============================================================================
                }
            } catch (Exception e) {
                System.out.println("Exception---3---" + e.toString());
                e.printStackTrace();
                nolayout.setVisibility(View.VISIBLE);
                scrollview.setVisibility(View.GONE);
                progressBar.setVisibility(View.GONE);
                netcheck_layout.setVisibility(View.GONE);
                button_layout.setVisibility(View.GONE);
                btn_ansquery.setVisibility(View.GONE);
            }

            progressBar.setVisibility(View.GONE);
            scrollview.setVisibility(View.VISIBLE);
            nolayout.setVisibility(View.GONE);
            netcheck_layout.setVisibility(View.GONE);
            button_layout.setVisibility(View.VISIBLE);

            //scrollview.fullScroll(ScrollView.FOCUS_DOWN);

         /*   //-----------------------------------------------------
            if (qtype.equals("new_query")) {
                btn_ansquery.setVisibility(View.VISIBLE);
                System.out.println("qtype--II new q-" + qtype);
            } else if (qtype.equals("answered_query")) {
                btn_ansquery.setVisibility(View.GONE);
                System.out.println("qtype--II asnwered q-" + qtype);
            }
            //-----------------------------------------------------*/


            if (qcanianswer.equals("1")) {
                followup_layout.setVisibility(View.GONE);
                //answer_layout.setVisibility(View.VISIBLE);
                btn_ansquery.setVisibility(View.VISIBLE);

/*                      nolayout.setVisibility(View.VISIBLE);
                        scrollview.setVisibility(View.GONE);
                        progressBar.setVisibility(View.GONE);
                        netcheck_layout.setVisibility(View.GONE);
                        button_layout.setVisibility(View.GONE);
                        btn_ansquery.setVisibility(View.GONE);*/

            } else {
                followup_layout.setVisibility(View.VISIBLE);
                btn_ansquery.setVisibility(View.GONE);
                answer_layout.setVisibility(View.GONE);
            }

            long elapsedTime = System.currentTimeMillis() - startTime;
            System.out.println("Total elapsed http request/response time in milliseconds: " + elapsedTime);

            //------- Scrolling Bottom ===============================
            Runnable runnable = new Runnable() {
                @Override
                public void run() {
                    scrollview.fullScroll(ScrollView.FOCUS_DOWN);
                }
            };
            scrollview.post(runnable);
            //------- Scrolling Bottom ===============================

            scrollview.fullScroll(ScrollView.FOCUS_DOWN);
        }
    }

    class JSONPostAnswer extends AsyncTask<JSONObject, Void, Boolean> {

        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(NewQueryViewActivity.this);
            dialog.setMessage("Submitting, please wait");
            dialog.show();
            dialog.setCancelable(false);

            startTime = System.currentTimeMillis();
        }

        @Override
        protected Boolean doInBackground(JSONObject... urls) {
            try {

                JSONParser jParser = new JSONParser();
                jsonobj_postans = jParser.JSON_POST(urls[0], "submitAnswer");

                System.out.println("urls[0]---------------" + urls[0]);
                System.out.println("jsonobj_postans-------------" + jsonobj_postans.toString());

                return true;
            } catch (Exception e) {
                e.printStackTrace();
            }

            return false;
        }

        protected void onPostExecute(Boolean result) {

            try {
                //jsonobj_postans = new JSONObject(str_response);

                if (jsonobj_postans.has("token_status")) {
                    String token_status = jsonobj_postans.getString("token_status");
                    if (token_status.equals("0")) {

                        //============================================================
                        SharedPreferences.Editor editor = sharedpreferences.edit();
                        editor.putString(Login_Status, "0");
                        editor.apply();
                        //============================================================

                        finishAffinity();
                        Intent intent = new Intent(NewQueryViewActivity.this, LoginActivity.class);
                        startActivity(intent);
                        finish();
                    }

                } else {

                    ans_isvalid = jsonobj_postans.getString("isValid");
                    ans_status = jsonobj_postans.getString("status");

                    if (ans_status != null && !ans_status.isEmpty() && !ans_status.equals("null") && !ans_status.equals("")) {

                        //--------------------------------------------------------------------
                        if ((ans_status).equals("1")) {

                            long elapsedTime = System.currentTimeMillis() - startTime;
                            System.out.println("Total elapsed http request/response time in milliseconds: " + elapsedTime);


                            //----------- Flurry -------------------------------------------------
                            Map<String, String> articleParams = new HashMap<String, String>();
                            articleParams.put("Qid:", current_qid);
                            articleParams.put("User_Id:", (Model.id));
                            articleParams.put("elapsedTime:", "" + elapsedTime);
                            FlurryAgent.logEvent("android.doc.Answer_Submit_Success", articleParams);
                            //----------- Flurry -------------------------------------------------

                            alertdia("Answer Submitted Successfully");

                        } else {

                            //----------- Flurry -------------------------------------------------
                            Map<String, String> articleParams = new HashMap<String, String>();
                            articleParams.put("Qid:", current_qid);
                            articleParams.put("User_Id:", (Model.id));
                            FlurryAgent.logEvent("android.doc.Answer_Submit_error", articleParams);
                            //----------- Flurry -------------------------------------------------

                            alertdia("Submitting Answer Failed");
                        }
                        //--------------------------------------------------------------------

                    } else {
                        alertdia("Submitting Answer Failed.!");
                    }
                }

                dialog.cancel();


            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    class JSON_Draft extends AsyncTask<JSONObject, Void, Boolean> {

        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Boolean doInBackground(JSONObject... urls) {

            try {
                JSONParser jParser = new JSONParser();
                text_jsonobj = jParser.JSON_POST(urls[0], "saveqanswer");
                return true;
            } catch (Exception e) {
                e.printStackTrace();
            }
            return false;
        }

        protected void onPostExecute(Boolean result) {

        }
    }

    class JSON_notanswer extends AsyncTask<String, Void, Boolean> {
        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(NewQueryViewActivity.this);
            dialog.setMessage("Query is releasing. Please wait..");
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
                dialog.dismiss();
                finish();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    class Resume_canianswer extends AsyncTask<String, Void, Boolean> {

        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(NewQueryViewActivity.this);
            dialog.setCanceledOnTouchOutside(false);
            dialog.setTitle("Checking Query Status., please wait");
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

                if (jsonobj_canisnaswer.has("token_status")) {
                    String token_status = jsonobj_canisnaswer.getString("token_status");
                    if (token_status.equals("0")) {

                        //============================================================
                        SharedPreferences.Editor editor = sharedpreferences.edit();
                        editor.putString(Login_Status, "0");
                        editor.apply();
                        //============================================================

                        finishAffinity();
                        Intent intent = new Intent(NewQueryViewActivity.this, LoginActivity.class);
                        startActivity(intent);
                        finish();
                    }

                } else {

                    answering_status = jsonobj_canisnaswer.getString("status");
                    opt_freefollow = jsonobj_canisnaswer.getString("opt_freefollow"); // 1 or 0
                    enable_freefollow = jsonobj_canisnaswer.getString("enable_freefollow");  // on or off

                    System.out.println("answering_status---- " + answering_status);
                    System.out.println("opt_freefollow---- " + opt_freefollow);
                    System.out.println("enable_freefollow---- " + enable_freefollow);

                    try {

                        //----------- Flurry -------------------------------------------------
                        Map<String, String> articleParams = new HashMap<String, String>();
                        articleParams.put("android.doc.Qid", current_qid);
                        articleParams.put("android.doc.answering_status", answering_status);
                        articleParams.put("android.doc.check_enable_free_followup", check_enable_ffollowup);
                        FlurryAgent.logEvent("android.doc.Resume_Query_Status", articleParams);
                        //----------- Flurry -------------------------------------------------

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    if ((answering_status).equals("0")) {
                        Toast.makeText(getApplicationContext(), "Sorry! Another doctor has already picked this query.", Toast.LENGTH_LONG).show();
                        query_release_check();
                    }
                }

                dialog.cancel();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
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

                        Picasso.with(NewQueryViewActivity.this).load(ad_img_path).placeholder(R.mipmap.ad_placeholder).error(R.mipmap.ad_placeholder).into(home_ad);
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

    private class JSON_post_feedback extends AsyncTask<JSONObject, Void, Boolean> {

        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(NewQueryViewActivity.this);
            dialog.setMessage("please wait..");
            dialog.show();
            dialog.setCancelable(false);
        }

        @Override
        protected Boolean doInBackground(JSONObject... urls) {
            try {
                JSONParser jParser = new JSONParser();
                json_response_obj = jParser.JSON_POST(urls[0], "reply_to_feedback");

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
                    System.out.println("response-POSTIVE-------------");

                    tv_replytext.setText(feedback_text);
                    doctor_reply_section.setVisibility(View.VISIBLE);
                    btn_feedbacksubmit.setVisibility(View.GONE);
                } else {
                    System.out.println("response-NEGATIVE-------------");
                    //Toast.makeText(getApplicationContext(), report_response, Toast.LENGTH_LONG).show();
                }

                dialog.cancel();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private class AsyncTask_fileupload extends AsyncTask<String, Void, Boolean> {

        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            try {
                dialog = new ProgressDialog(NewQueryViewActivity.this);
                dialog.setMessage("Uploading. Please wait...");
                dialog.show();
                dialog.setCancelable(false);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        protected Boolean doInBackground(String... urls) {

            try {
                upload_response = upload_file(urls[0]); //ok
                System.out.println("upload_response---------" + upload_response);

                return true;
            } catch (Exception e) {
                e.printStackTrace();
            }

            return false;
        }

        protected void onPostExecute(Boolean result) {

            try {
                JSONObject jObj = new JSONObject(upload_response);

                attach_qid = jObj.getString("qid");
                attach_status = jObj.getString("status");
                attach_file_url = jObj.getString("url");
                attach_filename = jObj.getString("filename");
                attach_id = jObj.getString("attach_id");

                //------------ Google firebase Analitics--------------------
                Model.mFirebaseAnalytics = FirebaseAnalytics.getInstance(getApplicationContext());
                Bundle params = new Bundle();
                params.putString("attach_qid", attach_qid);
                params.putString("attach_status", attach_status);
                params.putString("attach_file_url", attach_file_url);
                params.putString("attach_filename", attach_filename);
                params.putString("attach_id", attach_id);
                Model.mFirebaseAnalytics.logEvent("AskQuery2_File_Upload", params);
                //------------ Google firebase Analitics--------------------

                System.out.println("attach_qid-------" + attach_qid);
                System.out.println("attach_status-------" + attach_status);
                System.out.println("attach_file_url-------" + attach_file_url);
                System.out.println("attach_filename-------" + attach_filename);
                System.out.println("attach_attach_id-------" + attach_id);
                System.out.println("last_upload_file--------------" + last_upload_file);

                if (!(last_upload_file).equals("")) {

                    //------------------------------------
                    LayoutInflater layoutInflater = (LayoutInflater) getBaseContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    final View addView = layoutInflater.inflate(R.layout.ans_upload_file_list, null);

                    TextView tv_quest = addView.findViewById(R.id.tv_quest);
                    ImageView close_button = addView.findViewById(R.id.close_button);
                    thumb_img = addView.findViewById(R.id.imageView4);
                    tv_attach_url = addView.findViewById(R.id.tv_attach_url);
                    tv_attach_id = addView.findViewById(R.id.tv_attach_id);

                    tv_quest.setText(last_upload_file);
                    tv_attach_id.setText(attach_id);
                    tv_attach_url.setText(attach_file_url);
                    thumb_img.setImageBitmap(BitmapFactory.decodeFile(local_url));

                    System.out.println("Model.upload_files-----------" + (last_upload_file));
                    System.out.println("Model.attach_qid-----------" + (attach_qid));
                    System.out.println("Model.attach_id-----------" + (attach_id));
                    System.out.println("Model.attach_file_url-----------" + (attach_file_url));

                    close_button.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            View parent = (View) v.getParent();
                            //View grand_parent = (View)parent.getParent();

                            tv_attach_id = parent.findViewById(R.id.tv_attach_id);
                            String attid = tv_attach_id.getText().toString();

                            //---------------------------
                            String url = Model.BASE_URL + "/sapp/removeAnsAttachment?user_id=" + (Model.id) + "&attach_id=" + attid + "&token=" + Model.token;
                            System.out.println("Remover Attach url-------------" + url);
                            new JSON_remove_file().execute(url);
                            //---------------------------

                            System.out.println("Removed attach_id-----------" + attid);
                            ((LinearLayout) addView.getParent()).removeView(addView);
                        }
                    });

                    thumb_img.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            //preview_image(local_url);
                        }
                    });

                    file_list.addView(addView);
                    //------------------------------------
                }

                last_upload_file = "";

            } catch (Exception e) {
                e.printStackTrace();
            }

            dialog.cancel();
        }
    }

    private class JSON_remove_file extends AsyncTask<String, Void, Boolean> {

        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            dialog = new ProgressDialog(NewQueryViewActivity.this);
            dialog.setMessage("Please wait..");
            dialog.show();
            dialog.setCancelable(false);
        }

        @Override
        protected Boolean doInBackground(String... urls) {

            try {

                (new JSONParser()).getJSONFromUrl(urls[0]);

                return true;
            } catch (Exception e) {
                e.printStackTrace();
            }

            return false;
        }

        protected void onPostExecute(Boolean result) {

            dialog.dismiss();

        }
    }

    class JSONPostFollowup extends AsyncTask<JSONObject, Void, Boolean> {

        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(NewQueryViewActivity.this);
            dialog.setMessage("Submitting, please wait");
            dialog.show();
            dialog.setCancelable(false);

            startTime = System.currentTimeMillis();
        }

        @Override
        protected Boolean doInBackground(JSONObject... urls) {

            try {

                JSONParser jParser = new JSONParser();
                jsonobj_postans = jParser.JSON_POST(urls[0], "SubmitFollowup");

                System.out.println("urls[0]---------------" + urls[0]);
                System.out.println("jsonobj_postans-------------" + jsonobj_postans.toString());

                return true;
            } catch (Exception e) {
                e.printStackTrace();
            }

            return false;
        }

        protected void onPostExecute(Boolean result) {

            try {

                if (jsonobj_postans.has("token_status")) {
                    String token_status = jsonobj_postans.getString("token_status");
                    if (token_status.equals("0")) {

                        //============================================================
                        SharedPreferences.Editor editor = sharedpreferences.edit();
                        editor.putString(Login_Status, "0");
                        editor.apply();
                        //============================================================

                        finishAffinity();
                        Intent intent = new Intent(NewQueryViewActivity.this, LoginActivity.class);
                        startActivity(intent);
                        finish();
                    }

                } else {

                    ans_status = jsonobj_postans.getString("status");

                    if (ans_status != null && !ans_status.isEmpty() && !ans_status.equals("null") && !ans_status.equals("")) {

                        //--------------------------------------------------------------------
                        if ((ans_status).equals("1")) {

                            long elapsedTime = System.currentTimeMillis() - startTime;
                            System.out.println("Total elapsed http request/response time in milliseconds: " + elapsedTime);

                            //----------- Flurry -------------------------------------------------
                            Map<String, String> articleParams = new HashMap<String, String>();
                            articleParams.put("Answer_id:", current_answer_id);
                            articleParams.put("User_Id:", (Model.id));
                            articleParams.put("elapsedTime:", "" + elapsedTime);
                            FlurryAgent.logEvent("android.doc.Answer_Submit_Success", articleParams);
                            //----------- Flurry -------------------------------------------------

                            alertdia("Followup Submitted Successfully");

                        } else {

                            //----------- Flurry -------------------------------------------------
                            Map<String, String> articleParams = new HashMap<String, String>();
                            articleParams.put("current_answer_id:", current_answer_id);
                            articleParams.put("User_Id:", (Model.id));
                            FlurryAgent.logEvent("android.doc.Answer_Submit_error", articleParams);
                            //----------- Flurry -------------------------------------------------

                            alertdia("Submitting Followup Failed");
                        }
                        //--------------------------------------------------------------------

                    } else {
                        alertdia("Submitting Followup Failed.!");
                    }
                }

                dialog.cancel();


            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private class list_drugs extends AsyncTask<String, Void, Void> {

        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            dialog = new ProgressDialog(NewQueryViewActivity.this);
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
                            Toast.makeText(NewQueryViewActivity.this, msg_text, Toast.LENGTH_SHORT).show();
                        }
                    }
                    dialog.dismiss();
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static void dumpIntent(Intent i) {

        Bundle bundle = i.getExtras();
        if (bundle != null) {
            Set<String> keys = bundle.keySet();
            Iterator<String> it = keys.iterator();
            while (it.hasNext()) {
                String key = it.next();
                System.out.println("Data------>" + "[" + key + "=" + bundle.get(key) + "]");
            }
        }
    }

    public static String intentToString(Intent intent) {
        if (intent == null) {
            return null;
        }

        return intent.toString() + " " + bundleToString(intent.getExtras());
    }

    public static String bundleToString(Bundle bundle) {
        StringBuilder out = new StringBuilder("Bundle[");

        if (bundle == null) {
            out.append("null");
        } else {
            boolean first = true;
            for (String key : bundle.keySet()) {
                if (!first) {
                    out.append(", ");
                }

                out.append(key).append('=');

                Object value = bundle.get(key);

                if (value instanceof int[]) {
                    out.append(Arrays.toString((int[]) value));
                } else if (value instanceof byte[]) {
                    out.append(Arrays.toString((byte[]) value));
                } else if (value instanceof boolean[]) {
                    out.append(Arrays.toString((boolean[]) value));
                } else if (value instanceof short[]) {
                    out.append(Arrays.toString((short[]) value));
                } else if (value instanceof long[]) {
                    out.append(Arrays.toString((long[]) value));
                } else if (value instanceof float[]) {
                    out.append(Arrays.toString((float[]) value));
                } else if (value instanceof double[]) {
                    out.append(Arrays.toString((double[]) value));
                } else if (value instanceof String[]) {
                    out.append(Arrays.toString((String[]) value));
                } else if (value instanceof CharSequence[]) {
                    out.append(Arrays.toString((CharSequence[]) value));
                } else if (value instanceof Parcelable[]) {
                    out.append(Arrays.toString((Parcelable[]) value));
                } else if (value instanceof Bundle) {
                    out.append(bundleToString((Bundle) value));
                } else {
                    out.append(value);
                }

                first = false;
            }
        }

        out.append("]");
        return out.toString();
    }

    private final TextWatcher edt_answerWatcher = new TextWatcher() {
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        public void onTextChanged(CharSequence s, int start, int before, int count) {

            Model.query_typed = "" + s;
            Integer slength = s.length();
            Integer rema = slength % 50;
            System.out.println("slength------------------" + slength);
            System.out.println("remainder------------------" + rema);
            if (rema == 0) {

                try {

                    draft_json = new JSONObject();
                    draft_json.put("user_id", Model.id);
                    draft_json.put("answer", s);
                    draft_json.put("question_id", current_qid);
                    draft_json.put("answered_by", (Model.id));

                    System.out.println("draft_json------------------" + draft_json);

                    new JSON_Draft().execute(draft_json);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        public void afterTextChanged(Editable s) {

        }
    };

    private String query_presc_upload_file(String file_path) {

        last_upload_file = file_path;

        String ServerUploadPath = Model.BASE_URL + "/sapp/ansUpload?user_id=" + (Model.id) + "&qid=" + (current_qid) + "&token=" + Model.token;
        System.out.println("ServerUploadPath---------- " + ServerUploadPath);

        File file_value = new File(file_path);

        try {

            HttpClient client = new DefaultHttpClient();
            HttpPost post = new HttpPost(ServerUploadPath);
            MultipartEntity2 reqEntity = new MultipartEntity2();
            reqEntity.addPart("file", file_value);
            post.setEntity(reqEntity);

            HttpResponse response = client.execute(post);
            HttpEntity resEntity = response.getEntity();

            try {
                final String response_str = EntityUtils.toString(resEntity);

                if (resEntity != null) {
                    System.out.println("response_str-------" + response_str);
                    contentAsString = response_str;

                }
            } catch (Exception ex) {
                Log.e("Debug", "error: " + ex.getMessage(), ex);
            }
        } catch (Exception e) {
            Log.e("Upload Exception", "");
            e.printStackTrace();
        }

        return contentAsString;
    }


    public void view_prescription(View v) {

        try {

            View parent = (View) v.getParent();
            TextView tv_qid = parent.findViewById(R.id.tv_write_query_id);
            String ans_qid_val = tv_qid.getText().toString();

            String params = Model.BASE_URL + "sapp/previewPrescription?user_id=" + (Model.id) + "&token=" + Model.token + "&os_type=android&item_type=consult&item_id=" + ans_qid_val;
            System.out.println("Pressed Prescription-----------" + params);
            new list_drugs().execute(params);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
