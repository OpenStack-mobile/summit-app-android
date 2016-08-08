package org.openstack.android.summit.common.data_access;

import org.openstack.android.summit.common.utils.RealmFactory;
import org.powermock.api.mockito.PowerMockito;

import io.realm.Realm;
import io.realm.RealmObject;
import io.realm.RealmQuery;
import io.realm.RealmResults;

import static org.mockito.Mockito.mock;
import static org.powermock.api.mockito.PowerMockito.mockStatic;
import static org.powermock.api.mockito.PowerMockito.when;

/**
 * Created by Claudio Redi on 1/13/2016.
 */
public class MockSupport {

    public static Realm mockRealm() {
        mockStatic(Realm.class);

        Realm mockRealm = PowerMockito.mock(Realm.class);

        when(RealmFactory.getSession()).thenReturn(mockRealm);

        return mockRealm;
    }

    @SuppressWarnings("unchecked")
    public static <T extends RealmObject> RealmQuery<T> mockRealmQuery() {
        return mock(RealmQuery.class);
    }

    @SuppressWarnings("unchecked")
    public static <T extends RealmObject> RealmResults<T> mockRealmResults() {
        mockStatic(RealmResults.class);
        return mock(RealmResults.class);
    }
}