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

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import com.flurry.android.FlurryAgent;
import com.orane.docassist.Model.Model;
import com.orane.docassist.Model.MultipartEntity2;
import com.orane.docassist.Network.JSONParser;
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
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pl.tajchert.nammu.Nammu;
import pl.tajchert.nammu.PermissionCallback;

public class Signup_MyCertificates_AddActivity extends AppCompatActivity {
    public StringBuilder total;
    TextView tvtit, tv_upload_photo, tvmore, tv_attach_id, tv_attach_url;
    ImageView thumb_img;
    Button btn_submit;
    String upload_response, eduId_val, clinic_id_text,clinic_name_text,clinic_geo_text,mode_text;
    LinearLayout file_list;
    InputStream is = null;
    int serverResponseCode = 0;
    public String education_text, serverResponseMessage, selectedPath, qid, sel_filename, last_upload_file, attach_status, attach_file_url, attach_filename, local_url, contentAsString, upLoadServerUri, attach_id, attach_qid, image_path, selectedfilename;
    EditText edt_course, edt_college, edt_year;
    JSONObject json_post_edu, json_post_acedemic_resp;
    ImageView profile_image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup_my_certificates_add);

        //------------ Object Creations -------------------------------
        Toolbar toolbar = findViewById(R.id.toolbar);
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


        tv_upload_photo = findViewById(R.id.tv_upload_photo);
        btn_submit = findViewById(R.id.btn_submit);
        file_list = findViewById(R.id.file_list);
        edt_course = findViewById(R.id.edt_course);
        edt_college = findViewById(R.id.edt_college);
        edt_year = findViewById(R.id.edt_year);
        profile_image = findViewById(R.id.profile_image);

        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (selectedPath != null && !selectedPath.isEmpty() && !selectedPath.equals("null") && !selectedPath.equals("")) {

                    String course_name = edt_course.getText().toString();
                    String college_name = edt_college.getText().toString();
                    String year_val = edt_year.getText().toString();

                    System.out.println("course_name------" + course_name);
                    System.out.println("college_name------" + college_name);
                    System.out.println("year_val------" + year_val);
                    System.out.println("selectedPath------" + selectedPath);
                    System.out.println("eduId_val------" + eduId_val);

                    if (!course_name.equals("")) {
                        if (!college_name.equals("")) {
                            if (!year_val.equals("") && year_val.length() == 4) {

                                try {
                                    json_post_edu = new JSONObject();
                                    json_post_edu.put("eduId", eduId_val);
                                    json_post_edu.put("education", course_name);
                                    json_post_edu.put("educationYear", year_val);
                                    json_post_edu.put("college", college_name);
                                    json_post_edu.put("filename", "filename");

                                    System.out.println("json_post_edu---" + json_post_edu.toString());

                                    new Async_post_acdemic().execute(json_post_edu);

                                } catch (Exception e) {
                                    e.printStackTrace();
                                }

                            } else {
                                edt_year.requestFocus();
                                edt_year.setError("Please add the year of graduation");
                            }

                        } else {
                            edt_college.requestFocus();
                            edt_college.setError("Please add your college or institution name");
                        }
                    } else {
                        edt_course.requestFocus();
                        edt_course.setError("Please enter the course");
                    }
                } else {
                    Toast.makeText(Signup_MyCertificates_AddActivity.this, "Please select your certificate.", Toast.LENGTH_SHORT).show();
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

                    int permissionCheck = ContextCompat.checkSelfPermission(Signup_MyCertificates_AddActivity.this, Manifest.permission.CAMERA);
                    if (permissionCheck == PackageManager.PERMISSION_GRANTED) {
                        EasyImage.openCamera(Signup_MyCertificates_AddActivity.this, 0);
                    } else {
                        Nammu.askForPermission(Signup_MyCertificates_AddActivity.this, Manifest.permission.CAMERA, new PermissionCallback() {
                            @Override
                            public void permissionGranted() {
                                EasyImage.openCamera(Signup_MyCertificates_AddActivity.this, 0);
                            }

                            @Override
                            public void permissionRefused() {

                            }
                        });
                    }

                } else {
                    //showChooser();

                    int permissionCheck = ContextCompat.checkSelfPermission(Signup_MyCertificates_AddActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
                    if (permissionCheck == PackageManager.PERMISSION_GRANTED) {
                        EasyImage.openDocuments(Signup_MyCertificates_AddActivity.this, 0);
                    } else {
                        Nammu.askForPermission(Signup_MyCertificates_AddActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE, new PermissionCallback() {
                            @Override
                            public void permissionGranted() {
                                EasyImage.openDocuments(Signup_MyCertificates_AddActivity.this, 0);
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
                dialog = new ProgressDialog(Signup_MyCertificates_AddActivity.this);
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

                Model.query_launch = "add_education";

                JSONObject jObj = new JSONObject(upload_response);

                String upload_response_status = jObj.getString("status");

                System.out.println("upload_response_status ----------" + upload_response_status);

                if (upload_response_status.equals("1")) {

                   /* if (jObj.has("eduId")) {
                        eduId_val = jObj.getString("eduId");
                        System.out.println("Getting eduId_val ----------" + eduId_val);

                    }
*/
                    finish();

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
        int maxBufferSize = 1024 * 1024;
        File sourceFile = new File(fullpath);

        if (!sourceFile.isFile()) {
            System.out.println("Source File not exist :" + fullpath);
            return "";
        } else {

            try {
                upLoadServerUri = Model.BASE_URL + "/sapp/uploadDocEduCertificates?id=" + eduId_val + "&user_id=" + (Model.id) + "&token=" + Model.token;
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
                    File photoFile = EasyImage.lastlyTakenButCanceledPhoto(Signup_MyCertificates_AddActivity.this);
                    if (photoFile != null) photoFile.delete();
                }
            }
        });
    }

    private void onPhotosReturned(List<File> returnedPhotos) {

        //photos.addAll(returnedPhotos);

        for (int i = 0; i < returnedPhotos.size(); i++) {
            System.out.println("File Loc----------- " + returnedPhotos.get(i));
            selectedPath = (returnedPhotos.get(i).toString());
            selectedfilename = (returnedPhotos.get(i)).getName();

            System.out.println("File Name------------------" + selectedfilename);
            System.out.println("File selectedPath------------------" + selectedPath);


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


            //  new AsyncTask_fileupload().execute(selectedPath);

        }

    }

    @Override
    protected void onDestroy() {
        //EasyImage.clearConfiguration(this);
        super.onDestroy();
    }


    private class Async_post_acdemic extends AsyncTask<JSONObject, Void, Boolean> {

        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(Signup_MyCertificates_AddActivity.this);
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

                System.out.println("status_val--------------" + status_val);


                if (status_val.equals("1")) {

                    if (json_post_acedemic_resp.has("education")) {
                        education_text = json_post_acedemic_resp.getString("education");
                    }

                    eduId_val = json_post_acedemic_resp.getString("eduId");

                    System.out.println("education_text--------" + education_text);
                    System.out.println("eduId_val--------" + eduId_val);

                    if (education_text != null && !education_text.isEmpty() && !education_text.equals("null") && !education_text.equals("")) {
                        if (education_text.length() > 5) {
                            Model.query_launch = "add_education";
                            Model.education_response = education_text;
                            //finish();
                        }
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

        last_upload_file = file_path;
        String ServerUploadPath = Model.BASE_URL + "/sapp/uploadDocEduCertificates?id=" + eduId_val + "&user_id=" + (Model.id) + "&token=" + Model.token;

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
