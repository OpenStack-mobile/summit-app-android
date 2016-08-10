package org.openstack.android.summit.common;

import android.content.Context;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.ScrollView;

/**
 * Created by sebastian on 8/10/2016.
 */
public class HtmlTextView extends ScrollView {

    private WebView webView;
    private final int WEB_VIEW_ID = 0x152004;

    public HtmlTextView(Context context) {
        super(context);
        initialize(context);
    }

    private void initialize(Context context){
        webView = new WebView(context);
        webView.setId(this.getId()+WEB_VIEW_ID);
        webView.setVerticalScrollBarEnabled(false);
        webView.setHorizontalScrollBarEnabled(false);
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        webView.setLayoutParams(params);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setNetworkAvailable(false);
        addView(webView);
    }

    public HtmlTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initialize(context);
    }

    public HtmlTextView(Context context,
                       AttributeSet attrs,
                       int defStyle) {
        super(context, attrs, defStyle);
        initialize(context);
    }

    public void setText(String body){
        if(body.isEmpty()) return;

        StringBuilder html = new StringBuilder();
        html.append("<html>");
        html.append("<head>");
        html.append("<meta http-equiv='Content-Type' content='text/html; charset=utf-8'>");
        html.append("<link href=\"css/htmltextview.css\" rel=\"stylesheet\" type=\"text/css\">");
        html.append("</head>");
        html.append("<body>");
        html.append(body);
        html.append("</body>");
        html.append("</html>");

        webView.loadDataWithBaseURL("file:///android_asset/", html.toString(), "text/html; charset=utf-8", "UTF-8", null);
        //webView.loadUrl("file:///android_asset/css/htmltextview.css");
    }


}
