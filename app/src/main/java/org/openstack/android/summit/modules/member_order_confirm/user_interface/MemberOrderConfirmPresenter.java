package org.openstack.android.summit.modules.member_order_confirm.user_interface;

import org.openstack.android.summit.R;
import org.openstack.android.summit.common.DTOs.NonConfirmedSummitAttendeeDTO;
import org.openstack.android.summit.common.entities.exceptions.NotFoundEntityException;
import org.openstack.android.summit.common.entities.exceptions.ValidationException;
import org.openstack.android.summit.common.user_interface.AlertsBuilder;
import org.openstack.android.summit.common.user_interface.BasePresenter;
import org.openstack.android.summit.modules.member_order_confirm.IMemberOrderConfirmWireframe;
import org.openstack.android.summit.modules.member_order_confirm.business_logic.IMemberOrderConfirmInteractor;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;

/**
 * Created by Claudio Redi on 3/27/2016.
 */
public class MemberOrderConfirmPresenter
        extends BasePresenter<IMemberOrderConfirmView, IMemberOrderConfirmInteractor, IMemberOrderConfirmWireframe>
        implements IMemberOrderConfirmPresenter {

    private List<NonConfirmedSummitAttendeeDTO> nonConfirmedSummitAttendeeDTOs;
    private String orderNumber;

    public MemberOrderConfirmPresenter(IMemberOrderConfirmInteractor interactor, IMemberOrderConfirmWireframe wireframe) {
        super(interactor, wireframe);
    }

    @Override
    public void confirmOrder(final String orderNumber) {
        view.showActivityIndicator();

        this.orderNumber = orderNumber;

        try {
            interactor.getAttendeesForTicketOrder(orderNumber)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe((list) -> {
                        nonConfirmedSummitAttendeeDTOs = list;
                        if (list.size() == 0) {
                            AlertsBuilder.buildError(view.getFragmentActivity(), R.string.order_not_found).show();
                        }
                        else if (list.size() == 1) {
                            selectAttendeeFromOrderList(0);
                        }
                        else if (list.size() > 1) {
                            view.setAttendees(list);
                        }

                        view.hideActivityIndicator();

                    }, (ex) -> {
                        view.hideActivityIndicator();
                        try{
                            throw ex;
                        }
                        catch (NotFoundEntityException ex1){
                            view.hideActivityIndicator();
                            AlertsBuilder.buildValidationError(view.getFragmentActivity() ,ex1.getMessage()).show();
                        }
                        catch (ValidationException ex2){
                            view.hideActivityIndicator();
                            AlertsBuilder.buildValidationError(view.getFragmentActivity() ,ex2.getMessage()).show();
                        }
                        catch (Throwable ex3){
                            view.hideActivityIndicator();
                            AlertsBuilder.buildGenericError(view.getFragmentActivity()).show();
                        }
                    });
        }

        catch (Exception ex){
            view.hideActivityIndicator();
            AlertsBuilder.buildGenericError(view.getFragmentActivity()).show();
        }
    }

    public void selectAttendeeFromOrderList(int position) {
        view.showActivityIndicator();

        NonConfirmedSummitAttendeeDTO nonConfirmedSummitAttendee =  nonConfirmedSummitAttendeeDTOs.get(position);

        try {
            interactor.selectAttendeeFromOrderList(orderNumber, nonConfirmedSummitAttendee.getId())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe((res) -> {
                        interactor.bindCurrentUser();
                        view.hideActivityIndicator();
                    }, (ex) -> {
                        view.hideActivityIndicator();
                        try{
                            throw ex;
                        }
                        catch (NotFoundEntityException ex1){
                            view.hideActivityIndicator();
                            AlertsBuilder.buildValidationError(view.getFragmentActivity() ,ex1.getMessage()).show();
                        }
                        catch (ValidationException ex2){
                            view.hideActivityIndicator();
                            AlertsBuilder.buildValidationError(view.getFragmentActivity() ,ex2.getMessage()).show();
                        }
                        catch (Throwable ex3){
                            view.hideActivityIndicator();
                            AlertsBuilder.buildGenericError(view.getFragmentActivity()).show();
                        }
                    });
        }
        catch (Exception ex){
            view.hideActivityIndicator();
            AlertsBuilder.buildGenericError(view.getFragmentActivity()).show();
        }
    }

    @Override
    public void cancelOrder() {
        wireframe.back(view);
    }
}
