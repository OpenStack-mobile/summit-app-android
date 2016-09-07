package org.openstack.android.summit.common.data_access.deserialization;

import android.util.Log;

import org.openstack.android.summit.common.Constants;
import org.openstack.android.summit.common.entities.Company;
import org.openstack.android.summit.common.entities.EventType;
import org.openstack.android.summit.common.entities.Feedback;
import org.openstack.android.summit.common.entities.IEntity;
import org.openstack.android.summit.common.entities.Image;
import org.openstack.android.summit.common.entities.Member;
import org.openstack.android.summit.common.entities.Presentation;
import org.openstack.android.summit.common.entities.PresentationLink;
import org.openstack.android.summit.common.entities.PresentationSlide;
import org.openstack.android.summit.common.entities.PresentationSpeaker;
import org.openstack.android.summit.common.entities.PresentationVideo;
import org.openstack.android.summit.common.entities.Summit;
import org.openstack.android.summit.common.entities.SummitAttendee;
import org.openstack.android.summit.common.entities.SummitEvent;
import org.openstack.android.summit.common.entities.SummitType;
import org.openstack.android.summit.common.entities.Tag;
import org.openstack.android.summit.common.entities.TicketType;
import org.openstack.android.summit.common.entities.Track;
import org.openstack.android.summit.common.entities.TrackGroup;
import org.openstack.android.summit.common.entities.Venue;
import org.openstack.android.summit.common.entities.VenueFloor;
import org.openstack.android.summit.common.entities.VenueRoom;
import org.openstack.android.summit.common.utils.RealmFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.realm.Realm;
import io.realm.RealmObject;

/**
 * Created by Claudio Redi on 11/11/2015.
 */
public class DeserializerStorage implements IDeserializerStorage {

    private static final Object lock = new Object();
    private Map<Class, Map<Integer, IEntity>> deserializedEntityDictionary = new HashMap<Class, Map<Integer, IEntity>>();
    private boolean canClear  = true;
    private static final Object clearLock = new Object();

    @Override
    public boolean cancelClear(){
        synchronized (clearLock){
            if(!canClear) return false;
            canClear = false;
            Log.d(Constants.LOG_TAG, "disabling deserializer storage clear");
            return true;
        }
    }

    @Override
    public void enableClear(){
        synchronized (clearLock){
            Log.d(Constants.LOG_TAG, "enabling deserializer storage clear");
            canClear = true;
        }
    }

    @Override
    public boolean canClear(){
        synchronized (clearLock){
            return canClear;
        }
    }

    @Override
    public <T extends RealmObject & IEntity> void add(T entity, Class<T> type){
        add(entity, type, true);
    }

