package org.openstack.android.summit.modules.general_schedule_filter.user_interface;

import android.graphics.Color;
import android.os.Bundle;
import com.crashlytics.android.Crashlytics;
import org.openstack.android.summit.common.DTOs.NamedDTO;
import org.openstack.android.summit.common.IScheduleFilter;
import org.openstack.android.summit.modules.general_schedule_filter.IGeneralScheduleFilterWireframe;
import org.openstack.android.summit.modules.general_schedule_filter.business_logic.IGeneralScheduleFilterInteractor;

import java.util.List;

public class GeneralScheduleRoomsFilterPresenter
        extends AbstractScheduleFilterPresenter<IGeneralScheduleRoomsFilterView>
        implements IGeneralScheduleRoomsFilterPresenter {


    private List<NamedDTO> rooms;

    public GeneralScheduleRoomsFilterPresenter
            (
                    IGeneralScheduleFilterInteractor interactor,
                    IGeneralScheduleFilterWireframe wireframe,
                    IScheduleFilter scheduleFilter
            )
    {
        super(interactor, wireframe, scheduleFilter);
    }

    @Override
    public void onCreateView(Bundle savedInstanceState) {
        try {
            super.onCreate(savedInstanceState);
            Integer venueId = view.getSelectedVenueId();
            NamedDTO venueDTO = interactor.getVenue(venueId);
            rooms = interactor.getRoomsForVenue(venueId);

            scheduleFilter.removeFilterSectionByName(FilterSectionType.Rooms.toString());

            MultiFilterSection filterSection = new MultiFilterSection();
            filterSection.setType(FilterSectionType.Rooms);
            filterSection.setName(FilterSectionType.Rooms.toString());
            FilterSectionItem filterSectionItem;

            for (NamedDTO room : rooms) {
                filterSectionItem = createSectionItem(room.getId(), room.getName(), filterSection.getType());
                filterSection.getItems().add(filterSectionItem);
            }

            scheduleFilter.getFilterSections().add(filterSection);
            view.showRooms(rooms);
            view.setTitle(venueDTO.getName());
        }
        catch (Exception ex){
            Crashlytics.logException(ex);
        }
    }

    @Override
    public void toggleSelectionRooms(IGeneralScheduleFilterItemView item, int position) {
        AbstractFilterSection filterSection = scheduleFilter.getFilterSectionByName(FilterSectionType.Rooms.toString());
        NamedDTO room = rooms.get(position);
        toggleSelection(
                item,
                Color.WHITE, Color.LTGRAY,
                (MultiFilterSection) filterSection,
                position
        );
    }

    @Override
    public void buildRoomFilterItem(GeneralScheduleFilterItemView item, int position) {
        AbstractFilterSection filterSection = scheduleFilter.getFilterSectionByName(FilterSectionType.Rooms.toString());
        NamedDTO room = rooms.get(position);
        buildFilterItem(
                item,
                Color.WHITE, Color.LTGRAY,
                true,
                (MultiFilterSection) filterSection,
                position);
    }
}
