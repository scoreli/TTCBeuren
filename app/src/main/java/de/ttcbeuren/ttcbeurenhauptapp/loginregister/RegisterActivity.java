package de.ttcbeuren.ttcbeurenhauptapp.loginregister;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DialogFragment;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request.Method;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import de.ttcbeuren.ttcbeurenhauptapp.ConnectionDetector;
import de.ttcbeuren.ttcbeurenhauptapp.MainActivityStartseite;
import de.ttcbeuren.ttcbeurenhauptapp.R;
import de.ttcbeuren.ttcbeurenhauptapp.alertdialogs.AlertFragmentNotify;
import de.ttcbeuren.ttcbeurenhauptapp.internet.AppConfig;
import de.ttcbeuren.ttcbeurenhauptapp.internet.AppController;

public class RegisterActivity extends Activity {

	private static final String TAG = RegisterActivity.class.getSimpleName();
	private ProgressDialog pDialog;
	private SessionManager session;
	// private SQLiteHandlerLogin dblogin;

	/**
	 * Keep track of the login task to ensure we can cancel it if requested.
	 */

	// DatabasehandlerMitglieder db = new DatabasehandlerMitglieder(this);
	// UI references.
	private EditText mEmailViewR;
	private EditText mPasswordViewR, mPasswordViewConfirmR, mVorname,
			mNachname, mRegistercode;
	private View mProgressViewR;
	private View mLoginFormViewR;
	private ConnectionDetector myConnection;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register);

		// Progress dialog
		pDialog = new ProgressDialog(this);
		pDialog.setCancelable(false);
		// Zum Checken der Internetverbindung
		myConnection = new ConnectionDetector(getApplicationContext());
		// Session manager
		session = new SessionManager(getApplicationContext());

		// SQLite database handler
		// dblogin = new SQLiteHandlerLogin(getApplicationContext());

		// Check if user is already logged in or not
		if (session.isLoggedIn()) {
			// User is already logged in. Take him to main activity
			Intent intent = new Intent(RegisterActivity.this,
					MainActivityStartseite.class);
			startActivity(intent);
			finish();
		}

		// Set up the login form.
		mEmailViewR = (EditText) findViewById(R.id.emailregister);

		mPasswordViewR = (EditText) findViewById(R.id.passwordregister);
		mPasswordViewR
				.setOnEditorActionListener(new TextView.OnEditorActionListener() {
					@Override
					public boolean onEditorAction(TextView textView, int id,
							KeyEvent keyEvent) {
						if (id == R.id.login || id == EditorInfo.IME_NULL) {
							attemptRegister();
							return true;
						}
						return false;
					}
				});

		mPasswordViewConfirmR = (EditText) findViewById(R.id.passwordregisterueberpruefen);
		mPasswordViewConfirmR
				.setOnEditorActionListener(new TextView.OnEditorActionListener() {
					@Override
					public boolean onEditorAction(TextView textView, int id,
							KeyEvent keyEvent) {
						if (id == R.id.login || id == EditorInfo.IME_NULL) {
							attemptRegister();
							return true;
						}
						return false;
					}
				});

		mVorname = (EditText) findViewById(R.id.txf_vorname);
		mVorname.requestFocus();
		mVorname.setOnEditorActionListener(new TextView.OnEditorActionListener() {
			@Override
			public boolean onEditorAction(TextView textView, int id,
					KeyEvent keyEvent) {
				if (id == R.id.login || id == EditorInfo.IME_NULL) {
					attemptRegister();
					return true;
				}
				return false;
			}
		});
		mNachname = (EditText) findViewById(R.id.txf_nachname);
		mNachname
				.setOnEditorActionListener(new TextView.OnEditorActionListener() {
					@Override
					public boolean onEditorAction(TextView textView, int id,
							KeyEvent keyEvent) {
						if (id == R.id.login || id == EditorInfo.IME_NULL) {
							attemptRegister();
							return true;
						}
						return false;
					}
				});
		mRegistercode = (EditText) findViewById(R.id.txf_registrierungscode);
		mRegistercode
				.setOnEditorActionListener(new TextView.OnEditorActionListener() {
					@Override
					public boolean onEditorAction(TextView textView, int id,
							KeyEvent keyEvent) {
						if (id == R.id.login || id == EditorInfo.IME_NULL) {
							attemptRegister();
							return true;
						}
						return false;
					}
				});
		Button mEmailSignInButton = (Button) findViewById(R.id.btn_registrieren);
		mEmailSignInButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				attemptRegister();
			}
		});

		mLoginFormViewR = findViewById(R.id.register_form);
		mProgressViewR = findViewById(R.id.register_progress);
	}

	/**
	 * Attempts to sign in or register the account specified by the login form.
	 * If there are form errors (invalid email, missing fields, etc.), the
	 * errors are presented and no actual login attempt is made.
	 */
	public void attemptRegister() {

		// Reset errors.
		mEmailViewR.setError(null);
		mPasswordViewR.setError(null);
		mPasswordViewConfirmR.setError(null);
		mVorname.setError(null);
		mNachname.setError(null);
		mRegistercode.setError(null);
		// Store values at the time of the login attempt.
		String email = mEmailViewR.getText().toString();
		String password = mPasswordViewR.getText().toString();
		String passwordconfirm = mPasswordViewConfirmR.getText().toString();
		String vorname = mVorname.getText().toString();
		String nachname = mNachname.getText().toString();
		String regstriercode = mRegistercode.getText().toString();

		boolean cancel = false;
		View focusView = null;

		// Check for a valid password, if the user entered one.
		if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
			mPasswordViewR.setError(getString(R.string.error_invalid_password));
			focusView = mPasswordViewR;
			cancel = true;
		}
		/*
		 * Überprüfe ob das Passwortfeld ausgefüllt ist
		 */
		if (TextUtils.isEmpty(password)) {
			mPasswordViewR.setError(getString(R.string.error_field_required));
			focusView = mPasswordViewR;
			cancel = true;
		}
		/*
		 * Überprüfe ob die Eingegebenen Passwörter übereinstimmen
		 */
		if (!password.equals(passwordconfirm)) {
			mPasswordViewConfirmR
					.setError(getString(R.string.error_invalid_password_confirm));
			focusView = mPasswordViewConfirmR;
			cancel = true;
		}
		// Check for a valid email address.
		if (TextUtils.isEmpty(email)) {
			mEmailViewR.setError(getString(R.string.error_field_required));
			focusView = mEmailViewR;
			cancel = true;
		} else if (!isEmailValid(email)) {
			mEmailViewR.setError(getString(R.string.error_invalid_email));
			focusView = mEmailViewR;
			cancel = true;
		}
		/*
		 * Überprüfe ob das Vornamefeld ausgefüllt ist
		 */
		if (TextUtils.isEmpty(vorname)) {
			mVorname.setError(getString(R.string.error_field_required));
			focusView = mVorname;
			cancel = true;
		} else if (!isVornameValid(vorname)) {
			mVorname.setError(getString(R.string.error_invalid_vorname));
			focusView = mVorname;
			cancel = true;
		}
		if (TextUtils.isEmpty(nachname)) {
			mNachname.setError(getString(R.string.error_field_required));
			focusView = mNachname;
			cancel = true;
		} else if (!isNachnameValid(nachname)) {
			mNachname.setError(getString(R.string.error_invalid_name));
			focusView = mNachname;
			cancel = true;
		}
		if (TextUtils.isEmpty(regstriercode)) {
			mRegistercode.setError(getString(R.string.error_field_required));
			focusView = mRegistercode;
			cancel = true;
		} else if (!isRegistercodeValid(regstriercode)) {
			mRegistercode
					.setError(getString(R.string.error_field_registercode));
			focusView = mRegistercode;
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
			registerUser(nachname, vorname, email, password, regstriercode);

		}
	}

	private boolean isRegistercodeValid(String nachname) {
		// TODO Auto-generated method stub
		return true;
	}

	private boolean isNachnameValid(String nachname) {
		// TODO Auto-generated method stub
		return true;
	}

	private boolean isVornameValid(String vornamenachname) {

		return true;
	}

	private boolean isEmailValid(String email) {
		// TODO: Replace this with your own logic
		return email.contains("@");
	}

	private boolean isPasswordValid(String password) {
		// TODO: Replace this with your own logic
		return password.length() > 4;
	}

	/**
	 * Hierbei wird das Passwort,Email und der Name in die My SQL Datenbank
	 * geschrieben. Dabei wird die Volley Libary Benutzt. Hierbei wird ein PHP
	 * Script auf dem Server aberufen. Das dann mittels JSON die Abfragen regelt
	 * und diese auch so codiert wieder zurückgibt. Dieses funktioniert mit
	 * sogenannten Tags. Nach dem Loginvorgang wird dann diese Activity beendet
	 * und die Main gestartet.War dieser Vorgang erfolgreich wird dieses User
	 * zurückgegeben und auf der Lokalen Datenbank gespeichert.
	 * 
	 * @param name
	 * @param email
	 * @param password
	 */
	private void registerUser(final String name, final String vorname,
			final String email, final String password,
			final String registrierungscode) {
		if (myConnection.isConnectingToInternet()) {
			// Tag used to cancel the request
			String tag_string_req = "req_register";

			pDialog.setMessage("Registrieren ...");
			showDialog();

			StringRequest strReq = new StringRequest(Method.POST,
					AppConfig.URL_REGISTER, new Response.Listener<String>() {

						@Override
						public void onResponse(String response) {
							Log.d(TAG,
									"Register Response: " + response.toString());
							hideDialog();

							try {
								JSONObject jObj = new JSONObject(response);
								boolean error = jObj.getBoolean("error");
								if (!error) {
									// User successfully stored in MySQL
									// Now store the user in sqlite
									String uid = jObj.getString("uid");

									JSONObject user = jObj
											.getJSONObject("user");
									String name = user.getString("name");
									String email = user.getString("email");
									String created_at = user
											.getString("created_at");

									// Inserting row in users table
									// dblogin.addUser(name, email, uid,
									// created_at);

									// Launch login activity
									Intent intent = new Intent(
											RegisterActivity.this,
											LoginActivity.class);
									startActivity(intent);
									finish();
								} else {

									// Error occurred in registration. Get the
									// error
									// message
									String errorMsg = jObj
											.getString("error_msg");
									Toast.makeText(getApplicationContext(),
											errorMsg, Toast.LENGTH_LONG).show();
								}
							} catch (JSONException e) {
								e.printStackTrace();
							}

						}
					}, new Response.ErrorListener() {

						@Override
						public void onErrorResponse(VolleyError error) {
							Log.e(TAG,
									"Registration Error: " + error.getMessage());
							Toast.makeText(getApplicationContext(),
									error.getMessage(), Toast.LENGTH_LONG)
									.show();
							hideDialog();
						}
					}) {

				@Override
				protected Map<String, String> getParams() {
					// Posting params to register url
					Map<String, String> params = new HashMap<String, String>();
					params.put("tag", "register");// Zuerst Tag dann Daten
					params.put("name", name);
					params.put("vorname", vorname);
					params.put("email", email);
					params.put("password", password);
					params.put("registrierungscode", registrierungscode);

					return params;
				}

			};

			// Adding request to request queue
			AppController.getInstance().addToRequestQueue(strReq,
					tag_string_req);
		} else {
			DialogFragment notifyFragment = new AlertFragmentNotify();
			notifyFragment.show(getFragmentManager(),
					"notify");
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
}
