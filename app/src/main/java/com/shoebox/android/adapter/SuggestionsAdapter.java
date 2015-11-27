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

public class SuggestionsAdapter extends RecyclerView.Adapter<SuggestionsAdapter.SuggestionHolder> {
	private List<Suggestion> suggestions = new ArrayList<>();

	public void setSuggestions(List<Suggestion> offers) {
		this.suggestions.clear();
		this.suggestions.addAll(offers);
		notifyDataSetChanged();
	}

	@Override
	public SuggestionHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.suggestion_item, parent, false);
		return new SuggestionHolder(v);
	}

	@Override
	public void onBindViewHolder(SuggestionHolder holder, int position) {
		holder.setData(suggestions.get(position));
	}

	@Override
	public int getItemCount() {
		return suggestions.size();
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
}
