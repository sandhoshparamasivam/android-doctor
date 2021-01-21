package com.orane.docassist;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.flurry.android.FlurryAgent;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.analytics.FirebaseAnalytics;

import com.orane.docassist.Model.Model;
import com.orane.docassist.Model.MultipartEntity2;
import com.orane.docassist.Network.JSONParser;

import net.alhazmy13.mediapicker.Video.VideoPicker;

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
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import me.drakeet.materialdialog.MaterialDialog;
import pl.tajchert.nammu.Nammu;
import pl.tajchert.nammu.PermissionCallback;

public class Video_Description_Activity extends AppCompatActivity {

    Button btn_submit, btn_attach_video;
    EditText edt_desc, edt_title;
    TextView tv_video_tit, tv_notes_tit, tv_notes, tv_spec_name, tv_spec, tv_desc;
    public String pat_id, vid_id, err_text, edt_title_text, edt_desc_text, feedback_val, report_response;
    JSONObject json_feedback, json_apply_desc, json_response_obj;
    LinearLayout select_layout;
    ImageView img_remove;

    String listString, selectedPath, single_path, contentAsString, upLoadServerUri, serverResponseMessage, local_url, last_upload_file, attach_status, upload_response, selectedfilename;
    FloatingActionButton fab;
    int serverResponseCode = 0;
    StringBuilder total;
    InputStream is = null;
    TextView menu_title;
    private static final int REQUEST_CAMERA_PERMISSIONS = 931;
    private static final int CAPTURE_MEDIA = 368;
    private Activity activity;

