package org.openstack.android.summit.common.data_access.data_polling;

/**
 * Created by Claudio Redi on 2/8/2016.
 */
public class DataUpdateStrategyFactory implements IDataUpdateStrategyFactory {
    IDataUpdateStrategy genericDataUpdateProcessStrategy;
    IDataUpdateStrategy myScheduleDataUpdateStrategy;
    IDataUpdateStrategy summitDataUpdateStrategy;
    IDataUpdateStrategy trackGroupDataUpdateStrategy;
    IDataUpdateStrategy venueImageDataUpdateStrategy;

    public DataUpdateStrategyFactory(IDataUpdateStrategy genericDataUpdateProcessStrategy, IDataUpdateStrategy myScheduleDataUpdateStrategy, IDataUpdateStrategy summitDataUpdateStrategy, IDataUpdateStrategy trackGroupDataUpdateStrategy, IDataUpdateStrategy venueImageDataUpdateStrategy) {
        this.genericDataUpdateProcessStrategy = genericDataUpdateProcessStrategy;
        this.myScheduleDataUpdateStrategy     = myScheduleDataUpdateStrategy;
        this.summitDataUpdateStrategy         = summitDataUpdateStrategy;
        this.trackGroupDataUpdateStrategy     = trackGroupDataUpdateStrategy;
        this.venueImageDataUpdateStrategy     = venueImageDataUpdateStrategy;
    }

    @Override
    public IDataUpdateStrategy create(String className) {
        IDataUpdateStrategy dataUpdateProcessStrategy;

        switch (className) {
            case "MySchedule":
                dataUpdateProcessStrategy = myScheduleDataUpdateStrategy;
                break;
            case "Summit":
                dataUpdateProcessStrategy = summitDataUpdateStrategy;
                break;
            case "PresentationCategoryGroup":
                dataUpdateProcessStrategy = trackGroupDataUpdateStrategy;
                break;
            case "SummitLocationImage":
            case "SummitLocationMap":
                dataUpdateProcessStrategy = venueImageDataUpdateStrategy;
                break;
            default:
                dataUpdateProcessStrategy = genericDataUpdateProcessStrategy;
        }
        return dataUpdateProcessStrategy;
    }
}
