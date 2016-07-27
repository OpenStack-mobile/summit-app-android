package org.openstack.android.summit.common.data_access.data_polling;

import android.util.Log;

import org.openstack.android.summit.common.Constants;
import org.openstack.android.summit.common.entities.Image;
import org.openstack.android.summit.common.entities.SummitEvent;
import org.openstack.android.summit.common.entities.Track;
import org.openstack.android.summit.common.entities.TrackGroup;
import org.openstack.android.summit.common.entities.VenueFloor;

/**
 * Created by Claudio Redi on 2/8/2016.
 */
public class ClassResolver implements IClassResolver {
    @Override
    public Class fromName(String className) throws ClassNotFoundException{
        String fullyQualifiedClassName = String.format("org.openstack.android.summit.common.entities.%s", className);
        Class type = null;

        if (className.equals("PresentationCategory")) {
            type = Track.class;
        }

        if (className.equals("PresentationCategoryGroup")) {
            type = TrackGroup.class;
        }

        if (className.equals("SummitLocationMap")) {
            type = Image.class;
        }

        if (className.equals("SummitVenueFloor")) {
            type = VenueFloor.class;
        }

        if (className.equals("SummitLocationImage")) {
            type = Image.class;
        }

        if (className.equals("MySchedule") || className.equals("Presentation")) {
            type = SummitEvent.class;
        }

        if (type == null) {
            try {
                type = Class.forName(fullyQualifiedClassName);
            } catch (ClassNotFoundException e) {
                Log.d(Constants.LOG_TAG, String.format("Class with name %s not found", fullyQualifiedClassName));
            }
        }

        if (type == null) {
            try {
                fullyQualifiedClassName = String.format("org.openstack.android.summit.common.entities.%s", className.replace("Summit", ""));
                type = Class.forName(fullyQualifiedClassName);
            } catch (ClassNotFoundException e) {
                Log.d(Constants.LOG_TAG, String.format("Class with name %s not found", fullyQualifiedClassName));
            }
        }

        if (type == null) {
            fullyQualifiedClassName = String.format("%s.Summit%s", this.getClass().getPackage().getName(), className);
            type = Class.forName(fullyQualifiedClassName);
        }

        return type;
    }
}
