package com.orane.docassist;

import android.app.ProgressDialog;
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
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.orane.docassist.Model.Model;
import com.orane.docassist.Network.JSONParser;
import com.orane.docassist.R;

import org.json.JSONArray;
import org.json.JSONObject;


public class Credentials_Activity extends AppCompatActivity {


    public String url, str_response;
    SharedPreferences sharedpreferences;
    public static final String MyPREFERENCES = "MyPrefs";
    public static final String Login_Status = "Login_Status_key";
    public static final String user_name = "user_name_key";
    public static final String password = "password_key";
    public static final String Name = "Name_key";
    public static final String bcountry = "bcountry_key";
    public static final String photo_url = "photo_url";
    public static final String token = "token_key";
    public static final String id = "id";

    public String query_id, sta1, sta2, sta3, id_val, status_val, question_text, user_id_val, uname, country_code_no, country, pass, Log_Status;
    JSONObject jsonobj, jsonobj_items, qjsonobj_items;
    String type_val, user_id;
    TextView tv_followup_code, tv_status;
    RelativeLayout myclinic_layout;
    LinearLayout top_layout;
    View vi;

    TextView tv_Title, tv_qid, tv_subtitle, tv_usergeo, tv_query_tit, tv_query;
    LinearLayout cred_queries;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.credential);

        //------------ Object Creations -------------------------------
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            //getSupportActionBar().setIcon(getResources().getDrawable(R.mipmap.close_icon_white));
            getSupportActionBar().setTitle("");

            TextView mTitle = (TextView) toolbar.findViewById(R.id.toolbar_title);
            Typeface khandBold = Typeface.createFromAsset(getApplicationContext().getAssets(), Model.font_name_bold);
            mTitle.setTypeface(khandBold);
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.app_color));
        }
        //-----------------------------------------------------------------------
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

