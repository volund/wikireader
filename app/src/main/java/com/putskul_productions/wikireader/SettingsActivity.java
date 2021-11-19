/*
Copyright 2021 Amos JOSHUA

Permission is hereby granted, free of charge, to any person obtaining
a copy of this software and associated documentation files (the
"Software"), to deal in the Software without restriction, including
without limitation the rights to use, copy, modify, merge, publish,
distribute, sublicense, and/or sell copies of the Software, and to
permit persons to whom the Software is furnished to do so, subject to
the following conditions:

The above copyright notice and this permission notice shall be
included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE
LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION
OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION
WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
*/
    
package com.putskul_productions.wikireader;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class SettingsActivity extends AppCompatActivity {
    Button mLicensesButton;
    Button mSitesButton;
    TextView mAboutText;

    /*
    final String aboutText = " __        _____ _  _____                   \n" +
            " \\ \\      / /_ _| |/ /_ _|                  \n" +
            "  \\ \\ /\\ / / | || ' / | |                   \n" +
            "   \\ V  V /  | || . \\ | |                   \n" +
            "    \\_/\\_/  |___|_|\\_\\___|____  _____ ____  \n" +
            "     |  _ \\| ____|  / \\  |  _ \\| ____|  _ \\ \n" +
            "     | |_) |  _|   / _ \\ | | | |  _| | |_) |\n" +
            "     |  _ <| |___ / ___ \\| |_| | |___|  _ < \n" +
            "     |_| \\_\\_____/_/   \\_\\____/|_____|_| \\_\\\n" +
            "                                            ";*/

    final String aboutText = ""+
            "       _    _  ____  _  _  ____       \n" +
            "      ( \\/\\/ )(_  _)( )/ )(_  _)      \n" +
            "       )    (  _)(_  )  (  _)(_       \n" +
            "      (__/\\__)(____)(_)\\_)(____)      \n" +
            " ____  ____    __    ____  ____  ____ \n" +
            "(  _ \\( ___)  /__\\  (  _ \\( ___)(  _ \\\n" +
            " )   / )__)  /(__)\\  )(_) ))__)  )   /\n" +
            "(_)\\_)(____)(__)(__)(____/(____)(_)\\_)\n" +
            "\n";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("Settings");

        final Context context = this;
        mLicensesButton = findViewById(R.id.licensesButton);
        mSitesButton = findViewById(R.id.languagesAndSites);
        mAboutText = findViewById(R.id.aboutText);
        mAboutText.setText(aboutText);
        mSitesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, ContentActivity.class);
                startActivity(intent);
            }
        });
        mLicensesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, LicensesActivity.class);
                startActivity(intent);
            }
        });
    }
}
