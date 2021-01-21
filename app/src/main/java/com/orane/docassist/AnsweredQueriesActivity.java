package com.orane.docassist;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.flurry.android.FlurryAgent;
import com.orane.docassist.Model.Item;
import com.orane.docassist.Model.Model;
import com.orane.docassist.Network.JSONParser;
import com.orane.docassist.adapter.QueryAnsweredRowAdapter;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import me.drakeet.materialdialog.MaterialDialog;

public class AnsweredQueriesActivity extends AppCompatActivity {

    Toolbar toolbar;
    JSONObject object;
    Intent intent;

    ProgressBar progressBar, progressBar_Bottom;
    QueryAnsweredRowAdapter objAdapter;
    LinearLayout nolayout, netcheck_layout;
    SwipeRefreshLayout mSwipeRefreshLayout;
    Item objItem;
    List<Item> listArray;
    List<Item> arrayOfList;
    ListView listView;
    public String str_response, params;
    Button btn_reload;
    public String spec_val = "0";
    long startTime;

    SharedPreferences sharedpreferences;
    public static final String MyPREFERENCES = "MyPrefs";
    public static final String Login_Status = "Login_Status_key";
    public static final String user_name = "user_name_key";
    public static final String Name = "Name_key";
    public static final String password = "password_key";
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
    public static final String sp_km_id = "sp_km_id_key";
    public static final String sp_share_link = "sp_share_link";

    Double floor_val;
    Integer int_floor;

