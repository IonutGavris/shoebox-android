package com.shoebox.android.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckedTextView;

import com.shoebox.android.R;
import com.shoebox.android.beans.AgeInterval;
import com.shoebox.android.events.AgeSelectedEvent;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import de.greenrobot.event.EventBus;

public class AgePickerAdapter extends RecyclerView.Adapter<AgePickerAdapter.AgeHolder> {
	private List<AgeInterval> ageIntevals = new ArrayList<>();

	public void setAgeIntevals(List<AgeInterval> ageIntevals) {
		this.ageIntevals.clear();
		this.ageIntevals.addAll(ageIntevals);
	}

	@Override
	public AgeHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.age_item, parent, false);
		return new AgeHolder(v);
	}

	@Override
	public void onBindViewHolder(AgeHolder holder, int position) {
		holder.setData(ageIntevals.get(position));
	}

	@Override
	public int getItemCount() {
		return ageIntevals.size();
	}

	public static class AgeHolder extends RecyclerView.ViewHolder {
		@InjectView(R.id.ageInterval)
		CheckedTextView txtAgeInterval;

		Context context;

		private AgeInterval ageInterval;

		public AgeHolder(View itemView) {
			super(itemView);
			context = itemView.getContext();
			ButterKnife.inject(this, itemView);
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
					context.getString(R.string.ageOther) :
					String.format(context.getString(R.string.agePreset), interval.minAge, interval.maxAge));
		}

		public void setChecked(AgeInterval interval) {
			txtAgeInterval.setChecked(ageInterval.equals(interval));
		}
	}
}
