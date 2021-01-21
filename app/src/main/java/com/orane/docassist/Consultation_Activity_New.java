package com.orane.docassist;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;

import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.google.android.material.tabs.TabLayout;
import com.orane.docassist.Model.Model;
import com.orane.docassist.R;
import com.orane.docassist.adapter.Consult_ViewPagerAdapter;
import com.orane.docassist.adapter.ViewPagerAdapter;
import com.orane.docassist.fragment.Consult_HistoryFragment;
import com.orane.docassist.fragment.Unconfirmed_Consult_Fragment;
import com.orane.docassist.fragment.Upcoming_Consult_Fragment;


public class Consultation_Activity_New extends AppCompatActivity {

    private TabLayout tabLayout;
    private ViewPager viewPager;

    Upcoming_Consult_Fragment upcomingFragment;
    Unconfirmed_Consult_Fragment unconfirmedFragment;
    Consult_HistoryFragment historyFragment;

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
        setContentView(R.layout.consultation_new);

        //------- Object Creation ----------------------------------
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setTitle("");
        }

        mTitle = (TextView) toolbar.findViewById(R.id.toolbar_title);
        Typeface khandBold = Typeface.createFromAsset(getApplicationContext().getAssets(), Model.font_name_bold);
        mTitle.setTypeface(khandBold);

        //------- Object Creation ----------------------------------

        //================ Shared Pref ======================
        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        Model.token = sharedpreferences.getString(token, "");
        System.out.println("Model.token----------------------" + Model.token);
        //================ Shared Pref ======================

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.app_color));
        }

        Setup_viewPager();

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
        viewPager.setOffscreenPageLimit(4);
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

        upcomingFragment = new Upcoming_Consult_Fragment();
        unconfirmedFragment = new Unconfirmed_Consult_Fragment();
        historyFragment = new Consult_HistoryFragment();

        adapter.addFragment(unconfirmedFragment, "Bookings");
        adapter.addFragment(upcomingFragment, "Upcoming");
        adapter.addFragment(historyFragment, "History");

        viewPager.setAdapter(adapter);

        // --------------------- Initializing viewPager -----------------------------

    }
}
