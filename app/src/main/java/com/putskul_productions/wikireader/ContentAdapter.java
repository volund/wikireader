/*
Copyright 2021 Amos JOSHUA

Permission is hereby granted, free of charge, to any person obtaining
a copy of this software and associated documentation files (the
"Software"), to deal in the Software without restriction, including
without limitation the rights to use, copy, modify, merge, publish,
distribute, sublicense, and/or sell copies of the Software, and to
permit persons to whom the Software is furnished to do so, subject to
the following conditions:

The above copyright notice and this permission notice shall be
included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE
LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION
OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION
WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
*/
    
package com.putskul_productions.wikireader;

import android.annotation.SuppressLint;
import android.graphics.Paint;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

public class ContentAdapter extends RecyclerView.Adapter<ContentAdapter.SitesViewHolder> {
    private List<Language> languages;
    public Language currentLanguage;
    private OnClickContentListener mOnClickContentListener;

    public ContentAdapter(List<Language> myDataset, OnClickContentListener pListener) {
        languages = myDataset;
        mOnClickContentListener = pListener;
    }

    void updateData(List<Language> pLanguages) {
        languages = pLanguages;
        notifyDataSetChanged();
    }

    @Override
    public SitesViewHolder onCreateViewHolder(ViewGroup parent,
                                              int viewType) {
        LinearLayout layout = (LinearLayout)  LayoutInflater.from(parent.getContext())
                .inflate(R.layout.content_item_view, parent, false);
        SitesViewHolder vh = new SitesViewHolder(layout);
        return vh;
    }

    @SuppressLint({"ClickableViewAccessibility", "SetTextI18n"})
    @Override
    public void onBindViewHolder(final SitesViewHolder holder, int position) {
        if (currentLanguage == null) {
            final Language language = languages.get(position);
            holder.label.setText(StringUtils.capitalize(language.label));
            holder.label.setPaintFlags(language.enabled ?
                    holder.label.getPaintFlags() & ~Paint.STRIKE_THRU_TEXT_FLAG :
                    holder.label.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            holder.deleteIcon.setVisibility(View.GONE);
            holder.subLabel.setText(language.currentDictionary.name);
            holder.enabledCheckbox.setOnCheckedChangeListener(null);
            holder.enabledCheckbox.setVisibility(View.VISIBLE);
            holder.enabledCheckbox.setChecked(language.enabled);
            holder.enabledCheckbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    mOnClickContentListener.onToggleLanguageEnabled(language);
                    if (!language.enabled) {
                        notifyDataSetChanged();
                    }
                }
            });
            holder.layout.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {
                    if (motionEvent.getActionMasked() == MotionEvent.ACTION_UP) {
                            currentLanguage = language;
                            mOnClickContentListener.onSelectionChanged(language);
                            notifyDataSetChanged();
                    }
                    return true;
                }
            });
        }
        else {
            final Site site = currentLanguage.sites.get(position);
            holder.label.setText(site.label);
            holder.label.setPaintFlags(holder.label.getPaintFlags() & ~Paint.STRIKE_THRU_TEXT_FLAG);
            holder.subLabel.setText(site.address);
            holder.deleteIcon.setVisibility(View.VISIBLE);
            holder.enabledCheckbox.setVisibility(View.GONE);
            holder.deleteIcon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mOnClickContentListener.onDelete(currentLanguage, site);
                }
            });
            holder.layout.setOnTouchListener(null);
        }
    }

    @Override
    public int getItemCount() {
        return currentLanguage == null ? languages.size() : currentLanguage.sites.size();
    }

    public interface OnClickContentListener {
        void onDelete(Language lang, Site site);
        void onSelectionChanged(Language lang);
        void onToggleLanguageEnabled(Language lang);
    }

    public static class SitesViewHolder extends RecyclerView.ViewHolder {
        public CheckBox enabledCheckbox;
        public LinearLayout layout;
        public TextView label;
        public TextView subLabel;
        ImageView deleteIcon;

        public SitesViewHolder(LinearLayout v) {
            super(v);
            layout = v;
            enabledCheckbox = itemView.findViewById(R.id.languageEnabled);
            label = layout.findViewById(R.id.siteLabelTextView);
            subLabel = layout.findViewById(R.id.siteSubLabelTextView);
            deleteIcon = itemView.findViewById(R.id.deleteIcon);
        }
    }
}
