package org.openstack.android.summit.common.business_logic;

import android.util.Log;

import com.google.api.client.util.store.DataStore;

import org.json.JSONException;
import org.openstack.android.summit.common.Constants;
import org.openstack.android.summit.common.DTOs.Assembler.IDTOAssembler;
import org.openstack.android.summit.common.DTOs.ScheduleItemDTO;
import org.openstack.android.summit.common.DTOs.SummitDTO;
import org.openstack.android.summit.common.IPushNotificationsManager;
import org.openstack.android.summit.common.ISession;
import org.openstack.android.summit.common.data_access.IDataStoreOperationListener;
import org.openstack.android.summit.common.data_access.ISummitAttendeeDataStore;
import org.openstack.android.summit.common.data_access.ISummitDataStore;
import org.openstack.android.summit.common.data_access.ISummitEventDataStore;
import org.openstack.android.summit.common.data_access.data_polling.IDataUpdatePoller;
import org.openstack.android.summit.common.data_access.deserialization.DataStoreOperationListener;
import org.openstack.android.summit.common.entities.Member;
import org.openstack.android.summit.common.entities.Summit;
import org.openstack.android.summit.common.entities.SummitEvent;
import org.openstack.android.summit.common.security.ISecurityManager;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

/**
 * Created by Claudio Redi on 11/18/2015.
 */
public class ScheduleInteractor extends ScheduleableInteractor implements IScheduleInteractor {
    protected ISummitDataStore summitDataStore;
    private IPushNotificationsManager pushNotificationsManager;
    private ISession session;
    private final String PUSH_NOTIFICATIONS_SUBSCRIBED_KEY = "PUSH_NOTIFICATIONS_SUBSCRIBED_KEY";

    @Inject
    public ScheduleInteractor(ISummitEventDataStore summitEventDataStore, ISummitDataStore summitDataStore, ISummitAttendeeDataStore summitAttendeeDataStore, IDTOAssembler dtoAssembler, ISecurityManager securityManager, IPushNotificationsManager pushNotificationsManager, ISession session, IDataUpdatePoller dataUpdatePoller) {
        super(summitEventDataStore, summitAttendeeDataStore, dtoAssembler, securityManager, dataUpdatePoller);
        this.summitDataStore = summitDataStore;
        this.pushNotificationsManager = pushNotificationsManager;
        this.session = session;
    }

    @Override
    public List<ScheduleItemDTO> getScheduleEvents(Date startDate, Date endDate, List<Integer> eventTypes, List<Integer> summitTypes, List<Integer> trackGroups, List<Integer> tracks, List<String> tags, List<String> levels) {
        List<SummitEvent> summitEvents = summitEventDataStore.getByFilterLocal(startDate, endDate, eventTypes, summitTypes, trackGroups, tracks, tags, levels);

        List<ScheduleItemDTO> dtos = createDTOList(summitEvents, ScheduleItemDTO.class);

        return dtos;
    }

    @Override
    public void subscribeToPushChannelsUsingContextIfNotDoneAlready() {
        Summit summit = summitDataStore.getActiveLocal();
        if (summit != null && session.getString(PUSH_NOTIFICATIONS_SUBSCRIBED_KEY) == null) {
            if (securityManager.isLoggedIn()) {
                Member member = securityManager.getCurrentMember();
                pushNotificationsManager.subscribeMember(member, summit);
            }
            else {
                pushNotificationsManager.subscribeAnonymous(summit);
            }
            session.setString(PUSH_NOTIFICATIONS_SUBSCRIBED_KEY,"YES");
        }
    }

    @Override
    public void getActiveSummit(final IInteractorAsyncOperationListener<SummitDTO> delegate) {
        DataStoreOperationListener<Summit> dataStoreOperationListener = new DataStoreOperationListener<Summit>() {
            @Override
            public void onSuceedWithSingleData(Summit data) {
                if (delegate != null) {
                    try{
                        SummitDTO summitDTO = dtoAssembler.createDTO(data, SummitDTO.class);
                        delegate.onSucceedWithData(summitDTO);
                    } catch (Exception e) {
                        Log.e(Constants.LOG_TAG, "Error getting active summit", e);
                        delegate.onError(e.getMessage());
                    }
                }
            }

            @Override
            public void onError(String message) {
                if (delegate != null) {
                    delegate.onError(message);
                }
            }
        };
        summitDataStore.getActive(dataStoreOperationListener);
    }
}
