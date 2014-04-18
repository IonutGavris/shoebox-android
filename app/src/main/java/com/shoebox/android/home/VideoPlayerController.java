package com.shoebox.android.home;

import com.shoebox.android.MenuActivity;

import com.shoebox.android.R;
import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.widget.MediaController;
import android.widget.VideoView;

public class VideoPlayerController extends MenuActivity 
{
	String urlVideo = "http://embed.animoto.com/play.html?w=swf/vp1&e=1321925553&f=v0F0AqygXKswKHIfJ6wCqg&d=103&m=p&r=240p+480p&volume=100&start_res=480p&i=m&ct=Afla%20despre%20ShoeBox.ro!&cu=http://www.shoebox.ro&options=allowfullscreen";

	@Override
	public void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);

		setContentView(R.layout.video);

		VideoView videoView = (VideoView) findViewById(R.id.VideoView);
		MediaController mediaController = new MediaController(this);
		mediaController.setAnchorView(videoView);
		// Set video link (mp4 format )
		Uri video = Uri.parse(urlVideo);
		videoView.setMediaController(mediaController);
		videoView.setVideoURI(video);
		videoView.start();
	}
}