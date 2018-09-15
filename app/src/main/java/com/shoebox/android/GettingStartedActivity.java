package com.shoebox.android;

import android.animation.Animator;
import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AccelerateInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.shoebox.android.util.SettingsPrefs;
import com.shoebox.android.util.ShoeBoxAnalytics;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindColor;
import butterknife.BindView;
import butterknife.OnClick;

public class GettingStartedActivity extends BaseActivity {

	@BindView(R.id.viewPager)
	ViewPager viewPager;
	@BindView(R.id.dotsLayout)
	LinearLayout dotsLayout;
	@BindView(R.id.rootLayout)
	View content;
	@BindView(R.id.frameBackground)
	FrameLayout frameBackground;

	@BindColor(R.color.transparentOrange)
	int colorOrange;
	@BindColor(R.color.transparentGreen)
	int colorGreen;
	@BindColor(R.color.transparentBlue)
	int colorBlue;
	@BindColor(R.color.shoeBoxOrange)
	int buttonOrange;
	@BindColor(R.color.shoeBoxGreen)
	int buttonGreen;
	@BindColor(R.color.shoeBoxBlue)
	int buttonBlue;

	private ViewPager.OnPageChangeListener pageChangeListener;
	private Handler handler = new Handler();
	private SlidesAdapter slidesAdapter;
	private Runnable runnable;
	private ValueAnimator animator;
	private boolean animatingForward = true;

