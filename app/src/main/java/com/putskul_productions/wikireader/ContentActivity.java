package com.putskul_productions.wikireader;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.LinearLayout;

import java.util.List;


public class ContentActivity extends AppCompatActivity implements ContentAdapter.OnClickSiteListener {
    RecyclerView mRecyclerView;
    ContentAdapter mAdapter;
    MenuItem addItemButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Content");
        setContentView(R.layout.content_activity);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mRecyclerView = (RecyclerView) findViewById(R.id.sitesRecyclerView);
        mRecyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(layoutManager);

        List<Language> languages = Storage.shared.getLanguages(this);
        mAdapter = new ContentAdapter(languages, this);
        mRecyclerView.setAdapter(mAdapter);

        if (!Storage.shared.hasEnabledLanguages(this)) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Welcome to Wikireader!\n\nTap the checkbox on the left for each language you wish to read, then select the dictionary for use with that language");
            builder.setPositiveButton("Ok", null);
            builder.show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.sites_menu, menu);
        addItemButton = menu.findItem(R.id.addButton);
        addItemButton.setVisible(false);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.addButton) {
            showAddSiteDialogs();
        }
        else {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    void showAddSiteDialogs() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("New site");

        final Context context = this;

        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        final EditText labelInput = new EditText(this);
        final EditText addressInput = new EditText(this);
        labelInput.setHint("Label");
        addressInput.setHint("Address");
        layout.addView(labelInput);
        layout.addView(addressInput);

        builder.setView(layout);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String label = labelInput.getText().toString().trim();
                String address = addressInput.getText().toString().trim();

                if (!address.startsWith("http")) {
                    address = "http://" + address;
                }
                if (!label.trim().equals("")) {
                    Site newSite = new Site(label, address);
                    mAdapter.currentLanguage.sites.add(newSite);
                    Storage.shared.updateLanguage(context, mAdapter.currentLanguage);
                    refreshData();
                }
            }
        });
        builder.setNegativeButton("Cancel", null);
        builder.show();

    }

    public void onToggleLanguageEnabled(Language language) {
        language.enabled = !language.enabled;
        Storage.shared.updateLanguage(this, language);
        if (language.enabled) {
            showDictionarySelectionDialog(language);
        }
        else if (language.sites.contains(Settings.shared.getCurrentSite(this))) {
            Settings.shared.setCurrentSite(this, Site.BlankSite);
        }
    }

    void refreshData() {
        List<Language> languages = Storage.shared.getLanguages(this);
        mAdapter.updateData(languages);
    }

    void showDictionarySelectionDialog(final Language language) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Select dictionary (" + language.label + ")");
        final Context context = this;
        String[] dictionaries = new String[language.dictionaries.size()];
        int i = 0;
        for (Dictionary dict : language.dictionaries) {
            dictionaries[i] = dict.name;
            i += 1;
        }
        builder.setItems(dictionaries, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                language.currentDictionary = language.dictionaries.get(i);
                Storage.shared.updateLanguage(context, language);
                refreshData();
            }
        });
        builder.show();
    }

    @Override
    public void onDelete(final Language lang, final Site site) {
        final Context context = this;
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Really delete '" + site.label + "'");
        builder.setIcon(android.R.drawable.ic_dialog_alert);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                lang.sites.remove(site);
                Storage.shared.updateLanguage(context, lang);
                refreshData();
            }
        });
        builder.setNegativeButton("Cancel", null);
        builder.show();
    }

    @Override
    public void onSelectionChanged(Language language) {
        setTitle(language != null ? language.label : "Content");
        addItemButton.setVisible(language != null);
    }


    @Override
    public void onBackPressed() {
        if (mAdapter.currentLanguage != null) {
            mAdapter.currentLanguage = null;
            onSelectionChanged(null);
            refreshData();
        }
        else {
            finish();
        }
    }
}
