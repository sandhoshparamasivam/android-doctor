package com.orane.docassist.New_Main;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Switch;
import android.widget.TextView;

import com.orane.docassist.InviteDoctorActivity;
import com.orane.docassist.Model.Item;
import com.orane.docassist.QasesActivity;
import com.orane.docassist.Qases_Post1;
import com.orane.docassist.R;
import com.orane.docassist.adapter.HotlinePatientsRowAdapter;

import org.json.JSONObject;

import java.util.List;


public class CasesFragment extends Fragment {

    public CasesFragment() {
        // Required empty public constructor
    }

    Toolbar toolbar;
    ListView listView;
    LinearLayout nolayout, netcheck_layout, hl_patients_layout;
    ScrollView first_layout;
    RelativeLayout LinearLayout1;
    String params;
    List<Item> arrayOfList;
    JSONObject jsonoj_status, json_disable;
    HotlinePatientsRowAdapter objAdapter;
    ProgressBar progressBar, progressBar_bottom;
    Switch switch_hl_enable;
    public String status_val;
    TextView tvhltit, tv_subtit, tv_myqases_title, tv_qases_list_desc, tv_qases_list, tv_myqases_desc, tv_post_title, tv_post_desc, tv_invite_title, tv_invite_desc;
    LinearLayout postqases_layout, myqases_layout, invite_layout, qases_list_layout;
    View rootView;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.qases_home, container, false);


        qases_list_layout = (LinearLayout) rootView.findViewById(R.id.qases_list_layout);
        myqases_layout = (LinearLayout) rootView.findViewById(R.id.myqases_layout);
        postqases_layout = (LinearLayout) rootView.findViewById(R.id.postqases_layout);
        invite_layout = (LinearLayout) rootView.findViewById(R.id.qinvite_layout);
        tv_qases_list = (TextView) rootView.findViewById(R.id.tv_qases_list);
        tv_qases_list_desc = (TextView) rootView.findViewById(R.id.tv_qases_list_desc);
        tv_myqases_title = (TextView) rootView.findViewById(R.id.tv_myqases_title);
        tv_myqases_desc = (TextView) rootView.findViewById(R.id.tv_myqases_desc);
        tv_post_title = (TextView) rootView.findViewById(R.id.tv_post_title);
        tv_post_desc = (TextView) rootView.findViewById(R.id.tv_post_desc);
        tv_invite_title = (TextView) rootView.findViewById(R.id.tv_invite_title);
        tv_invite_desc = (TextView) rootView.findViewById(R.id.tv_invite_desc);
        tvhltit = (TextView) rootView.findViewById(R.id.tvhltit);
        tv_subtit = (TextView) rootView.findViewById(R.id.tv_subtit);

        qases_list_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), QasesActivity.class);
                intent.putExtra("qtype", "feeds");
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);
            }
        });

        myqases_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), QasesActivity.class);
                intent.putExtra("qtype", "myfeeds");
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);
            }
        });

        postqases_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), Qases_Post1.class);
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);
            }
        });

        invite_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), InviteDoctorActivity.class);
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);
            }
        });


        return rootView;
    }


}
