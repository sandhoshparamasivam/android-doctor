package com.orane.docassist;

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
import android.widget.TextView;

import com.flurry.android.FlurryAgent;

import com.orane.docassist.Model.ItemCons;
import com.orane.docassist.Model.Model;
import com.orane.docassist.Network.JSONParser;
import com.orane.docassist.R;
import com.orane.docassist.adapter.ConsHistoryRowAdapter;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ConsHistoryPatProfile extends AppCompatActivity {

    ConsHistoryRowAdapter objAdapter;
    Toolbar toolbar;
    SwipeRefreshLayout mSwipeRefreshLayout;
    JSONObject object;
    LinearLayout nolayout, netcheck_layout;
    ProgressBar progressBar, progressBar_bottom;
    Intent intent;
    long startTime;
    ListView listView;
    public boolean pagination = true;
    ItemCons objItem;
    public String str_response, pat_id;

    List<ItemCons> listArray;
    List<ItemCons> arrayOfList;

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
    ProgressBar progressBar_Bottom;
    public String params, consid, scroll_text, flash_text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.consult_history_activity);

        FlurryAgent.onPageView();
        //------------------------------------------------------
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setTitle("Consultation History");
        }
        //------------------------------------------------------

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.app_color));
        }

        progressBar_Bottom = (ProgressBar) findViewById(R.id.progressBar_Bottom);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        netcheck_layout = (LinearLayout) findViewById(R.id.netcheck_layout);
        nolayout = (LinearLayout) findViewById(R.id.nolayout);
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_query_new);
        listView = (ListView) findViewById(R.id.listview);

        try {
            Intent intent = getIntent();
            pat_id = intent.getStringExtra("pat_id");

            System.out.println("Get Cons History pat_id---" + pat_id);
        } catch (Exception e) {
            e.printStackTrace();
        }


        full_process();


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                try {
                    System.out.println("position-----" + position);

                    TextView tvid = (TextView) view.findViewById(R.id.tvid);
                    consid = tvid.getText().toString();

                    Intent intent = new Intent(ConsHistoryPatProfile.this, ConsHistoryView.class);
                    intent.putExtra("consid", consid);
                    intent.putExtra("cons_view_type", "History");
                    startActivity(intent);

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
                            //-----------------------------------------------
                            params = Model.BASE_URL + "sapp/consultHistory4Doc?os_type=android&patient_id=" + pat_id + "&user_id=" + (Model.id) + "&page=" + int_floor + "&token=" + Model.token + "&enc=1";
                            System.out.println("params-----------" + params);
                            new JSON_Pagination().execute(params);
                            //-----------------------------------------------
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

                    //---------------------------
                    params = Model.BASE_URL + "sapp/consultHistory4Doc?os_type=android&patient_id=" + pat_id + "&user_id=" + (Model.id) + "&page=1&token=" + Model.token + "&enc=1";
                    System.out.println("params-----------" + params);
                    new JSON_Server().execute(params);
                    //---------------------------

                    mSwipeRefreshLayout.setRefreshing(false);

                } else {

                    progressBar.setVisibility(View.GONE);
                    netcheck_layout.setVisibility(View.VISIBLE);
                    nolayout.setVisibility(View.GONE);
                    mSwipeRefreshLayout.setVisibility(View.GONE);
                    progressBar_Bottom.setVisibility(View.GONE);

                }
            }
        });
    }

    public void full_process() {

        try {

            if (isInternetOn()) {
                //-----------------------------------------------
                params = Model.BASE_URL + "sapp/consultHistory4Doc?os_type=android&patient_id=" + pat_id + "&user_id=" + (Model.id) + "&page=1&token=" + Model.token + "&enc=1";
                System.out.println("params-----------" + params);
                new JSON_Server().execute(params);
                //-----------------------------------------------

            } else {
                progressBar.setVisibility(View.GONE);
                netcheck_layout.setVisibility(View.VISIBLE);
                nolayout.setVisibility(View.GONE);
                mSwipeRefreshLayout.setVisibility(View.GONE);
                progressBar_Bottom.setVisibility(View.GONE);
            }
        } catch (Exception e2) {
            e2.printStackTrace();
        }
    }

    private class JSON_Server extends AsyncTask<String, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressBar.setVisibility(View.VISIBLE);
            netcheck_layout.setVisibility(View.GONE);
            nolayout.setVisibility(View.GONE);
            mSwipeRefreshLayout.setVisibility(View.GONE);
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
                            Intent intent = new Intent(ConsHistoryPatProfile.this, LoginActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    }

                } else if (json instanceof JSONArray) {
                    System.out.println("This is JSON ARRAY---------------" + str_response);

                    JSONArray jsonarr = new JSONArray(str_response);
                    listArray = new ArrayList<ItemCons>();

                    for (int i = 0; i < jsonarr.length(); i++) {
                        JSONObject jsonobj1 = jsonarr.getJSONObject(i);
                        objItem = new ItemCons();
                        objItem.setId(jsonobj1.getString("id"));
                        objItem.setPatient(jsonobj1.getString("patient_name") + ", " + jsonobj1.getString("patient_geo"));
                        objItem.setNotes(jsonobj1.getString("notes"));
                        objItem.setAppttype(jsonobj1.getString("str_appt_type"));
                        objItem.setStatus(jsonobj1.getString("str_status"));
                        objItem.setApptdt(jsonobj1.getString("dt") + "," + jsonobj1.getString("str_appt_time"));
                        listArray.add(objItem);
                    }

                    arrayOfList = listArray;

                    if (null == arrayOfList || arrayOfList.size() == 0) {

                        progressBar.setVisibility(View.GONE);
                        netcheck_layout.setVisibility(View.GONE);
                        nolayout.setVisibility(View.VISIBLE);
                        progressBar_Bottom.setVisibility(View.GONE);
                        mSwipeRefreshLayout.setVisibility(View.GONE);

                    } else {

                        if (arrayOfList.size() < 10) {
                            pagination = false;
                        }

                        progressBar.setVisibility(View.GONE);
                        netcheck_layout.setVisibility(View.GONE);
                        progressBar_Bottom.setVisibility(View.GONE);
                        nolayout.setVisibility(View.GONE);
                        mSwipeRefreshLayout.setVisibility(View.VISIBLE);

                        setAdapterToListview();
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
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

    private class JSON_Pagination extends AsyncTask<String, Void, Void> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBar.setVisibility(View.GONE);
            netcheck_layout.setVisibility(View.GONE);
            nolayout.setVisibility(View.GONE);
            progressBar_Bottom.setVisibility(View.VISIBLE);
            mSwipeRefreshLayout.setVisibility(View.VISIBLE);
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
                            Intent intent = new Intent(ConsHistoryPatProfile.this, LoginActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    }

                } else if (json instanceof JSONArray) {
                    System.out.println("This is JSON ARRAY---------------" + str_response);

                    JSONArray jsonarr = new JSONArray(str_response);
                    listArray = new ArrayList<ItemCons>();

                    for (int i = 0; i < jsonarr.length(); i++) {
                        JSONObject jsonobj1 = jsonarr.getJSONObject(i);
                        objItem = new ItemCons();
                        objItem.setId(jsonobj1.getString("id"));
                        objItem.setPatient(jsonobj1.getString("patient_name") + ", " + jsonobj1.getString("patient_geo"));
                        objItem.setNotes(jsonobj1.getString("notes"));
                        objItem.setAppttype(jsonobj1.getString("str_appt_type"));
                        objItem.setStatus(jsonobj1.getString("str_status"));
                        objItem.setApptdt(jsonobj1.getString("dt") + "," + jsonobj1.getString("str_appt_time"));
                        listArray.add(objItem);
                    }

                    arrayOfList = listArray;

                    if (null == arrayOfList || arrayOfList.size() == 0) {

                        progressBar_Bottom.setVisibility(View.GONE);
                        progressBar.setVisibility(View.GONE);
                        netcheck_layout.setVisibility(View.GONE);
                        nolayout.setVisibility(View.GONE);
                        mSwipeRefreshLayout.setVisibility(View.VISIBLE);

                    } else {
                        if (arrayOfList.size() < 10) {
                            pagination = false;
                        }

                        progressBar_Bottom.setVisibility(View.GONE);
                        progressBar.setVisibility(View.GONE);
                        netcheck_layout.setVisibility(View.GONE);
                        nolayout.setVisibility(View.GONE);
                        mSwipeRefreshLayout.setVisibility(View.VISIBLE);

                        add_page_AdapterToListview();
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //getMenuInflater().inflate(R.menu.myclinic_menu, menu);
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

    public void setAdapterToListview() {
        try {
            objAdapter = new ConsHistoryRowAdapter(ConsHistoryPatProfile.this, R.layout.conshistory_row, arrayOfList);
            listView.setAdapter(objAdapter);
        } catch (Exception e2) {
            e2.printStackTrace();
        }
    }

    public void add_page_AdapterToListview() {
        try {
            objAdapter.addAll(arrayOfList);
            listView.setSelection(objAdapter.getCount() - (arrayOfList.size()));
            objAdapter.notifyDataSetChanged();
        } catch (Exception e2) {
            e2.printStackTrace();
        }
    }

}
