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
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.orane.docassist.Model.Item;
import com.orane.docassist.Model.Model;
import com.orane.docassist.Network.JSONParser;
import com.orane.docassist.R;
import com.orane.docassist.adapter.PayoutTransactionAdapter;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import me.drakeet.materialdialog.MaterialDialog;


public class PayoutTransactions extends AppCompatActivity {

    Toolbar toolbar;
    RelativeLayout LinearLayout1;
    Button btn_reload;
    PayoutTransactionAdapter objAdapter;
    SwipeRefreshLayout mSwipeRefreshLayout;
    Item objItem;
    List<Item> listArray;
    List<Item> arrayOfList;
    ListView listView;
    TextView empty_msgmsg;
    JSONObject object;
    JSONObject jsonobj_payout;
    public String payouttext, str_response, Log_Status, query_price, pat_from, params, prio_text, followcode_text;
    private ProgressBar bar;
    LinearLayout nolayout, netcheck_layout;
    ProgressBar progressBar, progressBar_bottom;
    Intent intent;
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

    public boolean pagination = true;
    Double floor_val;
    Integer int_floor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.payout_history);

        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);

        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        progressBar_bottom = (ProgressBar) findViewById(R.id.progressBar_bottom);
        btn_reload = (Button) findViewById(R.id.btn_reload);
        netcheck_layout = (LinearLayout) findViewById(R.id.netcheck_layout);
        nolayout = (LinearLayout) findViewById(R.id.nolayout);
        empty_msgmsg = (TextView) findViewById(R.id.empty_msgmsg);
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_query_new);
        listView = (ListView) findViewById(R.id.listview);

        //------------------------------------------------------
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
        //------------------------------------------------------
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.app_color));
        }

        overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);

        full_process();


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
                            String url = Model.BASE_URL + "sapp/payouts?user_id=" + (Model.id) + "&browser_country=" + (Model.browser_country) + "&page=" + int_floor + "&token=" + Model.token;
                            System.out.println("url------------" + url);
                            new MyTask_Pagination().execute(url);
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
                    String url = Model.BASE_URL + "sapp/payouts?user_id=" + (Model.id) + "&browser_country=" + (Model.browser_country) + "&page=1&token=" + Model.token;
                    System.out.println("url------------" + url);
                    new MyTask_server().execute(url);

                    mSwipeRefreshLayout.setRefreshing(false);

                } else {
                    progressBar.setVisibility(View.GONE);
                    progressBar_bottom.setVisibility(View.GONE);
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

                if ((Model.id) != null && !(Model.id).isEmpty() && !(Model.id).equals("null") && !(Model.id).equals("")) {
                    String url = Model.BASE_URL + "sapp/payouts?user_id=" + (Model.id) + "&browser_country=" + (Model.browser_country) + "&page=1" + "&token=" + Model.token;
                    System.out.println("url------------" + url);
                    new MyTask_server().execute(url);
                } else {
                    go_back_msg();
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

        } else {
            nolayout.setVisibility(View.GONE);
            mSwipeRefreshLayout.setVisibility(View.GONE);
            netcheck_layout.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.GONE);
            progressBar_bottom.setVisibility(View.GONE);
        }

    }

    private class MyTask_server extends AsyncTask<String, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressBar.setVisibility(View.VISIBLE);
            progressBar_bottom.setVisibility(View.GONE);
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
                jsonobj_payout = new JSONObject(str_response);
                if (jsonobj_payout.has("token_status")) {
                    String token_status = jsonobj_payout.getString("token_status");
                    if (token_status.equals("0")) {

                        //============================================================
                        SharedPreferences.Editor editor = sharedpreferences.edit();
                        editor.putString(Login_Status, "0");
                        editor.apply();
                        //============================================================

                        finishAffinity();
                        Intent intent = new Intent(PayoutTransactions.this, LoginActivity.class);
                        startActivity(intent);
                        finish();
                    }
                } else {

                    payouttext = jsonobj_payout.getString("list");

                    if (payouttext.length() > 2) {


                        JSONArray jsonarr = new JSONArray(payouttext);
                        if (payouttext.length() > 2) {

                            listArray = new ArrayList<Item>();

                            for (int i = 0; i < jsonarr.length(); i++) {

                                JSONObject jsonobj1 = jsonarr.getJSONObject(i);
                                objItem = new Item();
                                objItem.setId(jsonobj1.getString("id"));
                                objItem.setWtype(jsonobj1.getString("status"));
                                objItem.setWdatetime(jsonobj1.getString("dt"));
                                objItem.setWdesc(jsonobj1.getString("proof"));
                                objItem.setWamt(jsonobj1.getString("amount"));

                                listArray.add(objItem);
                            }

                            arrayOfList = listArray;

                            if (null == arrayOfList || arrayOfList.size() == 0) {

                                progressBar.setVisibility(View.GONE);
                                progressBar_bottom.setVisibility(View.GONE);
                                mSwipeRefreshLayout.setVisibility(View.GONE);
                                nolayout.setVisibility(View.VISIBLE);
                                netcheck_layout.setVisibility(View.GONE);

                            } else {
                                if (arrayOfList.size() < 10) {
                                    pagination = false;
                                }

                                progressBar.setVisibility(View.GONE);
                                progressBar_bottom.setVisibility(View.GONE);
                                mSwipeRefreshLayout.setVisibility(View.VISIBLE);
                                nolayout.setVisibility(View.GONE);
                                netcheck_layout.setVisibility(View.GONE);

                                long elapsedTime = System.currentTimeMillis() - startTime;
                                System.out.println("Total Elapsed of AsyncTask (in milliseconds)--- " + elapsedTime);

                                setAdapterToListview();
                            }
                        }
                    } else {
                        progressBar.setVisibility(View.GONE);
                        progressBar_bottom.setVisibility(View.GONE);
                        mSwipeRefreshLayout.setVisibility(View.GONE);
                        nolayout.setVisibility(View.VISIBLE);
                        netcheck_layout.setVisibility(View.GONE);
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

            progressBar.setVisibility(View.GONE);
            progressBar_bottom.setVisibility(View.VISIBLE);
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


                jsonobj_payout = new JSONObject(str_response);

                if (jsonobj_payout.has("token_status")) {
                    String token_status = jsonobj_payout.getString("token_status");
                    if (token_status.equals("0")) {

                        //============================================================
                        SharedPreferences.Editor editor = sharedpreferences.edit();
                        editor.putString(Login_Status, "0");
                        editor.apply();
                        //============================================================

                        finishAffinity();
                        Intent intent = new Intent(PayoutTransactions.this, LoginActivity.class);
                        startActivity(intent);
                        finish();
                    }
                } else {

                    payouttext = jsonobj_payout.getString("list");

                    JSONArray jsonarr = new JSONArray(payouttext);
                    if (payouttext.length() > 2) {

                        listArray = new ArrayList<Item>();

                        for (int i = 0; i < jsonarr.length(); i++) {

                            JSONObject jsonobj1 = jsonarr.getJSONObject(i);
                            objItem = new Item();
                            objItem.setId(jsonobj1.getString("id"));
                            objItem.setWtype(jsonobj1.getString("status"));
                            objItem.setWdatetime(jsonobj1.getString("dt"));
                            objItem.setWdesc(jsonobj1.getString("proof"));
                            objItem.setWamt(jsonobj1.getString("amount"));

                            listArray.add(objItem);
                        }

                        arrayOfList = listArray;

                        if (null == arrayOfList || arrayOfList.size() == 0) {

                            //Toast.makeText(PayoutTransactions.this, "No more items to load", Toast.LENGTH_SHORT).show();

                            progressBar.setVisibility(View.GONE);
                            progressBar_bottom.setVisibility(View.GONE);
                            mSwipeRefreshLayout.setVisibility(View.VISIBLE);
                            nolayout.setVisibility(View.GONE);
                            netcheck_layout.setVisibility(View.GONE);

                        } else {
                            if (arrayOfList.size() < 10) {
                                pagination = false;
                            }
                            progressBar.setVisibility(View.GONE);
                            progressBar_bottom.setVisibility(View.GONE);
                            mSwipeRefreshLayout.setVisibility(View.VISIBLE);
                            nolayout.setVisibility(View.GONE);
                            netcheck_layout.setVisibility(View.GONE);

                            add_page_AdapterToListview();
                        }
                    }
                }


            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    public void setAdapterToListview() {

        objAdapter = new PayoutTransactionAdapter(PayoutTransactions.this, R.layout.payout_transaction_row, arrayOfList);
        listView.setAdapter(objAdapter);
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

    public void go_back_msg() {


        //---------------- Dialog------------------------------------------------------------------
        final MaterialDialog alert = new MaterialDialog(PayoutTransactions.this);
        alert.setTitle("Oops.!");
        alert.setMessage("Something went wrong. Please go back and Try again.");
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
    public boolean onCreateOptionsMenu(Menu menu) {
        //getMenuInflater().inflate(R.menu.new_query_menu, menu);
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
