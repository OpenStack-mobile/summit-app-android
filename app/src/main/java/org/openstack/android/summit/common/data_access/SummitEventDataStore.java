package org.openstack.android.summit.common.data_access;

import org.openstack.android.summit.common.entities.EventType;
import org.openstack.android.summit.common.entities.Member;
import org.openstack.android.summit.common.entities.Presentation;
import org.openstack.android.summit.common.entities.Summit;
import org.openstack.android.summit.common.entities.SummitEvent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import io.realm.RealmQuery;
import io.realm.RealmResults;

/**
 * Created by Claudio Redi on 12/20/2015.
 */
public class SummitEventDataStore extends GenericDataStore implements ISummitEventDataStore {

    @Override
    public List<SummitEvent> getByFilterLocal(Date startDate, Date endDate, List<Integer> eventTypes, List<Integer> summitTypes, List<Integer> tracks, List<String> tags, List<String> levels) {
        RealmQuery<SummitEvent> query = realm.where(SummitEvent.class)
                .greaterThanOrEqualTo("start", startDate)
                .lessThanOrEqualTo("end", endDate);

        if (eventTypes != null) {
            for (int eventTypeId : eventTypes) {
               query = query.equalTo("eventType.id", eventTypeId);
            }
        }

        if (levels != null) {
            for (String level : levels) {
                query = query.equalTo("presentation.level", level);
            }
        }

        if (tracks != null) {
            for (int trackId : tracks) {
                query = query.equalTo("presentation.track.id", trackId);
            }
        }

        return query.findAll();
    }

    public SummitEvent getByIdLocal(int id) {
        return getByIdLocal(id, SummitEvent.class);
    }

    @Override
    public List<String> getPresentationLevelsLocal() {
        ArrayList<String> levels = new ArrayList<>();
        RealmResults<Presentation> presentations = realm.where(Presentation.class).findAll();
        for (Presentation presentation : presentations) {
            if (!levels.contains(presentation.getLevel())) {
                levels.add(presentation.getLevel());
            }
        }

        Collections.sort(levels);

        // HACK: this is to return in a specific order (begginer - intermediate - advanced)
        ArrayList<String> levelsSorted = new ArrayList<>();
        levelsSorted.add(levels.get(1));
        levelsSorted.add(levels.get(2));
        levelsSorted.add(levels.get(0));
        return levelsSorted;
    }

    /*public func getByFilterLocal(startDate: NSDate, endDate: NSDate, eventTypes: [Int]?, summitTypes: [Int]?, tracks: [Int]?, tags: [String]?, levels: [String]?)->[SummitEvent]{
        var events = realm.objects(SummitEvent).filter("start >= %@ and end <= %@", startDate, endDate).sorted("start")
        if (eventTypes != nil && eventTypes!.count > 0) {
            events = events.filter("eventType.id in %@", eventTypes!)
        }
        if (tracks != nil && tracks!.count > 0) {
            events = events.filter("presentation.track.id in %@", tracks!)
        }
        if (levels != nil && levels!.count > 0) {
            events = events.filter("presentation.level in %@", levels!)
        }

        if (summitTypes != nil && summitTypes!.count > 0) {
            for summitTypeId in summitTypes! {
                    events = events.filter("ANY summitTypes.id = %@", summitTypeId)
            }
        }

        if (tags != nil && tags!.count > 0) {
            var tagsFilter = ""
            var separator = ""
            for tag in tags! {
                    tagsFilter += "\(separator)ANY tags.name = '\(tag)'"
                    separator = " OR "
            }
            events = events.filter(tagsFilter)
        }

        return events.map{$0}
    }  */
}
