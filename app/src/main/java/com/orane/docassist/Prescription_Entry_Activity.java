package com.orane.docassist;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.flurry.android.FlurryAgent;
import com.orane.docassist.Model.Item;
import com.orane.docassist.Model.Model;
import com.orane.docassist.Network.JSONParser;

import org.json.JSONArray;
import org.json.JSONObject;

import java.net.URLEncoder;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;


public class Prescription_Entry_Activity extends AppCompatActivity {

    public String dose_name, drug_qty, drugName, drug_days, when_to_take, drug_type_name, drug_name, dose_text, qty_val, how_to_take, days_val, add_type, drug_id, prescription_id, p_type_val, cur_qid, whentotake_name, whentotake_val, howtotake_name, howtotake_val, pat_id, drug_type_val, report_response;
    Button btn_submit;
    EditText edt_feedback;
    JSONObject json_feedback, json_response_obj;
    Intent intent;
    String timezone, tz_val, tz_string, cur_date;
    Spinner spinner_dtype, spinner_how, spinner_when;
    Button btn_entry_submit;
    JSONObject tz_jsonobj;
    ArrayList listArray;
    String str_drug_dets;
    EditText edt_drug_name, edt_drug_days, edt_drug_qty, edt_dosage;
    RelativeLayout drugtype_layout, howtotake_layout, when_layout;
    TextView tv_drug_type_name, tv_howtotake_name, tv_whentotake_name;

    Map<String, String> drug_type_map = new HashMap<String, String>();
    Map<String, String> howtotake_map = new HashMap<String, String>();
    Map<String, String> whentotake_map = new HashMap<String, String>();

    public static Object getKeyFromValue(Map hm, Object value) {
        for (Object o : hm.keySet()) {
            if (hm.get(o).equals(value)) {
                return o;
            }
        }
        return null;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.prescription_entry);

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

        spinner_dtype = findViewById(R.id.spinner_dtype);
        spinner_how = findViewById(R.id.spinner_how);
        spinner_when = findViewById(R.id.spinner_when);

        btn_entry_submit = findViewById(R.id.btn_entry_submit);
        tv_drug_type_name = findViewById(R.id.tv_drug_type_name);
        tv_howtotake_name = findViewById(R.id.tv_howtotake_name);

        edt_drug_name = findViewById(R.id.edt_drug_name);
        edt_dosage = findViewById(R.id.edt_dosage);
        edt_drug_days = findViewById(R.id.edt_drug_days);
        edt_drug_qty = findViewById(R.id.edt_drug_qty);
        drugtype_layout = findViewById(R.id.drugtype_layout);
        howtotake_layout = findViewById(R.id.howtotake_layout);
        when_layout = findViewById(R.id.when_layout);
        tv_whentotake_name = findViewById(R.id.tv_whentotake_name);

        apply_map();


        try {

            Intent intent = getIntent();
            add_type = intent.getStringExtra("add_type");
            pat_id = intent.getStringExtra("pat_id");
            cur_qid = intent.getStringExtra("cur_qid");
            p_type_val = intent.getStringExtra("p_type");
            prescription_id = intent.getStringExtra("prescription_id");
            drug_id = intent.getStringExtra("drug_id");
            //ptype_val = intent.getStringExtra("ptype");

/*
            //-------------------------------------------------------
            if (ptype_val.equals("view")) {
                btn_entry_submit.setVisibility(View.GONE);
            } else {
                btn_entry_submit.setVisibility(View.VISIBLE);
            }
            //-------------------------------------------------------
*/


            if (add_type.equals("update")) {

                //----------------------- Doctor Inbox -------------------------------
                String params = Model.BASE_URL + "sapp/previewPrescription?os_type=android&item_type=" + p_type_val + "&item_id=" + cur_qid + "&pre_type=drug_edit&drug_id=" + drug_id + "&user_id=" + (Model.id) + "&token=" + Model.token;
                System.out.println("params-----------" + params);
                new get_Drug_dets().execute(params);
                //----------------------- Doctor Inbox -------------------------------


            }

            System.out.println("add_type-----" + add_type);
            System.out.println("Get pat_id---" + pat_id);
            System.out.println("Get cur_qid---" + cur_qid);
            System.out.println("Get prescription_id---" + prescription_id);

        } catch (Exception e) {
            e.printStackTrace();
        }

