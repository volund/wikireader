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

import android.content.Context;
import android.support.design.widget.NavigationView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
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
        for (Language language : App.shared.model.enabledLanguages(getContext())) {
            sections.add(language);
            sections.addAll(language.sites);
        }
        mItemsAdapter = new SideDrawerAdapter(mContext, sections, mListener);
        mItemsList.setAdapter(mItemsAdapter);
        mItemsAdapter.notifyDataSetChanged();
    }

}
