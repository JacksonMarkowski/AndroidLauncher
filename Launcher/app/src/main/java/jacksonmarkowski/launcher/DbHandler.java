package jacksonmarkowski.launcher;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.sql.SQLException;
import java.util.ArrayList;

public class DbHandler extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "homescreen";
    private static final int DATABASE_VERSION = 1;

    private static final String TABLE_APPLICATIONS = "applications";
    private static final String APPLICATION_ID = "app_id";
    private static final String APPLICATION_NAME = "name";
    private static final String SQL_CREATE_APPLICATIONS = "CREATE TABLE " + TABLE_APPLICATIONS + " (" + APPLICATION_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + APPLICATION_NAME + " TEXT)";

    private static final String TABLE_APPLICATIONS_LIST = "applications_list";
    private static final String APPLICATIONS_LIST_ID = "app_list_id";
    private static final String PAGE = "page";
    private static final String XLOC = "xloc";
    private static final String YLOC = "yloc";
    private static final String SQL_CREATE_APPLICATIONS_LIST = "CREATE TABLE " + TABLE_APPLICATIONS_LIST + " (" + APPLICATIONS_LIST_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + APPLICATION_ID + " INTEGER, " + PAGE + " INTEGER, " + XLOC + " INTEGER, " + YLOC + " INTEGER)";

    public DbHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_APPLICATIONS);
        db.execSQL(SQL_CREATE_APPLICATIONS_LIST);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_APPLICATIONS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_APPLICATIONS_LIST);
        onCreate(db);
    }

    //Adds an application to the table of applications
    public int addApplication(String applicationName) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(APPLICATION_NAME, applicationName);

        db.insert(TABLE_APPLICATIONS, null, values);

        Cursor cursor = db.rawQuery("select * FROM " + TABLE_APPLICATIONS + " WHERE " + APPLICATION_NAME + " = '" + applicationName + "'", null);
        //ToDo: return error if id is not found, not 0
        int id = 0;
        if (cursor.moveToFirst()) {
            id = cursor.getInt(cursor.getColumnIndex(APPLICATION_ID));
        }

        db.close();
        return id;
    }

    //ToDo:check what happens when trying to delete if there is no matching column with name
    //Removes an application from the table of applications
    public int removeApplication(String applicationName) {
        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.rawQuery("select * FROM " + TABLE_APPLICATIONS + " WHERE " + APPLICATION_NAME + " = '" + applicationName + "'", null);
        //ToDo: return error if id is not found, not 0
        int id = 0;
        if (cursor.moveToFirst()) {
            id = cursor.getInt(cursor.getColumnIndex(APPLICATION_ID));
        }
        db.delete(TABLE_APPLICATIONS, APPLICATION_NAME + "='" + applicationName + "'", null);
        db.close();
        return id;
    }

    //Returns list of names of all applications
    public ArrayList<String> getAllApplications() {
        ArrayList<String> applicationNames = new ArrayList<String>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from " + TABLE_APPLICATIONS, null);
        if (cursor.moveToFirst()) {
            while (!cursor.isAfterLast()) {
                String name = cursor.getString(cursor.getColumnIndex(APPLICATION_NAME));
                applicationNames.add(name);
                cursor.moveToNext();
            }
        }
        db.close();
        return applicationNames;
    }



    public void addApplicationToList(int appID, int page, int xLoc, int yLoc) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(APPLICATION_ID, appID);
        values.put(PAGE, page);
        values.put(XLOC, xLoc);
        values.put(YLOC, yLoc);

        db.insert(TABLE_APPLICATIONS_LIST, null, values);
        db.close();
    }

    public void removeApplicationFromList(int appID) {
        SQLiteDatabase db = this.getWritableDatabase();

        db.delete(TABLE_APPLICATIONS_LIST, APPLICATION_ID + "='" + appID + "'", null);
        db.close();
    }

    public ArrayList<Application> getAllApplicationsInfo() {
        ArrayList<Application> apps = new ArrayList<Application>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from " + TABLE_APPLICATIONS, null);
        if (cursor.moveToFirst()) {
            while (!cursor.isAfterLast()) {
                String appName = cursor.getString(cursor.getColumnIndex(APPLICATION_NAME));
                int appID = cursor.getInt(cursor.getColumnIndex(APPLICATION_ID));
                Application app = new Application(appName, appID);

                Cursor cursor2 = db.rawQuery("select * from " + TABLE_APPLICATIONS_LIST + " WHERE " + APPLICATION_ID + " = '" + appID + "'", null);
                if (cursor2.moveToFirst()) {
                    int xLoc = cursor2.getInt(cursor2.getColumnIndex(XLOC));
                    int yLoc = cursor2.getInt(cursor2.getColumnIndex(YLOC));
                    int page = cursor2.getInt(cursor2.getColumnIndex(PAGE));
                    app.setListLoc(xLoc, yLoc, page);
                }
                apps.add(app);
                cursor.moveToNext();
            }
        }
        db.close();
        return apps;
    }



    public void testInsert() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_APPLICATIONS, null);
        cursor.moveToFirst();
        Log.v("Test", cursor.getString(cursor.getColumnIndex(APPLICATION_NAME)));

    }



}
