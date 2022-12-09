package com.thanhnguyen.smartorder.FragmentApp;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.thanhnguyen.smartorder.R;

public class noti extends AppCompatActivity {

    TextView notii;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.noti);
        notii= findViewById(R.id.notii);
        String message= getIntent().getStringExtra("message");
        notii.setText(message);

    }
}
