package com.orane.docassist.fragment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.orane.docassist.Model.Model;
import com.orane.docassist.MyClinicAddActivity;
import com.orane.docassist.Network.JSONParser;
import com.orane.docassist.R;

import org.json.JSONObject;

import me.drakeet.materialdialog.MaterialDialog;

public class ProffessionalFragment extends Fragment {

    RelativeLayout pat_layout, pat_week__layout, year_layout;
    LinearLayout awards_layout, bio_layout, links_layout;
    CardView card_practise;
    Button btn_submit;
    String clinic_id, tit_text, address_text, street_text, city_text, state_text, zip_text, country_text;
    JSONObject json_profile;
    TextView tv_sub_chat;
    JSONObject json_professional, login_json, login_jsonobj;
    EditText edt_lin, edt_tw, edt_fb, edt_personalbio, edt_week, edt_exp, edt_pat, edt_awards;
    TextView tv_pending_text;
    ScrollView scroll_layout;

    public ProffessionalFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.professional_fragment, container, false);

        tv_pending_text = rootView.findViewById(R.id.tv_pending_text);
        scroll_layout = rootView.findViewById(R.id.scroll_layout);

        tv_sub_chat = rootView.findViewById(R.id.tv_sub_chat);
        pat_week__layout = rootView.findViewById(R.id.pat_week__layout);
        awards_layout = rootView.findViewById(R.id.awards_layout);

        bio_layout = rootView.findViewById(R.id.bio_layout);
        year_layout = rootView.findViewById(R.id.year_layout);
        pat_layout = rootView.findViewById(R.id.pat_layout);
        pat_week__layout = rootView.findViewById(R.id.pat_week__layout);
        links_layout = rootView.findViewById(R.id.links_layout);

        card_practise = rootView.findViewById(R.id.card_practise);
        btn_submit = rootView.findViewById(R.id.btn_submit);

        edt_personalbio = rootView.findViewById(R.id.edt_personalbio);
        edt_pat = rootView.findViewById(R.id.edt_pat);
        edt_exp = rootView.findViewById(R.id.edt_exp);
        edt_week = rootView.findViewById(R.id.edt_week);
        edt_awards = rootView.findViewById(R.id.edt_awards);
        edt_lin = rootView.findViewById(R.id.edt_lin);
        edt_fb = rootView.findViewById(R.id.edt_fb);
        edt_tw = rootView.findViewById(R.id.edt_tw);

        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        apply_professional();