        //-------------Time Zone-------------------------------------------
        TimeZone tz = TimeZone.getDefault();
        System.out.println("TimeZone----------" + tz.getID());

        tz_string = tz.getID();


        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("GMT"),
                Locale.getDefault());
        Date currentLocalTime = calendar.getTime();

        DateFormat date = new SimpleDateFormat("ZZZZZ", Locale.getDefault());
        String localTime = date.format(currentLocalTime);
        System.out.println("localTime------" + localTime);

        localTime = localTime.replace(":","");
        System.out.println("localTime------" + localTime);

        tz_val = localTime;

        Date c = Calendar.getInstance().getTime();
        System.out.println("Current time => " + c);

        //SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        String formattedDate = df.format(c);
        cur_date = formattedDate;
        System.out.println("cur_date--------" + cur_date);
        //-------------Time Zone-------------------------------------------

        //-------------Time Zone-------------------------------------------

        drugtype_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog_ccode();
            }
        });

        howtotake_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                howtotake_layout_ccode();
            }
        });

        when_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                whentotake_layout_ccode();
            }
        });


        //------------------clinics------------------------------------------------------------------
        spinner_dtype.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                drug_type_name = spinner_dtype.getSelectedItem().toString();
                drug_type_val = drug_type_map.get(drug_type_name);

                System.out.println("drug_type_name------" + drug_type_name);
                System.out.println("drug_type_val------" + drug_type_val);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        //------------------clinics------------------------------------------------------------------

        //------------------clinics------------------------------------------------------------------
        spinner_how.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                howtotake_name = spinner_how.getSelectedItem().toString();
                howtotake_val = howtotake_map.get(howtotake_name);

                System.out.println("howtotake_name------" + howtotake_name);
                System.out.println("howtotake_val------" + drug_type_val);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        //------------------clinics------------------------------------------------------------------

        //------------------clinics------------------------------------------------------------------
        spinner_when.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                whentotake_name = spinner_when.getSelectedItem().toString();
                whentotake_val = whentotake_map.get(whentotake_name);

                System.out.println("whentotake_name------" + whentotake_name);
                System.out.println("whentotake_val------" + whentotake_val);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        //------------------clinics------------------------------------------------------------------


        btn_entry_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String tv_drug_type_text = tv_drug_type_name.getText().toString();
                String tv_howtotake_text = tv_howtotake_name.getText().toString();
                String tv_whentotake_text = tv_whentotake_name.getText().toString();
                drug_name = edt_drug_name.getText().toString();
                dose_name = edt_dosage.getText().toString();
                drug_qty = edt_drug_qty.getText().toString();
                drug_days = edt_drug_days.getText().toString();

                whentotake_val = whentotake_map.get(tv_whentotake_text);
                howtotake_val = howtotake_map.get(tv_howtotake_text);
                drug_type_val = drug_type_map.get(tv_drug_type_text);

                System.out.println("tv_drug_type_text---" + tv_drug_type_text);
                System.out.println("drug_name---" + drug_name);
                System.out.println("dose_name---" + dose_name);
                System.out.println("drug_qty---" + drug_qty);
                System.out.println("drug_days---" + drug_days);

                System.out.println("drug_type_name---" + tv_drug_type_text);
                System.out.println("drug_type_val---" + drug_type_val);

                System.out.println("howtotake_name---" + tv_howtotake_text);
                System.out.println("howtotake_val---" + howtotake_val);

                System.out.println("whentotake_name---" + tv_whentotake_text);
                System.out.println("whentotake_val---" + whentotake_val);


                if (drug_type_val != null && !drug_type_val.isEmpty() && !drug_type_val.equals("null") && !drug_type_val.equals("")) {
                    if (drug_name != null && !drug_name.isEmpty() && !drug_name.equals("null") && !drug_name.equals("")) {
                        if (dose_name != null && !dose_name.isEmpty() && !dose_name.equals("null") && !dose_name.equals("")) {
                            if (drug_qty != null && !drug_qty.isEmpty() && !drug_qty.equals("null") && !drug_qty.equals("")) {
                                if (whentotake_val != null && !whentotake_val.isEmpty() && !whentotake_val.equals("null") && !whentotake_val.equals("")) {
                                    if (howtotake_val != null && !howtotake_val.isEmpty() && !howtotake_val.equals("null") && !howtotake_val.equals("")) {

                                        if (drug_days != null && !drug_days.isEmpty() && !drug_days.equals("null") && !drug_days.equals("")) {

                                            try {

                                                json_feedback = new JSONObject();
                                                json_feedback.put("item_id", cur_qid);
                                                json_feedback.put("item_type", p_type_val);
                                                json_feedback.put("prescrDrugId", drug_id);
                                                json_feedback.put("submit_type", "drugs");
                                                json_feedback.put("status", "");
                                                json_feedback.put("drug_type", drug_type_val);
                                                json_feedback.put("drug_name", drug_name);
                                                json_feedback.put("drug_dose", dose_name);
                                                json_feedback.put("quantity", drug_qty);
                                                json_feedback.put("for_days", drug_days);
                                                json_feedback.put("when_take", whentotake_val);
                                                json_feedback.put("how_take", howtotake_val);

                                                json_feedback.put("agreed_dt", cur_date);
                                                //tz_string = URLEncoder.encode(tz_string, "UTF-8");
                                                json_feedback.put("agreed_tz_str", tz_string);
                                                json_feedback.put("agreed_tz", tz_val);

                                                System.out.println("json_feedback---" + json_feedback.toString());

                                                //json_feedback = new JSONObject("{\"drug_name\": \"gggh\", \"quantity\": \"555\", \"submit_type\": \"drugs\", \"for_days\": \"5555\", \"patientId\": \"\", \"agreed_tz_str\": \"Asia/Kolkata\", \"status\": \"\", \"drug_type\": \"7\", \"agreed_dt\": \"2020-06-24 13:16:30\", \"when_take\": \"3\", \"prescrDrugId\": \"\", \"how_take\": \"5\", \"drug_dose\": \"ggg\", \"item_id\": \"9\", \"agreed_tz\": \"+0530\", \"item_type\": \"consult\"}");

                                                new JSON_Feedback().execute(json_feedback);

                                                //say_success();
                                                //-------------------------------------------

                                                //----------- Flurry -------------------------------------------------
                                                Map<String, String> articleParams = new HashMap<String, String>();
                                                articleParams.put("android.doc.entry:", json_feedback.toString());
                                                FlurryAgent.logEvent("android.doc.prescription_entry", articleParams);
                                                //----------- Flurry -------------------------------------------------

                                            } catch (Exception e2) {
                                                e2.printStackTrace();
                                            }
                                        } else {
                                            Toast.makeText(Prescription_Entry_Activity.this, "Please enter the Drug days", Toast.LENGTH_SHORT).show();
                                        }

                                    } else {
                                        Toast.makeText(Prescription_Entry_Activity.this, "Please select How to take", Toast.LENGTH_SHORT).show();
                                    }
                                } else {
                                    Toast.makeText(Prescription_Entry_Activity.this, "Please select When to take", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                Toast.makeText(Prescription_Entry_Activity.this, "Please enter the drug quantity", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(Prescription_Entry_Activity.this, "Please enter Dose name", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(Prescription_Entry_Activity.this, "Please enter the Dose name", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(Prescription_Entry_Activity.this, "Please select the Drug Type", Toast.LENGTH_SHORT).show();
                }


            }
        });
    }

    public void say_success() {

       /* try {

            new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)
                    .setTitleText("Success..!")
                    .setContentText("Prescription has been saved")
                    .setConfirmText("Ok")
                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sDialog) {
                            sDialog.dismiss();
                            finish();
                        }
                    })
                    .show();
        } catch (Exception e) {
            e.printStackTrace();
        }*/
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

    public void dialog_ccode() {

        AlertDialog.Builder builderSingle = new AlertDialog.Builder(Prescription_Entry_Activity.this);
        //builderSingle.setIcon(R.mipmap);
        builderSingle.setTitle("Please select Drug Type");

        final ArrayAdapter<String> currency_categories = new ArrayAdapter<String>(Prescription_Entry_Activity.this, R.layout.dialog_list_textview);

        currency_categories.add("Tablet");
        drug_type_map.put("Tablet", "1");

        currency_categories.add("Capsule");
        drug_type_map.put("Capsule", "2");

        currency_categories.add("Suspension");
        drug_type_map.put("Suspension", "3");

        currency_categories.add("Syrup");
        drug_type_map.put("Syrup", "4");

        currency_categories.add("Sachet");
        drug_type_map.put("Sachet", "9");

        currency_categories.add("Injection");
        drug_type_map.put("Injection", "5");

        currency_categories.add("Cream");
        drug_type_map.put("Cream", "6");

        currency_categories.add("Lotion");
        drug_type_map.put("Lotion", "7");

        currency_categories.add("Oinment");
        drug_type_map.put("Oinment", "8");

        currency_categories.add("Inhaler");
        drug_type_map.put("Inhaler", "10");

        currency_categories.add("Patch");
        drug_type_map.put("Patch", "11");

        currency_categories.add("Other");
        drug_type_map.put("Other", "12");

        builderSingle.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        builderSingle.setAdapter(currency_categories, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                String select_text = currency_categories.getItem(which);
                String select_value = (drug_type_map).get(currency_categories.getItem(which));

                System.out.println("select_text---" + select_text);
                System.out.println("select_value---" + select_value);

                drug_type_val = select_value;
                drug_type_name = select_text;

                tv_drug_type_name.setText(drug_type_name);
            }
        });
        builderSingle.show();
    }

    public void howtotake_layout_ccode() {

        AlertDialog.Builder builderSingle = new AlertDialog.Builder(Prescription_Entry_Activity.this);
        //builderSingle.setIcon(R.mipmap);
        builderSingle.setTitle("How to take");

        final ArrayAdapter<String> howtotake_map_categories = new ArrayAdapter<String>(Prescription_Entry_Activity.this, R.layout.dialog_list_textview);

        howtotake_map_categories.add("1-0-0");
        howtotake_map.put("1-0-0", "1");

        howtotake_map_categories.add("0-1-0");
        howtotake_map.put("0-1-0", "2");

        howtotake_map_categories.add("0-0-1");
        howtotake_map.put("0-0-1", "3");

        howtotake_map_categories.add("1-1-0");
        howtotake_map.put("1-1-0", "4");

        howtotake_map_categories.add("0-1-1");
        howtotake_map.put("0-1-1", "5");

        howtotake_map_categories.add("1-0-1");
        howtotake_map.put("1-0-1", "6");

        howtotake_map_categories.add("1-1-1");
        howtotake_map.put("1-1-1", "7");


        builderSingle.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        builderSingle.setAdapter(howtotake_map_categories, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                String select_text = howtotake_map_categories.getItem(which);
                String select_value = (howtotake_map).get(howtotake_map_categories.getItem(which));

                System.out.println("howtotake_val---" + select_value);
                System.out.println("howtotake_name---" + select_text);

                howtotake_val = select_value;
                howtotake_name = select_text;

                tv_howtotake_name.setText(howtotake_name);
            }
        });
        builderSingle.show();
    }

    public void whentotake_layout_ccode() {

        AlertDialog.Builder builderSingle = new AlertDialog.Builder(Prescription_Entry_Activity.this);
        //builderSingle.setIcon(R.mipmap);
        builderSingle.setTitle("When to take");

        final ArrayAdapter<String> whentotake_map_categories = new ArrayAdapter<String>(Prescription_Entry_Activity.this, R.layout.dialog_list_textview);

        whentotake_map_categories.add("After Food");
        whentotake_map.put("After Food", "1");

        whentotake_map_categories.add("Before Food");
        whentotake_map.put("Before Food", "2");

        whentotake_map_categories.add("On Empty Stomach");
        whentotake_map.put("On Empty Stomach", "3");


        builderSingle.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        builderSingle.setAdapter(whentotake_map_categories, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                String select_text = whentotake_map_categories.getItem(which);
                String select_value = (whentotake_map).get(whentotake_map_categories.getItem(which));

                System.out.println("whentotake_val---" + select_value);
                System.out.println("whentotake_name---" + select_text);

                whentotake_val = select_value;
                whentotake_name = select_text;

                tv_whentotake_name.setText(whentotake_name);
            }
        });
        builderSingle.show();
    }

    public void apply_map() {

        //------- Setting Drug Type----------------------
        final List<String> currency_categories = new ArrayList<String>();

        currency_categories.add("Tablet");
        drug_type_map.put("Tablet", "1");

        currency_categories.add("Capsule");
        drug_type_map.put("Capsule", "2");

        currency_categories.add("Suspension");
        drug_type_map.put("Suspension", "3");

        currency_categories.add("Syrup");
        drug_type_map.put("Syrup", "4");

        currency_categories.add("Sachet");
        drug_type_map.put("Sachet", "9");

        currency_categories.add("Injection");
        drug_type_map.put("Injection", "5");

        currency_categories.add("Cream");
        drug_type_map.put("Cream", "6");

        currency_categories.add("Lotion");
        drug_type_map.put("Lotion", "7");

        currency_categories.add("Oinment");
        drug_type_map.put("Oinment", "8");

        currency_categories.add("Inhaler");
        drug_type_map.put("Inhaler", "10");

        currency_categories.add("Patch");
        drug_type_map.put("Patch", "11");

        currency_categories.add("Other");
        drug_type_map.put("Other", "12");


        ArrayAdapter<String> lang_dataAdapter = new ArrayAdapter<String>(Prescription_Entry_Activity.this, android.R.layout.simple_spinner_item, currency_categories);
        lang_dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_dtype.setAdapter(lang_dataAdapter);
        //------- Setting Drug Type----------------------


        //------- Setting howtotake_map----------------------
        final List<String> howtotake_map_categories = new ArrayList<String>();

        howtotake_map_categories.add("1-0-0");
        howtotake_map.put("1-0-0", "1");

        howtotake_map_categories.add("0-1-0");
        howtotake_map.put("0-1-0", "2");

        howtotake_map_categories.add("0-0-1");
        howtotake_map.put("0-0-1", "3");

        howtotake_map_categories.add("1-1-0");
        howtotake_map.put("1-1-0", "4");

        howtotake_map_categories.add("0-1-1");
        howtotake_map.put("0-1-1", "5");

        howtotake_map_categories.add("1-0-1");
        howtotake_map.put("1-0-1", "6");

        howtotake_map_categories.add("1-1-1");
        howtotake_map.put("1-1-1", "7");


        ArrayAdapter<String> howtotake_dataAdapter = new ArrayAdapter<String>(Prescription_Entry_Activity.this, android.R.layout.simple_spinner_item, howtotake_map_categories);
        howtotake_dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_how.setAdapter(howtotake_dataAdapter);
        //------- Setting Drug Type----------------------


        //------- Setting When to Take---------------------
        final List<String> whentotake_map_categories = new ArrayList<String>();

        whentotake_map_categories.add("After Food");
        whentotake_map.put("After Food", "1");

        whentotake_map_categories.add("Before Food");
        whentotake_map.put("Before Food", "2");

        whentotake_map_categories.add("On Empty Stomach");
        whentotake_map.put("On Empty Stomach", "3");


        ArrayAdapter<String> whentotake_dataAdapter = new ArrayAdapter<String>(Prescription_Entry_Activity.this, android.R.layout.simple_spinner_item, whentotake_map_categories);
        whentotake_dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_when.setAdapter(whentotake_dataAdapter);
        //------- Setting When to Take----------------------

    }

    private class JSON_Feedback extends AsyncTask<JSONObject, Void, Boolean> {

        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            try {
                dialog = new ProgressDialog(Prescription_Entry_Activity.this);
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
                json_response_obj = jParser.JSON_POST(urls[0], "writeDrug");

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

                report_response = json_response_obj.getString("status");
                System.out.println("report_response--------------" + report_response);
                Model.query_launch = "writeDrug";

                if (report_response.equals("1")) {

                    String drugs_text = json_response_obj.getString("drugs");
                    Model.json_dugs_array = new JSONArray(drugs_text);

                    finish();


                } else {
                    String msg_text = json_response_obj.getString("msg");
                    Toast.makeText(getApplicationContext(), msg_text, Toast.LENGTH_LONG).show();
                    Model.json_dugs_array = null;
                }


                dialog.cancel();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private class get_Drug_dets extends AsyncTask<String, Void, Void> {

        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            dialog = new ProgressDialog(Prescription_Entry_Activity.this);
            dialog.setMessage("Please wait..");
            dialog.show();
            dialog.setCancelable(false);
        }

        @Override
        protected Void doInBackground(String... params) {
            try {
                str_drug_dets = new JSONParser().getJSONString(params[0]);
                System.out.println("str_drug_dets--------------" + str_drug_dets);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);

            try {

                if (str_drug_dets != null && !str_drug_dets.isEmpty() && !str_drug_dets.equals("null") && !str_drug_dets.equals("")) {

                    JSONObject jobj = new JSONObject(str_drug_dets);

                    String status_text = jobj.getString("status");

                    if (status_text.equals("0")) {
                        String msg_text = jobj.getString("msg");
                        Toast.makeText(Prescription_Entry_Activity.this, msg_text, Toast.LENGTH_SHORT).show();
                    } else {

                        String id = jobj.getString("id");
                        drugName = jobj.getString("drugName");
                        String drugType = jobj.getString("drugType");
                        dose_name = jobj.getString("drugDose");
                        drug_qty = jobj.getString("quantity");
                        drug_days = jobj.getString("forDays");
                        String whenToTake = jobj.getString("whenToTake");
                        String howToTake = jobj.getString("howToTake");
                        String prescrPatStatus = jobj.getString("prescrPatStatus");

                        String howToTake_text = "" + getKeyFromValue(howtotake_map, howToTake);
                        String whenToTake_text = "" + getKeyFromValue(whentotake_map, whenToTake);
                        drug_type_val = "" + getKeyFromValue(drug_type_map, drugType);

                        System.out.println("howToTake_text-------" + howToTake_text);
                        System.out.println("whenToTake_text-------" + whenToTake_text);
                        System.out.println("drugType_text-------" + drug_type_val);


                        tv_drug_type_name.setText(drug_type_val);
                        edt_drug_name.setText(drugName);
                        edt_dosage.setText(dose_name);
                        edt_drug_qty.setText(drug_qty);
                        tv_howtotake_name.setText(howToTake_text);
                        edt_drug_days.setText(drug_days);
                        tv_whentotake_name.setText(whenToTake_text);

                    }
                }

                dialog.dismiss();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


}

