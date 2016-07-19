package jacksonmarkowski.launcher;

import android.app.ActionBar;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class AppsListManager {

    private Activity activity;
    private ArrayList<AppsGridList> grids;

    private String saveFileName = "listInfo";

    private int appsAcross;
    private int appsDown;
    private int pageCount;

    private ViewPager pager;

    private ArrayList<ImageView> pageIcons = new ArrayList<ImageView>();

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
        for (int i = 1; i < pageCount; i++) {
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
        if (!fileExists(saveFileName)) {
            generateBasicSaveFile();
        }
        //ToDo: reorder parse and evaluate save file
        parseSaveFile(readSaveFile());

        AppsManager manager = new AppsManager(activity);
        manager.updateApplicationsInfo();
        ArrayList<App> apps = manager.getApplicationsInfo();
        loadApplicationsIntoGrid(apps);
    }

    public int getPageCount() {
        return pageCount;
    }

    public AppsGridList getPage(int page) {
        if (page < grids.size()) {
            return grids.get(page);
        } else {
            return null;
        }
    }

    public ArrayList<AppsGridList> getAllPages() {
        return grids;
    }

    private void loadApplicationsIntoGrid(ArrayList<App> apps) {
        PackageManager pm = activity.getPackageManager();

        grids = new ArrayList<AppsGridList>();
        for (int i = 0; i < pageCount; i++) {
            grids.add(new AppsGridList(activity));
        }

        //ToDo: set size based on layout not screen
        //ToDo: 70 and 180 need to be replaced
        int buttonWidth = (activity.getResources().getDisplayMetrics().widthPixels - 100) / appsAcross;
        Log.v("down", Integer.toString(appsDown));
        int buttonHeight = (activity.getResources().getDisplayMetrics().heightPixels - 325) / appsDown;
        int buttonSize = Math.min(buttonWidth, buttonHeight);
        int iconSize = (int)(buttonSize / 1.54);
        int paddingSize = (buttonSize - iconSize)/2;

        for (int i=0; i < apps.size(); i++) {
            App app = apps.get(i);
            try {
                final String packageName = app.getName();

                String label = (String) pm.getApplicationLabel(pm.getApplicationInfo(packageName, 0));
                AppButtonText text = new AppButtonText(activity);
                text.setText(label);

                Drawable iconDrawable = pm.getApplicationIcon(packageName);
                AppButtonIcon icon = new AppButtonIcon(activity);
                icon.setPadding(paddingSize);
                icon.setImageDrawableScaled(iconDrawable, iconSize);

                AppButtonContainer container = new AppButtonContainer(activity);
                container.setDefaultParams(buttonWidth, buttonHeight);
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


    //ToDo: add info to preferences or new file that handles saving
    private boolean fileExists(String fileName) {
        File file = activity.getFileStreamPath(fileName);
        return file.exists();
    }

    private void generateBasicSaveFile() {
        writeToSaveFile("AppsAcross:4,AppsDown:5,PageCount:0,");
    }

    private void writeToSaveFile(String saveString) {
        try {
            FileOutputStream outputStream = activity.openFileOutput(saveFileName, activity.MODE_PRIVATE);
            outputStream.write(saveString.getBytes());
            outputStream.close();
        }  catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String readSaveFile() {
        try {
            InputStream inputStream = activity.openFileInput(saveFileName);
            if (inputStream != null) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                StringBuilder stringBuilder = new StringBuilder();
                String str;

                while((str = bufferedReader.readLine()) != null) {
                    stringBuilder.append(str);
                }

                inputStream.close();
                return stringBuilder.toString();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    private void parseSaveFile(String saveTxt) {
        boolean foundElement = false;
        int elementStart = 0;
        int elementEnd = 0;
        int dataStart = 0;
        int dataEnd = 0;
        for (int i = 0; i < saveTxt.length(); i++) {
            if (!foundElement) {
                if (saveTxt.substring(i, i+1).equals(":")) {
                    elementEnd = i;
                    dataStart = i+1;
                    foundElement = true;
                }
            } else {
                if (saveTxt.substring(i, i+1).equals(",")) {
                    dataEnd = i;
                    evaluateSaveFile(saveTxt.substring(elementStart, elementEnd), saveTxt.substring(dataStart, dataEnd));
                    elementStart = i+1;
                    foundElement = false;
                }
            }
        }
    }

    private void evaluateSaveFile(String element, String data) {
        switch (element) {
            case "AppsAcross": appsAcross = Integer.parseInt(data);
            case "AppsDown": appsDown = Integer.parseInt(data);
            case "PageCount": pageCount = Integer.parseInt(data);
        }
    }
}
