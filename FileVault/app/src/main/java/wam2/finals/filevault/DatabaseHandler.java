package wam2.finals.filevault;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.io.File;

public class DatabaseHandler extends SQLiteOpenHelper {

    private static final String DB_NAME = "file_vault";
    private static final int DB_VER = 1;
    private static final String PIN_TABLE = "pin";
    private static final String ID_COL = "id";
    private static final String PIN_COL1 = "entry_code";

    private static final String PASS_TABLE = "pass_table";
    private static final String PASS_ID = "id";
    private static final String PASS_FOR = "website";
    private static final String PASS_DATA = "password_data";


    public DatabaseHandler(Context context) {
        super(context, DB_NAME, null, DB_VER);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + PIN_TABLE + " (" +
                ID_COL + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                PIN_COL1 + " TEXT NOT NULL)");

        db.execSQL("CREATE TABLE " + PASS_TABLE + " (" +
                PASS_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                PASS_FOR + " TEXT NOT NULL," +
                PASS_DATA + " TEXT NOT NULL)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + PIN_TABLE + ";");
        db.execSQL("DROP TABLE IF EXISTS " + PASS_TABLE + ";");
        onCreate(db);
    }

    public Cursor getAllFiles(){
        SQLiteDatabase db = getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + PASS_FOR, null);
    }

    public boolean insertNewPass(String website, String pass){
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(PASS_FOR, website);
        cv.put(PASS_DATA, pass);
        long res = db.insert(PASS_TABLE, null, cv);
        return res != -1;
    }

    public void clearFileDatabase(){
        SQLiteDatabase db = getWritableDatabase();
        db.delete(PASS_TABLE, null, null);
        db.close();
    }

    public boolean insertNewCode(String code){
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(PIN_COL1, code);
        long res = db.insert(PIN_TABLE, null, cv);
        db.close();
        return res != -1;
    }

    public boolean updateCode(String newCode){
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(PIN_COL1, newCode);
        long res = db.update(PIN_TABLE, cv, "id = ?", new String[]{"1"});
        db.close();
        return res != 0;
    }

    public boolean checkVaultCode(String code){
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + PIN_TABLE + " WHERE " + ID_COL + "=1", null);
        cursor.moveToNext();
        boolean accessGranted = code.equals(cursor.getString(1));
        cursor.close();
        return accessGranted;
    }

    public boolean hasPinRegistered(){
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(PIN_TABLE, null, null, null, null, null, null);
        boolean hasPin = cursor.getCount() != 0;
        cursor.close();
        return hasPin;
    }
}
