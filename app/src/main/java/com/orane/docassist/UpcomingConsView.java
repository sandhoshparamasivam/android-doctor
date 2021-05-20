package com.orane.docassist;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
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

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import com.flurry.android.FlurryAgent;
import com.github.ksoichiro.android.observablescrollview.ObservableWebView;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.obsez.android.lib.filechooser.ChooserDialog;
import com.orane.docassist.Model.Model;
import com.orane.docassist.Model.MultipartEntity2;
import com.orane.docassist.Network.JSONParser;
import com.orane.docassist.attachment_view.GridViewActivity;
import com.orane.docassist.chime.InMeetingActivity;
import com.orane.docassist.chime.MeetingHomeActivity;
import com.orane.docassist.expand.ExpandableLayout;
import com.orane.docassist.fileattach_library.DefaultCallback;
import com.orane.docassist.fileattach_library.EasyImage;
import com.orane.docassist.zoom.Constants;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import me.drakeet.materialdialog.MaterialDialog;
import pl.tajchert.nammu.Nammu;
import pl.tajchert.nammu.PermissionCallback;
import us.zoom.sdk.MeetingError;
import us.zoom.sdk.MeetingService;
import us.zoom.sdk.MeetingServiceListener;
import us.zoom.sdk.MeetingStatus;
import us.zoom.sdk.StartMeetingOptions;
import us.zoom.sdk.StartMeetingParams4APIUser;
import us.zoom.sdk.ZoomError;
import us.zoom.sdk.ZoomSDK;
import us.zoom.sdk.ZoomSDKInitializeListener;

public class UpcomingConsView extends AppCompatActivity implements Constants, MeetingServiceListener, ZoomSDKInitializeListener {

    public final static String ACTION_RETURN_FROM_MEETING = "com.orane.docassist.action.ReturnFromMeeting";
    public final static String EXTRA_TAB_ID = "tabId";
    public final static int TAB_WELCOME = 1;
    public final static int TAB_MEETING = 2;
    public final static int TAB_PAGE_2 = 3;
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
    private final static String TAG = "Zoom SDK Example";
    private final static int STYPE = MeetingService.USER_TYPE_API_USER;
    private final static String DISPLAY_NAME = "Video Consultation";

    private static String dirPath;

    public String str_drug_dets,contentAsString, last_upload_file, attach_qid, attach_status, attach_file_url, attach_filename, attach_id, enable_prescription_val, has_upload_file_val, p_status_val, prescMsg_text, file_fileUrl_text, file_full_url, attach_file_text, file_name_only, z_meetingno_text, z_meetingno, appt_status, err_status, z_status, meetingNo, z_meeting_id, consid, z_host_token_text, z_host_id_text, video_cons_id, cons_view_type, err_text, note_time_text, note_text, url;
    public String token_text, upload_response, selectedPath, selectedfilename, family_list, url_meeting_id, sub_url = "";
    public String has_prescription_val, conf_name_text, conf_id_text, vendor_text, chime_url_text, open_url_text, session_id_text, Log_Status, str_response, status_value, call_status, notes_jsonobj_text, pres_jsonobj_text, files_jsonobj_text, filename, file_title, file_ext, appr_id, patient_id, str_cons_type, cons_id, pgeo, cons_patient, cons_date, cons_time, cons_type, cons_case_dets, cons_status, presc;
    int downloadIdOne;
    LinearLayout file_list, review_editboxes, expand_layout, pres_wrie_layout;
    EditText edt_review, edt_patient_quest, edt_impress;
    String strHtmlReview_text, strHtmlData_text, is_editable_val, impression_plan_text, ans_pat_qus_text, medical_opinion_text;
    TextView tv_attach_url, tv_attach_id, tv_fname_only, tv_pres_comment, tv_filename, tv_start_text, tv_ext, cons_tit, tvdate, tvtime, tvpatientgeo, tvpatient, tvcasedets, tvattached, tvconsdate, tvconsmode, tvconsstatus, tvnotes;
    Button btn_video_end, btn_upload_pres, btn_write_pres, btn_phone_end, btn_direct_end, btnJoinMeeting, btn_start_phone_cons, btn_start_video_cons, btnwrite_notes;
    ImageView consult_mode_img;
    JSONObject save_jsonobj, jsonoj_endcons, save_json, get_meetingid_jsonobj, call_jsonobj, call_json, jsonobj_files, consview_jsonobj, appt_jsonobj;
    View vi_files;
    ImageView file_image;
    LinearLayout direct_cons_layout, phone_cons_layout, video_cons_layout, end_call_layout, start_cons_layout, start_call_layout, files_full_layout, files_layout, notes_layout;
    ScrollView full_layout;
    ProgressBar progressBar;
    JSONObject jsonobj1;
    SharedPreferences sharedpreferences;
    ObservableWebView webview_notes, webview;
    String fileUrlSecure_text;
    Button btn_save, btn_submit, btn_view_prescription, btn_edit_notes;
    private String _path, extension;
    private View viewTabWelcome;
    private View viewTabMeeting;
    private View viewTabPage2;
    private Button btnTabWelcome;
    private Button btnTabMeeting;
    private Button btnTabPage2;
    private Toolbar toolbar;
    private EditText mEdtMeetingNo;
    private EditText mEdtMeetingPassword;
    TextView prescription_title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.consview_new);

        //setupTabs();

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

            ZoomSDK zoomSDK = ZoomSDK.getInstance();

