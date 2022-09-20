package pl.com.sng.sngtwojewodociagi;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by PBronk on 22.11.2016.
 */

public class DataHandler {

    public static final int DATABASE_VERSION =1;
    public static final String DATABASE_NAME = "alert";
    public static final String KEY_ROWID_ALERTY = "id_a";
    public static final String TIME_O = "created_at_a";
    public static final String KEY_ROWID_DZIENICE = "id_tab_d";
    public static final String TIME_A = "created_at_d";

    public static final String ID_ALERT = "id_alert";
    public static final String NAME = "name";
    public static final String VALUE = "value";

    public static final String ID_DZIENICE = "id_dzielnica";
    public static final String MAISTO = "miasto";
    public static final String DZIELNICA = "dzielnica";
    public static final String ZAZN_DZIENLICA = "zazn_dzielnica";

    public static final String  TABLE_ALERT ="table_alerts";
    public static final String  TABLE_DZIENLIC ="table_dzielnice";
    public static final String  TABLE_WYLACZENIA ="table_wylaczenia";
    public static final String KEY_ROWID_WYLACZENIA = "id_wyl";
    public static final String  ELEMENT_ID= "element_it";
    public static final String NAME_WYL = "name_wyl";
    public static final String VALUE_WYL = "value_wyl";
    public static final String CREATE_AT_WYL = "created_at_wyl";

    public static final String TABLE_NOTOFI = "notifi_table";
    public static final String ID_NOTIFI = "notifi_id";
    public static final String NOTIFI_MESSAGE = "message";
    public static final String NOTIFI_TIME= "created_notifi";

    public static final String TABLE_START = "start_table";
    public static final String ID_START = "start_id";
    public static final String START_NAZWA = "nazwa_ikony";
    public static  final String EKO_POINT_NAZWA = "eko_point";
    public static final String POINT_PLACE = "place";
    public static final String POINT_SAMPLE = "sample";
    public static final String POINT_DATEFIN = "datefin";
    public static final String POINT_LAT = "latitude";
    public static final String POINT_LONG = "longitude";
    public static final String EKO_BAKT_NAZWA = "eko_bakt";
    public static final String BAKT_INDICAT = "indicat";
    public static final String BAKT_SAMPLE = "sample";
    public static final String BAKT_VALUE = "value";
    public static final String BAKT_MAXVALUE = "maxvalue";
    public static  final String CZY_POWIADOMIENIE = "czy_powiadomienia";
    public static final  String CZY_POWIDADOMNIENIE_COL = "czy_powiadomienie";
    public static final String TAB_KTORTE_POW = "ktore_pow";
    public static final String KTORE_POW = "powiadomienia";
    public static final String KTORE_WYSL = "czy_wyslane";

    public static final String TABELA_PRACA = "praca";
    public static final String PRACA_MODULE = "Module";
    public static final String PRACA_ELEMENT = "Element";
    public static final String PRACA_NAME = "name";

    public static final String TABLE_ALERT_CREATE = "create table table_alerts(id_a INTEGER PRIMARY KEY AUTOINCREMENT, id_alert text not null, name text not null,value text,created_at_a DATETIME DEFAULT CURRENT_TIMESTAMP);";
    public static final String TABLE_DZIELNICE_CREATE = "create table table_dzielnice(id_d INTEGER PRIMARY KEY AUTOINCREMENT, id_dzielnica text not null, miasto text not null,dzielnica text,zazn_dzielnica INTEGER,created_at_d DATETIME DEFAULT CURRENT_TIMESTAMP);";
    public static final String TABLE_WYLACZENIA_CREATE = "create table table_wylaczenia(id_wyl INTEGER PRIMARY KEY AUTOINCREMENT,element_it text not null, name_wyl not null, value_wyl text not null, created_at_wyl DATETIME DEFAULT CURRENT_TIMESTAMP);";
    public static final String TABLE_NOTOFI_CREATE = "create table notifi_table(notifi_id INTEGER PRIMARY KEY AUTOINCREMENT, message text not null, created_notifi DATETIME DEFAULT CURRENT_TIMESTAMP);";
    public static final String TABLE_START_VIEW_CREATE = "create table start_table(start_id text not null,nazwa_ikony text not null, created_start DATETIME DEFAULT CURRENT_TIMESTAMP )";
    public static final String TABLE_EKOPLAZA_POINT = "create table eko_point(point_id INTEGER PRIMARY KEY AUTOINCREMENT, place text not null,sample text not null, datefin text not null, latitude text not null,longitude text not null);";
    public static final String TABLE_EKOPLAZA_BAKT = "create table eko_bakt(bakt_id INTEGER PRIMARY KEY AUTOINCREMENT,indicat text not null,sample text not null, value text not null, maxvalue text not null); ";
    public static final String TABLE_CZYPOWIADOMIENIA = "create table czy_powiadomienia(powiadomienia_id INTEGER PRIMARY KEY AUTOINCREMENT, czy_powiadomienie text not null);";
    public static final String TABLE_KTOREPOWIADOMIENIA= "create table ktore_pow(ktore_pow_id INTEGER PRIMARY KEY AUTOINCREMENT, powiadomienia text not null, czy_wyslane text not null);";
  //  public static final String TABLE_PRACA = "create table praca(praca_id INTEGER PRIMARY KEY AUTOINCREMENT,Module text not null,Element text not null,name text not null)";

