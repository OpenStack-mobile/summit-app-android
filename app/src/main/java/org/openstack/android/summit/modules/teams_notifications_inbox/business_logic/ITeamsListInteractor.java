package org.openstack.android.summit.modules.teams_notifications_inbox.business_logic;

import org.openstack.android.summit.common.DTOs.TeamDTO;
import org.openstack.android.summit.common.business_logic.IBaseInteractor;
import java.util.List;

import io.reactivex.Observable;

/**
 * Created by smarcet on 2/13/17.
 */

public interface ITeamsListInteractor extends IBaseInteractor
{
    Observable<List<TeamDTO>>  loadMyTeams();
}
