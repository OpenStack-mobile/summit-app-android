package org.openstack.android.summit.common.entities;

import java.util.Date;

/**
 * Created by sebastian on 8/10/2016.
 */
public interface IPresentationVideo extends IPresentationMaterial {

    String getYouTubeId();
    void setYouTubeId(String youTubeId);

    boolean isHighlighted();
    void setHighlighted(boolean highlighted);

    long getViews();
    void setViews(long views);

    Date getDateUploaded();
    void setDateUploaded(Date dateUploaded);
}
