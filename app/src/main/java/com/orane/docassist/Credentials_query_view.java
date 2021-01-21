package com.orane.docassist;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.appcompat.widget.Toolbar;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.google.firebase.analytics.FirebaseAnalytics;

import com.orane.docassist.Model.Model;
import com.orane.docassist.Network.JSONParser;
import com.orane.docassist.R;
import com.orane.docassist.attachment_view.GridViewActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import me.drakeet.materialdialog.MaterialDialog;

public class Credentials_query_view extends AppCompatActivity {

    SharedPreferences sharedpreferences;
    public static final String MyPREFERENCES = "MyPrefs";
    public static final String Login_Status = "Login_Status_key";
    public static final String user_name = "user_name_key";
    public static final String password = "password_key";
    public static final String Name = "Name_key";
    public static final String bcountry = "bcountry_key";
    public static final String photo_url = "photo_url";
    public static final String token = "token_key";

    LinearLayout answer_extra_layout, answer_layout, days_layout, main_data_layout, layout_attachfile, layout_file_open, extra_hw_title, extra_hw_details, prescribe_layout, show_more, ans_more_dets, show_less;
    CardView answer_show__layout;
    TextView tv_answer_tit, tvprivate, tv_keytext, tv_valuetext, tv_filename, tvclear, tv_anstit, tv_show;
    EditText prescribe_text, diagnosis, pb_cause, lab_t, ddx, pdx, treatment_plan, p_tips, followup;

    TextView tv_ext_title, tv_answer, tv_etitle, tvattached, tv_ext_desc, tv_query, tv_qid, tv_usergeo;
    String answer_test, review_info_text, AnswerExtTrain_test, user_place, AnswerTrain_text, followupcode, gender_text, age_text, title_text, ans_ext_expansion, query_ext_expansion, current_qid, answer_txt, patient_name, ent_ans, attach_file_text, qid, qtype, PatientProfile_text, Specialities_val, QueryAttachment_val, QueryHrecord_val, str_response, patient_id, question, followup_code_val, speciality_val, QueryExt_val;
    JSONObject jsonobj, json_fields, full_jsonobj, json, jsonobj_postans;
    View vi_hw_full;
    Typeface font_reg, font_bold;
    View vi_ext, vi_hw;
    EditText edt_answer;
    Map<String, String> extra_ans_map = new HashMap<String, String>();
    Map<String, String> extra_query_map = new HashMap<String, String>();
    Button btn_ansquery;
    LinearLayout extra_layout, attachment_layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.credential_query_view);

        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);

        //------------ Object Creations -------------------------------
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setTitle("Query View");

            TextView mTitle = (TextView) toolbar.findViewById(R.id.toolbar_title);
            Typeface khandBold = Typeface.createFromAsset(getApplicationContext().getAssets(), Model.font_name_bold);
            mTitle.setTypeface(khandBold);
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.app_color));
        }
        //-----------------------------------------------------------------------
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        try {

            Intent intent = getIntent();
            followupcode = intent.getStringExtra("followupcode");
            qtype = intent.getStringExtra("qtype");

            System.out.println("Get intent followupcode----" + followupcode);
            System.out.println("Get intent qtype----" + qtype);

        } catch (Exception e) {
            e.printStackTrace();
        }


        tv_answer_tit = (TextView) findViewById(R.id.tv_answer_tit);
        tvattached = (TextView) findViewById(R.id.tvattached);
        tv_answer = (TextView) findViewById(R.id.tv_answer);
        tv_show = (TextView) findViewById(R.id.tv_show);
        tv_anstit = (TextView) findViewById(R.id.tv_anstit);
        tvclear = (TextView) findViewById(R.id.tvclear);
        tv_filename = (TextView) findViewById(R.id.tv_filename);
        tv_query = (TextView) findViewById(R.id.tv_query);
        tv_usergeo = (TextView) findViewById(R.id.tv_usergeo);
        tv_qid = (TextView) findViewById(R.id.tv_qid);
        diagnosis = (EditText) findViewById(R.id.diagnosis);
        pb_cause = (EditText) findViewById(R.id.pb_cause);
        lab_t = (EditText) findViewById(R.id.lab_t);
        ddx = (EditText) findViewById(R.id.ddx);
        pdx = (EditText) findViewById(R.id.pdx);
        treatment_plan = (EditText) findViewById(R.id.treatment_plan);
        p_tips = (EditText) findViewById(R.id.p_tips);
        followup = (EditText) findViewById(R.id.followup);
        extra_layout = (LinearLayout) findViewById(R.id.extra_layout);
        show_more = (LinearLayout) findViewById(R.id.show_more);
        answer_show__layout = (CardView) findViewById(R.id.answer_show__layout);
        //show_less = (LinearLayout) findViewById(R.id.show_less);
        answer_layout = (LinearLayout) findViewById(R.id.answer_layout);
        extra_hw_details = (LinearLayout) findViewById(R.id.extra_hw_details);
        ans_more_dets = (LinearLayout) findViewById(R.id.ans_more_dets);
        answer_extra_layout = (LinearLayout) findViewById(R.id.answer_extra_layout);
        layout_attachfile = (LinearLayout) findViewById(R.id.layout_attachfile);
        btn_ansquery = (Button) findViewById(R.id.btn_ansquery);
        edt_answer = (EditText) findViewById(R.id.edt_answer);
        // attachment_layout = (LinearLayout) findViewById(R.id.attachment_layout);

        //----------------------------------------------------
        String credQueryView_url = Model.BASE_URL + "sapp/credQueryView?os_type=android&fcode=" + followupcode + "&doc_id=" + Model.id + "&token=" + Model.token + "&enc=1";
        System.out.println("full_url-------------" + credQueryView_url);
        new JSON_QueryView().execute(credQueryView_url);
        //----------------------------------------------------

        //------ Setup Fonts ------------------
        font_reg = Typeface.createFromAsset(getAssets(), Model.font_name);
        font_bold = Typeface.createFromAsset(getAssets(), Model.font_name_bold);

        tv_query.setTypeface(font_reg);
        tv_usergeo.setTypeface(font_reg);
        tvattached.setTypeface(font_reg);
        tv_anstit.setTypeface(font_reg);
        tvclear.setTypeface(font_reg);
        edt_answer.setTypeface(font_reg);
        tv_show.setTypeface(font_reg);
        btn_ansquery.setTypeface(font_bold);
        //--------------------------------

        show_more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ans_more_dets.setVisibility(View.VISIBLE);
                show_more.setVisibility(View.GONE);
            }
        });

