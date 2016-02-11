package org.openstack.android.summit.modules.level_list.business_logic;

import org.openstack.android.summit.common.business_logic.IBaseInteractor;

import java.util.List;

/**
 * Created by Claudio Redi on 1/11/2016.
 */
public interface ILevelListInteractor extends IBaseInteractor {
    List<String> getLevels();
}
