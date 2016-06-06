package jacksonmarkowski.launcher;

import android.app.Activity;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
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
        //ToDo: set for total number of pages
        grids.add(new ApplicationsGridList(activity));

        //ToDo: 4 and 5 should be replaced by size of grid list
        int buttonSize = Math.min((activity.getResources().getDisplayMetrics().widthPixels) / 4, ((activity.getResources().getDisplayMetrics().heightPixels) / 5));
        int iconSize = (int)(buttonSize / 1.666);
        int paddingSize = (buttonSize - iconSize)/2;

        for (int i=0; i < apps.size() && i < 20; i++) {
            Application app = apps.get(i);
            try {
                final String packageName = app.getName();
                Drawable icon = pm.getApplicationIcon(packageName);
                Bitmap iconB = scaleIcon(icon, iconSize);
                ApplicationIconButton button = new ApplicationIconButton(activity, paddingSize);
                button.setImageBitmap(iconB);
                button.addClickListener(packageName);
                grids.get(app.getListPage()).addView(button);
            } catch(PackageManager.NameNotFoundException e) {
                return;
            }

        }
        //ToDo: add all pages
        container.addView(grids.get(0));

    }

    private Bitmap scaleIcon(Drawable icon, int size) {
        Bitmap iconB = ((BitmapDrawable)icon).getBitmap();

        int originalIconWidth = iconB.getWidth();
        int originalIconHeight = iconB.getHeight();
        int newIconWidth;
        int newIconHeight;
        Bitmap square;

        if (originalIconWidth > originalIconHeight) {
            newIconHeight = size;
            newIconWidth = (newIconHeight * originalIconWidth) / originalIconHeight;
            Bitmap scaledIcon = Bitmap.createScaledBitmap(iconB, newIconWidth, newIconHeight, true);
            square = Bitmap.createBitmap(scaledIcon, scaledIcon.getWidth()/2 - scaledIcon.getHeight()/2, 0, scaledIcon.getHeight(), scaledIcon.getHeight());
        } else {
            newIconWidth = size;
            newIconHeight = (newIconWidth * originalIconHeight) / originalIconWidth;
            Bitmap scaledIcon = Bitmap.createScaledBitmap(iconB, newIconWidth, newIconHeight, true);
            square = Bitmap.createBitmap(scaledIcon, 0, scaledIcon.getHeight()/2 - scaledIcon.getWidth()/2, scaledIcon.getWidth(), scaledIcon.getWidth());
        }
        return square;
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
