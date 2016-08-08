package org.openstack.android.summit.common.utils;

import android.content.Context;
import io.realm.Realm;
import io.realm.RealmConfiguration;

/**
 * Created by sebastian on 8/6/2016.
 */
final public class RealmFactory {

    public static RealmConfiguration buildDefaultConfiguration(Context context){
        return new RealmConfiguration.Builder(context)
                .deleteRealmIfMigrationNeeded()
                .build();
    }

    public static Realm getSession() {
        Realm session = Realm.getDefaultInstance();
        session.setAutoRefresh(true);
        return session;
    }
}
