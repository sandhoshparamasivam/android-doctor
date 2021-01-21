package com.orane.docassist.New_Main;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.orane.docassist.AnsweredQueriesActivity;
import com.orane.docassist.Model.Model;
import com.orane.docassist.MyClinicActivity;
import com.orane.docassist.MyPatientActivity;
import com.orane.docassist.MyWalletActivity;
import com.orane.docassist.Patient_Profile;
import com.orane.docassist.Profile_Activity;
import com.orane.docassist.R;
import com.orane.docassist.SendApptHome;
import com.orane.docassist.SentMsgActivity;
import com.orane.docassist.Video_WebViewActivity;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;


public class MyAccountFragment extends Fragment {

    public MyAccountFragment() {
        // Required empty public constructor
    }

    public String status_val;
    TextView tv_doc_name, tv_subtit, tv_myqases_title, tv_qases_list_desc, tv_qases_list, tv_myqases_desc, tv_post_title, tv_post_desc, tv_invite_title, tv_invite_desc;
    LinearLayout postqases_layout, myqases_layout, invite_layout, qases_list_layout;
    View rootView;

    LinearLayout viewprofile_layout, sentappt_layout, sentmsg_layout, myvideos_layout, mypatients_layout, myqueries_layout, myclinics_layout, mywallet_layout;
    CircleImageView profile_image;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.myaccount_fragment, container, false);

        viewprofile_layout = (LinearLayout) rootView.findViewById(R.id.viewprofile_layout);
        mywallet_layout = (LinearLayout) rootView.findViewById(R.id.mywallet_layout);
        myclinics_layout = (LinearLayout) rootView.findViewById(R.id.myclinics_layout);
        myqueries_layout = (LinearLayout) rootView.findViewById(R.id.myqueries_layout);
        mypatients_layout = (LinearLayout) rootView.findViewById(R.id.mypatients_layout);
        myvideos_layout = (LinearLayout) rootView.findViewById(R.id.myvideos_layout);
        sentmsg_layout = (LinearLayout) rootView.findViewById(R.id.sentmsg_layout);
        sentappt_layout = (LinearLayout) rootView.findViewById(R.id.sentappt_layout);
        profile_image = (CircleImageView) rootView.findViewById(R.id.profile_image);

        tv_doc_name = (TextView) rootView.findViewById(R.id.tv_doc_name);

        tv_doc_name.setText(Model.name);

        System.out.println("Model.photo_url---------------" + Model.photo_url);
        Picasso.with(getActivity()).load(Model.photo_url).placeholder(R.mipmap.default_user).error(R.mipmap.default_user).into(profile_image);


        viewprofile_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), Profile_Activity.class);
                startActivity(i);
            }
        });

        mywallet_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), MyWalletActivity.class);
                startActivity(i);
            }
        });

        myclinics_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), MyClinicActivity.class);
                startActivity(i);
            }
        });
        myqueries_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(getActivity(), AnsweredQueriesActivity.class);
                i.putExtra("source", "Main");
                i.putExtra("pat_id", "0");
                startActivity(i);
            }
        });
        mypatients_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), MyPatientActivity.class);
                startActivity(i);
            }
        });
        myvideos_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), Video_WebViewActivity.class);
                startActivity(i);
            }
        });

        sentmsg_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), SentMsgActivity.class);
                startActivity(i);
            }
        });

        sentappt_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), SendApptHome.class);
                startActivity(i);
            }
        });

        return rootView;
    }


}
