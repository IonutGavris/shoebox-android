package com.shoebox.android.ui;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.View;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * All dialog fragments should extend from this one
 */
abstract public class BaseDialogFragment extends DialogFragment {

	@Nullable
	private Unbinder unbinder;

	@Override
	public void onDestroyView() {
		if (unbinder != null) {
			unbinder.unbind();
		}
		super.onDestroyView();
	}

	protected void bindViews(@NonNull Object target, @NonNull View source) {
		unbinder = ButterKnife.bind(target, source);
	}

}
