package com.orane.docassist.New_Main;

import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.telephony.PhoneNumberUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.flurry.android.FlurryAgent;
import com.google.firebase.analytics.FirebaseAnalytics;

import com.orane.docassist.CommonActivity;
import com.orane.docassist.InviteDoctorActivity;
import com.orane.docassist.LoginActivity;
import com.orane.docassist.Model.Model;
import com.orane.docassist.Network.JSONParser;
import com.orane.docassist.Network.ShareIntent;
import com.orane.docassist.R;
import com.orane.docassist.WebViewActivity;

import org.json.JSONObject;


public class SettingsFragment extends Fragment {

    LinearLayout chat_layout, abtus_layout, terms_layout, policy_layout, reportissue_layout, rate_layout, share_layout, aredoctor_layout, pv_consult_layout, contactus_layout, csupport_layout, invite_layout, signout_layout;
    Switch switch_notisound, switch_stopnoti;
    JSONObject logout_jsonobj, logout_json_validate;

    public String noti_sound_val, stop_noti_val;

    SharedPreferences sharedpreferences;
    public static final String noti_status = "noti_status_key";
    public static final String noti_sound = "noti_sound_key";
    public static final String MyPREFERENCES = "MyPrefs";
    public static final String Login_Status = "Login_Status_key";

    Typeface font_reg, font_bold;


    public static SettingsFragment newInstance(int pageIndex) {
        SettingsFragment contentFragment = new SettingsFragment();
        Bundle args = new Bundle();
        args.putInt("pageIndex", pageIndex);
        contentFragment.setArguments(args);
        return contentFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.settings_fragment, container, false);

        FlurryAgent.onPageView();

        chat_layout = (LinearLayout) view.findViewById(R.id.chat_layout);
        contactus_layout = (LinearLayout) view.findViewById(R.id.contactus_layout);
        csupport_layout = (LinearLayout) view.findViewById(R.id.csupport_layout);
        invite_layout = (LinearLayout) view.findViewById(R.id.invite_layout);
        signout_layout = (LinearLayout) view.findViewById(R.id.signout_layout);
        abtus_layout = (LinearLayout) view.findViewById(R.id.abtus_layout);
        terms_layout = (LinearLayout) view.findViewById(R.id.terms_layout);
        policy_layout = (LinearLayout) view.findViewById(R.id.policy_layout);
        reportissue_layout = (LinearLayout) view.findViewById(R.id.reportissue_layout);
        rate_layout = (LinearLayout) view.findViewById(R.id.rate_layout);
        share_layout = (LinearLayout) view.findViewById(R.id.share_layout);
        aredoctor_layout = (LinearLayout) view.findViewById(R.id.aredoctor_layout);
        pv_consult_layout = (LinearLayout) view.findViewById(R.id.pv_consult_layout);
        switch_notisound = (Switch) view.findViewById(R.id.switch_notisound);
        switch_stopnoti = (Switch) view.findViewById(R.id.switch_stopnoti);


        font_reg = Typeface.createFromAsset(getActivity().getAssets(), Model.font_name);
        font_bold = Typeface.createFromAsset(getActivity().getAssets(), Model.font_name_bold);

        ((TextView) view.findViewById(R.id.tv_share_tit)).setTypeface(font_bold);
        ((TextView) view.findViewById(R.id.tv_rate_app)).setTypeface(font_reg);
        ((TextView) view.findViewById(R.id.tv_share_friends)).setTypeface(font_reg);
        ((TextView) view.findViewById(R.id.tv_app_sett)).setTypeface(font_bold);
        ((TextView) view.findViewById(R.id.tv_noti_sound)).setTypeface(font_reg);
        ((TextView) view.findViewById(R.id.tv_noti_stat)).setTypeface(font_reg);
        ((TextView) view.findViewById(R.id.tv_about)).setTypeface(font_bold);
        ((TextView) view.findViewById(R.id.tv_terms)).setTypeface(font_reg);
        ((TextView) view.findViewById(R.id.tv_privatepolicy)).setTypeface(font_reg);
        ((TextView) view.findViewById(R.id.tv_reportissue)).setTypeface(font_reg);
        ((TextView) view.findViewById(R.id.tv_rupat)).setTypeface(font_reg);
        ((TextView) view.findViewById(R.id.tv_signout)).setTypeface(font_bold);


        //================ Initialize ======================---------------
        sharedpreferences = getActivity().getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        stop_noti_val = sharedpreferences.getString(noti_status, "off");
        noti_sound_val = sharedpreferences.getString(noti_sound, "on");

        if (stop_noti_val.equals("on")) switch_stopnoti.setChecked(true);
        else switch_stopnoti.setChecked(false);

        if (noti_sound_val.equals("on")) switch_notisound.setChecked(true);
        else switch_notisound.setChecked(false);
        //================ Initialize ======================---------------

