package org.openstack.android.summit.common.data_access.DTOs.assembler;

import junit.framework.Assert;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.openstack.android.summit.common.DTOs.Assembler.DTOAssembler;
import org.openstack.android.summit.common.DTOs.ScheduleItemDTO;
import org.openstack.android.summit.common.DTOs.SummitDTO;
import org.openstack.android.summit.common.entities.Company;
import org.openstack.android.summit.common.entities.EventType;
import org.openstack.android.summit.common.entities.Summit;
import org.openstack.android.summit.common.entities.SummitEvent;
import org.robolectric.RobolectricTestRunner;

import java.util.Date;

/**
 * Created by Claudio Redi on 11/18/2015.
 */
@RunWith(RobolectricTestRunner.class)
public class DTOAssemblerTests {
    @Test
    public void createDTO_summit2SummitDTO_createCorrectDTOInstance() {
        // Arrange
        DTOAssembler dtoAssembler = new DTOAssembler();
        Summit summit = new Summit();
        summit.setName("Tokio");
        summit.setStartDate(new Date(1447825312));
        summit.setEndDate(new Date(1447855312));
        summit.setTimeZone("CST");

        // Act
        SummitDTO summitDTO = dtoAssembler.createDTO(summit, SummitDTO.class);

        // Assert
        Assert.assertEquals(summitDTO.getName(), summit.getName());
        Assert.assertEquals(summitDTO.getStartDate().getTime(), summit.getStartDate().getTime());
        Assert.assertEquals(summitDTO.getEndDate().getTime(), summit.getEndDate().getTime());
        Assert.assertEquals(summitDTO.getTimeZone(), summit.getTimeZone());
    }

    @Test
    public void createDTO_summitEvent2SummitScheduleItemDTO_createCorrectDTOInstance() {
        // Arrange
        DTOAssembler dtoAssembler = new DTOAssembler();
        Summit summit = new Summit();
        summit.setTimeZone("Asia/Tokyo");

        Company company1 = new Company();
        company1.setName("company 1");

        Company company2 = new Company();
        company2.setName("company 2");

        EventType eventType = new EventType();
        eventType.setName("Keynotes");

        SummitEvent summitEvent = new SummitEvent();
        summitEvent.setName("Tokio");
        summitEvent.setStart(new Date(1447825312));
        summitEvent.setEnd(new Date(1447855312));
        summitEvent.setSummit(summit);
        summitEvent.getSponsors().add(company1);
        summitEvent.getSponsors().add(company2);

        // Act
        ScheduleItemDTO scheduleItemDTO = dtoAssembler.createDTO(summit, ScheduleItemDTO.class);

        // Assert
        Assert.assertEquals(scheduleItemDTO.getName(), summit.getName());
        Assert.assertEquals(scheduleItemDTO.getDate(), "");
    }

}
