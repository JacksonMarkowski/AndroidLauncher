package jacksonmarkowski.launcher;

import android.app.Activity;
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
            case DragEvent.ACTION_DROP:
                AppButtonContainer container = (AppButtonContainer) event.getLocalState();
                AppButtonIcon icon = container.getIcon();
                AppButtonText text = container.getText();

                AppButtonIcon iconDuplicate = new AppButtonIcon(activity);
                iconDuplicate.setPadding(icon.getPadding());
                iconDuplicate.setImageDrawable(icon.getDrawable());

                AppButtonText textDuplicate = new AppButtonText(activity);
                textDuplicate.setText(text.getText());

                int width = container.getWidth();
                int height = container.getHeight();
                AppButtonContainer containerDuplicate = new AppButtonContainer(activity);
                containerDuplicate.setDefaultParams(width, height);
                containerDuplicate.setApp(container.getApp());
                containerDuplicate.setIcon(iconDuplicate);
                containerDuplicate.setText(textDuplicate);
                containerDuplicate.addListTouchListener();

                FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(width, height);
                params.leftMargin = (int)event.getX() - (width/2);
                params.topMargin = (int)event.getY() - (height/2);
                FrameLayout mainLayout = (FrameLayout) v;
                mainLayout.addView(containerDuplicate, params);
                break;

            default:
                break;
        }
        return true;
    }
}
