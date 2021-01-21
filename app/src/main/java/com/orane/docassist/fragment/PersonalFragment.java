package com.orane.docassist.fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.DialogFragment;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.orane.docassist.Model.Model;
import com.orane.docassist.Model.MultipartEntity2;
import com.orane.docassist.Network.JSONParser;
import com.orane.docassist.R;
import com.orane.docassist.Signup1;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.squareup.picasso.Picasso;

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
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import me.drakeet.materialdialog.MaterialDialog;
import pl.tajchert.nammu.Nammu;


public class PersonalFragment extends DialogFragment {

    MaterialEditText edtname, edt_dob, edt_emailid, edt_panno;
    TextView tv_ccode, tv_upload_photo;
    Button btn_submit;
    Map<String, String> cc_map = new HashMap<String, String>();
    EditText edt_phoneno;
    String edt_text,err_val;
    InputStream is = null;
    JSONObject json_personal,json_validate, json_response_obj, json_profile, json_personalObj,login_jsonobj;
    RelativeLayout ccode_layout;
    String selected_cc_value, upload_response, contentAsString, attach_qid, upload_response_status, selectedfilename, selectedPath, serverResponseMessage, upLoadServerUri, last_upload_file, local_url, dob_val, gender_val, selected_cc_text;
    RadioButton radio1, radio2;
    StringBuilder total;
    ImageView profile_image;
    int serverResponseCode = 0;
    int IMAGE_PICKER_SELECT = 0;
    TextView tv_pending_text;
    ScrollView scroll_layout;

