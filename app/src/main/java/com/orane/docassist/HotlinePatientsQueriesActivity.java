package com.orane.docassist;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.flurry.android.FlurryAgent;

import com.orane.docassist.Model.Item;
import com.orane.docassist.Model.Model;
import com.orane.docassist.Network.JSONParser;
import com.orane.docassist.R;
import com.orane.docassist.adapter.HotlinePatientsQueriesRowAdapter;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HotlinePatientsQueriesActivity extends AppCompatActivity {

    ProgressBar progressBar_bottom, progressBar;
    Toolbar toolbar;
    SwipeRefreshLayout mSwipeRefreshLayout;
    ListView listView;
    LinearLayout nolayout, netcheck_layout;
    RelativeLayout LinearLayout1;
    List<Item> listArray;
    public String str_response, follouwupcode, params, pat_name, pat_id, query_id;
    List<Item> arrayOfList;
    Item objItem;
    public boolean pagination = true;
    HotlinePatientsQueriesRowAdapter objAdapter;

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
    public static final String token = "token_key";


    Double floor_val;
    Integer int_floor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.hotline_patients_queries);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        listView = (ListView) findViewById(R.id.listview);
        LinearLayout1 = (RelativeLayout) findViewById(R.id.LinearLayout1);
        netcheck_layout = (LinearLayout) findViewById(R.id.netcheck_layout);
        nolayout = (LinearLayout) findViewById(R.id.nolayout);
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_query_new);
        progressBar_bottom = (ProgressBar) findViewById(R.id.progressBar_bottom);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        //================ Shared Pref ======================
        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        Model.token = sharedpreferences.getString(token, "");
        System.out.println("Model.token----------------------" + Model.token);
        //================ Shared Pref ======================

        try {
            Intent intent = getIntent();
            pat_id = intent.getStringExtra("pat_id");
            pat_name = intent.getStringExtra("pat_name");

            System.out.println("Get intent pat_id----" + pat_id);
            System.out.println("Get intent pat_name----" + pat_name);

            if (getSupportActionBar() != null) {
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                getSupportActionBar().setDisplayShowHomeEnabled(true);
                getSupportActionBar().setTitle(pat_name);
            }

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                getWindow().setStatusBarColor(getResources().getColor(R.color.app_color));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            //----------- Flurry -------------------------------------------------
            Map<String, String> articleParams = new HashMap<String, String>();
            articleParams.put("android.doc.Patient_Id:", pat_id);
            articleParams.put("android.doc.Patient_Name:", pat_name);
            FlurryAgent.logEvent("android.doc.Hotline_Queries", articleParams);
            //----------- Flurry -------------------------------------------------

        } catch (Exception e) {
            e.printStackTrace();
        }

        full_process();


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                System.out.println("position-----" + position);

                final TextView qidval = (TextView) view.findViewById(R.id.tv_qid);
                final TextView tv_follouwupcode = (TextView) view.findViewById(R.id.tv_follouwupcode);

                query_id = qidval.getText().toString();
                follouwupcode = tv_follouwupcode.getText().toString();

                System.out.println("query_id--------------" + query_id);

                Intent i = new Intent(HotlinePatientsQueriesActivity.this, HotlineChatViewActivity.class);
                i.putExtra("pat_id", pat_id);
                i.putExtra("pat_name", pat_name);
                i.putExtra("selqid", query_id);
                i.putExtra("follouwupcode", follouwupcode);
                startActivity(i);
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
                            //Toast.makeText(getApplicationContext(),"No more to queries load",Toast.LENGTH_LONG).show();
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
                            params = Model.BASE_URL + "sapp/qinboxDoc?os_type=android&user_id=" + (Model.id) + "&browser_country=" + (Model.browser_country) + "&page=" + int_floor + "&patient_id=" + pat_id + "&hline=1&token=" + Model.token;
                            System.out.println("params-------------" + params);
                            new MyTask_Pagination().execute(params);
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

                if (isInternetOn()) {
                    pagination = true;
                    params = Model.BASE_URL + "sapp/qinboxDoc?os_type=android&user_id=" + (Model.id) + "&browser_country=" + (Model.browser_country) + "&page=1&patient_id=" + pat_id + "&hline=1&token=" + Model.token;
                    System.out.println("params-------------" + params);
                    new MyTask().execute(params);
                    mSwipeRefreshLayout.setRefreshing(false);
                } else {
                    mSwipeRefreshLayout.setVisibility(View.GONE);
                    nolayout.setVisibility(View.GONE);
                    netcheck_layout.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    public void full_process() {

        if (isInternetOn()) {
            try {
                //------------------------------------------
                params = Model.BASE_URL + "sapp/qinboxDoc?os_type=android&user_id=" + (Model.id) + "&browser_country=" + (Model.browser_country) + "&page=1&patient_id=" + pat_id + "&hline=1&token=" + Model.token + "&enc=1";
                System.out.println("params-------------" + params);
                new MyTask().execute(params);
                //------------------------------------------
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            mSwipeRefreshLayout.setVisibility(View.GONE);
            nolayout.setVisibility(View.GONE);
            netcheck_layout.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.GONE);
            progressBar_bottom.setVisibility(View.GONE);
        }
    }

    private class MyTask extends AsyncTask<String, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            mSwipeRefreshLayout.setVisibility(View.GONE);
            nolayout.setVisibility(View.GONE);
            netcheck_layout.setVisibility(View.GONE);
            progressBar.setVisibility(View.VISIBLE);
            progressBar_bottom.setVisibility(View.GONE);

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
                            Intent intent = new Intent(HotlinePatientsQueriesActivity.this, LoginActivity.class);
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

                        System.out.println("Patients List jsonobj1-----------" + jsonobj1.toString());

                        objItem = new Item();
                        objItem.setHlqid(jsonobj1.getString("qid"));
                        objItem.setHlaskedby_name(jsonobj1.getString("askedby_name"));
                        objItem.setHlasked_at(jsonobj1.getString("asked_at"));
                        //objItem.setHl(jsonobj1.getString("doctor_id"));
                        objItem.setHlquery(jsonobj1.getString("query"));
                        objItem.setHlstr_qstatus(jsonobj1.getString("str_qstatus"));

                        //objItem.setHlsp(jsonobj1.getString("speciality"));
                        //objItem.setHlplan_months(jsonobj1.getString("str_price"));
                        //objItem.setHlplan_start_date(jsonobj1.getString("price"));
                        objItem.setHlfollowcode(jsonobj1.getString("followcode"));
                        objItem.setHluser_geo(jsonobj1.getString("str_user_geo"));
                        objItem.setHltype_lable(jsonobj1.getString("q_type_label"));
                        //objItem.setHlnext_renewal(jsonobj1.getString("is_priority"));

                        listArray.add(objItem);

                    }


                    arrayOfList = listArray;

                    if (null == arrayOfList || arrayOfList.size() == 0) {

                        mSwipeRefreshLayout.setVisibility(View.GONE);
                        nolayout.setVisibility(View.VISIBLE);
                        netcheck_layout.setVisibility(View.GONE);
                        progressBar.setVisibility(View.GONE);
                        progressBar_bottom.setVisibility(View.GONE);

                    } else {
                        if (arrayOfList.size() < 10) {
                            pagination = false;
                        }

                        mSwipeRefreshLayout.setVisibility(View.VISIBLE);
                        nolayout.setVisibility(View.GONE);
                        netcheck_layout.setVisibility(View.GONE);
                        progressBar.setVisibility(View.GONE);
                        progressBar_bottom.setVisibility(View.GONE);

                        setAdapterToListview();
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

/*            if (null == arrayOfList || arrayOfList.size() == 0) {
            } else {
                params = "?user_id=" + (Model.id) + "&browser_country=" + (Model.browser_country) + "&page=2" + int_floor + "&patient_id=" + pat_id + "&hline=1";
                System.out.println("params---" + params);
                new MyTask_Pagination().execute(params);
            }*/
        }
    }

    private class MyTask_Pagination extends AsyncTask<String, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            mSwipeRefreshLayout.setVisibility(View.VISIBLE);
            nolayout.setVisibility(View.GONE);
            netcheck_layout.setVisibility(View.GONE);
            progressBar.setVisibility(View.GONE);
            progressBar_bottom.setVisibility(View.VISIBLE);

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
                            Intent intent = new Intent(HotlinePatientsQueriesActivity.this, LoginActivity.class);
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

                        System.out.println("Patients List jsonobj1-----------" + jsonobj1.toString());

                        objItem = new Item();
                        objItem.setHlqid(jsonobj1.getString("qid"));
                        objItem.setHlaskedby_name(jsonobj1.getString("askedby_name"));
                        objItem.setHlasked_at(jsonobj1.getString("asked_at"));
                        //objItem.setHl(jsonobj1.getString("doctor_id"));
                        objItem.setHlquery(jsonobj1.getString("query"));
                        objItem.setHlstr_qstatus(jsonobj1.getString("str_qstatus"));

                        //objItem.setHlsp(jsonobj1.getString("speciality"));
                        //objItem.setHlplan_months(jsonobj1.getString("str_price"));
                        //objItem.setHlplan_start_date(jsonobj1.getString("price"));
                        objItem.setHlfollowcode(jsonobj1.getString("followcode"));
                        objItem.setHluser_geo(jsonobj1.getString("str_user_geo"));
                        objItem.setHltype_lable(jsonobj1.getString("q_type_label"));
                        //objItem.setHlnext_renewal(jsonobj1.getString("is_priority"));

                        listArray.add(objItem);

                    }


                    arrayOfList = listArray;

                    if (null == arrayOfList || arrayOfList.size() == 0) {

                        mSwipeRefreshLayout.setVisibility(View.VISIBLE);
                        nolayout.setVisibility(View.GONE);
                        netcheck_layout.setVisibility(View.GONE);
                        progressBar.setVisibility(View.GONE);
                        progressBar_bottom.setVisibility(View.GONE);

                    } else {

                        if (arrayOfList.size() < 10) {
                            pagination = false;
                        }

                        mSwipeRefreshLayout.setVisibility(View.VISIBLE);
                        nolayout.setVisibility(View.GONE);
                        netcheck_layout.setVisibility(View.GONE);
                        progressBar.setVisibility(View.GONE);
                        progressBar_bottom.setVisibility(View.GONE);

                        add_page_AdapterToListview();
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    public void setAdapterToListview() {
        objAdapter = new HotlinePatientsQueriesRowAdapter(HotlinePatientsQueriesActivity.this, R.layout.hotline_patients_queries_row, arrayOfList);
        listView.setAdapter(objAdapter);
    }

    public void add_page_AdapterToListview() {
        objAdapter.addAll(arrayOfList);
        listView.setSelection(objAdapter.getCount() - (arrayOfList.size()));
        objAdapter.notifyDataSetChanged();
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
}
