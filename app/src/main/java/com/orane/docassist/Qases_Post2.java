package com.orane.docassist;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewSwitcher;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import com.android.volley.toolbox.ImageLoader;
import com.orane.docassist.Model.Model;
import com.orane.docassist.Model.MultipartEntity2;
import com.orane.docassist.Network.JSONParser;
import com.orane.docassist.file_picking.utils.FileUtils;
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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import me.drakeet.materialdialog.MaterialDialog;
import pl.tajchert.nammu.Nammu;
import pl.tajchert.nammu.PermissionCallback;


public class Qases_Post2 extends AppCompatActivity {


    View addView;
    GridView gridGallery;
    Handler handler;
    ImageView imgSinglePick;
    Button btnGalleryPick;
    public StringBuilder total;

    String action;
    ViewSwitcher viewSwitcher;
    ImageLoader imageLoader;
    InputStream is = null;
    int serverResponseCode = 0;
    ArrayList<String> imagePaths;
    ImageView thumb_img;
    View recc_vi;
    private static final int FILE_SELECT_CODE = 0;
    Uri selectedImageUri;
    LinearLayout layout_attachfile, file_list, takephoto_layout, browse_layout;
    public String compmore, prevhist, curmedi, pastmedi, labtest, serverResponseMessage, selectedPath, inv_id, inv_fee, inv_strfee, status_postquery, qid, sel_filename, last_upload_file, attach_status, attach_file_url, attach_filename, local_url, contentAsString, upLoadServerUri, attach_id, attach_qid, upload_response, image_path, selectedfilename;
    Button btn_attach, btn_submit;
    public JSONObject json_qase_post, jsonobj, jsonobj_prepinv, json;
    Toolbar toolbar;
    ScrollView scrollView1;
    TextView tvtit, tvmore, tv_attach_id, tv_attach_url;
    LinearLayout files_attached_layout;
    String status_val;
    EditText tv_compmore, tv_prevhist, tv_curmedi, tv_pastmedi, tv_labtest;

    public static final String MyPREFERENCES = "MyPrefs";
    public static final String Login_Status = "Login_Status_key";
    public static final String first_query = "first_query_key";
    public static final String have_free_credit = "have_free_credit";
    SharedPreferences sharedpreferences;
    String qase_id, comment_id;
    private static final int CAMERA_REQUEST = 1888;
    private static final String TAG = "FileChooserExampleActivity";
    private static final int REQUEST_CODE = 6384; // onActivityResult request

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.askquery2);

        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        Typeface tf = Typeface.createFromAsset(getAssets(), Model.font_name);

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

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.app_color2));
        }

        //------ getting Values ---------------------------
        Intent intent = getIntent();
        qase_id = intent.getStringExtra("qase_id");
        comment_id = intent.getStringExtra("comment_id");

        System.out.println("Get Intent qase_id-----" + qase_id);
        System.out.println("Get Intent comment_id-----" + comment_id);
        //------ getting Values ---------------------------

        Model.upload_files = "";

        TextView tvattach = (TextView) findViewById(R.id.tvattach);
        tvtit = (TextView) findViewById(R.id.tvtit);
        tvmore = (TextView) findViewById(R.id.tvmore);
        tvmore.setTypeface(tf);
        tvattach.setTypeface(tf);
        tvtit.setTypeface(tf);

        scrollView1 = (ScrollView) findViewById(R.id.scrollView1);
        btn_attach = (Button) findViewById(R.id.btn_attach);
        btn_submit = (Button) findViewById(R.id.btn_submit);
        layout_attachfile = (LinearLayout) findViewById(R.id.layout_attachfile);
        takephoto_layout = (LinearLayout) findViewById(R.id.takephoto_layout);
        browse_layout = (LinearLayout) findViewById(R.id.browse_layout);
        file_list = (LinearLayout) findViewById(R.id.file_list);
        files_attached_layout = (LinearLayout) findViewById(R.id.files_attached_layout);

        initImageLoader();
        // init0();

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

     /*   EasyImage.configuration(this)
                .setImagesFolderName("Attachments")
                .setCopyTakenPhotosToPublicGalleryAppFolder(true)
                .setCopyPickedImagesToPublicGalleryAppFolder(true)
                .setAllowMultiplePickInGallery(true);*/
        //------------------ Initialize File Attachment ---------------------------------


        btn_attach.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                attach_dialog();

