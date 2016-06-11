package jacksonmarkowski.launcher;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.RelativeLayout;

import java.util.ArrayList;

public class ApplicationsListManager {

    Context context;
    ArrayList<ApplicationsGridList> grids;

    public ApplicationsListManager(Context context) {
        this.context = context;
        updateApplicationsList();
    }

    public void updateApplicationsList() {
        ApplicationsManager manager = new ApplicationsManager(context);
        manager.updateApplicationsInfo();
        ArrayList<Application> apps = manager.getApplicationsInfo();
        loadApplicationsIntoGrid(apps);

    }

    public ApplicationsGridList getGridPage(int page) {
        if (page < grids.size()) {
            return grids.get(page);
        } else {
            return null;
        }
    }

    private void loadApplicationsIntoGrid(ArrayList<Application> apps) {
        PackageManager pm = context.getPackageManager();

        grids = new ArrayList<ApplicationsGridList>();
        //ToDo: set for total number of pages
        grids.add(new ApplicationsGridList(context));
        grids.add(new ApplicationsGridList(context));

        //ToDo: 4 and 5 should be replaced by size of grid list
        int buttonSize = Math.min((context.getResources().getDisplayMetrics().widthPixels) / 4, ((context.getResources().getDisplayMetrics().heightPixels) / 5));
        int iconSize = (int)(buttonSize / 1.666);
        int paddingSize = (buttonSize - iconSize)/2;

        for (int i=0; i < apps.size() && i < 40; i++) {
            Application app = apps.get(i);
            try {
                final String packageName = app.getName();
                Drawable icon = pm.getApplicationIcon(packageName);
                Bitmap iconB = scaleIcon(icon, iconSize);
                ApplicationIconButton button = new ApplicationIconButton(context, paddingSize);
                button.setImageBitmap(iconB);
                button.addClickListener(packageName);
                grids.get(app.getListPage()).addView(button);
            } catch (PackageManager.NameNotFoundException e) {

            }
        }
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
}
