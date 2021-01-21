package com.orane.docassist;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
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


import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.orane.docassist.Model.Item;
import com.orane.docassist.Model.Model;
import com.orane.docassist.Network.JSONParser;
import com.orane.docassist.R;
import com.orane.docassist.adapter.MyClinicAdapter;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.util.ArrayList;
import java.util.List;


public class MyClinicActivity extends AppCompatActivity {

    Toolbar toolbar;
    SwipeRefreshLayout mSwipeRefreshLayout;
    ListView listView;
    LinearLayout nolayout, netcheck_layout;
    RelativeLayout LinearLayout1;
    String str_response, params;
    List<Item> arrayOfList;
    List<Item> listArray;
    MyClinicAdapter objAdapter;
    ProgressBar progressBar, progressBar_bottom;
    FloatingActionButton fab;
    Double floor_val;
    Integer int_floor;
    long startTime;
    Item objItem;
    public boolean pagination = true;


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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.myclinic);

        Model.query_launch = "";

        //================ Shared Pref ======================
        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        Model.token = sharedpreferences.getString(token, "");
        System.out.println("Model.token----------------------" + Model.token);
        //================ Shared Pref ======================


        //--------------------------------------------------------
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setTitle("");

            TextView mTitle = (TextView) toolbar.findViewById(R.id.toolbar_title);
            Typeface khandBold = Typeface.createFromAsset(getApplicationContext().getAssets(), Model.font_name_bold);
            mTitle.setTypeface(khandBold);
        }
        //--------------------------------------------------------

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.app_color));
        }

        fab = (FloatingActionButton) findViewById(R.id.fab);
        progressBar_bottom = (ProgressBar) findViewById(R.id.progressBar_bottom);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        listView = (ListView) findViewById(R.id.listview);
        LinearLayout1 = (RelativeLayout) findViewById(R.id.LinearLayout1);
        netcheck_layout = (LinearLayout) findViewById(R.id.netcheck_layout);
        nolayout = (LinearLayout) findViewById(R.id.nolayout);
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_query_new);
        listView = (ListView) findViewById(R.id.listview);


        full_process();


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                try {
                    System.out.println("position-----" + position);

                    TextView tv_clinic_name = (TextView) view.findViewById(R.id.tv_clinic_name);
                    TextView tv_clinic_street = (TextView) view.findViewById(R.id.tv_clinic_street);
                    TextView tv_clinic_geo = (TextView) view.findViewById(R.id.tv_clinic_geo);
                    TextView tvid = (TextView) view.findViewById(R.id.tvid);
                    TextView tv_zip_code = (TextView) view.findViewById(R.id.tv_zip_code);

                    System.out.println("clinic_id------" + tvid.getText().toString());
                    System.out.println("clinic_name------" + tv_clinic_name.getText().toString());
                    System.out.println("clinic_Street------" + tv_clinic_street.getText().toString());
                    System.out.println("clinic_geo------" + tv_clinic_geo.getText().toString());
                    System.out.println("tv_zip_code------" + tv_zip_code.getText().toString());

                    Intent intent = new Intent(MyClinicActivity.this, MyClinicAddActivity.class);
                    intent.putExtra("clinic_id", tvid.getText().toString());
                    intent.putExtra("clinic_name", tv_clinic_name.getText().toString());
                    intent.putExtra("clinic_street", tv_clinic_street.getText().toString());
                    intent.putExtra("clinic_geo", tv_clinic_geo.getText().toString());
                    intent.putExtra("zip_code", tv_zip_code.getText().toString());
                    intent.putExtra("mode", "update");
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
                            params = Model.BASE_URL + "sapp/myClinics?user_id=" + (Model.id) + "&page=" + int_floor + "&token=" + Model.token;
                            System.out.println("params-----------" + params);
                            new MyTask_server_Pagination().execute(params);
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
                    params = Model.BASE_URL + "sapp/myClinics?user_id=" + (Model.id) + "&page=1&token=" + Model.token;
                    System.out.println("params-----------" + params);
                    new MyTask_server().execute(params);

                    mSwipeRefreshLayout.setRefreshing(false);

                } else {

                    progressBar_bottom.setVisibility(View.GONE);
                    progressBar.setVisibility(View.GONE);
                    mSwipeRefreshLayout.setVisibility(View.GONE);
                    nolayout.setVisibility(View.GONE);
                    netcheck_layout.setVisibility(View.VISIBLE);
                }
            }
        });

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(MyClinicActivity.this, MyClinicAddActivity.class);
                intent.putExtra("clinic_id", "0");
                intent.putExtra("clinic_name", "");
                intent.putExtra("clinic_geo", "");
                intent.putExtra("mode", "new");
                startActivity(intent);
            }
        });


    }

    public void full_process() {

        if (isInternetOn()) {

            try {
                params = Model.BASE_URL + "sapp/myClinics?user_id=" + (Model.id) + "&page=1&token=" + Model.token;
                System.out.println("params-----------" + params);
                new MyTask_server().execute(params);
            } catch (Exception e) {
                e.printStackTrace();
            }

        } else {
            progressBar_bottom.setVisibility(View.GONE);
            progressBar.setVisibility(View.GONE);
            mSwipeRefreshLayout.setVisibility(View.GONE);
            nolayout.setVisibility(View.GONE);
            netcheck_layout.setVisibility(View.VISIBLE);
        }
    }

    private class MyTask_server extends AsyncTask<String, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressBar_bottom.setVisibility(View.GONE);
            progressBar.setVisibility(View.VISIBLE);
            mSwipeRefreshLayout.setVisibility(View.GONE);
            nolayout.setVisibility(View.GONE);
            netcheck_layout.setVisibility(View.GONE);

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
                            Intent intent = new Intent(MyClinicActivity.this, LoginActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    }

                } else if (json instanceof JSONArray) {
                    System.out.println("This is JSON ARRAY---------------" + str_response);

                    JSONArray jsonarr = new JSONArray(str_response);
                    listArray = new ArrayList<Item>();

                    if (str_response.length() > 2) {

                        for (int i = 0; i < jsonarr.length(); i++) {
                            JSONObject jsonobj1 = jsonarr.getJSONObject(i);

                            System.out.println("myClinics jsonobj1-----------" + jsonobj1.toString());

                            objItem = new Item();
                            objItem.setHlid(jsonobj1.getString("id"));
                            objItem.setLocation(jsonobj1.getString("location"));
                            objItem.setCountry(jsonobj1.getString("country"));
                            objItem.setZip(jsonobj1.getString("zip"));
                            objItem.setState(jsonobj1.getString("state"));
                            objItem.setCity(jsonobj1.getString("city"));
                            objItem.setStreet(jsonobj1.getString("street"));
                            objItem.setTitle(jsonobj1.getString("title"));
                            //objItem.setTitle("Sample Clinic");

                            listArray.add(objItem);

                        }

                        arrayOfList = listArray;

                        if (null == arrayOfList || arrayOfList.size() == 0) {

                            progressBar_bottom.setVisibility(View.GONE);
                            progressBar.setVisibility(View.GONE);
                            mSwipeRefreshLayout.setVisibility(View.GONE);
                            nolayout.setVisibility(View.VISIBLE);
                            netcheck_layout.setVisibility(View.GONE);

                        } else {
                            if (arrayOfList.size() < 10) {
                                pagination = false;
                            }

                            progressBar_bottom.setVisibility(View.GONE);
                            progressBar.setVisibility(View.GONE);
                            mSwipeRefreshLayout.setVisibility(View.VISIBLE);
                            nolayout.setVisibility(View.GONE);
                            netcheck_layout.setVisibility(View.GONE);

                            if (getSupportActionBar() != null) {
                                getSupportActionBar().setTitle("");
                            }

                            setAdapterToListview();
                        }

                    } else {
                        progressBar_bottom.setVisibility(View.GONE);
                        progressBar.setVisibility(View.GONE);
                        mSwipeRefreshLayout.setVisibility(View.GONE);
                        nolayout.setVisibility(View.VISIBLE);
                        netcheck_layout.setVisibility(View.GONE);
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

    private class MyTask_server_Pagination extends AsyncTask<String, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressBar_bottom.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.GONE);
            mSwipeRefreshLayout.setVisibility(View.VISIBLE);
            nolayout.setVisibility(View.GONE);
            netcheck_layout.setVisibility(View.GONE);
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
                            Intent intent = new Intent(MyClinicActivity.this, LoginActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    }

                } else if (json instanceof JSONArray) {
                    System.out.println("This is JSON ARRAY---------------" + str_response);

                    JSONArray jsonarr = new JSONArray(str_response);
                    listArray = new ArrayList<Item>();

                    if (str_response.length() > 2) {

                        for (int i = 0; i < jsonarr.length(); i++) {
                            JSONObject jsonobj1 = jsonarr.getJSONObject(i);

                            System.out.println("myClinics jsonobj1-----------" + jsonobj1.toString());

                            objItem = new Item();
                            objItem.setHlid(jsonobj1.getString("id"));
                            objItem.setLocation(jsonobj1.getString("location"));
                            objItem.setCountry(jsonobj1.getString("country"));
                            objItem.setZip(jsonobj1.getString("zip"));
                            objItem.setState(jsonobj1.getString("state"));
                            objItem.setCity(jsonobj1.getString("city"));
                            objItem.setStreet(jsonobj1.getString("street"));
                            objItem.setTitle(jsonobj1.getString("title"));

                            listArray.add(objItem);

                        }

                        arrayOfList = listArray;

                        if (null == arrayOfList || arrayOfList.size() == 0) {

                            progressBar_bottom.setVisibility(View.GONE);
                            progressBar.setVisibility(View.GONE);
                            mSwipeRefreshLayout.setVisibility(View.VISIBLE);
                            nolayout.setVisibility(View.GONE);
                            netcheck_layout.setVisibility(View.GONE);

                        } else {
                            if (arrayOfList.size() < 10) {
                                pagination = false;
                            }

                            progressBar_bottom.setVisibility(View.GONE);
                            progressBar.setVisibility(View.GONE);
                            mSwipeRefreshLayout.setVisibility(View.VISIBLE);
                            nolayout.setVisibility(View.GONE);
                            netcheck_layout.setVisibility(View.GONE);

                            if (getSupportActionBar() != null) {
                                getSupportActionBar().setTitle("");
                            }

                            add_page_AdapterToListview();
                        }

                    } else {
                        progressBar_bottom.setVisibility(View.GONE);
                        progressBar.setVisibility(View.GONE);
                        mSwipeRefreshLayout.setVisibility(View.VISIBLE);
                        nolayout.setVisibility(View.GONE);
                        netcheck_layout.setVisibility(View.GONE);
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

    public void setAdapterToListview() {

        try {
            objAdapter = new MyClinicAdapter(MyClinicActivity.this, R.layout.myclinic_row, arrayOfList);
            listView.setAdapter(objAdapter);
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
        getMenuInflater().inflate(R.menu.myclinic_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == android.R.id.home) {
            finish();
            return true;
        }

        if (id == R.id.nav_sendapptslot) {
            Intent intent = new Intent(MyClinicActivity.this, SendApptSlot1.class);
            startActivity(intent);
        }

        if (id == R.id.nav_addclinic) {

            Intent intent = new Intent(MyClinicActivity.this, MyClinicAddActivity.class);
            intent.putExtra("clinic_id", "0");
            intent.putExtra("clinic_name", "");
            intent.putExtra("clinic_geo", "");
            intent.putExtra("mode", "new");
            startActivity(intent);

            return true;
        }

        if (id == R.id.nav_addpatients) {

            Intent intent = new Intent(MyClinicActivity.this, MyPatientAddActivity.class);
            intent.putExtra("ptype", "invitepatient");
            startActivity(intent);

            return true;
        }
        if (id == R.id.nav_mypatients) {

            System.out.println("My Patients------------------");
            Intent intent = new Intent(MyClinicActivity.this, MyPatientActivity.class);
            startActivity(intent);

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onResume() {
        super.onResume();
        if ((Model.query_launch).equals("cliniqadd")) {
            full_process();
        }
    }

}
