package org.openstack.android.summit.common.DTOs;

/**
 * Created by Claudio Redi on 11/18/2015.
 */
public class TrackDTO extends NamedDTO {
    private TrackGroupDTO trackGroup;

    public TrackGroupDTO getTrackGroup() {
        return trackGroup;
    }

    public void setTrackGroup(TrackGroupDTO trackGroup) {
        this.trackGroup = trackGroup;
    }
}
