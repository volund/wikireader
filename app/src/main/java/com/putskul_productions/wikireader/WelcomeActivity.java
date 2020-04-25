package com.putskul_productions.wikireader;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class WelcomeActivity extends AppCompatActivity {

    Button mSelectLanguagesButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        mSelectLanguagesButton = findViewById(R.id.selectLanguagesButton);
        final Context context = this;
        mSelectLanguagesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ContentActivity.class);
                startActivity(intent);
            }
        });
    }
}
