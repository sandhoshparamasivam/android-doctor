package com.orane.docassist;

import android.app.ProgressDialog;
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
import android.widget.AdapterView;
import android.widget.ImageView;
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
import com.orane.docassist.adapter.MyCertificateAdapter;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import me.drakeet.materialdialog.MaterialDialog;


public class MyCertificates_Activity extends AppCompatActivity {

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
    String list_type;
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
        setContentView(R.layout.my_certificates);



       /* //================ Shared Pref ======================
        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        Model.token = sharedpreferences.getString(token, "");
        System.out.println("Model.token----------------------" + Model.token);
        //================ Shared Pref ======================*/

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


        try {
            Intent intent = getIntent();
            list_type = intent.getStringExtra("list_type");
            System.out.println("Get list_type---" + list_type);

            if (list_type.equals("old_certificate")) {
                fab.setVisibility(View.GONE);
                getSupportActionBar().setTitle("Certificates");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        full_process();

        progressBar_bottom.setVisibility(View.GONE);
        progressBar.setVisibility(View.GONE);
        mSwipeRefreshLayout.setVisibility(View.GONE);
        nolayout.setVisibility(View.VISIBLE);
        netcheck_layout.setVisibility(View.GONE);


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


                if (!list_type.equals("old_certificate")) {
                    try {
                        System.out.println("position-----" + position);

                        TextView tv_clinic_name = (TextView) view.findViewById(R.id.tv_clinic_name);
                        TextView tv_year = (TextView) view.findViewById(R.id.tv_year);
                        TextView tv_clinic_geo = (TextView) view.findViewById(R.id.tv_clinic_geo);
                        TextView tvid = (TextView) view.findViewById(R.id.tvid);
                        TextView tv_filename = (TextView) view.findViewById(R.id.tv_filename);

                        System.out.println("clinic_id------" + tvid.getText().toString());
                        System.out.println("clinic_name------" + tv_clinic_name.getText().toString());
                        System.out.println("clinic_geo------" + tv_clinic_geo.getText().toString());
                        System.out.println("tv_year------" + tv_year.getText().toString());
                        System.out.println("tv_filename------" + tv_filename.getText().toString());

                        Intent intent = new Intent(MyCertificates_Activity.this, MyCertificates_AddActivity.class);
                        intent.putExtra("clinic_id", tvid.getText().toString());
                        intent.putExtra("clinic_name", tv_clinic_name.getText().toString());
                        intent.putExtra("clinic_geo", tv_clinic_geo.getText().toString());
                        intent.putExtra("year_val", tv_year.getText().toString());
                        intent.putExtra("attach_filename", tv_filename.getText().toString());
                        intent.putExtra("mode", "update");
                        startActivity(intent);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
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

                Intent intent = new Intent(MyCertificates_Activity.this, MyCertificates_AddActivity.class);
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
            System.out.println("arrayOfList----------------" + arrayOfList.toString());

            if (list_type.equals("old_certificate")) {

                objAdapter = new MyCertificateAdapter(MyCertificates_Activity.this, R.layout.old_certificates_row, arrayOfList);

            } else {

                objAdapter = new MyCertificateAdapter(MyCertificates_Activity.this, R.layout.my_certificates_row, arrayOfList);
            }


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
        //getMenuInflater().inflate(R.menu.myclinic_menu, menu);
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

        if ((Model.query_launch).equals("add_education")) {
            apply_list(Model.education_response);
        }
    }

    public void OnClose_Click(View v) {
        ImageView img1 = (ImageView) v.findViewById(R.id.img_close);

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

                    //---------------------------------------------
                    if (jsonobj1.has("eduId")) {
                        objItem.setHlid(jsonobj1.getString("eduId"));
                    } else if (jsonobj1.has("id")) {
                        objItem.setHlid(jsonobj1.getString("id"));
                    }
                    //---------------------------------------------

                    //---------------------------------------------
                    if (jsonobj1.has("educationYear")) {
                        objItem.setCountry(jsonobj1.getString("educationYear"));
                    } else if (jsonobj1.has("year_range")) {
                        objItem.setCountry(jsonobj1.getString("year_range"));
                    } else if (jsonobj1.has("desc")) {
                        objItem.setCountry(jsonobj1.getString("desc"));
                    }
                    //---------------------------------------------


                    if (jsonobj1.has("education")) {
                        objItem.setLocation(jsonobj1.getString("education"));
                    }


                    if (jsonobj1.has("title")) {
                        objItem.setLocation(jsonobj1.getString("title"));
                    }

                    if (jsonobj1.has("college")) {
                        objItem.setZip(jsonobj1.getString("college"));
                    }

                    if (jsonobj1.has("filename")) {
                        objItem.setFupcode(jsonobj1.getString("filename"));
                    }


                    listArray.add(objItem);

                }

                arrayOfList = listArray;

                System.out.println("arrayOfList.size() ------------ " + arrayOfList.size());


                if (null == arrayOfList || arrayOfList.size() == 0) {

                    progressBar_bottom.setVisibility(View.GONE);
                    progressBar.setVisibility(View.GONE);
                    mSwipeRefreshLayout.setVisibility(View.GONE);
                    nolayout.setVisibility(View.VISIBLE);
                    netcheck_layout.setVisibility(View.GONE);

                    System.out.println("arrayOfList---- No");

                } else {

                    progressBar_bottom.setVisibility(View.GONE);
                    progressBar.setVisibility(View.GONE);
                    mSwipeRefreshLayout.setVisibility(View.VISIBLE);
                    nolayout.setVisibility(View.GONE);
                    netcheck_layout.setVisibility(View.GONE);

                    setAdapterToListview();

                    System.out.println("arrayOfList---- Yes");
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
                final MaterialDialog alert = new MaterialDialog(MyCertificates_Activity.this);
                alert.setTitle("Confirmation");
                alert.setMessage("Are you sure, you want to delete this?");
                alert.setCanceledOnTouchOutside(false);
                alert.setPositiveButton("Yes", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        View parent = (View) btn.getParent();
                        TextView tvid = (TextView) parent.findViewById(R.id.tvid);
                        String edu_id = tvid.getText().toString();

                        System.out.println("edu_id-----------" + edu_id);

                        //---------------------------------------------------------
                        String remove_url = Model.BASE_URL + "sapp/insertOrUpdateDocEducation?os_type=android&id=" + edu_id + "&user_id=" + Model.id + "&token=" + Model.token + "&isDelete=true";
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


            final MaterialDialog alert = new MaterialDialog(MyCertificates_Activity.this);
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

            dialog = new ProgressDialog(MyCertificates_Activity.this);
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
