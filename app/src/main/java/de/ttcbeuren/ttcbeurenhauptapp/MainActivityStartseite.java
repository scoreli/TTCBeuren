package de.ttcbeuren.ttcbeurenhauptapp;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DialogFragment;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import de.ttcbeuren.ttcbeurenhauptapp.alertdialogs.AlertFragmentConfirm;
import de.ttcbeuren.ttcbeurenhauptapp.ergebnisse.ErgebnisseFragment;
import de.ttcbeuren.ttcbeurenhauptapp.listview.InterneRanglisteFragment;
import de.ttcbeuren.ttcbeurenhauptapp.loginregister.LoginActivity;
import de.ttcbeuren.ttcbeurenhauptapp.ttrrechner.TTRFragment;

public class MainActivityStartseite extends Activity implements
		NavigationDrawerFragment.NavigationDrawerCallbacks {
	/**
	 * Fragment managing the behaviors, interactions and presentation of the
	 * navigation drawer.
	 */
	private NavigationDrawerFragment mNavigationDrawerFragment;

	/**
	 * Used to store the last screen title. For use in
	 * {@link #restoreActionBar()}.
	 */
	private CharSequence mTitle;
	private static final String STATE_SELECTED_NAVIGATION_ITEM = "selected_navigation_item";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main_activity_startseite);
		mNavigationDrawerFragment = (NavigationDrawerFragment) getFragmentManager()
				.findFragmentById(R.id.navigation_drawer);
		mTitle = getTitle();

		// Set up the drawer.
		mNavigationDrawerFragment.setUp(R.id.navigation_drawer,
				(DrawerLayout) findViewById(R.id.drawer_layout));

	}

	@Override
	public void onNavigationDrawerItemSelected(int position) {
		/*
		 * if (position == 0) { FragmentManager fragmentManager =
		 * getFragmentManager(); FragmentTransaction ft =
		 * fragmentManager.beginTransaction(); ft.replace(R.id.container, new
		 * AktuellesFragment().newInstance(position + 1)) .commit(); mTitle =
		 * getString(R.string.title_aktuelles); }
		 */

		if (position == 0) {
			FragmentManager fragmentManager = getFragmentManager();
			fragmentManager
					.beginTransaction()
					.replace(R.id.container,
							new ErgebnisseFragment().newInstance(position + 1),
							"Ergebnisse_Fragment").commit();
			mTitle = getString(R.string.title_ergebnisse);

		}
		if (position == 1) {
			FragmentManager fragmentManager = getFragmentManager();
			FragmentTransaction ft = fragmentManager.beginTransaction();
			ft.replace(R.id.container,
					new TTRFragment().newInstance(position + 1)).commit();
			mTitle = getString(R.string.title_ttrrechner);

		}
		/*
		if (position == 2) {
			FragmentManager fragmentManager = getFragmentManager();
			FragmentTransaction ft = fragmentManager.beginTransaction();
			ft.replace(R.id.container,
					new InterneRanglisteFragment().newInstance(position + 1))
					.commit();
			mTitle = getString(R.string.title_interneRangliste);

		}*/

	}

	public void onSectionAttached(int number) {
		switch (number) {
		/*
		 * case 1: mTitle = getString(R.string.title_aktuelles);
		 * 
		 * break;
		 */

		case 1:
			mTitle = getString(R.string.title_ergebnisse);
			// Toast.makeText(this, number + "", Toast.LENGTH_SHORT).show();
			break;
		case 2:
			mTitle = getString(R.string.title_ttrrechner);
			// Toast.makeText(this, number + "", Toast.LENGTH_SHORT).show();

			break;
			/*
		case 3:
			mTitle = getString(R.string.title_interneRangliste);
			// Toast.makeText(this, number + "", Toast.LENGTH_SHORT).show();

			break;*/
		}
	}

	public void restoreActionBar() {
		ActionBar actionBar = getActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
		actionBar.setDisplayShowTitleEnabled(true);
		actionBar.setTitle(mTitle);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		if (!mNavigationDrawerFragment.isDrawerOpen()) {
			// Only show items in the action bar relevant to this screen
			// if the drawer is not showing. Otherwise, let the drawer
			// decide what to show in the action bar.

			getMenuInflater().inflate(R.menu.main_activity_startseite, menu);

			// Kann man punkte unsichtbar machen
			// menu.findItem(R.id.menu_aenderungskonstante).setVisible(false);

			restoreActionBar();// sonst zeigt es immer aktuelles an ?
			return true;
		}
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		/*
		 * if (id == R.id.action_settings) {
		 * 
		 * return true; }
		 */
		if (id == R.id.menu_account) {
			startActivity(new Intent(MainActivityStartseite.this,
					LoginActivity.class));

			return true;
		}
		if (id == R.id.menu_spiel_erstellen) {
			startActivity(new Intent(MainActivityStartseite.this,
					NeuesSpielActivity.class));
			return true;
		}
		/*
		 * //if (id == R.id.menu_erstellen_newsfeed) {
		 * 
		 * return true; }
		 */

		return super.onOptionsItemSelected(item);
	}

	/**
	 * A placeholder fragment containing a simple view.
	 */
	public static class PlaceholderFragment extends Fragment {
		/**
		 * The fragment argument representing the section number for this
		 * fragment.
		 */
		private static final String ARG_SECTION_NUMBER = "section_number";

		/**
		 * Returns a new instance of this fragment for the given section number.
		 */
		public static PlaceholderFragment newInstance(int sectionNumber) {
			PlaceholderFragment fragment = new PlaceholderFragment();
			Bundle args = new Bundle();
			args.putInt(ARG_SECTION_NUMBER, sectionNumber);
			fragment.setArguments(args);
			return fragment;
		}

		public PlaceholderFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(
					R.layout.fragment_main_activity_startseite, container,
					false);
			return rootView;
		}

		@Override
		public void onAttach(Activity activity) {
			super.onAttach(activity);
			((MainActivityStartseite) activity)
					.onSectionAttached(getArguments()
							.getInt(ARG_SECTION_NUMBER));
		}
	}

}
