package jacksonmarkowski.launcher;

import android.app.Activity;
import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ViewFlipper;

public class ApplicationIconButton extends ImageButton {

    private int padding;
    private boolean beingMoved = false;

    private int width;
    private int height;

    public ApplicationIconButton(Context context) {
        super(context);
        setElements();
    }

    public ApplicationIconButton(Context context, int padding) {
        super(context);
        this.padding = padding;
        setElements();
    }

    public ApplicationIconButton(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        setElements();
    }

    public void setElements() {
        setBackgroundColor(0);
        setPadding(padding, padding, padding, padding);
    }

    public boolean withinButton(float x, float y) {
        //ToDo: set and update width/height outside this method
        width = getWidth();
        height = getHeight();
        if (x > 0 && x < width && y > 0 && y < height) {
            return true;
        } else {
            return false;
        }
    }

    public void addTouchListener(final String packageName) {
        //ToDo: Make listener its own file
        //ToDo: check return types are correct
        setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                //Log.v("Event", event.toString());
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    return true;
                } else if (event.getAction() == MotionEvent.ACTION_MOVE){
                    long timeHeld = event.getEventTime() - event.getDownTime();
                    if (timeHeld > 1500 && withinButton(event.getX(), event.getY())) {
                        ClipData data = ClipData.newPlainText("", "");
                        DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(v);
                        v.startDrag(data, shadowBuilder, v, 0);
                        v.setVisibility(View.INVISIBLE);

                        beingMoved = true;
                        return true;
                    }
                    return false;
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (beingMoved) {
                        beingMoved = false;
                        return true;
                    } else {
                        if (withinButton(event.getX(), event.getY())) {
                            Context context = getContext();
                            Intent launchIntent = context.getPackageManager().getLaunchIntentForPackage(packageName);
                            context.startActivity(launchIntent);
                            return true;
                        }
                        return false;
                    }
                }
                return false;
            }
        });
    }
}
