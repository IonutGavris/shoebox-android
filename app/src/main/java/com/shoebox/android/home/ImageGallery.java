package com.shoebox.android.home;

import java.util.ArrayList;

import com.shoebox.android.MenuActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.ViewFlipper;
import com.shoebox.android.R;

public class ImageGallery extends MenuActivity
{
	private static final int SWIPE_MIN_DISTANCE = 120;
	private static final int SWIPE_MAX_OFF_PATH = 250;
	private static final int SWIPE_THRESHOLD_VELOCITY = 200;
	private GestureDetector gestureDetector;
	View.OnTouchListener gestureListener;
	private Animation slideLeftIn;
	private Animation slideLeftOut; 
	private Animation slideRightIn;
	private Animation slideRightOut;
	private ViewFlipper viewFlipper;

	ArrayList<Integer> listImages;

	@Override
	public void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.image_gallery);
		
		listImages = new ArrayList<Integer>();
		listImages.add(R.drawable.img1);
		listImages.add(R.drawable.img2);
		listImages.add(R.drawable.img3);
		listImages.add(R.drawable.img4);
		listImages.add(R.drawable.img5);
		listImages.add(R.drawable.img6);
		listImages.add(R.drawable.img7);
		listImages.add(R.drawable.img8);
		listImages.add(R.drawable.img9);
		listImages.add(R.drawable.img10);
		listImages.add(R.drawable.img11);
		listImages.add(R.drawable.img12);
		listImages.add(R.drawable.img13);
		listImages.add(R.drawable.img14);
		
		ViewPager viewPager = (ViewPager) findViewById(R.id.view_pager);
		ImagePagerAdapter adapter = new ImagePagerAdapter();
		viewPager.setAdapter(adapter);
	}

	private class ImagePagerAdapter extends PagerAdapter
	{
		@Override
		public int getCount() 
		{
			return listImages.size();
		}

		public int getItemPosition(Object object)
		{
		    return POSITION_NONE;
		}
		
		@Override
		public boolean isViewFromObject(View view, Object object) 
		{
			return view == ((ImageView) object);
		}

		@Override
		public Object instantiateItem(ViewGroup container, final int position)
		{
			Context context = ImageGallery.this;
			ImageView imageView = new ImageView(context);
			int padding = context.getResources().getDimensionPixelSize(
					R.dimen.padding_medium);
			imageView.setPadding(padding, padding, padding, padding);
			imageView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
			imageView.setImageResource(listImages.get(position));
			
			imageView.setOnClickListener(new OnClickListener() 
			{
				public void onClick(View v)  
				{
					Intent share = new Intent(Intent.ACTION_SEND);
					share.setType("image/png");
					
					Uri uri = Uri.parse("android.resource://com.shoebox.android/drawable/img"+ (position + 1));
					share.putExtra(Intent.EXTRA_STREAM, uri);
					startActivity(Intent.createChooser(share, "Share Image"));
				}
			});
			
			((ViewPager) container).addView(imageView, 0);
			return imageView;
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) 
		{
			((ViewPager) container).removeView((ImageView) object);
		}
	}
}

/*	viewFlipper = (ViewFlipper)findViewById(R.id.flipper);
		slideLeftIn = AnimationUtils.loadAnimation(this, R.anim.slide_left_in);
		slideLeftOut = AnimationUtils.loadAnimation(this, R.anim.slide_left_out);
		slideRightIn = AnimationUtils.loadAnimation(this, R.anim.slide_right_in);
		slideRightOut = AnimationUtils.loadAnimation(this, R.anim.slide_right_out);

		listImages = new ArrayList<Integer>();
		listImages.add(R.drawable.img1);
		listImages.add(R.drawable.img2);
		listImages.add(R.drawable.img3);
		listImages.add(R.drawable.img4);
		listImages.add(R.drawable.img5);
		listImages.add(R.drawable.img6);
		listImages.add(R.drawable.img7);
		listImages.add(R.drawable.img8);
		listImages.add(R.drawable.img9);
		listImages.add(R.drawable.img10);
		listImages.add(R.drawable.img11);
		listImages.add(R.drawable.img12);
		listImages.add(R.drawable.img13);
		listImages.add(R.drawable.img14);

		int nrImagini = listImages.size();
		processingViewFlippers(nrImagini);
		//viewFlipper.setDisplayedChild(0);

		gestureDetector = new GestureDetector(new MyGestureDetector());
		gestureListener = new View.OnTouchListener()
		{
			public boolean onTouch(View v, MotionEvent event)
			{
				if (gestureDetector.onTouchEvent(event))
				{
					return true;
				}
				return false;
			}
		};

		processingViewFlippers(nrImagini);
	}

	public void onResume()
	{
		super.onResume();


		int nrImagini = listImages.size();
		processingViewFlippers(nrImagini);
		viewFlipper.setDisplayedChild(0);
	}

	private void processingViewFlippers(int nrMasini) 
	{
		viewFlipper.removeAllViews();

		if(nrMasini > 0)
		{
			for(int i = 0; i < nrMasini; i++)
			{
				Log.e("in opricessing", "Da");
				View view = View.inflate(getParent(), R.layout.image_view, null);
				viewFlipper.addView(view);

				Drawable image = getResources().getDrawable(listImages.get(i));
				Bitmap bitmap = ((BitmapDrawable) image).getBitmap();
				Drawable d = new BitmapDrawable(getResources(), Bitmap.createScaledBitmap(bitmap, 140, 70, true));

				ImageView img = (ImageView)view.findViewById(R.id.img);
				img.setImageDrawable(d);
			}
		}
	} 

	class MyGestureDetector extends SimpleOnGestureListener 
	{ 
		@Override
		public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY)
		{
			try 
			{ 
				if (Math.abs(e1.getY() - e2.getY()) > SWIPE_MAX_OFF_PATH)
					return false;
				// right to left swipe
				if(e1.getX() - e2.getX() > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) 
				{
					Log.e("in slideLeftIn", "Da");
					viewFlipper.setInAnimation(slideLeftIn);
					viewFlipper.setOutAnimation(slideLeftOut);
					viewFlipper.showNext();
				}  
				else 
					if (e2.getX() - e1.getX() > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY)
					{
						Log.e("in slideRightIn", "Da");
						viewFlipper.setInAnimation(slideRightIn);
						viewFlipper.setOutAnimation(slideRightOut);
						viewFlipper.showPrevious();
					}
			}
			catch (Exception e)
			{
				// nothing
			}
			return false;
		}
	}

	@Override
	public boolean dispatchTouchEvent(MotionEvent ev)
	{
	    super.dispatchTouchEvent(ev);    
	    return gestureDetector.onTouchEvent(ev); 
	}

	@Override
	public boolean onTouchEvent(MotionEvent event)
	{
		if (gestureDetector.onTouchEvent(event))
			return true;
		else
			return false;
	}
}
 */