package org.openstack.android.summit.modules.main.user_interface;

import org.openstack.android.summit.common.user_interface.IBaseView;

/**
 * Created by smarcet on 12/9/16.
 */

public interface IDataLoadingView extends IBaseView {

    void hideActivityIndicator();

    void showErrorContainer(boolean show);

    void askToLoadNewData();

    void doDataLoading();

    void finish();

    void finishOk();

    void finishOkWithNewData();
}