/*
            if(savedInstanceState == null) {
                zoomSDK.initialize(this, APP_KEY, APP_SECRET, WEB_DOMAIN, this);
            }
*/

            if (!zoomSDK.isInitialized()) {
                //mInitAuthSDKCallback = callback;
                zoomSDK.initialize(UpcomingConsView.this, APP_KEY, APP_SECRET, WEB_DOMAIN, UpcomingConsView.this, true, 5);
            }


            if (zoomSDK.isInitialized()) {
                registerMeetingServiceListener();
            }

        } catch (Exception ee) {
            ee.printStackTrace();
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


        btn_upload_pres = (Button) findViewById(R.id.btn_upload_pres);
        btn_write_pres = (Button) findViewById(R.id.btn_write_pres);
        btn_view_prescription = (Button) findViewById(R.id.btn_view_prescription);

        btn_edit_notes = (Button) findViewById(R.id.btn_edit_notes);
        btn_save = (Button) findViewById(R.id.btn_save);
        btn_submit = (Button) findViewById(R.id.btn_submit);

        webview_notes = (ObservableWebView) findViewById(R.id.webview_notes);
        webview = (ObservableWebView) findViewById(R.id.webview);
        btnJoinMeeting = findViewById(R.id.btnJoinMeeting);
        btn_video_end = findViewById(R.id.btn_video_end);
        btn_phone_end = findViewById(R.id.btn_phone_end);
        btn_direct_end = findViewById(R.id.btn_direct_end);

        edt_impress = findViewById(R.id.edt_impress);
        edt_patient_quest = findViewById(R.id.edt_patient_quest);
        edt_review = findViewById(R.id.edt_review);

        file_list = findViewById(R.id.file_list);
        tv_pres_comment = findViewById(R.id.tv_pres_comment);
        pres_wrie_layout = findViewById(R.id.pres_wrie_layout);
        btn_start_phone_cons = findViewById(R.id.btn_start_phone_cons);
        start_cons_layout = findViewById(R.id.start_cons_layout);
        start_call_layout = findViewById(R.id.start_call_layout);
        end_call_layout = findViewById(R.id.end_call_layout);
        progressBar = findViewById(R.id.progressBar);
        full_layout = findViewById(R.id.full_LinearLayout);
        files_full_layout = findViewById(R.id.files_full_layout);
        files_layout = findViewById(R.id.files_layout);
        notes_layout = findViewById(R.id.notes_layout);
        video_cons_layout = findViewById(R.id.video_cons_layout);
        phone_cons_layout = findViewById(R.id.phone_cons_layout);
        direct_cons_layout = findViewById(R.id.direct_cons_layout);

        tv_start_text = findViewById(R.id.tv_start_text);
        cons_tit = findViewById(R.id.cons_tit);
        tvdate = findViewById(R.id.tvdate);
        tvtime = findViewById(R.id.tvtime);
        btnwrite_notes = findViewById(R.id.btnwrite_notes);
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
        expand_layout = findViewById(R.id.expand_layout);
        review_editboxes = findViewById(R.id.review_editboxes);

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
//                Log.e(" Full URL",url+" ");
//                Log.e(" Full BASE_URL",Model.BASE_URL +" ");
                new JSON_ViewCons().execute(url);
                //------------------------- Url Pass -------------------------

                //-------------------------------------------------------------------
                String get_family_url = Model.BASE_URL + "sapp/listHReportsData?item_id=" + consid + "&item_type=appointment&user_id=" + Model.id + "&token=" + Model.token;
                System.out.println("get_family_url---------" + get_family_url);
                new JSON_getFileList().execute(get_family_url);
                //-------------------------------------------------------------------


            } else {
                Toast.makeText(getApplicationContext(), "Something went wrong. Please try again.", Toast.LENGTH_LONG).show();
                //ask_logout();
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


        btn_edit_notes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {

                    //-------------------------------------------------------------------
                    String edit_review_url = Model.BASE_URL + "sapp/changeApptNotesSts?appt_id=" + consid + "&user_id=" + Model.id + "&token=" + Model.token;
                    System.out.println("edit_review_url ---------" + edit_review_url);
                    new JSON_edit_Review().execute(edit_review_url);
                    //-------------------------------------------------------------------

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        btn_view_prescription.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    String params = Model.BASE_URL + "sapp/previewPrescription?user_id=" + (Model.id) + "&token=" + Model.token + "&os_type=android&item_type=consult&item_id=" + cons_id;
                    System.out.println("Pressed Prescription-----------" + params);
                    new list_drugs().execute(params);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });



        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {

                    String imp_text = edt_impress.getText().toString();
                    String edt_patient_quest_text = edt_patient_quest.getText().toString();
                    String edt_review_text = edt_review.getText().toString();


                    save_json = new JSONObject();
                    save_json.put("item_id", consid);
                    save_json.put("user_id", Model.id);
                    save_json.put("impression_plan", imp_text);
                    save_json.put("ans_pat_qus", edt_patient_quest_text);
                    save_json.put("notes", edt_review_text);
                    save_json.put("is_save", "1");

                    System.out.println("save_json---------------" + save_json.toString());

                    new JSON_notes_save().execute(save_json);

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });

        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {

                    String imp_text = edt_impress.getText().toString();
                    String edt_patient_quest_text = edt_patient_quest.getText().toString();
                    String edt_review_text = edt_review.getText().toString();


                    save_json = new JSONObject();
                    save_json.put("item_id", consid);
                    save_json.put("user_id", Model.id);
                    save_json.put("impression_plan", imp_text);
                    save_json.put("ans_pat_qus", edt_patient_quest_text);
                    save_json.put("notes", edt_review_text);
                    save_json.put("is_save", "0");

                    System.out.println("save_json---------------" + save_json.toString());

                    new JSON_notes_submit().execute(save_json);

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });


        btn_write_pres.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(UpcomingConsView.this, Prescription_home.class);
                intent.putExtra("add_type", "new");
                intent.putExtra("cur_qid", consid);
                intent.putExtra("p_type", "consult");
                intent.putExtra("pres_status", p_status_val);
                startActivity(intent);

            }
        });


        btn_upload_pres.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                attach_dialog();
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


	/*	//---------------------- Object Creation -----------------------
		btnwrite_notes.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent i = new Intent(UpcomingConsView.this, WriteNotesActivity.class);
				startActivity(i);
				overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);
			}
		});
*/

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


   /* public void open_zoom() {

        if (z_meetingno_text.length() == 0) {
            Toast.makeText(this, "You cannot continue to this consultation. please Tryagain.", Toast.LENGTH_LONG).show();
            return;
        }

        ZoomSDK zoomSDK = ZoomSDK.getInstance();
        if (!zoomSDK.isInitialized()) {
            //Toast.makeText(this, "ZoomSDK has not been initialized successfully", Toast.LENGTH_LONG).show();
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

    }*/


    private void selectTab(int tabId) {
        if (tabId == TAB_WELCOME) {
            viewTabWelcome.setVisibility(View.VISIBLE);
            viewTabMeeting.setVisibility(View.GONE);
            viewTabPage2.setVisibility(View.GONE);
            btnTabWelcome.setSelected(true);
            btnTabMeeting.setSelected(false);
            btnTabPage2.setSelected(false);
        } else if (tabId == TAB_PAGE_2) {
            viewTabWelcome.setVisibility(View.GONE);
            viewTabMeeting.setVisibility(View.GONE);
            viewTabPage2.setVisibility(View.VISIBLE);
            btnTabWelcome.setSelected(false);
            btnTabMeeting.setSelected(false);
            btnTabPage2.setSelected(true);
        } else if (tabId == TAB_MEETING) {
            ZoomSDK zoomSDK = ZoomSDK.getInstance();
            if (!zoomSDK.isInitialized()) {
                //Toast.makeText(this, "ZoomSDK has not been initialized successfully", Toast.LENGTH_LONG).show();
                return;
            }

            MeetingService meetingService = zoomSDK.getMeetingService();
            if (meetingService == null)
                return;

            if (meetingService.getMeetingStatus() == MeetingStatus.MEETING_STATUS_IDLE) {

                viewTabWelcome.setVisibility(View.GONE);
                viewTabPage2.setVisibility(View.GONE);
                viewTabMeeting.setVisibility(View.VISIBLE);
                btnTabWelcome.setSelected(false);
                btnTabPage2.setSelected(false);
                btnTabMeeting.setSelected(true);

                startMeeting();
            } else {
                meetingService.returnToMeeting(this);
            }

            overridePendingTransition(0, 0);
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        // disable animation
        overridePendingTransition(0, 0);

        String action = intent.getAction();

        if (ACTION_RETURN_FROM_MEETING.equals(action)) {
            int tabId = intent.getIntExtra(EXTRA_TAB_ID, TAB_WELCOME);
            selectTab(tabId);
        }
    }

    @Override
    public void onZoomSDKInitializeResult(int errorCode, int internalErrorCode) {
        Log.i(TAG, "onZoomSDKInitializeResult, errorCode=" + errorCode + ", internalErrorCode=" + internalErrorCode);

        if (errorCode != ZoomError.ZOOM_ERROR_SUCCESS) {
            Toast.makeText(this, "Failed to initialize Zoom SDK. Error: " + errorCode + ", internalErrorCode=" + internalErrorCode, Toast.LENGTH_LONG);
        } else {
            //Toast.makeText(this, "Initialize Zoom SDK successfully.", Toast.LENGTH_LONG).show();
            registerMeetingServiceListener();
        }
    }


/*
    @Override
    public void onZoomSDKInitializeResult(int errorCode, int internalErrorCode) {
        Log.i(TAG, "onZoomSDKInitializeResult, errorCode=" + errorCode + ", internalErrorCode=" + internalErrorCode);
        if (errorCode != ZoomError.ZOOM_ERROR_SUCCESS) {

            Toast.makeText(this, "Failed to initialize Zoom SDK. Error: " + errorCode + ", internalErrorCode=" + internalErrorCode, Toast.LENGTH_LONG);
        } else {
            //Toast.makeText(this, "Initialize Zoom SDK successfully.", Toast.LENGTH_LONG).show();

            registerMeetingServiceListener();
        }
    }*/


    private void registerMeetingServiceListener() {
        ZoomSDK zoomSDK = ZoomSDK.getInstance();
        MeetingService meetingService = zoomSDK.getMeetingService();
        if (meetingService != null) {
            meetingService.addListener(this);
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

    public void startMeeting() {

        ZoomSDK zoomSDK = ZoomSDK.getInstance();

        if (!zoomSDK.isInitialized()) {
            Toast.makeText(this, "ZoomSDK has not been initialized successfully", Toast.LENGTH_LONG).show();
            return;
        }

        if (MEETING_ID == null) {
            Toast.makeText(this, "MEETING_ID in Constants can not be NULL", Toast.LENGTH_LONG).show();
            return;
        }

        MeetingService meetingService = zoomSDK.getMeetingService();

        StartMeetingOptions opts = new StartMeetingOptions();
        opts.no_driving_mode = true;
//		opts.no_meeting_end_message = true;
        opts.no_titlebar = true;
        opts.no_bottom_toolbar = true;
        opts.no_invite = true;

        StartMeetingParams4APIUser params = new StartMeetingParams4APIUser();
        params.userId = USER_ID;
        params.zoomToken = ZOOM_TOKEN;
        params.meetingNo = MEETING_ID;
        params.displayName = DISPLAY_NAME;

        int ret = meetingService.startMeetingWithParams(this, params, opts);

        Log.i(TAG, "onClickBtnStartMeeting, ret=" + ret);
    }

    @Override
    public void onMeetingStatusChanged(MeetingStatus meetingStatus, int errorCode,
                                       int internalErrorCode) {

        if (meetingStatus == MeetingStatus.MEETING_STATUS_FAILED && errorCode == MeetingError.MEETING_ERROR_CLIENT_INCOMPATIBLE) {
            Toast.makeText(this, "Version of ZoomSDK is too low!", Toast.LENGTH_LONG).show();
        }

       /* if(meetingStatus == MeetingStatus.MEETING_STATUS_IDLE || meetingStatus == MeetingStatus.MEETING_STATUS_FAILED) {
            selectTab(TAB_WELCOME);
        }*/
    }

    public void ask_end() {

        //---------------- Dialog------------------------------------------------------------------
        final MaterialDialog alert = new MaterialDialog(UpcomingConsView.this);
        alert.setTitle("End Consultation.");
        alert.setMessage("Are you sure to end this Consultation?");
        alert.setCanceledOnTouchOutside(false);
        alert.setPositiveButton("Yes", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //-------------- Logout-------------------------------------------------
                try {

                    //--------------------New Query Notify-----------------------------------------------------
                    String url = Model.BASE_URL + "/docapp/endConsult?user_id=" + (Model.id) + "&id=" + consid + "&token=" + Model.token;
                    System.out.println("url----" + url);
                    new JSON_End_Cons().execute(url);
                    //-------------------------------------------------------------------------

                    alert.dismiss();

                } catch (Exception e) {
                    e.printStackTrace();
                }
                //--------------- Logout------------------------------------------------


            }
        });

        alert.setNegativeButton("No", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alert.dismiss();
            }
        });
        alert.show();
        //-----------------Dialog-----------------------------------------------------------------


    }

    public void onClickBtnJoinMeeting(View view) {


        try {
            //------------------------- Url Pass ------------------------- open_zoom();
            //String get_meet_url = Model.BASE_URL + "sapp/getzid?appt_id=" + appr_id + "&user_id=" + (Model.id) + "&token=" + Model.token;
            String get_meet_url = Model.BASE_URL + "sapp/getzidV2?appt_id=" + appr_id + "&user_id=" + (Model.id) + "&token=" + Model.token;
            System.out.println("Cond get_meet_url------" + get_meet_url);
            new Get_Meeting_id().execute(get_meet_url);
            //------------------------- Url Pass -------------------------
        } catch (Exception e2) {
            e2.printStackTrace();
        }

/*        Intent i = new Intent(UpcomingConsView.this, Voxeet.class);
        i.putExtra("cons_user_name", "Mohan_app");
        i.putExtra("conf_name", "icliniq");
        startActivity(i);*/



       /* Intent intent = new Intent(UpcomingConsView.this, Voxeet.class);
        startActivity(intent);
        finish();*/

/*

        ZoomSDK zoomSDK = ZoomSDK.getInstance();

        if (!zoomSDK.isInitialized()) {
            Toast.makeText(this, "ZoomSDK has not been initialized successfully", Toast.LENGTH_LONG).show();
            return;
        }

        if (meetingNo == null) {
            Toast.makeText(this, "Meeting Number in Constants can not be NULL", Toast.LENGTH_LONG).show();
            return;
        }

        MeetingService meetingService = zoomSDK.getMeetingService();

        StartMeetingOptions opts = new StartMeetingOptions();
        opts.no_driving_mode = true;
//		opts.no_meeting_end_message = true;
        opts.no_titlebar = true;
        opts.no_bottom_toolbar = true;
        opts.no_invite = true;

        StartMeetingParams4APIUser params = new StartMeetingParams4APIUser();
        params.userId = USER_ID;
        params.zoomToken = ZOOM_TOKEN;
        params.meetingNo = meetingNo;
        params.displayName = DISPLAY_NAME;

        int ret = meetingService.startMeetingWithParams(this, params, opts);

        Log.i(TAG, "onClickBtnStartMeeting, ret=" + ret);






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
*/

    }

    public void join_video_cons() {

        meetingNo = z_meeting_id;
        String meetingPassword = "";

        if (meetingNo.length() == 0) {
            Toast.makeText(this, "You cannot continue to this consultation. please Try again.", Toast.LENGTH_LONG).show();
            return;
        }

      /*  ZoomSDK zoomSDK = ZoomSDK.getInstance();

        if (!zoomSDK.isInitialized()) {
            //Toast.makeText(this, "ZoomSDK has not been initialized successfully", Toast.LENGTH_LONG).show();
            return;
        }*/

        System.out.println("Inside zoom--meetingNo------" + meetingNo);
        System.out.println("Inside zoom---DISPLAY_NAME------" + Model.name);

        //MeetingService meetingService = zoomSDK.getMeetingService();
        //MeetingOptions opts = new MeetingOptions();
        //System.out.println("opts-----------" + opts.toString());

        System.out.println("meetingNo---------" + meetingNo);
        System.out.println("Model.name---------" + Model.name);
        System.out.println("meetingPassword---------" + meetingPassword);
        //System.out.println("opts---------" + opts);

        //int ret = meetingService.joinMeeting(this, meetingNo, Model.name, meetingPassword, opts);

        //System.out.println("ret---------" + ret);

        ZoomSDK zoomSDK = ZoomSDK.getInstance();

        if (!zoomSDK.isInitialized()) {
            // Toast.makeText(this, "ZoomSDK has not been initialized successfully", Toast.LENGTH_LONG).show();
            return;
        }

        if (meetingNo == null) {
            Toast.makeText(this, "Meeting Number in Constants can not be NULL", Toast.LENGTH_LONG).show();
            return;
        }

        MeetingService meetingService = zoomSDK.getMeetingService();

        StartMeetingOptions opts = new StartMeetingOptions();
        opts.no_driving_mode = false;
//		opts.no_meeting_end_message = true;
        opts.no_titlebar = false;
        opts.no_bottom_toolbar = false;
        opts.no_invite = true;
        opts.custom_meeting_id = z_meeting_id;

        StartMeetingParams4APIUser params = new StartMeetingParams4APIUser();
        params.userId = USER_ID;
        params.zoomToken = ZOOM_TOKEN;
        params.meetingNo = meetingNo;
        params.displayName = DISPLAY_NAME;


        int ret = meetingService.startMeetingWithParams(this, params, opts);

        Log.i(TAG, "onstr_filename-------sampleClickBtnStartMeeting, ret=" + ret);

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

    public void ask_to_save(final String file_path) {

        try {

            final MaterialDialog alert = new MaterialDialog(UpcomingConsView.this);
            alert.setTitle("Downloading file..");
            alert.setMessage("This file can be viewed only after download. Do you want to download now?");
            alert.setCanceledOnTouchOutside(false);
            alert.setPositiveButton("Download", new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    //--------------- Choose Folder -----------------------------------
                    final Context ctx = UpcomingConsView.this;
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

    public void start_consultation() {
        if (vendor_text.equals("1")) {
            start_voxeet_cons(conf_name_text, conf_id_text);
        } else if (vendor_text.equals("2")) {
            join_video_cons();
        } else if (vendor_text.equals("3")) {

        } else if (vendor_text.equals("4")) {

        } else {

        }
    }

    public void start_voxeet_cons(String conf_name_text, String conf_id_text) {

        Intent i = new Intent(UpcomingConsView.this, Voxeet.class);
        i.putExtra("cons_user_name", conf_name_text);
        i.putExtra("conf_name", conf_id_text);
        startActivity(i);

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


                    cons_case_dets = consview_jsonobj.getString("strHtmlData");
                    strHtmlReview_text = consview_jsonobj.getString("strHtmlReview");
                    impression_plan_text = consview_jsonobj.getString("impression_plan");
                    ans_pat_qus_text = consview_jsonobj.getString("ans_pat_qus");
                    medical_opinion_text = consview_jsonobj.getString("medical_opinion");
                    is_editable_val = consview_jsonobj.getString("is_editable");

                    enable_prescription_val = consview_jsonobj.getString("enable_prescription");
                    has_upload_file_val = consview_jsonobj.getString("has_upload_file");
                    p_status_val = consview_jsonobj.getString("p_status");
                    prescMsg_text = consview_jsonobj.getString("prescMsg");

                    //---------------------------------------------------------------------
                    webview_notes.getSettings().setJavaScriptEnabled(true);
                    webview_notes.loadDataWithBaseURL("", strHtmlReview_text, "text/html", "UTF-8", "");
                    webview_notes.setLongClickable(false);

                    //-------------------------------------------------------------
                    Log.e("is_editable_val",is_editable_val+" ");
                    Log.e("consview_jsonobj",consview_jsonobj+" ");
                    if (is_editable_val.equals("1")) {
                        review_editboxes.setVisibility(View.VISIBLE);

                        edt_impress.setEnabled(true);
                        edt_patient_quest.setEnabled(true);
                        edt_review.setEnabled(true);
                        btn_save.setEnabled(true);
                        btn_submit.setEnabled(true);
                        webview_notes.setVisibility(View.GONE);
                        btn_edit_notes.setVisibility(View.GONE);
                    } else {
                        review_editboxes.setVisibility(View.VISIBLE);
                        edt_impress.setEnabled(false);
                        edt_patient_quest.setEnabled(false);
                        edt_review.setEnabled(false);
                        btn_save.setEnabled(false);
                        btn_submit.setEnabled(false);
                        webview_notes.setVisibility(View.VISIBLE);
                        btn_edit_notes.setVisibility(View.VISIBLE);
                    }

                    edt_impress.setText(impression_plan_text);
                    edt_patient_quest.setText(ans_pat_qus_text);
                    edt_review.setText(medical_opinion_text);
                    //---------------------------------------------------------------------

                    //------------- get Appt Details ------------------------------------------------
                    appr_id = appt_jsonobj.getString("id");
                    cons_id = appr_id;


                    patient_id = appt_jsonobj.getString("patient_id");
                    cons_patient = appt_jsonobj.getString("patient_name");
                    pgeo = appt_jsonobj.getString("patient_geo");
                    cons_date = appt_jsonobj.getString("consult_date");
                    cons_time = appt_jsonobj.getString("consult_time");
                    cons_type = appt_jsonobj.getString("consult_type");
                    str_cons_type = appt_jsonobj.getString("str_consult_type");
                    //cons_case_dets = appt_jsonobj.getString("case");

                    status_value = appt_jsonobj.getString("status");
                    cons_status = appt_jsonobj.getString("str_status");


                    //----------------------------------------------
                    if (consview_jsonobj.has("has_prescription")) {
                        has_prescription_val = consview_jsonobj.getString("has_prescription");
                        if ((consview_jsonobj.getString("has_prescription")).equals("1")) {
                            btn_view_prescription.setVisibility(View.VISIBLE);
                        } else {
                            btn_view_prescription.setVisibility(View.GONE);
                        }
                    } else {
                        btn_view_prescription.setVisibility(View.GONE);
                    }
                    //----------------------------------------------


                    //----------------------------------------------
                    if (enable_prescription_val.equals("1")) {
                        pres_wrie_layout.setVisibility(View.VISIBLE);

                        //--------------------------------------------------
                        if (has_upload_file_val.equals("1")) {
                            btn_upload_pres.setVisibility(View.VISIBLE);
                        } else {
                            btn_upload_pres.setVisibility(View.GONE);
                        }
                        //--------------------------------------------------

                        if (p_status_val.equals("pending_review")) {
                            btn_write_pres.setVisibility(View.GONE);
                            tv_pres_comment.setVisibility(View.VISIBLE);
                            tv_pres_comment.setText(Html.fromHtml(prescMsg_text));
                        } else if (p_status_val.equals("rejected")) {
                            btn_write_pres.setVisibility(View.VISIBLE);
                            btn_write_pres.setText("Edit Prescription");
                            tv_pres_comment.setVisibility(View.VISIBLE);
                            tv_pres_comment.setText(Html.fromHtml(prescMsg_text));

                        }


                    } else {
                        pres_wrie_layout.setVisibility(View.GONE);
                    }


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

                   /* else {
                        consult_mode_img.setBackgroundResource(R.mipmap.consult_icon_color);
                        cons_tit.setText("Consultation Schedule");
                        start_cons_layout.setVisibility(View.GONE);
                        btn_start_phone_cons.setVisibility(View.GONE);
                        btnJoinMeeting.setVisibility(View.GONE);
                    }*/

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

                    //tvcasedets.setText(Html.fromHtml(cons_case_dets));
                    webview.getSettings().setJavaScriptEnabled(true);
                    webview.loadDataWithBaseURL("", cons_case_dets, "text/html", "UTF-8", "");
                    webview.setLongClickable(false);

                    tvconsstatus.setText(cons_status);

                    if (consview_jsonobj.has("notes")) {
                        notes_jsonobj_text = consview_jsonobj.getString("notes");
                        Log.e("notes_jsonobj_text",notes_jsonobj_text+"  ");
                        if (notes_jsonobj_text.length()<=2){
                            edt_impress.setEnabled(true);
                            edt_patient_quest.setEnabled(true);
                            edt_review.setEnabled(true);
                            btn_save.setEnabled(true);
                            btn_submit.setEnabled(true);
                        }

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

                             /* vi_files = getLayoutInflater().inflate(R.layout.attached_file_list, null);
                                file_image = (ImageView) vi_files.findViewById(R.id.file_image);
                                TextView tv_filename2 = (TextView) vi_files.findViewById(R.id.tv_filename);
                                tv_ext = (TextView) vi_files.findViewById(R.id.tv_ext);
                                //tv_userid = (TextView) vi_files.findViewById(R.id.tv_userid);
                                //tv_fname_only = (TextView) vi_files.findViewById(R.id.tv_fname_only);

                                tv_filename.setText(filename + "." + file_ext);
                                tv_ext.setText(file_ext);
                                //tv_fname_only.setText(""+filename);
                                files_layout.addView(vi_files);*/
                            }

                            files_layout.setVisibility(View.GONE);
                            //tv_filename.setText(attach_file_text);
                            tv_filename.setText(files_jsonobj_text);

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
                e.printStackTrace();
            }


            progressBar.setVisibility(View.GONE);
            full_layout.setVisibility(View.VISIBLE);
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

                    if (get_meetingid_jsonobj.has("chime_url")) {
                        chime_url_text = get_meetingid_jsonobj.getString("chime_url");
                        Model.chime_url = chime_url_text;
                        System.out.print("open_url_text------------" + open_url_text);
                    } else {
                        chime_url_text = "";
                        Model.chime_url = "";
                    }

                    if (get_meetingid_jsonobj.has("vendor")) {
                        vendor_text = get_meetingid_jsonobj.getString("vendor");
                        System.out.print("vendor_text------------" + vendor_text);
                    } else {
                        vendor_text = "";
                    }

                    if (get_meetingid_jsonobj.has("conf_id")) {
                        conf_id_text = get_meetingid_jsonobj.getString("conf_id");
                        System.out.print("conf_id_text------------" + conf_id_text);
                    } else {
                        conf_id_text = "";
                    }
                    if (get_meetingid_jsonobj.has("name")) {
                        conf_name_text = get_meetingid_jsonobj.getString("name");
                        System.out.print("conf_name_text------------" + conf_name_text);
                    } else {
                        conf_name_text = "";
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


                    if (get_meetingid_jsonobj.has("appt_status")) {
                        appt_status = get_meetingid_jsonobj.getString("appt_status");
                        System.out.println("get appt_status----------" + appt_status);
                    } else {
                        appt_status = "";
                    }

                    if (get_meetingid_jsonobj.has("status")) {
                        err_status = get_meetingid_jsonobj.getString("status");
                        System.out.println("get err_status----------" + err_status);
                    } else {
                        err_status = "";
                    }


                    if (vendor_text.equals("1")) {
                        start_voxeet_cons(conf_name_text, conf_id_text);
                    } else if (vendor_text.equals("2")) {

                        Intent i = new Intent(UpcomingConsView.this, MeetingHomeActivity.class);
                        i.putExtra("cons_user_name", conf_name_text);
                        i.putExtra("conf_name", conf_id_text);
                        i.putExtra("chime_url", chime_url_text);
                        startActivity(i);

                    } else if (vendor_text.equals("3")) {

/*                        if (z_meeting_id != null && !z_meeting_id.isEmpty() && !z_meeting_id.equals("null") && !z_meeting_id.equals("")) {
                            System.out.println("Open with Zoom ------------" + z_meeting_id);
                            join_video_cons();
                            //start_consultation();
                        } else {
                            start_cons_layout.setVisibility(View.GONE);
                        }*/

                    } else if (vendor_text.equals("4")) {
//                        Log.e("url",url+" ");
//                        Log.e("open_url_text",open_url_text+" ");
                        if (open_url_text != null && !open_url_text.isEmpty() && !open_url_text.equals("null") && !open_url_text.equals("")) {
                            System.out.println("Open with WebRtc SDK------------" + open_url_text);
                            try {

                                Uri uri = Uri.parse("googlechrome://navigate?url=" + open_url_text);
                                Intent i = new Intent(Intent.ACTION_VIEW, uri);
//                                if (i.resolveActivity(getPackageManager()) == null) {
//                                    i.setData(Uri.parse(url));
//                                }

                                startActivity(i);

                            } catch (Exception e) {
                                e.printStackTrace();
                                Toast.makeText(UpcomingConsView.this, "Please install chrome browser to open this video meeting..", Toast.LENGTH_SHORT).show();
                            }
                        }
                    } else {
                        if (open_url_text != null && !open_url_text.isEmpty() && !open_url_text.equals("null") && !open_url_text.equals("")) {

                            System.out.println("Open with WebRtc SDK------------" + open_url_text);

                            try {
                                Uri uri = Uri.parse("googlechrome://navigate?url=" + open_url_text);
                                Intent i = new Intent(Intent.ACTION_VIEW, uri);
//                                if (i.resolveActivity(getPackageManager()) == null) {
//                                    i.setData(Uri.parse(url));
//                                }

                                startActivity(i);

                            } catch (Exception e) {
                                e.printStackTrace();
                                Toast.makeText(UpcomingConsView.this, "Please install chrome browser to open this video meeting..", Toast.LENGTH_SHORT).show();
                            }


                        } else {
                            Toast.makeText(UpcomingConsView.this, "This constultation is not started yet.. Try after some time.", Toast.LENGTH_SHORT).show();
                        }
                    }

                }
            } catch (Exception e) {
                System.out.println("Call Error....." + e.toString());
            }
            dialog.cancel();

        }
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

               /* Intent intent = new Intent(UpcomingConsView.this, Webrtc_Activity.class);
                startActivity(intent);
                finish();*/


            } catch (Exception e) {
                System.out.println("Call Error....." + e.toString());
            }

            dialog.cancel();

        }
    }

    private class JSON_remove_file extends AsyncTask<String, Void, Boolean> {

        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            dialog = new ProgressDialog(UpcomingConsView.this);
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
            String get_family_url = Model.BASE_URL + "sapp/listHReportsData?item_id=" + appr_id + "&item_type=appointment&user_id=" + Model.id + "&token=" + Model.token;
            System.out.println("get_family_url---------" + get_family_url);
            new JSON_getFileList().execute(get_family_url);
            //-------------------------------------------------------------------

            dialog.dismiss();

        }
    }

