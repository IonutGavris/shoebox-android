package com.shoebox.android.locatii;
import com.shoebox.android.MenuActivity;

import com.shoebox.android.R;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

public class ShowMapActivity extends MenuActivity
{
	WebView webView;
	ProgressBar progressBar;
	String latitude, longitude;
	
	@Override
	public void onCreate(Bundle savedInstanceState)  
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.show_map_view);
		
		if (ShowMapActivity.this.getIntent().getExtras() != null)
		{
			Bundle bundles = this.getIntent().getExtras();
			latitude = bundles.getString("lat");
			longitude = bundles.getString("lon");
		} 
		

		progressBar = (ProgressBar) findViewById(R.id.ProgressBar);
		progressBar.setVisibility(View.VISIBLE);
		
		webView = (WebView)findViewById(R.id.webView);
		webView.getSettings().setJavaScriptEnabled(true);
		webView.setWebViewClient(new myWebClient());
		String url = "http://maps.google.com/maps?f=q&source=s_q&hl=en&geocode=&q="+ latitude +"," + longitude + "&sspn="+ latitude +"," + longitude + "&ie=UTF8&z=16";
		webView.loadUrl(url);
	}
	
	public class myWebClient extends WebViewClient
	{
		@Override
		public void onPageStarted(WebView view, String url, Bitmap favicon) 
		{
			super.onPageStarted(view, url, favicon);
		}

		@Override
		public boolean shouldOverrideUrlLoading(WebView view, String url) 
		{
			view.loadUrl(url);
			return true;
		}

		@Override
		public void onPageFinished(WebView view, String url) 
		{
			super.onPageFinished(view, url);
			progressBar.setVisibility(View.GONE);
		}
	}
}
 