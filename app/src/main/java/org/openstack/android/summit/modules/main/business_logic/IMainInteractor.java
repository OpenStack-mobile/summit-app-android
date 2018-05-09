package org.openstack.android.summit.modules.main.business_logic;

import org.openstack.android.summit.common.DTOs.EventDetailDTO;
import org.openstack.android.summit.common.business_logic.IBaseInteractor;

import io.reactivex.Observable;

/**
 * Created by Claudio Redi on 2/12/2016.
 */
public interface IMainInteractor extends IBaseInteractor {

    void subscribeToPushNotifications();

    boolean isNetworkingAvailable();

    long getNotReadNotificationsCount();

    void unSubscribeToPushNotifications();

    Observable<EventDetailDTO> getEventById(int eventId);
}