    private static final String TAG = "MainActivity";
    private List<String> mPath;
    TextView tv_attach_videos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.video_description);

        try {
            Intent intent = getIntent();
            pat_id = intent.getStringExtra("pat_id");
            System.out.println("Get pat_id---" + pat_id);

            Model.query_launch = "Video_Description";
            single_path = "";
        } catch (Exception e) {
            e.printStackTrace();
        }

        activity = this;


        //------------ Object Creations -------------------------------
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setTitle("");

            TextView mTitle = toolbar.findViewById(R.id.toolbar_title);
            Typeface khandBold = Typeface.createFromAsset(getApplicationContext().getAssets(), Model.font_name_bold);
            mTitle.setTypeface(khandBold);
        }
        //------------ Object Creations -------------------------------

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.app_color));
        }

        edt_title = findViewById(R.id.edt_title);
        edt_desc = findViewById(R.id.edt_desc);

        tv_video_tit = findViewById(R.id.tv_video_tit);
        tv_attach_videos = findViewById(R.id.tv_attach_videos);
        tv_desc = findViewById(R.id.tv_desc);
        tv_spec = findViewById(R.id.tv_spec);
        tv_spec_name = findViewById(R.id.tv_spec_name);
        tv_notes_tit = findViewById(R.id.tv_notes_tit);
        tv_notes = findViewById(R.id.tv_notes);
        img_remove = findViewById(R.id.img_remove);
        select_layout = findViewById(R.id.select_layout);
        btn_submit = findViewById(R.id.btn_submit);
        btn_attach_video = findViewById(R.id.btn_attach_video);

        tv_notes.setText(Html.fromHtml(getString(R.string.video_note)));

        img_remove.setVisibility(View.GONE);

        select_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Video_Description_Activity.this, SpecialityListActivity.class);
                startActivity(intent);
            }
        });

        btn_attach_video.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new VideoPicker.Builder(Video_Description_Activity.this)
                        .mode(VideoPicker.Mode.CAMERA_AND_GALLERY)
                        .directory(VideoPicker.Directory.DEFAULT)
                        .extension(VideoPicker.Extension.MP4)
                        .enableDebuggingMode(true)
                        .build();
            }
        });


        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                edt_title_text = edt_title.getText().toString();
                edt_desc_text = edt_desc.getText().toString();

                if ((Model.select_spec_val) != null && !(Model.select_spec_val).isEmpty() && !(Model.select_spec_val).equals("null") && !(Model.select_spec_val).equals("") && !(Model.select_spec_val).equals("0")) {

                    if (edt_title_text.equals("")) {
                        edt_title.setError("Please enter a title for the video");
                    } else if (edt_desc_text.equals("")) {
                        edt_desc.setError("Type details of your case here");
                    } else {

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

                        System.out.println("single_path--- " + single_path);

                        new AsyncTask_fileupload().execute(single_path);

                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Please Select the Speciality", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void say_success() {

        try {

            //---------------- Dialog------------------------------------------------------------------
            final MaterialDialog alert = new MaterialDialog(Video_Description_Activity.this);
            alert.setTitle("Success..!");
            alert.setMessage("Your video has been uploaded");
            alert.setCanceledOnTouchOutside(false);
            alert.setPositiveButton("OK", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    alert.dismiss();
                    finish();
                }
            });

                          /*  alert.setNegativeButton("No", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    alert.dismiss();
                                }
                            });*/
            alert.show();
            //-----------------Dialog-----------------------------------------------------------------

        } catch (Exception e) {
            e.printStackTrace();
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

    @Override
    protected void onResume() {
        super.onResume();
        try {

            if ((Model.query_launch).equals("SpecialityListActivity")) {
                System.out.println("Resume Model.query_launch-----" + Model.select_spec_val);
                System.out.println("Resume Model.select_specname-----" + Model.select_specname);

                if (Model.select_spec_val.equals("0")) {
                    tv_spec_name.setText("Select Speciality");
                    img_remove.setVisibility(View.GONE);
                } else {
                    tv_spec_name.setText(Model.select_specname);
                    //img_remove.setVisibility(View.VISIBLE);
                    img_remove.setVisibility(View.GONE);
                }
            }

            if (single_path != null && !single_path.isEmpty() && !single_path.equals("null") && !single_path.equals("") && !single_path.equals("0")) {
                tv_attach_videos.setVisibility(View.VISIBLE);
            } else {
                tv_attach_videos.setVisibility(View.GONE);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Log.d(TAG, "onActivityResult() called with: requestCode = [" + requestCode + "], resultCode = [" + resultCode + "], data = [" + data + "]");

        if (requestCode == VideoPicker.VIDEO_PICKER_REQUEST_CODE && resultCode == RESULT_OK) {
            mPath = (List<String>) data.getSerializableExtra(VideoPicker.EXTRA_VIDEO_PATH);

            single_path = mPath.get(0);
/*
            for (String s : mPath) {
                listString += s + ",";
            }*/

            System.out.println("single_path-----------" + single_path);
        }

      /*
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

            //new AsyncTask_fileupload().execute(single_path);

        }*/

    }

    private class AsyncTask_fileupload extends AsyncTask<String, Void, Boolean> {

        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            dialog = new ProgressDialog(Video_Description_Activity.this);
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

                System.out.println("Response jObj-------" + jObj.toString());

                if (attach_status.equals("1")) {
                    if (jObj.has("vid")) {
                        vid_id = jObj.getString("vid");
                        System.out.println("vid_id-------" + vid_id);

                        app_desc();
                    }
                } else {
                    if (jObj.has("err")) {
                        err_text = jObj.getString("err");
                        System.out.println("err_text-------" + err_text);
                    }
                }


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
                //------------ Google firebase Analytics--------------------

*/


            } catch (Exception e) {
                e.printStackTrace();
            }

            dialog.dismiss();

        }
    }


    /*public String upload_file(String fullpath) {

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

                upLoadServerUri = Model.BASE_URL + "sapp/uploadDocVideo?user_id=" + Model.id + "&token=" + Model.token;
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
    }*/

    public String convertInputStreamToString(InputStream stream) throws IOException {

        try {
            BufferedReader r = new BufferedReader(new InputStreamReader(stream, StandardCharsets.UTF_8));
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
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            System.out.println("checkSelfPermission--------- ");
        }

      /*  try {
            AnncaConfiguration.Builder videoLimited = new AnncaConfiguration.Builder(activity, CAPTURE_MEDIA);
            videoLimited.setMediaAction(AnncaConfiguration.MEDIA_ACTION_VIDEO);
            videoLimited.setMediaQuality(AnncaConfiguration.MEDIA_QUALITY_AUTO);
            videoLimited.setVideoFileSize(5 * 1024 * 1024);
            videoLimited.setMinimumVideoDuration(5 * 60 * 1000);
            new Annca(videoLimited.build()).launchCamera();

            System.out.println("checkSelfPermission--------- ");

        } catch (Exception e) {
            e.printStackTrace();
        }*/
    }

    protected final void askForPermissions(String[] permissions, int requestCode) {
        List<String> permissionsToRequest = new ArrayList<>();
        for (String permission : permissions) {
            if (ActivityCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
                permissionsToRequest.add(permission);
                System.out.println("permissionsToRequest--------- " + permissionsToRequest);
            }
        }
        if (!permissionsToRequest.isEmpty()) {
            ActivityCompat.requestPermissions(this, permissionsToRequest.toArray(new String[permissionsToRequest.size()]), requestCode);
            System.out.println("ToRequest--------- " + permissionsToRequest);
        } else enableCamera();
    }


    public String getPath(Uri uri) {

        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor = managedQuery(uri, projection, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }

/*    public void force_logout() {

        final MaterialDialog alert = new MaterialDialog(Video_Description_Activity.this);
        alert.setTitle("Please Login again..!");
        alert.setMessage("Something went wrong. Please Logout and Login again to continue");
        alert.setCanceledOnTouchOutside(false);
        alert.setPositiveButton("OK", new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //============================================================
                SharedPreferences.Editor editor = sharedpreferences.edit();
                editor.putString(Login_Status, "0");
                editor.apply();
                //============================================================
                finishAffinity();
                Intent i = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(i);
                alert.dismiss();
                finish();
            }
        });
        alert.show();
    }*/

    public void app_desc() {
        if (vid_id != null && !vid_id.isEmpty() && !vid_id.equals("null") && !vid_id.equals("") && !vid_id.equals("0")) {

            try {

                json_feedback = new JSONObject();
                json_feedback.put("vid", vid_id);
                json_feedback.put("speciality", Model.select_spec_val);
                json_feedback.put("vidTitle", edt_title_text);
                json_feedback.put("vidDescription", edt_desc_text);

                System.out.println("json_feedback---" + json_feedback.toString());

                new JSON_apply_desc().execute(json_feedback);


                //------------ Google firebase Analitics--------------------
                Model.mFirebaseAnalytics = FirebaseAnalytics.getInstance(getApplicationContext());
                Bundle params = new Bundle();
                params.putString("User", Model.id + "/" + Model.name);
                params.putString("Details", json_feedback.toString());
                Model.mFirebaseAnalytics.logEvent("video_description_entry", params);
                //------------ Google firebase Analitics--------------------

                //----------- Flurry -------------------------------------------------
                Map<String, String> articleParams = new HashMap<String, String>();
                articleParams.put("android.doc.notes_entry:", json_feedback.toString());
                FlurryAgent.logEvent("android.doc.video_description_entry", articleParams);
                //----------- Flurry -------------------------------------------------

            } catch (Exception e2) {
                e2.printStackTrace();
            }
        } else {
            System.out.println("Video is not uploaded correctly");
            Toast.makeText(Video_Description_Activity.this, "Failed to upload the video. Please check the format and try again.", Toast.LENGTH_SHORT).show();
        }
    }

    private class JSON_apply_desc extends AsyncTask<JSONObject, Void, Boolean> {

        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            try {
                dialog = new ProgressDialog(Video_Description_Activity.this);
                dialog.setMessage("Please wait..");
                dialog.show();
                dialog.setCancelable(false);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        protected Boolean doInBackground(JSONObject... urls) {

            try {

                JSONParser jParser = new JSONParser();
                json_response_obj = jParser.JSON_POST(urls[0], "video_desc_entry");

                System.out.println("Feedback URL---------------" + urls[0]);
                System.out.println("json_response_obj-----------" + json_response_obj.toString());

                return true;

            } catch (Exception e) {
                e.printStackTrace();
            }

            return false;
        }

        protected void onPostExecute(Boolean result) {

            try {

                report_response = json_response_obj.getString("status");
                System.out.println("report_response--------------" + report_response);

                Model.query_launch = "added_video_notes";

                if (report_response.equals("1")) {
                    say_success();
                } else {
                    Toast.makeText(getApplicationContext(), "Failed to upload the video. Please check the format and try again.", Toast.LENGTH_LONG).show();
                }

                dialog.cancel();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private String upload_file(String file_path) {
        last_upload_file=file_path;
        String ServerUploadPath = Model.BASE_URL + "sapp/uploadDocVideo?user_id=" + Model.id + "&token=" + Model.token;

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
                    contentAsString = response_str;

                }
            } catch (Exception ex) {
                Log.e("Debug", "error: " + ex.getMessage(), ex);
            }
        } catch (Exception e) {
            Log.e("Upload Exception", "");
            e.printStackTrace();
        }

        return contentAsString;
    }

}
