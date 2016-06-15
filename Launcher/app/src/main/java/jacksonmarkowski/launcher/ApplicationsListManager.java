package jacksonmarkowski.launcher;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;

public class ApplicationsListManager {

    private Context context;
    private ArrayList<ApplicationsGridList> grids;

    private String saveFileName = "listInfo";

    private int appsAcross;
    private int appsDown;
    private int pageCount;

    public ApplicationsListManager(Context context) {
        this.context = context;
    }

    public void updateApplicationsList() {
        if (!fileExists(saveFileName)) {
            generateBasicSaveFile();
        }
        parseSaveFile(readSaveFile());

        ApplicationsManager manager = new ApplicationsManager(context);
        manager.updateApplicationsInfo();
        ArrayList<Application> apps = manager.getApplicationsInfo();
        loadApplicationsIntoGrid(apps);
    }

    private boolean fileExists(String fileName) {
        File file = context.getFileStreamPath(fileName);
        return file.exists();
    }

    private void generateBasicSaveFile() {
        writeToSaveFile("AppsAcross:4,AppsDown:5,PageCount:0,");
    }

    private void writeToSaveFile(String saveString) {
        try {
            FileOutputStream outputStream = context.openFileOutput(saveFileName, context.MODE_PRIVATE);
            outputStream.write(saveString.getBytes());
            outputStream.close();
        }  catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String readSaveFile() {
        try {
            InputStream inputStream = context.openFileInput(saveFileName);
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

    public int getTotalPages() {
        return pageCount;
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
        for (int i = 0; i < pageCount; i++) {
            grids.add(new ApplicationsGridList(context));
        }

        int buttonSize = Math.min((context.getResources().getDisplayMetrics().widthPixels) / appsAcross, ((context.getResources().getDisplayMetrics().heightPixels) / appsDown));
        int iconSize = (int)(buttonSize / 1.666);
        int paddingSize = (buttonSize - iconSize)/2;

        for (int i=0; i < apps.size(); i++) {
            Application app = apps.get(i);
            try {
                final String packageName = app.getName();
                Drawable icon = pm.getApplicationIcon(packageName);
                Bitmap iconB = scaleIcon(icon, iconSize);
                ApplicationIconButton button = new ApplicationIconButton(context, paddingSize);
                button.setImageBitmap(iconB);
                //button.addClickListener(packageName);
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
}