        switch_notisound.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) noti_sound_val = "on";
                else noti_sound_val = "off";

                //===============Apply Noti Settings Values=============================================
                SharedPreferences.Editor editor = sharedpreferences.edit();
                editor.putString(noti_sound, noti_sound_val);
                editor.apply();
                //===============Apply Noti Settings Values=============================================

                System.out.println("noti_sound_val-------" + noti_sound_val);

                //------------ Google firebase Analitics--------------------
                Model.mFirebaseAnalytics = FirebaseAnalytics.getInstance(getActivity());
                Bundle params = new Bundle();
                params.putString("User", Model.id + "/" + Model.name);
                params.putString("status", noti_sound_val);
                Model.mFirebaseAnalytics.logEvent("Switch_Notify_Sound", params);
                //------------ Google firebase Analitics--------------------

            }
        });


        switch_stopnoti.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                if (b) stop_noti_val = "on";
                else stop_noti_val = "off";

                //===============Apply Noti Settings Values=============================================
                SharedPreferences.Editor editor = sharedpreferences.edit();
                editor.putString(noti_status, stop_noti_val);
                editor.apply();
                //===============Apply Noti Settings Values=============================================

                System.out.println("stop_noti_val-------" + stop_noti_val);

                //------------ Google firebase Analitics--------------------
                Model.mFirebaseAnalytics = FirebaseAnalytics.getInstance(getActivity());
                Bundle params = new Bundle();
                params.putString("User", Model.id + "/" + Model.name);
                params.putString("status", noti_sound_val);
                Model.mFirebaseAnalytics.logEvent("Switch_Notify_Sound", params);
                //------------ Google firebase Analitics--------------------
            }
        });


        terms_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                //------------ Google firebase Analitics--------------------
                Model.mFirebaseAnalytics = FirebaseAnalytics.getInstance(getActivity());
                Bundle params = new Bundle();
                params.putString("User", Model.id + "/" + Model.name);
                Model.mFirebaseAnalytics.logEvent("Terms", params);
                //------------ Google firebase Analitics--------------------


                Intent i = new Intent(getActivity(), WebViewActivity.class);
                i.putExtra("url", "https://www.icliniq.com/p/terms?nolayout=1");
                i.putExtra("type", "Terms");
                startActivity(i);
                getActivity().overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);
            }
        });

        chat_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                try {
                    Intent sendIntent = new Intent("android.intent.action.MAIN");
                    sendIntent.setComponent(new ComponentName("com.whatsapp", "com.whatsapp.Conversation"));
                    sendIntent.putExtra(Intent.EXTRA_TEXT, "Hi, i would like to chat with you");
                    sendIntent.putExtra("jid", PhoneNumberUtils.stripSeparators("919943270000") + "@s.whatsapp.net");
                    startActivity(sendIntent);

                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(getActivity(), "Sorry., WhatsApp not Installed", Toast.LENGTH_SHORT).show();
                }
            }
        });

        invite_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //------------ Google firebase Analitics--------------------
                Model.mFirebaseAnalytics = FirebaseAnalytics.getInstance(getActivity());
                Bundle params = new Bundle();
                params.putString("User", Model.id + "/" + Model.name);
                Model.mFirebaseAnalytics.logEvent("invite_doctors", params);
                //------------ Google firebase Analitics--------------------

                FlurryAgent.logEvent("Invite Doctors");
                Intent i = new Intent(getActivity(), InviteDoctorActivity.class);
                startActivity(i);
                getActivity().overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);
            }
        });

        abtus_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //------------ Google firebase Analitics--------------------
                Model.mFirebaseAnalytics = FirebaseAnalytics.getInstance(getActivity());
                Bundle params = new Bundle();
                params.putString("User", Model.id + "/" + Model.name);
                Model.mFirebaseAnalytics.logEvent("about_us", params);
                //------------ Google firebase Analitics--------------------


                Intent i = new Intent(getActivity(), CommonActivity.class);
                i.putExtra("type", "aboutus");
                startActivity(i);
                getActivity().overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);
            }
        });

        policy_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                //------------ Google firebase Analitics--------------------
                Model.mFirebaseAnalytics = FirebaseAnalytics.getInstance(getActivity());
                Bundle params = new Bundle();
                params.putString("User", Model.id + "/" + Model.name);
                Model.mFirebaseAnalytics.logEvent("PrivatePolicy", params);
                //------------ Google firebase Analitics--------------------

                Intent i = new Intent(getActivity(), WebViewActivity.class);
                i.putExtra("url", "https://www.icliniq.com/p/privacy?nolayout=1");
                i.putExtra("type", "Privacy Policy");
                startActivity(i);
                getActivity().overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);
            }
        });

        reportissue_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(getActivity(), CommonActivity.class);
                i.putExtra("type", "feedback");
                startActivity(i);
                getActivity().overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);
            }
        });

        csupport_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(getActivity(), CommonActivity.class);
                i.putExtra("type", "support");
                startActivity(i);
                getActivity().overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);
            }
        });

        rate_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //------------ Google firebase Analitics--------------------
                Model.mFirebaseAnalytics = FirebaseAnalytics.getInstance(getActivity());
                Bundle params = new Bundle();
                params.putString("User", Model.id + "/" + Model.name);
                Model.mFirebaseAnalytics.logEvent("RateApp_Clicked", params);
                //------------ Google firebase Analitics--------------------

                String url = "https://play.google.com/store/apps/details?id=com.orane.docassist";
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
            }
        });

        share_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                try {
                    ShareIntent sintent = new ShareIntent();
                    sintent.ShareApp(getActivity(), "MainActivity");

                    //------------ Google firebase Analitics--------------------
                    Model.mFirebaseAnalytics = FirebaseAnalytics.getInstance(getActivity());
                    Bundle params = new Bundle();
                    params.putString("User", Model.id + "/" + Model.name);
                    Model.mFirebaseAnalytics.logEvent("Settings_Share_App", params);
                    //------------ Google firebase Analitics--------------------

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        aredoctor_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                //------------ Google firebase Analitics--------------------
                Model.mFirebaseAnalytics = FirebaseAnalytics.getInstance(getActivity());
                Bundle params = new Bundle();
                params.putString("User", Model.id + "/" + Model.name);
                Model.mFirebaseAnalytics.logEvent("Are_you_doctor", params);
                //------------ Google firebase Analitics--------------------

                String url = "https://play.google.com/store/apps/details?id=com.orane.icliniq";
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
            }
        });

        contactus_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //------------ Google firebase Analitics--------------------
                Model.mFirebaseAnalytics = FirebaseAnalytics.getInstance(getActivity());
                Bundle params = new Bundle();
                params.putString("User", Model.id + "/" + Model.name);
                Model.mFirebaseAnalytics.logEvent("Settings_Contact_us", params);
                //------------ Google firebase Analitics--------------------

                Intent i = new Intent(getActivity(), CommonActivity.class);
                i.putExtra("type", "contactus");
                startActivity(i);
            }
        });

        pv_consult_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

