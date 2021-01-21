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
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.flurry.android.FlurryAgent;
import com.github.ksoichiro.android.observablescrollview.ObservableListView;
import com.github.ksoichiro.android.observablescrollview.ObservableScrollViewCallbacks;
import com.github.ksoichiro.android.observablescrollview.ScrollState;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.orane.docassist.Model.Item;
import com.orane.docassist.Model.Model;
import com.orane.docassist.Network.JSONParser;
import com.orane.docassist.R;
import com.orane.docassist.adapter.QasesListAdapter;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import me.drakeet.materialdialog.MaterialDialog;


public class QasesActivity extends AppCompatActivity implements ObservableScrollViewCallbacks {

    Item objItem;
    List<Item> listArray;
    Toolbar toolbar;
    SwipeRefreshLayout mSwipeRefreshLayout;
    ObservableListView listView;
    LinearLayout nolayout, netcheck_layout;
    RelativeLayout LinearLayout1;
    public String params, pat_id, qtype;
    public List<Item> arrayOfList;
    QasesListAdapter objAdapter;
    ProgressBar progressBar, progressBar_bottom;
    public Double floor_val;
    FloatingActionButton fab;
    Intent intent;
    Button btn_bookcons;
    public String str_response;
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
    public static final String first_query = "first_query_key";
    public static final String token = "token_key";
    TextView mTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.qases_list);

        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);

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
        btn_bookcons = (Button) findViewById(R.id.btn_bookcons);


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

            mTitle = (TextView) toolbar.findViewById(R.id.toolbar_title);
            Typeface khandBold = Typeface.createFromAsset(getApplicationContext().getAssets(), Model.font_name_bold);
            mTitle.setTypeface(khandBold);
            btn_bookcons.setTypeface(khandBold);
        }


        try {
            Intent intent = getIntent();
            qtype = intent.getStringExtra("qtype");

            if (qtype.equals("myfeeds")) {
                mTitle.setText("My Qases");
            } else {
                mTitle.setText("Medical Qases");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }


        full_process();


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                System.out.println("position-----" + position);

                TextView tvid = (TextView) view.findViewById(R.id.tv_id);

                String qases_id = tvid.getText().toString();

                System.out.println("qases_id------" + qases_id);

                Intent intent = new Intent(QasesActivity.this, Qases_View_Activity.class);
                intent.putExtra("id", qases_id);
                startActivity(intent);
            }
        });

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(QasesActivity.this, Qases_Post1.class);
                startActivity(intent);
            }
        });

        btn_bookcons.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(QasesActivity.this, Qases_Post1.class);
                startActivity(intent);
                finish();
            }
        });


        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

                Double floor_val;
                Integer int_floor = 0;

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
                            if (qtype.equals("myfeeds")) {
                                params = Model.BASE_URL + "sapp/caseList?os_type=android&page=" + int_floor + "&doctor_id=" + (Model.id) + "&token=" + Model.token;
                            } else {
                                params = Model.BASE_URL + "sapp/caseList?os_type=android&page=" + int_floor + "&token=" + Model.token;
                            }

                            System.out.println("params-----" + params);
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

                    if (qtype.equals("myfeeds")) {
                        params = Model.BASE_URL + "sapp/caseList?os_type=android&page=1&doctor_id=" + (Model.id) + "&token=" + Model.token;
                    } else {
                        params = Model.BASE_URL + "sapp/caseList?os_type=android&page=1&token=" + Model.token;
                    }

                    pagination = true;

                    System.out.println("params--------" + params);
                    new MyTask_server_refresh().execute(params);

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
/*        System.out.println("int i---------------------------" + i);
        System.out.println("boolean b---------------------------" + b);
        System.out.println("boolean b1---------------------------" + b1);*/
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
        //getMenuInflater().inflate(R.menu.mypatient_menu, menu);
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

