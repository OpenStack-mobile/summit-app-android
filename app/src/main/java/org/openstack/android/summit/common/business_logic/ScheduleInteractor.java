package org.openstack.android.summit.common.business_logic;

import android.util.Log;
import org.joda.time.DateTime;
import org.openstack.android.summit.common.Constants;
import org.openstack.android.summit.common.DTOs.Assembler.IDTOAssembler;
import org.openstack.android.summit.common.DTOs.ScheduleItemDTO;
import org.openstack.android.summit.common.DTOs.SummitDTO;
import org.openstack.android.summit.common.push_notifications.IPushNotificationsManager;
import org.openstack.android.summit.common.ISession;
import org.openstack.android.summit.common.data_access.ISummitAttendeeDataStore;
import org.openstack.android.summit.common.data_access.ISummitDataStore;
import org.openstack.android.summit.common.data_access.ISummitEventDataStore;
import org.openstack.android.summit.common.data_access.deserialization.DataStoreOperationListener;
import org.openstack.android.summit.common.entities.Member;
import org.openstack.android.summit.common.entities.Summit;
import org.openstack.android.summit.common.entities.SummitEvent;
import org.openstack.android.summit.common.security.ISecurityManager;

import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;

/**
 * Created by Claudio Redi on 11/18/2015.
 */
public class ScheduleInteractor extends ScheduleableInteractor implements IScheduleInteractor {

    private ISession session;
    private final String PUSH_NOTIFICATIONS_SUBSCRIBED_KEY = "PUSH_NOTIFICATIONS_SUBSCRIBED_KEY";

    @Inject
    public ScheduleInteractor(ISummitEventDataStore summitEventDataStore, ISummitDataStore summitDataStore, ISummitAttendeeDataStore summitAttendeeDataStore, IDTOAssembler dtoAssembler, ISecurityManager securityManager, IPushNotificationsManager pushNotificationsManager, ISession session) {
        super(summitEventDataStore, summitAttendeeDataStore, summitDataStore, dtoAssembler, securityManager, pushNotificationsManager);
        this.summitDataStore = summitDataStore;
        this.session         = session;
    }

    @Override
    public List<ScheduleItemDTO> getScheduleEvents(DateTime startDate, DateTime endDate, List<Integer> eventTypes, List<Integer> summitTypes, List<Integer> trackGroups, List<Integer> tracks, List<String> tags, List<String> levels, List<Integer> venues) {
        return createDTOList(
                summitEventDataStore.getByFilterLocal
                (
                                startDate,
                                endDate,
                                eventTypes,
                                summitTypes,
                                trackGroups,
                                tracks,
                                tags,
                                levels,
                                venues
                ),
                ScheduleItemDTO.class
        );
    }

    @Override
    public List<DateTime> getDatesWithoutEvents(DateTime startDate, DateTime endDate, List<Integer> eventTypes, List<Integer> summitTypes, List<Integer> trackGroups, List<Integer> tracks, List<String> tags, List<String> levels, List<Integer> venues) {
        ArrayList<DateTime> inactiveDates = new ArrayList<>();
        List<SummitEvent> events;

        while(startDate.isBefore(endDate)) {
           events = summitEventDataStore.getByFilterLocal(
                    startDate.withTime(0, 0, 0, 0),
                    startDate.withTime(23, 59, 59, 999),
                    eventTypes,
                    summitTypes,
                    trackGroups,
                    tracks,
                    tags,
                    levels,
                   venues);
            if (events.size() == 0) {
                inactiveDates.add(startDate);
            }
            startDate = startDate.plusDays(1);
        }

        return inactiveDates;
    }

    @Override
    public boolean eventExist(int id) {
        SummitEvent summitEvent = summitEventDataStore.getByIdLocal(id);
        return summitEvent != null;
    }

    @Override
    public SummitDTO getLocalActiveSummit(){
        Summit currentSummit = summitDataStore.getActiveLocal();
        if(currentSummit == null) return null;
        return dtoAssembler.createDTO(currentSummit, SummitDTO.class);
    }

    @Override
    public boolean isDataLoaded() {
        return summitDataStore.getActiveLocal() != null;
    }

    @Override
    public void getActiveSummit(final IInteractorAsyncOperationListener<SummitDTO> delegate) {
        DataStoreOperationListener<Summit> dataStoreOperationListener = new DataStoreOperationListener<Summit>() {
            @Override
            public void onSucceedWithSingleData(Summit data) {
                if (delegate != null) {
                    try{
                        SummitDTO summitDTO = dtoAssembler.createDTO(data, SummitDTO.class);
                        delegate.onSucceedWithData(summitDTO);
                    } catch (Exception e) {
                        String friendlyError = "Error getting summit information";
                        Log.e(Constants.LOG_TAG, friendlyError, e);
                        delegate.onError(friendlyError);
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
