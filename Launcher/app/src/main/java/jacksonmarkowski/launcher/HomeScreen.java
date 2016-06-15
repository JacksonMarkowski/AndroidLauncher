package jacksonmarkowski.launcher;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.util.ArrayList;

public class HomeScreen extends Activity {

    private ViewPager mPager;
    private ApplicationsList mPagerAdapter;

    private ArrayList<ImageView> pageIcons = new ArrayList<ImageView>();
    private int previousPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);

        mPager = (ViewPager) findViewById(R.id.pager);
        mPagerAdapter = new ApplicationsList(this.getApplicationContext());

        LinearLayout layout = (LinearLayout) findViewById(R.id.listPageIndicator);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(50,50);

        ImageView pageIcon = new ImageView(this);
        pageIcon.setImageResource(R.drawable.list_page_selected_icon);
        //pageIcons.add(new ImageView(activity));
        pageIcon.setLayoutParams(params);
        pageIcons.add(pageIcon);
        layout.addView(pageIcons.get(0));
        int pageCount = mPagerAdapter.getPageCount();
        for (int i = 0; i < pageCount-1; i++) {
            pageIcon = new ImageView(this);
            pageIcon.setImageResource(R.drawable.list_page_notselected_icon);
            //pageIcons.add(new ImageView(activity));
            pageIcon.setLayoutParams(params);
            pageIcons.add(pageIcon);
            layout.addView(pageIcons.get(i+1));
        }
        mPager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            public void onPageSelected(int position) {
                pageIcons.get(position).setImageResource(R.drawable.list_page_selected_icon);
                pageIcons.get(previousPosition).setImageResource(R.drawable.list_page_notselected_icon);
                previousPosition = position;
            }
        });
        mPager.setAdapter(mPagerAdapter);
    }



//    public boolean onTouchEvent(MotionEvent touchevent) {
//        Log.v("Hi", "working");
//        return true;
//    }
}