    DataBaseHelper dbhelper;
    Context ctx;
    SQLiteDatabase db;
    public DataHandler(Context ctx)
    {
        this.ctx = ctx;
        dbhelper = new DataBaseHelper(ctx);
    }
    private static class DataBaseHelper extends SQLiteOpenHelper {
        @Override
        public void onCreate(SQLiteDatabase db) {
            try{
                db.execSQL(TABLE_ALERT_CREATE);
                db.execSQL(TABLE_DZIELNICE_CREATE);
                db.execSQL(TABLE_WYLACZENIA_CREATE);
                db.execSQL(TABLE_NOTOFI_CREATE);
                db.execSQL(TABLE_START_VIEW_CREATE);
                db.execSQL(TABLE_EKOPLAZA_POINT);
                db.execSQL(TABLE_EKOPLAZA_BAKT);
                db.execSQL(TABLE_CZYPOWIADOMIENIA);
                db.execSQL(TABLE_KTOREPOWIADOMIENIA);
               // db.execSQL(TABLE_PRACA);

            }catch (SQLException e){
                e.printStackTrace();
            }
        }
        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS table_alerts");
            db.execSQL("DROP TABLE IF EXISTS table_dzielnice");
            db.execSQL("DROP TABLE IF EXISTS table_wylaczenia");
            db.execSQL("DROP TABLE IF EXISTS notifi_table");
            db.execSQL("DROP TABLE IF EXISTS start_table");
            db.execSQL("DROP TABLE IF EXISTS eko_point");
            db.execSQL("DROP TABLE IF EXISTS eko_bakt");
            db.execSQL("DROP TABLE IF EXISTS czy_powiadomienia");
            db.execSQL("DROP TABLE IF EXISTS ktore_pow");
        //    db.execSQL("DROP TABLE IF EXISTS praca");
            onCreate(db);
        }
        public DataBaseHelper(Context ctx){super(ctx, DATABASE_NAME, null, DATABASE_VERSION);}

    }
    public DataHandler open(){
        db = dbhelper.getWritableDatabase();
        return this;
    }
    public void close()
    {
        dbhelper.close();
    }

   // public void insertPraca(String modul, String elem, String name){

 //       ContentValues content20 = new ContentValues();
 //       content20.put(PRACA_MODULE,modul);
 //       content20.put(PRACA_ELEMENT,elem);
 //       content20.put(PRACA_NAME,name);
 //       db.insertOrThrow(TABELA_PRACA,null ,content20);
