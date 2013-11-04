package com.shoebox.android.news;

import org.w3c.dom.Text;

import com.shoebox.android.MenuActivity;
import com.shoebox.android.home.HomeActivity;
import com.shoebox.android.util.AlertDialogs;

import com.shoebox.android.R;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class DetailsNewsActivity extends MenuActivity
{
	String titlu, link, descriere;
	TextView txtDescriere;
	Button btnTitlu;
	
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.details_news);
    	if(DetailsNewsActivity.this.getIntent().getExtras() != null)
		{ 
			Bundle bundles = this.getIntent().getExtras();
			titlu = bundles.getString("titlu");
			link = bundles.getString("link");
			descriere = bundles.getString("descriere");
		}
    	
    	txtDescriere = (TextView)findViewById(R.id.txtDescriere);
    	btnTitlu = (Button)findViewById(R.id.txtTitle);
    	
    	txtDescriere.setMovementMethod(LinkMovementMethod.getInstance());
    	txtDescriere.setText(Html.fromHtml(descriere));
    	btnTitlu.setText(titlu);
    }
    
    public void btnTitlu_onClick(View v)
	{
    	if(AlertDialogs.checkNetworkStatus(DetailsNewsActivity.this))
		{
			Uri CONTENT_URI = Uri.parse(link);
			Intent myIntent = new Intent(Intent.ACTION_VIEW, CONTENT_URI);
			startActivity(myIntent);
		}
		else
		{
			AlertDialogs.createAlertDialogNoInternetConn(getParent());
		}
	}
}
