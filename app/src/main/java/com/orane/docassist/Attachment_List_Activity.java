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
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.github.ksoichiro.android.observablescrollview.ObservableGridView;

import com.orane.docassist.Model.Item;
import com.orane.docassist.Model.Model;
import com.orane.docassist.R;
import com.orane.docassist.adapter.AttachListAdapter;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Attachment_List_Activity extends AppCompatActivity {

    Toolbar toolbar;
    RelativeLayout LinearLayout1;
    AttachListAdapter objAdapter;
    SwipeRefreshLayout mSwipeRefreshLayout;
    Item objItem;
    List<Item> listArray;
    List<Item> arrayOfList;
    ObservableGridView listView;
    JSONObject object;
    public String attach_file_text, str_response, Log_Status, params;
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
    public static final String token = "token_key";

    Double floor_val;
    Integer int_floor;
    public boolean pagination = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.myheathdata_report_images_list);


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

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                getWindow().setStatusBarColor(getResources().getColor(R.color.app_color));
            }
        }
        //------------------------------------------------------

        //================ Shared Pref ======================
        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        Log_Status = sharedpreferences.getString(Login_Status, "");
        Model.id = sharedpreferences.getString(id, "");
        Model.email = sharedpreferences.getString(email, "");
        Model.browser_country = sharedpreferences.getString(browser_country, "");
        Model.fee_q = sharedpreferences.getString(fee_q, "");
        Model.fee_consult = sharedpreferences.getString(fee_consult, "");
        Model.currency_label = sharedpreferences.getString(currency_label, "");
        Model.currency_symbol = sharedpreferences.getString(currency_symbol, "");
        Model.have_free_credit = sharedpreferences.getString(have_free_credit, "");
        Model.photo_url = sharedpreferences.getString(photo_url, "");
        Model.kmid = sharedpreferences.getString(sp_km_id, "");
        //============================================================

        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        progressBar_bottom = (ProgressBar) findViewById(R.id.progressBar_bottom);
        netcheck_layout = (LinearLayout) findViewById(R.id.netcheck_layout);
        nolayout = (LinearLayout) findViewById(R.id.nolayout);
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_query_new);
        listView = (ObservableGridView) findViewById(R.id.listview);

        try {
            Intent intent = getIntent();
            attach_file_text = intent.getStringExtra("filetxt");
            System.out.println("attach_file_text---" + attach_file_text);
        } catch (Exception e) {
            e.printStackTrace();
        }


        full_process();

/*        if (Log_Status.equals("1")) {
            full_process();
        } else {
            finishAffinity();
            intent = new Intent(Attachment_List_Activity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        }*/

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                try {
                    System.out.println("position-----" + position);
                    objAdapter.setSelection(position);
                    TextView tv_url = (TextView) view.findViewById(R.id.tv_url);
                    String tv_url_text = tv_url.getText().toString();
                    System.out.println("tv_url_text------------------" + tv_url_text);


                    String extension = tv_url_text.substring(tv_url_text.lastIndexOf(".") + 1);
                    System.out.println("Extension-------------" + extension);

                    tv_url_text = tv_url_text.replaceFirst("https://", "");
                    System.out.println("tv_url_text-------------" + tv_url_text);

                    if (extension.equals("pdf") || (extension.equals("PDF"))
                            || (extension.equals("doc"))
                            || (extension.equals("docx"))
                            || (extension.equals("xls"))
                            || (extension.equals("xlsx"))
                            || (extension.equals("ppt"))
                            || (extension.equals("pps"))
                            || (extension.equals("pptx"))
                            || (extension.equals("ppsx"))
                            || (extension.equals("txt"))
                            || (extension.equals("rtf"))
                            ) {

                        Intent i = new Intent(getApplicationContext(), WebViewActivity.class);
                        i.putExtra("url", "http://docs.google.com/viewer?url=" + tv_url_text);
                        i.putExtra("type", "Attachment View");
                        startActivity(i);
                        overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);

                    } else {

                        Intent i = new Intent(getApplicationContext(), WebViewActivity.class);
                        i.putExtra("url", "http://" + tv_url_text);
                        i.putExtra("type", "Attachment View");
                        startActivity(i);
                        overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });


        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

                try {
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

                            progressBar_bottom.setVisibility(View.GONE);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
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

                        new MyTask_server().execute("");

                        mSwipeRefreshLayout.setRefreshing(false);

                    } else {
                        progressBar.setVisibility(View.GONE);
                        progressBar_bottom.setVisibility(View.GONE);
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

    public void full_process() {

        if (isInternetOn()) {

            try {

                new MyTask_server().execute("");

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
                str_response = attach_file_text;
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

                JSONArray jsonarr = new JSONArray(str_response);
                listArray = new ArrayList<Item>();

                for (int i = 0; i < jsonarr.length(); i++) {
                    JSONObject jsonobj1 = jsonarr.getJSONObject(i);

                    objItem = new Item();
                    objItem.setId(jsonobj1.getString("user_id"));
                    objItem.setVisitor_type(jsonobj1.getString("doctype"));
                    objItem.setTitle(jsonobj1.getString("file"));
                    objItem.setDes(jsonobj1.getString("ext"));
                    objItem.setDocimg(jsonobj1.getString("url"));

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

                    setAdapterToListview();
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void setAdapterToListview() {
        try {
            objAdapter = new AttachListAdapter(Attachment_List_Activity.this, R.layout.report_image_row, arrayOfList);
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
