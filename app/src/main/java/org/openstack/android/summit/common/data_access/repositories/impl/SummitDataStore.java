package org.openstack.android.summit.common.data_access.repositories.impl;

import android.util.Log;

import com.crashlytics.android.Crashlytics;

import org.openstack.android.summit.common.Constants;
import org.openstack.android.summit.common.data_access.repositories.ISummitDataStore;
import org.openstack.android.summit.common.data_access.repositories.strategies.IDeleteStrategy;
import org.openstack.android.summit.common.data_access.repositories.strategies.ISaveOrUpdateStrategy;
import org.openstack.android.summit.common.entities.Summit;
import org.openstack.android.summit.common.utils.RealmFactory;
import org.openstack.android.summit.common.utils.Void;

import java.security.InvalidParameterException;
import java.util.List;
import io.realm.Sort;

/**
 * Created by Claudio Redi on 11/17/2015.
 */
public class SummitDataStore extends GenericDataStore<Summit> implements ISummitDataStore {

    public SummitDataStore( ISaveOrUpdateStrategy saveOrUpdateStrategy, IDeleteStrategy deleteStrategy) {
        super(Summit.class, saveOrUpdateStrategy, deleteStrategy);
    }

    @Override
    public Summit getLatest() {
        List<Summit> summits = RealmFactory.getSession().where(Summit.class)
                .sort("startDate", Sort.DESCENDING)
                .findAll();
        return summits.size() > 0 ? summits.get(0) : null;
    }

    @Override
    public void updateActiveSummitFromDataUpdate(final Summit dataUpdateEntity) {
        try {

            RealmFactory.transaction(session -> {
                Summit summit = session.where(Summit.class).equalTo("id", dataUpdateEntity.getId()).findFirst();

                if (summit == null)
                    throw new InvalidParameterException(String.format("missing summit %d", dataUpdateEntity.getId()));

                summit.setName(dataUpdateEntity.getName());
                summit.setStartShowingVenuesDate(dataUpdateEntity.getStartShowingVenuesDate());
                summit.setStartDate(dataUpdateEntity.getStartDate());
                summit.setEndDate(dataUpdateEntity.getEndDate());
                summit.setDatesLabel(dataUpdateEntity.getDatesLabel());
                summit.setScheduleEventDetailUrl(dataUpdateEntity.getScheduleEventDetailUrl());
                summit.setSchedulePageUrl(dataUpdateEntity.getSchedulePageUrl());
                summit.setPageUrl(dataUpdateEntity.getPageUrl());

                return Void.getInstance();
            });
        } catch (Exception e) {
            Crashlytics.logException(e);
            Log.e(Constants.LOG_TAG, e.getMessage(), e);
        }
    }

}
