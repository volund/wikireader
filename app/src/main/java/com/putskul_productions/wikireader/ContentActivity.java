package com.putskul_productions.wikireader;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
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
        setContentView(R.layout.activity_sites);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mRecyclerView = (RecyclerView) findViewById(R.id.sitesRecyclerView);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(layoutManager);

        // specify an adapter (see also next example)
        List<Language> languages = Storage.shared.getLanguages(this);
        mAdapter = new ContentAdapter(languages, this);
        mRecyclerView.setAdapter(mAdapter);

        if (Storage.shared.enabledLanguages(this).size() == 0) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Welcome to Wikireader!\n\nTap the checkbox on the left for each language you wish to read, then select the dictionary for use with that language");
            builder.setPositiveButton("Ok", null);
            builder.show();
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // R.menu.mymenu is a reference to an xml file named mymenu.xml which should be inside your res/menu directory.
        // If you don't have res/menu, just create a directory named "menu" inside res
        getMenuInflater().inflate(R.menu.sites_menu, menu);
        addItemButton = menu.findItem(R.id.addButton);
        addItemButton.setVisible(false);
        return super.onCreateOptionsMenu(menu);
    }

    // handle button activities
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.addButton) {
            if (mAdapter.currentLanguage == null) {
                showAddLanguageDialog();
            }
            else {
                showAddSiteDialogs();
            }
        }
        else {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    void showAddLanguageDialog() {
        final Context context = this;
        final EditText input = new EditText(this);
        showPromptDialog("New language", input, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                final String label = input.getText().toString();
                if (!label.trim().equals("")) {
                    Language newLanguage = new Language(label);
                    Storage.shared.addLanguage(context, newLanguage);
                    refreshData();
                }
            }
        });
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

    public void onClick(final Site site) {
        final Context context = this;
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Really delete?");
        builder.setIcon(android.R.drawable.ic_dialog_alert);
        builder.setNegativeButton("Cancel", null);
        builder.show();
    }

    public void onToggleLanguageEnabled(Language language) {
        language.enabled = !language.enabled;
        Storage.shared.updateLanguage(this, language);
        if (language.enabled) {
            showDictionarySelectionDialog(language);
        }
        refreshData();
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
        Log.e("WIKIREADER", "found [" + language.dictionaries.size() + "] dictionaries");
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
    void showPromptDialog(String title, EditText input, DialogInterface.OnClickListener okListener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(title);
        builder.setView(input);
        builder.setPositiveButton("OK", okListener);
        builder.show();
    }

    @Override
    public void onDelete(final Language lang, final Site site) {
        final boolean deleteLanguage = site == null;
        String msg = deleteLanguage ? "Really delete '" + lang.label + "'? All sites for this language will be lost" : "Really delete '" + site.label + "'";

        final Context context = this;
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(msg);
        builder.setIcon(android.R.drawable.ic_dialog_alert);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                if (deleteLanguage) {
                    Storage.shared.removeLanguage(context, lang);
                }
                else {
                    lang.sites.remove(site);
                    Storage.shared.updateLanguage(context, lang);
                }
                refreshData();
            }
        });
        builder.setNegativeButton("Cancel", null);
        builder.show();
    }

    @Override
    public void onSelectionChanged(Language language) {
        if (language != null) {
            setTitle(language.label);
            addItemButton.setVisible(true);
        }
        else {
            setTitle("Content");
            addItemButton.setVisible(false);
        }
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