    private <T extends RealmObject & IEntity> void add(T entity, Class<T> type, boolean deepCopy) {

        synchronized (lock) {
            if (!deserializedEntityDictionary.containsKey(type)) {
                deserializedEntityDictionary.put(type, new HashMap<Integer, IEntity>());
            }
            deserializedEntityDictionary.get(type).put(entity.getId(), entity);
        }

        if(type == Summit.class && deepCopy){
            Summit summit =  (Summit) entity;

            for(SummitEvent e: summit.getEvents())
                add(e, SummitEvent.class, true);

            for(SummitType t: summit.getTypes())
                add(t, SummitType.class, true);

            for(EventType et: summit.getEventTypes())
                add(et, EventType.class, true);

            for(Track tr: summit.getTracks())
                add(tr, Track.class, true);

            for(TrackGroup gr: summit.getTrackGroups())
                add(gr, TrackGroup.class, true);

            for(Venue v: summit.getVenues())
                add(v, Venue.class, true);

            for(VenueRoom vr: summit.getVenueRooms())
                add(vr, VenueRoom.class, true);

            for(TicketType tt: summit.getTicketTypes())
                add(tt, TicketType.class, true);
        }

        if(type == Member.class && deepCopy){
            Member member = (Member) entity;

            if(member.getAttendeeRole() != null)
                add(member.getAttendeeRole(), SummitAttendee.class, false);

            if(member.getSpeakerRole() != null)
                add(member.getSpeakerRole(), PresentationSpeaker.class, false);

            for(Feedback f: member.getFeedback())
                add(f, Feedback.class, true);
        }

        if(type == SummitAttendee.class && deepCopy){
            SummitAttendee attendee = (SummitAttendee) entity;

            for(TicketType tt: attendee.getTicketTypes())
                add(tt, TicketType.class, false);

            for(SummitEvent e: attendee.getScheduledEvents())
                add(e, SummitEvent.class, false);
        }

        if(type == Feedback.class && deepCopy){
            Feedback feedback = (Feedback) entity;
            if(feedback.getEvent() != null)
                add(feedback.getEvent(), SummitEvent.class, false);
            if(feedback.getOwner() != null)
                add(feedback.getOwner(), Member.class, false);
        }

        if(type == Venue.class && deepCopy){
            Venue venue = (Venue) entity;

            for(Image i: venue.getImages())
                add(i, Image.class, false);

            for(Image m: venue.getMaps())
                add(m, Image.class, false);

            for(VenueFloor f: venue.getFloors())
                add(f, VenueFloor.class, true);
        }

        if(type == VenueFloor.class && deepCopy){
            VenueFloor floor  =  (VenueFloor) entity;

            if(floor.getVenue() != null)
                add(floor.getVenue(), Venue.class, false);

            for(VenueRoom vr: floor.getRooms())
                add(vr, VenueRoom.class, false);
        }

        if(type == VenueRoom.class && deepCopy){
            VenueRoom room  =  (VenueRoom) entity;
            if(room.getVenue() != null){
                add(room.getVenue(), Venue.class, false);
            }
            if(room.getFloor() != null){
                add(room.getFloor(), VenueFloor.class, false);
            }
        }

        if(type == SummitEvent.class && deepCopy){
            SummitEvent summitEvent  =  (SummitEvent) entity;

            if(summitEvent.getSummit() !=  null)
                add(summitEvent.getSummit(), Summit.class, false);

            if(summitEvent.getEventType() != null)
                add(summitEvent.getEventType(), EventType.class, false);

            if(summitEvent.getPresentation() != null)
                add(summitEvent.getPresentation(), Presentation.class, false);

            if(summitEvent.getVenue() != null)
                add(summitEvent.getVenue(), Venue.class, false);

            if(summitEvent.getVenueRoom() != null) {
                add(summitEvent.getVenueRoom(), VenueRoom.class, false);
            }

            for(Company c: summitEvent.getSponsors())
                add(c, Company.class, false);

            for(SummitType st: summitEvent.getSummitTypes())
                add(st, SummitType.class, false);

            for(Tag t: summitEvent.getTags())
                add(t, Tag.class, false);
        }

        if(type == Presentation.class){
            Presentation presentation  = (Presentation) entity;
            if(presentation.getTrack() != null)
                add(presentation.getTrack(), Track.class, false);

            if(presentation.getModerator() != null)
                add(presentation.getModerator(), PresentationSpeaker.class, false);

            for(PresentationSpeaker s: presentation.getSpeakers())
                add(s, PresentationSpeaker.class, false);

            for(PresentationSlide s: presentation.getSlides())
                add(s, PresentationSlide.class, false);

            for(PresentationVideo v: presentation.getVideos())
                add(v, PresentationVideo.class, false);

            for(PresentationLink l: presentation.getLinks())
                add(l, PresentationLink.class, false);
        }
    }

    @Override
    public <T extends RealmObject & IEntity> T get(int id, Class<T> type) {
        T detachedEntity = null;
        synchronized (lock) {
            if (deserializedEntityDictionary.containsKey(type) && deserializedEntityDictionary.get(type).containsKey(id)) {
                detachedEntity = (T) deserializedEntityDictionary.get(type).get(id);
            }
        }

        if (detachedEntity == null) {
            Realm session = RealmFactory.getSession();
            T managedEntity = session .where(type).equalTo("id", id).findFirst();
            if(managedEntity != null) {
                detachedEntity = session.copyFromRealm(managedEntity);
                add(detachedEntity, type);
            }
        }
        return detachedEntity;
    }

    @Override
    public <T extends RealmObject & IEntity> List<T> getAll(Class<T> type) {
        List<T> list = new ArrayList<T>();

        synchronized (lock) {
            if (deserializedEntityDictionary.containsKey(type)) {
                for (IEntity entity : deserializedEntityDictionary.get(type).values()) {
                    list.add((T)entity);
                }
            }
        }

        if(list.isEmpty()){
            Realm session = RealmFactory.getSession();
            list = session.where(type).findAll();
            list = session.copyFromRealm(list);
            for(T entity: list)
                this.add(entity, type);
        }
        return list;
    }

    @Override
    public <T extends RealmObject & IEntity> Boolean exist(int entityId, Class<T> type) {
        synchronized (lock) {
            return (deserializedEntityDictionary.containsKey(type) &&
                    deserializedEntityDictionary.get(type).containsKey(entityId)) ||
                    RealmFactory.getSession().where(type).equalTo("id", entityId).count() > 0;
        }
    }

    @Override
    public <T extends RealmObject & IEntity> Boolean exist(T entity, Class<T> type) {
        return exist(entity.getId(), type);
    }

    @Override
    public void clear() {
        synchronized (clearLock) {
            if(!canClear) return;
            synchronized (lock) {
                Log.d(Constants.LOG_TAG, "clearing deserializer storage");
                deserializedEntityDictionary.clear();
            }
        }
    }
}
