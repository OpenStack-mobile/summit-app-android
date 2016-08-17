package org.openstack.android.summit.common.DTOs;

/**
 * Created by sebastian on 8/17/2016.
 */
public class VideoDTO extends NamedDTO {
    private String youTubeId;

    public String getYouTubeId() {
        return youTubeId;
    }

    public void setYouTubeId(String youTubeId) {
        this.youTubeId = youTubeId;
    }
}
