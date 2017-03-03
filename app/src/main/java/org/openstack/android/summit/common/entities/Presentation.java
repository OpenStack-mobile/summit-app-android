package org.openstack.android.summit.common.entities;

import java.util.Date;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Claudio Redi on 11/4/2015.
 */
public class Presentation extends RealmObject implements IPresentation {
    /**
     *   'Level' => "Enum('Beginner,Intermediate,Advanced,N/A')",
     */
    public final static String LevelBeginner     = "Beginner";
    public final static String LevelIntermediate = "Intermediate";
    public final static String LevelAdvanced     = "Advanced";
    public final static String LevelNA           = "N/A";

    @PrimaryKey
    private int id;
    private String level;
    private RealmList<PresentationSpeaker> speakers = new RealmList<>();
    private RealmList<PresentationSlide> slides     = new RealmList<>();
    private RealmList<PresentationVideo> videos     = new RealmList<>();
    private RealmList<PresentationLink> links       = new RealmList<>();
    private PresentationSpeaker moderator;
    private SummitEvent event;
    private boolean toRecord;

    public boolean isToRecord() {
        return toRecord;
    }

    public void setToRecord(boolean toRecord) {
        this.toRecord = toRecord;
    }

    public RealmList<PresentationSlide> getSlides() {
        if(slides == null) slides = new RealmList<>();
        return slides;
    }

    public void setSlides(RealmList<PresentationSlide> slides) {
        this.slides = slides;
    }

    public RealmList<PresentationLink> getLinks() {
        if(links == null) links = new RealmList<>();
        return links;
    }

    public void setLinks(RealmList<PresentationLink> links) {
        this.links = links;
    }

    public RealmList<PresentationVideo> getVideos() {
        if(videos == null) videos = new RealmList<>();
        return videos;
    }

    public void setVideos(RealmList<PresentationVideo> videos) {
        this.videos = videos;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public RealmList<PresentationSpeaker> getSpeakers() {
        if(speakers == null) speakers = new RealmList<>();
        return speakers;
    }

    public void setSpeakers(RealmList<PresentationSpeaker> speakers) {
        this.speakers = speakers;
    }

    public PresentationSpeaker getModerator() {
        return moderator;
    }

    public void setModerator(PresentationSpeaker moderator) {
        this.moderator = moderator;
    }

    @Override
    public void setEvent(ISummitEvent event) {
        this.event = (SummitEvent)event;
    }

    @Override
    public ISummitEvent getEvent() {
        return event;
    }

    @Override
    public Date getStart() {
        return event.getStart();
    }

    @Override
    public void setStart(Date start) {
        event.setStart(start);
    }

    @Override
    public Date getEnd() {
        return event.getEnd();
    }

    @Override
    public void setEnd(Date end) {
        event.setEnd(end);
    }

    @Override
    public String getDescription() {
        return event.getDescription();
    }

    @Override
    public void setDescription(String description) {
        event.setDescription(description);
    }

    @Override
    public Boolean getAllowFeedback() {
        return event.getAllowFeedback();
    }

    @Override
    public void setAllowFeedback(Boolean allowFeedback) {
        event.setAllowFeedback(allowFeedback);
    }

    @Override
    public EventType getType() {
        return event.getType();
    }

    @Override
    public void setType(EventType type) {
        event.setType(type);
    }

    @Override
    public RealmList<Company> getSponsors() {
        return event.getSponsors();
    }

    @Override
    public void setSponsors(RealmList<Company> sponsors) {
        event.setSponsors(sponsors);
    }

    @Override
    public Venue getVenue() {
        return event.getVenue();
    }

    @Override
    public void setVenue(Venue venue) {
        event.setVenue(venue);
    }

    @Override
    public VenueRoom getVenueRoom() {
        return event.getVenueRoom();
    }

    @Override
    public void setVenueRoom(VenueRoom venueRoom) {
        event.setVenueRoom(venueRoom);
    }

    @Override
    public RealmList<Tag> getTags() {
        return event.getTags();
    }

    @Override
    public void setTags(RealmList<Tag> tags) {
        event.setTags(tags);
    }

    @Override
    public Summit getSummit() {
        return event.getSummit();
    }

    @Override
    public void setSummit(Summit summit) {
        event.setSummit(summit);
    }

    @Override
    public Track getTrack() {
        return event.getTrack();
    }

    @Override
    public void setTrack(Track track) {
        event.setTrack(track);
    }

    @Override
    public String getClassName() {
        return event.getClassName();
    }

    @Override
    public void setClassName(String classname) {
        event.setClassName(classname);
    }

    @Override
    public String getName() {
        return event.getName();
    }

    @Override
    public void setName(String name) {
        event.setName(name);
    }
}
