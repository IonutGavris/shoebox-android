package com.shoebox.android.injection.component;

import com.shoebox.android.ShoeBoxApplication;
import com.shoebox.android.injection.module.ApplicationModule;
import com.shoebox.android.injection.module.FirebaseModule;
import com.shoebox.android.injection.module.RepositoryModule;
import com.shoebox.android.injection.module.binding.ActivityBindingModule;
import com.shoebox.android.injection.module.binding.FragmentBindingModule;
import com.shoebox.android.injection.scope.ApplicationScope;
import com.shoebox.android.repository.BaseRepository;

import dagger.BindsInstance;
import dagger.Component;
import dagger.android.AndroidInjectionModule;
import dagger.android.AndroidInjector;
import dagger.android.DaggerApplication;
import dagger.android.support.AndroidSupportInjectionModule;

@ApplicationScope
@Component(modules = {
		AndroidInjectionModule.class,
		AndroidSupportInjectionModule.class,
		ApplicationModule.class,
		FirebaseModule.class,
		RepositoryModule.class,
		ActivityBindingModule.class,
		FragmentBindingModule.class
})
public interface ApplicationComponent extends AndroidInjector<DaggerApplication> {

	void inject(ShoeBoxApplication application);

	void inject(BaseRepository baseRepository);

	@Component.Builder
	interface Builder {

		@BindsInstance
		Builder application(ShoeBoxApplication application);

		ApplicationComponent build();
	}
}