    public String source_txt, pat_id;
    public boolean pagination = true;
    TextView mTitle;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.query_answered);

        //------------------------------------------------------
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setTitle("");

            mTitle = toolbar.findViewById(R.id.toolbar_title);
            Typeface khandBold = Typeface.createFromAsset(getApplicationContext().getAssets(), Model.font_name_bold);
            mTitle.setTypeface(khandBold);
        }
        //------------------------------------------------------
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.app_color));
        }

        //================ Shared Pref ======================
        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        //============================================================

        progressBar_Bottom = findViewById(R.id.progressBar_Bottom);
        progressBar = findViewById(R.id.progressBar);
        netcheck_layout = findViewById(R.id.netcheck_layout);
        btn_reload = findViewById(R.id.btn_reload);
        nolayout = findViewById(R.id.nolayout);
        mSwipeRefreshLayout = findViewById(R.id.swipe_query_new);
        listView = findViewById(R.id.listview);
        //----------------------------------------------------------------------------------

        try {
            Intent intent = getIntent();
            source_txt = intent.getStringExtra("source");
            pat_id = intent.getStringExtra("pat_id");

            //-----------------------------------------------
            if (source_txt != null && !source_txt.isEmpty() && !source_txt.equals("null") && !source_txt.equals("")) {

            } else {
                source_txt = "Main";
            }
            //-----------------------------
            //-----------------------------
            if (pat_id != null && !pat_id.isEmpty() && !pat_id.equals("null") && !pat_id.equals("")) {

            } else {
                pat_id = "0";
            }
            //-----------------------------------------------------------------------------------

            System.out.println("get intent source_txt-----" + source_txt);
            System.out.println("get intent pat_id-----" + pat_id);

            if (source_txt.equals("Main")) {
                mTitle.setText("Answered Queries");
            } else {
                mTitle.setText("Queries History");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        //----------------------------------------------------------------------------------

        if ((Model.id) != null && !(Model.id).isEmpty() && !(Model.id).equals("null") && !(Model.id).equals("")) {
            full_process();
        } else {
            force_logout();
        }

        btn_reload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    if ((Model.id) != null && !(Model.id).isEmpty() && !(Model.id).equals("null") && !(Model.id).equals("")) {
                        full_process();
                    } else {
                        force_logout();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                try {
                    TextView query = view.findViewById(R.id.tvquery);
                    TextView speciality = view.findViewById(R.id.tvspeciality);
                    TextView followcode = view.findViewById(R.id.tvfollowupcode);
                    TextView patient = view.findViewById(R.id.tvaskedname);
                    TextView from = view.findViewById(R.id.tvgeo);
                    TextView askeddate = view.findViewById(R.id.tvdate);

                    Model.query = query.getText().toString();
                    Model.from = from.getText().toString();
                    Model.followcode = followcode.getText().toString();
                    Model.patient = patient.getText().toString();
                    Model.askeddate = askeddate.getText().toString();
/*
                    Intent intent = new Intent(AnsweredQueriesActivity.this, QueryAnsweredDetailActivity.class);
                    intent.putExtra("followupcode", Model.followcode);
                    startActivity(intent);
                    */

/*                  Intent intent = new Intent(AnsweredQueriesActivity.this, AnsweredQueryViewActivity.class);
                    intent.putExtra("followupcode", (Model.followcode));
                    intent.putExtra("query_price", "0");

                    intent.putExtra("pat_from", (Model.from));
                    intent.putExtra("qtype", "answered_query");
                    startActivity(intent);
*/
                    Intent intent = new Intent(AnsweredQueriesActivity.this, NewQueryViewActivity.class);
                    intent.putExtra("followupcode", Model.followcode);
                    intent.putExtra("pat_location", Model.patient);
                    intent.putExtra("str_price", "");
                    intent.putExtra("qtype", "new_query");
                    intent.putExtra("finisher", new android.os.ResultReceiver(null) {
                        @Override
                        protected void onReceiveResult(int resultCode, Bundle resultData) {
                            AnsweredQueriesActivity.this.finish();
                        }
                    });
                    startActivityForResult(intent, 1);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });


        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

                int threshold = 1;
                int count = listView.getCount();
                System.out.println("count----- " + count);

                if (scrollState == SCROLL_STATE_IDLE) {
                    if (listView.getLastVisiblePosition() >= count - threshold) {

                        double cur_page = (listView.getAdapter().getCount()) / 10;
                        System.out.println("cur_page 1----" + cur_page);

                        if (count < 10) {
                            System.out.println("No more to Load");
                            //Toast.makeText(getApplicationContext(), "No more to queries load", Toast.LENGTH_LONG).show();
                            int_floor = 0;

                        } else if (count == 10) {
                            floor_val = cur_page + 1;

                            System.out.println("Final Val----" + floor_val);
                            int_floor = floor_val.intValue();

                        } else {
                            floor_val = Math.floor(cur_page);
                            Double diff = cur_page - floor_val;

                            System.out.println("cur_page 2----" + cur_page);
                            System.out.println("floor_val 2----" + floor_val);
                            System.out.println("diff 2----" + diff);

                            if (diff == 0) {
                                floor_val = floor_val + 1;
                            } else if (diff > 0) {
                                floor_val = floor_val + 2;
                            }

                            System.out.println("Final Val----" + floor_val);
                            int_floor = floor_val.intValue();
                        }

                        if (int_floor != 0 && (pagination)) {
                            //----------------------------------------------
                            String url = Model.BASE_URL + "sapp/qAnsweredDoc?os_type=android&user_id=" + (Model.id) + "&browser_country=" + (Model.browser_country) + "&page=" + int_floor + "&patient_id=" + pat_id + "&token=" + Model.token;
                            System.out.println("url----" + url);
                            new MyTask_Pagination().execute(url);
                            //----------------------------------------------
                        }
                    }
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

            }
        });


        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                try {
                    if (isInternetOn()) {
                        pagination = true;

                        //----------------------------------------------
                        String url = Model.BASE_URL + "sapp/qAnsweredDoc?os_type=android&user_id=" + (Model.id) + "&browser_country=" + (Model.browser_country) + "&page=1&patient_id=" + pat_id + "&token=" + Model.token;
                        System.out.println("url----" + url);
                        new MyTask_refresh().execute(url);
                        //----------------------------------------------

                        mSwipeRefreshLayout.setRefreshing(false);
                    } else {
                        mSwipeRefreshLayout.setVisibility(View.GONE);
                        nolayout.setVisibility(View.GONE);
                        netcheck_layout.setVisibility(View.VISIBLE);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void setAdapterToListview() {

        try {
            objAdapter = new QueryAnsweredRowAdapter(AnsweredQueriesActivity.this, R.layout.query_answered_row, arrayOfList);
            listView.setAdapter(objAdapter);
            objAdapter.notifyDataSetChanged();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void add_page_AdapterToListview() {

        try {
            objAdapter.addAll(arrayOfList);
            listView.setSelection(objAdapter.getCount() - (arrayOfList.size()));
            objAdapter.notifyDataSetChanged();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    class MyTask_server extends AsyncTask<String, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            nolayout.setVisibility(View.GONE);
            mSwipeRefreshLayout.setVisibility(View.GONE);
            netcheck_layout.setVisibility(View.GONE);
            progressBar.setVisibility(View.VISIBLE);
            progressBar_Bottom.setVisibility(View.GONE);

            startTime = System.currentTimeMillis();

        }

        @Override
        protected Void doInBackground(String... params) {
            try {
                str_response = new JSONParser().getJSONString(params[0]);
                System.out.println("str_response--------------" + str_response);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);

            try {

                if (str_response != null && !str_response.isEmpty() && !str_response.equals("null") && !str_response.equals("")) {

                    Object json = new JSONTokener(str_response).nextValue();
                    if (json instanceof JSONObject) {
                        System.out.println("This is JSON OBJECT---------------" + str_response);

                        JSONObject jobject = new JSONObject(str_response);

                        if (jobject.has("token_status")) {
                            String token_status = jobject.getString("token_status");
                            if (token_status.equals("0")) {

                                //============================================================
                                SharedPreferences.Editor editor = sharedpreferences.edit();
                                editor.putString(Login_Status, "0");
                                editor.apply();
                                //============================================================

                                finishAffinity();
                                Intent intent = new Intent(AnsweredQueriesActivity.this, LoginActivity.class);
                                startActivity(intent);
                                finish();
                            }
                        }

                    } else if (json instanceof JSONArray) {
                        System.out.println("This is JSON ARRAY---------------" + str_response);

                        JSONArray jsonarr = new JSONArray(str_response);
                        listArray = new ArrayList<Item>();

                        for (int i = 0; i < jsonarr.length(); i++) {
                            JSONObject jsonobj1 = jsonarr.getJSONObject(i);
                            objItem = new Item();
                            objItem.setId("1");
                            objItem.setTitle(jsonobj1.getString("query"));

                            if (jsonobj1.getString("speciality").equals("null"))
                                objItem.setSpeciality("Speciality: N/A");
                            else
                                objItem.setSpeciality(jsonobj1.getString("speciality"));

                            objItem.setPrice("");
                            objItem.setFupcode(jsonobj1.getString("followcode"));
                            objItem.seAskedName(jsonobj1.getString("askedby_name"));
                            objItem.setGeo(jsonobj1.getString("str_user_geo"));

                            //------------------------------------------------------
                            if (jsonobj1.getString("asked_at") != null)
                                objItem.setPubdate(jsonobj1.getString("asked_at"));
                            else
                                objItem.setPubdate("");
                            //------------------------------------------------------

                            objItem.setLink(jsonobj1.getString("askedby_name"));

                            listArray.add(objItem);
                        }

                        arrayOfList = listArray;

                        if (null == arrayOfList || arrayOfList.size() == 0) {

                            nolayout.setVisibility(View.VISIBLE);
                            mSwipeRefreshLayout.setVisibility(View.GONE);
                            netcheck_layout.setVisibility(View.GONE);
                            progressBar.setVisibility(View.GONE);
                            progressBar_Bottom.setVisibility(View.GONE);

                        } else {
                            if (arrayOfList.size() < 10) {
                                pagination = false;
                            }

                            nolayout.setVisibility(View.GONE);
                            mSwipeRefreshLayout.setVisibility(View.VISIBLE);
                            netcheck_layout.setVisibility(View.GONE);
                            progressBar.setVisibility(View.GONE);
                            progressBar_Bottom.setVisibility(View.GONE);

                            setAdapterToListview();
                        }

                        //----------------------------------------------------------
                        long elapsedTime = System.currentTimeMillis() - startTime;
                        System.out.println("Total Elapsed of AsyncTask (in milliseconds)--- " + elapsedTime);
                        System.out.println("arrayOfList.size()---------" + arrayOfList.size());
                        //----------------------------------------------------------
                    }

                } else {

                    try {

                        JSONObject json_err_feedback = new JSONObject();
                        json_err_feedback.put("user_id", (Model.id));

                        String err_dat = Model.id + "/" + Model.name + "/new_queries_open_page";
                        json_err_feedback.put("text", err_dat);
                        System.out.println("json_err_feedback---" + json_err_feedback.toString());

                        new JSON_Feedback().execute(json_err_feedback);

                        //----------- Flurry -------------------------------------------------
                        Map<String, String> articleParams = new HashMap<String, String>();
                        articleParams.put("android.doc.Not_Respond_Issue:", json_err_feedback.toString());
                        FlurryAgent.logEvent("android.doc.Not_Respond_Issue", articleParams);
                        //----------- Flurry -------------------------------------------------

                        server_err();

                    } catch (Exception e2) {
                        e2.printStackTrace();
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    class MyTask_Pagination extends AsyncTask<String, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            nolayout.setVisibility(View.GONE);
            mSwipeRefreshLayout.setVisibility(View.VISIBLE);
            netcheck_layout.setVisibility(View.GONE);
            progressBar.setVisibility(View.GONE);
            progressBar_Bottom.setVisibility(View.VISIBLE);

        }

        @Override
        protected Void doInBackground(String... params) {
            try {
                str_response = new JSONParser().getJSONString(params[0]);
                System.out.println("str_response--------------" + str_response);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);

            try {

                //----------------------------------------------------------
                Object json = new JSONTokener(str_response).nextValue();
                if (json instanceof JSONObject) {
                    System.out.println("This is JSON OBJECT---------------" + str_response);

                    JSONObject jobject = new JSONObject(str_response);

                    if (jobject.has("token_status")) {
                        String token_status = jobject.getString("token_status");
                        if (token_status.equals("0")) {

                            //============================================================
                            SharedPreferences.Editor editor = sharedpreferences.edit();
                            editor.putString(Login_Status, "0");
                            editor.apply();
                            //============================================================

                            finishAffinity();
                            Intent intent = new Intent(AnsweredQueriesActivity.this, LoginActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    }

                } else if (json instanceof JSONArray) {
                    System.out.println("This is JSON ARRAY---------------" + str_response);

                    JSONArray jsonarr = new JSONArray(str_response);
                    listArray = new ArrayList<Item>();

                    for (int i = 0; i < jsonarr.length(); i++) {
                        JSONObject jsonobj1 = jsonarr.getJSONObject(i);
                        objItem = new Item();
                        objItem.setId("1");
                        objItem.setTitle(jsonobj1.getString("query"));

                        //-------------------------
                        if (jsonobj1.getString("speciality").equals("null"))
                            objItem.setSpeciality("Speciality: N/A");
                        else
                            objItem.setSpeciality(jsonobj1.getString("speciality"));
                        //-------------------------

                        objItem.setPrice("");
                        objItem.setFupcode(jsonobj1.getString("followcode"));
                        objItem.seAskedName(jsonobj1.getString("askedby_name"));
                        objItem.setGeo(jsonobj1.getString("str_user_geo"));

                        if (jsonobj1.getString("asked_at") != null)
                            objItem.setPubdate(jsonobj1.getString("asked_at"));
                        else
                            objItem.setPubdate("");

                        objItem.setLink(jsonobj1.getString("askedby_name"));

                        listArray.add(objItem);
                    }

                    arrayOfList = listArray;

                    if (null == arrayOfList || arrayOfList.size() == 0) {

                        nolayout.setVisibility(View.GONE);
                        mSwipeRefreshLayout.setVisibility(View.VISIBLE);
                        netcheck_layout.setVisibility(View.GONE);
                        progressBar.setVisibility(View.GONE);
                        progressBar_Bottom.setVisibility(View.GONE);

                    } else {

                        if (arrayOfList.size() < 10) {
                            pagination = false;
                        }

                        nolayout.setVisibility(View.GONE);
                        mSwipeRefreshLayout.setVisibility(View.VISIBLE);
                        netcheck_layout.setVisibility(View.GONE);
                        progressBar.setVisibility(View.GONE);
                        progressBar_Bottom.setVisibility(View.GONE);

                        add_page_AdapterToListview();
                    }

                    //----------------------------------------------------------
                    long elapsedTime = System.currentTimeMillis() - startTime;
                    System.out.println("Total Elapsed of AsyncTask (in milliseconds)--- " + elapsedTime);
                    System.out.println("arrayOfList.size()---------" + arrayOfList.size());
                    //----------------------------------------------------------
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    class MyTask_refresh extends AsyncTask<String, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            nolayout.setVisibility(View.GONE);
            mSwipeRefreshLayout.setVisibility(View.VISIBLE);
            netcheck_layout.setVisibility(View.GONE);
            progressBar.setVisibility(View.GONE);
            progressBar_Bottom.setVisibility(View.GONE);
        }

        @Override
        protected Void doInBackground(String... params) {
            try {
                str_response = new JSONParser().getJSONString(params[0]);
                System.out.println("str_response--------------" + str_response);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);

            try {

                //----------------------------------------------------------
                Object json = new JSONTokener(str_response).nextValue();
                if (json instanceof JSONObject) {
                    System.out.println("This is JSON OBJECT---------------" + str_response);

                    JSONObject jobject = new JSONObject(str_response);

                    if (jobject.has("token_status")) {
                        String token_status = jobject.getString("token_status");
                        if (token_status.equals("0")) {

                            //============================================================
                            SharedPreferences.Editor editor = sharedpreferences.edit();
                            editor.putString(Login_Status, "0");
                            editor.apply();
                            //============================================================

                            finishAffinity();
                            Intent intent = new Intent(AnsweredQueriesActivity.this, LoginActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    }

                } else if (json instanceof JSONArray) {
                    System.out.println("This is JSON ARRAY---------------" + str_response);

                    JSONArray jsonarr = new JSONArray(str_response);
                    listArray = new ArrayList<Item>();

                    for (int i = 0; i < jsonarr.length(); i++) {

                        JSONObject jsonobj1 = jsonarr.getJSONObject(i);

                        objItem = new Item();
                        objItem.setId("1");
                        objItem.setTitle(jsonobj1.getString("query"));

                        //-------------------------------------------------
                        if (jsonobj1.getString("speciality").equals("null"))
                            objItem.setSpeciality("Speciality: N/A");
                        else
                            objItem.setSpeciality(jsonobj1.getString("speciality"));
                        //-------------------------------------------------

                        objItem.setPrice("");
                        objItem.setFupcode(jsonobj1.getString("followcode"));
                        objItem.seAskedName(jsonobj1.getString("askedby_name"));
                        objItem.setGeo(jsonobj1.getString("str_user_geo"));

                        //-------------------------------------------------
                        if (jsonobj1.getString("asked_at") != null)
                            objItem.setPubdate(jsonobj1.getString("asked_at"));
                        else
                            objItem.setPubdate("");
                        //-------------------------------------------------

                        objItem.setLink(jsonobj1.getString("askedby_name"));

                        listArray.add(objItem);
                    }

                    arrayOfList = listArray;

                    if (null == arrayOfList || arrayOfList.size() == 0) {

                        nolayout.setVisibility(View.VISIBLE);
                        mSwipeRefreshLayout.setVisibility(View.GONE);
                        netcheck_layout.setVisibility(View.GONE);
                        progressBar.setVisibility(View.GONE);
                        progressBar_Bottom.setVisibility(View.GONE);

                    } else {

                        if (arrayOfList.size() < 10) {
                            pagination = false;
                        }

                        nolayout.setVisibility(View.GONE);
                        mSwipeRefreshLayout.setVisibility(View.VISIBLE);
                        netcheck_layout.setVisibility(View.GONE);
                        progressBar.setVisibility(View.GONE);
                        progressBar_Bottom.setVisibility(View.GONE);

                        setAdapterToListview();
                    }

                    //----------------------------------------------------------
                    long elapsedTime = System.currentTimeMillis() - startTime;
                    System.out.println("Total Elapsed of AsyncTask (in milliseconds)--- " + elapsedTime);
                    System.out.println("arrayOfList.size()---------" + arrayOfList.size());
                    //----------------------------------------------------------
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    public void full_process() {

        if (isInternetOn()) {

            try {
                //--------------------------------------------------
                String answered_url = Model.BASE_URL + "sapp/qAnsweredDoc?os_type=android&user_id=" + (Model.id) + "&browser_country=" + (Model.browser_country) + "&page=1&patient_id=" + pat_id + "&token=" + Model.token;
                //String answered_url = Model.BASE_URL + "sapp/qAnsweredDoc?os_type=android&user_id=597789&browser_country=" + (Model.browser_country) + "&page=1&patient_id=" + pat_id + "&token=" + Model.token;
                System.out.println("url----" + answered_url);
                new MyTask_server().execute(answered_url);
                //--------------------------------------------------

                //----------- Flurry -------------------------------------------------
                Map<String, String> articleParams = new HashMap<String, String>();
                articleParams.put("android.doc.patient_ID", pat_id);
                FlurryAgent.logEvent("android.doc.answered_queries_list", articleParams);
                //----------- Flurry -------------------------------------------------

            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            nolayout.setVisibility(View.GONE);
            mSwipeRefreshLayout.setVisibility(View.GONE);
            netcheck_layout.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.GONE);
            progressBar_Bottom.setVisibility(View.GONE);
        }
    }

    public final boolean isInternetOn() {

        ConnectivityManager connec = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);

        if (connec.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.CONNECTED ||
                connec.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.CONNECTING ||
                connec.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.CONNECTING ||
                connec.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.CONNECTED) {

            //Toast.makeText(this, " Connected ", Toast.LENGTH_LONG).show();
            return true;

        } else if (
                connec.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.DISCONNECTED ||
                        connec.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.DISCONNECTED) {

            //Toast.makeText(this, " Not Connected ", Toast.LENGTH_LONG).show();
            return false;
        }

        return false;
    }

    public void force_logout() {

        try {

            //---------------- Dialog------------------------------------------------------------------
            final MaterialDialog alert = new MaterialDialog(AnsweredQueriesActivity.this);
            alert.setTitle("Please Re-Login the App");
            alert.setMessage("Something went wrong. Please Logout and Login again to continue");
            alert.setCanceledOnTouchOutside(false);
            alert.setPositiveButton("OK", new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    //============================================================
                    SharedPreferences.Editor editor = sharedpreferences.edit();
                    editor.putString(Login_Status, "0");
                    editor.apply();
                    //============================================================
                    finishAffinity();
                    Intent i = new Intent(AnsweredQueriesActivity.this, LoginActivity.class);
                    startActivity(i);
                    alert.dismiss();
                    AnsweredQueriesActivity.this.finish();
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


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //getMenuInflater().inflate(R.menu.ask_menu, menu);
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

    private class JSON_Feedback extends AsyncTask<JSONObject, Void, Boolean> {

        @Override
        protected Boolean doInBackground(JSONObject... urls) {

            try {
                JSONObject json_empty_feedback_obj = new JSONParser().JSON_POST(urls[0], "feedback");
                System.out.println("json_empty_feedback_obj-----------" + json_empty_feedback_obj);
                return true;
            } catch (Exception e) {
                e.printStackTrace();
            }

            return false;
        }
    }

    public void server_err() {

        try {

            final MaterialDialog alert = new MaterialDialog(AnsweredQueriesActivity.this);
            //alert.setTitle("Error..!");
            alert.setMessage("Hi, there seems to be an issue with this page. The support team has been notified. Please try reloading this page after sometime");
            alert.setCanceledOnTouchOutside(false);
            alert.setPositiveButton("OK", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    alert.dismiss();
                    finish();
                }
            });
            alert.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
