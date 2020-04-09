package com.putskul_productions.wikireader;

import android.annotation.SuppressLint;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

public class SitesAdapter extends RecyclerView.Adapter<SitesAdapter.SitesViewHolder> {
    private List<Language> languages;
    public Language currentLanguage;
    private OnClickSiteListener mOnClickSiteListener;

    public static class SitesViewHolder extends RecyclerView.ViewHolder {
        public LinearLayout layout;
        public TextView label;
        public TextView subLabel;
        ImageView editIcon;
        ImageView deleteIcon;
        public SitesViewHolder(LinearLayout v) {
            super(v);
            layout = v;
            label = (TextView)layout.findViewById(R.id.siteLabelTextView);
            subLabel = (TextView)layout.findViewById(R.id.siteSubLabelTextView);
            deleteIcon = itemView.findViewById(R.id.deleteIcon);
            editIcon = itemView.findViewById(R.id.editIcon);
        }
    }

    public SitesAdapter(List<Language> myDataset, OnClickSiteListener pListener) {
        languages = myDataset;
        mOnClickSiteListener = pListener;
    }

    void updateData(List<Language> pLanguages) {
        languages = pLanguages;
        notifyDataSetChanged();
    }

    @Override
    public SitesViewHolder onCreateViewHolder(ViewGroup parent,
                                              int viewType) {
        LinearLayout layout = (LinearLayout)  LayoutInflater.from(parent.getContext())
                .inflate(R.layout.sites_item_view, parent, false);
        SitesViewHolder vh = new SitesViewHolder(layout);
        return vh;
    }

    @SuppressLint({"ClickableViewAccessibility", "SetTextI18n"})
    @Override
    public void onBindViewHolder(final SitesViewHolder holder, int position) {
        if (currentLanguage == null) {
            final Language language = languages.get(position);
            holder.label.setText(language.label);
            holder.subLabel.setText("");
            holder.subLabel.setVisibility(View.GONE);
            holder.editIcon.setVisibility(View.VISIBLE);
            holder.editIcon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    currentLanguage = language;
                    notifyDataSetChanged();
                }
            });
            holder.deleteIcon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mOnClickSiteListener.onDelete(language, null);
                }
            });
        }
        else {
            final Site site = currentLanguage.sites.get(position);
            holder.label.setText(site.label);
            holder.subLabel.setText(site.address);
            holder.subLabel.setVisibility(View.VISIBLE);
            holder.editIcon.setVisibility(View.GONE);

            holder.deleteIcon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mOnClickSiteListener.onDelete(currentLanguage, site);
                }
            });
            /*
            holder.layout.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {
                    switch (motionEvent.getActionMasked()) {
                        case MotionEvent.ACTION_DOWN:
                            mOnClickSiteListener.onClick(site);
                        default:
                    }
                    return true;
                }
            });*/
        }
    }

    @Override
    public int getItemCount() {
        if (currentLanguage == null) {
            return languages.size();
        }
        return currentLanguage.sites.size();
    }


    public interface OnClickSiteListener {
        void onDelete(Language lang, Site site);
        void onEdit(Language lang, Site site);
    }
}