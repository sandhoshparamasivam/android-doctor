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
import android.widget.Toast;

import com.github.ksoichiro.android.observablescrollview.ObservableListView;
import com.github.ksoichiro.android.observablescrollview.ObservableScrollViewCallbacks;
import com.github.ksoichiro.android.observablescrollview.ScrollState;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.orane.docassist.Model.Item;
import com.orane.docassist.Model.Model;
import com.orane.docassist.Network.JSONParser;
import com.orane.docassist.R;
import com.orane.docassist.adapter.NotesAdapter;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import me.drakeet.materialdialog.MaterialDialog;


public class Patient_Notes_Activity extends AppCompatActivity implements ObservableScrollViewCallbacks {


    TextView tvid;
    Toolbar toolbar;
    SwipeRefreshLayout mSwipeRefreshLayout;
    ObservableListView listView;
    LinearLayout nolayout, netcheck_layout;
    RelativeLayout LinearLayout1;
    public String str_response, params, pat_id;
    JSONObject post_view_notes;
    List<Item> arrayOfList;
    JSONObject json_noteedit;
    JSONArray json_response_array;
    NotesAdapter objAdapter;
    ProgressBar progressBar, progressBar_bottom;
    FloatingActionButton fab;
    Double floor_val;
    Integer int_floor;
    long startTime;
    public boolean pagination = true;
    Item objItem;
    String json_response_string;
    List<Item> listArray;

    JSONObject json_response_obj;

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
        setContentView(R.layout.notes_list_view);

        Model.query_launch = "";

