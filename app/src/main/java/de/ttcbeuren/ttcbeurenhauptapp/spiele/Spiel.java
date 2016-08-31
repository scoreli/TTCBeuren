package de.ttcbeuren.ttcbeurenhauptapp.spiele;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

public class Spiel implements Serializable {
    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private int spiel_id, punkteHeim, punkteGast, benutzer_id;
    private String spielsystem, mannschaftsart, heimverein, heimvereinsnummer,
            gastverein, gastvereinsnummer, status, spielbegindatumtime,
            spielende;

    private int istspielbeendet;

    public Spiel() {
    }

    public Spiel(int spiel_id) {
        this.spiel_id = spiel_id;

    }

    public Spiel(int spiel_id, int punkteHeim, int punkteGast,
                 String spielsystem, String mannschaftsart, String heimverein,
                 String heimvereinsnummer, String gastverein,
                 String gastvereinsnummer, String status,
                 String spielbegindatumtime, String spielende, int istspielbeendet,
                 int benutzer_id) {
        super();
        this.spiel_id = spiel_id;
        this.punkteHeim = punkteHeim;
        this.punkteGast = punkteGast;
        this.spielsystem = spielsystem;
        this.mannschaftsart = mannschaftsart;
        this.heimverein = heimverein;
        this.heimvereinsnummer = heimvereinsnummer;
        this.gastverein = gastverein;
        this.gastvereinsnummer = gastvereinsnummer;
        this.status = status;
        this.spielbegindatumtime = spielbegindatumtime;
        this.spielende = spielende;
        this.istspielbeendet = istspielbeendet;
        this.benutzer_id = benutzer_id;
    }

    public Spiel(int spiel_id, int punkteHeim, int punkteGast,
                 String spielsystem, String mannschaftsart, String heimverein,
                 String heimvereinsnummer, String gastverein,
                 String gastvereinsnummer, String status,
                 String spielbegindatumtime, int istspielbeendet, int benutzer_id) {
        super();
        this.spiel_id = spiel_id;
        this.punkteHeim = punkteHeim;
        this.punkteGast = punkteGast;
        this.spielsystem = spielsystem;
        this.mannschaftsart = mannschaftsart;
        this.heimverein = heimverein;
        this.heimvereinsnummer = heimvereinsnummer;
        this.gastverein = gastverein;
        this.gastvereinsnummer = gastvereinsnummer;
        this.status = status;
        this.spielbegindatumtime = spielbegindatumtime;
        this.istspielbeendet = istspielbeendet;
        this.benutzer_id = benutzer_id;

    }

    public Spiel(int punkteHeim, int punkteGast, String spielsystem,
                 String mannschaftsart, String heimverein, String heimvereinsnummer,
                 String gastverein, String gastvereinsnummer, String status,
                 String spielbegindatumtime, String spielende, int istspielbeendet,
                 int benutzer_id) {
        super();
        this.punkteHeim = punkteHeim;
        this.punkteGast = punkteGast;
        this.spielsystem = spielsystem;
        this.mannschaftsart = mannschaftsart;
        this.heimverein = heimverein;
        this.heimvereinsnummer = heimvereinsnummer;
        this.gastverein = gastverein;
        this.gastvereinsnummer = gastvereinsnummer;
        this.status = status;
        this.spielbegindatumtime = spielbegindatumtime;
        this.spielende = spielende;
        this.istspielbeendet = istspielbeendet;
        this.benutzer_id = benutzer_id;

    }

    public Spiel(int punkteHeim, int punkteGast, String spielsystem,
                 String mannschaftsart, String heimverein, String heimvereinsnummer,
                 String gastverein, String gastvereinsnummer, String status,
                 String spielbegindatumtime, int istspielbeendet, int benutzer_id) {
        super();
        this.punkteHeim = punkteHeim;
        this.punkteGast = punkteGast;
        this.spielsystem = spielsystem;
        this.mannschaftsart = mannschaftsart;
        this.heimverein = heimverein;
        this.heimvereinsnummer = heimvereinsnummer;
        this.gastverein = gastverein;
        this.gastvereinsnummer = gastvereinsnummer;
        this.status = status;
        this.spielbegindatumtime = spielbegindatumtime;
        this.istspielbeendet = istspielbeendet;
        this.benutzer_id = benutzer_id;

    }

