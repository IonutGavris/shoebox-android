package com.shoebox.android.fragment;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.view.View;

import com.shoebox.android.BaseActivity;

import butterknife.Unbinder;

/**
 * All fragments should extend from this one
 */
abstract public class BaseFragment extends Fragment {

	protected Unbinder unbinder;

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
}
