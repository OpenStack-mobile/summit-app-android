package org.openstack.android.summit.common.data_access.data_polling;

import org.openstack.android.summit.common.api.ISummitSelector;
import org.openstack.android.summit.common.data_access.IGenericDataStore;
import org.openstack.android.summit.common.data_access.ISummitAttendeeDataStore;
import org.openstack.android.summit.common.data_access.ISummitDataStore;
import org.openstack.android.summit.common.entities.DataUpdate;
import org.openstack.android.summit.common.entities.Member;
import org.openstack.android.summit.common.entities.Summit;
import org.openstack.android.summit.common.entities.SummitEvent;
import org.openstack.android.summit.common.security.ISecurityManager;

/**
 * Created by Claudio Redi on 3/4/2016.
 */
public class SummitDataUpdateStrategy extends DataUpdateStrategy {
    ISummitDataStore summitDataStore;

    public SummitDataUpdateStrategy(IGenericDataStore genericDataStore, ISummitDataStore summitDataStore, ISummitSelector summitSelector) {
        super(genericDataStore, summitSelector);
        this.summitDataStore = summitDataStore;
    }

    @Override
    public void process(DataUpdate dataUpdate) {
        switch (dataUpdate.getOperation()) {
            case DataOperation.Update:
                summitDataStore.updateActiveSummitFromDataUpdate((Summit) dataUpdate.getEntity());
        }
    }
}
