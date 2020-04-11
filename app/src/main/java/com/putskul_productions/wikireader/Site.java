package com.putskul_productions.wikireader;

import android.support.annotation.NonNull;

public class Site implements Comparable {
    public String label;
    public String address;
    public String lastVisitedURL;

    Site(String pLabel, String pAddress) {
        label = pLabel.trim();
        address = pAddress.trim();
        lastVisitedURL = address;
    }

    public boolean equals(Object obj) {
        if (obj instanceof Site == false) {
            return false;
        }
        Site site = (Site)obj;
        return label.equals(site.label) && address.equals(site.address);
    }


    @Override
    public int compareTo(@NonNull Object obj) {
        if (obj instanceof Site == false) {
            return -1;
        }
        Site site = (Site)obj;
        return site.label.compareTo(label);
    }

    final static Site BlankSite = new Site("None", "about:blank");
}
