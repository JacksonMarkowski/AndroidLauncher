package jacksonmarkowski.launcher;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.GridLayout;

public class AppsGridList extends GridLayout {

    private int pageNumber;

    public AppsGridList(Context context) {
        super(context);
        setParams();
    }

    public AppsGridList(Context context, AttributeSet attributeSet) {
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
        setPadding(35,0,35,0);
        setColumnCount(4);
        setRowCount(5);
    }
}
