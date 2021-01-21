package com.orane.docassist.fragment;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.appcompat.widget.Toolbar;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.text.Html;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.flurry.android.FlurryAgent;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.orane.docassist.CommonActivity;
import com.orane.docassist.HotlineChatViewActivity;
import com.orane.docassist.LoginActivity;
import com.orane.docassist.Model.Item;
import com.orane.docassist.Model.Model;
import com.orane.docassist.MyWalletActivity;
import com.orane.docassist.Network.JSONParser;
import com.orane.docassist.NewQueryViewActivity;
import com.orane.docassist.POD_NewQueriesActivity;
import com.orane.docassist.POD_NewQueryViewActivity;
import com.orane.docassist.POD_Queries_Activity_New;
import com.orane.docassist.R;
import com.orane.docassist.adapter.NewQueryListAdapter;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import me.drakeet.materialdialog.MaterialDialog;

import static android.content.Context.CONNECTIVITY_SERVICE;

public class POD_queries_Fragment extends Fragment {

    NewQueryListAdapter objAdapter;
    RelativeLayout LinearLayout1;
    Button btn_reload;
    Item objItem;
    List<Item> listArray;
    List<Item> arrayOfList;
    ListView listView;
    TextView empty_msgmsg, tv_info;
    JSONObject object, json_response_obj, jsonobj_canisnaswer, json_err_feedback;
    public String tv_id_val, answering_status, tv_hline_text, str_response, Log_Status, pat_location, str_price, params, prio_text, followcode_text;
    private ProgressBar bar;
    ProgressBar progressBar, progressBar_bottom;
    Intent intent;
    long startTime;
    ImageView imgapp;
    JSONObject postappt_json, pod_PostSlot_jsonobj;
    TextView tvtips1, tvtips2, tvtips3;

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
    TextView tv_noqueries;
    LinearLayout nolayout, netcheck_layout;
    SwipeRefreshLayout mSwipeRefreshLayout;

    public POD_queries_Fragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.pod_query_new,
                container, false);

        //================ Shared Pref ======================
        sharedpreferences = getActivity().getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        Log_Status = sharedpreferences.getString(Login_Status, "");
        Model.id = sharedpreferences.getString(id, "");
        Model.token = sharedpreferences.getString(token, "");
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


        //================ Shared Pref ======================
        sharedpreferences = getActivity().getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
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

