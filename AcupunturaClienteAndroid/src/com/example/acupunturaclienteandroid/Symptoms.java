package com.example.acupunturaclienteandroid;

import java.io.IOException;
import java.util.ArrayList;

import org.json.JSONException;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.example.acupunturaclienteandroid.model.SintomaWEB;
import com.example.acupunturaclienteandroid.webutils.RestClientException;
import com.example.acupunturaclienteandroid.webutils.WebServiceUtils;

public class Symptoms extends Activity {

	private ListView lvSintomas;
	private ArrayList<SintomaWEB> listaSintomasWEB;
	private ArrayList<String> sintomasSelecionados = new ArrayList<String>();
	private String token = "";
	private ProgressDialog ringProgressDialog;
	private ArrayAdapter<SintomaWEB> adapter;
	private Button btnGetDiag;
	public static final String DIAGNOSTICOS = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_symptoms);

		lvSintomas = (ListView) findViewById(R.id.listViewDiag);
		btnGetDiag = (Button) findViewById(R.id.buttonGeraDiagn);

		token = PreferenceManager.getDefaultSharedPreferences(this).getString(
				"token", "null");
		new getListaSintomas().execute();
		lvSintomas.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
		lvSintomas.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {

				lvSintomas.setSelector(R.color.cachecolor);
				String sintomaSelecionado = lvSintomas.getItemAtPosition(
						position).toString();
				if (!sintomasSelecionados.contains(sintomaSelecionado))
					sintomasSelecionados.add(sintomaSelecionado);
				Toast.makeText(getApplicationContext(), sintomaSelecionado,
						Toast.LENGTH_SHORT).show();
			}
		});

		btnGetDiag.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				String res = "";
				for (String s : sintomasSelecionados) {
					res += s + "\n";
				}
				Intent intentDiag = new Intent(getApplicationContext(),
						Diagnostics.class);
				intentDiag.putExtra(DIAGNOSTICOS, res);
				startActivity(intentDiag);
				// Toast.makeText(getApplicationContext(), res,
				// Toast.LENGTH_LONG).show();
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.symptoms, menu);
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

	// Get Lista Sintomas --------------------------------------------------

	private class getListaSintomas extends
			AsyncTask<String, Void, ArrayList<SintomaWEB>> {
		@Override
		protected void onPreExecute() {

			ringProgressDialog = new ProgressDialog(Symptoms.this);
			ringProgressDialog.setTitle("Please Wait");
			ringProgressDialog.setMessage("Getting List of all Symptoms...");
			ringProgressDialog.setCancelable(false);
			ringProgressDialog.show();
		};

		@Override
		protected ArrayList<SintomaWEB> doInBackground(String... params) {
			ArrayList<SintomaWEB> lista = new ArrayList<SintomaWEB>();
			try {
				lista = WebServiceUtils.getAllSintomas(token);
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
		protected void onPostExecute(ArrayList<SintomaWEB> lista) {

			listaSintomasWEB = lista;

			adapter = new ArrayAdapter<SintomaWEB>(getApplicationContext(),
					android.R.layout.simple_list_item_activated_1,
					listaSintomasWEB);
			lvSintomas.setAdapter(adapter);
//			Toast.makeText(getApplicationContext(),
//					"Get Symptoms List Successfull!", Toast.LENGTH_LONG).show();
			ringProgressDialog.dismiss();
		}
	}

	// Fim Get Lista Sintomas -------------------------------------------

}
