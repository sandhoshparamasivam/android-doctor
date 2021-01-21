package com.orane.docassist;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.text.Html;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.flurry.android.FlurryAgent;

import com.github.ksoichiro.android.observablescrollview.ObservableWebView;
import com.obsez.android.lib.filechooser.ChooserDialog;
import com.orane.docassist.Model.Model;
import com.orane.docassist.Network.JSONParser;
import com.orane.docassist.attachment_view.GridViewActivity;
import com.orane.docassist.expand.ExpandableLayout;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.wdullaer.materialdatetimepicker.time.RadialPickerLayout;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import me.drakeet.materialdialog.MaterialDialog;

import static com.zipow.videobox.confapp.ConfMgr.getApplicationContext;

public class BookingViewDetails extends AppCompatActivity implements
        TimePickerDialog.OnTimeSetListener,
        DatePickerDialog.OnDateSetListener {

    TextView cons_tit, tv_filename, tv_ext, tvdate, tvtime, tvpatientgeo, tvpatient, tvcasedets, tvattached, tvconsdate, tvconsmode, tvconsstatus, tvnotes;
    Button btnwrite_notes;
    ImageView consult_mode_img;

    private static String dirPath;
    private String _path, extension;
    int downloadIdOne;

    public String fileUrlSecure_text, family_list, str_response, file_full_url, attach_file_text, status_val, tvgeo_val, tvpatient_val, strStatus, cons_view_type, err_text, note_time_text, note_text, url;
    public String slot_text, AM_PM, hourString, minuteString, cons_select_date, speciality, consult_date_actual, patient_geo, patient_name, file_doc_type, edt_notes_text, case_details, consult_date, str_time_range, timezone, strConsultType, language, strStatusZ, notes_jsonobj_text, pres_jsonobj_text, files_jsonobj_text, filename, file_title, file_ext, appr_id, patient_id, str_cons_type, cons_id, pgeo, cons_patient, cons_date, cons_time, cons_type, cons_case_dets, cons_status;

    Typeface font_reg, font_bold;

    JSONObject json_response_obj, jsonobj1, jsonobj_files, json_booking_param, appt_jsonobj;
    LinearLayout files_full_layout, files_layout, notes_layout;
    ScrollView full_layout;
    Spinner spinner_timeslot;
    ProgressBar progressBar;

    SharedPreferences sharedpreferences;
    public static final String MyPREFERENCES = "MyPrefs";
    public static final String Login_Status = "Login_Status_key";
    Button btn_confirm, btn_date, btn_time;

    ObservableWebView webview_notes, webview;
    LinearLayout expand_layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bookingview);

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

        //------------ Object Creations -------------------------------
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.app_color));
        }
        //---------------------- Object Creation -----------------------

        FlurryAgent.onPageView();

        expand_layout = findViewById(R.id.expand_layout);
        webview_notes = findViewById(R.id.webview_notes);
        webview = findViewById(R.id.webview);
        cons_tit = findViewById(R.id.cons_tit);
        btnwrite_notes = findViewById(R.id.btnwrite_notes);
        btn_confirm = findViewById(R.id.btn_confirm);
        progressBar = findViewById(R.id.progressBar);
        full_layout = findViewById(R.id.full_LinearLayout);
        files_full_layout = findViewById(R.id.files_full_layout);
        files_layout = findViewById(R.id.files_layout);
        notes_layout = findViewById(R.id.notes_layout);

        tvdate = findViewById(R.id.tvdate);
        tvtime = findViewById(R.id.tvtime);
        consult_mode_img = findViewById(R.id.consult_mode_img);
        tvpatientgeo = findViewById(R.id.tvpatientgeo);
        tvpatient = findViewById(R.id.tvpatient);
        tvcasedets = findViewById(R.id.tvcasedets);
        tvattached = findViewById(R.id.tvattached);
        tvconsdate = findViewById(R.id.tvconsdate);
        tvconsmode = findViewById(R.id.tvconsmode);
        tvconsstatus = findViewById(R.id.tvconsstatus);
        tvnotes = findViewById(R.id.tvnotes);
        tv_filename = findViewById(R.id.tv_filename);

        font_reg = Typeface.createFromAsset(getAssets(), Model.font_name);
        font_bold = Typeface.createFromAsset(getAssets(), Model.font_name_bold);

        cons_tit.setTypeface(font_bold);
        tvdate.setTypeface(font_reg);
        tvtime.setTypeface(font_reg);
        tvpatient.setTypeface(font_bold);
        tvpatientgeo.setTypeface(font_reg);
        tvconsstatus.setTypeface(font_reg);
        tvcasedets.setTypeface(font_reg);

        ((TextView) findViewById(R.id.tv_datelab)).setTypeface(font_bold);
        ((TextView) findViewById(R.id.tv_timelab)).setTypeface(font_bold);
        ((TextView) findViewById(R.id.tv_pat_lab)).setTypeface(font_bold);
        ((TextView) findViewById(R.id.tv_sta_lab)).setTypeface(font_bold);
        ((TextView) findViewById(R.id.tv_details_lab)).setTypeface(font_bold);
        ((TextView) findViewById(R.id.tvattached)).setTypeface(font_bold);
        ((TextView) findViewById(R.id.tv_botr_lab)).setTypeface(font_reg);

        files_full_layout.setVisibility(View.GONE);

        try {
            Intent intent = getIntent();
            cons_id = intent.getStringExtra("consid");
            tvpatient_val = intent.getStringExtra("tvpatient_val");
            tvgeo_val = intent.getStringExtra("tvgeo_val");


            System.out.println("cons_id-------------" + cons_id);
            System.out.println("tvpatient_val-------------" + tvpatient_val);
            System.out.println("tvgeo_val-------------" + tvgeo_val);
            //----------------------------------------------------------------
        } catch (Exception ee) {
            ee.printStackTrace();
        }

        btn_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    final MaterialDialog alert = new MaterialDialog(BookingViewDetails.this);
                    View view = LayoutInflater.from(BookingViewDetails.this).inflate(R.layout.booking_confirm_diag, null);
                    alert.setView(view);

                    alert.setTitle("Consultation Timing");

                    final EditText edt_coupon = view.findViewById(R.id.edt_coupon);

                    btn_date = view.findViewById(R.id.btn_date);
                    btn_time = view.findViewById(R.id.btn_time);
                    spinner_timeslot = view.findViewById(R.id.spinner_timeslot);

                    //--------- Setting Select Time Slots ---------------------------------
                    final List<String> categories = new ArrayList<String>();

                    slot_text = slot_text.replaceAll("\\[", "").replaceAll("\\]", "");
                    String[] separated = slot_text.split(",");
                    for (int i = 0; i < separated.length; i++) {
                        categories.add(separated[i]);
                    }

                    ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(BookingViewDetails.this, android.R.layout.simple_spinner_item, categories);
                    dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner_timeslot.setAdapter(dataAdapter);
                    //--------- Setting Select Time Slots ---------------------------------

                    alert.setCanceledOnTouchOutside(false);
                    alert.setPositiveButton("OK", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            String spinner_text = spinner_timeslot.getSelectedItem().toString();
                            System.out.println("spinner_text-------------" + spinner_text);

                            spinner_text = spinner_text.replace("\"", "");

                            try {
                                json_booking_param = new JSONObject();
                                json_booking_param.put("book_id", appr_id);
                                json_booking_param.put("doctor_id", Model.id);
                                json_booking_param.put("time_slot", spinner_text);

                                System.out.println("json_booking_param---" + json_booking_param.toString());

                                new JSON_ConfirmBooking().execute(json_booking_param);

                                alert.dismiss();

                            } catch (Exception e2) {
                                e2.printStackTrace();
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

                //Toast.makeText(getApplicationContext(), "Consultation Confirmed..", Toast.LENGTH_LONG).show();
            }
        });


        if (cons_id != null && !cons_id.isEmpty() && !cons_id.equals("null") && !cons_id.equals("")) {
            if ((Model.id) != null && !(Model.id).isEmpty() && !(Model.id).equals("null") && !(Model.id).equals("")) {

                //------------------------- Url Pass -------------------------
                url = Model.BASE_URL + "sapp/viewbooking4doc?user_id=" + (Model.id) + "&format=json&id=" + cons_id + "&token=" + Model.token + "&enc=1";
                System.out.println("Cond View url------" + url);
                new JSON_ViewCons().execute(url);
                //------------------------- Url Pass -------------------------

                //-------------------------------------------------------------------
                String get_family_url = Model.BASE_URL + "sapp/listHReportsData?item_id=" + cons_id + "&item_type=booking&user_id=" + Model.id + "&token=" + Model.token;
                System.out.println("get_family_url---------" + get_family_url);
                new JSON_getFileList().execute(get_family_url);
                //-------------------------------------------------------------------

            } else {
                Toast.makeText(getApplicationContext(), "Something went wrong. Please try again.", Toast.LENGTH_LONG).show();
                ask_logout();
            }
        } else {
            Toast.makeText(getApplicationContext(), "Something went wrong. Please try again.", Toast.LENGTH_LONG).show();
            ask_logout();
        }
    }

    public void ask_logout() {


        //---------------- Dialog------------------------------------------------------------------
        final MaterialDialog alert = new MaterialDialog(BookingViewDetails.this);
        alert.setTitle("Oops.!");
        alert.setMessage("Something went wrong. You need to logout and login again to proceed.");
        alert.setCanceledOnTouchOutside(false);
        alert.setPositiveButton("Yes,logout!", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alert.dismiss(); //============================================================
                sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedpreferences.edit();
                editor.putString(Login_Status, "0");
                editor.apply();
                //============================================================
                Intent i = new Intent(BookingViewDetails.this, LoginActivity.class);
                startActivity(i);
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


    public void onClick(View v) {

        try {
            TextView tv_filename = v.findViewById(R.id.tv_filename);
            String file_name = tv_filename.getText().toString();
            System.out.println("str_filename-------" + file_name);

            Intent i = new Intent(getApplicationContext(), GridViewActivity.class);
            i.putExtra("filetxt", file_name);
            startActivity(i);
            overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private class JSON_ViewCons extends AsyncTask<String, Void, Boolean> {

        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressBar.setVisibility(View.VISIBLE);
            full_layout.setVisibility(View.GONE);

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

            //dialog.cancel();

            try {

                appt_jsonobj = new JSONObject(str_response);

                if (appt_jsonobj.has("token_status")) {
                    String token_status = appt_jsonobj.getString("token_status");
                    if (token_status.equals("0")) {

                        //============================================================
                        SharedPreferences.Editor editor = sharedpreferences.edit();
                        editor.putString(Login_Status, "0");
                        editor.apply();
                        //============================================================

                        finishAffinity();
                        Intent intent = new Intent(BookingViewDetails.this, LoginActivity.class);
                        startActivity(intent);
                        finish();
                    }

                } else {

                    //------------- get Appt Details ------------------------------------------------
                    appr_id = appt_jsonobj.getString("id");
                    patient_name = appt_jsonobj.getString("patient_name");
                    patient_geo = appt_jsonobj.getString("patient_geo");
                    strConsultType = appt_jsonobj.getString("str_consult_type");
                    consult_date = appt_jsonobj.getString("consult_date");
                    consult_date_actual = appt_jsonobj.getString("consult_date_actual");
                    str_time_range = appt_jsonobj.getString("time_range");
                    case_details = appt_jsonobj.getString("case");
                    language = appt_jsonobj.getString("lang");
                    speciality = appt_jsonobj.getString("sp");
                    slot_text = appt_jsonobj.getString("slot");
                    //------------- get Appt Details --------------------------

                    cons_case_dets = appt_jsonobj.getString("strHtmlData");

                    //---------------------------------------------------------------------
                    webview.getSettings().setJavaScriptEnabled(true);
                    webview.loadDataWithBaseURL("", cons_case_dets, "text/html", "UTF-8", "");
                    webview.setLongClickable(false);
                    //-------------------------------------------------------------

                    //--------- Cons Image -------------------
                    if (strConsultType.equals("Phone Consultation")) { //---------- Phone ---------------------
                        consult_mode_img.setBackgroundResource(R.mipmap.phone_cons_ico_color);
                        cons_tit.setText("Phone Consultation Schedule");
                    } else if (strConsultType.equals("Video Consultation")) {
                        consult_mode_img.setBackgroundResource(R.mipmap.video_cons_ico_color);
                        cons_tit.setText("Video Consultation Schedule");
                    } else {
                        consult_mode_img.setBackgroundResource(R.mipmap.direct_cons_ico);
                        cons_tit.setText("Direct Consultation Schedule");
                    }

                    try {
                        //----------- Flurry -------------------------------------
                        Map<String, String> articleParams = new HashMap<String, String>();
                        articleParams.put("android.doc.Appt_id", appr_id);
                        articleParams.put("android.doc.case_details", case_details);
                        articleParams.put("android.doc.consult_date", consult_date);
                        articleParams.put("android.doc.Time_Range", str_time_range);
                        articleParams.put("android.doc.Cons_Type", strConsultType);
                        articleParams.put("android.doc.Language", language);
                        articleParams.put("android.doc.speciality", speciality);
                        FlurryAgent.logEvent("android.doc.Booking_View", articleParams);
                        //----------- Flurry -------------------------------------
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    System.out.println("appr_id--------" + appr_id);

                    cons_tit.setText(strConsultType + " Schedule");
                    tvpatient.setText(patient_name);
                    tvpatientgeo.setText(patient_geo);
                    tvdate.setText(consult_date);
                    tvtime.setText(str_time_range);
                    tvconsmode.setText(strConsultType);
                    tvcasedets.setText(Html.fromHtml(case_details));
                    //tvconsstatus.setText(strStatus);

                    System.out.println("tvpatient_val--------" + patient_name);
                    System.out.println("tvgeo_val--------" + patient_geo);
                    System.out.println("consult_date--------" + consult_date);
                    System.out.println("str_time_range--------" + str_time_range);
                    System.out.println("strConsultType--------" + strConsultType);
                    //System.out.println("case_details--------" + case_details);

                    notes_layout.setVisibility(View.GONE);

                    //---------------- Files ---------------------------------------
                    if (appt_jsonobj.has("files")) {

                        files_jsonobj_text = appt_jsonobj.getString("files");

                        if ((files_jsonobj_text.length()) > 2) {
                            files_full_layout.setVisibility(View.VISIBLE);
                            JSONArray jarray_files = appt_jsonobj.getJSONArray("files");

                            System.out.println("jsonobj_items------" + jarray_files.toString());
                            System.out.println("jarray_files.length()------" + jarray_files.length());

                            if (jarray_files.length() > 0) {

                                tvattached.setText("Attached " + jarray_files.length() + " File(s)");
                                attach_file_text = "";

                                for (int j = 0; j < jarray_files.length(); j++) {
                                    jsonobj_files = jarray_files.getJSONObject(j);

                                    System.out.println("jsonobj_files------" + j + " ----" + jsonobj_files.toString());
                                    patient_id = jsonobj_files.getString("user_id");
                                    file_doc_type = jsonobj_files.getString("doctype");
                                    filename = jsonobj_files.getString("file");
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

                                    System.out.println("filename--------" + filename);
                                    System.out.println("file_ext--------" + file_ext);
                                }

                                files_layout.setVisibility(View.GONE);
                                //tv_filename.setText(attach_file_text);
                                tv_filename.setText(files_jsonobj_text);
                            }
                        } else {
                            files_full_layout.setVisibility(View.GONE);
                        }
                    } else {
                        files_full_layout.setVisibility(View.GONE);
                    }
                    //-------------------------------------------------------
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

            try {


                //----------- Flurry -------------------------------------------------
                Map<String, String> articleParams = new HashMap<String, String>();
                articleParams.put("android.doc.cons_id", cons_id);
                articleParams.put("android.doc.cons_view_type", cons_view_type);
                FlurryAgent.logEvent("android.doc.Consultation_Booking", articleParams);
                //----------- Flurry -----------------------------------------------

            } catch (Exception e) {
                e.printStackTrace();
            }

            progressBar.setVisibility(View.GONE);
            full_layout.setVisibility(View.VISIBLE);
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        if (id == android.R.id.home) {
            finish();
            return true;
        }

       /* if (id == R.id.notify) {
            return true;
        }*/

        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onTimeSet(RadialPickerLayout view, int hourOfDay, int minute, int second) {

        try {

            if (hourOfDay < 12) {
                AM_PM = "AM";
            } else {
                AM_PM = "PM";
            }

            //String hourString = hourOfDay < 10 ? "0" + hourOfDay : "" + hourOfDay;
            hourString = (hourOfDay == 0) ? "12" : Integer.toString(hourOfDay);
            minuteString = minute < 10 ? "0" + minute : "" + minute;

            if (hourOfDay > 12) {
                hourString = "" + (12 - hourOfDay);
            } else {
                hourString = "" + hourOfDay;
            }

            //String secondString = second < 10 ? "0" + second : "" + second;

            //String time = "You picked the following time: " + hourString + "h" + minuteString + "m" + secondString + "s";
            String time = hourString + ":" + minuteString + " " + AM_PM;
            System.out.println("time---------" + time);

            btn_time.setText(time);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {

        try {
            //String date = dayOfMonth + "/" + (++monthOfYear) + "/" + year;
            cons_select_date = year + "/" + (monthOfYear + 1) + "/" + dayOfMonth;
            System.out.println("Cal Date------" + cons_select_date);

            //--------- for System -------------------
            SimpleDateFormat curFormater = new SimpleDateFormat("yyyy/MM/dd");
            Date dateObj = curFormater.parse(cons_select_date);
            String newDateStr = curFormater.format(dateObj);
            System.out.println("For System select_date---------" + newDateStr);
            //--------------------------------

            btn_date.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
            System.out.println("cons_select_date---------" + cons_select_date);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private class JSON_ConfirmBooking extends AsyncTask<JSONObject, Void, Boolean> {

        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(BookingViewDetails.this);
            dialog.setMessage("Confirming.. Please wait..");
            dialog.show();
            dialog.setCancelable(false);
        }

        @Override
        protected Boolean doInBackground(JSONObject... urls) {
            try {
                JSONParser jParser = new JSONParser();
                json_response_obj = jParser.JSON_POST(urls[0], "confirm_booking");

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

                if (json_response_obj.has("message")) {
                    String msg = json_response_obj.getString("message");
                    Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
                }

                btn_confirm.setVisibility(View.GONE);
                dialog.cancel();

                finish();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private class JSON_getFileList extends AsyncTask<String, Void, String> {

        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            dialog = new ProgressDialog(BookingViewDetails.this);
            dialog.setMessage("Please wait");
            dialog.show();
            dialog.setCancelable(false);

        }

        @Override
        protected String doInBackground(String... urls) {

            try {

                JSONParser jParser = new JSONParser();
                family_list = jParser.getJSONString(urls[0]);

                System.out.println("Family URL---------------" + urls[0]);

                return family_list;
            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }

        protected void onPostExecute(String family_list) {

            //apply_relaships_radio(family_list);

            try {

                System.out.println("Attached_Files_List----" + family_list);

                JSONObject jsonFileList = new JSONObject(family_list);
                String det_text = jsonFileList.getString("det");
                JSONArray det_jarray = new JSONArray(det_text);

                expand_layout.removeAllViews();


                for (int i = 0; i < det_jarray.length(); i++) {

                    jsonobj1 = det_jarray.getJSONObject(i);
                    System.out.println("jsonobj_first-----" + jsonobj1.toString());

                    String reportsLabel_text = jsonobj1.getString("reportsLabel");
                    String data_text = jsonobj1.getString("data");

                    JSONArray data_jarray = new JSONArray(data_text);

                    //------------------------------------
                    LayoutInflater layoutInflater = (LayoutInflater) getBaseContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    final View addView = layoutInflater.inflate(R.layout.attachment_expand_view, null);

                    final LinearLayout expand_inner_view = addView.findViewById(R.id.expand_inner_view);
                    TextView tv_att_title = addView.findViewById(R.id.tv_att_title);
                    final ImageView img_right_arrow = addView.findViewById(R.id.img_right_arrow);

                    //img_right_arrow.setImageResource(R.mipmap.down_icon);
                    tv_att_title.setText(reportsLabel_text);

                    ExpandableLayout expandableLayout = addView.findViewById(R.id.expandable_layout);

/*                    Random rnd = new Random();
                    int color = Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));
                    expandableLayout.setBackgroundColor(color);*/


                    expandableLayout.setOnExpandListener(new ExpandableLayout.OnExpandListener() {
                        @Override
                        public void onExpand(boolean expanded) {
                            //Toast.makeText(AskQuery2.this, "expand?" + expanded, Toast.LENGTH_SHORT).show();

                            if (expanded) {
                                img_right_arrow.setImageResource(R.mipmap.up_icon);
                            } else {
                                img_right_arrow.setImageResource(R.mipmap.down_icon);
                            }
                        }
                    });

                    for (int j = 0; j < data_jarray.length(); j++) {

                        JSONObject file_jobj = data_jarray.getJSONObject(j);
                        System.out.println("jsonobj_first-----" + file_jobj.toString());

                        final String attach_id_text = file_jobj.getString("attach_id");
                        String reportDate_text = file_jobj.getString("reportDate");

                        fileUrlSecure_text = file_jobj.getString("fileUrl");

                        String isDelete_text = file_jobj.getString("isDelete");
                        String reportDesc = file_jobj.getString("reportDesc");
                        String ext_text = file_jobj.getString("ext");


                        //--------------------------------------------------------------
                        if (file_jobj.has("fileUrlSecure")) {
                            fileUrlSecure_text = file_jobj.getString("fileUrlSecure");

                        } else {
                            fileUrlSecure_text = file_jobj.getString("fileUrl");
                        }

                        final String fileUrl_text = fileUrlSecure_text;
                        //--------------------------------------------------------------


                        //final String fileUrl_text = file_jobj.getString("fileUrl");
                        //final String fileUrl_text = file_jobj.getString("fileUrlSecure");

/*                        final String fileUrl_text  = file_jobj.getString("fileUrlSecure");

                        if (file_jobj.has("fileUrlSecure")) {
                            fileUrlSecure_text = file_jobj.getString("fileUrlSecure");

                        } else {
                            fileUrlSecure_text = "";

                        }*/


                        //------------------------------------
                        LayoutInflater layoutInflater2 = (LayoutInflater) getBaseContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                        final View addView2 = layoutInflater2.inflate(R.layout.attachment_expand_view2, null);

                        TextView tv_summ_title2 = addView2.findViewById(R.id.tv_summ_title2);
                        TextView tv_desc_summary_text = addView2.findViewById(R.id.tv_desc_summary_text);
                        TextView tv_file_path = addView2.findViewById(R.id.tv_file_path);
                        TextView tv_file_ext = addView2.findViewById(R.id.tv_file_ext);
                        TextView tv_isWebView = addView2.findViewById(R.id.tv_isWebView);

                        Button btn_open = addView2.findViewById(R.id.btn_open);
                        Button btn_remove = addView2.findViewById(R.id.btn_remove);

                        tv_summ_title2.setText("File : " + (j + 1) + " Dated on " + reportDate_text);
                        tv_desc_summary_text.setText(reportDesc);
                        tv_file_path.setText(fileUrl_text);
                        tv_file_ext.setText(ext_text);
                        //tv_isWebView.setText(isWebView_val);

                        img_right_arrow.setImageBitmap(BitmapFactory.decodeFile(fileUrl_text));

                        System.out.println("attach_id_text-----------" + (attach_id_text));
                        System.out.println("reportDate_text-----------" + (reportDate_text));
                        System.out.println("isDelete_text-----------" + (isDelete_text));
                        System.out.println("reportDesc-----------" + (reportDesc));


                        //------------------------------------------
                        if (isDelete_text.equals("1")) {
                            btn_remove.setVisibility(View.GONE);
                        } else {
                            btn_remove.setVisibility(View.GONE);
                        }
                        //------------------------------------------


                        btn_open.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                View parent = (View) v.getParent();

                                TextView tv_full_path = parent.findViewById(R.id.tv_file_path);
                                TextView tv_file_ext = parent.findViewById(R.id.tv_file_ext);
                                TextView tv_isWebView_val = parent.findViewById(R.id.tv_isWebView);

                                String path_text = tv_full_path.getText().toString();
                                String tv_file_ext_val = tv_file_ext.getText().toString();
                                String isWebView_text = tv_isWebView_val.getText().toString();


                                String file_full__url = "";

                                if ("?".contains(fileUrl_text)) {
                                    file_full__url = fileUrl_text + "&token=" + Model.token + "&viewer_doctor_id=" + Model.id;
                                } else {
                                    file_full__url = fileUrl_text + "?token=" + Model.token + "&viewer_doctor_id=" + Model.id;
                                }

                                System.out.println("file_full__url-------------" + tv_file_ext_val);
                                System.out.println("tv_file_ext_val-------------" + file_full__url);


                                Intent i = new Intent(Intent.ACTION_VIEW);
                                i.setData(Uri.parse(file_full__url));
                                startActivity(i);
/*

                                if (tv_file_ext_val.equals("jpg")
                                        || (tv_file_ext_val.equals("JPG"))
                                        || (tv_file_ext_val.equals("png"))
                                        || (tv_file_ext_val.equals("PNG"))
                                        || (tv_file_ext_val.equals("jpeg"))
                                        || (tv_file_ext_val.equals("JPEG"))
                                        || (tv_file_ext_val.equals("tiff"))
                                ) {

                                    //-------------------- webview -------------------------------------------
                                    Intent i = new Intent(getApplicationContext(), WebViewActivity.class);
                                    i.putExtra("url", file_full__url);
                                    i.putExtra("type", "Attachment View");
                                    startActivity(i);
                                    overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);
                                    //-------------------- webview -------------------------------------------

                                } else {
                                    Intent i = new Intent(Intent.ACTION_VIEW);
                                    i.setData(Uri.parse(file_full__url));
                                    startActivity(i);
                                }
*/


                            }
                        });

                        expand_inner_view.addView(addView2);
                        //-----------------------------------

                    }

                    expand_layout.addView(addView);

                    Model.query_launch = "";
                }

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

            dialog = new ProgressDialog(BookingViewDetails.this);
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

            //-------------------------------------------------------------------
            String get_family_url = Model.BASE_URL + "sapp/listHReportsData?item_id=" + appr_id + "&item_type=booking&user_id=" + Model.id + "&token=" + Model.token;
            System.out.println("get_family_url---------" + get_family_url);
            new JSON_getFileList().execute(get_family_url);
            //-------------------------------------------------------------------

            dialog.dismiss();

        }
    }

   /* public void Download_file(final String file_path, final String extension) {

        try {

            final ProgressDialog dialog = new ProgressDialog(BookingViewDetails.this);

            downloadIdOne = PRDownloader.download(file_path, dirPath, "filename." + extension)

                    .build()
                    .setOnStartOrResumeListener(new OnStartOrResumeListener() {
                        @Override
                        public void onStartOrResume() {

                            dialog.setMessage("please wait");
                            dialog.show();
                            dialog.setCancelable(false);
                        }
                    })
                    .setOnPauseListener(new OnPauseListener() {
                        @Override
                        public void onPause() {
                        }
                    })
                    .setOnCancelListener(new OnCancelListener() {
                        @Override
                        public void onCancel() {
                        }
                    })
                    .setOnProgressListener(new OnProgressListener() {
                        @Override
                        public void onProgress(Progress progress) {


                        }
                    })
                    .start(new OnDownloadListener() {
                        @Override
                        public void onDownloadComplete() {

                            dialog.cancel();

                            File imgFile = new File(dirPath + "/" + "filename." + extension);
                            System.out.println("imgFile--------------" + imgFile.toString());


                            if (extension.equals("pdf") || (extension.equals("PDF"))) {
                                //-------------------- webview -------------------------------------------
                                Intent i = new Intent(getApplicationContext(), WebViewActivity.class);
                                i.putExtra("url", "file://" + imgFile.toString());
                                i.putExtra("type", "PDF View");
                                startActivity(i);
                                overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);
                                //-------------------- webview -------------------------------------------
                            } else if (extension.equals("jpg")
                                    || (extension.equals("JPG"))
                                    || (extension.equals("png"))
                                    || (extension.equals("PNG"))
                                    || (extension.equals("jpeg"))
                                    || (extension.equals("JPEG"))
                                    || (extension.equals("tiff"))
                            ) {
                                //-------------------- webview -------------------------------------------
                                Intent i = new Intent(getApplicationContext(), WebViewActivity.class);
                                i.putExtra("url", "file://" + imgFile.toString());
                                i.putExtra("type", "Attachment View");
                                startActivity(i);
                                overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);
                                //-------------------- webview -------------------------------------------
                            } else {
                                ask_to_save(file_path);
                            }
                        }

                        @Override
                        public void onError(Error error) {

                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
*/

    public void ask_to_save(final String file_path) {

        try {

            final MaterialDialog alert = new MaterialDialog(BookingViewDetails.this);
            alert.setTitle("Downloading file..");
            alert.setMessage("This file can be viewed only after download. Do you want to download now?");
            alert.setCanceledOnTouchOutside(false);
            alert.setPositiveButton("Download", new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    //--------------- Choose Folder -----------------------------------
                    final Context ctx = BookingViewDetails.this;
                    new ChooserDialog().with(ctx)
                            .withIcon(R.mipmap.ic_launcher)
                            .withFilter(true, false)
                            .withStartFile(_path)
                            .withDateFormat("HH:mm")
                            .withResources(R.string.choose, R.string.btn_ok,
                                    R.string.cancel)

                            .withChosenListener(new ChooserDialog.Result() {
                                @Override
                                public void onChoosePath(String path, File pathFile) {
                                    // Toast.makeText(ctx, "FOLDER: " + path, Toast.LENGTH_SHORT).show();
                                    _path = path;
                                    System.out.println("_path---------" + _path);

                                    //Asked_Download_file(file_path, extension, _path);
                                }
                            })
                            .build()
                            .show();
                    //--------------- Choose Folder -----------------------------------

                    alert.dismiss();


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


   /* public void Asked_Download_file(final String file_path, final String extension, final String dirPath_text) {

        try {
            downloadIdOne = PRDownloader.download(file_path, dirPath_text, "filename." + extension)
                    .build()
                    .setOnStartOrResumeListener(new OnStartOrResumeListener() {
                        @Override
                        public void onStartOrResume() {
                        }
                    })
                    .setOnPauseListener(new OnPauseListener() {
                        @Override
                        public void onPause() {
                        }
                    })
                    .setOnCancelListener(new OnCancelListener() {
                        @Override
                        public void onCancel() {
                        }
                    })
                    .setOnProgressListener(new OnProgressListener() {
                        @Override
                        public void onProgress(Progress progress) {
                        }
                    })
                    .start(new OnDownloadListener() {
                        @Override
                        public void onDownloadComplete() {

                            File imgFile = new File(dirPath_text + "/" + "filename." + extension);
                            System.out.println("imgFile--------------" + imgFile.toString());

                            System.out.println("File Saved--on------------" + imgFile.toString());

                            Toast.makeText(getApplicationContext(), "File downloaded successfully..!", Toast.LENGTH_SHORT).show();

                        }

                        @Override
                        public void onError(Error error) {

                        }
                    });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }*/


}
