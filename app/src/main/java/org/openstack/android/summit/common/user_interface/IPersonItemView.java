package org.openstack.android.summit.common.user_interface;

import android.net.Uri;

/**
 * Created by Claudio Redi on 1/13/2016.
 */
public interface IPersonItemView {
    String getName();

    void setName(String name);

    String getTitle();

    void setTitle(String title);

    void setPictureUri(Uri pictureUri);
}