/*                try {
                    File_Picking_Test sintent = new File_Picking_Test();
                    sintent.get_Image(Qases_Post2.this, "Take");
                } catch (Exception e) {
                    e.printStackTrace();
                }*/

            }
        });

        btn_submit.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                try {
                    json_qase_post = new JSONObject();
                    json_qase_post.put("user_id", (Model.id));
                    json_qase_post.put("case_id", qase_id);

                    System.out.println("json_qase_post---" + json_qase_post.toString());

                    new JSONPostQuery().execute(json_qase_post);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void initImageLoader() {

        /*try {
            DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
                    .cacheOnDisc().imageScaleType(ImageScaleType.EXACTLY_STRETCHED)
                    .bitmapConfig(Bitmap.Config.RGB_565).build();
            ImageLoaderConfiguration.Builder builder = new ImageLoaderConfiguration.Builder(
                    this).defaultDisplayImageOptions(defaultOptions).memoryCache(
                    new WeakMemoryCache());

            ImageLoaderConfiguration config = builder.build();
            imageLoader = ImageLoader.getInstance();
            imageLoader.init(config);
        } catch (Exception e) {
            e.printStackTrace();
        }*/
    }

    private class JSONPostQuery extends AsyncTask<JSONObject, Void, Boolean> {

        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            dialog = new ProgressDialog(Qases_Post2.this);
            dialog.setMessage("Submitting, please wait");
            dialog.show();
            dialog.setCancelable(false);
        }

        @Override
        protected Boolean doInBackground(JSONObject... urls) {
            try {
                JSONParser jParser = new JSONParser();
                jsonobj = jParser.JSON_POST(urls[0], "PostQaseFinal");

                System.out.println("Parameters---------------" + urls[0]);
                System.out.println("Response jsonobj---------------" + jsonobj.toString());

                return true;
            } catch (Exception e) {
                e.printStackTrace();
            }

            return false;
        }

        protected void onPostExecute(Boolean result) {
            try {

                if (jsonobj.has("token_status")) {
                    String token_status = jsonobj.getString("token_status");
                    if (token_status.equals("0")) {
                        finishAffinity();
                        Intent intent = new Intent(Qases_Post2.this, LoginActivity.class);
                        startActivity(intent);
                        finish();
                    }
                } else {

                    status_val = jsonobj.getString("status");

                    System.out.println("status_val--------" + status_val);

                    if (status_val.equals("1")) {
                        alertdia("Your Case has been submitted successfully..!");
                    } else {
                        alertdia("Your Case submission Failed..!");
                    }


                }

                dialog.cancel();

            } catch (Exception e) {
                System.out.println("Exception Post Query1----" + e.toString());
                e.printStackTrace();
            }
        }
    }


    @Override
    protected void onResume() {

        super.onResume();
        System.out.println("Model.upload_files--------------" + Model.upload_files);
        //file_list.removeAllViews();

        if (!(Model.upload_files).equals("")) {

            //------------------------------------
            LayoutInflater layoutInflater = (LayoutInflater) getBaseContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            final View addView = layoutInflater.inflate(R.layout.upload_file_list, null);
            TextView tv_quest = (TextView) addView.findViewById(R.id.tv_quest);
            ImageView close_button = (ImageView) addView.findViewById(R.id.close_button);
            thumb_img = (ImageView) addView.findViewById(R.id.imageView4);
            tv_attach_url = (TextView) addView.findViewById(R.id.tv_attach_url);
            tv_attach_id = (TextView) addView.findViewById(R.id.tv_attach_id);

            tv_quest.setText(Model.upload_files);
            tv_attach_id.setText(Model.attach_id);
            tv_attach_url.setText(Model.attach_file_url);
            thumb_img.setImageBitmap(BitmapFactory.decodeFile(Model.local_file_url));

            System.out.println("Model.upload_files-----------" + (Model.upload_files));
            System.out.println("Model.attach_qid-----------" + (Model.attach_qid));
            System.out.println("Model.attach_id-----------" + (Model.attach_id));
            System.out.println("Model.attach_file_url-----------" + (Model.attach_file_url));

            close_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    View parent = (View) v.getParent();
                    //View grand_parent = (View)parent.getParent();

                    tv_attach_id = (TextView) parent.findViewById(R.id.tv_attach_id);
                    String attid = tv_attach_id.getText().toString();

                    //------------------------------------------------------------
                    String url = Model.BASE_URL + "/sapp/removeQAttachment?os_type=android&user_id=" + (Model.id) + "&attach_id=" + attid + "&token=" + Model.token;
                    System.out.println("Remover Attach url-------------" + url);
                    new JSON_remove_file().execute(url);
                    //------------------------------------------------------------

                    ((LinearLayout) addView.getParent()).removeView(addView);
                    System.out.println("Removed attach_id-----------" + attid);
                }
            });

            thumb_img.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    String att_url = tv_attach_url.getText().toString();
                    Intent i = new Intent(Intent.ACTION_VIEW);
                    i.setData(Uri.parse(att_url));
                    startActivity(i);
                }
            });

            file_list.addView(addView);
            //Model.upload_files = "";
            //------------------------------------

            layout_attachfile.setVisibility(View.VISIBLE);
        }

        Model.upload_files = "";
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        //getMenuInflater().inflate(R.menu.ask_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == android.R.id.home) {

            finish();

            return true;

        }


        return super.

                onOptionsItemSelected(item);
    }


    private class JSON_Prepare_inv extends AsyncTask<String, Void, Boolean> {

        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            dialog = new ProgressDialog(Qases_Post2.this);
            dialog.setMessage("Please wait");
            dialog.show();
            dialog.setCancelable(false);
        }

        @Override
        protected Boolean doInBackground(String... urls) {

            try {
                JSONParser jParser = new JSONParser();
                jsonobj_prepinv = jParser.getJSONFromUrl(urls[0]);

                System.out.println("jsonobj--------" + jsonobj_prepinv.toString());

                return true;
            } catch (Exception e) {
                e.printStackTrace();
            }

            return false;
        }

        protected void onPostExecute(Boolean result) {

            dialog.cancel();
            Model.query_launch = "Askquery2";

            try {

                if (jsonobj_prepinv.has("token_status")) {
                    String token_status = jsonobj_prepinv.getString("token_status");
                    if (token_status.equals("0")) {
                        //============================================================
                        SharedPreferences.Editor editor = sharedpreferences.edit();
                        editor.putString(Login_Status, "0");
                        editor.apply();
                        //============================================================
                        finishAffinity();
                        Intent intent = new Intent(Qases_Post2.this, LoginActivity.class);
                        startActivity(intent);
                        finish();
                    }

                } else {

                    inv_id = jsonobj_prepinv.getString("id");
                    inv_fee = jsonobj_prepinv.getString("fee");
                    inv_strfee = jsonobj_prepinv.getString("str_fee");

                    System.out.println("inv_id--------" + inv_id);
                    System.out.println("inv_fee--------" + (inv_fee));
                    System.out.println("inv_strfee--------" + inv_strfee);


                    if (!(inv_id).equals("0")) {

                        Model.have_free_credit = "0";

                       /* //----------------- Kissmetrics ----------------------------------

                        //----------- Flurry -------------------------------------------------
                        Map<String, String> articleParams = new HashMap<String, String>();
                        articleParams.put("Query_id:", qid);
                        articleParams.put("Invoice_id:", inv_id);
                        articleParams.put("Invoice_fee:", inv_strfee);
                        FlurryAgent.logEvent("android.patient.Query_Submit_Success", articleParams);
                        //----------- Flurry -------------------------------------------------

                        //------------ Google firebase Analitics--------------------
                        Model.mFirebaseAnalytics = FirebaseAnalytics.getInstance(getApplicationContext());
                        Bundle params = new Bundle();
                        params.putString("User", Model.id);
                        params.putString("Query_id", qid);
                        params.putString("Invoice_id", inv_id);
                        params.putString("Invoice_fee", inv_strfee);
                        Model.mFirebaseAnalytics.logEvent("Query_Submit_Success", params);
                        //------------ Google firebase Analitics--------------------
*/
                        ((android.os.ResultReceiver) getIntent().getParcelableExtra("finisher")).send(1, new Bundle());

                    /*    Intent intent = new Intent(Qases_Post2.this, Invoice_Page_New.class);
                        intent.putExtra("qid", qid);
                        intent.putExtra("inv_id", inv_id);
                        intent.putExtra("inv_strfee", inv_strfee);
                        intent.putExtra("type", "query");
                        startActivity(intent);
                        finish();
*/
                        //overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);

                    } else {

                        Model.have_free_credit = "0";

                        Toast.makeText(getApplicationContext(), "Your query has been posted successfully.", Toast.LENGTH_SHORT).show();

                        //============================================================
                        SharedPreferences.Editor editor = sharedpreferences.edit();
                        editor.putString(have_free_credit, "0");
                        editor.apply();
                        //============================================================


                        System.out.println("query_id--------------" + qid);

                        ((android.os.ResultReceiver) getIntent().getParcelableExtra("finisher")).send(1, new Bundle());

                        /*Intent i = new Intent(Qases_Post2.this, QueryViewActivity.class);
                        i.putExtra("qid", qid);
                        startActivity(i);
                        finish();*/
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }


    private class JSON_remove_file extends AsyncTask<String, Void, Boolean> {

        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            dialog = new ProgressDialog(Qases_Post2.this);
            dialog.setMessage("Please wait..");
            dialog.show();
            dialog.setCancelable(false);
        }

        @Override
        protected Boolean doInBackground(String... urls) {

            try {
                (new JSONParser()).getJSONFromUrl(urls[0]);

                return true;
            } catch (Exception e) {
                e.printStackTrace();
            }

            return false;
        }

        protected void onPostExecute(Boolean result) {

            dialog.dismiss();

        }
    }

/*    public void attach_dialog() {
        List<String> mAnimals = new ArrayList<String>();
        mAnimals.add("Take Photo");
        //mAnimals.add("Attach Images");
        mAnimals.add("Browse Files");

        //Create sequence of items
        final CharSequence[] Animals = mAnimals.toArray(new String[mAnimals.size()]);
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        dialogBuilder.setTitle("Attach Files/Images");
        dialogBuilder.setItems(Animals, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int item) {
                String selectedText = Animals[item].toString();
                System.out.println("selectedText---" + selectedText);

                if (selectedText.equals("Take Photo")) {
                    check_Camera_Permissions();
                } else {
                    check_Permissions_Open_FilePicker();
                }
            }
        });

        AlertDialog alertDialogObject = dialogBuilder.create();
        alertDialogObject.show();
    }*/


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

                    int permissionCheck = ContextCompat.checkSelfPermission(Qases_Post2.this, Manifest.permission.CAMERA);
                    if (permissionCheck == PackageManager.PERMISSION_GRANTED) {
                        EasyImage.openCamera(Qases_Post2.this, 0);
                    } else {
                        Nammu.askForPermission(Qases_Post2.this, Manifest.permission.CAMERA, new PermissionCallback() {
                            @Override
                            public void permissionGranted() {
                                EasyImage.openCamera(Qases_Post2.this, 0);
                            }

                            @Override
                            public void permissionRefused() {

                            }
                        });
                    }

                } else {
                    //showChooser();

                    int permissionCheck = ContextCompat.checkSelfPermission(Qases_Post2.this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
                    if (permissionCheck == PackageManager.PERMISSION_GRANTED) {
                        EasyImage.openDocuments(Qases_Post2.this, 0);
                    } else {
                        Nammu.askForPermission(Qases_Post2.this, Manifest.permission.WRITE_EXTERNAL_STORAGE, new PermissionCallback() {
                            @Override
                            public void permissionGranted() {
                                EasyImage.openDocuments(Qases_Post2.this, 0);
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

    private void showChooser() {
        try {
            Intent target = FileUtils.createGetContentIntent();
            Intent intent = Intent.createChooser(
                    target, getString(R.string.chooser_title));
            startActivityForResult(intent, REQUEST_CODE);

        } catch (ActivityNotFoundException e) {
            e.printStackTrace();
        }
    }

    private class AsyncTask_fileupload extends AsyncTask<String, Void, Boolean> {

        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            dialog = new ProgressDialog(Qases_Post2.this);
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


                if (!(last_upload_file).equals("")) {

                    //------------------------------------
                    LayoutInflater layoutInflater = (LayoutInflater) getBaseContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    final View addView = layoutInflater.inflate(R.layout.qase_upload_file_list, null);
                    TextView tv_quest = (TextView) addView.findViewById(R.id.tv_quest);
                    ImageView close_button = (ImageView) addView.findViewById(R.id.close_button);

                    thumb_img = (ImageView) addView.findViewById(R.id.imageView4);
                    tv_attach_url = (TextView) addView.findViewById(R.id.tv_attach_url);
                    tv_attach_id = (TextView) addView.findViewById(R.id.tv_attach_id);

                    tv_quest.setText(last_upload_file);
/*                    tv_attach_id.setText("");
                    tv_attach_url.setText("");*/
                    thumb_img.setImageBitmap(BitmapFactory.decodeFile(local_url));

                    System.out.println("Model.upload_files-----------" + (last_upload_file));
/*                    System.out.println("Model.attach_qid-----------" + (attach_qid));
                    System.out.println("Model.attach_id-----------" + (attach_id));*/
                    System.out.println("Model.attach_file_url-----------" + (attach_file_url));
                    System.out.println("Model.attach_file_url-----------" + (selectedPath));

                    close_button.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            View parent = (View) v.getParent();
                            //View grand_parent = (View)parent.getParent();

                            tv_attach_id = (TextView) parent.findViewById(R.id.tv_attach_id);
                            String attid = tv_attach_id.getText().toString();

                            String url = Model.BASE_URL + "/sapp/removeQAttachment?os_type=android&user_id=" + (Model.id) + "&attach_id=" + attid + "&token=" + Model.token;
                            System.out.println("Remover Attach url-------------" + url);
                            new JSON_remove_file().execute(url);

                            System.out.println("Removed attach_id-----------" + attid);
                            ((LinearLayout) addView.getParent()).removeView(addView);
                        }
                    });
                    thumb_img.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
/*
                            String att_url = tv_attach_url.getText().toString();
                            Intent i = new Intent(Intent.ACTION_VIEW);
                            i.setData(Uri.parse(att_url));
                            startActivity(i);
                            */
                            preview_image(local_url);

                        }
                    });

                    file_list.addView(addView);
                    //------------------------------------

                } else {
                    //scrollView1.setVisibility(View.GONE);
                }
                last_upload_file = "";

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
        int maxBufferSize = 1024 * 1024;
        File sourceFile = new File(fullpath);

        if (!sourceFile.isFile()) {
            System.out.println("Source File not exist :" + fullpath);
            return "";
        } else {

            try {

                //upLoadServerUri = Model.BASE_URL + "/sapp/upload?user_id=" + (Model.id) + "&qid=" + (qid) + "&token=" + Model.token;
                //System.out.println("upLoadServerUri---------------------" + upLoadServerUri);

                upLoadServerUri = Model.BASE_URL + "sapp/caseUpload?os_type=android&comment_id=" + comment_id + "&user_id=" + Model.id + "&case_id=" + qase_id + "&token=" + Model.token;
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
                int length = 35500;
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


/*    public String convertInputStreamToString(InputStream stream, int length) throws IOException {
        Reader reader = null;
        reader = new InputStreamReader(stream, "UTF-8");
        char[] buffer = new char[length];
        reader.read(buffer);
        return new String(buffer);
    }*/

    public void preview_image(String file_url) {

        try {

            sel_filename = file_url.substring(file_url.lastIndexOf("/") + 1);

            final MaterialDialog alert = new MaterialDialog(Qases_Post2.this);
            View view = LayoutInflater.from(Qases_Post2.this).inflate(R.layout.image_preview, null);
            alert.setView(view);
            alert.setTitle("");

            final ImageView image1 = (ImageView) view.findViewById(R.id.image1);
            final ImageView image_close = (ImageView) view.findViewById(R.id.image_close);
            final Button btn_upload = (Button) view.findViewById(R.id.btn_upload);


            System.out.println("sel_filename.endsWith(\".jpg\")----------" + file_url.endsWith(".jpg"));
            System.out.println("sel_filename.endsWith(\".pdf\")----------" + file_url.endsWith(".pdf"));

            image1.setImageBitmap(BitmapFactory.decodeFile(file_url));

          /*  //------------ Audio & Video -----------------------------------
            if (sel_filename.endsWith(".mp3"))
                image1.setBackgroundResource(R.mipmap.multimedia_file);
            if (sel_filename.endsWith(".wav"))
                image1.setBackgroundResource(R.mipmap.multimedia_file);
            if (sel_filename.endsWith(".m4a"))
                image1.setBackgroundResource(R.mipmap.multimedia_file);
            if (sel_filename.endsWith(".m4b"))
                image1.setBackgroundResource(R.mipmap.multimedia_file);
            if (sel_filename.endsWith(".m4p"))
                image1.setBackgroundResource(R.mipmap.multimedia_file);
            if (sel_filename.endsWith(".wma"))
                image1.setBackgroundResource(R.mipmap.multimedia_file);
            if (sel_filename.endsWith(".dat"))
                image1.setBackgroundResource(R.mipmap.multimedia_file);
            if (sel_filename.endsWith(".mpeg"))
                image1.setBackgroundResource(R.mipmap.multimedia_file);
            if (sel_filename.endsWith(".mp4"))
                image1.setBackgroundResource(R.mipmap.multimedia_file);
            //------------ Audio & Video -----------------------------------
            if (sel_filename.endsWith(".pdf")) image1.setBackgroundResource(R.mipmap.pdf_file);
            if (sel_filename.endsWith(".doc")) image1.setBackgroundResource(R.mipmap.text_file);
            if (sel_filename.endsWith(".xls")) image1.setBackgroundResource(R.mipmap.text_file);
            if (sel_filename.endsWith(".zip")) image1.setBackgroundResource(R.mipmap.zip_file);
            if (sel_filename.endsWith(".rar")) image1.setBackgroundResource(R.mipmap.zip_file);
            //------------ Doc -----------------------------------
*/

            image_close.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    alert.dismiss();
                }
            });

            btn_upload.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    alert.dismiss();
                }
            });

            alert.setCanceledOnTouchOutside(true);
            alert.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void dumpIntent(Intent i) {

        Bundle bundle = i.getExtras();
        if (bundle != null) {
            Set<String> keys = bundle.keySet();
            Iterator<String> it = keys.iterator();
            while (it.hasNext()) {
                String key = it.next();
                System.out.println("Data------>" + "[" + key + "=" + bundle.get(key) + "]");
            }
        }
    }


    public static String intentToString(Intent intent) {
        if (intent == null) {
            return null;
        }

        return intent.toString() + " " + bundleToString(intent.getExtras());
    }

    public static String bundleToString(Bundle bundle) {
        StringBuilder out = new StringBuilder("Bundle[");

        if (bundle == null) {
            out.append("null");
        } else {
            boolean first = true;
            for (String key : bundle.keySet()) {
                if (!first) {
                    out.append(", ");
                }

                out.append(key).append('=');

                Object value = bundle.get(key);

                if (value instanceof int[]) {
                    out.append(Arrays.toString((int[]) value));
                } else if (value instanceof byte[]) {
                    out.append(Arrays.toString((byte[]) value));
                } else if (value instanceof boolean[]) {
                    out.append(Arrays.toString((boolean[]) value));
                } else if (value instanceof short[]) {
                    out.append(Arrays.toString((short[]) value));
                } else if (value instanceof long[]) {
                    out.append(Arrays.toString((long[]) value));
                } else if (value instanceof float[]) {
                    out.append(Arrays.toString((float[]) value));
                } else if (value instanceof double[]) {
                    out.append(Arrays.toString((double[]) value));
                } else if (value instanceof String[]) {
                    out.append(Arrays.toString((String[]) value));
                } else if (value instanceof CharSequence[]) {
                    out.append(Arrays.toString((CharSequence[]) value));
                } else if (value instanceof Parcelable[]) {
                    out.append(Arrays.toString((Parcelable[]) value));
                } else if (value instanceof Bundle) {
                    out.append(bundleToString((Bundle) value));
                } else {
                    out.append(value);
                }

                first = false;
            }
        }

        out.append("]");
        return out.toString();
    }


    public String getPath(Uri uri) {

        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor = managedQuery(uri, projection, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
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
                    File photoFile = EasyImage.lastlyTakenButCanceledPhoto(Qases_Post2.this);
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
/*
            //----------------- Kissmetrics ----------------------------------
            Model.kiss = KISSmetricsAPI.sharedAPI(Model.kissmetric_apikey, getApplicationContext());
            Model.kiss.record("android.patient.Attach_Take_Photo");
            HashMap<String, String> properties = new HashMap<String, String>();
            properties.put("android.patient.Qid", (attach_qid));
            properties.put("android.patient.attach_file_path", selectedPath);
            properties.put("android.patient.attach_filename", selectedfilename);
            Model.kiss.set(properties);
            //----------------- Kissmetrics ----------------------------------

            //----------- Flurry -------------------------------------------------
            Map<String, String> articleParams = new HashMap<String, String>();
            articleParams.put("android.patient.Qid", (attach_qid));
            articleParams.put("android.patient.attach_file_path", selectedPath);
            articleParams.put("android.patient.attach_filename", selectedfilename);
            FlurryAgent.logEvent("android.patient.Attach_Take_Photo", articleParams);
            //----------- Flurry -------------------------------------------------

            //------------ Google firebase Analitics--------------------
            Model.mFirebaseAnalytics = FirebaseAnalytics.getInstance(getApplicationContext());
            Bundle params = new Bundle();
            params.putString("User", Model.id);
            params.putString("Qid", attach_qid);
            params.putString("attach_file_path", selectedPath);
            params.putString("attach_filename", selectedfilename);
            Model.mFirebaseAnalytics.logEvent("Attach_Files", params);
            //------------ Google firebase Analitics--------------------*/

            new AsyncTask_fileupload().execute(selectedPath);

        }

    }

    @Override
    protected void onDestroy() {
        // EasyImage.clearConfiguration(this);
        super.onDestroy();
    }

    public void alertdia(String diamsg) {
        final Context context = this;
        android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(context);
        alertDialogBuilder
                .setMessage(diamsg)
                .setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

/*                        ((android.os.ResultReceiver) getIntent().getParcelableExtra("finisher")).send(1, new Bundle());
                        Intent intent = new Intent(Qases_Post2.this, QasesActivity.class);
                        intent.putExtra("qtype", "myfeeds");
                        startActivity(intent);*/

                        //((android.os.ResultReceiver) getIntent().getParcelableExtra("finisher")).send(1, new Bundle());

                        finish();

                        Intent intent = new Intent(Qases_Post2.this, QasesActivity.class);
                        intent.putExtra("qtype", "myfeeds");
                        intent.putExtra("finisher", new android.os.ResultReceiver(null) {
                            @Override
                            protected void onReceiveResult(int resultCode, Bundle resultData) {
                                finish();
                            }
                        });

                        startActivityForResult(intent, 1);
                        overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);

/*                     Intent intent = new Intent(Qases_Post2.this, QasesActivity.class);
                        intent.putExtra("qtype", "myfeeds");
                        startActivity(intent);
*/
                    }
                });

        android.app.AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    private String upload_file(String file_path) {

        last_upload_file =file_path;

        if (comment_id != null && !comment_id.isEmpty() && !comment_id.equals("null") && !comment_id.equals("")){

        }
        else{
            comment_id="0";
        }

        String ServerUploadPath = Model.BASE_URL + "sapp/caseUpload?os_type=android&comment_id=" + comment_id + "&user_id=" + Model.id + "&case_id=" + qase_id + "&token=" + Model.token;
        System.out.println("upLoadServerUri---------------------" + ServerUploadPath);

        //String ServerUploadPath = Model.BASE_URL + "/sapp/upload?user_id=" + (Model.id) + "&qid=" + (qid) + "&token=" + Model.token;
        //System.out.println("ServerUploadPath--" + ServerUploadPath);

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
