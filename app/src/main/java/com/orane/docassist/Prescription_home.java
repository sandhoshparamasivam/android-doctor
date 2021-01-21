package com.orane.docassist;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
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
import android.webkit.WebSettings;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Scroller;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import com.flurry.android.FlurryAgent;
import com.github.ksoichiro.android.observablescrollview.ObservableWebView;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.orane.docassist.Model.Item;
import com.orane.docassist.Model.Model;
import com.orane.docassist.Model.MultipartEntity2;
import com.orane.docassist.Network.JSONParser;
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
import org.json.JSONObject;

import java.io.File;
import java.net.URLEncoder;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

import me.drakeet.materialdialog.MaterialDialog;
import pl.tajchert.nammu.Nammu;
import pl.tajchert.nammu.PermissionCallback;


public class Prescription_home extends AppCompatActivity {

    public String doc_sign_url, contentAsString, last_upload_file, attach_id, attach_qid, attach_status, attach_file_url, attach_filename, upload_response, selectedPath, selectedfilename, has_upload_file_val, enable_prescription_val, str_drug_dets, p_type, when_to_take, drug_type_name, drug_name, dose_text, qty_val, how_to_take, days_val, add_type, ptype_val, prescription_id, cur_qid, whentotake_name, whentotake_val, howtotake_name, howtotake_val, pat_id, drug_type_val, report_response;
    Button btn_submit;
    Integer drugs_length = 0;
    EditText edt_feedback;
    JSONObject json_feedback, json_response_obj;
    Spinner spinner_dtype, spinner_how, spinner_when;
    Button btn_entry_submit;
    ArrayList listArray;
    String tz_val, tz_string, cur_date;
    EditText edt_drug_name, edt_drug_days, edt_drug_qty, edt_dosage;
    RelativeLayout drugtype_layout, howtotake_layout, when_layout;
    TextView tv_attach_url, tv_attach_id, tv_pres_id, tv_drugs_heading, tv_drug_type_name, tv_howtotake_name, tv_whentotake_name;
    Intent intent;
    String pres_type;
    EditText edt_chiefcomp, edt_relpts, edt_examin, edt_sugginv, edt_diags, edt_inst;

    ImageView img_signature, img_delete_sig, img_edit_sig;

    ScrollView scroll_view;

    Map<String, String> drug_type_map = new HashMap<String, String>();
    Map<String, String> howtotake_map = new HashMap<String, String>();
    Map<String, String> whentotake_map = new HashMap<String, String>();

    Button btn_add_drugs;
    LinearLayout drugs_list_layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.prescription_home);

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

        try {
            Intent intent = getIntent();
            add_type = intent.getStringExtra("add_type");
            cur_qid = intent.getStringExtra("cur_qid");
            pat_id = intent.getStringExtra("pat_id");
            prescription_id = intent.getStringExtra("prescription_id");
            p_type = intent.getStringExtra("p_type");

            if (intent.hasExtra("pres_status")) {
                pres_type = intent.getStringExtra("pres_status");
            } else {
                pres_type = "";
            }

            System.out.println("Get pat_id---" + pat_id);
            System.out.println("Get cur_qid---" + cur_qid);
            System.out.println("Get add_type---" + add_type);
            System.out.println("Get prescription_id---" + prescription_id);
            System.out.println("Get p_type---" + p_type);
            System.out.println("Get pres_type---" + pres_type);


        } catch (Exception e) {
            e.printStackTrace();
        }

        scroll_view = findViewById(R.id.scroll_view);
        tv_drugs_heading = findViewById(R.id.tv_drugs_heading);
        tv_pres_id = findViewById(R.id.tv_pres_id);

        btn_add_drugs = findViewById(R.id.btn_add_drugs);
        btn_submit = findViewById(R.id.btn_submit);
        drugs_list_layout = findViewById(R.id.drugs_list_layout);

        edt_chiefcomp = findViewById(R.id.edt_chiefcomp);
        edt_relpts = findViewById(R.id.edt_relpts);
        edt_examin = findViewById(R.id.edt_examin);
        edt_sugginv = findViewById(R.id.edt_sugginv);
        edt_diags = findViewById(R.id.edt_diags);
        edt_inst = findViewById(R.id.edt_inst);

        img_edit_sig = findViewById(R.id.img_edit_sig);
        img_delete_sig = findViewById(R.id.img_delete_sig);
        img_signature = findViewById(R.id.img_signature);

        try {

            String params = Model.BASE_URL + "sapp/previewPrescription?user_id=" + (Model.id) + "&token=" + Model.token + "&os_type=android&item_type=" + p_type + "&item_id=" + cur_qid + "&pre_type=presc_edit&status=" + pres_type;
            System.out.println("params-----------" + params);
            new list_drugs().execute(params);

        } catch (Exception e) {
            e.printStackTrace();
        }


        //-------------Time Zone-------------------------------------------
        TimeZone tz = TimeZone.getDefault();
        System.out.println("TimeZone----------" + tz.getID());

        tz_string = tz.getID();


        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("GMT"),
                Locale.getDefault());
        Date currentLocalTime = calendar.getTime();

        DateFormat date = new SimpleDateFormat("ZZZZZ", Locale.getDefault());
        String localTime = date.format(currentLocalTime);
        System.out.println("localTime------" + localTime);

        localTime = localTime.replace(":", "");
        System.out.println("localTime------" + localTime);

        tz_val = localTime;

        Date c = Calendar.getInstance().getTime();
        System.out.println("Current time => " + c);

        //SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        String formattedDate = df.format(c);
        cur_date = formattedDate;
        System.out.println("cur_date--------" + cur_date);
        //-------------Time Zone-------------------------------------------


        img_edit_sig.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                attach_dialog();

