package com.orane.docassist;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.text.Html;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.orane.docassist.Model.Model;
import com.orane.docassist.Network.Detector;
import com.orane.docassist.New_Main.New_MainActivity;
import com.orane.docassist.R;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;


public class NotificationViewActivity extends AppCompatActivity {

    public static final String MyPREFERENCES = "MyPrefs";
    public static final String Login_Status = "Login_Status_key";
    public static final String user_name = "user_name_key";
    public static final String password = "password_key";
    public static final String Name = "Name_key";
    public static final String bcountry = "bcountry_key";
    public static final String photo_url = "photo_url";
    public static final String token = "token_key";

    SharedPreferences sharedpreferences;

    RelativeLayout mContainer;
    public String currentDateandTime;
    public String push_title, push_type, push_msg, Log_Status;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.notificationview);

        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.app_color));
        }

        SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy HH:mm a");
        currentDateandTime = sdf.format(new Date());

        Intent intent = getIntent();
        push_msg = intent.getStringExtra("push_msg");
        push_type = intent.getStringExtra("push_type");
        push_title = intent.getStringExtra("push_title");

        //================ Shared Pref ======================
        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        Model.token = sharedpreferences.getString(token, "");
        System.out.println("Model.token----------------------" + Model.token);
        //================ Shared Pref ======================

        mContainer = (RelativeLayout) findViewById(R.id.container);

        mContainer.removeAllViews();
        View v = getLayoutInflater().inflate(R.layout.notify_scrollview, mContainer, true);

        TextView tv_date = (TextView) v.findViewById(R.id.tv_date);
        TextView tv_title = (TextView) v.findViewById(R.id.tv_title);
        TextView tv_desc = (TextView) v.findViewById(R.id.tv_desc);
        TextView tv_gotohome = (TextView) v.findViewById(R.id.tv_gotohome);
        TextView tv_close = (TextView) v.findViewById(R.id.tv_close);

        tv_title.setText(Html.fromHtml(push_title));
        tv_date.setText(Html.fromHtml(currentDateandTime));
        tv_desc.setText(Html.fromHtml(push_msg));

        /*ParallaxScrollView mScrollView = (ParallaxScrollView) v.findViewById(R.id.view);
        mScrollView.setParallaxView(getLayoutInflater().inflate(R.layout.notifi_view_header, mScrollView, false));
*/

        tv_gotohome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Log_Status = sharedpreferences.getString(Login_Status, "");

                if (Log_Status.equals("1")) {

                    if (new Detector().isTablet(getApplicationContext())) {
                        Intent i = new Intent(NotificationViewActivity.this, New_MainActivity.class);
                        startActivity(i);
                        finish();
                    } else {
                        Intent i = new Intent(NotificationViewActivity.this, New_MainActivity.class);
                        startActivity(i);
                        finish();
                    }

                } else {
                    finishAffinity();
                    Intent i = new Intent(NotificationViewActivity.this, LoginActivity.class);
                    startActivity(i);
                    finish();
                }
            }
        });

        tv_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                finish();
            }
        });


    }
}
