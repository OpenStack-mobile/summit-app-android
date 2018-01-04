package org.openstack.android.summit.common;

import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.net.Uri;
import android.os.Build;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.ScrollView;

import org.openstack.android.summit.R;

/**
 * Created by sebastian on 8/10/2016.
 */
public class HtmlTextView extends ScrollView {

    private final static int WEB_VIEW_ID = 0x152004;
    private final static String BaseUrl  = "file:///android_asset/";
    private final static String MimeType = "text/html; charset=utf-8";
    private final static String Encoding = "UTF-8";

    private WebView webView;
    private Context context;

    public HtmlTextView(Context context) {
        super(context);
        initialize(context);
        this.context = context;
    }

    private void initialize(Context context) {
        this.context = context;
        webView = new WebView(context);

        webView.setId(this.getId() + WEB_VIEW_ID);
        webView.setVerticalScrollBarEnabled(false);
        webView.setHorizontalScrollBarEnabled(false);
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        webView.setLayoutParams(params);
        if(!isInEditMode()) {
            webView.getSettings().setJavaScriptEnabled(true);
            webView.setNetworkAvailable(false);
        }
        addView(webView);
        webView.setWebViewClient(new WebViewClient() {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            if (isValidUrl(url)) {
                loadExternalUrl(url);
            }
            return true;
            }
        });
    }

    protected boolean isValidUrl(String url) {
        return Uri.parse(url).getScheme().contains("http");
    }

    protected void loadExternalUrl(String url) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        context.startActivity(intent);
    }

    public HtmlTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initialize(context);
        setAttributes(attrs);
        this.context = context;
    }

    private void setAttributes( AttributeSet attrs){
        TypedArray a = context.getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.HtmlTextView,
                0, 0);
        try {
            String text = a.getString(R.styleable.HtmlTextView_text);
            setText(text);
        } finally {
            a.recycle();
        }
    }

    public HtmlTextView(Context context,
                        AttributeSet attrs,
                        int defStyle) {
        super(context, attrs, defStyle);
        initialize(context);
        setAttributes(attrs);
        this.context = context;
    }

    public void setText(String body) {

        if (body == null || body.isEmpty()) return;
        // convert raw urls to <a> tags
        body = body.replaceAll("((?<!(href=['\"]))(?:https?|ftps?):\\/\\/[\\w\\.\\?\\=\\d\\/]*)","<a href=\"$1\">$1</a>");

        StringBuilder html = new StringBuilder();
        html.append("<html>");
        html.append("<head>");
        if(Build.VERSION.SDK_INT <= Build.VERSION_CODES.JELLY_BEAN_MR2)
            html.append("<base href=\""+BaseUrl+"\">");

        // fix urls

        html.append("<meta http-equiv='Content-Type' content='text/html; charset=utf-8'>");
        html.append("<link href=\"css/htmltextview.css\" rel=\"stylesheet\" type=\"text/css\">");
        html.append("</head>");
        html.append("<body>");
        html.append(body);
        html.append("</body>");
        html.append("</html>");

        if(Build.VERSION.SDK_INT <= Build.VERSION_CODES.JELLY_BEAN_MR2){
            webView.loadData(html.toString(), MimeType , Encoding);
            return;
        }

        webView.loadDataWithBaseURL(BaseUrl, html.toString(), MimeType, Encoding, null);
    }
}
