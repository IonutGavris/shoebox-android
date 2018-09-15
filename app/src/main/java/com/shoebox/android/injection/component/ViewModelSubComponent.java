package com.shoebox.android.injection.component;

import com.shoebox.android.viewmodel.PermissionsViewModel;

import dagger.Subcomponent;

@Subcomponent
public interface ViewModelSubComponent {

	PermissionsViewModel getPermissionsViewModel();

	@Subcomponent.Builder
	interface Builder {
		ViewModelSubComponent build();
	}

}
