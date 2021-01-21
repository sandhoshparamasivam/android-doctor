package com.orane.docassist;

import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewSwitcher;

import com.flurry.android.FlurryAgent;

import com.orane.docassist.Model.Model;
import com.orane.docassist.Model.MultipartEntity2;
import com.orane.docassist.Network.JSONParser;
import com.orane.docassist.R;
import com.orane.docassist.file_picking.utils.FileUtils;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import me.drakeet.materialdialog.MaterialDialog;


public class QaseComment_Post2 extends AppCompatActivity {

    View addView;
    GridView gridGallery;
    Handler handler;
    ImageView imgSinglePick;
    Button btnGalleryPick;
    Button btnGalleryPickMul;

    String action;
    ViewSwitcher viewSwitcher;

    InputStream is = null;
    int serverResponseCode = 0;
    ArrayList<String> imagePaths;
    ImageView thumb_img;
    View recc_vi;
    private static final int FILE_SELECT_CODE = 0;
    Uri selectedImageUri;
    LinearLayout layout_attachfile, file_list, takephoto_layout, browse_layout;
    public String comment_id, qase_id, status_val, prevhist, curmedi, pastmedi, labtest, serverResponseMessage, selectedPath, inv_id, inv_fee, inv_strfee, status_postquery, qid, sel_filename, last_upload_file, attach_status, attach_file_url, attach_filename, local_url, contentAsString, upLoadServerUri, attach_id, attach_qid, upload_response, image_path, selectedfilename;
    Button btn_attach, btn_submit;
    public JSONObject jsonobj, json;
    Toolbar toolbar;
    ScrollView scrollView1;
    TextView tvtit, tvmore, tv_attach_id, tv_attach_url;

