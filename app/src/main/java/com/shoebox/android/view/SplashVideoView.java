package com.shoebox.android.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.VideoView;

/*
 * Created by vasi on 11/28/13.
 */
public class SplashVideoView extends VideoView {
    private DisplayMode screenMode;
    private int mVideoWidth = 1920;
    private int mVideoHeight = 1080;

    public SplashVideoView(Context context) {
        super(context);
    }

    public SplashVideoView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SplashVideoView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public enum DisplayMode {
        ORIGINAL,       // original aspect ratio
        FULL_SCREEN,    // fit to screen
        ZOOM            // zoom in
    }

    public void setScreenMode(DisplayMode screenMode) {
        this.screenMode = screenMode;
    }

    public void setVideoWidth(int mVideoWidth) {
        this.mVideoWidth = mVideoWidth;
    }

    public void setVideoHeight(int mVideoHeight) {
        this.mVideoHeight = mVideoHeight;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = getDefaultSize(mVideoWidth, widthMeasureSpec);
        int height = getDefaultSize(mVideoHeight, heightMeasureSpec);

        if (screenMode == DisplayMode.ORIGINAL) {
            if (mVideoWidth > 0 && mVideoHeight > 0) {
                if (mVideoWidth * height > width * mVideoHeight) {
                    // video height exceeds screen, shrink it
                    height = width * mVideoHeight / mVideoWidth;
                } else if (mVideoWidth * height < width * mVideoHeight) {
                    // video width exceeds screen, shrink it
                    width = height * mVideoWidth / mVideoHeight;
                } else {
                    // aspect ratio is correct
                }
            }
        } else if (screenMode == DisplayMode.FULL_SCREEN) {
            // just use the default screen width and screen height
        } else if (screenMode == DisplayMode.ZOOM) {
            // zoom video
            if (mVideoWidth > 0 && mVideoHeight > 0 && mVideoWidth < width) {
                height = mVideoHeight * width / mVideoWidth;
            }
        }

        // must set this at the end
        setMeasuredDimension(width, height);
    }

//    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
//        //Log.i("@@@@", "onMeasure");
//        int width = getDefaultSize(mVideoWidth, widthMeasureSpec);
//        int height = getDefaultSize(mVideoHeight, heightMeasureSpec);
//        if (mVideoWidth > 0 && mVideoHeight > 0) {
//            if ( mVideoWidth * height  > width * mVideoHeight ) {
//                //Log.i("@@@", "image too tall, correcting");
//                height = width * mVideoHeight / mVideoWidth;
//            } else if ( mVideoWidth * height  < width * mVideoHeight ) {
//                //Log.i("@@@", "image too wide, correcting");
//                width = height * mVideoWidth / mVideoHeight;
//            } else {
//                //Log.i("@@@", "aspect ratio is correct: " +
//                //width+"/"+height+"="+
//                //mVideoWidth+"/"+mVideoHeight);
//            }
//        }
//        //Log.i("@@@@@@@@@@", "setting size: " + width + 'x' + height);
//        setMeasuredDimension(width, height);
//    }

    public void changeVideoSize(int width, int height)
    {
        mVideoWidth = width;
        mVideoHeight = height;

        // not sure whether it is useful or not but safe to do so
        getHolder().setFixedSize(width, height);

        requestLayout();
        invalidate();     // very important, so that onMeasure will be triggered
    }
}
