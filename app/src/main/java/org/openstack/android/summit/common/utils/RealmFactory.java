package org.openstack.android.summit.common.utils;

import android.content.Context;
import android.util.Log;
import com.crashlytics.android.Crashlytics;
import org.openstack.android.summit.common.Constants;
import org.openstack.android.summit.common.data_access.DataAccessException;
import io.realm.Realm;
import io.realm.RealmConfiguration;

/**
 * Created by sebastian on 8/6/2016.
 */
final public class RealmFactory {

    public static RealmConfiguration buildDefaultConfiguration(Context context) {
        return new RealmConfiguration.Builder(context)
                .deleteRealmIfMigrationNeeded()
                .build();
    }

    // Thread local variable containing each thread's ID
    private static final ThreadLocal<Realm> session =
        new ThreadLocal<Realm>() {
        @Override
        protected Realm initialValue() {
            return Realm.getDefaultInstance();
        }
    };

    public static Realm getSession() {
        return session.get();
    }

    public static void closeSession(){
        session.get().close();
        session.remove();
    }

    /**
     * Reference couting is provide by RealmCache ( internal class)
     *
     * @param callback
     * @param <T>
     * @return
     * @throws DataAccessException
     */
    public static <T> T transaction(IRealmCallback<T> callback) throws DataAccessException {

        Realm session = null;
        T res = null;
        try {
            session = getSession();
            session.beginTransaction();
            res = callback.callback(session);
            session.commitTransaction();
        } catch (Exception ex) {
            session.cancelTransaction();
            Crashlytics.logException(ex);
            Log.e(Constants.LOG_TAG, ex.getMessage(), ex);
            throw new DataAccessException(ex);
        }
        return res;
    }

    public interface IRealmCallback<T> {
        T callback(Realm session) throws Exception;
    }

}
