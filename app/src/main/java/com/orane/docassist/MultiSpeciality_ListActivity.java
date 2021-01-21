package com.orane.docassist;

import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.orane.docassist.Model.Model;
import com.orane.docassist.Network.JSONParser;
import com.orane.docassist.R;
import com.orane.docassist.adapter.CustomAdapter;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class MultiSpeciality_ListActivity extends AppCompatActivity {

    Map<String, String> spec_map = new HashMap<String, String>();
    public String params, spec_val, str_response;
    Toolbar toolbar;
    TextView toolbar_menu;
    private ListView listView;
    JSONObject spec_json;
    private Button button;
    List<String> categories;
    CustomAdapter adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.multi_speciality_list);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //---------------------------------------------------------------------
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setTitle(null);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.app_color2));
        }
        //------------------------------------------------------------------------------

        listView = (ListView) findViewById(R.id.list);
        button = (Button) findViewById(R.id.button);
        listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);

        //---------------------- Reff Fee Get -----------------------------
        String spec_url = Model.BASE_URL + "sapp/getAllSpecialities?os_type=android";
        System.out.println("spec_url---------" + spec_url);
        new JSON_spec().execute(spec_url);
        //---------------------- Reff Fee Get -----------------------------

/*        String[] fruit = getResources().getStringArray(R.array.fruit_array);
        CustomAdapter adapter = new CustomAdapter(this, fruit);
        listView.setAdapter(adapter);*/

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<String> selected = getSelectedItems();
                String logString = "Selected items: " + TextUtils.join(", ", selected);

                Log.d("MainActivity", logString);
                Toast.makeText(MultiSpeciality_ListActivity.this, logString, Toast.LENGTH_SHORT).show();
            }
        });
    }


    private List<String> getSelectedItems() {

        List<String> result = new ArrayList<>();

        SparseBooleanArray checkedItems = listView.getCheckedItemPositions();

        for (int i = 0; i < listView.getCount(); ++i) {
            if (checkedItems.valueAt(i)) {
                result.add((String) listView.getItemAtPosition(checkedItems.keyAt(i)));
            }
        }


        return result;
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
            } catch (Exception e) {
                e.printStackTrace();
            }

            return false;
        }

        protected void onPostExecute(Boolean result) {

            try {

                spec_val = "";
                spec_json = new JSONObject(str_response);
                System.out.println("One spec_json-----------" + spec_json.toString());

                ArrayList<String> mylist = new ArrayList<String>();

                Integer i = 0;  //Initialize

                Iterator<String> iter = spec_json.keys();
                while (iter.hasNext()) {
                    String key = iter.next();
                    try {
                        Object value = spec_json.get(key);
                        System.out.println("key: " + key + ", value-----------" + value);

                        mylist.add(value.toString());
                        spec_map.put(value.toString(), key);
                        i++;

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                adapter = new CustomAdapter(getApplicationContext(), mylist);
                listView.setAdapter(adapter);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onBackPressed() {
        Model.query_launch = "";
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        //getMenuInflater().inflate(R.menu.spec_menu, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == android.R.id.home) {
            Model.query_launch = "";
            finish();
            return true;
        }

      /*  if (id == R.id.nav_cfilter) {
            spec_val = "0";
            Model.select_spec_val = "0";
            Model.query_launch = "SpecialityListActivity";
            finish();

            return true;
        }*/


        return true;
    }


}
