package org.openstack.android.summit.common.DTOs.Assembler.Converters;

import org.openstack.android.summit.common.entities.PresentationSpeaker;
import org.openstack.android.summit.common.entities.PresentationVideo;
import org.openstack.android.summit.common.entities.SummitEvent;

/**
 * Created by Claudio Redi on 1/21/2016.
 */
public class SummitEvent2EventDetailDTO extends AbstractSummitEvent2EventDetailDTO<SummitEvent, PresentationSpeaker, PresentationVideo> {
    public SummitEvent2EventDetailDTO() {
        presentationSpeaker2PersonListIemDTO = new PresentationSpeaker2PersonListIemDTO();
        video2VideoDTO = new AbstractPresentationVideo2VideoDTO<PresentationVideo>();
    }
}
