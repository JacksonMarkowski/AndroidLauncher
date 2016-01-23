package jacksonmarkowski.launcher;

import android.app.Activity;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.List;

public class LoadApplicationsList {

    Activity activity;

    public LoadApplicationsList(Activity activity) {
        this.activity = activity;
    }

    public void loadApplications() {
        addApplicationIcons(getPackageNames());
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
