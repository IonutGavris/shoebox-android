package com.shoebox.android.injection.module;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Provider;

import dagger.android.AndroidInjector;
import dagger.android.DispatchingAndroidInjector;

import static dagger.internal.Preconditions.checkNotNull;

/**
 * Copy of {@link DispatchingAndroidInjector} with modified {@link DispatchingAndroidInjector#maybeInject} method in order
 * to get the injector from upper class when the subclass doesn't inject anything.
 */
public class ShoeBoxAndroidInjector<T> implements AndroidInjector<T> {

	private static final String NO_SUPERTYPES_BOUND_FORMAT = "No injector factory bound for Class<%s>";
	private static final String SUPERTYPES_BOUND_FORMAT =
			"No injector factory bound for Class<%1$s>. Injector factories were bound for supertypes "
					+ "of %1$s: %2$s. Did you mean to bind an injector factory for the subtype?";


	private final Map<Class<? extends T>, Provider<Factory<? extends T>>> injectorFactories;

	@Inject
	public ShoeBoxAndroidInjector(Map<Class<? extends T>, Provider<Factory<? extends T>>> injectorFactories) {
		this.injectorFactories = injectorFactories;
	}

	/**
	 * Attempts to perform members-injection on {@code instance}, returning {@code true} if
	 * successful, {@code false} otherwise.
	 *
	 * @throws DispatchingAndroidInjector.InvalidInjectorBindingException if the injector factory bound for a class does not
	 *                                                                    inject instances of that class
	 */
	public boolean maybeInject(T instance) {
		Class instanceClass = instance.getClass();
		Provider<Factory<? extends T>> factoryProvider = injectorFactories.get(instanceClass);

		while (factoryProvider == null) {
			instanceClass = instanceClass.getSuperclass();
			if (instanceClass != null) {
				factoryProvider = injectorFactories.get(instanceClass);
			} else {
				break;
			}
		}

		if (factoryProvider == null) {
			return false;
		}

		@SuppressWarnings("unchecked")
		dagger.android.AndroidInjector.Factory<T> factory = (dagger.android.AndroidInjector.Factory<T>) factoryProvider.get();
		try {
			dagger.android.AndroidInjector<T> injector = checkNotNull(
					factory.create(instance),
					"%s.create(I) should not return null.",
					factory.getClass().getCanonicalName());

			injector.inject(instance);
			return true;
		} catch (ClassCastException e) {
			throw new InvalidInjectorBindingException(String.format("%s does not implement CarfaxAndroidInjector.Factory<%s>",
					factory.getClass().getCanonicalName(), instance.getClass().getCanonicalName()), e);
		}
	}

	/**
	 * Performs members-injection on {@code instance}.
	 *
	 * @throws InvalidInjectorBindingException if the injector factory bound for a class does not
	 *                                         inject instances of that class
	 * @throws IllegalArgumentException        if no {@link dagger.android.AndroidInjector.Factory} is bound for {@code
	 *                                         instance}
	 */
	@Override
	public void inject(T instance) {
		boolean wasInjected = maybeInject(instance);
		if (!wasInjected) {
			throw new IllegalArgumentException(errorMessageSuggestions(instance));
		}
	}

	private String errorMessageSuggestions(T instance) {
		List<String> suggestions = new ArrayList<>();
		for (Class<? extends T> activityClass : injectorFactories.keySet()) {
			if (activityClass.isInstance(instance)) {
				suggestions.add(activityClass.getCanonicalName());
			}
		}
		Collections.sort(suggestions);

		return String.format(
				suggestions.isEmpty() ? NO_SUPERTYPES_BOUND_FORMAT : SUPERTYPES_BOUND_FORMAT,
				instance.getClass().getCanonicalName(),
				suggestions);
	}

	public static final class InvalidInjectorBindingException extends RuntimeException {
		private static final long serialVersionUID = -371319470664538191L;

		InvalidInjectorBindingException(String message, ClassCastException cause) {
			super(message, cause);
		}
	}
}
