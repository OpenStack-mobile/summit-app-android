package org.openstack.android.summit.common.filters;
import org.joda.time.DateTime;

final public class DateRangeCondition {
    private DateTime startDate;
    private DateTime endDate;

    public DateRangeCondition(DateTime startDate, DateTime endDate) {
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public DateTime getStartDate() {
        return startDate;
    }

    public DateTime getEndDate() {
        return endDate;
    }
}
