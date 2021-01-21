package com.orane.docassist;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
import com.rengwuxian.materialedittext.MaterialEditText;
import com.scottyab.showhidepasswordedittext.ShowHidePasswordEditText;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import me.drakeet.materialdialog.MaterialDialog;

public class ChangePwdActivity extends AppCompatActivity {

    Button btn_submit, btn_pwdsubmit, btn_code_submit;
    MaterialEditText edtemail,  edt_confirmpwd, edt_old_pwd;
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
        setContentView(R.layout.change_pwd_screen);

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
        btn_pwdsubmit = findViewById(R.id.btn_pwdsubmit);

        edt_newpwd = findViewById(R.id.edt_newpwd);
        edt_confirmpwd = findViewById(R.id.edt_confirmpwd);
        enterpwd_Layout = findViewById(R.id.enterpwd_Layout);
        edt_old_pwd = findViewById(R.id.edt_old_pwd);


        btn_pwdsubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

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
                            Toast.makeText(ChangePwdActivity.this, "Those passwords don't match", Toast.LENGTH_SHORT).show();
                            edt_confirmpwd.setError("Those passwords don't match");
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
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

    }


    private class Async_Submitpwd extends AsyncTask<JSONObject, Void, Boolean> {

        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            dialog = new ProgressDialog(ChangePwdActivity.this);
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
                    Toast.makeText(getApplicationContext(), "Password reset failed. Please try again..", Toast.LENGTH_LONG).show();
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

            dialog.dismiss();

        }
    }

    public void success() {

        final MaterialDialog alert = new MaterialDialog(ChangePwdActivity.this);
        alert.setTitle("Success..!");
        alert.setMessage("Your password has been changed.");
        alert.setCanceledOnTouchOutside(false);
        alert.setPositiveButton("OK", new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                alert.dismiss();

                ((android.os.ResultReceiver) getIntent().getParcelableExtra("finisher")).send(1, new Bundle());

                finishAffinity();
                Intent i = new Intent(ChangePwdActivity.this, LoginActivity.class);
                startActivity(i);
                alert.dismiss();
                finish();
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
            final MaterialDialog alert = new MaterialDialog(ChangePwdActivity.this);
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
