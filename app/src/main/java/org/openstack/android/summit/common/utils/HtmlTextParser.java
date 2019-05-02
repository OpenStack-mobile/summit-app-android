package org.openstack.android.summit.common.utils;

import android.os.Build;

final public class HtmlTextParser {

    public static String parse(String htmlText, String BaseUrl){
        if (htmlText == null || htmlText.isEmpty()) return "";
        // convert raw urls to <a> tags
        htmlText = convertLinksToAnchorTags(htmlText);

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
        html.append(htmlText);
        html.append("</body>");
        html.append("</html>");

        return html.toString();
    }


    public static String convertLinksToAnchorTags(String htmlText){
        if (htmlText == null || htmlText.isEmpty()) return "";
        // convert raw urls to <a> tags
        return htmlText.replaceAll("((?<!(href=['\"]))(?:https?|ftps?):\\/\\/[\\w\\.\\?\\=\\d\\/\\-\\_]*)","<a href=\"$1\">$1</a>");
    }
}
