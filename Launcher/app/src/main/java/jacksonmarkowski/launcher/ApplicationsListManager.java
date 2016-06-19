package jacksonmarkowski.launcher;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.v4.view.ViewPager;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class ApplicationsListManager {

    private Activity activity;
    private ArrayList<ApplicationsGridList> grids;

    private String saveFileName = "listInfo";

    private int appsAcross;
    private int appsDown;
    private int pageCount;

    private ViewPager pager;

    private ArrayList<ImageView> pageIcons = new ArrayList<ImageView>();

    public ApplicationsListManager(Activity activity) {
        this.activity = activity;
    }

    public void addPagerAdapter() {
        pager = (ViewPager) activity.findViewById(R.id.listPager);
        pager.setAdapter(new ApplicationsListAdapter(grids));
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
        pager.addOnPageChangeListener(new ApplicationsListChangeListener(pageIcons));
    }

    public void updateApplicationsList() {
        if (!fileExists(saveFileName)) {
            generateBasicSaveFile();
        }
        //ToDo: reorder parse and evaluate save file
        parseSaveFile(readSaveFile());

        ApplicationsManager manager = new ApplicationsManager(activity);
        manager.updateApplicationsInfo();
        ArrayList<Application> apps = manager.getApplicationsInfo();
        loadApplicationsIntoGrid(apps);
    }

    public int getPageCount() {
        return pageCount;
    }

    public ApplicationsGridList getPage(int page) {
        if (page < grids.size()) {
            return grids.get(page);
        } else {
            return null;
        }
    }

    public ArrayList<ApplicationsGridList> getAllPages() {
        return grids;
    }

    private void loadApplicationsIntoGrid(ArrayList<Application> apps) {
        PackageManager pm = activity.getPackageManager();

        grids = new ArrayList<ApplicationsGridList>();
        for (int i = 0; i < pageCount; i++) {
            grids.add(new ApplicationsGridList(activity));
        }

        int buttonSize = Math.min((activity.getResources().getDisplayMetrics().widthPixels) / appsAcross, ((activity.getResources().getDisplayMetrics().heightPixels) / appsDown));
        int iconSize = (int)(buttonSize / 1.666);
        int paddingSize = (buttonSize - iconSize)/2;

        for (int i=0; i < apps.size(); i++) {
            Application app = apps.get(i);
            try {
                final String packageName = app.getName();
                Drawable icon = pm.getApplicationIcon(packageName);
                Bitmap iconScaled = scaleIcon(icon, iconSize);
                ApplicationIconButton button = new ApplicationIconButton(activity, paddingSize);
                button.setImageBitmap(iconScaled);
                button.addTouchListener(packageName);
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
