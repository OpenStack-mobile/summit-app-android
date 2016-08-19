package org.openstack.android.summit.common.entities;

/**
 * Created by Claudio Redi on 11/5/2015.
 */
public interface IPerson extends IEntity {
    public String getFirstName();
    public void setFirstName(String firstName);
    public String getLastName();
    public void setLastName(String lastName);
    public String getFullName();
    public void setFullName(String fullName);
    public String getTitle();
    public void setTitle(String title);
    public String getPictureUrl();
    public void setPictureUrl(String pictureUrl);
    public String getBio();
    public void setBio(String bio);
    public String getTwitter();
    public void setTwitter(String twitter);
    public String getIrc();
    public void setIrc(String irc);
    public String getEmail();
    public void setEmail(String email);
}
