package app.virufy;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import app.virufy.R;

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