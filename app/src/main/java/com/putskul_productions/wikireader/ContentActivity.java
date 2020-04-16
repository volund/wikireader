package com.putskul_productions.wikireader;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;

import java.util.List;


public class ContentActivity extends AppCompatActivity implements ContentAdapter.OnClickContentListener {
    RecyclerView mRecyclerView;
    ContentAdapter mAdapter;
    MenuItem addItemButton;
    MenuItem helpItemButton;
    Dialogs dialogs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Content");
        setContentView(R.layout.content_activity);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        dialogs = new Dialogs(this);
        setUpRecyclerView();
        showGreetingIfNecessary();
    }

    void setUpRecyclerView() {
        List<Language> languages = App.shared.model.getLanguages(this);
        mAdapter = new ContentAdapter(languages, this);
        mRecyclerView = findViewById(R.id.sitesRecyclerView);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.sites_menu, menu);
        helpItemButton = menu.findItem(R.id.helpButton);
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
        if (id == R.id.helpButton) {
            showGreeting();
        }
        else {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    void showAddSiteDialogs() {
        final Context context = this;
        dialogs.showDoubleInputDialog("New Site", "Label", "Address", new Dialogs.DoubleInputListener(){
            @Override
            public void onOk(String address, String label) {
                address = address.trim();
                if (!address.startsWith("http")) {
                    address = "http://" + address;
                }
                if (!label.trim().equals("")) {
                    Site newSite = new Site(label.trim(), address);
                    mAdapter.currentLanguage.sites.add(newSite);
                    App.shared.model.updateLanguage(context, mAdapter.currentLanguage);
                    refreshData();
                }
            }
        });
    }

    public void onToggleLanguageEnabled(Language language) {
        language.enabled = !language.enabled;
        App.shared.model.updateLanguage(this, language);
        if (language.enabled) {
            showDictionarySelectionDialog(language);
        }
        else if (language.sites.contains(App.shared.settings.getCurrentSite(this))) {
            App.shared.settings.setCurrentSite(this, Site.BlankSite);
        }
    }

    void refreshData() {
        List<Language> languages = App.shared.model.getLanguages(this);
        mAdapter.updateData(languages);
    }

    void showDictionarySelectionDialog(final Language language) {
        final Context context = this;
        String title = "Select dictionary (" + language.label + ")";
        dialogs.showListSelection(title, language.dictionariesStringArray(), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                language.currentDictionary = language.dictionaries.get(which);
                App.shared.model.updateLanguage(context, language);
                refreshData();
            }
        });
    }

    @Override
    public void onDelete(final Language language, final Site site) {
        final Context context = this;
        int icon = android.R.drawable.ic_dialog_alert;
        dialogs.showOkCancelDialog("Really delete '" + site.label + "'", icon, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                language.sites.remove(site);
                App.shared.model.updateLanguage(context, language);
                refreshData();
            }
        });
    }

    @Override
    public void onSelectionChanged(Language language) {
        setTitle(language != null ? language.label : "Content");
        addItemButton.setVisible(language != null);
        helpItemButton.setVisible(language == null);
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

    void showGreetingIfNecessary() {
        if (!App.shared.model.hasEnabledLanguages(this)) {
            showGreeting();
        }
    }

    void showGreeting() {
        dialogs.showOkDialog("Welcome to Wikireader!\n\nTap the checkbox [ ] for each language you wish to read, then select a dictionary.\n\nPress '<-' when you are done");
    }
}
