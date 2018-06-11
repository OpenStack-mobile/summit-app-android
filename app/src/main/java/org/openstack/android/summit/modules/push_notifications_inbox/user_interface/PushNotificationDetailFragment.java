package org.openstack.android.summit.modules.push_notifications_inbox.user_interface;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.joda.time.DateTime;
import org.openstack.android.summit.R;
import org.openstack.android.summit.common.HtmlTextView;
import org.openstack.android.summit.common.user_interface.BaseFragment;
import org.openstack.android.summit.common.utils.LocalDateFormat;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by sebastian on 8/22/2016.
 */
public class PushNotificationDetailFragment
        extends BaseFragment<IPushNotificationDetailPresenter>
        implements IPushNotificationDetailView {

    private MenuItem itemGo2Event = null;
    private MenuItem itemDelete   = null;
    private boolean showGo2Event  = false;
    private boolean showDelete    = true;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        getComponent().inject(this);
        super.onCreate(savedInstanceState);
        presenter.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        presenter.onSaveInstanceState(outState);
    }

    @Override
    public void onResume() {
        super.onResume();
        setTitle(getResources().getString(R.string.notification));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_push_notification_detail, container, false);
        super.onCreateView(inflater, container, savedInstanceState);
        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.push_notifications, menu);
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        itemGo2Event = menu.findItem(R.id.menu_item_push_notification_go_2_event);
        itemDelete   = menu.findItem(R.id.menu_item_push_notification_delete);
        if(itemDelete != null) itemDelete.setVisible(showDelete);
        showGo2EventMenuItem(showGo2Event);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_item_push_notification_delete:
                presenter.onRemovePushNotification();
                return true;
            case R.id.menu_item_push_notification_go_2_event:
                presenter.go2Event();
                return true;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void setSubject(String name) {
        TextView subjectTextView = (TextView)view.findViewById(R.id.push_notification_detail_subject);
        subjectTextView.setText(name);
    }

    @Override
    public void setBody(String name) {
        HtmlTextView bodyTextView = (HtmlTextView)view.findViewById(R.id.push_notification_detail_body);
        bodyTextView.setText(name);
    }

    @Override
    public void setType(String type){
        TextView typeTextView = (TextView)view.findViewById(R.id.push_notification_detail_type);
        typeTextView.setText(type);
    }

    @Override
    public void setReceived(Date received) {
        TextView receivedTextView = (TextView)view.findViewById(R.id.push_notification_detail_received);
        DateTime nowBegin = new DateTime().withTime(0,0,0,0);
        DateTime nowEnd   = new DateTime().withTime(23,59,59,0);
        DateFormat df     = received.after(nowBegin.toDate()) && received.before(nowEnd.toDate()) ?  new LocalDateFormat("hh:mm a") : new SimpleDateFormat("E d");
        receivedTextView.setText(df.format(received));
    }

    @Override
    public void showGo2EventMenuItem(boolean show) {
        showGo2Event = show;
        if(itemGo2Event == null) return;
        itemGo2Event.setVisible(showGo2Event);
    }

    @Override
    public void close() {
        getFragmentManager().popBackStack();
    }

    @Override
    public void hideView() {
        TextView receivedTextView = (TextView)view.findViewById(R.id.push_notification_detail_received);
        if(receivedTextView != null) receivedTextView.setVisibility(View.GONE);
        TextView typeTextView = (TextView)view.findViewById(R.id.push_notification_detail_type);
        if(typeTextView != null) typeTextView.setVisibility(View.GONE);
        HtmlTextView bodyTextView = (HtmlTextView)view.findViewById(R.id.push_notification_detail_body);
        if(bodyTextView != null) bodyTextView.setVisibility(View.GONE);
        showDelete = showGo2Event = false;
    }
}
