package jacksonmarkowski.launcher;

import android.app.Activity;
import android.view.Gravity;
import android.widget.TextView;

public class AppButtonText extends TextView {

    Activity activity;

    public AppButtonText(Activity activity) {
        super(activity);
        this.activity = activity;
        setDefaultElements();
    }

    public void setDefaultElements() {
        setTextSize(activity.getResources().getDimension(R.dimen.appButtonTextSize));
        setTextColor(0xffffffff);
        setGravity(Gravity.CENTER_HORIZONTAL);
    }
}
