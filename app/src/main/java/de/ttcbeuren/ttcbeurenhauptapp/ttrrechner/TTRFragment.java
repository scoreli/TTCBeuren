package de.ttcbeuren.ttcbeurenhauptapp.ttrrechner;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;
import de.ttcbeuren.ttcbeurenhauptapp.MainActivityStartseite;
import de.ttcbeuren.ttcbeurenhauptapp.R;

public class TTRFragment extends Fragment {
	private EditText ttra, ttrb0, ttrb1, ttrb2, ttrb3, ttrb4, ttrb5, ttrb6,
			ttrb7, ttrb8, ttrb9;

	private CheckBox check_b0, check_b1, check_b2, check_b3, check_b4,
			check_b5, check_b6, check_b7, check_b8, check_b9;
	EditText[] edittextboxen;
	CheckBox[] checkboxen;
	private Button btnberechnen;
	private static final String ARG_SECTION_NUMBER = "section_number";
	MainActivityStartseite mainac;

	// public int AENDERUNGSKONSTANTE = 16;

	public TTRFragment newInstance(int sectionNumber) {
		TTRFragment fragment = new TTRFragment();
		Bundle args = new Bundle();
		args.putInt(ARG_SECTION_NUMBER, sectionNumber);
		fragment.setArguments(args);

		return fragment;

	}

	@Override
	public void onAttach(Activity activity) {
		// Wichtig hier muss die Section angegeben werden ???
		super.onAttach(activity);
		((MainActivityStartseite) activity).onSectionAttached(2);
	}

	/*
	 * @Override public void onSaveInstanceState(Bundle outState) {
	 * super.onSaveInstanceState(outState); outState.putString("text", "" +
	 * ttra.getText()); }
	 * 
	 * @Override public void onActivityCreated(Bundle savedInstanceState) { //
	 * TODO Auto-generated method stub
	 * super.onActivityCreated(savedInstanceState); if (savedInstanceState !=
	 * null) { ttra.setText(savedInstanceState.getString("text")); } }
	 */
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		setHasOptionsMenu(true);
		View root = inflater.inflate(R.layout.fragment_ttrrechner, container,
				false);
		init(root);
		final EditText[] edittextboxen = { ttrb0, ttrb1, ttrb2, ttrb3, ttrb4,
				ttrb5, ttrb6, ttrb7, ttrb8, ttrb9 };
		final CheckBox[] checkboxen = { check_b0, check_b1, check_b2, check_b3,
				check_b4, check_b5, check_b6, check_b7, check_b8, check_b9 };

		btnberechnen = (Button) root.findViewById(R.id.btn_berechnen);

		// TODO Auto-generated method stub

