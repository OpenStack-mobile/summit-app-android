package org.openstack.android.summit.modules.level_list;

import org.openstack.android.summit.common.entities.IPresentation;
import org.openstack.android.summit.common.user_interface.IBaseView;
import org.openstack.android.summit.modules.level_schedule.ILevelScheduleWireframe;

/**
 * Created by Claudio Redi on 1/11/2016.
 */
public class LevelListWireframe implements ILevelListWireframe {

    private ILevelScheduleWireframe levelScheduleWireframe;

    public LevelListWireframe(ILevelScheduleWireframe levelScheduleWireframe) {
        this.levelScheduleWireframe = levelScheduleWireframe;
    }

    @Override
    public void showLevelSchedule(String level, IBaseView context) {

        if(level.toLowerCase().equals("na"))
            level = IPresentation.LevelNA;
        if(level.toLowerCase().equals("advanced"))
            level = IPresentation.LevelAdvanced;
        if(level.toLowerCase().equals("beginner"))
            level = IPresentation.LevelBeginner;
        if(level.toLowerCase().equals("intermediate"))
            level = IPresentation.LevelIntermediate;

        levelScheduleWireframe.presentLevelScheduleView(level, context);
    }
}
