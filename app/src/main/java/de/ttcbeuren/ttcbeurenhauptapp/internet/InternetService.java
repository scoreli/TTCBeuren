package de.ttcbeuren.ttcbeurenhauptapp.internet;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request.Method;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import de.ttcbeuren.ttcbeurenhauptapp.ConnectionDetector;
import de.ttcbeuren.ttcbeurenhauptapp.ImSpielActivity;
import de.ttcbeuren.ttcbeurenhauptapp.NavigationDrawerFragment;
import de.ttcbeuren.ttcbeurenhauptapp.R;
import de.ttcbeuren.ttcbeurenhauptapp.alertdialogs.AlertFragmentConfirm;
import de.ttcbeuren.ttcbeurenhauptapp.alertdialogs.AlertFragmentNotify;
import de.ttcbeuren.ttcbeurenhauptapp.ergebnisse.ErgebnisseFragment;
import de.ttcbeuren.ttcbeurenhauptapp.spiele.DatabasehandlerSpiele;
import de.ttcbeuren.ttcbeurenhauptapp.spiele.Spiel;

/**
 * Diese Activity regelt die Kommunikation mit dem Internet und schickt und holt
 * die Veranstaltungen usw.
 * 
 * @author philipp
 *
 */
public class InternetService extends Activity {

	private static final String TAGregister = InternetService.class
			.getSimpleName();
	// public SpieleDesUsersActivity desUsers;
	DatabasehandlerSpiele db;
	private static final String TAG_Spiel = "spiel";
	// Für die Veranstaltungen holen
	private static final String TAGholen = NavigationDrawerFragment.class
			.getSimpleName();
	private static final String TAGimSpiel = ImSpielActivity.class
			.getSimpleName();
	// private static final String TAG_VeranstaltungenUserLogcat =
	// SpieleDesUsersActivity.class
	// .getSimpleName();

	private static final String TAG_VeranstaltungenDesUsers = "veranstaltungdesusers";
	private ProgressDialog pDialog;
	Activity hans;
	private ConnectionDetector myConnection;
	// private MainActivityStartseite mainactivity;
	/**
	 * Musste hier eingefügt werden wegen dem zwang zu final
	 */
	boolean uebergabeerfolgreich;
	String meldung = "Aktualisiert";

	/**
	 * Hierbei wird die Activity übergeben und dann dem Attribut der Klasse
	 * Internet Service übergeben. Dies ist nötig, da die Methoden auf die
	 * Activity zugreigen. Und so können sie das.
	 * 
	 * @param servus
	 */
	public InternetService(Activity servus) {
		db = new DatabasehandlerSpiele(servus.getApplicationContext());
		hans = servus;
		myConnection = new ConnectionDetector(servus.getApplicationContext());
		// Progress dialog
		// Geht nicht
		pDialog = new ProgressDialog(servus);
		pDialog.setCancelable(false);
	}

