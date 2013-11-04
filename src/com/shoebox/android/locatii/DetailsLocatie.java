package com.shoebox.android.locatii;

import java.util.ArrayList;
import java.util.List;

import com.shoebox.android.MenuActivity;
import com.shoebox.android.util.AlertDialogs;
import com.shoebox.android.util.CustomClass;

import de.greenrobot.daoexample.DaoSession;
import de.greenrobot.daoexample.Locatii;
import de.greenrobot.daoexample.LocatiiDao;
import de.greenrobot.daoexample.LocatiiDao.Properties;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.shoebox.android.R;

public class DetailsLocatie extends MenuActivity {
  TextView txtLocalitate, txtAdresa, txtPersoanaContact1, txtPersoanaContact2, txtOrar;
  Button btnPhone1, btnPhone2;
  LinearLayout layoutPersoana, layoutTelefon, layoutOrar;
  String id;

  DaoSession daoSession;
  LocatiiDao locatiiDao;
  Locatii locatie;

  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.details_locatie);

    if (DetailsLocatie.this.getIntent().getExtras() != null) {
      Bundle bundles = this.getIntent().getExtras();
      id = bundles.getString("id");
    }

    txtLocalitate = (TextView) findViewById(R.id.txtLocalitate);
    txtAdresa = (TextView) findViewById(R.id.txtAdresa);
    txtPersoanaContact1 = (TextView) findViewById(R.id.txtPersoanaContact1);
    txtPersoanaContact2 = (TextView) findViewById(R.id.txtPersoanaContact2);
    txtOrar = (TextView) findViewById(R.id.txtOrar);

    btnPhone1 = (Button) findViewById(R.id.btnPhone1);
    btnPhone2 = (Button) findViewById(R.id.btnPhone2);

    layoutOrar = (LinearLayout) findViewById(R.id.layoutOrar);
    layoutPersoana = (LinearLayout) findViewById(R.id.layoutPersoana);
    layoutTelefon = (LinearLayout) findViewById(R.id.layoutTelefonPersoana);

    daoSession = DBHelper.getSessionInstance(getApplicationContext());
    locatiiDao = daoSession.getLocatiiDao();

    List<Locatii> locatii = new ArrayList<Locatii>();
    locatii = locatiiDao.queryBuilder().where(Properties.Id.eq(id)).list();
    int size = locatii.size();
    if (size > 0) {
      locatie = locatii.get(0);

      String adresa = locatie.getAdresa() + "; " + locatie.getDetaliiAdresa();
      txtAdresa.setText(adresa);

      String pers1 = locatie.getPersoanaContact1().toString();
      txtPersoanaContact1.setText(pers1);

      String phone1 = locatie.getNrTelefonPersoanaContact1().toString();
      btnPhone1.setText(phone1);

      String pers2 = locatie.getPersoanaContact2().toString();
      if (CustomClass.checkString(pers2)) {
        txtPersoanaContact2.setText(pers2);
      }
      else {
        layoutPersoana.setVisibility(View.GONE);
      }

      String phone2 = locatie.getNrTelefonPersoanaContact2().toString();
      if (CustomClass.checkString(phone2)) {
        btnPhone2.setText(phone2);
      }
      else {
        layoutTelefon.setVisibility(View.GONE);
      }

      String orar = locatie.getOrar().toString();
      if (CustomClass.checkString(orar)) {
        txtOrar.setText(orar);
      }
      else {
        layoutOrar.setVisibility(View.GONE);
      }

      String oras = locatie.getOras().toString();
      txtLocalitate.setText(oras);
    }
  }

  public void btnTelefon1_onClick(View v) {
    AlertDialog.Builder builder = new AlertDialog.Builder(getParent());
    builder.setTitle("Atentie");
    builder.setMessage("Doriti sa apelati acest numar de telefon: " + btnPhone1.getText() + " ?");
    builder.setPositiveButton("Da", new DialogInterface.OnClickListener() {
      public void onClick(DialogInterface dialog, int which) {
        try {
          String uri = "tel:" + btnPhone1.getText();
          Intent intent = new Intent(Intent.ACTION_CALL);
          intent.setData(Uri.parse(uri));
          startActivity(intent);
        }
        catch (ActivityNotFoundException activityException) {
        }
      }
    });
    builder.setNegativeButton("Nu", new DialogInterface.OnClickListener() {
      public void onClick(DialogInterface dialog, int which) {
      }
    });

    builder.create().show();
  }

  public void btnTelefon2_onClick(View v) {
    AlertDialog.Builder builder = new AlertDialog.Builder(getParent());
    builder.setTitle("Atentie");
    builder.setMessage("Doriti sa apelati acest numar de telefon: " + btnPhone2.getText() + " ?");
    builder.setPositiveButton("Da", new DialogInterface.OnClickListener() {
      public void onClick(DialogInterface dialog, int which) {
        try {
          String uri = "tel:" + btnPhone2.getText();
          Intent intent = new Intent(Intent.ACTION_CALL);
          intent.setData(Uri.parse(uri));
          startActivity(intent);
        }
        catch (ActivityNotFoundException activityException) {
        }
      }
    });
    builder.setNegativeButton("Nu", new DialogInterface.OnClickListener() {
      public void onClick(DialogInterface dialog, int which) {
      }
    });

    builder.create().show();
  }

  public void btnMaps_onClick(View v) {
    if (AlertDialogs.checkNetworkStatus(getApplicationContext())) {
      /*
       * Intent view = new Intent(DetailsLocatie.this, ShowMapActivity.class); String latitude =
       * locatie.getLatitudine(); String longitude = locatie.getLongitudine(); view.putExtra("lat", latitude);
       * view.putExtra("lon", longitude); LocatiiGroup parentActivity = (LocatiiGroup)getParent();
       * parentActivity.replaceContentView("", view);
       */
      String latitude = locatie.getLatitudine();
      String longitude = locatie.getLongitudine();
      String url = "http://maps.google.com/maps?f=q&source=s_q&hl=en&geocode=&q=" + latitude + "," + longitude
          + "&sspn=" + latitude + "," + longitude + "&ie=UTF8&z=16";
      Intent mapIntent = new Intent(Intent.ACTION_VIEW);
      mapIntent.setData(Uri.parse(url));
      startActivity(mapIntent);
    }
    else {
      AlertDialogs.createAlertDialogNoInternetConn(getParent());
    }
  }
}
