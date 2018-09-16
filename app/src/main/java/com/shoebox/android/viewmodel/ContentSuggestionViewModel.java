package com.shoebox.android.viewmodel;


import android.arch.lifecycle.ViewModel;

import com.shoebox.android.bean.AgeInterval;
import com.shoebox.android.bean.Suggestion;
import com.shoebox.android.repository.ContentSuggestionsRepository;
import com.shoebox.android.uimodel.ContentSuggestionUiModel;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;
import timber.log.Timber;

public class ContentSuggestionViewModel extends ViewModel {

	private ContentSuggestionsRepository suggestionsRepository;
	private boolean isMale;
	private AgeInterval ageInterval;

	private Observable<ContentSuggestionUiModel> uiModelObservable;

	@Inject
	public ContentSuggestionViewModel(ContentSuggestionsRepository suggestionsRepository) {
		this.suggestionsRepository = suggestionsRepository;
	}

	public void init(boolean isMale, AgeInterval ageInterval) {
		if (this.ageInterval != null) { // model was already initialised
			return;
		}
		this.isMale = isMale;
		this.ageInterval = ageInterval;
		Timber.d("init: isMale=%b & ageInterval=%s", isMale, ageInterval);

		uiModelObservable = suggestionsRepository.getDataStream()
				.map(repoData -> {
					List<Suggestion> suggestions = getValidSuggestions((List<Suggestion>) repoData.data);
					Timber.d("combineLatest() params: \n\t suggestions: %s \n\t error: %s ", suggestions, repoData.error);
					return new ContentSuggestionUiModel(suggestions, repoData.error, repoData.data == null);
				})
				.replay(1)
				.refCount();
	}

	/**
	 * Unsubscribes from the streams of data that are composing the UiModel
	 */
	@Override
	protected void onCleared() {
		super.onCleared();
		Timber.d("onCleared: Destroying viewModel");
		suggestionsRepository.stopDataStream();
	}

	/**
	 * Use this method to get the stream of ui model changes.
	 *
	 * @return The ui model observable
	 */
	public Observable<ContentSuggestionUiModel> getUiModel() {
		return uiModelObservable.distinctUntilChanged()
				.doOnNext(uiModel -> Timber.d("Emitting uiModel %s", uiModel));
	}

	private List<Suggestion> getValidSuggestions(List<Suggestion> suggestions) {
		List<Suggestion> validSuggestions = new ArrayList<>();
		if (suggestions != null && suggestions.size() > 0) {
			for (Suggestion suggestion : suggestions) {
				if (suggestion.isValid(isMale, ageInterval.minAge, ageInterval.maxAge)) {
					validSuggestions.add(suggestion);
				}
			}
		}
		return validSuggestions;
	}

}
