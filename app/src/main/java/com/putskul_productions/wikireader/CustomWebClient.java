package com.putskul_productions.wikireader;

import android.graphics.Bitmap;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;


public class CustomWebClient extends WebViewClient {
    private int       webViewPreviousState;
    private final int PAGE_STARTED    = 0x1;
    private final int PAGE_REDIRECTED = 0x2;
    private WebClientListener clientListener;

    CustomWebClient(WebClientListener pListener) {
        super();
        clientListener = pListener;
    }

    final String script = "\n" +
            "document.body.addEventListener('click', function(event){\n" +
            "  var target = event.target || event.srcElement;\n" +
            "  if ((target.tagName === 'A') || (target.tagName === 'a')) {\n" +
            //"    console.log('DBG did tap link' + JSON.stringify(target));\n" +
            "    event.preventDefault(); javascriptBridge.handleLink(target.getAttribute('href'), target.innerHTML);\n" +
            "    return false;\n" +
            "  }\n" +
            "  else if ((target.tagName === 'h2') || (target.tagName === 'H2')) {\n" +
            "    if ((target.className != null) && (target.className.indexOf('collapsible-heading') >= 0)) {\n" +
            "      var expanded_ids = [];\n" +
            "      var divs = document.querySelectorAll('div[aria-expanded=\\\"true\\\"');\n" +
            "      var expanded_ids = Array.prototype.map.call(divs, function(div) { return div.id });\n" +
            "      javascriptBridge.saveExpandedSections(JSON.stringify(expanded_ids));\n" +
            "    }\n" +
            "  }\n" +
            "}, false);\n" +
            "\n" +
            "document.body.addEventListener('dblclick', function(event){\n" +
            "  wr_wikiReaderWordLookup();\n" +
            "}, false);\n" +
            "\n" +
            "function wr_wikiReaderWordLookup() {\n" +
            "  var s = window.getSelection();\n" +
            "  s.modify('extend','backward','word');\n" +
            "  var rect1 = s.getRangeAt(0).getBoundingClientRect();\n" +
            "  var b = s.toString();\n" +
            "  s.modify('extend','forward','word');\n" +
            "  var a = s.toString();\n" +
            "  var rect2 = s.getRangeAt(0).getBoundingClientRect();\n" +
            "  s.modify('move','forward','character');\n" +
            "  var selectiondiv = document.getElementById('__wikireader_selectiondiv');\n" +
            "  if (selectiondiv == null) {\n" +
            "    selectiondiv = document.createElement('div');\n" +
            "    selectiondiv.setAttribute('id', '__wikireader_selectiondiv');\n" +
            "    selectiondiv.style.display='none'; document.body.appendChild(selectiondiv);\n" +
            "  }\n" +
            "  var rx=s.getRangeAt(0).getBoundingClientRect();\n" +
            "  var relative=document.body.parentNode.getBoundingClientRect();\n" +
            "  selectiondiv.style.top =(rx.bottom -relative.top)+'px';\n" +
            "  selectiondiv.style.right=-(rx.right-relative.right)+'px';\n" +
            "  selectiondiv.style.position='absolute';\n" +
            "  selectiondiv.style.borderTop = '3px solid #000000';\n" +
            "  selectiondiv.style.width = (rect1.width + rect2.width)+'px';\n" +
            "  javascriptBridge.lookupCompositeWord(b+a);\n" +
            "}";


    @Override
    public boolean shouldOverrideUrlLoading(WebView view, String urlNewString) {
        webViewPreviousState = PAGE_REDIRECTED;
        view.loadUrl(urlNewString);
        return true;
    }

    @Override
    public void onPageStarted(WebView view, String url, Bitmap favicon) {
        super.onPageStarted(view, url, favicon);
        webViewPreviousState = PAGE_STARTED;
        if (clientListener != null) {
            clientListener.onPageStarted(url);
        }
    }

    @Override
    public void onPageFinished(WebView view, String url) {
        if (clientListener != null) {
            clientListener.onPageFinished(url);
        }
        if (webViewPreviousState == PAGE_STARTED) {
            view.evaluateJavascript(script, null);
        }

    }

    public interface WebClientListener {
        public void onPageStarted(String URL);
        public void onPageFinished(String URL);
    }
}
