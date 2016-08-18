package jacksonmarkowski.launcher;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;
import android.view.DragEvent;
import android.view.View;
import android.widget.FrameLayout;

public class MainLayoutDragListener implements View.OnDragListener {

    private Activity activity;
    FrameLayout.LayoutParams params2 = new FrameLayout.LayoutParams(500, 500);
    int x = 0;


    public MainLayoutDragListener(Activity activity) {
        this.activity = activity;
    }

    @Override
    public boolean onDrag(View v, DragEvent event) {
        switch (event.getAction()) {
            case DragEvent.ACTION_DRAG_STARTED:
                FrameLayout mainLayout = (FrameLayout) v;

                params2.leftMargin = 5000;
                params2.topMargin = 5000;
                Rectangle rec = new Rectangle(activity);
                rec.setBackgroundColor(0);
                mainLayout.addView(rec, params2);
                break;
            case DragEvent.ACTION_DRAG_LOCATION:
                AppButtonContainer container = (AppButtonContainer) event.getLocalState();
                int width = container.getWidth();
                int height = container.getHeight();

                Preferences prefs = new Preferences(activity);
                int size = prefs.getDefaultIconSize();
                mainLayout = (FrameLayout) v;
                params2.leftMargin = snapToGridX(event.getX()) - width;
                params2.topMargin = snapToGridY(event.getY()) - height;
                mainLayout.requestLayout();
                break;
            case DragEvent.ACTION_DROP:
                //ToDo: have container added to grid snap to x and y
                mainLayout = (FrameLayout) v;

                container = (AppButtonContainer) event.getLocalState();
                width = container.getWidth();
                height = container.getHeight();

                if (container.getContainerInMain()) {
                    container.setVisibility(View.VISIBLE);
                    mainLayout.removeView(container);
                } else {
                    AppButtonIcon icon = container.getIcon();
                    AppButtonText text = container.getText();

                    AppButtonIcon iconDuplicate = new AppButtonIcon(activity);
                    iconDuplicate.setPadding(icon.getPadding());
                    iconDuplicate.setImageDrawable(icon.getDrawable());

                    AppButtonText textDuplicate = new AppButtonText(activity);
                    textDuplicate.setText(text.getText());

                    AppButtonContainer containerDuplicate = new AppButtonContainer(activity);
                    containerDuplicate.setDefaultParams(width, height);
                    containerDuplicate.setApp(container.getApp());
                    containerDuplicate.setIcon(iconDuplicate);
                    containerDuplicate.setText(textDuplicate);
                    containerDuplicate.addMainTouchListener();
                    containerDuplicate.setContainerInMain(true);
                    container = containerDuplicate;
                }
                FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(width, height);
                params.leftMargin = snapToGridX(event.getX()) - width;
                params.topMargin = snapToGridY(event.getY()) - height;
                mainLayout.addView(container, params);

                break;

            default:
                break;
        }
        return true;
    }


    //ToDo: replace 20 and 10 with grid snap to size
    private int snapToGridX(float x) {
        if (x % 50 < 25) {
            return ((int)(x / 50)) * 50;
        } else {
            return ((int)(x / 50) + 1) * 50;
        }
    }

    private int snapToGridY(float y) {
        if (y % 50 < 25) {
            return ((int)(y / 50)) * 50;
        } else {
            return ((int)(y / 50) + 1) * 50;
        }
    }
}
