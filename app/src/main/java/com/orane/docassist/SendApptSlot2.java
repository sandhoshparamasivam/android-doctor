package com.orane.docassist;

import android.app.ProgressDialog;
import android.content.Context;
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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;

import com.flurry.android.FlurryAgent;

import com.google.firebase.analytics.FirebaseAnalytics;

import com.orane.docassist.Model.Model;
import com.orane.docassist.Network.JSONParser;
import com.orane.docassist.R;
import com.rengwuxian.materialedittext.MaterialEditText;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SendApptSlot2 extends AppCompatActivity implements
        View.OnClickListener {

    Spinner spinner_mypatients;
    public String str_response, pat_id, pat_name;
    LinearLayout newpat_layout;
    Button btn_submit;

    Map<String, String> cc_map = new HashMap<String, String>();
    Map<String, String> pat_map_email = new HashMap<String, String>();
    Map<String, String> pat_map_phno = new HashMap<String, String>();

    JSONArray pat_json_array;
    public String appt_id, inv_id, err, ccode_val, pat_emailid, ccode_txt, pat_phno, date_txt, get_pat_email, get_pat_mobile, timezone_val, get_pat_name, time_txt, notes, cons_type, clinic_id, curr_code, fees;
    List<String> pat_categories = new ArrayList<String>();
    JSONObject jsonobj1c_cl, postappt_json, PostSlot_jsonobj;
    ArrayAdapter<String> pat_dataAdapter;

    MaterialEditText edt_pname, edt_pemail, edt_mobno;
    Spinner spinner_ccode;

    SharedPreferences sharedpreferences;
    public static final String MyPREFERENCES = "MyPrefs";
    public static final String Login_Status = "Login_Status_key";
    public static final String user_name = "user_name_key";
    public static final String Name = "Name_key";
    public static final String password = "password_key";
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
    public static final String sp_km_id = "sp_km_id_key";
    public static final String sp_share_link = "sp_share_link";
    public static final String token = "token_key";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.sendapptslot2);

        //================ Shared Pref ======================
        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        Model.token = sharedpreferences.getString(token, "");
        System.out.println("Model.token----------------------" + Model.token);
        //================ Shared Pref ======================

        //------------ Object Creations -------------------------------
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setTitle("Patient details");
        }
        //------------ Object Creations -------------------------------
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.app_color));
        }

        spinner_mypatients = findViewById(R.id.spinner_mypatients);
        spinner_ccode = findViewById(R.id.spinner_ccode);
        newpat_layout = findViewById(R.id.newpat_layout);
        btn_submit = findViewById(R.id.btn_submit);
        edt_pname = findViewById(R.id.edt_pname);
        edt_pemail = findViewById(R.id.edt_pemail);
        edt_mobno = findViewById(R.id.edt_mobno);

        Typeface font_reg = Typeface.createFromAsset(getAssets(), Model.font_name);
        Typeface font_bold = Typeface.createFromAsset(getAssets(), Model.font_name_bold);

        btn_submit.setTypeface(font_bold);

        try {
            Intent intent = getIntent();

            date_txt = intent.getStringExtra("date");
            timezone_val = intent.getStringExtra("timezone_val");
            time_txt = intent.getStringExtra("time");
            notes = intent.getStringExtra("notes");
            cons_type = intent.getStringExtra("cons_type");
            clinic_id = intent.getStringExtra("clinic_id");
            curr_code = intent.getStringExtra("curr_code");
            fees = intent.getStringExtra("fees");

            System.out.println("Get Intent date_txt----------" + date_txt);
            System.out.println("Get Intent timezone_val----------" + timezone_val);
            System.out.println("Get Intent time_txt----------" + time_txt);
            System.out.println("Get Intent notes----------" + notes);
            System.out.println("Get Intent cons_type----------" + cons_type);
            System.out.println("Get Intent clinic_id----------" + clinic_id);
            System.out.println("Get Intent curr_code----------" + curr_code);
            System.out.println("Get Intent fees----------" + fees);

        } catch (Exception e) {
            e.printStackTrace();
        }

        country_code();

        //-------------spinner_clinics-------------------------------------------
        String mypat_url = Model.BASE_URL + "sapp/myPatients?os_type=android&user_id=" + (Model.id) + "&page=1&token=" + Model.token + "&q=";
        System.out.println("mypat_url----" + mypat_url);
        new JSON_mypat().execute(mypat_url);
        //-------------spinner_clinics-------------------------------------------


        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    if (pat_name.equals("Add New Patient")) {

                        pat_name = edt_pname.getText().toString();
                        pat_emailid = edt_pemail.getText().toString();
                        pat_phno = edt_mobno.getText().toString();

                        ccode_txt = spinner_ccode.getSelectedItem().toString();
                        ccode_val = cc_map.get(ccode_txt);

                    } else {

                        pat_name = spinner_mypatients.getSelectedItem().toString();
                        pat_emailid = pat_map_email.get(pat_name);
                        pat_phno = pat_map_phno.get(pat_name);

                        String[] separated = pat_phno.split(" ");
                        ccode_val = separated[0];
                        pat_phno = separated[1];

                        System.out.println("====pat_name---------" + pat_name);
                        System.out.println("==pat_emailid---------" + pat_emailid);
                        System.out.println("====pat_phno---------" + pat_phno);
                    }

                    System.out.println("Submit-pname----" + pat_name);
                    System.out.println("Submit-pemail----" + pat_emailid);
                    System.out.println("Submit-ccode_val----" + ccode_val);
                    System.out.println("Submit-pmobno----" + pat_phno);

                    System.out.println("Get Intent date_txt----------" + date_txt);
                    System.out.println("Get Intent timezone_val----------" + timezone_val);
                    System.out.println("Get Intent time_txt----------" + time_txt);
                    System.out.println("Get Intent notes----------" + notes);
                    System.out.println("Get Intent cons_type----------" + cons_type);
                    System.out.println("Get Intent clinic_id----------" + clinic_id);
                    System.out.println("Get Intent curr_code----------" + curr_code);
                    System.out.println("Get Intent fees----------" + fees);

                    try {

                        postappt_json = new JSONObject();
                        postappt_json.put("user_id", (Model.id));

                        postappt_json.put("name", pat_name);
                        postappt_json.put("email", pat_emailid);
                        postappt_json.put("cc", ccode_val);
                        postappt_json.put("mobile", pat_phno);
                        postappt_json.put("appointment_date", date_txt);
                        postappt_json.put("appt_timezone", timezone_val);
                        postappt_json.put("appointment_type", cons_type);
                        postappt_json.put("notes", notes);
                        postappt_json.put("address_id", clinic_id);
                        postappt_json.put("patient_id", pat_id);
                        postappt_json.put("currency_id", curr_code);
                        postappt_json.put("fee", fees);
                        postappt_json.put("appt_time", time_txt);

                        System.out.println("postappt_json------------->" + postappt_json.toString());


                        //------------ Google firebase Analitics--------------------
                        Model.mFirebaseAnalytics = FirebaseAnalytics.getInstance(getApplicationContext());
                        Bundle params = new Bundle();
                        params.putString("Details", "");
                        Model.mFirebaseAnalytics.logEvent("Send_Appointment_Slot", params);
                        //------------ Google firebase Analitics--------------------

                        //-------------------------------------------------------------
                        HashMap<String, String> properties = new HashMap<String, String>();
                        properties.put("android.doc.Appt_Date", date_txt);
                        properties.put("android.doc.Time_Zone", timezone_val);
                        properties.put("android.doc.Appt_Time", time_txt);
                        properties.put("android.doc.notes", notes);
                        properties.put("android.doc.cons_type", cons_type);
                        properties.put("android.doc.Clinic_id", clinic_id);
                        properties.put("android.doc.Currency_code", curr_code);
                        properties.put("android.doc.Appt_Fees", fees);
                        properties.put("android.doc.pat_id", pat_id);
                        properties.put("android.doc.pat_name", pat_name);
                        properties.put("android.doc.pat_emailid", pat_emailid);
                        properties.put("android.doc.Country_Code", ccode_val);
                        properties.put("android.doc.pat_phno", pat_phno);


                        //----------- Flurry -------------------------------------------------
                        Map<String, String> articleParams = new HashMap<String, String>();
                        articleParams.put("android.doc.Appt_Date", date_txt);
                        articleParams.put("android.doc.Time_Zone", timezone_val);
                        articleParams.put("android.doc.Appt_Time", time_txt);
                        articleParams.put("android.doc.notes", notes);
                        articleParams.put("android.doc.cons_type", cons_type);
                        articleParams.put("android.doc.Clinic_id", clinic_id);
                        articleParams.put("android.doc.Currency_code", curr_code);
                        articleParams.put("android.doc.Appt_Fees", fees);
                        articleParams.put("android.doc.pat_id", pat_id);
                        articleParams.put("android.doc.pat_name", pat_name);
                        articleParams.put("android.doc.pat_emailid", pat_emailid);
                        articleParams.put("android.doc.Country_Code", ccode_val);
                        articleParams.put("android.doc.pat_phno", pat_phno);
                        FlurryAgent.logEvent("android.doc.Send_Appointment_Slot", articleParams);
                        //----------- Flurry -------------------------------------------------

                        new JSON_Send_Appt().execute(postappt_json);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });


        //------------------------------------------------------------------------------------
        spinner_mypatients.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> arg0, View view, int arg2, long arg3) {

                pat_name = spinner_mypatients.getSelectedItem().toString();
                pat_emailid = pat_map_email.get(pat_name);
                pat_phno = pat_map_phno.get(pat_name);

                System.out.println("pat_name---------" + pat_name);
                System.out.println("pat_emailid---------" + pat_emailid);
                System.out.println("pat_phno---------" + pat_phno);


                if (pat_emailid != null && !pat_emailid.isEmpty() && !pat_emailid.equals("null") && !pat_emailid.equals("null")) {
                    if (pat_emailid.equals("0")) {
                        newpat_layout.setVisibility(View.VISIBLE);
                    } else {
                        newpat_layout.setVisibility(View.GONE);
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {

            }
        });
        //------------------------------------------------------------------------------------


        //------------------------------------------------------------------------------------
        spinner_ccode.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> arg0, View view, int arg2, long arg3) {

                ccode_txt = spinner_ccode.getSelectedItem().toString();
                ccode_val = cc_map.get(ccode_txt);

                System.out.println("ccode_txt---------" + ccode_txt);
                System.out.println("ccode_val---------" + ccode_val);
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {

            }
        });
        //------------------------------------------------------------------------------------


    }

    @Override
    public void onClick(View view) {

    }

    private class JSON_mypat extends AsyncTask<String, Void, Boolean> {

        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            dialog = new ProgressDialog(SendApptSlot2.this);
            dialog.setMessage("Please wait..");
            dialog.show();
            dialog.setCancelable(false);
        }

        @Override
        protected Boolean doInBackground(String... urls) {

            try {
                str_response = new JSONParser().getJSONString(urls[0]);
                System.out.println("str_response--------------" + str_response);

                return true;

            } catch (Exception e) {
                e.printStackTrace();
            }

            return false;
        }

        protected void onPostExecute(Boolean result) {

            try {

                //----------------------------------------------------------
                Object json = new JSONTokener(str_response).nextValue();
                if (json instanceof JSONObject) {
                    System.out.println("This is JSON OBJECT---------------" + str_response);

                    JSONObject jobject = new JSONObject(str_response);

                    if (jobject.has("token_status")) {
                        String token_status = jobject.getString("token_status");
                        if (token_status.equals("0")) {

                            //============================================================
                            SharedPreferences.Editor editor = sharedpreferences.edit();
                            editor.putString(Login_Status, "0");
                            editor.apply();
                            //============================================================

                            finishAffinity();
                            Intent intent = new Intent(SendApptSlot2.this, LoginActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    }

                } else if (json instanceof JSONArray) {
                    System.out.println("This is JSON ARRAY---------------" + str_response);

                    pat_json_array = new JSONArray(str_response);

                    for (int i = 0; i < pat_json_array.length(); i++) {
                        jsonobj1c_cl = pat_json_array.getJSONObject(i);
                        System.out.println("jsonobj1c_cl-----" + jsonobj1c_cl.toString());

                        get_pat_name = jsonobj1c_cl.getString("name");
                        get_pat_email = jsonobj1c_cl.getString("email");
                        get_pat_mobile = jsonobj1c_cl.getString("mobile");

                        System.out.println("get_pat_name-----" + get_pat_name);
                        System.out.println("get_pat_email-----" + get_pat_email);
                        System.out.println("get_pat_mobile-----" + get_pat_mobile);

                        //======================================================================
                        pat_categories.add(get_pat_name);
                        pat_map_email.put(get_pat_name, get_pat_email);
                        pat_map_phno.put(get_pat_name, get_pat_mobile);
                        //======================================================================
                    }

                    //======================================================================
                    pat_categories.add("Add New Patient");
                    pat_map_email.put("Add New Patient", "0");
                    pat_map_phno.put("Add New Patient", "0");
                    //=========================================================

                    pat_dataAdapter = new ArrayAdapter<String>(SendApptSlot2.this, android.R.layout.simple_spinner_item, pat_categories);
                    pat_dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner_mypatients.setAdapter(pat_dataAdapter);
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

            dialog.cancel();

        }
    }

    public void country_code() {

        final List<String> categories = new ArrayList<String>();

        categories.add("India (+91)");
        cc_map.put("India (+91)", "91");
        categories.add("US and Canada (+1)");
        cc_map.put("US and Canada (+1)", "1");
        categories.add("United Kingdom (+44)");
        cc_map.put("United Kingdom (+44)", "44");
        categories.add("Pakistan(+92)");
        cc_map.put("Pakistan(+92)", "92");
        categories.add("Australia (+61)");
        cc_map.put("Australia (+61)", "61");
        categories.add("Philippines (+63)");
        cc_map.put("Philippines (+63)", "63");
        categories.add("Nigeria (+234)");
        cc_map.put("Nigeria (+234)", "234");
        categories.add("Malaysia (+60)");
        cc_map.put("Malaysia (+60)", "60");
        categories.add("Bangladesh (+880)");
        cc_map.put("Bangladesh (+880)", "880");
        categories.add("Kenya (+254)");
        cc_map.put("Kenya (+254)", "254");
        categories.add("spain (+34)");
        cc_map.put("spain (+34)", "34");
        categories.add("Saudi Arabia(+966)");
        cc_map.put("Saudi Arabia(+966)", "966");
        categories.add("Singapore (+65)");
        cc_map.put("Singapore (+65)", "65");
        categories.add("Netherlands (+31)");
        cc_map.put("Netherlands (+31)", "31");
        categories.add("Qatar(+974)");
        cc_map.put("Qatar(+974)", "974");
        categories.add("United Arab Emirates(+971)");
        cc_map.put("UAE (+971)", "971");

        categories.add("---------------");
        categories.add("Austria (+43)");
        cc_map.put("Austria (+43)", "43");

        categories.add("Egypt (+20)");
        cc_map.put("Egypt (+20)", "20");

        categories.add("South Sudan (+211)");
        cc_map.put("South Sudan (+211)", "211");

        categories.add("Morocco (+212)");
        cc_map.put("Morocco (+212)", "212");

        categories.add("Algeria (+213)");
        cc_map.put("Algeria (+213)", "213");

        categories.add("Tunisia (+216)");
        cc_map.put("Tunisia (+216)", "216");

        categories.add("Libya (+218)");
        cc_map.put("Libya (+218)", "218");

        categories.add("Gambia (+220)");
        cc_map.put("Gambia (+220)", "220");

        categories.add("Senegal (+221)");
        cc_map.put("Senegal (+221)", "221");

        categories.add("Mauritania(+222)");
        cc_map.put("Mauritania(+222)", "222");

        categories.add("Mali (+223)");
        cc_map.put("Mali (+223)", "223");

        categories.add("Guinea (+224)");
        cc_map.put("Guinea (+224)", "224");

        categories.add("Ivory Coast (+225)");
        cc_map.put("Ivory Coast (+225)", "225");

        categories.add("Burkina Faso (+226)");
        cc_map.put("Burkina Faso (+226)", "226");

        categories.add("Niger (+227)");
        cc_map.put("Niger (+227)", "227");

        categories.add("Togo (+228)");
        cc_map.put("Togo (+228)", "228");

        categories.add("Benin(+229)");
        cc_map.put("Benin(+229)", "229");

        categories.add("Mauritius (+230)");
        cc_map.put("Mauritius (+230)", "230");

        categories.add("Liberia (+231)");
        cc_map.put("Liberia (+231)", "231");

        categories.add("Sierra Leone (+232)");
        cc_map.put("Sierra Leone (+232)", "232");

        categories.add("Ghana (+233)");
        cc_map.put("Ghana (+233)", "233");

        categories.add("Chad (+235)");
        cc_map.put("Chad (+235)", "235");

        categories.add("Central African Republic (+236)");
        cc_map.put("Central African Republic (+236)", "236");

        categories.add("Cameroon (+237)");
        cc_map.put("Cameroon (+237)", "237");

        categories.add("Cape Verde (+238)");
        cc_map.put("Cape Verde (+238)", "238");

        categories.add("Sao Tome and Príncipe (+239)");
        cc_map.put("Sao Tome and Príncipe (+239)", "239");

        categories.add("Equatorial Guinea (+240)");
        cc_map.put("Equatorial Guinea (+240)", "240");

        categories.add("Gabon (+241)");
        cc_map.put("Gabon (+241)", "241");

        categories.add("Republic of the Congo (+242)");
        cc_map.put("Republic of the Congo (+242)", "242");

        categories.add("Democratic Republic of the congo (+243)");
        cc_map.put("Democratic Republic of the congo (+243)", "243");

        categories.add("Angola (+244)");
        cc_map.put("Angola (+244)", "244");

        categories.add("Guinea-Bissau (+245)");
        cc_map.put("Guinea-Bissau (+245)", "245");

        categories.add("British Indian Ocean Territory (+246)");
        cc_map.put("British Indian Ocean Territory (+246)", "246");

        categories.add("Ascension Island (+247)");
        cc_map.put("Ascension Island (+247)", "247");

        categories.add("Seychelles (+248)");
        cc_map.put("Seychelles (+248)", "248");

        categories.add("Sudan (+249)");
        cc_map.put("Sudan (+249)", "249");

        categories.add("Rwanda (+250)");
        cc_map.put("Rwanda (+250)", "250");

        categories.add("Ethiopia (+251)");
        cc_map.put("Ethiopia (+251)", "251");

        categories.add("Somalia (+252)");
        cc_map.put("Somalia (+252)", "252");

        categories.add("Djibouti (+253)");
        cc_map.put("Djibouti (+253", "253");

        categories.add("Tanzania (+255)");
        cc_map.put("Tanzania (+255)", "255");

        categories.add("Uganda (+256)");
        cc_map.put("Uganda (+256)", "256");

        categories.add("Berundi (+257)");
        cc_map.put("Uganda (+256)", "257");

        categories.add("Mozambique (+258)");
        cc_map.put("Mozambique (+258)", "258");

        categories.add("Zambia (+260)");
        cc_map.put("Zambia (+260)", "260");

        categories.add("Madagascar (+261)");
        cc_map.put("Madagascar (+261)", "261");

        categories.add("Reunion (+262)");
        cc_map.put("Reunion (+262)", "262");

        categories.add("Zimbabwe (+263)");
        cc_map.put("Zimbabwe (+263)", "263");

        categories.add("Namibia (+264)");
        cc_map.put("Namibia (+264)", "264");

        categories.add("Malawi (+265)");
        cc_map.put("Malawi (+265)", "265");

        categories.add("Lesotho (+266)");
        cc_map.put("Lesotho (+266)", "266");

        categories.add("Botswana (+267)");
        cc_map.put("Botswana (+267)", "267");

        categories.add("Swaziland (+268)");
        cc_map.put("Swaziland (+268)", "268");

        categories.add("Saint  Helena (+290)");
        cc_map.put("Saint  Helena (+290)", "290");

        categories.add("Aruba (+297)");
        cc_map.put("Aruba (+297)", "297");

        categories.add("Faroe Islands (+298)");
        cc_map.put("Faroe Islands (+298)", "298");

        categories.add("Greenland (+299)");
        cc_map.put("Greenland (+299)", "299");

        categories.add("South Africa (+27)");
        cc_map.put("South Africa (+27)", "27");

        categories.add("Greece (+30)");
        cc_map.put("Greece (+30)", "30");

        categories.add("Belgium (+32)");
        cc_map.put("Belgium (+32)", "32");

        categories.add("France (+33)");
        cc_map.put("France (+33)", "33");

        categories.add("Gibraltar (+350)");
        cc_map.put("Gibraltar (+350)", "350");

        categories.add("Portugal (+351)");
        cc_map.put("Portugal (+351)", "351");

        categories.add("Luxembourg (+352)");
        cc_map.put("Luxembourg (+352)", "352");

        categories.add("Ireland (+353)");
        cc_map.put("Ireland (+353)", "353");

        categories.add("Iceland(+354)");
        cc_map.put("Iceland(+354)", "354");

        categories.add("Albania (+355)");
        cc_map.put("Albania (+355)", "355");

        categories.add("Malta (+356)");
        cc_map.put("Malta (+356)", "356");

        categories.add("Cyprus (+357)");
        cc_map.put("Cyprus (+357)", "357");

        categories.add("Finland (+358)");
        cc_map.put("Finland (+358)", "358");

        categories.add("Bulgaria (+359)");
        cc_map.put("Bulgaria (+359)", "359");

        categories.add("Hungary (+36)");
        cc_map.put("Hungary (+36)", "36");

        categories.add("Italy (+39)");
        cc_map.put("Italy (+39)", "39");

        categories.add("Lithuania (+370)");
        cc_map.put("Lithuania (+370)", "370");

        categories.add("Latvia (+371)");
        cc_map.put("Latvia (+371)", "371");

        categories.add("Estonia(+372)");
        cc_map.put("Estonia(+372)", "372");

        categories.add("Moldova (+373)");
        cc_map.put("Moldova (+373)", "373");

        categories.add("Armenia (+374)");
        cc_map.put("Armenia (+374)", "374");

        categories.add("Belarus (+375)");
        cc_map.put("Belarus (+375)", "375");

        categories.add("Andorra (+376)");
        cc_map.put("Andorra (+376)", "376");

        categories.add("Monaco (+377)");
        cc_map.put("Monaco (+377)", "377");

        categories.add("San Marino (+378)");
        cc_map.put("San Marino (+378)", "378");

        categories.add("Vatican City (+379)");
        cc_map.put("Vatican City (+379)", "379");

        categories.add("Ukraine (+380)");
        cc_map.put("Ukraine (+380)", "380");

        categories.add("Serbia (+381)");
        cc_map.put("Serbia (+381)", "381");

        categories.add("Montenegro (+383)");
        cc_map.put("Montenegro (+383)", "383");

        categories.add("Croatia (+385)");
        cc_map.put("Croatia (+385)", "385");

        categories.add("Slovenia (+386)");
        cc_map.put("Slovenia (+386)", "386");

        categories.add("Bosnia and Herzegovina (+387)");
        cc_map.put("Bosnia and Herzegovina (+387)", "387");

        categories.add("Romania (+40)");
        cc_map.put("Romania (+40)", "40");

        categories.add("Switzerland (+41)");
        cc_map.put("Switzerland (+41)", "41");

        categories.add("Czech Republic (+420)");
        cc_map.put("Czech Republic (+420)", "420");

        categories.add("Slovakia (+421)");
        cc_map.put("Slovakia (+421)", "421");

        categories.add("Denmark (+45)");
        cc_map.put("Denmark (+45)", "45");

        categories.add("Sweden (+46)");
        cc_map.put("Sweden (+46)", "46");

        categories.add("Norway (+47)");
        cc_map.put("Norway (+47)", "47");

        categories.add("Poland (+48)");
        cc_map.put("Poland (+48)", "48");

        categories.add("Germany (+49)");
        cc_map.put("Germany (+49)", "49");

        categories.add("Peru (+51)");
        cc_map.put("Peru (+51)", "51");

        categories.add("Mexico(+52)");
        cc_map.put("Mexico(+52)", "52");

        categories.add("Cuba (+53)");
        cc_map.put("Cuba (+53)", "53");

        categories.add("Argentina (+54)");
        cc_map.put("Argentina (+54)", "54");

        categories.add("Brazil (+55)");
        cc_map.put("Brazil (+55)", "55");

        categories.add("Chile (+56)");
        cc_map.put("Chile (+56)", "56");

        categories.add("Colombia (+57)");
        cc_map.put("Colombia (+57)", "57");

        categories.add("Venezuela (+58)");
        cc_map.put("Venezuela (+58)", "58");

        categories.add("Guadeloupe (+590)");
        cc_map.put("Guadeloupe (+590)", "590");

        categories.add("Bolivia (+591)");
        cc_map.put("Bolivia (+591)", "591");

        categories.add("Guyana (+592)");
        cc_map.put("Guyana (+592)", "592");

        categories.add("Ecuador (+593)");
        cc_map.put("Ecuador (+593)", "593");

        categories.add("French Guiana (+594)");
        cc_map.put("French Guiana (+594)", "594");

        categories.add("Paraguay (+595)");
        cc_map.put("Paraguay (+595)", "595");

        categories.add("Indonesia (+62)");
        cc_map.put("Indonesia (+62)", "62");

        categories.add("New Zealand (+64)");
        cc_map.put("New Zealand (+64)", "64");

        categories.add("Thailand (+66)");
        cc_map.put("Thailand (+66)", "66");

        categories.add("Martinique (+596)");
        cc_map.put("Martinique (+596)", "596");

        categories.add("Suriname (+597)");
        cc_map.put("Suriname (+597)", "597");

        categories.add("Uruguay(+598)");
        cc_map.put("Uruguay(+598)", "598");

        categories.add("Brunei(+673)");
        cc_map.put("Brunei(+673)", "673");

        categories.add("Nauru(+674)");
        cc_map.put("Nauru(+674)", "674");

        categories.add("Papua new Guinea(+675)");
        cc_map.put("Papua new Guinea(+675)", "675");

        categories.add("Tonga(+676)");
        cc_map.put("Tonga(+676)", "676");

        categories.add("Solomon Islands(+677)");
        cc_map.put("Solomon Islands(+677)", "677");

        categories.add("Vanuatu(+678)");
        cc_map.put("Vanuatu(+678)", "678");

        categories.add("Fizi(+679)");
        cc_map.put("Fizi(+679)", "679");

        categories.add("Palau(+680)");
        cc_map.put("Palau(+680)", "680");

        categories.add("Wallis and Futuna(+681)");
        cc_map.put("Wallis and Futuna(+681)", "681");

        categories.add("Cook islands(+682)");
        cc_map.put("Cook islands(+682)", "682");

        categories.add("Niue(+683)");
        cc_map.put("Niue(+683)", "683");

        categories.add("Tuvalu(+688)");
        cc_map.put("Tuvalu(+688)", "688");

        categories.add("Samoa(+685)");
        cc_map.put("Samoa(+685)", "685");

        categories.add("Kiribati(+686)");
        cc_map.put("Kiribati(+686)", "686");

        categories.add("New Polynesia(+687)");
        cc_map.put("New Polynesia(+687)", "687");

        categories.add("French Polynesia(+689)");
        cc_map.put("French Polynesia(+689)", "689");

        categories.add("Tokelau(+690)");
        cc_map.put("Tokelau(+690)", "690");

        categories.add("Federated States of Micronesia(+691)");
        cc_map.put("Federated States of Micronesia(+691)", "691");

        categories.add("Marshall islands(+692)");
        cc_map.put("Marshall islands(+692)", "692");

        categories.add("Russia(+7)");
        cc_map.put("Russia(+7)", "7");

        categories.add("Japan(+81)");
        cc_map.put("Japan(+81)", "81");

        categories.add("South Korea(+82)");
        cc_map.put("South Korea(+82)", "82");

        categories.add("Vietnam(+84)");
        cc_map.put("Vietnam(+84)", "84");

        categories.add("China(+86)");
        cc_map.put("China(+86)", "86");

        categories.add("Taiwan(+886)");
        cc_map.put("Taiwan(+886)", "886");

        categories.add("Turkey(+90)");
        cc_map.put("Turkey(+90)", "90");

        categories.add("Afghanistan(+93)");
        cc_map.put("Afghanistan(+93)", "93");

        categories.add("Srilanka(+94)");
        cc_map.put("Srilanka(+94)", "94");

        categories.add("Myanmar(+95)");
        cc_map.put("Myanmar(+95)", "95");

        categories.add("Maldives(+960)");
        cc_map.put("Maldives(+960)", "960");

        categories.add("Lebanon(+961)");
        cc_map.put("Lebanon(+961)", "961");

        categories.add("Jordan(+962)");
        cc_map.put("Jordan(+962)", "962");

        categories.add("Syria(+963)");
        cc_map.put("Syria(+963)", "963");

        categories.add("Iraq(+964)");
        cc_map.put("Iraq(+964)", "964");

        categories.add("Kuwait(+965)");
        cc_map.put("Kuwait(+965)", "965");

        categories.add("Yemen(+967)");
        cc_map.put("Yemen(+967)", "967");

        categories.add("Oman(+968)");
        cc_map.put("Oman(+968)", "968");

        categories.add("Palestinian territories(+970)");
        cc_map.put("Palestinian territories(+970)", "970");

        categories.add("Israel(+972)");
        cc_map.put("Israel(+972)", "972");

        categories.add("Bahrain(+973)");
        cc_map.put("Bahrain(+973)", "973");

        categories.add("Bhutan(+975)");
        cc_map.put("Bhutan(+975)", "975");

        categories.add("Mongolia(+976)");
        cc_map.put("Mongolia(+976)", "976");

        categories.add("Nepal(+977)");
        cc_map.put("Nepal(+977)", "977");

        categories.add("Iran(+98)");
        cc_map.put("Iran(+98)", "98");

        categories.add("Tajikistan(+992)");
        cc_map.put("Tajikistan(+992)", "992");

        categories.add("Turkmenistan(+993)");
        cc_map.put("Turkmenistan(+993)", "993");

        categories.add("Azerbaijan(+994)");
        cc_map.put("Azerbaijan(+994)", "994");

        categories.add("Georgia(+995)");
        cc_map.put("Georgia(+995)", "995");

        categories.add("Kyrgyzstan(+996)");
        cc_map.put("Kyrgyzstan(+996)", "996");

        categories.add("Uzbekistan(+998)");
        cc_map.put("Uzbekistan(+998)", "998");


        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(SendApptSlot2.this, android.R.layout.simple_spinner_item, categories);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_ccode.setAdapter(dataAdapter);
    }


    class JSON_Send_Appt extends AsyncTask<JSONObject, Void, Boolean> {

        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            dialog = new ProgressDialog(SendApptSlot2.this);
            dialog.setMessage("Please wait");
            dialog.show();
            dialog.setCancelable(false);
        }

        @Override
        protected Boolean doInBackground(JSONObject... urls) {

            try {

                JSONParser jParser = new JSONParser();
                PostSlot_jsonobj = jParser.JSON_POST(urls[0], "sendSlot");

                System.out.println("urls[0]----------" + urls[0]);
                System.out.println("PostSlot_jsonobj----------" + PostSlot_jsonobj);

                return true;
            } catch (Exception e) {
                e.printStackTrace();
            }
            return false;
        }

        protected void onPostExecute(Boolean result) {

            try {

                if (PostSlot_jsonobj.has("status")) {
                    String status_val = PostSlot_jsonobj.getString("");
                    if (status_val.equals("0")) {
                        finishAffinity();
                        Intent intent = new Intent(SendApptSlot2.this, LoginActivity.class);
                        startActivity(intent);
                        finish();
                    }
                } else {


                    if (PostSlot_jsonobj.has("appt_id")) {
                        appt_id = PostSlot_jsonobj.getString("appt_id");
                    }

                    if (PostSlot_jsonobj.has("inv_id")) {
                        inv_id = PostSlot_jsonobj.getString("inv_id");
                    }

                    if (PostSlot_jsonobj.has("err")) {
                        err = PostSlot_jsonobj.getString("err");
                    }

                    //----------- Flurry -------------------------------------------------
                    Map<String, String> articleParams = new HashMap<String, String>();
                    articleParams.put("android.doc.Date", date_txt);
                    articleParams.put("android.doc.TimeZone", timezone_val);
                    articleParams.put("android.doc.Time", time_txt);
                    articleParams.put("android.doc.Notes", notes);
                    articleParams.put("android.doc.Cons_Type", cons_type);
                    articleParams.put("android.doc.Clinic_ID", Model.clinic_id);
                    articleParams.put("android.doc.Currency_Code", curr_code);
                    articleParams.put("android.doc.patient_ID", pat_id);
                    articleParams.put("android.doc.patient_name", pat_name);
                    articleParams.put("android.doc.patient_email", pat_emailid);
                    articleParams.put("android.doc.patient_country_code", ccode_val);
                    articleParams.put("android.doc.patient_mobile", pat_phno);
                    FlurryAgent.logEvent("android.doc.Sending_Slot_Success", articleParams);
                    //----------- Flurry -------------------------------------------------


                    ((android.os.ResultReceiver) getIntent().getParcelableExtra("finisher")).send(1, new Bundle());

                    Intent intent = new Intent(SendApptSlot2.this, ApptSendSlotThankyou.class);
                    startActivity(intent);
                    finish();
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

            dialog.cancel();

        }
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

        return super.onOptionsItemSelected(item);
    }

}
