package org.openstack.android.summit.modules.member_order_confirm.business_logic;

import org.openstack.android.summit.OpenStackSummitApplication;
import org.openstack.android.summit.common.DTOs.Assembler.IDTOAssembler;
import org.openstack.android.summit.common.DTOs.NonConfirmedSummitAttendeeDTO;
import org.openstack.android.summit.common.api.ISummitSelector;
import org.openstack.android.summit.common.business_logic.BaseInteractor;
import org.openstack.android.summit.common.business_logic.IInteractorAsyncOperationListener;
import org.openstack.android.summit.common.data_access.IDataStoreOperationListener;
import org.openstack.android.summit.common.data_access.IMemberRemoteDataStore;
import org.openstack.android.summit.common.data_access.repositories.ISummitDataStore;
import org.openstack.android.summit.common.data_access.deserialization.DataStoreOperationListener;
import org.openstack.android.summit.common.entities.NonConfirmedSummitAttendee;
import org.openstack.android.summit.common.entities.exceptions.ValidationException;
import org.openstack.android.summit.common.network.IReachability;
import org.openstack.android.summit.common.security.ISecurityManager;
import java.util.List;

import io.reactivex.Observable;

/**
 * Created by Claudio Redi on 3/27/2016.
 */
public class MemberOrderConfirmInteractor
        extends BaseInteractor
        implements IMemberOrderConfirmInteractor {

    IMemberRemoteDataStore memberRemoteDataStore;

    public MemberOrderConfirmInteractor(IMemberRemoteDataStore memberRemoteDataStore,ISecurityManager securityManager, IDTOAssembler dtoAssembler, ISummitDataStore summitDataStore, ISummitSelector summitSelector, IReachability reachability) {
        super(securityManager, dtoAssembler, summitSelector, summitDataStore, reachability);

        this.memberRemoteDataStore = memberRemoteDataStore;
    }

    @Override
    public Observable<List<NonConfirmedSummitAttendeeDTO>> getAttendeesForTicketOrder(String orderNumber) throws Exception {

        if (!reachability.isNetworkingAvailable(OpenStackSummitApplication.context)) {
           throw new ValidationException("Order can't be confirmed, there is no connectivity");
        }

        return memberRemoteDataStore.getAttendeesForTicketOrder(orderNumber)
                .map(list -> createDTOList(list, NonConfirmedSummitAttendeeDTO.class));
    }

    @Override
    public Observable<Boolean> selectAttendeeFromOrderList(String orderNumber, int externalAttendeeId) throws Exception {

        if (!reachability.isNetworkingAvailable(OpenStackSummitApplication.context)) {
            throw new ValidationException("Order can't be confirmed, there is no connectivity");
        }
        return memberRemoteDataStore.selectAttendeeFromOrderList(orderNumber, externalAttendeeId);
    }

    @Override
    public void bindCurrentUser() {
        securityManager.bindCurrentUser();
    }
}
