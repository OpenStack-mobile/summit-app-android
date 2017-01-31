package org.openstack.android.summit.common.entities;

import java.util.Date;

import io.realm.RealmList;

/**
 * Created by Claudio Redi on 12/28/2015.
 */
public interface ISummitEvent extends INamedEntity, ISummitOwned {

    Date getStart();

    void setStart(Date start);

    Date getEnd();

    void setEnd(Date end);

    String getDescription();

    void setDescription(String description);

    Boolean getAllowFeedback();

    void setAllowFeedback(Boolean allowFeedback);

    EventType getType();

    void setType(EventType type);

    RealmList<Company> getSponsors();

    void setSponsors(RealmList<Company> sponsors);

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

    String getClassName();

    void setClassName(String className);

}
