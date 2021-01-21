package com.orane.docassist;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import androidx.appcompat.widget.SwitchCompat;
import androidx.appcompat.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Switch;
import android.widget.TextView;


import com.orane.docassist.Model.Item;
import com.orane.docassist.Model.Model;
import com.orane.docassist.Network.Detector;
import com.orane.docassist.R;
import com.orane.docassist.adapter.HotlinePatientsRowAdapter;

import org.json.JSONObject;

import java.util.List;

public class SendApptHome extends AppCompatActivity {

    Toolbar toolbar;
    ListView listView;
    LinearLayout nolayout, netcheck_layout, updateprice_layout, invite_layout, hl_patients_layout;
    ScrollView first_layout;
    RelativeLayout LinearLayout1;
    String params;
    List<Item> arrayOfList;
    JSONObject jsonoj_status, json_disable;
    HotlinePatientsRowAdapter objAdapter;
    ProgressBar progressBar, progressBar_bottom;
    Switch switch_hl_enable;
    public String sensappt_home_flag_val;
    TextView tv_postatus;
    Button btn_try;
    SwitchCompat switch_show;
    TextView text1, text2, text3, text4, text5, text6, tv_tit2, tv_tit1, text6_ext;

    SharedPreferences sharedpreferences;
    public static final String MyPREFERENCES = "MyPrefs";
    public static final String Login_Status = "Login_Status_key";
    public static final String sensappt_home_flag = "sensappt_home_flag_key";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (new Detector().isTablet(getApplicationContext())) {
            setContentView(R.layout.sendappt_home);
        } else {
            setContentView(R.layout.sendappt_home);
        }

        //---------------------------------------------------------
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setTitle("Send an Appointment Slot");
        }
        //---------------------------------------------------------

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.app_color));
        }

        btn_try = (Button) findViewById(R.id.btn_try);
        switch_show = (SwitchCompat) findViewById(R.id.switch_show);
        text1 = (TextView) findViewById(R.id.text1);
        text2 = (TextView) findViewById(R.id.text2);
        text3 = (TextView) findViewById(R.id.text3);
        text4 = (TextView) findViewById(R.id.text4);
        text5 = (TextView) findViewById(R.id.text5);
        text6 = (TextView) findViewById(R.id.text6);
        tv_tit1 = (TextView) findViewById(R.id.tv_tit1);
        tv_tit2 = (TextView) findViewById(R.id.tv_tit2);
        text6_ext = (TextView) findViewById(R.id.text6_ext);


        Typeface noto_reg = Typeface.createFromAsset(getAssets(), Model.font_name);
        Typeface noto_bold = Typeface.createFromAsset(getAssets(), Model.font_name_bold);

        text1.setTypeface(noto_bold);
        //text2.setTypeface(noto_reg);
        text3.setTypeface(noto_reg);
        text4.setTypeface(noto_reg);
        text5.setTypeface(noto_reg);
        text6.setTypeface(noto_bold);
        text6.setTypeface(noto_bold);
        tv_tit1.setTypeface(noto_bold);
        tv_tit2.setTypeface(noto_reg);

        text6_ext.setTypeface(noto_bold);
        text6_ext.setPaintFlags(text6_ext.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);

        btn_try.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SendApptHome.this, SendApptSlot1.class);
                startActivity(intent);
                overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);
            }
        });


        switch_show.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (switch_show.isChecked()) {
                    System.out.println("Switch ON");
                } else {
                    System.out.println("Switch Off");
                }

            }
        });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //getMenuInflater().inflate(R.menu.ask_menu, menu);
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

}
