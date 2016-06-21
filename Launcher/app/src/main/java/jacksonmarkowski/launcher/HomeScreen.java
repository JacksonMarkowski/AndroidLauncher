package jacksonmarkowski.launcher;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

public class HomeScreen extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);

        AppsListManager list = new AppsListManager(this);
        list.updateApplicationsList();
        list.addPagerAdapter();
        list.addPageIndicator();

        View v = findViewById(R.id.MainLayout);
        v.setOnDragListener(new MainLayoutDragListener(this));
    }
}


