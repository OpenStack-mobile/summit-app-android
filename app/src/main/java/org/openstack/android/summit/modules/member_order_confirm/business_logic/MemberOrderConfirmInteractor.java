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
import org.openstack.android.summit.common.network.IReachability;
import org.openstack.android.summit.common.security.ISecurityManager;
import java.util.List;

/**
 * Created by Claudio Redi on 3/27/2016.
 */
public class MemberOrderConfirmInteractor extends BaseInteractor implements IMemberOrderConfirmInteractor {
    IMemberRemoteDataStore memberRemoteDataStore;
    IReachability reachability;
    ISecurityManager securityManager;

    public MemberOrderConfirmInteractor(IMemberRemoteDataStore memberRemoteDataStore, IReachability reachability, ISecurityManager securityManager, IDTOAssembler dtoAssembler, ISummitDataStore summitDataStore, ISummitSelector summitSelector) {
        super(dtoAssembler, summitSelector, summitDataStore);
        this.memberRemoteDataStore = memberRemoteDataStore;
        this.reachability = reachability;
        this.securityManager = securityManager;
    }

    @Override
    public void getAttendeesForTicketOrder(String orderNumber, final IInteractorAsyncOperationListener<List<NonConfirmedSummitAttendeeDTO>> interactorAsyncOperationListener) {
        String error;
        if (!reachability.isNetworkingAvailable(OpenStackSummitApplication.context)) {
            error = "Order can't be confirmed, there is no connectivity";
            interactorAsyncOperationListener.onError(error);
            return;
        }

        //TODO logged in

        IDataStoreOperationListener<NonConfirmedSummitAttendee> dataStoreOperationListener = new DataStoreOperationListener<NonConfirmedSummitAttendee>() {

            @Override
            public void onSucceedWithDataCollection(List<NonConfirmedSummitAttendee> data) {
                List<NonConfirmedSummitAttendeeDTO> nonConfirmedSummitAttendeeDTOs = createDTOList(data, NonConfirmedSummitAttendeeDTO.class);
                interactorAsyncOperationListener.onSucceedWithData(nonConfirmedSummitAttendeeDTOs);
            }

            @Override
            public void onError(String message) {
                interactorAsyncOperationListener.onError(message);
            }
        };

        /*List<NonConfirmedSummitAttendeeDTO> nonConfirmedSummitAttendees = new ArrayList<>();
        NonConfirmedSummitAttendeeDTO nonConfirmedSummitAttendee = new NonConfirmedSummitAttendeeDTO();
        nonConfirmedSummitAttendee.setId(12345678);
        nonConfirmedSummitAttendee.setName("Claudio Redi");
        nonConfirmedSummitAttendees.add(nonConfirmedSummitAttendee);

        nonConfirmedSummitAttendee = new NonConfirmedSummitAttendeeDTO();
        nonConfirmedSummitAttendee.setId(22345678);
        nonConfirmedSummitAttendee.setName("Valentino Redi");
        nonConfirmedSummitAttendees.add(nonConfirmedSummitAttendee);

        interactorAsyncOperationListener.onSucceedWithData(nonConfirmedSummitAttendees);*/

        memberRemoteDataStore.getAttendeesForTicketOrder(orderNumber, dataStoreOperationListener);
    }

    @Override
    public void selectAttendeeFromOrderList(String orderNumber, int externalAttendeeId, final IInteractorAsyncOperationListener<Void> interactorAsyncOperationListener) {
        String error;
        if (!reachability.isNetworkingAvailable(OpenStackSummitApplication.context)) {
            error = "Order can't be confirmed, there is no connectivity";
            interactorAsyncOperationListener.onError(error);
            return;
        }

        //TODO logged in

        IDataStoreOperationListener<NonConfirmedSummitAttendee> dataStoreOperationListener = new DataStoreOperationListener<NonConfirmedSummitAttendee>() {

            @Override
            public void onSucceedWithoutData() {
                interactorAsyncOperationListener.onSucceed();
            }

            @Override
            public void onError(String message) {
                interactorAsyncOperationListener.onError(message);
            }
        };

        //interactorAsyncOperationListener.onSucceed();
        memberRemoteDataStore.selectAttendeeFromOrderList(orderNumber, externalAttendeeId, dataStoreOperationListener);
    }

    @Override
    public void bindCurrentUser() {
        securityManager.bindCurrentUser();
    }
}
