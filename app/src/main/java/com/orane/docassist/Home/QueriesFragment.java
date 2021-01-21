package com.orane.docassist.Home;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.flurry.android.FlurryAgent;

import com.google.android.material.appbar.AppBarLayout;
import com.orane.docassist.AnsweredQueryViewActivity;
import com.orane.docassist.HotlineChatViewActivity;
import com.orane.docassist.LoginActivity;
import com.orane.docassist.Model.Item;
import com.orane.docassist.Model.Model;
import com.orane.docassist.Network.JSONParser;
import com.orane.docassist.NewQueryViewActivity;
import com.orane.docassist.R;
import com.orane.docassist.adapter.QueryAnsweredRowAdapter;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import me.drakeet.materialdialog.MaterialDialog;

import static android.content.Context.CONNECTIVITY_SERVICE;

public class QueriesFragment extends Fragment {

    ProgressBar progressBar, progressBar_Bottom;
    QueryAnsweredRowAdapter objAdapter;
    LinearLayout nolayout, netcheck_layout;
    SwipeRefreshLayout mSwipeRefreshLayout;
    Item objItem;
    String doctor_id;
    List<Item> listArray;
    List<Item> arrayOfList;
    ListView listView;
    public String str_response, params, hline_val;
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

    public static QueriesFragment newInstance(int pageIndex) {
        QueriesFragment contentFragment = new QueriesFragment();
        Bundle args = new Bundle();
        args.putInt("pageIndex", pageIndex);
        contentFragment.setArguments(args);
        return contentFragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.query_answered, container, false);

