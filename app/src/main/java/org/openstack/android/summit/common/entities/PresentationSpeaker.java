package org.openstack.android.summit.common.entities;

import io.realm.RealmObject;

public class PresentationSpeaker extends RealmObject {

    private String role;

    private Speaker speaker;

    public PresentationSpeaker(){
        super();
    }

    public PresentationSpeaker(Speaker speaker, String role){
        this();
        this.speaker = speaker;
        this.role    = role;
    }

    public String getRole() {
        return role;
    }

    public Speaker getSpeaker() {
        return speaker;
    }
}
