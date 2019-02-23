package org.openstack.android.summit.common.data_access.repositories.impl;

import org.joda.time.DateTime;
import org.openstack.android.summit.common.data_access.ISummitEventRemoteDataStore;
import org.openstack.android.summit.common.data_access.repositories.ISummitEventDataStore;
import org.openstack.android.summit.common.data_access.repositories.strategies.IDeleteStrategy;
import org.openstack.android.summit.common.data_access.repositories.strategies.ISaveOrUpdateStrategy;
import org.openstack.android.summit.common.entities.Feedback;
import org.openstack.android.summit.common.entities.ISummitEventType;
import org.openstack.android.summit.common.entities.Member;
import org.openstack.android.summit.common.entities.Presentation;
import org.openstack.android.summit.common.entities.SummitEvent;
import org.openstack.android.summit.common.entities.Track;
import org.openstack.android.summit.common.entities.TrackGroup;
import org.openstack.android.summit.common.filters.DateRangeCondition;
import org.openstack.android.summit.common.filters.FilterConditions;
import org.openstack.android.summit.common.security.ISecurityManager;
import org.openstack.android.summit.common.utils.RealmFactory;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.realm.Case;
import io.realm.RealmQuery;
import io.realm.RealmResults;
import io.realm.Sort;

/**
 * Created by Claudio Redi on 12/20/2015.
 */
public class SummitEventDataStore extends GenericDataStore<SummitEvent> implements ISummitEventDataStore {

    private ISummitEventRemoteDataStore summitEventRemoteDataStore;
    private ISecurityManager securityManager;

    public SummitEventDataStore
    (
        ISecurityManager securityManager,
        ISummitEventRemoteDataStore summitEventRemoteDataStore,
        ISaveOrUpdateStrategy saveOrUpdateStrategy,
        IDeleteStrategy deleteStrategy
    )
    {
        super(SummitEvent.class, saveOrUpdateStrategy, deleteStrategy);
        this.securityManager            = securityManager;
        this.summitEventRemoteDataStore = summitEventRemoteDataStore;
    }

    @Override
    public long countByTrackGroup(int trackGroupId){
        TrackGroup group = RealmFactory.getSession().where(TrackGroup.class).equalTo("id", trackGroupId).findFirst();
        List<Integer> trackIds = new ArrayList<>();
        for(Track track : group.getTracks()){
            trackIds.add(track.getId());
        }

        if(trackIds.size() == 0) return 0;

        RealmQuery<SummitEvent> query = RealmFactory.getSession().where(SummitEvent.class);
        query = query.in("track.id", trackIds.toArray(new Integer[trackIds.size()]));
        return query.count();
    }

    @Override
    public long countByTrack(int trackId){
        RealmQuery<SummitEvent> query = RealmFactory.getSession().where(SummitEvent.class);
        query = query.equalTo("track.id", trackId);
        return query.count();
    }

    @Override
    public long countByEventType(int eventTypeId){
        RealmQuery<SummitEvent> query = RealmFactory.getSession().where(SummitEvent.class);
        query = query.equalTo("type.id", eventTypeId);
        return query.count();
    }

    @Override
    public long countBySummitType(int sumitTypeId) {
        RealmQuery<SummitEvent> query = RealmFactory.getSession().where(SummitEvent.class);
        query = query.equalTo("summitTypes.id", sumitTypeId);
        return query.count();
    }

    @Override
    public long countByLevel(String level){
        return 0;
    }

    @Override
    public List<SummitEvent> getByFilter(FilterConditions conditions){
        return getByFilter(new DateRangeCondition(conditions.getStartDate(), conditions.getEndDate()), conditions);
    }

    @Override
    public boolean existEventsOnRoom(int roomId){
        RealmQuery<SummitEvent> query = RealmFactory.getSession().where(SummitEvent.class);
        query   = query.equalTo("venueRoom.id", roomId);
        return query.count() > 0;
    }

    @Override
    public boolean existEventsOnVenue(int venueId){
        RealmQuery<SummitEvent> query = RealmFactory.getSession().where(SummitEvent.class);
        query = query.equalTo("venueRoom.venue.id", venueId);
        query = query.or();
        query = query.equalTo("venue.id", venueId);
        return query.count() > 0;
    }

