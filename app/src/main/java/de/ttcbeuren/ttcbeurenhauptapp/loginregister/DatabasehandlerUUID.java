package de.ttcbeuren.ttcbeurenhauptapp.loginregister;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Hier wird die SqlLite Datenbank konfiguriert. Diese wird dann erstellt. Hier
 * sind auch alle Methoden vermerkt die für die Datenbankzugriffe notwendig
 * sind. Hierbei werden jeweils Sql zugriffe vollzogen. Hier werden die Daten
 * von dem angemeldeten User gespeichert. Damit es dann für weitere Zwecke
 * verwendet kann.
 * 
 * @author philipp
 */

public class DatabasehandlerUUID extends SQLiteOpenHelper {
	// All Static variables
	// Database Version
	private static final int DATABASE_VERSION = 1;

	// Database Name
	private static final String DATABASE_NAME = "UUID.db";

	// Contacts table name
	private static final String TABLE_MITGLIEDER = "UUID";

	// Contacts Table Columns names
	private static final String MITGLIEDER_ID = "id";
	private static final String MITGLIEDER_UUID = "UUID";
	private static final String BENUTZER_ADMIN = "Erweitert";// ADMIN

	public DatabasehandlerUUID(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	/**
	 * Mit dieser Methode wird die Sql-lite Datenbank gebaut bzw. die Tabelle.
	 * Hierbei wird ein Sql-Befehl erzeugt und dieser dann ausgeführt.(execSQL)
	 */
	@Override
	public void onCreate(SQLiteDatabase db) {
		String CREATE_CONTACTS_TABLE = "CREATE TABLE " + TABLE_MITGLIEDER + "("
				+ MITGLIEDER_ID + " INTEGER PRIMARY KEY," + MITGLIEDER_UUID
				+ " TEXT, " + BENUTZER_ADMIN + " INTEGER" + ")";
		db.execSQL(CREATE_CONTACTS_TABLE);
	}

	// Upgrading database
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// Drop older table if existed
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_MITGLIEDER);

		// Create tables again
		onCreate(db);
	}

	/**
	 * Hier wird das Mitglied in der Datenbank eingspeichert.
	 * 
	 * @param benutzer
	 */
	public void addBenutzer(Benutzer benutzer) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(MITGLIEDER_UUID, benutzer.getUuid()); // UUID
		values.put(MITGLIEDER_ID, benutzer.get_id());
		values.put(BENUTZER_ADMIN, benutzer.getAdmin());

		// Inserting Row
		db.insert(TABLE_MITGLIEDER, null, values);
		db.close(); // Closing database connection
	}

	/**
	 * Hier wird das Mitglied mit hilfe einer SQL-Abfrage aus der Datenbank
	 * geholt und das objekt wird zurückggeben.
	 * 
	 * @return mitglied
	 */
	public Benutzer getBenutzer() {
		// Select All Query
		String selectQuery = "SELECT  * FROM " + TABLE_MITGLIEDER;
		SQLiteDatabase db = this.getWritableDatabase();
		Benutzer benutzer = null;

		Cursor cursor = db.rawQuery(selectQuery, null);
		cursor.moveToFirst();
		benutzer = new Benutzer();
		benutzer.set_id(Integer.parseInt(cursor.getString(0)));
		benutzer.setUuid(cursor.getString(1));

		db.close(); // Closing database connection
		return benutzer;

	}

	/**
	 * Hier wird ein Mitglied mit hilfe einer SQL-Abfrage gelöscht. Der
	 * Eintrag/Mitglied wird mit der ID-Referenziert.
	 * 
	 * @param benutzer
	 */
	public void deleteBenutzer(Benutzer benutzer) {
		SQLiteDatabase db = this.getWritableDatabase();
		db.delete(TABLE_MITGLIEDER, MITGLIEDER_ID + " = ?",
				new String[] { String.valueOf(benutzer.get_id()) });
		db.close();
	}

	/**
	 * Mit dieser Methode werden alle Einträger der Datenbank gelöscht.
	 */
	public void deleteUsers() {
		SQLiteDatabase db = this.getWritableDatabase();
		// Delete All Rows
		db.delete(TABLE_MITGLIEDER, null, null);
		db.close();

	}
}