/*
    public void Download_file(final String file_path, final String extension) {

        try {

            final ProgressDialog dialog = new ProgressDialog(UpcomingConsView.this);

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
                                Intent i = new Intent(getApplicationContext(), Attachment_WebViewActivity.class);
                                i.putExtra("url", "http://drive.google.com/viewerng/viewer?embedded=true&url=file:///" + imgFile.toString());
                                //i.putExtra("url", "file:///storage/emulated/0/Android/data/com.orane.icliniq/files/filename.pdf");
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
                                Intent i = new Intent(getApplicationContext(), Attachment_WebViewActivity.class);
                                i.putExtra("url", "file:///" + imgFile.toString());
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
    }*/

    private class JSON_notes_save extends AsyncTask<JSONObject, Void, Boolean> {

        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            dialog = new ProgressDialog(UpcomingConsView.this);
            dialog.setMessage("Please wait");
            dialog.show();
            dialog.setCancelable(false);

        }

        @Override
        protected Boolean doInBackground(JSONObject... urls) {

            try {

                JSONParser jParser = new JSONParser();
                save_jsonobj = jParser.JSON_POST(urls[0], "notes_save");

                System.out.println("urls[0]----------" + urls[0]);
                System.out.println("save_jsonobj---------" + save_jsonobj.toString());

                return true;
            } catch (Exception e) {
                e.printStackTrace();
            }
            return false;
        }

        protected void onPostExecute(Boolean result) {

            try {

                System.out.println("save_jsonobj----------" + save_jsonobj.toString());

                String status_val = save_jsonobj.getString("status");

                if (status_val.equals("1")) {

                    Toast.makeText(UpcomingConsView.this, "Notes have been successfully saved.", Toast.LENGTH_SHORT).show();
/*                    review_editboxes.setVisibility(View.GONE);
                    webview_notes.setVisibility(View.VISIBLE);*/
                } else {
                    Toast.makeText(UpcomingConsView.this, "Failed to save notes. Please try again", Toast.LENGTH_SHORT).show();
                }


            } catch (Exception e) {
                e.printStackTrace();
            }

            dialog.dismiss();

        }
    }


