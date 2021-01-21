package com.orane.docassist.Network;

import android.util.Log;

import com.orane.docassist.Model.Model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class JSONParser {

    static InputStream is = null;
    static JSONObject jObj = null;
    static JSONArray jarray = null;
    static String json = "";
    static JSONArray JArr = null;
    final String TAG = "JsonParser.java";
    public StringBuilder total;
    public String sub_url;
    String contentAsString;
    long startTime;
    URL url2;


    public JSONObject JSON_POST(JSONObject jsonobj, String post_flag) throws IOException {

        if (post_flag.equals("confirm_password")) {
            sub_url = "/app/updateNewPassword?os_type=android&user_id=" + Model.id + "&token=" + Model.token;
        } else if (post_flag.equals("getforgotpwd_requet")) {
            sub_url = "/app/getForgotPassPin?os_type=android&user_id=" + Model.id + "&token=" + Model.token;
        } else if (post_flag.equals("feedback")) {
            sub_url = "/sapp/feedback?os_type=android&user_id=" + Model.id + "&token=" + Model.token;
        } else if (post_flag.equals("reply_to_feedback")) {
            sub_url = "/sapp/docReply2Feedback?os_type=android&user_id=" + Model.id + "&token=" + Model.token;
        } else if (post_flag.equals("sendTextAns")) {
            sub_url = "query/sendTextAns?os_type=android&user_id=" + Model.id + "&token=" + Model.token;
        } else if (post_flag.equals("submitAnswer")) {
            sub_url = "/sapp/submitAnswer?os_type=android&user_id=" + Model.id + "&token=" + Model.token;
        } else if (post_flag.equals("mobileWallet")) {
            sub_url = "/sapp/mobileWallet?os_type=android&user_id=" + Model.id + "&token=" + Model.token;
        } else if (post_flag.equals("saveqanswer")) {
            sub_url = "/sapp/saveqanswer?os_type=android&user_id=" + Model.id + "&token=" + Model.token;
        } else if (post_flag.equals("dialappt")) {
            sub_url = "/sapp/dialappt?os_type=android&user_id=" + Model.id + "&token=" + Model.token;
        } else if (post_flag.equals("hostClickVideo")) {
            sub_url = "/sapp/hostClickVideo?os_type=android&user_id=" + Model.id + "&token=" + Model.token;
        } else if (post_flag.equals("sendSlot")) {
            sub_url = "sapp/sendSlot?os_type=android&user_id=" + Model.id + "&token=" + Model.token;
        } else if (post_flag.equals("sendEmail")) {
            sub_url = "/sapp/sendEmail?os_type=android&user_id=" + Model.id + "&token=" + Model.token;
        } else if (post_flag.equals("updateHlinePlans")) {
            sub_url = "sapp/updateHlinePlans?os_type=android&user_id=" + Model.id + "&token=" + Model.token;
        } else if (post_flag.equals("loginSubmit")) {
            sub_url = "/mobileajax/loginSubmit?os_type=android";
        } else if (post_flag.equals("addPatient")) {
            sub_url = "/sapp/addPatient?os_type=android&user_id=" + Model.id + "&token=" + Model.token;
        } else if (post_flag.equals("addClinic")) {
            sub_url = "/sapp/addClinic?os_type=android&user_id=" + Model.id + "&token=" + Model.token;
        } else if (post_flag.equals("writenotes")) {
            sub_url = "/sapp/addConsultNote?os_type=android&user_id=" + Model.id + "&token=" + Model.token;
        } else if (post_flag.equals("comments")) {
            sub_url = "/sapp/caseComment?oos_type=android&user_id=" + Model.id + "&token=" + Model.token;
        } else if (post_flag.equals("PostQase")) {
            sub_url = "/sapp/caseAdd?os_type=android&user_id=" + Model.id + "&token=" + Model.token;
        } else if (post_flag.equals("PostQaseFinal")) {
            sub_url = "/sapp/caseActivate?os_type=android&user_id=" + Model.id + "&token=" + Model.token;
        } else if (post_flag.equals("submit_qases")) {
            sub_url = "/sapp/caseActivate?os_type=android&user_id=" + Model.id + "&token=" + Model.token;
        } else if (post_flag.equals("upvote")) {
            sub_url = "/sapp/vote?os_type=android&user_id=" + Model.id + "&token=" + Model.token;
        } else if (post_flag.equals("best")) {
            sub_url = "/sapp/voteBestComment?os_type=android&user_id=" + Model.id + "&token=" + Model.token;
        } else if (post_flag.equals("notes_entry")) {
            sub_url = "/sapp/addPatientNote?os_type=android&user_id=" + Model.id + "&token=" + Model.token;
        } else if (post_flag.equals("notes_view")) {
            sub_url = "/sapp/viewPatientNotes?os_type=android&user_id=" + Model.id + "&token=" + Model.token;
        } else if (post_flag.equals("update_notes")) {
            sub_url = "/sapp/updatePatientNote?os_type=android&user_id=" + Model.id + "&token=" + Model.token;
        } else if (post_flag.equals("delete_notes")) {
            sub_url = "/sapp/deletePatientNote?os_type=android&user_id=" + Model.id + "&token=" + Model.token;
        } else if (post_flag.equals("confirm_booking")) {
            sub_url = "/sapp/consultationConfirmBooking?os_type=android&user_id=" + Model.id + "&token=" + Model.token;
        } else if (post_flag.equals("mypatsearch")) {
            sub_url = "/sapp/patientSearch?os_type=android&user_id=" + Model.id + "&token=" + Model.token;
        } else if (post_flag.equals("SubmitFollowup")) {
            sub_url = "/sapp/followMessage2Pat?os_type=android&user_id=" + Model.id + "&token=" + Model.token;
        } else if (post_flag.equals("stage1")) {
            sub_url = "/sapp/updateDoctorProfileDet?stage=1&os_type=android&user_id=" + Model.id + "&token=" + Model.token;
        } else if (post_flag.equals("academic")) {
            sub_url = "/sapp/updateDoctorProfileDet?user_id=" + Model.id + "&token=" + Model.token + "&os_type=android&stage=2";
        } else if (post_flag.equals("credential_answer")) {
            sub_url = "/sapp/credential_answer?os_type=android&user_id=" + Model.id + "&token=" + Model.token;
        } else if (post_flag.equals("cred_submitAnswer")) {
            sub_url = "/sapp/credSubmitAnswer?os_type=android&user_id=" + Model.id + "&token=" + Model.token;
        } else if (post_flag.equals("writePrescription")) {
            sub_url = "/sapp/writePrescription?os_type=android&user_id=" + Model.id + "&token=" + Model.token;
        } else if (post_flag.equals("writeDrug")) {
            sub_url = "/sapp/addPrescription?os_type=android&user_id=" + Model.id + "&token=" + Model.token;
        } else if (post_flag.equals("writePresNotes")) {
            sub_url = "/sapp/addPrescription?os_type=android&user_id=" + Model.id + "&token=" + Model.token;
        } else if (post_flag.equals("video_desc_entry")) {
            sub_url = "/sapp/insertOrUpdateVideoDet?os_type=android&user_id=" + Model.id + "&token=" + Model.token;
        } else if (post_flag.equals("check_uname")) {
            sub_url = "mobileajax/verifyUsername?os_type=android&user_id=" + Model.id + "&token=" + Model.token;
        } else if (post_flag.equals("validatemobnoexists")) {
            sub_url = "mobileajax/validateMobileNoEmail?user_id=" + Model.id + "&token=" + Model.token + "&os_type=android";
        } else if (post_flag.equals("signupDoc_Update_Mobno")) {
            sub_url = "sapp/changeMobileOrEmail?os_type=android&user_id=" + Model.id + "&token=" + Model.token + "&t=mob";
        } else if (post_flag.equals("signupDoc_Update_email")) {
            sub_url = "sapp/changeMobileOrEmail?os_type=android&user_id=" + Model.id + "&token=" + Model.token + "&t=email";
        } else if (post_flag.equals("resend_OTP_Mob")) {
            sub_url = "sapp/resendMobileAndEmailOtp?os_type=android&user_id=" + Model.id + "&token=" + Model.token + "&t=mob";
        } else if (post_flag.equals("resend_OTP_email")) {
            sub_url = "sapp/resendMobileAndEmailOtp?os_type=android&user_id=" + Model.id + "&token=" + Model.token + "&t=email";
        } else if (post_flag.equals("signupDoc_verify_Mobno")) {
            sub_url = "sapp/verifyMobileAndEmail?os_type=android&user_id=" + Model.id + "&token=" + Model.token + "&t=mob";
        } else if (post_flag.equals("signupDoc_verify_email")) {
            sub_url = "sapp/verifyMobileAndEmail?os_type=android&user_id=" + Model.id + "&token=" + Model.token + "&t=email";
        } else if (post_flag.equals("post_acedemic")) {
            sub_url = "sapp/insertOrUpdateDocEducation?os_type=android&user_id=" + Model.id + "&token=" + Model.token + "&isNew=1";
        } else if (post_flag.equals("signup1")) {
            sub_url = "mobileajax/docSignup4PersonalDet?os_type=android";
        } else if (post_flag.equals("signup2")) {
            sub_url = "sapp/docSignup4AcademicDet?os_type=android&user_id=" + Model.id + "&token=" + Model.token;
        } else if (post_flag.equals("proffessional")) {
            sub_url = "sapp/updateDoctorProfileDet?stage=3&os_type=android&user_id=" + Model.id + "&token=" + Model.token;
        } else if (post_flag.equals("POD_submitAnswer")) {
            sub_url = "/sapp/submitAnswerPod?os_type=android&user_id=" + Model.id + "&token=" + Model.token;
        } else if (post_flag.equals("newsignupDocStep3")) {
            sub_url = "/sapp/docSignup4ProfessionalDet?os_type=android&user_id=" + Model.id + "&token=" + Model.token;
        } else if (post_flag.equals("signupDocStep1")) {
            sub_url = "/mobileajax/signupDocStep1";
        } else if (post_flag.equals("signupDocStep2")) {
            sub_url = "/mobileajax/signupDocStep2";
        } else if (post_flag.equals("signupDocStep3")) {
            sub_url = "/mobileajax/signupDocStep3";
        } else if (post_flag.equals("personal")) {
            sub_url = "/sapp/updateDoctorProfile?updFlagg=personal&os_type=android&user_id=" + Model.id + "&token=" + Model.token;
        } else if (post_flag.equals("professional")) {
            sub_url = "/sapp/updateDoctorProfile?updFlagg=professional&os_type=android&user_id=" + Model.id + "&token=" + Model.token;
        } else if (post_flag.equals("validate_username")) {
            sub_url = "mobileajax/verifyUsername?os_type=android&user_id=" + Model.id + "&token=" + Model.token;
        } else if (post_flag.equals("validate_email_mobno")) {
            sub_url = "mobileajax/validateMobileNoEmail?os_type=android";
        } else if (post_flag.equals("logout")) {
            sub_url = "sapp/appLogout?os_type=android&user_id=" + Model.id + "&token=" + Model.token;
        } else if (post_flag.equals("notes_save")) {
            sub_url = "sapp/writeConsultationReview?user_id=" + Model.id + "&token=" + Model.token;
        }


        InputStream is = null;

        try {

            url2 = new URL(Model.BASE_URL + sub_url);
            System.out.println("url2---" + url2);
            HttpURLConnection conn = (HttpURLConnection) url2.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setDoInput(true);
            DataOutputStream wr = new DataOutputStream(conn.getOutputStream());
            wr.writeBytes(jsonobj.toString());
            wr.flush();
            wr.close();
            conn.connect();

            int response = conn.getResponseCode();
            is = conn.getInputStream();
            contentAsString = convertInputStreamToString(is);
            System.out.println("contentAsString----" + contentAsString);

            try {
                jObj = new JSONObject(contentAsString.trim());
                System.out.println("jObj----" + jObj.toString());
            } catch (Throwable t) {
                t.printStackTrace();
            }

            return jObj;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }


    public String JSON_POST_ARRAY(JSONObject jsonobj, String post_flag) throws IOException {

        if (post_flag.equals("notes_view")) {
            sub_url = "/sapp/viewPatientNotes?token=" + Model.token;
        } else if (post_flag.equals("mypatsearch")) {
            sub_url = "/sapp/patientSearch?token=" + Model.token;
        } else if (post_flag.equals("pod_list")) {
            sub_url = "/sapp/qinboxDocPod?user_id=" + Model.id + "&token=" + Model.token;
        }

        InputStream is = null;

        try {

            url2 = new URL(Model.BASE_URL + sub_url);
            System.out.println("url2---" + url2);
            HttpURLConnection conn = (HttpURLConnection) url2.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setDoInput(true);
            DataOutputStream wr = new DataOutputStream(conn.getOutputStream());
            wr.writeBytes(jsonobj.toString());
            wr.flush();
            wr.close();
            conn.connect();

            int response = conn.getResponseCode();
            System.out.println("response----- " + response);

            is = conn.getInputStream();
            contentAsString = convertInputStreamToString(is);

         /*   try {
                jarray = new JSONArray(contentAsString.trim());
                System.out.println("jarray----" + jarray.toString());
            } catch (Throwable t) {
                t.printStackTrace();
            }
*/
            return contentAsString;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }


    public JSONObject getJSONFromUrl(String url) throws IOException {

        InputStream is = null;

        try {
            URL url2 = new URL(url);
            HttpURLConnection conn = (HttpURLConnection) url2.openConnection();
            conn.setRequestMethod("POST");
            conn.setDoInput(true);
            conn.connect();
            int response = conn.getResponseCode();

            System.out.println("response-------" + response);

            is = conn.getInputStream();
            String contentAsString = convertInputStreamToString(is);

            System.out.println("contentAsString-------" + contentAsString);

            try {
                jObj = new JSONObject(contentAsString.trim());
                System.out.println("jObj-------" + jObj.toString());
            } catch (JSONException e) {
                Log.e(TAG, "Error parsing data " + e.toString());
            }
            return jObj;

        } finally {
            if (is != null) {
                is.close();
            }
        }
    }


    public JSONObject getJSON_URL(String url) throws IOException {

        InputStream is = null;
        try {
            URL url2 = new URL(url);
            HttpURLConnection conn = (HttpURLConnection) url2.openConnection();
            conn.setRequestMethod("GET");
            conn.setDoInput(true);
            conn.connect();

            int response = conn.getResponseCode();
            is = conn.getInputStream();
            contentAsString = convertInputStreamToString(is);

            System.out.println("JSON response----" + response);

            try {
                jObj = new JSONObject(contentAsString.trim());
                System.out.println("jObj----" + jObj.toString());
                Model.wallettran = jObj;
            } catch (Throwable t) {
                t.printStackTrace();
            }

            return jObj;


        } finally {
            if (is != null) {
                is.close();
            }
        }
    }


    public String convertInputStreamToString(InputStream stream) throws IOException {

        try {
            BufferedReader r = new BufferedReader(new InputStreamReader(stream));
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


    public String getJSONString(String url) throws IOException {

        InputStream is = null;

        try {
            URL url2 = new URL(url);
            HttpURLConnection conn = (HttpURLConnection) url2.openConnection();
            conn.setRequestMethod("GET");
            conn.setDoInput(true);
            conn.connect();
            int response = conn.getResponseCode();
            System.out.println("response-------" + response);

            is = conn.getInputStream();
            contentAsString = convertInputStreamToString(is);
            System.out.println("contentAsString-------" + contentAsString);

            return contentAsString;

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (is != null) {
                is.close();
            }
        }
        return null;
    }
}