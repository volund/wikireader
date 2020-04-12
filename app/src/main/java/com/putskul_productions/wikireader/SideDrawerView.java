package com.putskul_productions.wikireader;

import android.content.Context;
import android.support.design.widget.NavigationView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;


public class SideDrawerView extends NavigationView {
    private LayoutInflater mInflater;
    private Context mContext;
    private ListView mItemsList;
    private SideDrawerAdapter mItemsAdapter;
    private OnClickMenu mListener;

    private ImageView mHeader;

    //region Constructors
    public SideDrawerView(Context context) {
        super(context);
        mContext = context;
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        initLayout();
        setData();
    }

    public SideDrawerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        initLayout();
        setData();
    }

    public SideDrawerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        initLayout();
        setData();
    }
    //endregion

    private void initLayout(){
        mInflater.inflate(R.layout.side_drawer, this);
        mItemsList = (ListView) findViewById(R.id.menu_items_list);
        mHeader = (ImageView) findViewById(R.id.header);
        mHeader.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // do something
            }
        });
    }


    public void setListener(OnClickMenu mListener) {
        this.mListener = mListener;
    }

    void setData() {
        List<Language> languages = Storage.shared.getLanguages(getContext());
        List<Object> sections = new ArrayList<>();
        for (Language language : languages) {
            if (language.enabled) {
                sections.add(language);
                sections.addAll(language.sites);
            }
        }

        mItemsAdapter = new SideDrawerAdapter(mContext, sections, new OnClickMenu() {
            @Override
            public void onClick(Language language, Site site) {

                if (mListener != null)
                    mListener.onClick(language, site);
            }
        });
        mItemsList.setAdapter(mItemsAdapter);
        mItemsAdapter.notifyDataSetChanged();
    }

}