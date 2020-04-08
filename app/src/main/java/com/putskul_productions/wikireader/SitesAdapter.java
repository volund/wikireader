package com.putskul_productions.wikireader;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

public class SitesAdapter extends RecyclerView.Adapter<SitesAdapter.SitesViewHolder> {
    private List<Site> mDataset;
    private OnClickSiteListener mOnClickSiteListener;

    public static class SitesViewHolder extends RecyclerView.ViewHolder {
        public LinearLayout layout;
        public TextView label;
        public TextView subLabel;
        public SitesViewHolder(LinearLayout v) {
            super(v);
            layout = v;
            label = (TextView)layout.findViewById(R.id.siteLabelTextView);
            subLabel = (TextView)layout.findViewById(R.id.siteSubLabelTextView);
        }
    }

    public SitesAdapter(List<Site> myDataset, OnClickSiteListener pListener) {
        mDataset = myDataset;
        mOnClickSiteListener = pListener;
    }

    void updateData(List<Site> sites) {
        mDataset = sites;
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
        final Site site = mDataset.get(position);
        holder.label.setText(site.language + " - " + site.label);
        holder.subLabel.setText(site.address);
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
        });
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }


    public interface OnClickSiteListener {
        void onClick(Site site);
    }
}