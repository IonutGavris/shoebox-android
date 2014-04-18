package com.shoebox.android.contact;

import com.shoebox.android.MenuActivity;
import com.shoebox.android.util.AlertDialogs;
import com.shoebox.android.util.CustomClass;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import com.shoebox.android.R;


public class ContactsListActivity extends MenuActivity
{
	EditText eTxtNume ;
	EditText eTxtEmail ;
	EditText eTxtTelefon ;
	EditText eTxtMesaj ;

	@Override
	public void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.contact); 

		eTxtNume = (EditText)findViewById(R.id.eTxtNume);
		eTxtEmail = (EditText)findViewById(R.id.eTxtEmail);
		eTxtTelefon = (EditText)findViewById(R.id.eTxtTelefon);
		eTxtMesaj = (EditText)findViewById(R.id.eTxtMesaj);
	}

	public void btnTrimite_onClick(View v)
	{
		String nume = eTxtNume.getText().toString();
		String email = eTxtEmail.getText().toString();
		String telefon = eTxtTelefon.getText().toString();
		String mesaj = eTxtMesaj.getText().toString();

		if(AlertDialogs.checkNetworkStatus(getApplicationContext()))
		{
			if(CustomClass.checkString(nume) && CustomClass.checkString(email) && CustomClass.checkString(mesaj))
			{
				if(CustomClass.checkEmail(email)) 
				{
					new SendMailAsynk().execute();
				}
				else
				{
					AlertDialogs.createAlertWithSingleButton(getParent(), "Alerta", "Email invalid!", "OK");
				}
			}
			else
			{
				AlertDialogs.createAlertDialogCompleteAllFields(getParent());
			}
		}
		else
		{
			AlertDialogs.createAlertDialogNoInternetConn(getParent());
		}

	}

	private class SendMailAsynk extends AsyncTask<Void, Void, Void>
	{
		private ProgressDialog progress = null;

		@SuppressWarnings("unchecked") 
		@Override
		protected Void doInBackground(Void... params)
		{
			String nume = eTxtNume.getText().toString();
			String email = eTxtEmail.getText().toString();
			String telefon = eTxtTelefon.getText().toString();
			String mesaj = eTxtMesaj.getText().toString();
			try 
			{   
				SendMail sm = new  SendMail();
				sm.sendMail(email, "valvesa@gmail.com", "Mesaj aplicatie ShoeBox Android ", "Mesaj de la : " + nume + "\nAdresa email: " + email + "\nContinut mesaj: " + mesaj + " \nNr. telefon: " + telefon);
			} 
			catch (Exception e) 
			{   
				Log.e("SendMail", e.getMessage(), e);   
			} 

			return null;
		}

		@Override
		protected void onCancelled() 
		{
			super.onCancelled(); 
		}

		@Override
		protected void onPreExecute() 
		{
			progress = ProgressDialog.show(getParent(), null, "Asteptati...");
			super.onPreExecute();
		}

		@Override
		protected void onPostExecute(Void result)  
		{
			super.onPostExecute(result);
			progress.dismiss();

			AlertDialog alertDialog = new AlertDialog.Builder(getParent()).create();
			alertDialog.setTitle("Alerta");
			alertDialog.setMessage("Mesajul a fost trimis cu succes!");
			alertDialog.setButton("OK", new DialogInterface.OnClickListener() 
			{
				public void onClick(DialogInterface dialog, int which) 
				{
					eTxtEmail.setText("");
					eTxtMesaj.setText("");
					eTxtNume.setText("");
					eTxtTelefon.setText("");
				} 
			}); 
			alertDialog.show();
		}

		@Override 
		protected void onProgressUpdate(Void... values) 
		{ 
			super.onProgressUpdate(values);
		}
	}

} 

