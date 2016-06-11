package jacksonmarkowski.launcher;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;

public class HomeScreen extends Activity {

    private ViewPager mPager;
    private PagerAdapter mPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);

        mPager = (ViewPager) findViewById(R.id.pager);
        mPagerAdapter = new ApplicationsList(this.getApplicationContext());
        mPager.setAdapter(mPagerAdapter);
    }



//    public boolean onTouchEvent(MotionEvent touchevent) {
//        Log.v("Hi", "working");
//        return true;
//    }
}


