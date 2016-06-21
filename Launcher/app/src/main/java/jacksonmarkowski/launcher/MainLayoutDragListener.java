package jacksonmarkowski.launcher;

import android.app.Activity;
import android.util.Log;
import android.view.DragEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

public class MainLayoutDragListener implements View.OnDragListener {

    private Activity activity;

    public MainLayoutDragListener(Activity activity) {
        this.activity = activity;
    }

    @Override
    public boolean onDrag(View v, DragEvent event) {
        switch (event.getAction()) {
            case DragEvent.ACTION_DRAG_STARTED:
                break;
            case DragEvent.ACTION_DROP:
                AppIconButton icon = (AppIconButton) event.getLocalState();
                int width = icon.getWidth();
                int height = icon.getHeight();

                AppIconButton iconDuplicate = new AppIconButton(activity);
                iconDuplicate.setApp(icon.getApp());
                iconDuplicate.setImageDrawable(icon.getDrawable());
                iconDuplicate.setPadding(icon.getPadding());
                iconDuplicate.addTouchListener();

                FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(width, height);
                params.leftMargin = (int)event.getX() - (width/2);
                params.topMargin = (int)event.getY() - (height/2);
                FrameLayout container = (FrameLayout) v;
                container.addView(iconDuplicate, params);
                break;

            default:
                break;
        }
        return true;
    }
}
