/*
package com.orane.docassist;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.orane.docassist.Model.Model;
import com.orane.docassist.Network.Detector;
import com.orane.docassist.Network.JSONParser;
import com.orane.docassist.attachment_view.GridViewActivity;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;

import us.zoom.sdk.MeetingError;
import us.zoom.sdk.MeetingEvent;
import us.zoom.sdk.MeetingOptions;
import us.zoom.sdk.MeetingService;
import us.zoom.sdk.MeetingServiceListener;
import us.zoom.sdk.ZoomError;
import us.zoom.sdk.ZoomSDK;
import us.zoom.sdk.ZoomSDKInitializeListener;

public class UpcomingConsView extends AppCompatActivity implements com.orane.docassist.zoom.Constants, ZoomSDKInitializeListener, MeetingServiceListener {


    private Toolbar toolbar;
    TextView tv_fname_only, tv_filename, tv_start_text, tv_ext, cons_tit, tvdate, tvtime, tvpatientgeo, tvpatient, tvcasedets, tvattached, tvconsdate, tvconsmode, tvconsstatus, tvnotes;
    Button btn_video_end, btn_phone_end, btn_direct_end, btnJoinMeeting, btn_start_phone_cons, btn_start_video_cons, btnwrite_notes;
    ImageView consult_mode_img;
    public String file_full_url, attach_file_text, extension, file_name_only, z_meetingno_text, z_meetingno, appt_status, err_status, z_status, meetingNo, z_meeting_id, consid, z_host_token_text, z_host_id_text, video_cons_id, cons_view_type, err_text, note_time_text, note_text, url;
    public String token_text, url_meeting_id, sub_url = "";
    public String open_url_text, session_id_text, Log_Status, str_response, status_value, call_status, notes_jsonobj_text, pres_jsonobj_text, files_jsonobj_text, filename, file_title, file_ext, appr_id, patient_id, str_cons_type, cons_id, pgeo, cons_patient, cons_date, cons_time, cons_type, cons_case_dets, cons_status, presc;
    JSONObject jsonoj_endcons, get_meetingid_jsonobj, call_jsonobj, call_json, jsonobj_files, consview_jsonobj, appt_jsonobj;
    View vi_files;
    ImageView file_image;
    LinearLayout direct_cons_layout, phone_cons_layout, video_cons_layout, end_call_layout, start_cons_layout, start_call_layout, files_full_layout, files_layout, notes_layout;
    ScrollView full_layout;
    ProgressBar progressBar;
    private final static String TAG = "Zoom SDK Example";
    private EditText mEdtMeetingNo;
    private EditText mEdtMeetingPassword;

    private final static int STYPE = MeetingService.USER_TYPE_ZOOM;
    private final static String DISPLAY_NAME = "ZoomUS SDK";


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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (new Detector().isTablet(getApplicationContext())) {
            setContentView(R.layout.tab_consview_new);
        } else {
            setContentView(R.layout.consview_new);
        }


        //================ Shared Pref ======================
        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        Log_Status = sharedpreferences.getString(Login_Status, "");
        Model.name = sharedpreferences.getString(Name, "");
        Model.id = sharedpreferences.getString(id, "");
        Model.email = sharedpreferences.getString(email, "");
        Model.browser_country = sharedpreferences.getString(browser_country, "");
        //============================================================

        //================ Shared Pref ======================
        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        Model.token = sharedpreferences.getString(token, "");
        System.out.println("Model.token----------------------" + Model.token);
        //================ Shared Pref ======================

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.app_color));
        }

        try {
            //----------------------------------------------------------------
            Bundle bundle = getIntent().getExtras();
            consid = bundle.getString("consid");
            cons_view_type = bundle.getString("cons_view_type");

            System.out.println("get consid------------" + consid);
            System.out.println("get cons_view_type------------" + cons_view_type);
            //----------------------------------------------------------------

        } catch (Exception ee) {
            ee.printStackTrace();
        }

        if (savedInstanceState == null) {
            ZoomSDK sdk = ZoomSDK.getInstance();
            sdk.initialize(this, APP_KEY, APP_SECRET, WEB_DOMAIN, UpcomingConsView.this);
            sdk.setDropBoxAppKeyPair(this, DROPBOX_APP_KEY, DROPBOX_APP_SECRET);
            sdk.setOneDriveClientId(this, ONEDRIVE_CLIENT_ID);
            //sdk.setGoogleDriveClientId(this, GOOGLE_DRIVE_CLIENT_ID);
        } else {
            registerMeetingServiceListener();
        }


        //------------ Object Creations -------------------------------
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setTitle("Consultation Room");
        }
        //------------ Object Creations -------------------------------

        //---------------------- Object Creation -----------------------
        btnJoinMeeting = (Button) findViewById(R.id.btnJoinMeeting);
        btn_video_end = (Button) findViewById(R.id.btn_video_end);
        btn_phone_end = (Button) findViewById(R.id.btn_phone_end);
        btn_direct_end = (Button) findViewById(R.id.btn_direct_end);


        btn_start_phone_cons = (Button) findViewById(R.id.btn_start_phone_cons);
        start_cons_layout = (LinearLayout) findViewById(R.id.start_cons_layout);
        start_call_layout = (LinearLayout) findViewById(R.id.start_call_layout);
        end_call_layout = (LinearLayout) findViewById(R.id.end_call_layout);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        full_layout = (ScrollView) findViewById(R.id.full_LinearLayout);
        files_full_layout = (LinearLayout) findViewById(R.id.files_full_layout);
        files_layout = (LinearLayout) findViewById(R.id.files_layout);
        notes_layout = (LinearLayout) findViewById(R.id.notes_layout);
        video_cons_layout = (LinearLayout) findViewById(R.id.video_cons_layout);
        phone_cons_layout = (LinearLayout) findViewById(R.id.phone_cons_layout);
        direct_cons_layout = (LinearLayout) findViewById(R.id.direct_cons_layout);

        tv_start_text = (TextView) findViewById(R.id.tv_start_text);
        cons_tit = (TextView) findViewById(R.id.cons_tit);
        tvdate = (TextView) findViewById(R.id.tvdate);
        tvtime = (TextView) findViewById(R.id.tvtime);
        btnwrite_notes = (Button) findViewById(R.id.btnwrite_notes);
        consult_mode_img = (ImageView) findViewById(R.id.consult_mode_img);
        tvpatientgeo = (TextView) findViewById(R.id.tvpatientgeo);
        tvpatient = (TextView) findViewById(R.id.tvpatient);
        tvcasedets = (TextView) findViewById(R.id.tvcasedets);
        tvattached = (TextView) findViewById(R.id.tvattached);
        tvconsdate = (TextView) findViewById(R.id.tvconsdate);
        tvconsmode = (TextView) findViewById(R.id.tvconsmode);
        tvconsstatus = (TextView) findViewById(R.id.tvconsstatus);
        tvnotes = (TextView) findViewById(R.id.tvnotes);
        tv_filename = (TextView) findViewById(R.id.tv_filename);

        files_full_layout.setVisibility(View.GONE);
        notes_layout.setVisibility(View.GONE);


        Typeface font_reg = Typeface.createFromAsset(getAssets(), Model.font_name);
        Typeface font_bold = Typeface.createFromAsset(getAssets(), Model.font_name_bold);

        ((TextView) findViewById(R.id.cons_tit)).setTypeface(font_bold);
        ((TextView) findViewById(R.id.tv_datelab)).setTypeface(font_bold);
        ((TextView) findViewById(R.id.tvdate)).setTypeface(font_reg);
        ((TextView) findViewById(R.id.tv_timelab)).setTypeface(font_bold);
        ((TextView) findViewById(R.id.tvtime)).setTypeface(font_reg);
        ((TextView) findViewById(R.id.tv_opat_header)).setTypeface(font_bold);
        ((TextView) findViewById(R.id.tvpatient)).setTypeface(font_bold);
        ((TextView) findViewById(R.id.tvpatientgeo)).setTypeface(font_reg);
        ((TextView) findViewById(R.id.tv_curr)).setTypeface(font_bold);
        ((TextView) findViewById(R.id.tvconsstatus)).setTypeface(font_bold);
        ((TextView) findViewById(R.id.tv_case_lab)).setTypeface(font_bold);
        ((TextView) findViewById(R.id.tvcasedets)).setTypeface(font_reg);
        ((TextView) findViewById(R.id.tvattached)).setTypeface(font_bold);
        ((TextView) findViewById(R.id.tv_start_text)).setTypeface(font_reg);

        btn_direct_end.setTypeface(font_bold);
        btn_phone_end.setTypeface(font_bold);
        btn_start_phone_cons.setTypeface(font_bold);
        btn_video_end.setTypeface(font_bold);
        btnJoinMeeting.setTypeface(font_bold);

        //-----------------------------------------------------------------
        if (consid != null && !consid.isEmpty() && !consid.equals("null") && !consid.equals("")) {
            if ((Model.id) != null && !(Model.id).isEmpty() && !(Model.id).equals("null") && !(Model.id).equals("")) {
                //------------------------- Url Pass -------------------------
                url = Model.BASE_URL + "sapp/jsonviewconsult4doc?user_id=" + (Model.id) + "&id=" + consid + "&token=" + Model.token;
                System.out.println("Cond View url------" + url);
                new JSON_ViewCons().execute(url);
                //------------------------- Url Pass -------------------------
            } else {
                Toast.makeText(getApplicationContext(), "Something went wrong. Please try again.", Toast.LENGTH_LONG).show();
                ask_logout();
            }

        } else {
            Toast.makeText(getApplicationContext(), "Something went wrong. Please try again.", Toast.LENGTH_LONG).show();
            //ask_logout();
        }
        //-----------------------------------------------------------------

        //-------- Auto Scroll to UP------------------
        full_layout.post(new Runnable() {
            @Override
            public void run() {
                full_layout.fullScroll(ScrollView.FOCUS_UP);
            }
        });
        //-------- Auto Scroll to UP------------------


        //--------------------------------- Phone Call Start -------------------------------------------------
        btn_start_phone_cons.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (appr_id != null && !appr_id.isEmpty() && !appr_id.equals("null") && !appr_id.equals("")) {

                    try {
                        call_json = new JSONObject();
                        call_json.put("user_id", (Model.id));
                        call_json.put("appt_id", appr_id);
                        System.out.println("call_json------" + call_json.toString());

                        new Call_Connect_JSON().execute(call_json);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Call is not getting connected", Toast.LENGTH_LONG).show();
                    System.out.println("appr_id------" + appr_id);
                }

            }
        });
        //--------------------------------- Video Call Start -------------------------------------------------
btn_start_video_cons.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (appr_id != null && !appr_id.isEmpty() && !appr_id.equals("null") && !appr_id.equals("")) {

                    try {
                        call_json = new JSONObject();
                        call_json.put("user_id", (Model.id));
                        call_json.put("appt_id", appr_id);
                        System.out.println("call_json------" + call_json.toString());

                        Intent intent = new Intent(MainActivity.this, UpcomingConsView.class);
                        //intent.putExtra("url", url);
                        //intent.putExtra("file_ext", file_ext);
                        startActivity(intent);

                        //new Vide_Call_Connect_JSON().execute(call_json);

                    } catch (Exception e) {
                        System.out.println("Video Call Exception------" + e.toString());
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Sorry...! Video Call could not be connected. Try Again", Toast.LENGTH_LONG).show();
                    System.out.println("appr_id------" + appr_id);
                }

            }
        });

        //--------------------------------- Video Call Start -------------------------------------------------
        btn_video_end.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ask_end();
            }
        });

        btn_phone_end.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ask_end();
            }
        });
        btn_direct_end.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ask_end();
            }
        });



        //---------------------- Object Creation -----------------------
        btnwrite_notes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(UpcomingConsView.this, WriteNotesActivity.class);
                startActivity(i);

                overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);
            }
        });



    }

    private void registerMeetingServiceListener() {
        ZoomSDK zoomSDK = ZoomSDK.getInstance();
        MeetingService meetingService = zoomSDK.getMeetingService();
        if (meetingService != null) {
            meetingService.addListener(this);
        }
    }

    @Override
    public void onZoomSDKInitializeResult(int errorCode, int internalErrorCode) {
        // Log.i(TAG, "onZoomSDKInitializeResult, errorCode=" + errorCode + ", internalErrorCode=" + internalErrorCode);

        if (errorCode != ZoomError.ZOOM_ERROR_SUCCESS) {
            // Toast.makeText(this, "Failed to initialize. Error: " + errorCode + ", internalErrorCode=" + internalErrorCode, Toast.LENGTH_LONG).show();
        } else {
            //Toast.makeText(this, "Initialize Zoom SDK successfully.", Toast.LENGTH_LONG).show();

            registerMeetingServiceListener();
        }
    }

    @Override
    protected void onDestroy() {
        ZoomSDK zoomSDK = ZoomSDK.getInstance();

        if (zoomSDK.isInitialized()) {
            MeetingService meetingService = zoomSDK.getMeetingService();
            meetingService.removeListener(this);
        }

        super.onDestroy();
    }

    public void onClickBtnJoinMeeting(View view) {


        if (isInternetOn()) {
            try {
                //------------------------- Url Pass ------------------------- open_zoom();
                String get_meet_url = Model.BASE_URL + "sapp/getzid?appt_id=" + appr_id + "&user_id=" + (Model.id) + "&token=" + Model.token;
                System.out.println("Cond get_meet_url------" + get_meet_url);
                new Get_Meeting_id().execute(get_meet_url);
                //------------------------- Url Pass -------------------------
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }



        String meetingNo = mEdtMeetingNo.getText().toString().trim();
        String meetingPassword = mEdtMeetingPassword.getText().toString().trim();

        meetingNo = z_meeting_id;
        String meetingPassword = "";

        if (meetingNo.length() == 0) {
            Toast.makeText(this, "You cannot continue to this consultation. please Tryagain.", Toast.LENGTH_LONG).show();
            return;
        }

        ZoomSDK zoomSDK = ZoomSDK.getInstance();

        if (!zoomSDK.isInitialized()) {
            Toast.makeText(this, "ZoomSDK has not been initialized successfully", Toast.LENGTH_LONG).show();
            return;
        }

        MeetingService meetingService = zoomSDK.getMeetingService();
        MeetingOptions opts = new MeetingOptions();
        int ret = meetingService.joinMeeting(this, meetingNo, DISPLAY_NAME, meetingPassword, opts);

    }

    public void onClickBtnStartMeeting(View view) {
        String meetingNo = mEdtMeetingNo.getText().toString().trim();

        if (meetingNo.length() == 0) {
            Toast.makeText(this, "You need to enter a scheduled meeting number.", Toast.LENGTH_LONG).show();
            return;
        }

        ZoomSDK zoomSDK = ZoomSDK.getInstance();

        if (!zoomSDK.isInitialized()) {
            Toast.makeText(this, "ZoomSDK has not been initialized successfully", Toast.LENGTH_LONG).show();
            return;
        }

        MeetingService meetingService = zoomSDK.getMeetingService();

        MeetingOptions opts = new MeetingOptions();
//		opts.no_driving_mode = true;
//		opts.no_invite = true;
//		opts.no_meeting_end_message = true;
//		opts.no_titlebar = true;
//		opts.no_bottom_toolbar = true;
//		opts.no_dial_in_via_phone = true;
//		opts.no_dial_out_to_phone = true;
//		opts.no_disconnect_audio = true;
//		opts.invite_options = InviteOptions.INVITE_ENABLE_ALL;
//		opts.meeting_views_options = MeetingViewsOptions.NO_BUTTON_SHARE + MeetingViewsOptions.NO_BUTTON_VIDEO;

        int ret = meetingService.startMeeting(this, USER_ID, ZOOM_TOKEN, STYPE, meetingNo, DISPLAY_NAME, opts);

        Log.i(TAG, "onClickBtnStartMeeting, ret=" + ret);
    }

    public void onClickBtnStartInstantMeeting(View view) {


        if (appr_id != null && !appr_id.isEmpty() && !appr_id.equals("null") && !appr_id.equals("")) {

            try {
                call_json = new JSONObject();
                call_json.put("user_id", (Model.id));
                call_json.put("appt_id", appr_id);
                System.out.println("call_json------" + call_json.toString());

                new Video_Call_Connect_JSON().execute(call_json);

            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            Toast.makeText(getApplicationContext(), "Sorry...! Video Call could not be connected. Try Again", Toast.LENGTH_LONG).show();
            System.out.println("appr_id------" + appr_id);
        }


  ZoomSDK zoomSDK = ZoomSDK.getInstance();

        if (!zoomSDK.isInitialized()) {
            //Toast.makeText(this, "ZoomSDK has not been initialized successfully", Toast.LENGTH_LONG).show();
            return;
        }

        MeetingService meetingService = zoomSDK.getMeetingService();

        MeetingOptions opts = new MeetingOptions();
//		opts.no_driving_mode = true;
//		opts.no_invite = true;
//		opts.no_meeting_end_message = true;
//		opts.no_titlebar = true;
//		opts.no_bottom_toolbar = true;
//		opts.no_dial_in_via_phone = true;
//		opts.no_dial_out_to_phone = true;
//		opts.no_disconnect_audio = true;
//		opts.invite_options = InviteOptions.INVITE_DISABLE_ALL;
//		opts.meeting_views_options = MeetingViewsOptions.NO_BUTTON_SHARE + MeetingViewsOptions.NO_BUTTON_VIDEO;


        int ret = meetingService.startMeeting(this, USER_ID, ZOOM_TOKEN, STYPE, DISPLAY_NAME, opts);

        Log.i(TAG, "onClickBtnStartMeeting, ret=" + ret);

    }

    @Override
    public void onMeetingEvent(int meetingEvent, int errorCode,
                               int internalErrorCode) {

        if (meetingEvent == MeetingEvent.MEETING_CONNECT_FAILED && errorCode == MeetingError.MEETING_ERROR_CLIENT_INCOMPATIBLE) {
            Toast.makeText(this, "Version of ZoomSDK is too low!", Toast.LENGTH_LONG).show();
        }
    }




    public void ask_logout() {
new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)
                .setTitleText("Oops!")
                .setContentText("Something went wrong. You need to logout and login again to proceed.")
                //.setCancelText("No,cancel")
                .setConfirmText("Yes,logout!")
                //.showCancelButton(true)

.setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {
                        sDialog.dismiss();
                    }
                })
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {

                        Model.kiss.record("android.doc.Went_Wrong_Logout");
                        //============================================================
                        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedpreferences.edit();
                        editor.putString(Login_Status, "0");
                        editor.apply();
                        //============================================================
                        finishAffinity();
                        Intent i = new Intent(UpcomingConsView.this, LoginActivity.class);
                        startActivity(i);
                        sDialog.dismiss();
                        finish();
                    }
                })
                .show();

    }





    class Get_Meeting_id extends AsyncTask<String, Void, Boolean> {

        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            dialog = new ProgressDialog(UpcomingConsView.this);
            dialog.setMessage("Connecting..., please wait");
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

                get_meetingid_jsonobj = new JSONObject(str_response);

                if (get_meetingid_jsonobj.has("token_status")) {
                    String token_status = get_meetingid_jsonobj.getString("token_status");
                    if (token_status.equals("0")) {

                        //============================================================
                        SharedPreferences.Editor editor = sharedpreferences.edit();
                        editor.putString(Login_Status, "0");
                        editor.apply();
                        //============================================================

                        finishAffinity();
                        Intent intent = new Intent(UpcomingConsView.this, LoginActivity.class);
                        startActivity(intent);
                        finish();
                    }
                } else {


                    //-------------------------------------------------------
                    if (get_meetingid_jsonobj.has("open_url")) {
                        open_url_text = get_meetingid_jsonobj.getString("open_url");
                        System.out.print("open_url_text------------" + open_url_text);
                    } else {
                        open_url_text = "";
                    }
                    if (get_meetingid_jsonobj.has("z_meeting_id")) {
                        z_meeting_id = get_meetingid_jsonobj.getString("z_meeting_id");
                        System.out.println("get z_meeting_id----------" + z_meeting_id);
                    }
                    if (get_meetingid_jsonobj.has("z_status")) {
                        z_status = get_meetingid_jsonobj.getString("z_status");
                        System.out.println("get z_status----------" + z_status);
                    }
                    //-------------------------------------------------------

                    System.out.println("get z_meeting_id----------" + z_meeting_id);
                    System.out.println("get z_status----------" + z_status);
                    System.out.println("get open_url_text----------" + open_url_text);


                    if (get_meetingid_jsonobj.has("appt_status")) {
                        appt_status = get_meetingid_jsonobj.getString("appt_status");
                        System.out.println("get appt_status----------" + appt_status);
                    }
                    if (get_meetingid_jsonobj.has("status")) {
                        err_status = get_meetingid_jsonobj.getString("status");
                        System.out.println("get err_status----------" + err_status);
                    }


                    if (z_status.equals("1")) {

                        if (open_url_text != null && !open_url_text.isEmpty() && !open_url_text.equals("null") && !open_url_text.equals("")) {

                            System.out.println("Open with WebRtc SDK------------" + open_url_text);

                            Intent i = new Intent(Intent.ACTION_VIEW);
                            i.setData(Uri.parse(open_url_text));
                            startActivity(i);



                            Uri uri = Uri.parse(open_url_text);
                            String server = uri.getAuthority();
                            String path = uri.getPath();
                            String protocol = uri.getScheme();
                            Set<String> args = uri.getQueryParameterNames();


                            url_meeting_id = uri.getQueryParameter("id");
                            System.out.println("url_meeting_id---------------" + url_meeting_id);

                            try {
                                //-------------------------get Meeting Token------------------------
                                String get_meet_url = Model.BASE_URL + "sapp/getWebRtcToken?id=" + url_meeting_id;
                                System.out.println("Cond get_meet_url------" + get_meet_url);
                                new Get_Token().execute(get_meet_url);
                                //-------------------------get Meeting Token------------------------

                            } catch (Exception e2) {
                                e2.printStackTrace();
                            }

                        } else {
                            if (z_meeting_id != null && !z_meeting_id.isEmpty() && !z_meeting_id.equals("null") && !z_meeting_id.equals("null")) {
                                System.out.println("Open with Zoom ------------" + z_meeting_id);
                                join_video_cons();
                            }
                        }

                    } else {
                        Toast.makeText(getApplicationContext(), "Sorry, The Consultation is not started yet, Please wait for another Notification to come", Toast.LENGTH_LONG).show();
                    }


                    if (z_status.equals("0")) {

                        //join_video_cons();

                    } else if (z_status.equals("1")) {


                        if (z_meeting_id != null && !z_meeting_id.isEmpty() && !z_meeting_id.equals("null") && !z_meeting_id.equals("null")) {
                            join_video_cons();
                        } else {
                            join_video_cons();
                        }
                    } else {
                        Toast.makeText(getApplicationContext(), "Sorry...! Call Cannot be Connected Try again..!", Toast.LENGTH_LONG).show();
                        System.out.println("Call Cannot be Connected.....");
                    }

                }
            } catch (Exception e) {
                System.out.println("Call Error....." + e.toString());
            }

            dialog.cancel();

        }
    }


    class Video_Call_Connect_JSON extends AsyncTask<JSONObject, Void, Boolean> {

        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            dialog = new ProgressDialog(UpcomingConsView.this);
            dialog.setMessage("Connecting..., please wait");
            dialog.show();
            dialog.setCancelable(false);
        }

        @Override
        protected Boolean doInBackground(JSONObject... urls) {

            try {

                JSONParser jParser = new JSONParser();
                call_jsonobj = jParser.JSON_POST(urls[0], "hostClickVideo");
                System.out.println("Video call_jsonobj----------" + call_jsonobj.toString());

                return true;
            } catch (Exception e) {
                e.printStackTrace();
            }
            return false;
        }

        protected void onPostExecute(Boolean result) {

            try {

                //call_jsonobj = new JSONObject(str_response);

                if (call_jsonobj.has("token_status")) {
                    String token_status = call_jsonobj.getString("token_status");
                    if (token_status.equals("0")) {

                        //============================================================
                        SharedPreferences.Editor editor = sharedpreferences.edit();
                        editor.putString(Login_Status, "0");
                        editor.apply();
                        //============================================================

                        finishAffinity();
                        Intent intent = new Intent(UpcomingConsView.this, LoginActivity.class);
                        startActivity(intent);
                        finish();
                    }
                } else {

                    //-------------------------------------------------------
                    if (call_jsonobj.has("status")) {
                        call_status = call_jsonobj.getString("status");
                    }
                    if (call_jsonobj.has("id")) {
                        video_cons_id = call_jsonobj.getString("id");
                    }
                    if (call_jsonobj.has("z_host_id")) {
                        z_host_id_text = call_jsonobj.getString("z_host_id");
                    }
                    if (call_jsonobj.has("z_host_token")) {
                        z_host_token_text = call_jsonobj.getString("z_host_token");
                    }
                    if (call_jsonobj.has("z_meeting_id")) {
                        z_meetingno_text = call_jsonobj.getString("z_meeting_id");
                    }

                    //-------------------------------------------------------
                    System.out.println("Video call_status----------" + call_status);
                    System.out.println("Video video_cons_id----------" + video_cons_id);
                    System.out.println("Video z_host_id_text----------" + z_host_id_text);
                    System.out.println("Video z_host_token_text----------" + z_host_token_text);
                    System.out.println("Video z_meetingno_text----------" + z_meetingno_text);


                    if (call_jsonobj.has("err")) {
                        err_text = call_jsonobj.getString("err");
                    }

                    if (call_status.equals("1")) {
                        System.out.println("Video Call is Connected.....");
                        Toast.makeText(getApplicationContext(), "Call Connected.", Toast.LENGTH_LONG).show();
                        end_call_layout.setVisibility(View.GONE);
                        start_call_layout.setVisibility(View.GONE);

                        open_zoom();


                    } else {

                        Toast.makeText(getApplicationContext(), "Oops! Problem with the connectivity. Please try again.", Toast.LENGTH_LONG).show();
                        System.out.println("Call Error text....." + err_text);

                    }
                }
            } catch (Exception e) {
                System.out.println("Call Error....." + e.toString());
                e.printStackTrace();
            }

            dialog.cancel();

        }
    }


    class Call_Connect_JSON extends AsyncTask<JSONObject, Void, Boolean> {

        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            dialog = new ProgressDialog(UpcomingConsView.this);
            dialog.setMessage("Connecting..., please wait");
            dialog.show();
            dialog.setCancelable(false);
        }

        @Override
        protected Boolean doInBackground(JSONObject... urls) {

            try {

                JSONParser jParser = new JSONParser();
                call_jsonobj = jParser.JSON_POST(urls[0], "dialappt");
                System.out.println("call_jsonobj----------" + call_jsonobj.toString());

                return true;
            } catch (Exception e) {
                e.printStackTrace();
            }
            return false;
        }

        protected void onPostExecute(Boolean result) {

            try {

                if (call_jsonobj.has("token_status")) {
                    String token_status = call_jsonobj.getString("token_status");
                    if (token_status.equals("0")) {

                        //============================================================
                        SharedPreferences.Editor editor = sharedpreferences.edit();
                        editor.putString(Login_Status, "0");
                        editor.apply();
                        //============================================================

                        finishAffinity();
                        Intent intent = new Intent(UpcomingConsView.this, LoginActivity.class);
                        startActivity(intent);
                        finish();
                    }

                } else {

                    call_status = call_jsonobj.getString("status");
                    System.out.println("call_status----------" + call_status);

                    if (call_jsonobj.has("err")) {
                        err_text = call_jsonobj.getString("err");
                    }

                    if (call_status.equals("1")) {
                        System.out.println("Call is Connected.....");
                        Toast.makeText(getApplicationContext(), "Please Wait. Call is connected.", Toast.LENGTH_LONG).show();

                        end_call_layout.setVisibility(View.GONE);
                        start_call_layout.setVisibility(View.GONE);


                    } else {

                        Toast.makeText(getApplicationContext(), "Oops! Problem with the connectivity. Please try again.", Toast.LENGTH_LONG).show();
                        System.out.println("Call Error text....." + err_text);

                    }
                }
            } catch (Exception e) {
                System.out.println("Call Error....." + e.toString());
                e.printStackTrace();
            }

            dialog.cancel();

        }
    }


    class JSON_ViewCons extends AsyncTask<String, Void, Boolean> {

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

                JSONParser jParser = new JSONParser();
                consview_jsonobj = jParser.getJSON_URL(urls[0]);


                return true;
            } catch (Exception e) {
                e.printStackTrace();
            }
            return false;
        }

        protected void onPostExecute(Boolean result) {

            try {

                consview_jsonobj = new JSONObject(str_response);

                if (consview_jsonobj.has("token_status")) {
                    String token_status = consview_jsonobj.getString("token_status");
                    if (token_status.equals("0")) {

                        //============================================================
                        SharedPreferences.Editor editor = sharedpreferences.edit();
                        editor.putString(Login_Status, "0");
                        editor.apply();
                        //============================================================

                        finishAffinity();
                        Intent intent = new Intent(UpcomingConsView.this, LoginActivity.class);
                        startActivity(intent);
                        finish();
                    }
                } else {

                    appt_jsonobj = consview_jsonobj.getJSONObject("appt");
                    //------------- get Appt Details ------------------------------------------------
                    appr_id = appt_jsonobj.getString("id");
                    patient_id = appt_jsonobj.getString("patient_id");
                    cons_patient = appt_jsonobj.getString("patient_name");
                    pgeo = appt_jsonobj.getString("patient_geo");
                    cons_date = appt_jsonobj.getString("consult_date");
                    cons_time = appt_jsonobj.getString("consult_time");
                    cons_type = appt_jsonobj.getString("consult_type");
                    str_cons_type = appt_jsonobj.getString("str_consult_type");
                    cons_case_dets = appt_jsonobj.getString("case");
                    status_value = appt_jsonobj.getString("status");
                    cons_status = appt_jsonobj.getString("str_status");
                    //------------- get Appt Details ------------------------------------------------

                    System.out.println("cons_type--------" + cons_type);

                    //--------- Cons Image -------------------
                    if (cons_type.equals("4")) { //---------- Phone ---------------------
                        consult_mode_img.setBackgroundResource(R.mipmap.phone_cons_ico_color);
                        video_cons_layout.setVisibility(View.GONE);
                        phone_cons_layout.setVisibility(View.VISIBLE);
                        direct_cons_layout.setVisibility(View.GONE);
                        cons_tit.setText("Phone Consultation Schedule");
                        tv_start_text.setText("Please click the below button, in one or two minutes before the scheduled time.");
                    } else if (cons_type.equals("0")) {
                        consult_mode_img.setBackgroundResource(R.mipmap.video_cons_ico_color);
                        video_cons_layout.setVisibility(View.VISIBLE);
                        phone_cons_layout.setVisibility(View.GONE);
                        direct_cons_layout.setVisibility(View.GONE);
                        cons_tit.setText("Video Consultation Schedule");
                        tv_start_text.setText("Please click the below button, in one or two minutes before the scheduled time.");
                    } else if (cons_type.equals("2")) {
                        consult_mode_img.setBackgroundResource(R.mipmap.direct_cons_ico);
                        cons_tit.setText("Direct Consultation Schedule");
                        video_cons_layout.setVisibility(View.GONE);
                        phone_cons_layout.setVisibility(View.GONE);
                        direct_cons_layout.setVisibility(View.VISIBLE);
                        tv_start_text.setText("Please click the below button to End your consultation");
                    } else {
                        consult_mode_img.setBackgroundResource(R.mipmap.direct_cons_ico);
                        cons_tit.setText("Direct Consultation Schedule");
                        video_cons_layout.setVisibility(View.GONE);
                        phone_cons_layout.setVisibility(View.GONE);
                        direct_cons_layout.setVisibility(View.VISIBLE);
                        tv_start_text.setText("Please click the below button to End your consultation");
                    }

else {
                    consult_mode_img.setBackgroundResource(R.mipmap.consult_icon_color);
                    cons_tit.setText("Consultation Schedule");
                    start_cons_layout.setVisibility(View.GONE);
                    btn_start_phone_cons.setVisibility(View.GONE);
                    btnJoinMeeting.setVisibility(View.GONE);
                }

                    //--------- Cons Image -------------------

                    if (status_value.equals("1")) {
                        start_cons_layout.setVisibility(View.VISIBLE);
                    } else if (status_value.equals("2")) {
                        start_cons_layout.setVisibility(View.VISIBLE);
                    } else {
                        start_cons_layout.setVisibility(View.GONE);
                    }

                    if (consview_jsonobj.has("z_meeting_id")) {
                        z_meeting_id = consview_jsonobj.getString("z_meeting_id");
                        System.out.println("z_meeting_id--------" + z_meeting_id);
                        start_cons_layout.setVisibility(View.VISIBLE);
                    }

                    System.out.println("appr_id--------" + appr_id);

                    tvpatient.setText(cons_patient);
                    tvpatientgeo.setText(pgeo);
                    tvdate.setText(cons_date);
                    tvtime.setText(cons_time);
                    tvconsmode.setText(str_cons_type);
                    tvcasedets.setText(Html.fromHtml(cons_case_dets));
                    tvconsstatus.setText(cons_status);

                    if (consview_jsonobj.has("notes")) {
                        notes_jsonobj_text = consview_jsonobj.getString("notes");

                        //---------------- notes ---------------------------------------
                        if ((notes_jsonobj_text.length()) > 2) {
                            notes_layout.setVisibility(View.VISIBLE);
                            JSONArray jarray_files = consview_jsonobj.getJSONArray("notes");
                            for (int j = 0; j < jarray_files.length(); j++) {
                                jsonobj_files = jarray_files.getJSONObject(j);

                                note_text = jsonobj_files.getString("note");
                                note_time_text = jsonobj_files.getString("time");

                                System.out.println("note_text--------" + note_text);
                                System.out.println("note_time_text--------" + note_time_text);

                                tvnotes.setText(note_text);
                            }
                        } else {
                            notes_layout.setVisibility(View.GONE);
                        }
                        //---------------- ---------------------------------------
                    } else {
                        notes_layout.setVisibility(View.GONE);
                    }


                    if (consview_jsonobj.has("files")) {
                        files_jsonobj_text = consview_jsonobj.getString("files");

                        //---------------- Files ---------------------------------------
                        if ((files_jsonobj_text.length()) > 2) {

                            files_full_layout.setVisibility(View.VISIBLE);
                            JSONArray jarray_files = consview_jsonobj.getJSONArray("files");

                            System.out.println("jsonobj_items------" + jarray_files.toString());
                            System.out.println("jarray_files.length()------" + jarray_files.length());

                            tvattached.setText("Attached " + jarray_files.length() + " File(s)");
                            attach_file_text = "";

                            tvattached.setText("Smile");

                            for (int j = 0; j < jarray_files.length(); j++) {
                                jsonobj_files = jarray_files.getJSONObject(j);
                                System.out.println("jsonobj_files------" + j + " ----" + jsonobj_files.toString());

                                filename = jsonobj_files.getString("filename");
                                file_title = jsonobj_files.getString("file_title");
                                file_ext = jsonobj_files.getString("ext");

                                if (jsonobj_files.has("url")) {
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
                                }

                                System.out.println("filename--------" + filename);
                                System.out.println("file_title--------" + file_title);
                                System.out.println("file_ext--------" + file_ext);
                                System.out.println("file_full_url--------" + file_full_url);

 vi_files = getLayoutInflater().inflate(R.layout.attached_file_list, null);
                            file_image = (ImageView) vi_files.findViewById(R.id.file_image);
                            tv_filename = (TextView) vi_files.findViewById(R.id.tv_filename);
                            tv_ext = (TextView) vi_files.findViewById(R.id.tv_ext);
                            //tv_userid = (TextView) vi_files.findViewById(R.id.tv_userid);
                            //tv_fname_only = (TextView) vi_files.findViewById(R.id.tv_fname_only);

                            tv_filename.setText(filename + "." + file_ext);
                            tv_ext.setText(file_ext);
                            //tv_fname_only.setText(""+filename);

                            files_layout.addView(vi_files);

                            }

                            files_layout.setVisibility(View.GONE);
                            tv_filename.setText(attach_file_text);

                        } else {
                            files_full_layout.setVisibility(View.GONE);
                        }
                    } else {
                        files_full_layout.setVisibility(View.GONE);
                    }
                    //-------------------------------------------------------


                    pres_jsonobj_text = consview_jsonobj.getString("prescription");

                }
            } catch (Exception e) {
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

        if (id == R.id.notify) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    public void open_zoom() {

        if (z_meetingno_text.length() == 0) {
            Toast.makeText(this, "You cannot continue to this consultation. please Tryagain.", Toast.LENGTH_LONG).show();
            return;
        }

        ZoomSDK zoomSDK = ZoomSDK.getInstance();
        if (!zoomSDK.isInitialized()) {
            Toast.makeText(this, "ZoomSDK has not been initialized successfully", Toast.LENGTH_LONG).show();
            return;
        }

        MeetingService meetingService = zoomSDK.getMeetingService();
        MeetingOptions opts = new MeetingOptions();

        System.out.println("Inside zoom--z_host_id_text------" + z_host_id_text);
        System.out.println("Inside zoom--z_host_token_text------" + z_host_token_text);
        System.out.println("Inside zoom--z_meetingno_text------" + z_meetingno_text);
        System.out.println("Inside zoom---STYPE------" + STYPE);
        System.out.println("Inside zoom---DISPLAY_NAME------" + Model.name);
        System.out.println("Inside zoom---opts------" + opts.toString());

        int ret = meetingService.startMeeting(this, z_host_id_text, z_host_token_text, STYPE, z_meetingno_text, "(doctor)" + (Model.name), opts);
        System.out.println("Inside zoom---ret------" + ret);

    }

    public void join_video_cons() {

        meetingNo = z_meeting_id;
        String meetingPassword = "";

        if (meetingNo.length() == 0) {
            Toast.makeText(this, "You cannot continue to this consultation. please Try again.", Toast.LENGTH_LONG).show();
            return;
        }

        ZoomSDK zoomSDK = ZoomSDK.getInstance();

        if (!zoomSDK.isInitialized()) {
            //Toast.makeText(this, "ZoomSDK has not been initialized successfully", Toast.LENGTH_LONG).show();
            return;
        }

        System.out.println("Inside zoom--meetingNo------" + meetingNo);
        System.out.println("Inside zoom---DISPLAY_NAME------" + Model.name);

        MeetingService meetingService = zoomSDK.getMeetingService();
        MeetingOptions opts = new MeetingOptions();
        System.out.println("opts-----------" + opts.toString());

        System.out.println("meetingNo---------" + meetingNo.toString() );
        System.out.println("Model.name---------" + Model.name);
        System.out.println("meetingPassword---------" + meetingPassword);
        System.out.println("opts---------" + opts);

        int ret = meetingService.joinMeeting(this, meetingNo, Model.name, meetingPassword, opts);

        System.out.println("ret---------" + ret);
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


    public void init_video_cons() {

        if ((Model.id) != null && !(Model.id).isEmpty() && !(Model.id).equals("null") && !(Model.id).equals("")) {
            if (appr_id != null && !appr_id.isEmpty() && !appr_id.equals("null") && !appr_id.equals("")) {
                try {
                    call_json = new JSONObject();
                    call_json.put("user_id", (Model.id));
                    call_json.put("appt_id", appr_id);
                    System.out.println("call_json------" + call_json.toString());

                    new Video_Call_Connect_JSON().execute(call_json);

                } catch (Exception e) {
                    System.out.println("Video Call Exception------" + e.toString());
                }
            } else {
                Toast.makeText(getApplicationContext(), "Sorry...! Video Call could not be connected. Try Again", Toast.LENGTH_LONG).show();
                System.out.println("appr_id------" + appr_id);
            }
        } else {
            Toast.makeText(getApplicationContext(), "Sorry...! Please Logout the App and Try again..!", Toast.LENGTH_LONG).show();
        }

    }




    class JSON_End_Cons extends AsyncTask<String, Void, Boolean> {

        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            dialog = new ProgressDialog(UpcomingConsView.this);
            dialog.setMessage("Ending Consultation, please wait");
            dialog.setCancelable(false);
            dialog.show();

        }

        @Override
        protected Boolean doInBackground(String... urls) {

            try {

                JSONParser jParser = new JSONParser();
                jsonoj_endcons = jParser.getJSON_URL(urls[0]);


                return true;
            } catch (Exception e) {
                e.printStackTrace();
            }

            return false;
        }

        protected void onPostExecute(Boolean result) {

            try {

                String status_val = jsonoj_endcons.getString("status");

                if (status_val.equals("1")) {
                    Toast.makeText(getApplicationContext(), "Consultation Ended.", Toast.LENGTH_LONG).show();
                    start_cons_layout.setVisibility(View.GONE);
                } else {
                    Toast.makeText(getApplicationContext(), "Failed to end your consultation. ", Toast.LENGTH_LONG).show();
                }

                dialog.dismiss();

            } catch (Exception e) {
                System.out.println("End Consult Somthing went Wrong");
            }
        }
    }

    public void ask_end() {
 new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)
                .setTitleText("End Consultation")
                .setContentText("Are you sure to end this Consultation?")
                .setCancelText("No")
                .setConfirmText("Yes")
                .showCancelButton(true)
                .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {
                        sDialog.dismiss();
                    }
                })
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {

                        //--------------------New Query Notify-----------------------------------------------------
                        String url = Model.BASE_URL + "/docapp/endConsult?user_id=" + (Model.id) + "&id=" + consid;
                        System.out.println("url----" + url);
                        new JSON_End_Cons().execute(url);
                        //-------------------------------------------------------------------------
                        sDialog.dismiss();
                    }
                })
                .show();

    }


    class Get_Token extends AsyncTask<String, Void, Boolean> {

        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            dialog = new ProgressDialog(UpcomingConsView.this);
            dialog.setMessage("Connecting..., please wait");
            dialog.show();
            dialog.setCancelable(false);
        }

        @Override
        protected Boolean doInBackground(String... urls) {

            try {
                str_response = new JSONParser().getJSONString(urls[0]);
                return true;
            } catch (Exception e) {
                e.printStackTrace();
            }
            return false;
        }

        protected void onPostExecute(Boolean result) {

            try {
                System.out.println("str_response--------------" + str_response);

                JSONObject json = new JSONObject(str_response);

                if (json.has("token")) {
                    token_text = json.getString("token");
                    session_id_text = json.getString("session_id");

                    Model.webrtc_token = json.getString("token");
                    Model.webrtc_session_id = json.getString("session_id");

                } else {
                    token_text = "";
                }

                System.out.println("token_text....." + token_text);

                System.out.println("Open with Web Browser------------" + open_url_text);
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(open_url_text));
                startActivity(i);

                Intent intent = new Intent(UpcomingConsView.this, Webrtc_Activity.class);
                startActivity(intent);
                finish();



            } catch (Exception e) {
                System.out.println("Call Error....." + e.toString());
            }

            dialog.cancel();

        }
    }


}
*/
