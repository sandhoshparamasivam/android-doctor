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
import androidx.appcompat.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.flurry.android.FlurryAgent;


import com.orane.docassist.Model.Model;
import com.orane.docassist.Network.Detector;
import com.orane.docassist.Network.JSONParser;
import com.orane.docassist.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import me.drakeet.materialdialog.MaterialDialog;


public class HotlineFeeActivity extends AppCompatActivity {

    LinearLayout terms;
    TextView tv_title1, tv_title2, tv_appver, tv_app_rel, hl3curr, hl6curr, hl12curr, hl3str, hl6str, hl12str;
    EditText edt_hl3, edt_hl6, edt_hl12;
    Button btn_done;
    JSONObject jsonobj, post_fee_json, jsonobj1, fee_json;
    JSONArray jsonarray;
    public String str_response, Hl_fee_curr_txt, Hl_fee_3_txt, Hl_fee_6_txt, Hl_fee_12_txt, status_val, hl3_fee, hl6_fee, hl12_fee, hl_id, hl_val_str, hl_val_int, hl_val_curr, hl_val_for;

    public static final String MyPREFERENCES = "MyPrefs";
    public static final String Login_Status = "Login_Status_key";
    public static final String Hl_fee_3 = "Hl_fee_3_key";
    public static final String Hl_fee_6 = "Hl_fee_6_key";
    public static final String Hl_fee_12 = "Hl_fee_12_key";
    public static final String Hl_fee_curr = "Hl_fee_curr_key";
    SharedPreferences sharedpreferences;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.hotline_fees_edit);

        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setTitle("Hotline Chat Fee");
        }
        //------------ Object Creations -------------------------------

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.app_color));
        }

        btn_done = (Button) findViewById(R.id.btn_done);
        edt_hl3 = (EditText) findViewById(R.id.edt_hl3);
        edt_hl6 = (EditText) findViewById(R.id.edt_hl6);
        edt_hl12 = (EditText) findViewById(R.id.edt_hl12);
        hl3curr = (TextView) findViewById(R.id.hl3curr);
        hl6curr = (TextView) findViewById(R.id.hl6curr);
        hl12curr = (TextView) findViewById(R.id.hl12curr);
        tv_appver = (TextView) findViewById(R.id.tv_appver);
        tv_app_rel = (TextView) findViewById(R.id.tv_app_rel);
        hl3str = (TextView) findViewById(R.id.hl3str);
        hl6str = (TextView) findViewById(R.id.hl6str);
        terms = (LinearLayout) findViewById(R.id.terms);
        tv_title1 = (TextView) findViewById(R.id.tv_title1);
        tv_title2 = (TextView) findViewById(R.id.tv_title2);
        hl12str = (TextView) findViewById(R.id.hl12str);

        //================ Shared Pref ======================
        Hl_fee_3_txt = sharedpreferences.getString(Hl_fee_3, "");
        Hl_fee_6_txt = sharedpreferences.getString(Hl_fee_6, "");
        Hl_fee_12_txt = sharedpreferences.getString(Hl_fee_12, "");
        Hl_fee_curr_txt = sharedpreferences.getString(Hl_fee_curr, "");
        //------------ Object Creations -------------------------------

        System.out.println("Hl_fee_3_txt-------------" + Hl_fee_3_txt);
        System.out.println("Hl_fee_6_txt-------------" + Hl_fee_6_txt);
        System.out.println("Hl_fee_12_txt-------------" + Hl_fee_12_txt);
        System.out.println("Hl_fee_curr_txt-------------" + Hl_fee_curr_txt);

        hl3curr.setText(hl_val_curr);
        edt_hl3.setText(hl_val_int);
        hl6curr.setText(hl_val_curr);
        edt_hl6.setText(hl_val_int);
        hl12curr.setText(hl_val_curr);
        edt_hl12.setText(hl_val_int);

        Typeface font_reg = Typeface.createFromAsset(getAssets(), Model.font_name);
        Typeface font_bold = Typeface.createFromAsset(getAssets(), Model.font_name_bold);

        tv_title1.setTypeface(font_bold);
        tv_title2.setTypeface(font_reg);
        hl3str.setTypeface(font_reg);
        hl3curr.setTypeface(font_reg);
        hl6str.setTypeface(font_reg);
        hl6curr.setTypeface(font_reg);
        hl12str.setTypeface(font_reg);
        hl12curr.setTypeface(font_reg);
        btn_done.setTypeface(font_bold);

        if (new Detector().isTablet(getApplicationContext())) {

            tv_title1.setTextSize(25);
            tv_title2.setTextSize(20);
            hl3str.setTextSize(22);
            hl6str.setTextSize(22);
            hl12str.setTextSize(22);

            hl3curr.setTextSize(22);
            hl6curr.setTextSize(22);
            hl12curr.setTextSize(22);

            edt_hl3.setTextSize(32);
            edt_hl6.setTextSize(32);
            edt_hl12.setTextSize(32);

            btn_done.setHeight(55);
            btn_done.setTextSize(25);
            btn_done.setPadding(10, 10, 10, 10);

        }

        if (isInternetOn()) {

            //---------------------- Reff Fee Get -----------------------------
            String full_url = Model.BASE_URL + "sapp/listSubscription?doc_id=" + (Model.id) + "&token=" + Model.token + "&for_doc=";
            System.out.println("listSubscription FEE---------" + full_url);
            new JSON_get_Fees().execute(full_url);
            //---------------------- Reff Fee Get -----------------------------

        } else {
            Toast.makeText(getApplicationContext(), "Please check your Internet Connection and try again", Toast.LENGTH_SHORT).show();
        }


        btn_done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                hl3_fee = edt_hl3.getText().toString();
                hl6_fee = edt_hl6.getText().toString();
                hl12_fee = edt_hl12.getText().toString();

                try {
                    if (hl3_fee.equals("")) hl3_fee = "0";
                    if (hl6_fee.equals("")) hl6_fee = "0";
                    if (hl12_fee.equals("")) hl12_fee = "0";

                    fee_json = new JSONObject();
                    fee_json.put("user_id", (Model.id));
                    fee_json.put("expiry_type_2", hl3_fee);
                    fee_json.put("expiry_type_3", hl6_fee);
                    fee_json.put("expiry_type_4", hl12_fee);

                    Hl_fee_curr_txt = hl3curr.getText().toString();

                    //============================================================
                    SharedPreferences.Editor editor = sharedpreferences.edit();
                    editor.putString(Hl_fee_3, hl3_fee);
                    editor.putString(Hl_fee_6, hl6_fee);
                    editor.putString(Hl_fee_12, hl12_fee);
                    editor.putString(Hl_fee_curr, Hl_fee_curr);
                    editor.apply();
                    //============================================================
                    System.out.println("fee_json-----" + fee_json.toString());

                    new JSON_PostFee().execute(fee_json);


                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

    }

    private class JSON_PostFee extends AsyncTask<JSONObject, Void, Boolean> {

        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            dialog = new ProgressDialog(HotlineFeeActivity.this);
            dialog.setMessage("Please wait");
            dialog.show();
            dialog.setCancelable(false);
        }

        @Override
        protected Boolean doInBackground(JSONObject... urls) {

            try {

                JSONParser jParser = new JSONParser();
                post_fee_json = jParser.JSON_POST(urls[0], "updateHlinePlans");
                return true;

            } catch (Exception e) {
                e.printStackTrace();
            }
            return false;
        }

        protected void onPostExecute(Boolean result) {

            try {

                status_val = post_fee_json.getString("status");

                if (status_val.equals("1")) {

                    Model.status_val = "1";

                    Fee_Success();

                    try {

                        //----------- Flurry -------------------------------------------------
                        Map<String, String> articleParams = new HashMap<String, String>();
                        articleParams.put("android.doc.User_ID", (Model.id));
                        articleParams.put("android.doc.NewFee_3_months", hl3_fee);
                        articleParams.put("android.doc.NewFee_6_months", hl6_fee);
                        articleParams.put("android.doc.NewFee_12_months", hl12_fee);
                        FlurryAgent.logEvent("android.doc.Hotline_FeeUpdate_Success", articleParams);
                        //----------- Flurry -------------------------------------------------


                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                } else {
                    Toast.makeText(getApplicationContext(), "Update Failed. Try Again.!", Toast.LENGTH_LONG).show();
                    try {
                        //----------- Flurry -------------------------------------------------
                        Map<String, String> articleParams = new HashMap<String, String>();
                        articleParams.put("android.doc.User_ID", (Model.id));
                        articleParams.put("android.doc.NewFee_3_months", hl3_fee);
                        articleParams.put("android.doc.NewFee_6_months", hl6_fee);
                        articleParams.put("android.doc.NewFee_12_months", hl12_fee);
                        FlurryAgent.logEvent("android.doc.hotline_feeupdate_failed", articleParams);
                        //----------- Flurry -------------------------------------------------


                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            dialog.cancel();

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


    private class JSON_get_Fees extends AsyncTask<String, Void, Boolean> {

        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Boolean doInBackground(String... urls) {

            try {
                str_response = new JSONParser().getJSONString(urls[0]);
                System.out.println("str_response--------------" + str_response);
            } catch (Exception e) {
                e.printStackTrace();
            }

            return false;
        }

        protected void onPostExecute(Boolean result) {

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
                        Intent intent = new Intent(HotlineFeeActivity.this, LoginActivity.class);
                        startActivity(intent);
                        finish();
                    }
                } else {

                    jsonarray = new JSONArray();
                    jsonarray.put(jsonobj);

                    System.out.println("jsonarray.length()-----" + jsonarray.length());
                    System.out.println("jsonarray-----" + jsonarray.toString());

                    for (int i = 0; i < jsonarray.length(); i++) {
                        jsonobj1 = jsonarray.getJSONObject(i);
                        System.out.println("jsonobj_first-----" + jsonobj1.toString());

                        for (int j = 1; j <= 10; j++) {

                            String s = "" + j;
                            if (jsonobj1.has("" + s)) {

                                String thread = jsonobj1.getString("" + s);
                                System.out.println("thread-----" + thread);

                                JSONObject jsononjsec = new JSONObject(thread);

                                hl_id = jsononjsec.getString("id");
                                hl_val_str = jsononjsec.getString("val");
                                hl_val_int = jsononjsec.getString("val_int");
                                hl_val_curr = jsononjsec.getString("val_currency");
                                hl_val_for = jsononjsec.getString("val_for");

                                System.out.println("hl_id-----" + hl_id);
                                System.out.println("hl_val_str-----" + hl_val_str);
                                System.out.println("hl_val_int-----" + hl_val_int);
                                System.out.println("hl_val_curr-----" + hl_val_curr);
                                System.out.println("hl_val_for-----" + hl_val_for);

                                if (s.equals("2")) {
                                    hl3curr.setText(hl_val_curr);
                                    edt_hl3.setText(hl_val_int);
                                }
                                if (s.equals("3")) {
                                    hl6curr.setText(hl_val_curr);
                                    edt_hl6.setText(hl_val_int);
                                }
                                if (s.equals("4")) {
                                    hl12curr.setText(hl_val_curr);
                                    edt_hl12.setText(hl_val_int);
                                }
                            }
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void Fee_Success() {


        //---------------- Dialog------------------------------------------------------------------
        final MaterialDialog alert = new MaterialDialog(HotlineFeeActivity.this);
        alert.setTitle("Updated successfully..!");
        alert.setMessage("Your hotline fee has been updated successfully");
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
}
