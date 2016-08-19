package org.openstack.android.summit.modules.member_order_confirm.user_interface;

import org.openstack.android.summit.R;
import org.openstack.android.summit.common.DTOs.NonConfirmedSummitAttendeeDTO;
import org.openstack.android.summit.common.business_logic.IInteractorAsyncOperationListener;
import org.openstack.android.summit.common.business_logic.InteractorAsyncOperationListener;
import org.openstack.android.summit.common.entities.NonConfirmedSummitAttendee;
import org.openstack.android.summit.common.user_interface.BasePresenter;
import org.openstack.android.summit.modules.member_order_confirm.IMemberOrderConfirmWireframe;
import org.openstack.android.summit.modules.member_order_confirm.business_logic.IMemberOrderConfirmInteractor;

import java.util.List;

/**
 * Created by Claudio Redi on 3/27/2016.
 */
public class MemberOrderConfirmPresenter extends BasePresenter<IMemberOrderConfirmView, IMemberOrderConfirmInteractor, IMemberOrderConfirmWireframe> implements IMemberOrderConfirmPresenter {
    private List<NonConfirmedSummitAttendeeDTO> nonConfirmedSummitAttendeeDTOs;
    private String orderNumber;

    public MemberOrderConfirmPresenter(IMemberOrderConfirmInteractor interactor, IMemberOrderConfirmWireframe wireframe) {
        super(interactor, wireframe);
    }

    @Override
    public void confirmOrder(final String orderNumber) {
        view.showActivityIndicator();

        this.orderNumber = orderNumber;

        IInteractorAsyncOperationListener<List<NonConfirmedSummitAttendeeDTO>> interactorAsyncOperationListener = new InteractorAsyncOperationListener<List<NonConfirmedSummitAttendeeDTO>>() {
            @Override
            public void onSucceedWithData(List<NonConfirmedSummitAttendeeDTO> data) {
                nonConfirmedSummitAttendeeDTOs = data;

                if (nonConfirmedSummitAttendeeDTOs.size() == 0) {
                    view.showInfoMessage(view.getResources().getString(R.string.order_not_found));
                }
                else if (nonConfirmedSummitAttendeeDTOs.size() == 1) {
                    selectAttendeeFromOrderList(0);
                }
                else if (nonConfirmedSummitAttendeeDTOs.size() > 1) {
                    view.setAttendees(data);
                }

                view.hideActivityIndicator();
            }

            @Override
            public void onError(String message) {
                String friendlyMessage = message;
                if (message.startsWith("412")) {
                    friendlyMessage = view.getResources().getString(R.string.eventbrite_order_already_in_use);
                }
                else if (message.startsWith("404")) {
                    friendlyMessage = view.getResources().getString(R.string.order_not_found);
                }

                view.hideActivityIndicator();
                view.showErrorMessage(friendlyMessage);
            }
        };

        interactor.getAttendeesForTicketOrder(orderNumber, interactorAsyncOperationListener);
    }

    public void selectAttendeeFromOrderList(int position) {
        view.showActivityIndicator();

        NonConfirmedSummitAttendeeDTO nonConfirmedSummitAttendee =  nonConfirmedSummitAttendeeDTOs.get(position);

        IInteractorAsyncOperationListener<Void> interactorAsyncOperationListener = new InteractorAsyncOperationListener<Void>() {
            @Override
            public void onSucceed() {
                interactor.bindCurrentUser();
                view.hideActivityIndicator();
            }

            @Override
            public void onError(String message) {
                String friendlyMessage = message;
                if (message.startsWith("412")) {
                    friendlyMessage = view.getResources().getString(R.string.eventbrite_order_already_in_use);
                }
                else if (message.startsWith("404")) {
                    friendlyMessage = view.getResources().getString(R.string.order_not_found);
                }

                view.hideActivityIndicator();
                view.showErrorMessage(friendlyMessage);
            }
        };

        interactor.selectAttendeeFromOrderList(orderNumber,nonConfirmedSummitAttendee.getId(), interactorAsyncOperationListener);
    }
}
