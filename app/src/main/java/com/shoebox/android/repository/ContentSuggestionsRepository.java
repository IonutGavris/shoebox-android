package com.shoebox.android.repository;

import android.content.Context;
import android.support.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.Query;
import com.shoebox.android.bean.Suggestion;
import com.shoebox.android.util.UIUtils;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import timber.log.Timber;

public class ContentSuggestionsRepository extends BaseRepository {

	private static final String dataPath_ro = "suggestions";
	private static final String dataPath_en = "suggestions_en";

	private Query dataQuery;

	@Inject
	public ContentSuggestionsRepository(Context context) {
		super(context);
		String path = UIUtils.useRomanianLanguage(context) ? dataPath_ro : dataPath_en;
		// TODO define the .indexOn rule to index those keys on the server and improve query performance
		dataQuery = firebaseDatabase.getReference().child(path).orderByChild(Suggestion.ORDER_BY);
	}

	@NonNull
	@Override
	public Query getDataQuery() {
		return dataQuery;
	}

	@NonNull
	@Override
	public Object getUIData(@NonNull DataSnapshot dataSnapshot) {
		GenericTypeIndicator<List<Suggestion>> t = new GenericTypeIndicator<List<Suggestion>>() {
		};
		List<Suggestion> data = dataSnapshot.getValue(t);
		if (data != null && data.size() > 1 && data.get(0) == null) {
			data.remove(0);
			Timber.d("onDataChange: count = %d", data.size());
		} else if (data == null) {
			data = new ArrayList<>(0);
		}
		return data;
	}

}
