package com.shoebox.android.injection.module.binding;


import com.shoebox.android.BaseActivity;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

/**
 * All activities that inject a class should be declared here.
 */
@Module
public abstract class ActivityBindingModule {

	@ContributesAndroidInjector
	abstract BaseActivity contributesBaseActivity();

}