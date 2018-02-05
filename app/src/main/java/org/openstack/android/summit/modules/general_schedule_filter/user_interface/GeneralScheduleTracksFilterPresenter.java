package org.openstack.android.summit.modules.general_schedule_filter.user_interface;

import android.graphics.Color;
import android.os.Bundle;

import org.openstack.android.summit.common.DTOs.NamedDTO;
import org.openstack.android.summit.common.DTOs.TrackDTO;
import org.openstack.android.summit.common.IScheduleFilter;
import org.openstack.android.summit.modules.general_schedule_filter.IGeneralScheduleFilterWireframe;
import org.openstack.android.summit.modules.general_schedule_filter.business_logic.IGeneralScheduleFilterInteractor;

import java.util.List;

/**
 * Created by smarcet on 2/5/18.
 */

public class GeneralScheduleTracksFilterPresenter
        extends AbstractScheduleFilterPresenter<IGeneralScheduleTracksFilterView>
        implements IGeneralScheduleTracksFilterPresenter
{
    private List<TrackDTO>  tracks;

    public GeneralScheduleTracksFilterPresenter
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
        super.onCreate(savedInstanceState);
        Integer     trackGroupId = view.getSelectedTrackGroupId();
        tracks      = interactor.getTracksForGroup(trackGroupId);

        scheduleFilter.removeFilterSectionByName(FilterSectionType.Tracks.toString());

        MultiFilterSection filterSection = new MultiFilterSection();
        filterSection.setType(FilterSectionType.Tracks);
        filterSection.setName(FilterSectionType.Tracks.toString());
        FilterSectionItem filterSectionItem;
        for (NamedDTO track : tracks) {
            filterSectionItem = createSectionItem(track.getId(), track.getName(), filterSection.getType());
            filterSection.getItems().add(filterSectionItem);
        }
        scheduleFilter.getFilterSections().add(filterSection);
        view.showTracks(tracks);
    }

    @Override
    public void toggleSelectionTrack(IGeneralScheduleFilterItemView item, int position) {
        AbstractFilterSection filterSection = scheduleFilter.getFilterSectionByName("Tracks");
        TrackDTO trackDTO = tracks.get(position);
        toggleSelection(
                item,
                Color.parseColor(trackDTO.getTrackGroup().getColor()),
                Color.parseColor(trackDTO.getTrackGroup().getColor()),
                (MultiFilterSection) filterSection,
                position
        );
    }

    @Override
    public void buildTrackFilterItem(GeneralScheduleFilterItemView item, int position) {
        AbstractFilterSection filterSection = scheduleFilter.getFilterSectionByName(FilterSectionType.Tracks.toString());
        TrackDTO trackDTO = tracks.get(position);
        buildFilterItem(
                item,
                Color.parseColor(trackDTO.getTrackGroup().getColor()),
                Color.parseColor(trackDTO.getTrackGroup().getColor()),
                true,
                (MultiFilterSection) filterSection,
                position);
    }

}
