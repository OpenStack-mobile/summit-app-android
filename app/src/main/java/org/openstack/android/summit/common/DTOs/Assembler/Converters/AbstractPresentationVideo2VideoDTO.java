package org.openstack.android.summit.common.DTOs.Assembler.Converters;

import android.util.Log;
import com.crashlytics.android.Crashlytics;
import org.modelmapper.AbstractConverter;
import org.openstack.android.summit.common.Constants;
import org.openstack.android.summit.common.DTOs.VideoDTO;
import org.openstack.android.summit.common.entities.PresentationVideo;

/**
 * Created by sebastian on 8/17/2016.
 */
public class AbstractPresentationVideo2VideoDTO<S extends PresentationVideo> extends AbstractConverter<S, VideoDTO> {

    @Override
    protected VideoDTO convert(S source) {
        VideoDTO video = new VideoDTO();

        try {

            video.setId(source.getId());
            video.setName(source.getName());
            video.setYouTubeId(source.getYouTubeId());
        } catch (Exception e) {
            Crashlytics.logException(e);
            Log.e(Constants.LOG_TAG, e.getMessage(), e);
            throw e;
        }
        return video;
    }
}
