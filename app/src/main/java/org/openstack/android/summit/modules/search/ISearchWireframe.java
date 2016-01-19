package org.openstack.android.summit.modules.search;

import android.support.v4.app.FragmentActivity;

import org.openstack.android.summit.common.DTOs.NamedDTO;

/**
 * Created by Claudio Redi on 1/14/2016.
 */
public interface ISearchWireframe {
    void presentSearchView(String searchTerm, FragmentActivity context);

    void showTrackSchedule(NamedDTO track, FragmentActivity activity);
}
