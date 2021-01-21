package com.orane.docassist;


import android.app.ProgressDialog;
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

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.github.ksoichiro.android.observablescrollview.ObservableListView;
import com.github.ksoichiro.android.observablescrollview.ObservableScrollViewCallbacks;
import com.github.ksoichiro.android.observablescrollview.ScrollState;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.analytics.FirebaseAnalytics;

import com.orane.docassist.Model.Item;
import com.orane.docassist.Model.Model;
import com.orane.docassist.Network.JSONParser;
import com.orane.docassist.R;
import com.orane.docassist.adapter.MyPatientAdapter;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import me.drakeet.materialdialog.MaterialDialog;


public class MyPatientActivity extends AppCompatActivity implements ObservableScrollViewCallbacks, DatePickerDialog.OnDateSetListener {


    Toolbar toolbar;
    SwipeRefreshLayout mSwipeRefreshLayout;
    ObservableListView listView;
    EditText edt_patname, edt_pemail, edt_pmobno;
    LinearLayout nolayout, netcheck_layout;
    RelativeLayout LinearLayout1;
    public String str_response, params, pat_id;
    List<Item> arrayOfList;
    JSONObject json_search;
    MyPatientAdapter objAdapter;
    ProgressBar progressBar, progressBar_bottom;
    FloatingActionButton fab;
    Double floor_val;
    Button btn_date1, btn_date2;
    Integer int_floor;
    long startTime;
    public boolean pagination = true;
    Item objItem;
    List<Item> listArray;
    JSONObject json_response_obj;
    public String fdate1, fdate2, from_date, to_date;

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
        setContentView(R.layout.mypatients);

        Model.query_launch = "";

