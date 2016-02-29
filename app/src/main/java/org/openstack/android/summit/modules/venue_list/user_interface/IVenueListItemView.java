package org.openstack.android.summit.modules.venue_list.user_interface;

import android.net.Uri;

/**
 * Created by Claudio Redi on 2/11/2016.
 */
public interface IVenueListItemView {
    void setName(String name);

    void setAddress(String address);

    void setPictureUri(Uri pictureUri);
}
