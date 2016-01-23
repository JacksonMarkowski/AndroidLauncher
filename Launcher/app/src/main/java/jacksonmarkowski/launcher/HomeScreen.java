package jacksonmarkowski.launcher;

import android.app.Activity;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import java.util.List;

public class HomeScreen extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);
        addToDb();
    }

    public void addToDb() {
        //DbHandler db = new DbHandler(this);
        //db.addApplication("TestApp");
        //db.testInsert();

        LoadApplicationsList loadApps = new LoadApplicationsList(this);
        loadApps.loadApplications();

    }
}
