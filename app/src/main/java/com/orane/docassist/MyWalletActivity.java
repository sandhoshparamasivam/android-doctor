package com.orane.docassist;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.orane.docassist.Model.Model;
import com.orane.docassist.Network.Detector;
import com.orane.docassist.Network.JSONParser;
import com.orane.docassist.cache_files.Wallet10transFile;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;


public class MyWalletActivity extends AppCompatActivity {

    public static final String MyPREFERENCES = "MyPrefs";
    public static final String Login_Status = "Login_Status_key";
    public static final String user_name = "user_name_key";
    public static final String password = "password_key";
    public static final String Name = "Name_key";
    public static final String wallet_balance = "wallet_balance_key";
    public String payout_bal, currentDateandTime;
    public String push_title, push_type, push_msg, Log_Status;
    public JSONObject jsonobj, json, json_wallet_tran;
    public long startTime;
    public String url, desc_val, amt_val, type_val, date_val, id_val, currency_symbol, wallet_bal, po_url = "mobileajax/payouts", wb_url = "mobileajax/listearning";
    public String wallet_balance_text, log_txt, str_earn_txt, itemlen_txt, sub_url = "mobileajax/listearning";
    SharedPreferences sharedpreferences;
    ArrayAdapter<String> mAdapter;
    RelativeLayout mContainer;
    ImageView mImageView;
    TextView tv_date, tv_bal, tv_desc, tv_price;
    View vi;
    LinearLayout parent_layout;
    TextView wcursymbol;
    Button btn_trans, btn_payouts;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mywalletnew);


        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);

        //------------ Object Creations -------------------------------
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setTitle("");

            TextView mTitle = toolbar.findViewById(R.id.toolbar_title);
            Typeface khandBold = Typeface.createFromAsset(getApplicationContext().getAssets(), Model.font_name_bold);
            mTitle.setTypeface(khandBold);
        }

        //------------ Object Creations -------------------------------
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.app_color));
        }

        SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy HH:mm a");
        currentDateandTime = sdf.format(new Date());

        //--------- initialize -------------------------------------------

        parent_layout = findViewById(R.id.parent_layout);
        wcursymbol = findViewById(R.id.wcursymbol);
        tv_bal = findViewById(R.id.tv_bal);
        btn_trans = findViewById(R.id.btn_trans);
        btn_payouts = findViewById(R.id.btn_payouts);


        //-------------- Animation ---------------------------------
        Animation animSlideDown1 = AnimationUtils.loadAnimation(MyWalletActivity.this, R.anim.slide_right_to_left1);
        animSlideDown1.setStartOffset(100);
        btn_trans.startAnimation(animSlideDown1);

        Animation animSlideDown2 = AnimationUtils.loadAnimation(MyWalletActivity.this, R.anim.bounce2);
        animSlideDown2.setStartOffset(100);
        btn_payouts.startAnimation(animSlideDown2);
        //-------------- Animation ---------------------------------


        //--------------------- Font ----------------------------------
        Typeface khand = Typeface.createFromAsset(getAssets(), Model.font_name);
        //((TextView) toolbar.findViewById(R.id.tv_walletbal)).setTypeface(khand);
        tv_bal.setTypeface(khand);
        btn_trans.setTypeface(khand);
        btn_payouts.setTypeface(khand);
        //--------------------- Font ----------------------------------

        //--------- initialize -------------------------------------------

        /*if (new Detector().isTablet(getApplicationContext())) {
            tv_bal.setTextSize(35);
        }*/

        full_process();

        btn_trans.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(MyWalletActivity.this, WalletTransactions.class);
                startActivity(i);
                overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);

                //------------ Google firebase Analitics--------------------
                Model.mFirebaseAnalytics = FirebaseAnalytics.getInstance(getApplicationContext());
                Bundle params = new Bundle();
                params.putString("User", Model.id + "/" + Model.name);
                Model.mFirebaseAnalytics.logEvent("Wallet_Transaction", params);
                //------------ Google firebase Analitics--------------------
            }
        });


        btn_payouts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(MyWalletActivity.this, PayoutTransactions.class);
                startActivity(i);
                overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);


                //------------ Google firebase Analitics--------------------
                Model.mFirebaseAnalytics = FirebaseAnalytics.getInstance(getApplicationContext());
                Bundle params = new Bundle();
                params.putString("User", Model.id + "/" + Model.name);
                Model.mFirebaseAnalytics.logEvent("Payout_Transaction", params);
                //------------ Google firebase Analitics--------------------

            }
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // getMenuInflater().inflate(R.menu.wallet_menu, menu);
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

        try {

            //--------------- Show Wallet Amount --------------------------
            json = new JSONObject();
            json.put("user_id", (Model.id));
            json.put("browser_country", (Model.browser_country));

            System.out.println("json------:" + json.toString());

            new JSON_WalletBal().execute(json);
            //--------------- Show Wallet Amount --------------------------

            //-------- Transactions -------------
            String url = Model.BASE_URL + "sapp/listearning?user_id=" + (Model.id) + "&browser_country=" + (Model.browser_country) + "&page=1&token=" + Model.token;
            System.out.println("url------" + url);
            new MyTask().execute(url);
            //-------- Transactions -------------


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private class MyTask extends AsyncTask<String, Void, Boolean> {

        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            dialog = new ProgressDialog(MyWalletActivity.this);
            dialog.setMessage("Loading...Please wait.");
            dialog.show();
            dialog.setCancelable(false);
        }

        @Override
        protected Boolean doInBackground(String... urls) {

            try {
                JSONParser jParser = new JSONParser();
                json_wallet_tran = jParser.getJSON_URL(urls[0]);

                System.out.println("json_wallet_tran---------------" + json_wallet_tran.toString());

                Wallet10transFile.saveData(getApplicationContext(), ((Model.wallettran).toString()));
                //System.out.println("json_wallet_tran-----" + json_wallet_tran.toString());

                return true;

            } catch (Exception e) {
                e.printStackTrace();
            }
            return false;
        }

        protected void onPostExecute(Boolean result) {

            try {
                log_txt = json_wallet_tran.getString("log");
                str_earn_txt = json_wallet_tran.getString("str_earning");
                itemlen_txt = json_wallet_tran.getString("itemlen");

                JSONArray jsonarray = new JSONArray(log_txt);

                for (int i = 0; i < jsonarray.length(); i++) {
                    JSONObject jsonobj_logs = jsonarray.getJSONObject(i);
                    id_val = jsonobj_logs.getString("id");
                    date_val = jsonobj_logs.getString("datetime");
                    type_val = jsonobj_logs.getString("type");
                    amt_val = jsonobj_logs.getString("amount");
                    desc_val = jsonobj_logs.getString("description");

                    vi = getLayoutInflater().inflate(R.layout.last10_trans_row, null);
                    tv_date = vi.findViewById(R.id.tv_date);
                    tv_desc = vi.findViewById(R.id.tv_desc);
                    tv_price = vi.findViewById(R.id.tv_price);

                    //---------- tab ---------------------------------------------
                    if (new Detector().isTablet(getApplicationContext())) {
                        tv_date.setTextSize(20);
                        tv_desc.setTextSize(20);
                        tv_price.setTextSize(20);
                    }
                    //---------- tab ---------------------------------------------


                    //---------------- Font -------------------------
                    Typeface khandBold = Typeface.createFromAsset(getApplicationContext().getAssets(), Model.font_name_bold);
                    Typeface khand = Typeface.createFromAsset(getApplicationContext().getAssets(), Model.font_name);
                    tv_date.setTypeface(khand);
                    tv_desc.setTypeface(khand);
                    tv_price.setTypeface(khand);
                    //---------------- Font -------------------------


                    tv_date.setText(date_val);
                    tv_desc.setText(desc_val);
                    tv_price.setText(amt_val);

                    parent_layout.addView(vi);
                }

                dialog.cancel();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    private class JSON_WalletBal extends AsyncTask<JSONObject, Void, Boolean> {

        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            dialog = new ProgressDialog(MyWalletActivity.this);
            dialog.setMessage("Loading...Please wait.");
            dialog.show();
            dialog.setCancelable(false);
        }

        @Override
        protected Boolean doInBackground(JSONObject... urls) {
            try {
                JSONParser jParser = new JSONParser();
                jsonobj = jParser.JSON_POST(urls[0], "mobileWallet");

                return true;
            } catch (Exception e) {
                e.printStackTrace();
            }

            return false;
        }

        protected void onPostExecute(Boolean result) {

            try {
                System.out.println("jsonobj---------" + jsonobj.toString());

                currency_symbol = jsonobj.getString("currency_symbol");
                wallet_bal = jsonobj.getString("wallet_balance");
                payout_bal = jsonobj.getString("payout_sum");

                System.out.println("currency_symbol---" + currency_symbol);
                System.out.println("wallet_bal---" + wallet_bal);
                System.out.println("payout_bal---" + payout_bal);

                wcursymbol.setText(currency_symbol);
                tv_bal.setText(wallet_bal);

                //============================================================
                SharedPreferences.Editor editor = sharedpreferences.edit();
                editor.putString(wallet_balance, wallet_bal);
                editor.apply();
                //============================================================

                dialog.cancel();

            } catch (Exception e) {
                e.printStackTrace();
            }

            // dialog.cancel();

        }
    }


}
