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
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.flurry.android.FlurryAgent;

import com.github.ksoichiro.android.observablescrollview.ObservableWebView;
import com.obsez.android.lib.filechooser.ChooserDialog;
import com.orane.docassist.Model.Model;
import com.orane.docassist.Model.MultipartEntity2;
import com.orane.docassist.Network.Detector;
import com.orane.docassist.Network.JSONParser;
import com.orane.docassist.R;
import com.orane.docassist.attachment_view.GridViewActivity;
import com.orane.docassist.expand.ExpandableLayout;
import com.orane.docassist.fileattach_library.DefaultCallback;
import com.orane.docassist.fileattach_library.EasyImage;

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

public class ConsHistoryView extends AppCompatActivity {

    private Toolbar toolbar;
    TextView tv_filename,tv_attach_url,tv_attach_id, tv_ext, tv_pres_comment,cons_tit, tvdate, tvtime, tvpatientgeo, tvpatient, tvcasedets, tvattached, tvconsdate, tvconsmode, tvconsstatus;
    Button btnwrite_notes,btn_upload_pres,btn_write_pres;
    ImageView consult_mode_img;
    public String contentAsString,last_upload_file,upload_response,selectedPath,selectedfilename,enable_prescription_val,has_upload_file_val,p_status_val,prescMsg_text,has_prescription_val, str_drug_dets, fileUrlSecure_text, file_full_url, family_list, attach_file_text, extension, z_host_token_text, z_host_id_text, video_cons_id, cons_view_type, err_text, note_time_text, note_text, url;
    public String attach_qid,attach_status,attach_file_url,attach_filename,attach_id,strHtmlReview_text, impression_plan_text, ans_pat_qus_text, medical_opinion_text, is_editable_val, file_fileUrl_text, edt_notes_text, str_response, call_status, notes_jsonobj_text, pres_jsonobj_text, files_jsonobj_text, filename, file_title, file_ext, appr_id, patient_id, str_cons_type, cons_id, pgeo, patient_name, cons_date, cons_time, cons_type, cons_case_dets, cons_status, presc;
    JSONObject save_jsonobj, jsonobj1, json_resp_notes, json_notes, jsonobj_files, consview_jsonobj, appt_jsonobj;
    View vi_files;
    private static String dirPath;
    private String _path;
    int downloadIdOne;
    JSONObject save_json;
    ImageView file_image;
    LinearLayout expand_layout, file_list,pres_wrie_layout,review_editboxes, files_full_layout, files_layout, notes_layout;
    ScrollView full_layout;
    ProgressBar progressBar;
    TextView tv_notes;
    EditText edt_impress, edt_patient_quest, edt_review;

    Button btn_edit_notes, btn_save, btn_submit;
    ObservableWebView webview_notes, webview;
    LinearLayout prescribe_view_layout;
    Button btn_write_prescription, btn_view_prescription;
    TextView tv_cuurent_query_id;

    SharedPreferences sharedpreferences;
    public static final String MyPREFERENCES = "MyPrefs";
    public static final String Login_Status = "Login_Status_key";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (new Detector().isTablet(getApplicationContext())) {
            setContentView(R.layout.consviewdetail);
        } else {
            setContentView(R.layout.consviewdetail);
        }

        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        FlurryAgent.onPageView();

