package org.openstack.android.summit.modules.track_list;

import android.support.v4.app.FragmentActivity;

import org.openstack.android.summit.common.DTOs.NamedDTO;

/**
 * Created by Claudio Redi on 1/12/2016.
 */
public interface ITrackListWireframe {
    void showTrackSchedule(NamedDTO namedDTO, FragmentActivity activity);
}