//    }
//


    public Cursor returnCzyPowiadomienie() {

        return db.query(CZY_POWIADOMIENIE, new String[]{CZY_POWIDADOMNIENIE_COL}, null , null, null, null, null);

    }
    public Cursor returnKtorePow() {

        return db.query(TAB_KTORTE_POW, new String[]{KTORE_POW,KTORE_WYSL}, null , null, null, null, null);

    }
    public Cursor returnKtorePowEnable() {
        String where = KTORE_WYSL +" = '1'";
        return db.query(TAB_KTORTE_POW, new String[]{KTORE_POW}, where , null, null, null, null);

    }

    public int updateKtorePoowiadomienia(String ktorePow, String czyWyslane){
        ContentValues value = new ContentValues();
        value.put(KTORE_WYSL,czyWyslane);
        String where = KTORE_POW +" = '"+ktorePow+"'";
        return db.update(TAB_KTORTE_POW,value , where, null);

    }

    public void insertKtorePowiadomienia(String ktorePow, String czyWyslane){

        ContentValues content3 = new ContentValues();
        content3.put(KTORE_POW,ktorePow);
        content3.put(KTORE_WYSL,czyWyslane);


        db.insertOrThrow(TAB_KTORTE_POW,null ,content3);
    }


    public void updatePowiadoniemia(String powiadamiac){
        db.execSQL("delete from "+CZY_POWIADOMIENIE+";");

        ContentValues content2 = new ContentValues();
        content2.put(CZY_POWIDADOMNIENIE_COL,powiadamiac);

        db.insertOrThrow(CZY_POWIADOMIENIE,null ,content2);
    }




    public void deleteAllEkoPoint(){
        try {
            db.execSQL("delete from "+EKO_POINT_NAZWA+";");
            db.execSQL("delete from sqlite_sequence WHERE name = 'eko_point';");// resetowanie id autentycznosci
        }catch (SQLException e){
            e.printStackTrace();
        }
    }
    public void deleteAllEkoBakt(){
        try {
            db.execSQL("delete from "+EKO_BAKT_NAZWA+";");
            db.execSQL("delete from sqlite_sequence WHERE name = 'eko_bakt';");// resetowanie id autentycznosci
        }catch (SQLException e){
            e.printStackTrace();
        }
    }




    public long insert_eko_point(String place, String sample,String datefin, String lati, String longa ){
        ContentValues content = new ContentValues();
        content.put(POINT_PLACE,place);
        content.put(POINT_SAMPLE,sample);
        content.put(POINT_DATEFIN, datefin);
        content.put(POINT_LAT, lati);
        content.put(POINT_LONG, longa);
        return db.insertOrThrow(EKO_POINT_NAZWA,null,content);
    }
    public long insert_eko_bakt(String indicat, String sample, String value, String maxvalue){
        ContentValues content = new ContentValues();
        content.put(BAKT_INDICAT,indicat);
        content.put(BAKT_SAMPLE, sample);
        content.put(BAKT_VALUE, value);
        content.put(BAKT_MAXVALUE, maxvalue);
        return db.insertOrThrow(EKO_BAKT_NAZWA,null,content);
    }



    public Cursor returnContect(String id) {

        return db.query(TABLE_START, new String[]{START_NAZWA}, "start_id = " + id, null, null, null, null);

    }


    public Cursor returnEkoPoint() {

        return db.query(true,TABLE_EKOPLAZA_POINT,new String[]{POINT_PLACE,POINT_LAT,POINT_LONG,POINT_DATEFIN},null,null,null,null,null,null);

    }
    public Cursor returnBakt(String sample) {
        String where = BAKT_SAMPLE  +" = '"+sample+"'";
        return  db.query(true,TABLE_EKOPLAZA_BAKT,new String[]{BAKT_INDICAT,BAKT_VALUE,BAKT_MAXVALUE},where,null,null,null,null,null);

    }



    public int updateTableStart(String id, String nazwaIcony){

        ContentValues value = new ContentValues();
        value.put(START_NAZWA,nazwaIcony);


        String where = ID_START +" = '"+id+"'";
        return db.update(TABLE_START,value , where, null);
    }



    public long insert_start_tab(String id_code, String nazwa_icon){
        ContentValues content = new ContentValues();
        content.put(ID_START,id_code);
        content.put(START_NAZWA, nazwa_icon);
        return db.insertOrThrow(TABLE_START,null,content);
    }


    public long insertMessage(String message){
        ContentValues content = new ContentValues();
        content.put(NOTIFI_MESSAGE, message);
        return db.insertOrThrow(TABLE_NOTOFI,null,content);
    }
    public Cursor returnMessage() {

        return db.query(TABLE_NOTOFI, new String[]{NOTIFI_MESSAGE},null, null, null, null, null);

    }

    public void deleteAllMessage(){
        try {
            db.execSQL("delete from "+TABLE_NOTOFI+";");
            db.execSQL("delete from sqlite_sequence WHERE name = 'notifi_table';");// resetowanie id autentycznosci
        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    public long insertAlert(String id,String name, String value){
        ContentValues content = new ContentValues();
        content.put(ID_ALERT, id);
        content.put(NAME, name);
        content.put(VALUE, value);
        return db.insertOrThrow(TABLE_ALERT,null,content);
    }
    public long insertDzielnice(String id,String miasto, String dzielnice){
        ContentValues content = new ContentValues();
        content.put(ID_DZIENICE, id);
        content.put( MAISTO,miasto);
        content.put(DZIELNICA, dzielnice);
        content.put(ZAZN_DZIENLICA, 1);
        return db.insertOrThrow(TABLE_DZIENLIC,null,content);
    }
    public long insertWylaczenia(String id_wyl,String name_wyl, String value_wyl){
        ContentValues content = new ContentValues();
        content.put(ELEMENT_ID, id_wyl);
        content.put(NAME_WYL, name_wyl);
        content.put(VALUE_WYL, value_wyl);
        return db.insertOrThrow(TABLE_WYLACZENIA,null,content);
    }

    public Cursor returnAlerts() {

        return db.query(TABLE_ALERT, new String[]{ID_ALERT,NAME,VALUE, TIME_O}, "name like" + "'%Title%'", null, null, null, null);

    }
    public Cursor returnDzielnice() {

        return db.query(TABLE_DZIENLIC, new String[]{DZIELNICA, MAISTO,ZAZN_DZIENLICA}, null, null, null, null, null);

    }
    public Cursor returnPlanoweWyl() {

        return db.query(TABLE_WYLACZENIA, new String[]{ELEMENT_ID,NAME_WYL,VALUE_WYL, CREATE_AT_WYL}, "name_wyl like" + "'%HTML%'", null, null, null, null);

    }

    public Cursor returnAlertsFromId(String alert_idd) {

        return db.query(TABLE_ALERT, new String[]{ID_ALERT,NAME,VALUE, TIME_O}, "id_alert = '" + alert_idd +"'", null, null, null, null);

    }
    public Cursor returnDzielniceActive() {

        return db.query(TABLE_DZIENLIC, new String[]{ID_DZIENICE,DZIELNICA}, "zazn_dzielnica = " + 1, null, null, null, null);

    }


    public void deleteAllAlerts(){
        try {
            db.execSQL("delete from "+TABLE_ALERT+";");
            db.execSQL("delete from sqlite_sequence WHERE name = 'table_alerts';");// resetowanie id autentycznosci
        }catch (SQLException e){
            e.printStackTrace();
        }
    }
    public void deleteAllPlanoweWyl(){
        try {
            db.execSQL("delete from "+TABLE_WYLACZENIA+";");
            db.execSQL("delete from sqlite_sequence WHERE name = 'table_wylaczenia';");// resetowanie id autentycznosci
        }catch (SQLException e){
            e.printStackTrace();
        }
    }


    public void deleteAllDzielnice(){
        try {
            db.execSQL("delete from "+TABLE_DZIENLIC+";");
            db.execSQL("delete from sqlite_sequence WHERE name = 'table_dzielnice';");// resetowanie id autentycznosci
        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    public int updateTableDzielnice(String dzielnica, Integer zaznWartosc){

        ContentValues value = new ContentValues();
        value.put(ZAZN_DZIENLICA,zaznWartosc);

        String where = DZIELNICA +" = '"+dzielnica+"'";
        return db.update(TABLE_DZIENLIC,value , where, null);
    }

    private String getDateTime() {
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        Date date = new Date();
        return dateFormat.format(date);
    }

    public int selectAll(){

        ContentValues value = new ContentValues();
        value.put(ZAZN_DZIENLICA,1);
        return db.update(TABLE_DZIENLIC,value,null , null);

        //  String where = "UPDATE "+ TABLE_DZIENLIC +" SET " + ZAZN_DZIENLICA + " = " + 1 ;
        //    db.rawQuery(where, null);

    }
    public int unSelectAll(){

        ContentValues value = new ContentValues();
        value.put(ZAZN_DZIENLICA,0);
        return db.update(TABLE_DZIENLIC,value,null , null);
        //   String where = "UPDATE "+ TABLE_DZIENLIC +" SET " + ZAZN_DZIENLICA + " = " + 0 ;
        //   db.rawQuery(where, null);

    }


}
