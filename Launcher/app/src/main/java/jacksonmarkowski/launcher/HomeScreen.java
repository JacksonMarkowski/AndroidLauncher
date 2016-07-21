package jacksonmarkowski.launcher;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.content.res.ResourcesCompat;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ViewFlipper;

public class HomeScreen extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);

        Preferences prefs = new Preferences(this);

        AppsListManager list = new AppsListManager(this);
        list.updateApplicationsList();
        list.addPagerAdapter();
        list.addPageIndicator();

        addMainToListButton();

        View v = findViewById(R.id.MainLayout);
        v.setOnDragListener(new MainLayoutDragListener(this));
    }

    public void appsListButtonClick(View view) {
        ViewFlipper flipper = (ViewFlipper) findViewById(R.id.viewFlipper);
        flipper.setInAnimation(this, R.anim.in_from_top);
        flipper.setOutAnimation(this, R.anim.out_to_bottom);
        flipper.showNext();
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