/*      Model.id = "2479";
        Model.token = "1f34004ebcb05f9acda6016d5cc52d5e-2f9aa47d34a52323ed2ea81506be444a";*/

        progressBar = (ProgressBar) rootView.findViewById(R.id.progressBar);
        progressBar_bottom = (ProgressBar) rootView.findViewById(R.id.progressBar_bottom);
        btn_reload = (Button) rootView.findViewById(R.id.btn_reload);
        netcheck_layout = (LinearLayout) rootView.findViewById(R.id.netcheck_layout);
        nolayout = (LinearLayout) rootView.findViewById(R.id.nolayout);
        empty_msgmsg = (TextView) rootView.findViewById(R.id.empty_msgmsg);
        mSwipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.swipe_query_new);
        listView = (ListView) rootView.findViewById(R.id.listview);
        tv_noqueries = (TextView) rootView.findViewById(R.id.tv_noqueries);
        tv_info = (TextView) rootView.findViewById(R.id.tv_info);

        Typeface khandBold = Typeface.createFromAsset(getActivity().getAssets(), Model.font_name);
        tv_noqueries.setTypeface(khandBold);

        if (Log_Status.equals("1")) {
            full_process();
        } else {
            getActivity().finishAffinity();
            intent = new Intent(getActivity(), LoginActivity.class);
            startActivity(intent);
            getActivity().finish();
        }


        btn_reload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (isInternetOn()) {

                    full_process();

                } else {
                    progressBar.setVisibility(View.GONE);
                    progressBar_bottom.setVisibility(View.GONE);
                    mSwipeRefreshLayout.setVisibility(View.GONE);
                    nolayout.setVisibility(View.GONE);
                    netcheck_layout.setVisibility(View.VISIBLE);
                }
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                try {
                    System.out.println("position-----" + position);

                    objAdapter.setSelection(position);

                    TextView tv_id = (TextView) view.findViewById(R.id.tv_id);
                    TextView query = (TextView) view.findViewById(R.id.tvquery);
                    TextView speciality = (TextView) view.findViewById(R.id.tvspeciality);
                    TextView price = (TextView) view.findViewById(R.id.tvprice);
                    TextView followcode = (TextView) view.findViewById(R.id.tvfollowupcode);
                    TextView patient = (TextView) view.findViewById(R.id.tvaskedname);
                    TextView from = (TextView) view.findViewById(R.id.tvgeo);
                    TextView askeddate = (TextView) view.findViewById(R.id.tvdate);
                    TextView prio = (TextView) view.findViewById(R.id.tvpriority);
                    TextView tv_hline = (TextView) view.findViewById(R.id.tv_hline);

                    prio_text = prio.getText().toString();
                    tv_id_val = tv_id.getText().toString();
                    pat_location = from.getText().toString();
                    str_price = price.getText().toString();
                    followcode_text = followcode.getText().toString();
                    tv_hline_text = tv_hline.getText().toString();

                    Model.ans_cache = "";

                    System.out.println("tv_id_val------------------" + tv_id_val);
                    System.out.println("prio_text------------------" + prio_text);
                    System.out.println("pat_location------------------" + pat_location);
                    System.out.println("str_price------------------" + str_price);
                    System.out.println("followcode_text------------------" + followcode_text);
                    System.out.println("tv_hline_text------------------" + tv_hline_text);


                    try {
                        Intent intent = new Intent(getActivity(), POD_NewQueryViewActivity.class);
                        intent.putExtra("followupcode", followcode_text);
                        intent.putExtra("pat_location", pat_location);
                        intent.putExtra("str_price", str_price);
                        intent.putExtra("qtype", "new_query");
                        intent.putExtra("finisher", new android.os.ResultReceiver(null) {
                            @Override
                            protected void onReceiveResult(int resultCode, Bundle resultData) {
                                getActivity().finish();
                            }
                        });
                        startActivityForResult(intent, 1);

/*                                //------------------------------------------------------
                                String url = Model.BASE_URL + "sapp/canIAnswer?os_type=android&user_id=" + (Model.id) + "&qid=" + tv_id_val + "&token=" + Model.token;
                                System.out.println("canIAnswer url------" + url);
                                new JSON_canianswer().execute(url);
                                //------------------------------------------------------*/
                    } catch (Exception e) {
                        //go_back_msg();
                        e.printStackTrace();
                    }


                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        /*listView.setOnScrollListener(new AbsListView.OnScrollListener() {
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

                            if (int_floor != 0 && (pagination)) {
                                //-----------------------------------------
                                params = Model.BASE_URL + "sapp/qinboxDoc?user_id=" + (Model.id) + "&browser_country=" + (Model.browser_country) + "&page=" + int_floor + "&token=" + Model.token + "&enc=1";
                                System.out.println("params-----------" + params);
                               // new MyTask_Pagination().execute(params);
                                //-----------------------------------------
                            }
                        }
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

            }
        });*/

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                try {
                    if (isInternetOn()) {
                        pagination = true;

                        full_process();
/*                        //----------------------- Doctor Inbox -------------------------------
                        params = Model.BASE_URL + "sapp/qinboxDoc?os_type=android&user_id=" + (Model.id) + "&browser_country=" + (Model.browser_country) + "&page=1&token=" + Model.token + "&enc=1";
                        System.out.println("params-----------" + params);
                        new MyTask_server().execute(params);
                        //----------------------- Doctor Inbox -------------------------------*/

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


        return rootView;
    }

    public void full_process() {

        if (isInternetOn()) {

            try {

                postappt_json = new JSONObject();
                postappt_json.put("user_id", (Model.id));
                postappt_json.put("token", Model.token);

                System.out.println("POD_Post ------------->" + postappt_json.toString());

                new JSON_POD_list().execute(postappt_json);

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


    class MyTask_Pagination extends AsyncTask<String, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressBar.setVisibility(View.GONE);
            progressBar_bottom.setVisibility(View.VISIBLE);
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
                        if ((jsonobj1.getString("speciality").equals("null")))
                            objItem.setSpeciality("Internal Medicine");
                        else
                            objItem.setSpeciality(jsonobj1.getString("speciality"));
                        //----------------------------------------------------------

                        //objItem.setSpeciality("Speciality : " + jsonobj1.getString("speciality"));

                        objItem.setPrice(jsonobj1.getString("str_price"));
                        objItem.setFupcode(jsonobj1.getString("followcode"));
                        objItem.seAskedName(jsonobj1.getString("askedby_name"));
                        objItem.setGeo(jsonobj1.getString("str_user_geo"));
                        objItem.setPubdate(jsonobj1.getString("asked_at"));
                        objItem.setLink(jsonobj1.getString("askedby_name"));
                        objItem.setLabel(jsonobj1.getString("q_type_label"));

                        Model.is_priority_text = "nil";
                        objItem.setPri("nil");

                        //-----------------------------------------------------------
                        try {
                            if ((jsonobj1.getString("is_priority")).equals(JSONObject.NULL)) {
                                System.out.println("NULL");
                                //Model.is_priority_text = "nil";
                            } else {
                                System.out.println("NOT NULL");
                                String ptext = jsonobj1.getString("is_priority");
                                if ((ptext).equals("1")) {
                                    objItem.setPri("Priority Query");
                                    Model.is_priority_text = "Priority Query";
                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        //-----------------------------------------------------------

                        //-----------------------------------------------------------
                        try {
                            if ((jsonobj1.getString("do_not_answer")).equals(JSONObject.NULL)) {
                                System.out.println("NULL");
                            } else {
                                System.out.println("NOT NULL");
                                String ptext = jsonobj1.getString("do_not_answer");
                                if ((ptext).equals("1")) {
                                    objItem.setPri("Please answer the priority queries first, before answering this");
                                    Model.is_priority_text = "Please answer the priority queries first, before answering this";
                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        //-----------------------------------------------------------

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

                        add_page_AdapterToListview();
                    }

                    //----------------------------------------------------------
                    long elapsedTime = System.currentTimeMillis() - startTime;
                    System.out.println("Total Elapsed of AsyncTask (in milliseconds)--- " + elapsedTime);
                    System.out.println("arrayOfList.size()---------" + arrayOfList.size());
                    //----------------------------------------------------------
                    //----------- Flurry -------------------------------------------------
                    Map<String, String> articleParams = new HashMap<String, String>();
                    articleParams.put("android.doc.NewQueryListCount", "" + arrayOfList.size());
                    articleParams.put("android.doc.ElapsedTime", "" + elapsedTime);
                    FlurryAgent.logEvent("android.doc.New_Queries_List", articleParams);
                    //----------- Flurry -------------------------------------------------

                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    public void setAdapterToListview() {
        try {
            objAdapter = new NewQueryListAdapter(getActivity(), R.layout.pod_query_new_row, arrayOfList);
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

    public void go_back_msg() {

        //---------------- Dialog------------------------------------------------------------------
        final MaterialDialog alert = new MaterialDialog(getActivity());
        alert.setTitle("Oops!");
        alert.setMessage("Something went wrong; please try again.");
        alert.setCanceledOnTouchOutside(false);
        alert.setPositiveButton("Yes,logout!", new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //============================================================
                sharedpreferences = getActivity().getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedpreferences.edit();
                editor.putString(Login_Status, "0");
                editor.apply();
                //============================================================

                getActivity().finishAffinity();
                Intent i = new Intent(getActivity(), LoginActivity.class);
                startActivity(i);
                alert.dismiss();
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



    }


    public void show_guidelines() {

        try {

            final MaterialDialog alert = new MaterialDialog(getActivity());
            View view = LayoutInflater.from(getActivity()).inflate(R.layout.answeringgudlines, null);
            alert.setView(view);
            alert.setTitle("Answering Guidelines");
            alert.setCanceledOnTouchOutside(false);

            Toolbar toolBar = (Toolbar) view.findViewById(R.id.toolBar);
            imgapp = (ImageView) view.findViewById(R.id.imgapp);
            final TextView tvguidline = (TextView) view.findViewById(R.id.tvguidline);

            toolBar.setVisibility(View.GONE);
            imgapp.setVisibility(View.GONE);

            tvguidline.setText(Html.fromHtml(getString(R.string.guidline)));

            alert.setPositiveButton("OK", new View.OnClickListener() {
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


    public void show_tips() {

        try {

            final MaterialDialog alert = new MaterialDialog(getActivity());
            View view = LayoutInflater.from(getActivity()).inflate(R.layout.tipstoearning, null);
            alert.setView(view);
            alert.setTitle("Tips for Earning");
            alert.setCanceledOnTouchOutside(false);

            Toolbar toolBar = (Toolbar) view.findViewById(R.id.toolBar);
            imgapp = (ImageView) view.findViewById(R.id.imgapp);
            tvtips1 = (TextView) view.findViewById(R.id.tvtips1);
            tvtips2 = (TextView) view.findViewById(R.id.tvtips2);
            tvtips3 = (TextView) view.findViewById(R.id.tvtips3);

            toolBar.setVisibility(View.GONE);
            imgapp.setVisibility(View.GONE);

            tvtips1.setText(Html.fromHtml(getString(R.string.tips1)));
            tvtips2.setText(Html.fromHtml(getString(R.string.tips2)));
            tvtips3.setText(Html.fromHtml(getString(R.string.tips3)));

            alert.setPositiveButton("OK", new View.OnClickListener() {
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


    private class JSON_canianswer extends AsyncTask<String, Void, Boolean> {

        AlertDialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            dialog = new ProgressDialog(getActivity());
            dialog.setTitle("Checking Query Status..Please wait");
            dialog.show();
            dialog.setCancelable(false);
        }

        @Override
        protected Boolean doInBackground(String... urls) {
            try {
                str_response = new JSONParser().getJSONString(urls[0]);
                System.out.println("str_response--------------" + str_response);
                return true;
            } catch (Exception e) {
                e.printStackTrace();
            }
            return false;
        }

        protected void onPostExecute(Boolean result) {

            try {

                jsonobj_canisnaswer = new JSONObject(str_response);


                //------------ Google firebase Analitics--------------------
                Model.mFirebaseAnalytics = FirebaseAnalytics.getInstance(getActivity());
                Bundle params = new Bundle();
                params.putString("Details", str_response);
                Model.mFirebaseAnalytics.logEvent("Open_Query", params);
                //------------ Google firebase Analitics--------------------


                if (jsonobj_canisnaswer.has("token_status")) {
                    String token_status = jsonobj_canisnaswer.getString("token_status");

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

                } else {

                    answering_status = jsonobj_canisnaswer.getString("status");

                    if ((answering_status).equals("0")) {
                        Toast.makeText(getActivity(), "Sorry! Another doctor has already picked this query.", Toast.LENGTH_LONG).show();
                    } else {
                        System.out.println("Success----");

                        Intent intent = new Intent(getActivity(), NewQueryViewActivity.class);
                        intent.putExtra("followupcode", followcode_text);
                        intent.putExtra("pat_location", pat_location);
                        intent.putExtra("str_price", str_price);
                        intent.putExtra("qtype", "new_query");
                        intent.putExtra("finisher", new android.os.ResultReceiver(null) {
                            @Override
                            protected void onReceiveResult(int resultCode, Bundle resultData) {
                                getActivity().finish();
                            }
                        });
                        startActivityForResult(intent, 1);
                    }

                    dialog.cancel();
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    public void server_err() {

        final MaterialDialog alert = new MaterialDialog(getActivity());
        //alert.setTitle("Error..!");
        alert.setMessage("Hi, there seems to be an issue with this page. The support team has been notified. Please try reloading this page after sometime");
        alert.setCanceledOnTouchOutside(false);
        alert.setPositiveButton("OK", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alert.dismiss();
                getActivity().finish();
            }
        });
        alert.show();
    }

    private class JSON_Feedback extends AsyncTask<JSONObject, Void, Boolean> {

        @Override
        protected Boolean doInBackground(JSONObject... urls) {
            try {
                json_response_obj = new JSONParser().JSON_POST(urls[0], "feedback");
                return true;
            } catch (Exception e) {
                e.printStackTrace();
            }

            return false;
        }
    }


    class JSON_POD_list extends AsyncTask<JSONObject, Void, Boolean> {

        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            dialog = new ProgressDialog(getActivity());
            dialog.setMessage("Please wait");
            dialog.show();
            dialog.setCancelable(false);
        }

        @Override
        protected Boolean doInBackground(JSONObject... urls) {

            try {

                JSONParser jParser = new JSONParser();
                str_response = jParser.JSON_POST_ARRAY(urls[0], "pod_list");

                System.out.println("POD_urls[0]----------" + urls[0]);
                System.out.println("POD_str_response----------" + str_response);

                return true;
            } catch (Exception e) {
                e.printStackTrace();
            }
            return false;
        }

        protected void onPostExecute(Boolean result) {

            try {

                if (str_response != null && !str_response.isEmpty() && !str_response.equals("null") && !str_response.equals("")) {

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

                            objItem.setFupcode(jsonobj1.getString("fcode"));
                            objItem.seAskedName(jsonobj1.getString("askedby_name"));
                            objItem.setPubdate(jsonobj1.getString("asked_at"));
                            objItem.setTitle(jsonobj1.getString("query"));
                            //---------------------------------------------------------
                            if (jsonobj1.has("speciality")) {
                                objItem.setSpeciality(jsonobj1.getString("speciality"));
                            } else {
                                objItem.setSpeciality("Internal Medicine");
                            }
                            //---------------------------------------------------------
                            objItem.setGeo(jsonobj1.getString("str_user_geo"));
                            objItem.setLabel(jsonobj1.getString("q_type_label"));
                            // -----------------Info------------------------------------------
                            try {
                                if (jsonobj1.has("info")) {
                                    String ptext = jsonobj1.getString("info");
                                    System.out.println("ptext-------------" + ptext);

                                    if (ptext != null && !ptext.isEmpty() && !ptext.equals("null") && !ptext.equals("")) {
                                        objItem.setInfo(ptext);
                                    } else {
                                        objItem.setInfo("");
                                    }
                                } else {
                                    objItem.setInfo("");
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            //------------------Info-----------------------------------------

                            objItem.setId("");
                            objItem.setPrice("");

                            if (jsonobj1.has("is_hline")) {
                                objItem.setHline(jsonobj1.getString("is_hline"));
                            } else {
                                objItem.setHline("");
                            }

                            Model.is_priority_text = "nil";
                            objItem.setPri("nil");
                            //-----------------------------------------------------------

                            try {
                                if (jsonobj1.has("is_priority")) {
                                    String ptext = jsonobj1.getString("is_priority");
                                    if ((ptext).equals("1")) {
                                        objItem.setPri("Priority Query");
                                        Model.is_priority_text = "Priority Query";
                                        System.out.println("This is is_priority Flag");
                                    }
                                } else {
                                    Model.is_priority_text = "";
                                    System.out.println("There is no is_priority Flag");
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            //-----------------------------------------------------------


                            //-----------------------------------------------------------
                            try {
                                if (jsonobj1.has("do_not_answer")) {
                                    String ptext = jsonobj1.getString("do_not_answer");
                                    if ((ptext).equals("1")) {
                                        objItem.setPri("Please answer the priority queries first, before answering this");
                                        Model.is_priority_text = "Please answer the priority queries first, before answering this";
                                    }
                                } else {
                                    Model.is_priority_text = "";
                                    System.out.println("There is no do_not_answer Flag");
                                }

/*                            if ((jsonobj1.getString("do_not_answer")).equals(JSONObject.NULL)) {
                                System.out.println("NULL");
                            } else {
                                System.out.println("NOT NULL");
                                String ptext = jsonobj1.getString("do_not_answer");
                                if ((ptext).equals("1")) {
                                    objItem.setPri("Please answer the priority queries first, before answering this");
                                    Model.is_priority_text = "Please answer the priority queries first, before answering this";
                                }
                            }*/
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            //-----------------------------------------------------------

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

                        //----------------------------------------------------------
                        long elapsedTime = System.currentTimeMillis() - startTime;
                        System.out.println("Total Elapsed of AsyncTask (in milliseconds)--- " + elapsedTime);
                        System.out.println("arrayOfList.size()---------" + arrayOfList.size());
                        //----------------------------------------------------------
                    }

                } else {

                    try {

                        json_err_feedback = new JSONObject();
                        json_err_feedback.put("user_id", (Model.id));
                        String err_dat = Model.id + "/" + Model.name + "/new_queries_open_page/android-app";
                        json_err_feedback.put("text", err_dat);
                        json_err_feedback.put("str_response", str_response);
                        System.out.println("json_err_feedback---" + json_err_feedback.toString());

                        new JSON_Feedback().execute(json_err_feedback);


                        //------------ Google firebase Analitics--------------------
                        Model.mFirebaseAnalytics = FirebaseAnalytics.getInstance(getActivity());
                        Bundle params = new Bundle();
                        params.putString("Details", str_response);
                        Model.mFirebaseAnalytics.logEvent("Not_Respond_Issue", params);
                        //------------ Google firebase Analitics--------------------


                        //----------- Flurry -------------------------------------------------
                        Map<String, String> articleParams = new HashMap<String, String>();
                        articleParams.put("android.doc.Not_Respond_Issue:", json_err_feedback.toString());
                        FlurryAgent.logEvent("android.doc.Not_Respond_Issue", articleParams);
                        //----------- Flurry -------------------------------------------------

                        server_err();

                    } catch (Exception e2) {
                        e2.printStackTrace();
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

            dialog.cancel();

        }
    }


    @Override
    public void onResume() {
        super.onResume();

        if ((Model.query_launch) != null && !(Model.query_launch).isEmpty() && !(Model.query_launch).equals("null") && !(Model.query_launch).equals("")) {
            if ((Model.query_launch).equals("pod_not_available")) {
                Model.query_launch = "";
                full_process();
            }
        }

    }
}
