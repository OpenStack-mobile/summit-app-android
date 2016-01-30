package org.openstack.android.summit.modules.speakers_list.user_interface;

import android.net.Uri;
import android.os.Bundle;

import org.openstack.android.summit.common.DTOs.PersonListItemDTO;
import org.openstack.android.summit.common.user_interface.BasePresenter;
import org.openstack.android.summit.common.user_interface.PersonItemView;
import org.openstack.android.summit.modules.speakers_list.ISpeakerListWireframe;
import org.openstack.android.summit.modules.speakers_list.business_logic.ISpeakerListInteractor;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

/**
 * Created by Claudio Redi on 1/13/2016.
 */
public class SpeakerListPresenter extends BasePresenter<ISpeakerListView, ISpeakerListInteractor, ISpeakerListWireframe> implements ISpeakerListPresenter {

    List<PersonListItemDTO> speakers = new ArrayList<PersonListItemDTO>();
    private int page = 1;
    private final int OBJECTS_PER_PAGE = 20;
    private Boolean loadedAllSpeakers = false;

    @Inject
    public SpeakerListPresenter(ISpeakerListInteractor interactor, ISpeakerListWireframe wireframe) {
        super(interactor, wireframe);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        loadData();
    }

    public int getObjectsPerPage() {
        return OBJECTS_PER_PAGE;
    }

    @Override
    public void showSpeakerProfile(int position) {
        PersonListItemDTO speaker = speakers.get(position);
        wireframe.showSpeakerProfile(speaker.getId(), view);
    }

    public void loadData() {
        if (loadedAllSpeakers) {
            return;
        }

        List<PersonListItemDTO> speakersPage = interactor.getSpeakers(page, OBJECTS_PER_PAGE);
        speakers.addAll(speakersPage);
        view.setSpeakers(speakers);
        loadedAllSpeakers = speakersPage.size() < OBJECTS_PER_PAGE;
        page++;
    }

    @Override
    public void buildItem(PersonItemView personItemView, int position) {
        PersonListItemDTO personListItemDTO = speakers.get(position);
        personItemView.setName(personListItemDTO.getName());
        personItemView.setTitle(personListItemDTO.getTitle());

        Uri uri = Uri.parse(personListItemDTO.getPictureUrl().replace("https", "http"));
        personItemView.setPictureUri(uri);
    }
}
