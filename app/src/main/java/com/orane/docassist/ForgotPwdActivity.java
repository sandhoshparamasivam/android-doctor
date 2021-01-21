package com.orane.docassist;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.flurry.android.FlurryAgent;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.orane.docassist.Model.Model;
import com.orane.docassist.Network.JSONParser;
import com.orane.docassist.New_Main.New_MainActivity;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.scottyab.showhidepasswordedittext.ShowHidePasswordEditText;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import me.drakeet.materialdialog.MaterialDialog;

public class ForgotPwdActivity extends AppCompatActivity {

    Button btn_submit, btn_pwdsubmit, btn_code_submit;
    MaterialEditText edtemail, edt_confirmpwd;
    ShowHidePasswordEditText edt_newpwd;
    public String user_id, status_text, code_txt, name_txt, email_text, status_txt, err_txt, newpwd_text, confirmpwd_text, user_id_txt, isvalid, pin_txt, edt_otp_text;
    JSONObject post_json, login_json, login_jsonobj, jsonobj_forgotpwd, jsonobj_pwd_submit, jsonobj_pinsubmit;
    LinearLayout AskMail_Layout, verify_layout, enterpwd_Layout;
    EditText edt_otp;
    TextView tv_resend, tv_mobno, tv_text;


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
    public static final String sp_has_free_follow = "sp_has_free_follow_key";
    public static final String token = "token_key";
    public static final String short_url = "short_url_key";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.forgot_pwd);

        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        //------- Initialize ------------------==========================================

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.app_color));
        }

        //------------ Object Creations -------------------------------
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setTitle("Forgot Password");
        }
        //------------ Object Creations -------------------------------

        //=========================================================

        //----------- Flurry -------------------------------------------------
        Map<String, String> articleParams = new HashMap<String, String>();
        articleParams.put("android.doc.id", Model.id);
        FlurryAgent.logEvent("android.doc.ForgotPwd_Screen", articleParams);
        //----------- Flurry -------------------------------------------------


        //------- Initialize ------------------==========================================
        btn_submit = findViewById(R.id.btn_submit);
        btn_pwdsubmit = findViewById(R.id.btn_pwdsubmit);
        btn_code_submit = findViewById(R.id.btn_otp_submit);

        tv_mobno = findViewById(R.id.tv_mobno);
        tv_text = findViewById(R.id.tv_text);
        tv_resend = findViewById(R.id.tv_resend);

        edt_otp = findViewById(R.id.edt_otp);
        edtemail = findViewById(R.id.edtemail);
        edt_newpwd = findViewById(R.id.edt_newpwd);
        edt_confirmpwd = findViewById(R.id.edt_confirmpwd);
        AskMail_Layout = findViewById(R.id.AskMail_Layout);
        enterpwd_Layout = findViewById(R.id.enterpwd_Layout);
        verify_layout = findViewById(R.id.verify_layout);

        AskMail_Layout.setVisibility(View.VISIBLE);
        enterpwd_Layout.setVisibility(View.GONE);
        verify_layout.setVisibility(View.GONE);


        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (isInternetOn()) {
                    email_text = edtemail.getText().toString();
                    System.out.println("forgot_email_text----" + email_text);

                    if (!email_text.equals("")) {

                        try {
                            post_json = new JSONObject();
                            post_json.put("email", email_text);
                            post_json.put("user_type", "2");

                            System.out.println("Forgot pwd json------------" + post_json.toString());

                            new Async_requestCode().execute(post_json);


                            //------------ Google firebase Analitics--------------------
                            Model.mFirebaseAnalytics = FirebaseAnalytics.getInstance(getApplicationContext());
                            Bundle params = new Bundle();
                            params.putString("User", Model.id + "/" + Model.name);
                            params.putString("Details", post_json.toString());
                            Model.mFirebaseAnalytics.logEvent("Forgot_Pwd", params);
                            //------------ Google firebase Analitics--------------------
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else {
                        edtemail.setError("Please enter your email address");
                    }

                } else {
                    Toast.makeText(getApplicationContext(), "Please check your Internet Connection and try again", Toast.LENGTH_SHORT).show();
                }
            }
        });

        edtemail.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

                if (actionId == EditorInfo.IME_ACTION_DONE) {

                    btn_submit.performClick();
                    return true;
                }
                return false;
            }
        });


        btn_code_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (isInternetOn()) {

                    try {
                        edt_otp_text = edt_otp.getText().toString();

                        if (code_txt.equals(edt_otp_text)) {
                            System.out.println("Code is Matched-----------");

                            AskMail_Layout.setVisibility(View.GONE);
                            verify_layout.setVisibility(View.GONE);
                            enterpwd_Layout.setVisibility(View.VISIBLE);

                            //------------ Google firebase Analitics--------------------
                            Model.mFirebaseAnalytics = FirebaseAnalytics.getInstance(getApplicationContext());
                            Bundle params = new Bundle();
                            params.putString("User", Model.id + "/" + Model.name);
                            Model.mFirebaseAnalytics.logEvent("Forgot_Pwd", params);
                            //------------ Google firebase Analitics--------------------

                        } else {
                            Toast.makeText(ForgotPwdActivity.this, "Verification Code is incorrect. Please enter a valid Code.", Toast.LENGTH_SHORT).show();

                            System.out.println("Code is not matched-----------");
                            System.out.println("edt_otp_text-----------" + edt_otp_text);
                            System.out.println("code_txt-----------" + code_txt);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Please check your Internet Connection and try again", Toast.LENGTH_SHORT).show();
                }
            }
        });


        edt_otp.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {

                    btn_code_submit.performClick();
                    return true;
                }
                return false;
            }
        });


        btn_pwdsubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (isInternetOn()) {

                    newpwd_text = edt_newpwd.getText().toString();
                    confirmpwd_text = edt_confirmpwd.getText().toString();


                    if (newpwd_text.length() < 8 || !isValidPassword(newpwd_text)) {
                        System.out.println("Pwd is Not Valid");
                        diag_err_pwd();
                    } else {
                        System.out.println("newpwd_text----" + newpwd_text);
                        System.out.println("confirmpwd_text----" + confirmpwd_text);

                        try {

                            if (newpwd_text.equals(confirmpwd_text)) {

                                post_json = new JSONObject();
                                post_json.put("pwd", newpwd_text);
                                post_json.put("user_id", user_id_txt);

                                System.out.println("Submit_pwd_json------------" + post_json.toString());

                                new Async_Submitpwd().execute(post_json);

                                //------------ Google firebase Analitics--------------------
                                Model.mFirebaseAnalytics = FirebaseAnalytics.getInstance(getApplicationContext());
                                Bundle params = new Bundle();
                                params.putString("User", Model.id + "/" + Model.name);
                                Model.mFirebaseAnalytics.logEvent("Forgot_pwdsubmit", params);
                                //------------ Google firebase Analitics--------------------

                            } else {
                                System.out.println("Password is not matched------");
                                Toast.makeText(ForgotPwdActivity.this, "Those passwords don't match", Toast.LENGTH_SHORT).show();
                                edt_confirmpwd.setError("Those passwords don't match");
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                } else {
                    Toast.makeText(getApplicationContext(), "Please check your Internet Connection and try again", Toast.LENGTH_SHORT).show();
                }
            }
        });

        edt_confirmpwd.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {

                    btn_pwdsubmit.performClick();
                    return true;
                }
                return false;
            }
        });

        tv_resend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btn_submit.performClick();
            }
        });
    }


    private class Async_requestCode extends AsyncTask<JSONObject, Void, Boolean> {

        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            dialog = new ProgressDialog(ForgotPwdActivity.this);
            dialog.setMessage("Sending verification code. Please wait");
            dialog.show();
            dialog.setCancelable(false);

        }

        @Override
        protected Boolean doInBackground(JSONObject... urls) {

            try {

                JSONParser jParser = new JSONParser();
                jsonobj_forgotpwd = jParser.JSON_POST(urls[0], "getforgotpwd_requet");

                System.out.println("jsonobj_forgotpwd_resoponse----------" + jsonobj_forgotpwd.toString());

                return true;

            } catch (Exception e) {
                e.printStackTrace();
            }

            return false;
        }

        protected void onPostExecute(Boolean result) {

            try {

                if (!jsonobj_forgotpwd.has("err")) {

                    name_txt = jsonobj_forgotpwd.getString("name");
                    user_id_txt = jsonobj_forgotpwd.getString("user_id");
                    code_txt = jsonobj_forgotpwd.getString("code");

                    if (code_txt != null && !code_txt.isEmpty() && !code_txt.equals("null") && !code_txt.equals("")) {

                        tv_mobno.setText(email_text);

                        AskMail_Layout.setVisibility(View.GONE);
                        verify_layout.setVisibility(View.VISIBLE);
                        enterpwd_Layout.setVisibility(View.GONE);

                    } else {
                        Toast.makeText(getApplicationContext(), "Incorrect Email address. Please enter valid Email address.", Toast.LENGTH_LONG).show();
                    }

                } else {
                    err_txt = jsonobj_forgotpwd.getString("err");
                    email_notvalid(err_txt);
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

            dialog.dismiss();

        }
    }


    private class Async_Submitpwd extends AsyncTask<JSONObject, Void, Boolean> {

        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            dialog = new ProgressDialog(ForgotPwdActivity.this);
            dialog.setMessage("Resetting password. Please wait");
            dialog.show();
            dialog.setCancelable(false);
        }

        @Override
        protected Boolean doInBackground(JSONObject... urls) {

            try {
                JSONParser jParser = new JSONParser();
                jsonobj_pwd_submit = jParser.JSON_POST(urls[0], "confirm_password");

                System.out.println("jsonobj_Submit_pwd_resoponse----------" + jsonobj_pwd_submit.toString());

                return true;
            } catch (Exception e) {
                e.printStackTrace();
            }

            return false;
        }

        protected void onPostExecute(Boolean result) {

            try {

                status_text = jsonobj_pwd_submit.getString("status");
                System.out.println("Status_text----------" + status_text);

                if (status_text.equals("1")) {

                    try {

                        //----------- Flurry -------------------------------------------------
                        Map<String, String> articleParams = new HashMap<String, String>();
                        articleParams.put("android.doc.user_id", user_id_txt);
                        articleParams.put("android.doc.pwd", newpwd_text);
                        FlurryAgent.logEvent("android.doc.Password_Change_Success", articleParams);
                        //----------- Flurry -------------------------------------------------

                    } catch (Exception t) {
                        t.printStackTrace();
                    }

                    success();

                } else {
                    Toast.makeText(getApplicationContext(), "Password submission failed. Please try again.", Toast.LENGTH_LONG).show();
                }


            } catch (Exception e) {
                e.printStackTrace();
            }

            dialog.dismiss();

        }
    }

    public void success() {

        final MaterialDialog alert = new MaterialDialog(ForgotPwdActivity.this);
        alert.setTitle("Success..!");
        alert.setMessage("Your password has been changed.");
        alert.setCanceledOnTouchOutside(false);
        alert.setPositiveButton("OK", new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                alert.dismiss();

                ((android.os.ResultReceiver) getIntent().getParcelableExtra("finisher")).send(1, new Bundle());

                try {
                    login_json = new JSONObject();
                    login_json.put("username", user_id_txt);
                    login_json.put("pwd", confirmpwd_text);
                    login_json.put("user_type", "2");

                    new JSON_Login().execute(login_json);

                } catch (Exception e) {
                    e.printStackTrace();
                }

/*                Intent i = new Intent(ForgotPwdActivity.this, MainActivity.class);
                startActivity(i);
                alert.dismiss();
                finish();*/
            }
        });

        alert.show();
    }

    public void email_notvalid(String err) {
        final MaterialDialog alert = new MaterialDialog(ForgotPwdActivity.this);
        alert.setTitle("Oops..!");
        alert.setMessage(err);
        alert.setCanceledOnTouchOutside(false);
        alert.setPositiveButton("OK", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alert.dismiss();
            }
        });

        alert.show();
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

