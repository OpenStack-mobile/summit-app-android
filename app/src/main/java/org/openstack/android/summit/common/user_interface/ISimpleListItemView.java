package org.openstack.android.summit.common.user_interface;

/**
 * Created by Claudio Redi on 1/12/2016.
 */
public interface ISimpleListItemView {
    void setName(String name);

    void setColor(String color);

    void allowNavigation(boolean allow);
}
