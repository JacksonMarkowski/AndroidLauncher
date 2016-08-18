package jacksonmarkowski.launcher;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

public class DbHandler extends SQLiteOpenHelper {

    private Activity activity;
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

    public DbHandler(Activity activity) {
        super(activity, DATABASE_NAME, null, DATABASE_VERSION);
        this.activity = activity;
    }

    //ToDo: possibly make this its own class
    private class AppPos{
        public int page;
        public int yLoc;
        public int xLoc;
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



    public void addApplicationToList(int appID) {
        AppPos pos = generateAppPosition();
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(APPLICATION_ID, appID);
        values.put(PAGE, pos.page);
        values.put(XLOC, pos.xLoc);
        values.put(YLOC, pos.yLoc);

        db.insert(TABLE_APPLICATIONS_LIST, null, values);
        db.close();
    }

    public AppPos generateAppPosition() {
        SQLiteDatabase db = this.getReadableDatabase();

        AppPos pos = new AppPos();

        Cursor cursor = db.rawQuery("select * from " + TABLE_APPLICATIONS_LIST, null);
        if (!cursor.moveToFirst()) {
            pos.xLoc = 0;
            pos.yLoc = 0;
            pos.page = 0;
        } else {
            cursor = db.rawQuery("select max(" + PAGE + ") from " + TABLE_APPLICATIONS_LIST, null);
            if (cursor.moveToFirst()) {
                int maxPage = cursor.getInt(0);
                cursor = db.rawQuery("select max(" + YLOC + ") from " + TABLE_APPLICATIONS_LIST + " where " + PAGE + " = " + Integer.toString(maxPage), null);
                cursor.moveToFirst();
                int maxY = cursor.getInt(0);
                cursor = db.rawQuery("select max(" + XLOC + ") from " + TABLE_APPLICATIONS_LIST + " where " + PAGE + " = " + Integer.toString(maxPage) + " and " + YLOC + " = " + Integer.toString(maxY), null);
                cursor.moveToFirst();
                int maxX = cursor.getInt(0);

                Preferences prefs = new Preferences(activity);
                int across = prefs.getAppsAcross();
                int down = prefs.getAppsDown();
                if (maxY >= down - 1 && maxX >= across - 1) {
                    pos.page = (maxPage + 1);
                    pos.xLoc = 0;
                    pos.yLoc = 0;
                    prefs.incrementListPages();
                } else if (maxX >= across - 1) {
                    pos.page = maxPage;
                    pos.xLoc = 0;
                    pos.yLoc = (maxY + 1);
                } else {
                    pos.page = maxPage;
                    pos.xLoc = (maxX + 1);
                    pos.yLoc = maxY;
                }
            }
        }
        db.close();
        return pos;
    }

    //ToDo: remove page if no apps remain on page after deletion
    public void removeApplicationFromList(int appID) {
        SQLiteDatabase db = this.getWritableDatabase();

        db.delete(TABLE_APPLICATIONS_LIST, APPLICATION_ID + "='" + appID + "'", null);
        db.close();
    }

    public ArrayList<App> getAllApplicationsInfo() {
        ArrayList<App> apps = new ArrayList<App>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from " + TABLE_APPLICATIONS, null);
        if (cursor.moveToFirst()) {
            while (!cursor.isAfterLast()) {
                String appName = cursor.getString(cursor.getColumnIndex(APPLICATION_NAME));
                int appID = cursor.getInt(cursor.getColumnIndex(APPLICATION_ID));
                App app = new App(appName, appID);

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

}
