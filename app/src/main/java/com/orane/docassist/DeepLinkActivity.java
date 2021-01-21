package com.orane.docassist;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;

import com.flurry.android.FlurryAgent;

import com.orane.docassist.Model.Model;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class DeepLinkActivity extends Activity {

    public JSONObject get_meetingid_jsonobj;

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

    public String full_url, idval, uname, docname, pass, Log_Status;


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        //================ Shared Pref ======================
        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        Model.id = sharedpreferences.getString(id, "");
        Log_Status = sharedpreferences.getString(Login_Status, "");
        uname = sharedpreferences.getString(user_name, "");
        docname = sharedpreferences.getString(Name, "");
        Model.name = sharedpreferences.getString(Name, "");
        pass = sharedpreferences.getString(password, "");
        Model.email = sharedpreferences.getString(email, "");
        Model.browser_country = sharedpreferences.getString(browser_country, "");
        Model.fee_q = sharedpreferences.getString(fee_q, "");
        Model.fee_consult = sharedpreferences.getString(fee_consult, "");
        Model.currency_label = sharedpreferences.getString(currency_label, "");
        Model.currency_symbol = sharedpreferences.getString(currency_symbol, "");
        Model.have_free_credit = sharedpreferences.getString(have_free_credit, "");
        Model.photo_url = sharedpreferences.getString(photo_url, "");
        Model.kmid = sharedpreferences.getString(sp_km_id, "");
        //============================================================

        try {

            Intent intent = getIntent();
            String action = intent.getAction();
            Uri data = intent.getData();

            System.out.println("uri---------------" + data.toString());

            full_url = "" + data;

            String sub_str_msg = "zlink";
            boolean title_check = full_url.toLowerCase().contains(sub_str_msg.toLowerCase());

            String pwd_str_msg = "resetpassword";
            boolean pwd_str_check = full_url.toLowerCase().contains(pwd_str_msg.toLowerCase());

            if (title_check) {

                String full_url = data.toString();
                System.out.println("full_url-----------------" + full_url);

                Uri uri = Uri.parse(full_url);
                idval = uri.getQueryParameter("id");
                System.out.println("idval------------" + idval);

                if (Log_Status.equals("1")) {

                    try {

                        if ((Model.id) != null && !(Model.id).isEmpty() && !(Model.id).equals("null") && !(Model.id).equals("")) {

                            //--------------------Kissmetrics--------------------------
                            //----------- Flurry -------------------------------------------------
                            Map<String, String> articleParams = new HashMap<String, String>();
                            articleParams.put("android.doc.Cons_id", idval);
                            FlurryAgent.logEvent("android.doc.answered_queries_list", articleParams);
                            //----------- Flurry -------------------------------------------------

                            intent = new Intent(this, UpcomingConsView.class);
                            Bundle to_consview = new Bundle();
                            to_consview.putString("consid", idval);
                            to_consview.putString("cons_view_type", "Upcoming");
                            intent.putExtras(to_consview);
                            startActivity(intent);

                            Model.launch = "PushNotificationService";

                        } else {
                            System.out.println("Please Login the App------");
                            intent = new Intent(this, LoginActivity.class);
                            startActivity(intent);
                        }
                    } catch (Exception e2) {
                        e2.printStackTrace();
                    }
                } else {
                    System.out.println("Please Login the App------");
                    intent = new Intent(this, LoginActivity.class);
                    startActivity(intent);
                }
            }

            if (pwd_str_check) {
                Intent i = new Intent(DeepLinkActivity.this, ForgotPwdActivity.class);
                i.putExtra("finisher", new android.os.ResultReceiver(null) {
                    @Override
                    protected void onReceiveResult(int resultCode, Bundle resultData) {
                        DeepLinkActivity.this.finish();
                    }
                });
                startActivityForResult(i, 1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}