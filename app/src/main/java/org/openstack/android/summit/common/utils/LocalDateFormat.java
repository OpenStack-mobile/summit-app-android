package org.openstack.android.summit.common.utils;

import android.text.format.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;
import org.openstack.android.summit.OpenStackSummitApplication;

/**
 * Created by spalenque on 8/2/2016.
 */
public class LocalDateFormat extends SimpleDateFormat {

    /**
     * Construct using default Locale
     * @param pattern
     */
    public LocalDateFormat(String pattern)
    {
        this(pattern, Locale.getDefault(Locale.Category.FORMAT));
    }

    /**
     * Change SimpleDateFormat hour pattern according to phone setting
     * @param pattern
     * @param locale
     */
    public LocalDateFormat(String pattern, Locale locale) {

        super(pattern, locale);

        if (DateFormat.is24HourFormat(OpenStackSummitApplication.context)) {
            String newPattern = pattern.replace("h", "H").replace(" a", "");
            this.applyPattern(newPattern);
        }

    }
}
