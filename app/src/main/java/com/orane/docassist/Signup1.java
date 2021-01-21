package com.orane.docassist;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import com.flurry.android.FlurryAgent;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.hbb20.CountryCodePicker;
import com.orane.docassist.Model.Model;
import com.orane.docassist.Model.MultipartEntity2;
import com.orane.docassist.Network.JSONParser;
import com.orane.docassist.fileattach_library.DefaultCallback;
import com.orane.docassist.fileattach_library.EasyImage;
import com.scottyab.showhidepasswordedittext.ShowHidePasswordEditText;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import me.drakeet.materialdialog.MaterialDialog;
import pl.tajchert.nammu.Nammu;
import pl.tajchert.nammu.PermissionCallback;

public class Signup1 extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    Map<String, String> cc_map = new HashMap<String, String>();
    Map<String, String> gender_map = new HashMap<String, String>();

    TextView tvtit, tvmore, tv_attach_id, tv_attach_url;
    int serverResponseCode = 0;
    public String err_val, serverResponseMessage, isValid_val, msg_text, vpin_text, t_text, token_text, labtest, selectedPath, inv_id, inv_fee, inv_strfee, status_postquery, qid, sel_filename, last_upload_file, attach_status, attach_file_url, attach_filename, local_url, contentAsString, upLoadServerUri, attach_id, attach_qid, upload_response, image_path, selectedfilename;
    Spinner spinner_ccode, spinner_gender;
    Button btn_submit;
    JSONObject json_validate, signup_jsonobj, json_uname_check, json_check_userid;
    InputStream is = null;
    ImageView profile_image;
    public StringBuilder total;
    EditText edtname, edt_emailid, edt_confirmpassword;
    ShowHidePasswordEditText edt_password;
    EditText edt_userid, edt_phoneno;
    RelativeLayout ccode_layout;
    public String gender_val, upload_response_status, cons_select_date, userid, country_code_val, user_id, selected_cc_value, selected_cc_text, status_val, gender_code, gender_name, cc_name, cccode, fullanme, emailid, pwd, phno, cpwd;
    JSONObject login_json, login_jsonobj;
    TextView tv_ccode;
    RadioButton rad_male, rad_gender, rad_female;
    CountryCodePicker countryCodePicker;
    ImageView thumb_img;
    TextView mTitle;
    LinearLayout photo_layout;
    Button btn_dob;
    RelativeLayout layout_check, layout_correct, layout_wrong;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup1);

        //------------ Object Creations -------------------------------
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(false);
            getSupportActionBar().setDisplayShowHomeEnabled(false);
            getSupportActionBar().setTitle("");

            mTitle = toolbar.findViewById(R.id.toolbar_title);
            Typeface khandBold = Typeface.createFromAsset(getApplicationContext().getAssets(), Model.font_name_bold);
            mTitle.setTypeface(khandBold);
        }

        //------------ Object Creations -------------------------------
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.app_color));
        }
        //------------ Object Creations -------------------------------

        Model.query_launch = "";
        selectedPath = "";

        layout_check = findViewById(R.id.layout_check);
        layout_correct = findViewById(R.id.layout_correct);
        layout_wrong = findViewById(R.id.layout_wrong);

        photo_layout = findViewById(R.id.photo_layout);
        profile_image = findViewById(R.id.profile_image);

        countryCodePicker = findViewById(R.id.ccp);
        edt_userid = findViewById(R.id.edt_userid);
        edtname = findViewById(R.id.edtname);
        edt_emailid = findViewById(R.id.edt_emailid);
        edt_password = findViewById(R.id.edt_password);
        edt_confirmpassword = findViewById(R.id.edt_confirmpassword);
        edt_phoneno = findViewById(R.id.edt_phoneno);

        btn_submit = findViewById(R.id.btn_submit);
        spinner_ccode = findViewById(R.id.spinner_ccode);
        spinner_gender = findViewById(R.id.spinner_gender);
        tv_ccode = findViewById(R.id.tv_ccode);
        ccode_layout = findViewById(R.id.ccode_layout);
        rad_male = findViewById(R.id.rad_male);
        rad_female = findViewById(R.id.rad_female);
        rad_gender = findViewById(R.id.rad_gender);
        btn_dob = findViewById(R.id.btn_dob);

        gender_code = "0";
        rad_male.setSelected(true);
        cccode = Model.country_code;

        System.out.println("cccode-------------" + cccode);

        //country_code();

        edt_userid.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {

                layout_check.setVisibility(View.VISIBLE);
                layout_correct.setVisibility(View.GONE);
                layout_wrong.setVisibility(View.GONE);

            }
        });


        layout_check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                System.out.println("check_userid---");

                String uname = edt_userid.getText().toString();

                if (!uname.equals("")) {

                    try {
                        json_check_userid = new JSONObject();
                        json_check_userid.put("username", uname);

                        System.out.println("json_check_userid---" + json_check_userid.toString());

                        new Async_Check_Uname().execute(json_check_userid);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    edt_userid.requestFocus();
                    edt_userid.setError("Please enter your User Id");
                }

            }
        });

        layout_correct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                try {

                    String uname = edt_userid.getText().toString();

                    json_check_userid = new JSONObject();
                    json_check_userid.put("username", uname);

                    System.out.println("json_check_userid---" + json_check_userid.toString());

                    new Async_Check_Uname().execute(json_check_userid);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        layout_wrong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                try {

                    String uname = edt_userid.getText().toString();

                    json_check_userid = new JSONObject();
                    json_check_userid.put("username", uname);

                    System.out.println("json_check_userid---" + json_check_userid.toString());

                    new Async_Check_Uname().execute(json_check_userid);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        edt_userid.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {

                if (hasFocus) {
                    //Toast.makeText(getApplicationContext(), "Got the focus", Toast.LENGTH_LONG).show();


                } else {
                    //Toast.makeText(getApplicationContext(), "Lost the focus", Toast.LENGTH_LONG).show();

                    try {

                        String uname = edt_userid.getText().toString();

                        json_check_userid = new JSONObject();
                        json_check_userid.put("username", uname);

                        System.out.println("json_check_userid---" + json_check_userid.toString());

                        new Async_Check_Uname().execute(json_check_userid);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });


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

        /*layout_check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                layout_check.setVisibility(View.GONE);
                layout_correct.setVisibility(View.VISIBLE);
                layout_wrong.setVisibility(View.GONE);
            }
        });

        layout_correct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                layout_check.setVisibility(View.GONE);
                layout_correct.setVisibility(View.VISIBLE);
                layout_wrong.setVisibility(View.GONE);
            }
        });

        layout_wrong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                layout_check.setVisibility(View.GONE);






                layout_correct.setVisibility(View.VISIBLE);
                layout_wrong.setVisibility(View.GONE);
            }
        });*/

        photo_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                attach_dialog();
            }
        });


        rad_male.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    gender_code = "1";
                }
            }
        });

        rad_female.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    gender_code = "2";
                }
            }
        });

        rad_gender.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    gender_code = "3";
                }
            }
        });

        countryCodePicker.registerCarrierNumberEditText(edt_phoneno);


        countryCodePicker.setOnCountryChangeListener(new CountryCodePicker.OnCountryChangeListener() {
            @Override
            public void onCountrySelected() {

                System.out.println("getSelectedCountryCode------------" + countryCodePicker.getSelectedCountryCode());
                System.out.println("getSelectedCountryCodeAsInt------------" + countryCodePicker.getSelectedCountryCodeAsInt());
                System.out.println("getSelectedCountryName------------" + countryCodePicker.getSelectedCountryName());
                System.out.println("getSelectedCountryNameCode------------" + countryCodePicker.getSelectedCountryNameCode());

                country_code_val = "" + countryCodePicker.getSelectedCountryCodeAsInt();
                cccode = country_code_val;
                System.out.println("cccode--------------" + cccode);
            }
        });

        countryCodePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
