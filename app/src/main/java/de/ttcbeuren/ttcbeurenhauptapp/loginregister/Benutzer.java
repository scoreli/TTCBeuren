package de.ttcbeuren.ttcbeurenhauptapp.loginregister;

public class Benutzer {
int _id;
String uuid,name,vorname;
int admin;

public Benutzer() {
}

public Benutzer(int _id, String uuid, String name, String vorname, int admin) {
	super();
	this._id = _id;
	this.uuid = uuid;
	this.name = name;
	this.vorname = vorname;
	this.admin = admin;
}
public Benutzer(int _id,String uuid,int admin){
	this.uuid = uuid;
	this._id = _id;
	this.admin = admin;


}
public int get_id() {
	return _id;
}

public void set_id(int _id) {
	this._id = _id;
}

public String getUuid() {
	return uuid;
}

public void setUuid(String uuid) {
	this.uuid = uuid;
}

public String getName() {
	return name;
}

public void setName(String name) {
	this.name = name;
}

public String getVorname() {
	return vorname;
}

public void setVorname(String vorname) {
	this.vorname = vorname;
}

public int getAdmin() {
	return admin;
}

public void setAdmin(int admin) {
	this.admin = admin;
}

}