        //================ Shared Pref ======================
        sharedpreferences = getActivity().getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);

        //============================================================

        //------------------ Variable Initialization ---------------------------------------------------
        progressBar_Bottom = (ProgressBar) rootView.findViewById(R.id.progressBar_Bottom);
        progressBar = (ProgressBar) rootView.findViewById(R.id.progressBar);
        netcheck_layout = (LinearLayout) rootView.findViewById(R.id.netcheck_layout);
        btn_reload = (Button) rootView.findViewById(R.id.btn_reload);
        nolayout = (LinearLayout) rootView.findViewById(R.id.nolayout);
        mSwipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.swipe_query_new);
        listView = (ListView) rootView.findViewById(R.id.listview);
        AppBarLayout appBarLayout = (AppBarLayout) rootView.findViewById(R.id.appBarLayout);
        appBarLayout.setVisibility(View.GONE);
        //------------------ Variable Initialization ---------------------------------------------------

        full_process();

        btn_reload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if ((Model.id) != null && !(Model.id).isEmpty() && !(Model.id).equals("null") && !(Model.id).equals("")) {
                    full_process();
                } else {
                    force_logout();
                }
            }
        });


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                try {

                    TextView query = (TextView) view.findViewById(R.id.tvquery);
                    TextView speciality = (TextView) view.findViewById(R.id.tvspeciality);
                    TextView followcode = (TextView) view.findViewById(R.id.tvfollowupcode);
                    TextView patient = (TextView) view.findViewById(R.id.tvaskedname);
                    TextView from = (TextView) view.findViewById(R.id.tvgeo);
                    TextView askeddate = (TextView) view.findViewById(R.id.tvdate);
                    TextView tv_hline = (TextView) view.findViewById(R.id.tv_hline);
                    TextView tv_doctor_id = (TextView) view.findViewById(R.id.tv_doctor_id);

                    Model.query = query.getText().toString();
                    Model.from = from.getText().toString();
                    Model.followcode = followcode.getText().toString();
                    Model.patient = patient.getText().toString();
                    Model.askeddate = askeddate.getText().toString();
                    hline_val = tv_hline.getText().toString();
                    doctor_id = tv_doctor_id.getText().toString();

/*
                    Intent intent = new Intent(AnsweredQueriesActivity.this, QueryAnsweredDetailActivity.class);
                    intent.putExtra("followupcode", Model.followcode);
                    startActivity(intent);
*/
                    if (hline_val.equals("1")) {
                        Intent intent = new Intent(getActivity(), HotlineChatViewActivity.class);
                        intent.putExtra("follouwupcode", Model.followcode);
                        intent.putExtra("doctor_id", doctor_id);
                        startActivity(intent);

                    } else {
                     /*   Intent intent = new Intent(getActivity(), AnsweredQueryViewActivity.class);
                        intent.putExtra("followupcode", Model.followcode);
                        intent.putExtra("query_price", "0");
                        intent.putExtra("pat_from", (Model.from));
                        intent.putExtra("qtype", "answered_query");
                        startActivity(intent);
*/
                        Intent intent = new Intent(getActivity(), NewQueryViewActivity.class);
                        intent.putExtra("followupcode", Model.followcode);
                        intent.putExtra("pat_location", Model.patient );
                        intent.putExtra("str_price", "");
                        intent.putExtra("qtype", "new_query");
                        intent.putExtra("finisher", new android.os.ResultReceiver(null) {
                            @Override
                            protected void onReceiveResult(int resultCode, Bundle resultData) {
                                getActivity().finish();
                            }
                        });
                        startActivityForResult(intent, 1);
                    }
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
                            //--------------------------------------------------------
                            String url = Model.BASE_URL + "sapp/qAnsweredDoc?user_id=" + (Model.id) + "&browser_country=" + (Model.browser_country) + "&page=" + int_floor + "&token=" + Model.token;
                            System.out.println("url----" + url);
                            new MyTask_Pagination().execute(url);
                            //--------------------------------------------------------
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

                        //-------------------------------------------------------
                        String url = Model.BASE_URL + "sapp/qAnsweredDoc?user_id=" + (Model.id) + "&browser_country=" + (Model.browser_country) + "&page=1&token=" + Model.token;
                        System.out.println("url----" + url);
                        new MyTask_refresh().execute(url);
                        //-------------------------------------------------------

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


        return rootView;
    }

    public void setAdapterToListview() {

        objAdapter = new QueryAnsweredRowAdapter(getActivity(), R.layout.query_answered_row, arrayOfList);
        listView.setAdapter(objAdapter);
        objAdapter.notifyDataSetChanged();
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


    private class MyTask_server extends AsyncTask<String, Void, Void> {

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

                            getActivity().finishAffinity();
                            Intent intent = new Intent(getActivity(), LoginActivity.class);
                            startActivity(intent);
                            getActivity().finish();
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

                        //----------------------------------------------------------
                        if (jsonobj1.getString("speciality").equals("null"))
                            objItem.setSpeciality("Speciality: N/A");
                        else
                            objItem.setSpeciality(jsonobj1.getString("speciality"));
                        //----------------------------------------------------------

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
                        objItem.setHline(jsonobj1.getString("is_hline"));
                        objItem.setDocname(jsonobj1.getString("doctor_id"));

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

                            getActivity().finishAffinity();
                            Intent intent = new Intent(getActivity(), LoginActivity.class);
                            startActivity(intent);
                            getActivity().finish();
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
                        objItem.setHline(jsonobj1.getString("is_hline"));

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

    private class MyTask_refresh extends AsyncTask<String, Void, Void> {

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

                            getActivity().finishAffinity();
                            Intent intent = new Intent(getActivity(), LoginActivity.class);
                            startActivity(intent);
                            getActivity().finish();
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
                        objItem.setHline(jsonobj1.getString("is_hline"));

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

               // Model.id = "597789";

                //--------------------------------------------------
                String url = Model.BASE_URL + "sapp/qAnsweredDoc?user_id=" + (Model.id) + "&browser_country=" + (Model.browser_country) + "&page=1&&token=" + Model.token;
                //String url = Model.BASE_URL + "sapp/qAnsweredDoc?user_id=597789&browser_country=" + (Model.browser_country) + "&page=1&&token=" + Model.token;


                System.out.println("url----" + url);
                new MyTask_server().execute(url);
                //------------------------------------------------------------

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

        ConnectivityManager connec = (ConnectivityManager) getActivity().getSystemService(CONNECTIVITY_SERVICE);

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
            final MaterialDialog alert = new MaterialDialog(getActivity());
            alert.setTitle("Please Re-Login the App");
            alert.setMessage("Something went wrong. Please Logout and Login again to continue");
            alert.setCanceledOnTouchOutside(false);
            alert.setPositiveButton("OK", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    alert.dismiss();
                    //============================================================
                    SharedPreferences.Editor editor = sharedpreferences.edit();
                    editor.putString(Login_Status, "0");
                    editor.apply();
                    //============================================================
                    getActivity().finishAffinity();
                    Intent i = new Intent(getActivity(), LoginActivity.class);
                    startActivity(i);
                    getActivity().finish();
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

}
