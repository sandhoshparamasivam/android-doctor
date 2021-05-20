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
import android.view.View.OnClickListener;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.flurry.android.FlurryAgent;

import com.orane.docassist.Model.Model;
import com.orane.docassist.Network.ShareIntent;

import java.io.IOException;

import me.drakeet.materialdialog.MaterialDialog;

public class SettingsActivity extends AppCompatActivity {


    LinearLayout terms_layout, policy_layout,  reportissue_layout, rate_layout, share_layout, aredoctor_layout, pv_consult_layout, signout_layout;
    Switch switch_notisound, switch_stopnoti;

    public String noti_sound_val, stop_noti_val;

    SharedPreferences sharedpreferences;
    public static final String noti_status = "noti_status_key";
    public static final String noti_sound = "noti_sound_key";
    public static final String MyPREFERENCES = "MyPrefs";
    public static final String Login_Status = "Login_Status_key";

    Typeface font_reg, font_bold;

    RelativeLayout lytRelative;
    ImageView imgPatient;
    LinearLayout invitePatientLyt;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_fragment);


        FlurryAgent.onPageView();

        //------------ Object Creations -------------------------------
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setTitle("Settings");
        }
        //------------ Object Creations -------------------------------

        //invitePatientLyt =  findViewById(R.id.invitePatientLyt);
//        lytRelative =  findViewById(R.id.lytRelative);
//        imgPatient =  findViewById(R.id.imgPatient);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.app_color));
        }


        FlurryAgent.onPageView();


        invitePatientLyt = (LinearLayout) findViewById(R.id.invitePatientLyt);
        signout_layout = (LinearLayout) findViewById(R.id.signout_layout);
        terms_layout = (LinearLayout) findViewById(R.id.terms_layout);
        policy_layout = (LinearLayout) findViewById(R.id.policy_layout);
        reportissue_layout = (LinearLayout) findViewById(R.id.reportissue_layout);
        rate_layout = (LinearLayout) findViewById(R.id.rate_layout);
        share_layout = (LinearLayout) findViewById(R.id.share_layout);
        aredoctor_layout = (LinearLayout) findViewById(R.id.aredoctor_layout);
        pv_consult_layout = (LinearLayout) findViewById(R.id.pv_consult_layout);
        switch_notisound = (Switch) findViewById(R.id.switch_notisound);
        switch_stopnoti = (Switch) findViewById(R.id.switch_stopnoti);


        font_reg = Typeface.createFromAsset(getApplicationContext().getAssets(), Model.font_name);
        font_bold = Typeface.createFromAsset(getApplicationContext().getAssets(), Model.font_name_bold);

        ((TextView) findViewById(R.id.tv_share_tit)).setTypeface(font_bold);
        ((TextView) findViewById(R.id.tv_rate_app)).setTypeface(font_reg);
        ((TextView) findViewById(R.id.tv_share_friends)).setTypeface(font_reg);
        ((TextView) findViewById(R.id.tv_app_sett)).setTypeface(font_bold);
        ((TextView) findViewById(R.id.tv_noti_sound)).setTypeface(font_reg);
        ((TextView) findViewById(R.id.tv_noti_stat)).setTypeface(font_reg);
        ((TextView) findViewById(R.id.tv_about)).setTypeface(font_bold);
        ((TextView) findViewById(R.id.tv_terms)).setTypeface(font_reg);
        ((TextView) findViewById(R.id.tv_privatepolicy)).setTypeface(font_reg);
        ((TextView) findViewById(R.id.tv_reportissue)).setTypeface(font_reg);
        ((TextView) findViewById(R.id.tv_rupat)).setTypeface(font_reg);
        ((TextView) findViewById(R.id.tv_signout)).setTypeface(font_bold);

        //================ Initialize ======================---------------
        sharedpreferences = getApplicationContext().getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        stop_noti_val = sharedpreferences.getString(noti_status, "off");
        noti_sound_val = sharedpreferences.getString(noti_sound, "on");

        switch_stopnoti.setChecked(stop_noti_val.equals("on"));

        switch_notisound.setChecked(noti_sound_val.equals("on"));
        //================ Initialize ======================---------------


//        imgPatient.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                    Log.e("invite_Patient_layout","working");
//                    ShareIntent shareIntent = new ShareIntent();
//                try {
//                    shareIntent.ShareApp(SettingsActivity.this, "InvitePatient");
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//
//
//            }
//        });

//        lytPatient.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                    Log.e("invite_Patient_layout","working");
//
//
//
//            }
//        });


        switch_notisound.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) noti_sound_val = "on";
                else noti_sound_val = "off";

                //===============Apply Noti Settings Values=============================================
                SharedPreferences.Editor editor = sharedpreferences.edit();
                editor.putString(noti_sound, noti_sound_val);
                editor.apply();
                //===============Apply Noti Settings Values=============================================

                System.out.println("noti_sound_val-------" + noti_sound_val);

            }
        });

        switch_stopnoti.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                if (b) stop_noti_val = "on";
                else stop_noti_val = "off";

                //===============Apply Noti Settings Values=============================================
                SharedPreferences.Editor editor = sharedpreferences.edit();
                editor.putString(noti_status, stop_noti_val);
                editor.apply();
                //===============Apply Noti Settings Values=============================================

                System.out.println("stop_noti_val-------" + stop_noti_val);
            }
        });


        terms_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Intent i = new Intent(getApplicationContext(), WebViewActivity.class);
                i.putExtra("url", Model.BASE_URL + "/p/terms?nolayout=1");
                i.putExtra("type", "Terms");
                startActivity(i);

                overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);
            }
        });

        policy_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Intent i = new Intent(getApplicationContext(), WebViewActivity.class);
                i.putExtra("url", "https://www.icliniq.com/p/privacy?nolayout=1");
                i.putExtra("type", "Privacy Policy");
                startActivity(i);
                overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);
            }
        });

        reportissue_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(getApplicationContext(), CommonActivity.class);
                i.putExtra("type", "feedback");
                startActivity(i);
                overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);
            }
        });

        rate_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String url = "https://goo.gl/vu2SdY";
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);

            }
        });

        share_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                try {
                    ShareIntent sintent = new ShareIntent();
                    sintent.ShareApp(SettingsActivity.this, "MainActivity");

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });


        aredoctor_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                String url = "https://play.google.com/store/apps/details?id=com.orane.icliniq";
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
            }
        });

        pv_consult_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

/*                Intent intent = new Intent(getActivity(), Invite_doctors.class);
                startActivity(intent);*/
            }
        });

        signout_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ask_logout();
            }
        });


    }

    public void ask_logout() {

        final MaterialDialog alert = new MaterialDialog(SettingsActivity.this);
        alert.setTitle("Logout.!");
        alert.setMessage("Are you sure you want to logout?");
        alert.setCanceledOnTouchOutside(false);
        alert.setPositiveButton("Yes", new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //============================================================
                SharedPreferences.Editor editor = sharedpreferences.edit();
                editor.putString(Login_Status, "0");
                editor.apply();
                //============================================================
                finishAffinity();
                Intent intent = new Intent(SettingsActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });


        alert.setNegativeButton("No", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alert.dismiss();
            }
        });

        alert.show();
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

    public void shareApp(View view) {

        ShareIntent shareIntent = new ShareIntent();
                try {
                    shareIntent.ShareApp(SettingsActivity.this, "InvitePatient");
                } catch (IOException e) {
                    e.printStackTrace();
                }
//
    }
}