/*                intent = new Intent(Prescription_home.this, Signature_Activity.class);
                startActivity(intent);*/
            }
        });


        btn_add_drugs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(Prescription_home.this, Prescription_Entry_Activity.class);
                intent.putExtra("add_type", "new");
                intent.putExtra("cur_qid", cur_qid);
                intent.putExtra("pat_id", pat_id);
                intent.putExtra("p_type", p_type);
                intent.putExtra("prescription_id", "0");
                intent.putExtra("drug_id", "0");
                startActivity(intent);

            }
        });

        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    System.out.println("doc_sign_url----------" + doc_sign_url);
                    System.out.println("Model.signature_uri----------" + Model.signature_uri);


                    if (doc_sign_url != null && !doc_sign_url.equals("null") && !doc_sign_url.equals("") && !doc_sign_url.isEmpty()) {

                        apply_prescription();
                    } else {
                        Toast.makeText(Prescription_home.this, "Signature is must for write prescription", Toast.LENGTH_SHORT).show();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

    }

    @Override
    public void onResume() {
        super.onResume();

        try {

            if ((Model.json_dugs_array) != null && !(Model.json_dugs_array).equals("null") && !(Model.json_dugs_array).equals("")) {

                System.out.println("Model.json_dugs_array------------" + Model.json_dugs_array);

                //JSONArray drugs_array = new JSONArray(Model.json_dugs_array);
                JSONArray drugs_array = Model.json_dugs_array;

                drugs_list_layout.removeAllViews();

                drugs_length = drugs_array.length();

                for (int i = 0; i < drugs_array.length(); i++) {
                    JSONObject jsonobj1 = drugs_array.getJSONObject(i);

                    String id_val = jsonobj1.getString("id");
                    String drugName = jsonobj1.getString("drugName");
                    String drugType = jsonobj1.getString("drugType");
                    String drugDose = jsonobj1.getString("drugDose");
                    String quantity = jsonobj1.getString("quantity");
                    String forDays = jsonobj1.getString("forDays");
                    String whenToTake = jsonobj1.getString("whenToTake");
                    String howToTake = jsonobj1.getString("howToTake");

                    LayoutInflater layoutInflater = (LayoutInflater) getBaseContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    final View addView = layoutInflater.inflate(R.layout.prescription_row, null);

                    final TextView tv_drug_type = addView.findViewById(R.id.tv_drug_type);
                    final TextView tvquery = addView.findViewById(R.id.tvquery);
                    final TextView tvspeciality = addView.findViewById(R.id.tvspeciality);
                    final TextView tv_id = addView.findViewById(R.id.tv_id);
                    final TextView tv_how_to_take = addView.findViewById(R.id.tv_how_to_take);
                    final TextView tv_when_to_take = addView.findViewById(R.id.tv_when_to_take);
                    final TextView tv_days = addView.findViewById(R.id.tv_days);
                    final TextView tvprice = addView.findViewById(R.id.tvprice);
                    final ImageView imageview_poster = addView.findViewById(R.id.imageview_poster);

                    if (drugType.equals("Tablet")) {
                        Picasso.with(Prescription_home.this).load(R.mipmap.tablet_icon).placeholder(R.drawable.zm_image_placeholder).error(R.mipmap.logo).into(imageview_poster);
                    } else if (drugType.equals("Cream")) {
                        Picasso.with(Prescription_home.this).load(R.mipmap.cream_icon).placeholder(R.drawable.zm_image_placeholder).error(R.mipmap.logo).into(imageview_poster);
                    } else if (drugType.equals("Lotion")) {
                        Picasso.with(Prescription_home.this).load(R.mipmap.lotion_icon).placeholder(R.drawable.zm_image_placeholder).error(R.mipmap.logo).into(imageview_poster);
                    } else {
                        Picasso.with(Prescription_home.this).load(R.mipmap.medicine_icon).placeholder(R.drawable.zm_image_placeholder).error(R.mipmap.logo).into(imageview_poster);
                    }

                    tv_id.setText(id_val);
                    tvquery.setText(drugName);
                    tv_drug_type.setText(drugType);
                    tvspeciality.setText(drugDose);
                    tv_days.setText(forDays);
                    tv_when_to_take.setText(whenToTake);
                    tv_how_to_take.setText(howToTake);
                    tvprice.setText(quantity);

                    drugs_list_layout.addView(addView);
                }

                Model.json_dugs_array = null;
            }

            if ((Model.doc_sign_url) != null && !(Model.doc_sign_url).equals("null") && !(Model.doc_sign_url).equals("")) {

                System.out.println("Resume doc_sign_url---------------" + (Model.doc_sign_url));
                doc_sign_url = Model.doc_sign_url;

                Picasso.with(Prescription_home.this).load(doc_sign_url).placeholder(R.mipmap.sig_palceholder).error(R.mipmap.default_user).into(img_signature);

            }


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void onClick_drugs(View v) {

        try {
            TextView tv_id = (TextView) v.findViewById(R.id.tv_id);
            String tv_id_val = tv_id.getText().toString();
            System.out.println("tv_id_val-------" + tv_id_val);

            intent = new Intent(Prescription_home.this, Prescription_Entry_Activity.class);
            intent.putExtra("add_type", "update");
            intent.putExtra("pat_id", pat_id);
            intent.putExtra("cur_qid", cur_qid);
            intent.putExtra("p_type", p_type);
            intent.putExtra("prescription_id", "0");
            intent.putExtra("drug_id", tv_id_val);
            startActivity(intent);


         /*   //----------------------- Doctor Inbox -------------------------------
            String params = Model.BASE_URL + "sapp/previewPrescription?os_type=android&item_type=query&item_id=" + cur_qid + "&pre_type=drug_edit&drug_id=" + tv_id_val + "&user_id=" + (Model.id) + "&token=" + Model.token;
            System.out.println("params-----------" + params);
            new get_Drug_dets().execute(params);
            //----------------------- Doctor Inbox -------------------------------
*/

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private class JSON_Feedback extends AsyncTask<JSONObject, Void, Boolean> {

        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            try {
                dialog = new ProgressDialog(Prescription_home.this);
                dialog.setMessage("Please wait..");
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
                json_response_obj = jParser.JSON_POST(urls[0], "writePresNotes");

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
                Model.query_launch = "writeDrug";

                if (report_response.equals("1")) {

                    Toast.makeText(Prescription_home.this, "Prescription has been added succesfully", Toast.LENGTH_SHORT).show();
                    Model.prescribe_flag = "true";
                    //Model.signature_uri = null;

                    finish();

                } else {
                    if (json_response_obj.has("msg")) {
                        String msg_text = json_response_obj.getString("msg");
                        Toast.makeText(getApplicationContext(), msg_text, Toast.LENGTH_LONG).show();
                    }
                    Model.prescribe_flag = "false";
                }

                Model.json_dugs_array = null;

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

            dialog = new ProgressDialog(Prescription_home.this);
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

                        String strPrescGuidelines_text = jobj.getString("strPrescGuidelines");
                        if (strPrescGuidelines_text.length() > 5) {

                            try {
                                final MaterialDialog alert = new MaterialDialog(Prescription_home.this);
                                View view = LayoutInflater.from(Prescription_home.this).inflate(R.layout.pres_terms, null);
                                alert.setView(view);
                                alert.setTitle("Writing Notes");

                                final ObservableWebView wv_webview = view.findViewById(R.id.webview);
                                wv_webview.setBackgroundColor(Color.TRANSPARENT);
                                wv_webview.getSettings().setJavaScriptEnabled(true);
                                wv_webview.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NORMAL);
                                wv_webview.loadDataWithBaseURL("", strPrescGuidelines_text, "text/html", "UTF-8", "");
                                wv_webview.setLongClickable(false);

                                alert.setCanceledOnTouchOutside(false);
                                alert.setPositiveButton("Accept", new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {

                                        try {

                                            if (jobj.has("doc_sign")) {
                                                doc_sign_url = jobj.getString("doc_sign");
                                                System.out.println("doc_sign_url from server---------- " + doc_sign_url);
                                                Picasso.with(Prescription_home.this).load(doc_sign_url).placeholder(R.mipmap.sig_palceholder).error(R.mipmap.img_placeholder_new).into(img_signature);
                                            }

                                            if (jobj.has("prescrPatId")) {

                                                String prescrPatId = jobj.getString("prescrPatId");
                                                String patientId = jobj.getString("patientId");
                                                String chiefComplaints = jobj.getString("chiefComplaints");
                                                String relevantPoints = jobj.getString("relevantPoints");
                                                String examination = jobj.getString("examination");
                                                String suggestedInvestications = jobj.getString("suggestedInvestications");
                                                String provisionalDiagnosis = jobj.getString("provisionalDiagnosis");
                                                String specialInstructions = jobj.getString("specialInstructions");
                                                String isRemoveDrug_val = jobj.getString("isRemoveDrug");


                                                if (strPrescGuidelines_text.length() > 5) {

                                                    try {

                                                        tv_pres_id.setText(prescrPatId);
                                                        edt_chiefcomp.setText(chiefComplaints);
                                                        edt_relpts.setText(relevantPoints);
                                                        edt_examin.setText(examination);
                                                        edt_sugginv.setText(suggestedInvestications);
                                                        edt_diags.setText(provisionalDiagnosis);
                                                        edt_inst.setText(specialInstructions);


                                                        String drugs = jobj.getString("drugs");

                                                        if (drugs.length() > 10) {

                                                            tv_drugs_heading.setText("Added Drugs list ");
                                                            JSONArray drugs_array = new JSONArray(drugs);

                                                            drugs_list_layout.removeAllViews();

                                                            drugs_length = drugs_array.length();

                                                            for (int i = 0; i < drugs_array.length(); i++) {
                                                                JSONObject jsonobj1 = drugs_array.getJSONObject(i);

                                                                String id_val = jsonobj1.getString("id");
                                                                String drugName = jsonobj1.getString("drugName");
                                                                String drugType = jsonobj1.getString("drugType");
                                                                String drugDose = jsonobj1.getString("drugDose");
                                                                String quantity = jsonobj1.getString("quantity");
                                                                String forDays = jsonobj1.getString("forDays");
                                                                String whenToTake = jsonobj1.getString("whenToTake");
                                                                String howToTake = jsonobj1.getString("howToTake");

                                                                LayoutInflater layoutInflater = (LayoutInflater) getBaseContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                                                                final View addView = layoutInflater.inflate(R.layout.prescription_row, null);

                                                                final ImageView remove_icon = addView.findViewById(R.id.remove_icon);
                                                                final TextView tv_drug_type = addView.findViewById(R.id.tv_drug_type);
                                                                final TextView tvquery = addView.findViewById(R.id.tvquery);
                                                                final TextView tvspeciality = addView.findViewById(R.id.tvspeciality);
                                                                final TextView tv_id = addView.findViewById(R.id.tv_id);
                                                                final TextView tv_how_to_take = addView.findViewById(R.id.tv_how_to_take);
                                                                final TextView tv_when_to_take = addView.findViewById(R.id.tv_when_to_take);
                                                                final TextView tv_days = addView.findViewById(R.id.tv_days);
                                                                final TextView tvprice = addView.findViewById(R.id.tvprice);
                                                                final ImageView imageview_poster = addView.findViewById(R.id.imageview_poster);

                                                                //------------------------------------------
                                                                if (isRemoveDrug_val.equals("1")) {
                                                                    remove_icon.setVisibility(View.VISIBLE);
                                                                } else {
                                                                    remove_icon.setVisibility(View.GONE);
                                                                }
                                                                //------------------------------------------


                                                                if (drugType.equals("Tablet")) {
                                                                    Picasso.with(Prescription_home.this).load(R.mipmap.tablet_icon).placeholder(R.drawable.zm_image_placeholder).error(R.mipmap.logo).into(imageview_poster);
                                                                } else if (drugType.equals("Cream")) {
                                                                    Picasso.with(Prescription_home.this).load(R.mipmap.cream_icon).placeholder(R.drawable.zm_image_placeholder).error(R.mipmap.logo).into(imageview_poster);
                                                                } else if (drugType.equals("Lotion")) {
                                                                    Picasso.with(Prescription_home.this).load(R.mipmap.lotion_icon).placeholder(R.drawable.zm_image_placeholder).error(R.mipmap.logo).into(imageview_poster);
                                                                } else {
                                                                    Picasso.with(Prescription_home.this).load(R.mipmap.medicine_icon).placeholder(R.drawable.zm_image_placeholder).error(R.mipmap.logo).into(imageview_poster);
                                                                }


                                                                tv_id.setText(id_val);
                                                                tvquery.setText(drugName);
                                                                tv_drug_type.setText(drugType);
                                                                tvspeciality.setText(drugDose);
                                                                tv_days.setText(forDays);
                                                                tv_when_to_take.setText(whenToTake);
                                                                tv_how_to_take.setText(howToTake);
                                                                tvprice.setText(quantity);

                                                                remove_icon.setOnClickListener(new View.OnClickListener() {
                                                                    @Override
                                                                    public void onClick(View v) {
                                                                        try {
                                                                            View parent = (View) v.getParent();
                                                                            //View grand_parent = (View)parent.getParent();

                                                                            TextView tv_id = (TextView) parent.findViewById(R.id.tv_id);
                                                                            String tv_id_val = tv_id.getText().toString();

                                                                            //---------------------------
                                                                            String url = Model.BASE_URL + "/sapp/removeDrug4mPrescription?os_type=android&pre_id=" + prescription_id + "&user_id=" + (Model.id) + "&drug_id=" + tv_id_val + "&token=" + Model.token;
                                                                            System.out.println("Remover Attach url-------------" + url);
                                                                            new JSON_remove_file().execute(url);
                                                                            //---------------------------

                                                                            System.out.println("Removed tv_id_val-----------" + tv_id_val);
                                                                            ((LinearLayout) addView.getParent()).removeView(addView);

                                                                        } catch (Exception e) {
                                                                            e.printStackTrace();
                                                                        }
                                                                    }
                                                                });

                                                                drugs_list_layout.addView(addView);
                                                            }
                                                        } else {
                                                            drugs_list_layout.removeAllViews();
                                                            tv_drugs_heading.setText("No drugs added yet");
                                                        }

                                                        alert.dismiss();
                                                    } catch (Exception e) {
                                                        e.printStackTrace();
                                                    }
                                                }

                                            } else {
                                                drugs_list_layout.removeAllViews();
                                                tv_drugs_heading.setText("No drugs added yet");
                                            }


                                            alert.dismiss();
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                    }
                                });

                                alert.setNegativeButton("Cancel", new View.OnClickListener() {
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

                    } else {
                        String msg_text = jobj.getString("msg");
                        Toast.makeText(Prescription_home.this, msg_text, Toast.LENGTH_SHORT).show();
                    }

                }

                scroll_view.fullScroll(View.FOCUS_UP);
                dialog.dismiss();

            } catch (Exception e) {
                e.printStackTrace();
            }
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

    public void attach_dialog() {
        List<String> mAnimals = new ArrayList<String>();
        mAnimals.add("Draw Signature");
        mAnimals.add("Attach Signature");

        final CharSequence[] Animals = mAnimals.toArray(new String[mAnimals.size()]);
        android.app.AlertDialog.Builder dialogBuilder = new android.app.AlertDialog.Builder(this);
        dialogBuilder.setTitle("Attach Files/Images");
        dialogBuilder.setItems(Animals, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int item) {
                String selectedText = Animals[item].toString();
                System.out.println("selectedText---" + selectedText);

                if (selectedText.equals("Draw Signature")) {

                    intent = new Intent(Prescription_home.this, Signature_Activity.class);
                    startActivity(intent);

                } else {
                    //showChooser();

                    int permissionCheck = ContextCompat.checkSelfPermission(Prescription_home.this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
                    if (permissionCheck == PackageManager.PERMISSION_GRANTED) {
                        EasyImage.openImages(Prescription_home.this, 0);
                    } else {
                        Nammu.askForPermission(Prescription_home.this, Manifest.permission.WRITE_EXTERNAL_STORAGE, new PermissionCallback() {
                            @Override
                            public void permissionGranted() {
                                EasyImage.openImages(Prescription_home.this, 0);
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
                    File photoFile = EasyImage.lastlyTakenButCanceledPhoto(Prescription_home.this);
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

            // Picasso.with(Prescription_home.this).load(selectedPath).placeholder(R.mipmap.img_placeholder_new).error(R.mipmap.default_user).into(img_signature);

            new AsyncTask_fileupload().execute(selectedPath);

        }
    }


    private class AsyncTask_fileupload extends AsyncTask<String, Void, Boolean> {

        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            try {
                dialog = new ProgressDialog(Prescription_home.this);
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

                attach_status = jObj.getString("status");

                if (attach_status.equals("1")) {

                    if (jObj.has("doc_sign")) {
                        doc_sign_url = jObj.getString("doc_sign");
                        Model.doc_sign_url = doc_sign_url;
                        System.out.println("doc_sign_url--updated--------" + doc_sign_url);
                        Picasso.with(Prescription_home.this).load(doc_sign_url).placeholder(R.mipmap.sig_palceholder).error(R.mipmap.default_user).into(img_signature);
                    }

                    Toast.makeText(Prescription_home.this, "Signature file is successfully attached", Toast.LENGTH_SHORT).show();
                    // finish();
                } else {
                    String msg_val = jObj.getString("msg");
                    Toast.makeText(Prescription_home.this, msg_val, Toast.LENGTH_SHORT).show();
                }


            } catch (Exception e) {
                e.printStackTrace();
            }

            dialog.cancel();
        }
    }


    private String upload_file(String file_path) {

        last_upload_file = file_path;

        String ServerUploadPath = Model.BASE_URL + "/sapp/saveDocSign?user_id=" + (Model.id) + "&os_type=android&token=" + Model.token;

        //file_path = file_path.replace("file://", "");

        File file_value = new File(file_path);
        System.out.println("file_path------------" + file_path);


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

    public void apply_prescription() {
        if (drugs_length > 0) {

            String edt_chiefcomp_text = edt_chiefcomp.getText().toString();
            String edt_relpts_text = edt_relpts.getText().toString();
            String edt_examin_text = edt_examin.getText().toString();
            String edt_sugginv_text = edt_sugginv.getText().toString();
            String edt_diags_text = edt_diags.getText().toString();
            String edt_inst_text = edt_inst.getText().toString();

            System.out.println("edt_chiefcomp_text---" + edt_chiefcomp_text);
            System.out.println("edt_relpts_text---" + edt_relpts_text);
            System.out.println("edt_examin_text---" + edt_examin_text);
            System.out.println("edt_sugginv_text---" + edt_sugginv_text);
            System.out.println("edt_diags_text---" + edt_diags_text);
            System.out.println("edt_inst_text---" + edt_inst_text);

            try {

                json_feedback = new JSONObject();
                json_feedback.put("item_id", cur_qid);
                json_feedback.put("item_type", p_type);
                json_feedback.put("submit_type", "notes");
                json_feedback.put("chiefComplaints", edt_chiefcomp_text);
                json_feedback.put("relevantPoints", edt_relpts_text);
                json_feedback.put("examination", edt_examin_text);
                json_feedback.put("suggestedInvestications", edt_sugginv_text);
                json_feedback.put("provisionalDiagnosis", edt_diags_text);
                json_feedback.put("specialInstructions", edt_inst_text);

                json_feedback.put("agreed_dt", cur_date);
                //tz_string = URLEncoder.encode(tz_string, "UTF-8");
                json_feedback.put("agreed_tz_str", tz_string);
                json_feedback.put("agreed_tz", tz_val);

                System.out.println("json_feedback---" + json_feedback.toString());

                new JSON_Feedback().execute(json_feedback);

                //----------- Flurry -------------------------------------------------
                Map<String, String> articleParams = new HashMap<String, String>();
                articleParams.put("android.prescription.entry:", json_feedback.toString());
                FlurryAgent.logEvent("android.doc.prescription_entry", articleParams);
                //----------- Flurry -------------------------------------------------

            } catch (Exception e2) {
                e2.printStackTrace();
            }
        } else {
            Toast.makeText(Prescription_home.this, "Please add at least one drug name to submit", Toast.LENGTH_SHORT).show();
        }

    }

    private class JSON_remove_file extends AsyncTask<String, Void, Boolean> {

        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            dialog = new ProgressDialog(Prescription_home.this);
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

}
