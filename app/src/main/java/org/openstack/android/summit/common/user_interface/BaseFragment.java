package org.openstack.android.summit.common.user_interface;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.widget.Toast;

import org.openstack.android.summit.MainActivity;
import org.openstack.android.summit.dagger.components.ApplicationComponent;

/**
 * Created by Claudio Redi on 11/3/2015.
 */
public abstract class BaseFragment extends Fragment {

    @Override public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    /**
     * Shows a {@link android.widget.Toast} message.
     *
     * @param message An string representing a message to be shown.
     */
    protected void showToastMessage(String message) {
        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
    }

    /**
     * Gets a component for dependency injection by its type.
     */
    @SuppressWarnings("unchecked")
    protected ApplicationComponent getComponent() {
        return ((MainActivity)getActivity()).getApplicationComponent();
    }
}