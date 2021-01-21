package com.orane.docassist;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.flurry.android.FlurryAgent;

import com.orane.docassist.Model.Model;
import com.orane.docassist.Network.JSONParser;
import com.orane.docassist.R;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.wdullaer.materialdatetimepicker.time.RadialPickerLayout;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class SendApptSlot1 extends AppCompatActivity implements
        View.OnClickListener,
        TimePickerDialog.OnTimeSetListener,
        DatePickerDialog.OnDateSetListener {


    TextView tv_tit1, tv_tith2, tv_bot1;
    Button btn_promo_submit, btn_date, btn_time, btn_entry_submit;
    LinearLayout schedule_layout, promo_layout;
    Spinner spinner_timezone, spinner_clinics, spinner_curr;
    MaterialEditText edt_notes, edt_fee;
    RadioGroup rad_consgroup;
    Calendar calander;

    Map<String, String> curr_map = new HashMap<String, String>();
    Map<String, String> timezone_map = new HashMap<String, String>();
    Map<String, String> clinics_map = new HashMap<String, String>();


    public String str_response, cons_type, cl_id, cl_title, timezone, hourString, minuteString, secondString, date_string, date_string_api, time_string, time_string_api, ampm_string, curr_name, curr_code, cons_select_date;

    RadioButton rad_video_cons, rad_direct;
    JSONObject jsonobj, tz_jsonobj, jsonobj1c_cl, post_jsonobj;

    ArrayAdapter<String> dataAdapter;
    List<String> categories = new ArrayList<String>();
    List<String> clinic_categories = new ArrayList<String>();

    ArrayAdapter<String> myclinics_dataAdapter;

    JSONArray clinics_json_array;
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
    public static final String first_query = "first_query_key";
    public static final String first_hotline = "first_hotline_key";
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sendapptslot1);

        //------------ Object Creations -------------------------------
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setTitle("");

            TextView mTitle = (TextView) toolbar.findViewById(R.id.toolbar_title);
            Typeface khandBold = Typeface.createFromAsset(getApplicationContext().getAssets(), Model.font_name_bold);
            mTitle.setTypeface(khandBold);
        }
        //------------ Object Creations -------------------------------

        //================ Shared Pref ======================
        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.app_color));
        }

        rad_video_cons = (RadioButton) findViewById(R.id.rad_video_cons);
        rad_direct = (RadioButton) findViewById(R.id.rad_direct);

        rad_consgroup = (RadioGroup) findViewById(R.id.rad_consgroup);
        schedule_layout = (LinearLayout) findViewById(R.id.schedule_layout);
        promo_layout = (LinearLayout) findViewById(R.id.promo_layout);
        edt_notes = (MaterialEditText) findViewById(R.id.edt_notes);
        edt_fee = (MaterialEditText) findViewById(R.id.edt_fee);
        spinner_curr = (Spinner) findViewById(R.id.spinner_curr);
        spinner_timezone = (Spinner) findViewById(R.id.spinner_timezone);
        spinner_clinics = (Spinner) findViewById(R.id.spinner_clinics);
        btn_time = (Button) findViewById(R.id.btn_time);
        btn_date = (Button) findViewById(R.id.btn_date);
        btn_promo_submit = (Button) findViewById(R.id.btn_promo_submit);
        btn_entry_submit = (Button) findViewById(R.id.btn_entry_submit);
        tv_tit1 = (TextView) findViewById(R.id.tv_tit1);
        tv_tith2 = (TextView) findViewById(R.id.tv_tith2);
        tv_bot1 = (TextView) findViewById(R.id.tv_bot1);

        Typeface font_reg = Typeface.createFromAsset(getAssets(), Model.font_name);
        Typeface font_bold = Typeface.createFromAsset(getAssets(), Model.font_name_bold);

        ((TextView) findViewById(R.id.tv_appt_header)).setTypeface(font_bold);
        ((TextView) findViewById(R.id.tv_date_lab)).setTypeface(font_reg);
        ((TextView) findViewById(R.id.btn_date)).setTypeface(font_bold);
        ((TextView) findViewById(R.id.tv_timezone)).setTypeface(font_reg);
        ((TextView) findViewById(R.id.tv_time)).setTypeface(font_reg);
        ((TextView) findViewById(R.id.edt_notes)).setTypeface(font_bold);
        ((TextView) findViewById(R.id.tv_sidehead2)).setTypeface(font_bold);
        ((TextView) findViewById(R.id.edt_fee)).setTypeface(font_bold);

        rad_video_cons.setTypeface(font_reg);
        rad_direct.setTypeface(font_reg);
        btn_entry_submit.setTypeface(font_bold);
        btn_time.setTypeface(font_bold);

        cons_type = "0";

        //--------- text init -------------------------------------------
        tv_tit1.setText(Html.fromHtml("Now it is extremely easy to <b>get paid</b> online and send <b>video and direct</b> slots to patients."));
        tv_tith2.setText(Html.fromHtml("29%"));
        tv_tith2.setPaintFlags(tv_tith2.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        tv_bot1.setText(Html.fromHtml("You can send both <b>free and paid</b> slots. Try Now!"));
        //--------- text init -------------------------------------------

        if (Model.id != null && !Model.id.isEmpty() && !Model.id.equals("null") && !Model.id.equals("")) {

            //-------------Time Zone-------------------------------------------
            String url = Model.BASE_URL + "/sapp/myTzList?os_type=android";
            System.out.println("TimeZone URL_--------------" + url);
            new JSON_TimeZone().execute(url);
            //-------------Time Zone-------------------------------------------

            //-------------spinner_clinics-------------------------------------------
            String myclinic_url = Model.BASE_URL + "sapp/myClinics?os_type=android&user_id=" + (Model.id) + "&token=" + Model.token;
            System.out.println("myclinic_url----------" + myclinic_url);
            new JSON_clinics().execute(myclinic_url);
            //-------------spinner_clinics-------------------------------------------

        } else {
            Toast.makeText(SendApptSlot1.this, "Something went wrong. Please logout and try again", Toast.LENGTH_SHORT).show();
        }

        //---------------- Today Instances-------------------------------------------
        //String date = new SimpleDateFormat("dd/MM/yyyy").format(new Date());

        Date today = new Date();
        SimpleDateFormat formattedDate = new SimpleDateFormat("dd/MM/yyyy");
        Calendar c = Calendar.getInstance();
        c.add(Calendar.DATE, 1);  // number of days to add
        String tomorrow = formattedDate.format(c.getTime());
        System.out.println("Tomorrows date is " + tomorrow);
        btn_date.setText(tomorrow);

        SimpleDateFormat formattedDate2 = new SimpleDateFormat("yyyy/MM/dd");
        Calendar c2 = Calendar.getInstance();
        c2.add(Calendar.DATE, 1);  // number of days to add
        String tomorrow2 = formattedDate2.format(c2.getTime());
        date_string_api = tomorrow2;

        try {
            calander = Calendar.getInstance();
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm:a");
            String time = simpleDateFormat.format(calander.getTime());
            System.out.println("Current Time Val------>" + time);
            time_string_api = time;
            btn_time.setText(time);

        } catch (Exception e) {
            System.out.println("Exception Time---" + e.toString());
            e.printStackTrace();
        }
        //---------------- Time Instances-------------------------------------------


        btn_entry_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    String notes = edt_notes.getText().toString();
                    String fee_txt = edt_fee.getText().toString();

                    //----- getting Clinic name ----------------------------
                    String clinic_name = spinner_clinics.getSelectedItem().toString();
                    Model.clinic_id = clinics_map.get(clinic_name);

                    System.out.println("clinic_name---------" + clinic_name);
                    System.out.println("clinic_id---------" + Model.clinic_id);
                    //----- getting Clinic name ----------------------------

                    //----- getting time Zone----------------------------
                    String spintz = spinner_timezone.getSelectedItem().toString();
                    Model.cons_timezone_val = timezone_map.get(spintz);

                    System.out.println("spintz---------" + spintz);
                    System.out.println("cons_timezone_val---------" + Model.cons_timezone_val);
                    //----- getting time Zone----------------------------


                    if (date_string_api != null && !date_string_api.isEmpty() && !date_string_api.equals("null") && !date_string_api.equals("")) {
                        if ((Model.cons_timezone_val) != null && !(Model.cons_timezone_val).isEmpty() && !(Model.cons_timezone_val).equals("null") && !(Model.cons_timezone_val).equals("")) {
                            if (time_string_api != null && !time_string_api.isEmpty() && !time_string_api.equals("null") && !time_string_api.equals("")) {
                                if (cons_type != null && !cons_type.isEmpty() && !cons_type.equals("null") && !cons_type.equals("")) {
                                    if ((Model.clinic_id) != null && !(Model.clinic_id).isEmpty() && !(Model.clinic_id).equals("null") && !(Model.clinic_id).equals("")) {
                                        if (curr_code != null && !curr_code.isEmpty() && !curr_code.equals("null") && !curr_code.equals("")) {
                                            if (!fee_txt.isEmpty() && !fee_txt.equals("null") && !fee_txt.equals("")) {


                                                Intent intent = new Intent(SendApptSlot1.this, SendApptSlot2.class);
                                                intent.putExtra("date", date_string_api);
                                                intent.putExtra("timezone_val", (Model.cons_timezone_val));
                                                intent.putExtra("time", time_string_api);
                                                intent.putExtra("notes", notes);
                                                intent.putExtra("cons_type", cons_type);
                                                intent.putExtra("clinic_id", Model.clinic_id);
                                                intent.putExtra("curr_code", curr_code);
                                                intent.putExtra("fees", fee_txt);


                                                //----------- Flurry -------------------------------------------------
                                                Map<String, String> articleParams = new HashMap<String, String>();
                                                articleParams.put("android.doc.Slot_Date", date_string_api);
                                                articleParams.put("android.doc.TimeZone", Model.cons_timezone_val);
                                                articleParams.put("android.doc.notes", notes);
                                                articleParams.put("android.doc.cons_type", cons_type);
                                                articleParams.put("android.doc.clinic_id", Model.clinic_id);
                                                articleParams.put("android.doc.curr_code", curr_code);
                                                articleParams.put("android.doc.fee_txt", fee_txt);
                                                FlurryAgent.logEvent("android.doc.Send_Appt_Slot1", articleParams);
                                                //----------- Flurry -------------------------------------------------


                                                intent.putExtra("finisher", new android.os.ResultReceiver(null) {
                                                    @Override
                                                    protected void onReceiveResult(int resultCode, Bundle resultData) {
                                                        SendApptSlot1.this.finish();
                                                    }
                                                });
                                                startActivityForResult(intent, 1);


                                            } else edt_fee.setError("Please enter the fees");
                                        } else
                                            Toast.makeText(getApplicationContext(), "Please Select Currency", Toast.LENGTH_LONG).show();
                                    } else
                                        Toast.makeText(getApplicationContext(), "Please select your Clinic", Toast.LENGTH_LONG).show();
                                } else
                                    Toast.makeText(getApplicationContext(), "Please select Consultation type", Toast.LENGTH_LONG).show();
                            } else
                                Toast.makeText(getApplicationContext(), "Please select Time for consultation", Toast.LENGTH_LONG).show();
                        } else
                            Toast.makeText(getApplicationContext(), "Please select your Timezone", Toast.LENGTH_LONG).show();
                    } else
                        Toast.makeText(getApplicationContext(), "Please select Date for consultation", Toast.LENGTH_LONG).show();


                    System.out.println("date_string_api---" + date_string_api);
                    System.out.println("Model.cons_timezone_val---" + Model.cons_timezone_val);
                    System.out.println("time_string_api---" + time_string_api);
                    System.out.println("notes---" + notes);
                    System.out.println("cons_type---" + cons_type);
                    System.out.println("Model.clinic_id---" + Model.clinic_id);
                    System.out.println("curr_code---" + curr_code);
                    System.out.println("fee_txt---" + fee_txt);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });


        rad_video_cons.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (rad_video_cons.isChecked()) {

                    System.out.println("Yes 1 Select----");
                    spinner_clinics.setVisibility(View.GONE);
                    cons_type = "0";
                }

            }
        });

        rad_direct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (rad_direct.isChecked()) {

                    System.out.println("Yes 2 Select----");
                    spinner_clinics.setVisibility(View.VISIBLE);
                    cons_type = "2";
                }
            }
        });


        btn_promo_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                promo_layout.setVisibility(View.GONE);
                schedule_layout.setVisibility(View.VISIBLE);
            }
        });


        btn_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                try {
                    Calendar now = Calendar.getInstance();
                    DatePickerDialog dpd = DatePickerDialog.newInstance(
                            SendApptSlot1.this,
                            now.get(Calendar.YEAR),
                            now.get(Calendar.MONTH),
                            now.get(Calendar.DAY_OF_MONTH)
                    );
                    dpd.setThemeDark(false);
                    dpd.vibrate(true);
                    dpd.dismissOnPause(true);
                    dpd.showYearPickerFirst(false);
                    dpd.setAccentColor(Color.parseColor("#9C27B0"));
                    dpd.setTitle("DatePicker Title");
                    //---------------------------------------
                    Calendar[] dates = new Calendar[300];
                    for (int i = 1; i <= 300; i++) {
                        Calendar date = Calendar.getInstance();
                        date.add(Calendar.DATE, i);
                        dates[i - 1] = date;
                    }
                    dpd.setSelectableDays(dates);
                    //---------------------------------------
                    //---------------------------------------
                    Calendar[] dates2 = new Calendar[13];
                    for (int i = -6; i <= 6; i++) {
                        Calendar date = Calendar.getInstance();
                        date.add(Calendar.WEEK_OF_YEAR, i);
                        dates2[i + 6] = date;
                    }
                    dpd.setHighlightedDays(dates2);
                    //---------------------------------------
                    dpd.show(getFragmentManager(), "Datepickerdialog");

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });


        btn_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar now = Calendar.getInstance();
                TimePickerDialog tpd = TimePickerDialog.newInstance(
                        SendApptSlot1.this,
                        now.get(Calendar.HOUR_OF_DAY),
                        now.get(Calendar.MINUTE),
                        false
                );
                tpd.setThemeDark(false);
                tpd.vibrate(true);
                tpd.dismissOnPause(true);
                tpd.enableSeconds(false);
                //tpd.enableMinutes(false);
                tpd.setAccentColor(Color.parseColor("#9C27B0"));
                tpd.setTitle("Choose time");
                //tpd.setTimeInterval(2, 5, 10);
                tpd.setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialogInterface) {
                        Log.d("TimePicker", "Dialog was cancelled");
                    }
                });
                tpd.show(getFragmentManager(), "Timepickerdialog");
            }
        });


        String timezone_str = "";

        //------------------------------------------------------------------------------------
        spinner_timezone.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> arg0, View view, int arg2, long arg3) {

                String spintz = spinner_timezone.getSelectedItem().toString();
                System.out.println("spintz---------" + spintz);
                Model.cons_timezone_val = timezone_map.get(spintz);

                if (spintz.equals("Choose another Timezone")) {
                    Intent intent = new Intent(SendApptSlot1.this, TimeZoneActivity.class);
                    startActivity(intent);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {

            }
        });
        //------------------------------------------------------------------------------------

        //----------------------clinics--------------------------------------------------------------
        spinner_clinics.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> arg0, View view, int arg2, long arg3) {

                String clinic_name = spinner_clinics.getSelectedItem().toString();
                System.out.println("spintz---------" + clinic_name);
                Model.clinic_id = clinics_map.get(clinic_name);

                if (clinic_name.equals("Add New Clinic")) {

                    Model.launch = "SendApptSlot1";
                    Intent intent = new Intent(SendApptSlot1.this, MyClinicAddActivity.class);
                    intent.putExtra("clinic_id", "0");
                    intent.putExtra("clinic_name", "");
                    intent.putExtra("clinic_geo", "");
                    intent.putExtra("mode", "new");
                    startActivity(intent);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {

            }
        });
        //------------------clinics------------------------------------------------------------------

        //------------------clinics------------------------------------------------------------------
        spinner_curr.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                curr_name = spinner_curr.getSelectedItem().toString();
                curr_code = curr_map.get(curr_name);

                System.out.println("curr_name------" + curr_name);
                System.out.println("curr_code------" + curr_code);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        //------------------clinics------------------------------------------------------------------


        //------- Setting Currency----------------------
        final List<String> currency_categories = new ArrayList<String>();

        currency_categories.add("Rs. (INR)");
        curr_map.put("Rs. (INR)", "1");

        currency_categories.add("$ (USD)");
        curr_map.put("$ (USD)", "2");

        currency_categories.add("€ (EUR)");
        curr_map.put("€ (EUR)", "3");

        currency_categories.add("£ (GBP)");
        curr_map.put("£ (GBP)", "4");

        currency_categories.add("AU $ (AUD)");
        curr_map.put("AU $ (AUD)", "5");

        currency_categories.add("PHP (Philippines)");
        curr_map.put("PHP (Philippines)", "6");

        currency_categories.add("₪ (ILS)");
        curr_map.put("₪ (ILS)", "7");

        ArrayAdapter<String> lang_dataAdapter = new ArrayAdapter<String>(SendApptSlot1.this, android.R.layout.simple_spinner_item, currency_categories);
        lang_dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_curr.setAdapter(lang_dataAdapter);
        //----------------Currency-----------------------------


    }

    @Override
    public void onClick(View view) {

/*        if (enableSeconds.isChecked() && view.getId() == R.id.enable_seconds) enableMinutes.setChecked(true);
        if (!enableMinutes.isChecked() && view.getId() == R.id.enable_minutes) enableSeconds.setChecked(false);*/

    }

    @Override
    public void onResume() {
        super.onResume();

        try {
            TimePickerDialog tpd = (TimePickerDialog) getFragmentManager().findFragmentByTag("Timepickerdialog");
            DatePickerDialog dpd = (DatePickerDialog) getFragmentManager().findFragmentByTag("Datepickerdialog");

            if (tpd != null) tpd.setOnTimeSetListener(this);
            if (dpd != null) dpd.setOnDateSetListener(this);


            System.out.println("Model.cons_timezone_name---" + Model.cons_timezone_name);

            if ((Model.cons_timezone_val) != null && !(Model.cons_timezone_val).isEmpty() && !(Model.cons_timezone_val).equals("null") && !(Model.cons_timezone_val).equals("")) {
                if ((Model.cons_timezone_name) != null && !(Model.cons_timezone_name).isEmpty() && !(Model.cons_timezone_name).equals("null") && !(Model.cons_timezone_name).equals("")) {

                    //======================================================================
                    categories.add("Select another timezone");
                    timezone_map.put("Select another timezone", "0");
                    //=========================================================

                    //spinner_timezone.setAdapter(null);
                    categories.clear();

                    //======================================================================
                    categories.add(Model.cons_timezone_name);
                    timezone_map.put(Model.cons_timezone_name, Model.cons_timezone_val);
                    //=========================================================
                    //======================================================================
                    categories.add("Select another timezone");
                    timezone_map.put("Select another timezone", "0");
                    //=========================================================

                    dataAdapter = new ArrayAdapter<String>(SendApptSlot1.this, android.R.layout.simple_spinner_item, categories);
                    dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner_timezone.setAdapter(dataAdapter);
                    //dataAdapter.notifyDataSetChanged();
                }
            }


            if ((Model.clinic_id) != null && !(Model.clinic_id).isEmpty() && !(Model.clinic_id).equals("null") && !(Model.clinic_id).equals("")) {
                if ((Model.clinic_location) != null && !(Model.clinic_location).isEmpty() && !(Model.clinic_location).equals("null") && !(Model.clinic_location).equals("")) {
/*
                //======================================================================
                clinic_categories.add("Add New Clinic");
                timezone_map.put("Add New Clinic", "0");
                //=========================================================

                clinic_categories.clear();*/

                    System.out.println("Resume Model.clinic_location ----------------- " + Model.clinic_location);
                    System.out.println("Resume Model.clinic_id ----------------- " + Model.clinic_id);

                    //======================================================================
                    clinic_categories.add(Model.clinic_location);
                    timezone_map.put(Model.clinic_location, Model.clinic_id);
                    //=========================================================

                    //======================================================================
                    clinic_categories.add("Add New Clinic");
                    timezone_map.put("Add New Clinic", "0");
                    //=========================================================

                    myclinics_dataAdapter = new ArrayAdapter<String>(SendApptSlot1.this, android.R.layout.simple_spinner_item, clinic_categories);
                    //myclinics_dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    myclinics_dataAdapter.addAll(clinic_categories);
                    myclinics_dataAdapter.notifyDataSetChanged();

                }
            }


        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onTimeSet(RadialPickerLayout view, int hourOfDay, int minute, int second) {

        hourString = hourOfDay < 10 ? "0" + hourOfDay : "" + hourOfDay;
        minuteString = minute < 10 ? "0" + minute : "" + minute;
        secondString = second < 10 ? "0" + second : "" + second;

        if (hourOfDay >= 12) {
            ampm_string = "PM";
        } else {
            ampm_string = "AM";
        }

        if (hourOfDay > 12) {
            hourString = "" + (hourOfDay - 12);
        }

        time_string = hourString + ":" + minuteString + " " + ampm_string;
        time_string_api = hourString + ":" + minuteString + ":" + ampm_string;

        final String time = hourString + ":" + minuteString;

        try {
            final SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
            final Date dateObj = sdf.parse(time);
            System.out.println("dateObj---->" + dateObj);
            System.out.println("----->" + new SimpleDateFormat("HH:mm a").format(dateObj));
        } catch (final ParseException e) {
            e.printStackTrace();
        }

        System.out.println("time_string-----" + time_string);
        System.out.println("time_string_api-----" + time_string_api);


        btn_time.setText(time_string);

    }

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {

        String monthOfYear_txt = monthOfYear < 10 ? "0" + monthOfYear : "" + monthOfYear;
        String dayOfMonth_txt = dayOfMonth < 10 ? "0" + dayOfMonth : "" + dayOfMonth;

        date_string = dayOfMonth_txt + "/" + monthOfYear_txt + "/" + year;
        date_string_api = year + "-" + monthOfYear_txt + "-" + dayOfMonth_txt;

        System.out.println("date_string-----" + date_string);
        System.out.println("date_string_api-----" + date_string_api);

        btn_date.setText(date_string);
    }


    private class JSON_TimeZone extends AsyncTask<String, Void, Boolean> {

        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            dialog = new ProgressDialog(SendApptSlot1.this);
            dialog.setMessage("Please wait..");
            dialog.show();
            dialog.setCancelable(false);
        }

        @Override
        protected Boolean doInBackground(String... urls) {

            try {

                JSONParser jsonParser1 = new JSONParser();
                tz_jsonobj = jsonParser1.getJSON_URL(urls[0]);

                System.out.println("Time_Range jsonobj-----" + tz_jsonobj.toString());

                return true;

            } catch (Exception e) {
                e.printStackTrace();
            }

            return false;
        }

        protected void onPostExecute(Boolean result) {


            try {

                //======================================================================
                categories.add("Select another timezone");
                timezone_map.put("Select another timezone", "0");
                //=========================================================

                categories.clear();

                Iterator keys = tz_jsonobj.keys();

                while (keys.hasNext()) {
                    // loop to get the dynamic key
                    String currentDynamicKey = (String) keys.next();
                    System.out.println("currentDynamicKey----------->" + currentDynamicKey);

                    timezone = tz_jsonobj.getString(currentDynamicKey);
                    System.out.println("currentDynamicValue----------->" + timezone);


                    //======================================================================
                    categories.add(timezone);
                    timezone_map.put(timezone, currentDynamicKey);
                    //=========================================================
                }

                //======================================================================
                categories.add("Select another timezone");
                timezone_map.put("Select another timezone", "0");
                //=========================================================

                dataAdapter = new ArrayAdapter<String>(SendApptSlot1.this, android.R.layout.simple_spinner_item, categories);
                dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner_timezone.setAdapter(dataAdapter);

            } catch (Exception e) {
                e.printStackTrace();
            }

            dialog.cancel();

        }
    }


    private class JSON_clinics extends AsyncTask<String, Void, Boolean> {

        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            dialog = new ProgressDialog(SendApptSlot1.this);
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
                            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    }

                } else if (json instanceof JSONArray) {
                    System.out.println("This is JSON ARRAY---------------" + str_response);


                    clinics_json_array = new JSONArray(str_response);
                    clinic_categories.clear();
                    //======================================================================
                    clinic_categories.add("Select clinic");
                    clinics_map.put("Select clinic", "0");
                    //=========================================================

                    for (int i = 0; i < clinics_json_array.length(); i++) {
                        jsonobj1c_cl = clinics_json_array.getJSONObject(i);
                        System.out.println("jsonobj1c_cl-----" + jsonobj1c_cl.toString());

                        cl_id = jsonobj1c_cl.getString("id");
                        cl_title = jsonobj1c_cl.getString("location");

                        System.out.println("cl_title-----" + cl_title);

                        //======================================================================
                        clinic_categories.add(cl_title);
                        clinics_map.put(cl_title, cl_id);
                        //=========================================================
                    }

                    //======================================================================
                    clinic_categories.add("Add New Clinic");
                    clinics_map.put("Add New Clinic", "0");
                    //=========================================================

                    myclinics_dataAdapter = new ArrayAdapter<String>(SendApptSlot1.this, android.R.layout.simple_spinner_item, clinic_categories);
                    myclinics_dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner_clinics.setAdapter(myclinics_dataAdapter);
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
