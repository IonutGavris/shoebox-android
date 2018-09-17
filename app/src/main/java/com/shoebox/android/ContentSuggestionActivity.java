package com.shoebox.android;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.shoebox.android.adapter.SuggestionsAdapter;
import com.shoebox.android.bean.AgeInterval;
import com.shoebox.android.uimodel.ContentSuggestionUiModel;
import com.shoebox.android.util.DividerItemDecoration;
import com.shoebox.android.util.ShoeBoxAnalytics;
import com.shoebox.android.util.UIUtils;
import com.shoebox.android.viewmodel.ContentSuggestionViewModel;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import timber.log.Timber;

public class ContentSuggestionActivity extends BaseActivity {
	private static final String EXTRA_IS_MALE = "is_male";
	private static final String EXTRA_AGE_INTERVAL = "age_interval";

	@BindView(R.id.recyclerView)
	RecyclerView recyclerView;

	@BindView(R.id.listStatusView)
	View listStatusView;

	private SuggestionsAdapter adapter;
	private ContentSuggestionViewModel viewModel;
	private Disposable uiModelDisposable;

	public static Intent getLaunchingIntent(@NonNull Context context, boolean isMale, @NonNull AgeInterval interval) {
		Intent intent = new Intent(context, ContentSuggestionActivity.class);
		intent.putExtra(EXTRA_IS_MALE, isMale);
		intent.putExtra(EXTRA_AGE_INTERVAL, interval);
		return intent;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_content_suggestion);

		AgeInterval ageInterval = (AgeInterval) getIntent().getSerializableExtra(EXTRA_AGE_INTERVAL);
		boolean isMale = getIntent().getBooleanExtra(EXTRA_IS_MALE, false);
		adapter = new SuggestionsAdapter(isMale, ageInterval);

		recyclerView.setHasFixedSize(true);
		LinearLayoutManager layoutManager = new LinearLayoutManager(this);
		layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
		recyclerView.setLayoutManager(layoutManager);
		recyclerView.setItemAnimator(new DefaultItemAnimator());
		recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL_LIST));
		recyclerView.setAdapter(adapter);

		viewModel = ViewModelProviders.of(this, viewModelFactory).get(ContentSuggestionViewModel.class);
		viewModel.init(isMale, ageInterval);
		uiModelDisposable = viewModel.getUiModel()
				.doOnNext(model -> Timber.d("renderUiModel: %s", model.toString()))
				.observeOn(AndroidSchedulers.mainThread())
				.subscribe(this::renderUiModel, Timber::e);

		Bundle bundle = new Bundle(2);
		bundle.putSerializable(ShoeBoxAnalytics.Param.AGE_INTERVAL, ageInterval);
		bundle.putBoolean(ShoeBoxAnalytics.Param.IS_MALE, isMale);
		firebaseAnalytics.logEvent(ShoeBoxAnalytics.State.CONTENT_SUGGESTIONS, bundle);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (uiModelDisposable != null && !uiModelDisposable.isDisposed()) {
			uiModelDisposable.dispose();
		}
	}

	@OnClick(R.id.nextStep)
	public void nextStepClick(View view) {
		Timber.d("nextStepClick");
		firebaseAnalytics.logEvent(ShoeBoxAnalytics.Action.GOTO_LOCATIONS, null);
		startActivity(LocationsActivity.getLaunchingIntent(this));
	}

	private void renderUiModel(ContentSuggestionUiModel model) {
		adapter.setSuggestions(model.getSuggestions());

		if (adapter.hasData()) {
			listStatusView.setVisibility(View.GONE);
			if (model.containsError()) {
				UIUtils.showMessage(this, model.getError());
			}
		} else {
			UIUtils.setListStatus(listStatusView, getString(model.showProgress() ? R.string.msg_loading :
					model.containsError() ? R.string.msg_fetch_suggestions : R.string.msg_no_results), model.containsError());
			listStatusView.setVisibility(View.VISIBLE);
		}
	}
}