/*                Intent intent = new Intent(getActivity(), Invite_doctors.class);
                startActivity(intent);*/
            }
        });

        signout_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //------------ Google firebase Analitics--------------------
                Model.mFirebaseAnalytics = FirebaseAnalytics.getInstance(getActivity());
                Bundle params = new Bundle();
                params.putString("User", Model.id + "/" + Model.name);
                Model.mFirebaseAnalytics.logEvent("Settings_Sign_out", params);
                //------------ Google firebase Analitics--------------------
                ask_logout();
            }
        });


        return view;

    }

    public void ask_logout() {


        AlertDialog.Builder builder1 = new AlertDialog.Builder(getActivity());
        builder1.setMessage("Are you sure you want to logout?");
        builder1.setCancelable(true);

        builder1.setPositiveButton(
                "Yes",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        try {

                            logout_json_validate = new JSONObject();
                            logout_json_validate.put("user_id", Model.id);
                            logout_json_validate.put("reg_id", Model.token);
                            logout_json_validate.put("token", Model.token);
                            logout_json_validate.put("os_type", "1");

                            System.out.println("logout_json_validate----" + logout_json_validate.toString());

                            new JSON_logout().execute(logout_json_validate);

                            //--------------------------------------------------
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        //--------------- Logout------------------------------------------------

                    }
                });

        builder1.setNegativeButton(
                "No",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        AlertDialog alert11 = builder1.create();
        alert11.show();


    }


    class JSON_logout extends AsyncTask<JSONObject, Void, Boolean> {

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
                logout_jsonobj = jParser.JSON_POST(urls[0], "logout");


                System.out.println("logout_jsonobj---------------" + logout_jsonobj.toString());

                return true;
            } catch (Exception e) {
                e.printStackTrace();
            }

            return false;
        }

        protected void onPostExecute(Boolean result) {

            try {
                System.out.println("logout_jsonobj---------------" + logout_jsonobj.toString());

                dialog.cancel();

                if (logout_jsonobj.has("status")) {
                    String sta_val = logout_jsonobj.getString("status");
                    if (sta_val.equals("1")) {

                        //===============Apply Noti Settings Values=============================================
                        SharedPreferences.Editor editor = sharedpreferences.edit();
                        editor.putString(Login_Status, "0");
                        editor.apply();
                        //===============Apply Noti Settings Values=============================================

                        getActivity().finishAffinity();
                        Intent intent = new Intent(getActivity(), LoginActivity.class);
                        startActivity(intent);
                        getActivity().finish();
                    }
                    else{
                        Toast.makeText(getActivity(), "Logout failed. please try later", Toast.LENGTH_SHORT).show();
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


}
