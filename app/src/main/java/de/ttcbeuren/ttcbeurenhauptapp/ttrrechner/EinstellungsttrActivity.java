package de.ttcbeuren.ttcbeurenhauptapp.ttrrechner;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.widget.Toast;
import de.ttcbeuren.ttcbeurenhauptapp.R;

/**
 * Hier wird die Aenderungskonstante festgelegt und erfasst. Dabei wird auf
 * Preferencen zugegriffen die man vorher als Einträge in einer XML Datei
 * festlegt.
 * 
 * @author philipp
 *
 */
public class EinstellungsttrActivity extends PreferenceActivity implements
		OnPreferenceChangeListener {
	int anzahlgegner = 1;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		addPreferencesFromResource(R.xml.preferences_aenderungskonstante);
		/**
		 * Bitte noch verbessern aber Der eigenstellte wert wird als summary
		 * ausgegeben und von den SharedPreferences geholt. Verbesserung das
		 * beim Auswählen der richtige Wert angezeigt wird.(Der vorher ausgwählt wurde)
		 */
		String[] uebergabe = getResources().getStringArray(
				R.array.PreferencesKeys);
		Preference findPreference = findPreference(uebergabe[5]);
		findPreference.setOnPreferenceChangeListener(this);
		SharedPreferences prefs = getSharedPreferences("Zwischenwerte",
				MODE_PRIVATE);
		findPreference.setSummary("" + prefs.getInt("AnzahlderGegner", 3));
	}

	@Override
	protected void onStop() {
		int aenderungskonstante = 16;
		Object[] arrayb = new Object[6];
		SharedPreferences sPrefs = PreferenceManager
				.getDefaultSharedPreferences(this);
		// Auslesen des Anzeige-Modus aus den SharedPreferences

		arrayb[0] = sPrefs.getBoolean(
				getString(R.string.preference_ptext_unter16_key), false);
		arrayb[1] = sPrefs.getBoolean(
				getString(R.string.preference_ptext_unter21_key), false);
		arrayb[2] = sPrefs.getBoolean(
				getString(R.string.preference_ptext_unter1jahrkein_key), false);
		arrayb[3] = sPrefs.getBoolean(
				getString(R.string.preference_ptext_wenigerals30_key), false);
		arrayb[4] = sPrefs.getBoolean(
				getString(R.string.preference_ptext_nachwuchsausgleich_key),
				false);
		// arrayb[5] = sPrefs.get(
		// getString(R.string.preference_plist_anzahlgegner_key), false);

		/**
		 * Hier wird geprüft wie sich die Änderungskonstante sich zusammensetzt.
		 */
		for (int i = 0; i < arrayb.length; i++) {
			if (i < 5) {
				if ((boolean) arrayb[i]) {
					if (i < 4) {
						aenderungskonstante = aenderungskonstante + 4;
					} else {
						aenderungskonstante = aenderungskonstante + 2;
					}
				}
			} else {
				// Toast.makeText(getApplicationContext(), arrayb[5].toString(),
				// Toast.LENGTH_SHORT).show();
			}
		}
		// Hier wird die Aenderungskonstante zwischengespeichert. Diese ist in
		// einer XML Datei dann abgelegt.
		SharedPreferences prefs = getSharedPreferences("Zwischenwerte",
				MODE_PRIVATE);
		prefs.edit().putInt("Aenderungskonstante", aenderungskonstante).apply();
		super.onStop();
	}

	@Override
	public boolean onPreferenceChange(Preference preference, Object newValue) {
		/**
		 * Hat keine Funktion soll mal verbessert werden
		 */
		/*
		 * preference.setDefaultValue(newValue); if (null !=
		 * getPreferenceScreen().findPreference(
		 * getString(R.string.preference_plist_anzahlgegner_key))) {
		 * preference.setSummary(newValue.toString()); }
		 */
		String uebergabe = (String) newValue;
		preference.setSummary((String) newValue);
		preference.setDefaultValue(newValue);
		/**
		 * Wird benutzt um den Fokus auf den ausgewählten zu legen in Listpreference
		 */
		((ListPreference) preference).setValueIndex(Integer.parseInt(uebergabe)-1);
		SharedPreferences prefs = getSharedPreferences("Zwischenwerte",
				MODE_PRIVATE);
		prefs.edit().putInt("AnzahlderGegner", Integer.parseInt(uebergabe))
				.apply();

		return false;
	}

}
