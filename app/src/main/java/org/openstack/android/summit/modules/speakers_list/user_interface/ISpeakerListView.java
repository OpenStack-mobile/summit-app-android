package org.openstack.android.summit.modules.speakers_list.user_interface;

import org.openstack.android.summit.common.DTOs.PersonListItemDTO;
import org.openstack.android.summit.common.user_interface.IBaseView;

import java.util.List;

/**
 * Created by Claudio Redi on 1/21/2016.
 */
public interface ISpeakerListView extends IBaseView {
    void setSpeakers(List<PersonListItemDTO> speakers);
}