        //------------ Object Creations -------------------------------
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setTitle("Consultation Room");
        }

        //------------ Object Creations ------------------------------
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.app_color));
        }

        //---------------------- Object Creation -----------------------
        btn_view_prescription = findViewById(R.id.btn_view_prescription);
        tv_cuurent_query_id = findViewById(R.id.tv_cuurent_query_id);

        pres_wrie_layout = findViewById(R.id.pres_wrie_layout);
        webview_notes = findViewById(R.id.webview_notes);
        webview = findViewById(R.id.webview);

        edt_impress = findViewById(R.id.edt_impress);
        edt_patient_quest = findViewById(R.id.edt_patient_quest);
        edt_review = findViewById(R.id.edt_review);

        file_list = findViewById(R.id.file_list);
        tv_pres_comment = findViewById(R.id.tv_pres_comment);
        progressBar = findViewById(R.id.progressBar);
        btn_write_pres = findViewById(R.id.btn_write_pres);
        btn_upload_pres = findViewById(R.id.btn_upload_pres);
        full_layout = findViewById(R.id.full_LinearLayout);
        files_full_layout = findViewById(R.id.files_full_layout);
        files_layout = findViewById(R.id.files_layout);
        notes_layout = findViewById(R.id.notes_layout);
        expand_layout = findViewById(R.id.expand_layout);
        review_editboxes = findViewById(R.id.review_editboxes);

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
        tv_notes = findViewById(R.id.tv_notes);
        tv_filename = findViewById(R.id.tv_filename);

        btn_edit_notes = findViewById(R.id.btn_edit_notes);
        btn_save = findViewById(R.id.btn_save);
        btn_submit = findViewById(R.id.btn_submit);

        files_full_layout.setVisibility(View.GONE);
        notes_layout.setVisibility(View.GONE);

        try {

            Intent intent = getIntent();
            cons_id = intent.getStringExtra("consid");
            cons_view_type = intent.getStringExtra("cons_view_type");
            //----------------------------------------------------------------

            System.out.println("cons_id------------" + cons_id);
            System.out.println("Cons_view_type------------" + cons_view_type);

        } catch (Exception ee) {
            ee.printStackTrace();
        }

        //---------------------- Object Creation -----------------------
        btnwrite_notes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final MaterialDialog alert = new MaterialDialog(ConsHistoryView.this);
                View view = LayoutInflater.from(ConsHistoryView.this).inflate(R.layout.write_notes_pres, null);
                alert.setView(view);
                alert.setTitle("Writing Notes");

                final TextView tv_notes = view.findViewById(R.id.edt_notes);
                alert.setCanceledOnTouchOutside(false);
                alert.setPositiveButton("Save", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        edt_notes_text = tv_notes.getText().toString();

                        if (edt_notes_text.equals("")) {
                            tv_notes.setError("Please enter your notes");
                        } else {

                            try {
                                json_notes = new JSONObject();
                                json_notes.put("user_id", (Model.id));
                                json_notes.put("note_type", "2");
                                json_notes.put("id", cons_id);
                                json_notes.put("note", edt_notes_text);

                                System.out.println("json_notes---" + json_notes.toString());

                                new JSON_Write_Notes().execute(json_notes);

                                //----------- Flurry -------------------------------------------------
                                Map<String, String> articleParams = new HashMap<String, String>();
                                articleParams.put("android.doc.cons_id", cons_id);
                                articleParams.put("android.doc.notes", edt_notes_text);
                                FlurryAgent.logEvent("android.doc.Cosultation_Notes_Entry", articleParams);
                                //----------- Flurry -------------------------------------------------

                            } catch (Exception e2) {
                                e2.printStackTrace();
                            }
                        }

                        alert.dismiss();
                    }
                });

                alert.setNegativeButton("Cancel", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alert.dismiss();
                    }
                });
                alert.show();
            }
        });

        if (cons_id != null && !cons_id.isEmpty() && !cons_id.equals("null") && !cons_id.equals("")) {
            if ((Model.id) != null && !(Model.id).isEmpty() && !(Model.id).equals("null") && !(Model.id).equals("")) {

                //------------------------- Url Pass -------------------------
                url = Model.BASE_URL + "sapp/jsonviewconsult4doc?user_id=" + (Model.id) + "&id=" + cons_id + "&token=" + Model.token;
                System.out.println("Consultation View url------" + url);
                new JSON_ViewCons().execute(url);
                //------------------------- Url Pass -------------------------

                //-------------------------------------------------------------------
                String get_family_url = Model.BASE_URL + "sapp/listHReportsData?item_id=" + cons_id + "&item_type=appointment&user_id=" + Model.id + "&token=" + Model.token;
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


        btn_edit_notes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {

                    //-------------------------------------------------------------------
                    String edit_review_url = Model.BASE_URL + "sapp/changeApptNotesSts?appt_id=" + cons_id + "&user_id=" + Model.id + "&token=" + Model.token;
                    System.out.println("edit_review_url ---------" + edit_review_url);
                    new JSON_edit_Review().execute(edit_review_url);
                    //-------------------------------------------------------------------

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
                    save_json.put("item_id", cons_id);
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
                    save_json.put("item_id", cons_id);
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



        btn_write_pres.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(ConsHistoryView.this, Prescription_home.class);
                intent.putExtra("add_type", "new");
                intent.putExtra("cur_qid", cons_id);
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


    public void ask_logout() {

        //---------------- Dialog------------------------------------------------------------------
        final MaterialDialog alert = new MaterialDialog(ConsHistoryView.this);
        alert.setTitle("Oops!");
        alert.setMessage("Something went wrong. You need to logout and login again to proceed.");
        alert.setCanceledOnTouchOutside(false);
        alert.setPositiveButton("OK", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alert.dismiss();
                //============================================================
                sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedpreferences.edit();
                editor.putString(Login_Status, "0");
                editor.apply();
                //============================================================
                finishAffinity();
                Intent i = new Intent(ConsHistoryView.this, LoginActivity.class);
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
                return true;
            } catch (Exception e) {
                e.printStackTrace();
            }

            return false;
        }

        protected void onPostExecute(Boolean result) {

            try {

                System.out.println("str_response--------------" + str_response);
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
                        Intent intent = new Intent(ConsHistoryView.this, LoginActivity.class);
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
                    webview.getSettings().setJavaScriptEnabled(true);
                    webview.loadDataWithBaseURL("", cons_case_dets, "text/html", "UTF-8", "");
                    webview.setLongClickable(false);
                    //-------------------------------------------------------------

                    //---------------------------------------------------------------------
                    webview_notes.getSettings().setJavaScriptEnabled(true);
                    webview_notes.loadDataWithBaseURL("", strHtmlReview_text, "text/html", "UTF-8", "");
                    webview_notes.setLongClickable(false);
                    //-------------------------------------------------------------
                    if (is_editable_val.equals("1")) {
                        review_editboxes.setVisibility(View.VISIBLE);
                        webview_notes.setVisibility(View.GONE);
                        btn_edit_notes.setVisibility(View.GONE);
                    } else {
                        review_editboxes.setVisibility(View.GONE);
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
                    patient_name = appt_jsonobj.getString("patient_name");
                    pgeo = appt_jsonobj.getString("patient_geo");
                    cons_date = appt_jsonobj.getString("consult_date");
                    cons_time = appt_jsonobj.getString("consult_time");
                    cons_type = appt_jsonobj.getString("consult_type");
                    str_cons_type = appt_jsonobj.getString("str_consult_type");
                    cons_case_dets = appt_jsonobj.getString("case");
                    cons_status = appt_jsonobj.getString("str_status");
                    //------------- get Appt Details ------------------------------------------------

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


                    System.out.println("appr_id--------" + appr_id);

                    tvpatient.setText(patient_name);
                    tvpatientgeo.setText(pgeo);
                    tvdate.setText(cons_date);
                    tvtime.setText(cons_time);
                    tvconsmode.setText(str_cons_type);
                    tvcasedets.setText(Html.fromHtml(cons_case_dets));
                    tvconsstatus.setText(cons_status);

                    //-----------------------------------------------------------------------------------------------
                    if (str_cons_type.equals("Phone Consultation")) {
                        consult_mode_img.setImageResource(R.mipmap.phone_cons_ico_color);
                        cons_tit.setText("Phone Consultation Schedule");
                    } else if (str_cons_type.equals("Video Consultation")) {
                        consult_mode_img.setImageResource(R.mipmap.video_cons_ico_color);
                        cons_tit.setText("Video Consultation Schedule");
                    } else if (str_cons_type.equals("Direct Visit")) {
                        consult_mode_img.setImageResource(R.mipmap.direct_cons_ico);
                        cons_tit.setText("Direct Consultation Schedule");
                    } else {
                        consult_mode_img.setImageResource(R.mipmap.direct_cons_ico);
                        cons_tit.setText("Consultation Schedule");
                    }
                    //-----------------------------------------------------------------------------------------------

                    if (consview_jsonobj.has("files")) {
                        files_jsonobj_text = consview_jsonobj.getString("files");
                    }
                    if (consview_jsonobj.has("notes")) {
                        notes_jsonobj_text = consview_jsonobj.getString("notes");
                    }

                    if (consview_jsonobj.has("prescription")) {
                        pres_jsonobj_text = consview_jsonobj.getString("prescription");
                        if (pres_jsonobj_text.length() > 2) {
                            notes_layout.setVisibility(View.VISIBLE);
                            tv_notes.setText(Html.fromHtml(pres_jsonobj_text));
                        } else {
                            notes_layout.setVisibility(View.GONE);
                        }
                    }
                    //---------------- Files ---------------------------------------

                    files_layout.removeAllViews();

                    if ((files_jsonobj_text.length()) > 2) {

                        files_full_layout.setVisibility(View.VISIBLE);
                        JSONArray jarray_files = consview_jsonobj.getJSONArray("files");

                        System.out.println("jsonobj_items------" + jarray_files.toString());
                        System.out.println("jarray_files.length()------" + jarray_files.length());

                        tvattached.setText("Attached " + jarray_files.length() + " File(s)");

                        attach_file_text = "";

                        for (int j = 0; j < jarray_files.length(); j++) {

                            jsonobj_files = jarray_files.getJSONObject(j);
                            System.out.println("jsonobj_files------" + j + " ----" + jsonobj_files.toString());

                            filename = jsonobj_files.getString("filename");
                            file_title = jsonobj_files.getString("file_title");
                            file_ext = jsonobj_files.getString("ext");
                            file_full_url = jsonobj_files.getString("url");

                            System.out.println("filename--------" + filename);
                            System.out.println("file_title--------" + file_title);
                            System.out.println("file_ext--------" + file_ext);
                            System.out.println("file_full_url--------" + file_full_url);

                       /* vi_files = getLayoutInflater().inflate(R.layout.attached_file_list, null);
                        file_image = (ImageView) vi_files.findViewById(R.id.file_image);
                        tv_filename = (TextView) vi_files.findViewById(R.id.tv_filename);
                        tv_ext = (TextView) vi_files.findViewById(R.id.tv_ext);
                        //tv_userid = (TextView) vi_files.findViewById(R.id.tv_userid);

                        tv_filename.setText(filename + "." + file_ext);
                        tv_ext.setText(file_ext);*/

                            //------------------------ File Attached Text --------------------------------
                            if (attach_file_text.equals("")) {
                                attach_file_text = file_full_url;
                                System.out.println("attach_file_text-------" + attach_file_text);
                            } else {
                                attach_file_text = attach_file_text + "###" + file_full_url;
                                System.out.println("attach_file_text-------" + attach_file_text);
                            }
                            //------------------------ File Attached Text --------------------------------

                            //files_layout.addView(vi_files);
                        }

                        files_layout.setVisibility(View.GONE);
                        //tv_filename.setText(attach_file_text);
                        tv_filename.setText(files_jsonobj_text);
                    } else {
                        files_full_layout.setVisibility(View.GONE);
                    }
                    //-------------------------------------------------------
                }
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

        /*if (id == R.id.notify) {
            return true;
        }*/

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (cons_id != null && !cons_id.isEmpty() && !cons_id.equals("null") && !cons_id.equals("")) {
            if ((Model.id) != null && !(Model.id).isEmpty() && !(Model.id).equals("null") && !(Model.id).equals("")) {

                //------------------------- Url Pass -------------------------
                url = Model.BASE_URL + "sapp/jsonviewconsult4doc?user_id=" + (Model.id) + "&id=" + cons_id + "&token=" + Model.token;
                System.out.println("Cond View url------" + url);
                new JSON_ViewCons().execute(url);
                //------------------------- Url Pass -------------------------

            } else {
                // Toast.makeText(getApplicationContext(), "Something went wrong. Please try again.", Toast.LENGTH_LONG).show();
                ask_logout();
            }

        } else {
            //Toast.makeText(getApplicationContext(), "Something went wrong. Please try again.", Toast.LENGTH_LONG).show();
            ask_logout();
        }

        try {
            if ((Model.prescribe_flag) != null && !(Model.prescribe_flag).isEmpty() && !(Model.prescribe_flag).equals("null") && !(Model.prescribe_flag).equals("")) {
                try {

                    if ((Model.prescribe_flag).equals("true")) {
                        // btn_view_prescription.setVisibility(View.VISIBLE);
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

    private class JSON_Write_Notes extends AsyncTask<JSONObject, Void, Boolean> {

        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            try {
                dialog = new ProgressDialog(ConsHistoryView.this);
                dialog.setMessage("Saving Notes. Please wait..");
                //dialog.setTitle("");
                dialog.show();
                dialog.setCancelable(false);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        protected Boolean doInBackground(JSONObject... urls) {
            try {
                JSONParser jParser = new JSONParser();
                json_resp_notes = jParser.JSON_POST(urls[0], "writenotes");

                System.out.println("JSON_Write_Notes---------------" + json_resp_notes.toString());

                return true;

            } catch (Exception e) {
                e.printStackTrace();
            }

            dialog.dismiss();

            return false;
        }

        protected void onPostExecute(Boolean result) {

            try {

                if (json_resp_notes.has("token_status")) {
                    String token_status = json_resp_notes.getString("token_status");
                    if (token_status.equals("0")) {

                        //============================================================
                        SharedPreferences.Editor editor = sharedpreferences.edit();
                        editor.putString(Login_Status, "0");
                        editor.apply();
                        //============================================================

                        finishAffinity();
                        Intent intent = new Intent(ConsHistoryView.this, LoginActivity.class);
                        startActivity(intent);
                        finish();
                    }

                } else {

                    String status_txt = json_resp_notes.getString("status");

                    if (status_txt.equals("1")) {
                        say_success();

                        String old_text = tv_notes.getText().toString();

                        System.out.println("old_text--------" + old_text);
                        System.out.println("edt_notes_text--------" + edt_notes_text);

                        tv_notes.setText(Html.fromHtml(old_text + "\n\n" + edt_notes_text));

                        System.out.println(Html.fromHtml("Notes-----------" + old_text + "\n\n" + edt_notes_text));

                    } else {
                        try {
                            say_failed();
                            //Toast.makeText(getApplicationContext(), "Sending Failed", Toast.LENGTH_SHORT).show();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }

                dialog.dismiss();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void say_success() {
        Toast.makeText(getApplicationContext(), "Notes have been successfully saved", Toast.LENGTH_SHORT).show();
    }

    public void say_failed() {
        Toast.makeText(getApplicationContext(), "Failed to save notes", Toast.LENGTH_SHORT).show();
    }


    private class JSON_remove_file extends AsyncTask<String, Void, Boolean> {

        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            dialog = new ProgressDialog(ConsHistoryView.this);
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

    private class JSON_notes_save extends AsyncTask<JSONObject, Void, Boolean> {

        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            try {
                dialog = new ProgressDialog(ConsHistoryView.this);
                dialog.setMessage("Please wait");
                dialog.show();
                dialog.setCancelable(false);
            } catch (Exception e) {
                e.printStackTrace();
            }

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
                    Toast.makeText(ConsHistoryView.this, "Notes have been successfully saved.", Toast.LENGTH_SHORT).show();
                } else {

                    String msg_val = save_jsonobj.getString("msg");

                    Toast.makeText(ConsHistoryView.this, msg_val , Toast.LENGTH_SHORT).show();
                }


            } catch (Exception e) {
                e.printStackTrace();
            }

            dialog.dismiss();

        }
    }


    private class JSON_notes_submit extends AsyncTask<JSONObject, Void, Boolean> {

        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            dialog = new ProgressDialog(ConsHistoryView.this);
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
                    review_editboxes.setVisibility(View.GONE);
                    webview_notes.setVisibility(View.VISIBLE);
                    btn_edit_notes.setVisibility(View.VISIBLE);
                }

                if (status_val.equals("1")) {
                    Toast.makeText(ConsHistoryView.this, "Notes have been submitted successfully.", Toast.LENGTH_SHORT).show();
                } else {

                    String msg_val = save_jsonobj.getString("msg");
                    Toast.makeText(ConsHistoryView.this, msg_val , Toast.LENGTH_SHORT).show();
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

    private class JSON_edit_Review extends AsyncTask<String, Void, String> {

        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            dialog = new ProgressDialog(ConsHistoryView.this);
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

                if (status_val.equals("1")) {

                    //-------------------------------------------------------------
                    if (status_val.equals("1")) {
                        review_editboxes.setVisibility(View.VISIBLE);
                        webview_notes.setVisibility(View.GONE);
                        btn_edit_notes.setVisibility(View.GONE);
                    } else {
                        review_editboxes.setVisibility(View.GONE);
                        webview_notes.setVisibility(View.VISIBLE);
                        btn_edit_notes.setVisibility(View.VISIBLE);
                    }
                    //-------------------------------------------------------------

                } else {

                    String msg_val = jobjstatus.getString("msg");
                    System.out.println("msg_val---------" + msg_val);

                    Toast.makeText(ConsHistoryView.this, msg_val, Toast.LENGTH_SHORT).show();

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

            dialog = new ProgressDialog(ConsHistoryView.this);
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


   /* public void Download_file(final String file_path, final String extension) {

        try {

            final ProgressDialog dialog = new ProgressDialog(ConsHistoryView.this);

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
    }*/


    public void ask_to_save(final String file_path) {

        try {

            final MaterialDialog alert = new MaterialDialog(ConsHistoryView.this);
            alert.setTitle("Downloading file..");
            alert.setMessage("This file can be viewed only after download. Do you want to download now?");
            alert.setCanceledOnTouchOutside(false);
            alert.setPositiveButton("Download", new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    //--------------- Choose Folder -----------------------------------
                    final Context ctx = ConsHistoryView.this;
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
    }*/


    private class list_drugs extends AsyncTask<String, Void, Void> {

        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            dialog = new ProgressDialog(ConsHistoryView.this);
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
                            Toast.makeText(ConsHistoryView.this, msg_text, Toast.LENGTH_SHORT).show();
                        }
                    }
                    dialog.dismiss();
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
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

                    int permissionCheck = ContextCompat.checkSelfPermission(ConsHistoryView.this, Manifest.permission.CAMERA);
                    if (permissionCheck == PackageManager.PERMISSION_GRANTED) {
                        EasyImage.openCamera(ConsHistoryView.this, 0);
                    } else {
                        Nammu.askForPermission(ConsHistoryView.this, Manifest.permission.CAMERA, new PermissionCallback() {
                            @Override
                            public void permissionGranted() {
                                EasyImage.openCamera(ConsHistoryView.this, 0);
                            }

                            @Override
                            public void permissionRefused() {

                            }
                        });
                    }

                } else {
                    //showChooser();

                    int permissionCheck = ContextCompat.checkSelfPermission(ConsHistoryView.this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
                    if (permissionCheck == PackageManager.PERMISSION_GRANTED) {
                        EasyImage.openDocuments(ConsHistoryView.this, 0);
                    } else {
                        Nammu.askForPermission(ConsHistoryView.this, Manifest.permission.WRITE_EXTERNAL_STORAGE, new PermissionCallback() {
                            @Override
                            public void permissionGranted() {
                                EasyImage.openDocuments(ConsHistoryView.this, 0);
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
                    File photoFile = EasyImage.lastlyTakenButCanceledPhoto(ConsHistoryView.this);
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

            new ConsHistoryView.AsyncTask_fileupload().execute(selectedPath);

        }
    }


    private class AsyncTask_fileupload extends AsyncTask<String, Void, Boolean> {

        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            try {
                dialog = new ProgressDialog(ConsHistoryView.this);
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
                attach_file_url = jObj.getString("url");
                attach_filename = jObj.getString("filename");
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
            new ConsHistoryView.list_drugs().execute(params);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
