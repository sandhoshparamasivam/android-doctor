package com.orane.docassist;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;


import com.google.android.material.snackbar.Snackbar;
import com.orane.docassist.Model.Model;
import com.orane.docassist.R;


public class ShareToFriends extends AppCompatActivity {

    Toolbar toolbar;
    TextView tvrefcode;
    ImageView img_whatsapp, img_facebook, img_mail, img_sms,
            imageView1, img_share;
    Animation myAnimation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.share_friends);


        toolbar = (Toolbar) findViewById(R.id.toolBar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setTitle("Share to Friends");
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            //getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            getWindow().setStatusBarColor(getResources().getColor(R.color.app_color));
            //getWindow().setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION, WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }


        final Animation animTranslate = AnimationUtils.loadAnimation(
                this, R.anim.anim_translate);
        final Animation animAlpha = AnimationUtils.loadAnimation(this,
                R.anim.anim_alpha);
        final Animation animScale = AnimationUtils.loadAnimation(this,
                R.anim.anim_scale);
        final Animation animRotate = AnimationUtils.loadAnimation(
                this, R.anim.anim_rotate);
        myAnimation = AnimationUtils.loadAnimation(this,
                R.anim.myanimation);

        img_share = (ImageView) findViewById(R.id.img_share);
        img_whatsapp = (ImageView) findViewById(R.id.img_whatsapp);
        img_facebook = (ImageView) findViewById(R.id.img_facebook);

        img_share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                img_share.startAnimation(myAnimation);

                /* Intent sharingIntent = new Intent(Intent.ACTION_SEND);
                sharingIntent.setType("text/html");
                //String msg="Please Downloard iCliniq App from Google Play store, <a href=\"https://play.google.com/store/apps/details?id=com.orane.docassist&amp;hl=en\">tap here</a>";
                sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT,  Html.fromHtml(getResources().getString(R.string.share_msg)));
                startActivity(Intent.createChooser(sharingIntent,"Share using")); */

                Intent i = new Intent(Intent.ACTION_SEND);
                i.setType("text/plain");
                i.putExtra(Intent.EXTRA_SUBJECT, "iCliniq");
                String sAux = "\nPlease download iCliniq App from Google Play store\n\n";
                sAux = sAux + Uri.parse("https://play.google.com/store/apps/details?id=com.orane.docassist&hl=en");
                i.putExtra(Intent.EXTRA_TEXT, sAux);
                startActivity(Intent.createChooser(i, "choose one"));
            }
        });

        img_whatsapp.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                img_whatsapp.startAnimation(myAnimation);
                //img_whatsapp.startAnimation(animRotate);

                PackageManager pm = getPackageManager();
                try {


                    Intent waIntent = new Intent(Intent.ACTION_SEND);
                    waIntent.setType("text/plain");
                    String sAux = "\nPlease Download iCliniq App from Google Play store\n\n";
                    sAux = sAux + Uri.parse("https://play.google.com/store/apps/details?id=com.orane.docassist&hl=en");

                    //Uri.parse("https://play.google.com/store/apps/details?id=com.android.example")

                    waIntent.setPackage("com.whatsapp");
                    if (waIntent != null) {
                        waIntent.putExtra(Intent.EXTRA_TEXT, sAux);//
                        startActivity(Intent.createChooser(waIntent, "Share with"));
                    } else {

                        Snackbar snackbar = Snackbar
                                .make(v, "WhatsApp is not Installed", Snackbar.LENGTH_LONG)
                                .setAction("Ok", this);
                        snackbar.setActionTextColor(Color.WHITE);
                        View snackbarView = snackbar.getView();
                        snackbarView.setBackgroundColor(Color.GRAY);
                        TextView textView = (TextView) snackbarView.findViewById(R.id.snackbar_text);
                        textView.setTextColor(Color.WHITE);
                    }

/*

                    Intent i = new Intent(Intent.ACTION_SEND);
                    i.setType("text/plain");
                    i.putExtra(Intent.EXTRA_SUBJECT, "iCliniq");
                    String sAux = "\nPlease Downloard iCliniq App from Google Play store\n\n";
                    sAux = sAux + "https://play.google.com/store/apps/details?id=com.orane.docassist\n\n";
                    i.putExtra(Intent.EXTRA_TEXT, sAux);
                    startActivity(Intent.createChooser(i, "choose one"));

*/


                } catch (Exception e) {
                    //Toast.makeText(getApplicationContext(), "WhatsApp not Installed",Toast.LENGTH_SHORT).show();
                    Snackbar snackbar = Snackbar
                            .make(v, "WhatsApp is not Installed", Snackbar.LENGTH_LONG)
                            .setAction("Ok", this);
                    snackbar.setActionTextColor(Color.WHITE);
                    View snackbarView = snackbar.getView();
                    snackbarView.setBackgroundColor(Color.GRAY);
                    TextView textView = (TextView) snackbarView.findViewById(R.id.snackbar_text);
                    textView.setTextColor(Color.WHITE);
                    snackbar.show();

                }

            }
        });

        img_facebook.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                try {
                    img_facebook.startAnimation(myAnimation);
                    //img_facebook.startAnimation(animRotate);
                    Intent sendIntent = new Intent();
                    sendIntent.setAction(Intent.ACTION_SEND);
                    //String msg="Please Downloard iCliniq App from Google Play store, <a href=\"https://play.google.com/store/apps/details?id=com.orane.docassist&amp;hl=en\">tap here</a>";
                    String sAux = "\nPlease Download iCliniq App from Google Play store\n\n";
                    sAux = sAux + Uri.parse("https://play.google.com/store/apps/details?id=com.orane.docassist&hl=en");
                    sendIntent.putExtra(Intent.EXTRA_TEXT, sAux);
                    sendIntent.setType("text/plain");
                    sendIntent.setPackage("com.facebook.orca");
                    try {
                        startActivity(sendIntent);
                    } catch (android.content.ActivityNotFoundException ex) {

                        Snackbar snackbar = Snackbar
                                .make(v, "Facebook Messenger is not Installed", Snackbar.LENGTH_LONG)
                                .setAction("Ok", null);
                        snackbar.setActionTextColor(Color.WHITE);
                        View snackbarView = snackbar.getView();
                        snackbarView.setBackgroundColor(Color.GRAY);
                        TextView textView = (TextView) snackbarView.findViewById(R.id.snackbar_text);
                        textView.setTextColor(Color.WHITE);
                        snackbar.show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        //getMenuInflater().inflate(R.menu.main_query, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            finish();
            return true;
        }
       /* if (id == R.id.notify) {
            return true;
        }*/
        return super.onOptionsItemSelected(item);
    }
}
