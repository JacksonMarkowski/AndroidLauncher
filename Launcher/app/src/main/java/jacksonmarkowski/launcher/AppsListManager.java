package jacksonmarkowski.launcher;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.support.v4.view.ViewPager;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.util.ArrayList;

public class AppsListManager {

    private Activity activity;
    private ArrayList<AppsGridList> grids;

    private ViewPager pager;

    private ArrayList<ImageView> pageIcons = new ArrayList<>();

    public AppsListManager(Activity activity) {
        this.activity = activity;
    }

    public void addPagerAdapter() {
        pager = (ViewPager) activity.findViewById(R.id.listPager);
        pager.setAdapter(new AppsListAdapter(grids));
    }

    public void addPageIndicator() {
        LinearLayout layout = (LinearLayout) activity.findViewById(R.id.listPageIndicator);

        //ToDo: set the size of dots based on screen size
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(50,50);

        //Adds page icon for the first page that will be currently selected
        ImageView pageIcon = new ImageView(activity);
        pageIcon.setImageResource(R.drawable.list_page_selected_icon);
        pageIcon.setLayoutParams(params);
        pageIcons.add(pageIcon);
        layout.addView(pageIcons.get(0));

        //Adds page icon for the remaining pages that are not currently selected;
        Preferences prefs = new Preferences(activity);
        for (int i = 1; i < prefs.getListPages(); i++) {
            pageIcon = new ImageView(activity);
            pageIcon.setImageResource(R.drawable.list_page_notselected_icon);
            pageIcon.setLayoutParams(params);
            pageIcons.add(pageIcon);
            layout.addView(pageIcons.get(i));
        }
        pager.addOnPageChangeListener(new AppsListChangeListener(pageIcons));
    }

    //ToDo: pass in the updated apps list, dont create a applicationsmanager here
    public void updateApplicationsList() {

        AppsManager manager = new AppsManager(activity);
        manager.updateApplicationsInfo();
        ArrayList<App> apps = manager.getApplicationsInfo();
        loadApplicationsIntoGrid(apps);
    }

    private void loadApplicationsIntoGrid(ArrayList<App> apps) {
        Preferences prefs = new Preferences(activity);

        grids = new ArrayList<AppsGridList>();
        for (int i = 0; i < prefs.getListPages(); i++) {
            grids.add(new AppsGridList(activity));
        }

        PackageManager pm = activity.getPackageManager();
        for (int i=0; i < apps.size(); i++) {
            App app = apps.get(i);
            try {
                final String packageName = app.getName();

                String label = (String) pm.getApplicationLabel(pm.getApplicationInfo(packageName, 0));
                AppButtonText text = new AppButtonText(activity);
                text.setText(label);

                Drawable iconDrawable = pm.getApplicationIcon(packageName);
                AppButtonIcon icon = new AppButtonIcon(activity);
                icon.setPadding(prefs.getDefaultPaddingSize());
                icon.setImageDrawableScaled(iconDrawable, prefs.getDefaultIconSize());

                AppButtonContainer container = new AppButtonContainer(activity);
                container.setDefaultParams(prefs.getDefaultAppWidth(), prefs.getDefaultAppHeight());
                container.setApp(app);
                container.addListTouchListener();
                container.setIcon(icon);
                container.setText(text);
                container.setContainerInMain(false);

                grids.get(app.getListPage()).addView(container);
            } catch (PackageManager.NameNotFoundException e) {

            }
        }
    }
}
