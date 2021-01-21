package com.orane.docassist;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Switch;
import android.widget.TextView;


import com.orane.docassist.Model.Item;
import com.orane.docassist.Model.Model;
import com.orane.docassist.Network.ShareIntent;
import com.orane.docassist.R;
import com.orane.docassist.adapter.HotlinePatientsRowAdapter;

import org.json.JSONObject;

import java.util.List;

public class QasesHome extends AppCompatActivity {

    Toolbar toolbar;
    ListView listView;
    LinearLayout nolayout, netcheck_layout, hl_patients_layout;
    ScrollView first_layout;
    RelativeLayout LinearLayout1;
    String params;
    List<Item> arrayOfList;
    JSONObject jsonoj_status, json_disable;
    HotlinePatientsRowAdapter objAdapter;
    ProgressBar progressBar, progressBar_bottom;
    Switch switch_hl_enable;
    public String status_val;
    TextView tvhltit,tv_subtit,tv_myqases_title,tv_qases_list_desc,tv_qases_list,tv_myqases_desc,tv_post_title,tv_post_desc,tv_invite_title,tv_invite_desc;
    LinearLayout postqases_layout, myqases_layout, invite_layout, qases_list_layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.qases_home);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.app_color));
        }

        qases_list_layout = (LinearLayout) findViewById(R.id.qases_list_layout);
        myqases_layout = (LinearLayout) findViewById(R.id.myqases_layout);
        postqases_layout = (LinearLayout) findViewById(R.id.postqases_layout);
        invite_layout = (LinearLayout) findViewById(R.id.qinvite_layout);
        tv_qases_list = (TextView) findViewById(R.id.tv_qases_list);
        tv_qases_list_desc = (TextView) findViewById(R.id.tv_qases_list_desc);
        tv_myqases_title = (TextView) findViewById(R.id.tv_myqases_title);
        tv_myqases_desc = (TextView) findViewById(R.id.tv_myqases_desc);
        tv_post_title = (TextView) findViewById(R.id.tv_post_title);
        tv_post_desc = (TextView) findViewById(R.id.tv_post_desc);
        tv_invite_title = (TextView) findViewById(R.id.tv_invite_title);
        tv_invite_desc = (TextView) findViewById(R.id.tv_invite_desc);
        tvhltit = (TextView) findViewById(R.id.tvhltit);
        tv_subtit = (TextView) findViewById(R.id.tv_subtit);


       /* //---------------------------------------------------------
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setTitle("");

            //----------------Font---------------------------------------
            TextView mTitle = (TextView) toolbar.findViewById(R.id.toolbar_title);
            Typeface khandBold = Typeface.createFromAsset(getApplicationContext().getAssets(), Model.font_name_bold);
            mTitle.setTypeface(khandBold);
            //----------------Font---------------------------------------
        }
        //---------------------------------------------------------
*/
        //-------------------------------
        Typeface font_reg = Typeface.createFromAsset(getAssets(), Model.font_name);
        Typeface font_bold = Typeface.createFromAsset(getAssets(), Model.font_name_bold);

        tvhltit.setTypeface(font_bold);
        tv_subtit.setTypeface(font_reg);
        tv_qases_list.setTypeface(font_bold);
        tv_qases_list_desc.setTypeface(font_reg);
        tv_myqases_title.setTypeface(font_bold);
        tv_myqases_desc.setTypeface(font_reg);
        tv_post_title.setTypeface(font_bold);
        tv_post_desc.setTypeface(font_reg);
        tv_invite_title.setTypeface(font_bold);
        tv_invite_desc.setTypeface(font_reg);
        //-------------------------------------------


        qases_list_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(QasesHome.this, QasesActivity.class);
                intent.putExtra("qtype", "feeds");
                startActivity(intent);
                overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);
            }
        });

        myqases_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(QasesHome.this, QasesActivity.class);
                intent.putExtra("qtype", "myfeeds");
                startActivity(intent);
                overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);
            }
        });

        postqases_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(QasesHome.this, Qases_Post1.class);
                startActivity(intent);
                overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);
            }
        });

        invite_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(QasesHome.this, InviteDoctorActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.qases_home_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == android.R.id.home) {
            finish();
            return true;
        }

        if (id == R.id.nav_qshare) {
            try {
                ShareIntent sintent = new ShareIntent();
                sintent.ShareApp(QasesHome.this, "QasesHome");
            } catch (Exception e) {
                e.printStackTrace();
            }

            return true;
        }


        return super.onOptionsItemSelected(item);
    }
}
