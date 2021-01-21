package com.orane.docassist;

import android.app.ProgressDialog;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;



import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.flurry.android.FlurryAgent;

import com.google.firebase.analytics.FirebaseAnalytics;

import com.orane.docassist.Model.Model;
import com.orane.docassist.Network.Detector;
import com.orane.docassist.Network.JSONParser;
import com.orane.docassist.R;
import com.rengwuxian.materialedittext.MaterialEditText;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import me.drakeet.materialdialog.MaterialDialog;


public class Invite_Pat_To_Hotline extends AppCompatActivity implements AdapterView.OnItemClickListener {

    AutoCompleteTextView autoCompView;

    private static final String LOG_TAG = "SignupPlace";
    private static final String PLACES_API_BASE = "https://maps.googleapis.com/maps/api/place";
    private static final String TYPE_AUTOCOMPLETE = "/autocomplete";
    private static final String OUT_JSON = "/json";
    private static final String API_KEY = ""; //Browser key
    Button btn_submit;
    public String pname, pemail, pmobno, get_pat_name, get_pat_email, get_pat_mobile, ccode_txt, ccode_val, status, cname, sstreet, clinic_street, edt_city, place_id_txt, clinic_id, clinic_name, clinic_geo;
    MaterialEditText edt_cname, edt_street, edt_state, edt_zip, edt_country;
    Map<String, String> placeid_map = new HashMap<String, String>();
    JSONObject adpatient_json, addpatient_jsonobj;
    MaterialEditText edt_pname, edt_pemail, edt_mobno;
    Spinner spinner_ccode;
    Button btn_done;

    Map<String, String> cc_map = new HashMap<String, String>();
    Map<String, String> pat_map_email = new HashMap<String, String>();
    Map<String, String> pat_map_phno = new HashMap<String, String>();

    ArrayAdapter<String> pat_dataAdapter;
    Toolbar toolbar;

