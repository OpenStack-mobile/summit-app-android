package org.openstack.android.summit.common.entities;

import org.openstack.android.summit.common.utils.RealmFactory;

import io.realm.Realm;
import io.realm.RealmModel;

/**
 * Created by smarcet on 2/8/18.
 */

final public class RealmIdGenerator {

    public static final <E extends RealmModel> int generateKey(Class<E> clazz){
        int id = 0;
        Realm session = RealmFactory.getSession();
        do{
            id = ((int) System.currentTimeMillis() / 1000);
        }
        while(session.where(clazz).equalTo("id", id).count() > 0 );
        return id;
    }
}
