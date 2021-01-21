
package com.orane.docassist;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Vibrator;
import androidx.appcompat.widget.Toolbar;
import android.text.Html;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.flurry.android.FlurryAgent;
import com.github.ksoichiro.android.observablescrollview.ObservableScrollView;
import com.github.ksoichiro.android.observablescrollview.ObservableScrollViewCallbacks;
import com.github.ksoichiro.android.observablescrollview.ScrollState;

import com.orane.docassist.Model.BaseActivity;
import com.orane.docassist.Model.Model;
import com.orane.docassist.Network.JSONParser;
import com.orane.docassist.New_Main.New_MainActivity;
import com.orane.docassist.R;
import com.orane.docassist.attachment_view.GridViewActivity;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


import de.hdodenhof.circleimageview.CircleImageView;
import me.drakeet.materialdialog.MaterialDialog;

public class Qases_View_Activity extends BaseActivity implements ObservableScrollViewCallbacks {

    ObservableScrollView mScrollable;
    RelativeLayout mToolbar;
    public File imageFile;
    ProgressBar progressBar;
    LinearLayout bottom_layout;
    public String report_response, is_best_val, has_star_val, comm_id_val, upvote_val, q_case_url, attach_file_text, filename_text, url_text, is_image_flag, id_val, comment_text, comm_id, comm_text, comm_docname, comm_docphoto, comm_date, q_str_date, q_comments, q_docphoto, q_docname, q_spec, q_desc, q_title, q_id, doc_photo_url, doc_id, doc_name;
    ObservableScrollView scrollview;
    public JSONObject jsonobj_qases;
    public String url, track_id_val, art_title;
    CheckBox checkbox;
    public static final String MyPREFERENCES = "MyPrefs";
    public static final String Login_Status = "Login_Status_key";
    SharedPreferences sharedpreferences;
    public String pass, Log_Status;
    public String q_files_text, qases_id;
    public TextView tv_comm_id, tv_like_count, tv_best, tv_nocomm, tv_filename, tvattached, tv_discc, tv_cdocname, tv_comment, tv_time, tv_spec, tv_date, tv_title, tv_docname, tv_docedu, tv_desc;
    ImageView img_docimg;
    JSONObject jsonobj_files, json_response_obj, jsonobj_comment, json_comment;
    View vi;
    ImageView img_heart_grey, img_heart_blue, img_best_grey, img_best_blue;
    CircleImageView img_doc_photo;
    LinearLayout comments_layout, files_layout, layout_attachfile;
    Button btn_post_submit;
    EditText edt_comment;
    JSONObject json_vote;
    View tv_dot2;
    LinearLayout like_layout, best_layout;