        //================ Shared Pref ======================
        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        Model.token = sharedpreferences.getString(token, "");
        System.out.println("Model.token----------------------" + Model.token);
        //================ Shared Pref ======================

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

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.app_color));
        }

        fab = (FloatingActionButton) findViewById(R.id.fab);
        progressBar_bottom = (ProgressBar) findViewById(R.id.progressBar_bottom);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        listView = (ObservableListView) findViewById(R.id.listview);
        LinearLayout1 = (RelativeLayout) findViewById(R.id.LinearLayout1);
        netcheck_layout = (LinearLayout) findViewById(R.id.netcheck_layout);
        nolayout = (LinearLayout) findViewById(R.id.nolayout);
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_query_new);

        full_process();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                try {
                    System.out.println("position-----" + position);
                    TextView tv_pat_name = (TextView) view.findViewById(R.id.tv_pat_name);
                    TextView tv_pat_email = (TextView) view.findViewById(R.id.tv_pat_email);
                    TextView tvid = (TextView) view.findViewById(R.id.tvid);

                    System.out.println("tv_pat_name------" + tv_pat_name.getText().toString());
                    System.out.println("tv_pat_email------" + tv_pat_email.getText().toString());
                    System.out.println("tvid------" + tvid.getText().toString());

                    Intent intent = new Intent(MyPatientActivity.this, Patient_Profile.class);
                    intent.putExtra("pat_id", tvid.getText().toString());
                    startActivity(intent);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(MyPatientActivity.this, MyPatientAddActivity.class);
                intent.putExtra("ptype", "addpatient");
                startActivity(intent);
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

                        double cur_page = (listView.getAdapter().getCount()) / 7;
                        System.out.println("cur_page 1----" + cur_page);

                        if (count < 7) {
                            System.out.println("No more to Load");
                            int_floor = 0;
                        } else if (count == 7) {
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
                            params = Model.BASE_URL + "sapp/myPatients?os_type=android&user_id=" + (Model.id) + "&browser_country=" + (Model.browser_country) + "&page=" + int_floor + "&token=" + Model.token;
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

                    //------------------------- My Patients ----------------------------
                    params = Model.BASE_URL + "sapp/myPatients?os_type=android&user_id=" + (Model.id) + "&browser_country=" + (Model.browser_country) + "&page=1&token=" + Model.token;
                    System.out.println("params-----------" + params);
                    new MyTask_server().execute(params);
                    //------------------------- My Patients ----------------------------


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


        listView.setScrollViewCallbacks(this);

    }

    @Override
    public void onScrollChanged(int i, boolean b, boolean b1) {
    }

    @Override
    public void onDownMotionEvent() {

    }

    @Override
    public void onUpOrCancelMotionEvent(ScrollState scrollState) {

        System.out.println("Scrolling----------------------" + scrollState);

        if (scrollState == ScrollState.UP) {
            //mFabToolbar.slideOutFab();
            System.out.println("Scrolling UP---------------------------");
            fab.hide();
        } else if (scrollState == ScrollState.DOWN) {
            //mFabToolbar.slideInFab();
            System.out.println("Scrolling Down---------------------------");
            fab.show();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.mypatient_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == android.R.id.home) {
            finish();
            return true;
        }

        if (id == R.id.nav_filter) {
            filter_dia();
            return true;
        }


        return super.onOptionsItemSelected(item);
    }


    public void full_process() {

        if (isInternetOn()) {

            try {

                //------------------------- My Patients ----------------------------
                String param_url = Model.BASE_URL + "sapp/myPatients?os_type=android&user_id=" + (Model.id) + "&browser_country=" + (Model.browser_country) + "&page=1&token=" + Model.token;
                System.out.println("params-----------" + param_url);
                new MyTask_server().execute(param_url);
                //------------------------- My Patients ----------------------------

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
                            Intent intent = new Intent(MyPatientActivity.this, LoginActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    }

                } else if (json instanceof JSONArray) {

                    System.out.println("This is JSON ARRAY---------------" + str_response);

                    apply_list(str_response);

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

                            //=============== Shared Preferences =============================================
                            SharedPreferences.Editor editor = sharedpreferences.edit();
                            editor.putString(Login_Status, "0");
                            editor.apply();
                            //==============Shared Preferences ===============================================

                            finishAffinity();
                            Intent intent = new Intent(MyPatientActivity.this, LoginActivity.class);
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

                        System.out.println("myPatients jsonobj1-----------" + jsonobj1.toString());

                        objItem = new Item();
                        objItem.setId(jsonobj1.getString("id"));
                        objItem.setName(jsonobj1.getString("name"));
                        objItem.setLocation(jsonobj1.getString("user_geo"));
                        //objItem.setCountry(jsonobj1.getString("last_login"));
                        //objItem.setZip(jsonobj1.getString("joined"));
                        objItem.setEmail(jsonobj1.getString("email"));
                        objItem.setLink(jsonobj1.getString("mobile"));

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

                        if (arrayOfList.size() < 7) {
                            pagination = false;
                        }

                        progressBar_bottom.setVisibility(View.GONE);
                        progressBar.setVisibility(View.GONE);
                        mSwipeRefreshLayout.setVisibility(View.VISIBLE);
                        nolayout.setVisibility(View.GONE);
                        netcheck_layout.setVisibility(View.GONE);

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

    public void setAdapterToListview() {

        objAdapter = new MyPatientAdapter(MyPatientActivity.this, R.layout.mypatinets_row, arrayOfList);
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
    public void onResume() {
        super.onResume();
        if ((Model.query_launch).equals("patadd")) {
            Model.query_launch = "";
            full_process();
        }
    }


    public void filter_dia() {

        final MaterialDialog alert = new MaterialDialog(MyPatientActivity.this);
        View view = LayoutInflater.from(MyPatientActivity.this).inflate(R.layout.mypatient_filter, null);
        alert.setView(view);

        alert.setTitle("Filter");

        edt_patname = (EditText) view.findViewById(R.id.edt_patname);
        edt_pemail = (EditText) view.findViewById(R.id.edt_pemail);
        edt_pmobno = (EditText) view.findViewById(R.id.edt_pmobno);
        btn_date1 = (Button) view.findViewById(R.id.btn_date1);
        btn_date2 = (Button) view.findViewById(R.id.btn_date2);
        TextView tv_resetfilter = (TextView) view.findViewById(R.id.tv_resetfilter);

        tv_resetfilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edt_patname.setText("");
                edt_pemail.setText("");
                edt_pmobno.setText("");

                btn_date1.setText("Select From Date");
                btn_date2.setText("Select To Date");

/*                //----------------------------------------
                if (from_date != null && !from_date.isEmpty() && !from_date.equals("null") && !from_date.equals("")) {
                    btn_date.setText(from_date);
                }
                if (to_date != null && !to_date.isEmpty() && !to_date.equals("null") && !to_date.equals("")) {
                    btn_todate.setText(to_date);
                }
                //----------------------------------------*/
            }
        });

        btn_date1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar now = Calendar.getInstance();
                DatePickerDialog dpd = DatePickerDialog.newInstance(
                        MyPatientActivity.this,
                        now.get(Calendar.YEAR),
                        now.get(Calendar.MONTH),
                        now.get(Calendar.DAY_OF_MONTH)
                );
                dpd.setThemeDark(false);
                dpd.vibrate(false);
                dpd.dismissOnPause(false);
                dpd.showYearPickerFirst(false);
                dpd.setTitle("From Date");
                dpd.show(getFragmentManager(), "Datepickerdialog1");
            }
        });


        btn_date2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar now = Calendar.getInstance();
                DatePickerDialog dpd2 = DatePickerDialog.newInstance(
                        MyPatientActivity.this,
                        now.get(Calendar.YEAR),
                        now.get(Calendar.MONTH),
                        now.get(Calendar.DAY_OF_MONTH)
                );
                dpd2.setThemeDark(false);
                dpd2.vibrate(false);
                dpd2.dismissOnPause(false);
                dpd2.showYearPickerFirst(false);
                dpd2.setTitle("To Date");
                dpd2.show(getFragmentManager(), "Datepickerdialog2");
            }
        });


        alert.setCanceledOnTouchOutside(false);
        alert.setPositiveButton("OK", new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String patname = edt_patname.getText().toString();
                String patemail = edt_pemail.getText().toString();
                String patmobno = edt_pmobno.getText().toString();

                //----------------------------------------------------
                if (btn_date1.getText().toString().equals("Select Date"))
                    fdate1 = "";
                else
                    fdate1 = btn_date1.getText().toString();
                //----------------------------------------------------

                //----------------------------------------------------
                if (btn_date2.getText().toString().equals("Select Date"))
                    fdate2 = "";
                else
                    fdate2 = btn_date2.getText().toString();
                //----------------------------------------------------

                try {
                    json_search = new JSONObject();
                    json_search.put("page", "1");
                    json_search.put("user_id", (Model.id));
                    json_search.put("patient_name", patname);
                    json_search.put("email", patemail);
                    json_search.put("mobile", patmobno);
                    json_search.put("dt_from", fdate1);
                    json_search.put("dt_to", fdate2);

                    System.out.println("json_search---" + json_search.toString());

                    new JSON_filterapply().execute(json_search);

                    //------------ Google firebase Analitics--------------------
                    Model.mFirebaseAnalytics = FirebaseAnalytics.getInstance(getApplicationContext());
                    Bundle params = new Bundle();
                    params.putString("User", Model.id + "/" + Model.name);
                    params.putString("Details", json_search.toString());
                    Model.mFirebaseAnalytics.logEvent("mypatient_filter", params);
                    //------------ Google firebase Analitics--------------------
                } catch (Exception e2) {
                    e2.printStackTrace();
                }

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


    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        try {

            System.out.println("view------" + view.getTag());

            if (view.getTag().equals("Datepickerdialog1")) {
                //String date = dayOfMonth + "/" + (++monthOfYear) + "/" + year;
                from_date = year + "/" + (monthOfYear + 1) + "/" + dayOfMonth;
                System.out.println("Cal Date------" + from_date);
                //--------- for System -------------------
                SimpleDateFormat curFormater = new SimpleDateFormat("yyyy/MM/dd");
                Date dateObj = curFormater.parse(from_date);
                String newDateStr = curFormater.format(dateObj);
                System.out.println("For System select_date---------" + newDateStr);
                //--------------------------------
                btn_date1.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
                System.out.println("from_date---------" + from_date);
            } else {
                //String date = dayOfMonth + "/" + (++monthOfYear) + "/" + year;
                to_date = year + "/" + (monthOfYear + 1) + "/" + dayOfMonth;
                System.out.println("Cal Date------" + to_date);
                //--------- for System -------------------
                SimpleDateFormat curFormater = new SimpleDateFormat("yyyy/MM/dd");
                Date dateObj = curFormater.parse(to_date);
                String newDateStr = curFormater.format(dateObj);
                System.out.println("For System select_date---------" + newDateStr);
                //--------------------------------
                btn_date2.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
                System.out.println("to_date---------" + to_date);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    private class JSON_filterapply extends AsyncTask<JSONObject, Void, Boolean> {

        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(MyPatientActivity.this);
            dialog.setMessage("Please wait..");
            dialog.show();
            dialog.setCancelable(false);
        }

        @Override
        protected Boolean doInBackground(JSONObject... urls) {
            try {

                JSONParser jParser = new JSONParser();
                str_response = jParser.JSON_POST_ARRAY(urls[0], "mypatsearch");

                System.out.println("Feedback URL---------------" + urls[0]);
                System.out.println("json_response_obj-----------" + str_response);

                return true;
            } catch (Exception e) {
                e.printStackTrace();
            }

            return false;
        }

        protected void onPostExecute(Boolean result) {

            try {
                apply_list(str_response);
                dialog.cancel();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    public void apply_list(String str_response) {

        try {

            JSONArray jsonarr = new JSONArray(str_response);
            listArray = new ArrayList<Item>();


            for (int i = 0; i < jsonarr.length(); i++) {
                JSONObject jsonobj1 = jsonarr.getJSONObject(i);

                System.out.println("myPatients jsonobj1-----------" + jsonobj1.toString());

                objItem = new Item();
                objItem.setId(jsonobj1.getString("id"));
                objItem.setName(jsonobj1.getString("name"));
                objItem.setLocation(jsonobj1.getString("user_geo"));
                //objItem.setCountry(jsonobj1.getString("last_login"));
                //objItem.setZip(jsonobj1.getString("joined"));
                objItem.setEmail(jsonobj1.getString("email"));
                objItem.setLink(jsonobj1.getString("mobile"));

                //----------------------------------------------
                if (jsonobj1.has("note")) {
                    objItem.setNotes(jsonobj1.getString("note"));
                } else {
                    objItem.setNotes("");
                }
                //----------------------------------------------

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

                if (arrayOfList.size() < 7) {
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

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
