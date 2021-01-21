package com.orane.docassist;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

import com.orane.docassist.Model.Model;
import com.orane.docassist.R;

public class MessageReceivedActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.pushmsg_receive);

        TextView view = (TextView) findViewById(R.id.result);
        view.setText(Model.push_msg);
        super.onCreate(savedInstanceState);

    }
}