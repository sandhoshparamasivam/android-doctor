package com.orane.docassist;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.flurry.android.FlurryAgent;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.orane.docassist.Model.Model;
import com.orane.docassist.Network.JSONParser;
import com.orane.docassist.New_Main.New_MainActivity;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.scottyab.showhidepasswordedittext.ShowHidePasswordEditText;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.util.HashMap;
import java.util.Map;


public class LoginActivity extends Activity {

    JSONObject login_jsonobj, login_json;
    Button btnlogin;
    MaterialEditText edtemail;
    ShowHidePasswordEditText edtpassword;

    TextView tvforgotpw, tv_signup;
    BufferedReader reader = null;
    public StringBuffer json_response = new StringBuffer();
    public String isvalid, url, user_id;

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
    public static final String token = "token_key";
    public static final String short_url = "short_url_key";

    SharedPreferences sharedpreferences;
    TextView tv_otp;

    String uname, pwd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.app_color));
        }

        btnlogin = findViewById(R.id.btnlogin);
        edtemail = findViewById(R.id.edtemail);
        edtpassword = findViewById(R.id.edtpassword);
        tvforgotpw = findViewById(R.id.tvforgotpw);
        tv_signup = findViewById(R.id.tv_signup);
        tv_otp = findViewById(R.id.tv_otp);

        if (!isInternetOn()) {
            Toast.makeText(getApplicationContext(), "Please check your Internet Connection and try again", Toast.LENGTH_SHORT).show();
        }

        tvforgotpw.setText(Html.fromHtml(getResources().getString(R.string.forgotpw)));

        tv_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

/*                Intent intent = new Intent(LoginActivity.this, Signup_MyCertificates_Activity.class);
                startActivity(intent);*/

                Intent intent = new Intent(LoginActivity.this, Signup1.class);
                startActivity(intent);

/*              Intent intent = new Intent(LoginActivity.this, Signup1.class);
                intent.putExtra("finisher", new android.os.ResultReceiver(null) {
                    @Override
                    protected void onReceiveResult(int resultCode, Bundle resultData) {
                        LoginActivity.this.finish();
                    }
                });
                startActivityForResult(intent, 1);

                //--------- Google Analytics Events ---------------------------
                MyApp.tracker().send(new HitBuilders.EventBuilder()
                        .setCategory("Login_Page")
                        .setAction("Signup_Button")
                        .build());
                //--------- Google Analytics Events ---------------------------*/

            }
        });

        tv_otp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(LoginActivity.this, Signup1.class);
                startActivity(intent);
            }
        });


        edtpassword.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {

                    btnlogin.performClick();
                    return true;
                }
                return false;
            }
        });


        tvforgotpw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(LoginActivity.this, ForgotPwdActivity.class);
                intent.putExtra("finisher", new android.os.ResultReceiver(null) {
                    @Override
                    protected void onReceiveResult(int resultCode, Bundle resultData) {
                        LoginActivity.this.finish();
                    }
                });
                startActivityForResult(intent, 1);

            }
        });

        btnlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

/*        Intent i = new Intent(LoginActivity.this, Voxeet.class);
        i.putExtra("cons_user_name", "Mohan_app");
        i.putExtra("conf_name", "icliniq");
        startActivity(i);*/


                uname = edtemail.getText().toString();
                pwd = edtpassword.getText().toString();

                if (isInternetOn()) {

                    try {

                        if (uname.equals("")) edtemail.setError("Please enter your Email or Mobile Number");
                        else if (pwd.equals("")) edtpassword.setError("Please enter your password");
                        else {
                            //url = Model.BASE_URL + sub_url + "?username=" + uname + "&pwd=" + password + "&user_type=1";

                            login_json = new JSONObject();
                            login_json.put("username", uname);
                            login_json.put("pwd", pwd);
                            login_json.put("user_type", "2");

                            System.out.println("login_json---------------" + login_json.toString());

                            new JSON_Login().execute(login_json);

                            //------------ Google firebase Analitics--------------------
                            Model.mFirebaseAnalytics = FirebaseAnalytics.getInstance(getApplicationContext());
                            Bundle params = new Bundle();
                            params.putString("User", Model.id + "/" + Model.name);
                            Model.mFirebaseAnalytics.logEvent("Login_Access", params);
                            //------------ Google firebase Analitics--------------------

                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Please check your Internet Connection and try again", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private class JSON_Login extends AsyncTask<JSONObject, Void, Boolean> {

        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            dialog = new ProgressDialog(LoginActivity.this);
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

                                Intent intent = new Intent(LoginActivity.this, OTPActivity.class);
                                intent.putExtra("type", "login");
                                startActivity(intent);

                            } else if (isEmailVerified.equals("0")) {

                                Intent intent = new Intent(LoginActivity.this, Email_OTPActivity.class);
                                intent.putExtra("type", "login");
                                startActivity(intent);
                            }

                        } else if (pendingStage.equals("2")) {

                            Model.query_launch = "login";
                            Intent intent = new Intent(LoginActivity.this, Signup2.class);
                            intent.putExtra("type", "login");
                            startActivity(intent);

                        } else if (pendingStage.equals("3")) {
                            Model.query_launch = "login";
                            Intent intent = new Intent(LoginActivity.this, Signup3.class);
                            intent.putExtra("type", "login");
                            startActivity(intent);

                        } else if (pendingStage.equals("0")) {

                            if (login_jsonobj.has("profile_pending_review")) {

                                String pend_rev = login_jsonobj.getString("profile_pending_review");

                                if (pend_rev.equals("1")) {
                                    Model.query_launch = "login";
                                    Intent intent = new Intent(LoginActivity.this, ThankyouActivity.class);
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
                                Intent intent = new Intent(LoginActivity.this, ThankyouActivity.class);
                                intent.putExtra("type", "login");
                                startActivity(intent);
                                //overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);
                            }

                        } else if (login_jsonobj.has("credentialing_progress")) {

                            String is_cred = login_jsonobj.getString("credentialing_progress");

                            Model.token = login_jsonobj.getString("token");

                            if (is_cred.equals("1")) {
                                Model.query_launch = "login";
                                Intent intent = new Intent(LoginActivity.this, Credentials_Activity.class);
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
                                articleParams.put("android.doc.Logwith", (edtemail.getText().toString() + "/" + (edtpassword.getText().toString())));
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

                    //------------------------------------
                    //Model.id = "410066";
                    //------------------------------------

                    //----------------------------------------------------------------
                    if (login_jsonobj.has("short_url")) {
                        Model.short_url = login_jsonobj.getString("short_url");
                    } else {
                        Model.short_url = "";
                    }
                    //--------------------------------  --------------------------------

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
                    Intent i = new Intent(LoginActivity.this, New_MainActivity.class);
                    startActivity(i);
                    finish();
                    //-------------------------------------

                    try {

                        String PhoneModel = Build.MODEL;

                        System.out.println("PhoneModel--------------" + PhoneModel);

                        //----------- Flurry -------------------------------------------------
                        Map<String, String> articleParams = new HashMap<String, String>();
                        articleParams.put("android.doc.App_version:", (Model.app_ver));
                        articleParams.put("android.doc.id:", Model.id);
                        articleParams.put("android.doc.Logwith", (edtemail.getText().toString() + "/" + (edtpassword.getText().toString())));
                        FlurryAgent.logEvent("android.doc.Login_Success", articleParams);
                        //----------- Flurry -------------------------------------------------

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

            dialog.dismiss();

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

    public static boolean isTablet(Context context) {

        return (context.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) >= Configuration.SCREENLAYOUT_SIZE_LARGE;
    }
}