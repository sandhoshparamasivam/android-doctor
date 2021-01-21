package com.orane.docassist;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.flurry.android.FlurryAgent;

import com.google.firebase.analytics.FirebaseAnalytics;

import com.orane.docassist.Model.Model;
import com.orane.docassist.Network.JSONParser;
import com.orane.docassist.R;
import com.rengwuxian.materialedittext.MaterialEditText;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MyClinicAddActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    AutoCompleteTextView autoCompView;

    private static final String LOG_TAG = "SignupPlace";
    private static final String PLACES_API_BASE = "https://maps.googleapis.com/maps/api/place";
    private static final String TYPE_AUTOCOMPLETE = "/autocomplete";
    private static final String OUT_JSON = "/json";
    private static final String API_KEY = "AIzaSyCNuv_IjcLxDa8j_L3xFtnO3gAmglhdC1I"; //Browser key

    Button btn_submit;
    public String clinic_mode, status, cname, sstreet, clinic_street, edt_city, place_id_txt, clinic_id, clinic_name, clinic_geo;

    EditText edt_cname, edt_zip, edt_street;
    Map<String, String> placeid_map = new HashMap<String, String>();
    JSONObject adclinic_json, addclinic_jsonobj;
    String type_val, clinic_zip_code;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.myclinic_add);

        //------------ Object Creations -------------------------------
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setTitle("Add a Clinic");
        }
        //------------ Object Creations -------------------------------

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.app_color));
        }
        edt_cname = (EditText) findViewById(R.id.edt_cname);
        edt_street = (EditText) findViewById(R.id.edt_street);
        edt_zip = (EditText) findViewById(R.id.edt_zip);
        btn_submit = (Button) findViewById(R.id.btn_submit);


        autoCompView = (AutoCompleteTextView) findViewById(R.id.autoCompleteTextView);
        autoCompView.setAdapter(new GooglePlacesAutocompleteAdapter(this, R.layout.google_place_item));
        autoCompView.setOnItemClickListener(this);


        try {
            Intent intent = getIntent();
            clinic_id = intent.getStringExtra("clinic_id");
            clinic_name = intent.getStringExtra("clinic_name");
            clinic_street = intent.getStringExtra("clinic_street");
            clinic_geo = intent.getStringExtra("clinic_geo");
            clinic_mode = intent.getStringExtra("mode");
            clinic_zip_code = intent.getStringExtra("zip_code");

            if (intent.hasExtra("type")) {
                type_val = intent.getStringExtra("type");
            } else {
                type_val = "";
            }


            System.out.println("Get Intent clinic_id----" + clinic_id);
            System.out.println("Get Intent clinic_name----" + clinic_name);
            System.out.println("Get Intent clinic_street----" + clinic_street);
            System.out.println("Get Intent clinic_geo----" + clinic_geo);
            System.out.println("Get Intent type_val----" + type_val);

            edt_cname.setText(clinic_name);
            edt_street.setText(clinic_street);

            //---------------------------------------------
            if (clinic_zip_code != null && !clinic_zip_code.isEmpty() && !clinic_zip_code.equals("null") && !clinic_zip_code.equals("")) {
                edt_zip.setText(clinic_zip_code);
            } else {
                edt_zip.setText("");
            }
            //---------------------------------------------


            autoCompView.setText(clinic_geo);


            if (clinic_mode.equals("new")) {
                btn_submit.setText("Add Clinic");

                if (getSupportActionBar() != null) {
                    getSupportActionBar().setTitle("Add a Clinic");
                }
            } else {
                btn_submit.setText("Update");
                if (getSupportActionBar() != null) {
                    getSupportActionBar().setTitle("Update Clinic Address");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                cname = edt_cname.getText().toString();
                sstreet = edt_street.getText().toString();
                String edt_zip_val = edt_zip.getText().toString();

                if (isInternetOn()) {

                    try {
                        if (cname.equals("")) {
                            edt_cname.setError("Please enter your clinic name");
                            edt_cname.requestFocus();
                        } else if (sstreet.equals("")) {
                            edt_street.setError("Please enter the street");
                            edt_street.requestFocus();
                        } else if (edt_zip_val.equals("")) {
                            edt_zip.setError("Please enter the Zip code");
                            edt_zip.requestFocus();
                        } else {

                            adclinic_json = new JSONObject();
                            adclinic_json.put("user_id", (Model.id));
                            adclinic_json.put("clinic_id", clinic_id);
                            adclinic_json.put("clinic_name", cname);
                            adclinic_json.put("street", sstreet);
                            adclinic_json.put("place_id", place_id_txt);
                            adclinic_json.put("zip", edt_zip_val);

                            Model.cliniq_id = clinic_id;
                            Model.clinic_title = cname;
                            Model.clinic_street = sstreet;
                            Model.clinic_placeid = place_id_txt;


                            if (type_val.equals("primary")) {
                                adclinic_json.put("type", "primary");
                            }

                            System.out.println("adclinic_json---" + adclinic_json.toString());

                            if (!type_val.equals("profile_update")) {
                                new JSON_AddClinic().execute(adclinic_json);
                            } else {
                                Model.query_launch = "cliniqadd";
                                finish();

                                System.out.println("This is Profile Update Call");
                            }

                            //------------ Google firebase Analitics--------------------
                            Model.mFirebaseAnalytics = FirebaseAnalytics.getInstance(getApplicationContext());
                            Bundle params = new Bundle();
                            params.putString("User", Model.id + "/" + Model.name);
                            params.putString("Details", adclinic_json.toString());
                            Model.mFirebaseAnalytics.logEvent("Add_New_Clinic", params);
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
                (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);

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

                System.out.println("place_id_txt------------" + place_id_txt);

                placeid_map.put(descp_txt, place_id_txt);

                resultList.add(predsJsonArray.getJSONObject(i).getString("description"));
            }
        } catch (JSONException e) {
            Log.e(LOG_TAG, "Cannot process JSON results", e);
        }

        return resultList;
    }

    private class GooglePlacesAutocompleteAdapter extends ArrayAdapter<String> implements Filterable {
        private ArrayList<String> resultList;

        public GooglePlacesAutocompleteAdapter(Context context, int textViewResourceId) {
            super(context, textViewResourceId);
        }

        @Override
        public int getCount() {
            return resultList.size();
        }

        @Override
        public String getItem(int index) {
            return resultList.get(index);
        }

        @Override
        public Filter getFilter() {
            Filter filter = new Filter() {
                @Override
                protected FilterResults performFiltering(CharSequence constraint) {
                    FilterResults filterResults = new FilterResults();
                    if (constraint != null) {
                        resultList = autocomplete(constraint.toString());
                        filterResults.values = resultList;
                        filterResults.count = resultList.size();
                    }
                    return filterResults;
                }

                @Override
                protected void publishResults(CharSequence constraint, FilterResults results) {
                    if (results != null && results.count > 0) {
                        notifyDataSetChanged();
                    } else {
                        notifyDataSetInvalidated();
                    }
                }
            };
            return filter;
        }
    }

    private class JSON_AddClinic extends AsyncTask<JSONObject, Void, Boolean> {

        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            dialog = new ProgressDialog(MyClinicAddActivity.this);
            dialog.setMessage("Please wait");
            dialog.show();
            dialog.setCancelable(false);
        }

        @Override
        protected Boolean doInBackground(JSONObject... urls) {

            try {

                JSONParser jParser = new JSONParser();
                addclinic_jsonobj = jParser.JSON_POST(urls[0], "addClinic");

                System.out.println("addclinic_jsonobj---------" + addclinic_jsonobj.toString());
                System.out.println("urls[0]----------" + urls[0]);

                return true;

            } catch (Exception e) {
                e.printStackTrace();
            }

            return false;
        }

        protected void onPostExecute(Boolean result) {

            try {
                if (addclinic_jsonobj.has("status")) {
                    status = addclinic_jsonobj.getString("status");
                }
                if (addclinic_jsonobj.has("id")) {
                    Model.clinic_id = addclinic_jsonobj.getString("id");
                }
                if (addclinic_jsonobj.has("location")) {
                    Model.clinic_location = addclinic_jsonobj.getString("location");
                }

                System.out.println("status----------" + status);
                System.out.println("Model.clinic_id----------" + Model.clinic_id);
                System.out.println("Model.clinic_location----------" + Model.clinic_location);

                if (status.equals("1")) {

                    if (clinic_id.equals("0")) {

                        //----------- Flurry -------------------------------------------------
                        Map<String, String> articleParams = new HashMap<String, String>();
                        articleParams.put("android.doc.clinic_id", clinic_id);
                        articleParams.put("android.doc.clinic_name", cname);
                        articleParams.put("android.doc.clinic_street", sstreet);
                        articleParams.put("android.doc.clinic_geo", place_id_txt);
                        FlurryAgent.logEvent("android.doc.Add_New_Clinic", articleParams);
                        //----------- Flurry -------------------------------------------------

                        Toast.makeText(getApplicationContext(), "Your clinic has been successfully added", Toast.LENGTH_LONG).show();

                    } else {

                        //----------- Flurry -------------------------------------------------
                        Map<String, String> articleParams = new HashMap<String, String>();
                        articleParams.put("android.doc.clinic_id", clinic_id);
                        articleParams.put("android.doc.clinic_name", cname);
                        articleParams.put("android.doc.clinic_street", sstreet);
                        articleParams.put("android.doc.clinic_geo", place_id_txt);
                        FlurryAgent.logEvent("android.doc.Update_Clinic", articleParams);
                        //----------- Flurry -------------------------------------------------

                        Toast.makeText(getApplicationContext(), "Your clinic has been successfully updated", Toast.LENGTH_LONG).show();
                    }

                    Model.query_launch = "cliniqadd";

                    finish();

                } else {
                    Toast.makeText(getApplicationContext(), "Your requested update failed. Please try again..!", Toast.LENGTH_LONG).show();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            dialog.cancel();
        }
    }
}
