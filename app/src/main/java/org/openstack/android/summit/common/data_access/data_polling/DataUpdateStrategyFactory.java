package org.openstack.android.summit.common.data_access.data_polling;

/**
 * Created by Claudio Redi on 2/8/2016.
 */
public class DataUpdateStrategyFactory implements IDataUpdateStrategyFactory {
    IDataUpdateStrategy genericDataUpdateProcessStrategy;
    IDataUpdateStrategy myScheduleDataUpdateStrategy;

    public DataUpdateStrategyFactory(IDataUpdateStrategy genericDataUpdateProcessStrategy, IDataUpdateStrategy myScheduleDataUpdateStrategy) {
        this.genericDataUpdateProcessStrategy = genericDataUpdateProcessStrategy;
        this.myScheduleDataUpdateStrategy = myScheduleDataUpdateStrategy;
    }

    @Override
    public IDataUpdateStrategy create(String className) {
        IDataUpdateStrategy dataUpdateProcessStrategy;

        switch (className) {
            case "MySchedule":
                dataUpdateProcessStrategy = myScheduleDataUpdateStrategy;
                break;
            default:
                dataUpdateProcessStrategy = genericDataUpdateProcessStrategy;
        }
        return dataUpdateProcessStrategy;
    }
}
