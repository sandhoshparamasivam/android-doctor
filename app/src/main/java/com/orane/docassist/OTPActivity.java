package com.orane.docassist;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.flurry.android.FlurryAgent;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.hbb20.CountryCodePicker;
import com.orane.docassist.Model.Model;
import com.orane.docassist.Network.JSONParser;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import me.drakeet.materialdialog.MaterialDialog;

public class OTPActivity extends AppCompatActivity {

    Spinner spinner_ccode;
    Map<String, String> cc_map = new HashMap<String, String>();
    Button btn_submit, btn_mobsubmit;
    TextView tv_ccode, tv_text, tv_mobno, tv_wait, tv1, tv_resend, tv_timertext;
    EditText edt_otp, edt_phoneno;
    EditText edt_mobno;
    JSONObject jsonobj, jsonobj_verify_mono_response, jsonobj_change_mono_response, json_change_mobno, json_validate;
    LinearLayout otp_layout, timer_layout;
    public String email_pin_val, email_text, t_text, vpin_text, cccode_text, phno_text, mVerificationId, userid, isValid_val, country, str_response, selected_cc_value, selected_cc_text, isvalid, pin_val, otp_text, user_id, status_val, user_id_val, otp_code, cc_name, cccode, phoneno_text;
    public static OTPActivity otpinst;
    JSONObject json, request_response_json;
    ProgressBar progressBar;
    RelativeLayout ccode_layout;
    Button btn_mob_done;
    MyCountDownTimer myCountDownTimer;
    private boolean hasStarted = false;
    ImageView img_mob_edit;
    RelativeLayout mobno_layout;
    Button btn_ignore;
    LinearLayout edit_mobno_layout;
    CountryCodePicker countryCodePicker;
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


    public static OTPActivity instance() {
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
        setContentView(R.layout.otp_screen);

        FlurryAgent.onPageView();

        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);

