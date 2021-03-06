package com.example.acupunturaclienteandroid;

import java.io.IOException;
import java.util.ArrayList;

import org.json.JSONException;

import com.example.acupunturaclienteandroid.model.SintomaWEB;
import com.example.acupunturaclienteandroid.webutils.RestClientException;
import com.example.acupunturaclienteandroid.webutils.WebServiceUtils;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class Diagnostics extends Activity {

	private ArrayList<SintomaWEB> listaSintomasSelecionados = new ArrayList<SintomaWEB>();
	private ArrayList<String> diagnosticosResult = new ArrayList<String>();
	private ArrayAdapter<String> adapter;
	private ListView lvDiag;
	private ProgressDialog ringProgressDialog;
	private String token = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_diagnostics);
		
		token = PreferenceManager.getDefaultSharedPreferences(this).getString(
				"token", "null");
		
		lvDiag = (ListView) findViewById(R.id.listViewDiag);
		
		String sintomas = getIntent().getStringExtra(Symptoms.DIAGNOSTICOS);
		
		for(String s : sintomas.split("\n")){
			SintomaWEB sin = new SintomaWEB();
			sin.setNome(s);
			listaSintomasSelecionados.add(sin);
		}
		
		new getDiagnosticos().execute();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.diagnostics, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	// Get Lista Diagnosticos --------------------------------------------------

	private class getDiagnosticos extends
			AsyncTask<String, Void, ArrayList<String>> {
		@Override
		protected void onPreExecute() {

			ringProgressDialog = new ProgressDialog(Diagnostics.this);
			ringProgressDialog.setTitle("Please Wait");
			ringProgressDialog.setMessage("Generating Diagnostics...");
			ringProgressDialog.setCancelable(false);
			ringProgressDialog.show();
		};

		@Override
		protected ArrayList<String> doInBackground(String... params) {
			ArrayList<String> lista = new ArrayList<String>();

			try {
				lista = WebServiceUtils.getDiagnosticos(token, listaSintomasSelecionados);
			} catch (IOException e) {
				e.printStackTrace();
			} catch (RestClientException e) {
				e.printStackTrace();
			} catch (JSONException e) {
				e.printStackTrace();
			}
			return lista;
		}

		@Override
		protected void onPostExecute(ArrayList<String> lista) {

			diagnosticosResult = lista;

			adapter = new ArrayAdapter<String>(getApplicationContext(),
					android.R.layout.simple_list_item_activated_1,
					diagnosticosResult);
			lvDiag.setAdapter(adapter);
			ringProgressDialog.dismiss();
		}
	}

	// Fim Get Lista Diagnosticos -------------------------------------------
}
