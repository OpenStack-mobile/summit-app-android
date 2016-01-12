package org.openstack.android.summit.modules.track_list.user_interface;

import org.openstack.android.summit.common.DTOs.NamedDTO;
import org.openstack.android.summit.common.user_interface.IBaseFragment;

import java.util.List;

/**
 * Created by Claudio Redi on 1/12/2016.
 */
public interface ITrackListFragment extends IBaseFragment {
    void setTracks(List<NamedDTO> tracks);
    void reloadData();
}