        try {

            Intent intent = getIntent();
            pat_id = intent.getStringExtra("pat_id");
            System.out.println("Get pat_id---" + pat_id);

        } catch (Exception e) {
            e.printStackTrace();
        }

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

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Patient_Notes_Activity.this, AddNotesActivity.class);
                intent.putExtra("pat_id", pat_id);
                startActivity(intent);
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                try {
                    System.out.println("position-----" + position);
                    Button btn_delete = (Button) view.findViewById(R.id.btn_delete);
                    Button btn_edit = (Button) view.findViewById(R.id.btn_edit);
                    tvid = (TextView) view.findViewById(R.id.tvid);

                    System.out.println("btn_delete------" + btn_delete.getText().toString());
                    System.out.println("tvid------" + tvid.getText().toString());

                    Intent intent = new Intent(Patient_Notes_Activity.this, Patient_Profile.class);
                    intent.putExtra("pat_id", tvid.getText().toString());
                    startActivity(intent);

/*
                    btn_delete.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            System.out.println("Deleting--ID----" + tvid.getText().toString());
                        }
                    });

                    btn_edit.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            System.out.println("Editing--ID----" + tvid.getText().toString());
                        }
                    });*/

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

                            try {

                                post_view_notes = new JSONObject();
                                post_view_notes.put("user_id", (Model.id));
                                post_view_notes.put("patient_id", pat_id);
                                post_view_notes.put("page", int_floor);
                                post_view_notes.put("limit", "10");

                                System.out.println("post_view_notes---" + post_view_notes.toString());
                                new MyTask_server_Pagination().execute(post_view_notes);

                            } catch (Exception e) {
                                e.printStackTrace();
                            }
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

                    try {

                        post_view_notes = new JSONObject();
                        post_view_notes.put("user_id", (Model.id));
                        post_view_notes.put("patient_id", pat_id);
                        post_view_notes.put("page", "1");
                        post_view_notes.put("limit", "10");

                        System.out.println("post_view_notes---" + post_view_notes.toString());
                        new MyTask_server().execute(post_view_notes);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
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


    public void full_process() {

        if (isInternetOn()) {

            try {

                post_view_notes = new JSONObject();
                post_view_notes.put("user_id", (Model.id));
                post_view_notes.put("patient_id", pat_id);
                post_view_notes.put("page", "1");
                post_view_notes.put("limit", "10");

                System.out.println("post_view_notes---" + post_view_notes.toString());
                new MyTask_server().execute(post_view_notes);

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

    private class MyTask_server extends AsyncTask<JSONObject, Void, Void> {

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
        protected Void doInBackground(JSONObject... params) {
            try {
                JSONParser jParser = new JSONParser();
                json_response_string = jParser.JSON_POST_ARRAY(params[0], "notes_view");

                System.out.println("Param URL---------------" + params[0]);
                System.out.println("json_response_string-----------" + json_response_string);

            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);

            try {

                if (json_response_string.length() > 2) {

                    json_response_array = new JSONArray(json_response_string);
                    listArray = new ArrayList<Item>();

                    for (int i = 0; i < json_response_array.length(); i++) {
                        JSONObject jsonobj1 = json_response_array.getJSONObject(i);
                        System.out.println("myPatients jsonobj1-----------" + jsonobj1.toString());
                        objItem = new Item();
                        objItem.setNotes(jsonobj1.getString("note"));
                        objItem.setDt(jsonobj1.getString("dt"));
                        objItem.setHid(jsonobj1.getString("noteId"));
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


            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private class MyTask_server_Pagination extends AsyncTask<JSONObject, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressBar_bottom.setVisibility(View.GONE);
            progressBar.setVisibility(View.GONE);
            mSwipeRefreshLayout.setVisibility(View.VISIBLE);
            nolayout.setVisibility(View.GONE);
            netcheck_layout.setVisibility(View.GONE);

            startTime = System.currentTimeMillis();
        }

        @Override
        protected Void doInBackground(JSONObject... params) {
            try {
                JSONParser jParser = new JSONParser();
                json_response_string = jParser.JSON_POST_ARRAY(params[0], "notes_view");

                System.out.println("Param URL---------------" + params[0]);
                System.out.println("json_response_string-----------" + json_response_string);

            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);


            try {

                if (json_response_string.length() > 2) {

                    for (int i = 0; i < json_response_array.length(); i++) {
                        JSONObject jsonobj1 = json_response_array.getJSONObject(i);

                        System.out.println("myPatients jsonobj1-----------" + jsonobj1.toString());

                        objItem = new Item();
                        objItem.setId(jsonobj1.getString("id"));
                        objItem.setNotes(jsonobj1.getString("note"));
                        objItem.setDt(jsonobj1.getString("dt"));
                        objItem.setHid(jsonobj1.getString("noteId"));
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


            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void setAdapterToListview() {
        try {
            objAdapter = new NotesAdapter(Patient_Notes_Activity.this, R.layout.notes_view, arrayOfList);
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
    public void onResume() {
        super.onResume();
        if ((Model.query_launch).equals("added_notes")) {
            Model.query_launch = "";
            full_process();
        }
    }

    public void btn_click_edit(View v) {
        try {
            View parent = (View) v.getParent();
            View old_parent = (View) parent.getParent();

            TextView tvid = (TextView) parent.findViewById(R.id.tvid);
            TextView tv_notes = (TextView) old_parent.findViewById(R.id.tv_notes);

            String idval = tvid.getText().toString();
            String tv_notes_val = tv_notes.getText().toString();

            System.out.println("idval--------------" + idval);
            System.out.println("tv_notes_val--------------" + tv_notes_val);

            ask_notes(tv_notes_val, idval);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void btn_click_del(View v) {
        View parent = (View) v.getParent();
        View old_parent = (View) parent.getParent();

        TextView tvid = (TextView) parent.findViewById(R.id.tvid);
        TextView tv_notes = (TextView) old_parent.findViewById(R.id.tv_notes);

        String idval = tvid.getText().toString();
        String tv_notes_val = tv_notes.getText().toString();

        System.out.println("idval--------------" + idval);
        System.out.println("tv_notes_val--------------" + tv_notes_val);

        try {
            json_noteedit = new JSONObject();
            json_noteedit.put("note_id", idval);
            json_noteedit.put("user_id", Model.id);
            json_noteedit.put("token", Model.token);
            json_noteedit.put("patient_id", pat_id);

            System.out.println("json_noteedit---" + json_noteedit.toString());

            new JSON_del_notes().execute(json_noteedit);

        } catch (Exception e2) {
            e2.printStackTrace();
        }
    }


    public void ask_notes(String old_notes, final String note_id) {

        try {
            final MaterialDialog alert = new MaterialDialog(Patient_Notes_Activity.this);
            View view = LayoutInflater.from(Patient_Notes_Activity.this).inflate(R.layout.edit_notes, null);
            alert.setView(view);
            alert.setTitle("Notes");
            final EditText edt_feedback = (EditText) view.findViewById(R.id.edt_feedback);
            edt_feedback.setText(old_notes);

            alert.setCanceledOnTouchOutside(false);
            alert.setPositiveButton("OK", new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    String feedback = edt_feedback.getText().toString();

                    try {
                        json_noteedit = new JSONObject();
                        json_noteedit.put("note_id", note_id);
                        json_noteedit.put("notes", edt_feedback.getText().toString());
                        json_noteedit.put("user_id", Model.id);
                        json_noteedit.put("token", Model.token);

                        System.out.println("json_noteedit---" + json_noteedit.toString());

                        new JSON_editnotes().execute(json_noteedit);

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

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private class JSON_editnotes extends AsyncTask<JSONObject, Void, Boolean> {

        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(Patient_Notes_Activity.this);
            dialog.setMessage("Please wait..");
            dialog.show();
            dialog.setCancelable(false);
        }

        @Override
        protected Boolean doInBackground(JSONObject... urls) {
            try {
                JSONParser jParser = new JSONParser();
                json_response_obj = jParser.JSON_POST(urls[0], "update_notes");

                System.out.println("Feedback URL---------------" + urls[0]);
                System.out.println("json_response_obj-----------" + json_response_obj.toString());

                return true;
            } catch (Exception e) {
                e.printStackTrace();
            }

            return false;
        }

        protected void onPostExecute(Boolean result) {
            try {

                String report_response = json_response_obj.getString("status");
                System.out.println("report_response--------------" + report_response);

                if (report_response.equals("1")) {
                    full_process();
                } else {
                    Toast.makeText(getApplicationContext(), "Failed to update noted. Please try again.", Toast.LENGTH_LONG).show();
                }

                dialog.cancel();

                Model.query_launch = "added_notes";

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    private class JSON_del_notes extends AsyncTask<JSONObject, Void, Boolean> {

        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(Patient_Notes_Activity.this);
            dialog.setMessage("Please wait..");
            dialog.show();
            dialog.setCancelable(false);
        }

        @Override
        protected Boolean doInBackground(JSONObject... urls) {
            try {
                JSONParser jParser = new JSONParser();
                json_response_obj = jParser.JSON_POST(urls[0], "delete_notes");

                System.out.println("Feedback URL---------------" + urls[0]);
                System.out.println("json_response_obj-----------" + json_response_obj.toString());

                return true;
            } catch (Exception e) {
                e.printStackTrace();
            }

            return false;
        }

        protected void onPostExecute(Boolean result) {
            try {

                String report_response = json_response_obj.getString("status");
                System.out.println("report_response--------------" + report_response);

                if (report_response.equals("1")) {
                    full_process();
                } else {
                    Toast.makeText(getApplicationContext(), "Failed to update noted. Please try again.", Toast.LENGTH_LONG).show();
                }
                dialog.cancel();

                Model.query_launch = "added_notes";

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


}
