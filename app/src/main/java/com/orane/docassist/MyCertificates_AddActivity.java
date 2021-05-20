package com.orane.docassist;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;


import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.internal.http.multipart.MultipartEntity;
import com.flurry.android.FlurryAgent;

import com.orane.docassist.Model.Model;
import com.orane.docassist.Model.MultipartEntity2;
import com.orane.docassist.Network.JSONParser;
import com.orane.docassist.R;
import com.orane.docassist.fileattach_library.DefaultCallback;
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
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pl.tajchert.nammu.Nammu;
import pl.tajchert.nammu.PermissionCallback;

public class MyCertificates_AddActivity extends AppCompatActivity {
    public StringBuilder total;
    TextView tvtit, tv_upload_photo, tvmore, tv_attach_id, tv_attach_url;
    ImageView thumb_img;
    Button btn_submit;
    String filename_text, upload_response, mode_val, course_name, college_name, year_val, eduId_val, upload_response_status;
    LinearLayout file_list, upload_layout;
    InputStream is = null;
    int serverResponseCode = 0;
    public String serverResponseMessage, selectedPath, qid, sel_filename, last_upload_file, attach_status, attach_file_url, local_url, contentAsString, upLoadServerUri, attach_id, attach_qid, image_path, selectedfilename;
    EditText edt_course, edt_college, edt_year;
    JSONObject json_post_edu, json_post_acedemic_resp;
    ImageView profile_image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_certificates_add);

        //------------ Object Creations -------------------------------

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setTitle("Add a Education");
        }
        //------------ Object Creations -------------------------------

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.app_color));
        }

        try {

            Intent intent = getIntent();

            mode_val = intent.getStringExtra("mode");
            eduId_val = intent.getStringExtra("clinic_id");
            course_name = intent.getStringExtra("clinic_name");
            college_name = intent.getStringExtra("clinic_geo");
            year_val = intent.getStringExtra("year_val");
            filename_text = intent.getStringExtra("attach_filename");

            System.out.println("mode_val---------------" + mode_val);
            System.out.println("edu_id---------------" + eduId_val);
            System.out.println("course_name---------------" + course_name);
            System.out.println("college_name---------------" + college_name);
            System.out.println("year_val---------------" + year_val);
            System.out.println("attach_filename---------------" + filename_text);


        } catch (Exception e) {
            e.printStackTrace();
        }

        upload_layout = (LinearLayout) findViewById(R.id.upload_layout);
        tv_upload_photo = (TextView) findViewById(R.id.tv_upload_photo);
        btn_submit = (Button) findViewById(R.id.btn_submit);
        file_list = (LinearLayout) findViewById(R.id.file_list);
        edt_course = (EditText) findViewById(R.id.edt_course);
        edt_college = (EditText) findViewById(R.id.edt_college);
        edt_year = (EditText) findViewById(R.id.edt_year);
        profile_image = (ImageView) findViewById(R.id.profile_image);

        edt_course.setText(course_name);
        edt_college.setText(college_name);
        edt_year.setText(year_val);

        if (mode_val.equals("update")) {
            upload_layout.setVisibility(View.GONE);
        } else {
            upload_layout.setVisibility(View.VISIBLE);
        }

        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String course_name = edt_course.getText().toString();
                String college_name = edt_college.getText().toString();
                String year_val = edt_year.getText().toString();

                System.out.println("course_name------" + course_name);
                System.out.println("college_name------" + college_name);
                System.out.println("year_val------" + year_val);
                System.out.println("selectedPath------" + selectedPath);
                System.out.println("eduId_val------" + eduId_val);
                System.out.println("filename_text------" + filename_text);

                if (filename_text != null && !filename_text.isEmpty() && !filename_text.equals("null") && !filename_text.equals("")) {

                } else {
                    filename_text = "";
                }

                if (!course_name.equals("")) {
                    if (!college_name.equals("")) {
                        if (!year_val.equals("") && year_val.length() == 4) {

                            try {
                                json_post_edu = new JSONObject();
                                json_post_edu.put("eduId", eduId_val);
                                json_post_edu.put("education", course_name);
                                json_post_edu.put("educationYear", year_val);
                                json_post_edu.put("college", college_name);
                                json_post_edu.put("filename", filename_text);

                                System.out.println("json_post_edu---" + json_post_edu.toString());

                                if ((Model.new_education) != null && !(Model.new_education).isEmpty() && !(Model.new_education).equals("null") && !(Model.new_education).equals("")) {
                                    Model.new_education = Model.new_education + "," + json_post_edu.toString();
                                } else {
                                    Model.new_education = json_post_edu.toString();
                                }

                                Model.new_education = "[" + Model.new_education + "]";

                                System.out.println("Model.new_education---------------------" + Model.new_education);

                               finish();

                               // new Async_post_acdemic().execute(json_post_edu);

                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                        } else {
                            edt_year.requestFocus();
                            edt_year.setError("Invalid year");
                        }

                    } else {
                        edt_college.requestFocus();
                        edt_college.setError("Please add your college or institution name");
                    }
                } else {
                    edt_course.requestFocus();
                    edt_course.setError("Please enter the course");
                }


            }
        });

        tv_upload_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                attach_dialog();
            }
        });

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        //------------------ Initialize File Attachment ---------------------------------
        Nammu.init(this);
        int permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            Nammu.askForPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE, new PermissionCallback() {
                @Override
                public void permissionGranted() {
                    //Nothing, this sample saves to Public gallery so it needs permission
                }

                @Override
                public void permissionRefused() {
                    finish();
                }
            });
        }

      /*  EasyImage.configuration(this)
                .setImagesFolderName("Attachments")
                .setCopyTakenPhotosToPublicGalleryAppFolder(true)
                .setCopyPickedImagesToPublicGalleryAppFolder(true)
                .setAllowMultiplePickInGallery(true);*/
        //------------------ Initialize File Attachment ---------------------------------


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

    public void attach_dialog() {
        List<String> mAnimals = new ArrayList<String>();
        mAnimals.add("Take Photo");
        mAnimals.add("Browse Files");

        final CharSequence[] Animals = mAnimals.toArray(new String[mAnimals.size()]);
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        dialogBuilder.setTitle("Attach Files/Images");
        dialogBuilder.setItems(Animals, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int item) {
                String selectedText = Animals[item].toString();
                System.out.println("selectedText---" + selectedText);

                if (selectedText.equals("Take Photo")) {

                    int permissionCheck = ContextCompat.checkSelfPermission(MyCertificates_AddActivity.this, Manifest.permission.CAMERA);
                    if (permissionCheck == PackageManager.PERMISSION_GRANTED) {
                        EasyImage.openCamera(MyCertificates_AddActivity.this, 0);
                    } else {
                        Nammu.askForPermission(MyCertificates_AddActivity.this, Manifest.permission.CAMERA, new PermissionCallback() {
                            @Override
                            public void permissionGranted() {
                                EasyImage.openCamera(MyCertificates_AddActivity.this, 0);
                            }

                            @Override
                            public void permissionRefused() {

                            }
                        });
                    }

                } else {
                    //showChooser();

                    int permissionCheck = ContextCompat.checkSelfPermission(MyCertificates_AddActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
                    if (permissionCheck == PackageManager.PERMISSION_GRANTED) {
                        EasyImage.openDocuments(MyCertificates_AddActivity.this, 0);
                    } else {
                        Nammu.askForPermission(MyCertificates_AddActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE, new PermissionCallback() {
                            @Override
                            public void permissionGranted() {
                                EasyImage.openDocuments(MyCertificates_AddActivity.this, 0);
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

    private class AsyncTask_fileupload extends AsyncTask<String, Void, Boolean> {

        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            try {
                dialog = new ProgressDialog(MyCertificates_AddActivity.this);
                dialog.setMessage("Uploading file. Please wait...");
                dialog.show();
                dialog.setCancelable(false);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        protected Boolean doInBackground(String... urls) {

            try {
                upload_response = upload_file(urls[0]);  //ok

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

                upload_response_status = jObj.getString("status");

                if (jObj.has("filename")) {
                    filename_text = jObj.getString("filename");
                } else {
                    filename_text = "";
                }


                System.out.println("upload_response_status ----------" + upload_response_status);

                if (upload_response_status.equals("1")) {

                    if (jObj.has("eduId")) {
                        eduId_val = jObj.getString("eduId");
                        System.out.println("eduId_val ----------" + eduId_val);
                    }

                } else {

                    String err_val = jObj.getString("err");
                    System.out.println("err_val ----------" + err_val);
                    Toast.makeText(getApplicationContext(), err_val, Toast.LENGTH_SHORT).show();
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

            dialog.cancel();
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
        String boundary = "**************";
        int bytesRead, bytesAvailable, bufferSize;
        byte[] buffer;
        int maxBufferSize = 1024 * 1024;
        File sourceFile = new File(fullpath);

        if (!sourceFile.isFile()) {
            System.out.println("Source File not exist :" + fullpath);
            return "";
        } else {

            try {
                upLoadServerUri = Model.BASE_URL + "/sapp/uploadDocEduCertificates?os_type=android&user_id=" + (Model.id) + "&token=" + Model.token;
                System.out.println("upLoadServerUri---------------------" + upLoadServerUri);

                FileInputStream fileInputStream = new FileInputStream(fullpath);
                System.out.println("fullpath---------------------------------" + fullpath);
                URL url = new URL(upLoadServerUri);

                conn = (HttpURLConnection) url.openConnection();
                 conn.setDoInput(true);
                conn.setDoOutput(true);
                conn.setUseCaches(false);
                conn.setRequestMethod("POST");
                  conn.setRequestProperty("Connection", "Keep-Alive");
                conn.setRequestProperty("ENCTYPE", "multipart/form-data");
                conn.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);
                conn.setRequestProperty("uploaded_file", fullpath);

                conn.getRequestMethod();
                conn.connect();
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
                System.out.println("serverResponseCode----------" + serverResponseCode);


                 serverResponseMessage = conn.getResponseMessage();
              System.out.println("serverResponseMessage----------" + serverResponseMessage);

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

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        Nammu.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        EasyImage.handleActivityResult(requestCode, resultCode, data, this, new DefaultCallback() {
            @Override
            public void onImagePickerError(Exception e, EasyImage.ImageSource source, int type) {
                e.printStackTrace();
            }

            @Override
            public void onImagesPicked(List<File> imageFiles, EasyImage.ImageSource source, int type) {
                onPhotosReturned(imageFiles);
                System.out.println("Selected file------------" + source.toString());

            }

            @Override
            public void onCanceled(EasyImage.ImageSource source, int type) {
                if (source == EasyImage.ImageSource.CAMERA) {
                    File photoFile = EasyImage.lastlyTakenButCanceledPhoto(MyCertificates_AddActivity.this);
                    if (photoFile != null) photoFile.delete();
                }
            }
        });
    }

    private void onPhotosReturned(List<File> returnedPhotos) {

        //photos.addAll(returnedPhotos);

        for (int i = 0; i < returnedPhotos.size(); i++) {
            System.out.println(returnedPhotos.get(i));

            System.out.println("File Name------------------" + (returnedPhotos.get(i)).getName());
            System.out.println("File selectedPath------------------" + selectedPath);

            selectedPath = (returnedPhotos.get(i).toString());
            selectedfilename = (returnedPhotos.get(i)).getName();


            //----------- Flurry -------------------------------------------------
            Map<String, String> articleParams = new HashMap<String, String>();
            articleParams.put("android.doctor.upload_profile_image", (attach_qid));
            articleParams.put("android.doctor.attach_file_path", selectedPath);
            articleParams.put("android.doctor.attach_filename", selectedfilename);
            FlurryAgent.logEvent("android.doctor.Attach_Take_Photo", articleParams);
            //----------- Flurry -------------------------------------------------

/*            //------------ Google firebase Analitics--------------------
            Model.mFirebaseAnalytics = FirebaseAnalytics.getInstance(getApplicationContext());
            Bundle params = new Bundle();
            params.putString("User", Model.id);
            params.putString("Qid", attach_qid);
            params.putString("attach_file_path", selectedPath);
            params.putString("attach_filename", selectedfilename);
            Model.mFirebaseAnalytics.logEvent("Attach_Files", params);
            //------------ Google firebase Analitics--------------------*/

            File imgFile = new File(selectedPath);

            if (imgFile.exists()) {
                Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                //ImageView myImage = (ImageView) findViewById(R.id.profile_image);
                profile_image.setImageBitmap(myBitmap);
            }


            new AsyncTask_fileupload().execute(selectedPath);

        }

    }

    @Override
    protected void onDestroy() {
      //  EasyImage.clearConfiguration(this);
        super.onDestroy();
    }


    private class Async_post_acdemic extends AsyncTask<JSONObject, Void, Boolean> {

        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(MyCertificates_AddActivity.this);
            dialog.setMessage("Please wait..");
            dialog.show();
            dialog.setCancelable(false);
        }

        @Override
        protected Boolean doInBackground(JSONObject... urls) {
            try {
                JSONParser jParser = new JSONParser();
                json_post_acedemic_resp = jParser.JSON_POST(urls[0], "post_acedemic");

                System.out.println("json_uname_check Params---------------" + urls[0]);
                System.out.println("json_post_acedemic_resp-----------" + json_post_acedemic_resp.toString());

                return true;
            } catch (Exception e) {
                e.printStackTrace();
            }

            return false;
        }

        protected void onPostExecute(Boolean result) {

            try {

                String status_val = json_post_acedemic_resp.getString("status");

                if (status_val.equals("1")) {

                    String education_text = json_post_acedemic_resp.getString("education");
                    eduId_val = json_post_acedemic_resp.getString("eduId");

                    System.out.println("education_text--------" + education_text);
                    System.out.println("eduId_val--------" + eduId_val);

                    if (education_text.length() > 5) {
                        finish();
                        Model.query_launch = "add_education";
                        Model.education_response = education_text;
                    }

                    new AsyncTask_fileupload().execute(selectedPath);

                } else {
                    if (json_post_acedemic_resp.has("err")) {
                        String err_val = json_post_acedemic_resp.getString("err");
                        Toast.makeText(getApplicationContext(), err_val, Toast.LENGTH_LONG).show();
                    }
                }

                dialog.cancel();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    private String upload_file(String file_path) {

        last_upload_file=file_path;
        String ServerUploadPath = Model.BASE_URL + "/sapp/uploadDocEduCertificates?os_type=android&user_id=" + (Model.id) + "&token=" + Model.token;

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
