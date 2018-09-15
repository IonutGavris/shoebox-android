package com.shoebox.android.injection.scope;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import javax.inject.Scope;

/**
 * Application scope for dependencies that are singleton per application.
 */
@Scope
@Retention(RetentionPolicy.RUNTIME)
public @interface ApplicationScope {
}
