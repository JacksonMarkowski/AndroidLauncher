package jacksonmarkowski.launcher;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.sql.SQLException;

public class DbHandler extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "homescreen";
    private static final int DATABASE_VERSION = 1;

    private static final String TABLE_APPLICATION = "application";
    private static final String APPLICATION_ID = "id";
    private static final String APPLICATION_NAME = "name";
    private static final String SQL_CREATE_ENTRIES = "CREATE TABLE " + TABLE_APPLICATION + " (" + APPLICATION_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + APPLICATION_NAME + " TEXT)";

    public DbHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_APPLICATION);
        onCreate(db);
    }

    public void addApplication(String applicationName) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(APPLICATION_NAME, applicationName);

        db.insert(TABLE_APPLICATION, null, values);
        db.close();
    }

    public void testInsert() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_APPLICATION, null);
        cursor.moveToFirst();
        Log.v("Test", cursor.getString(cursor.getColumnIndex(APPLICATION_NAME)));

    }



}
