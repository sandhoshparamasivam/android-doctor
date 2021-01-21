package com.orane.docassist;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.flurry.android.FlurryAgent;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.orane.docassist.Model.Model;
import com.orane.docassist.Network.JSONParser;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


public class AddNotesActivity extends AppCompatActivity {

    Button btn_submit;
    EditText edt_feedback;
    public String pat_id, feedback_val, report_response;
    JSONObject json_feedback, json_response_obj;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_notes);

        try {
            Intent intent = getIntent();
            pat_id = intent.getStringExtra("pat_id");
            System.out.println("Get pat_id---" + pat_id);

        } catch (Exception e) {
            e.printStackTrace();
        }

        //------------ Object Creations -------------------------------
        /*
         *
         * */
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

        edt_feedback = findViewById(R.id.edt_feedback);
        btn_submit = findViewById(R.id.btn_submit);

        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                feedback_val = edt_feedback.getText().toString();

                if (feedback_val.equals("")) {

                    edt_feedback.setError("Please enter your notes");

                } else {

                    try {

                        json_feedback = new JSONObject();
                        json_feedback.put("user_id", (Model.id));
                        json_feedback.put("patient_id", pat_id);
                        json_feedback.put("note", feedback_val);

                        System.out.println("json_feedback---" + json_feedback.toString());

                        new JSON_Feedback().execute(json_feedback);

                        //say_success();

                        //------------ Google firebase Analytics--------------------
                        Model.mFirebaseAnalytics = FirebaseAnalytics.getInstance(AddNotesActivity.this);
                        Bundle params = new Bundle();
                        params.putString("User", Model.id + "/" + Model.name);
                        params.putString("Details", feedback_val);
                        Model.mFirebaseAnalytics.logEvent("notes_entry", params);
                        //------------ Google firebase Analitics--------------------

                        //------------- Flurry ----------------------------------------------
                        Map<String, String> articleParams = new HashMap<String, String>();
                        articleParams.put("android.doc.notes_entry:", feedback_val);
                        FlurryAgent.logEvent("android.doc.notes_entry", articleParams);
                        //------------- Flurry ----------------------------------------------

                    } catch (Exception e2) {
                        e2.printStackTrace();
                    }
                }
            }
        });
    }

    private class JSON_Feedback extends AsyncTask<JSONObject, Void, Boolean> {

        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            try {
                dialog = new ProgressDialog(AddNotesActivity.this);
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
                json_response_obj = jParser.JSON_POST(urls[0], "notes_entry");

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
                Model.query_launch = "added_notes";

                if (report_response.equals("1")) {
                    say_success();
                } else {
                    Toast.makeText(getApplicationContext(), "Something went wrong. Please try again.", Toast.LENGTH_LONG).show();
                }

                dialog.cancel();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    public void say_success() {

        try {

          /*  new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)
                    .setTitleText("Success..!")
                    .setContentText("Your note has been saved")
                    .setConfirmText("Ok")
                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sDialog) {
                            sDialog.dismiss();
                            finish();
                        }
                    })
              .show();*/

            Toast.makeText(getApplicationContext(), "Your notes has sent successfully", Toast.LENGTH_SHORT);

            finish();

        } catch (Exception e) {
            e.printStackTrace();
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
}