/*      Animation animSlideDown1 = AnimationUtils.loadAnimation(Signup3.this, R.anim.bounce);
        animSlideDown1.setStartOffset(100);
        bio_layout.startAnimation(animSlideDown1);

        Animation animSlideDown2 = AnimationUtils.loadAnimation(Signup3.this, R.anim.bounce2);
        animSlideDown2.setStartOffset(300);
        year_layout.startAnimation(animSlideDown2);

        Animation animSlideDown3 = AnimationUtils.loadAnimation(Signup3.this, R.anim.bounce3);
        animSlideDown3.setStartOffset(500);
        pat_layout.startAnimation(animSlideDown3);


        Animation animSlideDown4 = AnimationUtils.loadAnimation(Signup3.this, R.anim.bounce4);
        animSlideDown4.setStartOffset(700);
        card_practise.startAnimation(animSlideDown4);*/


        edt_personalbio.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (hasFocus) {
                    edt_personalbio.setMaxLines(1000);
                } else {
                    edt_personalbio.setMaxLines(5);
                }
            }
        });

        edt_awards.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (hasFocus) {
                    edt_awards.setMaxLines(1000);
                } else {
                    edt_awards.setMaxLines(5);
                }
            }
        });


        card_practise.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Model.launch = "Signup";
                Intent intent = new Intent(getActivity(), MyClinicAddActivity.class);
                intent.putExtra("clinic_id", clinic_id);
                intent.putExtra("clinic_name", tit_text);
                intent.putExtra("clinic_street", street_text);
                intent.putExtra("clinic_geo", address_text);
                intent.putExtra("mode", "update");
                intent.putExtra("type", "profile_update");
                startActivity(intent);
            }
        });

        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String pers_text = edt_personalbio.getText().toString();
                String exp_years = edt_exp.getText().toString();
                String edt_pat_val = edt_pat.getText().toString();
                String edt_week_val = edt_week.getText().toString();
                String edt_awards_val = edt_awards.getText().toString();

                if (!pers_text.equals("")) {
                    if (!exp_years.equals("")) {
                        if (!edt_pat_val.equals("")) {
                            if (!edt_week_val.equals("")) {

                                Signup_submit();

                            } else {
                                edt_week.setError("How many patients do you consult in a week?");
                            }
                        } else {
                            edt_pat.setError("Enter No.of patients");
                        }
                    } else {
                        edt_exp.setError("Please enter your Experience in years");
                    }
                } else {
                    edt_personalbio.setError("Please fill the personal bio informations");
                }

            }
        });
        return rootView;
    }


    public void Signup_submit() {

        try {

            String pers_text = edt_personalbio.getText().toString();
            String exp_years = edt_exp.getText().toString();
            String edt_pat_val = edt_pat.getText().toString();
            String edt_week_val = edt_week.getText().toString();
            String edt_awards_val = edt_awards.getText().toString();
            String edt_lin_val = edt_lin.getText().toString();
            String edt_fb_val = edt_fb.getText().toString();
            String edt_tw_val = edt_tw.getText().toString();

            login_json = new JSONObject();
            login_json.put("about", pers_text);
            login_json.put("exp", exp_years);
            login_json.put("pat_total", edt_pat_val);
            login_json.put("pat_avg", edt_week_val);
            login_json.put("awards", edt_awards_val);
            login_json.put("linkedIn", edt_lin_val);
            login_json.put("fbLink", edt_fb_val);
            login_json.put("tweetLink", edt_tw_val);

            JSONObject json_address = new JSONObject();
            json_address.put("clinic_id", Model.cliniq_id);
            json_address.put("title", Model.clinic_title);
            json_address.put("street", Model.clinic_street);
            json_address.put("place_id", Model.clinic_placeid);

            login_json.put("address", json_address);

            System.out.println("Signup_json----" + login_json.toString());

            new JSON_post_Proffesional().execute(login_json);


        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private class JSON_post_Proffesional extends AsyncTask<JSONObject, Void, Boolean> {

        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            dialog = new ProgressDialog(getActivity());
            dialog.setMessage("Submitting.. please wait");
            dialog.show();
            dialog.setCancelable(false);
        }

        @Override
        protected Boolean doInBackground(JSONObject... urls) {

            try {
                JSONParser jParser = new JSONParser();
                login_jsonobj = jParser.JSON_POST(urls[0], "proffessional");

                System.out.println("urls[0]----------" + urls[0]);
                System.out.println("Server_Response_JSON----------" + login_jsonobj.toString());

                return true;
            } catch (Exception e) {
                e.printStackTrace();
            }
            return false;
        }

        protected void onPostExecute(Boolean result) {

            try {

                System.out.println("json_response-----------" + login_jsonobj.toString());

                String status_val = login_jsonobj.getString("status");

                if (status_val.equals("1")) {
                    say_success();

                    tv_pending_text.setVisibility(View.VISIBLE);
                    scroll_layout.setVisibility(View.GONE);

                } else {

                    if (login_jsonobj.has("err")) {
                        String err_val = login_jsonobj.getString("err");
                        Toast.makeText(getActivity(), err_val, Toast.LENGTH_SHORT).show();
                    }
                }
                dialog.cancel();

            } catch (Exception e) {
                e.printStackTrace();
            }

            dialog.cancel();

        }
    }

    public void say_success() {

        final MaterialDialog alert = new MaterialDialog(getActivity());
        //alert.setTitle("Thankyou.!");
        alert.setMessage("Proffessional details saved successfully..!");
        alert.setCanceledOnTouchOutside(false);
        alert.setPositiveButton("Ok", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alert.dismiss();
            }
        });
        alert.show();
    }


    public void apply_professional() {

        try {

            System.out.println("Model.profile_str-------------" + Model.profile_str);

            json_profile = new JSONObject(Model.profile_str);
            String professional_str = json_profile.getString("professional");

            System.out.println("Proffessional_str-----------" + professional_str);
            json_professional = new JSONObject(professional_str);

            System.out.println("json_professional-----------" + json_professional.toString());

            String about_text = json_professional.getString("about");
            String exp_text = json_professional.getString("exp");
            String pat_total = json_professional.getString("pat_total");
            String pat_avg = json_professional.getString("pat_avg");
            String awards_text = json_professional.getString("awards");
            String linkedIn = json_professional.getString("linkedIn");
            String fbLink = json_professional.getString("fbLink");
            String tweetLink = json_professional.getString("tweetLink");
            String address = json_professional.getString("address");
            String hasUpdated_val = json_professional.getString("hasUpdated");


            //--------------------------------------------
            if (hasUpdated_val.equals("1")) {
                tv_pending_text.setVisibility(View.VISIBLE);
                scroll_layout.setVisibility(View.GONE);
            } else {
                tv_pending_text.setVisibility(View.GONE);
                scroll_layout.setVisibility(View.VISIBLE);
            }
            //--------------------------------------------


            edt_personalbio.setText(Html.fromHtml(about_text));
            edt_exp.setText(Html.fromHtml(exp_text));

            if (pat_total != null && !pat_total.isEmpty() && !pat_total.equals("null") && !pat_total.equals("")) {
                edt_pat.setText(pat_total);
            }

            if (pat_avg != null && !pat_avg.isEmpty() && !pat_avg.equals("null") && !pat_avg.equals("")) {
                edt_week.setText(pat_avg);
            }


            edt_awards.setText(Html.fromHtml(awards_text));
            edt_lin.setText(Html.fromHtml(linkedIn));
            edt_fb.setText(Html.fromHtml(fbLink));
            edt_tw.setText(Html.fromHtml(tweetLink));

            if (address.length() > 5) {
                JSONObject json_address = new JSONObject(address);
                clinic_id = json_address.getString("id");
                tit_text = json_address.getString("title");
                street_text = json_address.getString("street");
                city_text = json_address.getString("city");
                state_text = json_address.getString("state");
                zip_text = json_address.getString("zip");
                country_text = json_address.getString("country");

                address_text = city_text + ", " + state_text + ", " + zip_text + ", " + country_text;

                tv_sub_chat.setVisibility(View.VISIBLE);
                tv_sub_chat.setText(Html.fromHtml(address_text));
            }
            else{
                tv_sub_chat.setVisibility(View.GONE);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
