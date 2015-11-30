package com.shoebox.android;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.Indicators.PagerIndicator;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.daimajia.slider.library.Tricks.ViewPagerEx;

import java.util.HashMap;

public class SliderImagesActivity extends AppCompatActivity implements BaseSliderView.OnSliderClickListener, ViewPagerEx.OnPageChangeListener {

    private SliderLayout mDemoSlider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_slider_images);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mDemoSlider = (SliderLayout) findViewById(R.id.slider);
        HashMap<String, String> url_maps = new HashMap<String, String>();
        url_maps.put(getResources().getString(R.string.deadline_slide), "http://www.shoebox.ro/wp-content/uploads/2014/06/Slide-2-shoebox.ro-top-header.jpg");
        url_maps.put(getResources().getString(R.string.boxcontent_slide), "http://www.shoebox.ro/wp-content/uploads/2014/06/Slide-3-shoebox.ro-top-header.jpg");
        url_maps.put(getResources().getString(R.string.location_slider), "http://www.shoebox.ro/wp-content/uploads/2014/06/Slide-4-shoebox.ro-top-header-740x360.jpg");
        url_maps.put(getResources().getString(R.string.partner_slide), "http://www.shoebox.ro/wp-content/uploads/2014/06/Slide-5-shoebox.ro-top-header.jpg");
        url_maps.put(getResources().getString(R.string.shoebox_slide), "http://www.shoebox.ro/wp-content/uploads/2014/06/Slide-1-shoebox.ro-top-header-740x360.jpg");

        HashMap<String, Integer> file_maps = new HashMap<String, Integer>();
        file_maps.put(getResources().getString(R.string.deadline_slide), R.drawable.slide2);
        file_maps.put(getResources().getString(R.string.boxcontent_slide), R.drawable.slide3);
        file_maps.put(getResources().getString(R.string.location_slider), R.drawable.slide4);
        file_maps.put(getResources().getString(R.string.partner_slide), R.drawable.slide5);
        file_maps.put("Ce este ShoeBox?\n\n" +
                "ShoeBox reprezinta o incercare minuscula de a face lumea un loc mai frumos. In fiecare an, in preajma sarbatorilor de Craciun, vrem sa punem un zambet pe chipurile a zeci de mii de copii sarmani din orasele in care locuim si nu numai."
                , R.drawable.slide1);

        for (String name : file_maps.keySet()) {
            TextSliderView textSliderView = new TextSliderView(this);
            // initialize a SliderLayout
            textSliderView
                    .description(name)
                    .image(file_maps.get(name))
                    .setScaleType(BaseSliderView.ScaleType.Fit)
                    .setOnSliderClickListener(this);

            //add your extra information
            textSliderView.bundle(new Bundle());
            textSliderView.getBundle()
                    .putString("extra", name);

            mDemoSlider.addSlider(textSliderView);
        }
        mDemoSlider.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
        mDemoSlider.setCustomAnimation(new DescriptionAnimation());
        mDemoSlider.setDuration(4000);
        mDemoSlider.addOnPageChangeListener(this);
        mDemoSlider.setPresetTransformer(SliderLayout.Transformer.DepthPage);
        mDemoSlider.setCustomIndicator((PagerIndicator) findViewById(R.id.custom_indicator));
    }

    @Override
    protected void onStop() {
        // To prevent a memory leak on rotation, make sure to call stopAutoCycle() on the slider before activity or fragment is destroyed
        mDemoSlider.stopAutoCycle();
        super.onStop();
    }

    @Override
    public void onSliderClick(BaseSliderView slider) {
        Toast.makeText(this, slider.getBundle().get("extra") + "", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
    }

    @Override
    public void onPageSelected(int position) {
        Log.d("Slider Demo", "Page Changed: " + position);
    }

    @Override
    public void onPageScrollStateChanged(int state) {
    }


    public void btnSkip_onClick(View v) {
        Intent main = new Intent(SliderImagesActivity.this, MainActivity.class);
        startActivity(main);
    }
}
