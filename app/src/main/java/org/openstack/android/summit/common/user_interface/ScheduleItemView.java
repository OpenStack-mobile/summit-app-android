package org.openstack.android.summit.common.user_interface;

import android.graphics.Color;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.openstack.android.summit.R;

/**
 * Created by Claudio Redi on 12/23/2015.
 */
public class ScheduleItemView implements IScheduleItemView {
    private View view;
    private boolean scheduled;

    public ScheduleItemView(View view) {
        this.view = view;
    }

    @Override
    public void setName(String name) {
        TextView nameTextView = (TextView) view.findViewById(R.id.item_schedule_textview_name);
        nameTextView.setText(name);
    }

    @Override
    public void setTime(String time) {
        TextView timeTextView = (TextView) view.findViewById(R.id.item_schedule_textview_time);
        timeTextView.setText(time);
    }

    @Override
    public void setSponsors(String sponsors) {
        TextView sponsorsTextView = (TextView) view.findViewById(R.id.item_schedule_textview_sponsors);
        sponsorsTextView.setText(sponsors);
        sponsorsTextView.setVisibility(sponsors == null || sponsors.length() == 0 ? View.GONE : View.VISIBLE);
    }

    @Override
    public void setEventType(String eventType) {
        TextView eventTypeTextView = (TextView) view.findViewById(R.id.item_schedule_textview_event_type);
        eventTypeTextView.setText(eventType);
    }

    @Override
    public void setTrack(String track) {
        TextView trackTextView = (TextView) view.findViewById(R.id.item_schedule_textview_track);
        trackTextView.setText(track);
        trackTextView.setVisibility(track == null || track.length() == 0 ? View.INVISIBLE : View.VISIBLE);
    }

    @Override
    public void setLocation(String location) {
        TextView locationTextView = (TextView) view.findViewById(R.id.item_schedule_textview_location);
        locationTextView.setText(location);
        ((LinearLayout)locationTextView.getParent()).setVisibility(location == null || location.length() == 0 ? View.GONE : View.VISIBLE);
    }

    @Override
    public void setColor(String color) {
        View colorView = view.findViewById(R.id.item_schedule_view_color);
        TextView trackTextView = (TextView) view.findViewById(R.id.item_schedule_textview_track);

        if (color == null || color.length() == 0) {
            colorView.setVisibility(View.INVISIBLE);
            trackTextView.setTextColor(view.getResources().getColor(R.color.openStackGray));
        }
        else {
            colorView.setVisibility(View.VISIBLE);
            colorView.setBackgroundColor(Color.parseColor(color));

            trackTextView.setTextColor(Color.parseColor(color));
        }
    }

    @Override
    public Boolean getScheduled() {
        return scheduled;
    }

    @Override
    public void setScheduled(Boolean scheduled) {
        this.scheduled = scheduled;
        ImageButton scheduledStatusImageButton = (ImageButton)view.findViewById(R.id.item_schedule_imagebutton_scheduled);
        if (scheduled) {
            scheduledStatusImageButton.setImageResource(R.drawable.checked_active);
        }
        else {
            scheduledStatusImageButton.setImageResource(R.drawable.unchecked);
        }
    }

    @Override
    public void setIsScheduledStatusVisible(Boolean isScheduledStatusVisible) {
        ImageButton scheduledStatusImageButton = (ImageButton)view.findViewById(R.id.item_schedule_imagebutton_scheduled);
        scheduledStatusImageButton.setVisibility(isScheduledStatusVisible ? View.VISIBLE : View.INVISIBLE);
    }
}