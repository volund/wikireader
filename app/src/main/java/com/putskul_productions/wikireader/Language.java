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

import java.util.ArrayList;
import java.util.List;

public class Language implements Comparable {
    public boolean enabled;
    public String label;
    public List<Site> sites;
    public List<Dictionary> dictionaries;
    public Dictionary currentDictionary;

    public Language(String pLabel) {
        enabled = false;
        label = pLabel;
        sites = new ArrayList<>();
        dictionaries = new ArrayList<>();
        currentDictionary = Dictionary.BlankDictionary;
    }

    String[] dictionariesStringArray() {
        String[] strs = new String[dictionaries.size()];
        int i = 0;
        for (Dictionary dict : dictionaries) {
            strs[i] = dict.name;
            i += 1;
        }
        return strs;
    }

    public boolean equals(Object obj) {
        if (obj instanceof Language == false) {
            return false;
        }
        Language language = (Language)obj;
        return label.equals(language.label);
    }

    @Override
    public int compareTo(@NonNull Object obj) {
        if (obj instanceof Language == false) {
            return -1;
        }
        Language lang = (Language)obj;
        return label.compareTo(lang.label);
    }
    final static Language NoLanguage = new Language("None");
}
