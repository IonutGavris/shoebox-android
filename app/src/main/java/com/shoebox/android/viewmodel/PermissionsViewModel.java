package com.shoebox.android.viewmodel;

import android.app.Activity;
import android.arch.lifecycle.ViewModel;

import com.shoebox.android.util.permission.PermissionRequest;
import com.shoebox.android.util.permission.PermissionResult;
import com.shoebox.android.util.schedulers.BaseSchedulerProvider;
import com.shoebox.android.util.schedulers.SchedulerProvider;
import com.tbruyelle.rxpermissions2.RxPermissions;

import java.lang.ref.WeakReference;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.subjects.PublishSubject;
import timber.log.Timber;

public class PermissionsViewModel extends ViewModel {

	private final BaseSchedulerProvider baseSchedulerProvider;
	private boolean initialised;
	private WeakReference<Activity> activityRef;
	private RxPermissions rxPermissions;
	private PublishSubject<PermissionRequest> permissionsDetailedSubject = PublishSubject.create();
	private PublishSubject<PermissionRequest> permissionsSubject = PublishSubject.create();
	private PublishSubject<PermissionResult> resultsSubject = PublishSubject.create();
	private CompositeDisposable compositeDisposable = new CompositeDisposable();

	/**
	 * When the ViewModelProvider creates the view model, it needs a zero argument constructor.
	 */
	@SuppressWarnings("unused")
	public PermissionsViewModel() {
		this.baseSchedulerProvider = SchedulerProvider.getInstance();
	}

	@Inject
	public PermissionsViewModel(BaseSchedulerProvider baseSchedulerProvider) {
		this.baseSchedulerProvider = baseSchedulerProvider;
	}

	public void init(Activity activity) {
		this.activityRef = new WeakReference<>(activity);
		rxPermissions = new RxPermissions(activityRef.get());

		if (initialised) {
			return;
		}
		this.initialised = true;

		compositeDisposable.add(permissionsDetailedSubject
				.doOnNext(permissionRequest -> Timber.d("Request permission detailed  %s", permissionRequest))
				.flatMap(permissionRequest -> rxPermissions.requestEach(permissionRequest.getPermission()),
						(permissionRequest, permission) -> PermissionResult.detailed(permission.granted, permission.shouldShowRequestPermissionRationale, permissionRequest))
				.observeOn(baseSchedulerProvider.ui())
				.subscribe(permissionResult -> resultsSubject.onNext(permissionResult), Timber::e)
		);

		compositeDisposable.add(permissionsSubject
				.doOnNext(permissionRequest -> Timber.d("Request permission  %s", permissionRequest))
				.flatMap(permissionRequest -> rxPermissions.request(permissionRequest.getPermission()),
						(permissionRequest, granted) -> PermissionResult.simple(granted, permissionRequest))
				.observeOn(baseSchedulerProvider.ui())
				.subscribe(permissionResult -> resultsSubject.onNext(permissionResult), Timber::e)
		);

	}

	public void clear() {
		this.activityRef = null;
	}

	public Observable<PermissionResult> getPermissionResult() {
		return resultsSubject.distinctUntilChanged()
				.doOnNext(permissionResult -> Timber.d("Emitting permission result %s", permissionResult));
	}

	public void requestPermissionDetailed(PermissionRequest permissionRequest) {
		permissionsDetailedSubject.onNext(permissionRequest);
	}

	public void requestPermission(PermissionRequest permissionRequest) {
		permissionsSubject.onNext(permissionRequest);
	}

	@Override
	protected void onCleared() {
		compositeDisposable.clear();
		super.onCleared();
	}
}
