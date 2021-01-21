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
import android.text.Html;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.flurry.android.FlurryAgent;

import com.orane.docassist.Model.Model;
import com.orane.docassist.Network.JSONParser;
import com.orane.docassist.R;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import me.drakeet.materialdialog.MaterialDialog;


public class ReferDoctorActivity extends AppCompatActivity {

    private Toolbar toolbar;
    EditText edt_doctname, edt_doctemail;
    public String url;
    public String str_response, json_status;
    Button btn_done;
    public String doc_name, doc_email, ref_fee;
    public JSONObject jsonobj, json_ref_fee;
    TextView refer_text;

    SharedPreferences sharedpreferences;
    public static final String MyPREFERENCES = "MyPrefs";
    public static final String Login_Status = "Login_Status_key";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.refer_doctor);

        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);

        toolbar = (Toolbar) findViewById(R.id.toolBar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setTitle("Refer a Doctor");
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.app_color));
        }

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        refer_text = (TextView) findViewById(R.id.refer_text);
        edt_doctname = (EditText) findViewById(R.id.edt_doctname);
        edt_doctemail = (EditText) findViewById(R.id.edt_doctemail);
        btn_done = (Button) findViewById(R.id.btn_done);

        //---------------------- Reff Fee Get -----------------------------
        String params = Model.BASE_URL + "/sapp/getDocReferralFee?os_type=android&user_id=" + (Model.id) + "&token=" + Model.token;
        System.out.println("ReferFee URL--------" + params);
        new JSON_RefFee().execute(params);
        //---------------------- Reff Fee Get -----------------------------

        btn_done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                doc_name = edt_doctname.getText().toString();
                doc_email = edt_doctemail.getText().toString();

                System.out.println("doc_name--------------" + doc_name);
                System.out.println("doc_email--------------" + doc_email);

                if (doc_name.length() > 0) {
                    if (doc_email.length() > 0) {

                        //------------------------------
                        url = Model.BASE_URL + "sapp/referDoc?os_type=android&user_id=" + (Model.id) + "&name=" + doc_name + "&email=" + doc_email + "&token=" + Model.token;
                        System.out.println("url---------" + url);
                        new JSONAsyncTask().execute(url);
                        //------------------------------

                    } else {
                        edt_doctname.setError("Please enter the doctor Email id");
                    }
                } else {
                    edt_doctname.setError("Please enter the doctor name");
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


    class JSONAsyncTask extends AsyncTask<String, Void, Boolean> {

        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(ReferDoctorActivity.this);
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
                        Intent intent = new Intent(ReferDoctorActivity.this, LoginActivity.class);
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
                            FlurryAgent.logEvent("android.doc.Refer_Doctor_Success", articleParams);
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


            dialog.cancel();
            edt_doctname.setText("");
            edt_doctemail.setText("");

        }
    }

    public void say_success() {


        //---------------- Dialog------------------------------------------------------------------
        final MaterialDialog alert = new MaterialDialog(ReferDoctorActivity.this);
        alert.setTitle("Thank you!");
        alert.setMessage("You have succesfully invited a doctor!");
        alert.setCanceledOnTouchOutside(false);
        alert.setPositiveButton("OK", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alert.dismiss();
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

    public void say_failure() {


        //---------------- Dialog------------------------------------------------------------------
        final MaterialDialog alert = new MaterialDialog(ReferDoctorActivity.this);
        alert.setTitle("Oops...!");
        alert.setMessage("Referring failed. Please try again!");
        alert.setCanceledOnTouchOutside(false);
        alert.setPositiveButton("OK", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alert.dismiss();
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


    class JSON_RefFee extends AsyncTask<String, Void, Boolean> {

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
                json_ref_fee = new JSONObject(str_response);

                if (json_ref_fee.has("token_status")) {
                    String token_status = json_ref_fee.getString("token_status");
                    if (token_status.equals("0")) {
                        //============================================================
                        SharedPreferences.Editor editor = sharedpreferences.edit();
                        editor.putString(Login_Status, "0");
                        editor.apply();
                        //============================================================
                        finishAffinity();
                        Intent intent = new Intent(ReferDoctorActivity.this, LoginActivity.class);
                        startActivity(intent);
                        finish();
                    }
                } else {
                    ref_fee = json_ref_fee.getString("fee");
                    refer_text.setText(Html.fromHtml("You will earn <b>" + ref_fee + "</b> for every successful doctor referral. Use the form below to send your invitation now!"));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}
