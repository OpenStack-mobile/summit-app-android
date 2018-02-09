package org.openstack.android.summit.modules.main.user_interface;

import android.util.Log;

import org.openstack.android.summit.common.Constants;
import org.openstack.android.summit.common.DTOs.SummitDTO;
import org.openstack.android.summit.common.IBaseWireframe;
import org.openstack.android.summit.common.api.ISummitSelector;
import org.openstack.android.summit.common.user_interface.BasePresenter;
import org.openstack.android.summit.modules.main.business_logic.IDataLoadingInteractor;

/**
 * Created by smarcet on 12/9/16.
 */

public class SummitListDataLoadingPresenter
        extends BasePresenter<IDataLoadingView, IDataLoadingInteractor, IBaseWireframe>
implements IDataLoadingPresenter {

    private ISummitSelector summitSelector;

    public SummitListDataLoadingPresenter
    (
        IDataLoadingInteractor interactor,
        IBaseWireframe wireframe,
        ISummitSelector summitSelector
    )
    {
        super(interactor, wireframe);
        this.summitSelector = summitSelector;
    }

    @Override
    public boolean shouldShowLoadingDataError() {
        return !interactor.isDataLoaded();
    }

    @Override
    public void onFailedInitialLoad() {
        view.hideActivityIndicator();
        if(shouldShowLoadingDataError()) {
            view.showErrorContainer(true);
            return;
        }
       view.finishOk();
    }

    @Override
    public void newDataAvailable() {
        view.hideActivityIndicator();
        view.askToLoadNewData();
    }

    @Override
    public void retryButtonPressed() {
        view.doDataLoading();
    }

    @Override
    public void loadNewDataButtonPressed() {
        Log.d(Constants.LOG_TAG, "SummitsListDataLoaderActivity.doLoad.setOnClickListener");
        SummitDTO  summit = interactor.getLatestSummit();
        if(summit != null) {
            summitSelector.setCurrentSummitId(summit.getId());
            view.finishOkWithNewData();
        }
    }

    @Override
    public void cancelLoadNewDataButtonPressed() {
        view.finishOk();
    }
}
