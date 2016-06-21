package jacksonmarkowski.launcher;

import android.app.Activity;
import android.view.DragEvent;
import android.view.View;
import android.widget.LinearLayout;

public class MainLayoutDragListener implements View.OnDragListener {

    private Activity activity;

    public MainLayoutDragListener(Activity activity) {
        this.activity = activity;
    }

    @Override
    public boolean onDrag(View v, DragEvent event) {
        AppIconButton icon = (AppIconButton) event.getLocalState();

        AppIconButton iconDuplicate = new AppIconButton(activity);
        iconDuplicate.setApp(icon.getApp());
        iconDuplicate.setImageDrawable(icon.getDrawable());
        LinearLayout container = (LinearLayout) v;
        ((LinearLayout) v).addView(iconDuplicate);
        return true;
    }
}
