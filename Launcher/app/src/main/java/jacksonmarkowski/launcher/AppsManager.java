package jacksonmarkowski.launcher;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class AppsManager {

    Activity activity;

    public AppsManager(Activity activity) {
        this.activity = activity;
    }

    private ArrayList<String> getPackageNames() {
        ArrayList<String> packageNames = new ArrayList<String>();

        PackageManager pm = activity.getPackageManager();
        List<ApplicationInfo> apps = pm.getInstalledApplications(0);
        for (int i=0; i < apps.size(); i++) {
            String packageName = apps.get(i).packageName;
            if (pm.getLaunchIntentForPackage(packageName) != null) {
                packageNames.add(packageName);
            }
        }
        return packageNames;
    }

    public void updateApplicationsInfo() {
        DbHandler db = new DbHandler(activity);

        ArrayList<String> applicationsOnSystem = getPackageNames();
        ArrayList<String> applicationsOnSystemUnaltered = new ArrayList<String>(applicationsOnSystem);
        ArrayList<String> applicationsInDb = db.getAllApplications();

        //Apps not in the database but installed on the system get added to the database
        applicationsOnSystem.removeAll(applicationsInDb);
        for (int i=0; i<applicationsOnSystem.size(); i++) {
            String appName = applicationsOnSystem.get(i);

            //Adds app to apps table
            int appID = db.addApplication(appName);

            //Adds app to app list table
            db.addApplicationToList(appID);
        }

        //Removes apps in the database but no longer installed on the system
        applicationsInDb.removeAll(applicationsOnSystemUnaltered);
        for (int i=0; i<applicationsInDb.size(); i++) {
            String appName = applicationsInDb.get(i);

            int appID = db.removeApplication(appName);

            db.removeApplicationFromList(appID);

            Log.v("Remove", applicationsInDb.get(i));
        }
    }

    public ArrayList<App> getApplicationsInfo() {
        DbHandler db = new DbHandler(activity);
        return db.getAllApplicationsInfo();
    }

}
