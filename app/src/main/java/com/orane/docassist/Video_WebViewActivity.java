package com.orane.docassist;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.flurry.android.FlurryAgent;
import com.github.ksoichiro.android.observablescrollview.ObservableWebView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.orane.docassist.Model.Model;
import com.orane.docassist.Model.MultipartEntity2;
import com.orane.docassist.fileattach_library.EasyImage;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pl.tajchert.nammu.Nammu;
import pl.tajchert.nammu.PermissionCallback;


public class Video_WebViewActivity extends AppCompatActivity {

    public String url, type;
    ObservableWebView webView;
    ImageView close_button;
    String selectedPath, single_path, contentAsString, upLoadServerUri, serverResponseMessage, local_url, last_upload_file, attach_status, upload_response, selectedfilename;
    FloatingActionButton fab;
    int serverResponseCode = 0;
    StringBuilder total;
    ImageView img_remove;
    InputStream is = null;
    TextView menu_title;
    private static final int REQUEST_CAMERA_PERMISSIONS = 931;
    private static final int CAPTURE_MEDIA = 368;
    private Activity activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.video_webview);

        activity = this;

        final ProgressBar progress = (ProgressBar) findViewById(R.id.progress);
        fab = (FloatingActionButton) findViewById(R.id.fab);

        //------ getting Values ---------------------------
        Intent intent = getIntent();
        url = intent.getStringExtra("url");
        type = intent.getStringExtra("type");
        System.out.println("url-----" + url);
        //------ getting Values ---------------------------

        url = Model.BASE_URL + "users/myVideos?user_id=" + Model.id + "&layout=empty&t=mob&token=" + Model.token;
        System.out.println("url------------" + url);

        //------------ Object Creations -------------------------------
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setTitle("Video Gallery");
        }

        TextView mTitle = (TextView) toolbar.findViewById(R.id.toolbar_title);
        close_button = (ImageView) toolbar.findViewById(R.id.close_button);

        Typeface khandBold = Typeface.createFromAsset(getApplicationContext().getAssets(), Model.font_name_bold);
        mTitle.setTypeface(khandBold);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.app_color2));
        }
        //------------ Object Creations -------------------------------


        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //attach_dialog();

                Intent intent = new Intent(Video_WebViewActivity.this, Video_Description_Activity.class);
                startActivity(intent);

               /* if (Build.VERSION.SDK_INT > 15) {
                    askForPermissions(new String[]{
                                    android.Manifest.permission.CAMERA,
                                    android.Manifest.permission.RECORD_AUDIO,
                                    android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                    android.Manifest.permission.READ_EXTERNAL_STORAGE},
                            REQUEST_CAMERA_PERMISSIONS);
                } else {
                    enableCamera();
                }*/
            }
        });


        try {
            webView = (ObservableWebView) findViewById(R.id.webview);
            webView.getSettings().setJavaScriptEnabled(true);
            webView.getSettings().setBuiltInZoomControls(true);
            webView.setInitialScale(0);
            webView.getSettings().setLoadWithOverviewMode(true);
            webView.getSettings().setUseWideViewPort(true);
        } catch (Exception e) {
            e.printStackTrace();
        }

        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(final WebView view, final String url) {
                return false;
            }

            @Override
            public void onPageStarted(final WebView view, final String url, final Bitmap favicon) {
                progress.setVisibility(View.VISIBLE);
                super.onPageStarted(view, url, favicon);
            }

            @Override
            public void onPageFinished(final WebView view, final String url) {
                progress.setVisibility(View.GONE);
                super.onPageFinished(view, url);
            }
        });

        close_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new AlertDialog.Builder(Video_WebViewActivity.this)
                        //.setIcon(android.R.drawable.ic_dialog_alert)
                        .setTitle("Exit!")
                        .setMessage("Are you sure you want to close?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                finish();
                            }
                        })
                        .setNegativeButton("No", null)
                        .show();
            }
        });


        webView.loadUrl(url);
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
            if (webView.canGoBack()) {
                webView.goBack();
            } else {
                new AlertDialog.Builder(this)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setTitle("Exit!")
                        .setMessage("Are you sure you want to close?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                finish();
                            }
                        })
                        .setNegativeButton("No", null)
                        .show();
            }
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (webView.canGoBack()) {
            webView.goBack();
        } else {
            new AlertDialog.Builder(this)
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setTitle("Exit!")
                    .setMessage("Are you sure you want to close?")
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                        }

                    })
                    .setNegativeButton("No", null)
                    .show();
        }
    }


    public void attach_dialog() {

        List<String> mAnimals = new ArrayList<String>();
        mAnimals.add("Take Video");
        mAnimals.add("Browse Files");

        final CharSequence[] Animals = mAnimals.toArray(new String[mAnimals.size()]);
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        dialogBuilder.setTitle("Attach Videos");
        dialogBuilder.setItems(Animals, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int item) {
                String selectedText = Animals[item].toString();
                System.out.println("selectedText---" + selectedText);

                if (selectedText.equals("Take Video")) {

                    int permissionCheck = ContextCompat.checkSelfPermission(Video_WebViewActivity.this, Manifest.permission.CAMERA);
                    if (permissionCheck == PackageManager.PERMISSION_GRANTED) {
                        EasyImage.openCamera(Video_WebViewActivity.this, 0);
                    } else {
                        Nammu.askForPermission(Video_WebViewActivity.this, Manifest.permission.CAMERA, new PermissionCallback() {
                            @Override
                            public void permissionGranted() {
                                EasyImage.openCamera(Video_WebViewActivity.this, 0);
                            }

                            @Override
                            public void permissionRefused() {

                            }
                        });
                    }

                } else {
                    //showChooser();
                    int permissionCheck = ContextCompat.checkSelfPermission(Video_WebViewActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
                    if (permissionCheck == PackageManager.PERMISSION_GRANTED) {
                        EasyImage.openDocuments(Video_WebViewActivity.this, 0);
                    } else {
                        Nammu.askForPermission(Video_WebViewActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE, new PermissionCallback() {
                            @Override
                            public void permissionGranted() {
                                EasyImage.openDocuments(Video_WebViewActivity.this, 0);
                            }

                            @Override
                            public void permissionRefused() {

                            }
                        });
                    }

                }
            }
        });
        AlertDialog alertDialogObject = dialogBuilder.create();
        alertDialogObject.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CAPTURE_MEDIA && resultCode == RESULT_OK) {
            Toast.makeText(this, "Media captured.", Toast.LENGTH_SHORT).show();

            System.out.println("Camera OK-----------------------------");

            Bundle bundle = data.getExtras();
            if (bundle != null) {
                for (String key : bundle.keySet()) {
                    Object value = bundle.get(key);
                    System.out.println("key------------" + key.toString());
                    System.out.println("Data------------" + value.toString());

                    single_path = value.toString();
                }
            }

            new AsyncTask_fileupload().execute(single_path);

        }

    }

    private void onPhotosReturned(List<File> returnedPhotos) {

        //photos.addAll(returnedPhotos);

        for (int i = 0; i < returnedPhotos.size(); i++) {
            System.out.println(returnedPhotos.get(i));

            System.out.println("File Name------------------" + (returnedPhotos.get(i)).getName());

            selectedPath = (returnedPhotos.get(i).toString());
            selectedfilename = (returnedPhotos.get(i)).getName();

            new AsyncTask_fileupload().execute(selectedPath);

        }

    }

    private class AsyncTask_fileupload extends AsyncTask<String, Void, Boolean> {

        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            dialog = new ProgressDialog(Video_WebViewActivity.this);
            dialog.setMessage("Uploading. Please wait...");
            dialog.show();
            dialog.setCancelable(false);
        }

        @Override
        protected Boolean doInBackground(String... urls) {

            try {
                upload_response = upload_file(urls[0]); //ok
                System.out.println("upload_response---------" + upload_response);

                return true;
            } catch (Exception e) {
                e.printStackTrace();
            }

            return false;
        }

        protected void onPostExecute(Boolean result) {

            try {

                JSONObject jObj = new JSONObject(upload_response);
                attach_status = jObj.getString("status");

                System.out.println("attach_status-------" + attach_status);


                //----------- Flurry -------------------------------------------------
                Map<String, String> articleParams = new HashMap<String, String>();
                articleParams.put("android.doc.attach_file_path", selectedPath);
                FlurryAgent.logEvent("android.doc.upload_video", articleParams);
                //----------- Flurry -------------------------------------------------

/*
                //------------ Google firebase Analitics--------------------
                Model.mFirebaseAnalytics = FirebaseAnalytics.getInstance(getApplicationContext());
                Bundle params = new Bundle();
                params.putString("User", Model.id);
                params.putString("Qid", attach_qid);
                params.putString("attach_file_path", selectedPath);
                params.putString("attach_filename", selectedfilename);
                Model.mFirebaseAnalytics.logEvent("Attach_Files", params);
                //------------ Google firebase Analitics--------------------

*/


            } catch (Exception e) {
                e.printStackTrace();
            }

            dialog.dismiss();

        }
    }

