package com.putskul_productions.wikireader;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
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
    private static String lastSelectedSection;

    private List<String> mSections;
    private int currentTextcolor;
    private OnClickMenu mListener;

    public MenuItemsAdapter(Context context, List<String> sections, OnClickMenu listener) {
        mContext = context;
        mSections = sections;
        mListener = listener;

        lastSelectedSection = sections.get(0);
    }

    @Override
    public int getCount() {
        return mSections.size();
    }

    @Override
    public String getItem(int position) {
        return mSections.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }

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

        holder.position = position;

       // holder.mLine.setVisibility(View.GONE);
        holder.mTitle.setTextColor(mContext.getResources().getColor(R.color.colorPrimary));
        holder.mIconView.setColorFilter(mContext.getResources().getColor(R.color.colorPrimary));

        if (mSections.get(position).equals(mContext.getString(R.string.login_id))) {
            holder.mIconView.setImageResource(R.drawable.ic_arrow_back_black_24dp);
            // holder.mIconView.setColorFilter(mContext.getResources().getColor(R.color.primary));
            holder.mTitle.setText(mContext.getString(R.string.action_settings));
           // holder.mLine.setVisibility(View.VISIBLE);
            //holder.mLayoutItem.setPadding(0, pxMarginSection, 0, pxMarginSection);
        }

        /*
        else if (mSections.get(position).equals(mContext.getString(R.string.settings_id))) {
            holder.mIconView.setImageResource(R.drawable.option);
            holder.mTitle.setText(mContext.getString(R.string.action_settings));
            holder.mLayoutItem.setPadding(0, pxMarginSection, 0, pxMarginSection);
            holder.mLine.setVisibility(View.VISIBLE);
        } else if (mSections.get(position).equals(mContext.getString(R.string.exit_id))) {
            holder.mIconView.setImageResource(R.drawable.shutdown);
            holder.mTitle.setText(mContext.getString(R.string.salir));
            holder.mLayoutItem.setPadding(0, pxMarginSection, 0, pxMarginSection);
            holder.mLine.setVisibility(View.VISIBLE);
        }*/

        holder.mLayoutItem.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {

                switch (motionEvent.getActionMasked()){
                    case MotionEvent.ACTION_DOWN:
                        currentTextcolor = holder.mTitle.getCurrentTextColor();
                        //holder.mLayoutItemSelect.setBackgroundColor(mContext.getResources().getColor(R.color.colorPrimary));
                        holder.mTitle.setTextColor(mContext.getResources().getColor(R.color.textInfo));
                        holder.mIconView.setColorFilter(mContext.getResources().getColor(R.color.textInfo));
                        return true;


                    case MotionEvent.ACTION_UP:

                        //holder.mLayoutItemSelect.setBackgroundResource(R.color.bgLeftMenu);
                        holder.mTitle.setTextColor(currentTextcolor);
                        holder.mIconView.setColorFilter(mContext.getResources().getColor(R.color.colorPrimary));

                        mListener.onClick(mSections.get(position));
                        return true;

                    case MotionEvent.ACTION_CANCEL:

                       // holder.mLayoutItemSelect.setBackgroundColor(mContext.getResources().getColor(R.color.bgLeftMenu));
                        holder.mTitle.setTextColor(currentTextcolor);
                        holder.mIconView.setColorFilter(mContext.getResources().getColor(R.color.colorPrimary));

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
        ImageView mIconView;
        LinearLayout mLayoutItem;
        View mLine;
        LinearLayout mLayoutItemSelect;

        int position;

        public MenuItemHolder(View itemView) {
            mTitle = itemView.findViewById(R.id.title);
            mIconView = itemView.findViewById(R.id.icon);

            mLayoutItem = itemView.findViewById(R.id.layoutItem);
            //mLine = itemView.findViewById(R.id.rl_line);
            //mLayoutItemSelect = itemView.findViewById(R.id.layoutItemSelect);

            view = itemView;
        }

    }

    public void setLastSelectedSection(String idSection) {
        lastSelectedSection = idSection;
    }
}