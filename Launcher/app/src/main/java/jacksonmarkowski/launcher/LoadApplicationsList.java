package jacksonmarkowski.launcher;

import android.app.Activity;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.List;

public class LoadApplicationsList {

    Activity activity;

    public LoadApplicationsList(Activity activity) {
        this.activity = activity;
    }

    public void loadApplications() {
        ApplicationsManager manager = new ApplicationsManager(activity);
        manager.updateApplicationsInfo();
        ArrayList<Application> apps = manager.getApplicationsInfo();
        loadApplicationsIntoGrid(apps);
        //addApplicationIcons(getPackageNames());
    }

    private void loadApplicationsIntoGrid(ArrayList<Application> apps) {
        PackageManager pm = activity.getPackageManager();
        RelativeLayout container = (RelativeLayout) activity.findViewById(R.id.applicationsListContainer);

        ArrayList<ApplicationsGridList> grids = new ArrayList<ApplicationsGridList>();
        //ToDO: set for total number of pages
        grids.add(new ApplicationsGridList(activity));

        for (int i=0; i < apps.size(); i++) {
            Application app = apps.get(i);
            try {
                final String packageName = app.getName();
                Drawable icon = pm.getApplicationIcon(packageName);
                ApplicationIconButton button = new ApplicationIconButton(activity);
                button.setImageDrawable(icon);
                button.addClickListener(packageName);
                grids.get(app.getListPage()).addView(button);
            } catch(PackageManager.NameNotFoundException e) {
                return;
            }

        }
        //ToDo: add all pages
        container.addView(grids.get(0));

    }

    private void addApplicationIcons(ArrayList<String> packageNames) {
        PackageManager pm = activity.getPackageManager();
        RelativeLayout container = (RelativeLayout) activity.findViewById(R.id.applicationsListContainer);
        ApplicationsGridList grid = new ApplicationsGridList(activity);
        container.addView(grid);

        for (int i=0; i < packageNames.size() && i < 20; i++) {
            try {
                final String packageName = packageNames.get(i);
                Drawable icon = pm.getApplicationIcon(packageName);
                ApplicationIconButton button = new ApplicationIconButton(activity);
                button.setImageDrawable(icon);
                button.addClickListener(packageName);
                grid.addView(button);
            } catch(PackageManager.NameNotFoundException e) {
                return;
            }

        }
    }
}