/*

        spinner_gender.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                gender_name = spinner_gender.getSelectedItem().toString();
                gender_code = gender_map.get(gender_name);

                System.out.println("gender_name------" + gender_name);
                System.out.println("gender_code------" + gender_code);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

*/

        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if (isInternetOn()) {

                    userid = edt_userid.getText().toString();
                    fullanme = edtname.getText().toString();
                    emailid = edt_emailid.getText().toString();
                    pwd = edt_password.getText().toString();
                    cpwd = edt_confirmpassword.getText().toString();
                    phno = edt_phoneno.getText().toString();
                    String date_text = btn_dob.getText().toString();

                    System.out.println("fullanme---------" + fullanme);
                    System.out.println("emailid---------" + emailid);
                    System.out.println("pwd---------" + pwd);
                    System.out.println("cpwd---------" + cpwd);
                    System.out.println("phno---------" + phno);
                    System.out.println("cons_select_date---------" + cons_select_date);
                    System.out.println("date_text---------" + date_text);

                    country_code_val = "" + countryCodePicker.getSelectedCountryCodeAsInt();
                    cccode = country_code_val;
                    System.out.println("Get cccode--------------" + cccode);

                    if (!pwd.equals("")) {
                        if (pwd.equals(cpwd)) {
                            if (!userid.equals("")) {
                                if (!fullanme.equals("")) {
                                    if (!emailid.equals("")) {
                                        if (!phno.equals("")) {
                                            if (!gender_code.equals("0")) {
                                                if (!date_text.equals("Date of birth")) {

                                                    if (pwd.length() < 8 || !isValidPassword(pwd)) {
                                                        System.out.println("Pwd is Not Valid");
                                                        diag_err_pwd();
                                                    } else {

                                                        if (pwd.equals(cpwd)) {

                                                            if (layout_correct.getVisibility() == View.VISIBLE) {
                                                                //signup();
                                                                validate_mob_email();
                                                            } else {
                                                                Toast.makeText(Signup1.this, "User ID already exists (Please choose another user ID)", Toast.LENGTH_SHORT).show();
                                                            }


                                                        } else {
                                                            edt_confirmpassword.requestFocus();
                                                            edt_confirmpassword.setError("Those passwords don't match");
                                                            Toast.makeText(Signup1.this, "Those passwords don't match", Toast.LENGTH_SHORT).show();
                                                        }
                                                    }
                                                } else {
                                                    Toast.makeText(Signup1.this, "Please select your date of birth", Toast.LENGTH_SHORT).show();
                                                }
                                            } else
                                                Toast.makeText(Signup1.this, "Please choose your Gender", Toast.LENGTH_SHORT).show();
                                        } else {
                                            edt_phoneno.requestFocus();
                                            edt_phoneno.setError("Please enter your mobile number");
                                        }
                                    } else {
                                        edt_emailid.requestFocus();
                                        edt_emailid.setError("Please enter the Please enter your valid Email address");
                                    }
                                } else {
                                    edtname.requestFocus();
                                    edtname.setError("Please enter your name");
                                }
                            } else {
                                edt_userid.requestFocus();
                                edt_userid.setError("Please enter your User Id");
                            }
                        } else {
                            edt_confirmpassword.requestFocus();
                            edt_confirmpassword.setError("Those passwords don't match");
                        }
                    } else {
                        edt_password.requestFocus();
                        edt_password.setError("Those passwords don't match");
                    }

                } else {
                    Toast.makeText(getApplicationContext(), "Please check your Internet Connection and try again", Toast.LENGTH_SHORT).show();
                }
            }
        });


        btn_dob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Calendar now = Calendar.getInstance();
                DatePickerDialog dpd = DatePickerDialog.newInstance(
                        Signup1.this,
                        now.get(Calendar.YEAR),
                        now.get(Calendar.MONTH),
                        now.get(Calendar.DAY_OF_MONTH)
                );

                dpd.setThemeDark(false);
                dpd.vibrate(false);
                dpd.dismissOnPause(false);
                dpd.showYearPickerFirst(false);
                dpd.setAccentColor(Color.parseColor("#9C27B0"));
                dpd.setTitle("Please select your date of birth");
                now.add(Calendar.YEAR, -15); // Selecting max Date
                dpd.setMaxDate(now);


