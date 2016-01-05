package org.openstack.android.summit.common.business_logic;

import android.util.Log;

import com.google.api.client.util.store.DataStore;

import org.json.JSONException;
import org.openstack.android.summit.common.Constants;
import org.openstack.android.summit.common.DTOs.Assembler.IDTOAssembler;
import org.openstack.android.summit.common.DTOs.ScheduleItemDTO;
import org.openstack.android.summit.common.DTOs.SummitDTO;
import org.openstack.android.summit.common.data_access.IDataStoreOperationListener;
import org.openstack.android.summit.common.data_access.ISummitDataStore;
import org.openstack.android.summit.common.data_access.ISummitEventDataStore;
import org.openstack.android.summit.common.data_access.deserialization.DataStoreOperationListener;
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
    private ISummitEventDataStore summitEventDataStore;
    private ISummitDataStore summitDataStore;
    private IDTOAssembler dtoAssembler;

    @Inject
    public ScheduleInteractor(ISummitEventDataStore summitEventDataStore, ISummitDataStore summitDataStore, IDTOAssembler dtoAssembler, ISecurityManager securityManager) {
        super(securityManager, summitEventDataStore);
        this.summitDataStore = summitDataStore;
        this.dtoAssembler = dtoAssembler;
    }

    @Override
    public List<ScheduleItemDTO> getScheduleEvents(Date startDate, Date endDate, List<Integer> eventTypes, List<Integer> summitTypes, List<Integer> tracks, List<String> tags, List<String> levels) {
        List<SummitEvent> summitEvents = summitEventDataStore.getByFilterLocal(startDate, endDate, eventTypes, summitTypes, tracks, tags, levels);

        ArrayList<ScheduleItemDTO> dtos = new ArrayList<>();
        ScheduleItemDTO scheduleItemDTO;
        for (SummitEvent event: summitEvents) {
            scheduleItemDTO = dtoAssembler.createDTO(event, ScheduleItemDTO.class);
            dtos.add(scheduleItemDTO);
        }

        return dtos;
    }

    @Override
    public void getActiveSummit(IInteractorAsyncOperationListener<SummitDTO> delegate) {
        final IInteractorAsyncOperationListener<SummitDTO> innerDelegate = delegate;
        DataStoreOperationListener<Summit> dataStoreOperationListener = new DataStoreOperationListener<Summit>() {
            @Override
            public void onSuceedWithData(Summit data) {
                if (innerDelegate != null) {
                    try{
                        SummitDTO summitDTO = dtoAssembler.createDTO(data, SummitDTO.class);
                        innerDelegate.onSuceedWithData(summitDTO);
                    } catch (Exception e) {
                        Log.e(Constants.LOG_TAG, "", e);
                        innerDelegate.onError(e.getMessage());
                    }
                }
            }

            @Override
            public void onError(String message) {
                if (innerDelegate != null) {
                    innerDelegate.onError(message);
                }
            }
        };
        summitDataStore.setDelegate(dataStoreOperationListener);
        summitDataStore.getActive();
    }

    public Boolean isEventScheduledByLoggedMember(int eventId) {
        /*guard let loggedInMember = securityManager.getCurrentMember() else {
            return false
        }

        return loggedInMember.attendeeRole!.scheduledEvents.filter("id = \(eventId)").count > 0*/
        return false;
    }

    @Override
    public void addEventToLoggedInMemberSchedule(int id, InteractorAsyncOperationListener<Void> interactorOperationListener) {

    }

    @Override
    public void removeEventToLoggedInMemberSchedule(int id, InteractorAsyncOperationListener<Void> interactorOperationListener) {

    }
}
