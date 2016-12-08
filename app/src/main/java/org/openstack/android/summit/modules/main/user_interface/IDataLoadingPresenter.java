package org.openstack.android.summit.modules.main.user_interface;

import org.openstack.android.summit.common.user_interface.IBasePresenter;


/**
 * Created by smarcet on 12/9/16.
 */

public interface IDataLoadingPresenter extends IBasePresenter<IDataLoadingView> {

    boolean shouldShowLoadingDataError();

    void onFailedInitialLoad();

    void newDataAvailable();

    void retryButtonPressed();

    void loadNewDataButtonPressed();

    void cancelLoadNewDataButtonPressed();
}
