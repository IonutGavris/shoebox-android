package com.shoebox.android.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.shoebox.android.R;
import com.shoebox.android.bean.Suggestion;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SuggestionsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
	private static final int ITEM_HEADER_IMAGE = 0;
	private static final int ITEM_HEADER_TEXT = 1;
	private static final int ITEM_SUGGESTION = 2;
	private List<Suggestion> suggestions = new ArrayList<>();
	private boolean isMale;
	private int minAge;
	private int maxAge;


	public void setSuggestions(List<Suggestion> list) {
		this.suggestions.clear();
		for (Suggestion s : list) {
			if (s != null) {
				this.suggestions.add(s);
			}
		}

		notifyDataSetChanged();
	}

	public void setSuggestionsTarget(boolean isMale, int minAge, int maxAge) {
		this.isMale = isMale;
		this.minAge = minAge;
		this.maxAge = maxAge;
	}

	@Override
	public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		LayoutInflater inflater = LayoutInflater.from(parent.getContext());
		switch (viewType) {
			case ITEM_HEADER_IMAGE:
				return new DefaultViewHolder(inflater.inflate(R.layout.suggestion_header_image, parent, false));
			case ITEM_HEADER_TEXT:
				return new SuggestionHeaderHolder(inflater.inflate(R.layout.suggestion_header_text, parent, false));
			case ITEM_SUGGESTION:
				return new SuggestionItemHolder(inflater.inflate(R.layout.suggestion_item, parent, false));
		}
		return null;
	}

	@Override
	public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
		switch (getItemViewType(position)) {
			case ITEM_HEADER_TEXT:
				((SuggestionHeaderHolder) holder).setData(isMale, minAge, maxAge);
				break;
			case ITEM_SUGGESTION:
				((SuggestionItemHolder) holder).setData(suggestions.get(position - 2));
				break;
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

	public static class SuggestionItemHolder extends RecyclerView.ViewHolder {

		@BindView(R.id.suggestionTitle)
		TextView suggestionTitle;

		@BindView(R.id.suggestionDescription)
		TextView suggestionDescription;

		public SuggestionItemHolder(View itemView) {
			super(itemView);
			ButterKnife.bind(this, itemView);
		}

		public void setData(Suggestion suggestion) {
			suggestionTitle.setText(suggestion.name);
			suggestionDescription.setText(suggestion.description);
		}
	}

	public static class SuggestionHeaderHolder extends RecyclerView.ViewHolder {

		@BindView(R.id.suggestionTitle)
		TextView suggestionTitle;
		Context context;

		public SuggestionHeaderHolder(View itemView) {
			super(itemView);
			context = itemView.getContext();
			ButterKnife.bind(this, itemView);
		}

		public void setData(boolean isMale, int minAge, int maxAge) {
			String sex = context.getString(isMale ? R.string.btn_boy : R.string.btn_girl);
			String age = minAge == maxAge ? String.valueOf(maxAge) : minAge + "-" + maxAge;
			suggestionTitle.setText(String.format(context.getString(R.string.header_suggestions), age, sex));
		}
	}

	public static class DefaultViewHolder extends RecyclerView.ViewHolder {

		public DefaultViewHolder(View itemView) {
			super(itemView);
		}
	}
}
