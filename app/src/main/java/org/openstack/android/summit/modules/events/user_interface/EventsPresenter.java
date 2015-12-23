package org.openstack.android.summit.modules.events.user_interface;

import android.os.Bundle;

import org.openstack.android.summit.modules.events.IEventsWireframe;

import javax.inject.Inject;

/**
 * Created by claudio on 10/30/2015.
 */
public class EventsPresenter implements IEventsPresenter {

    private EventsFragment view;
    private IEventsWireframe wireframe;

    @Inject
    public EventsPresenter(IEventsWireframe wireframe) {
        this.wireframe = wireframe;
    }

    public void setView(EventsFragment view) {
        this.view = view;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

    }

    @Override
    public void resume() {
    }

    @Override
    public void pause() {

    }

    @Override
    public void destroy() {

    }
}