    TextView refer_text, tv_tit2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.invite_patient_tohotline);

        //------------ Object Creations -------------------------------
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setTitle("Invite a Patient");
        }
        //------------ Object Creations -------------------------------

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.app_color));
        }

        edt_pname = (MaterialEditText) findViewById(R.id.edt_pname);
        edt_pemail = (MaterialEditText) findViewById(R.id.edt_pemail);
        edt_mobno = (MaterialEditText) findViewById(R.id.edt_mobno);
        spinner_ccode = (Spinner) findViewById(R.id.spinner_ccode);
        btn_done = (Button) findViewById(R.id.btn_done);
        refer_text = (TextView) findViewById(R.id.refer_text);
        tv_tit2 = (TextView) findViewById(R.id.tv_tit2);

        Typeface font_reg = Typeface.createFromAsset(getAssets(), Model.font_name);
        Typeface font_bold = Typeface.createFromAsset(getAssets(), Model.font_name_bold);

        refer_text.setTypeface(font_bold);
        tv_tit2.setTypeface(font_reg);
        edt_pname.setTypeface(font_reg);
        edt_pemail.setTypeface(font_reg);
        edt_mobno.setTypeface(font_reg);
        btn_done.setTypeface(font_bold);

        if (new Detector().isTablet(getApplicationContext())) {

            refer_text.setTextSize(30);
            tv_tit2.setTextSize(25);

            edt_pname.setTextSize(25);
            edt_pemail.setTextSize(25);
            edt_mobno.setTextSize(25);

            btn_done.setHeight(55);
            btn_done.setTextSize(25);
        }

        country_code();

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


        btn_done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                pname = edt_pname.getText().toString();
                pemail = edt_pemail.getText().toString();
                pmobno = edt_mobno.getText().toString();

                ccode_txt = spinner_ccode.getSelectedItem().toString();
                ccode_val = cc_map.get(ccode_txt);


                if (isInternetOn()) {

                    try {
                        if (pname.equals("")) edt_pname.setError("Please enter the patient name");
                        else if (pemail.equals("")) edt_pemail.setError("Please enter the email address");
                        else if (pmobno.equals("")) edt_pemail.setError("Mobile number is mandatory");
                        else {

                            adpatient_json = new JSONObject();
                            adpatient_json.put("user_id", (Model.id));
                            adpatient_json.put("name", pname);
                            adpatient_json.put("email", pemail);
                            adpatient_json.put("country_code", ccode_val);
                            adpatient_json.put("mobile", pmobno);

                            System.out.println("Add Patient---" + adpatient_json.toString());

                            new JSON_AddPatient().execute(adpatient_json);

                            //------------ Google firebase Analitics--------------------
                            Model.mFirebaseAnalytics = FirebaseAnalytics.getInstance(getApplicationContext());
                            Bundle params = new Bundle();
                            params.putString("User", Model.id + "/" + Model.name);
                            Model.mFirebaseAnalytics.logEvent("Hotline_invite", params);
                            //------------ Google firebase Analitics--------------------

                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Please check your Internet Connection and try again", Toast.LENGTH_LONG).show();
                }
            }
        });
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


    public final boolean isInternetOn() {

        ConnectivityManager connec =
                (ConnectivityManager) getSystemService(getBaseContext().CONNECTIVITY_SERVICE);

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
        return false;
    }

    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
        String str = (String) adapterView.getItemAtPosition(position);

        Toast.makeText(this, str, Toast.LENGTH_SHORT).show();

        place_id_txt = placeid_map.get(str);

        System.out.println("str----" + str);
        System.out.println("place_id_txt----" + place_id_txt);

    }

    public ArrayList<String> autocomplete(String input) {
        ArrayList<String> resultList = null;

        HttpURLConnection conn = null;
        StringBuilder jsonResults = new StringBuilder();
        try {
            StringBuilder sb = new StringBuilder(PLACES_API_BASE + TYPE_AUTOCOMPLETE + OUT_JSON);
            sb.append("?key=" + API_KEY);
            sb.append("&input=" + URLEncoder.encode(input, "utf8"));

            URL url = new URL(sb.toString());

            System.out.println("URL: " + url);
            conn = (HttpURLConnection) url.openConnection();
            InputStreamReader in = new InputStreamReader(conn.getInputStream());

            // Load the results into a StringBuilder
            int read;
            char[] buff = new char[1024];
            while ((read = in.read(buff)) != -1) {
                jsonResults.append(buff, 0, read);
            }

        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Error processing Places API URL", e);
            return resultList;
        } catch (IOException e) {
            Log.e(LOG_TAG, "Error connecting to Places API", e);
            return resultList;
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }

        try {
            JSONObject jsonObj = new JSONObject(jsonResults.toString());
            JSONArray predsJsonArray = jsonObj.getJSONArray("predictions");

            resultList = new ArrayList<String>(predsJsonArray.length());

            for (int i = 0; i < predsJsonArray.length(); i++) {

                System.out.println(predsJsonArray.getJSONObject(i).getString("description"));
                System.out.println(predsJsonArray.getJSONObject(i).getString("place_id"));
                String descp_txt = predsJsonArray.getJSONObject(i).getString("description");
                place_id_txt = predsJsonArray.getJSONObject(i).getString("place_id");
                System.out.println("============================================================");
                placeid_map.put(descp_txt, place_id_txt);
                resultList.add(predsJsonArray.getJSONObject(i).getString("description"));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return resultList;
    }


    private class JSON_AddPatient extends AsyncTask<JSONObject, Void, Boolean> {

        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            dialog = new ProgressDialog(Invite_Pat_To_Hotline.this);
            dialog.setMessage("Please wait");
            dialog.show();
            dialog.setCancelable(false);
        }

        @Override
        protected Boolean doInBackground(JSONObject... urls) {

            try {
                JSONParser jParser = new JSONParser();
                addpatient_jsonobj = jParser.JSON_POST(urls[0], "addPatient");

                System.out.println("urls[0]----------" + urls[0]);
                return true;
            } catch (Exception e) {
                e.printStackTrace();
            }
            return false;
        }

        protected void onPostExecute(Boolean result) {

            try {
                status = addpatient_jsonobj.getString("status");
                System.out.println("status----------" + status);

                if (status.equals("1")) {

                    Fee_Success();

                    try {

                        //----------- Flurry -------------------------------------------------
                        Map<String, String> articleParams = new HashMap<String, String>();
                        articleParams.put("android.doc.Doctor_ID", (Model.id));
                        articleParams.put("android.doc.Patient_name", pname);
                        articleParams.put("android.doc.Country_code", ccode_val);
                        articleParams.put("android.doc.Mobile_No", pmobno);
                        FlurryAgent.logEvent("android.doc.Hotline_Invite_Patient_Success", articleParams);
                        //----------- Flurry -------------------------------------------------

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    //Toast.makeText(getApplicationContext(), "Patient Invited Successfully", Toast.LENGTH_LONG).show();

                } else {

                    try {

                        //----------- Flurry -------------------------------------------------
                        Map<String, String> articleParams = new HashMap<String, String>();
                        articleParams.put("android.doc.Doctor_ID", (Model.id));
                        articleParams.put("android.doc.Patient_name", pname);
                        articleParams.put("android.doc.Country_code", ccode_val);
                        articleParams.put("android.doc.Mobile_No", pmobno);
                        FlurryAgent.logEvent("android.doc.Hotline_Invite_Patient_Failed", articleParams);
                        //----------- Flurry -------------------------------------------------

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    Toast.makeText(getApplicationContext(), "Your requested update failed. Please try again..!", Toast.LENGTH_LONG).show();
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


        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(Invite_Pat_To_Hotline.this, android.R.layout.simple_spinner_item, categories);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_ccode.setAdapter(dataAdapter);
    }

    public void Fee_Success() {


        //---------------- Dialog------------------------------------------------------------------
        final MaterialDialog alert = new MaterialDialog(Invite_Pat_To_Hotline.this);
        alert.setTitle("Invited successfully..!");
        alert.setMessage("Your invitation has been sent to the patient");
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


    }

}
