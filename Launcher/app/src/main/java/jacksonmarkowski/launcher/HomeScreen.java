package jacksonmarkowski.launcher;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ViewFlipper;

public class HomeScreen extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);

        Preferences prefs = new Preferences(this);
        //ToDo: prefs.generateDefaultAppSize();

        AppsListManager list = new AppsListManager(this);
        list.updateApplicationsList();
        list.addPagerAdapter();
        list.addPageIndicator();

        View v = findViewById(R.id.MainLayout);
        v.setOnDragListener(new MainLayoutDragListener(this));
    }

    public void appsListButtonClick(View view) {
        ViewFlipper flipper = (ViewFlipper) findViewById(R.id.viewFlipper);
        flipper.setInAnimation(this, R.anim.in_from_top);
        flipper.setOutAnimation(this, R.anim.out_to_bottom);
        flipper.showNext();
    }
}


