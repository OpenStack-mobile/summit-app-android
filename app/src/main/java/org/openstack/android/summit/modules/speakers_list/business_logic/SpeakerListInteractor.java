package org.openstack.android.summit.modules.speakers_list.business_logic;

import org.openstack.android.summit.common.DTOs.Assembler.IDTOAssembler;
import org.openstack.android.summit.common.DTOs.PersonListItemDTO;
import org.openstack.android.summit.common.api.ISummitSelector;
import org.openstack.android.summit.common.business_logic.BaseInteractor;
import org.openstack.android.summit.common.data_access.repositories.IPresentationSpeakerDataStore;
import org.openstack.android.summit.common.data_access.repositories.ISummitDataStore;
import org.openstack.android.summit.common.entities.PresentationSpeaker;
import org.openstack.android.summit.common.security.ISecurityManager;

import java.util.List;

/**
 * Created by Claudio Redi on 1/13/2016.
 */
public class SpeakerListInteractor extends BaseInteractor implements ISpeakerListInteractor {
    IPresentationSpeakerDataStore presentationSpeakerDataStore;

    public SpeakerListInteractor
    (
        ISecurityManager securityManager,
        IPresentationSpeakerDataStore presentationSpeakerDataStore,
        IDTOAssembler dtoAssembler,
        ISummitDataStore summitDataStore,
        ISummitSelector summitSelector
    )
    {
        super(securityManager, dtoAssembler, summitSelector, summitDataStore);
        this.presentationSpeakerDataStore = presentationSpeakerDataStore;
    }

    @Override
    public List<PersonListItemDTO> getSpeakers(int page, int objectsPerPage) {
        String searchTerm = null;

        List<PresentationSpeaker> speakers = presentationSpeakerDataStore.getByFilter
        (
            summitSelector.getCurrentSummitId(),
            searchTerm,
            page,
            objectsPerPage
        );

        return createDTOList(speakers, PersonListItemDTO.class);
    }
}
