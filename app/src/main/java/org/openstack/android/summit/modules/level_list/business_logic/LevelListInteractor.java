package org.openstack.android.summit.modules.level_list.business_logic;

import org.openstack.android.summit.common.DTOs.Assembler.IDTOAssembler;
import org.openstack.android.summit.common.api.ISummitSelector;
import org.openstack.android.summit.common.business_logic.BaseInteractor;
import org.openstack.android.summit.common.data_access.ISummitDataStore;
import org.openstack.android.summit.common.data_access.ISummitEventDataStore;
import java.util.List;
import javax.inject.Inject;

/**
 * Created by Claudio Redi on 1/11/2016.
 */
public class LevelListInteractor extends BaseInteractor implements ILevelListInteractor {
    ISummitEventDataStore summitEventDataStore;

    @Inject
    public LevelListInteractor(ISummitEventDataStore summitEventDataStore, IDTOAssembler dtoAssembler, ISummitDataStore summitDataStore, ISummitSelector summitSelector) {
        super(dtoAssembler, summitSelector, summitDataStore);
        this.summitEventDataStore = summitEventDataStore;
    }

    @Override
    public List<String> getLevels() {
        return summitEventDataStore.getPresentationLevelsLocal();
    }
}