    /**
     * Die Methode ist wichtig für die Übertragung zum Chromecast.
     * Diese wandelt das Spiel-Objekt in ein JSON-Objekt um.
     * Dieses erzeugte Objekt wird dann noch in einen String umgewandelt,
     * damit es mittels ChromCastBus einfach verschickt werden kann.
     * Auf der Reciever Seite wird der String wieder umgewandelt.
     * @return
     * @throws JSONException
     */
    public String toJSon() throws JSONException {
        try {
            JSONObject jsonObj = new JSONObject();
            jsonObj.put("heimmanschaft", this.getHeimverein());
            jsonObj.put("heimmanschaftnummer", this.getHeimvereinsnummer());
            jsonObj.put("gastmannschaft", this.getGastverein());
            jsonObj.put("gastmannschaftnummer", this.getGastvereinsnummer());
            jsonObj.put("status", this.getStatus());
            jsonObj.put("spielstandHeim", this.getPunkteHeim());
            jsonObj.put("spielstandGast", this.getPunkteGast());

            return jsonObj.toString();
        } catch (JSONException ex) {
            ex.printStackTrace();
        }
        return null;


    }

    public int getSpiel_id() {
        return spiel_id;
    }

    public void setSpiel_id(int spiel_id) {
        this.spiel_id = spiel_id;
    }

    public int getPunkteHeim() {
        return punkteHeim;
    }

    public void setPunkteHeim(int punkteHeim) {
        this.punkteHeim = punkteHeim;
    }

    public int getPunkteGast() {
        return punkteGast;
    }

    public void setPunkteGast(int punkteGast) {
        this.punkteGast = punkteGast;
    }

    public String getSpielsystem() {
        return spielsystem;
    }

    public void setSpielsystem(String spielsystem) {
        this.spielsystem = spielsystem;
    }

    public String getMannschaftsart() {
        return mannschaftsart;
    }

    public void setMannschaftsart(String mannschaftsart) {
        this.mannschaftsart = mannschaftsart;
    }

    public String getHeimverein() {
        return heimverein;
    }

    public void setHeimverein(String heimverein) {
        this.heimverein = heimverein;
    }

    public String getHeimvereinsnummer() {
        return heimvereinsnummer;
    }

    public void setHeimvereinsnummer(String heimvereinsnummer) {
        this.heimvereinsnummer = heimvereinsnummer;
    }

    public String getGastverein() {
        return gastverein;
    }

    public void setGastverein(String gastverein) {
        this.gastverein = gastverein;
    }

    public String getGastvereinsnummer() {
        return gastvereinsnummer;
    }

    public void setGastvereinsnummer(String gastvereinsnummer) {
        this.gastvereinsnummer = gastvereinsnummer;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int isIstspielbeendet() {
        return istspielbeendet;
    }

    public int getIstspielbeendet() {
        return istspielbeendet;
    }

    public void setIstspielbeendet(int istspielbeendet) {
        this.istspielbeendet = istspielbeendet;
    }

    public String getSpielbegindatumtime() {
        return spielbegindatumtime;
    }

    public void setSpielbegindatumtime(String spielbegindatumtime) {
        this.spielbegindatumtime = spielbegindatumtime;
    }

    public String getSpielende() {
        return spielende;
    }

    public void setSpielende(String spielende) {
        this.spielende = spielende;
    }

    public int getBenutzer_id() {
        return benutzer_id;
    }

    public void setBenutzer_id(int benutzer_id) {
        this.benutzer_id = benutzer_id;
    }

    @Override
    public String toString() {
        String nummer = null;
        if (heimverein.equals("TTC Beuren a.d. Aach")) {
            nummer = heimvereinsnummer;
        } else {
            nummer = gastvereinsnummer;
        }
        return mannschaftsart + " " + nummer + ": " + heimverein + " " + heimvereinsnummer
                + " : " + gastverein + " " + gastvereinsnummer + " "
                + punkteHeim + ":" + punkteGast;
    }

    public String ueberschrift(String ueberschrift) {
        return ueberschrift;
    }

}
