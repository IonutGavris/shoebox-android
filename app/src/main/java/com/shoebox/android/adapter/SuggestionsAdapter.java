package com.shoebox.android.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.shoebox.android.R;
import com.shoebox.android.bean.AgeInterval;
import com.shoebox.android.bean.Suggestion;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SuggestionsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

	private List<Pair<ItemType, Object>> listItems = new ArrayList<>();
	private boolean isMale;
	private AgeInterval ageInterval;

	public SuggestionsAdapter(boolean isMale, @NonNull AgeInterval ageInterval) {
		this.isMale = isMale;
		this.ageInterval = ageInterval;
	}

	public void setSuggestions(@NonNull List<Suggestion> list) {
		List<Pair<ItemType, Object>> newItems = new ArrayList<>();
		newItems.add(new Pair<>(ItemType.IMAGE_HEADER, null));
		newItems.add(new Pair<>(ItemType.TEXT_HEADER, null));
		for (Suggestion suggestion : list) {
			newItems.add(new Pair<>(ItemType.SUGGESTION, suggestion));
		}

		DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(new ItemsDiffCallback(listItems, newItems));
		this.listItems.clear();
		this.listItems.addAll(newItems);
		diffResult.dispatchUpdatesTo(this);
	}

	public boolean hasData() {
		return listItems != null && listItems.size() > 2; // ignore headers for data count
	}

	@NonNull
	@Override
	public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
		LayoutInflater inflater = LayoutInflater.from(parent.getContext());
		switch (ItemType.valueOf(viewType)) {
			case IMAGE_HEADER:
				return new ImageHeaderHolder(inflater.inflate(R.layout.suggestion_header_image, parent, false));
			case TEXT_HEADER:
				return new TextHeaderHolder(inflater.inflate(R.layout.suggestion_header_text, parent, false));
			case SUGGESTION:
				return new SuggestionItemHolder(inflater.inflate(R.layout.suggestion_item, parent, false));
		}
		return null;
	}

	@Override
	public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
		switch (ItemType.valueOf(getItemViewType(position))) {
			case TEXT_HEADER:
				((TextHeaderHolder) holder).setData(isMale, ageInterval);
				break;
			case SUGGESTION:
				((SuggestionItemHolder) holder).setData((Suggestion) listItems.get(position).second);
				break;
		}
	}

	@Override
	public int getItemCount() {
		return listItems.size();
	}

	@Override
	public int getItemViewType(int position) {
		return listItems.get(position).first.ordinal();
	}

	enum ItemType {
		IMAGE_HEADER, TEXT_HEADER, SUGGESTION;

		public static ItemType valueOf(int ordinal) {
			return ItemType.class.getEnumConstants()[ordinal];
		}
	}

	class ItemsDiffCallback extends DiffUtil.Callback {
		private List<Pair<ItemType, Object>> oldItems;
		private List<Pair<ItemType, Object>> newItems;

		ItemsDiffCallback(List<Pair<ItemType, Object>> oldItems, List<Pair<ItemType, Object>> newItems) {
			this.oldItems = oldItems;
			this.newItems = newItems;
		}

		@Override
		public int getOldListSize() {
			return oldItems.size();
		}

		@Override
		public int getNewListSize() {
			return newItems.size();
		}

		@Override
		public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
			Pair<ItemType, Object> oldItem = oldItems.get(oldItemPosition);
			Pair<ItemType, Object> newItem = newItems.get(newItemPosition);
			if (oldItem.first != newItem.first) {
				return false;
			} else if (oldItem.first == ItemType.SUGGESTION) {
				return ((Suggestion) oldItem.second).getKey().equals(((Suggestion) newItem.second).getKey());
			}
			return false;
		}

		@Override
		public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
			Pair<ItemType, Object> oldItem = oldItems.get(oldItemPosition);
			Pair<ItemType, Object> newItem = newItems.get(newItemPosition);

			return oldItem.first != ItemType.SUGGESTION || oldItem.second.equals(newItem.second);
		}
	}

	class SuggestionItemHolder extends RecyclerView.ViewHolder {

		@BindView(R.id.suggestionTitle)
		TextView suggestionTitle;

		@BindView(R.id.suggestionDescription)
		TextView suggestionDescription;

		SuggestionItemHolder(View itemView) {
			super(itemView);
			ButterKnife.bind(this, itemView);
		}

		public void setData(Suggestion suggestion) {
			suggestionTitle.setText(suggestion.getName());
			suggestionDescription.setText(suggestion.getDescription());
		}
	}

	class TextHeaderHolder extends RecyclerView.ViewHolder {

		@BindView(R.id.suggestionTitle)
		TextView suggestionTitle;

		TextHeaderHolder(View itemView) {
			super(itemView);
			ButterKnife.bind(this, itemView);
		}

		void setData(boolean isMale, AgeInterval ageInterval) {
			Context context = itemView.getContext();
			String sex = context.getString(isMale ? R.string.btn_boy : R.string.btn_girl);
			String age = ageInterval.getAgeInterval();
			suggestionTitle.setText(context.getResources().getQuantityString(R.plurals.header_suggestions, ageInterval.maxAge, age, sex));
		}
	}

	class ImageHeaderHolder extends RecyclerView.ViewHolder {
		ImageHeaderHolder(View itemView) {
			super(itemView);
		}
	}


}


