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
import java.util.List;

public class Settings {

    public boolean isFreshInstall(Context context) {
        return App.shared.storage.getStringValue(context, "is-fresh-install", "true").equals("true");
    }

    public void setIsFreshInstall(Context context, boolean val) {
        App.shared.storage.setStringValue(context, "is-fresh-install", val ? "true" : "false");
    }

    private String lastVisitedKey(Language language, Site site) {
        return language.label + "-" + site.label;
    }

    public String lastVisitedURL(Context context, Language language, Site site) {
        String key = lastVisitedKey(language, site);
        return App.shared.storage.getStringValue(context, key, site.address);
    }

    public void setLastVisitedURL(Context context, Language language, Site site, String url) {
        String key = lastVisitedKey(language, site);
        App.shared.storage.setStringValue(context, key, url);
    }

    public Language getCurrentLanguage(Context context) {
        String currentStr = App.shared.storage.getStringValue(context, "current-language", "");
        List<Language> languages = App.shared.model.getLanguages(context);
        for (Language language : languages) {
            if (language.label.equals(currentStr)) {
                return language;
            }
        }
        return Language.NoLanguage;
    }

    public void setCurrentLanguage(Context context, Language language) {
        App.shared.storage.setStringValue(context, "current-language", language.label);
    }

    public Site getCurrentSite(Context context) {
        String currentStr = App.shared.storage.getStringValue(context, "current-site", "");
        Language currentLanguage = getCurrentLanguage(context);
        for (Site site : currentLanguage.sites) {
            if (site.address.equals(currentStr)) {
                return site;
            }
        }
        return Site.BlankSite;
    }

    public void setCurrentSite(Context context, Site site) {
        App.shared.storage.setStringValue(context, "current-site", site.address);
    }

    public boolean currentSiteIsBlank(Context context) {
        return getCurrentSite(context).equals(Site.BlankSite);
    }
}
