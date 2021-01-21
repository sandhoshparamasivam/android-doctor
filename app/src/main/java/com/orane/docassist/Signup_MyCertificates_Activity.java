package com.orane.docassist;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.orane.docassist.Model.Item;
import com.orane.docassist.Model.Model;
import com.orane.docassist.Network.JSONParser;
import com.orane.docassist.adapter.MyCertificateAdapter;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import me.drakeet.materialdialog.MaterialDialog;


public class Signup_MyCertificates_Activity extends AppCompatActivity {

    Toolbar toolbar;
    SwipeRefreshLayout mSwipeRefreshLayout;
    ListView listView;
    LinearLayout nolayout, netcheck_layout;
    RelativeLayout LinearLayout1;
    String str_response, params;
    Integer tot_edu;
    List<Item> arrayOfList;
    List<Item> listArray;
    MyCertificateAdapter objAdapter;
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
        setContentView(R.layout.signup_my_certificates);


       /* //================ Shared Pref ======================
        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        Model.token = sharedpreferences.getString(token, "");
        System.out.println("Model.token----------------------" + Model.token);
        //================ Shared Pref ======================*/


        //--------------------------------------------------------
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setTitle("");

            TextView mTitle = toolbar.findViewById(R.id.toolbar_title);
            Typeface khandBold = Typeface.createFromAsset(getApplicationContext().getAssets(), Model.font_name_bold);
            mTitle.setTypeface(khandBold);
        }
        //--------------------------------------------------------

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.app_color));
        }

        fab = findViewById(R.id.fab);
        progressBar_bottom = findViewById(R.id.progressBar_bottom);
        progressBar = findViewById(R.id.progressBar);
        LinearLayout1 = findViewById(R.id.LinearLayout1);
        netcheck_layout = findViewById(R.id.netcheck_layout);
        nolayout = findViewById(R.id.nolayout);
        mSwipeRefreshLayout = findViewById(R.id.mSwipeRefreshLayout);
        listView = findViewById(R.id.listview);


        full_process();


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
/*
                try {
                    System.out.println("position-----" + position);

                    TextView tv_clinic_name = view.findViewById(R.id.tv_clinic_name);
                    // TextView tv_clinic_street = (TextView) view.findViewById(R.id.tv_clinic_street);
                    TextView tv_clinic_geo = view.findViewById(R.id.tv_clinic_geo);
                    TextView tvid = view.findViewById(R.id.tvid);

                    String cid = tvid.getText().toString();
                    String cname = tv_clinic_name.getText().toString();
                    String cgeo = tv_clinic_geo.getText().toString();


                    System.out.println("clinic_id------" + cid);
                    System.out.println("clinic_name------" + cname);
                    // System.out.println("clinic_Street------" + tv_clinic_street.getText().toString());
                    System.out.println("clinic_geo------" + cgeo);

                    Intent intent = new Intent(Signup_MyCertificates_Activity.this, Signup_MyCertificates_AddActivity.class);
                    intent.putExtra("clinic_id", cid);
                    intent.putExtra("clinic_name", cname);
                    // intent.putExtra("clinic_street", tv_clinic_street.getText().toString());
                    intent.putExtra("clinic_geo", cgeo);
                    intent.putExtra("mode", "update");
                    startActivity(intent);

                } catch (Exception e) {
                    e.printStackTrace();
                }*/
            }
        });


        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                if (isInternetOn()) {

                    apply_list(Model.education_response);

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

                Intent intent = new Intent(Signup_MyCertificates_Activity.this, Signup_MyCertificates_AddActivity.class);
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
                System.out.println("Model.education_response-------------------" + Model.education_response);

                if ((Model.education_response) != null && !(Model.education_response).isEmpty() && !(Model.education_response).equals("null") && !(Model.education_response).equals("")) {
                    apply_list(Model.education_response);
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

        } else {
            progressBar_bottom.setVisibility(View.GONE);
            progressBar.setVisibility(View.GONE);
            mSwipeRefreshLayout.setVisibility(View.GONE);
            nolayout.setVisibility(View.GONE);
            netcheck_layout.setVisibility(View.VISIBLE);
        }
    }


    public void setAdapterToListview() {

        try {
            objAdapter = new MyCertificateAdapter(Signup_MyCertificates_Activity.this, R.layout.signup_my_certificates_row, arrayOfList);
            listView.setAdapter(objAdapter);

            System.out.println("Adapter_set---------");
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
        //getMenuInflater().inflate(R.menu.myclinic_menu, menu);
        /*
        1. Unavukadai
        2. Vanga santhipom
        3. Namma Ooru samaiyal
        4.
        * */
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


    @Override
    public void onResume() {
        super.onResume();

        if ((Model.query_launch) != null && !(Model.query_launch).isEmpty() && !(Model.query_launch).equals("null") && !(Model.query_launch).equals("")) {
            if ((Model.query_launch).equals("add_education")) {
                System.out.println("Resume Model.education_response----------------" + Model.education_response);

                //String str_aaray ="[{\"eduId\":\"4452\",\"education\":\"mvvs\",\"educationYear\":\"2017\",\"college\":\"fjbfd\"}]";

                apply_list(Model.education_response);

            }
        }
    }

    public void OnClose_Click(View v) {
        ImageView img1 = v.findViewById(R.id.img_close);

        System.out.println("Education is removed...");
    }


    public void apply_list(String str_response) {
        try {

            JSONArray jsonarr = new JSONArray(str_response);
            listArray = new ArrayList<Item>();

            if (str_response.length() > 5) {

                tot_edu = jsonarr.length();

                for (int i = 0; i < jsonarr.length(); i++) {


                    JSONObject jsonobj1 = jsonarr.getJSONObject(i);

                    System.out.println("MyEducation jsonobj1-----------" + jsonobj1.toString());

                    objItem = new Item();
                    objItem.setHlid(jsonobj1.getString("eduId"));
                    objItem.setLocation(jsonobj1.getString("education"));
                    objItem.setCountry(jsonobj1.getString("educationYear"));
                    objItem.setZip(jsonobj1.getString("college"));

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

                    setAdapterToListview();

                    progressBar_bottom.setVisibility(View.GONE);
                    progressBar.setVisibility(View.GONE);
                    mSwipeRefreshLayout.setVisibility(View.VISIBLE);
                    nolayout.setVisibility(View.GONE);
                    netcheck_layout.setVisibility(View.GONE);

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


    public void remove_education(final View btn) {

        if (tot_edu > 1) {
            try {
                final MaterialDialog alert = new MaterialDialog(Signup_MyCertificates_Activity.this);
                alert.setTitle("Confirmation");
                alert.setMessage("Are you sure, you want to delete this?");
                alert.setCanceledOnTouchOutside(false);
                alert.setPositiveButton("Yes", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        View parent = (View) btn.getParent();
                        TextView tvid = parent.findViewById(R.id.tvid);
                        String edu_id = tvid.getText().toString();

                        System.out.println("edu_id-----------" + edu_id);

                        //---------------------------------------------------------
                        String remove_url = Model.BASE_URL + "sapp/insertOrUpdateDocEducation?id=" + edu_id + "&user_id=" + Model.id + "&token=" + Model.token + "&isDelete=true";
                        System.out.println("remove_url-------------" + remove_url);
                        new JSON_remove_edu().execute(remove_url);
                        //---------------------------------------------------------

                        alert.dismiss();
                    }
                });

                alert.setNegativeButton("No", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alert.dismiss();
                    }
                });

                alert.show();

            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {


            final MaterialDialog alert = new MaterialDialog(Signup_MyCertificates_Activity.this);
            alert.setTitle("Alert");
            alert.setMessage("Sorry, You cannot delete this last item");
            alert.setCanceledOnTouchOutside(false);

            alert.setNegativeButton("OK", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    alert.dismiss();
                }
            });

            alert.show();


        }


    }


    private class JSON_remove_edu extends AsyncTask<String, Void, Boolean> {

        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            dialog = new ProgressDialog(Signup_MyCertificates_Activity.this);
            dialog.setMessage("Please wait");
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

                JSONObject jsonobj = new JSONObject(str_response);

                String status_val = jsonobj.getString("status");

                if (status_val.equals("1")) {

                    String education_text = jsonobj.getString("education");
                    Model.education_response = education_text;

                    apply_list(Model.education_response);
                } else {
                    String err_val = jsonobj.getString("err");
                }

                dialog.cancel();

            } catch (Exception e) {
                e.printStackTrace();
            }
            //dialog.cancel();

        }
    }


}
