package com.orane.docassist;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.LayoutInflater;
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
import com.orane.docassist.Network.JSONParser;
import com.orane.docassist.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


import me.drakeet.materialdialog.MaterialDialog;

public class Patient_Profile extends AppCompatActivity {

    TextView tv_nonotes, tv_addnotes, tv_viewall, tv_pname, tv_gender, tv_location, tv_email, tv_mobno, tv_lastadded, tv_lastlogin;
    LinearLayout notes_list_layout, notes_layout, payment_layout, refer_layout, subs_layout, offer_layout, sensappt_layout, senmail_layout, viewquery_layout, viewcons_layout;
    Button btn_slot_send, btn_send_msg, btn_view_query, btn_cons_view;
    public String notes_text, gen_text, pat_id, edt_subject_text, edt_msg_text, pat_name, pat_email, pat_mobno, pat_gender, pat_age, pat_location, pat_lastlogin, pat_addedat;
    JSONObject jsonoj_pat, json_send_mail, json_resp_send_mail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.patient_profile);

        Model.query_launch = "";

        //------------ Object Creations -------------------------------
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setTitle("Patient Profile");
        }
        //------------ Object Creations -------------------------------

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.app_color));
        }

        viewcons_layout = (LinearLayout) findViewById(R.id.viewcons_layout);
        sensappt_layout = (LinearLayout) findViewById(R.id.sensappt_layout);
        senmail_layout = (LinearLayout) findViewById(R.id.senmail_layout);
        viewquery_layout = (LinearLayout) findViewById(R.id.viewquery_layout);
        payment_layout = (LinearLayout) findViewById(R.id.payment_layout);
        refer_layout = (LinearLayout) findViewById(R.id.refer_layout);
        subs_layout = (LinearLayout) findViewById(R.id.subs_layout);
        offer_layout = (LinearLayout) findViewById(R.id.offer_layout);
        notes_layout = (LinearLayout) findViewById(R.id.notes_layout);
        notes_list_layout = (LinearLayout) findViewById(R.id.notes_list_layout);

        tv_pname = (TextView) findViewById(R.id.tv_pname);
        tv_addnotes = (TextView) findViewById(R.id.tv_addnotes);
        tv_viewall = (TextView) findViewById(R.id.tv_viewall);
        tv_gender = (TextView) findViewById(R.id.tv_gender);
        tv_location = (TextView) findViewById(R.id.tv_location);
        tv_email = (TextView) findViewById(R.id.tv_email);
        tv_mobno = (TextView) findViewById(R.id.tv_mobno);
        tv_lastadded = (TextView) findViewById(R.id.tv_lastadded);
        tv_lastlogin = (TextView) findViewById(R.id.tv_lastlogin);
        tv_nonotes = (TextView) findViewById(R.id.tv_nonotes);

        btn_slot_send = (Button) findViewById(R.id.btn_slot_send);
        btn_send_msg = (Button) findViewById(R.id.btn_send_msg);
        btn_view_query = (Button) findViewById(R.id.btn_view_query);
        btn_cons_view = (Button) findViewById(R.id.btn_cons_view);


        try {
            Intent intent = getIntent();
            pat_id = intent.getStringExtra("pat_id");
            System.out.println("Get pat_id---" + pat_id);
        } catch (Exception e) {
            e.printStackTrace();
        }

        full_process();


        payment_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(Patient_Profile.this, MyPatientSendSlot.class);
                i.putExtra("pat_id", pat_id);
                startActivity(i);
            }
        });

        tv_viewall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(Patient_Profile.this, Patient_Notes_Activity.class);
                i.putExtra("pat_id", pat_id);
                startActivity(i);
            }
        });

        tv_addnotes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(Patient_Profile.this, AddNotesActivity.class);
                i.putExtra("pat_id", pat_id);
                startActivity(i);
            }
        });

        refer_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                send_mail();
            }
        });

        subs_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(Patient_Profile.this, AnsweredQueriesActivity.class);
                i.putExtra("source", "patient_profile");
                i.putExtra("pat_id", pat_id);
                startActivity(i);

            }
        });

        offer_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(Patient_Profile.this, ConsHistoryPatProfile.class);
                i.putExtra("pat_id", pat_id);
                startActivity(i);

            }
        });
    }

    public void full_process() {

        if (isInternetOn()) {

            try {
                String patientProfile_url = Model.BASE_URL + "docapp/patientProfile?user_id=" + (Model.id) + "&patient_id=" + pat_id + "&token=" + Model.token;
                System.out.println("patientProfile_url-----" + patientProfile_url);
                new JSON_patient_info().execute(patientProfile_url);

            } catch (Exception e) {
                e.printStackTrace();
            }

        } else {
            Toast.makeText(Patient_Profile.this, "Internet connection is not available", Toast.LENGTH_SHORT).show();
        }
    }

    private class JSON_patient_info extends AsyncTask<String, Void, Boolean> {

        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            dialog = new ProgressDialog(Patient_Profile.this);
            dialog.setMessage("Please wait");
            dialog.show();
            dialog.setCancelable(false);
        }

        @Override
        protected Boolean doInBackground(String... urls) {

            try {

                JSONParser jParser = new JSONParser();
                jsonoj_pat = jParser.getJSON_URL(urls[0]);

                return true;
            } catch (Exception e) {
                e.printStackTrace();
            }

            return false;
        }

        protected void onPostExecute(Boolean result) {

            try {

                pat_name = jsonoj_pat.getString("name");
                pat_email = jsonoj_pat.getString("email");
                pat_mobno = jsonoj_pat.getString("mobile");
                pat_gender = jsonoj_pat.getString("gender");
                pat_age = jsonoj_pat.getString("age");
                pat_location = jsonoj_pat.getString("location");
                pat_lastlogin = jsonoj_pat.getString("last_login");
                pat_addedat = jsonoj_pat.getString("added_at");
                notes_text = jsonoj_pat.getString("arrNote");

                System.out.println("pat_name-----" + pat_name);
                System.out.println("pat_email-----" + pat_email);
                System.out.println("pat_mobno-----" + pat_mobno);
                System.out.println("pat_gender-----" + pat_gender);
                System.out.println("pat_age-----" + pat_age);
                System.out.println("pat_location-----" + pat_location);
                System.out.println("pat_lastlogin-----" + pat_lastlogin);
                System.out.println("pat_addedat-----" + pat_addedat);
                System.out.println("notes_text-----" + notes_text);

                //----------- Flurry -------------------------------------------------
                Map<String, String> articleParams = new HashMap<String, String>();
                articleParams.put("android.doc.pat_name", pat_name);
                articleParams.put("android.doc.pat_email", pat_email);
                articleParams.put("android.doc.pat_mobno", pat_mobno);
                articleParams.put("android.doc.pat_gender", pat_gender);
                articleParams.put("android.doc.pat_location", pat_location);
                articleParams.put("android.doc.pat_lastlogin", pat_lastlogin);
                articleParams.put("android.doc.pat_added_at", pat_addedat);
                FlurryAgent.logEvent("android.doc.Resume_Query_Status", articleParams);
                //----------- Flurry -------------------------------------------------

                gen_text = "";

                //-----------------------------
                if (pat_gender != null && !pat_gender.isEmpty() && !pat_gender.equals("null") && !pat_gender.equals(""))
                    gen_text = pat_gender;
                //-----------------------------
                if (pat_age != null && !pat_age.isEmpty() && !pat_age.equals("null") && !pat_age.equals(""))
                    gen_text = gen_text + ", " + pat_age;
                //----------------------------
                tv_gender.setText(gen_text);

                //-----------------------------
                if (pat_name != null && !pat_name.isEmpty() && !pat_name.equals("null") && !pat_name.equals(""))
                    tv_pname.setText(pat_name);
                else tv_pname.setVisibility(View.GONE);

                if (pat_location != null && !pat_location.isEmpty() && !pat_location.equals("null") && !pat_location.equals(""))
                    tv_location.setText(pat_location);
                else tv_location.setVisibility(View.GONE);

                if (pat_email != null && !pat_email.isEmpty() && !pat_email.equals("null") && !pat_email.equals(""))
                    tv_email.setText(pat_email);
                else tv_email.setVisibility(View.GONE);

                if (pat_mobno != null && !pat_mobno.isEmpty() && !pat_mobno.equals("null") && !pat_mobno.equals(""))
                    tv_mobno.setText(pat_mobno);
                else tv_mobno.setVisibility(View.GONE);

                if (pat_addedat != null && !pat_addedat.isEmpty() && !pat_addedat.equals("null") && !pat_addedat.equals(""))
                    tv_lastadded.setText(pat_addedat);
                else tv_lastadded.setVisibility(View.GONE);

                if (pat_lastlogin != null && !pat_lastlogin.isEmpty() && !pat_lastlogin.equals("null") && !pat_lastlogin.equals(""))
                    tv_lastlogin.setText(pat_lastlogin);
                else tv_lastlogin.setVisibility(View.GONE);


                JSONArray jaaray = new JSONArray(notes_text);

                notes_list_layout.removeAllViews();

                if (notes_text.length() > 2) {

                    tv_viewall.setVisibility(View.VISIBLE);
                    tv_nonotes.setVisibility(View.GONE);

                    for (int i = 0; i < jaaray.length(); i++) {
                        JSONObject jsonobj1 = jaaray.getJSONObject(i);
                        String notes_date = jsonobj1.getString("dt");
                        String notes_text = jsonobj1.getString("note");

                        LayoutInflater layoutInflater = (LayoutInflater) getBaseContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                        final View addView = layoutInflater.inflate(R.layout.notes_row, null);

                        TextView tv_notes_date = (TextView) addView.findViewById(R.id.tv_notes_date);
                        TextView tv_notes = (TextView) addView.findViewById(R.id.tv_notes);

                        //------------------Font-------------------------------
                        Typeface khandBold = Typeface.createFromAsset(getAssets(), Model.font_name_bold);
                        Typeface khand = Typeface.createFromAsset(getAssets(), Model.font_name);
                        tv_notes_date.setTypeface(khand);
                        tv_notes.setTypeface(khand);

                        //------------------Font-------------------------------

                        tv_notes_date.setText(notes_date);
                        tv_notes.setText(notes_text);

                        notes_list_layout.addView(addView);
                    }
                } else {
                    tv_viewall.setVisibility(View.GONE);
                    tv_nonotes.setVisibility(View.VISIBLE);

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

        } else if (
                connec.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.DISCONNECTED ||
                        connec.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.DISCONNECTED) {

            return false;
        }
        return false;
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


    public void send_mail() {

        final MaterialDialog alert = new MaterialDialog(Patient_Profile.this);
        View view = LayoutInflater.from(Patient_Profile.this).inflate(R.layout.send_mail, null);
        alert.setView(view);

        alert.setTitle("Send Message");

        final EditText edt_subject = (EditText) view.findViewById(R.id.edt_subject);
        final EditText edt_msg = (EditText) view.findViewById(R.id.edt_msg);

        alert.setTitle("Sending Message to the Patient");
        alert.setCanceledOnTouchOutside(false);
        alert.setPositiveButton("Send", new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                edt_subject_text = edt_subject.getText().toString();
                edt_msg_text = edt_msg.getText().toString();

                if (edt_subject_text != null && !edt_subject_text.isEmpty() && !edt_subject_text.equals("null") && !edt_subject_text.equals("")) {
                    if (edt_msg_text != null && !edt_msg_text.isEmpty() && !edt_msg_text.equals("null") && !edt_msg_text.equals("")) {

                        try {
                            json_send_mail = new JSONObject();
                            json_send_mail.put("user_id", (Model.id));
                            json_send_mail.put("patient_id", pat_id);
                            json_send_mail.put("subject", edt_subject_text);
                            json_send_mail.put("message", edt_msg_text);
                            json_send_mail.put("appointment_id", "0");

                            System.out.println("json_send_mail---" + json_send_mail.toString());

                            new JSON_Send_mail().execute(json_send_mail);

                        } catch (Exception e2) {
                            e2.printStackTrace();
                        }


                    } else edt_msg.setError("Please enter your message");
                } else edt_msg.setError("Please enter the subject");


                alert.dismiss();
            }
        });

        alert.setNegativeButton("CANCEL", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alert.dismiss();
            }
        });
        alert.show();
    }


    private class JSON_Send_mail extends AsyncTask<JSONObject, Void, Boolean> {

        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            dialog = new ProgressDialog(Patient_Profile.this);
            dialog.setMessage("Sending Mail..., please wait..");
            //dialog.setTitle("");
            dialog.show();
            dialog.setCancelable(false);
        }

        @Override
        protected Boolean doInBackground(JSONObject... urls) {
            try {
                JSONParser jParser = new JSONParser();
                json_resp_send_mail = jParser.JSON_POST(urls[0], "sendEmail");

                System.out.println("Send Mail Params---------------" + urls[0]);
                System.out.println("json_resp_send_mail---------------" + json_resp_send_mail);

                return true;
            } catch (Exception e) {
                e.printStackTrace();
            }

            dialog.dismiss();

            return false;
        }

        protected void onPostExecute(Boolean result) {

            try {

                String status_txt = json_resp_send_mail.getString("status");

                if (status_txt.equals("1")) {
                    post_Success();
                } else {
                    Toast.makeText(getApplicationContext(), "Sending Failed", Toast.LENGTH_SHORT).show();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void post_Success() {


        //---------------- Dialog------------------------------------------------------------------
        final MaterialDialog alert = new MaterialDialog(Patient_Profile.this);
        alert.setTitle("Message Sent successfully...!");
        alert.setMessage("Your Message has been sent to the patient.");
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

    @Override
    public void onResume() {
        super.onResume();

        if ((Model.query_launch).equals("added_notes")) {
            Model.query_launch = "";
            try {
                //----------------------------------------------------------------------------------
                String patientProfile_url = Model.BASE_URL + "docapp/patientProfile?user_id=" + (Model.id) + "&patient_id=" + pat_id + "&token=" + Model.token;
                System.out.println("patientProfile_url-----" + patientProfile_url);
                new JSON_patient_info().execute(patientProfile_url);
                //----------------------------------------------------------------------------------
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

}
