package org.openstack.android.summit.common.entities;

/**
 * Created by smarcet on 1/30/17.
 */

public interface IPresentation extends ISummitEvent {

    void setEvent(ISummitEvent event);

    ISummitEvent getEvent();
    /**
     *   'Level' => "Enum('Beginner,Intermediate,Advanced,N/A')",
     */
    public final static String LevelBeginner     = "Beginner";
    public final static String LevelIntermediate = "Intermediate";
    public final static String LevelAdvanced     = "Advanced";
    public final static String LevelNA           = "N/A";

}
