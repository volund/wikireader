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

import android.util.Log;

import java.net.URLEncoder;

public class Dictionary {
    String urlTemplate;
    String name;
    String destinationLanguage;

    Dictionary(String pName, String pDestinationLanguage, String pUrlTemplate) {
        name = pName;
        destinationLanguage = pDestinationLanguage;
        urlTemplate = pUrlTemplate;
    }

    String urlForWord(String word) {
        String url = urlTemplate.replace("%s", word); // URLEncoder.encode(word));
        Log.e("WIKIREADER", "DBG returning url [" + url + "]");
        return url;
    }

    final static Dictionary BlankDictionary = new Dictionary("None", "", "about:blank");
}
