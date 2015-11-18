package org.openstack.android.openstacksummit.modules.general_schedule.business_logic;

import org.openstack.android.openstacksummit.common.data_access.IDataStoreOperationListener;
import org.openstack.android.openstacksummit.common.data_access.ISummitRemoteDataStore;
import org.openstack.android.openstacksummit.common.entities.Summit;

import javax.inject.Inject;

/**
 * Created by claudio on 10/30/2015.
 */
public class GeneralScheduleInteractor implements IDataStoreOperationListener<Summit> {
    ISummitRemoteDataStore summitRemoteDataStore;

    @Inject
    public GeneralScheduleInteractor(ISummitRemoteDataStore summitRemoteDataStore) {
        this.summitRemoteDataStore = summitRemoteDataStore;
    }

    @Override
    public void onSuceedWithData(Summit data) {

    }

    @Override
    public void onSucceed() {

    }

    @Override
    public void onError(String message) {

    }

    //public getActiveSummit()

    /*public func getActiveSummit(completionBlock: (SummitDTO?, NSError?) -> Void) {
        summitDataStore.getActive() { summit, error in
            var summitDTO: SummitDTO?
            if (error == nil) {
                summitDTO = self.summitDTOAssembler.createDTO(summit!)
            }
            completionBlock(summitDTO, error)
        }
    }  */
}