/*                //----------------------------------------------
                Calendar[] dates = new Calendar[300];
                for (int i = 1; i <= 300; i++) {
                    Calendar date = Calendar.getInstance();
                    date.add(Calendar.DATE, i);
                    dates[i - 1] = date;
                }
                dpd.setSelectableDays(dates);
                //----------------------------------------------*/


/*                //----------------------------------------------
                    Calendar[] dates = new Calendar[13];
                    for (int i = -6; i <= 6; i++) {
                        Calendar date = Calendar.getInstance();
                        date.add(Calendar.WEEK_OF_YEAR, i);
                        dates[i + 6] = date;
                    }
                    dpd.setHighlightedDays(dates);
                //----------------------------------------------*/

/*                //---------- Set Tomo Date -----------------------------------
                Calendar now_new = Calendar.getInstance();
                DatePickerDialog dpd_new = DatePickerDialog.newInstance(
                        Signup1.this,
                        now_new.get(Calendar.YEAR),
                        now_new.get(Calendar.MONTH),
                        now_new.get(Calendar.DAY_OF_MONTH)
                );
                dpd_new.show(getFragmentManager(), "Datepickerdialog");
                now_new.add(Calendar.YEAR, -15); // Selecting max Date
                dpd_new.setMaxDate(now_new);
                //---------- Set Tomo Date -----------------------------------*/

                //dpd.setMaxDate(now_new);


                dpd.show(getFragmentManager(), "Datepickerdialog");

            }
        });

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

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
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {

        try {
            //String date = dayOfMonth + "/" + (++monthOfYear) + "/" + year;
            cons_select_date = year + "/" + (monthOfYear + 1) + "/" + dayOfMonth;
            System.out.println("Cal Date------" + cons_select_date);

            //--------- for System -------------------
            SimpleDateFormat curFormater = new SimpleDateFormat("yyyy/MM/dd");
            Date dateObj = curFormater.parse(cons_select_date);
            String newDateStr = curFormater.format(dateObj);
            System.out.println("For System select_date---------" + newDateStr);
            //--------------------------------

            btn_dob.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
            System.out.println("cons_select_date---------" + cons_select_date);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    public final boolean isInternetOn() {

        try {
            ConnectivityManager connec = (ConnectivityManager) getSystemService(getBaseContext().CONNECTIVITY_SERVICE);
            if (connec.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.CONNECTED ||
                    connec.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.CONNECTING ||
                    connec.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.CONNECTING ||
                    connec.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.CONNECTED) {

                return true;

            } else if (
                    connec.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.DISCONNECTED ||
                            connec.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.DISCONNECTED) {

                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    private class JSON_signup extends AsyncTask<JSONObject, Void, Boolean> {

        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            dialog = new ProgressDialog(Signup1.this);
            dialog.setMessage("Submitting.. please wait");
            dialog.show();
            dialog.setCancelable(false);
        }

        @Override
        protected Boolean doInBackground(JSONObject... urls) {

            try {
                JSONParser jParser = new JSONParser();
                signup_jsonobj = jParser.JSON_POST(urls[0], "signup1");

                System.out.println("urls[0]----------" + urls[0]);
                System.out.println("Server_Response_JSON----------" + signup_jsonobj.toString());

                return true;
            } catch (Exception e) {
                e.printStackTrace();
            }
            return false;
        }

        protected void onPostExecute(Boolean result) {

            try {

                status_val = signup_jsonobj.getString("status");

                if (status_val.equals("1")) {

                    user_id = signup_jsonobj.getString("user_id");
                    vpin_text = signup_jsonobj.getString("vpin");
                    t_text = signup_jsonobj.getString("t");
                    token_text = signup_jsonobj.getString("token");

                    Model.id = user_id;
                    Model.token = token_text;


                    System.out.println("status_val----------" + status_val);
                    System.out.println("user_id----------" + user_id);


                    overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);
                    Intent i = new Intent(Signup1.this, OTPActivity.class);
                    i.putExtra("email", emailid);
                    i.putExtra("cccode", cccode);
                    i.putExtra("phno", phno);
                    i.putExtra("vpin_text", vpin_text);
                    i.putExtra("t_text", t_text);
                    startActivity(i);


/*                    if (selectedPath != null && !selectedPath.isEmpty() && !selectedPath.equals("null") && !selectedPath.equals("")) {

                        overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);
                        Intent i = new Intent(Signup1.this, Signup2.class);
                        startActivity(i);

                    } else {


                        final MaterialDialog alert = new MaterialDialog(Signup1.this);
                        alert.setTitle("profile Photo");
                        alert.setMessage("You are not uploaded profile photo, Do you want to continue without photo?");
                        alert.setCanceledOnTouchOutside(false);
                        alert.setPositiveButton("Yes", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);
                                Intent i = new Intent(Signup1.this, Signup2.class);
                                startActivity(i);
                            }
                        });
                        alert.setNegativeButton("No", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                alert.dismiss();
                            }
                        });
                        alert.show();

                        Toast.makeText(getApplicationContext(), "Upload profile photo", Toast.LENGTH_SHORT).show();
                    }*/

                    //new AsyncTask_fileupload().execute(selectedPath);


                } else {

                    String err_val = signup_jsonobj.getString("err");

                    Toast.makeText(getApplicationContext(), err_val, Toast.LENGTH_LONG).show();

                    try {

                        //----------- Flurry -------------------------------------------------
                        Map<String, String> articleParams = new HashMap<String, String>();
                        articleParams.put("android.doc.App_version:", (Model.app_ver));
                        articleParams.put("android.doc.Android_Version", getAndroidVersion());
                        FlurryAgent.logEvent("android.doc.Signup1_Failed", articleParams);
                        //----------- Flurry -------------------------------------------------

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            dialog.cancel();

        }
    }

    public String getAndroidVersion() {
        String release = Build.VERSION.RELEASE;
        int sdkVersion = Build.VERSION.SDK_INT;
        return "Android SDK: " + sdkVersion + " (" + release + ")";
    }

    public void attach_dialog() {
        List<String> mAnimals = new ArrayList<String>();
        mAnimals.add("Take Photo");
        mAnimals.add("Browse Files");

        final CharSequence[] Animals = mAnimals.toArray(new String[mAnimals.size()]);
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        dialogBuilder.setTitle("Upload profile image");
        dialogBuilder.setItems(Animals, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int item) {
                String selectedText = Animals[item].toString();
                System.out.println("selectedText---" + selectedText);

                if (selectedText.equals("Take Photo")) {

                    int permissionCheck = ContextCompat.checkSelfPermission(Signup1.this, Manifest.permission.CAMERA);
                    if (permissionCheck == PackageManager.PERMISSION_GRANTED) {
                        EasyImage.openCamera(Signup1.this, 0);
                    } else {
                        Nammu.askForPermission(Signup1.this, Manifest.permission.CAMERA, new PermissionCallback() {
                            @Override
                            public void permissionGranted() {
                                EasyImage.openCamera(Signup1.this, 0);
                            }

                            @Override
                            public void permissionRefused() {

                            }
                        });
                    }

                } else {
                    //showChooser();
                    int permissionCheck = ContextCompat.checkSelfPermission(Signup1.this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
                    if (permissionCheck == PackageManager.PERMISSION_GRANTED) {
                        EasyImage.openDocuments(Signup1.this, 0);
                    } else {
                        Nammu.askForPermission(Signup1.this, Manifest.permission.WRITE_EXTERNAL_STORAGE, new PermissionCallback() {
                            @Override
                            public void permissionGranted() {
                                EasyImage.openDocuments(Signup1.this, 0);
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
                dialog = new ProgressDialog(Signup1.this);
                dialog.setMessage("Uploading Photo. Please wait...");
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

                upload_response_status = jObj.getString("status");
                System.out.println("upload_response_status ----------" + upload_response_status);

                if (upload_response_status.equals("1")) {

                    ((android.os.ResultReceiver) getIntent().getParcelableExtra("finisher")).send(1, new Bundle());

                    Intent intent = new Intent(Signup1.this, OTPActivity.class);
                    intent.putExtra("user_id", user_id);
                    startActivity(intent);
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

    /* public String upload_file(String fullpath) {

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
                 upLoadServerUri = Model.BASE_URL + "/sapp/uploadDocPhoto?os_type=android&user_id=" + (Model.id) + "&token=" + Model.token;
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
                    File photoFile = EasyImage.lastlyTakenButCanceledPhoto(Signup1.this);
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

            File imgFile = new File(selectedPath);

            if (imgFile.exists()) {
                Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                //ImageView myImage = (ImageView) findViewById(R.id.profile_image);
                profile_image.setImageBitmap(myBitmap);
            }


            //new AsyncTask_fileupload().execute(selectedPath);

        }

    }

    @Override
    protected void onDestroy() {
        //EasyImage.clearConfiguration(this);
        super.onDestroy();
    }


    public void validate_mob_email() {
        try {
            json_validate = new JSONObject();
            json_validate.put("email", emailid);
            json_validate.put("ccode", cccode);
            json_validate.put("mobile", phno);

            System.out.println("json_validate----" + json_validate.toString());

            new Async_validateEmailMobno().execute(json_validate);

            //--------------------------------------------------
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    class Async_validateEmailMobno extends AsyncTask<JSONObject, Void, Boolean> {

        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(Signup1.this);
            dialog.setMessage("Validating. Please Wait...");
            dialog.show();
            dialog.setCancelable(false);
        }

        @Override
        protected Boolean doInBackground(JSONObject... urls) {
            try {

                System.out.println("Parameters---------------" + urls[0]);

                JSONParser jParser = new JSONParser();
                login_jsonobj = jParser.JSON_POST(urls[0], "validatemobnoexists");

                System.out.println("Signup json---------------" + login_jsonobj.toString());

                return true;
            } catch (Exception e) {
                e.printStackTrace();
            }

            return false;
        }

        protected void onPostExecute(Boolean result) {

            try {

                isValid_val = login_jsonobj.getString("isValid");

                System.out.println("isValid_val ---" + isValid_val);

                if (isValid_val.equals("true")) {

                    if (selectedPath != null && !selectedPath.isEmpty() && !selectedPath.equals("null") && !selectedPath.equals("")) {

                        signup();

                    } else {
                        final MaterialDialog alert = new MaterialDialog(Signup1.this);
                        alert.setTitle("profile Photo");
                        alert.setMessage("You are not uploaded profile photo, Do you want to continue without photo?");
                        alert.setCanceledOnTouchOutside(false);
                        alert.setPositiveButton("Yes", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                alert.dismiss();
                                signup();
                            }
                        });
                        alert.setNegativeButton("No", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                alert.dismiss();
                            }
                        });
                        alert.show();
                    }

                } else {

                    if (login_jsonobj.has("err")) {
                        err_val = login_jsonobj.getString("err");
                    }

                    if (login_jsonobj.has("t")) {
                        String type_val = login_jsonobj.getString("t");

                        if (type_val.equals("mob")) {
                            edt_phoneno.requestFocus();
                            edt_phoneno.setError(err_val);
                        }
                        if (type_val.equals("email")) {
                            edt_emailid.requestFocus();
                            edt_emailid.setError(err_val);
                        }
                    } else {
                        Toast.makeText(getApplicationContext(), err_val, Toast.LENGTH_SHORT).show();
                    }
                }

                getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

                dialog.cancel();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    public void signup() {

        try {

            System.out.println("Pwd is Valid-----------");
            login_json = new JSONObject();

            phno = phno.replaceAll(" ", "");

            login_json.put("username", userid);
            login_json.put("name", fullanme);
            login_json.put("gender", gender_code);
            login_json.put("dob", cons_select_date);
            login_json.put("mobile", phno);
            login_json.put("ccode", cccode);
            login_json.put("email", emailid);
            login_json.put("pwd", pwd);

            System.out.println("Signup_json----" + login_json.toString());

            new JSON_signup().execute(login_json);


            //------------ Google firebase Analitics--------------------
            Model.mFirebaseAnalytics = FirebaseAnalytics.getInstance(getApplicationContext());
            Bundle params = new Bundle();
            params.putString("Details", login_json.toString());
            Model.mFirebaseAnalytics.logEvent("Doctor_Signup_stage1", params);
            //------------ Google firebase Analitics--------------------


        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private class Async_Check_Uname extends AsyncTask<JSONObject, Void, Boolean> {

        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(Signup1.this);
            dialog.setMessage("Checking user id.. Please wait..");
            dialog.show();
            dialog.setCancelable(false);
        }

        @Override
        protected Boolean doInBackground(JSONObject... urls) {
            try {
                JSONParser jParser = new JSONParser();
                json_uname_check = jParser.JSON_POST(urls[0], "check_uname");

                System.out.println("json_uname_check Params---------------" + urls[0]);
                System.out.println("json_uname_check-----------" + json_uname_check.toString());

                return true;
            } catch (Exception e) {
                e.printStackTrace();
            }

            return false;
        }

        protected void onPostExecute(Boolean result) {

            try {

                String status_val = json_uname_check.getString("status");

                if (json_uname_check.has("msg")) {
                    msg_text = json_uname_check.getString("msg");
                } else {
                    msg_text = "";
                }

                System.out.println("status_val--------------" + status_val);

                if (status_val.equals("1")) {

                    layout_check.setVisibility(View.GONE);
                    layout_correct.setVisibility(View.VISIBLE);
                    layout_wrong.setVisibility(View.GONE);

                } else {
                    Toast.makeText(getApplicationContext(), msg_text, Toast.LENGTH_LONG).show();
                    edt_userid.requestFocus();
                    edt_userid.setError("Provided User Id is not available. Please enter a different one.");

                    layout_check.setVisibility(View.GONE);
                    layout_correct.setVisibility(View.GONE);
                    layout_wrong.setVisibility(View.VISIBLE);

                }

                dialog.cancel();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    private String upload_file(String file_path) {

        last_upload_file = file_path;
        String ServerUploadPath = Model.BASE_URL + "/sapp/uploadDocPhoto?os_type=android&user_id=" + (Model.id) + "&token=" + Model.token;

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

    public static boolean isValidPassword(final String password) {

        Pattern pattern;
        Matcher matcher;
        final String PASSWORD_PATTERN = "^(?=.*[0-9])(?=.*[A-Z])(?=.*[@#$%^&+=!])(?=\\S+$).{4,}$";
        pattern = Pattern.compile(PASSWORD_PATTERN);
        matcher = pattern.matcher(password);

        return matcher.matches();

    }

    public void diag_err_pwd() {


        try {
            final MaterialDialog alert = new MaterialDialog(Signup1.this);
            alert.setTitle("Incorrect password. Please try again.");
            alert.setMessage("Password should contain atleast 1 Uppercase, 1 lowercase, 1 number, 1 special symbol and minimum 8 charecters");
            alert.setCanceledOnTouchOutside(false);
            alert.setPositiveButton("OK", new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    edt_password.setError("Incorrect password. Please try again.");
                    edt_password.requestFocus();

                    alert.dismiss();
                }
            });
/*

            alert.setNegativeButton("No", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    alert.dismiss();
                }
            });
*/

            alert.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}