package jacksonmarkowski.launcher;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.widget.GridLayout;

public class ApplicationsGridList extends GridLayout {

    private int pageNumber;

    public ApplicationsGridList(Context context) {
        super(context);
        setParams();
    }

    public ApplicationsGridList(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        setParams();
    }

    public void addApplication(Application app) {

    }

    public void setPageNumber(int page) {
        pageNumber = page;
    }

    public int getPageNumber() {
        return pageNumber;
    }

    public void setParams() {
        setPadding(10,0,10,0);
        setColumnCount(4);
        setRowCount(5);
    }
}