/*

    public void Download_file(final String file_path, final String extension) {

        try {

            final ProgressDialog dialog = new ProgressDialog(UpcomingConsView.this);

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

    private class JSON_notes_submit extends AsyncTask<JSONObject, Void, Boolean> {

        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            dialog = new ProgressDialog(UpcomingConsView.this);
            dialog.setMessage("Please wait");
            dialog.show();
            dialog.setCancelable(false);

        }

        @Override
        protected Boolean doInBackground(JSONObject... urls) {

            try {

                JSONParser jParser = new JSONParser();
                save_jsonobj = jParser.JSON_POST(urls[0], "notes_save");

                System.out.println("urls[0]----------" + urls[0]);
                return true;
            } catch (Exception e) {
                e.printStackTrace();
            }
            return false;
        }

        protected void onPostExecute(Boolean result) {

            try {

                System.out.println("save_jsonobj----------" + save_jsonobj.toString());

                String status_val = save_jsonobj.getString("status");

                if (save_jsonobj.has("strHtmlReview")) {
                    String strHtmlReview_text = save_jsonobj.getString("strHtmlReview");

                    //---------------------------------------------------------------------
                    webview_notes.getSettings().setJavaScriptEnabled(true);
                    webview_notes.loadDataWithBaseURL("", strHtmlReview_text, "text/html", "UTF-8", "");
                    webview_notes.setLongClickable(false);
                    //-------------------------------------------------------------
//                    review_editboxes.setVisibility(View.GONE);
//                    webview_notes.setVisibility(View.VISIBLE);
//                    btn_edit_notes.setVisibility(View.VISIBLE);
                }


               /* if (status_val.equals("1")) {

                    review_editboxes.setVisibility(View.GONE);
                    webview_notes.setVisibility(View.VISIBLE);

                    //------------------------- Url Pass -------------------------
                    url = Model.BASE_URL + "sapp/jsonviewconsult4doc?user_id=" + (Model.id) + "&id=" + cons_id + "&token=" + Model.token;
                    System.out.println("Consultation View url------" + url);
                    new JSON_ViewCons().execute(url);
                    //------------------------- Url Pass -------------------------

                    //-------------------------------------------------------------------
                    String get_family_url = Model.BASE_URL + "sapp/listHReportsData?item_id=" + appr_id + "&item_type=appointment&user_id=" + Model.id + "&token=" + Model.token;
                    System.out.println("get_family_url---------" + get_family_url);
                    new JSON_getFileList().execute(get_family_url);
                    //-------------------------------------------------------------------


                } else {
                    Toast.makeText(ConsHistoryView.this, "Notes submit failed. Please try again", Toast.LENGTH_SHORT).show();
                }
*/

            } catch (Exception e) {
                e.printStackTrace();
            }


            dialog.dismiss();

        }
    }

