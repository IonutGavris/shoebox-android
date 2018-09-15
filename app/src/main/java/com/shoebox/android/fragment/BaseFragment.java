package com.shoebox.android.fragment;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.View;

import com.shoebox.android.BaseActivity;
import com.shoebox.android.injection.module.ShoeBoxAndroidInjector;

import javax.inject.Inject;

import butterknife.Unbinder;
import dagger.android.AndroidInjector;
import dagger.android.support.AndroidSupportInjection;
import dagger.android.support.HasSupportFragmentInjector;

/**
 * All fragments should extend from this one
 */
abstract public class BaseFragment extends Fragment implements HasSupportFragmentInjector {

	@Inject
	protected ShoeBoxAndroidInjector<Fragment> childFragmentInjector;

	@Nullable
	protected Unbinder unbinder;

	@Override
	public void onAttach(Context context) {
		AndroidSupportInjection.inject(this);
		super.onAttach(context);
	}

	@Override
	public void onDestroyView() {
		if (unbinder != null) {
			unbinder.unbind();
		}
		super.onDestroyView();
	}

	/**
	 * Gets the base view of this fragment.
	 *
	 * @return Returns a CoordinatorLayout or the base content view.
	 */
	protected View getBaseView() {
		Activity activity = getActivity();
		return activity instanceof BaseActivity ?
				((BaseActivity) activity).coordinatorLayout : activity.getWindow().getDecorView().findViewById(android.R.id.content);
	}

	@Override
	public AndroidInjector<Fragment> supportFragmentInjector() {
		return childFragmentInjector;
	}
}
