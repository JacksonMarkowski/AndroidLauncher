package jacksonmarkowski.launcher;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.GridLayout;
import android.widget.RelativeLayout;

public class AppsListGrid extends RelativeLayout {

    Activity activity;

    private int pageNumber;

    public AppsListGrid(Activity activity) {
        super(activity);
        this.activity = activity;
        setParams();
    }

    public AppsListGrid(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        setParams();
    }

    public void addApplication(App app) {

    }

    public void setPageNumber(int page) {
        pageNumber = page;
    }

    public int getPageNumber() {
        return pageNumber;
    }

    public void addApp(App app) {
        int xLoc = app.getListXLoc();
        int yLoc = app.getListYLoc();
    }

    //ToDo: clean up, move sizing into preferences class
    public void addView(AppButtonContainer child) {
        Preferences prefs = new Preferences(activity);

        int cellWidth = (activity.getResources().getDisplayMetrics().widthPixels - 100) / prefs.getAppsAcross();
        int cellHeight = (activity.getResources().getDisplayMetrics().heightPixels - 325) / prefs.getAppsDown();

        App app = child.getApp();
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(child.getSetWidth(), child.getSetHeight());
        params.leftMargin = cellWidth * app.getListXLoc();
        params.topMargin = cellHeight * app.getListYLoc();
        addView(child, params);
    }

    public void setParams() {
        setPadding(50, 0, 50, 0);
    }
}
