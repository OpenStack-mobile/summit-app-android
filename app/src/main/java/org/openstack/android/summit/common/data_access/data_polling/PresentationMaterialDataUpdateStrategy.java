package org.openstack.android.summit.common.data_access.data_polling;
import android.util.Log;

import com.crashlytics.android.Crashlytics;

import org.json.JSONObject;
import org.openstack.android.summit.common.Constants;
import org.openstack.android.summit.common.data_access.IGenericDataStore;
import org.openstack.android.summit.common.entities.DataUpdate;
import org.openstack.android.summit.common.entities.Presentation;
import org.openstack.android.summit.common.entities.PresentationLink;
import org.openstack.android.summit.common.entities.PresentationSlide;
import org.openstack.android.summit.common.entities.PresentationVideo;
import org.openstack.android.summit.common.utils.RealmFactory;
import org.openstack.android.summit.common.utils.Void;

import io.realm.Realm;

/**
 * Created by sebastian on 8/10/2016.
 */
public class PresentationMaterialDataUpdateStrategy extends DataUpdateStrategy  {

    public PresentationMaterialDataUpdateStrategy(IGenericDataStore genericDataStore) {
        super(genericDataStore);
    }

    @Override
    public void process(final DataUpdate dataUpdate) throws DataUpdateException {
        final String className      = dataUpdate.getEntityClassName();
        switch (dataUpdate.getOperation()) {
            case DataOperation.Insert:
                JSONObject entityJSON = dataUpdate.getOriginalJSON().optJSONObject("entity");
                if(entityJSON == null) return;
                final Integer presentation_id = entityJSON.optInt("presentation_id");

                if (presentation_id == null)
                    throw new DataUpdateException("It wasn't possible to find presentation_id on data update json");

                try {

                    RealmFactory.transaction(new RealmFactory.IRealmCallback<Void>() {
                        @Override
                        public Void callback(Realm session) throws Exception {
                            Presentation managedPresentation = genericDataStore.getByIdLocal(presentation_id, Presentation.class);
                            if (managedPresentation == null)
                                throw new DataUpdateException(String.format("Presentation with id %d not found", presentation_id));

                            if (className.equals("PresentationSlide")) {
                                PresentationSlide slide = (PresentationSlide) dataUpdate.getEntity();
                                slide.setPresentation(managedPresentation);
                                managedPresentation.getSlides().add(slide);
                            }
                            if (className.equals("PresentationVideo")) {
                                PresentationVideo video = (PresentationVideo) dataUpdate.getEntity();
                                video.setPresentation(managedPresentation);
                                managedPresentation.getVideos().add(video);
                            }
                            if (className.equals("PresentationLink")) {
                                PresentationLink link = (PresentationLink) dataUpdate.getEntity();
                                link.setPresentation(managedPresentation);
                                managedPresentation.getLinks().add(link);
                            }
                            return Void.getInstance();
                        }
                    });
                }
                catch (Exception ex){
                    Crashlytics.logException(ex);
                    Log.e(Constants.LOG_TAG, ex.getMessage(), ex);
                }
                break;
            case DataOperation.Update:
                if(className.equals("PresentationSlide")){
                    genericDataStore.saveOrUpdate((PresentationSlide)dataUpdate.getEntity(), null, PresentationSlide.class);
                }
                if(className.equals("PresentationVideo")){
                    genericDataStore.saveOrUpdate((PresentationVideo)dataUpdate.getEntity(), null, PresentationVideo.class);
                }
                if(className.equals("PresentationLink")){
                    genericDataStore.saveOrUpdate((PresentationLink)dataUpdate.getEntity(), null, PresentationLink.class);
                }
                break;
            case DataOperation.Delete:
                if(className.equals("PresentationSlide")){
                    genericDataStore.delete(((PresentationSlide)dataUpdate.getEntity()).getId(), null, PresentationSlide.class);
                }
                if(className.equals("PresentationVideo")){
                    genericDataStore.delete(((PresentationVideo)dataUpdate.getEntity()).getId(), null, PresentationVideo.class);
                }
                if(className.equals("PresentationLink")){
                    genericDataStore.delete(((PresentationLink)dataUpdate.getEntity()).getId(), null, PresentationLink.class);
                }
                break;
        }
    }
}

