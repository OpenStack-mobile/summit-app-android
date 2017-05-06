package org.openstack.android.summit.modules.speakers_list.user_interface;

import android.net.Uri;
import android.os.Bundle;

import org.openstack.android.summit.common.DTOs.PersonListItemDTO;
import org.openstack.android.summit.common.user_interface.BasePresenter;
import org.openstack.android.summit.modules.speakers_list.ISpeakerListWireframe;
import org.openstack.android.summit.modules.speakers_list.business_logic.ISpeakerListInteractor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import javax.inject.Inject;

/**
 * Created by Claudio Redi on 1/13/2016.
 */
public class SpeakerListPresenter
        extends BasePresenter<ISpeakerListView, ISpeakerListInteractor, ISpeakerListWireframe>
        implements ISpeakerListPresenter {

    List<PersonListItemDTO> speakers   = new ArrayList<PersonListItemDTO>();
    private Boolean loadedAllSpeakers  = false;

    @Inject
    public SpeakerListPresenter(ISpeakerListInteractor interactor, ISpeakerListWireframe wireframe) {
        super(interactor, wireframe);
    }

    @Override
    public void onCreateView(Bundle savedInstanceState) {
        loadedAllSpeakers = false;
        loadData();
    }


    @Override
    public void showSpeakerProfile(int position) {
        if(speakers == null || speakers.isEmpty() || (speakers.size() - 1) < position || position < 0 ) return;
        PersonListItemDTO speaker = speakers.get(position);
        wireframe.showSpeakerProfile(speaker.getId(), view);
    }

    public void loadData() {
        if (loadedAllSpeakers) {
            return;
        }

        List<PersonListItemDTO> speakersPage = interactor.getAllSpeakers();
        speakers.addAll(speakersPage);
        view.setSpeakers(speakers);
        loadedAllSpeakers = true;

        HashMap<String, Integer> mapIndex = createMapIndex();
        view.setIndex(mapIndex);
    }

    @Override
    public void buildItem(SpeakerListAdapter.SpeakerItemViewHolder speakerItemView, int position) {
        if(speakers == null || speakers.isEmpty() || (speakers.size() - 1) < position || position < 0 ) return;

        PersonListItemDTO personListItemDTO = speakers.get(position);
        speakerItemView.setName(personListItemDTO.getName());
        speakerItemView.setTitle(personListItemDTO.getTitle());
        speakerItemView.setIsModerator(false);
        Uri uri = Uri.parse(personListItemDTO.getPictureUrl().replace("https", "http"));
        speakerItemView.setPictureUri(uri);
    }

    public HashMap<String, Integer> createMapIndex() {
        HashMap<String, Integer> mapIndex = new LinkedHashMap<String, Integer>();
        for (int i = 0; i < speakers.size(); i++){
            String index = null;
            String name  = speakers.get(i).getName();
            if(!name.trim().isEmpty()) {
                index = name.substring(0, 1);
                index = index.toUpperCase();
            }
            else{
                index = "#";
            }

            if (!mapIndex.containsKey(index)) {
                mapIndex.put(index, i);
            }
        }
        return mapIndex;
    }

    @Override public void onDestroy(){
        super.onDestroy();
        loadedAllSpeakers = false;
        speakers = null;
        speakers = new ArrayList<>();
    }

}
