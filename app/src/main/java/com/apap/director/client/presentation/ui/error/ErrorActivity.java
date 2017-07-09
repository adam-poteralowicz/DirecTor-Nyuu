package com.apap.director.client.presentation.ui.error;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

import com.apap.director.client.R;

import butterknife.BindView;

/**
 * Created by Adam Poterałowicz
 */

public class ErrorActivity extends Activity {
    private static final int LAYOUT_ID = R.layout.error_activity_layout;

    @BindView(R.id.errorActivity_textView)
    TextView errorTextView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(LAYOUT_ID);

        errorTextView.setText(getIntent().getStringExtra("error"));
    }
}
