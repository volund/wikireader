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

import java.util.List;

public class SitesActivity extends AppCompatActivity implements SitesAdapter.OnClickSiteListener {
    RecyclerView mRecyclerView;
    SitesAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Sites");
        setContentView(R.layout.activity_sites);

        mRecyclerView = (RecyclerView) findViewById(R.id.sitesRecyclerView);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(layoutManager);

        // specify an adapter (see also next example)
        List<Site> sites = Settings.shared.getSites(this);
        mAdapter = new SitesAdapter(sites, this);
        mRecyclerView.setAdapter(mAdapter);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // R.menu.mymenu is a reference to an xml file named mymenu.xml which should be inside your res/menu directory.
        // If you don't have res/menu, just create a directory named "menu" inside res
        getMenuInflater().inflate(R.menu.sites_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    // handle button activities
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.addButton) {
            showAddSiteDialogs();
        }
        return super.onOptionsItemSelected(item);
    }

    void showAddSiteDialogs() {

        final EditText input = new EditText(this);
        showPromptDialog("Site language", input, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                final String language = input.getText().toString();
                showPromptDialog("Site label", input, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        final String label = input.getText().toString();
                    }
                });
            }
        });
    }
    @Override
    public void onClick(final Site site) {
        final Context context = this;
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Really delete?");
        builder.setIcon(android.R.drawable.ic_dialog_alert);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                List<Site> sites = Settings.shared.getSites(context);
                sites.remove(site);
                Settings.shared.setSites(context, sites);
                refreshData();
            }
        });
        builder.setNegativeButton("Cancel", null);
        builder.show();
    }

    void refreshData() {
        List<Site> sites = Settings.shared.getSites(this);
        mAdapter.updateData(sites);
    }

    void showPromptDialog(String title, EditText input, DialogInterface.OnClickListener okListener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(title);
        builder.setView(input);
        builder.setPositiveButton("OK", okListener);
        builder.show();
    }
}
