package com.orane.docassist.fragment;


import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

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


import com.orane.docassist.BookingViewDetails;
import com.orane.docassist.LoginActivity;
import com.orane.docassist.Model.ItemConsUnConfirmed;
import com.orane.docassist.Model.Model;
import com.orane.docassist.Network.JSONParser;
import com.orane.docassist.R;
import com.orane.docassist.adapter.UnconfirmedConsRowAdapter;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.util.ArrayList;
import java.util.List;

public class Unconfirmed_Consult_Fragment extends Fragment {

    UnconfirmedConsRowAdapter objAdapter;
    SwipeRefreshLayout mSwipeRefreshLayout;
    LinearLayout nolayout, netcheck_layout;
    ItemConsUnConfirmed objItem;
    List<ItemConsUnConfirmed> listArray;
    List<ItemConsUnConfirmed> arrayOfList;
    ListView listView;
    public String str_response, params, consid;
    ProgressBar progressBar, progressBar_bottom;
    TextView empty_msgmsg;

    Button btn_reload;
    Double floor_val;
    Integer int_floor;
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

    public Unconfirmed_Consult_Fragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.unconfirmed_consult,
                container, false);



        progressBar = (ProgressBar) rootView.findViewById(R.id.progressBar);
        progressBar_bottom = (ProgressBar) rootView.findViewById(R.id.progressBar_bottom);
        netcheck_layout = (LinearLayout) rootView.findViewById(R.id.netcheck_layout);
        nolayout = (LinearLayout) rootView.findViewById(R.id.nolayout);
        mSwipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.swipe_query_new);
        listView = (ListView) rootView.findViewById(R.id.listview);

        try {
            full_process();
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {

            full_process();

        } catch (Exception e) {
            e.printStackTrace();
        }

/*
        btn_reload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                full_process();
            }
        });
*/

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                try {
                    System.out.println("position-----" + position);

                    TextView tvpatient = (TextView) view.findViewById(R.id.tvpatient);
                    TextView tvgeo = (TextView) view.findViewById(R.id.tvgeo);
                    TextView tvid = (TextView) view.findViewById(R.id.tvid);

                    consid = tvid.getText().toString();
                    String tvpatient_val = tvpatient.getText().toString();
                    String tvgeo_val = tvgeo.getText().toString();

                    Intent intent = new Intent(getActivity(), BookingViewDetails.class);
                    intent.putExtra("consid", consid);
                    intent.putExtra("tvpatient_val", tvpatient_val);
                    intent.putExtra("tvgeo_val", tvgeo_val);
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

                            if (diff == 0) {
                                floor_val = floor_val + 1;
                            } else if (diff > 0) {
                                floor_val = floor_val + 2;
                            }

                            System.out.println("Final Val----" + floor_val);
                            int_floor = floor_val.intValue();

                        }

                        if (int_floor != 0 && (pagination)) {
                            params = Model.BASE_URL + "sapp/loadbooking4doc?user_id=" + (Model.id) + "&browser_country=" + (Model.browser_country) + "&page=" + int_floor + "&token=" + Model.token;
                            System.out.println("params-----------" + params);
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

                    params = Model.BASE_URL + "sapp/loadbooking4doc?user_id=" + (Model.id) + "&browser_country=" + (Model.browser_country) + "&page=1&token=" + Model.token;
                    System.out.println("params-----------" + params);
                    new MyTask().execute(params);
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

        return rootView;
    }


    class MyTask extends AsyncTask<String, Void, Void> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressBar.setVisibility(View.VISIBLE);
            netcheck_layout.setVisibility(View.GONE);
            nolayout.setVisibility(View.GONE);
            progressBar_bottom.setVisibility(View.GONE);
            mSwipeRefreshLayout.setVisibility(View.GONE);

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
                    listArray = new ArrayList<ItemConsUnConfirmed>();

                    for (int i = 0; i < jsonarr.length(); i++) {
                        JSONObject jsonobj1 = jsonarr.getJSONObject(i);
                        objItem = new ItemConsUnConfirmed();
                        objItem.setId(jsonobj1.getString("id"));
                        objItem.setPatient_name(jsonobj1.getString("patient_name"));
                        objItem.setPatient_geo(jsonobj1.getString("patient_geo"));
                        objItem.setConsult_type(jsonobj1.getString("str_consult_type"));
                        objItem.setConsult_date(jsonobj1.getString("consult_date"));
                        objItem.setTime_range(jsonobj1.getString("time_range"));
                        objItem.setCcase(jsonobj1.getString("case"));
                        objItem.setEearning(jsonobj1.getString("earning"));
                        listArray.add(objItem);
                    }

                    arrayOfList = listArray;

                    if (null == arrayOfList || arrayOfList.size() == 0) {

                        progressBar.setVisibility(View.GONE);
                        netcheck_layout.setVisibility(View.GONE);
                        nolayout.setVisibility(View.VISIBLE);
                        progressBar_bottom.setVisibility(View.GONE);
                        mSwipeRefreshLayout.setVisibility(View.GONE);

                    } else {
                        if (arrayOfList.size() < 10) {
                            pagination = false;
                        }

                        progressBar.setVisibility(View.GONE);
                        progressBar_bottom.setVisibility(View.GONE);
                        netcheck_layout.setVisibility(View.GONE);
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

    class MyTask_Pagination extends AsyncTask<String, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressBar.setVisibility(View.GONE);
            netcheck_layout.setVisibility(View.GONE);
            nolayout.setVisibility(View.GONE);
            progressBar_bottom.setVisibility(View.VISIBLE);
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

                            getActivity().finishAffinity();
                            Intent intent = new Intent(getActivity(), LoginActivity.class);
                            startActivity(intent);
                            getActivity().finish();
                        }
                    }

                } else if (json instanceof JSONArray) {
                    System.out.println("This is JSON ARRAY---------------" + str_response);

                    JSONArray jsonarr = new JSONArray(str_response);
                    listArray = new ArrayList<ItemConsUnConfirmed>();

                    for (int i = 0; i < jsonarr.length(); i++) {
                        JSONObject jsonobj1 = jsonarr.getJSONObject(i);
                        objItem = new ItemConsUnConfirmed();
                        objItem.setId(jsonobj1.getString("id"));
                        objItem.setPatient_name(jsonobj1.getString("patient_name"));
                        objItem.setPatient_geo(jsonobj1.getString("patient_geo"));
                        objItem.setConsult_type(jsonobj1.getString("str_consult_type"));
                        objItem.setConsult_date(jsonobj1.getString("consult_date"));
                        objItem.setTime_range(jsonobj1.getString("time_range"));
                        objItem.setCcase(jsonobj1.getString("case"));
                        objItem.setEearning(jsonobj1.getString("earning"));
                        listArray.add(objItem);
                    }

                    arrayOfList = listArray;

                    if (null == arrayOfList || arrayOfList.size() == 0) {

                        progressBar.setVisibility(View.GONE);
                        netcheck_layout.setVisibility(View.GONE);
                        nolayout.setVisibility(View.GONE);
                        progressBar_bottom.setVisibility(View.GONE);
                        mSwipeRefreshLayout.setVisibility(View.VISIBLE);

                    } else {
                        if (arrayOfList.size() < 10) {
                            pagination = false;
                        }

                        progressBar.setVisibility(View.GONE);
                        progressBar_bottom.setVisibility(View.GONE);
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


    public void setAdapterToListview() {
        try {
            objAdapter = new UnconfirmedConsRowAdapter(getActivity(), R.layout.unconfirmed_cons_row, arrayOfList);
            listView.setAdapter(objAdapter);
            objAdapter.notifyDataSetChanged();
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

    public void full_process() {
        try {
            if (isInternetOn()) {
                //------------------------------------------------------------------
                params = Model.BASE_URL + "sapp/loadbooking4doc?user_id=" + (Model.id) + "&browser_country=" + (Model.browser_country) + "&page=1&token=" + Model.token;
                System.out.println("params-----------" + params);
                new MyTask().execute(params);
                //------------------------------------------------------------------
            } else {
                progressBar.setVisibility(View.GONE);
                progressBar_bottom.setVisibility(View.GONE);
                netcheck_layout.setVisibility(View.VISIBLE);
                nolayout.setVisibility(View.GONE);
                mSwipeRefreshLayout.setVisibility(View.GONE);
            }

        } catch (Exception e2) {
            e2.printStackTrace();
        }
    }

    public final boolean isInternetOn() {

        ConnectivityManager connec = (ConnectivityManager) getActivity().getSystemService(getActivity().CONNECTIVITY_SERVICE);

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
}