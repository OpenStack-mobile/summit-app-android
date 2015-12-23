package org.openstack.android.summit.common.user_interface;

import android.view.View;
import android.widget.TextView;

import org.openstack.android.summit.R;

/**
 * Created by Claudio Redi on 12/23/2015.
 */
public class ScheduleItem implements IScheduleItem {
    private View view;

    public ScheduleItem(View view) {
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

}
