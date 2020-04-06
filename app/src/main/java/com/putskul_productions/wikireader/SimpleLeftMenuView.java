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


public class SimpleLeftMenuView extends NavigationView {
    private LayoutInflater mInflater;
    private Context mContext;

    private ListView mItemsList;
    private MenuItemsAdapter mItemsAdapter;
    private ProgressBar mProgress;

    private OnClickMenu mListener;

    private ImageView mHeader;
    private TextView userName;
    private TextView userEmail;

    //region Constructors
    public SimpleLeftMenuView(Context context) {
        super(context);
        mContext = context;
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        initLayout();

        setData();
    }

    public SimpleLeftMenuView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        initLayout();

        setData();
    }

    public SimpleLeftMenuView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        initLayout();

        setData();
    }
    //endregion

    private void initLayout(){
        mInflater.inflate(R.layout.layout_left_menu, this);
        mItemsList = (ListView) findViewById(R.id.menu_items_list);
        mProgress = (ProgressBar) findViewById(R.id.progress);

        mHeader = (ImageView) findViewById(R.id.header);
        userName = (TextView) findViewById(R.id.userName);
        userEmail = (TextView) findViewById(R.id.userEmail);

        mHeader.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // do something
            }
        });
    }

    public void setSelectedSection(String idSection) {
        mItemsAdapter.setLastSelectedSection(idSection);
    }

    public void setmListener(OnClickMenu mListener) {
        this.mListener = mListener;
    }

    private void setData() {

        List<String> sections = new ArrayList<>();

        sections.add(mContext.getString(R.string.home_id));
        sections.add(mContext.getString(R.string.login_id));
        sections.add(mContext.getString(R.string.settings_id));
        //.........
        //sections.add(mContext.getString(R.string.exit_id));

        mItemsAdapter = new MenuItemsAdapter(mContext, sections, new OnClickMenu() {
            @Override
            public void onClick(String id) {
                mItemsAdapter.setLastSelectedSection(id);

                if (mListener != null)
                    mListener.onClick(id);
            }
        });
        mItemsList.setAdapter(mItemsAdapter);
        mItemsList.setSelection(0);
        mItemsList.setItemChecked(0, true);
    }
}