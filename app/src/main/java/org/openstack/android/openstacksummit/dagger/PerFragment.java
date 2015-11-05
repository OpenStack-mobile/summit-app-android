package org.openstack.android.openstacksummit.dagger;

import java.lang.annotation.Retention;

import javax.inject.Scope;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Created by Claudio Redi on 11/3/2015.
 */
@Scope
@Retention(RUNTIME)
public @interface PerFragment {}
