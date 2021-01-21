package com.orane.docassist.fragment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.orane.docassist.Model.Model;
import com.orane.docassist.Multiselect.ListViewExample;
import com.orane.docassist.Network.JSONParser;
import com.orane.docassist.R;
import com.rengwuxian.materialedittext.MaterialEditText;

import org.json.JSONObject;

import me.drakeet.materialdialog.MaterialDialog;

public class ProfessionalFragment extends Fragment {

    MaterialEditText edt_imc;
    EditText edt_awards, edt_exp;
    TextView tv_awards, tv_spec_name, tv_lang_name;
    Button btn_submit;
    JSONObject lang_obj;
    LinearLayout select_spec_layout, lang_layout;
    String str_response, speciality_list, specialities_text, lang_val, speak_lang, spec_val;
    JSONObject json_personalObj, json_professional, spec_json, json_response_obj, json_profile;
    RelativeLayout ccode_layout;

    public ProfessionalFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.professional_fragment, container, false);

        edt_imc = (MaterialEditText) rootView.findViewById(R.id.edt_imc);
        edt_awards = (EditText) rootView.findViewById(R.id.edt_awards);
        // edt_lic = (MaterialEditText) rootView.findViewById(R.id.edt_lic);
        edt_exp = (EditText) rootView.findViewById(R.id.edt_exp);
        tv_awards = (TextView) rootView.findViewById(R.id.tv_awards);
        tv_spec_name = (TextView) rootView.findViewById(R.id.tv_spec_name);
        tv_lang_name = (TextView) rootView.findViewById(R.id.tv_lang_name);
        select_spec_layout = (LinearLayout) rootView.findViewById(R.id.select_spec_layout);
        lang_layout = (LinearLayout) rootView.findViewById(R.id.lang_layout);
        btn_submit = (Button) rootView.findViewById(R.id.btn_submit);

      //  apply_professional();

        select_spec_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ListViewExample.class);
                intent.putExtra("json_data", speciality_list);
                intent.putExtra("data_src", "speciality");
                startActivity(intent);
            }
        });

        lang_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ListViewExample.class);
                intent.putExtra("json_data", lang_obj.toString());
                intent.putExtra("data_src", "language");
                startActivity(intent);
            }
        });


        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {

                    String edt_imc_val = edt_imc.getText().toString();
                    //String edt_lic_val = edt_lic.getText().toString();
                    String edt_exp_val = edt_exp.getText().toString();
                    String edt_awards_val = edt_awards.getText().toString();

                    json_personalObj = new JSONObject();
                    json_personalObj.put("user_id", (Model.id));
                    json_personalObj.put("imc_id", edt_imc_val);
                    //json_personalObj.put("license_at", edt_lic_val);
                    json_personalObj.put("token", Model.token);
                    json_personalObj.put("language", Model.selected_lang_api);
                    json_personalObj.put("exp", edt_exp_val);
                    json_personalObj.put("awards", edt_awards_val);
                    json_personalObj.put("specialities", Model.selected_spec_api);
                    //json_personalObj.put("pwd", "");

                    System.out.println("json_personalObj---" + json_personalObj.toString());

                    new Asyc_PersonData().execute(json_personalObj);

                } catch (Exception e2) {
                    e2.printStackTrace();
                }
            }
        });


        return rootView;
    }

   /* public void apply_professional() {

        try {

            System.out.println("Model.profile_str-------------" + Model.profile_str);

            json_profile = new JSONObject(Model.profile_str);
            String professional_str = json_profile.getString("professional");

            System.out.println("Proffessional_str-----------" + professional_str);
            json_professional = new JSONObject(professional_str);

            System.out.println("json_personal-----------" + json_professional.toString());

            String imc_id = json_professional.getString("imc_id");
            String license_at = json_professional.getString("license_at");
            String exp = json_professional.getString("exp");
            String awards = json_professional.getString("awards");
            speak_lang = json_professional.getString("speak_lang");
            String certificate = json_professional.getString("certificate");
            String education = json_professional.getString("education");
            specialities_text = json_professional.getString("specialities");

            Model.selected_spec_api = specialities_text.replaceAll("\\[", "").replaceAll("\\]", "");
            Model.selected_lang_api = speak_lang.replaceAll("\\[", "").replaceAll("\\]", "");

            System.out.println("One specialities_text-----------" + specialities_text);

            edt_imc.setText(imc_id);
            // edt_lic.setText(license_at);
            edt_exp.setText(exp + " year(s)");
            edt_awards.setText(Html.fromHtml(awards));

            //----------- speak_lang -----------------------------
            apply_lang();
            //----------- speak_lang -----------------------------
            //---------------------- Reff Fee Get -----------------------------
            String spec_url = Model.BASE_URL + "sapp/getAllSpecialities";
            System.out.println("spec_url---------" + spec_url);
            new JSON_spec().execute(spec_url);
            //---------------------- Reff Fee Get -----------------------------

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
*/

    private class Asyc_PersonData extends AsyncTask<JSONObject, Void, Boolean> {

        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(getActivity());
            dialog.setMessage("Please wait..");
            dialog.show();
            dialog.setCancelable(false);
        }

        @Override
        protected Boolean doInBackground(JSONObject... urls) {

            try {

                JSONParser jParser = new JSONParser();
                json_response_obj = jParser.JSON_POST(urls[0], "professional");

                System.out.println("URL---------------" + urls[0]);
                System.out.println("json_response_obj-----------" + json_response_obj.toString());

                return true;
            } catch (Exception e) {
                e.printStackTrace();
            }

            return false;
        }

        protected void onPostExecute(Boolean result) {

            try {

                say_success();

                //------------ Google firebase Analitics--------------------
                Model.mFirebaseAnalytics = FirebaseAnalytics.getInstance(getActivity());
                Bundle params = new Bundle();
                params.putString("Details", json_response_obj.toString());
                Model.mFirebaseAnalytics.logEvent("Profile_Professional_Success", params);
                //------------ Google firebase Analitics--------------------


                dialog.cancel();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void say_success() {

        final MaterialDialog alert = new MaterialDialog(getActivity());
        //alert.setTitle("Thankyou.!");
        alert.setMessage("Professional informations saved successfully..!");
        alert.setCanceledOnTouchOutside(false);
        alert.setPositiveButton("Ok", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alert.dismiss();
            }
        });
        alert.show();

    }

    public void apply_lang() {

        try {
/*            lang_obj = new JSONObject();
            lang_obj.put("ab", "abraham");
            lang_obj.put("ta", "tamil");*/

            lang_obj = new JSONObject("{\n" +
                    "\t\"ab\": \"Abkhazian\",\n" +
                    "\t\"ace\": \"Achinese\",\n" +
                    "\t\"ach\": \"Acoli\",\n" +
                    "\t\"ada\": \"Adangme\",\n" +
                    "\t\"ady\": \"Adyghe\",\n" +
                    "\t\"aa\": \"Afar\",\n" +
                    "\t\"afh\": \"Afrihili\",\n" +
                    "\t\"af\": \"Afrikaans\",\n" +
                    "\t\"afa\": \"Afro-Asiatic Language\",\n" +
                    "\t\"ain\": \"Ainu\",\n" +
                    "\t\"ak\": \"Akan\",\n" +
                    "\t\"akk\": \"Akkadian\",\n" +
                    "\t\"sq\": \"Albanian\",\n" +
                    "\t\"ale\": \"Aleut\",\n" +
                    "\t\"alg\": \"Algonquian Language\",\n" +
                    "\t\"tut\": \"Altaic Language\",\n" +
                    "\t\"am\": \"Amharic\",\n" +
                    "\t\"egy\": \"Ancient Egyptian\",\n" +
                    "\t\"grc\": \"Ancient Greek\",\n" +
                    "\t\"anp\": \"Angika\",\n" +
                    "\t\"apa\": \"Apache Language\",\n" +
                    "\t\"ar\": \"Arabic\",\n" +
                    "\t\"an\": \"Aragonese\",\n" +
                    "\t\"arc\": \"Aramaic\",\n" +
                    "\t\"arp\": \"Arapaho\",\n" +
                    "\t\"arn\": \"Araucanian\",\n" +
                    "\t\"arw\": \"Arawak\",\n" +
                    "\t\"hy\": \"Armenian\",\n" +
                    "\t\"rup\": \"Aromanian\",\n" +
                    "\t\"art\": \"Artificial Language\",\n" +
                    "\t\"as\": \"Assamese\",\n" +
                    "\t\"ast\": \"Asturian\",\n" +
                    "\t\"ath\": \"Athapascan Language\",\n" +
                    "\t\"cch\": \"Atsam\",\n" +
                    "\t\"en_AU\": \"Australian English\",\n" +
                    "\t\"aus\": \"Australian Language\",\n" +
                    "\t\"de_AT\": \"Austrian German\",\n" +
                    "\t\"map\": \"Austronesian Language\",\n" +
                    "\t\"av\": \"Avaric\",\n" +
                    "\t\"ae\": \"Avestan\",\n" +
                    "\t\"awa\": \"Awadhi\",\n" +
                    "\t\"ay\": \"Aymara\",\n" +
                    "\t\"az\": \"Azerbaijani\",\n" +
                    "\t\"ban\": \"Balinese\",\n" +
                    "\t\"bat\": \"Baltic Language\",\n" +
                    "\t\"bal\": \"Baluchi\",\n" +
                    "\t\"bm\": \"Bambara\",\n" +
                    "\t\"bai\": \"Bamileke Language\",\n" +
                    "\t\"bad\": \"Banda\",\n" +
                    "\t\"bnt\": \"Bantu\",\n" +
                    "\t\"bas\": \"Basa\",\n" +
                    "\t\"ba\": \"Bashkir\",\n" +
                    "\t\"eu\": \"Basque\",\n" +
                    "\t\"btk\": \"Batak\",\n" +
                    "\t\"bej\": \"Beja\",\n" +
                    "\t\"be\": \"Belarusian\",\n" +
                    "\t\"bem\": \"Bemba\",\n" +
                    "\t\"bn\": \"Bengali\",\n" +
                    "\t\"ber\": \"Berber\",\n" +
                    "\t\"bho\": \"Bhojpuri\",\n" +
                    "\t\"bh\": \"Bihari\",\n" +
                    "\t\"bik\": \"Bikol\",\n" +
                    "\t\"bin\": \"Bini\",\n" +
                    "\t\"bi\": \"Bislama\",\n" +
                    "\t\"byn\": \"Blin\",\n" +
                    "\t\"zbl\": \"Blissymbols\",\n" +
                    "\t\"bs\": \"Bosnian\",\n" +
                    "\t\"bra\": \"Braj\",\n" +
                    "\t\"pt_BR\": \"Brazilian Portuguese\",\n" +
                    "\t\"br\": \"Breton\",\n" +
                    "\t\"en_GB\": \"British English\",\n" +
                    "\t\"bug\": \"Buginese\",\n" +
                    "\t\"bg\": \"Bulgarian\",\n" +
                    "\t\"bua\": \"Buriat\",\n" +
                    "\t\"my\": \"Burmese\",\n" +
                    "\t\"cad\": \"Caddo\",\n" +
                    "\t\"en_CA\": \"Canadian English\",\n" +
                    "\t\"fr_CA\": \"Canadian French\",\n" +
                    "\t\"car\": \"Carib\",\n" +
                    "\t\"ca\": \"Catalan\",\n" +
                    "\t\"cau\": \"Caucasian Language\",\n" +
                    "\t\"ceb\": \"Cebuano\",\n" +
                    "\t\"cel\": \"Celtic Language\",\n" +
                    "\t\"cai\": \"Central American Indian Language\",\n" +
                    "\t\"chg\": \"Chagatai\",\n" +
                    "\t\"cmc\": \"Chamic Language\",\n" +
                    "\t\"ch\": \"Chamorro\",\n" +
                    "\t\"ce\": \"Chechen\",\n" +
                    "\t\"chr\": \"Cherokee\",\n" +
                    "\t\"chy\": \"Cheyenne\",\n" +
                    "\t\"chb\": \"Chibcha\",\n" +
                    "\t\"zh\": \"Chinese\",\n" +
                    "\t\"chn\": \"Chinook Jargon\",\n" +
                    "\t\"chp\": \"Chipewyan\",\n" +
                    "\t\"cho\": \"Choctaw\",\n" +
                    "\t\"cu\": \"Church Slavic\",\n" +
                    "\t\"chk\": \"Chuukese\",\n" +
                    "\t\"cv\": \"Chuvash\",\n" +
                    "\t\"nwc\": \"Classical Newari\",\n" +
                    "\t\"syc\": \"Classical Syriac\",\n" +
                    "\t\"cop\": \"Coptic\",\n" +
                    "\t\"kw\": \"Cornish\",\n" +
                    "\t\"co\": \"Corsican\",\n" +
                    "\t\"cr\": \"Cree\",\n" +
                    "\t\"mus\": \"Creek\",\n" +
                    "\t\"crp\": \"Creole or Pidgin\",\n" +
                    "\t\"crh\": \"Crimean Turkish\",\n" +
                    "\t\"hr\": \"Croatian\",\n" +
                    "\t\"cus\": \"Cushitic Language\",\n" +
                    "\t\"cs\": \"Czech\",\n" +
                    "\t\"dak\": \"Dakota\",\n" +
                    "\t\"da\": \"Danish\",\n" +
                    "\t\"dar\": \"Dargwa\",\n" +
                    "\t\"day\": \"Dayak\",\n" +
                    "\t\"del\": \"Delaware\",\n" +
                    "\t\"din\": \"Dinka\",\n" +
                    "\t\"dv\": \"Divehi\",\n" +
                    "\t\"doi\": \"Dogri\",\n" +
                    "\t\"dgr\": \"Dogrib\",\n" +
                    "\t\"dra\": \"Dravidian Language\",\n" +
                    "\t\"dua\": \"Duala\",\n" +
                    "\t\"nl\": \"Dutch\",\n" +
                    "\t\"dyu\": \"Dyula\",\n" +
                    "\t\"dz\": \"Dzongkha\",\n" +
                    "\t\"frs\": \"Eastern Frisian\",\n" +
                    "\t\"efi\": \"Efik\",\n" +
                    "\t\"eka\": \"Ekajuk\",\n" +
                    "\t\"elx\": \"Elamite\",\n" +
                    "\t\"en\": \"English\",\n" +
                    "\t\"cpe\": \"English-based Creole or Pidgin\",\n" +
                    "\t\"myv\": \"Erzya\",\n" +
                    "\t\"eo\": \"Esperanto\",\n" +
                    "\t\"et\": \"Estonian\",\n" +
                    "\t\"ee\": \"Ewe\",\n" +
                    "\t\"ewo\": \"Ewondo\",\n" +
                    "\t\"fan\": \"Fang\",\n" +
                    "\t\"fat\": \"Fanti\",\n" +
                    "\t\"fo\": \"Faroese\",\n" +
                    "\t\"fj\": \"Fijian\",\n" +
                    "\t\"fil\": \"Filipino\",\n" +
                    "\t\"fi\": \"Finnish\",\n" +
                    "\t\"fiu\": \"Finno-Ugrian Language\",\n" +
                    "\t\"nl_BE\": \"Flemish\",\n" +
                    "\t\"fon\": \"Fon\",\n" +
                    "\t\"fr\": \"French\",\n" +
                    "\t\"cpf\": \"French-based Creole or Pidgin\",\n" +
                    "\t\"fur\": \"Friulian\",\n" +
                    "\t\"ff\": \"Fulah\",\n" +
                    "\t\"gaa\": \"Ga\",\n" +
                    "\t\"gl\": \"Galician\",\n" +
                    "\t\"lg\": \"Ganda\",\n" +
                    "\t\"gay\": \"Gayo\",\n" +
                    "\t\"gba\": \"Gbaya\",\n" +
                    "\t\"gez\": \"Geez\",\n" +
                    "\t\"ka\": \"Georgian\",\n" +
                    "\t\"de\": \"German\",\n" +
                    "\t\"gem\": \"Germanic Language\",\n" +
                    "\t\"gil\": \"Gilbertese\",\n" +
                    "\t\"gon\": \"Gondi\",\n" +
                    "\t\"gor\": \"Gorontalo\",\n" +
                    "\t\"got\": \"Gothic\",\n" +
                    "\t\"grb\": \"Grebo\",\n" +
                    "\t\"el\": \"Greek\",\n" +
                    "\t\"gn\": \"Guarani\",\n" +
                    "\t\"gu\": \"Gujarati\",\n" +
                    "\t\"gwi\": \"Gwichʼin\",\n" +
                    "\t\"hai\": \"Haida\",\n" +
                    "\t\"ht\": \"Haitian\",\n" +
                    "\t\"ha\": \"Hausa\",\n" +
                    "\t\"haw\": \"Hawaiian\",\n" +
                    "\t\"he\": \"Hebrew\",\n" +
                    "\t\"hz\": \"Herero\",\n" +
                    "\t\"hil\": \"Hiligaynon\",\n" +
                    "\t\"him\": \"Himachali\",\n" +
                    "\t\"hi\": \"Hindi\",\n" +
                    "\t\"ho\": \"Hiri Motu\",\n" +
                    "\t\"hit\": \"Hittite\",\n" +
                    "\t\"hmn\": \"Hmong\",\n" +
                    "\t\"hu\": \"Hungarian\",\n" +
                    "\t\"hup\": \"Hupa\",\n" +
                    "\t\"iba\": \"Iban\",\n" +
                    "\t\"pt_PT\": \"Iberian Portuguese\",\n" +
                    "\t\"es_ES\": \"Iberian Spanish\",\n" +
                    "\t\"is\": \"Icelandic\",\n" +
                    "\t\"io\": \"Ido\",\n" +
                    "\t\"ig\": \"Igbo\",\n" +
                    "\t\"ijo\": \"Ijo\",\n" +
                    "\t\"ilo\": \"Iloko\",\n" +
                    "\t\"smn\": \"Inari Sami\",\n" +
                    "\t\"inc\": \"Indic Language\",\n" +
                    "\t\"ine\": \"Indo-European Language\",\n" +
                    "\t\"id\": \"Indonesian\",\n" +
                    "\t\"inh\": \"Ingush\",\n" +
                    "\t\"ia\": \"Interlingua\",\n" +
                    "\t\"ie\": \"Interlingue\",\n" +
                    "\t\"iu\": \"Inuktitut\",\n" +
                    "\t\"ik\": \"Inupiaq\",\n" +
                    "\t\"ira\": \"Iranian Language\",\n" +
                    "\t\"ga\": \"Irish\",\n" +
                    "\t\"iro\": \"Iroquoian Language\",\n" +
                    "\t\"it\": \"Italian\",\n" +
                    "\t\"ja\": \"Japanese\",\n" +
                    "\t\"jv\": \"Javanese\",\n" +
                    "\t\"kaj\": \"Jju\",\n" +
                    "\t\"jrb\": \"Judeo-Arabic\",\n" +
                    "\t\"jpr\": \"Judeo-Persian\",\n" +
                    "\t\"kbd\": \"Kabardian\",\n" +
                    "\t\"kab\": \"Kabyle\",\n" +
                    "\t\"kac\": \"Kachin\",\n" +
                    "\t\"kl\": \"Kalaallisut\",\n" +
                    "\t\"xal\": \"Kalmyk\",\n" +
                    "\t\"kam\": \"Kamba\",\n" +
                    "\t\"kn\": \"Kannada\",\n" +
                    "\t\"kr\": \"Kanuri\",\n" +
                    "\t\"krc\": \"Karachay-Balkar\",\n" +
                    "\t\"kaa\": \"Kara-Kalpak\",\n" +
                    "\t\"krl\": \"Karelian\",\n" +
                    "\t\"kar\": \"Karen\",\n" +
                    "\t\"ks\": \"Kashmiri\",\n" +
                    "\t\"csb\": \"Kashubian\",\n" +
                    "\t\"kaw\": \"Kawi\",\n" +
                    "\t\"kk\": \"Kazakh\",\n" +
                    "\t\"kha\": \"Khasi\",\n" +
                    "\t\"km\": \"Khmer\",\n" +
                    "\t\"khi\": \"Khoisan Language\",\n" +
                    "\t\"kho\": \"Khotanese\",\n" +
                    "\t\"ki\": \"Kikuyu\",\n" +
                    "\t\"kmb\": \"Kimbundu\",\n" +
                    "\t\"rw\": \"Kinyarwanda\",\n" +
                    "\t\"ky\": \"Kirghiz\",\n" +
                    "\t\"tlh\": \"Klingon\",\n" +
                    "\t\"kv\": \"Komi\",\n" +
                    "\t\"kg\": \"Kongo\",\n" +
                    "\t\"kok\": \"Konkani\",\n" +
                    "\t\"ko\": \"Korean\",\n" +
                    "\t\"kfo\": \"Koro\",\n" +
                    "\t\"kos\": \"Kosraean\",\n" +
                    "\t\"kpe\": \"Kpelle\",\n" +
                    "\t\"kro\": \"Kru\",\n" +
                    "\t\"kj\": \"Kuanyama\",\n" +
                    "\t\"kum\": \"Kumyk\",\n" +
                    "\t\"ku\": \"Kurdish\",\n" +
                    "\t\"kru\": \"Kurukh\",\n" +
                    "\t\"kut\": \"Kutenai\",\n" +
                    "\t\"lad\": \"Ladino\",\n" +
                    "\t\"lah\": \"Lahnda\",\n" +
                    "\t\"lam\": \"Lamba\",\n" +
                    "\t\"lo\": \"Lao\",\n" +
                    "\t\"la\": \"Latin\",\n" +
                    "\t\"es_419\": \"Latin American Spanish\",\n" +
                    "\t\"lv\": \"Latvian\",\n" +
                    "\t\"lez\": \"Lezghian\",\n" +
                    "\t\"li\": \"Limburgish\",\n" +
                    "\t\"ln\": \"Lingala\",\n" +
                    "\t\"lt\": \"Lithuanian\",\n" +
                    "\t\"jbo\": \"Lojban\",\n" +
                    "\t\"dsb\": \"Lower Sorbian\",\n" +
                    "\t\"nds\": \"Low German\",\n" +
                    "\t\"loz\": \"Lozi\",\n" +
                    "\t\"lu\": \"Luba-Katanga\",\n" +
                    "\t\"lua\": \"Luba-Lulua\",\n" +
                    "\t\"lui\": \"Luiseno\",\n" +
                    "\t\"smj\": \"Lule Sami\",\n" +
                    "\t\"lun\": \"Lunda\",\n" +
                    "\t\"luo\": \"Luo\",\n" +
                    "\t\"lus\": \"Lushai\",\n" +
                    "\t\"lb\": \"Luxembourgish\",\n" +
                    "\t\"mk\": \"Macedonian\",\n" +
                    "\t\"mad\": \"Madurese\",\n" +
                    "\t\"mag\": \"Magahi\",\n" +
                    "\t\"mai\": \"Maithili\",\n" +
                    "\t\"mak\": \"Makasar\",\n" +
                    "\t\"mg\": \"Malagasy\",\n" +
                    "\t\"ms\": \"Malay\",\n" +
                    "\t\"ml\": \"Malayalam\",\n" +
                    "\t\"mt\": \"Maltese\",\n" +
                    "\t\"mnc\": \"Manchu\",\n" +
                    "\t\"mdr\": \"Mandar\",\n" +
                    "\t\"man\": \"Mandingo\",\n" +
                    "\t\"mni\": \"Manipuri\",\n" +
                    "\t\"mno\": \"Manobo Language\",\n" +
                    "\t\"gv\": \"Manx\",\n" +
                    "\t\"mi\": \"Maori\",\n" +
                    "\t\"mr\": \"Marathi\",\n" +
                    "\t\"chm\": \"Mari\",\n" +
                    "\t\"mh\": \"Marshallese\",\n" +
                    "\t\"mwr\": \"Marwari\",\n" +
                    "\t\"mas\": \"Masai\",\n" +
                    "\t\"myn\": \"Mayan Language\",\n" +
                    "\t\"men\": \"Mende\",\n" +
                    "\t\"mic\": \"Micmac\",\n" +
                    "\t\"dum\": \"Middle Dutch\",\n" +
                    "\t\"enm\": \"Middle English\",\n" +
                    "\t\"frm\": \"Middle French\",\n" +
                    "\t\"gmh\": \"Middle High German\",\n" +
                    "\t\"mga\": \"Middle Irish\",\n" +
                    "\t\"min\": \"Minangkabau\",\n" +
                    "\t\"mwl\": \"Mirandese\",\n" +
                    "\t\"mis\": \"Miscellaneous Language\",\n" +
                    "\t\"moh\": \"Mohawk\",\n" +
                    "\t\"mdf\": \"Moksha\",\n" +
                    "\t\"mo\": \"Moldavian\",\n" +
                    "\t\"lol\": \"Mongo\",\n" +
                    "\t\"mn\": \"Mongolian\",\n" +
                    "\t\"mkh\": \"Mon-Khmer Language\",\n" +
                    "\t\"mfe\": \"Morisyen\",\n" +
                    "\t\"mos\": \"Mossi\",\n" +
                    "\t\"mul\": \"Multiple Languages\",\n" +
                    "\t\"mun\": \"Munda Language\",\n" +
                    "\t\"nah\": \"Nahuatl\",\n" +
                    "\t\"na\": \"Nauru\",\n" +
                    "\t\"nv\": \"Navajo\",\n" +
                    "\t\"ng\": \"Ndonga\",\n" +
                    "\t\"nap\": \"Neapolitan\",\n" +
                    "\t\"ne\": \"Nepali\",\n" +
                    "\t\"new\": \"Newari\",\n" +
                    "\t\"nia\": \"Nias\",\n" +
                    "\t\"nic\": \"Niger-Kordofanian Language\",\n" +
                    "\t\"ssa\": \"Nilo-Saharan Language\",\n" +
                    "\t\"niu\": \"Niuean\",\n" +
                    "\t\"nqo\": \"N’Ko\",\n" +
                    "\t\"nog\": \"Nogai\",\n" +
                    "\t\"zxx\": \"No linguistic content\",\n" +
                    "\t\"nai\": \"North American Indian Language\",\n" +
                    "\t\"frr\": \"Northern Frisian\",\n" +
                    "\t\"se\": \"Northern Sami\",\n" +
                    "\t\"nso\": \"Northern Sotho\",\n" +
                    "\t\"nd\": \"North Ndebele\",\n" +
                    "\t\"no\": \"Norwegian\",\n" +
                    "\t\"nb\": \"Norwegian Bokmål\",\n" +
                    "\t\"nn\": \"Norwegian Nynorsk\",\n" +
                    "\t\"nub\": \"Nubian Language\",\n" +
                    "\t\"nym\": \"Nyamwezi\",\n" +
                    "\t\"ny\": \"Nyanja\",\n" +
                    "\t\"nyn\": \"Nyankole\",\n" +
                    "\t\"tog\": \"Nyasa Tonga\",\n" +
                    "\t\"nyo\": \"Nyoro\",\n" +
                    "\t\"nzi\": \"Nzima\",\n" +
                    "\t\"oc\": \"Occitan\",\n" +
                    "\t\"oj\": \"Ojibwa\",\n" +
                    "\t\"ang\": \"Old English\",\n" +
                    "\t\"fro\": \"Old French\",\n" +
                    "\t\"goh\": \"Old High German\",\n" +
                    "\t\"sga\": \"Old Irish\",\n" +
                    "\t\"non\": \"Old Norse\",\n" +
                    "\t\"peo\": \"Old Persian\",\n" +
                    "\t\"pro\": \"Old Provençal\",\n" +
                    "\t\"or\": \"Oriya\",\n" +
                    "\t\"om\": \"Oromo\",\n" +
                    "\t\"osa\": \"Osage\",\n" +
                    "\t\"os\": \"Ossetic\",\n" +
                    "\t\"oto\": \"Otomian Language\",\n" +
                    "\t\"ota\": \"Ottoman Turkish\",\n" +
                    "\t\"pal\": \"Pahlavi\",\n" +
                    "\t\"pau\": \"Palauan\",\n" +
                    "\t\"pi\": \"Pali\",\n" +
                    "\t\"pam\": \"Pampanga\",\n" +
                    "\t\"pag\": \"Pangasinan\",\n" +
                    "\t\"pap\": \"Papiamento\",\n" +
                    "\t\"paa\": \"Papuan Language\",\n" +
                    "\t\"ps\": \"Pashto\",\n" +
                    "\t\"fa\": \"Persian\",\n" +
                    "\t\"phi\": \"Philippine Language\",\n" +
                    "\t\"phn\": \"Phoenician\",\n" +
                    "\t\"pon\": \"Pohnpeian\",\n" +
                    "\t\"pl\": \"Polish\",\n" +
                    "\t\"pt\": \"Portuguese\",\n" +
                    "\t\"cpp\": \"Portuguese-based Creole or Pidgin\",\n" +
                    "\t\"pra\": \"Prakrit Language\",\n" +
                    "\t\"pa\": \"Punjabi\",\n" +
                    "\t\"qu\": \"Quechua\",\n" +
                    "\t\"raj\": \"Rajasthani\",\n" +
                    "\t\"rap\": \"Rapanui\",\n" +
                    "\t\"rar\": \"Rarotongan\",\n" +
                    "\t\"rm\": \"Rhaeto-Romance\",\n" +
                    "\t\"roa\": \"Romance Language\",\n" +
                    "\t\"ro\": \"Romanian\",\n" +
                    "\t\"rom\": \"Romany\",\n" +
                    "\t\"root\": \"Root\",\n" +
                    "\t\"rn\": \"Rundi\",\n" +
                    "\t\"ru\": \"Russian\",\n" +
                    "\t\"sal\": \"Salishan Language\",\n" +
                    "\t\"sam\": \"Samaritan Aramaic\",\n" +
                    "\t\"smi\": \"Sami Language\",\n" +
                    "\t\"sm\": \"Samoan\",\n" +
                    "\t\"sad\": \"Sandawe\",\n" +
                    "\t\"sg\": \"Sango\",\n" +
                    "\t\"sa\": \"Sanskrit\",\n" +
                    "\t\"sat\": \"Santali\",\n" +
                    "\t\"sc\": \"Sardinian\",\n" +
                    "\t\"sas\": \"Sasak\",\n" +
                    "\t\"sco\": \"Scots\",\n" +
                    "\t\"gd\": \"Scottish Gaelic\",\n" +
                    "\t\"sel\": \"Selkup\",\n" +
                    "\t\"sem\": \"Semitic Language\",\n" +
                    "\t\"sr\": \"Serbian\",\n" +
                    "\t\"sh\": \"Serbo-Croatian\",\n" +
                    "\t\"srr\": \"Serer\",\n" +
                    "\t\"shn\": \"Shan\",\n" +
                    "\t\"sn\": \"Shona\",\n" +
                    "\t\"ii\": \"Sichuan Yi\",\n" +
                    "\t\"scn\": \"Sicilian\",\n" +
                    "\t\"sid\": \"Sidamo\",\n" +
                    "\t\"sgn\": \"Sign Language\",\n" +
                    "\t\"bla\": \"Siksika\",\n" +
                    "\t\"zh_Hans\": \"Simplified Chinese\",\n" +
                    "\t\"sd\": \"Sindhi\",\n" +
                    "\t\"si\": \"Sinhala\",\n" +
                    "\t\"sit\": \"Sino-Tibetan Language\",\n" +
                    "\t\"sio\": \"Siouan Language\",\n" +
                    "\t\"sms\": \"Skolt Sami\",\n" +
                    "\t\"den\": \"Slave\",\n" +
                    "\t\"sla\": \"Slavic Language\",\n" +
                    "\t\"sk\": \"Slovak\",\n" +
                    "\t\"sl\": \"Slovenian\",\n" +
                    "\t\"sog\": \"Sogdien\",\n" +
                    "\t\"so\": \"Somali\",\n" +
                    "\t\"son\": \"Songhai\",\n" +
                    "\t\"snk\": \"Soninke\",\n" +
                    "\t\"wen\": \"Sorbian Language\",\n" +
                    "\t\"sai\": \"South American Indian Language\",\n" +
                    "\t\"alt\": \"Southern Altai\",\n" +
                    "\t\"sma\": \"Southern Sami\",\n" +
                    "\t\"st\": \"Southern Sotho\",\n" +
                    "\t\"nr\": \"South Ndebele\",\n" +
                    "\t\"es\": \"Spanish\",\n" +
                    "\t\"srn\": \"Sranan Tongo\",\n" +
                    "\t\"suk\": \"Sukuma\",\n" +
                    "\t\"sux\": \"Sumerian\",\n" +
                    "\t\"su\": \"Sundanese\",\n" +
                    "\t\"sus\": \"Susu\",\n" +
                    "\t\"sw\": \"Swahili\",\n" +
                    "\t\"ss\": \"Swati\",\n" +
                    "\t\"sv\": \"Swedish\",\n" +
                    "\t\"fr_CH\": \"Swiss French\",\n" +
                    "\t\"gsw\": \"Swiss German\",\n" +
                    "\t\"de_CH\": \"Swiss High German\",\n" +
                    "\t\"syr\": \"Syriac\",\n" +
                    "\t\"tl\": \"Tagalog\",\n" +
                    "\t\"ty\": \"Tahitian\",\n" +
                    "\t\"tai\": \"Tai Language\",\n" +
                    "\t\"tg\": \"Tajik\",\n" +
                    "\t\"tmh\": \"Tamashek\",\n" +
                    "\t\"ta\": \"Tamil\",\n" +
                    "\t\"trv\": \"Taroko\",\n" +
                    "\t\"tt\": \"Tatar\",\n" +
                    "\t\"te\": \"Telugu\",\n" +
                    "\t\"ter\": \"Tereno\",\n" +
                    "\t\"tet\": \"Tetum\",\n" +
                    "\t\"th\": \"Thai\",\n" +
                    "\t\"bo\": \"Tibetan\",\n" +
                    "\t\"tig\": \"Tigre\",\n" +
                    "\t\"ti\": \"Tigrinya\",\n" +
                    "\t\"tem\": \"Timne\",\n" +
                    "\t\"tiv\": \"Tiv\",\n" +
                    "\t\"tli\": \"Tlingit\",\n" +
                    "\t\"tkl\": \"Tokelau\",\n" +
                    "\t\"tpi\": \"Tok Pisin\",\n" +
                    "\t\"to\": \"Tonga\",\n" +
                    "\t\"zh_Hant\": \"Traditional Chinese\",\n" +
                    "\t\"tsi\": \"Tsimshian\",\n" +
                    "\t\"ts\": \"Tsonga\",\n" +
                    "\t\"tn\": \"Tswana\",\n" +
                    "\t\"tum\": \"Tumbuka\",\n" +
                    "\t\"tup\": \"Tupi Language\",\n" +
                    "\t\"tr\": \"Turkish\",\n" +
                    "\t\"tk\": \"Turkmen\",\n" +
                    "\t\"tvl\": \"Tuvalu\",\n" +
                    "\t\"tyv\": \"Tuvinian\",\n" +
                    "\t\"tw\": \"Twi\",\n" +
                    "\t\"kcg\": \"Tyap\",\n" +
                    "\t\"udm\": \"Udmurt\",\n" +
                    "\t\"uga\": \"Ugaritic\",\n" +
                    "\t\"ug\": \"Uighur\",\n" +
                    "\t\"uk\": \"Ukrainian\",\n" +
                    "\t\"umb\": \"Umbundu\",\n" +
                    "\t\"und\": \"Unknown or Invalid Language\",\n" +
                    "\t\"hsb\": \"Upper Sorbian\",\n" +
                    "\t\"ur\": \"Urdu\",\n" +
                    "\t\"en_US\": \"U.S. English\",\n" +
                    "\t\"uz\": \"Uzbek\",\n" +
                    "\t\"vai\": \"Vai\",\n" +
                    "\t\"ve\": \"Venda\",\n" +
                    "\t\"vi\": \"Vietnamese\",\n" +
                    "\t\"vo\": \"Volapük\",\n" +
                    "\t\"vot\": \"Votic\",\n" +
                    "\t\"wak\": \"Wakashan Language\",\n" +
                    "\t\"wal\": \"Walamo\",\n" +
                    "\t\"wa\": \"Walloon\",\n" +
                    "\t\"war\": \"Waray\",\n" +
                    "\t\"was\": \"Washo\",\n" +
                    "\t\"cy\": \"Welsh\",\n" +
                    "\t\"fy\": \"Western Frisian\",\n" +
                    "\t\"wo\": \"Wolof\",\n" +
                    "\t\"xh\": \"Xhosa\",\n" +
                    "\t\"sah\": \"Yakut\",\n" +
                    "\t\"yao\": \"Yao\",\n" +
                    "\t\"yap\": \"Yapese\",\n" +
                    "\t\"yi\": \"Yiddish\",\n" +
                    "\t\"yo\": \"Yoruba\",\n" +
                    "\t\"ypk\": \"Yupik Language\",\n" +
                    "\t\"znd\": \"Zande\",\n" +
                    "\t\"zap\": \"Zapotec\",\n" +
                    "\t\"zza\": \"Zaza\",\n" +
                    "\t\"zen\": \"Zenaga\",\n" +
                    "\t\"za\": \"Zhuang\",\n" +
                    "\t\"zu\": \"Zulu\",\n" +
                    "\t\"zun\": \"Zuni\"\n" +
                    "}");

            System.out.println("lang_obj--- " + lang_obj.toString());

            speak_lang = speak_lang.replaceAll("\\[", "").replaceAll("\\]", "");
            speak_lang = speak_lang.replace("\"", "");

            System.out.println("speak_lang--- " + speak_lang);

            String[] separated_lang = speak_lang.split(",");
            lang_val = "";

/*          System.out.println("separated_lang[0]--- " + separated_lang[0]);
            String result_word = lang_obj.getString("ab");
            System.out.println("result_word--- " + result_word);  */


            for (int i = 0; i < separated_lang.length; i++) {
                if (i == 0) {
                    System.out.println("Inner_obj--- " + separated_lang[i]);

                    if (lang_obj.has(separated_lang[i])) {
                        lang_val = lang_val + lang_obj.getString("" + separated_lang[i]);
                    }

                } else {
                    if (lang_obj.has(separated_lang[i])) {
                        lang_val = lang_val + ", " + lang_obj.getString("" + separated_lang[i]);
                    }
                }
            }

            System.out.println("lang_val-------------" + lang_val);
            tv_lang_name.setText(lang_val);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private class JSON_spec extends AsyncTask<String, Void, Boolean> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Boolean doInBackground(String... urls) {

            try {
                str_response = new JSONParser().getJSONString(urls[0]);
                System.out.println("str_response--------------" + str_response);
                speciality_list = str_response;
            } catch (Exception e) {
                e.printStackTrace();
            }

            return false;
        }

        protected void onPostExecute(Boolean result) {

            try {

                spec_val = "";

                spec_json = new JSONObject(str_response);
                System.out.println("One specialities_text-----------" + specialities_text);
                specialities_text = specialities_text.replaceAll("\\[", "").replaceAll("\\]", "");

                System.out.println("One specialities_text-----------" + specialities_text);

                String[] separated = specialities_text.split(",");

                for (int i = 0; i < separated.length; i++) {
                    if (i == 0) spec_val = spec_val + (spec_json).getString(separated[i]);
                    else spec_val = spec_val + ", " + (spec_json).getString(separated[i]);
                }

                tv_spec_name.setText(spec_val);
                System.out.println("spec_val-------------" + spec_val);

/*                specialities_text = specialities_text.replaceAll("\\[", "").replaceAll("\\]", "");
                String[] separated = specialities_text.split(",");

                for (int i = 0; i < separated.length; i++) {
                    if (i == 0) spec_val = spec_val + (Model.spec_json).getString(separated[i]);
                    else spec_val = spec_val + ", " + (Model.spec_json).getString(separated[i]);
                }

                tv_spec_name.setText(spec_val);
                System.out.println("spec_val-------------" + spec_val);*/


            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        if ((Model.data_src) != null && !(Model.data_src).isEmpty() && !(Model.data_src).equals("null") && !(Model.data_src).equals("")) {
            if ((Model.data_src).equals("speciality")) {
                if ((Model.selected_spec) != null && !(Model.selected_spec).isEmpty() && !(Model.selected_spec).equals("null") && !(Model.selected_spec).equals("")) {
                    tv_spec_name.setText(Model.selected_spec);
                }
            } else if ((Model.data_src).equals("language")) {
                if ((Model.selected_spec) != null && !(Model.selected_spec).isEmpty() && !(Model.selected_spec).equals("null") && !(Model.selected_spec).equals("")) {
                    tv_lang_name.setText(Model.selected_spec);
                }
            }
        }
    }


}
