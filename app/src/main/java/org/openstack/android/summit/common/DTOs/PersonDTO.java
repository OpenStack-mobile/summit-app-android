package org.openstack.android.summit.common.DTOs;

/**
 * Created by Claudio Redi on 11/18/2015.
 */
public class PersonDTO extends PersonListItemDTO  {
    private String bio;
    private String twitter;

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public String getTwitter() {
        return twitter;
    }

    public void setTwitter(String twitter) {
        this.twitter = twitter;
    }
}
