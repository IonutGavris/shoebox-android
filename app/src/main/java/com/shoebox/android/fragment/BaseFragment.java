package com.shoebox.android.fragment;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.view.View;

import com.shoebox.android.BaseActivity;

import butterknife.ButterKnife;

/**
 * All fragment should extend from this one
 */
abstract public class BaseFragment extends Fragment {

	@Override
	public void onDestroy() {
		ButterKnife.reset(this);
		super.onDestroy();
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
