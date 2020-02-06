package org.openstack.android.summit.common.entities.notifications;

import org.openstack.android.summit.common.entities.IEntity;
import org.openstack.android.summit.common.entities.Member;
import org.openstack.android.summit.common.entities.Summit;
import org.openstack.android.summit.common.entities.SummitEvent;

import java.util.Date;

/**
 * Created by smarcet on 1/24/17.
 */

public interface IPushNotification extends IEntity {

    String getTitle();

    void setTitle(String title);

    String getChannel();

    void setChannel(String channel);

    Date getCreatedAt();

    void setCreatedAt(Date created_at);

    Member getOwner();

    void setOwner(Member owner);

    Summit getSummit() ;

    void setSummit(Summit summit);

    String getBody();

    void setBody(String body);

    void setType(String type);

    String getType();

    boolean isOpened();

    void markAsRead();

    SummitEvent getEvent();

    void setEvent(SummitEvent event);
}
