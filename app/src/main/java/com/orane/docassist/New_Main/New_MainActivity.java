package com.orane.docassist.New_Main;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.orane.docassist.Home.QueriesFragment;
import com.orane.docassist.LoginActivity;
import com.orane.docassist.Model.Model;
import com.orane.docassist.Network.JSONParser;
import com.orane.docassist.R;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.annotations.NonNull;
import me.drakeet.materialdialog.MaterialDialog;

public class New_MainActivity extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;

    //viewPager
    private ViewPager viewPager;

    //Fragments
    HomeFragment homeFragment;
    QueriesFragment queriesFragment;
    CasesFragment casesFragment;
    MyAccountFragment myaccountFragment;
    String str_response;
    SettingsFragment settingsFragment;
    MenuItem prevMenuItem;

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
    public static final String sensappt_home_flag = "sensappt_home_flag_key";
    public static final String update_status = "update_status_key";
    public static final String update_alert_time = "update_alert_time_key";
    public static final String noti_status = "noti_status_key";
    public static final String noti_sound = "noti_sound_key";
    public static final String do_like_pulse = "do_like_pulse_key";
    public static final String token = "token_key";

    String Log_Status, uname, docname, pass;
    boolean doubleBackToExitPressedOnce = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_activity_main);

        //================ Shared Pref ======================
        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        Log_Status = sharedpreferences.getString(Login_Status, "");
        uname = sharedpreferences.getString(user_name, "");
        docname = sharedpreferences.getString(Name, "");
        Model.name = sharedpreferences.getString(Name, "");
        pass = sharedpreferences.getString(password, "");
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
        Model.like_pulse = sharedpreferences.getString(do_like_pulse, "");
        Model.sensappt_home_flag = sharedpreferences.getString(sensappt_home_flag, "yes");
        Model.token = sharedpreferences.getString(token, "");
        //============================================================

/*      //============================================================
        SharedPreferences.Editor editor = sharedpreferences.edit();
        -------------
        editor.putString(id, "524736");
        editor.apply();
        Model.id = "524736";
        //============================================================*/


        try {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                getWindow().setStatusBarColor(getResources().getColor(R.color.light_blue_600));
            }

            if ((Model.id) != null && !(Model.id).isEmpty() && !(Model.id).equals("null") && !(Model.id).equals("")) {
                System.out.println("Model.id----Ok-----");
            } else {
                force_logout();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }


        System.out.println("(Model.device_token)-----------------" + (Model.device_token));

        if ((Model.device_token) != null && !(Model.device_token).isEmpty() && !(Model.device_token).equals("null") && !(Model.device_token).equals("")) {

            //-------------------------------------------------------------------
            String gcm_url = Model.BASE_URL + "sapp/updateDeviceRegId?reg_id=" + Model.device_token + "&user_id=" + (Model.id) + "&v=" + Model.app_slno + "&token=" + Model.token;
            System.out.println("FCM_url---------" + gcm_url);
            new JSON_GCM_Regid().execute(gcm_url);
            //-------------------------------------------------------------------
        }

        viewPager = findViewById(R.id.viewpager);
        bottomNavigationView = findViewById(R.id.navigation);

        bottomNavigationView.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.navigation_home:
                                viewPager.setCurrentItem(0);
                                break;
                            case R.id.navigation_cases:
                                viewPager.setCurrentItem(1);
                                break;
                            case R.id.navigation_myaccount:
                                viewPager.setCurrentItem(2);
                                break;
                            case R.id.navigation_settings:
                                viewPager.setCurrentItem(3);
                                break;
                        }

                        return false;
                    }
                });

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                if (prevMenuItem != null) {
                    prevMenuItem.setChecked(false);
                } else {
                    bottomNavigationView.getMenu().getItem(0).setChecked(false);
                }
                Log.d("page", "" + position);

                bottomNavigationView.getMenu().getItem(position).setChecked(true);
                prevMenuItem = bottomNavigationView.getMenu().getItem(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        setupViewPager(viewPager);
    }

    private void setupViewPager(ViewPager viewPager) {

        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        homeFragment = new HomeFragment();
        casesFragment = new CasesFragment();
        myaccountFragment = new MyAccountFragment();
        settingsFragment = new SettingsFragment();

        adapter.addFragment(homeFragment);
        adapter.addFragment(casesFragment);
        adapter.addFragment(myaccountFragment);
        adapter.addFragment(settingsFragment);

        viewPager.setAdapter(adapter);
    }

    private class ViewPagerAdapter extends FragmentStatePagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment) {
            mFragmentList.add(fragment);
        }

    }

    public void force_logout() {

        final MaterialDialog alert = new MaterialDialog(New_MainActivity.this);
        alert.setTitle("Please login again..!");
        alert.setMessage("Something went wrong. Please Logout and Login again to continue");
        alert.setCanceledOnTouchOutside(false);

        alert.setPositiveButton("OK", new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //------------ Google firebase Analitics--------------------
                Model.mFirebaseAnalytics = FirebaseAnalytics.getInstance(New_MainActivity.this);
                Bundle params = new Bundle();
                params.putString("User", Model.id + "/" + Model.name);
                Model.mFirebaseAnalytics.logEvent("Home_Logout", params);
                //------------ Google firebase Analitics--------------------

                //============================================================
                SharedPreferences.Editor editor = sharedpreferences.edit();
                editor.putString(Login_Status, "0");
                editor.apply();
                //============================================================

                finishAffinity();
                Intent i = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(i);
                alert.dismiss();
                finish();
            }
        });
        alert.show();
    }

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(getApplicationContext(), "Press back again to exit", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                doubleBackToExitPressedOnce = false;
            }
        }, 2000);

    }


    private class JSON_GCM_Regid extends AsyncTask<String, Void, Boolean> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected Boolean doInBackground(String... urls) {

            try {
                str_response = new JSONParser().getJSONString(urls[0]);
                System.out.println("str_response--------------" + str_response);
                return true;
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("Exception--GCM Response Json----" + e.toString());
            }

            return false;
        }

        protected void onPostExecute(Boolean result) {
            System.out.println("str_response--------------" + str_response);
        }
    }


}