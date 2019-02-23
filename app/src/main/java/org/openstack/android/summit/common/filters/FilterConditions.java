package org.openstack.android.summit.common.filters;

import org.joda.time.DateTime;

import java.util.List;

final public class FilterConditions {

    private DateTime startDate;
    private DateTime endDate;
    private List<Integer> eventTypes;
    private List<Integer> summitTypes;
    private List<Integer> trackGroups;
    private List<Integer> tracks;
    private List<String> tags;
    private List<String> levels;
    private List<Integer> venues;
    private List<Integer> rooms;
    private boolean showVideoTalks;

    public FilterConditions(DateTime startDate, DateTime endDate, List<Integer> eventTypes, List<Integer> summitTypes, List<Integer> trackGroups, List<Integer> tracks, List<String> tags, List<String> levels, List<Integer> venues, List<Integer> rooms, boolean showVideoTalks) {
        this.startDate = startDate;
        this.endDate = endDate;
        this.eventTypes = eventTypes;
        this.summitTypes = summitTypes;
        this.trackGroups = trackGroups;
        this.tracks = tracks;
        this.tags = tags;
        this.levels = levels;
        this.venues = venues;
        this.rooms = rooms;
        this.showVideoTalks = showVideoTalks;
    }

    public DateTime getStartDate() {
        return startDate;
    }

    public DateTime getEndDate() {
        return endDate;
    }

    public List<Integer> getEventTypes() {
        return eventTypes;
    }

    public List<Integer> getSummitTypes() {
        return summitTypes;
    }

    public List<Integer> getTrackGroups() {
        return trackGroups;
    }

    public List<Integer> getTracks() {
        return tracks;
    }

    public List<String> getTags() {
        return tags;
    }

    public List<String> getLevels() {
        return levels;
    }

    public List<Integer> getVenues() {
        return venues;
    }

    public List<Integer> getRooms() {
        return rooms;
    }

    public boolean isShowVideoTalks() {
        return showVideoTalks;
    }
}
