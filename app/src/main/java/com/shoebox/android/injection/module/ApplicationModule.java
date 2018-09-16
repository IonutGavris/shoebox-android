package com.shoebox.android.injection.module;

import android.arch.lifecycle.ViewModelProvider;
import android.content.Context;

import com.shoebox.android.ShoeBoxApplication;
import com.shoebox.android.injection.component.ViewModelSubComponent;
import com.shoebox.android.injection.scope.ApplicationScope;
import com.shoebox.android.util.schedulers.BaseSchedulerProvider;
import com.shoebox.android.util.schedulers.SchedulerProvider;
import com.shoebox.android.viewmodel.ShoeBoxViewModelFactory;

import dagger.Module;
import dagger.Provides;

@Module(subcomponents = ViewModelSubComponent.class)
public class ApplicationModule {

	@Provides
	@ApplicationScope
	Context provideContext(ShoeBoxApplication application) {
		return application.getApplicationContext();
	}

	@Provides
	@ApplicationScope
	ViewModelProvider.Factory provideViewModelFactory(ViewModelSubComponent.Builder viewModelSubComponent) {
		return new ShoeBoxViewModelFactory(viewModelSubComponent.build());
	}

	@Provides
	@ApplicationScope
	public static BaseSchedulerProvider provideSchedulerProvider() {
		return SchedulerProvider.getInstance();
	}

}
