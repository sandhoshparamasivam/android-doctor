package com.orane.docassist.Multiselect;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.util.SparseBooleanArray;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.orane.docassist.Model.Model;
import com.orane.docassist.R;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class ListViewExample extends AppCompatActivity {
    ArrayList<Team> myTeams;
    TeamListViewAdapter myAdapter;
    ListView myListView;
    String str_response, data_src, spec_val, spec_name, json_data_text;
    JSONObject spec_json;
    Map<String, String> spec_map = new HashMap<String, String>();
    Button myButton;
    Toolbar toolbar;
    TextView mTitle;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.multiselect_main);

        //------------------------------------------------------
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setTitle("");

            mTitle = (TextView) toolbar.findViewById(R.id.toolbar_title);
            Typeface khandBold = Typeface.createFromAsset(getApplicationContext().getAssets(), Model.font_name_bold);
            mTitle.setTypeface(khandBold);
        }
        //------------------------------------------------------

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.app_color));
        }


        myListView = (ListView) findViewById(R.id.myListView);
        myButton = (Button) findViewById(R.id.buttonStart);

        myTeams = new ArrayList<Team>();

/*        //---------------------- Reff Fee Get -----------------------------
        String spec_url = Model.BASE_URL + "sapp/getAllSpecialities";
        System.out.println("spec_url---------" + spec_url);
        new JSON_spec().execute(spec_url);
        //---------------------- Reff Fee Get -----------------------------*/

        try {
            Intent intent = getIntent();
            json_data_text = intent.getStringExtra("json_data");
            data_src = intent.getStringExtra("data_src");
            //----------------------------------------------------------------
            System.out.println("json_data_text------------" + json_data_text);
            System.out.println("data_src------------" + data_src);

            if (data_src.equals("speciality")) {
                mTitle.setText("Select specialities");
            } else if (data_src.equals("language")) {
                mTitle.setText("Select languages");
            }

            new JSON_spec().execute("");

        } catch (Exception ee) {
            ee.printStackTrace();
        }

/*        myTeams.add(new Team("Winners", 10));
        myTeams.add(new Team("Philidelphia Flyers", 5));
        myTeams.add(new Team("Detroit Red Wings", 1));*/

        /*myAdapter = new TeamListViewAdapter(this, R.layout.row_team_layout, myTeams);
        myListView.setAdapter(myAdapter);
        myListView.setItemsCanFocus(false);*/

        myButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ArrayList<Team> selectedTeams = new ArrayList<Team>();
                final SparseBooleanArray checkedItems = myListView.getCheckedItemPositions();
                int checkedItemsCount = checkedItems.size();
                for (int i = 0; i < checkedItemsCount; ++i) {
                    // Item position in adapter
                    int position = checkedItems.keyAt(i);
                    // Add team if item is checked == TRUE!
                    if (checkedItems.valueAt(i))
                        selectedTeams.add(myAdapter.getItem(position));
                }

                if (selectedTeams.size() < 1)
                    Toast.makeText(getBaseContext(), "Select atleast one Speciality", Toast.LENGTH_SHORT).show();
                else {

                    spec_val = "";
                    spec_name = "";

                    for (Team t : selectedTeams) {
                        // System.out.println("Items------------" + t.getTeamName());

                        //----------- Getting Values ------------------------------
                        if (spec_val.equals("")) {
                            spec_val = spec_map.get(t.getTeamName());
                        } else {
                            spec_val = spec_val + "," + spec_map.get(t.getTeamName());
                        }
                        //----------- Getting Values ------------------------------

                        //----------- Getting Names ------------------------------
                        if (spec_name.equals("")) {
                            spec_name = t.getTeamName();
                        } else {
                            spec_name = spec_name + "," + t.getTeamName();
                        }
                        //----------- Getting Names ------------------------------
                    }

                    System.out.println("spec_val------------" + spec_val);
                    Model.selected_spec_id = spec_val;

                    System.out.println("spec_name------------" + spec_name);
                    Model.selected_spec = spec_name;
                    Model.data_src = data_src;


                    //------------------ Getting IDs ----------------------------
                    if ((Model.data_src) != null && !(Model.data_src).isEmpty() && !(Model.data_src).equals("null") && !(Model.data_src).equals("")) {
                        if ((Model.data_src).equals("speciality")) {
                            if ((Model.selected_spec_id) != null && !(Model.selected_spec_id).isEmpty() && !(Model.selected_spec_id).equals("null") && !(Model.selected_spec_id).equals("")) {
                                Model.selected_spec_api = Model.selected_spec_id;
                            }
                        } else if ((Model.data_src).equals("language")) {
                            if ((Model.selected_spec_id) != null && !(Model.selected_spec_id).isEmpty() && !(Model.selected_spec_id).equals("null") && !(Model.selected_spec_id).equals("")) {
                                Model.selected_lang_api = Model.selected_spec_id;
                            }
                        }
                    }
                    //------------------ Getting IDs ----------------------------


                    finish();
                }
            }
        });
    }

    private class JSON_spec extends AsyncTask<String, Void, Boolean> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Boolean doInBackground(String... urls) {

            try {
/*                str_response = new JSONParser().getJSONString(urls[0]);
                System.out.println("str_response--------------" + str_response);*/

                str_response = json_data_text;
                System.out.println("Got json_data_text--------------" + json_data_text);
            } catch (Exception e) {
                e.printStackTrace();
            }

            return false;
        }

        protected void onPostExecute(Boolean result) {

            try {

                spec_json = new JSONObject(str_response);
                System.out.println("One spec_json-----------" + spec_json.toString());


                Integer i = 0;  //Initialize

                Iterator<String> iter = spec_json.keys();
                while (iter.hasNext()) {
                    String key = iter.next();
                    try {
                        Object value = spec_json.get(key);
                        //System.out.println("key: " + key + ", value-----------" + value);

                        myTeams.add(new Team(value.toString(), key));
                        //mylist.add(value.toString());
                        spec_map.put(value.toString(), key);
                        i++;

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                myAdapter = new TeamListViewAdapter(getApplicationContext(), R.layout.row_team_layout, myTeams);
                myListView.setAdapter(myAdapter);
                myListView.setItemsCanFocus(false);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //getMenuInflater().inflate(R.menu.ask_menu, menu);
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
}