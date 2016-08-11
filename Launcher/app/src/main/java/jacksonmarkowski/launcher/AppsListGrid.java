package jacksonmarkowski.launcher;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.GridLayout;

public class AppsListGrid extends GridLayout {

    private int pageNumber;

    public AppsListGrid(Context context) {
        super(context);
        setParams();
    }

    public AppsListGrid(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        setParams();
    }

    public void addApplication(App app) {

    }

    public void setPageNumber(int page) {
        pageNumber = page;
    }

    public int getPageNumber() {
        return pageNumber;
    }

    public void setParams() {
        setPadding(50,0,50,0);
        setColumnCount(4);
        setRowCount(5);
    }
}
