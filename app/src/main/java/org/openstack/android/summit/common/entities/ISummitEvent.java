package org.openstack.android.summit.common.entities;

import java.util.Date;

import io.realm.RealmList;

/**
 * Created by Claudio Redi on 12/28/2015.
 */
public interface ISummitEvent extends INamedEntity {

    Date getStart();

    void setStart(Date start);

    Date getEnd();

    void setEnd(Date end);

    String getEventDescription();

    void setEventDescription(String eventDescription);

    Boolean getAllowFeedback();

    void setAllowFeedback(Boolean allowFeedback);

    EventType getEventType();

    void setEventType(EventType eventType);

    RealmList<Company> getSponsors();

    void setSponsors(RealmList<Company> sponsors);

    Presentation getPresentation();

    void setPresentation(Presentation presentation);

    Venue getVenue();

    void setVenue(Venue venue);

    VenueRoom getVenueRoom();

    void setVenueRoom(VenueRoom venueRoom);

    RealmList<Tag> getTags();

    void setTags(RealmList<Tag> tags);

    Summit getSummit();

    void setSummit(Summit summit);

    Track getTrack();

    void setTrack(Track track);

}
