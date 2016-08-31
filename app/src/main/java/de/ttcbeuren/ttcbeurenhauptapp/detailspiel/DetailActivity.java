package de.ttcbeuren.ttcbeurenhauptapp.detailspiel;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import de.ttcbeuren.ttcbeurenhauptapp.ImSpielActivity;
import de.ttcbeuren.ttcbeurenhauptapp.R;
import de.ttcbeuren.ttcbeurenhauptapp.ergebnisse.ErgebnisseFragment;
import de.ttcbeuren.ttcbeurenhauptapp.loginregister.DatabasehandlerUUID;
import de.ttcbeuren.ttcbeurenhauptapp.loginregister.SessionManager;
import de.ttcbeuren.ttcbeurenhauptapp.spiele.DatabasehandlerSpiele;
import de.ttcbeuren.ttcbeurenhauptapp.spiele.Spiel;

public class DetailActivity extends Activity {
	TextView txt_heimmanschaft, txt_gastmannschaft, txt_spielsystem,
			txt_mannschaftsart, txt_spieldatum, txt_spielende,
			txt_spielstandheim, txtspielstandgast, txt_status,
			txt_beschreibungspielende;
	int uebergabespiel_id;
	Spiel uebergabespiel;
	DatabasehandlerSpiele dbspiele;
	DatabasehandlerUUID dbuuid;
	SessionManager session;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_detail);
		dbspiele = new DatabasehandlerSpiele(this);
		dbuuid = new DatabasehandlerUUID(this);
		init();
		session = new SessionManager(getApplicationContext());
		uebergabespiel_id = getIntent().getExtras().getInt(
				ErgebnisseFragment.KEY);
		uebergabespiel = dbspiele.getSpiel(uebergabespiel_id);
		anzeigen();

	}

	private void anzeigen() {
		if (uebergabespiel.getIstspielbeendet() == 1) {
			txt_spielende.setVisibility(View.VISIBLE);
			txt_beschreibungspielende.setVisibility(View.VISIBLE);
			txt_spielende.setText(uebergabespiel.getSpielende());
		}
		txt_spielsystem.setText(uebergabespiel.getSpielsystem());
		txt_mannschaftsart.setText(uebergabespiel.getMannschaftsart());
		txt_heimmanschaft.setText(uebergabespiel.getHeimverein() + " "
				+ uebergabespiel.getHeimvereinsnummer());

		txt_gastmannschaft.setText(uebergabespiel.getGastverein() + " "
				+ uebergabespiel.getGastvereinsnummer());

		txt_spieldatum.setText(uebergabespiel.getSpielbegindatumtime());
		txt_spielstandheim.setText("" + uebergabespiel.getPunkteHeim());
		/**
		 * Wirft sonst eine Ex weil textview keine ints anzeigen kann
		 */

		txtspielstandgast.setText("" + uebergabespiel.getPunkteGast());
		txt_status.setText(uebergabespiel.getStatus());
	}

	private void init() {

		txt_spielsystem = (TextView) findViewById(R.id.textv_spielsystem);
		txt_mannschaftsart = (TextView) findViewById(R.id.textv_mannschaftsart);
		txt_heimmanschaft = (TextView) findViewById(R.id.textv_Heimmanschaft);
		txt_gastmannschaft = (TextView) findViewById(R.id.textv_Gastmannschaft);
		txt_spieldatum = (TextView) findViewById(R.id.textv_Spieldatum);
		txt_spielstandheim = (TextView) findViewById(R.id.textv_spielstandheim);
		txtspielstandgast = (TextView) findViewById(R.id.textv_spielstandgast);
		txt_status = (TextView) findViewById(R.id.textv_status);

		txt_beschreibungspielende = (TextView) findViewById(R.id.textV_beschreibungspielende);
		txt_spielende = (TextView) findViewById(R.id.textv_Spielende);
		txt_spielende.setVisibility(View.GONE);
		txt_beschreibungspielende.setVisibility(View.GONE);

	}

	/**
	 * Wird ausgegraut wenn man nicht der Besitzer des Spieles ist.
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.detail, menu);
		if (session.isLoggedIn()) {
			if (uebergabespiel.getBenutzer_id() == dbuuid.getBenutzer()
					.get_id() && uebergabespiel.getIstspielbeendet() == 0) {
				menu.getItem(0).setEnabled(true);
			} else {
				menu.getItem(0).setEnabled(false);
			}
		} else {
			menu.getItem(0).setEnabled(false);
		}
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();

		if (id == R.id.change_toimSpiel) {
			Intent i = new Intent(DetailActivity.this, ImSpielActivity.class);
			i.putExtra(ErgebnisseFragment.KEY, uebergabespiel.getSpiel_id());
			startActivity(i);

			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	/**
	 * Diese Methode wandelt das serialisierte Objekt wieder zu einem Objekt um.
	 * In unserem Fall ein Veranstaltungsobjekt.
	 * 
	 * @param bytes
	 * @return
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	public static Spiel deserialize(byte[] bytes) throws IOException,
			ClassNotFoundException {
		ByteArrayInputStream b = new ByteArrayInputStream(bytes);
		ObjectInputStream o = new ObjectInputStream(b);
		return (Spiel) o.readObject();
	}

}
