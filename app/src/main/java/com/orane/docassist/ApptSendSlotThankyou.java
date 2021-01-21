package com.orane.docassist;

import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import androidx.appcompat.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.orane.docassist.Model.Model;
import com.orane.docassist.R;

public class ApptSendSlotThankyou extends AppCompatActivity {

    TextView tv_close_button;
    LinearLayout query_post_layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sendappt_finish);

        tv_close_button = (TextView) findViewById(R.id.tv_close_button);
        query_post_layout = (LinearLayout) findViewById(R.id.query_post_layout);

        Typeface robo_bold = Typeface.createFromAsset(getAssets(), Model.font_name);
        tv_close_button.setTypeface(robo_bold);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.app_color));
        }


        tv_close_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                finish();

            }
        }, 10000);
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
}
