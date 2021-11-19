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
