package com.putskul_productions.wikireader;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

public class SettingsActivity extends AppCompatActivity {

    Button mDictionaryButton;
    Button mSitesButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("Settings");

        final Context context = this;
        mSitesButton = (Button)findViewById(R.id.languagesAndSites);
        mSitesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, SitesActivity.class);
                startActivity(intent);
            }
        });

        mDictionaryButton = (Button)findViewById(R.id.dictionaryButton);
        mDictionaryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDictionaryLanguageDialog();
            }
        });

        updateDictionaryButtonText();
    }

    void updateDictionaryButtonText() {
        mDictionaryButton.setText("Dictionray: " + Settings.shared.dictionaryLanguage(this));
    }

    void showDictionaryLanguageDialog() {
        final String[] dictLangs = Settings.shared.languageMap.get(Settings.shared.sourceLanguage(this));
        final Context context = this;
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setItems(dictLangs, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String dictLang = dictLangs[which];
                Settings.shared.setDictionaryLanguage(context, dictLang);
                updateDictionaryButtonText();
            }
        });
        builder.show();
    }

}
