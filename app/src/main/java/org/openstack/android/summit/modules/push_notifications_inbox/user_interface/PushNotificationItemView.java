package org.openstack.android.summit.modules.push_notifications_inbox.user_interface;

import android.graphics.Typeface;
import android.view.View;
import android.widget.TextView;

import org.joda.time.DateTime;
import org.openstack.android.summit.R;
import org.openstack.android.summit.common.utils.LocalDateFormat;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by sebastian on 8/20/2016.
 */
public class PushNotificationItemView implements IPushNotificationItemView {

    private View view;

    public PushNotificationItemView(View view) {
        this.view = view;
    }

    @Override
    public void setSubject(String subject) {
        TextView txtSubject = (TextView) view.findViewById(R.id.item_push_notification_subject);
        if(txtSubject == null) return;
        txtSubject.setText(subject);
    }

    @Override
    public void setBody(String body) {
        TextView txtBody = (TextView) view.findViewById(R.id.item_push_notification_body);
        if(txtBody == null) return;
        txtBody.setText(body);
    }

    @Override
    public void setReceivedDate(Date receivedDate) {
        TextView txtReceivedDate = view.findViewById(R.id.item_push_notification_received_date);
        if(txtReceivedDate == null) return;
        Calendar cal = Calendar.getInstance();
        cal.setTime(receivedDate);
        int month = cal.get(Calendar.MONTH) + 1;
        int year = cal.get(Calendar.YEAR);
        DateTime nowBegin = new DateTime().withTime(0,0,0,0);
        DateTime nowEnd   = new DateTime().withTime(23,59,59,0);
        DateFormat df = null;
        // if we are on current month
        if ( ( month == nowBegin.getMonthOfYear() ) && ( year == nowBegin.getYear() ) ) {
            df = receivedDate.after(nowBegin.toDate()) && receivedDate.before(nowEnd.toDate()) ? new LocalDateFormat("hh:mm a") : new SimpleDateFormat("E d");
        }
        else{
            df = new SimpleDateFormat("yyyy/MM/dd hh:mm a");
        }


        txtReceivedDate.setText(df.format(receivedDate));
    }

    @Override
    public void setOpened(boolean isOpened) {
        TextView txtSubject = (TextView) view.findViewById(R.id.item_push_notification_subject);
        if(txtSubject == null) return;
        if(isOpened){
            txtSubject.setTypeface(null, Typeface.NORMAL);
            return;
        }
        txtSubject.setTypeface(null, Typeface.BOLD);
    }
}
