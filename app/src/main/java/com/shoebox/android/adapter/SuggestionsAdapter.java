package com.shoebox.android.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.shoebox.android.R;
import com.shoebox.android.beans.Suggestion;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class SuggestionsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
	private static final int ITEM_HEADER_IMAGE = 0;
	private static final int ITEM_HEADER_TEXT = 1;
	private static final int ITEM_SUGGESTION = 2;
	private List<Suggestion> suggestions = new ArrayList<>();


	public void setSuggestions(List<Suggestion> offers) {
		this.suggestions.clear();
		this.suggestions.addAll(offers);
		notifyDataSetChanged();
	}

	@Override
	public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		LayoutInflater inflater = LayoutInflater.from(parent.getContext());
		switch (viewType) {
			case ITEM_HEADER_IMAGE:
				return new DefaultViewHolder(inflater.inflate(R.layout.suggestion_header_image, parent, false));
			case ITEM_HEADER_TEXT:
				return new DefaultViewHolder(inflater.inflate(R.layout.suggestion_header_text, parent, false));
			case ITEM_SUGGESTION:
				return new SuggestionHolder(inflater.inflate(R.layout.suggestion_item, parent, false));
		}
		return null;
	}

	@Override
	public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
		if (getItemViewType(position) == ITEM_SUGGESTION) {
			((SuggestionHolder) holder).setData(suggestions.get(position - 2));
		}
	}

	@Override
	public int getItemCount() {
		// add the first two items
		return suggestions.size() + 2;
	}

	@Override
	public int getItemViewType(int position) {
		switch (position) {
			case 0:
				return ITEM_HEADER_IMAGE;
			case 1:
				return ITEM_HEADER_TEXT;
			default:
				return ITEM_SUGGESTION;
		}
	}

	public static class SuggestionHolder extends RecyclerView.ViewHolder {

		@InjectView(R.id.suggestionTitle)
		TextView suggestionTitle;

		public SuggestionHolder(View itemView) {
			super(itemView);
			ButterKnife.inject(this, itemView);
		}

		public void setData(Suggestion suggestion) {
			suggestionTitle.setText(suggestion.name);
		}
	}

	public static class DefaultViewHolder extends RecyclerView.ViewHolder {

		public DefaultViewHolder(View itemView) {
			super(itemView);
		}
	}
}