/*
    public String get_phone_type() {

        TelephonyManager manager = (TelephonyManager) getApplicationContext().getSystemService(Context.TELEPHONY_SERVICE);
        if (manager.getPhoneType() == TelephonyManager.PHONE_TYPE_NONE) {
            return "Tablet";
        } else {
            return "Mobile";
        }
    }*/


    private class JSON_Login extends AsyncTask<JSONObject, Void, Boolean> {

        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            dialog = new ProgressDialog(ForgotPwdActivity.this);
            dialog.setMessage("Logging in, please wait");
            dialog.show();
            dialog.setCancelable(false);
        }

        @Override
        protected Boolean doInBackground(JSONObject... urls) {

            try {

                JSONParser jParser = new JSONParser();
                login_jsonobj = jParser.JSON_POST(urls[0], "loginSubmit");

                System.out.println("urls[0]----------" + urls[0]);
                System.out.println("login_jsonobj---------" + login_jsonobj.toString());

                return true;
            } catch (Exception e) {
                e.printStackTrace();
            }
            return false;
        }

        protected void onPostExecute(Boolean result) {

            try {

                isvalid = login_jsonobj.getString("isValid");

                if (login_jsonobj.has("id")) {
                    user_id = login_jsonobj.getString("id");
                    Model.token = login_jsonobj.getString("token");
                    Model.id = user_id;
                }

                System.out.println("isvalid----------" + isvalid);

                if (isvalid.equals("0")) {
                    //Toast.makeText(getApplicationContext(), "Login Failed. Try Again.!", Toast.LENGTH_LONG).show();

                    if (login_jsonobj.has("pendingStatus")) {

                        String pend_status = login_jsonobj.getString("pendingStatus");
                        System.out.println("pend_status-----------" + pend_status);

                        JSONObject jobj = new JSONObject(pend_status);
                        String pendingStage = jobj.getString("pendingStage");
                        System.out.println("pendingStage----" + pendingStage);

                        if (pendingStage.equals("1")) {

                            String isMobileVerified = jobj.getString("isMobileVerified");
                            String isEmailVerified = jobj.getString("isEmailVerified");

                            System.out.println("isMobileVerified------------- " + isMobileVerified);
                            System.out.println("isEmailVerified------------- " + isEmailVerified);

                            if (isMobileVerified.equals("0")) {

                                Model.query_launch = "login";

                                Intent intent = new Intent(ForgotPwdActivity.this, OTPActivity.class);
                                intent.putExtra("type", "login");
                                startActivity(intent);

                            } else if (isEmailVerified.equals("0")) {

                                Intent intent = new Intent(ForgotPwdActivity.this, Email_OTPActivity.class);
                                intent.putExtra("type", "login");
                                startActivity(intent);
                            }

                        } else if (pendingStage.equals("2")) {

                            Model.query_launch = "login";
                            Intent intent = new Intent(ForgotPwdActivity.this, Signup2.class);
                            intent.putExtra("type", "login");
                            startActivity(intent);

                        } else if (pendingStage.equals("3")) {
                            Model.query_launch = "login";
                            Intent intent = new Intent(ForgotPwdActivity.this, Signup3.class);
                            intent.putExtra("type", "login");
                            startActivity(intent);

                        } else if (pendingStage.equals("0")) {

                            if (login_jsonobj.has("profile_pending_review")) {

                                String pend_rev = login_jsonobj.getString("profile_pending_review");

                                if (pend_rev.equals("1")) {
                                    Model.query_launch = "login";
                                    Intent intent = new Intent(ForgotPwdActivity.this, ThankyouActivity.class);
                                    intent.putExtra("type", "login");
                                    startActivity(intent);
                                    //overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);
                                }
                            }
                        }

                    } else {

                        if (login_jsonobj.has("profile_pending_review")) {

                            String pend_rev = login_jsonobj.getString("profile_pending_review");

                            if (pend_rev.equals("1")) {
                                Model.query_launch = "login";
                                Intent intent = new Intent(ForgotPwdActivity.this, ThankyouActivity.class);
                                intent.putExtra("type", "login");
                                startActivity(intent);
                                //overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);
                            }

                        } else if (login_jsonobj.has("credentialing_progress")) {

                            String is_cred = login_jsonobj.getString("credentialing_progress");
                            Model.token = login_jsonobj.getString("token");

                            if (is_cred.equals("1")) {
                                Model.query_launch = "login";
                                Intent intent = new Intent(ForgotPwdActivity.this, Credentials_Activity.class);
                                intent.putExtra("type", "login");
                                intent.putExtra("user_id", user_id);
                                startActivity(intent);
                                finish();
                            }

                        } else {

                            try {

                                Toast.makeText(getApplicationContext(), "Incorrect Username or Password. Please enter your valid Username and Password", Toast.LENGTH_LONG).show();

                                //----------- Flurry -------------------------------------------------
                                Map<String, String> articleParams = new HashMap<String, String>();
                                articleParams.put("android.doc.Logwith", (edtemail.getText().toString() + "/" + (confirmpwd_text)));
                                FlurryAgent.logEvent("android.doc.Login_Access_Failed", articleParams);
                                //----------- Flurry -------------------------------------------------

                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }

                } else {

                    Model.kmid = login_jsonobj.getString("kmid");
                    Model.isValid = login_jsonobj.getString("isValid");
                    Model.name = login_jsonobj.getString("name");
                    Model.id = login_jsonobj.getString("id");
                    Model.browser_country = login_jsonobj.getString("browser_country");
                    Model.email = login_jsonobj.getString("email");
                    Model.fee_q = login_jsonobj.getString("fee_q");
                    Model.fee_consult = login_jsonobj.getString("fee_consult");
                    Model.fee_q_inr = login_jsonobj.getString("fee_q_inr");
                    Model.fee_consult_inr = login_jsonobj.getString("fee_consult_inr");
                    Model.currency_symbol = login_jsonobj.getString("currency_symbol");
                    Model.currency_label = login_jsonobj.getString("currency_label");
                    Model.have_free_credit = login_jsonobj.getString("have_free_credit");
                    Model.photo_url = login_jsonobj.getString("photo_url");
                    Model.token = login_jsonobj.getString("token");

                    //----------------------------------------------------------------
                    if (login_jsonobj.has("short_url")) {
                        Model.short_url = login_jsonobj.getString("short_url");
                    } else {
                        Model.short_url = "";
                    }
                    //----------------------------------------------------------------

                    //============================================================
                    SharedPreferences.Editor editor = sharedpreferences.edit();
                    editor.putString(Login_Status, "1");
                    editor.putString(isValid, Model.isValid);
                    editor.putString(user_name, Model.email);
                    editor.putString(Name, Model.name);
                    editor.putString(id, Model.id);
                    editor.putString(browser_country, Model.browser_country);
                    editor.putString(email, Model.email);
                    editor.putString(fee_q, Model.fee_q);
                    editor.putString(fee_consult, Model.fee_consult);
                    editor.putString(fee_q_inr, Model.fee_q_inr);
                    editor.putString(fee_consult_inr, Model.fee_consult_inr);
                    editor.putString(currency_symbol, Model.currency_symbol);
                    editor.putString(currency_label, Model.currency_label);
                    editor.putString(have_free_credit, Model.have_free_credit);
                    editor.putString(photo_url, Model.photo_url);
                    editor.putString(sp_km_id, Model.kmid);
                    editor.putString(token, Model.token);
                    editor.putString(short_url, Model.short_url);
                    editor.apply();
                    //============================================================

                    Toast.makeText(getApplicationContext(), "Login Success...", Toast.LENGTH_LONG).show();

                    //-------------------------------------
                    Intent i = new Intent(ForgotPwdActivity.this, New_MainActivity.class);
                    startActivity(i);
                    finish();
                    //-------------------------------------

                    try {

                        String PhoneModel = Build.MODEL;
                        System.out.println("PhoneModel--------------" + PhoneModel);

                        //String AndroidVersion = android.os.Build.VERSION.RELEASE;

                        //----------- Flurry -------------------------------------------------
                        Map<String, String> articleParams = new HashMap<String, String>();
                        articleParams.put("android.doc.App_version:", (Model.app_ver));
                        articleParams.put("android.doc.id:", Model.id);
                        articleParams.put("android.doc.Logwith", (edtemail.getText().toString() + "/" + (confirmpwd_text)));
                        FlurryAgent.logEvent("android.doc.Login_Success", articleParams);
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

    public final boolean isInternetOn() {

        ConnectivityManager connec = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);

        if (connec.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.CONNECTED ||
                connec.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.CONNECTING ||
                connec.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.CONNECTING ||
                connec.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.CONNECTED) {
            return true;

        } else if (connec.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.DISCONNECTED ||
                connec.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.DISCONNECTED) {
            return false;
        }
        return false;
    }

    public static boolean isValidPassword(final String password) {

        Pattern pattern;
        Matcher matcher;
        final String PASSWORD_PATTERN = "^(?=.*[0-9])(?=.*[A-Z])(?=.*[@#$%^&+=!])(?=\\S+$).{4,}$";
        pattern = Pattern.compile(PASSWORD_PATTERN);
        matcher = pattern.matcher(password);

        return matcher.matches();

    }

    public void diag_err_pwd() {


        try {
            final MaterialDialog alert = new MaterialDialog(ForgotPwdActivity.this);
            alert.setTitle("Incorrect password. Please try again.");
            alert.setMessage("Password should contain atleast 1 Uppercase, 1 lowercase, 1 number, 1 special symbol and minimum 8 charecters");
            alert.setCanceledOnTouchOutside(false);
            alert.setPositiveButton("OK", new View.OnClickListener() {
                @Override
                public void onClick(View v) {

/*                    edt_password.setError("Incorrect password. Please try again.");
                    edt_password.requestFocus();*/

                    alert.dismiss();
                }
            });
/*

            alert.setNegativeButton("No", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    alert.dismiss();
                }
            });
*/

            alert.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