	/**
	 * Diese Methode dient zur Speicherung einer Veranstaltungen online. Dazu
	 * werden die einzelnen Attribute übergeben. Dieses wird mit der Android
	 * Volley Bibliotehk realisiert. Dabei wird mit JSON gearbeitet. Dazu wird
	 * erst ein Anfangstag geschickt. Laut diesem entscheidet der Server welche
	 * Methode/Zweig ausgeführt werden sollen. Dabei sind dann noch weitere Tags
	 * versehen die dann mit den Eigenschaften der neuen Veranstaltungen
	 * versehen sind. Diese werden dann auch geholt. Nach diesem Vorgang wartet
	 * die Methode auf das Rückarray und speichert dieses dann auch in der
	 * internen Datenbank ab. Das heißt es wird nur eins gespeichert wenn die
	 * Speicherung online auch erfolgreich war. Nach diesem Prinzip funktioniert
	 * jede Methode so die mit dem Internet etwas zu tun hat.
	 * */
	public void speichereSpiel(final String spielsystem, final String user_id,
			final String mannschaftsart, final String heimverein,
			final String heimvereinsnummer, final String gastverein,
			final String gastvereinsnummer, final String punkteHeim,
			final String punkteGast, final String spielbeginn,
			final String spielende, final String status, final String istbeendet) {
		if (myConnection.isConnectingToInternet()) {
			// Tag used to cancel the request
			String tag_string_req = "req_speichereSpiel";

			pDialog.setMessage("Erstellen ...");
			showDialog();

			StringRequest strReq = new StringRequest(Method.POST,
					AppConfig.URL_Spiele, new Response.Listener<String>() {

						@Override
						public void onResponse(String response) {
							Log.d(TAGregister,
									"Register Response: " + response.toString());
							hideDialog();
							try {

								JSONObject jObj = new JSONObject(response);
								boolean error = jObj.getBoolean("error");
								if (!error) {
									// User successfully stored in MySQL
									// Now store the user in sqlite
									// String uid = jObj.getString("uid");

									JSONObject user = jObj
											.getJSONObject("spiel");
									String veranstaltung_idj = user
											.getString("veranstaltung_id");
									String benutzer_idj = user
											.getString("benutzer_id");
									String spielsystemj = user
											.getString("spielsystem");
									String mannschaftsartj = user
											.getString("mannschaftsart");
									String heimvereinj = user
											.getString("heimverein");
									String heimvereinsnummerj = user
											.getString("heimvereinsnummer");
									String gastvereinj = user
											.getString("gastverein");
									String gastvereinsnummerj = user
											.getString("gastvereinsnummer");
									String punkteHeimj = user
											.getString("punkteHeim");
									String punkteGastj = user
											.getString("punkteGast");
									String spielbeginnj = user
											.getString("spielbeginn");
									String spielendej = user
											.getString("spielende");
									String statusj = user.getString("status");
									String istbeendetj = user
											.getString("istbeendet");
									db.addSpiel(new Spiel(Integer
											.parseInt(veranstaltung_idj),
											Integer.parseInt(punkteHeimj),
											Integer.parseInt(punkteGastj),
											spielsystemj, mannschaftsartj,
											heimvereinj, heimvereinsnummerj,
											gastvereinj, gastvereinsnummerj,
											statusj, spielbeginnj, spielendej,
											Integer.parseInt(istbeendetj),
											Integer.parseInt(benutzer_idj)));

									/*
									 * // Launch login activity Intent intent =
									 * new Intent( RegisterVeranstaltung.this,
									 * LoginActivity.class);
									 * startActivity(intent); finish();
									 */
									hans.finish();// Hat beendet da man was
													// aufgerufen
													// hat obwohl es beendet
													// worden
													// ist.
								} else {

									// Error occurred in registration. Get the
									// error
									// message
									String errorMsg = jObj
											.getString("error_msg");

									Toast.makeText(
											hans.getApplicationContext(),
											errorMsg, Toast.LENGTH_LONG).show();

								}
							} catch (JSONException e) {
								e.printStackTrace();
								Toast.makeText(hans.getApplicationContext(),
										e.toString(), Toast.LENGTH_LONG).show();
							}
						}

					}, new Response.ErrorListener() {

						@Override
						public void onErrorResponse(VolleyError error) {
							Log.e(TAGregister,
									"Registration Error: " + error.getMessage());

							Toast.makeText(hans.getApplicationContext(),
									error.getMessage() + "hier",
									Toast.LENGTH_LONG).show();
							hideDialog();

						}
					}) {

				@Override
				protected Map<String, String> getParams() {
					// Posting params to register url
					Map<String, String> params = new HashMap<String, String>();
					params.put("tag", "speicherSpiel");// Zuerst Tag dann
					// Daten
					params.put("user_id", user_id);
					params.put("spielsystem", spielsystem);
					params.put("mannschaftsart", mannschaftsart);
					params.put("heimverein", heimverein);
					params.put("heimvereinsnummer", heimvereinsnummer);
					params.put("gastverein", gastverein);
					params.put("gastvereinsnummer", gastvereinsnummer);
					params.put("punkteHeim", punkteHeim);
					params.put("punkteGast", punkteGast);
					params.put("spielbeginn", spielbeginn);
					params.put("spielende", spielende);
					params.put("status", status);
					params.put("istbeendet", istbeendet);

					return params;
				}

			};

			// Adding request to request queue
			AppController.getInstance().addToRequestQueue(strReq,
					tag_string_req);
		} else {
			DialogFragment notifyFragment = new AlertFragmentNotify();
			notifyFragment.show(hans.getFragmentManager(), "notify");

		}
	}

