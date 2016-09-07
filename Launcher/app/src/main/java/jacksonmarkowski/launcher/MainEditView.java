package jacksonmarkowski.launcher;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.support.v4.content.res.ResourcesCompat;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

public class MainEditView extends RelativeLayout {

    Activity activity;

    public MainEditView(Activity activity) {
        super(activity);
        this.activity = activity;
        addGridCrosses();
    }

    public void addGridCrosses() {
        Preferences prefs = new Preferences(activity);
        int width = prefs.getDefaultAppWidth();
        int height = prefs.getDefaultAppHeight();
        //Drawable crossImage = ResourcesCompat.getDrawable(activity.getResources(), R.drawable.grid_edit_cross, null);

        for (int i = 1; i < prefs.getAppsAcross(); i++) {
            for (int j = 1; j < prefs.getAppsDown(); j++) {
                RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(34, 34);
                ImageView cross = new ImageView(activity);
                //cross.setImageResource(R.drawable.grid_edit_cross, 25);
                cross.setImageResource(R.drawable.grid_edit_cross);
                params.leftMargin = width*i - 17;
                params.topMargin = height*j - 17;
                addView(cross, params);
            }
        }
    }
}
