package com.orane.docassist;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.telephony.TelephonyManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.flurry.android.FlurryAgent;

import com.google.firebase.analytics.FirebaseAnalytics;

import com.orane.docassist.Model.Model;
import com.orane.docassist.Network.JSONParser;
import com.orane.docassist.R;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Email_OTPActivity extends AppCompatActivity {

    Spinner spinner_ccode;
    Map<String, String> cc_map = new HashMap<String, String>();
    Button btn_submit, btn_mobsubmit;
    TextView tv_ccode, tv_text, tv_mobno, tv_wait, tv1, tv_resend, tv_timertext;
    EditText edt_otp, edt_phoneno;
    EditText edt_mobno;
    JSONObject jsonobj, jsonobj_verify_email_response, jsonobj_change_mono_response, json_change_mobno, json_validate;
    LinearLayout otp_layout;
    public String email_pin, email_text, t_text, vpin_text, cccode_text, phno_text, mVerificationId, userid, isValid_val, country, str_response, selected_cc_value, selected_cc_text, isvalid, pin_val, otp_text, user_id, status_val, user_id_val, otp_code, cc_name, cccode, phoneno_text;
    public static Email_OTPActivity otpinst;
    JSONObject json, request_response_json;
    RelativeLayout ccode_layout;
    Button btn_mob_done;
    private final boolean hasStarted = false;
    ImageView img_mob_edit;
    RelativeLayout mobno_layout;
    Button btn_ignore;
    LinearLayout edit_mobno_layout;
    String new_cccode, t_val, mobile_val, mccodeval;

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
    public static final String first_query = "first_query_key";
    public static final String sp_mcode = "sp_mcode_key";
    public static final String sp_mnum = "sp_mnum_key";
    public static final String sp_has_free_follow = "sp_has_free_follow_key";
    public static final String token = "token_key";
    public static final String chat_tip = "chat_tip_key";


    public static Email_OTPActivity instance() {
        return otpinst;
    }

    @Override
    public void onStart() {
        super.onStart();
        otpinst = this;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.email_otp_screen);

        FlurryAgent.onPageView();

        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);

        //--------------------------------------------------------------------
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setTitle("Email Verification");
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.app_color2));
        }
        //--------------------------------------------------------------------

        otp_layout = (LinearLayout) findViewById(R.id.otp_layout);
        edt_phoneno = (EditText) findViewById(R.id.edt_phoneno);
        btn_ignore = (Button) findViewById(R.id.btn_ignore);

        tv_wait = (TextView) findViewById(R.id.tv_wait);
        tv_timertext = (TextView) findViewById(R.id.tv_timertext);
        tv_resend = (TextView) findViewById(R.id.tv_resend);
        tv_text = (TextView) findViewById(R.id.tv_text);
        tv_mobno = (TextView) findViewById(R.id.tv_mobno);
        img_mob_edit = (ImageView) findViewById(R.id.img_mob_edit);
        edt_mobno = (EditText) findViewById(R.id.edt_mobno);
        edt_otp = (EditText) findViewById(R.id.edt_otp);
        btn_mob_done = (Button) findViewById(R.id.btn_mob_done);
        btn_submit = (Button) findViewById(R.id.btn_submit);

        mobno_layout = (RelativeLayout) findViewById(R.id.mobno_layout);
        edit_mobno_layout = (LinearLayout) findViewById(R.id.edit_mobno_layout);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        try {

            Intent intent = getIntent();
            email_text = intent.getStringExtra("email");
            email_pin = intent.getStringExtra("email_pin");

            System.out.println("email_text-----" + email_text);
            System.out.println("email_pin-----" + email_pin);

            tv_mobno.setText(email_text);

            //---------------------------------------------------------
            String url = Model.BASE_URL + "sapp/country?os_type=android";
            System.out.println("url-------------" + url);
            //new JSON_getCountry().execute(url);
            //---------------------------------------------------------

            mobno_layout.setVisibility(View.VISIBLE);

            if ((Model.query_launch).equals("login")) {

                try {

                    phoneno_text = edt_phoneno.getText().toString();
                    System.out.println("phoneno_text----" + phoneno_text);

                    //--------------Send OTP---------------------------------------------
                    try {

                        json_validate = new JSONObject();
                        json_validate.put("email", "");

                        System.out.println("json_validate----" + json_validate.toString());

                        new Async_SendOTP().execute(json_validate);

                        Toast.makeText(getApplicationContext(), "OTP has been sent to your Email address. Please check.", Toast.LENGTH_SHORT).show();

                        //--------------------------------------------------
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    //--------------Send OTP---------------------------------------------

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

/*      tv_ccode.setText("+" + Model.country_code);
        selected_cc_value = Model.country_code;
        selected_cc_text = Model.pat_country;*/

        btn_ignore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edit_mobno_layout.setVisibility(View.GONE);
                otp_layout.setVisibility(View.VISIBLE);
            }
        });

        img_mob_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                edit_mobno_layout.setVisibility(View.VISIBLE);
                otp_layout.setVisibility(View.GONE);

                edt_phoneno.setText("");

/*                //-------------- Stop Timer ------------------------------
                hasStarted = false;
                myCountDownTimer.cancel();

                if (hasStarted) {
                    myCountDownTimer.cancel();
                }
                timer_layout.setVisibility(View.GONE);
                tv_resend.setVisibility(View.VISIBLE);
                tv_wait.setVisibility(View.GONE);
                //-------------- Stop Timer ------------------------------*/

            }
        });


        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                otp_text = edt_otp.getText().toString();

                System.out.println("Entered otp_text-----" + otp_text);
                System.out.println("Got pin_val-----" + email_pin);

                if (otp_text.equals(email_pin)) {


                    try {
                        json_change_mobno = new JSONObject();

                        json_change_mobno.put("vpin", otp_text);
                        System.out.println("json_change_mobno----" + json_change_mobno.toString());

                        new JSON_verify_Mobno().execute(json_change_mobno);

                        //------------ Google firebase Analitics--------------------
                        Model.mFirebaseAnalytics = FirebaseAnalytics.getInstance(getApplicationContext());
                        Bundle params = new Bundle();
                        params.putString("User", Model.id + "/" + Model.name);
                        params.putString("Details", json_change_mobno.toString());
                        Model.mFirebaseAnalytics.logEvent("Doctor_Signup_Update_Email", params);
                        //------------ Google firebase Analitics--------------------

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

/*
                    Toast.makeText(getApplicationContext(), "OTP Matched", Toast.LENGTH_SHORT).show();

                    finishAffinity();
                    Intent i = new Intent(Email_OTPActivity.this, Signup2.class);
                    startActivity(i);
                    finish();
*/


                    getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

                } else {
                    edt_otp.requestFocus();
                    edt_otp.setError("The OTP you have entered is incorrect. Please enter the valid OTP.");
                    //Snackbar.make(v, "Entered OTP is incorrect. Please try again.", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                }

                //finish();
            }
        });


        btn_mob_done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String old_ccode = cccode_text;
                String old_phno = phno_text;
                String old_full_phno = old_ccode + old_phno;

                String new_phno = edt_phoneno.getText().toString();
                System.out.println("new_phno--------" + new_phno);

                if (!new_phno.equals("")) {

                    tv_mobno.setText(new_phno);

                    if (old_full_phno.equals(new_phno)) {
                        Toast.makeText(getApplicationContext(), "Please confirm the Email address", Toast.LENGTH_SHORT).show();
                    } else {

                        try {
                            json_change_mobno = new JSONObject();

                            json_change_mobno.put("email", new_phno);

                            System.out.println("json_change_mobno----" + json_change_mobno.toString());

                            new JSON_Change_Mobno().execute(json_change_mobno);

                            //------------ Google firebase Analitics--------------------
                            Model.mFirebaseAnalytics = FirebaseAnalytics.getInstance(getApplicationContext());
                            Bundle params = new Bundle();
                            params.putString("User", Model.id + "/" + Model.name);
                            params.putString("Details", json_change_mobno.toString());
                            Model.mFirebaseAnalytics.logEvent("Doctor_Signup_Update_Email", params);
                            //------------ Google firebase Analitics--------------------

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Please enter your mobile number", Toast.LENGTH_SHORT).show();
                    edt_phoneno.setError("Please enter your mobile number");
                    edt_phoneno.requestFocus();
                }
            }
        });


        tv_resend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {

                    phoneno_text = edt_phoneno.getText().toString();
                    System.out.println("phoneno_text----" + phoneno_text);

                    //--------------Send OTP---------------------------------------------
                    try {

                        json_validate = new JSONObject();
                        json_validate.put("email", phoneno_text);

                        System.out.println("json_validate----" + json_validate.toString());

                        new Async_SendOTP().execute(json_validate);

                        Toast.makeText(getApplicationContext(), "OTP has been resent to your registered mobile number to your email address", Toast.LENGTH_SHORT).show();

                        //--------------------------------------------------
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    //--------------Send OTP---------------------------------------------

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });


    }

    public void getSMS(final String otp_value) {

        edt_otp.setText("" + otp_value);

        if (otp_value.length() == 4) {
            if (hasStarted) {

                try {
                    //----------- Flurry -------------------------------------------------
                    Map<String, String> articleParams = new HashMap<String, String>();
                    articleParams.put("android.patient.OTP", otp_value);
                    articleParams.put("android.patient.Mobile_number", phoneno_text);
                    FlurryAgent.logEvent("android.patient.OTP_Receive", articleParams);
                    //----------- Flurry -------------------------------------------------

                } catch (Exception t) {
                    t.printStackTrace();
                }

                btn_submit.performClick();

                System.out.println("PIN Received----" + otp_value);
                // myCountDownTimer.cancel();
            }
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


    private class Async_SendOTP extends AsyncTask<JSONObject, Void, Boolean> {

        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

/*            dialog = new ProgressDialog(OTPActivity.this);
            dialog.setMessage("Please wait..");
            dialog.show();
            dialog.setCancelable(false);*/
        }

        @Override
        protected Boolean doInBackground(JSONObject... urls) {
            try {
                JSONParser jParser = new JSONParser();
                jsonobj = jParser.JSON_POST(urls[0], "resend_OTP_email");

                return true;

            } catch (Exception e) {
                e.printStackTrace();
            }

            return false;
        }

        protected void onPostExecute(Boolean result) {

            try {

                System.out.println("jsonobj-------------" + jsonobj.toString());

                status_val = jsonobj.getString("status");
                email_pin = jsonobj.getString("vpin");
                t_text = jsonobj.getString("t");
                email_text = jsonobj.getString("email");

                tv_mobno.setText(email_text);

                tv_resend.setVisibility(View.VISIBLE);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    private class JSON_Change_Mobno extends AsyncTask<JSONObject, Void, Boolean> {

        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            dialog = new ProgressDialog(Email_OTPActivity.this);
            dialog.setMessage("Updating.. please wait");
            dialog.show();
            dialog.setCancelable(false);
        }

        @Override
        protected Boolean doInBackground(JSONObject... urls) {

            try {
                JSONParser jParser = new JSONParser();
                jsonobj_change_mono_response = jParser.JSON_POST(urls[0], "signupDoc_Update_email");

                System.out.println("urls[0]----------" + urls[0]);
                System.out.println("jsonobj_change_mono_response----------" + jsonobj_change_mono_response.toString());

                return true;
            } catch (Exception e) {
                e.printStackTrace();
            }
            return false;
        }

        protected void onPostExecute(Boolean result) {

            try {

                status_val = jsonobj_change_mono_response.getString("status");

                if (status_val.equals("1")) {

                    edit_mobno_layout.setVisibility(View.GONE);
                    otp_layout.setVisibility(View.VISIBLE);
                    edt_otp.setText("");
                    tv_resend.setVisibility(View.VISIBLE);

                    email_pin = jsonobj_change_mono_response.getString("vpin");
                    t_val = jsonobj_change_mono_response.getString("t");
                    email_text = jsonobj_change_mono_response.getString("email");

                } else {

                    edit_mobno_layout.setVisibility(View.VISIBLE);
                    otp_layout.setVisibility(View.GONE);

                    String err_val = jsonobj_change_mono_response.getString("err");

                    Toast.makeText(getApplicationContext(), err_val, Toast.LENGTH_LONG).show();

                    edt_phoneno.requestFocus();
                    edt_phoneno.setError(err_val);

                    try {

                        //----------- Flurry -------------------------------------------------
                        Map<String, String> articleParams = new HashMap<String, String>();
                        articleParams.put("android.doc.App_version:", (Model.app_ver));
                        FlurryAgent.logEvent("android.doc.email_Update_Failed", articleParams);
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


    private class JSON_verify_Mobno extends AsyncTask<JSONObject, Void, Boolean> {

        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            dialog = new ProgressDialog(Email_OTPActivity.this);
            dialog.setMessage("please wait");
            dialog.show();
            dialog.setCancelable(false);
        }

        @Override
        protected Boolean doInBackground(JSONObject... urls) {

            try {
                JSONParser jParser = new JSONParser();
                jsonobj_verify_email_response = jParser.JSON_POST(urls[0], "signupDoc_verify_email");

                System.out.println("urls[0]----------" + urls[0]);
                System.out.println("jsonobj_verify_email_response----------" + jsonobj_verify_email_response.toString());

                return true;
            } catch (Exception e) {
                e.printStackTrace();
            }
            return false;
        }

        protected void onPostExecute(Boolean result) {

            try {

                status_val = jsonobj_verify_email_response.getString("status");

                if (status_val.equals("1")) {

                    Toast.makeText(getApplicationContext(), "OTP verified successfully", Toast.LENGTH_LONG).show();

                    finishAffinity();
                    Intent i = new Intent(Email_OTPActivity.this, Signup2.class);
                    i.putExtra("user_id", Model.id);
                    startActivity(i);
                    finish();


                } else {

                    String err_val = jsonobj_verify_email_response.getString("err");
                    System.out.println("err_val---------" + err_val);

                    edt_otp.setError("OTP verification failed");
                    edt_otp.requestFocus();

                    Toast.makeText(getApplicationContext(), err_val, Toast.LENGTH_LONG).show();

                    try {

                        //----------- Flurry -------------------------------------------------
                        Map<String, String> articleParams = new HashMap<String, String>();
                        articleParams.put("android.doc.App_version:", (Model.app_ver));
                        FlurryAgent.logEvent("android.doc.Mob_Update_Failed", articleParams);
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


}
