package jacksonmarkowski.launcher;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.DragEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ViewFlipper;

import java.util.ArrayList;

public class HomeScreen extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);

        ApplicationsListManager list = new ApplicationsListManager(this);
        list.updateApplicationsList();
        list.addPagerAdapter();
        list.addPageIndicator();

        FrameLayout view = (FrameLayout) findViewById(R.id.listLayout);
        view.setOnDragListener(new View.OnDragListener() {
            @Override
            public boolean onDrag(View v, DragEvent event) {
                Log.v("Test", "Working");
                ViewFlipper flipper = (ViewFlipper) findViewById(R.id.viewFlipper);
                flipper.showNext();
                return true;
            }
        });
    }




//    public boolean onTouchEvent(MotionEvent touchevent) {
//        Log.v("Hi", "working");
//        return true;
//    }
}


