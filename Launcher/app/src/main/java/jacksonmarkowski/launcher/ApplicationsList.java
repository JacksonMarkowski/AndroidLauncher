package jacksonmarkowski.launcher;

import android.app.Activity;
import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class ApplicationsList extends PagerAdapter {

    private Context context;
    private ApplicationsListManager listM;
    private int pageCount = 0;

    public ApplicationsList(Context context) {
        this.context = context;
        listM = new ApplicationsListManager(context);
        Log.v("Loading", "once");
    }

    @Override
    public Object instantiateItem(ViewGroup collection, int position) {
        LayoutInflater inflater = LayoutInflater.from(context);
        //ViewGroup layout = (ViewGroup) inflater.inflate()
        ViewGroup layout = listM.getGridPage(pageCount);
        //Check if page is null
        pageCount++;
        collection.addView(layout);
        return layout;
    }

    @Override
    public void destroyItem(ViewGroup collection, int position, Object view) {
        collection.removeView((View)view);
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

}