        //--------------------------------------------------------------------
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setTitle("Mobile Verification");
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.app_color2));
        }
        //--------------------------------------------------------------------

        otp_layout = findViewById(R.id.otp_layout);
        edt_phoneno = findViewById(R.id.edt_phoneno);
        btn_ignore = findViewById(R.id.btn_ignore);

        tv_wait = findViewById(R.id.tv_wait);
        tv_timertext = findViewById(R.id.tv_timertext);
        tv_resend = findViewById(R.id.tv_resend);
        tv_text = findViewById(R.id.tv_text);
        tv_mobno = findViewById(R.id.tv_mobno);
        img_mob_edit = findViewById(R.id.img_mob_edit);
        edt_mobno = findViewById(R.id.edt_mobno);
        edt_otp = findViewById(R.id.edt_otp);
        btn_mob_done = findViewById(R.id.btn_mob_done);
        btn_submit = findViewById(R.id.btn_submit);
        progressBar = findViewById(R.id.progressBar);

        countryCodePicker = findViewById(R.id.ccp);
        mobno_layout = findViewById(R.id.mobno_layout);
        edit_mobno_layout = findViewById(R.id.edit_mobno_layout);
        timer_layout = findViewById(R.id.timer_layout);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        try {

            Intent intent = getIntent();
            email_text = intent.getStringExtra("email");
            cccode_text = intent.getStringExtra("cccode");
            phno_text = intent.getStringExtra("phno");
            pin_val = intent.getStringExtra("vpin_text");
            t_text = intent.getStringExtra("t_text");

            tv_mobno.setText(cccode_text + " " + phno_text);

            //---------------------------------------------------------
            String url = Model.BASE_URL + "sapp/country?os_type=android&";
            System.out.println("url-------------" + url);
            //new JSON_getCountry().execute(url);
            //---------------------------------------------------------

            //---------- Start Timer -------------------------------
            try {
                if (hasStarted) {
                    myCountDownTimer.cancel();
                }
                timer_layout.setVisibility(View.VISIBLE);
                myCountDownTimer = new MyCountDownTimer(60000, 1000);
                myCountDownTimer.start();
                hasStarted = true;

            } catch (Exception e) {
                e.printStackTrace();
            }
            //---------- Start Timer -------------------------------


            mobno_layout.setVisibility(View.VISIBLE);


            if ((Model.query_launch).equals("login")) {
                try {

                    phoneno_text = edt_phoneno.getText().toString();
                    System.out.println("phoneno_text----" + phoneno_text);

                    //--------------Send OTP---------------------------------------------
                    try {
                        json_validate = new JSONObject();
                        json_validate.put("mobile", "");
                        json_validate.put("country_code", "");

                        System.out.println("json_validate----" + json_validate.toString());

                        new Async_SendOTP().execute(json_validate);

                        Toast.makeText(getApplicationContext(), "OTP has been sent to the registered mobile number", Toast.LENGTH_SHORT).show();

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

        progressBar.setMax(60);


        countryCodePicker.registerCarrierNumberEditText(edt_phoneno);


        countryCodePicker.setOnCountryChangeListener(new CountryCodePicker.OnCountryChangeListener() {
            @Override
            public void onCountrySelected() {

                System.out.println("getSelectedCountryCode------------" + countryCodePicker.getSelectedCountryCode());
                System.out.println("getSelectedCountryCodeAsInt------------" + countryCodePicker.getSelectedCountryCodeAsInt());
                System.out.println("getSelectedCountryName------------" + countryCodePicker.getSelectedCountryName());
                System.out.println("getSelectedCountryNameCode------------" + countryCodePicker.getSelectedCountryNameCode());

                new_cccode = "" + countryCodePicker.getSelectedCountryCodeAsInt();
                cccode = new_cccode;
                System.out.println("cccode--------------" + cccode);
            }
        });

        countryCodePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

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

                //-------------- Stop Timer ------------------------------
                hasStarted = false;
                myCountDownTimer.cancel();

                if (hasStarted) {
                    myCountDownTimer.cancel();
                }
                timer_layout.setVisibility(View.GONE);
                tv_resend.setVisibility(View.VISIBLE);
                tv_wait.setVisibility(View.GONE);
                //-------------- Stop Timer ------------------------------

            }
        });


        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                otp_text = edt_otp.getText().toString();

                System.out.println("Entered otp_text-----" + otp_text);
                System.out.println("Got pin_val-----" + pin_val);

                if (otp_text.equals(pin_val)) {

                    if (hasStarted) {
                        hasStarted = false;
                        myCountDownTimer.cancel();
                    }

                    //verifyPhoneNumberWithCode(mVerificationId, otp_text);

                    Toast.makeText(getApplicationContext(), "OTP Matched", Toast.LENGTH_SHORT).show();


                    try {
                        json_change_mobno = new JSONObject();

                        json_change_mobno.put("vpin", otp_text);
                        System.out.println("json_change_mobno----" + json_change_mobno.toString());

                        new JSON_verify_Mobno().execute(json_change_mobno);

                        //------------ Google firebase Analitics--------------------
                        Model.mFirebaseAnalytics = FirebaseAnalytics.getInstance(getApplicationContext());
                        Bundle params = new Bundle();
                        params.putString("User", Model.id + "/" + Model.name);
                        Model.mFirebaseAnalytics.logEvent("Doctor_Signup_Update_Mobno", params);
                        //------------ Google firebase Analitics--------------------

                    } catch (Exception e) {
                        e.printStackTrace();
                    }



/*                    finishAffinity();
                    Intent i = new Intent(OTPActivity.this, Email_OTPActivity.class);
                    i.putExtra("email", email_text);
                    startActivity(i);
                    finish();*/

                    getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

                } else {
                    //edt_otp.setError("Enter OTP");
                    Snackbar.make(v, "The OTP you have entered is incorrect. Please enter the valid OTP.", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                    edt_otp.setError("The OTP you have entered is incorrect. Please enter the valid OTP.");
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

                String new_ccode = "" + countryCodePicker.getSelectedCountryCodeAsInt();
                String new_phno = edt_phoneno.getText().toString();
                String new_full_phno = new_ccode + new_phno;

                System.out.println("new_ccode----------" + new_ccode);

                if (!new_phno.equals("")) {


                    tv_mobno.setText(new_ccode + " " + new_phno);

                    if (old_full_phno.equals(new_full_phno)) {
                        Toast.makeText(getApplicationContext(), "You have entered same mobile number", Toast.LENGTH_SHORT).show();
                    } else {

                        // validate_mob_no();

                        try {
                            json_change_mobno = new JSONObject();

                            json_change_mobno.put("ccode", new_ccode);
                            json_change_mobno.put("mobile", new_phno);

                            System.out.println("json_change_mobno----" + json_change_mobno.toString());

                            new JSON_Change_Mobno().execute(json_change_mobno);


                            //------------ Google firebase Analitics--------------------
                            Model.mFirebaseAnalytics = FirebaseAnalytics.getInstance(getApplicationContext());
                            Bundle params = new Bundle();
                            params.putString("User", Model.id + "/" + Model.name);
                            Model.mFirebaseAnalytics.logEvent("Doctor_Signup_Update_Mobno", params);
                            //------------ Google firebase Analitics--------------------


                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Please enter your mobile number", Toast.LENGTH_SHORT).show();
                    edt_phoneno.setError("Please enter your mobile number");
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
                        json_validate.put("mobile", phoneno_text);
                        json_validate.put("country_code", cccode);

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
                    //----------------------------------------------------------------------------
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
                myCountDownTimer.cancel();
            }
        }
    }

    public class MyCountDownTimer extends CountDownTimer {

        public MyCountDownTimer(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);

            tv_resend.setVisibility(View.GONE);
            tv_wait.setVisibility(View.VISIBLE);

        }

        @Override
        public void onTick(long millisUntilFinished) {


            int progress = (int) (millisUntilFinished / 1000);
            progressBar.setProgress(progressBar.getMax() - progress);

            int seconds = (int) (millisUntilFinished / 1000);
            int minutes = seconds / 60;
            seconds = seconds % 60;
            System.out.println("TIME : " + String.format("%02d", minutes) + ":" + String.format("%02d", seconds));

            tv_timertext.setText(minutes + " : " + seconds);
        }

        @Override
        public void onFinish() {
            System.out.println("Finish------------");
            //-------------- Stop Timer ------------------------------
            hasStarted = false;
            if (hasStarted) {
                myCountDownTimer.cancel();
            }
            timer_layout.setVisibility(View.GONE);
            tv_resend.setVisibility(View.VISIBLE);
            tv_wait.setVisibility(View.GONE);
            //-------------- Stop Timer ------------------------------
            //finish();
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
            try {
                if (hasStarted) {
                    myCountDownTimer.cancel();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            finish();

            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    public void ask_Signup() {

        final MaterialDialog alert = new MaterialDialog(OTPActivity.this);
        //alert.setTitle("mobile number not Exist..!");
        alert.setMessage("This mobile number is not exist, Do you want to signup now?");
        alert.setCanceledOnTouchOutside(false);
        alert.setPositiveButton("Yes, Signup", new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //============================================================
                SharedPreferences.Editor editor = sharedpreferences.edit();
                editor.putString(Login_Status, "0");
                editor.apply();
                //============================================================

                //finishAffinity();
                Intent intent = new Intent(OTPActivity.this, Signup1.class);
                startActivity(intent);
                finish();
            }
        });


        alert.setNegativeButton("No", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alert.dismiss();
            }
        });

        alert.show();
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
                jsonobj = jParser.JSON_POST(urls[0], "resend_OTP_Mob");

                return true;

            } catch (Exception e) {
                e.printStackTrace();
            }

            return false;
        }

        protected void onPostExecute(Boolean result) {

            try {

                System.out.println("jsonobj-------------" + jsonobj.toString());

               // email_text = "";
                status_val = jsonobj.getString("status");
                cccode_text = jsonobj.getString("ccode");
                phno_text = jsonobj.getString("mobile");
                pin_val = jsonobj.getString("vpin");
                t_text = jsonobj.getString("t");

                tv_mobno.setText(cccode_text + " " + phno_text);

                //---------- Start Timer -------------------------------
                try {
                    if (hasStarted) {
                        myCountDownTimer.cancel();
                    }
                    timer_layout.setVisibility(View.VISIBLE);
                    myCountDownTimer = new MyCountDownTimer(60000, 1000);
                    myCountDownTimer.start();
                    hasStarted = true;

                } catch (Exception e) {
                    e.printStackTrace();
                }
                //---------- Start Timer -------------------------------

                tv_resend.setVisibility(View.GONE);
                tv_wait.setVisibility(View.VISIBLE);

                //dialog.cancel();

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

            dialog = new ProgressDialog(OTPActivity.this);
            dialog.setMessage("Updating.. please wait");
            dialog.show();
            dialog.setCancelable(false);
        }

        @Override
        protected Boolean doInBackground(JSONObject... urls) {

            try {
                JSONParser jParser = new JSONParser();
                jsonobj_change_mono_response = jParser.JSON_POST(urls[0], "signupDoc_Update_Mobno");

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
                System.out.println("Changr mobno status_val-------------- " + status_val);

                if (status_val.equals("1")) {

                    edit_mobno_layout.setVisibility(View.GONE);
                    otp_layout.setVisibility(View.VISIBLE);

                    pin_val = jsonobj_change_mono_response.getString("vpin");
                    t_val = jsonobj_change_mono_response.getString("t");
                    mobile_val = jsonobj_change_mono_response.getString("mobile");
                    mccodeval = jsonobj_change_mono_response.getString("ccode");

                    edit_mobno_layout.setVisibility(View.GONE);

                    //tv_mobno.setText(new_cccode + " " + new_cccode);
                    edt_otp.setText("");

                    tv_resend.setVisibility(View.GONE);
                    tv_wait.setVisibility(View.VISIBLE);


                    //---------- Start Timer -------------------------------
                    try {
                        if (hasStarted) {
                            myCountDownTimer.cancel();
                        }
                        timer_layout.setVisibility(View.VISIBLE);
                        myCountDownTimer = new MyCountDownTimer(60000, 1000);
                        myCountDownTimer.start();
                        hasStarted = true;

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    //---------- Start Timer -------------------------------


                } else {

                    edit_mobno_layout.setVisibility(View.VISIBLE);
                    otp_layout.setVisibility(View.GONE);

                    String err_val = jsonobj_change_mono_response.getString("err");
                    System.out.println("err_val--------------" + err_val);
                    Toast.makeText(getApplicationContext(), err_val, Toast.LENGTH_LONG).show();

                    edt_phoneno.requestFocus();
                    edt_phoneno.setError(err_val);

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


    private class JSON_verify_Mobno extends AsyncTask<JSONObject, Void, Boolean> {

        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            dialog = new ProgressDialog(OTPActivity.this);
            dialog.setMessage("please wait");
            dialog.show();
            dialog.setCancelable(false);
        }

        @Override
        protected Boolean doInBackground(JSONObject... urls) {

            try {
                JSONParser jParser = new JSONParser();
                jsonobj_verify_mono_response = jParser.JSON_POST(urls[0], "signupDoc_verify_Mobno");

                System.out.println("urls[0]----------" + urls[0]);
                System.out.println("jsonobj_verify_mono_response----------" + jsonobj_verify_mono_response.toString());

                return true;
            } catch (Exception e) {
                e.printStackTrace();
            }
            return false;
        }

        protected void onPostExecute(Boolean result) {

            try {

                if (jsonobj_verify_mono_response.has("token_status")) {
                    String token_status = jsonobj_verify_mono_response.getString("token_status");

                    if (token_status.equals("0")) {
                        Toast.makeText(OTPActivity.this, "Invalid token, please verify and try again.", Toast.LENGTH_SHORT).show();
                    }
                } else {

                    status_val = jsonobj_verify_mono_response.getString("status");

                    if (status_val.equals("1")) {

                        email_pin_val = jsonobj_verify_mono_response.getString("vpin");
                        String email_t_val = jsonobj_verify_mono_response.getString("t");

                        Toast.makeText(getApplicationContext(), "OTP verified successfully", Toast.LENGTH_LONG).show();

                        //finishAffinity();
                        Intent i = new Intent(OTPActivity.this, Email_OTPActivity.class);
                        i.putExtra("email", email_text);
                        i.putExtra("email_pin", email_pin_val);
                        startActivity(i);
                        finish();

                    } else {

                        String err_val = jsonobj_verify_mono_response.getString("err");

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
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            dialog.cancel();

        }
    }


}
