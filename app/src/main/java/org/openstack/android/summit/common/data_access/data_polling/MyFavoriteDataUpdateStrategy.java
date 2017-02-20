package org.openstack.android.summit.common.data_access.data_polling;

import org.openstack.android.summit.common.api.ISummitSelector;
import org.openstack.android.summit.common.data_access.repositories.IMemberDataStore;
import org.openstack.android.summit.common.entities.DataUpdate;
import org.openstack.android.summit.common.entities.Member;
import org.openstack.android.summit.common.entities.SummitEvent;
import org.openstack.android.summit.common.security.ISecurityManager;

/**
 * Created by smarcet on 2/17/17.
 */

public class MyFavoriteDataUpdateStrategy extends DataUpdateStrategy {

    ISecurityManager securityManager;
    IMemberDataStore memberDataStore;

    public MyFavoriteDataUpdateStrategy(IMemberDataStore memberDataStore, ISecurityManager securityManager, ISummitSelector summitSelector) {
        super(summitSelector);
        this.memberDataStore = memberDataStore;
        this.securityManager = securityManager;
    }

    @Override
    public void process(DataUpdate dataUpdate) {
        Member currentMember = securityManager.getCurrentMember();

        if(currentMember == null) return;

        switch (dataUpdate.getOperation()) {
            case DataOperation.Insert:
            case DataOperation.Update:
                memberDataStore.addEventToMyFavoritesLocal(currentMember, (SummitEvent)dataUpdate.getEntity());
                break;
            case DataOperation.Delete:
                memberDataStore.removeEventFromMyFavoritesLocal(currentMember, (SummitEvent)dataUpdate.getEntity());
                break;
        }
    }
}
