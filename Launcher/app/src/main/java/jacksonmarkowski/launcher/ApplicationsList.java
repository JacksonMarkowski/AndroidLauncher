package jacksonmarkowski.launcher;

import android.app.Activity;
import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.util.ArrayList;

public class ApplicationsList extends PagerAdapter {

    private Context context;
    private ApplicationsListManager listM;
    private int pageCount;

    public ApplicationsList(Context context) {
        this.context = context;
        listM = new ApplicationsListManager(context);
        listM.updateApplicationsList();
        pageCount = listM.getTotalPages();
    }

    public int getPageCount() {
        return pageCount;
    }

    @Override
    public Object instantiateItem(ViewGroup collection, int position) {
        ViewGroup layout = listM.getGridPage(position);
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
        return pageCount;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

}