    @Override
    public List<SummitEvent> getByFilter(DateRangeCondition dateRangeCondition, FilterConditions conditions){
        RealmQuery<SummitEvent> query = RealmFactory.getSession().where(SummitEvent.class)
                .greaterThanOrEqualTo("end", dateRangeCondition.getStartDate().toDate())
                .lessThanOrEqualTo("end", dateRangeCondition.getEndDate().toDate());

        boolean isFirst;
        Member currentMember = securityManager.getCurrentMember();

        isFirst = true;
        if (conditions.getEventTypes() != null) {
            query.beginGroup();
            for (int eventTypeId : conditions.getEventTypes() ) {
                if (!isFirst) {
                    query = query.or();
                }
                query = query.equalTo("type.id", eventTypeId);
                isFirst = false;
            }
            query.endGroup();
        }

        if(currentMember == null){
            query = query.notEqualTo("class_name", ISummitEventType.Type.SummitGroupEvent.toString());
        }
        else{
            query.beginGroup();
            query = query.in("class_name", new String[]{
                    ISummitEventType.Type.SummitEvent.toString(),
                    ISummitEventType.Type.Presentation.toString(),
                    ISummitEventType.Type.SummitEventWithFile.toString()
            });
            query = query.or();
            query.beginGroup();
            query = query.equalTo("class_name", ISummitEventType.Type.SummitGroupEvent.toString());
            query = query.equalTo("groupEvent.owner.id", currentMember.getId());
            query.endGroup();
            query.endGroup();
        }

        isFirst = true;
        if (conditions.getTags() != null) {
            query.beginGroup();
            for (String tag : conditions.getTags() ) {
                if (!isFirst) {
                    query = query.or();
                }
                query = query.equalTo("tags.tag", tag);
                isFirst = false;
            }
            query.endGroup();
        }

        isFirst = true;
        if (conditions.getSummitTypes() != null) {
            query.beginGroup();
            for (int summitTypeId : conditions.getSummitTypes()) {
                if (!isFirst) {
                    query = query.or();
                }
                query = query.equalTo("summitTypes.id", summitTypeId);
                isFirst = false;
            }
            query.endGroup();
        }

        isFirst = true;
        if (conditions.getLevels() != null) {
            query.beginGroup();
            for (String level : conditions.getLevels()) {
                if (!isFirst) {
                    query = query.or();
                }
                query = query.equalTo("presentation.level", level);
                isFirst = false;
            }
            query.endGroup();
        }

        isFirst = true;
        if (conditions.getTracks() != null) {
            query.beginGroup();
            for (int trackId : conditions.getTracks()) {
                if (!isFirst) {
                    query = query.or();
                }
                query = query.equalTo("track.id", trackId);
                isFirst = false;
            }
            query.endGroup();
        }

        isFirst = true;
        if (conditions.getTrackGroups() != null) {
            query.beginGroup();
            for (int trackGroupId : conditions.getTrackGroups() ) {
                if (!isFirst) {
                    query = query.or();
                }
                query = query.equalTo("track.trackGroups.id", trackGroupId);
                isFirst = false;
            }
            query.endGroup();
        }

        // venues and rooms
        isFirst = true;
        if (conditions.getRooms() != null || conditions.getVenues() != null) {
            query.beginGroup();

            for (int roomId : conditions.getRooms() ) {
                if (!isFirst) {
                    query = query.or();
                }
                query   = query.equalTo("venueRoom.id", roomId);
                isFirst = false;
            }

            for (int venueId : conditions.getVenues()) {
                if (!isFirst) {
                    query = query.or();
                }
                query = query.equalTo("venueRoom.venue.id", venueId);
                query = query.or();
                query = query.equalTo("venue.id", venueId);
                isFirst = false;
            }

            query.endGroup();
        }

        if(conditions.isShowVideoTalks() ){
            query = query.isNotEmpty("presentation.videos");
        }

        return query.findAllSorted(new String[] { "start", "end", "name"}, new Sort[] { Sort.ASCENDING, Sort.ASCENDING, Sort.ASCENDING });
    }

    @Override
    public List<String> getPresentationLevels() {
        ArrayList<String> levels = new ArrayList<>();
        levels.add(Presentation.LevelBeginner);
        levels.add(Presentation.LevelIntermediate);
        levels.add(Presentation.LevelAdvanced);
        levels.add(Presentation.LevelNA);
        return levels;
    }

    @Override
    public List<SummitEvent> getBySearchTerm(int summitId, String searchTerm) {
        RealmQuery<SummitEvent> query = RealmFactory.getSession().where(SummitEvent.class).equalTo("summit.id", summitId)
                .beginGroup()
                        .contains("name", searchTerm, Case.INSENSITIVE)
                    .or()
                        .contains("tags.tag", searchTerm, Case.INSENSITIVE)
                    .or()
                        .contains("presentation.speakers.fullName", searchTerm, Case.INSENSITIVE)
                    .or()
                        .contains("presentation.moderator.fullName", searchTerm, Case.INSENSITIVE)
                    .or()
                        .contains("presentation.level", searchTerm, Case.INSENSITIVE)
                    .or()
                        .contains("sponsors.name", searchTerm, Case.INSENSITIVE)
                .endGroup();

        RealmResults<SummitEvent> results = query.findAll();
        results = results.sort(new String[] { "start", "end", "name"}, new Sort[] { Sort.ASCENDING, Sort.ASCENDING, Sort.ASCENDING });
        return results;
    }

    @Override
    public List<SummitEvent> getSpeakerEvents(int speakerId, DateTime startDate, DateTime endDate) {

        RealmQuery<SummitEvent> query = RealmFactory.getSession().where(SummitEvent.class)
                .greaterThanOrEqualTo("start", startDate.toDate())
                .lessThanOrEqualTo("end", endDate.toDate())
                .beginGroup()
                    .equalTo("presentation.speakers.id", speakerId)
                    .or()
                    .equalTo("presentation.moderator.id", speakerId)
                .endGroup();

        return query.findAllSorted(new String[] { "start", "end", "name"}, new Sort[] { Sort.ASCENDING, Sort.ASCENDING, Sort.ASCENDING });
    }

    @Override
    public Observable<List<Feedback>> getFeedback(int eventId, int page, int objectsPerPage) {
        return summitEventRemoteDataStore.getFeedback(eventId, page, objectsPerPage);
    }

    @Override
    public Observable<Double> getAverageFeedback(int eventId) {
        return summitEventRemoteDataStore.getAverageFeedback(eventId);
    }

}