    public PersonalFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.personal_fragment, container, false);

        scroll_layout = rootView.findViewById(R.id.scroll_layout);
        tv_pending_text = rootView.findViewById(R.id.tv_pending_text);
        edtname = rootView.findViewById(R.id.edtname);
        edt_emailid = rootView.findViewById(R.id.edt_emailid);
        edt_dob = rootView.findViewById(R.id.edt_dob);
        edt_phoneno = rootView.findViewById(R.id.edt_phoneno);
        edt_panno = rootView.findViewById(R.id.edt_panno);
        tv_ccode = rootView.findViewById(R.id.tv_ccode);
        btn_submit = rootView.findViewById(R.id.btn_submit);
        ccode_layout = rootView.findViewById(R.id.ccode_layout);
        radio1 = rootView.findViewById(R.id.radio1);
        radio2 = rootView.findViewById(R.id.radio2);
        profile_image = rootView.findViewById(R.id.profile_image);
        tv_upload_photo = rootView.findViewById(R.id.tv_upload_photo);

        apply_personal();

        ccode_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog_ccode();
            }
        });

        tv_upload_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //attach_dialog();

                Intent i = new Intent(
                        Intent.ACTION_PICK,
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(i, IMAGE_PICKER_SELECT);

            }
        });

        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                try {

                    String email_val = edt_emailid.getText().toString();
                    String ccode_val = tv_ccode.getText().toString();
                    String phno = edt_phoneno.getText().toString();

                    json_validate = new JSONObject();
                    json_validate.put("email", email_val);
                    json_validate.put("ccode", ccode_val);
                    json_validate.put("mobile", phno);

                    System.out.println("json_validate----" + json_validate.toString());

                    new Async_validateEmailMobno().execute(json_validate);

                    //--------------------------------------------------
                } catch (Exception e) {
                    e.printStackTrace();
                }



            }
        });

        /*edt_dob.addTextChangedListener(new TextWatcher(){

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub
                edt_text =edt_dob.getText().toString();
                int o = 0;

                if ((edt_text.charAt(2) == '/') && (edt_text.charAt(4) == '/')) {
                    //Toast.makeText(getActivity(), "Format Is right", Toast.LENGTH_LONG).show();
                    edt_dob.setTextColor(Color.BLACK);
                } else {
                    edt_dob.setTextColor(Color.RED);
                    edt_dob.setError("Invalid date");
                }
                //ss = "";
            }
        });*/


        return rootView;
    }

    public void apply_personal() {

        try {

            System.out.println("Model.profile_str-------------" + Model.profile_str);

            json_profile = new JSONObject(Model.profile_str);
            String personal_str = json_profile.getString("personal");

            System.out.println("personal_str-----------" + personal_str);

            json_personal = new JSONObject(personal_str);

            System.out.println("json_personal-----------" + json_personal.toString());

            String name = json_personal.getString("name");
            gender_val = json_personal.getString("gender");
            dob_val = json_personal.getString("dob");

            System.out.println("dob_val---------" + dob_val);

            String[] separated = dob_val.split("-");
            String year_val = separated[0];
            String month_val = separated[1];
            String day_val = separated[2];

            String display_text = day_val + "-" + month_val + "-" + year_val;
            System.out.println("display_text--------" + display_text);

            String email = json_personal.getString("email");
            String ccode = json_personal.getString("ccode");
            String mobile = json_personal.getString("mobile");
            String img_url = json_personal.getString("docPhotoUrl");
            String hasUpdated_val = json_personal.getString("hasUpdated");

            //--------------------------------------------
            if (hasUpdated_val.equals("1")) {
                tv_pending_text.setVisibility(View.VISIBLE);
                scroll_layout.setVisibility(View.GONE);
            } else {
                tv_pending_text.setVisibility(View.GONE);
                scroll_layout.setVisibility(View.VISIBLE);
            }
            //--------------------------------------------

            edtname.setText(name);
            //------------------------------------------
            if (gender_val.equals("1")) {
                radio1.setChecked(true);
                radio2.setChecked(false);
            }
            if (gender_val.equals("2")) {
                radio1.setChecked(false);
                radio2.setChecked(true);
            }
            //------------------------------------------
            edt_dob.setText(display_text);
            tv_ccode.setText(ccode);
            //------------------------------------------
            if (email != null && !email.isEmpty() && !email.equals("null") && !email.equals("")) {
                edt_emailid.setText(email);
            } else {
                edt_emailid.setText("");
            }
            if (mobile != null && !mobile.isEmpty() && !mobile.equals("null") && !mobile.equals("")) {
                edt_phoneno.setText(mobile);
            } else {
                edt_phoneno.setText("");
            }
            //------------------------------------------

            Picasso.with(getActivity()).load(img_url).placeholder(R.drawable.progress_animation).error(R.mipmap.logo).into(profile_image);


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void dialog_ccode() {

        try {
            AlertDialog.Builder builderSingle = new AlertDialog.Builder(getActivity());
            //builderSingle.setIcon(R.mipmap);
            builderSingle.setTitle("Select Country code");

            final ArrayAdapter<String> categories = new ArrayAdapter<String>(getActivity(), R.layout.dialog_list_textview);

            cc_map = new HashMap<String, String>();

            categories.add("United States (+1), Canada (+1)");
            cc_map.put("United States (+1), Canada (+1)", "1");
            categories.add("United Kingdom (+44)");
            cc_map.put("United Kingdom (+44)", "44");
            categories.add("India (+91)");
            cc_map.put("India (+91)", "91");
            categories.add("Russian Federation (+7), Kazakhstan (+7)");
            cc_map.put("Russian Federation (+7), Kazakhstan (+7)", "7");
            categories.add("Egypt (+20)");
            cc_map.put("Egypt (+20)", "20");
            categories.add("South Africa (+27)");
            cc_map.put("South Africa (+27)", "27");
            categories.add("Greece (+30)");
            cc_map.put("Greece (+30)", "30");
            categories.add("Netherlands (+31)");
            cc_map.put("Netherlands (+31)", "31");
            categories.add("Belgium (+32)");
            cc_map.put("Belgium (+32)", "32");
            categories.add("France (+33)");
            cc_map.put("France (+33)", "33");
            categories.add("Spain (+34)");
            cc_map.put("Spain (+34)", "34");
            categories.add("Hungary (+36)");
            cc_map.put("Hungary (+36)", "36");
            categories.add("Italy (+39)");
            cc_map.put("Italy (+39)", "39");
            categories.add("Romania (+40)");
            cc_map.put("Romania (+40)", "40");
            categories.add("Switzerland (+41)");
            cc_map.put("Switzerland (+41)", "41");
            categories.add("Austria (+43)");
            cc_map.put("Austria (+43)", "43");
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
            categories.add("Mexico (+52)");
            cc_map.put("Mexico (+52)", "52");
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
            categories.add("Malaysia (+60)");
            cc_map.put("Malaysia (+60)", "60");
            categories.add("Australia (+61)");
            cc_map.put("Australia (+61)", "61");
            categories.add("Indonesia (+62)");
            cc_map.put("Indonesia (+62)", "62");
            categories.add("Philippines (+63)");
            cc_map.put("Philippines (+63)", "63");
            categories.add("New Zealand (+64)");
            cc_map.put("New Zealand (+64)", "64");
            categories.add("Singapore (+65)");
            cc_map.put("Singapore (+65)", "65");
            categories.add("Thailand (+66)");
            cc_map.put("Thailand (+66)", "66");
            categories.add("Japan (+81)");
            cc_map.put("Japan (+81)", "81");
            categories.add("Korea, Republic of (+82)");
            cc_map.put("Korea, Republic of (+82)", "82");
            categories.add("Viet Nam (+84)");
            cc_map.put("Viet Nam (+84)", "84");
            categories.add("China (+86)");
            cc_map.put("China (+86)", "86");
            categories.add("Turkey (+90)");
            cc_map.put("Turkey (+90)", "90");
            categories.add("Pakistan (+92)");
            cc_map.put("Pakistan (+92)", "92");
            categories.add("Afghanistan (+93)");
            cc_map.put("Afghanistan (+93)", "93");
            categories.add("Sri Lanka (+94)");
            cc_map.put("Sri Lanka (+94)", "94");
            categories.add("Myanmar (+95)");
            cc_map.put("Myanmar (+95)", "95");
            categories.add("Iran, Islamic Republic of (+98)");
            cc_map.put("Iran, Islamic Republic of (+98)", "98");
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
            categories.add("Mauritania (+222)");
            cc_map.put("Mauritania (+222)", "222");
            categories.add("Mali (+223)");
            cc_map.put("Mali (+223)", "223");
            categories.add("Guinea (+224)");
            cc_map.put("Guinea (+224)", "224");
            categories.add("Cote d'Ivoire (+225)");
            cc_map.put("Cote d'Ivoire (+225)", "225");
            categories.add("Burkina Faso (+226)");
            cc_map.put("Burkina Faso (+226)", "226");
            categories.add("Niger (+227)");
            cc_map.put("Niger (+227)", "227");
            categories.add("Togo (+228)");
            cc_map.put("Togo (+228)", "228");
            categories.add("Benin (+229)");
            cc_map.put("Benin (+229)", "229");
            categories.add("Mauritius (+230)");
            cc_map.put("Mauritius (+230)", "230");
            categories.add("Liberia (+231)");
            cc_map.put("Liberia (+231)", "231");
            categories.add("Sierra Leone (+232)");
            cc_map.put("Sierra Leone (+232)", "232");
            categories.add("Ghana (+233)");
            cc_map.put("Ghana (+233)", "233");
            categories.add("Nigeria (+234)");
            cc_map.put("Nigeria (+234)", "234");
            categories.add("Chad (+235)");
            cc_map.put("Chad (+235)", "235");
            categories.add("Central African Republic (+236)");
            cc_map.put("Central African Republic (+236)", "236");
            categories.add("Cameroon (+237)");
            cc_map.put("Cameroon (+237)", "237");
            categories.add("Cape Verde (+238)");
            cc_map.put("Cape Verde (+238)", "238");
            categories.add("Sao Tome and Principe (+239)");
            cc_map.put("Sao Tome and Principe (+239)", "239");
            categories.add("Equatorial Guinea (+240)");
            cc_map.put("Equatorial Guinea (+240)", "240");
            categories.add("Gabon (+241)");
            cc_map.put("Gabon (+241)", "241");
            categories.add("Congo (+242)");
            cc_map.put("Congo (+242)", "242");
            categories.add("Democratic Republic of the Congo (+243)");
            cc_map.put("Democratic Republic of the Congo (+243)", "243");
            categories.add("Angola (+244)");
            cc_map.put("Angola (+244)", "244");
            categories.add("GuineaBissau (+245)");
            cc_map.put("GuineaBissau (+245)", "245");
            categories.add("British Indian Ocean Territory (+246)");
            cc_map.put("British Indian Ocean Territory (+246)", "246");
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
            cc_map.put("Djibouti (+253)", "253");
            categories.add("Kenya (+254)");
            cc_map.put("Kenya (+254)", "254");
            categories.add("United Republic of Tanzania (+255)");
            cc_map.put("United Republic of Tanzania (+255)", "255");
            categories.add("Uganda (+256)");
            cc_map.put("Uganda (+256)", "256");
            categories.add("Burundi (+257)");
            cc_map.put("Burundi (+257)", "257");
            categories.add("Mozambique (+258)");
            cc_map.put("Mozambique (+258)", "258");
            categories.add("Zambia (+260)");
            cc_map.put("Zambia (+260)", "260");
            categories.add("Madagascar (+261)");
            cc_map.put("Madagascar (+261)", "261");
            categories.add("Mayotte (+262)");
            cc_map.put("Mayotte (+262)", "262");
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
            categories.add("Comoros (+269)");
            cc_map.put("Comoros (+269)", "269");
            categories.add("Saint Helena (+290)");
            cc_map.put("Saint Helena (+290)", "290");
            categories.add("Eritrea (+291)");
            cc_map.put("Eritrea (+291)", "291");
            categories.add("Aruba (+297)");
            cc_map.put("Aruba (+297)", "297");
            categories.add("Faroe Islands (+298)");
            cc_map.put("Faroe Islands (+298)", "298");
            categories.add("Greenland (+299)");
            cc_map.put("Greenland (+299)", "299");
            categories.add("Gibraltar (+350)");
            cc_map.put("Gibraltar (+350)", "350");
            categories.add("Portugal (+351)");
            cc_map.put("Portugal (+351)", "351");
            categories.add("Luxembourg (+352)");
            cc_map.put("Luxembourg (+352)", "352");
            categories.add("Ireland (+353)");
            cc_map.put("Ireland (+353)", "353");
            categories.add("Iceland (+354)");
            cc_map.put("Iceland (+354)", "354");
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
            categories.add("Lithuania (+370)");
            cc_map.put("Lithuania (+370)", "370");
            categories.add("Latvia (+371)");
            cc_map.put("Latvia (+371)", "371");
            categories.add("Estonia (+372)");
            cc_map.put("Estonia (+372)", "372");
            categories.add("Moldova, Republic of (+373)");
            cc_map.put("Moldova, Republic of (+373)", "373");
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
            categories.add("Holy See (Vatican City State) (+379)");
            cc_map.put("Holy See (Vatican City State) (+379)", "379");
            categories.add("Ukraine (+380)");
            cc_map.put("Ukraine (+380)", "380");
            categories.add("Serbia (+381)");
            cc_map.put("Serbia (+381)", "381");
            categories.add("Montenegro (+382)");
            cc_map.put("Montenegro (+382)", "382");
            categories.add("Croatia (+385)");
            cc_map.put("Croatia (+385)", "385");
            categories.add("Slovenia (+386)");
            cc_map.put("Slovenia (+386)", "386");
            categories.add("Bosnia and Herzegovina (+387)");
            cc_map.put("Bosnia and Herzegovina (+387)", "387");
            categories.add("Macedonia, the Former Yugoslav Republic of (+389)");
            cc_map.put("Macedonia, the Former Yugoslav Republic of (+389)", "389");
            categories.add("Czech Republic (+420)");
            cc_map.put("Czech Republic (+420)", "420");
            categories.add("Slovakia (+421)");
            cc_map.put("Slovakia (+421)", "421");
            categories.add("Liechtenstein (+423)");
            cc_map.put("Liechtenstein (+423)", "423");
            categories.add("Falkland Islands (Malvinas) (+500)");
            cc_map.put("Falkland Islands (Malvinas) (+500)", "500");
            categories.add("Belize (+501)");
            cc_map.put("Belize (+501)", "501");
            categories.add("Guatemala (+502)");
            cc_map.put("Guatemala (+502)", "502");
            categories.add("El Salvador (+503)");
            cc_map.put("El Salvador (+503)", "503");
            categories.add("Honduras (+504)");
            cc_map.put("Honduras (+504)", "504");
            categories.add("Nicaragua (+505)");
            cc_map.put("Nicaragua (+505)", "505");
            categories.add("Costa Rica (+506)");
            cc_map.put("Costa Rica (+506)", "506");
            categories.add("Panama (+507)");
            cc_map.put("Panama (+507)", "507");
            categories.add("Saint Pierre and Miquelon (+508)");
            cc_map.put("Saint Pierre and Miquelon (+508)", "508");
            categories.add("Haiti (+509)");
            cc_map.put("Haiti (+509)", "509");
            categories.add("Saint Barthelemy (+590)");
            cc_map.put("Saint Barthelemy (+590)", "590");
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
            categories.add("Martinique (+596)");
            cc_map.put("Martinique (+596)", "596");
            categories.add("Suriname (+597)");
            cc_map.put("Suriname (+597)", "597");
            categories.add("Uruguay (+598)");
            cc_map.put("Uruguay (+598)", "598");
            categories.add("Bonaire (+599), Curacao (+599)");
            cc_map.put("Bonaire (+599), Curacao (+599)", "599");
            categories.add("TimorLeste (+670)");
            cc_map.put("TimorLeste (+670)", "670");
            categories.add("Antarctica (+672)");
            cc_map.put("Antarctica (+672)", "672");
            categories.add("Brunei Darussalam (+673)");
            cc_map.put("Brunei Darussalam (+673)", "673");
            categories.add("Nauru (+674)");
            cc_map.put("Nauru (+674)", "674");
            categories.add("Papua New Guinea (+675)");
            cc_map.put("Papua New Guinea (+675)", "675");
            categories.add("Tonga (+676)");
            cc_map.put("Tonga (+676)", "676");
            categories.add("Solomon Islands (+677)");
            cc_map.put("Solomon Islands (+677)", "677");
            categories.add("Vanuatu (+678)");
            cc_map.put("Vanuatu (+678)", "678");
            categories.add("Fiji (+679)");
            cc_map.put("Fiji (+679)", "679");
            categories.add("Palau (+680)");
            cc_map.put("Palau (+680)", "680");
            categories.add("Wallis and Futuna (+681)");
            cc_map.put("Wallis and Futuna (+681)", "681");
            categories.add("Cook Islands (+682)");
            cc_map.put("Cook Islands (+682)", "682");
            categories.add("Niue (+683)");
            cc_map.put("Niue (+683)", "683");
            categories.add("Samoa (+685)");
            cc_map.put("Samoa (+685)", "685");
            categories.add("Kiribati (+686)");
            cc_map.put("Kiribati (+686)", "686");
            categories.add("New Caledonia (+687)");
            cc_map.put("New Caledonia (+687)", "687");
            categories.add("Tuvalu (+688)");
            cc_map.put("Tuvalu (+688)", "688");
            categories.add("French Polynesia (+689)");
            cc_map.put("French Polynesia (+689)", "689");
            categories.add("Tokelau (+690)");
            cc_map.put("Tokelau (+690)", "690");
            categories.add("Micronesia, Federated States of (+691)");
            cc_map.put("Micronesia, Federated States of (+691)", "691");
            categories.add("Marshall Islands (+692)");
            cc_map.put("Marshall Islands (+692)", "692");
            categories.add("Korea, Democratic People's Republic of (+850)");
            cc_map.put("Korea, Democratic People's Republic of (+850)", "850");
            categories.add("Hong Kong (+852)");
            cc_map.put("Hong Kong (+852)", "852");
            categories.add("Macao (+853)");
            cc_map.put("Macao (+853)", "853");
            categories.add("Cambodia (+855)");
            cc_map.put("Cambodia (+855)", "855");
            categories.add("Lao People's Democratic Republic (+856)");
            cc_map.put("Lao People's Democratic Republic (+856)", "856");
            categories.add("Pitcairn (+870)");
            cc_map.put("Pitcairn (+870)", "870");
            categories.add("Bangladesh (+880)");
            cc_map.put("Bangladesh (+880)", "880");
            categories.add("Taiwan, Province of China (+886)");
            cc_map.put("Taiwan, Province of China (+886)", "886");
            categories.add("Maldives (+960)");
            cc_map.put("Maldives (+960)", "960");
            categories.add("Lebanon (+961)");
            cc_map.put("Lebanon (+961)", "961");
            categories.add("Jordan (+962)");
            cc_map.put("Jordan (+962)", "962");
            categories.add("Syrian Arab Republic (+963)");
            cc_map.put("Syrian Arab Republic (+963)", "963");
            categories.add("Iraq (+964)");
            cc_map.put("Iraq (+964)", "964");
            categories.add("Kuwait (+965)");
            cc_map.put("Kuwait (+965)", "965");
            categories.add("Saudi Arabia (+966)");
            cc_map.put("Saudi Arabia (+966)", "966");
            categories.add("Yemen (+967)");
            cc_map.put("Yemen (+967)", "967");
            categories.add("Oman (+968)");
            cc_map.put("Oman (+968)", "968");
            categories.add("Palestine, State of (+970)");
            cc_map.put("Palestine, State of (+970)", "970");
            categories.add("United Arab Emirates (+971)");
            cc_map.put("United Arab Emirates (+971)", "971");
            categories.add("Israel (+972)");
            cc_map.put("Israel (+972)", "972");
            categories.add("Bahrain (+973)");
            cc_map.put("Bahrain (+973)", "973");
            categories.add("Qatar (+974)");
            cc_map.put("Qatar (+974)", "974");
            categories.add("Bhutan (+975)");
            cc_map.put("Bhutan (+975)", "975");
            categories.add("Mongolia (+976)");
            cc_map.put("Mongolia (+976)", "976");
            categories.add("Nepal (+977)");
            cc_map.put("Nepal (+977)", "977");
            categories.add("Tajikistan (+992)");
            cc_map.put("Tajikistan (+992)", "992");
            categories.add("Turkmenistan (+993)");
            cc_map.put("Turkmenistan (+993)", "993");
            categories.add("Azerbaijan (+994)");
            cc_map.put("Azerbaijan (+994)", "994");
            categories.add("Georgia (+995)");
            cc_map.put("Georgia (+995)", "995");
            categories.add("Kyrgyzstan (+996)");
            cc_map.put("Kyrgyzstan (+996)", "996");
            categories.add("Uzbekistan (+998)");
            cc_map.put("Uzbekistan (+998)", "998");
            categories.add("Bahamas (+1242)");
            cc_map.put("Bahamas (+1242)", "1242");
            categories.add("Barbados (+1246)");
            cc_map.put("Barbados (+1246)", "1246");
            categories.add("Anguilla (+1264)");
            cc_map.put("Anguilla (+1264)", "1264");
            categories.add("Antigua and Barbuda (+1268)");
            cc_map.put("Antigua and Barbuda (+1268)", "1268");
            categories.add("British Virgin Islands (+1284)");
            cc_map.put("British Virgin Islands (+1284)", "1284");
            categories.add("US Virgin Islands (+1340)");
            cc_map.put("US Virgin Islands (+1340)", "1340");
            categories.add("Cayman Islands (+1345)");
            cc_map.put("Cayman Islands (+1345)", "1345");
            categories.add("Bermuda (+1441)");
            cc_map.put("Bermuda (+1441)", "1441");
            categories.add("Grenada (+1473)");
            cc_map.put("Grenada (+1473)", "1473");
            categories.add("Turks and Caicos Islands (+1649)");
            cc_map.put("Turks and Caicos Islands (+1649)", "1649");
            categories.add("Montserrat (+1664)");
            cc_map.put("Montserrat (+1664)", "1664");
            categories.add("Northern Mariana Islands (+1670)");
            cc_map.put("Northern Mariana Islands (+1670)", "1670");
            categories.add("Guam (+1671)");
            cc_map.put("Guam (+1671)", "1671");
            categories.add("American Samoa (+1684)");
            cc_map.put("American Samoa (+1684)", "1684");
            categories.add("Sint Maarten (Dutch part) (+1721)");
            cc_map.put("Sint Maarten (Dutch part) (+1721)", "1721");
            categories.add("Saint Lucia (+1758)");
            cc_map.put("Saint Lucia (+1758)", "1758");
            categories.add("Dominica (+1767)");
            cc_map.put("Dominica (+1767)", "1767");
            categories.add("Saint Vincent and the Grenadines (+1784)");
            cc_map.put("Saint Vincent and the Grenadines (+1784)", "1784");
            categories.add("Dominican Republic (+1809)");
            cc_map.put("Dominican Republic (+1809)", "1809");
            categories.add("Trinidad and Tobago (+1868)");
            cc_map.put("Trinidad and Tobago (+1868)", "1868");
            categories.add("Saint Kitts and Nevis (+1869)");
            cc_map.put("Saint Kitts and Nevis (+1869)", "1869");
            categories.add("Jamaica (+1876)");
            cc_map.put("Jamaica (+1876)", "1876");

            builderSingle.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });

            builderSingle.setAdapter(categories, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                    String select_text = categories.getItem(which);
                    String select_value = (cc_map).get(categories.getItem(which));

                    System.out.println("select_text---" + select_text);
                    System.out.println("select_value---" + select_value);

                    selected_cc_value = select_value;
                    selected_cc_text = select_text;

                    tv_ccode.setText("+" + selected_cc_value);
                }
            });
            builderSingle.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private class Asyc_PersonData extends AsyncTask<JSONObject, Void, Boolean> {

        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            try {
                dialog = new ProgressDialog(getActivity());
                dialog.setMessage("Please wait..");
                dialog.show();
                dialog.setCancelable(false);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        protected Boolean doInBackground(JSONObject... urls) {
            try {
                JSONParser jParser = new JSONParser();
                json_response_obj = jParser.JSON_POST(urls[0], "stage1");

                System.out.println("Params URL---------------" + urls[0]);
                System.out.println("json_response_obj-----------" + json_response_obj.toString());

                return true;
            } catch (Exception e) {
                e.printStackTrace();
            }

            return false;
        }

        protected void onPostExecute(Boolean result) {

            try {

                String status_val = json_response_obj.getString("status");
                System.out.println("status_val---------" + status_val);

                if (status_val.equals("1")) {
                    say_success();

                    tv_pending_text.setVisibility(View.VISIBLE);
                    scroll_layout.setVisibility(View.GONE);

                    //------------ Google firebase Analitics--------------------
                    Model.mFirebaseAnalytics = FirebaseAnalytics.getInstance(getActivity());
                    Bundle params = new Bundle();
                    params.putString("Details", json_response_obj.toString());
                    Model.mFirebaseAnalytics.logEvent("Profile_Personal_Success", params);
                    //------------ Google firebase Analitics--------------------

                } else {
                    String err_val = json_response_obj.getString("err");
                    Toast.makeText(getActivity(), err_val, Toast.LENGTH_SHORT).show();
                }


                dialog.cancel();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void say_success() {

        final MaterialDialog alert = new MaterialDialog(getActivity());
        //alert.setTitle("Thankyou.!");
        alert.setMessage("Personal informations saved successfully..!");
        alert.setCanceledOnTouchOutside(false);
        alert.setPositiveButton("Ok", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alert.dismiss();
            }
        });
        alert.show();

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
/*

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        EasyImage.handleActivityResult(requestCode, resultCode, data, getActivity(), new DefaultCallback() {
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
                    File photoFile = EasyImage.lastlyTakenButCanceledPhoto(getActivity());
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

*/
/*            //------------ Google firebase Analitics--------------------
            Model.mFirebaseAnalytics = FirebaseAnalytics.getInstance(getApplicationContext());
            Bundle params = new Bundle();
            params.putString("User", Model.id);
            params.putString("Qid", attach_qid);
            params.putString("attach_file_path", selectedPath);
            params.putString("attach_filename", selectedfilename);
            Model.mFirebaseAnalytics.logEvent("Attach_Files", params);
            //------------ Google firebase Analitics--------------------*//*


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
    public void onDestroy() {
        EasyImage.clearConfiguration(getActivity());
        super.onDestroy();
    }
*/


    private class AsyncTask_fileupload extends AsyncTask<String, Void, Boolean> {

        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            try {
                dialog = new ProgressDialog(getActivity());
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
                System.out.println("upload_response_status ----------" + upload_response_status);

                if (upload_response_status.equals("1")) {

                    System.out.println("Upload Success-----------------------");

                } else {
                    String err_val = jObj.getString("err");
                    System.out.println("err_val ----------" + err_val);
                    Toast.makeText(getActivity(), err_val, Toast.LENGTH_SHORT).show();
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

            dialog.cancel();
        }
    }


    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == IMAGE_PICKER_SELECT && resultCode == Activity.RESULT_OK) {

            String path = getPathFromCameraData(data, this.getActivity());

            Log.i("PICTURE", "Path: " + path);

            if (path != null) {

                System.out.println("path--------------------------- " + path);

                File imgFile = new File(path);

                if (imgFile.exists()) {
                    Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                    //ImageView myImage = (ImageView) findViewById(R.id.profile_image);
                    profile_image.setImageBitmap(myBitmap);
                }

                selectedPath = path;

                new AsyncTask_fileupload().execute(path);

            }
        }
    }

    public static String getPathFromCameraData(Intent data, Context context) {
        Uri selectedImage = data.getData();
        String[] filePathColumn = {MediaStore.Images.Media.DATA};
        Cursor cursor = context.getContentResolver().query(selectedImage,
                filePathColumn, null, null, null);
        cursor.moveToFirst();
        int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
        String picturePath = cursor.getString(columnIndex);
        cursor.close();
        return picturePath;
    }


    class Async_validateEmailMobno extends AsyncTask<JSONObject, Void, Boolean> {

        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(getActivity());
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

                String isValid_val = login_jsonobj.getString("isValid");

                System.out.println("isValid_val ---" + isValid_val);

                if (isValid_val.equals("true")) {

                    try {

                        String uname = edtname.getText().toString();
                        String dob_val = edt_dob.getText().toString();
                        String email_val = edt_emailid.getText().toString();
                        String ccode_val = tv_ccode.getText().toString();
                        String phno = edt_phoneno.getText().toString();
                        String panno = edt_panno.getText().toString();

                        String[] separated = dob_val.split("-");
                        String DD_val = separated[0];
                        String month_val = separated[1];
                        String yyyy_val = separated[2];

                        String post_date = yyyy_val + "-" + month_val + "-" + DD_val;


                        if (radio1.isChecked()) {
                            gender_val = "1";
                        }

                        if (radio2.isChecked()) {
                            gender_val = "2";
                        }


                        json_personalObj = new JSONObject();
                        json_personalObj.put("name", uname);
                        json_personalObj.put("gender", gender_val);
                        json_personalObj.put("dob", post_date);
                        json_personalObj.put("mobile", phno);
                        json_personalObj.put("ccode", ccode_val);
                        json_personalObj.put("email", email_val);
                        json_personalObj.put("photo", selectedPath);

                        System.out.println("json_personalObj---" + json_personalObj.toString());

                        new Asyc_PersonData().execute(json_personalObj);

                    } catch (Exception e2) {
                        e2.printStackTrace();
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
                       /* final MaterialDialog alert = new MaterialDialog(getActivity());
                        alert.setTitle("Alert");
                        alert.setMessage("This mobile number or Email id is already exists");
                        alert.setCanceledOnTouchOutside(false);
                        alert.setPositiveButton("Ok", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                signup();
                            }
                        });
                        alert.show();*/

                        Toast.makeText(getActivity(), err_val, Toast.LENGTH_SHORT).show();
                    }
                }

                getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

                dialog.cancel();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    private String upload_file(String file_path) {
        last_upload_file=file_path;
        String ServerUploadPath = Model.BASE_URL + "mobileajax/uploadDocPhoto?user_id=" + Model.id + "&token=" + Model.token;

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
