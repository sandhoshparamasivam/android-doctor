package com.orane.docassist;

import java.util.Iterator;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.webkit.WebSettings;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.flurry.android.FlurryAgent;
import com.github.ksoichiro.android.observablescrollview.ObservableWebView;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.orane.docassist.Model.Model;
import com.orane.docassist.Network.Detector;
import com.orane.docassist.Network.JSONParser;
import com.orane.docassist.attachment_view.GridViewActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import me.drakeet.materialdialog.MaterialDialog;

public class AnsweredQueryViewActivity extends AppCompatActivity {


    public static final String MyPREFERENCES = "MyPrefs";
    public static final String Login_Status = "Login_Status_key";
    public static final String user_name = "user_name_key";
    public static final String Name = "Name_key";
    public static final String password = "password_key";
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
    public static final String sp_km_id = "sp_km_id_key";
    public static final String first_query = "first_query_key";
    public static final String first_hotline = "first_hotline_key";
    public String strHtml_text, str_drug_dets, answer_files_text, attach_file_text, title_text, fields_text, qtype, query_price_text, pat_from_text, check_enable_ffollowup, answering_status, opt_freefollow, enable_freefollow, isEnablefFollowup,msg, qansby, qcanianswer, html_file_str, followupcode, qitems, regards, msg_text, msg_ext_text, files_text, class_text, time_text;
    public String str_response, ans_followup_text, file_full_url, complaint_more, p_history, c_medications, p_medications, tests, file_user_id, file_doctype, file_file, file_ext;
    public String feedback_id_text, enable_prescription_val, current_answer_id, followup_txt, report_response, feedback_text, rating_text, pat_feedback_text, reply_text, arr_feedback_text, q_comments, extension, age_txt, gender_txt, extra_txt, age_gender_txt, prescribe_value, pb_cause_text, lab_t_text, ddx_text, pdx_text, treatment_plan_text, followup_text, p_tips_text;
    public View vi_ans, vi, vi_files;
    public JSONObject json_fields, jsonobj_hwextra, text_jsonobj, draft_json, json;
    public JSONArray jarray_hw;
    public String type, ent_ans, current_qid, answer_txt, allow_answer, params, ans_isvalid, ans_status;
    Switch followup_switch;
    Button btn_notanswer;
    ObservableWebView query_webview;
    JSONObject json_response_obj, feedback_json, jsonobj_feedbacks, jsonobj_postans, jsonon_titem, json_gender, jsonobj_canisnaswer, jsonobj, jsonobj_items, jsonobj_files;
    LinearLayout answer_files_layout, answer_layout_attachfile, answer_display_layout, query_display_layout, doctor_reply_section, ans_extra_layout, extra_layout, feedback_section, main_data_layout, extra_hwlayout, extra_ans_layout, answer_layout, netcheck_layout, nolayout, layout_attachfile, myLayout, files_layout;
    EditText edt_followup;
    ObservableWebView webview_answer;
    TextView tv_answer_filename, tv_patfeedback, tv_replytext, tv_ext_title, tv_etitle, tv_valuetext, tv_keytext, tv_title, tv_extra, tv_ext, tv_userid, tv_gender, tvattached, tv_filename, tv_pat_name, tv_pat_place, tv_query, tv_morecomp, tvt_prevhist, tv_prevhist, tvt_curmedi, tv_curmedi, tvt_pastmedi, tv_pastmedi, tvt_labtest, tv_labtest, tv_datetime;