/*        show_less.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ans_more_dets.setVisibility(View.GONE);
                show_more.setVisibility(View.VISIBLE);
            }
        });*/

        tvclear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                edt_answer.setText("");
                Model.query_typed = "";
            }
        });

        btn_ansquery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isInternetOn()) {

                    ent_ans = edt_answer.getText().toString();

                    if (!ent_ans.equals("")) {

                        submit_answer();

                    } else {
                        edt_answer.setError("Please enter the answer");
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Please check your Internet Connection and try again", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    class JSON_QueryView extends AsyncTask<String, Void, Boolean> {

        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            dialog = new ProgressDialog(Credentials_query_view.this);
            dialog.setMessage("please wait");
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

                full_jsonobj = new JSONObject(str_response);

                review_info_text = full_jsonobj.getString("review_info");
                query_ext_expansion = full_jsonobj.getString("query_ext_expansion");
                ans_ext_expansion = full_jsonobj.getString("ans_ext_expansion");

                System.out.println("review_info_text------ " + review_info_text);
                System.out.println("query_ext_expansion------ " + query_ext_expansion);
                System.out.println("ans_ext_expansion------ " + ans_ext_expansion);

                String thread_str = full_jsonobj.getString("thread");
                //thread_str = thread_str.replaceAll("\\[", "").replaceAll("\\]", "");

                //---------- Removing First and Last charector---------------
                thread_str = thread_str.substring(1);
                thread_str = thread_str.substring(0, thread_str.length() - 1);
                //---------- Removing First and Last Charectpr ---------------

                //qid = (new JSONObject(review_info_text)).getString("id");


                System.out.println("thread_str------ " + thread_str);

                jsonobj = new JSONObject(thread_str);

                if (jsonobj.has("token_status")) {
                    String token_status = jsonobj.getString("token_status");
                    if (token_status.equals("0")) {

                        //============================================================
                        SharedPreferences.Editor editor = sharedpreferences.edit();
                        editor.putString(Login_Status, "0");
                        editor.apply();
                        //============================================================

                        finishAffinity();
                        Intent intent = new Intent(Credentials_query_view.this, LoginActivity.class);
                        startActivity(intent);
                        finish();
                    }
                } else {

                    qid = jsonobj.getString("id");
                    question = jsonobj.getString("question");
                    followup_code_val = jsonobj.getString("followup_code");
                    speciality_val = jsonobj.getString("speciality");
                    QueryExt_val = jsonobj.getString("QueryExt");
                    AnswerTrain_text = jsonobj.getString("AnswerTrain");

                    System.out.println("AnswerTrain_text-------- " + AnswerTrain_text);


                    //---------- Removing First and Last Charectpr ---------------
                    QueryExt_val = QueryExt_val.substring(1);
                    QueryExt_val = QueryExt_val.substring(0, QueryExt_val.length() - 1);
                    //---------- Removing First and Last Charectpr ---------------

                    //-------------- query Key -----------------------------
                    JSONObject query_arrayJSONObj = new JSONObject(query_ext_expansion);
                    Iterator<String> query_arrayiterator = query_arrayJSONObj.keys();
                    while (query_arrayiterator.hasNext()) {
                        String query_key = query_arrayiterator.next();
                        String value_of_key = query_arrayJSONObj.optString(query_key);

                        System.out.println("query_key------------------" + query_key);
                        System.out.println("value_of_key------------------" + value_of_key);

                        extra_query_map.put(query_key, value_of_key);
                    }
                    //-------------- query Key -----------------------------

                    //-------------- Answer Key -----------------------------
                    JSONObject answer_arrayJSONObj = new JSONObject(ans_ext_expansion);
                    Iterator<String> answer_arrayiterator = answer_arrayJSONObj.keys();
                    while (answer_arrayiterator.hasNext()) {
                        String query_key = answer_arrayiterator.next();
                        String value_of_key = answer_arrayJSONObj.optString(query_key);

                        System.out.println("answer_key------------------" + query_key);
                        System.out.println("answer_value_of_key------------------" + value_of_key);

                        extra_ans_map.put(query_key, value_of_key);
                    }
                    //-------------- Answer Key -----------------------------


                    //============================ Health Records-===================================
                    QueryHrecord_val = full_jsonobj.getString("hrecord");
                    System.out.println("QueryHrecord_val---- " + QueryHrecord_val);

                    JSONObject jobj = new JSONObject(QueryHrecord_val);

/*                    age_text = jobj.getString("age");
                    gender_text = jobj.getString("gender");*/

                    if (jobj.has("age")) {
                        age_text = jobj.getString("age");
                    } else {
                        age_text = "";
                    }

                    if (jobj.has("gender")) {
                        gender_text = jobj.getString("gender");
                    } else {
                        gender_text = "";
                    }

                    if (jobj.has("vitals")) {
                        String vitals_text = jobj.getString("vitals");


                        JSONArray vitals_jarray = new JSONArray(vitals_text);

                        System.out.println("vitals_jarray.length()---- " + vitals_jarray.length());

                        for (int i = 0; i < vitals_jarray.length(); i++) {
                            JSONObject jsonobj = vitals_jarray.getJSONObject(i);

                            String title_text = jsonobj.getString("title");
                            String fields_text = jsonobj.getString("fields");

                            System.out.println("title_text--------------" + title_text);
                            System.out.println("fields_text--------------" + fields_text);

                            vi_hw_full = getLayoutInflater().inflate(R.layout.query_view_extra_full, null);

                            tv_etitle = (TextView) vi_hw_full.findViewById(R.id.tv_etitle);
                            main_data_layout = (LinearLayout) vi_hw_full.findViewById(R.id.main_data_layout);
                            tv_etitle.setText(Html.fromHtml("<b>" + title_text + "</b>"));

                            System.out.println("title_text-------------------->" + title_text);
                            //extra_hw_title.addView(vi_hw_tit);
                            //--------------------- Title--------------------------------
                            json_fields = new JSONObject(fields_text);

                            for (int f = 1; f <= 5; f++) {

                                String s = "" + f;
                                if (json_fields.has("" + s)) {

                                    String thread = json_fields.getString("" + s);
                                    System.out.println("thread-----" + thread);

                                    JSONObject jsonon_titem = new JSONObject(thread);
                                    System.out.println("jsonon_titem------" + jsonon_titem.toString());
                                    //String jsonon_titem.getString("");

                                    Iterator<String> iter = jsonon_titem.keys();

                                    while (iter.hasNext()) {
                                        String key = iter.next();
                                        System.out.println("Iterator key-----" + key);

                                        try {
                                            Object value = jsonon_titem.get(key);
                                            System.out.println("key_values=======" + value.toString());

                                            View vi_hw = getLayoutInflater().inflate(R.layout.query_view_extra_details, null);

                                            tv_keytext = (TextView) vi_hw.findViewById(R.id.tv_keytext);
                                            tv_valuetext = (TextView) vi_hw.findViewById(R.id.tv_valuetext);

                                            tv_keytext.setText(Html.fromHtml("<b>" + key + "</b>"));
                                            //tv_keytext.setText(key + ": ");
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
                        //============================ Health Records-===================================
                    }

                    //------ Getting user ----------------------------------
                    String user_text = jsonobj.getString("User");
                    String UserGeo_text = jsonobj.getString("UserGeo");
                    System.out.println("user_text--------------" + user_text);
                    System.out.println("UserGeo_text--------------" + UserGeo_text);

                    //---------- Removing First and Last Charectpr ---------------
                    UserGeo_text = UserGeo_text.substring(1);
                    UserGeo_text = UserGeo_text.substring(0, UserGeo_text.length() - 1);
                    //---------- Removing First and Last Charectpr ---------------


                    user_place = (new JSONObject(UserGeo_text)).getString("city");


                    //---------- Removing First and Last Charectpr ---------------
                    user_text = user_text.substring(1);
                    user_text = user_text.substring(0, user_text.length() - 1);
                    //---------- Removing First and Last Charectpr ---------------

                    JSONObject user_text_obj = new JSONObject(user_text);
                    patient_id = user_text_obj.getString("id");
                    patient_name = user_text_obj.getString("display_name");
                    PatientProfile_text = user_text_obj.getString("PatientProfile");

                    //---------- Removing First and Last Charectpr ---------------
                    PatientProfile_text = PatientProfile_text.substring(1);
                    PatientProfile_text = PatientProfile_text.substring(0, PatientProfile_text.length() - 1);
                    //---------- Removing First and Last Charectpr ---------------

                    System.out.println("PatientProfile_text--------------" + PatientProfile_text);
                    JSONObject j1 = new JSONObject(PatientProfile_text);

                    String dob_val = j1.getString("dob");
                    String gender_val = j1.getString("gender");

                    System.out.println("dob_val--------------" + dob_val);
                    System.out.println("gender_val--------------" + gender_val);
                    //------ Getting user ----------------------------------

                    Specialities_val = jsonobj.getString("Specialities");


                    tv_query.setText(Html.fromHtml(question));

                    //===================== Query Extra =============================================

                    if ((QueryExt_val.length()) > 5) {

                        extra_layout.removeAllViews();

                        QueryExt_val = QueryExt_val.replaceAll("\\[", "").replaceAll("\\]", "");

                        //-------------- Dynamic Key -----------------------------
                        JSONObject categoryJSONObj = new JSONObject(QueryExt_val);
                        System.out.println("QueryExt_val-----" + QueryExt_val);

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
                                //tv_ext_desc.setText(Html.fromHtml(categoryJSONObj.optString(key)));
                                tv_ext_desc.setText(value_of_key);
                                tv_ext_desc.setMovementMethod(LinkMovementMethod.getInstance());


                                font_reg = Typeface.createFromAsset(getAssets(), Model.font_name);
                                font_bold = Typeface.createFromAsset(getAssets(), Model.font_name_bold);

                                tv_ext_title.setTypeface(font_bold);
                                tv_ext_desc.setTypeface(font_reg);

                                if (!key.equals("id")) {
                                    extra_layout.addView(vi_ext);
                                }
                            }
                        }
                    }
                    //===================== Query Extra =============================================


                    //---------------- Files ---------------------------------------
                    QueryAttachment_val = full_jsonobj.getString("files");

                    System.out.println("QueryAttachment_val--------- " + QueryAttachment_val);


                    JSONArray jsonArray = new JSONArray(QueryAttachment_val);
                    String[] strArr = new String[jsonArray.length()];

                    for (int i = 0; i < jsonArray.length(); i++) {
                        strArr[i] = jsonArray.getString(i);
                        System.out.println("strArr------" + strArr[i]);
                    }


                    if ((QueryAttachment_val.length()) > 5) {

                        layout_attachfile.setVisibility(View.VISIBLE);

                        System.out.println("QueryAttachment_val------" + QueryAttachment_val);

                        JSONArray jarray_files = new JSONArray(QueryAttachment_val);

                        System.out.println("jsonobj_items------" + jarray_files.toString());
                        System.out.println("jarray_files.length()------" + jarray_files.length());

                        tvattached.setText("Attachment:   " + jarray_files.length() + " File(s)");

                        attach_file_text = "";

                        for (int j = 0; j < jarray_files.length(); j++) {

                            strArr[j] = jsonArray.getString(j);
                            System.out.println("strArr------" + strArr[j]);

                            String url_text = strArr[j];

                            //------------------------ File Attached Text --------------------------------
                            if (attach_file_text.equals("")) {
                                attach_file_text = url_text;
                                System.out.println("attach_file_text-------" + attach_file_text);
                            } else {
                                attach_file_text = attach_file_text + "###" + url_text;
                                System.out.println("attach_file_text-------" + attach_file_text);
                            }
                            //------------------------ File Attached Text --------------------------------

                        }

                        //files_layout.setVisibility(View.GONE);
                        //tv_filename.setText(attach_file_text);
                        //tv_filename.setText(attach_file_text);
                        tv_filename.setText(QueryAttachment_val);

                    } else {
                        layout_attachfile.setVisibility(View.GONE);
                    }
                    //---------------- Files---------------------------------------


                    tv_usergeo.setText(patient_name + "-" + age_text + "-" + gender_text + ", " + user_place);


                    if (AnswerTrain_text.length() > 5) {

                        answer_layout.setVisibility(View.GONE);
                        answer_show__layout.setVisibility(View.VISIBLE);
                        tv_answer_tit.setVisibility(View.VISIBLE);
                        btn_ansquery.setVisibility(View.GONE);

                        //---------- Removing First and Last Charectpr ---------------
                        AnswerTrain_text = AnswerTrain_text.substring(1);
                        AnswerTrain_text = AnswerTrain_text.substring(0, AnswerTrain_text.length() - 1);
                        //---------- Removing First and Last Charectpr ---------------

                        //------------- Getting Answer --------------------------
                        JSONObject janswer = new JSONObject(AnswerTrain_text);
                        answer_test = janswer.getString("answer");
                        AnswerExtTrain_test = janswer.getString("AnswerExtTrain");

                        tv_answer.setText(Html.fromHtml(answer_test));
                        //------------- Getting Answer --------------------------

                        //===================== Answer Extra =============================================
                        if ((AnswerExtTrain_test.length()) > 5) {

                            answer_extra_layout.removeAllViews();

                            AnswerExtTrain_test = AnswerExtTrain_test.replaceAll("\\[", "").replaceAll("\\]", "");

                            //-------------- Dynamic Key -----------------------------
                            JSONObject categoryJSONObj = new JSONObject(AnswerExtTrain_test);
                            System.out.println("QueryExt_val-----" + AnswerExtTrain_test);

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

                                    String GET_KEY = extra_ans_map.get(key);

                                    System.out.println("keykeykey---------------" + key);
                                    System.out.println("GET_KEY---------------" + GET_KEY);

                                    tv_ext_title.setText(GET_KEY);
                                    //tv_ext_desc.setText(Html.fromHtml(categoryJSONObj.optString(key)));
                                    tv_ext_desc.setText(value_of_key);
                                    tv_ext_desc.setMovementMethod(LinkMovementMethod.getInstance());


                                    font_reg = Typeface.createFromAsset(getAssets(), Model.font_name);
                                    font_bold = Typeface.createFromAsset(getAssets(), Model.font_name_bold);

                                    tv_ext_title.setTypeface(font_bold);
                                    tv_ext_desc.setTypeface(font_reg);

                                    if (!key.equals("id")) {
                                        answer_extra_layout.addView(vi_ext);
                                    }
                                }
                            }
                        }
                        //===================== Answer Extra =============================================


                    } else {
                        answer_layout.setVisibility(View.VISIBLE);
                        answer_show__layout.setVisibility(View.GONE);
                        btn_ansquery.setVisibility(View.VISIBLE);
                        tv_answer_tit.setVisibility(View.GONE);
                    }
                }

                dialog.dismiss();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void submit_answer() {

        try {

            try {
                answer_txt = URLEncoder.encode((edt_answer.getText().toString()), "UTF-8");

                Model.query_typed = "";

                json = new JSONObject();
                json.put("user_id", (Model.id));
                json.put("qid", qid);
                json.put("reply", answer_txt);
                json.put("diagnosis", "");
                json.put("pb_cause", (pb_cause.getText().toString()));
                json.put("lab_t", (lab_t.getText().toString()));
                json.put("ddx", (ddx.getText().toString()));
                json.put("pdx", (pdx.getText().toString()));
                json.put("treatment_plan", (treatment_plan.getText().toString()));
                json.put("followup", (followup.getText().toString()));
                json.put("p_tips", (p_tips.getText().toString()));
                json.put("prescription", "");

                System.out.println("json------------" + json.toString());


                //------------ Google firebase Analitics--------------------
                Model.mFirebaseAnalytics = FirebaseAnalytics.getInstance(getApplicationContext());
                Bundle params = new Bundle();
                params.putString("User", Model.id + "/" + Model.name);
                params.putString("Details", str_response);
                Model.mFirebaseAnalytics.logEvent("Credential_Query_View", params);
                //------------ Google firebase Analitics--------------------


                new JSONPostAnswer().execute(json);

            } catch (Exception e) {
                e.printStackTrace();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    class JSONPostAnswer extends AsyncTask<JSONObject, Void, Boolean> {

        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            dialog = new ProgressDialog(Credentials_query_view.this);
            dialog.setMessage("Submitting, please wait");
            dialog.show();
            dialog.setCancelable(false);
        }

        @Override
        protected Boolean doInBackground(JSONObject... urls) {
            try {

                JSONParser jParser = new JSONParser();
                jsonobj_postans = jParser.JSON_POST(urls[0], "cred_submitAnswer");

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

                String ans_status = jsonobj_postans.getString("status");
                System.out.println("ans_status------------" + ans_status);

                say_success();

                Model.query_launch = "cred_answered";

                dialog.cancel();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
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

    public void onClick(View v) {

        if (isInternetOn()) {

            try {
                TextView tv_filename = (TextView) v.findViewById(R.id.tv_filename);
                String file_name = tv_filename.getText().toString();
                System.out.println("str_filename-------" + file_name);

                Intent i = new Intent(getApplicationContext(), GridViewActivity.class);
                //Intent i = new Intent(getApplicationContext(), Attachment_List_Activity.class);
                i.putExtra("filetxt", file_name);
                startActivity(i);
                overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);


            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            Toast.makeText(getApplicationContext(), "Please check your Internet Connection and try again.", Toast.LENGTH_SHORT).show();
        }
    }

    public void say_success() {

        try {
            final MaterialDialog alert = new MaterialDialog(Credentials_query_view.this);
            alert.setTitle("Thank you.!");
            alert.setMessage("Answer has been posted successfully..!");
            alert.setCanceledOnTouchOutside(false);
            alert.setPositiveButton("Ok", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    alert.dismiss();
                    finish();
                }
            });
            alert.show();
        }
        catch (Exception e){
            e.printStackTrace();
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        //getMenuInflater().inflate(R.menu.prepack_menu, menu);
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


}
