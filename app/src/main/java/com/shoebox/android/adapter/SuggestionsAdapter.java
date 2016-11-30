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
	private static final String MALE = "male";
	private static final String FEMALE = "female";

	private static final int ITEM_HEADER_IMAGE = 0;
	private static final int ITEM_HEADER_TEXT = 1;
	private static final int ITEM_SUGGESTION = 2;

	private List<Suggestion> suggestions = new ArrayList<>();
	private boolean isMale;
	private int minAge;
	private int maxAge;

	public SuggestionsAdapter(boolean isMale, int minAge, int maxAge) {
		this.isMale = isMale;
		this.minAge = minAge;
		this.maxAge = maxAge;
	}

//	public void setSuggestions(List<Suggestion> list) {
//		this.suggestions.clear();
//		for (Suggestion suggestion : list) {
//			if (suggestion == null) continue;
//	        if (isInvalidSuggestion(suggestion)) continue;
//			this.suggestions.add(suggestion);
//		}
//
//		notifyDataSetChanged();
//	}

	public void addSuggestion(Suggestion suggestion) {
		if (suggestion == null) return;
		if (isInvalidSuggestion(suggestion)) return;

		suggestions.add(suggestion);
		// notifyItemInserted(suggestions.size() - 1);  --- we don't need to scroll to the item inserted
		notifyDataSetChanged();
	}

	public void removeSuggestion(Suggestion suggestion) {
		if (suggestion == null) return;
		int index = findDocumentIndexByKey(suggestion.key);
		notifyItemRemoved(index);
		suggestions.remove(index);
	}

	public void changeSuggestion(Suggestion suggestion) {
		if (suggestion == null) return;
		int index = findDocumentIndexByKey(suggestion.key);
		suggestions.set(index, suggestion);
		notifyItemChanged(index);
	}

	public boolean hasData() {
		return suggestions != null && suggestions.size() > 0;
	}

	private boolean isInvalidSuggestion(Suggestion suggestion) {
		// picked up 8-10 and interval is 11-100
		if (maxAge < suggestion.minAge) return true;

		//TODO
		// picked up 1, and interval is 0-1 (de ex suzeta, care-i pana pe la 1 an)

		if (isMale && suggestion.sex.equals(FEMALE)) return true;
		if (!isMale && suggestion.sex.equals(MALE)) return true;

		return false;
	}

	private int findDocumentIndexByKey(String key) {
		for (int i = 0; i < suggestions.size(); i++) {
			if (suggestions.get(i).key.equals(key)) return i;
		}
		return -1;
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
			suggestionTitle.setText(context.getResources().getQuantityString(R.plurals.header_suggestions, maxAge, age, sex));
		}
	}

	public static class DefaultViewHolder extends RecyclerView.ViewHolder {

		public DefaultViewHolder(View itemView) {
			super(itemView);
		}
	}
}
