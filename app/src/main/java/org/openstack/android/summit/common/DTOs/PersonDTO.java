package org.openstack.android.summit.common.DTOs;

import java.util.Locale;

/**
 * Created by Claudio Redi on 11/18/2015.
 */
public class PersonDTO extends PersonListItemDTO  {

    private String firstName;
    private String lastName;
    private String bio;
    private String twitter;
    private String irc;

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getFullName(){ return String.format(Locale.US, "%s %s", firstName, lastName); }

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

    public String getIrc() {
        return irc;
    }

    public void setIrc(String irc) {
        this.irc = irc;
    }
}
