package jacksonmarkowski.launcher;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

public class AppsListAdapter extends PagerAdapter {

    private ArrayList<AppsListGrid> pages;

    public AppsListAdapter(ArrayList<AppsListGrid> pages) {
        this.pages = pages;
    }

    @Override
    public Object instantiateItem(ViewGroup collection, int position) {
        ViewGroup layout = pages.get(position);
        //Check if page is null
        collection.addView(layout);
        return layout;
    }

    @Override
    public void destroyItem(ViewGroup collection, int position, Object view) {
        collection.removeView((View)view);
    }

    @Override
    public int getCount() {
        return pages.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

}

