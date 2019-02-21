package org.openstack.android.summit.common.DTOs;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import org.joda.time.Days;
/**
 * Created by Claudio Redi on 11/18/2015.
 */
public class SummitDTO extends NamedDTO {

    private Date startDate;
    private Date endDate;
    private String timeZone;
    private Date startShowingVenuesDate;
    private Date scheduleStartDate;
    private String datesLabel;

    private List<WifiListItemDTO> wifiConnections = new ArrayList<WifiListItemDTO>();

    public Date getScheduleStartDate() {
        return scheduleStartDate;
    }

    public void setScheduleStartDate(Date scheduleStartDate) {
        this.scheduleStartDate = scheduleStartDate;
    }

    public int getScheduleStartDay() {
        DateTimeZone summitTimeZone = DateTimeZone.forID(getTimeZone());
        DateTime auxStartSchedule   = new DateTime(scheduleStartDate.getTime(), summitTimeZone).withTime(0, 0, 0, 0);
        return auxStartSchedule.getDayOfMonth();
    }

    public DateTime getFirstEnabledDate(List<DateTime> inactiveDates){
        DateTime start = this.getLocalStartDate().withTime(0, 0, 0, 0);
        DateTime end   = this.getLocalEndDate().withTime(0, 0, 0, 0);
        DateTime first = null;

        while(start.isBefore(end.getMillis())){
            if(inactiveDates.contains(start)){
                start = start.plusDays(1);
                continue;
            }
            first = start;
            break;
        }
        if(first == null && !inactiveDates.contains(start)) first = start;
        return first;
    }

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

    public DateTime getLocalStartDate() {
        DateTimeZone summitTimeZone = DateTimeZone.forID(getTimeZone());
        return new DateTime(getStartDate(), summitTimeZone);
    }

    public DateTime getLocalStartShowingVenuesDate() {
        if(this.startShowingVenuesDate == null) return null;
        DateTimeZone summitTimeZone = DateTimeZone.forID(getTimeZone());
        return new DateTime(this.startShowingVenuesDate, summitTimeZone);
    }

    public DateTime getLocalEndDate() {
        DateTimeZone summitTimeZone = DateTimeZone.forID(getTimeZone());
        return new DateTime(getEndDate(), summitTimeZone);
    }

    public DateTime convertFromLocalToUTC(DateTime dateTime) {
        DateTimeZone summitTimeZone = DateTimeZone.forID(getTimeZone());
        DateTime localDateTime = new DateTime(dateTime, summitTimeZone);
        return localDateTime.toDateTime(DateTimeZone.UTC);
    }

    public DateTime getLocalScheduleStartDate(){
        DateTimeZone summitTimeZone = DateTimeZone.forID(getTimeZone());
        return new DateTime(getScheduleStartDate().getTime(), summitTimeZone).withTime(0,0,0,0);
    }

    public DateTime convertFromUTCToLocal(DateTime dateTime) {
        DateTimeZone summitTimeZone = DateTimeZone.forID(getTimeZone());
        DateTime utcDateTime = new DateTime(dateTime, DateTimeZone.UTC);
        return utcDateTime.toDateTime(summitTimeZone);
    }

    public DateTime getCurrentLocalTime() {
        return this.convertFromUTCToLocal(new DateTime(DateTimeZone.UTC));
    }

    public boolean isCurrentDateTimeInsideSummitRange() {
        DateTime currentLocal = getCurrentLocalTime();
        DateTime startDate    = this.getLocalStartDate();
        DateTime endDate      = this.getLocalEndDate();

        return ( startDate.isBefore(currentLocal) || startDate.isEqual(currentLocal)) && ( endDate.isAfter(currentLocal) || endDate.isEqual(currentLocal));
    }

    public boolean shouldShowVenues(){
        DateTime currentLocal = getCurrentLocalTime();
        DateTime startDate    = this.getLocalStartShowingVenuesDate();
        if(startDate == null) return true;
        return currentLocal.isAfter(startDate) || currentLocal.isEqual(startDate);
    }

    public boolean isNotStarted(){
        DateTime currentLocal = getCurrentLocalTime().withTimeAtStartOfDay();
        DateTime startDate    = this.scheduleStartDate != null ?
                                this.getLocalScheduleStartDate().withTimeAtStartOfDay():
                                this.getLocalStartDate().withTimeAtStartOfDay();
        return startDate.isAfter(currentLocal);
    }

    public boolean isGoingOn(){
        DateTime currentLocal = getCurrentLocalTime().withTimeAtStartOfDay();
        DateTime startDate    = this.scheduleStartDate != null ?
                                this.getLocalScheduleStartDate().withTimeAtStartOfDay():
                                this.getLocalStartDate().withTimeAtStartOfDay();
        DateTime endDate      = this.getLocalEndDate().withTimeAtStartOfDay();
        return ( startDate.isBefore(currentLocal) || startDate.isEqual(currentLocal)) && ( endDate.isAfter(currentLocal) || endDate.isEqual(currentLocal));
    }

    public int getDaysLeft(){
        if(isNotStarted()){
            DateTime currentLocal = getCurrentLocalTime();
            DateTime startDate    = this.scheduleStartDate != null ?
                                    this.getLocalScheduleStartDate():
                                    this.getLocalStartDate();

            return Days.daysBetween(currentLocal.withTimeAtStartOfDay() , startDate.withTimeAtStartOfDay() ).getDays();
        }
        return 0;
    }

    public int getCurrentDay(){
        if(isGoingOn()){
            DateTime currentLocal = getCurrentLocalTime();
            DateTime startDate    = this.scheduleStartDate != null ?
                                    this.getLocalScheduleStartDate():
                                    this.getLocalStartDate();

            return Days.daysBetween(startDate.withTimeAtStartOfDay() , currentLocal.withTimeAtStartOfDay()).getDays() + 1;
        }
        return 0;
    }

    public ArrayList<DateTime> getPastDates() {
        ArrayList<DateTime> list = new ArrayList<>();
        if (this.isCurrentDateTimeInsideSummitRange()) {
            DateTime currentLocal       = getCurrentLocalTime();
            Calendar c                  = Calendar.getInstance();
            DateTimeZone summitTimeZone = DateTimeZone.forID(getTimeZone());
            do {
                c.setTime(currentLocal.withTime(0, 0, 0, 0).toDate());
                c.add(Calendar.DATE, -1);
                currentLocal = new DateTime(c.getTime(), summitTimeZone);
                if (getLocalStartDate().withTime(0, 0, 0, 0).getMillis() > currentLocal.withTime(0, 0, 0, 0).getMillis())
                    break;
                list.add(currentLocal.withTime(0, 0, 0, 0));

            } while (true);
        }
        return list;
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

    public List<WifiListItemDTO> getWifiConnections() {
        return wifiConnections;
    }

    public void setWifiConnections(List<WifiListItemDTO> wifiConnections) {
        this.wifiConnections = wifiConnections;
    }

    public String getDatesLabel() {
        return datesLabel;
    }

    public void setDatesLabel(String datesLabel) {
        this.datesLabel = datesLabel;
    }
}
