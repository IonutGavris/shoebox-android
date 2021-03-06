package com.shoebox.android.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckedTextView;

import com.shoebox.android.R;
import com.shoebox.android.bean.AgeInterval;
import com.shoebox.android.event.AgeSelectedEvent;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AgePickerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
	private static final int LIST_HEADER = 0;
	private static final int LIST_ITEM = 1;
	private List<AgeInterval> ageIntervals = new ArrayList<>();

	public void setAgeIntervals(List<AgeInterval> ageIntervals) {
		this.ageIntervals.clear();
		this.ageIntervals.addAll(ageIntervals);
		notifyDataSetChanged();
	}

	@Override
	public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		switch (viewType) {
			case LIST_HEADER:
				return new DefaultViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.age_header, parent, false));
			case LIST_ITEM:
				return new AgeHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.age_item, parent, false));
		}
		return null;
	}

	@Override
	public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
		if (getItemViewType(position) == LIST_ITEM) {
			((AgeHolder) holder).setData(ageIntervals.get(position - 1));
		}
	}

	@Override
	public int getItemCount() {
		return ageIntervals.size() + 1;
	}

	@Override
	public int getItemViewType(int position) {
		switch (position) {
			case 0:
				return LIST_HEADER;
			default:
				return LIST_ITEM;
		}
	}

	public static class AgeHolder extends RecyclerView.ViewHolder {
		@BindView(R.id.ageInterval)
		CheckedTextView txtAgeInterval;

		Context context;

		private AgeInterval ageInterval;

		public AgeHolder(View itemView) {
			super(itemView);
			context = itemView.getContext();
			ButterKnife.bind(this, itemView);
			itemView.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					EventBus.getDefault().post(new AgeSelectedEvent(ageInterval));
				}
			});
		}

		public void setData(AgeInterval interval) {
			this.ageInterval = interval;
			txtAgeInterval.setText(interval.custom ?
					(interval.maxAge <= 0 ? context.getString(R.string.age_other_undefined) :
							context.getResources().getQuantityString(R.plurals.age_other_defined, interval.maxAge, interval.maxAge)) :
					context.getString(R.string.age_preset, interval.minAge, interval.maxAge));
		}

		public boolean setChecked(AgeInterval interval) {
			boolean checked = ageInterval.equals(interval);
			txtAgeInterval.setChecked(checked);
			return checked;
		}
	}

	public static class DefaultViewHolder extends RecyclerView.ViewHolder {
		public DefaultViewHolder(View itemView) {
			super(itemView);
		}
	}
}
