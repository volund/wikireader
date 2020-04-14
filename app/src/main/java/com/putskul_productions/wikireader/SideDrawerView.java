package com.putskul_productions.wikireader;

import android.content.Context;
import android.support.design.widget.NavigationView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import java.util.ArrayList;
import java.util.List;

public class SideDrawerView extends NavigationView {
    private LayoutInflater mInflater;
    private Context mContext;
    private ListView mItemsList;
    private SideDrawerAdapter mItemsAdapter;
    private SideDrawerListener mListener;
    private ImageView mHeader;

    public SideDrawerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        initLayout();
        loadData();
    }

    private void initLayout(){
        mInflater.inflate(R.layout.side_drawer, this);
        mItemsList = findViewById(R.id.menu_items_list);
        mHeader = findViewById(R.id.header);
    }

    public void setListener(SideDrawerListener mListener) {
        this.mListener = mListener;
    }

    void loadData() {
        List<Object> sections = new ArrayList<>();
        for (Language language : Storage.shared.enabledLanguages(getContext())) {
            sections.add(language);
            sections.addAll(language.sites);
        }
        mItemsAdapter = new SideDrawerAdapter(mContext, sections, mListener);
        mItemsList.setAdapter(mItemsAdapter);
        mItemsAdapter.notifyDataSetChanged();
    }

}