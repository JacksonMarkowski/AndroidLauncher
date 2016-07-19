package jacksonmarkowski.launcher;

import android.app.Activity;
import android.util.Log;
import android.view.DragEvent;
import android.view.View;
import android.widget.FrameLayout;

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
            case DragEvent.ACTION_DRAG_LOCATION:
                Log.v("loc", Integer.toString(snapToGridX(event.getX())));
                break;
            case DragEvent.ACTION_DROP:
                //ToDo: have container added to grid snap to x and y
                FrameLayout mainLayout = (FrameLayout) v;

                AppButtonContainer container = (AppButtonContainer) event.getLocalState();
                int width = container.getWidth();
                int height = container.getHeight();

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
                params.leftMargin = (int)event.getX() - (width/2);
                params.topMargin = (int)event.getY() - (height/2);
                mainLayout.addView(container, params);
                break;

            default:
                break;
        }
        return true;
    }


    //ToDo: replace 20 and 10 with grid snap to size
    private int snapToGridX(float x) {
        if (x % 20 < 10) {
            return ((int)(x / 20)) * 20;
        } else {
            return ((int)(x / 20) + 1) * 20;
        }
    }

    private int snapToGridY(float y) {
        if (y % 20 < 10) {
            return ((int)(y / 20)) * 20;
        } else {
            return ((int)(y / 20) + 1) * 20;
        }
    }
}
