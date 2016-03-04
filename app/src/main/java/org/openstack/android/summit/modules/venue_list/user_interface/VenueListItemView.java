package org.openstack.android.summit.modules.venue_list.user_interface;

import android.graphics.Color;
import android.net.Uri;
import android.view.View;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;

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

    public void setAddress(String address) {
        TextView addressTextView = (TextView) view.findViewById(R.id.item_venue_address);
        addressTextView.setText(address);
    }

    public void setPictureUri(Uri pictureUri) {
        SimpleDraweeView draweeView = (SimpleDraweeView) view.findViewById(R.id.item_venue_image);
        draweeView.setImageURI(pictureUri);
    }
}
