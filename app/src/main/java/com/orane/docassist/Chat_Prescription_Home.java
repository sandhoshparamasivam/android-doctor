package com.orane.docassist;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import com.flurry.android.FlurryAgent;
import com.google.firebase.analytics.FirebaseAnalytics;
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

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pl.tajchert.nammu.Nammu;
import pl.tajchert.nammu.PermissionCallback;


public class Chat_Prescription_Home extends AppCompatActivity {

    Button btn_Submit_pres, btn_write_pres, btn_upload_pres;
    EditText edt_feedback;
    public String enable_prescription_val, has_upload_file_val, local_url, last_upload_file, q_id, contentAsString, upload_response, selectedPath, selectedfilename, feedback_val, report_response, attach_qid, attach_status, attach_file_url, attach_filename, attach_id;
    JSONObject json_feedback, json_response_obj;
    TextView tv_pres_added,tv_write_query_id, tv_attach_url, tv_attach_id;
    ImageView thumb_img;
    LinearLayout file_list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chat_prescription_home);

        try {
            Intent intent = getIntent();
            q_id = intent.getStringExtra("q_id");

            has_upload_file_val = intent.getStringExtra("has_upload_file_val");
            enable_prescription_val = intent.getStringExtra("enable_prescription_val");

            System.out.println("Get q_id---" + q_id);
            System.out.println("Get has_upload_file_val---" + has_upload_file_val);
            System.out.println("Get enable_prescription_val---" + enable_prescription_val);



        } catch (Exception e) {
            e.printStackTrace();
        }

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

        tv_pres_added = findViewById(R.id.tv_pres_added);
        btn_Submit_pres = findViewById(R.id.btn_Submit_pres);
        btn_upload_pres = findViewById(R.id.btn_upload_pres);
        btn_write_pres = findViewById(R.id.btn_write_pres);
        tv_write_query_id = findViewById(R.id.tv_write_query_id);
        file_list = findViewById(R.id.file_list);


        btn_Submit_pres.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btn_upload_pres.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                upload_dialog();
            }
        });

        btn_write_pres.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Chat_Prescription_Home.this, Prescription_home.class);
                intent.putExtra("add_type", "new");
                intent.putExtra("cur_qid", q_id);
                intent.putExtra("p_type", "chat");
                //intent.putExtra("pres_status", p_status_val);
                startActivity(intent);
            }
        });

        if (enable_prescription_val.equals("1")) {
            btn_write_pres.setVisibility(View.VISIBLE);
        } else {
            btn_write_pres.setVisibility(View.GONE);
        }

        if (has_upload_file_val.equals("1")) {
            btn_upload_pres.setVisibility(View.VISIBLE);
        } else {
            btn_upload_pres.setVisibility(View.GONE);
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


    public void upload_dialog() {
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

                    int permissionCheck = ContextCompat.checkSelfPermission(Chat_Prescription_Home.this, Manifest.permission.CAMERA);
                    if (permissionCheck == PackageManager.PERMISSION_GRANTED) {
                        EasyImage.openCamera(Chat_Prescription_Home.this, 0);
                    } else {
                        Nammu.askForPermission(Chat_Prescription_Home.this, Manifest.permission.CAMERA, new PermissionCallback() {
                            @Override
                            public void permissionGranted() {
                                EasyImage.openCamera(Chat_Prescription_Home.this, 0);
                            }

                            @Override
                            public void permissionRefused() {

                            }
                        });
                    }

                } else {
                    //showChooser();

                    int permissionCheck = ContextCompat.checkSelfPermission(Chat_Prescription_Home.this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
                    if (permissionCheck == PackageManager.PERMISSION_GRANTED) {
                        EasyImage.openDocuments(Chat_Prescription_Home.this, 0);
                    } else {
                        Nammu.askForPermission(Chat_Prescription_Home.this, Manifest.permission.WRITE_EXTERNAL_STORAGE, new PermissionCallback() {
                            @Override
                            public void permissionGranted() {
                                EasyImage.openDocuments(Chat_Prescription_Home.this, 0);
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
                    File photoFile = EasyImage.lastlyTakenButCanceledPhoto(Chat_Prescription_Home.this);
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

            selectedPath = (returnedPhotos.get(i).toString());
            selectedfilename = (returnedPhotos.get(i)).getName();

            System.out.println("selectedPath------------" + selectedPath);
            new AsyncTask_fileupload().execute(selectedPath);

        }
    }

    private class AsyncTask_fileupload extends AsyncTask<String, Void, Boolean> {

        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            try {
                dialog = new ProgressDialog(Chat_Prescription_Home.this);
                dialog.setMessage("Uploading. Please wait...");
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
                JSONObject jObj = new JSONObject(upload_response);

                attach_qid = jObj.getString("qid");
                attach_status = jObj.getString("status");
                attach_file_url = jObj.getString("url");
                attach_filename = jObj.getString("filename");
                attach_id = jObj.getString("attach_id");

                //------------ Google firebase Analitics--------------------
                Model.mFirebaseAnalytics = FirebaseAnalytics.getInstance(getApplicationContext());
                Bundle params = new Bundle();
                params.putString("attach_qid", attach_qid);
                params.putString("attach_status", attach_status);
                params.putString("attach_file_url", attach_file_url);
                params.putString("attach_filename", attach_filename);
                params.putString("attach_id", attach_id);
                Model.mFirebaseAnalytics.logEvent("AskQuery2_File_Upload", params);
                //------------ Google firebase Analitics--------------------

                System.out.println("attach_qid-------" + attach_qid);
                System.out.println("attach_status-------" + attach_status);
                System.out.println("attach_file_url-------" + attach_file_url);
                System.out.println("attach_filename-------" + attach_filename);
                System.out.println("attach_attach_id-------" + attach_id);
                System.out.println("last_upload_file--------------" + last_upload_file);

                if (!(last_upload_file).equals("")) {

                    //------------------------------------
                    LayoutInflater layoutInflater = (LayoutInflater) getBaseContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    final View addView = layoutInflater.inflate(R.layout.ans_upload_file_list, null);

                    TextView tv_quest = addView.findViewById(R.id.tv_quest);
                    ImageView close_button = addView.findViewById(R.id.close_button);
                    thumb_img = addView.findViewById(R.id.imageView4);
                    tv_attach_url = addView.findViewById(R.id.tv_attach_url);
                    tv_attach_id = addView.findViewById(R.id.tv_attach_id);

                    tv_quest.setText(last_upload_file);
                    tv_attach_id.setText(attach_id);
                    tv_attach_url.setText(attach_file_url);
                    //thumb_img.setImageBitmap(BitmapFactory.decodeFile(local_url));

                    System.out.println("Model.upload_files-----------" + (last_upload_file));
                    System.out.println("Model.attach_qid-----------" + (attach_qid));
                    System.out.println("Model.attach_id-----------" + (attach_id));
                    System.out.println("Model.attach_file_url-----------" + (attach_file_url));

                    close_button.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            View parent = (View) v.getParent();
                            //View grand_parent = (View)parent.getParent();

                            tv_attach_id = parent.findViewById(R.id.tv_attach_id);
                            String attid = tv_attach_id.getText().toString();

                            //---------------------------
                            String url = Model.BASE_URL + "/sapp/removeAnsAttachment?user_id=" + (Model.id) + "&attach_id=" + attid + "&token=" + Model.token;
                            System.out.println("Remover Attach url-------------" + url);
                            //new JSON_remove_file().execute(url);
                            //---------------------------

                            System.out.println("Removed attach_id-----------" + attid);
                            ((LinearLayout) addView.getParent()).removeView(addView);
                        }
                    });

                    thumb_img.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            //preview_image(local_url);
                        }
                    });

                    file_list.addView(addView);
                    //------------------------------------
                }

                last_upload_file = "";

            } catch (Exception e) {
                e.printStackTrace();
            }

            dialog.cancel();
        }
    }

    private String upload_file(String file_path) {

        last_upload_file = file_path;

        System.out.println("last_upload_file------------" + last_upload_file);

        String ServerUploadPath = Model.BASE_URL + "/sapp/ansUpload?user_id=" + (Model.id) + "&qid=" + q_id + "&token=" + Model.token;
        //String ServerUploadPath = Model.BASE_URL + "/sapp/ansUpload?user_id=" + (Model.id) + "&os_type=android&appt_id=" + cons_id + "&token=" + Model.token;

        System.out.println("ServerUploadPath------------" + ServerUploadPath);

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

    @Override
    protected void onResume() {
        super.onResume();

        try {
            if ((Model.prescribe_flag) != null && !(Model.prescribe_flag).isEmpty() && !(Model.prescribe_flag).equals("null") && !(Model.prescribe_flag).equals("")) {
                try {
                    if ((Model.prescribe_flag).equals("true")) {
                        tv_pres_added.setVisibility(View.VISIBLE);
                        Model.prescribe_flag="";
                    }
                    else{
                        tv_pres_added.setVisibility(View.GONE);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            else{
                tv_pres_added.setVisibility(View.GONE);
            }
        } catch (Exception e) {
            System.out.println("Resume Error---" + e.toString());
            e.printStackTrace();
        }

    }

}
