package com.shoebox.android.uimodel;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.shoebox.android.bean.Suggestion;

import java.util.List;
import java.util.Objects;

public class ContentSuggestionUiModel {

	private final List<Suggestion> suggestions;
	private final String error;
	private final boolean showProgress;

	public ContentSuggestionUiModel(@NonNull List<Suggestion> suggestions, @Nullable String error, boolean showProgress) {
		this.suggestions = suggestions;
		this.error = error;
		this.showProgress = showProgress;
	}

	@NonNull
	public List<Suggestion> getSuggestions() {
		return suggestions;
	}

	@Nullable
	public String getError() {
		return error;
	}

	public boolean showProgress() {
		return showProgress;
	}

	public boolean containsError() {
		return !TextUtils.isEmpty(error);
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		ContentSuggestionUiModel that = (ContentSuggestionUiModel) o;
		return showProgress == that.showProgress &&
				Objects.equals(suggestions, that.suggestions) &&
				Objects.equals(error, that.error);
	}

	@Override
	public int hashCode() {
		return Objects.hash(suggestions, error, showProgress);
	}

	@Override
	public String toString() {
		return "ContentSuggestionUiModel{" +
				"suggestions=" + suggestions +
				", error='" + error + '\'' +
				", showProgress=" + showProgress +
				'}';
	}
}
