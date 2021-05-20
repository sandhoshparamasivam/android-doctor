package com.orane.docassist.Model;


import android.net.Uri;

import com.google.firebase.analytics.FirebaseAnalytics;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;

public class Model {

    public static String app_slno = "168";
    public static String app_ver = "3.0.3";
    public static String app_rel = "19-May-2021";

    public static String BASE_URL = "https://www.icliniq.com/";
//    public static String BASE_URL = "https://pentest.icliniq.com/";
    //public static String BASE_URL = "https://www.production.icliniq.com/";
    //public static String BASE_URL = "http://192.168.1.102/projects/icliniq/web/index.php/";
    //public static String BASE_URL = "https://ecsdemo.icliniq.com/";
    //public static String BASE_URL = "http://3.83.253.45/index.php/";

    public static String font_name = "fonts/NotoSans-Regular.ttf";
    public static String font_name_bold = "fonts/NotoSans-Bold.ttf";

    public static String voxeet_cons_key = "OXEyM3VpZDZjYjVwdg==";
    public static String voxeet_cons_alias_name = "cGY2ZXM2M2ExaGZ1a3JkaWhzOGJzYmVybQ==";

    public static FirebaseAnalytics mFirebaseAnalytics;

    public static int update_alert_days = 2;
    public static Integer update_count;
    public static String doc_photo, prescription_added, doc_name, doc_sp, surveyFormUrl, str_response, short_url, country_code, doc_profile_url, selected_spec_api, selected_lang_api, speciality_list, home_ad_img_path, home_ad_flag, pat_country, query_reponse, webrtc_token, webrtc_session_id, hline_val, query_typed, qcount, ccount, bcount, attach_filename, attach_status, fule_full_path, attach_qid, local_file_url, attach_id, attach_file_url, first_query, token, query_launch, select_specname, select_spec_val, status_val, location, clinic_location, clinic_id, clinic_name, cons_timezone_val, cons_timezone_name, push_url, appt_id, meeting_id, cons_id, cons_view_type, chat_cur_fcode, fcode, txt, to_id, launch, wallet_bal, payout_bal;
    public static String profile_str, login_status, kmid, isValid, password, name, id, status, browser_country, email, fee_q, fee_consult, fee_q_inr, fee_consult_inr, currency_symbol, currency_label, have_free_credit;
    public static String has_free_follow, screen_status;
    public static String search_url, query, speciality, selected_spec, selected_spec_id, followcode, patient, askeddate, from;
    public static String device_token, data_src, education_response, qnew_det_qid;
    public static String answering_status;
    public static String[] itemstr;
    public static Integer attach_file_count;
    public static String patient_id, qid, prescribe_flag;
    public static String consid, chime_url, cnotes, photo_url, ans_cache;
    public static String push_msg, push_type, push_title, followup_code, from_name, push_cons_id, push_cons_from_name, push_cons_startdate, push_cons_tz;
    public static String opt_freefollow, enable_freefollow, answer_txt = "";
    public static String is_priority_text;
    public static String new_education, share_link;
    public static String cliniq_id, doc_sign_url, clinic_title, clinic_street, clinic_placeid;
    public static String like_pulse;
    public static String sensappt_home_flag;
    public static String chat_cur_followupcode;
    public static String upload_files, check_enable_ffollowup, enable_freefollow_parameter;
    public static Uri signature_uri;
    public static JSONArray queryAnsweredArray, json_dugs_array;
    public static JSONObject wallettran, spec_json, json_dugs;

    public static HashMap<String, String> attach_files_map;

    public static final String FLURRY_APIKEY = "XC4CBRTJVK8PZSB3G2W4";
    //public static final String FLURRY_APIKEY = "YN7Q8SH7MVPJTQC4V76X1_Sample";

}
