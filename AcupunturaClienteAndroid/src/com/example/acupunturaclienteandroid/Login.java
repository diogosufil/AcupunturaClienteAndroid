package com.example.acupunturaclienteandroid;

import java.io.IOException;

import org.json.JSONException;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.acupunturaclienteandroid.webutils.RestClientException;
import com.example.acupunturaclienteandroid.webutils.WebServiceUtils;

public class Login extends Activity {

	private EditText username;
	private EditText password;
	ProgressDialog ringProgressDialog = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);

		username = (EditText) findViewById(R.id.editText_Login_Username);
		password = (EditText) findViewById(R.id.editText_Login_Password);
		Button btnLogin = (Button) findViewById(R.id.buttonLogout);

		btnLogin.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (isNetworkAvailable()) {
					if (username.getText().toString().equals("")
							|| password.getText().toString().equals("")) {
						Toast.makeText(getApplicationContext(),
								"Please insert Username and Password",
								Toast.LENGTH_SHORT).show();

					} else {
						new LogInWeb().execute(username.getText().toString()
								.trim(), password.getText().toString().trim());
					}
				} else {
					Toast.makeText(getApplicationContext(),
							"No Internet connection!\nPlease connect to Internet...",
							Toast.LENGTH_LONG).show();
				}
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.login, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.

		return super.onOptionsItemSelected(item);
	}

	private class LogInWeb extends AsyncTask<String, Void, String> {

		@Override
		protected void onPreExecute() {
			ringProgressDialog = new ProgressDialog(Login.this);
			ringProgressDialog.setIcon(R.drawable.ic_launcher);
			ringProgressDialog.setTitle("Please wait");
			ringProgressDialog.setMessage("Loging in...");

			ringProgressDialog.setCancelable(false);

			ringProgressDialog.show();
		};

		@Override
		protected String doInBackground(String... params) {
			String token = "";

			try {
				token = WebServiceUtils.logIn(params[0], params[1]);
			} catch (IOException e) {
				e.printStackTrace();
			} catch (RestClientException e) {
				e.printStackTrace();
			} catch (JSONException e) {
				e.printStackTrace();
			}

			return token;
		}

		@Override
		protected void onPostExecute(String token) {
			if (token != null) {
				if (!token.isEmpty()) {
					ringProgressDialog.dismiss();
					token = token.replace("\"", "");

					PreferenceManager
							.getDefaultSharedPreferences(
									getApplicationContext()).edit()
							.putString("token", token).commit();
					startActivity(new Intent(getApplicationContext(),
							MainActivity.class));

				}
			} else {
				ringProgressDialog.dismiss();
				Toast.makeText(getApplicationContext(), "Error loging in",
						Toast.LENGTH_SHORT).show();

			}
		}
	}

	private boolean isNetworkAvailable() {
		ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo activeNetworkInfo = connectivityManager
				.getActiveNetworkInfo();
		return activeNetworkInfo != null && activeNetworkInfo.isConnected();
	}
}
