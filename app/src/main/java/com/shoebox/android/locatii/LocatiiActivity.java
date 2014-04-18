package com.shoebox.android.locatii;

import java.util.ArrayList;
import java.util.List;

import com.shoebox.android.MenuActivity;
import com.shoebox.android.news.DetailsNewsActivity;
import com.shoebox.android.news.NewsActivity;
import com.shoebox.android.news.NewsGroup;

import de.greenrobot.daoexample.DaoSession;
import de.greenrobot.daoexample.Locatii;
import de.greenrobot.daoexample.LocatiiDao;

import com.shoebox.android.R;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class LocatiiActivity extends MenuActivity {
  List<Locatii> listaLocatii;
  DaoSession daoSession;
  LocatiiDao locatiiDao;

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.lista_locatii);

    daoSession = DBHelper.getSessionInstance(getApplicationContext());
    locatiiDao = daoSession.getLocatiiDao();

    listaLocatii = new ArrayList<Locatii>();
    listaLocatii = locatiiDao.queryBuilder().list();
    if (listaLocatii.size() == 0) {
      insert();
      listaLocatii = locatiiDao.queryBuilder().list();
    }
    else {
      Log.e("lista", listaLocatii.toString());
    }

    ListView lista = (ListView) findViewById(R.id.listView);
    LocatiiListAdapter adapter = new LocatiiListAdapter(getParent());
    lista.setAdapter(adapter);
  }

  private void insert() {
    Locatii locatie1 = new Locatii();
    locatie1.setAdresa("Str.Doinei nr 2A");
    locatie1.setDetaliiAdresa("sediul TELL-US Consulting");
    locatie1.setNrTelefonPersoanaContact1("0751344273");
    locatie1.setNrTelefonPersoanaContact2("");
    locatie1.setOrar("Luni-Vineri intre 10:00 - 19:00");
    locatie1.setOras("ALBA IULIA");
    locatie1.setPersoanaContact1("Tina");
    locatie1.setPersoanaContact2("");
    locatie1.setLatitudine("46.074742");
    locatie1.setLongitudine("23.576261");
    locatiiDao.insert(locatie1);

    Locatii locatie2 = new Locatii();
    locatie2.setAdresa("Calea Grivitei 33, et. 1, ap.4, sector 1");
    locatie2.setDetaliiAdresa("Blogal Initiative,  interfon 004C");
    locatie2.setNrTelefonPersoanaContact1("0731336829");
    locatie2.setNrTelefonPersoanaContact2("");
    locatie2.setOrar("Luni-Vineri intre 9.00 - 17.00");
    locatie2.setOras("BUCURESTI");
    locatie2.setPersoanaContact1("Loredana Pricopie");
    locatie2.setPersoanaContact2("");
    locatie2.setLatitudine("44.458137");
    locatie2.setLongitudine("26.06021");
    locatiiDao.insert(locatie2);

    Locatii locatie3 = new Locatii();
    locatie3.setAdresa("Calea Motilor 6-8, etaj 8, Room D");
    locatie3.setDetaliiAdresa("sediul iQuest, vis-a-vis de primaria Cluj-Napoca, in curte cu ING Bank");
    locatie3.setNrTelefonPersoanaContact1("0758083786");
    locatie3.setNrTelefonPersoanaContact2("");
    locatie3.setOrar("Luni-Vineri intre 09:00 - 18:00");
    locatie3.setOras("CLUJ-NAPOCA");
    locatie3.setPersoanaContact1("Valentin");
    locatie3.setPersoanaContact2("");
    locatie3.setLatitudine("46.769028");
    locatie3.setLongitudine("23.584124");
    locatiiDao.insert(locatie3);

    Locatii locatie4 = new Locatii();
    locatie4.setAdresa("str. B.P. Hasdeu Nr. 13");
    locatie4.setDetaliiAdresa("sediul iQuest");
    locatie4.setNrTelefonPersoanaContact1("0372343420");
    locatie4.setNrTelefonPersoanaContact2("");
    locatie4.setOrar("");
    locatie4.setOras("BRASOV");
    locatie4.setPersoanaContact1("Sorina Licherdopol");
    locatie4.setPersoanaContact2("");
    locatie4.setLatitudine("45.653528");
    locatie4.setLongitudine("25.623516");
    locatiiDao.insert(locatie4);

    Locatii locatie5 = new Locatii();
    locatie5.setAdresa("str. Nicolaus Olahus Nr.5");
    locatie5.setDetaliiAdresa("sediul iQuest");
    locatie5.setNrTelefonPersoanaContact1("0372343460");
    locatie5.setNrTelefonPersoanaContact2("0742579146");
    locatie5.setOrar("");
    locatie5.setOras("SIBIU");
    locatie5.setPersoanaContact1("Ioana Comsa");
    locatie5.setPersoanaContact2("");
    locatie5.setLatitudine("45.783567");
    locatie5.setLongitudine("24.14652");
    locatiiDao.insert(locatie5);

    Locatii locatie6 = new Locatii();
    locatie6.setAdresa("Str. George Cosbuc, nr.7");
    locatie6.setDetaliiAdresa("sediul Magazinului Profrig, in fata Autogarii Antares");
    locatie6.setNrTelefonPersoanaContact1("0745645153");
    locatie6.setNrTelefonPersoanaContact2("");
    locatie6.setOrar("Luni-Vineri intre 10:00 - 18:00; Sambata intre 10:00 - 12:00");
    locatie6.setOras("RAMNICU VALCEA");
    locatie6.setPersoanaContact1("Carmen Craciun- Cirstoiu");
    locatie6.setPersoanaContact2("");
    locatie6.setLatitudine("45.097314");
    locatie6.setLongitudine("24.365496");
    locatiiDao.insert(locatie6);

    Locatii locatie7 = new Locatii();
    locatie7.setAdresa("Str. Dragoslavele 6B");
    locatie7.setDetaliiAdresa("Centrul de Zi");
    locatie7.setNrTelefonPersoanaContact1("0747499022");
    locatie7.setNrTelefonPersoanaContact2("");
    locatie7.setOrar("Luni-Vineri intre 10:00 - 16:00");
    locatie7.setOras("CONSTANTA");
    locatie7.setPersoanaContact1("Alina Dragoman");
    locatie7.setPersoanaContact2("");
    locatie7.setLatitudine("44.202289");
    locatie7.setLongitudine("28.645851");
    locatiiDao.insert(locatie7);

    Locatii locatie8 = new Locatii();
    locatie8.setAdresa("strada Hermann Oberth nr. 12");
    locatie8.setDetaliiAdresa("Sala Mihai Eminescu");
    locatie8.setNrTelefonPersoanaContact1("0740993720");
    locatie8.setNrTelefonPersoanaContact2("0744426171");
    locatie8.setOrar("14 decembrie 2012 intre 09:00 - 19:00");
    locatie8.setOras("SIGHISOARA");
    locatie8.setPersoanaContact1("Eduard Banarescu");
    locatie8.setPersoanaContact2("Ina Hentz");
    locatie8.setLatitudine("46.217481");
    locatie8.setLongitudine("24.793701");
    locatiiDao.insert(locatie8);

    Locatii locatie9 = new Locatii();
    locatie9.setAdresa("b-dul Mihai Viteazu nr.1");
    locatie9.setDetaliiAdresa("Redactia Graiul Salajului, in spatele restaurantului Time Out");
    locatie9.setNrTelefonPersoanaContact1("0746224969");
    locatie9.setNrTelefonPersoanaContact2("");
    locatie9.setOrar("Luni-Vineri intre 08:00 - 16:00");
    locatie9.setOras("ZALAU");
    locatie9.setPersoanaContact1("Florica Sas");
    locatie9.setPersoanaContact2("");
    locatie9.setLatitudine("47.178775");
    locatie9.setLongitudine("23.055514");
    locatiiDao.insert(locatie9);

    Locatii locatie10 = new Locatii();
    locatie10.setAdresa("str Dr Iosif Bulbuca, nr 15");
    locatie10.setDetaliiAdresa("Centrul Crestin Aletheia, zona calea Buziasului noul Lidl");
    locatie10.setNrTelefonPersoanaContact1("0730582793");
    locatie10.setNrTelefonPersoanaContact2("0726100993");
    locatie10.setOrar("Luni-Vineri intre 08:00 - 20:00");
    locatie10.setOras("TIMISOARA");
    locatie10.setPersoanaContact1("Anne Gunaru");
    locatie10.setPersoanaContact2("Vesa Eunice");
    locatie10.setLatitudine("45.737563");
    locatie10.setLongitudine("21.247104");
    locatiiDao.insert(locatie10);

    Locatii locatie11 = new Locatii();
    locatie11.setAdresa("str. Buciumului, nr. 9, etaj 2");
    locatie11.setDetaliiAdresa("Laboratorul Paval Dent, cladirea Medimex");
    locatie11.setNrTelefonPersoanaContact1("0784030030");
    locatie11.setNrTelefonPersoanaContact2("");
    locatie11.setOrar("");
    locatie11.setOras("BACAU");
    locatie11.setPersoanaContact1("Catalin Paval");
    locatie11.setPersoanaContact2("");
    locatie11.setLatitudine("46.579151");
    locatie11.setLongitudine("26.907769");
    locatiiDao.insert(locatie11);

    Locatii locatie12 = new Locatii();
    locatie12.setAdresa("Strada Domneasca Nr 155 (campus studentesc) camin D, etaj 2, camera 210B");
    locatie12.setDetaliiAdresa("");
    locatie12.setNrTelefonPersoanaContact1("0758303246");
    locatie12.setNrTelefonPersoanaContact2("");
    locatie12.setOrar("");
    locatie12.setOras("GALATI");
    locatie12.setPersoanaContact1("Radu Clapa");
    locatie12.setPersoanaContact2("");
    locatie12.setLatitudine("45.453373");
    locatie12.setLongitudine("28.04998");
    locatiiDao.insert(locatie12);

    Locatii locatie13 = new Locatii();
    locatie13.setAdresa("sediul CLUBUL RECONECTARII, Str.Duraului nr 24");
    locatie13.setDetaliiAdresa("langa cofetaria Diana");
    locatie13.setNrTelefonPersoanaContact1("0748521627");
    locatie13.setNrTelefonPersoanaContact2("");
    locatie13.setOrar("");
    locatie13.setOras("PIATRA NEAMT");
    locatie13.setPersoanaContact1("Oana Apetrei");
    locatie13.setPersoanaContact2("");
    locatie13.setLatitudine("46.931532");
    locatie13.setLongitudine("26.365105");
    locatiiDao.insert(locatie13);

    Locatii locatie14 = new Locatii();
    locatie14.setAdresa("str. Paun nr.70");
    locatie14.setDetaliiAdresa("Asociatia pentru Dezvoltarea Programelor Sociale (ADPS) Iasi, in incinta CSC Bucium");
    locatie14.setNrTelefonPersoanaContact1("0741090309");
    locatie14.setNrTelefonPersoanaContact2("0743752498");
    locatie14.setOrar("");
    locatie14.setOras("IASI");
    locatie14.setPersoanaContact1("Madalina Belcescu");
    locatie14.setPersoanaContact2("Ifrosie Laura");
    locatie14.setLatitudine("47.165114");
    locatie14.setLongitudine("27.596888");
    locatiiDao.insert(locatie14);

    Locatii locatie15 = new Locatii();
    locatie15.setAdresa("strada Aurel Vlaicu nr.5");
    locatie15.setDetaliiAdresa("Sediul Radio Fir");
    locatie15.setNrTelefonPersoanaContact1("0748148447");
    locatie15.setNrTelefonPersoanaContact2("");
    locatie15.setOrar("Luni-Vineri intre 08:00 - 16:00");
    locatie15.setOras("DEJ");
    locatie15.setPersoanaContact1("Magdalena Rus");
    locatie15.setPersoanaContact2("");
    locatie15.setLatitudine("47.141482");
    locatie15.setLongitudine("23.873695");
    locatiiDao.insert(locatie15);

    Locatii locatie16 = new Locatii();
    locatie16.setAdresa("str. Isaccei, Casa Sindicatelor");
    locatie16.setDetaliiAdresa("Sediul Magazinului Belle Boutique. parter (langa Piata Veche)");
    locatie16.setNrTelefonPersoanaContact1("0745796208");
    locatie16.setNrTelefonPersoanaContact2("");
    locatie16.setOrar("Luni-Vineri intre 10:00 - 19:00; Sambata intre 12:00 - 15:00");
    locatie16.setOras("TULCEA");
    locatie16.setPersoanaContact1("Nascu Adriana");
    locatie16.setPersoanaContact2("");
    locatie16.setLatitudine("45.179965");
    locatie16.setLongitudine("28.795913");
    locatiiDao.insert(locatie16);

    Locatii locatie17 = new Locatii();
    locatie17.setAdresa("Str. Principala, nr. 113");
    locatie17.setDetaliiAdresa("Casa Huniadi");
    locatie17.setNrTelefonPersoanaContact1("0745035179");
    locatie17.setNrTelefonPersoanaContact2("0372708825");
    locatie17.setOrar("Luni-Duminica intre 08:00 - 20:00");
    locatie17.setOras("DEVA - SILVASU DE SUS");
    locatie17.setPersoanaContact1("Cristina");
    locatie17.setPersoanaContact2("");
    locatie17.setLatitudine("45.646528");
    locatie17.setLongitudine("22.881385");
    locatiiDao.insert(locatie17);

    Locatii locatie18 = new Locatii();
    locatie18.setAdresa("Bvd Carol I, nr. 138, Bl J3");
    locatie18.setDetaliiAdresa("Pe dreapta, fata in fata cu noul cazino; Ap2, interfon 2 (Cabinet Stomatologic Dr. Cojocaru Melania)");
    locatie18.setNrTelefonPersoanaContact1("0753777081");
    locatie18.setNrTelefonPersoanaContact2("0726179780");
    locatie18.setOrar("Luni, Marti, Joi, Vineri si Sambata intre 15:00 - 20:00");
    locatie18.setOras("CRAIOVA");
    locatie18.setPersoanaContact1("Rares Cojocaru");
    locatie18.setPersoanaContact2("Radu Ionescu");
    locatie18.setLatitudine("44.328138");
    locatie18.setLongitudine("23.814528");
    locatiiDao.insert(locatie18);
  }

  private class LocatiiListAdapter extends BaseAdapter {
    private LayoutInflater mInflater;

    public LocatiiListAdapter(Context context) {
      mInflater = LayoutInflater.from(context);
    }

    public int getCount() {
      return listaLocatii.size();
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
        convertView = mInflater.inflate(R.layout.item_locatie, null);
        holder = new ViewHolder();

        holder.txtNume = (TextView) convertView.findViewById(R.id.txtDenumire);
        convertView.setTag(holder);
      }
      else {
        holder = (ViewHolder) convertView.getTag();
      }

      holder.txtNume.setText(listaLocatii.get(position).getOras());

      /*
       * convertView.setOnLongClickListener(new OnLongClickListener() { public boolean onLongClick(View v) {
       * AlertDialog.Builder builder = new AlertDialog.Builder(getParent());
       * builder.setCancelable(false).setTitle("Alert").setMessage("Doriti sa apelati?") .setPositiveButton("Da", new
       * DialogInterface.OnClickListener() { public void onClick(DialogInterface dialog, int id) { } })
       * .setNegativeButton("Nu", new DialogInterface.OnClickListener() { public void onClick(DialogInterface dialog,
       * int id) { dialog.dismiss(); } }); AlertDialog alert = builder.create(); alert.show();
       * 
       * return false; } });
       */

      convertView.setOnClickListener(new OnClickListener() {
        public void onClick(View v) {
          Intent o = new Intent(LocatiiActivity.this, DetailsLocatie.class);
          o.putExtra("id", listaLocatii.get(position).getId() + "");
          LocatiiGroup parentActivity = (LocatiiGroup) getParent();
          parentActivity.replaceContentView("", o);

        }
      });
      return convertView;
    }
  }

  class ViewHolder {
    TextView txtNume;
  }
}
