package org.openstack.android.summit.common.user_interface;

/**
 * Created by Claudio Redi on 1/7/2016.
 */
public interface IBaseFragment {
    void showActivityIndicator(int delay);

    void showActivityIndicator();

    void hideActivityIndicator();

    void showErrorMessage(String message);
}
