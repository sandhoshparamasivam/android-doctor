package com.orane.docassist;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
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



public class InviteDoctorActivity extends AppCompatActivity {

    private Toolbar toolbar;
    EditText edt_doctname, edt_doctemail;
    public String Log_Status, url;
    public String json_status;
    Button btn_done;
    public String str_response, doc_name, doc_email, ref_fee;
    public JSONObject jsonobj, json_ref_fee;
    TextView refer_text;

    SharedPreferences sharedpreferences;
    public static final String MyPREFERENCES = "MyPrefs";
    public static final String Login_Status = "Login_Status_key";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.invite_doctor_qases);


        //================ Shared Pref ======================
        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        //============================================================

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        refer_text = (TextView) findViewById(R.id.refer_text);
        edt_doctname = (EditText) findViewById(R.id.edt_doctname);
        edt_doctemail = (EditText) findViewById(R.id.edt_doctemail);
        btn_done = (Button) findViewById(R.id.btn_done);


        //-------------------- TootlBar --------------------------------------
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setTitle("");

            TextView mTitle = (TextView) toolbar.findViewById(R.id.toolbar_title);
            Typeface khandBold = Typeface.createFromAsset(getApplicationContext().getAssets(), Model.font_name_bold);
            mTitle.setTypeface(khandBold);
            btn_done.setTypeface(khandBold);

        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.app_color));
        }
        //-------------------- TootlBar --------------------------------------

        btn_done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //--------- Getting Data -----------------------
                doc_name = edt_doctname.getText().toString();
                doc_email = edt_doctemail.getText().toString();
                //--------- Getting Data -----------------------

                if (doc_name.length() > 0) {
                    if (doc_email.length() > 0) {

                        //-------------------------------------------------
                        url = Model.BASE_URL + "sapp/referDoc?os_type=android&user_id=" + (Model.id) + "&name=" + doc_name + "&email=" + doc_email + "&token=" + Model.token;
                        System.out.println("Invite url--------- " + url);
                        new JSONAsyncTask().execute(url);
                        //-------------------------------------------------

                        //------------ Google firebase Analitics--------------------
                        Model.mFirebaseAnalytics = FirebaseAnalytics.getInstance(getApplicationContext());
                        Bundle params = new Bundle();
                        params.putString("User", Model.id + "/" + Model.name);
                        Model.mFirebaseAnalytics.logEvent("Invite_Doctor", params);
                        //------------ Google firebase Analitics--------------------


                    } else {
                        edt_doctemail.setError("Please enter valid email address.");
                        edt_doctemail.requestFocus();
                    }
                } else {
                    edt_doctname.setError("Please enter the Doctor name");
                    edt_doctname.requestFocus();
                }
            }
        });
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

        /*if (id == R.id.notify) {
            return true;
        }*/

        return super.onOptionsItemSelected(item);
    }


    private class JSONAsyncTask extends AsyncTask<String, Void, Boolean> {

        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(InviteDoctorActivity.this);
            dialog.setMessage("please wait");
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
                        Intent intent = new Intent(InviteDoctorActivity.this, LoginActivity.class);
                        startActivity(intent);
                        finish();
                    }
                } else {

                    json_status = jsonobj.getString("status");

                    if (json_status.equals("1")) {
                        try {

                            //----------- Flurry -------------------------------------------------
                            Map<String, String> articleParams = new HashMap<String, String>();
                            articleParams.put("android.doc.Refer_Doctor_Name", doc_name);
                            articleParams.put("android.doc.Refer_Doctor_Email", doc_email);
                            FlurryAgent.logEvent("android.doc.Invite_Doctor_Qases_Success", articleParams);
                            //----------- Flurry -------------------------------------------------

                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        say_success();

                    } else {

                        say_failure();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }


            dialog.dismiss();

            edt_doctname.setText("");
            edt_doctemail.setText("");

        }
    }

    public void say_success() {

        Toast.makeText(InviteDoctorActivity.this,"You have succesfully invited a doctor!",Toast.LENGTH_SHORT).show();
/*
        new SweetAlertDialog(this, SweetAlertDialog.SUCCESS_TYPE)
                .setTitleText("Thank you!")
                .setContentText("You have succesfully invited a doctor!")
                .show();*/
    }

    public void say_failure() {

        Toast.makeText(InviteDoctorActivity.this,"Failed to invite a doctor. Please try again.",Toast.LENGTH_SHORT).show();

/*        new SweetAlertDialog(this, SweetAlertDialog.ERROR_TYPE)
                .setTitleText("Oops...")
                .setContentText("Invited failed. Please try again!")
                .show();*/
    }


}