    public static final String MyPREFERENCES = "MyPrefs";
    public static final String Login_Status = "Login_Status_key";
    public static final String first_query = "first_query_key";
    public static final String have_free_credit = "have_free_credit";
    SharedPreferences sharedpreferences;

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
            getSupportActionBar().setTitle("Attach Files");
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.app_color2));
        }

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

        //------ getting Values ---------------------------
        Intent intent = getIntent();
        comment_id = intent.getStringExtra("comment_id");
        qase_id = intent.getStringExtra("qase_id");

        System.out.println("Get Intent qase_id-----" + qase_id);
        System.out.println("Get Intent comment_id-----" + comment_id);
        //------ getting Values ---------------------------

        initImageLoader();
        //init();


        btn_attach.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                attach_dialog();

            }
        });
        btn_submit.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                try {
                    alertdia("Your Qases Submitted Successfully..!");

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }


    private void initImageLoader() {
/*
        DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
                .cacheOnDisc().imageScaleType(ImageScaleType.EXACTLY_STRETCHED)
                .bitmapConfig(Bitmap.Config.RGB_565).build();
        ImageLoaderConfiguration.Builder builder = new ImageLoaderConfiguration.Builder(
                this).defaultDisplayImageOptions(defaultOptions).memoryCache(
                new WeakMemoryCache());

        ImageLoaderConfiguration config = builder.build();
        imageLoader = ImageLoader.getInstance();
        imageLoader.init(config);*/
    }

   /* private void init() {


        handler = new Handler();
        gridGallery = (GridView) findViewById(R.id.gridGallery);
        gridGallery.setFastScrollEnabled(true);
        adapter = new CustomGalleryAdapter(getApplicationContext(), imageLoader);
        adapter.setMultiplePick(false);
        gridGallery.setAdapter(adapter);

        gridGallery.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Toast.makeText(Qases_Post2.this, "" + imagePaths.get(i), Toast.LENGTH_LONG).show();
            }
        });

        viewSwitcher = (ViewSwitcher) findViewById(R.id.viewSwitcher);
        viewSwitcher.setDisplayedChild(1);

        imgSinglePick = (ImageView) findViewById(R.id.imgSinglePick);
        imgSinglePick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (imagePaths != null)// if minimum image is choose
                    Toast.makeText(Qases_Post2.this, "" + imagePaths.get(0), Toast.LENGTH_LONG).show();
            }
        });

        btnGalleryPick = (Button) findViewById(R.id.btnGalleryPick);
        btnGalleryPick.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                Intent i = new Intent(Action.ACTION_PICK);
                startActivityForResult(i, 110);

            }
        });

        btnGalleryPickMul = (Button) findViewById(R.id.btnGalleryPickMul);
        btnGalleryPickMul.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent i = new Intent(Action.ACTION_MULTIPLE_PICK);
                startActivityForResult(i, 200);
            }
        });
    }
*/

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

            try {

                //----------- Flurry -------------------------------------------------
                Map<String, String> articleParams = new HashMap<String, String>();
                articleParams.put("android.doc.tv_attach_url:", Model.attach_file_url);
                FlurryAgent.logEvent("android.doc.Qase_file_upload", articleParams);
                //----------- Flurry -------------------------------------------------

            } catch (Exception e) {
                e.printStackTrace();
            }


            /*close_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    View parent = (View) v.getParent();
                    //View grand_parent = (View)parent.getParent();

                    tv_attach_id = (TextView) parent.findViewById(R.id.tv_attach_id);
                    String attid = tv_attach_id.getText().toString();

                    String url = Model.BASE_URL + "/sapp/removeQAttachment?user_id=" + (Model.id) + "&attach_id=" + attid + "&token=" + Model.token;
                    System.out.println("Remover Attach url-------------" + url);
                    new JSON_remove_file().execute(url);

                    System.out.println("Removed attach_id-----------" + attid);
                    ((LinearLayout) addView.getParent()).removeView(addView);
                }
            });*/

          /*  thumb_img.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    String att_url = tv_attach_url.getText().toString();
                    Intent i = new Intent(Intent.ACTION_VIEW);
                    i.setData(Uri.parse(att_url));
                    startActivity(i);

                }
            });
*/
            file_list.addView(addView);
            //Model.upload_files = "";
            //------------------------------------
        }

        Model.upload_files = "";
    }


    /* public void onActivityResult(int requestCode, int resultCode, Intent data) {

         System.out.println("resultCode------------" + resultCode);
         System.out.println("requestCode------------" + requestCode);

         imagePaths = new ArrayList<String>();

         try {



             // ------------ Browse Photo -------------------------------
             if (requestCode == 110) {

                 adapter.clear();

                 if (data.getData() != null) {
                     selectedImageUri = data.getData();

                     selectedPath = getPath(selectedImageUri);
                     selectedfilename = selectedPath.substring(selectedPath.lastIndexOf("/") + 1);

                     System.out.println("selectedPath-------" + selectedPath);
                     System.out.println("selectedfilename-------" + selectedfilename);
                 } else {
                     System.out.println("Else get data---");
                 }

                 viewSwitcher.setDisplayedChild(1);
                 String single_path = data.getStringExtra("single_path");
                 imagePaths.add(single_path);
                 imageLoader.displayImage("file://" + single_path, imgSinglePick);

                 image_path = single_path;
                 System.out.println("single_path------" + single_path);

                 //----------------- Kissmetrics ----------------------------------
                 Model.kiss = KISSmetricsAPI.sharedAPI(Model.kissmetric_apikey, getApplicationContext());
                 Model.kiss.record("android.doc.qases_browse_image");
                 //----------------- Kissmetrics ----------------------------------

                 new AsyncTask_fileupload().execute(single_path);
             }
         } catch (Exception e) {
             System.out.println("Exception Attachments----" + e.toString());
         }
     }
 */
    public String getPath(Uri uri) {

        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor = managedQuery(uri, projection, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // getMenuInflater().inflate(R.menu.ask_menu, menu);
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


    class JSON_remove_file extends AsyncTask<String, Void, Boolean> {

        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            dialog = new ProgressDialog(QaseComment_Post2.this);
            dialog.setMessage("Please wait..");
            dialog.show();
            dialog.setCancelable(false);
        }

        @Override
        protected Boolean doInBackground(String... urls) {

            try {

                JSONParser jParser = new JSONParser();
                JSONObject jsonobj = jParser.getJSONFromUrl(urls[0]);

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

    public void attach_dialog() {
        List<String> mAnimals = new ArrayList<String>();
        mAnimals.add("Take Photo");
        mAnimals.add("Browse Images/Files");
        //mAnimals.add("Attach Files");

        final CharSequence[] Animals = mAnimals.toArray(new String[mAnimals.size()]);
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        dialogBuilder.setTitle("Attach Files/Images");
        dialogBuilder.setItems(Animals, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int item) {
                String selectedText = Animals[item].toString();
                System.out.println("selectedText---" + selectedText);

                if (selectedText.equals("Take Photo")) {

                    try {
                        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        startActivityForResult(cameraIntent, CAMERA_REQUEST);
                    } catch (Exception e) {
                        e.printStackTrace();
                        System.out.println("Exception Take Photo----- " + e.toString());
                    }

                } else {
                    showChooser();
                }
            }
        });

        AlertDialog alertDialogObject = dialogBuilder.create();
        alertDialogObject.show();
    }


    class AsyncTask_fileupload extends AsyncTask<String, Void, Boolean> {

        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            dialog = new ProgressDialog(QaseComment_Post2.this);
            dialog.setMessage("Uploading. Please wait...");
            dialog.show();
            dialog.setCancelable(false);
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

                    /*close_button.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            View parent = (View) v.getParent();
                            //View grand_parent = (View)parent.getParent();

                            tv_attach_id = (TextView) parent.findViewById(R.id.tv_attach_id);
                            String attid = tv_attach_id.getText().toString();

                            String url = Model.BASE_URL + "/sapp/removeQAttachment?user_id=" + (Model.id) + "&attach_id=" + attid + "&token=" + Model.token;
                            System.out.println("Remover Attach url-------------" + url);
                            new JSON_remove_file().execute(url);

                            System.out.println("Removed attach_id-----------" + attid);
                            ((LinearLayout) addView.getParent()).removeView(addView);
                        }
                    });
*/
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


 /*   public String upload_file(String fullpath) {

        String fpath_filename = fullpath.substring(fullpath.lastIndexOf("/") + 1);

        local_url = fullpath;
        System.out.println("fpath---------" + fullpath);
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

                upLoadServerUri = Model.BASE_URL + "/icliniq/web/index.php/sapp/CaseUpload?user_id=" + Model.id + "&token=" + Model.token + "&case_id=" + qase_id + "&comment_Id=" + comment_id;
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


                // create a buffer of  maximum size
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

                // Responses from the server (code and message)
                serverResponseCode = conn.getResponseCode();
                serverResponseMessage = conn.getResponseMessage();


                // JSON Response on Server
                int response = conn.getResponseCode();
                System.out.println("response-------" + response);
                is = conn.getInputStream();
                int length = 25500;
                contentAsString = convertInputStreamToString(is, length);
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
    public String convertInputStreamToString(InputStream stream, int length) throws IOException {
        Reader reader = null;
        reader = new InputStreamReader(stream, "UTF-8");
        char[] buffer = new char[length];
        reader.read(buffer);
        return new String(buffer);
    }

    public void preview_image(String file_url) {

        try {

            sel_filename = file_url.substring(file_url.lastIndexOf("/") + 1);

            final MaterialDialog alert = new MaterialDialog(QaseComment_Post2.this);
            View view = LayoutInflater.from(QaseComment_Post2.this).inflate(R.layout.image_preview, null);
            alert.setView(view);
            alert.setTitle("");

            final ImageView image1 = (ImageView) view.findViewById(R.id.image1);
            final ImageView image_close = (ImageView) view.findViewById(R.id.image_close);
            final Button btn_upload = (Button) view.findViewById(R.id.btn_upload);


            System.out.println("sel_filename.endsWith(\".jpg\")----------" + file_url.endsWith(".jpg"));
            System.out.println("sel_filename.endsWith(\".pdf\")----------" + file_url.endsWith(".pdf"));

            image1.setImageBitmap(BitmapFactory.decodeFile(file_url));

            //------------ Audio & Video -----------------------------------
           /* if (sel_filename.endsWith(".mp3"))
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
            //------------ Doc -----------------------------------*/

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
            System.out.println("Exception File Preview----" + e.toString());
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

    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }


    public void alertdia(String diamsg) {
        final Context context = this;
        android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(context);
        alertDialogBuilder
                .setMessage(diamsg)
                .setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        ((android.os.ResultReceiver) getIntent().getParcelableExtra("finisher")).send(1, new Bundle());
                        Intent intent = new Intent(QaseComment_Post2.this, QasesActivity.class);
                        intent.putExtra("qtype", "myfeeds");
                        startActivity(intent);
                        finish();
                    }
                });

        android.app.AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    private void showChooser() {
        Intent target = FileUtils.createGetContentIntent();
        Intent intent = Intent.createChooser(
                target, getString(R.string.chooser_title));
        try {
            startActivityForResult(intent, REQUEST_CODE);
        } catch (ActivityNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {

            case CAMERA_REQUEST:
                if (resultCode == RESULT_OK) {

                    // ------------- Take Photo -------------------------------
                    Bitmap mphoto = (Bitmap) data.getExtras().get("data");

                    Uri tempUri = getImageUri(getApplicationContext(), mphoto);
                    selectedPath = getPath(tempUri);
                    selectedfilename = selectedPath.substring(selectedPath.lastIndexOf("/") + 1);

                    System.out.println("selectedPath-------" + selectedPath);
                    System.out.println("selectedfilename-------" + selectedfilename);

                    dumpIntent(data);

                    new AsyncTask_fileupload().execute(selectedPath);
                }

                // ------------- Take Photo -------------------------------
                break;

            case REQUEST_CODE:
                if (resultCode == RESULT_OK) {
                    if (data != null) {
                        final Uri uri = data.getData();
                        try {
                            final String path = FileUtils.getPath(this, uri);
                            Toast.makeText(QaseComment_Post2.this, "File Selected: " + path, Toast.LENGTH_LONG).show();

                            //adapter.clear();


                            new AsyncTask_fileupload().execute(path);

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }


    private String upload_file(String file_path) {

        last_upload_file=file_path;

        if (comment_id != null && !comment_id.isEmpty() && !comment_id.equals("null") && !comment_id.equals("")){
            comment_id="0";
        }


        String ServerUploadPath = Model.BASE_URL + "/sapp/CaseUpload?user_id=" + Model.id + "&token=" + Model.token + "&case_id=" + qase_id + "&comment_Id=" + comment_id;
        System.out.println("ServerUploadPath---------" + ServerUploadPath);

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
