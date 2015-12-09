package org.openstack.android.summit.common.DTOs;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Claudio Redi on 11/18/2015.
 */
public class PresentationSpeakerDTO extends PersonDTO {
    public List<ScheduleItemDTO> presentations = new ArrayList<ScheduleItemDTO>();
}
