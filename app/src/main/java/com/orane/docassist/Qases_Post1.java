package com.orane.docassist;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.flurry.android.FlurryAgent;

import com.orane.docassist.Model.Model;
import com.orane.docassist.Network.JSONParser;
import com.orane.docassist.New_Main.New_MainActivity;
import com.orane.docassist.R;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


public class Qases_Post1 extends AppCompatActivity {

    EditText edt_qtitle, edt_desc;
    RelativeLayout first_layout;

    Spinner spinner_speciality;
    TextView tvdocname, tv_strprice, tv_forname, tv_soid, tvqfee, tvtit, tvfquery, tvprice;
    TextView follow_text, tv_editbutton, tv_spec_name;
    Button btn_submit;
    public String qid, qase_id, query_txt;
    public JSONObject json, jsonobj;
    Map<String, String> spec_map = new HashMap<String, String>();
    public String qtitle_txt, qdesc_txt, spec_val, editbutton_txt, soid_val;
    ImageView img_someoneshow;
    ScrollView someone_scrollview;
    View someone_vi;
    ImageView img_remove;
    LinearLayout someone_layout, parent_layout, select_layout;
    JSONObject json_qase_post;

    SharedPreferences sharedpreferences;
    public static final String first_query = "first_query_key";
    public static final String MyPREFERENCES = "MyPrefs";
    public static final String Login_Status = "Login_Status_key";
    public static final String sp_km_id = "sp_km_id_key";
    public static final String user_name = "user_name_key";
    public static final String Name = "Name_key";
    public static final String password = "password_key";
    public static final String bcountry = "bcountry_key";
    public static final String isValid = "isValid";
    public static final String id = "id";
    public static final String browser_country = "browser_country";
    public static final String email = "email";
    public static final String fee_q = "fee_q";
    public static final String fee_consult = "fee_consult";
    public static final String fee_q_inr = "fee_q_inr";
    public static final String fee_consult_inr = "fee_consult_inr";
    public static final String currency_symbol = "currency_symbol";
    public static final String currency_label = "currency_label";
    public static final String have_free_credit = "have_free_credit";
    public static final String photo_url = "photo_url";
    public static final String sp_mcode = "sp_mcode_key";
    public static final String sp_mnum = "sp_mnum_key";
    public static final String sp_qid = "sp_qid_key";
    public static final String sp_has_free_follow = "sp_has_free_follow_key";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.qases_post1);

        //------------ Object Creations -------------------------------
        tv_forname = (TextView) findViewById(R.id.tv_forname);
        tv_spec_name = (TextView) findViewById(R.id.tv_spec_name);
        select_layout = (LinearLayout) findViewById(R.id.select_layout);
        parent_layout = (LinearLayout) findViewById(R.id.parent_layout);
        someone_layout = (LinearLayout) findViewById(R.id.someone_layout);
        someone_scrollview = (ScrollView) findViewById(R.id.someone_scrollview);
        img_someoneshow = (ImageView) findViewById(R.id.img_someoneshow);
        tvqfee = (TextView) findViewById(R.id.tvqfee);
        edt_qtitle = (EditText) findViewById(R.id.edt_qtitle);
        edt_desc = (EditText) findViewById(R.id.edt_desc);
        spinner_speciality = (Spinner) findViewById(R.id.spinner_speciality);
        tvprice = (TextView) findViewById(R.id.tvprice);
        btn_submit = (Button) findViewById(R.id.btn_submit);
        tvtit = (TextView) findViewById(R.id.tvtit);
        tvfquery = (TextView) findViewById(R.id.tvfquery);
        img_remove = (ImageView) findViewById(R.id.img_remove);

        edt_qtitle.clearFocus();


        //--------------------------------------------------------------------
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setTitle("");

            //----------------Font---------------------------------------
            Typeface font_reg = Typeface.createFromAsset(getAssets(), Model.font_name);
            Typeface font_bold = Typeface.createFromAsset(getAssets(), Model.font_name_bold);

            ((TextView) findViewById(R.id.tvtit)).setTypeface(font_reg);
            ((TextView) findViewById(R.id.tv_spec_name)).setTypeface(font_reg);
            ((TextView) findViewById(R.id.tvqfee)).setTypeface(font_reg);
            ((TextView) findViewById(R.id.toolbar_title)).setTypeface(font_reg);

            btn_submit.setTypeface(font_bold);
            //----------------Font---------------------------------------


        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.app_color2));
        }
        //----------- initialize --------------------------------------

        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        Model.query_launch = "";
        Model.select_specname = "";
        Model.select_spec_val = "0";

        tv_spec_name.setText("Select Speciality");
        img_remove.setVisibility(View.GONE);

        //----------- initialize --------------------------------------

        img_remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Model.select_specname = "";
                Model.select_spec_val = "0";

                tv_spec_name.setText("Select Speciality");
                img_remove.setVisibility(View.GONE);
            }
        });


        select_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Qases_Post1.this, SpecialityListActivity.class);
                startActivity(intent);
            }
        });


        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                qtitle_txt = edt_qtitle.getText().toString();
                qdesc_txt = edt_desc.getText().toString();


                if (qtitle_txt != null && !qtitle_txt.isEmpty() && !qtitle_txt.equals("null") && !qtitle_txt.equals("")) {

                    if (qdesc_txt != null && !qdesc_txt.isEmpty() && !qdesc_txt.equals("null") && !qdesc_txt.equals("")) {


                        if (!(Model.select_spec_val).equals("0")) {

                            try {
                                json_qase_post = new JSONObject();
                                json_qase_post.put("user_id", (Model.id));
                                json_qase_post.put("title", qtitle_txt);
                                json_qase_post.put("question", qdesc_txt);
                                json_qase_post.put("speciality", Model.select_spec_val);

                                System.out.println("json_qase_post---" + json_qase_post.toString());

                                new JSONPostQuery().execute(json_qase_post);

                                try {

                                    //----------- Flurry -------------------------------------------------
                                    Map<String, String> articleParams = new HashMap<String, String>();
                                    articleParams.put("android.doc.title_txt:", qtitle_txt);
                                    articleParams.put("android.doc.qase_Desc:", qdesc_txt);
                                    articleParams.put("android.doc.speciality:", Model.select_spec_val);
                                    FlurryAgent.logEvent("android.doc.post_Qase1", articleParams);
                                    //----------- Flurry -------------------------------------------------

                                } catch (Exception e) {
                                    e.printStackTrace();
                                }

                            } catch (Exception e2) {
                                e2.printStackTrace();
                            }
                        } else {
                            Toast.makeText(Qases_Post1.this, "Please Select the Speciality!", Toast.LENGTH_LONG).show();
                        }


                    } else {
                        edt_desc.setError("Type details of your case here");
                        edt_desc.requestFocus();
                    }
                } else {
                    edt_qtitle.setError("Please enter the case");
                    edt_qtitle.requestFocus();
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        if ((Model.query_launch).equals("SpecialityListActivity")) {

            if (Model.select_spec_val.equals("0")) {
                tv_spec_name.setText("Select Speciality (optional)");
                img_remove.setVisibility(View.GONE);
            } else {
                System.out.println("Resume Model.query_launch-----" + Model.select_spec_val);
                System.out.println("Resume Model.select_specname-----" + Model.select_specname);

                tv_spec_name.setText(Model.select_specname);
                img_remove.setVisibility(View.VISIBLE);
            }
        }
    }

    private class JSONPostQuery extends AsyncTask<JSONObject, Void, Boolean> {

        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            dialog = new ProgressDialog(Qases_Post1.this);
            dialog.setMessage("Submitting, please wait");
            dialog.show();
            dialog.setCancelable(false);
        }

        @Override
        protected Boolean doInBackground(JSONObject... urls) {
            try {
                JSONParser jParser = new JSONParser();
                jsonobj = jParser.JSON_POST(urls[0], "PostQase");

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
                        //----------------------------------
                        finishAffinity();
                        Intent intent = new Intent(Qases_Post1.this, LoginActivity.class);
                        startActivity(intent);
                        finish();
                        //----------------------------------
                    }

                } else {

                    qase_id = jsonobj.getString("id");

                    System.out.println("Submitting Query id:--------" + qase_id);

                    if (qase_id != null && !qase_id.isEmpty() && !qase_id.equals("null") && !qase_id.equals("") && !qase_id.equals("0")) {

                        Intent intent = new Intent(Qases_Post1.this, Qases_Post2.class);
                        intent.putExtra("qase_id", qase_id);
                        intent.putExtra("finisher", new android.os.ResultReceiver(null) {
                            @Override
                            protected void onReceiveResult(int resultCode, Bundle resultData) {
                                Qases_Post1.this.finish();
                            }
                        });

                        startActivityForResult(intent, 1);
                        overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);

                    } else {

                        Toast.makeText(Qases_Post1.this, "Something went wrong. Please back and try again..!", Toast.LENGTH_LONG).show();

                        //----------------------------------
                        Intent intent = new Intent(Qases_Post1.this, New_MainActivity.class);
                        startActivity(intent);
                        finish();
                        //----------------------------------
                    }
                }

                dialog.cancel();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
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


}
