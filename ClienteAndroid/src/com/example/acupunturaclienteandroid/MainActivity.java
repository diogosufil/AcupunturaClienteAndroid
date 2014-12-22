package com.example.acupunturaclienteandroid;

import java.io.IOException;
import java.util.ArrayList;

import org.json.JSONException;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.acupunturaclienteandroid.model.UtilizadorWEB;
import com.example.acupunturaclienteandroid.webutils.RestClientException;
import com.example.acupunturaclienteandroid.webutils.WebServiceUtils;

public class MainActivity extends Activity {

	private String token;
    private ProgressDialog ringProgressDialog;
    private ListView listaUtilizadores;
    private ArrayAdapter<UtilizadorWEB> adaptadorUtilizadores;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		token = PreferenceManager.getDefaultSharedPreferences(this).getString("token", "null");
		TextView texto = (TextView) findViewById(R.id.textView_Main_token);
		listaUtilizadores = (ListView) findViewById(R.id.listView1);
		
		texto.setText(token);
		
		
		Button btnLogin = (Button) findViewById(R.id.button1);
        
        btnLogin.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				new getUtilizadores().execute(token);
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
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
	
	private class getUtilizadores extends AsyncTask<String, Void, ArrayList<UtilizadorWEB>> {
		@Override
        protected void onPreExecute() {

            ringProgressDialog = new ProgressDialog(MainActivity.this);
            ringProgressDialog.setTitle("Aguarde...");
            ringProgressDialog.setMessage("A carregar Dados...");

            // ringProgressDialog = ProgressDialog.show(Login.this,
            // "Please wait ...", "Loging in...", true);
            ringProgressDialog.setCancelable(false);

            ringProgressDialog.show();
        }

        ;

        @Override
        protected ArrayList<UtilizadorWEB> doInBackground(String... params) {
            ArrayList<UtilizadorWEB> lista = null;

            try {
                lista = WebServiceUtils.getAllUtilizadores(token);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (RestClientException e) {
				e.printStackTrace();
			} catch (JSONException e) {
				e.printStackTrace();
			}

            return lista;
        }
        protected void onPostExecute(ArrayList<UtilizadorWEB> lista) {
            if (lista != null) {
                adaptadorUtilizadores = new ArrayAdapter<UtilizadorWEB>(getBaseContext(),
                        android.R.layout.simple_list_item_1, lista);

                listaUtilizadores.setAdapter(adaptadorUtilizadores);

                // "Connex�o Efetuada com Sucesso!");
                Toast.makeText(getApplicationContext(),
                        "Get Utilizadores successful!" + lista.toString(), Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(getApplicationContext(),
                        "Get Utilizadores unsuccessful...", Toast.LENGTH_LONG)
                        .show();

            }
            ringProgressDialog.dismiss();
        }
    }
}
