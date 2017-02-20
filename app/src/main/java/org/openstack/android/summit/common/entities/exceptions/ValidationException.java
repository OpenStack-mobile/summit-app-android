package org.openstack.android.summit.common.entities.exceptions;

/**
 * Created by smarcet on 2/20/17.
 */

public class ValidationException extends Exception  {

    public ValidationException() {
    }

    public ValidationException(String detailMessage) {
        super(detailMessage);
    }
}
