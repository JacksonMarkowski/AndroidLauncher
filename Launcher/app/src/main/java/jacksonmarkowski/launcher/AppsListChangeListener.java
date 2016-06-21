package jacksonmarkowski.launcher;

import android.support.v4.view.ViewPager;
import android.widget.ImageView;

import java.util.ArrayList;

public class AppsListChangeListener extends ViewPager.SimpleOnPageChangeListener {

    private ArrayList<ImageView> pageIcons;
    private int previousPosition;

    public AppsListChangeListener(ArrayList<ImageView> pageIcons) {
        this.pageIcons = pageIcons;
    }

    public void onPageSelected(int position) {
        pageIcons.get(position).setImageResource(R.drawable.list_page_selected_icon);
        pageIcons.get(previousPosition).setImageResource(R.drawable.list_page_notselected_icon);
        previousPosition = position;
    }
}
