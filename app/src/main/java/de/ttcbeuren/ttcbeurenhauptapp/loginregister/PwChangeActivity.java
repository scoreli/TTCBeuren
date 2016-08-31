package de.ttcbeuren.ttcbeurenhauptapp.loginregister;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.Request.Method;
import com.android.volley.toolbox.StringRequest;

import de.ttcbeuren.ttcbeurenhauptapp.ConnectionDetector;
import de.ttcbeuren.ttcbeurenhauptapp.R;
import de.ttcbeuren.ttcbeurenhauptapp.R.id;
import de.ttcbeuren.ttcbeurenhauptapp.R.layout;
import de.ttcbeuren.ttcbeurenhauptapp.R.string;
import de.ttcbeuren.ttcbeurenhauptapp.alertdialogs.AlertFragmentNotify;
import de.ttcbeuren.ttcbeurenhauptapp.internet.AppConfig;
import de.ttcbeuren.ttcbeurenhauptapp.internet.AppController;
import android.app.Activity;
import android.app.DialogFragment;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class PwChangeActivity extends Activity {
	private Button btnaendern;
	private EditText altespw, neuespw, neuespwconfirm;
	// LogCat tag
	private static final String TAG = PwChangeActivity.class.getSimpleName();
	/**
	 * Folgende Attribute sind für die Anzeige des Prozesses und für die
	 * Session. Diese wird gestartet wenn sich ein Benutzer anmeldet.
	 */
	private ProgressDialog pDialog;
	private SessionManager session;
	// private SQLiteHandlerLogin dblogin;
	// Fehlt noch muss hinzugefügt werden.
	private DatabasehandlerUUID dbuuid;
	private ConnectionDetector myConnection;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(layout.activity_pw_change);
		init();
		// Progress dialog
		pDialog = new ProgressDialog(this);
		pDialog.setCancelable(false);
		// Session manager
		session = new SessionManager(getApplicationContext());
		dbuuid = new DatabasehandlerUUID(getApplicationContext());
		// Zum Checken der Internetverbindung
		myConnection = new ConnectionDetector(getApplicationContext());
		btnaendern.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				pwchange();

			}

		});
	}

	void init() {
		btnaendern = (Button) findViewById(id.btn_changepw);
		altespw = (EditText) findViewById(id.txf_oldpw);
		neuespw = (EditText) findViewById(id.txf_newpw);
		neuespwconfirm = (EditText) findViewById(id.txf_newpwconfirm);

	}

	void pwchange() {
		// Reset errors.
		altespw.setError(null);
		neuespw.setError(null);
		neuespwconfirm.setError(null);
		String altespassword = altespw.getText().toString();
		String newpassword = neuespw.getText().toString();
		String newpasswordconfirm = neuespwconfirm.getText().toString();

		boolean cancel = false;
		View focusView = null;
		/*
		 * Überprüfe ob das Passwortfeld ausgefüllt ist
		 */
		if (TextUtils.isEmpty(altespassword)) {
			altespw.setError(getString(string.error_field_required));
			focusView = altespw;
			cancel = true;
		}
		if (!isPasswordValid(altespassword)) {
			altespw.setError(getString(string.error_invalid_password));
			focusView = altespw;
			cancel = true;
		}
		// Check for a valid password, if the user entered one.
		if (!TextUtils.isEmpty(newpassword) && !isPasswordValid(newpassword)) {
			neuespw.setError(getString(string.error_invalid_password));
			focusView = neuespw;
			cancel = true;
		}
		/*
		 * Überprüfe ob das Passwortfeld ausgefüllt ist
		 */
		if (TextUtils.isEmpty(newpassword)) {
			neuespw.setError(getString(string.error_field_required));
			focusView = neuespw;
			cancel = true;
		}
		/*
		 * Überprüfe ob das Passwortfeld ausgefüllt ist
		 */
		if (TextUtils.isEmpty(newpasswordconfirm)) {
			neuespwconfirm.setError(getString(string.error_field_required));
			focusView = neuespwconfirm;
			cancel = true;
		}
		/*
		 * Überprüfe ob die Eingegebenen Passwörter übereinstimmen
		 */
		if (!newpassword.equals(newpasswordconfirm)) {
			neuespwconfirm
					.setError(getString(string.error_invalid_password_confirm));
			focusView = neuespwconfirm;
			cancel = true;
		}
		if (cancel) {
			// There was an error; don't attempt login and focus the first
			// form field with an error.
			focusView.requestFocus();
		} else {
			// Show a progress spinner, and kick off a background task to
			// perform the user login attempt.

			/**
			 * In die Datenbank schreiben
			 */
			changepwsend(dbuuid.getBenutzer().getUuid(), altespassword,
					newpassword);

		}

	}

	/**
	 * 
	 * Hierbei wird das Passwort und die Email mit der My SQL Datenbank
	 * verglichen. Dabei wird die Volley Libary Benutzt. Hierbei wird ein PHP
	 * Script auf dem Server aberufen. Das dann mittels JSON die Abfragen regelt
	 * und diese auch so codiert wieder zurückgibt.Dieses funktioniert mit
	 * sogenannten Tags. Nach dem Loginvorgang wird dann diese Activity beendet
	 * und die Main gestartet.
	 * 
	 * @param email
	 * @param password
	 * @return
	 */
	private void changepwsend(final String uuid, final String oldpassword,
			final String newpassword) {
		if (myConnection.isConnectingToInternet()) {
			// Tag used to cancel the request
			String tag_string_req = "req_login";

			pDialog.setMessage("Ändere PW ...");
			showDialog();
			// Post geht hier irgendwie nicht
			StringRequest strReq = new StringRequest(Method.POST,
					AppConfig.URL_LOGIN, new Response.Listener<String>() {

						@Override
						public void onResponse(String response) {
							Log.d(TAG,
									"PW Change Response: "
											+ response.toString());
							hideDialog();

							try {
								JSONObject jObj = new JSONObject(response);
								boolean error = jObj.getBoolean("error");

								// Check for error node in json
								if (!error) {
									Log.e(TAG, "PW Success: "
											+ "Passwort wurde erfolgreich geändert!");
									Toast.makeText(getApplicationContext(),
											"Passwort wurde erfolgreich geändert!",
											Toast.LENGTH_LONG).show();
									logoutUserandBack();
									
								} else {
									Log.e(TAG, "PW Error: " + "Passwort Falsch");
									Toast.makeText(getApplicationContext(),
											"Fehlgeschlagen:Passwort falsch",
											Toast.LENGTH_LONG).show();
								}

							} catch (JSONException e) {
								// JSON error
								e.printStackTrace();
							}

						}
					}, new Response.ErrorListener() {

						@Override
						public void onErrorResponse(VolleyError error) {
							Log.e(TAG, "Login Error: " + error.getMessage());
							Toast.makeText(getApplicationContext(),
									error.getMessage(), Toast.LENGTH_LONG)
									.show();
							hideDialog();
						}
					}) {

				@Override
				protected Map<String, String> getParams() {
					// Posting parameters to login url
					Map<String, String> params = new HashMap<String, String>();
					params.put("tag", "passwortaendern");
					params.put("uid", uuid);
					params.put("password", oldpassword);
					params.put("newpassword", newpassword);

					return params;
				}

			};

			// Adding request to request queue
			try {
				AppController.getInstance().addToRequestQueue(strReq,
						tag_string_req);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				Toast.makeText(getApplicationContext(), e.toString(),
						Toast.LENGTH_SHORT).show();
			}
		} else {
			DialogFragment notifyFragment = new AlertFragmentNotify();
			notifyFragment.show(getFragmentManager(), "notify");
		}

	}

	private void showDialog() {
		if (!pDialog.isShowing())
			pDialog.show();
	}

	private void hideDialog() {
		if (pDialog.isShowing())
			pDialog.dismiss();
	}

	private boolean isPasswordValid(String password) {
		// TODO: Replace this with your own logic
		return password.length() > 4;
	}

	private void logoutUserandBack() {
		session.setLogin(false);
		dbuuid.deleteUsers(); // dblogin.deleteUsers();

		// Launching the login activity
		Intent intent = new Intent(PwChangeActivity.this, LoginActivity.class);
		startActivity(intent);
		finish();
	}
}