/*

    public String upload_file(String fullpath) {

        String fpath_filename = fullpath.substring(fullpath.lastIndexOf("/") + 1);

        local_url = fullpath;

        System.out.println("fpath-------" + fullpath);
        System.out.println("fpath_filename---------" + fpath_filename);

        last_upload_file = fpath_filename;

        HttpURLConnection conn = null;
        DataOutputStream dos = null;

        String lineEnd = "\r\n";
        String twoHyphens = "--";
        String boundary = "*****";
        int bytesRead, bytesAvailable, bufferSize;
        byte[] buffer;
        int maxBufferSize = 10 * 1024 * 1024;
        File sourceFile = new File(fullpath);

        if (!sourceFile.isFile()) {
            System.out.println("Source File not exist :" + fullpath);
            return "";
        } else {

            try {

                upLoadServerUri = Model.BASE_URL + "users/myVideos?user_id=" + Model.id + "&layout=empty&t=mob&token=" + Model.token;
                System.out.println("upLoadServerUri---------------------" + upLoadServerUri);


                FileInputStream fileInputStream = new FileInputStream(fullpath);
                System.out.println("fullpath---------------------------------" + fullpath);
                URL url = new URL(upLoadServerUri);

                // Open a HTTP  connection to  the URL
                conn = (HttpURLConnection) url.openConnection();
                conn.setDoInput(true);
                conn.setDoOutput(true);
                conn.setUseCaches(false);
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Connection", "Keep-Alive");
                conn.setRequestProperty("ENCTYPE", "multipart/form-data");
                conn.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);
                conn.setRequestProperty("uploaded_file", fullpath);

                dos = new DataOutputStream(conn.getOutputStream());
                dos.writeBytes(twoHyphens + boundary + lineEnd);
                dos.writeBytes("Content-Disposition: form-data; name=\"file\";filename=\"" + fullpath + "\"" + lineEnd);
                dos.writeBytes(lineEnd);

                bytesAvailable = fileInputStream.available();
                bufferSize = Math.min(bytesAvailable, maxBufferSize);
                buffer = new byte[bufferSize];
                bytesRead = fileInputStream.read(buffer, 0, bufferSize);

                while (bytesRead > 0) {
                    dos.write(buffer, 0, bufferSize);
                    bytesAvailable = fileInputStream.available();
                    bufferSize = Math.min(bytesAvailable, maxBufferSize);
                    bytesRead = fileInputStream.read(buffer, 0, bufferSize);
                }

                dos.writeBytes(lineEnd);
                dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);

                serverResponseCode = conn.getResponseCode();
                serverResponseMessage = conn.getResponseMessage();

                int response = conn.getResponseCode();
                System.out.println("response-------" + response);
                is = conn.getInputStream();
                contentAsString = convertInputStreamToString(is);
                System.out.println("Upload File Response-----------------" + contentAsString);

                fileInputStream.close();
                dos.flush();
                dos.close();

            } catch (MalformedURLException ex) {
                ex.printStackTrace();

                runOnUiThread(new Runnable() {
                    public void run() {
                    }
                });

            } catch (Exception e) {
                e.printStackTrace();
            }

            return contentAsString;
        }
    }
*/

    public String convertInputStreamToString(InputStream stream) throws IOException {

        try {
            BufferedReader r = new BufferedReader(new InputStreamReader(stream, "UTF-8"));
            total = new StringBuilder();
            String line;
            while ((line = r.readLine()) != null) {
                total.append(line).append('\n');
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return total.toString();

    }

/*
    protected final void askForPermissions(String[] permissions, int requestCode) {
        List<String> permissionsToRequest = new ArrayList<>();
        for (String permission : permissions) {
            if (ActivityCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
                permissionsToRequest.add(permission);
            }
        }
        if (!permissionsToRequest.isEmpty()) {
            ActivityCompat.requestPermissions(this, permissionsToRequest.toArray(new String[permissionsToRequest.size()]), requestCode);
        } else enableCamera();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults.length == 0) return;
        enableCamera();
    }
*/


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults.length == 0) return;
        enableCamera();
    }

    public void enableCamera() {

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

        }

     /*   try {
            AnncaConfiguration.Builder videoLimited = new AnncaConfiguration.Builder(activity, CAPTURE_MEDIA);
            videoLimited.setMediaAction(AnncaConfiguration.MEDIA_ACTION_VIDEO);
            videoLimited.setMediaQuality(AnncaConfiguration.MEDIA_QUALITY_AUTO);
            videoLimited.setVideoFileSize(5 * 1024 * 1024);
            videoLimited.setMinimumVideoDuration(5 * 60 * 1000);
            new Annca(videoLimited.build()).launchCamera();
        } catch (Exception e) {
            e.printStackTrace();
        }*/
    }

    protected final void askForPermissions(String[] permissions, int requestCode) {
        List<String> permissionsToRequest = new ArrayList<>();
        for (String permission : permissions) {
            if (ActivityCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
                permissionsToRequest.add(permission);
            }
        }
        if (!permissionsToRequest.isEmpty()) {
            ActivityCompat.requestPermissions(this, permissionsToRequest.toArray(new String[permissionsToRequest.size()]), requestCode);
        }
        else enableCamera();
    }


    public String getPath(Uri uri) {

        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor = managedQuery(uri, projection, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }

    @Override
    protected void onResume() {
        super.onResume();
        try {

            if ((Model.query_launch).equals("added_video_notes")) {
                webView.loadUrl(url);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private String upload_file(String file_path) {
        last_upload_file=file_path;
        String ServerUploadPath   = Model.BASE_URL + "users/myVideos?user_id=" + Model.id + "&layout=empty&t=mob&token=" + Model.token;

        System.out.println("ServerUploadPath-------------" + ServerUploadPath);
        System.out.println("file_path-------------" + file_path);

        File file_value = new File(file_path);

        try {

            HttpClient client = new DefaultHttpClient();
            HttpPost post = new HttpPost(ServerUploadPath);
            MultipartEntity2 reqEntity = new MultipartEntity2();
            reqEntity.addPart("file", file_value);
            post.setEntity(reqEntity);

            HttpResponse response = client.execute(post);
            HttpEntity resEntity = response.getEntity();

            try {
                final String response_str = EntityUtils.toString(resEntity);

                if (resEntity != null) {
                    System.out.println("response_str-------" + response_str);
                    contentAsString =response_str;

                }
            } catch (Exception ex) {
                Log.e("Debug", "error: " + ex.getMessage(), ex);
            }
        } catch (Exception e) {
            Log.e("Upload Exception", "");
            e.printStackTrace();
        }

        return  contentAsString;
    }

}