    //public HashMap<String, String> attach_files_map;
    TextView tv_answer, tv_ext_desc, tv_followtit, tvt_probcause, tv_probcause, tvt_invdone, tv_invdone, tvt_diffdiag, tv_diffdiag, tvt_probdiag, tv_probdiag, tvt_tratplan, tv_tratplan, tvt_prevmeasure, tv_prevmeasure, tvt_follup, tv_follup, tv_datetimeans;
    ScrollView scrollview;
    ImageView file_image;
    ProgressBar progressBar;
    View vi_ext;
    Typeface font_bold, font_reg;
    Button btn_reload, btn_feedbacksubmit;
    long startTime;
    Button btn_write_pres;
    View vi_hw, vi_ans_ext, vi_hw_full;
    Map<String, String> extra_ans_map = new HashMap<String, String>();
    Map<String, String> extra_query_map = new HashMap<String, String>();
    Button btn_done, btn_follow_submit;
    EditText edt_answer;
    LinearLayout extra_hw_title, extra_hw_details, ans_more_dets, show_less;
    TextView tvclear;
    EditText prescribe_text, diagnosis, pb_cause, lab_t, ddx, pdx, treatment_plan, p_tips, followup;
    Button btn_ansquery;
    RelativeLayout followup_layout;
    SharedPreferences sharedpreferences;
    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.answered_query_view_detail);

        //Model.attach_files_map = new HashMap<String, String>();

        //------------ Object Creations -------------------------------
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setTitle("");
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.app_color));
        }

        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        allow_answer = "no";

        followup_layout = (RelativeLayout) findViewById(R.id.followup_layout);
        btn_ansquery = (Button) findViewById(R.id.btn_ansquery);
        prescribe_text = (EditText) findViewById(R.id.prescribe_text);
        //prescribe_layout = (LinearLayout) findViewById(R.id.prescribe_layout);
        extra_ans_layout = (LinearLayout) findViewById(R.id.extra_ans_layout);
        btn_notanswer = (Button) findViewById(R.id.btn_notanswer);
        followup_switch = (Switch) findViewById(R.id.followup_switch);
        btn_reload = (Button) findViewById(R.id.btn_reload);
        netcheck_layout = (LinearLayout) findViewById(R.id.netcheck_layout);
        nolayout = (LinearLayout) findViewById(R.id.nolayout);
        myLayout = (LinearLayout) findViewById(R.id.parent_qalayout);
        scrollview = (ScrollView) findViewById(R.id.scrollview);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        answer_layout = (LinearLayout) findViewById(R.id.answer_layout);
        ans_more_dets = (LinearLayout) findViewById(R.id.ans_more_dets);
        diagnosis = (EditText) findViewById(R.id.diagnosis);
        pb_cause = (EditText) findViewById(R.id.pb_cause);
        lab_t = (EditText) findViewById(R.id.lab_t);
        ddx = (EditText) findViewById(R.id.ddx);
        pdx = (EditText) findViewById(R.id.pdx);
        treatment_plan = (EditText) findViewById(R.id.treatment_plan);
        p_tips = (EditText) findViewById(R.id.p_tips);
        followup = (EditText) findViewById(R.id.followup);
        btn_done = (Button) findViewById(R.id.btn_done);
        tvclear = (TextView) findViewById(R.id.tvclear);
        show_less = (LinearLayout) findViewById(R.id.show_less);
        edt_answer = (EditText) findViewById(R.id.edt_answer);
        edt_followup = (EditText) findViewById(R.id.edt_followup);
        btn_follow_submit = (Button) findViewById(R.id.btn_follow_submit);
        tv_followtit = (TextView) findViewById(R.id.tv_followtit);
        btn_write_pres = (Button) findViewById(R.id.btn_write_pres);
        //opt_ffollowup = (CheckBox) findViewById(R.id.opt_ffollowup);

        try {

            Intent intent = getIntent();
            followupcode = intent.getStringExtra("followupcode");
            query_price_text = intent.getStringExtra("query_price");
            pat_from_text = intent.getStringExtra("pat_from");
            qtype = intent.getStringExtra("qtype");

            //----------- Flurry -------------------------------------------------
            Map<String, String> articleParams = new HashMap<String, String>();
            articleParams.put("followupcode:", followupcode);
            articleParams.put("query_price_text:", query_price_text);
            articleParams.put("pat_from_text:", pat_from_text);
            articleParams.put("qtype:", qtype);
            FlurryAgent.logEvent("android.doc.Answer_Query_View", articleParams);
            //----------- Flurry -------------------------------------------------

            System.out.println("Get intent followupcode----" + followupcode);
            System.out.println("Get intent query_price_text----" + query_price_text);
            System.out.println("Get intent pat_from_text----" + pat_from_text);

        } catch (Exception e) {
            e.printStackTrace();
        }

        String locale = getResources().getConfiguration().locale.getCountry();
        System.out.println("locale---------------" + locale);


        if (isInternetOn()) {

            fullprocess();

        } else {
            progressBar.setVisibility(View.GONE);
            scrollview.setVisibility(View.GONE);
            nolayout.setVisibility(View.GONE);
            netcheck_layout.setVisibility(View.VISIBLE);
        }


        btn_follow_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (isInternetOn()) {

                    ans_followup_text = edt_followup.getText().toString();

                    if (!ans_followup_text.equals("")) {
                        submit_followup();
                    } else {
                        edt_followup.setError("Please enter your message");
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Please check your Internet Connection and try again", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btn_write_pres.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
/*                Intent i = new Intent(AnsweredQueryViewActivity.this, Prescriptions_Activity.class);
                i.putExtra("qid", current_qid);
                startActivity(i);
                //finish();*/

             /*   try {
                    String params = Model.BASE_URL + "sapp/previewPrescription?user_id=" + (Model.id) + "&token=" + Model.token + "&os_type=android&item_type=query&item_id=" + current_qid;
                    System.out.println("Prescription-----------" + params);
                    new list_drugs().execute(params);
                } catch (Exception e) {
                    e.printStackTrace();
                }*/
            }
        });


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

                }
            }
        });


    }


    public void onClick(View v) {

        try {

            View parent = (View) v.getParent();
            TextView tv_filename = (TextView) v.findViewById(R.id.tv_filename);
            String file_name = tv_filename.getText().toString();

            System.out.println("Clicked str_filename-------" + file_name);

            Intent i = new Intent(getApplicationContext(), GridViewActivity.class);
            i.putExtra("filetxt", file_name);
            startActivity(i);
            overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void onClickFileOpen(View v) {

        try {

            View parent = (View) v.getParent();

            TextView tv_filename = (TextView) v.findViewById(R.id.tv_answer_filename);
            String file_name = tv_filename.getText().toString();
            System.out.println("Clicked str_filename-------" + file_name);

            Intent i = new Intent(getApplicationContext(), GridViewActivity.class);
            i.putExtra("filetxt", file_name);
            startActivity(i);
            overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void fullprocess() {

        //------------ Object Creations ------------------------------------------------
        try {
            if ((Model.id) != null && !(Model.id).isEmpty() && !(Model.id).equals("null") && !(Model.id).equals("")) {
                if (followupcode != null && !followupcode.isEmpty() && !followupcode.equals("null") && !followupcode.equals("")) {

                    //Model.id = "597789";

                    //---------------Query View ------------------------------------------
                    String full_url = Model.BASE_URL + "sapp/jsonviewquery4doc?os_type=android&followupcode=" + followupcode + "&user_id=" + (Model.id) + "&token=" + Model.token + "&enc=1&isAFiles=1";
                    System.out.println("full_url-------------" + full_url);
                    new JSON_QueryView().execute(full_url);
                    //---------------Query View ------------------------------------------

                } else {
                    go_back_msg();
                }

            } else {
                go_back_msg();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        //------------ Object Creations ---------------------------------------------
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //getMenuInflater().inflate(R.menu.ask_menu, menu);
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

    public final boolean isInternetOn() {

        ConnectivityManager connec = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);

        if (connec.getNetworkInfo(0).getState() == NetworkInfo.State.CONNECTED ||
                connec.getNetworkInfo(0).getState() == NetworkInfo.State.CONNECTING ||
                connec.getNetworkInfo(1).getState() == NetworkInfo.State.CONNECTING ||
                connec.getNetworkInfo(1).getState() == NetworkInfo.State.CONNECTED) {

            return true;

        } else if (
                connec.getNetworkInfo(0).getState() == NetworkInfo.State.DISCONNECTED ||
                        connec.getNetworkInfo(1).getState() == NetworkInfo.State.DISCONNECTED) {

            return false;
        }

        return false;
    }

    public void go_back_msg() {


        //---------------- Dialog------------------------------------------------------------------
        final MaterialDialog alert = new MaterialDialog(AnsweredQueryViewActivity.this);
        alert.setTitle("");
        alert.setMessage("Something went wrong; please try again.");
        alert.setCanceledOnTouchOutside(false);
        alert.setPositiveButton("OK", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alert.dismiss();
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


    }

    public void query_release_check() {

        //---------------- Dialog------------------------------------------------------------------
        final MaterialDialog alert = new MaterialDialog(AnsweredQueryViewActivity.this);
        alert.setTitle("Oops!");
        alert.setMessage("This query has been released. Please check your dashboard for new query.");
        alert.setCanceledOnTouchOutside(false);
        alert.setPositiveButton("OK", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(AnsweredQueryViewActivity.this, NewQueriesActivity.class);
                startActivity(i);
                finish();
                alert.dismiss();
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

        //----------- Flurry -------------------------------------------------
        Map<String, String> articleParams = new HashMap<String, String>();
        articleParams.put("qid:", current_qid);
        articleParams.put("user_id:", (Model.id));
        FlurryAgent.logEvent("android.doc.resume_return_msgbox", articleParams);
        //----------- Flurry -------------------------------------------------
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    public void showFeedbackDialog() {

        try {
            final MaterialDialog alert = new MaterialDialog(AnsweredQueryViewActivity.this);
            View view = LayoutInflater.from(AnsweredQueryViewActivity.this).inflate(R.layout.ask_feedback, null);
            alert.setView(view);

            alert.setTitle("Reply to feedback");

            final EditText edt_coupon = (EditText) view.findViewById(R.id.edt_coupon);

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
                        json.put("followup", ans_followup_text);

                        System.out.println("json------------" + json.toString());

                        new JSONPostFollowup().execute(json);

                        //------------ Google firebase Analitics--------------------
                        Model.mFirebaseAnalytics = FirebaseAnalytics.getInstance(getApplicationContext());
                        Bundle params = new Bundle();
                        params.putString("User", Model.id + "/" + Model.name);
                        params.putString("Details", ans_followup_text);
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

    public void alertdia(String diamsg) {

        final Context context = this;
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
        alertDialogBuilder
                .setMessage(diamsg)
                .setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        edt_followup.setText("");
                        fullprocess();

                    }
                });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    private class JSON_QueryView extends AsyncTask<String, Void, Boolean> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            try {
                progressBar.setVisibility(View.VISIBLE);
                scrollview.setVisibility(View.GONE);
                nolayout.setVisibility(View.GONE);
                netcheck_layout.setVisibility(View.GONE);

                startTime = System.currentTimeMillis();

            } catch (Exception e) {
                e.printStackTrace();
            }
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
                        Intent intent = new Intent(AnsweredQueryViewActivity.this, LoginActivity.class);
                        startActivity(intent);
                        finish();
                    }

                } else {

                    qansby = jsonobj.getString("askedby_name");
                    qcanianswer = jsonobj.getString("cananswer");
                    current_qid = jsonobj.getString("qid");
                    qitems = jsonobj.getString("items");


                    //--------------------------------------------------------
                    if (qcanianswer.equals("1")) {
                        btn_ansquery.setVisibility(View.VISIBLE);
                        followup_layout.setVisibility(View.GONE);
                    } else {
                        btn_ansquery.setVisibility(View.GONE);
                        followup_layout.setVisibility(View.VISIBLE);
                    }
                    //--------------------------------------------------------

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
                    //------------ Get Array -----------------------------------


                    //------------------------------------------------------------------------
                    if (qitems.length() > 2) {

                        try {
                            JSONArray jarray = jsonobj.getJSONArray("items");
                            System.out.println("Items jarray.length------" + jarray.length());

                            for (int i = 0; i < jarray.length(); i++) {

                                jsonobj_items = jarray.getJSONObject(i);
                                System.out.println("jsonobj_Items------" + jsonobj_items.toString());
                                class_text = jsonobj_items.getString("class");

                                //enable_prescription_val = jsonobj_items.getString("enable_prescription");

                                System.out.println("class_text------" + class_text);

                                if (class_text.equals("bubbledRight")) {

                                    if (new Detector().isTablet(getApplicationContext())) {
                                        vi = getLayoutInflater().inflate(R.layout.query_thread_view, null);
                                    } else {
                                        vi = getLayoutInflater().inflate(R.layout.query_thread_view, null);
                                    }

                                    //vi = getLayoutInflater().inflate(R.layout.query_thread_view, null);

                                    query_display_layout = (LinearLayout) vi.findViewById(R.id.query_display_layout);


                                    query_webview = (ObservableWebView) vi.findViewById(R.id.query_webview);
                                    query_webview.setVerticalScrollBarEnabled(false);


                                    extra_layout = (LinearLayout) vi.findViewById(R.id.extra_layout);
                                    extra_hwlayout = (LinearLayout) vi.findViewById(R.id.extra_hwlayout);
                                    extra_hw_details = (LinearLayout) vi.findViewById(R.id.extra_hw_details);
                                    extra_hw_title = (LinearLayout) vi.findViewById(R.id.extra_hw_title);

                                    tv_gender = (TextView) vi.findViewById(R.id.tv_gender);
                                    tvattached = (TextView) vi.findViewById(R.id.tvattached);
                                    files_layout = (LinearLayout) vi.findViewById(R.id.files_layout);
                                    layout_attachfile = (LinearLayout) vi.findViewById(R.id.layout_attachfile);
                                    tv_pat_name = (TextView) vi.findViewById(R.id.tv_pat_name);
                                    tv_pat_place = (TextView) vi.findViewById(R.id.tv_pat_place);

                                    tv_query = (TextView) vi.findViewById(R.id.tvquery);
                                    tv_datetime = (TextView) vi.findViewById(R.id.tv_datetime);
                                    tv_filename = (TextView) vi.findViewById(R.id.tv_filename);

                                    font_reg = Typeface.createFromAsset(getAssets(), Model.font_name);
                                    font_bold = Typeface.createFromAsset(getAssets(), Model.font_name_bold);

                                    tv_pat_name.setTypeface(font_bold);
                                    tv_pat_place.setTypeface(font_reg);
                                    tv_followtit.setTypeface(font_reg);

                                    tv_query.setTypeface(font_reg);

                                    layout_attachfile.setVisibility(View.GONE);

                                    msg_text = jsonobj_items.getString("msg");
                                    msg_ext_text = jsonobj_items.getString("msg_ext");
                                    files_text = jsonobj_items.getString("files");
                                    class_text = jsonobj_items.getString("class");
                                    time_text = jsonobj_items.getString("time");

                                    System.out.println("files_text============" + files_text);

                                    if (jsonobj_items.has("strHtml")) {
                                        strHtml_text = jsonobj_items.getString("strHtml");

                                        query_display_layout.setVisibility(View.GONE);
                                        query_webview.setVisibility(View.VISIBLE);

                                        query_webview.setBackgroundColor(Color.TRANSPARENT);
                                        query_webview.getSettings().setJavaScriptEnabled(true);
                                        query_webview.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NORMAL);

                                        strHtml_text = "<b>Patient:</b> Rajah (Myself), 35 years, Male<br /><br /><b>Query</b><br />Common We have helped solve over a million cases. Ask a doctor online and get quick medical advice for your health queries. Our medical panel consists of over 3500 doctors from 80+ specialities. Get professi...\n" +
                                                "\n" +
                                                "Read more at: https://www.icliniq.com/dashboard/patientV2\n" +
                                                "this is a copyright content of iCliniqCommon We have helped solve over a million cases. Ask a doctor online and get quick medical advice for your health queries. Our medical panel consists of over 3500 doctors from 80+ specialities. Get professi...\n" +
                                                "\n" +
                                                "Read more at: https://www.icliniq.com/dashboard/patientV2\n" +
                                                "this is a copyright content of iCliniqCommon We have helped solve over a million cases. Ask a doctor online and get quick medical advice for your health queries. Our medical panel consists of over 3500 doctors from 80+ specialities. Get professi...\n" +
                                                "\n" +
                                                "Read more at: https://www.icliniq.com/dashboard/patientV2\n" +
                                                "this is a copyright content of iCliniqCommon We have helped solve over a million cases. Ask a doctor online and get quick medical advice for your health queries. Our medical panel consists of over 3500 doctors from 80+ specialities. Get professi...\n" +
                                                "\n" +
                                                "Read more at: https://www.icliniq.com/dashboard/patientV2\n" +
                                                "this is a copyright content of iCliniqCommon We have helped solve over a million cases. Ask a doctor online and get quick medical advice for your health queries. Our medical panel consists of over 3500 doctors from 80+ specialities. Get professi...\n" +
                                                "\n" +
                                                "Read more at: https://www.icliniq.com/dashboard/patientV2\n" +
                                                "this is a copyright content of iCliniqCommon We have helped solve over a million cases. Ask a doctor online and get quick medical advice for your health queries. Our medical panel consists of over 3500 doctors from 80+ specialities. Get professi...\n" +
                                                "\n" +
                                                "Read more at: https://www.icliniq.com/dashboard/patientV2\n" +
                                                "this is a copyright content of iCliniqCommon We have helped solve over a million cases. Ask a doctor online and get quick medical advice for your health queries. Our medical panel consists of over 3500 doctors from 80+ specialities. Get professi...\n" +
                                                "\n" +
                                                "Read more at: https://www.icliniq.com/dashboard/patientV2\n" +
                                                "this is a copyright content of iCliniqCommon We have helped solve over a million cases. Ask a doctor online and get quick medical advice for your health queries. Our medical panel consists of over 3500 doctors from 80+ specialities. Get professi...\n" +
                                                "\n" +
                                                "Read more at: https://www.icliniq.com/dashboard/patientV2\n" +
                                                "this is a copyright content of iCliniqCommon We have helped solve over a million cases. Ask a doctor online and get quick medical advice for your health queries. Our medical panel consists of over 3500 doctors from 80+ specialities. Get professi...\n" +
                                                "Read more at: https://www.icliniq.com/dashboard/patientV2\n" +
                                                "this is a copyright content of iCliniq<br /><br /><ul style=\"padding-left:15px;\"><li style=\"padding-bottom:10px;\"><b>More details about the presenting complaint</b>:<br />Dfgdf</li><li style=\"padding-bottom:10px;\"><b>Previous history of the same issue</b>:<br />Gdfgd</li></ul><br /><b>Height, Weight&nbsp;:</b><br /><br /><table width=\"100%\" cellspacing=\"0\" cellpadding=\"5\" border=\"1\" style=\"border:1px solid #000;\"><tr><td width=\"40%\"><b>Height</b></td><td valign=\"top\" width=\"60%\">6' 0\" (182.88 cm)</td></tr><tr><td width=\"40%\"><b>Weight</b></td><td valign=\"top\" width=\"60%\">167 lbs (75.75 kg)</td></tr></table>";

/*                                        query_webview.loadDataWithBaseURL("", strHtml_text, "text/html", "UTF-8", "");
                                        query_webview.setLongClickable(false);*/
                                        query_webview.loadDataWithBaseURL("", strHtml_text, "text/html", "UTF-8", "");
                                        query_webview.setLongClickable(false);
                                    } else {

                                        query_display_layout.setVisibility(View.VISIBLE);
                                        query_webview.setVisibility(View.GONE);
                                    }

                                    //query_webview.loadData(strHtml_text, "text/html; charset=utf-8", "UTF-8");


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
                                        /*if (json_gender.has("extra")) {
                                            extra_txt = json_gender.getString("extra");
                                        }*/

                                            System.out.println("age_txt------" + age_txt);
                                            System.out.println("gender_txt------" + gender_txt);

                                            if (age_txt != null && !age_txt.isEmpty() && !age_txt.equals("null") && !age_txt.equals("")) {
                                                tv_gender.setVisibility(View.VISIBLE);
                                                tv_gender.setText(Html.fromHtml("<b>Age: </b>" + age_txt + ", <b>Gender: </b>" + gender_txt));
                                            } else {
                                                tv_gender.setVisibility(View.GONE);
                                            }

                                            //extra_hwlayout.removeAllViews();
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

                                                    //vi_hw_full = getLayoutInflater().inflate(R.layout.query_view_extra_full, null);

                                                    tv_etitle = (TextView) vi_hw_full.findViewById(R.id.tv_etitle);
                                                    main_data_layout = (LinearLayout) vi_hw_full.findViewById(R.id.main_data_layout);
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

                                                                    //vi_hw = getLayoutInflater().inflate(R.layout.query_view_extra_details, null);

                                                                    //main_data_layout = (LinearLayout) vi_hw.findViewById(R.id.main_data_layout);
                                                                    //tv_etitle = (TextView) vi_hw.findViewById(R.id.tv_etitle);
                                                                    tv_keytext = (TextView) vi_hw.findViewById(R.id.tv_keytext);
                                                                    tv_valuetext = (TextView) vi_hw.findViewById(R.id.tv_valuetext);

                                                                    //tv_keytext.setText(Html.fromHtml("<b>" + key + "</b>"));
                                                                    tv_keytext.setText(key + ": ");
                                                                    tv_valuetext.setText(value.toString());

                                                                    main_data_layout.addView(vi_hw);

                                                                } catch (JSONException e) {
                                                                    e.printStackTrace();
                                                                }
                                                            }
                                                        }
                                                    }

                                                    extra_hw_details.addView(vi_hw_full);
                                                }
                                            }
                                            //-----------------------Extra HW --------------------------------------
                                        }
                                    }

                                    //-------------------------- Age Gender ---------------------------------------------
                                    System.out.println("msg_text------" + msg_text);
                                    System.out.println("msg_ext_text------" + msg_ext_text);
                                    System.out.println("files_text------" + files_text);
                                    System.out.println("class_text------" + class_text);
                                    System.out.println("time_text------" + time_text);

                                    tv_query.setText(Html.fromHtml(msg_text));
                                    tv_query.setMovementMethod(LinkMovementMethod.getInstance());
                                    tv_datetime.setText(time_text);

                                    //---------------- Msg Extra ---------------------------------
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
                                                tv_ext_title = (TextView) vi_ext.findViewById(R.id.tv_ext_title);
                                                tv_ext_desc = (TextView) vi_ext.findViewById(R.id.tv_ext_desc);

                                                String GET_KEY = extra_query_map.get(key);
                                                System.out.println("keykeykey---------------" + key);
                                                System.out.println("GET_KEY---------------" + GET_KEY);

                                                tv_ext_title.setText(GET_KEY);
                                                tv_ext_desc.setText(Html.fromHtml(categoryJSONObj.optString(key)));

                                                tv_ext_desc.setMovementMethod(LinkMovementMethod.getInstance());

                                                extra_layout.addView(vi_ext);
                                            }


                                        }
                                        //-------------- Dynamic Key -----------------------------


                                    }
                                    //---------------- Msg Extra ---------------------------------------


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

                                    //---------------- Files ---------------------------------------
                                    if ((files_text.length()) > 2) {

                                        layout_attachfile.setVisibility(View.VISIBLE);

                                        System.out.println("files_text------" + files_text);
                                        JSONArray jarray_files = jsonobj_items.getJSONArray("files");
                                        System.out.println("jsonobj_items------" + jarray_files.toString());
                                        System.out.println("jarray_files.length()------" + jarray_files.length());

                                        tvattached.setText("Attachment :   " + jarray_files.length() + " File(s)");

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
                                            System.out.println("file_url--------" + file_full_url);
                                            System.out.println("attach_file_text--------" + attach_file_text);
                                        }

                                        files_layout.setVisibility(View.GONE);
                                        //tv_filename.setText(attach_file_text);


                                        System.out.println("files_text###########" + files_text);
                                        tv_filename.setText(files_text);

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
                                    //vi_ans = getLayoutInflater().inflate(R.layout.query_thread_view_answer, null);


                                    answer_layout_attachfile = (LinearLayout) vi_ans.findViewById(R.id.answer_layout_attachfile);
                                    answer_files_layout = (LinearLayout) vi_ans.findViewById(R.id.answer_files_layout);

                                    Button btn_view_prescription = (Button) vi_ans.findViewById(R.id.btn_view_prescription);
                                    TextView tv_cuurent_query_id = (TextView) vi_ans.findViewById(R.id.tv_cuurent_query_id);
                                    LinearLayout prescribe_layout = (LinearLayout) vi_ans.findViewById(R.id.prescribe_layout);

                                    answer_display_layout = (LinearLayout) vi_ans.findViewById(R.id.answer_display_layout);
                                    webview_answer = (ObservableWebView) vi_ans.findViewById(R.id.webview_answer);
                                    tv_answer = (TextView) vi_ans.findViewById(R.id.tvanswer);
                                    ans_extra_layout = (LinearLayout) vi_ans.findViewById(R.id.ans_extra_layout);
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
                                    tv_answer_filename = (TextView) vi_ans.findViewById(R.id.tv_answer_filename);

                                    feedback_section = (LinearLayout) vi_ans.findViewById(R.id.feedback_section);
                                    doctor_reply_section = (LinearLayout) vi_ans.findViewById(R.id.doctor_reply_section);
                                    tv_patfeedback = (TextView) vi_ans.findViewById(R.id.tv_patfeedback);
                                    tv_replytext = (TextView) vi_ans.findViewById(R.id.tv_replytext);
                                    btn_feedbacksubmit = (Button) vi_ans.findViewById(R.id.btn_feedbacksubmit);


                                    font_reg = Typeface.createFromAsset(getAssets(), Model.font_name);
                                    font_bold = Typeface.createFromAsset(getAssets(), Model.font_name_bold);

                                    tv_answer.setTypeface(font_reg);

                                    msg_text = jsonobj_items.getString("msg");
                                    msg_ext_text = jsonobj_items.getString("msg_ext");
                                    class_text = jsonobj_items.getString("class");
                                    regards = jsonobj_items.getString("regards");
                                    current_answer_id = jsonobj_items.getString("answer_id");
                                    time_text = jsonobj_items.getString("time");
                                    arr_feedback_text = jsonobj_items.getString("arr_feedback");

                                    answer_files_text = jsonobj_items.getString("files");

                                    System.out.println("received answer_files_text------------- " + answer_files_text);

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


                                    System.out.println("time_text---------" + time_text);
                                    System.out.println("arr_feedback_text---------" + arr_feedback_text);

                                    tv_cuurent_query_id.setText(current_qid);
                                    //----------------------------------------------
                                    if (jsonobj_items.has("has_prescription")) {
                                        String has_prescription_val = jsonobj_items.getString("has_prescription");
                                        System.out.println("has_prescription_val---------" + has_prescription_val);

                                        if (has_prescription_val.equals("1")) {
                                            prescribe_layout.setVisibility(View.VISIBLE);
                                        } else {
                                            prescribe_layout.setVisibility(View.GONE);
                                        }
                                    } else {
                                        prescribe_layout.setVisibility(View.GONE);
                                    }
                                    //----------------------------------------------


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


                                    tv_answer.setText(Html.fromHtml(msg_text));
                                    tv_answer.setMovementMethod(LinkMovementMethod.getInstance());
                                    tv_datetimeans.setText(time_text);

                                    System.out.println("msg_text------" + msg_text);
                                    System.out.println("msg_ext_text------" + msg_ext_text);
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
                                                TextView tv_ext_title = (TextView) vi_ans_ext.findViewById(R.id.tv_ext_title);
                                                tv_ext_desc = (TextView) vi_ans_ext.findViewById(R.id.tv_ext_desc);

                                                tv_ext_title.setText(extra_ans_map.get(key));
                                                tv_ext_desc.setText(Html.fromHtml(categoryJSONObj.optString(key)));
                                                tv_ext_desc.setMovementMethod(LinkMovementMethod.getInstance());


                                                ans_extra_layout.addView(vi_ans_ext);
                                            }

                                        }
                                        //-------------- Dynamic Key -----------------------------

                                    }


                                    //---------------- Files ---------------------------------------
                                    if ((answer_files_text.length()) > 2) {

                                        answer_layout_attachfile.setVisibility(View.VISIBLE);

                                        System.out.println("answer_files_text------" + answer_files_text);
                                        //JSONArray jarray_files = jsonobj_items.getJSONArray("files");

                                        JSONArray jarray_files = new JSONArray(answer_files_text);


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
                                        //tv_answer_filename.setText(attach_file_text);
                                        //Asst.commis (traffic) - Raj Kanna
                                        //Dr.Latha Venkatesan, RG nae
                                        System.out.println("files_text############" + answer_files_text);
                                        tv_answer_filename.setText(answer_files_text);

                                    } else {
                                        answer_layout_attachfile.setVisibility(View.GONE);
                                    }
                                    //---------------- Files---------------------------------------


                                    myLayout.addView(vi_ans);
                                }
                                //---------------- Files-------------------------------------------------------------

                                //------- Scrolling Bottom ===============================
                                Runnable runnable = new Runnable() {
                                    @Override
                                    public void run() {
                                        scrollview.fullScroll(ScrollView.FOCUS_DOWN);
                                    }
                                };
                                scrollview.post(runnable);
                                //------- Scrolling Bottom ===============================

                            }

                        } catch (Exception e) {

                            e.printStackTrace();
                            System.out.println("Exception---1---" + e.toString());

                            nolayout.setVisibility(View.VISIBLE);
                            scrollview.setVisibility(View.GONE);
                            progressBar.setVisibility(View.GONE);
                            netcheck_layout.setVisibility(View.GONE);

                        }
                    } else {

                        nolayout.setVisibility(View.VISIBLE);
                        scrollview.setVisibility(View.GONE);
                        progressBar.setVisibility(View.GONE);
                        netcheck_layout.setVisibility(View.GONE);
                    }

                }


            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("Exception---3---" + e.toString());

                nolayout.setVisibility(View.VISIBLE);
                scrollview.setVisibility(View.GONE);
                progressBar.setVisibility(View.GONE);
                netcheck_layout.setVisibility(View.GONE);
            }

            progressBar.setVisibility(View.GONE);
            scrollview.setVisibility(View.VISIBLE);
            nolayout.setVisibility(View.GONE);
            netcheck_layout.setVisibility(View.GONE);

            //------- Scrolling Bottom ===============================
            Runnable runnable = new Runnable() {
                @Override
                public void run() {
                    scrollview.fullScroll(ScrollView.FOCUS_DOWN);
                }
            };
            scrollview.post(runnable);
            //------- Scrolling Bottom ===============================

            //btn_ansquery.setVisibility(View.VISIBLE);

            long elapsedTime = System.currentTimeMillis() - startTime;
            System.out.println("Total elapsed http request/response time in milliseconds: " + elapsedTime);

            try {


                //----------- Flurry -------------------------------------------------
                Map<String, String> articleParams = new HashMap<String, String>();
                articleParams.put("android.doc.qid", "" + current_qid);
                articleParams.put("android.doc.user_id", "" + (Model.id));
                articleParams.put("android.doc.followupcode", "" + followupcode);
                articleParams.put("android.doc.milliseconds", "" + elapsedTime);
                FlurryAgent.logEvent("android.doc.Answered_Query_View", articleParams);
                //----------- Flurry -------------------------------------------------

            } catch (Exception er) {
                er.printStackTrace();

            }

        }
    }

    private class JSON_post_feedback extends AsyncTask<JSONObject, Void, Boolean> {

        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(AnsweredQueryViewActivity.this);
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

    class JSONPostFollowup extends AsyncTask<JSONObject, Void, Boolean> {

        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(AnsweredQueryViewActivity.this);
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
                        Intent intent = new Intent(AnsweredQueryViewActivity.this, LoginActivity.class);
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

    private class JSON_canianswer extends AsyncTask<String, Void, Boolean> {

        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(AnsweredQueryViewActivity.this);
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
                        Intent intent = new Intent(AnsweredQueryViewActivity.this, LoginActivity.class);
                        startActivity(intent);
                        finish();
                    }
                } else {


               /*     opt_freefollow = jsonobj_canisnaswer.getString("opt_freefollow");
                    enable_freefollow = jsonobj_canisnaswer.getString("enable_freefollow");

                    //----------------------------------------------
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
                    //----------------------------------------------*/

                    //----------------------------------------------
                    answering_status = jsonobj_canisnaswer.getString("status");

                    if(jsonobj_canisnaswer.has("msg")) {
                        msg = jsonobj_canisnaswer.getString("msg");

                    }

                    if ((answering_status).equals("0")) {
                        if (msg!=null){
                            AlertBoxMethod(msg);
                        }else{
                            AlertBoxMethod("Sorry! Another doctor has already picked this query.");
                        }
                    } else {
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

    private void AlertBoxMethod(String msg_text) {
        final MaterialDialog alert = new MaterialDialog(this);
        //alert.setTitle("Error..!");
        alert.setMessage(msg_text);
        alert.setCanceledOnTouchOutside(false);
        alert.setPositiveButton("OK", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alert.dismiss();
                finish();
            }
        });
        alert.show();

    }


}

