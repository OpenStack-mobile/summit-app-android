package org.openstack.android.summit.modules.venue_list.user_interface;

import android.graphics.Color;
import android.view.View;
import android.widget.TextView;

import org.openstack.android.summit.R;

/**
 * Created by Claudio Redi on 2/11/2016.
 */
public class VenueListItemView implements IVenueListItemView {
    private View view;

    public VenueListItemView(View view) {
        this.view = view;
    }

    public void setName(String name) {
        TextView nameTextView = (TextView) view.findViewById(R.id.item_venue_name);
        nameTextView.setText(name);
    }
}