	public static Intent getLaunchingIntent(Activity activity) {
		return new Intent(activity, GettingStartedActivity.class);
	}

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_getting_started);

		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
			Window w = getWindow();
			w.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
		}

		slidesAdapter = new SlidesAdapter(getSupportFragmentManager());
		slidesAdapter.setSlides(getIntroSlides());
		initDotIndicator(slidesAdapter.getCount());

		final ArgbEvaluator argbEvaluator = new ArgbEvaluator();

		pageChangeListener = new ViewPager.OnPageChangeListener() {

			@Override
			public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
				int color;
				int buttonColor;
				switch (position) {
					case 0:
						color = (Integer) argbEvaluator.evaluate(positionOffset, colorOrange, colorGreen);
						buttonColor = (Integer) argbEvaluator.evaluate(positionOffset, buttonOrange, buttonGreen);
						content.setBackgroundColor(color);
						frameBackground.setBackgroundColor(buttonColor);
						break;
					case 1:
						color = (Integer) argbEvaluator.evaluate(positionOffset, colorGreen, colorBlue);
						buttonColor = (Integer) argbEvaluator.evaluate(positionOffset, buttonGreen, buttonBlue);
						content.setBackgroundColor(color);
						frameBackground.setBackgroundColor(buttonColor);
						break;
					case 2:
						content.setBackgroundColor(colorBlue);
						frameBackground.setBackgroundColor(buttonBlue);
						break;
				}
			}

			@Override
			public void onPageSelected(int page) {
				for (int i = 0; i < dotsLayout.getChildCount(); i++) {
					dotsLayout.getChildAt(i).setSelected(i == page);
				}
			}

			@Override
			public void onPageScrollStateChanged(int i) {

			}
		};

		viewPager.addOnPageChangeListener(pageChangeListener);
		viewPager.setAdapter(slidesAdapter);
		viewPager.setOnTouchListener((v, event) -> {
			stopChangePages();
			return false;
		});

		firebaseAnalytics.logEvent(FirebaseAnalytics.Event.TUTORIAL_BEGIN, null);
	}

	private void changePages(final int page, final boolean forward) {
		runnable = () -> {
			final float SLIDE_WITDH = (viewPager.getWidth() / getResources().getDisplayMetrics().density) / 2;
			final float OFFSET = forward ? -SLIDE_WITDH : SLIDE_WITDH;
			animatingForward = forward;

			animator = ValueAnimator.ofFloat(0, OFFSET);
			animator.setInterpolator(new AccelerateInterpolator(2.0f));
			animator.setDuration(1000);
			animator.addUpdateListener(animation -> {
				if (!viewPager.isFakeDragging()) {
					viewPager.beginFakeDrag();
				}
				viewPager.fakeDragBy((float) animation.getAnimatedValue());
			});
			animator.addListener(new Animator.AnimatorListener() {
				@Override
				public void onAnimationStart(Animator animation) {
				}

				@Override
				public void onAnimationEnd(Animator animation) {
					viewPager.endFakeDrag();
					changePage(page, forward);
				}

				@Override
				public void onAnimationCancel(Animator animation) {
				}

				@Override
				public void onAnimationRepeat(Animator animation) {
				}
			});
			animator.start();
		};
		handler.postDelayed(runnable, 3000);
	}

	private void changePage(int currentPage, boolean forward) {
		if ((forward || currentPage == 0) && currentPage + 1 < slidesAdapter.getCount()) {
			changePages(currentPage + 1, true);
		} else if ((!forward || currentPage == slidesAdapter.getCount() - 1) && currentPage - 1 >= 0) {
			changePages(currentPage - 1, false);
		}
	}

	private void stopChangePages() {
		if (animator != null) {
			animator.cancel();
			animator = null;
		}
		if (runnable != null) {
			handler.removeCallbacks(runnable);
			runnable = null;
		}
	}

	public int getStatusBarHeight() {
		int result = 0;
		int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
		if (resourceId > 0) {
			result = getResources().getDimensionPixelSize(resourceId);
		}
		return result;
	}

	@OnClick(R.id.buttonStart)
	public void buttonStartClicked(View view) {
		Bundle bundle = new Bundle(1);
		bundle.putString(ShoeBoxAnalytics.Param.SLIDE_LAST_SEEN, String.valueOf(viewPager.getCurrentItem() + 1));
		firebaseAnalytics.logEvent(FirebaseAnalytics.Event.TUTORIAL_COMPLETE, bundle);
		finish();
	}

	@Override
	public void finish() {
		super.finish();
		SettingsPrefs.setIsNotFirstTime(getApplicationContext());
	}

	@Override
	protected void onPause() {
		stopChangePages();
		super.onPause();
	}

	@Override
	protected void onResume() {
		super.onResume();
		changePages(viewPager.getCurrentItem(), animatingForward);
	}

	@Override
	protected void onDestroy() {
		viewPager.removeOnPageChangeListener(pageChangeListener);
		super.onDestroy();
	}

	private void initDotIndicator(int size) {
		ImageView dotImg;
		dotsLayout.removeAllViews();
		for (int i = 0; i < size; i++) {
			dotImg = new ImageView(this);
			dotImg.setPadding(10, 0, 10, 0);
			dotImg.setImageResource(R.drawable.dot_indicator);

			dotsLayout.addView(dotImg);
			dotImg.setSelected(i == 0);
		}
	}


	private List<Fragment> getIntroSlides() {
		List<Fragment> slides = new ArrayList<>();
		slides.add(SlideContent.newInstance(0));
		slides.add(SlideContent.newInstance(1));
		slides.add(SlideContent.newInstance(2));
		return slides;
	}

	/**
	 * Fragment that will hold the slide content.
	 */
	public static class SlideContent extends Fragment {
		/**
		 * The fragment argument representing the section number for this fragment.
		 */
		private static final String SLIDE_INDEX = "slide_index";

		/**
		 * Returns a new instance of this fragment for the given section number.
		 */
		public static SlideContent newInstance(int slideIndex) {
			SlideContent fragment = new SlideContent();
			Bundle args = new Bundle();
			args.putInt(SLIDE_INDEX, slideIndex);
			fragment.setArguments(args);
			return fragment;
		}

		@Override
		public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
			View view = null;
			int slideIndex = getArguments() == null ? 0 : getArguments().getInt(SLIDE_INDEX, 0);
			switch (slideIndex) {
				case 0:
					view = inflater.inflate(R.layout.fragment_gs, container, false);
					((ImageView) view.findViewById(R.id.slideImage)).setImageResource(R.drawable.gift_for_boy_girl_large);
					((TextView) view.findViewById(R.id.textMsg1)).setText(R.string.slide1_msg1);
					((TextView) view.findViewById(R.id.textMsg2)).setText(R.string.slide1_msg2);
					break;
				case 1:
					view = inflater.inflate(R.layout.fragment_gs, container, false);
					((ImageView) view.findViewById(R.id.slideImage)).setImageResource(R.drawable.suggested_box_content);
					((TextView) view.findViewById(R.id.textMsg1)).setText(R.string.slide2_msg1);
					((TextView) view.findViewById(R.id.textMsg2)).setText(R.string.slide2_msg2);
					break;
				case 2:
					view = inflater.inflate(R.layout.fragment_gs, container, false);
					((ImageView) view.findViewById(R.id.slideImage)).setImageResource(R.drawable.shoebox_location_center);
					((TextView) view.findViewById(R.id.textMsg1)).setText(R.string.slide3_msg1);
					((TextView) view.findViewById(R.id.textMsg2)).setText(R.string.slide3_msg2);
					break;
			}

			return view;
		}
	}

	private static class SlidesAdapter extends FragmentPagerAdapter {

		private List<Fragment> slides = new ArrayList<>();

		SlidesAdapter(FragmentManager fm) {
			super(fm);
		}

		void setSlides(List<Fragment> slides) {
			this.slides = slides;
		}

		@Override
		public Fragment getItem(int index) {
			return slides.get(index);
		}

		@Override
		public int getCount() {
			return slides.size();
		}
	}
}