/*        //================ Shared Pref ======================
        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        Model.token = sharedpreferences.getString(token, "");
        user_id_val = sharedpreferences.getString(id, "");
        Model.id = sharedpreferences.getString(id, "");

        System.out.println("Model.token----------------------" + Model.token);
        System.out.println("Model.id----------------------" + Model.id);
        //================ Shared Pref ======================*/

        Model.home_ad_flag = "true";
        Model.query_launch = "";

        tv_Title = (TextView) findViewById(R.id.tv_Title);
        tv_subtitle = (TextView) findViewById(R.id.tv_subtitle);
        top_layout = (LinearLayout) findViewById(R.id.top_layout);
        cred_queries = (LinearLayout) findViewById(R.id.cred_queries);

        try {

            Intent intent = getIntent();
            type_val = intent.getStringExtra("type");
            user_id_val = intent.getStringExtra("user_id");

            System.out.println("Cr user_id_val-------------" + user_id_val);

            //---------------------------------------------------------
            String qurl = Model.BASE_URL + "/sapp/credInbox?os_type=android&doc_id=" + user_id_val;
            System.out.println("qurl-------------" + qurl);
            new JSON_getQuery().execute(qurl);
            //---------------------------------------------------------

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private class JSON_getQuery extends AsyncTask<String, Void, Boolean> {

        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            dialog = new ProgressDialog(Credentials_Activity.this);
            dialog.setMessage("Please wait");
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

                cred_queries.removeAllViews();

                jsonobj = new JSONObject(str_response);

                String q_text = jsonobj.getString("q");
                String status = jsonobj.getString("status");

                System.out.println("q_text----------" + q_text);
                System.out.println("status----------" + status);

                JSONArray qjarray = new JSONArray(q_text);

                for (int q = 0; q < qjarray.length(); q++) {

                    qjsonobj_items = qjarray.getJSONObject(q);
                    System.out.println("qjsonobj_items------" + qjsonobj_items.toString());

                    id_val = qjsonobj_items.getString("id");
                    status_val = qjsonobj_items.getString("status");
                    question_text = qjsonobj_items.getString("Question");

                    //----------------------------------------
                    if (q == 0) {
                        sta1 = status_val;
                    }
                    if (q == 1) {
                        sta2 = status_val;
                    }
                    if (q == 2) {
                        sta3 = status_val;
                    }
                    //----------------------------------------

                    System.out.println("Sta1-----------------" + sta1);
                    System.out.println("Sta2-----------------" + sta2);
                    System.out.println("Sta3-----------------" + sta3);

                    question_text = question_text.replaceAll("\\[", "").replaceAll("\\]", "");

                    System.out.println("id_val----------" + id_val);
                    System.out.println("status_val----------" + status_val);
                    System.out.println("question_text----------" + question_text);

                    //----------------- Quetions -------------------------------
                    JSONObject questions = new JSONObject(question_text);

                    String query_id = questions.getString("id");
                    String question = questions.getString("question");
                    String followup_code = questions.getString("followup_code");
                    String user_text = questions.getString("User");

                    System.out.println("query_id------" + query_id);
                    System.out.println("question------" + question);
                    System.out.println("followup_code------" + followup_code);
                    System.out.println("user_text------" + user_text);
                    //----------------- Quetions -------------------------------

                    //----------------- User -------------------------------
                    user_text = user_text.replaceAll("\\[", "").replaceAll("\\]", "");

                    JSONObject user_obj = new JSONObject(user_text);

                    String user_id = user_obj.getString("id");
                    String display_name = user_obj.getString("display_name");
                    String browser_country = user_obj.getString("browser_country");
                    String UserGeo = user_obj.getString("UserGeo");

                    UserGeo = UserGeo.replaceAll("\\[", "").replaceAll("\\]", "");

                    JSONObject user_geo = new JSONObject(UserGeo);
                    String region = user_geo.getString("region");
                    String city = user_geo.getString("city");
                    //----------------- User -------------------------------

                    //----------------- Setting Values -------------------------------
                    vi = getLayoutInflater().inflate(R.layout.credentials_query, null);

                    tv_query_tit = (TextView) vi.findViewById(R.id.tv_query_tit);
                    tv_status = (TextView) vi.findViewById(R.id.tv_status);
                    tv_query = (TextView) vi.findViewById(R.id.tv_query);
                    tv_usergeo = (TextView) vi.findViewById(R.id.tv_usergeo);
                    tv_qid = (TextView) vi.findViewById(R.id.tv_qid);
                    tv_followup_code = (TextView) vi.findViewById(R.id.tv_followup_code);
                    myclinic_layout = (RelativeLayout) vi.findViewById(R.id.myclinic_layout);

                    tv_qid.setText(query_id);
                    tv_query_tit.setText("Query " + (q + 1));
                    tv_query.setText(question);
                    tv_followup_code.setText(followup_code);


                    if (status_val.equals("new")) {
                        tv_status.setTextColor(getResources().getColor(R.color.green_500));
                        tv_status.setText("New Query");
                    } else if (status_val.equals("answered")) {
                        tv_status.setTextColor(getResources().getColor(R.color.red_500));
                        tv_status.setText("Answered");
                    } else {
                        tv_status.setTextColor(getResources().getColor(R.color.blue_700));
                        //tv_status.setTextSize(13);
                        tv_status.setText(status_val);
                    }

                    final String pat_loc = display_name + "," + region + " " + city;
                    tv_usergeo.setText(pat_loc);

                    myclinic_layout.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            TextView tv_followup_code = (TextView) v.findViewById(R.id.tv_followup_code);
                            String tv_followup_code_val = tv_followup_code.getText().toString();
                            System.out.println("tv_followup_code_val-------------------------" + tv_followup_code_val);

/*
                            Intent intent = new Intent(Credentials_Activity.this, Credentials_query_view.class);
                            intent.putExtra("followupcode", tv_followup_code_val);
                            intent.putExtra("user_id", user_id_val);
                            startActivity(intent);
*/

                            Intent intent = new Intent(Credentials_Activity.this, Credentials_query_view.class);
                            intent.putExtra("followupcode", tv_followup_code_val);
                            intent.putExtra("pat_location", "pat_location");
                            intent.putExtra("str_price", "str_price");
                            intent.putExtra("qtype", "new_query");
                            startActivity(intent);

                        }
                    });

                    cred_queries.addView(vi);
                    //----------------- Setting Values -------------------------------
                }

                if (sta1.equals("answered") && sta2.equals("answered") && sta3.equals("answered")) {
                    top_layout.setVisibility(View.VISIBLE);
                } else {
                    top_layout.setVisibility(View.GONE);
                }


                dialog.cancel();

            } catch (Exception e) {
                e.printStackTrace();
            }
            //dialog.cancel();

        }
    }


    @Override
    public void onResume() {
        super.onResume();

        if ((Model.query_launch).equals("cred_answered")) {

            Model.query_launch = "";

            try {
                //---------------------------------------------------------
                String qurl = Model.BASE_URL + "/sapp/credInbox?os_type=android&doc_id=" + user_id_val;
                System.out.println("qurl-------------" + qurl);
                new JSON_getQuery().execute(qurl);
                //---------------------------------------------------------
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        //getMenuInflater().inflate(R.menu.prepack_menu, menu);
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