	/**
	 * Die Funktion holt alle Veranstaltungen und speichert diese in die
	 * My_Sqllite Datenbank ab. Dies wird nach der gleichen vorhergehensweise
	 * realisiert wie in registerVeranstaltung(). Da die gleiche Bibliothek
	 * benutzt wird.
	 * */
	public void Spielholen() {
		if (myConnection.isConnectingToInternet()) {
			// Tag used to cancel the request
			String tag_string_req = "req_holen";

			pDialog.setMessage("Aktualisieren ...");
			showDialog();

			StringRequest strReq = new StringRequest(Method.POST,
					AppConfig.URL_Spiele, new Response.Listener<String>() {

						@Override
						public void onResponse(String response) {
							Log.d(TAGholen,
									"Veranstaltung Holen: "
											+ response.toString());
							hideDialog();
							try {
								/*
								 * Toast.makeText(getApplicationContext(),
								 * response.toString(), Toast.LENGTH_SHORT)
								 * .show();
								 */
								JSONObject jObj = new JSONObject(response);
								boolean error = jObj.getBoolean("error");
								// error = false;
								if (!error) {
									// User successfully stored in MySQL
									// Now store the user in sqlite
									db.deleteSpiele();

									JSONArray uebergabe = jObj
											.getJSONArray(TAG_Spiel);
									for (int i = 0; i < uebergabe.length(); i++) {
										JSONObject veranstaltung = uebergabe
												.getJSONObject(i);

										String veranstaltung_idj = veranstaltung
												.getString("veranstaltung_id");
										String benutzer_idj = veranstaltung
												.getString("benutzer_id");
										String spielsystemj = veranstaltung
												.getString("spielsystem");
										String mannschaftsartj = veranstaltung
												.getString("mannschaftsart");
										String heimvereinj = veranstaltung
												.getString("heimverein");
										String heimvereinsnummerj = veranstaltung
												.getString("heimvereinsnummer");
										String gastvereinj = veranstaltung
												.getString("gastverein");
										String gastvereinsnummerj = veranstaltung
												.getString("gastvereinsnummer");
										String punkteHeimj = veranstaltung
												.getString("punkteHeim");
										String punkteGastj = veranstaltung
												.getString("punkteGast");
										String spielbeginnj = veranstaltung
												.getString("spielbeginn");
										String spielendej = veranstaltung
												.getString("spielende");
										String statusj = veranstaltung
												.getString("status");
										String istbeendetj = veranstaltung
												.getString("istbeendet");
										db.addSpiel(new Spiel(Integer
												.parseInt(veranstaltung_idj),
												Integer.parseInt(punkteHeimj),
												Integer.parseInt(punkteGastj),
												spielsystemj, mannschaftsartj,
												heimvereinj,
												heimvereinsnummerj,
												gastvereinj,
												gastvereinsnummerj, statusj,
												spielbeginnj, spielendej,
												Integer.parseInt(istbeendetj),
												Integer.parseInt(benutzer_idj)));

									}

								} else {

									// Error occurred in registration. Get the
									// error
									// message
									// String errorMsg =
									// jObj.getString("error_msg");

									// Toast.makeText(getApplicationContext(),
									// errorMsg, Toast.LENGTH_LONG).show();

								}
							} catch (JSONException e) {
								meldung = "Keine Daten vorhanden";
								e.printStackTrace();
							}
							/**
							 * Hier wird das Fragment neu aufgerufen um so die
							 * Anzeige zu aktualisieren. Wichtig ist hierbei das
							 * man von der Activity den Fragmentmanager aufruft
							 * sonst findet er kein fragment
							 */
							ErgebnisseFragment myFragment = (ErgebnisseFragment) hans
									.getFragmentManager().findFragmentByTag(
											"Ergebnisse_Fragment");

							if (myFragment != null && myFragment.isVisible()) {

								FragmentManager fragmentManager = hans
										.getFragmentManager();

								fragmentManager

										.beginTransaction()
										.replace(
												R.id.container,
												new ErgebnisseFragment()
														.newInstance(2),
												"Ergebnisse_Fragment").commit();
							}
							Toast.makeText(hans.getApplicationContext(),
									meldung,

									Toast.LENGTH_SHORT).show();

						}

					}, new Response.ErrorListener() {

						@Override
						public void onErrorResponse(VolleyError error) {
							Log.e(TAGholen,
									"Holenfehler: " + error.getMessage());

							Toast.makeText(hans.getApplicationContext(),
									error.getMessage(), Toast.LENGTH_LONG)
									.show();
							hideDialog();

						}
					}) {

				@Override
				protected Map<String, String> getParams() {
					// Posting params to register url
					Map<String, String> params = new HashMap<String, String>();
					params.put("tag", "holespiele");
					return params;
				}

			};

			// Adding request to request queue
			AppController.getInstance().addToRequestQueue(strReq,
					tag_string_req);
		} else {

			DialogFragment notifyFragment = new AlertFragmentNotify();
			notifyFragment.show(hans.getFragmentManager(), "notify");
		}
	}

