package md.uper.uperclient.app;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.HashMap;

public class SQLiteHandler extends SQLiteOpenHelper {

    private static final String TAG = "SQLiteHandler";

    // All Static variables
    // Database Version
    private static final int database_version = 1;

    // Database Name
    private static final String database_name = "stark567_uper";

    // Login table name
    private static final String table_client = "client";

    // Login Table Columns names
    private static final String key_id_client= "id_client";
    private static final String key_nume = "nume";
    private static final String key_prenume= "prenume";
    private static final String key_telefon = "telefon";
    private static final String key_oras = "oras";
    private static final String key_email = "email";
    private static final String key_data_creare = "data_creare";

    public SQLiteHandler(Context context) {
        super(context, database_name, null, database_version);
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_LOGIN_TABLE = "CREATE TABLE " + table_client + " ("
                + key_id_client + " TEXT," + key_nume + " TEXT,"
                + key_prenume + " TEXT," + key_telefon + " TEXT,"
                + key_oras + " TEXT," + key_email + " TEXT," + key_data_creare  + " TEXT" + ")";
        db.execSQL(CREATE_LOGIN_TABLE);

        Log.d(TAG, "Database tables created");
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + table_client);

        // Create tables again
        onCreate(db);
    }

    /**
     * Storing client details in database
     * */
    public void addclient(String id_client, String nume, String prenume, String telefon, String oras, String email, String data_creare) {

        SQLiteDatabase db = this.getWritableDatabase();

        Log.d(TAG, "pasul 1 este realizat");

        ContentValues values = new ContentValues();

        Log.d(TAG, "pasul 2 este realizat");

        values.put(key_id_client, id_client);
        values.put(key_nume, nume);
        values.put(key_prenume, prenume);
        values.put(key_telefon, telefon);
        values.put(key_oras, oras);
        values.put(key_email, email);
        values.put(key_data_creare, data_creare);
        Log.d(TAG, "pasul 3 este realizat");
        // Inserare rind
        long id = db.insert(table_client, null, values);

        Log.d(TAG, "Client nou inserat in SQLite cu id : " + id);
    }

    /**
     * Getting client data from database
     * */
    public HashMap<String, String> getclientDetails() {
        HashMap<String, String> client = new HashMap<>();
        String selectQuery = "SELECT * FROM " + table_client;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // Mutam la primul rind
        cursor.moveToFirst();
        Log.d(TAG, "obiectul selectat din db :::" + cursor.getString(6));
        if (cursor.getCount() > 0) {

            client.put("id_client", cursor.getString(0));
            client.put("nume", cursor.getString(1));
            client.put("prenume", cursor.getString(2));
            client.put("telefon", cursor.getString(3));
            client.put("oras", cursor.getString(4));
            client.put("email", cursor.getString(5));
            client.put("data_creare", cursor.getString(6));
        }
        cursor.close();
        db.close();
        // return client
        Log.d(TAG, "Preluare client din Sqlite: " + client.toString());

        return client;
    }

    /**
     * Re crate database Delete all tables and create them again
     * */
    public void deleteclients() {
        SQLiteDatabase db = this.getWritableDatabase();
        // Delete All Rows
        db.delete(table_client, null, null);
        db.close();

        Log.d(TAG, "Deleted all client info from sqlite");
    }

}