/*    @Override
    public void onResume() {
        super.onResume();

        full_process();

    }*/


    public void full_process() {

        if (isInternetOn()) {

            try {

                if ((Model.token) != null && !(Model.token).isEmpty() && !(Model.token).equals("null") && !(Model.token).equals("")) {

                    if (qtype.equals("myfeeds")) {
                        params = Model.BASE_URL + "sapp/caseList?os_type=android&page=1&doctor_id=" + (Model.id) + "&token=" + Model.token;
                    } else {
                        params = Model.BASE_URL + "sapp/caseList?os_type=android&page=1&token=" + Model.token;
                    }

                    //----------------------------------------
                    System.out.println("params-----------" + params);
                    new MyTask_server().execute(params);
                    //----------------------------------------

                } else {
                    ask_logout();
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

        } else {
            progressBar_bottom.setVisibility(View.GONE);
            progressBar.setVisibility(View.GONE);
            mSwipeRefreshLayout.setVisibility(View.GONE);
            nolayout.setVisibility(View.GONE);
            netcheck_layout.setVisibility(View.VISIBLE);
            fab.setVisibility(View.GONE);
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
            fab.setVisibility(View.GONE);
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
                    System.out.println("Doctors List--This is JSON OBJECT---------------" + str_response);

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
                            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    }

                } else if (json instanceof JSONArray) {

                    JSONArray jsonarr = new JSONArray(str_response);
                    listArray = new ArrayList<Item>();

                    //----------- Flurry -------------------------------------------------
                    Map<String, String> articleParams = new HashMap<String, String>();
                    articleParams.put("android.doc.qases_list_count", ("" + jsonarr.length()));
                    FlurryAgent.logEvent("android.doc.Qases_List", articleParams);
                    //----------- Flurry -------------------------------------------------

                    for (int i = 0; i < jsonarr.length(); i++) {
                        JSONObject jsonobj1 = jsonarr.getJSONObject(i);

                        System.out.println("jsonobj1-------------" + jsonobj1.toString());

                        //------------------------------------------------
                        objItem = new Item();
                        objItem.setQid(jsonobj1.getString("id"));
                        objItem.setSpec(jsonobj1.getString("str_sp"));
                        objItem.setDocimg(jsonobj1.getString("doc_photo"));
                        objItem.setQbanner("");
                        objItem.setDocname(jsonobj1.getString("doc_name"));
                        objItem.setDocspec(jsonobj1.getString("str_sp"));
                        objItem.setQtime("");
                        objItem.setQtitle(jsonobj1.getString("title"));
                        objItem.setQdesc(jsonobj1.getString("desc"));
                        objItem.setQlikes("");
                        objItem.setLink(jsonobj1.getString("case_url"));
                        objItem.setQcomments(jsonobj1.getString("comment_count") + " Comments");
                        //---------------------------------------------------

                        listArray.add(objItem);

                    }

                    arrayOfList = listArray;
                }


                if (null == arrayOfList || arrayOfList.size() == 0) {

                    progressBar_bottom.setVisibility(View.GONE);
                    progressBar.setVisibility(View.GONE);
                    mSwipeRefreshLayout.setVisibility(View.GONE);
                    nolayout.setVisibility(View.VISIBLE);
                    netcheck_layout.setVisibility(View.GONE);
                    fab.setVisibility(View.GONE);

                } else {


                    if (arrayOfList.size() < 10) {
                        pagination = false;
                    }

                    progressBar_bottom.setVisibility(View.GONE);
                    progressBar.setVisibility(View.GONE);
                    mSwipeRefreshLayout.setVisibility(View.VISIBLE);
                    nolayout.setVisibility(View.GONE);
                    netcheck_layout.setVisibility(View.GONE);
                    fab.setVisibility(View.VISIBLE);

                    setAdapterToListview();
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
                    System.out.println("Doctors List--This is JSON OBJECT---------------" + str_response);

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
                            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    }

                } else if (json instanceof JSONArray) {

                    JSONArray jsonarr = new JSONArray(str_response);
                    listArray = new ArrayList<Item>();


                    for (int i = 0; i < jsonarr.length(); i++) {
                        JSONObject jsonobj1 = jsonarr.getJSONObject(i);

                        System.out.println("jsonobj1-------------" + jsonobj1.toString());

                        //------------------------------------------------
                        objItem = new Item();
                        objItem.setQid(jsonobj1.getString("id"));
                        objItem.setSpec(jsonobj1.getString("str_sp"));
                        objItem.setDocimg(jsonobj1.getString("doc_photo"));
                        objItem.setQbanner("");
                        objItem.setDocname(jsonobj1.getString("doc_name"));
                        objItem.setDocspec(jsonobj1.getString("str_sp"));
                        objItem.setQtime("");
                        objItem.setQtitle(jsonobj1.getString("title"));
                        objItem.setQdesc(jsonobj1.getString("desc"));
                        objItem.setQlikes("");
                        objItem.setLink(jsonobj1.getString("case_url"));
                        objItem.setQcomments(jsonobj1.getString("comment_count") + " Comments");
                        //---------------------------------------------------

                        listArray.add(objItem);

                    }
                    arrayOfList = listArray;
                }


                if (null == arrayOfList || arrayOfList.size() == 0) {

                    //Toast.makeText(getApplicationContext(), "No more patients to load", Toast.LENGTH_SHORT).show();

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

                    add_page_AdapterToListview();
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    private class MyTask_server_refresh extends AsyncTask<String, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressBar_bottom.setVisibility(View.GONE);
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
                    System.out.println("Doctors List--This is JSON OBJECT---------------" + str_response);

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
                            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    }

                } else if (json instanceof JSONArray) {

                    JSONArray jsonarr = new JSONArray(str_response);
                    listArray = new ArrayList<Item>();

                    for (int i = 0; i < jsonarr.length(); i++) {
                        JSONObject jsonobj1 = jsonarr.getJSONObject(i);
                        System.out.println("jsonobj1-------------" + jsonobj1.toString());

                        //------------------------------------------------
                        objItem = new Item();
                        objItem.setQid(jsonobj1.getString("id"));
                        objItem.setSpec(jsonobj1.getString("str_sp"));
                        objItem.setDocimg(jsonobj1.getString("doc_photo"));
                        objItem.setQbanner("");
                        objItem.setDocname(jsonobj1.getString("doc_name"));
                        objItem.setDocspec(jsonobj1.getString("str_sp"));
                        objItem.setQtime("");
                        objItem.setQtitle(jsonobj1.getString("title"));
                        objItem.setQdesc(jsonobj1.getString("desc"));
                        objItem.setQlikes("");
                        objItem.setLink(jsonobj1.getString("case_url"));
                        objItem.setQcomments(jsonobj1.getString("comment_count") + " Comments");
                        //---------------------------------------------------

                        listArray.add(objItem);
                    }
                }

                arrayOfList = listArray;

                if (null == arrayOfList || arrayOfList.size() == 0) {

                    progressBar_bottom.setVisibility(View.GONE);
                    progressBar.setVisibility(View.GONE);
                    mSwipeRefreshLayout.setVisibility(View.GONE);
                    nolayout.setVisibility(View.VISIBLE);
                    netcheck_layout.setVisibility(View.GONE);
                    fab.setVisibility(View.GONE);

                } else {

                    progressBar_bottom.setVisibility(View.GONE);
                    progressBar.setVisibility(View.GONE);
                    mSwipeRefreshLayout.setVisibility(View.VISIBLE);
                    nolayout.setVisibility(View.GONE);
                    netcheck_layout.setVisibility(View.GONE);
                    fab.setVisibility(View.VISIBLE);

                    setAdapterToListview();
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void setAdapterToListview() {

        objAdapter = new QasesListAdapter(QasesActivity.this, R.layout.qases_row, arrayOfList);
        listView.setAdapter(objAdapter);


        /*
        //----------------------- Delayed Notify Dialog ------------------------------

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

                try {
                    View view = QasesActivity.this.getLayoutInflater().inflate(R.layout.layout_tip_content_horizontal, null);
                    ed = new EasyDialog(QasesActivity.this)
                            //.setLayoutResourceId(R.layout.layout_tip_content_horizontal)//layout resource id
                            .setLayout(view)
                            .setBackgroundColor(QasesActivity.this.getResources().getColor(R.color.white))
                            //.setLocation(new location[])//point in screen
                            .setLocationByAttachedView(fab)
                            .setGravity(EasyDialog.GRAVITY_TOP)
                            .setAnimationTranslationShow(EasyDialog.DIRECTION_X, 1000, -600, 100, -50, 50, 0)
                            .setAnimationAlphaShow(1000, 0.3f, 1.0f)
                            .setAnimationTranslationDismiss(EasyDialog.DIRECTION_X, 500, -50, 800)
                            .setAnimationAlphaDismiss(500, 1.0f, 0.0f)
                            .setTouchOutsideDismiss(true)
                            .setMatchParent(true)
                            .setMarginLeftAndRight(24, 24)
                            .setOutsideColor(QasesActivity.this.getResources().getColor(R.color.outside_color_gray))
                            .setCancelable(true)
                            .show();

                    Button btn_butt = (Button) view.findViewById(R.id.btn_butt);

                    btn_butt.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(QasesActivity.this, Qases_Post1.class);
                            startActivity(intent);

                            ed.dismiss();
                        }
                    });

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }, 1300);

        //----------------------- Delayed Notify Dialog ------------------------------
        */


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


    public void onSeenClick(View v) {

        //ImageView imgview = (ImageView) v.findViewById(R.id.tv_img18);

        switch (v.getId()) {

            case R.id.like_layout:

                System.out.println("Liked....");

/*                intent = new Intent(getApplicationContext(), WebViewActivity.class);
                intent.putExtra("url", "https://www.icliniq.com/blog/innovator-online-doctor-click-away-the-week/");
                intent.putExtra("type", "iCliniq As Seen as");
                startActivity(intent);
                overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);*/
                break;

            case R.id.share_layout:

                TextView txt_url = (TextView) v.findViewById(R.id.tv_qases_url);
                String url = txt_url.getText().toString();

                Intent sharingIntent = new Intent(Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                sharingIntent.putExtra(Intent.EXTRA_TEXT, "Discuss medical cases with icliniq doctors for \n\n" + url);
                startActivity(Intent.createChooser(sharingIntent, "Share using"));
                break;


        }

    }


    public void ask_logout() {

        final MaterialDialog alert = new MaterialDialog(QasesActivity.this);
        alert.setTitle("Logout..!");
        alert.setMessage("For security purpose, please logout and login again to continue");
        alert.setCanceledOnTouchOutside(false);
        alert.setPositiveButton("Ok", new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //============================================================
                SharedPreferences.Editor editor = sharedpreferences.edit();
                editor.putString(Login_Status, "0");
                editor.apply();
                //============================================================
                alert.dismiss();

                finishAffinity();
                Intent intent = new Intent(QasesActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });


        alert.show();
    }


}
