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
    IDataUpdateStrategy presentationMaterialDataUpdateStrategy;
    IDataUpdateStrategy venueLocationsDataUpdateStrategy;

    public DataUpdateStrategyFactory(IDataUpdateStrategy genericDataUpdateProcessStrategy, IDataUpdateStrategy myScheduleDataUpdateStrategy, IDataUpdateStrategy summitDataUpdateStrategy, IDataUpdateStrategy trackGroupDataUpdateStrategy, IDataUpdateStrategy venueImageDataUpdateStrategy, IDataUpdateStrategy presentationMaterialDataUpdateStrategy, IDataUpdateStrategy venueLocationsDataUpdateStrategy) {
        this.genericDataUpdateProcessStrategy       = genericDataUpdateProcessStrategy;
        this.myScheduleDataUpdateStrategy           = myScheduleDataUpdateStrategy;
        this.summitDataUpdateStrategy               = summitDataUpdateStrategy;
        this.trackGroupDataUpdateStrategy           = trackGroupDataUpdateStrategy;
        this.venueImageDataUpdateStrategy           = venueImageDataUpdateStrategy;
        this.presentationMaterialDataUpdateStrategy = presentationMaterialDataUpdateStrategy;
        this.venueLocationsDataUpdateStrategy       = venueLocationsDataUpdateStrategy;
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
            case "PresentationVideo":
            case "PresentationLink":
            case "PresentationSlide":
                dataUpdateProcessStrategy = presentationMaterialDataUpdateStrategy;
                break;
            case "SummitVenueFloor":
            case "SummitVenueRoom":
                dataUpdateProcessStrategy = venueLocationsDataUpdateStrategy;
                break;
            default:
                dataUpdateProcessStrategy = genericDataUpdateProcessStrategy;
        }
        return dataUpdateProcessStrategy;
    }
}
