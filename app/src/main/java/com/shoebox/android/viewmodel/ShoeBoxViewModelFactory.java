package com.shoebox.android.viewmodel;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;
import android.support.v4.util.ArrayMap;

import com.shoebox.android.injection.component.ViewModelSubComponent;
import com.shoebox.android.injection.scope.ApplicationScope;

import java.util.Map;
import java.util.concurrent.Callable;

import javax.inject.Inject;

@ApplicationScope
public class ShoeBoxViewModelFactory implements ViewModelProvider.Factory {

	private final ArrayMap<Class, Callable<? extends ViewModel>> creators;

	@Inject
	public ShoeBoxViewModelFactory(ViewModelSubComponent viewModelSubComponent) {
		creators = new ArrayMap<>();
		creators.put(PermissionsViewModel.class, viewModelSubComponent::getPermissionsViewModel);
		creators.put(ContentSuggestionViewModel.class, viewModelSubComponent::getContentSuggestionViewModel);
	}

	@NonNull
	@Override
	public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
		Callable<? extends ViewModel> creator = creators.get(modelClass);
		if (creator == null) {
			for (Map.Entry<Class, Callable<? extends ViewModel>> entry : creators.entrySet()) {
				if (modelClass.isAssignableFrom(entry.getKey())) {
					creator = entry.getValue();
					break;
				}
			}
		}
		if (creator == null) {
			throw new IllegalArgumentException("unknown model class " + modelClass);
		}
		try {
			return (T) creator.call();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}
