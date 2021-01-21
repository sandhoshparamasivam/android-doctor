package com.orane.docassist;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.orane.docassist.Model.Model;
import com.orane.docassist.Network.JSONParser;
import com.orane.docassist.New_Main.New_MainActivity;

import org.json.JSONObject;


public class SplashActivity extends AppCompatActivity {

    public String url, str_response;

    SharedPreferences sharedpreferences;
    public static final String MyPREFERENCES = "MyPrefs";
    public static final String Login_Status = "Login_Status_key";
    public static final String user_name = "user_name_key";
    public static final String password = "password_key";
    public static final String Name = "Name_key";
    public static final String bcountry = "bcountry_key";
    public static final String photo_url = "photo_url";
    public static final String token = "token_key";
    public static final String id = "id";
    public static final String update_count = "update_count_key";
    public static final String update_count_start = "update_count_start_key";


    AlertDialog alertDialog;
    public String country_url, update_count_start_text, currentVersion, user_id_val, uname, country_code_no, country, pass, Log_Status;
    JSONObject jsonobj, jsonobj_ad;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);

        //================ Shared Pref ======================
        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        Model.token = sharedpreferences.getString(token, "");
        user_id_val = sharedpreferences.getString(id, "");
        Model.id = sharedpreferences.getString(id, "");

        update_count_start_text = sharedpreferences.getString(update_count_start, "false");

        try {
            Model.update_count = Integer.parseInt((sharedpreferences.getString(update_count, "0")));
        } catch (Exception e) {
            e.printStackTrace();
        }

        System.out.println("Model.token----------------------" + Model.token);
        System.out.println("Model.id----------------------" + Model.id);
        System.out.println("Model.update_count---------------------" + Model.update_count);
        //================ Shared Pref ======================================================

        if (update_count_start_text != null && !update_count_start_text.isEmpty() && !update_count_start_text.equals("null") && !update_count_start_text.equals("")) {
            if (update_count_start_text.equals("true")) {
                Model.update_count = Model.update_count + 1;
            } else {
                Model.update_count = 0;
            }
        } else {
            Model.update_count = 0;
        }


        Model.home_ad_flag = "true";

        try {

            if (user_id_val != null && !user_id_val.isEmpty() && !user_id_val.equals("null") && !user_id_val.equals("")) {
                country_url = Model.BASE_URL + "sapp/country?os_type=android&track=true&user_id=" + user_id_val + "&token=" + Model.token;
            } else {
                country_url = Model.BASE_URL + "sapp/country?token=" + Model.token + "&os_type=android&track=true&user_id=";
            }

            //---------------------------------------------------------
            System.out.println("country_url-------------" + country_url);
            new JSON_getCountry().execute(country_url);
            //---------------------------------------------------------

        } catch (Exception e) {
            e.printStackTrace();
        }


        full_process();


    }


    public void process_code() {

        try {
            if (Log_Status.equals("1")) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        Intent i = new Intent(SplashActivity.this, New_MainActivity.class);
                        startActivity(i);
                        finish();

                    }

                }, 3000);
            } else {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Intent i = new Intent(SplashActivity.this, LoginActivity.class);
                        startActivity(i);
                        finish();
                    }
                }, 3000);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public final boolean isInternetOn() {

        ConnectivityManager connec = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);

        if (connec.getNetworkInfo(0).getState() == NetworkInfo.State.CONNECTED ||
                connec.getNetworkInfo(0).getState() == NetworkInfo.State.CONNECTING ||
                connec.getNetworkInfo(1).getState() == NetworkInfo.State.CONNECTING ||
                connec.getNetworkInfo(1).getState() == NetworkInfo.State.CONNECTED) {
            return true;

        } else if (
                connec.getNetworkInfo(0).getState() == NetworkInfo.State.DISCONNECTED ||
                        connec.getNetworkInfo(1).getState() == NetworkInfo.State.DISCONNECTED) {

            //Toast.makeText(this, " Not Connected ", Toast.LENGTH_LONG).show();
            return false;
        }
        return false;
    }

    public void full_process() {

        if (isInternetOn()) {

            Log_Status = sharedpreferences.getString(Login_Status, "");

            uname = sharedpreferences.getString(user_name, "");
            pass = sharedpreferences.getString(password, "");

            process_code();

        } else {

            Log_Status = sharedpreferences.getString(Login_Status, "");

            uname = sharedpreferences.getString(user_name, "");
            pass = sharedpreferences.getString(password, "");

            process_code();

            Toast.makeText(SplashActivity.this, "Please check your Internet Connection and try again", Toast.LENGTH_SHORT).show();
        }
    }

    public boolean isTablet(Context context) {
        boolean xlarge = ((context.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == 4);
        boolean large = ((context.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_LARGE);
        return (xlarge || large);
    }


    private class JSON_getCountry extends AsyncTask<String, Void, Boolean> {

        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

/*            dialog = new ProgressDialog(OTPActivity.this);
            dialog.setMessage("Please wait");
            dialog.show();
            dialog.setCancelable(false);*/
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

                jsonobj = new JSONObject(str_response);

                country = jsonobj.getString("country");
                country_code_no = jsonobj.getString("code");

                System.out.println("country----------" + country);
                System.out.println("country_code_no----------" + country_code_no);

                Model.browser_country = country;
                Model.country_code = country_code_no;

            } catch (Exception e) {
                e.printStackTrace();
            }
            //dialog.cancel();

        }
    }


   /* public void ask_update() {
        alertDialog = new AlertDialog.Builder(SplashActivity.this).create();
        alertDialog.setTitle("New update found..!");
        alertDialog.setMessage("New update is available. Please update application");
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Ask Later", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                System.out.println("Ask Latyer-------------------");

                update_count_start_text = "true";
                Model.update_count = Model.update_count + 1;

                //============================================================
                SharedPreferences.Editor editor = sharedpreferences.edit();
                editor.putString(update_count_start, "true");
                editor.putString(update_count, "" + Model.update_count);
                editor.apply();
                //============================================================

                full_process();


            }
        });

        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Cancel", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int id) {
                System.out.println("Cancel-------------------");

                full_process();
            }
        });

        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "Update", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int id) {
                System.out.println("Update the App-------------------");
                Model.update_count = 0;
                update_count_start_text = "false";

                //============================================================
                SharedPreferences.Editor editor = sharedpreferences.edit();
                editor.putString(update_count_start, "false");
                editor.putString(update_count, "0");
                editor.apply();
                //============================================================

            }
        });
    }*/

}
