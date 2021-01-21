package com.orane.docassist;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.google.firebase.analytics.FirebaseAnalytics;

import com.orane.docassist.Model.Model;
import com.orane.docassist.Network.JSONParser;

import org.json.JSONObject;

public class Signup3 extends AppCompatActivity {


    RelativeLayout pat_layout, pat_week__layout, year_layout;
    LinearLayout awards_layout, bio_layout, links_layout;
    CardView card_practise;
    Button btn_submit;
    String user_id;
    TextView tv_sub_chat;
    JSONObject login_json, login_jsonobj;
    EditText edt_lin, edt_tw, edt_fb, edt_personalbio, edt_week, edt_exp, edt_pat, edt_awards;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup3);

        //------------ Object Creations -------------------------------
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setTitle("Proffessional details");
        }
        //------------ Object Creations -------------------------------

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.app_color));
        }

        try {
            Intent intent = getIntent();
            user_id = intent.getStringExtra("user_id");

            if ((Model.query_launch).equals("login")) {
                user_id = Model.id;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        tv_sub_chat = (TextView) findViewById(R.id.tv_sub_chat);

        pat_week__layout = (RelativeLayout) findViewById(R.id.pat_week__layout);
        awards_layout = (LinearLayout) findViewById(R.id.awards_layout);

        bio_layout = (LinearLayout) findViewById(R.id.bio_layout);
        year_layout = (RelativeLayout) findViewById(R.id.year_layout);
        pat_layout = (RelativeLayout) findViewById(R.id.pat_layout);
        pat_week__layout = (RelativeLayout) findViewById(R.id.pat_week__layout);
        links_layout = (LinearLayout) findViewById(R.id.links_layout);


        card_practise = (CardView) findViewById(R.id.card_practise);
        btn_submit = (Button) findViewById(R.id.btn_submit);

        edt_personalbio = (EditText) findViewById(R.id.edt_personalbio);
        edt_pat = (EditText) findViewById(R.id.edt_pat);
        edt_exp = (EditText) findViewById(R.id.edt_exp);
        edt_week = (EditText) findViewById(R.id.edt_week);
        edt_awards = (EditText) findViewById(R.id.edt_awards);
        edt_lin = (EditText) findViewById(R.id.edt_lin);
        edt_fb = (EditText) findViewById(R.id.edt_fb);
        edt_tw = (EditText) findViewById(R.id.edt_tw);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

/*        Animation animSlideDown1 = AnimationUtils.loadAnimation(Signup3.this, R.anim.bounce);
        animSlideDown1.setStartOffset(100);
        bio_layout.startAnimation(animSlideDown1);

        Animation animSlideDown2 = AnimationUtils.loadAnimation(Signup3.this, R.anim.bounce2);
        animSlideDown2.setStartOffset(300);
        year_layout.startAnimation(animSlideDown2);

        Animation animSlideDown3 = AnimationUtils.loadAnimation(Signup3.this, R.anim.bounce3);
        animSlideDown3.setStartOffset(500);
        pat_layout.startAnimation(animSlideDown3);


        Animation animSlideDown4 = AnimationUtils.loadAnimation(Signup3.this, R.anim.bounce4);
        animSlideDown4.setStartOffset(700);
        card_practise.startAnimation(animSlideDown4);*/


        card_practise.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Model.launch = "Signup";
                Intent intent = new Intent(Signup3.this, MyClinicAddActivity.class);
                intent.putExtra("clinic_id", "0");
                intent.putExtra("clinic_name", "");
                intent.putExtra("clinic_geo", "");
                intent.putExtra("mode", "new");
                intent.putExtra("type", "primary");
                startActivity(intent);
            }
        });

        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String pers_text = edt_personalbio.getText().toString();
                String exp_years = edt_exp.getText().toString();
                String edt_pat_val = edt_pat.getText().toString();
                String edt_week_val = edt_week.getText().toString();
                String edt_awards_val = edt_awards.getText().toString();

                if (!pers_text.equals("")) {
                    if (!exp_years.equals("")) {
                        if (!edt_pat_val.equals("")) {
                            if (!edt_week_val.equals("")) {

                                Signup_submit();

                            } else {
                                edt_week.setError("How many patients do you consult in a week?");
                            }
                        } else {
                            edt_pat.setError("How many patients have you consulted so far?");
                        }
                    } else {
                        edt_exp.setError("How many years of experience do you have?");
                    }
                } else {
                    edt_personalbio.setError("Please enter your professional bio");
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

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onResume() {
        super.onResume();

        if ((Model.query_launch) != null && !(Model.query_launch).isEmpty() && !(Model.query_launch).equals("null") && !(Model.query_launch).equals("")) {
            if ((Model.query_launch).equals("cliniqadd")) {
                tv_sub_chat.setText("Location added succesfully");
                tv_sub_chat.setTextColor(getResources().getColor(R.color.green_800));
            }
        }
    }

    public void Signup_submit() {

        try {

            String pers_text = edt_personalbio.getText().toString();
            String exp_years = edt_exp.getText().toString();
            String edt_pat_val = edt_pat.getText().toString();
            String edt_week_val = edt_week.getText().toString();
            String edt_awards_val = edt_awards.getText().toString();
            String edt_lin_val = edt_lin.getText().toString();
            String edt_fb_val = edt_fb.getText().toString();
            String edt_tw_val = edt_tw.getText().toString();

            login_json = new JSONObject();
            login_json.put("about", pers_text);
            login_json.put("exp", exp_years);
            login_json.put("pat_total", edt_pat_val);
            login_json.put("pat_avg", edt_week_val);
            login_json.put("awards", edt_awards_val);
            login_json.put("linkedIn", edt_lin_val);
            login_json.put("fbLink", edt_fb_val);
            login_json.put("tweetLink", edt_tw_val);

            System.out.println("Signup_json----" + login_json.toString());

            new JSON_signup().execute(login_json);

            //------------ Google firebase Analitics--------------------
            Model.mFirebaseAnalytics = FirebaseAnalytics.getInstance(getApplicationContext());
            Bundle params = new Bundle();
            params.putString("Details", login_json.toString());
            Model.mFirebaseAnalytics.logEvent("Doctor_Signup_stage3", params);
            //------------ Google firebase Analitics--------------------


        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private class JSON_signup extends AsyncTask<JSONObject, Void, Boolean> {

        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            dialog = new ProgressDialog(Signup3.this);
            dialog.setMessage("Submitting.. please wait");
            dialog.show();
            dialog.setCancelable(false);
        }

        @Override
        protected Boolean doInBackground(JSONObject... urls) {

            try {
                JSONParser jParser = new JSONParser();
                login_jsonobj = jParser.JSON_POST(urls[0], "newsignupDocStep3");

                System.out.println("urls[0]----------" + urls[0]);
                System.out.println("Server_Response_JSON----------" + login_jsonobj.toString());

                return true;
            } catch (Exception e) {
                e.printStackTrace();
            }
            return false;
        }

        protected void onPostExecute(Boolean result) {

            try {

                String status_val = login_jsonobj.getString("status");
                System.out.println("status_val-------------- " + status_val);

                if (status_val.equals("1")) {
                    Intent intent = new Intent(Signup3.this, ThankyouActivity.class);
                    intent.putExtra("type", "login");
                    startActivity(intent);
                    //overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);
                } else {

                    if (login_jsonobj.has("err")) {
                        String err_val = login_jsonobj.getString("err");
                        Toast.makeText(getApplicationContext(), err_val, Toast.LENGTH_SHORT).show();
                    }

                }

            } catch (Exception e) {
                e.printStackTrace();
            }

            dialog.cancel();

        }
    }
}
