package com.putskul_productions.wikireader;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

public class SideDrawerAdapter extends BaseAdapter {
    private Context mContext;
    private List<Object> mSections;
    private SideDrawerListener mListener;

    public SideDrawerAdapter(Context context, List<Object> sections, SideDrawerListener listener) {
        mContext = context;
        mSections = sections;
        mListener = listener;
    }

    @Override
    public int getCount() {
        return mSections.size();
    }

    @Override
    public String getItem(int position) {
        Object obj = mSections.get(position);
        return obj instanceof Language ? ((Language) obj).label : ((Site) obj).label;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    View convertViewForPosition(int position, View convertView, ViewGroup parent) {
        if (convertView != null) {
            return convertView;
        }
        LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();
        convertView = inflater.inflate(R.layout.side_drawer_item, parent, false);
        MenuItemHolder holder = new MenuItemHolder(convertView);
        holder.position = position;
        convertView.setTag(holder);
        return convertView;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        convertView = convertViewForPosition(position, convertView, parent);
        final MenuItemHolder holder = (MenuItemHolder)convertView.getTag();
        holder.mTitle.setText(getItem(position));
        Object obj = mSections.get(position);

        if (obj instanceof Language) {
            holder.clickable = false;
            holder.mTitle.setTextColor(Color.LTGRAY);
            holder.mTitle.setTextSize(16);
            holder.mTitle.setPadding(35, 40, 30, 0);
        }
        else {
            Site site = (Site)obj;
            Language language = Language.NoLanguage;
            // This is a hack: find language for site by
            // looking for "higher up" items in the menu
            for (int i = position; i >= 0; i -= 1) {
                if ((mSections.get(i) instanceof Language) && (((Language)mSections.get(i)).sites.contains(site))) {
                    language = (Language)mSections.get(i);
                    break;
                }
            }
            holder.mTitle.setTextColor(Color.WHITE);
            holder.mTitle.setTextSize(22);
            holder.mTitle.setPadding(60, 20, 50, 0);
            holder.site = site;
            holder.language = language;
        }

        holder.mLayoutItem.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                Log.e("WIKIREADER", "DBG MOTION");
                if (holder.clickable && (motionEvent.getActionMasked() == MotionEvent.ACTION_UP)) {
                    Log.e("WIKIREADER", "DBG UP");
                    mListener.onSideDrawerItemClick(holder.language, holder.site);
                    return true;
                }
                return (motionEvent.getActionMasked() == MotionEvent.ACTION_DOWN) || (motionEvent.getActionMasked() == MotionEvent.ACTION_MOVE);
            }
        });
        return convertView;
    }

    class MenuItemHolder {
        LinearLayout mLayoutItem;
        TextView mTitle;
        View view;
        boolean clickable = true;
        Language language;
        Site site;
        int position;

        public MenuItemHolder(View itemView) {
            mTitle = itemView.findViewById(R.id.title);
            mLayoutItem = itemView.findViewById(R.id.layoutItem);
            view = itemView;
        }
    }
}