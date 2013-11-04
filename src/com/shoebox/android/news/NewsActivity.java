package com.shoebox.android.news;

import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.shoebox.android.MenuActivity;
import com.shoebox.android.R;
import com.shoebox.android.rss.RSSFeed;
import com.shoebox.android.rss.RSSHandler;
import com.shoebox.android.util.AlertDialogs;
import com.shoebox.android.util.CustomClass;

public class NewsActivity extends MenuActivity {
  private static final String TAG = NewsActivity.class.getSimpleName();
  private RSSFeed myRssFeed = null;
  private ListView rssList;
  private boolean rssLoaded = false;

  /** Called when the activity is first created. */
  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.rss_list);
    rssList = (ListView) findViewById(R.id.listView);
  }

  @Override
  protected void onResume() {
    super.onResume();
    if (!rssLoaded) {
      loadFeeds();
    }
  }

  private void loadFeeds() {
    AsyncTask<Void, Integer, RSSFeed> task = new AsyncTask<Void, Integer, RSSFeed>() {

      private static final int ERROR = 0;
      private static final int NO_NETWORK = 1;
      private ProgressDialog progressDialog;

      @Override
      protected void onPreExecute() {
        progressDialog = ProgressDialog.show(NewsActivity.this, null, "Loading data...");
      }

      @Override
      protected RSSFeed doInBackground(Void... params) {
        try {
          if (AlertDialogs.checkNetworkStatus(NewsActivity.this)) {
            URL rssUrl = new URL(CustomClass.urlRSSFeed);
            SAXParserFactory mySAXParserFactory = SAXParserFactory.newInstance();
            SAXParser mySAXParser = mySAXParserFactory.newSAXParser();
            XMLReader myXMLReader = mySAXParser.getXMLReader();
            RSSHandler myRSSHandler = new RSSHandler();
            myXMLReader.setContentHandler(myRSSHandler);
            InputSource myInputSource = new InputSource(rssUrl.openStream());
            myXMLReader.parse(myInputSource);

            return myRSSHandler.getFeed();
          }
          else {
            // AlertDialogs.createAlertDialogNoInternetConn(getParent());
            publishProgress(NO_NETWORK);
            return null;
          }

        }
        catch (Throwable e) {
          Log.e(TAG, "Failed to load feeds.", e);
          publishProgress(ERROR);
          return null;
        }
      }

      @Override
      protected void onPostExecute(RSSFeed result) {
        if (progressDialog != null) {
          progressDialog.dismiss();
        }
        if (result != null) {
          myRssFeed = result;
          RssListAdapter adapter = new RssListAdapter(getParent());
          rssList.setAdapter(adapter);
          rssLoaded = true;
        }
      }

      @Override
      protected void onProgressUpdate(Integer... values) {
        switch (values[0]) {
        case NO_NETWORK:
          AlertDialogs.createAlertDialogNoInternetConn(getParent());
          break;
        case ERROR:
          AlertDialog.Builder builder = new AlertDialog.Builder(NewsActivity.this);
          builder.setTitle("Eroare");
          builder.setMessage("Incercati mai tarziu.");
          builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
              finish();
            }
          });
          builder.create().show();
          break;
        }
      }

    };
    if (Build.VERSION.SDK_INT >= 11) {
      task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, null);
    }
    else {
      task.execute();
    }

  }

  private class RssListAdapter extends BaseAdapter {
    private LayoutInflater mInflater;

    public RssListAdapter(Context context) {
      mInflater = LayoutInflater.from(context);
    }

    public int getCount() {
      return myRssFeed.getList().size();
    }

    public Object getItem(int position) {
      return position;
    }

    public long getItemId(int position) {
      return position;
    }

    public View getView(final int position, View convertView, ViewGroup parent) {
      ViewHolder holder;
      if (convertView == null) {
        convertView = mInflater.inflate(R.layout.item_rss_list, null);
        holder = new ViewHolder();

        holder.txtNume = (TextView) convertView.findViewById(R.id.rowtext);
        holder.txtPubDate = (TextView) convertView.findViewById(R.id.pubDate);
        convertView.setTag(holder);
      }
      else {
        holder = (ViewHolder) convertView.getTag();
      }

      holder.txtNume.setText(myRssFeed.getList().get(position).getTitle());

      String date = myRssFeed.getList().get(position).getPubdate();

      SimpleDateFormat inputFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm");
      SimpleDateFormat outputFormat = new SimpleDateFormat("EEE, dd MMMM yyyy HH:mm:ss z");
      Date inputDate = null;
      try {
        inputDate = outputFormat.parse(date);
      }
      catch (ParseException e) {
        e.printStackTrace();
      }
      holder.txtPubDate.setText(inputFormat.format(inputDate));

      convertView.setOnClickListener(new OnClickListener() {
        public void onClick(View v) {
          String link = myRssFeed.getList().get(position).getLink();
          String descriere = myRssFeed.getList().get(position).getDescription();
          String titlu = myRssFeed.getList().get(position).getTitle();

          Intent o = new Intent(NewsActivity.this, DetailsNewsActivity.class);
          o.putExtra("link", link);
          o.putExtra("descriere", descriere);
          o.putExtra("titlu", titlu);
          /*NewsGroup parentActivity = (NewsGroup) getParent();
          parentActivity.replaceContentView("", o);*/
          startActivity(o);
        }
      });
      return convertView;
    }
  }

  class ViewHolder {
    TextView txtNume, txtPubDate;
  }
}