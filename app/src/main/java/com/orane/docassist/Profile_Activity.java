package com.orane.docassist;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.google.android.material.tabs.TabLayout;
import com.orane.docassist.Model.Model;
import com.orane.docassist.Network.JSONParser;
import com.orane.docassist.adapter.Consult_ViewPagerAdapter;
import com.orane.docassist.adapter.ViewPagerAdapter;
import com.orane.docassist.fragment.AcademicFragment;
import com.orane.docassist.fragment.PersonalFragment;
import com.orane.docassist.fragment.ProffessionalFragment;

import org.json.JSONObject;


public class Profile_Activity extends AppCompatActivity {

    private TabLayout tabLayout;
    private ViewPager viewPager;

    JSONObject jsonobj_qases;
    PersonalFragment personalFragment;
    AcademicFragment academicFragment;
    ProffessionalFragment proffessionalFragment;

    private Toolbar toolbar;
    private com.orane.docassist.Home.SlidingTabLayout SlidingTabLayout;
    private Consult_ViewPagerAdapter viewPagerAdapter;

    TextView mTitle;

    SharedPreferences sharedpreferences;
    public static final String MyPREFERENCES = "MyPrefs";
    public static final String Login_Status = "Login_Status_key";
    public static final String user_name = "user_name_key";
    public static final String password = "password_key";
    public static final String Name = "Name_key";
    public static final String bcountry = "bcountry_key";
    public static final String photo_url = "photo_url";
    public static final String token = "token_key";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile);

        //------- Object Creation ----------------------------------
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setTitle("Profile");
        }


     /*   mTitle = (TextView) toolbar.findViewById(R.id.toolbar_title);
        Typeface khandBold = Typeface.createFromAsset(getApplicationContext().getAssets(), Model.font_name_bold);
        mTitle.setTypeface(khandBold);

        mTitle.setText("Profile");*/
        //------- Object Creation ----------------------------------

        //================ Shared Pref ======================
        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        Model.token = sharedpreferences.getString(token, "");
        System.out.println("Model.token----------------------" + Model.token);
        //================ Shared Pref ======================

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.app_color));
        }

        //------------- getting Qases------------------------------------
        String full_url = Model.BASE_URL + "/sapp/getDoctorProfileDet?os_type=android&user_id=" + Model.id + "&token=" + Model.token;
        System.out.println("qases_view_url------------" + full_url);
        new JSONAsyncTask().execute(full_url);
        //------------- getting Qases------------------------------------

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //getMenuInflater().inflate(R.menu.main, menu);
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

    public void Setup_viewPager() {

        // --------------------- Initializing viewPager -----------------------------
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        viewPager.setOffscreenPageLimit(3);
        tabLayout = (TabLayout) findViewById(R.id.tablayout);
        tabLayout.setupWithViewPager(viewPager);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                viewPager.setCurrentItem(position, false);

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());

        personalFragment = new PersonalFragment();
        academicFragment = new AcademicFragment();
        proffessionalFragment = new ProffessionalFragment();

        adapter.addFragment(personalFragment, "Personal");
        adapter.addFragment(academicFragment, "Academic");
        adapter.addFragment(proffessionalFragment, "Proffessional");

        viewPager.setAdapter(adapter);

        // --------------------- Initializing viewPager -----------------------------

    }


    private class JSONAsyncTask extends AsyncTask<String, Void, Boolean> {

        ProgressDialog pd;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            pd = new ProgressDialog(Profile_Activity.this);
            pd.setMessage("Loading. Please wait...");
            pd.setCancelable(true);
            pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            pd.show();

        }

        @Override
        protected Boolean doInBackground(String... urls) {

            try {
                JSONParser jParser = new JSONParser();
                jsonobj_qases = jParser.getJSONFromUrl(urls[0]);
                System.out.println("jsonobj_qases---------- " + jsonobj_qases.toString());

                return true;
            } catch (Exception e) {
                e.printStackTrace();
            }

            return false;
        }

        protected void onPostExecute(Boolean result) {

            try {

                Model.profile_str = jsonobj_qases.toString();
                System.out.println("Profile Model.profile_str----------" + Model.profile_str);

                Setup_viewPager();

                pd.dismiss();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}
