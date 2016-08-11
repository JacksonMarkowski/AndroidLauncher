package jacksonmarkowski.launcher;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.content.res.ResourcesCompat;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ViewFlipper;

public class HomeScreen extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);

        addMainToListButton();

        AppsListManager list = new AppsListManager(this);
        list.updateApplicationsList();
        list.addPagerAdapter();
        list.addPageIndicator();

        View v = findViewById(R.id.mainLayout);
        v.setOnDragListener(new MainLayoutDragListener(this));
    }

    public void addMainToListButton() {
        Preferences prefs = new Preferences(this);
        AppButtonText text = new AppButtonText(this);
        text.setText("Apps");

        Drawable iconDrawable = ResourcesCompat.getDrawable(getResources(), R.drawable.apps_list_icon, null);
        AppButtonIcon icon = new AppButtonIcon(this);
        icon.setPadding(prefs.getDefaultPaddingSize());
        icon.setImageDrawableScaled(iconDrawable, prefs.getDefaultIconSize());

        MainToListButton container = new MainToListButton(this);
        container.setDefaultParams(prefs.getDefaultAppWidth(), prefs.getDefaultAppHeight());
        container.addTouchListener();
        container.setIcon(icon);
        container.setText(text);
        container.setContainerInMain(true);

        LinearLayout mainBar = (LinearLayout) findViewById(R.id.mainBar);
        mainBar.addView(container);
    }
}