	/**
	 * Bei dieser Methode wird eine Veranstaltung gelöscht. Dabei wird die
	 * veranstaltungs_id und die user_id (uuid) benötigt. Es wird dabei wieder
	 * die Volleybibliothek benutzt.
	 * 
	 * @param veranstaltungs_id
	 * @param benutzer_id
	 */
	public void Spielloeschen(final String veranstaltungs_id,
			final String benutzer_id) {
		if (myConnection.isConnectingToInternet()) {
			// Tag used to cancel the request
			String tag_string_req = "req_loescheSpiel";

			pDialog.setMessage("Löschen ...");
			showDialog();

			StringRequest strReq = new StringRequest(Method.POST,
					AppConfig.URL_Spiele, new Response.Listener<String>() {

						@Override
						public void onResponse(String response) {
							Log.d(TAGimSpiel, "Veranstaltung Response: "
									+ response.toString());
							hideDialog();

							try {

								JSONObject jObj = new JSONObject(response);
								boolean error = jObj.getBoolean("error");

								if (!error) {
									// uebergabeerfolgreich = true;
									Toast.makeText(
											hans.getApplicationContext(),
											"Gelöscht", Toast.LENGTH_SHORT)
											.show();
								} else {
									// uebergabeerfolgreich = false;

									Toast.makeText(
											hans.getApplicationContext(),
											"Löschen fehlgeschlagen",
											Toast.LENGTH_SHORT).show();
								}

							} catch (JSONException e) { // JSON error
								e.printStackTrace();
							}

						}

					}, new Response.ErrorListener() {

						@Override
						public void onErrorResponse(VolleyError error) {
							Log.e(TAGimSpiel,
									"Registration Error: " + error.getMessage());

							Toast.makeText(hans.getApplicationContext(),
									error.getMessage(), Toast.LENGTH_LONG)
									.show();
							hideDialog();

						}
					}) {

				@Override
				protected Map<String, String> getParams() {
					// Posting params to register url
					Map<String, String> params = new HashMap<String, String>();
					params.put("tag", "loeschespiel");
					params.put("veranstaltungs_id", veranstaltungs_id);
					params.put("benutzer_id", benutzer_id);
					return params;
				}

			};

			// Adding request to request queue
			AppController.getInstance().addToRequestQueue(strReq,
					tag_string_req);
			// return uebergabeerfolgreich;
		} else {
			DialogFragment notifyFragment = new AlertFragmentNotify();
			notifyFragment.show(hans.getFragmentManager(), "notify");

		}
	}

