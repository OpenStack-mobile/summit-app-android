package org.openstack.android.summit.modules.teams_notifications_inbox.user_interface;

import org.openstack.android.summit.common.DTOs.TeamDTO;
import org.openstack.android.summit.common.user_interface.IBaseView;

import java.util.List;

/**
 * Created by smarcet on 2/13/17.
 */

public interface ITeamsListView extends IBaseView {
    void setData(List<TeamDTO> list);
}
