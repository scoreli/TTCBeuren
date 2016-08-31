package de.ttcbeuren.ttcbeurenhauptapp.ergebnisse;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.List;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.CursorJoiner.Result;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;
import de.ttcbeuren.ttcbeurenhauptapp.ImSpielActivity;
import de.ttcbeuren.ttcbeurenhauptapp.MainActivityStartseite;
import de.ttcbeuren.ttcbeurenhauptapp.R;
import de.ttcbeuren.ttcbeurenhauptapp.detailspiel.DetailActivity;
import de.ttcbeuren.ttcbeurenhauptapp.loginregister.DatabasehandlerUUID;
import de.ttcbeuren.ttcbeurenhauptapp.loginregister.SessionManager;
import de.ttcbeuren.ttcbeurenhauptapp.spiele.DatabasehandlerSpiele;
import de.ttcbeuren.ttcbeurenhauptapp.spiele.Spiel;

public class ErgebnisseFragment extends Fragment implements
		OnItemSelectedListener {
	List<Spiel> spielelive, spielebeendet;
	ListView listview;
	Spinner auswahlspinner;
	DatabasehandlerSpiele dbspiele;
	DatabasehandlerUUID dbuuid;
	SessionManager session;
	public static final String KEY = "Spieluebergabe";
	private static final String ARG_SECTION_NUMBER = "section_number";

	// private MyArrayAdapter adapterlive,adapterbeendet;
	private ListViewadapter mAdapter;

	// WebView browser = (WebView) root.findViewById(R.id.webviewakuelles);
	// browser.loadUrl("http://www.ttc-beuren.de");
	public ErgebnisseFragment newInstance(int sectionNumber) {
		ErgebnisseFragment fragment = new ErgebnisseFragment();
		Bundle args = new Bundle();
		args.putInt(ARG_SECTION_NUMBER, sectionNumber);
		fragment.setArguments(args);
		return fragment;

	}

	@Override
	public void onAttach(Activity activity) {
		// Wichtig hier muss die Section angegeben werden ???
		super.onAttach(activity);
		((MainActivityStartseite) activity).onSectionAttached(1);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// setHasOptionsMenu(true);
		mAdapter = new ListViewadapter(getActivity());
		dbspiele = new DatabasehandlerSpiele(getActivity());
		dbuuid = new DatabasehandlerUUID(getActivity());
		session = new SessionManager(getActivity());
		View root = inflater.inflate(R.layout.fragment_ergebnisse, container,
				false);
		auswahlspinner = (Spinner) root.findViewById(R.id.spin_Mannschaften);
		listview = (ListView) root.findViewById(R.id.listV_ergebnisse);

		ArrayAdapter<String> Auswahlspinneradapter = new ArrayAdapter<String>(
				getActivity(), android.R.layout.simple_spinner_dropdown_item,
				getResources().getStringArray(R.array.Mannschaftsartenduenn));
		auswahlspinner.setAdapter(Auswahlspinneradapter);
		auswahlspinner.setOnItemSelectedListener(this);

		listview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				/**
				 * Die 1 ist wichtig damit nicht die Überschriften genommen
				 * werden
				 */
				if (mAdapter.getItemViewType(position) != 1) {

					Spiel uebergabespiel = (Spiel) listview
							.getItemAtPosition(position);
					/**
					 * Wird benötigt da sonst die dbuuid aufgerufen wird und
					 * wenn dort nichts drin ist dann schmeißt es eine exe
					 * 
					 * Vll noch besser lösen
					 */
					Intent i = null;
					if (session.isLoggedIn()) {
						if (uebergabespiel.getBenutzer_id() == dbuuid
								.getBenutzer().get_id()
								&& uebergabespiel.getIstspielbeendet() == 0) {
							i = new Intent(getActivity(), ImSpielActivity.class);
						} else {
							i = new Intent(getActivity(), DetailActivity.class);
						}
					} else {
						i = new Intent(getActivity(), DetailActivity.class);
					}

					i.putExtra(KEY, uebergabespiel.getSpiel_id());
					startActivity(i);
				}

			}
		});
		updateListe("Alle Spiele");

		return root;

	}

	@Override
	public void onResume() {
		/**
		 * http://developer.android.com/training/basics/data-storage/shared-
		 * preferences.html
		 */
		super.onResume();

		SharedPreferences sharedPref = getActivity().getPreferences(
				Context.MODE_PRIVATE);
		updateListe(sharedPref.getString(
				getString(R.string.preference_aktualisieren), ""));
	}

	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int position,
			long id) {
		String NameDerMannschaft = (String) parent.getItemAtPosition(position);
		updateListe(NameDerMannschaft);
		/*
		 * SharedPreferences sPrefs = getActivity().getSharedPreferences(
		 * getString(R.string.preference_aktualisieren), Context.MODE_APPEND);
		 */
		SharedPreferences sharedPref = getActivity().getPreferences(
				Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = sharedPref.edit();
		editor.putString(getString(R.string.preference_aktualisieren),
				NameDerMannschaft);
		editor.commit();
		updateListe(NameDerMannschaft);

	}

	public void updateListe(String NameDerMannschaft) {
		/**
		 * Leider funktioniert die Sql abfrage nicht richtig irgenwie.Deswegen
		 * nur verdünnt
		 */
		switch (NameDerMannschaft) {
		case "Alle Spiele":
			spielelive = dbspiele.getAllLiveSpiele();
			spielebeendet = dbspiele.getAllBeendetSpiele();
			break;
		/*
		 * case "1.Herren": spielelive = dbspiele.getVariableSpiele(
		 * getString(R.string.mannschaftsart_herren),
		 * getString(R.string.mannschaftsnummer_1), 0); spielebeendet =
		 * dbspiele.getVariableSpiele(
		 * getString(R.string.mannschaftsart_herren),
		 * getString(R.string.mannschaftsnummer_1), 1); break; case "2.Herren":
		 * spielelive = dbspiele.getVariableSpiele(
		 * getString(R.string.mannschaftsart_herren),
		 * getString(R.string.mannschaftsnummer_2), 0); spielebeendet =
		 * dbspiele.getVariableSpiele(
		 * getString(R.string.mannschaftsart_herren),
		 * getString(R.string.mannschaftsnummer_2), 1); break; case "3.Herren":
		 * spielelive = dbspiele.getVariableSpiele(
		 * getString(R.string.mannschaftsart_herren),
		 * getString(R.string.mannschaftsnummer_3), 0); spielebeendet =
		 * dbspiele.getVariableSpiele(
		 * getString(R.string.mannschaftsart_herren),
		 * getString(R.string.mannschaftsnummer_3), 1); break; case "4.Herren":
		 * spielelive = dbspiele.getVariableSpiele(
		 * getString(R.string.mannschaftsart_herren),
		 * getString(R.string.mannschaftsnummer_4), 0); spielebeendet =
		 * dbspiele.getVariableSpiele(
		 * getString(R.string.mannschaftsart_herren),
		 * getString(R.string.mannschaftsnummer_4), 1); break; case "1.Damen":
		 * Toast.makeText(getActivity(), "1.Damen", Toast.LENGTH_SHORT).show();
		 * spielelive = dbspiele.getVariableSpiele(
		 * getString(R.string.mannschaftsart_damen),
		 * getString(R.string.mannschaftsnummer_1), 0); spielebeendet =
		 * dbspiele.getVariableSpiele( getString(R.string.mannschaftsart_damen),
		 * getString(R.string.mannschaftsnummer_1), 1); break; case "1.Jugend":
		 * spielelive = dbspiele.getVariableSpiele(
		 * getString(R.string.mannschaftsart_jugend),
		 * getString(R.string.mannschaftsnummer_1), 0); spielebeendet =
		 * dbspiele.getVariableSpiele(
		 * getString(R.string.mannschaftsart_jugend),
		 * getString(R.string.mannschaftsnummer_1), 1); break; case "2.Jugend":
		 * spielelive = dbspiele.getVariableSpiele(
		 * getString(R.string.mannschaftsart_jugend),
		 * getString(R.string.mannschaftsnummer_2), 0); spielebeendet =
		 * dbspiele.getVariableSpiele(
		 * getString(R.string.mannschaftsart_jugend),
		 * getString(R.string.mannschaftsnummer_2), 1); break; case "3.Jugend":
		 * spielelive = dbspiele.getVariableSpiele(
		 * getString(R.string.mannschaftsart_jugend),
		 * getString(R.string.mannschaftsnummer_3), 0); spielebeendet =
		 * dbspiele.getVariableSpiele(
		 * getString(R.string.mannschaftsart_jugend),
		 * getString(R.string.mannschaftsnummer_3), 1); break; case "4.Jugend":
		 * spielelive = dbspiele.getVariableSpiele(
		 * getString(R.string.mannschaftsart_jugend),
		 * getString(R.string.mannschaftsnummer_4), 0); spielebeendet =
		 * dbspiele.getVariableSpiele(
		 * getString(R.string.mannschaftsart_jugend),
		 * getString(R.string.mannschaftsnummer_4), 1); break; case "5.Jugend":
		 * spielelive = dbspiele.getVariableSpiele(
		 * getString(R.string.mannschaftsart_jugend),
		 * getString(R.string.mannschaftsnummer_5), 0); spielebeendet =
		 * dbspiele.getVariableSpiele(
		 * getString(R.string.mannschaftsart_jugend),
		 * getString(R.string.mannschaftsnummer_5), 1); break; case "6.Jugend":
		 * spielelive = dbspiele.getVariableSpiele(
		 * getString(R.string.mannschaftsart_jugend),
		 * getString(R.string.mannschaftsnummer_6), 0); spielebeendet =
		 * dbspiele.getVariableSpiele(
		 * getString(R.string.mannschaftsart_jugend),
		 * getString(R.string.mannschaftsnummer_6), 1); break;
		 */
		case "Herren":
			spielelive = dbspiele.getVariableSpiele(
					getString(R.string.mannschaftsart_herren), 0);
			spielebeendet = dbspiele.getVariableSpiele(
					getString(R.string.mannschaftsart_herren), 1);
			break;
		case "Damen":
			spielelive = dbspiele.getVariableSpiele(
					getString(R.string.mannschaftsart_damen), 0);
			spielebeendet = dbspiele.getVariableSpiele(
					getString(R.string.mannschaftsart_damen), 1);
			break;
		case "Jugend U18":
			spielelive = dbspiele.getVariableSpiele(
					getString(R.string.mannschaftsart_jugend), 0);
			spielebeendet = dbspiele.getVariableSpiele(
					getString(R.string.mannschaftsart_jugend), 1);
			break;

		}
		mAdapter.deletelist();
		/**
		 * Damit werden eigene Spiele anderst gefärbt.
		 */
		if (session.isLoggedIn()) {
			mAdapter.setBenutzer_id(dbuuid.getBenutzer().get_id());
		}

		mAdapter.addSectionHeaderItem("Live :");
		for (int i = 0; i < spielelive.size(); i++) {
			mAdapter.addItem(spielelive.get(i));

		}
		mAdapter.addSectionHeaderItem("Beendet :");
		for (int i = 0; i < spielebeendet.size(); i++) {
			mAdapter.addItem(spielebeendet.get(i));
		}
		spielelive.clear();
		spielebeendet.clear();
		listview.setAdapter(mAdapter);

	}

	/**
	 * Diese Methode serialisiert ein beliebiges Objekt,damit es über Bluetooth
	 * oder zwischen Activitys übergeben werden kann. In unserem Fall ist es ein
	 * Veranstaltungsobjekt.
	 * 
	 * @param obj
	 * @return
	 * @throws IOException
	 */
	public static byte[] serialize(Object obj) throws IOException {
		ByteArrayOutputStream b = new ByteArrayOutputStream();
		ObjectOutputStream o = new ObjectOutputStream(b);
		o.writeObject(obj);
		return b.toByteArray();
	}

	@Override
	public void onNothingSelected(AdapterView<?> parent) {
		// TODO Auto-generated method stub

	}
	/**
	 * Muss noch verbessert werden
	 */
	/*
	 * public class ListAktualisierenTask extends AsyncTask<String, Void,
	 * List<Spiel>> {
	 * 
	 * @Override protected List<Spiel> doInBackground(String... params) {
	 * 
	 * 
	 * return null;
	 * 
	 * }
	 * 
	 * @Override protected void onPostExecute(List<Spiel> result) { // TODO
	 * Auto-generated method stub
	 * 
	 * System.out.println("had");
	 * 
	 * 
	 * 
	 * }
	 * 
	 * }
	 */

}