	/**
	 * Bei dieser Mehtode wird die Veranstaltung geupdatet. Dabei wird wieder
	 * die Volleybibiliothek benutzt. Aber die geänderte Veranstaltung wird
	 * nicht wieder empfangen .
	 * 
	 * @param punkteHeim
	 * @param punkteGast
	 * @param status
	 * @param veranstaltungs_id
	 */
	public void updateSpiel(final String punkteHeim, final String punkteGast,
			final String status, final String veranstaltungs_id,
			final String user_id, final String istbeendet,
			final String spielende,final boolean castVerbunden) {
		if (myConnection.isConnectingToInternet()) {
			// Tag used to cancel the request
			String tag_string_req = "req_updateveranstaltung";

			pDialog.setMessage("Updaten ...");
			showDialog();

			StringRequest strReq = new StringRequest(Method.POST,
					AppConfig.URL_Spiele, new Response.Listener<String>() {

						@Override
						public void onResponse(String response) {
							Log.d(TAGimSpiel,
									"Register Response: " + response.toString());
							hideDialog();

							try {
								JSONObject jObj = new JSONObject(response);
								boolean error = jObj.getBoolean("error");
								if (!error) {
									JSONObject user = jObj
											.getJSONObject("spiel");
									String veranstaltung_idj = user
											.getString("veranstaltung_id");
									String benutzer_idj = user
											.getString("benutzer_id");
									String spielsystemj = user
											.getString("spielsystem");
									String mannschaftsartj = user
											.getString("mannschaftsart");
									String heimvereinj = user
											.getString("heimverein");
									String heimvereinsnummerj = user
											.getString("heimvereinsnummer");
									String gastvereinj = user
											.getString("gastverein");
									String gastvereinsnummerj = user
											.getString("gastvereinsnummer");
									String punkteHeimj = user
											.getString("punkteHeim");
									String punkteGastj = user
											.getString("punkteGast");
									String spielbeginnj = user
											.getString("spielbeginn");
									String spielendej = user
											.getString("spielende");
									String statusj = user.getString("status");
									String istbeendetj = user
											.getString("istbeendet");
									db.updateSpiel(new Spiel(Integer
											.parseInt(veranstaltung_idj),
											Integer.parseInt(punkteHeimj),
											Integer.parseInt(punkteGastj),
											spielsystemj, mannschaftsartj,
											heimvereinj, heimvereinsnummerj,
											gastvereinj, gastvereinsnummerj,
											statusj, spielbeginnj, spielendej,
											Integer.parseInt(istbeendetj),
											Integer.parseInt(benutzer_idj)));
									Toast.makeText(
											hans.getApplicationContext(),
											"Aktualisiert", Toast.LENGTH_SHORT)
											.show();
									/**
                                     * Hat den Zweck, dass die ImSpielActivity nur beendet wird
									 * falls kein Chromecast verbunden ist.
									 */
									if(!castVerbunden){
										hans.finish();
									}

								} else {

									// Error occurred in registration. Get the
									// error
									// message
									String errorMsg = jObj
											.getString("error_msg");

									Toast.makeText(
											hans.getApplicationContext(),
											errorMsg, Toast.LENGTH_LONG).show();

								}
							} catch (JSONException e) {
								e.printStackTrace();
								Toast.makeText(hans.getApplicationContext(),
										e.toString(), Toast.LENGTH_LONG).show();
							}
						}

					}, new Response.ErrorListener() {

						@Override
						public void onErrorResponse(VolleyError error) {
							Log.e(TAGimSpiel,
									"Registration Error: " + error.getMessage());

							Toast.makeText(hans.getApplicationContext(),
									error.getMessage(), Toast.LENGTH_LONG)
									.show();
							hideDialog();

						}
					}) {

				@Override
				protected Map<String, String> getParams() {
					// Posting params to register url
					Map<String, String> params = new HashMap<String, String>();
					params.put("tag", "updateSpiel");// Zuerst Tag dann
					// Daten
					params.put("benutzer_id", user_id);
					params.put("punkteHeim", punkteHeim);
					params.put("punkteGast", punkteGast);
					params.put("veranstaltungs_id", veranstaltungs_id);
					params.put("status", status);
					params.put("istbeendet", istbeendet);
					params.put("spielende", spielende);
					return params;
				}

			};

			// Adding request to request queue
			AppController.getInstance().addToRequestQueue(strReq,
					tag_string_req);
		} else {
			DialogFragment notifyFragment = new AlertFragmentNotify();
			notifyFragment.show(hans.getFragmentManager(), "notify");

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
	/**
	 * Gelöst durch extra Klasse Musste ausgelagert werden im onclicklistener
	 * hat es die activity beendet
	 * 
	 * Bitte noch ändern mit Fragment und onActivityResult usw. Schmeißt mit dem
	 * eine Exepetion wenn man das Gerät dreht. DialogFragment notifyFragment =
	 * new AlertFragmentNotify(); notifyFragment.show(hans.getFragmentManager(),
	 * "notfiyfragment");
	 */

}