		btnberechnen.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				ttrberechnen(edittextboxen, checkboxen);
			}
		});
		return root;
	}

	/**
	 * Hier wird festgelegt wie viele Gegner angezeigt werden.
	 */
	@Override
	public void onStart() {
		// TODO Auto-generated method stub
		super.onStart();

		View root = getView();
		init(root);
		SharedPreferences prefs = this.getActivity().getSharedPreferences(
				"Zwischenwerte", Context.MODE_PRIVATE);
		int uebergabeanzahl = prefs.getInt("AnzahlderGegner", 3);
		for (int i = uebergabeanzahl; i < 10; i++) {
			EditText uebergabeedit = (EditText) root
					.findViewById(getResources().getIdentifier("etxt_ttrb" + i,
							"id", getActivity().getPackageName()));
			uebergabeedit.setVisibility(View.GONE);
			CheckBox uebergabetext = (CheckBox) root
					.findViewById(getResources().getIdentifier("box_siegb" + i,
							"id", getActivity().getPackageName()));
			uebergabetext.setVisibility(View.GONE);
		}
	}

	private void init(View root) {
		ttra = (EditText) root.findViewById(R.id.etxt_ttra);
		ttra.requestFocus();

		ttrb0 = (EditText) root.findViewById(R.id.etxt_ttrb0);
		ttrb1 = (EditText) root.findViewById(R.id.etxt_ttrb1);
		ttrb2 = (EditText) root.findViewById(R.id.etxt_ttrb2);
		ttrb3 = (EditText) root.findViewById(R.id.etxt_ttrb3);
		ttrb4 = (EditText) root.findViewById(R.id.etxt_ttrb4);
		ttrb5 = (EditText) root.findViewById(R.id.etxt_ttrb5);
		ttrb6 = (EditText) root.findViewById(R.id.etxt_ttrb6);
		ttrb7 = (EditText) root.findViewById(R.id.etxt_ttrb7);
		ttrb8 = (EditText) root.findViewById(R.id.etxt_ttrb8);
		ttrb9 = (EditText) root.findViewById(R.id.etxt_ttrb9);

		check_b0 = (CheckBox) root.findViewById(R.id.box_siegb0);
		check_b1 = (CheckBox) root.findViewById(R.id.box_siegb1);
		check_b2 = (CheckBox) root.findViewById(R.id.box_siegb2);
		check_b3 = (CheckBox) root.findViewById(R.id.box_siegb3);
		check_b4 = (CheckBox) root.findViewById(R.id.box_siegb4);
		check_b5 = (CheckBox) root.findViewById(R.id.box_siegb5);
		check_b6 = (CheckBox) root.findViewById(R.id.box_siegb6);
		check_b7 = (CheckBox) root.findViewById(R.id.box_siegb7);
		check_b8 = (CheckBox) root.findViewById(R.id.box_siegb8);
		check_b9 = (CheckBox) root.findViewById(R.id.box_siegb9);

		for (int i = 0; i < 10; i++) {
			EditText uebergabeedit = (EditText) root
					.findViewById(getResources().getIdentifier("etxt_ttrb" + i,
							"id", getActivity().getPackageName()));
			uebergabeedit.setVisibility(View.VISIBLE);
			CheckBox uebergabetext = (CheckBox) root
					.findViewById(getResources().getIdentifier("box_siegb" + i,
							"id", getActivity().getPackageName()));
			uebergabetext.setVisibility(View.VISIBLE);
		}
	}

	/**
	 * http://www.tt-tsvgomaringen.de/news/view.php?sid=689
	 */
	private void ttrberechnen(EditText[] edittextboxen, CheckBox[] checkboxen) {
		int gesamt = 0;

		boolean ttrab = false;
		int ittra = 0;
		// Text aus den Felder in Strings umwandeln
		String[] getTTR = new String[11];
		for (int i = 0; i < edittextboxen.length; i++) {
			getTTR[i] = edittextboxen[i].getText().toString();
		}

		/**
		 * Hier wird abgeprüft ob die Felder leer sind. Außerdem ob das ttra
		 * feld nicht leer ist.
		 */
		if (!TextUtils.isEmpty(ttra.getText().toString())) {
			ittra = Integer.parseInt(ttra.getText().toString());
			ttrab = true;
		}
		for (int i = 0; i < edittextboxen.length; i++) {
			if (!TextUtils.isEmpty(getTTR[i]) && ttrab) {
				int uebergabe = Integer.parseInt(getTTR[i]);
				int zwischen = gesamt;

				double uebergabewahrscheinlichkeit = gewinnwahrscheinlichkeit(
						ittra, uebergabe);
				// Wird geprüft ob gewonnen oder nicht
				if (checkboxen[i].isChecked()) {
					gesamt = gewinnpunkte(uebergabewahrscheinlichkeit);
				} else {
					gesamt = verlustpunkte(uebergabewahrscheinlichkeit);

				}
				gesamt = gesamt + zwischen;
			}

		}
		Toast.makeText(
				getActivity(),
				"Du hast " + gesamt + " Punke gemacht! Dein neuer TTR ist: "
						+ (gesamt + ittra), Toast.LENGTH_SHORT).show();

	}

	public double gewinnwahrscheinlichkeit(int ttra, int ttrb) {
		double ttrzwischen = ttrb - ttra;// In einzelschritte aufgeteilt
		// da er sonst null castet
		ttrzwischen = ttrzwischen / 150;
		return (1 / (1 + Math.pow(10, ttrzwischen)));

	}

	public int gewinnpunkte(double gewinnwahrscheinlichkeit) {
		/**
		 * Hier wird die Zwischengespeicherte Aenderungskonstante wieder geholt
		 * von der XML Datei
		 */
		SharedPreferences preferences = this.getActivity()
				.getSharedPreferences("Zwischenwerte", Context.MODE_PRIVATE);
		return (int) Math.round(((1 - gewinnwahrscheinlichkeit) * preferences
				.getInt("Aenderungskonstante", 16)));

	}

	public int verlustpunkte(double gewinnwahrscheinlichkeit) {
		/**
		 * Hier wird die Zwischengespeicherte Aenderungskonstante wieder geholt
		 * von der XML Datei
		 */
		SharedPreferences preferences = this.getActivity()
				.getSharedPreferences("Zwischenwerte", Context.MODE_PRIVATE);

		return (int) Math.round((-gewinnwahrscheinlichkeit * preferences
				.getInt("Aenderungskonstante", 16)));

	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		inflater.inflate(R.menu.ttrfragment, menu);

		super.onCreateOptionsMenu(menu, inflater);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();

		if (id == R.id.menu_aenderungskonstante) {
			startActivity(new Intent(getActivity(),
					EinstellungsttrActivity.class));
			return true;
		}

		return super.onOptionsItemSelected(item);
	}

}