    FrameLayout ad_layout;
    ImageView home_ad, ad_close;
    JSONObject jsonobj_ad;
    RelativeLayout ad_close_layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.qases_view);

        //------------ Object Creations -------------------------------
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setTitle("");

            TextView mTitle = (TextView) toolbar.findViewById(R.id.toolbar_title);
            Typeface khandBold = Typeface.createFromAsset(getApplicationContext().getAssets(), Model.font_name_bold);
            mTitle.setTypeface(khandBold);
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.app_color2));
        }
        //------------ Object Creations -------------------------------

        ad_layout = (FrameLayout) findViewById(R.id.ad_layout);
        home_ad = (ImageView) findViewById(R.id.home_ad);
        ad_close = (ImageView) findViewById(R.id.ad_close);
        ad_close_layout = (RelativeLayout) findViewById(R.id.ad_close_layout);

        mScrollable = (ObservableScrollView) findViewById(R.id.scrollable);
        tv_spec = (TextView) findViewById(R.id.tv_spec);
        tv_date = (TextView) findViewById(R.id.tv_date);
        tv_title = (TextView) findViewById(R.id.tv_title);
        tv_docname = (TextView) findViewById(R.id.tv_docname);
        tv_docedu = (TextView) findViewById(R.id.tv_docedu);
        tv_desc = (TextView) findViewById(R.id.tv_desc);
        tv_discc = (TextView) findViewById(R.id.tv_discc);
        tv_filename = (TextView) findViewById(R.id.tv_filename);
        tv_nocomm = (TextView) findViewById(R.id.tv_nocomm);
        checkbox = (CheckBox) findViewById(R.id.checkbox);

        ad_close_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ad_layout.setVisibility(View.GONE);

                //--------------------Ad- Close--------
                String ad_url = Model.BASE_URL + "/sapp/closeAd?os_type=android&user_id=" + Model.id + "&track_id=" + track_id_val + "&token=" + Model.token;
                System.out.println("ad_url----------" + ad_url);
                new JSON_Close().execute(ad_url);
                //---------------------Ad- Close-------
            }
        });

        tvattached = (TextView) findViewById(R.id.tvattached);
        files_layout = (LinearLayout) findViewById(R.id.files_layout);
        layout_attachfile = (LinearLayout) findViewById(R.id.layout_attachfile);

        img_docimg = (ImageView) findViewById(R.id.img_docimg);
        comments_layout = (LinearLayout) findViewById(R.id.comments_layout);
        btn_post_submit = (Button) findViewById(R.id.btn_post_submit);
        edt_comment = (EditText) findViewById(R.id.edt_comment);

        layout_attachfile.setVisibility(View.GONE);
        /* InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0); */
        edt_comment.clearFocus();

        mScrollable.setScrollViewCallbacks(this);
        mScrollable.fullScroll(ScrollView.FOCUS_UP);

        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        Log_Status = sharedpreferences.getString(Login_Status, "");

        try {
            //------------- getting Values ------------------------------------
            Intent intent = getIntent();
            qases_id = intent.getStringExtra("id");
            System.out.println("Get Intent qases_id----------" + qases_id);
            //------------- getting Values ------------------------------------

            //------------- getting Qases------------------------------------
            String full_url = Model.BASE_URL + "sapp/caseView?os_type=android&id=" + qases_id + "&user_id=" + (Model.id) + "&html=1&token=" + Model.token;
            System.out.println("qases_view_url------------" + full_url);
            new JSONAsyncTask().execute(full_url);
            //------------- getting Qases------------------------------------

        } catch (Exception e) {
            e.printStackTrace();
        }

        btn_post_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                comment_text = edt_comment.getText().toString();

                if (comment_text.equals("")) {
                    edt_comment.setError("Please enter the comment");
                } else {
                    try {
                        json_comment = new JSONObject();
                        json_comment.put("user_id", (Model.id));
                        json_comment.put("case_id", qases_id);
                        json_comment.put("comment", comment_text);

                        System.out.println("json_comment---" + json_comment.toString());

                        new jsonAsync_comment().execute(json_comment);

                        try {

                            //----------- Flurry -------------------------------------------------
                            Map<String, String> articleParams = new HashMap<String, String>();
                            articleParams.put("android.doc.case_id:", qases_id);
                            articleParams.put("android.doc.comment:", comment_text);
                            FlurryAgent.logEvent("android.doc.qase_postComment_success", articleParams);
                            //----------- Flurry -------------------------------------------------
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    } catch (Exception e2) {
                        e2.printStackTrace();
                    }
                }
            }
        });


        checkbox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //is chkIos checked?
                if (((CheckBox) v).isChecked()) {
                    btn_post_submit.setText("Continue");
                } else {
                    btn_post_submit.setText("Submit");
                }
                //case 2
            }
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.qases_view_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == android.R.id.home) {

            if ((Model.launch) != null && !(Model.launch).isEmpty() && !(Model.launch).equals("null") && !(Model.launch).equals("")) {
                if (Model.launch.equals("PushNotificationService")) {
                    Intent intent = new Intent(this, New_MainActivity.class);
                    startActivity(intent);
                    finish();
                    Model.launch = "";
                } else {
                    Model.launch = "";
                    finish();
                }
            } else {
                Model.launch = "";
                finish();
            }

            return true;
        }

        if (id == R.id.nav_postcomment) {
            mScrollable.fullScroll(ScrollView.FOCUS_DOWN);
            return true;
        }

        if (id == R.id.nav_qshare) {
            //TakeScreenshot_Share();

            Intent sharingIntent = new Intent(Intent.ACTION_SEND);
            sharingIntent.setType("text/plain");
            sharingIntent.putExtra(Intent.EXTRA_TEXT, "Discuss medical cases with icliniq doctors for \n\n" + q_case_url);
            startActivity(Intent.createChooser(sharingIntent, "Share using"));

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onScrollChanged(int scrollY, boolean firstScroll, boolean dragging) {

    }

    @Override
    public void onDownMotionEvent() {
        System.out.println("scroll Down--------");
    }

    @Override
    public void onUpOrCancelMotionEvent(ScrollState scrollState) {
        //System.out.println("scrollState--------" + scrollState);

        /*if (scrollState == ScrollState.UP) {
            System.out.println("scrollDir-----UP---" + scrollState);
            //bottom_layout.animate().translationY(bottom_layout.getHeight());

            if (toolbarIsShown()) {
               // hideToolbar();
                //hideBottomBar();
            }

        } else if (scrollState == ScrollState.DOWN) {
            System.out.println("scrollDir-----Down---" + scrollState);
            //bottom_layout.animate().translationY(0);

            if (toolbarIsHidden()) {
               // showToolbar();
                //showBottomBar();
            }
        }*/
    }


    private class JSONAsyncTask extends AsyncTask<String, Void, Boolean> {

        ProgressDialog pd;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            mScrollable.setVisibility(View.GONE);

            pd = new ProgressDialog(Qases_View_Activity.this);
            pd.setMessage("Loading. Please wait...");
            pd.setCancelable(true);
            pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            pd.show();

        }

        @Override
        protected Boolean doInBackground(String... urls) {

            try {
                JSONParser jParser = new JSONParser();
                jsonobj_qases = jParser.getJSONFromUrl(urls[0]);
                System.out.println("jsonobj_qases---------- " + jsonobj_qases.toString());

                return true;
            } catch (Exception e) {
                e.printStackTrace();
            }

            return false;
        }

        protected void onPostExecute(Boolean result) {

            try {

                mScrollable.setVisibility(View.VISIBLE);
                pd.dismiss();

                //------------------------ Response Values------------------------------------------------
                q_id = jsonobj_qases.getString("id");
                q_title = jsonobj_qases.getString("title");
                q_desc = jsonobj_qases.getString("desc");
                q_spec = jsonobj_qases.getString("str_sp");
                q_docname = jsonobj_qases.getString("doc_name");
                q_docphoto = jsonobj_qases.getString("doc_photo");
                q_str_date = jsonobj_qases.getString("str_date");
                q_case_url = jsonobj_qases.getString("case_url");

                System.out.println("q_id-------" + q_id);
                System.out.println("q_title-------" + q_title);
                System.out.println("q_desc-------" + q_desc);
                System.out.println("q_spec-------" + q_spec);
                System.out.println("q_docname-------" + q_docname);
                System.out.println("q_docphoto-------" + q_docphoto);
                System.out.println("q_comments-------" + q_comments);
                System.out.println("q_str_date-------" + q_str_date);
                System.out.println("q_case_url-------" + q_case_url);


                //------------- Font ---------------------------
                Typeface font_reg = Typeface.createFromAsset(getApplicationContext().getAssets(), Model.font_name);
                Typeface font_bold = Typeface.createFromAsset(getApplicationContext().getAssets(), Model.font_name_bold);

                tv_spec.setTypeface(font_reg);
                tv_date.setTypeface(font_reg);
                tv_title.setTypeface(font_bold);
                tv_docname.setTypeface(font_bold);
                tv_docedu.setTypeface(font_reg);
                tv_desc.setTypeface(font_reg);
                tv_discc.setTypeface(font_bold);
                //------------- Font ---------------------------

                tv_spec.setText(Html.fromHtml(q_spec));
                tv_date.setText(Html.fromHtml(q_str_date));
                tv_title.setText(Html.fromHtml(q_title));
                tv_docname.setText(Html.fromHtml(q_docname));
                tv_docedu.setText(Html.fromHtml(q_spec));
                tv_desc.setText(Html.fromHtml(q_desc));

                //------------ Doc Photo ------------------------------------
                if (q_docphoto != null && !q_docphoto.isEmpty() && !q_docphoto.equals("null") && !q_docphoto.equals("")) {
                    Picasso.with(getApplicationContext()).load(q_docphoto).placeholder(R.drawable.progress_animation).error(R.mipmap.logo).into(img_docimg);
                } else {
                    img_docimg.setVisibility(View.GONE);
                }
                //------------ Doc Photo ------------------------------------

                //------------ View Comments------------------------------------
                if (jsonobj_qases.has("arr_comments")) {
                    q_comments = jsonobj_qases.getString("arr_comments");

                    JSONArray jarray = new JSONArray(q_comments);
                    System.out.println("length-----" + jarray.length());

                    if (jarray.length() > 0) {

                        comments_layout.removeAllViews();
                        tv_nocomm.setVisibility(View.GONE);

                        for (int i = 0; i < jarray.length(); i++) {
                            jsonobj_comment = jarray.getJSONObject(i);
                            System.out.println("jsonobj_comment-----" + jsonobj_comment.toString());

                            comm_id = jsonobj_comment.getString("id");
                            comm_text = jsonobj_comment.getString("comment");
                            comm_docname = jsonobj_comment.getString("doc_name");
                            comm_docphoto = jsonobj_comment.getString("doc_photo");
                            comm_date = jsonobj_comment.getString("str_date");
                            upvote_val = jsonobj_comment.getString("upvote");
                            has_star_val = jsonobj_comment.getString("has_star");
                            is_best_val = jsonobj_comment.getString("is_best");

                            System.out.println("comm_id-----" + comm_id);
                            System.out.println("comm_text-----" + comm_text);
                            System.out.println("comm_docname-----" + comm_docname);
                            System.out.println("comm_docphoto-----" + comm_docphoto);
                            System.out.println("comm_date-----" + comm_date);
                            System.out.println("upvote_val-----" + upvote_val);
                            System.out.println("has_star_val-----" + has_star_val);
                            System.out.println("is_best_val-----" + is_best_val);

                            vi = getLayoutInflater().inflate(R.layout.qases_comments_row, null);
                            tv_cdocname = (TextView) vi.findViewById(R.id.tv_cdocname);
                            tv_comment = (TextView) vi.findViewById(R.id.tv_comment);
                            tv_time = (TextView) vi.findViewById(R.id.tv_time);
                            img_doc_photo = (CircleImageView) vi.findViewById(R.id.img_doc_photo);
                            like_layout = (LinearLayout) vi.findViewById(R.id.like_layout);
                            best_layout = (LinearLayout) vi.findViewById(R.id.best_layout);
                            tv_like_count = (TextView) vi.findViewById(R.id.tv_like_count);
                            tv_best = (TextView) vi.findViewById(R.id.tv_best);
                            tv_comm_id = (TextView) vi.findViewById(R.id.tv_comm_id);
                            tv_dot2 = vi.findViewById(R.id.tv_dot2);
                            img_best_grey = (ImageView) vi.findViewById(R.id.img_best_grey);
                            img_best_blue = (ImageView) vi.findViewById(R.id.img_best_blue);
                            img_heart_grey = (ImageView) vi.findViewById(R.id.img_heart_grey);
                            img_heart_blue = (ImageView) vi.findViewById(R.id.img_heart_blue);

                            //------------- Font -----------------------------------
                            Typeface fontreg = Typeface.createFromAsset(getApplicationContext().getAssets(), Model.font_name);
                            Typeface fontbold = Typeface.createFromAsset(getApplicationContext().getAssets(), Model.font_name_bold);
                            tv_cdocname.setTypeface(fontbold);
                            tv_comment.setTypeface(fontreg);
                            tv_time.setTypeface(fontreg);
                            tv_like_count.setTypeface(fontreg);
                            tv_best.setTypeface(fontreg);
                            //------------- Font -----------------------------------------

                            //--------------------------------------------------------------1
                            if (has_star_val.equals("1")) {
                                img_heart_grey.setVisibility(View.GONE);
                                img_heart_blue.setVisibility(View.VISIBLE);
                            } else {
                                img_heart_grey.setVisibility(View.VISIBLE);
                                img_heart_blue.setVisibility(View.GONE);
                            }
                            //--------------------------------------------------------------

                            //--------------------------------------------------------------
                            if (is_best_val.equals("1")) {
                                best_layout.setVisibility(View.VISIBLE);
                                tv_dot2.setVisibility(View.VISIBLE);
                            } else {
                                best_layout.setVisibility(View.GONE);
                                tv_dot2.setVisibility(View.GONE);
                            }
                            //--------------------------------------------------------------

                            tv_cdocname.setText(Html.fromHtml(comm_docname));
                            tv_comment.setText(Html.fromHtml(comm_text));
                            tv_time.setText(Html.fromHtml(comm_date));
                            tv_like_count.setText(upvote_val);
                            tv_comm_id.setText(comm_id);

                            //------------ Doc Photo ------------------------------------
                            if (comm_docphoto != null && !comm_docphoto.isEmpty() && !comm_docphoto.equals("null") && !comm_docphoto.equals("")) {
                                Picasso.with(getApplicationContext()).load(comm_docphoto).placeholder(R.mipmap.doctor_icon).error(R.mipmap.logo).into(img_doc_photo);
                            } else {
                                img_doc_photo.setVisibility(View.GONE);
                            }
                            //------------ Doc Photo ------------------------------------

                            comments_layout.addView(vi);
                        }
                    } else {
                        tv_nocomm.setVisibility(View.VISIBLE);
                    }
                }
                //------------ View Comments------------------------------------


                //------------ View Files------------------------------------

                if (jsonobj_qases.has("arr_file")) {
                    q_files_text = jsonobj_qases.getString("arr_file");
                    System.out.println("q_files_text---------" + q_files_text);

                    if ((q_files_text.length()) > 2) {

                        JSONArray jarray_files = new JSONArray(q_files_text);

                        attach_file_text = "";

                        if (jarray_files.length() > 0) {

                            for (int j = 0; j < jarray_files.length(); j++) {
                                jsonobj_files = jarray_files.getJSONObject(j);
                                System.out.println("jsonobj_files---------" + jsonobj_files.toString());

                                filename_text = jsonobj_files.getString("filename");
                                url_text = jsonobj_files.getString("url");
                                is_image_flag = jsonobj_files.getString("is_image");

                                //------------------------ File Attached Text --------------------------------
                                if (attach_file_text.equals("")) {
                                    attach_file_text = url_text;
                                    System.out.println("attach_file_text-------" + attach_file_text);
                                } else {
                                    attach_file_text = attach_file_text + "###" + url_text;
                                    System.out.println("attach_file_text-------" + attach_file_text);
                                }
                                //------------------------ File Attached Text --------------------------------
                            }

                            layout_attachfile.setVisibility(View.VISIBLE);
                            tvattached.setText("Attached   " + jarray_files.length() + " File(s)");
                        }

                        //tv_filename.setText(attach_file_text);
                        tv_filename.setText(q_files_text); //Full Array
                    }
                }
                //------------ View Files------------------------------------


                try {

                    //----------- Flurry -------------------------------------------------
                    Map<String, String> articleParams = new HashMap<String, String>();
                    articleParams.put("android.doc.qaseid", qases_id);
                    FlurryAgent.logEvent("android.doc.qases_view", articleParams);
                    //----------- Flurry -------------------------------------------------

                } catch (Exception e) {
                    e.printStackTrace();
                }


                //--------------------Ad---------
                String ad_url = Model.BASE_URL + "/sapp/fetchAd?token=" + Model.token + "&os_type=android&user_id=" + Model.id + "&browser_country=" + Model.browser_country + "&mcase_id=" + qases_id + "page_src=10";
                System.out.println("ad_url----------" + ad_url);
                new JSON_Ad().execute(ad_url);
                //---------------------Ad--------


            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

   /* public void onClick(View v) {
        TextView tv_quest = (TextView) v.findViewById(R.id.tv_quest);
        String str = tv_quest.getText().toString();
        String qtext_title_hash = reccom_list.get(str);
        System.out.println("str-----------" + str);
        System.out.println("qtext_title_hash-----------" + qtext_title_hash);

        Intent intent = new Intent(Qases_View_Activity.this, SupportActivity.class);
        startActivity(intent);
        finish();

        //scrollview.fullScroll(View.FOCUS_DOWN);
    }*/

    private void TakeScreenshot_Share() {

        try {
            Date now = new Date();
            long milliseconds = now.getTime();
            String mPath = Environment.getExternalStorageDirectory().toString() + "/" + milliseconds + ".jpg";
            System.out.println("mPath---" + mPath);

            View v1 = getWindow().getDecorView().getRootView();
            v1.setDrawingCacheEnabled(true);
            Bitmap bitmap = Bitmap.createBitmap(v1.getDrawingCache());
            v1.setDrawingCacheEnabled(false);

            imageFile = new File(mPath);

            FileOutputStream outputStream = new FileOutputStream(imageFile);
            int quality = 100;
            bitmap.compress(Bitmap.CompressFormat.JPEG, quality, outputStream);
            outputStream.flush();
            outputStream.close();

            System.out.println("Share immediatly--------------------");
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_VIEW);
            Uri uri = Uri.fromFile(imageFile);

            Intent shareIntent = new Intent();
            shareIntent.setAction(Intent.ACTION_SEND);
            shareIntent.putExtra(Intent.EXTRA_TEXT, "Discuss medical cases and clinical scenarios with medical doctors of iCliniq" +
                    " \n\n" + "" + "\n\n" + "");
            shareIntent.putExtra(Intent.EXTRA_STREAM, uri);
            shareIntent.setType("image/jpeg");
            shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            startActivity(Intent.createChooser(shareIntent, "send"));

        } catch (Throwable e) {
            //e.printStackTrace();

            System.out.println("Exception from Taking Screenshot---" + e.toString());
            System.out.println("Exception Screenshot---" + e.toString());

            Intent sharingIntent = new Intent(Intent.ACTION_SEND);
            sharingIntent.setType("text/plain");
            sharingIntent.putExtra(Intent.EXTRA_TEXT, "Discuss medical cases and clinical scenarios with medical doctors of iCliniq" + "" + "\n\n" + "");
            startActivity(Intent.createChooser(sharingIntent, "Share using"));
        }
    }


    class jsonAsync_comment extends AsyncTask<JSONObject, Void, Boolean> {

        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(Qases_View_Activity.this);
            dialog.setMessage("Submitting Comments, please wait..");
            //dialog.setTitle("");
            dialog.show();
            dialog.setCancelable(false);
        }

        @Override
        protected Boolean doInBackground(JSONObject... urls) {
            try {
                JSONParser jParser = new JSONParser();
                json_response_obj = jParser.JSON_POST(urls[0], "comments");

                System.out.println("comments URL---------------" + urls[0]);
                System.out.println("json_response_obj-----------" + json_response_obj.toString());

                return true;
            } catch (Exception e) {
                e.printStackTrace();
            }

            return false;
        }

        protected void onPostExecute(Boolean result) {
            try {
                id_val = json_response_obj.getString("id");
                System.out.println("id_val--------------" + id_val);


                boolean isChecked = checkbox.isChecked();

                if (isChecked) {
                    Intent intent = new Intent(Qases_View_Activity.this, Qases_Post2.class);
                    intent.putExtra("comment_id", id_val);
                    intent.putExtra("qase_id", qases_id);
                    startActivity(intent);
                    finish();
                    overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);
                } else {
                    //say_success();
                    edt_comment.setText("");

                    //------------- getting Qases------------------------------------
                    String full_url = Model.BASE_URL + "sapp/caseView?os_type=android&id=" + qases_id + "&user_id=" + (Model.id) + "&html=1&token=" + Model.token;
                    System.out.println("qases_view_url------------" + full_url);
                    new JSONAsyncTask().execute(full_url);
                    //------------- getting Qases------------------------------------

                }


                dialog.dismiss();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    public void say_success() {



        //---------------- Dialog------------------------------------------------------------------
        final MaterialDialog alert = new MaterialDialog(Qases_View_Activity.this);
        alert.setTitle("Success!");
        alert.setMessage("Your comment has been posted successfully.");
        alert.setCanceledOnTouchOutside(false);
        alert.setPositiveButton("OK", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alert.dismiss();
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



    }

    public void onClick(View v) {

        try {

            switch (v.getId()) {

                case R.id.like_layout:

                    try {
                        img_heart_grey = (ImageView) v.findViewById(R.id.img_heart_grey);
                        img_heart_blue = (ImageView) v.findViewById(R.id.img_heart_blue);
                        tv_like_count = (TextView) v.findViewById(R.id.tv_like_count);
                        tv_comm_id = (TextView) v.findViewById(R.id.tv_comm_id);


                        if (img_heart_grey.getVisibility() == View.VISIBLE) {
                            img_heart_grey.setVisibility(View.GONE);
                            img_heart_blue.setVisibility(View.VISIBLE);

                            //---------------- POSTING ---------------------------
                            json_vote = new JSONObject();
                            json_vote.put("comment_id", (tv_comm_id.getText().toString()));
                            json_vote.put("user_id", (Model.id));
                            json_vote.put("type", "1");
                            System.out.println("json_vote-----------" + json_vote.toString());
                            new JSON_VOTE_POST().execute(json_vote);
                            //---------------- POSTING ---------------------------

                            Integer after_num = (Integer.parseInt(tv_like_count.getText().toString())) + 1;
                            System.out.println("after_num----------" + after_num);
                            tv_like_count.setText("" + after_num);

                            play_dound();

                        } else {
                            img_heart_grey.setVisibility(View.VISIBLE);
                            img_heart_blue.setVisibility(View.GONE);

                            //---------------- POSTING ---------------------------
                            json_vote = new JSONObject();
                            json_vote.put("comment_id", (tv_comm_id.getText().toString()));
                            json_vote.put("user_id", (Model.id));
                            json_vote.put("type", "0");
                            System.out.println("json_vote---" + json_vote.toString());
                            new JSON_VOTE_POST().execute(json_vote);
                            //---------------- POSTING ---------------------------

                            Integer after_num = (Integer.parseInt(tv_like_count.getText().toString())) - 1;
                            System.out.println("after_num----------" + after_num);
                            tv_like_count.setText("" + after_num);

                            try {

                                //----------- Flurry -------------------------------------------------
                                Map<String, String> articleParams = new HashMap<String, String>();
                                articleParams.put("android.doc.qaseid", qases_id);
                                articleParams.put("android.doc.comment_id", (tv_comm_id.getText().toString()));
                                FlurryAgent.logEvent("android.doc.qases_like", articleParams);
                                //----------- Flurry -------------------------------------------------

                            } catch (Exception e) {
                                e.printStackTrace();
                            }


                            play_dound();
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    break;

                case R.id.layout_attachfile:

                    TextView tv_filename = (TextView) v.findViewById(R.id.tv_filename);
                    String file_name = tv_filename.getText().toString();
                    System.out.println("str_filename-------" + file_name);

                    Intent i = new Intent(getApplicationContext(), GridViewActivity.class);
                    i.putExtra("filetxt", file_name);
                    startActivity(i);
                    overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);

                    break;

                case R.id.best_layout:

                    try {
                        img_best_grey = (ImageView) v.findViewById(R.id.img_best_grey);
                        img_best_blue = (ImageView) v.findViewById(R.id.img_best_blue);

                        if (img_best_grey.getVisibility() == View.VISIBLE) {
                            img_best_grey.setVisibility(View.GONE);
                            img_best_blue.setVisibility(View.VISIBLE);

                            //---------------- POSTING -----1----------------------
                            json_vote = new JSONObject();
                            json_vote.put("comment_id", (tv_comm_id.getText().toString()));
                            json_vote.put("user_id", (Model.id));
                            json_vote.put("type", "1");
                            System.out.println("JSON_BEST_POST---" + json_vote.toString());
                            new JSON_BEST_POST().execute(json_vote);
                            //---------------- POSTING -----1----------------------

                            play_dound();

                        } else {
                            img_best_grey.setVisibility(View.VISIBLE);
                            img_best_blue.setVisibility(View.GONE);

                            //---------------- POSTING ------0---------------------
                            json_vote = new JSONObject();
                            json_vote.put("comment_id", (tv_comm_id.getText().toString()));
                            json_vote.put("user_id", (Model.id));
                            json_vote.put("type", "0");
                            System.out.println("JSON_BEST_POST---" + json_vote.toString());
                            new JSON_BEST_POST().execute(json_vote);
                            //---------------- POSTING ------0---------------------

                            play_dound();
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    break;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static boolean checkRingerIsOn(Context context) {
        AudioManager am = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        return am.getRingerMode() == AudioManager.RINGER_MODE_NORMAL;
    }


    public void play_dound() {
        //--------------- Alert Service -------------------------------------------------------
        if (checkRingerIsOn(getApplicationContext())) {
            MediaPlayer mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.click_sound);
            mediaPlayer.start();
        } else {
            Vibrator vi = (Vibrator) getApplicationContext().getSystemService(Context.VIBRATOR_SERVICE);
            vi.vibrate(250);
        }
        //--------------- Alert Service -------------------------------------------------------
    }


    class JSON_VOTE_POST extends AsyncTask<JSONObject, Void, Boolean> {

        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

/*            dialog = new ProgressDialog(Qases_View_Activity.this);
            dialog.setMessage("Applying Coupon, please wait..");
            //dialog.setTitle("");
            dialog.show();
            dialog.setCancelable(false);*/
        }

        @Override
        protected Boolean doInBackground(JSONObject... urls) {
            try {
                JSONParser jParser = new JSONParser();
                json_response_obj = jParser.JSON_POST(urls[0], "upvote");

                System.out.println("Input Params---------------" + urls[0]);
                System.out.println("json_response_obj-----------" + json_response_obj.toString());

                return true;
            } catch (Exception e) {
                e.printStackTrace();
            }

            return false;
        }

        protected void onPostExecute(Boolean result) {
            try {
                if (json_response_obj.has("upvote")) {
                    report_response = json_response_obj.getString("upvote");
                    System.out.println("report_response--------------" + report_response);
                }
                //dialog.cancel();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    class JSON_BEST_POST extends AsyncTask<JSONObject, Void, Boolean> {

        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Boolean doInBackground(JSONObject... urls) {
            try {
                JSONParser jParser = new JSONParser();
                json_response_obj = jParser.JSON_POST(urls[0], "best");

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

                if (json_response_obj.has("status")) {
                    report_response = json_response_obj.getString("status");
                    System.out.println("report_response--------------" + report_response);
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    private class JSON_Ad extends AsyncTask<String, Void, Boolean> {

        ProgressDialog pd;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

/*            pd = new ProgressDialog(NewQueryViewActivity.this);
            pd.setMessage("Loading Ad. Please wait...");
            pd.setCancelable(true);
            pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            pd.show();*/

        }

        @Override
        protected Boolean doInBackground(String... urls) {

            try {
                JSONParser jParser = new JSONParser();
                jsonobj_ad = jParser.getJSONFromUrl(urls[0]);
                System.out.println("jsonobj_ad---------- " + jsonobj_ad.toString());

                return true;
            } catch (Exception e) {
                e.printStackTrace();
            }

            return false;
        }

        protected void onPostExecute(Boolean result) {

            try {

                if (jsonobj_ad.has("status")) {
                    String str_status = jsonobj_ad.getString("status");
                    if (str_status.equals("1")) {
                        ad_layout.setVisibility(View.VISIBLE);
                        String ad_img_path = jsonobj_ad.getString("img");
                        track_id_val = jsonobj_ad.getString("track_id");

                        Picasso.with(Qases_View_Activity.this).load(ad_img_path).placeholder(R.mipmap.ad_placeholder).error(R.mipmap.ad_placeholder).into(home_ad);
                    } else {
                        ad_layout.setVisibility(View.GONE);
                    }
                } else {
                    ad_layout.setVisibility(View.GONE);
                }

                // pd.dismiss();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    private class JSON_Close extends AsyncTask<String, Void, Boolean> {

        @Override
        protected Boolean doInBackground(String... urls) {

            try {
                JSONParser jParser = new JSONParser();
                jsonobj_ad = jParser.getJSONFromUrl(urls[0]);
                System.out.println("jsonobj_ad---------- " + jsonobj_ad.toString());

                return true;
            } catch (Exception e) {
                e.printStackTrace();
            }

            return false;
        }
    }
}
