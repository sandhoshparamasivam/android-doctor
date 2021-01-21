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
import android.text.Html;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;


import com.orane.docassist.Model.Item;
import com.orane.docassist.Model.Model;
import com.orane.docassist.Network.Detector;
import com.orane.docassist.Network.JSONParser;
import com.orane.docassist.R;
import com.orane.docassist.adapter.HotlinePatientsRowAdapter;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;

import me.drakeet.materialdialog.MaterialDialog;

public class HotlineHome extends AppCompatActivity {

    Toolbar toolbar;
    ListView listView;
    LinearLayout nolayout, netcheck_layout, updateprice_layout, invite_layout, hl_patients_layout;
    ScrollView first_layout;
    RelativeLayout LinearLayout1;
    String params;
    List<Item> arrayOfList;
    JSONObject jsonoj_status, json_disable;
    HotlinePatientsRowAdapter objAdapter;
    ProgressBar progressBar, progressBar_bottom;
    Switch switch_hl_enable;
    ImageView imgapp;
    public String str_response, status_val;
    TextView tv_postatus, tv_whatis;

    SharedPreferences sharedpreferences;
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
    public static final String sp_share_link = "sp_share_link";
    public static final String token = "token_key";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.hotline_home);


        //================ Shared Pref ======================
        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        Model.token = sharedpreferences.getString(token, "");
        System.out.println("Model.token----------------------" + Model.token);
        //================ Shared Pref ======================

        //---------------------------------------------------------
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setTitle("");

            TextView mTitle = (TextView) toolbar.findViewById(R.id.toolbar_title);
            Typeface khandBold = Typeface.createFromAsset(getApplicationContext().getAssets(), Model.font_name_bold);
            mTitle.setTypeface(khandBold);
        }

        //---------------------------------------------------------
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.app_color));
        }

        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        updateprice_layout = (LinearLayout) findViewById(R.id.updateprice_layout);
        LinearLayout1 = (RelativeLayout) findViewById(R.id.LinearLayout1);
        netcheck_layout = (LinearLayout) findViewById(R.id.netcheck_layout);
        invite_layout = (LinearLayout) findViewById(R.id.invite_layout);
        hl_patients_layout = (LinearLayout) findViewById(R.id.hl_patients_layout);
        nolayout = (LinearLayout) findViewById(R.id.nolayout);
        tv_postatus = (TextView) findViewById(R.id.tv_postatus);
        tv_whatis = (TextView) findViewById(R.id.tv_whatis);
        switch_hl_enable = (Switch) findViewById(R.id.switch_hl_enable);
        first_layout = (ScrollView) findViewById(R.id.first_layout);


        Typeface font_reg = Typeface.createFromAsset(getAssets(), Model.font_name);
        Typeface font_bold = Typeface.createFromAsset(getAssets(), Model.font_name_bold);

        ((TextView) findViewById(R.id.tvhltit)).setTypeface(font_bold);
        ((TextView) findViewById(R.id.tit2)).setTypeface(font_bold);
        ((TextView) findViewById(R.id.tv_pausepayout)).setTypeface(font_reg);
        ((TextView) findViewById(R.id.tv_postatus)).setTypeface(font_bold);
        ((TextView) findViewById(R.id.tv_rowtit1)).setTypeface(font_bold);
        ((TextView) findViewById(R.id.tv_rowdesc1)).setTypeface(font_reg);
        ((TextView) findViewById(R.id.tv_rowtit2)).setTypeface(font_bold);
        ((TextView) findViewById(R.id.tv_rowdesc2)).setTypeface(font_reg);
        ((TextView) findViewById(R.id.tv_rowtit3)).setTypeface(font_bold);
        ((TextView) findViewById(R.id.tv_rowdesc3)).setTypeface(font_reg);
        ((TextView) findViewById(R.id.tv_whatis)).setTypeface(font_bold);

        full_process();

        tv_whatis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                show_guidelines();
            }
        });

        switch_hl_enable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (isInternetOn()) {

                    if (switch_hl_enable.isChecked()) {

                        tv_postatus.setText("Enabled");
                        System.out.println("Hotline Enabled");
                        Intent intent = new Intent(HotlineHome.this, HotlineFeeActivity.class);
                        startActivity(intent);
                        overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);

                    } else {

                        tv_postatus.setText("Disabled");
                        System.out.println("Hotline Disabled");
                        //---------------------- Reff Fee Get -----------------------------
                        String full_url = Model.BASE_URL + "sapp/disableHotline?os_type=android&user_id=" + (Model.id) + "&token=" + Model.token;
                        System.out.println("full_url--------------" + full_url);
                        new Disable_Hotline().execute(full_url);
                        //---------------------- Reff Fee Get -----------------------------
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Please check your Internet Connection and try again", Toast.LENGTH_SHORT).show();
                }
            }
        });


        updateprice_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (isInternetOn()) {
                    Intent intent = new Intent(HotlineHome.this, HotlineFeeActivity.class);
                    startActivity(intent);
                    overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);
                } else {
                    Toast.makeText(getApplicationContext(), "Please check your Internet Connection and try again", Toast.LENGTH_SHORT).show();
                }
            }
        });

        invite_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (isInternetOn()) {

                    Intent intent = new Intent(HotlineHome.this, Invite_Pat_To_Hotline.class);
                    startActivity(intent);
                    overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);

                } else {
                    Toast.makeText(getApplicationContext(), "Please check your Internet Connection and try again", Toast.LENGTH_SHORT).show();
                }
            }
        });

        hl_patients_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(HotlineHome.this, HotlinePatientsActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);
            }
        });

    }

    private class Disable_Hotline extends AsyncTask<String, Void, Boolean> {

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
                return true;
            } catch (Exception e) {
                e.printStackTrace();
            }

            return false;
        }

        protected void onPostExecute(Boolean result) {

            try {
                json_disable = new JSONObject(str_response);

                if (json_disable.has("token_status")) {
                    String token_status = json_disable.getString("token_status");
                    if (token_status.equals("0")) {

                        //============================================================
                        SharedPreferences.Editor editor = sharedpreferences.edit();
                        editor.putString(Login_Status, "0");
                        editor.apply();
                        //============================================================

                        finishAffinity();
                        Intent intent = new Intent(HotlineHome.this, LoginActivity.class);
                        startActivity(intent);
                        finish();
                    }
                } else {
                    if (json_disable.has("status")) {
                        status_val = json_disable.getString("status");
                        System.out.println("status_val---------------" + status_val);
                        System.out.println("Hotline Status Changed.");
                    }
                }


            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    public void full_process() {

        if (isInternetOn()) {

            try {
                //--------------------New Query Notify-----------------------------------------------------
                String url2 = Model.BASE_URL + "sapp/getHotlineStatus?os_type=android&user_id=" + (Model.id) + "&token=" + Model.token;
                System.out.println("getHotlineStatus url---------" + url2);
                new JSON_Hl_status().execute(url2);
                //-------------------------------------------------------------------------
            } catch (Exception e) {
                e.printStackTrace();
            }

        } else {

            Toast.makeText(getApplicationContext(), "Please check your Internet Connection and try again", Toast.LENGTH_SHORT).show();

            progressBar.setVisibility(View.GONE);
            netcheck_layout.setVisibility(View.GONE);
            first_layout.setVisibility(View.VISIBLE);
        }
    }

    private class JSON_Hl_status extends AsyncTask<String, Void, Boolean> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
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

                jsonoj_status = new JSONObject(str_response);

                if (jsonoj_status.has("status")) {
                    status_val = jsonoj_status.getString("status");

                    if (status_val.equals("1")) {
                        switch_hl_enable.setChecked(true);
                        tv_postatus.setText("Enabled");
                    } else {
                        switch_hl_enable.setChecked(false);
                        tv_postatus.setText("Disabled");
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    public final boolean isInternetOn() {
        try {
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

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
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

    @Override
    protected void onResume() {
        super.onResume();

        try {
            if ((Model.status_val) != null && !(Model.status_val).isEmpty() && !(Model.status_val).equals("null") && !(Model.status_val).equals("")) {
                if ((Model.status_val).equals("1")) switch_hl_enable.setChecked(true);
                else switch_hl_enable.setChecked(false);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void show_guidelines() {

        try {

            final MaterialDialog alert = new MaterialDialog(HotlineHome.this);
            View view = LayoutInflater.from(HotlineHome.this).inflate(R.layout.hotlinegudlines, null);
            alert.setView(view);
            alert.setTitle("What is Hotline Chat?");
            alert.setCanceledOnTouchOutside(false);

            final TextView tvguidline = (TextView) view.findViewById(R.id.tvguidline);
            final TextView tvappverview = (TextView) view.findViewById(R.id.tvappverview);

            if (new Detector().isTablet(getApplicationContext())) {
                tvguidline.setTextSize(18);
                tvappverview.setTextSize(20);
            }

            tvguidline.setText(Html.fromHtml(getString(R.string.hotline_guidline)));
            alert.setPositiveButton("OK", new View.OnClickListener() {
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
}
