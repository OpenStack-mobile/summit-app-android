package org.openstack.android.summit.common.entities.processable_user_actions;

import org.openstack.android.summit.common.entities.Member;
import org.openstack.android.summit.common.entities.SummitEvent;

import java.util.Date;
import java.util.UUID;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by smarcet on 2/8/18.
 */

public class MyFavoriteProcessableUserAction  extends RealmObject {

    public MyFavoriteProcessableUserAction(){
        this.isProcessed   = false;
        this.processedDate = null;
    }

    public MyFavoriteProcessableUserAction(MyFavoriteProcessableUserAction.Type type, Member owner, SummitEvent event){
        this();
        this.type  = type.toString();
        this.owner = owner;
        this.event = event;
    }

    public enum Type {
        Add,
        Remove,
    }

    @PrimaryKey
    private String id = UUID.randomUUID().toString();

    private String type;

    private SummitEvent event;

    private Member owner;

    private boolean isProcessed;

    private Date processedDate;

    public String getId() {
        return id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public SummitEvent getEvent() {
        return event;
    }

    public void setEvent(SummitEvent event) {
        this.event = event;
    }

    public Member getOwner() {
        return owner;
    }

    public void setOwner(Member owner) {
        this.owner = owner;
    }

    public boolean isProcessed() {
        return isProcessed;
    }

    public void setProcessed(boolean processed) {
        isProcessed = processed;
    }

    public Date getProcessedDate() {
        return processedDate;
    }

    public void setProcessedDate(Date processedDate) {
        this.processedDate = processedDate;
    }

    public void markAsProcessed(){
        this.isProcessed   = true;
        this.processedDate = new Date();
    }
}
