package org.openstack.android.summit.common.utils;

import java.util.NoSuchElementException;

import io.reactivex.annotations.Nullable;

/**
 * Created by smarcet on 2/19/18.
 */

public class Optional<M> {

    private final M optional;

    public Optional(@Nullable M optional) {
        this.optional = optional;
    }

    public boolean isEmpty() {
        return this.optional == null;
    }

    public M get() {
        if (optional == null) {
            throw new NoSuchElementException("No value present");
        }
        return optional;
    }
}
