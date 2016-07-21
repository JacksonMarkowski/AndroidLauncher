package jacksonmarkowski.launcher;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

public class Preferences {

    private Activity activity;

    private static final String firstRun = "FirstRun";

    private static final String listAppsAcross = "ListAppsAcross";
    private static final String listAppsDown = "ListAppsDown";
    private static final String listPages = "ListPages";

    private static final String defaultAppWidth = "DefaultAppWidth";
    private static final String defaultAppHeight = "DefaultAppHeight";
    private static final String defaultIconSize = "DefaultIconSize";
    private static final String defaultPaddingSize = "DefaultPaddingSize";

    public Preferences(Activity activity) {
        this.activity = activity;

        SharedPreferences sharedPref = activity.getPreferences(Context.MODE_PRIVATE);
        if (sharedPref.getBoolean(firstRun, true)) {
            Log.v("first", "run");
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putBoolean(firstRun, false);
            editor.apply();
            generateDefaultAppSize();
        }


    }

    private void createDefaultPreference() {
        SharedPreferences sharedPref = activity.getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt(listAppsAcross, 4);
        editor.putInt(listAppsDown, 5);
        editor.putInt(listPages, 1);
        editor.apply();
    }

    public int getAppsAcross() {
        SharedPreferences sharedPref = activity.getPreferences(Context.MODE_PRIVATE);
        int across = sharedPref.getInt(listAppsAcross, 4);
        return across;
    }

    public void setAppsAcross(int across) {
        SharedPreferences sharedPref = activity.getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt(listAppsAcross, across);
        editor.apply();
    }

    public int getAppsDown() {
        SharedPreferences sharedPref = activity.getPreferences(Context.MODE_PRIVATE);
        int down = sharedPref.getInt(listAppsDown, 5);
        return down;
    }

    public void setAppsDown(int down) {
        SharedPreferences sharedPref = activity.getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt(listAppsDown, down);
        editor.apply();
    }

    public int getListPages() {
        SharedPreferences sharedPref = activity.getPreferences(Context.MODE_PRIVATE);
        int pages = sharedPref.getInt(listPages, 1);
        return pages;
    }

    public void setListPages(int pages) {
        SharedPreferences sharedPref = activity.getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt(listPages, pages);
        editor.apply();
    }

    public void incrementListPages() {
        int currentCount = getListPages();
        setListPages(currentCount + 1);
    }

    public void decrementListPages() {
        int currentCount = getListPages();
        setListPages(currentCount - 1);
    }

    public int getDefaultAppWidth() {
        SharedPreferences sharedPref = activity.getPreferences(Context.MODE_PRIVATE);
        int width = sharedPref.getInt(defaultAppWidth, 250);
        return width;
    }

    public int getDefaultAppHeight() {
        SharedPreferences sharedPref = activity.getPreferences(Context.MODE_PRIVATE);
        int height = sharedPref.getInt(defaultAppHeight, 250);
        return height;
    }

    public int getDefaultIconSize() {
        SharedPreferences sharedPref = activity.getPreferences(Context.MODE_PRIVATE);
        int size = sharedPref.getInt(defaultIconSize, 250);
        return size;
    }

    public int getDefaultPaddingSize() {
        SharedPreferences sharedPref = activity.getPreferences(Context.MODE_PRIVATE);
        int padding = sharedPref.getInt(defaultPaddingSize, 0);
        return padding;
    }

    public void generateDefaultAppSize() {
        //ToDo: change 100 and 325 to be the size of padding/other elements taking space
        int buttonWidth = (activity.getResources().getDisplayMetrics().widthPixels - 100) / getAppsAcross();
        int buttonHeight = (activity.getResources().getDisplayMetrics().heightPixels - 325) / getAppsDown();
        int buttonSize = Math.min(buttonWidth, buttonHeight);
        int iconSize = (int)(buttonSize / 1.54);
        int paddingSize = (buttonSize - iconSize)/2;

        SharedPreferences sharedPref = activity.getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt(defaultAppWidth, buttonWidth);
        editor.putInt(defaultAppHeight, buttonHeight);
        editor.putInt(defaultIconSize, iconSize);
        editor.putInt(defaultPaddingSize, paddingSize);
        editor.apply();
    }
}
