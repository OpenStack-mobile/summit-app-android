package org.openstack.android.summit.modules.speakers_list.business_logic;

import org.openstack.android.summit.common.DTOs.Assembler.IDTOAssembler;
import org.openstack.android.summit.common.DTOs.PersonListItemDTO;
import org.openstack.android.summit.common.business_logic.BaseInteractor;
import org.openstack.android.summit.common.data_access.IPresentationSpeakerDataStore;
import org.openstack.android.summit.common.entities.PresentationSpeaker;

import java.util.List;

/**
 * Created by Claudio Redi on 1/13/2016.
 */
public class SpeakerListInteractor extends BaseInteractor implements ISpeakerListInteractor {
    IPresentationSpeakerDataStore presentationSpeakerDataStore;
    IDTOAssembler dtoAssembler;

    public SpeakerListInteractor(IPresentationSpeakerDataStore presentationSpeakerDataStore, IDTOAssembler dtoAssembler) {
        super(dtoAssembler);
        this.presentationSpeakerDataStore = presentationSpeakerDataStore;
        dtoAssembler = dtoAssembler;
    }

    @Override
    public List<PersonListItemDTO> getSpeakers(int page, int objectsPerPage) {
        List<PresentationSpeaker> speakers = presentationSpeakerDataStore.getByFilterLocal(null, page, objectsPerPage);

        List<PersonListItemDTO> dtos = createDTOList(speakers, PersonListItemDTO.class);
        return dtos;
    }
}
