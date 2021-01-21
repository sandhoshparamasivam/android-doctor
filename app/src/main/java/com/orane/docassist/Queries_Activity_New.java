package com.orane.docassist;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import android.text.Html;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.tabs.TabLayout;
import com.orane.docassist.Home.QueriesFragment;
import com.orane.docassist.Model.Model;
import com.orane.docassist.adapter.Consult_ViewPagerAdapter;
import com.orane.docassist.adapter.ViewPagerAdapter;
import com.orane.docassist.fragment.Consult_HistoryFragment;
import com.orane.docassist.fragment.New_queries_Fragment;
import com.orane.docassist.fragment.Unconfirmed_Consult_Fragment;
import com.orane.docassist.fragment.Upcoming_Consult_Fragment;

import me.drakeet.materialdialog.MaterialDialog;


public class Queries_Activity_New extends AppCompatActivity {

    private TabLayout tabLayout;
    private ViewPager viewPager;

    New_queries_Fragment new_queries_fragment;
    QueriesFragment queriesFragment;

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
        setContentView(R.layout.queries_tabview_new);

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
        getMenuInflater().inflate(R.menu.new_query_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == android.R.id.home) {
            finish();
            return true;
        }
        if (id == R.id.nav_wallet) {
            Intent i = new Intent(Queries_Activity_New.this, MyWalletActivity.class);
            startActivity(i);
            return true;
        }
        if (id == R.id.nav_ans_improve) {
            show_tips();
            return true;
        }
        if (id == R.id.nav_ansguide) {
            show_guidelines();
            return true;
        }
        if (id == R.id.ratenow) {
            String url = "https://play.google.com/store/apps/details?id=" + getPackageName();
            Intent i = new Intent(Intent.ACTION_VIEW);
            i.setData(Uri.parse(url));
            startActivity(i);
            return true;
        }
        if (id == R.id.csupport) {
            Intent i = new Intent(Queries_Activity_New.this, CommonActivity.class);
            i.putExtra("type", "support");
            startActivity(i);
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

        new_queries_fragment = new New_queries_Fragment();
        queriesFragment = new QueriesFragment();

        adapter.addFragment(new_queries_fragment, "New Queries");
        adapter.addFragment(queriesFragment, "Answered");

        viewPager.setAdapter(adapter);

        // --------------------- Initializing viewPager -----------------------------

    }

    public void show_tips() {

        try {

            final MaterialDialog alert = new MaterialDialog(Queries_Activity_New.this);
            View view = LayoutInflater.from(Queries_Activity_New.this).inflate(R.layout.tipstoearning, null);
            alert.setView(view);
            alert.setTitle("Tips for Earning");
            alert.setCanceledOnTouchOutside(false);

            Toolbar toolBar = (Toolbar) view.findViewById(R.id.toolBar);
            ImageView imgapp = (ImageView) view.findViewById(R.id.imgapp);
            TextView tvtips1 = (TextView) view.findViewById(R.id.tvtips1);
            TextView tvtips2 = (TextView) view.findViewById(R.id.tvtips2);
            TextView tvtips3 = (TextView) view.findViewById(R.id.tvtips3);

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

    public void show_guidelines() {

        try {
            final MaterialDialog alert = new MaterialDialog(Queries_Activity_New.this);
            View view = LayoutInflater.from(Queries_Activity_New.this).inflate(R.layout.answeringgudlines, null);
            alert.setView(view);
            alert.setTitle("Answering Guidelines");
            alert.setCanceledOnTouchOutside(false);

            Toolbar toolBar = (Toolbar) view.findViewById(R.id.toolBar);
            ImageView imgapp = (ImageView) view.findViewById(R.id.imgapp);
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


}
