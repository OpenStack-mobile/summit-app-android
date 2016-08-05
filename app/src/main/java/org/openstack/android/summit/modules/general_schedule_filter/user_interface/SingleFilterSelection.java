package org.openstack.android.summit.modules.general_schedule_filter.user_interface;

/**
 * Created by sebastian on 8/4/2016.
 */
public class SingleFilterSelection extends AbstractFilterSection {

    private Object value;

    SingleFilterSelection(Object value){
        this.value = value;
    }

    public Object getValue(){
        return value;
    }

    public void setValue(Object value){
        this.value = value;
    }
}
