package com.shoebox.android.injection.module;

import android.content.Context;

import com.shoebox.android.injection.scope.ApplicationScope;
import com.shoebox.android.repository.ContentSuggestionsRepository;

import dagger.Module;
import dagger.Provides;

@Module
public class RepositoryModule {

	@Provides
	@ApplicationScope
	ContentSuggestionsRepository provideContentSuggestionsRepository(Context context) {
		return new ContentSuggestionsRepository(context);
	}

}
