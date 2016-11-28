package com.shoebox.android.ui;

import android.app.Activity;
import android.support.v4.app.DialogFragment;
import android.view.View;

import com.shoebox.android.BaseActivity;

import butterknife.Unbinder;

/**
 * All dialog fragments should extend from this one
 */
abstract public class BaseDialogFragment extends DialogFragment {

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
