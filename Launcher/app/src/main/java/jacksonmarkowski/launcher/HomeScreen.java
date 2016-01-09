package jacksonmarkowski.launcher;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

public class HomeScreen extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);
    }

    public void addToDb(View view) {
        DbHandler db = new DbHandler(this);

        db.addApplication("TestApp");

        db.testInsert();
    }
}
