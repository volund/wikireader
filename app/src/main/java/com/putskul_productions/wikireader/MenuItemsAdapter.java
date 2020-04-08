package com.putskul_productions.wikireader;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.putskul_productions.wikireader.R;

import java.util.List;

public class MenuItemsAdapter extends BaseAdapter {

    private Context mContext;
    private static Object lastSelectedSection;

    private List<Object> mSections;
    private int currentTextcolor;
    private OnClickMenu mListener;

    public MenuItemsAdapter(Context context, List<Object> sections, OnClickMenu listener) {
        mContext = context;
        mSections = sections;
        mListener = listener;
        if (sections.size() > 0) {
            lastSelectedSection = sections.get(0);
        }
    }

    @Override
    public int getCount() {
        return mSections.size();
    }

    @Override
    public String getItem(int position) {
        Object obj = mSections.get(position);
        if (obj instanceof String) {
            return (String)obj;
        }
        Site site = (Site)obj;
        return ((Site) obj).label;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }

    @SuppressLint("ClickableViewAccessibility")
    public View getCustomView(final int position, View convertView, ViewGroup parent) {
        final MenuItemHolder holder;

        if (convertView==null){

            LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();
            convertView = inflater.inflate(R.layout.layout_left_menu_item, parent, false);
            holder = new MenuItemHolder(convertView);
            convertView.setTag(holder);

        }else {

            holder = (MenuItemHolder) convertView.getTag();

        }

        Resources r = mContext.getResources();
        int pxMarginSection = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 8, r.getDisplayMetrics());

        Object obj = mSections.get(position);

        holder.position = position;
        holder.mTitle.setText(getItem(position));

        if (obj instanceof String) {
            holder.clickable = false;
            holder.mTitle.setTextColor(Color.LTGRAY);
            holder.mTitle.setTextSize(20);
            holder.mTitle.setPadding(35, 40, 30, 0);
        }
        else {
            holder.mTitle.setTextColor(Color.WHITE);
            holder.mTitle.setTextSize(25);
            holder.mTitle.setPadding(60, 20, 50, 0);
            holder.site = (Site)obj;
        }

        holder.mLayoutItem.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {

                switch (motionEvent.getActionMasked()){
                    case MotionEvent.ACTION_DOWN:
                        if (holder.clickable) {
                            currentTextcolor = holder.mTitle.getCurrentTextColor();
                            holder.mTitle.setTextColor(mContext.getResources().getColor(R.color.textInfo));
                        }
                        return true;


                    case MotionEvent.ACTION_UP:
                        if (holder.clickable) {
                            holder.mTitle.setTextColor(currentTextcolor);
                            mListener.onClick(holder.site);
                        }
                        return true;

                    case MotionEvent.ACTION_CANCEL:
                        if (holder.clickable) {
                            holder.mTitle.setTextColor(currentTextcolor);
                        }
                        return true;

                }

                return false;
            }
        });

        return convertView;

    }

    class MenuItemHolder {

        // butterKnife
        View view;
        TextView mTitle;
        LinearLayout mLayoutItem;
        boolean clickable = true;
        Site site;
        int position;

        public MenuItemHolder(View itemView) {
            mTitle = itemView.findViewById(R.id.title);
            mLayoutItem = itemView.findViewById(R.id.layoutItem);
            view = itemView;
        }

    }

    public void setLastSelectedSection(String idSection) {
        lastSelectedSection = idSection;
    }
}