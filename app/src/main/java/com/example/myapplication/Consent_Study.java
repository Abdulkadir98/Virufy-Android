package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.virufy.R;

public class Consent_Study extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consent__study);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        /*Button btn = findViewById(R.id.getStarted);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Consent_Study.this, TermsAndServices.class));
            }
        });*/
    }
}