package org.openstack.android.summit.common.user_interface;

import android.graphics.Color;
import android.view.View;
import android.widget.TextView;

import org.openstack.android.summit.R;

/**
 * Created by Claudio Redi on 12/23/2015.
 */
public class ScheduleItemView implements IScheduleItemView {
    private View view;

    public ScheduleItemView(View view) {
        this.view = view;
    }

    @Override
    public String getName() {
        TextView nameTextView = (TextView) view.findViewById(R.id.item_schedule_textview_name);
        return nameTextView.getText().toString();
    }

    @Override
    public void setName(String name) {
        TextView nameTextView = (TextView) view.findViewById(R.id.item_schedule_textview_name);
        nameTextView.setText(name);
    }

    @Override
    public String getTime() {
        TextView timeTextView = (TextView) view.findViewById(R.id.item_schedule_textview_time);
        return timeTextView.getText().toString();
    }

    @Override
    public void setTime(String time) {
        TextView timeTextView = (TextView) view.findViewById(R.id.item_schedule_textview_time);
        timeTextView.setText(time);
    }

    @Override
    public String getSponsors() {
        TextView sponsorsTextView = (TextView) view.findViewById(R.id.item_schedule_textview_sponsors);
        return sponsorsTextView.getText().toString();
    }

    @Override
    public void setSponsors(String sponsors) {
        TextView sponsorsTextView = (TextView) view.findViewById(R.id.item_schedule_textview_sponsors);
        sponsorsTextView.setText(sponsors);
        sponsorsTextView.setVisibility(sponsors == null || sponsors.length() == 0 ? View.GONE : View.VISIBLE);
    }

    @Override
    public String getEventType() {
        TextView eventTypeTextView = (TextView) view.findViewById(R.id.item_schedule_textview_event_type);
        return eventTypeTextView.getText().toString();
    }

    @Override
    public void setEventType(String eventType) {
        TextView eventTypeTextView = (TextView) view.findViewById(R.id.item_schedule_textview_event_type);
        eventTypeTextView.setText(eventType);
    }

    @Override
    public String getTrack() {
        TextView trackTextView = (TextView) view.findViewById(R.id.item_schedule_textview_track);
        return trackTextView.getText().toString();
    }

    @Override
    public void setTrack(String track) {
        TextView trackTextView = (TextView) view.findViewById(R.id.item_schedule_textview_track);
        trackTextView.setText(track);
        trackTextView.setVisibility(track == null || track.length() == 0 ? View.INVISIBLE : View.VISIBLE);
    }

    @Override
    public void setSummitTypeColor(String color) {
        if (color == null || color.length() == 0) {
            return;
        }

        View summitTypeColorView = (View) view.findViewById(R.id.item_schedule_view_summit_type_color);
        summitTypeColorView.setBackgroundColor(Color.parseColor(color));
    }

    @Override
    public Boolean getScheduled() {
        return true;
    }

    @Override
    public void setScheduled(Boolean scheduled) {

    }

    @Override
    public Boolean getIsScheduledStatusVisible() {
        return null;
    }

    @Override
    public void setIsScheduledStatusVisible(Boolean isScheduledStatusVisible) {

    }
}