package org.openstack.android.summit.modules.level_list.business_logic;

import org.openstack.android.summit.common.data_access.ISummitEventDataStore;

import java.util.List;

import javax.inject.Inject;

/**
 * Created by Claudio Redi on 1/11/2016.
 */
public class LevelListInteractor implements ILevelListInteractor {
    ISummitEventDataStore summitEventDataStore;

    @Inject
    public LevelListInteractor(ISummitEventDataStore summitEventDataStore) {
        this.summitEventDataStore = summitEventDataStore;
    }

    @Override
    public List<String> getLevels() {
        return summitEventDataStore.getPresentationLevelsLocal();
    }
}
