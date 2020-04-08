package com.putskul_productions.wikireader;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.List;

public class SitesActivity extends AppCompatActivity {
    RecyclerView mRecyclerView;

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
        SitesAdapter mAdapter = new SitesAdapter(sites);
        mRecyclerView.setAdapter(mAdapter);

    }






}
