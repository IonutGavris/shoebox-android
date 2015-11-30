package com.shoebox.android;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.shoebox.android.util.HelperClass;

public class AboutActivity extends AppCompatActivity {

    private TextView txtAbout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        txtAbout = (TextView) findViewById(R.id.txtAbout);
    }

    public void btnFacebook_onClick(View v) {
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(HelperClass.urlFacebook));
        startActivity(browserIntent);
    }

    public void btnTwitter_onClick(View v) {
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(HelperClass.urlTwitter));
        startActivity(browserIntent);
    }

    public void btnPhone_onClick(View v) {
        Intent callIntent = new Intent(Intent.ACTION_DIAL);
        callIntent.setData(Uri.parse("tel:" + Uri.encode(HelperClass.contactPhoneNumber)));
        callIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(callIntent);
    }

    public void btnMail_onClick(View v) {
        Intent sendIntent = new Intent(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{HelperClass.contactEmail});
        sendIntent.putExtra(Intent.EXTRA_TEXT, "");
        sendIntent.putExtra(Intent.EXTRA_SUBJECT, getResources().getString(R.string.subject_mail));
        sendIntent.setType("message/rfc822");
        startActivity(Intent.createChooser(sendIntent, "Send a Message:"));
    }
}