/*

    public void Asked_Download_file(final String file_path, final String extension, final String dirPath_text) {

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
    }
*/

    private class JSON_edit_Review extends AsyncTask<String, Void, String> {

        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            dialog = new ProgressDialog(UpcomingConsView.this);
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


            try {
                JSONObject jobjstatus = new JSONObject(family_list);
                String status_val = jobjstatus.getString("status");
                    Log.e("status vale",status_val+" ");
                    Log.e("jobjstatus",jobjstatus+" ");
                if (status_val.equals("1")) {

                    //-------------------------------------------------------------
                    if (status_val.equals("1")) {
                        review_editboxes.setVisibility(View.VISIBLE);
                        webview_notes.setVisibility(View.GONE);
                        btn_edit_notes.setVisibility(View.GONE);
                    } else {
                        review_editboxes.setVisibility(View.VISIBLE);
                        webview_notes.setVisibility(View.VISIBLE);
                        btn_edit_notes.setVisibility(View.GONE);
                    }
                    //-------------------------------------------------------------

                } else {

                    String msg_val = jobjstatus.getString("msg");
                    System.out.println("msg_val---------" + msg_val);

                    Toast.makeText(UpcomingConsView.this, msg_val, Toast.LENGTH_SHORT).show();

                }

            } catch (Exception e) {
                e.printStackTrace();
            }

            dialog.cancel();
        }
    }

    private class JSON_getFileList extends AsyncTask<String, Void, String> {

        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            dialog = new ProgressDialog(UpcomingConsView.this);
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

    @Override
    public void onResume() {
        super.onResume();

        System.out.println("Resume  Model.prescribe_flag---" + Model.prescribe_flag);

        try {
            if ((Model.prescribe_flag) != null && !(Model.prescribe_flag).isEmpty() && !(Model.prescribe_flag).equals("null") && !(Model.prescribe_flag).equals("")) {
                try {

                    if ((Model.prescribe_flag).equals("true")) {
                        //btn_view_prescription.setVisibility(View.VISIBLE);
/*                            prescription_title.setText("----- Prescription has been added --- ");
                            btn_write_pres.setText("Write more prescription");*/
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        } catch (Exception e) {
            System.out.println("Resume Error---" + e.toString());
            e.printStackTrace();
        }

        //------------------------- Url Pass -------------------------
        url = Model.BASE_URL + "sapp/jsonviewconsult4doc?user_id=" + (Model.id) + "&id=" + consid + "&token=" + Model.token;
        System.out.println("Cond View url------" + url);
//        Log.e(" Full URL",url+" ");
//        Log.e(" Full BASE_URL",Model.BASE_URL +" ");
        new JSON_ViewCons().execute(url);
        //------------------------- Url Pass -------------------------

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

                    int permissionCheck = ContextCompat.checkSelfPermission(UpcomingConsView.this, Manifest.permission.CAMERA);
                    if (permissionCheck == PackageManager.PERMISSION_GRANTED) {
                        EasyImage.openCamera(UpcomingConsView.this, 0);
                    } else {
                        Nammu.askForPermission(UpcomingConsView.this, Manifest.permission.CAMERA, new PermissionCallback() {
                            @Override
                            public void permissionGranted() {
                                EasyImage.openCamera(UpcomingConsView.this, 0);
                            }

                            @Override
                            public void permissionRefused() {

                            }
                        });
                    }

                } else {
                    //showChooser();

                    int permissionCheck = ContextCompat.checkSelfPermission(UpcomingConsView.this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
                    if (permissionCheck == PackageManager.PERMISSION_GRANTED) {
                        EasyImage.openDocuments(UpcomingConsView.this, 0);
                    } else {
                        Nammu.askForPermission(UpcomingConsView.this, Manifest.permission.WRITE_EXTERNAL_STORAGE, new PermissionCallback() {
                            @Override
                            public void permissionGranted() {
                                EasyImage.openDocuments(UpcomingConsView.this, 0);
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
                    File photoFile = EasyImage.lastlyTakenButCanceledPhoto(UpcomingConsView.this);
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

            new AsyncTask_fileupload().execute(selectedPath);

        }
    }


    private class AsyncTask_fileupload extends AsyncTask<String, Void, Boolean> {

        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            try {
                dialog = new ProgressDialog(UpcomingConsView.this);
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


                attach_qid = "";
                attach_status = jObj.getString("status");
                attach_file_url = jObj.getString("file_url");
                attach_filename = jObj.getString("file");
                attach_id = "";


                if (!(last_upload_file).equals("")) {

                    //------------------------------------
                    LayoutInflater layoutInflater = (LayoutInflater) getBaseContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    final View addView = layoutInflater.inflate(R.layout.ans_upload_file_list, null);

                    TextView tv_quest = addView.findViewById(R.id.tv_quest);
                    ImageView close_button = addView.findViewById(R.id.close_button);
                    //thumb_img = addView.findViewById(R.id.imageView4);
                    tv_attach_url = addView.findViewById(R.id.tv_attach_url);
                    tv_attach_id = addView.findViewById(R.id.tv_attach_id);


                    tv_quest.setText(last_upload_file);
                    tv_attach_id.setText(attach_id);
                    tv_attach_url.setText(attach_file_url);
                    //thumb_img.setImageBitmap(BitmapFactory.decodeFile(local_url));

                    System.out.println("Model.upload_files-----------" + (last_upload_file));
                    System.out.println("Model.attach_qid-----------" + (attach_qid));
                    System.out.println("Model.attach_id-----------" + (attach_id));
                    System.out.println("Model.attach_file_url-----------" + (attach_file_url));


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


    private String upload_file(String file_path) {

        last_upload_file = file_path;

        String ServerUploadPath = Model.BASE_URL + "/sapp/consultUpload?user_id=" + (Model.id) + "&os_type=android&appt_id=" + cons_id + "&token=" + Model.token;

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

            String params = Model.BASE_URL + "sapp/previewPrescription?user_id=" + (Model.id) + "&token=" + Model.token + "&os_type=android&item_type=consult&item_id=" + cons_id;
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

            dialog = new ProgressDialog(UpcomingConsView.this);
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
                            Toast.makeText(UpcomingConsView.this, msg_text, Toast.LENGTH_SHORT).show();
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
