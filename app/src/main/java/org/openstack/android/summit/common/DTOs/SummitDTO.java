package org.openstack.android.summit.common.DTOs;

import java.util.Date;

/**
 * Created by Claudio Redi on 11/18/2015.
 */
public class SummitDTO extends NamedDTO {
    private Date startDate;
    private Date endDate;
    private String timeZone;
    private Date startShowingVenuesDate;

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public String getTimeZone() {
        return timeZone;
    }

    public void setTimeZone(String timeZone) {
        this.timeZone = timeZone;
    }

    public Date getStartShowingVenuesDate() {
        return startShowingVenuesDate;
    }

    public void setStartShowingVenuesDate(Date startShowingVenuesDate) {
        this.startShowingVenuesDate = startShowingVenuesDate;
    }
}
