package org.openstack.android.summit.common.data_access.data_polling;

import org.openstack.android.summit.common.api.ISummitSelector;
import org.openstack.android.summit.common.entities.DataUpdate;
import org.openstack.android.summit.common.entities.Member;
import org.openstack.android.summit.common.entities.SummitEvent;
import org.openstack.android.summit.common.security.ISecurityManager;

import io.realm.RealmObject;

/**
 * Created by smarcet on 1/31/17.
 */

public class SummitGroupEventDataUpdateStrategy extends DataUpdateStrategy {

    ISecurityManager securityManager;

    public SummitGroupEventDataUpdateStrategy
    (
        ISecurityManager securityManager,
        ISummitSelector summitSelector
    )
    {
        super(summitSelector);
        this.securityManager = securityManager;
    }

    @Override
    public void process(DataUpdate dataUpdate) throws DataUpdateException {
        Member currentMember = securityManager.getCurrentMember();

        if(currentMember == null) return;

        if(dataUpdate.getEntity()  == null) return;
        if(dataUpdate.getEntityType() == null) return;
        if(!(dataUpdate.getEntity() instanceof SummitEvent)) return;
        RealmObject entity = dataUpdate.getEntity();
        ((SummitEvent)entity).getGroupEvent().setOwner(currentMember);
        super.process(dataUpdate);

    }
}
