package jacksonmarkowski.launcher;

import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;

public class ApplicationIconButton extends ImageButton {

    private int padding;
    private boolean beingMoved = false;

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

    public void addClickListener(final String packageName) {
        setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Context context = getContext();
                Intent launchIntent = context.getPackageManager().getLaunchIntentForPackage(packageName);
                context.startActivity(launchIntent);
            }
        });
    }

    public void addTouchListener(final String packageName) {
        setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Log.v("Event", event.toString());
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    return true;
                } else if (event.getAction() == MotionEvent.ACTION_MOVE){
                    long timeHeld = event.getEventTime() - event.getDownTime();
                    if (timeHeld > 1500) {
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
                        Context context = getContext();
                        Intent launchIntent = context.getPackageManager().getLaunchIntentForPackage(packageName);
                        context.startActivity(launchIntent);
                        return true;
                    }
                }
                return false;
            }
        });
    }
}
