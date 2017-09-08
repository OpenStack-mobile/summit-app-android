package org.openstack.android.summit.common.utils;

import android.content.Context;
import android.util.Log;
import com.crashlytics.android.Crashlytics;
import org.openstack.android.summit.common.Constants;
import org.openstack.android.summit.common.data_access.DataAccessException;

import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

import io.realm.Realm;
import io.realm.RealmConfiguration;

/**
 * Created by sebastian on 8/6/2016.
 */
final public class RealmFactory {

    public static RealmConfiguration buildDefaultConfiguration(Context context) {
        https://realm.io/docs/java/latest/#realms
        // The Realm file will be located in Context.getFilesDir() with name "default.realm"
        Realm.init(context);
        https://realm.io/docs/java/latest/#the-default-realmconfiguration
        return new RealmConfiguration
                .Builder()
                .deleteRealmIfMigrationNeeded()
                .compactOnLaunch()
                .build();
    }

    // Thread local variable containing each thread's ID
    private static final ThreadLocal<Realm> session =
        new ThreadLocal<Realm>() {
        @Override
        protected Realm initialValue() {
            Log.d(Constants.LOG_TAG, String.format("Getting initial Realm instance for thread %s", Thread.currentThread().getName()));
            return Realm.getDefaultInstance();
        }
    };

    private static final ThreadLocal<AtomicInteger> txCounter =
            new ThreadLocal<AtomicInteger >() {
            @Override
            protected AtomicInteger  initialValue() {
                return new AtomicInteger(0);
            }
    };

    private static Integer decrementTxCounter(){ return txCounter.get().decrementAndGet() ;};

    private static Integer incrementTxCounter(){ return txCounter.get().incrementAndGet() ;};

    private static Object lockCloseSession = new Object();

    public static Realm getSession() {
        return session.get();
    }

    public static void closeSession(){
        synchronized (lockCloseSession) {
            Realm realmSession = session.get();
            if (realmSession != null && !realmSession.isClosed()) {
                Log.d(Constants.LOG_TAG, String.format("Closing Realm instance for thread %s", Thread.currentThread().getName()));
                realmSession.close();
            }
            session.remove();
        }
    }

    /**
     * Reference counting is provide by RealmCache ( internal class)
     *
     * @param callback
     * @param <T>
     * @return
     * @throws DataAccessException
     */
    public static <T> T transaction(IRealmCallback<T> callback) throws DataAccessException {

        Realm session = null;
        T res         = null;
        try {
            session = getSession();
            beginTransaction(session);
            res = callback.callback(session);
            commitTransaction(session);
        } catch (Exception ex) {
            rollbackTransaction(session);
            Crashlytics.logException(ex);
            Log.e(Constants.LOG_TAG, ex.getMessage(), ex);
            throw new DataAccessException(ex);
        }
        return res;
    }

    private static void beginTransaction(Realm session){
        Integer counter = incrementTxCounter();
        if(session != null && counter == 1) {
            session.beginTransaction();
        }
    }

    private static void commitTransaction(Realm session){
        Integer counter = decrementTxCounter();
        if(session != null && counter == 0) {
            session.commitTransaction();
        }
    }

    private static void rollbackTransaction(Realm session){
        Integer counter = decrementTxCounter();
        if(session != null && counter == 0) {
            session.cancelTransaction();
        }
    }

    public interface IRealmCallback<T> {
        T callback(Realm session) throws Exception;
    }

}
