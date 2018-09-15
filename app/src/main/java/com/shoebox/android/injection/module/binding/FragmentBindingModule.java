package com.shoebox.android.injection.module.binding;

import com.shoebox.android.fragment.BaseFragment;
import com.shoebox.android.fragment.LocationsMapFragment;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

/**
 * All fragments that inject a class should be declared here.
 */
@Module
public abstract class FragmentBindingModule {

	@ContributesAndroidInjector
	abstract BaseFragment contributesBaseFragment();

	@ContributesAndroidInjector
	abstract LocationsMapFragment contributesLocationsMapFragment();

}
