package org.openstack.android.summit.common.DTOs;

/**
 * Created by Claudio Redi on 3/31/2016.
 */
public class NonConfirmedSummitAttendeeDTO {
    private int id;
    private String name;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return getName();
    }
}
