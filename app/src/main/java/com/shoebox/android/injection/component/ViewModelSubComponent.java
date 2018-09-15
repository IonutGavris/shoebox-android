package com.shoebox.android.injection.component;

import dagger.Subcomponent;

@Subcomponent
public interface ViewModelSubComponent {

	@Subcomponent.Builder
	interface Builder {
		ViewModelSubComponent build();
	}

}
