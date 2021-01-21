package com.orane.docassist;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;

import android.util.Log;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.WriterException;

import com.orane.docassist.Model.Model;
import com.orane.docassist.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import androidmads.library.qrgenearator.QRGContents;
import androidmads.library.qrgenearator.QRGEncoder;
import me.drakeet.materialdialog.MaterialDialog;

public class QR_Code_Activity extends AppCompatActivity {

    String shorturl, inputValue, TAG = "GenerateQRCode";
    ImageView qrImage;
    Bitmap bitmap;
    Toolbar toolbar;
    TextView mTitle;
    QRGEncoder qrgEncoder;
    Button btn_share;

    SharedPreferences sharedpreferences;
    public static final String MyPREFERENCES = "MyPrefs";
    public static final String short_url = "short_url_key";

    private static final int PERMISSION_REQUEST_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.qr_layout);

        //------------------------------------------------------
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setTitle("");

            mTitle = toolbar.findViewById(R.id.toolbar_title);
            Typeface khandBold = Typeface.createFromAsset(getApplicationContext().getAssets(), Model.font_name_bold);
            mTitle.setTypeface(khandBold);
        }
        //------------------------------------------------------
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.app_color));
        }

        //------------------------------------------------------------
        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        inputValue = sharedpreferences.getString(short_url, "");
        Log.e("inputValue",inputValue+" ");

        System.out.println("inputValue-------------- " + inputValue);
        //------------------------------------------------------------

        qrImage = (ImageView) findViewById(R.id.QR_Image);
        btn_share = (Button) findViewById(R.id.btn_share);

        Animation animSlideDown1 = AnimationUtils.loadAnimation(QR_Code_Activity.this, R.anim.bounce);
        animSlideDown1.setStartOffset(100);
        qrImage.startAnimation(animSlideDown1);


        if (Build.VERSION.SDK_INT >= 23) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(QR_Code_Activity.this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                //Toast.makeText(QR_Code_Activity.this, "Write External Storage permission allows us to do store images. Please allow this permission in App Settings.", Toast.LENGTH_LONG).show();
            } else {
                ActivityCompat.requestPermissions(QR_Code_Activity.this, new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
            }
        }

        btn_share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    qrImage.setDrawingCacheEnabled(true);
                    Bitmap bitmap = qrImage.getDrawingCache();

                    String bitmapPath = MediaStore.Images.Media.insertImage(getContentResolver(), bitmap, "title", null);
                    Uri bitmapUri = Uri.parse(bitmapPath);

                    System.out.println("bitmapUri-----------" + bitmapUri.toString());
                    Log.e("bitmapUri",bitmapUri+" ");
                    if (bitmapUri != null) {
                        // Construct a ShareIntent with link to image
                        Intent shareIntent = new Intent();
                        shareIntent.setAction(Intent.ACTION_SEND);
                        shareIntent.putExtra(Intent.EXTRA_STREAM, bitmapUri);
                        shareIntent.putExtra(Intent.EXTRA_TEXT, "You can now consult with me online... Via this Profile QR Code \n" +
                                "I am available for online consultation at icliniq.\n");
                        shareIntent.setType("image/*");
                        // Launch sharing dialog for image
                        startActivity(Intent.createChooser(shareIntent, "Share Image"));
                    } else {
                        System.out.println("bitmapUri---Null");
                    }
                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(), "Please allow permission on App settings to Share this QR Code", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
            }
        });


        if (inputValue != null && !inputValue.isEmpty() && !inputValue.equals("null") && !inputValue.equals("")) {

            try {
                if (inputValue.length() > 0) {
                    WindowManager manager = (WindowManager) getSystemService(WINDOW_SERVICE);
                    Display display = manager.getDefaultDisplay();
                    Point point = new Point();
                    display.getSize(point);
                    int width = point.x;
                    int height = point.y;
                    int smallerDimension = width < height ? width : height;
                    smallerDimension = smallerDimension * 3 / 4;

                    qrgEncoder = new QRGEncoder(
                            inputValue, null,
                            QRGContents.Type.TEXT,
                            smallerDimension);
                    try {
                        bitmap = qrgEncoder.encodeAsBitmap();
                        qrImage.setImageBitmap(bitmap);
                    } catch (WriterException e) {
                        Log.v(TAG, e.toString());
                    }
                }
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        } else {
            set_error();
        }

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

    public void set_error() {
        final MaterialDialog alert = new MaterialDialog(QR_Code_Activity.this);
        //alert.setTitle("Oops..!");
        alert.setMessage("Something went wrong.. Just login again to view Your iCliniq Profile link QR Code..");
        alert.setCanceledOnTouchOutside(false);
        alert.setPositiveButton("OK", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alert.dismiss();
                finish();
            }
        });
        alert.show();
    }
}
