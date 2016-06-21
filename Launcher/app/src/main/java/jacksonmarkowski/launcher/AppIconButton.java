package jacksonmarkowski.launcher;

import android.app.Activity;
import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ViewFlipper;

public class AppIconButton extends ImageButton {

    private Activity activity;

    private int padding;
    private int width;
    private int height;
    private App app;

    private boolean beingMoved = false;

    public AppIconButton(Activity activity) {
        super(activity);
        this.activity = activity;
    }

    public AppIconButton(Activity activity, AttributeSet attributeSet) {
        super(activity, attributeSet);
        this.activity = activity;
    }

    public void setApp(App app) {
        this.app = app;
    }

    public App getApp() {
        return app;
    }

    public void setDefaultElements() {
        setBackgroundColor(0);
    }

    public void setPadding(int paddingSize) {
        padding = paddingSize;
        setPadding(padding, padding, padding, padding);
    }

    public int getPadding() {
        return padding;
    }

    public void setImageDrawableScaled(Drawable icon, int size) {
        setImageBitmap(scaleIcon(icon, size));
    }

    private Bitmap scaleIcon(Drawable icon, int size) {
        Bitmap iconB = ((BitmapDrawable)icon).getBitmap();

        int originalIconWidth = iconB.getWidth();
        int originalIconHeight = iconB.getHeight();
        int newIconWidth;
        int newIconHeight;
        Bitmap square;

        if (originalIconWidth > originalIconHeight) {
            newIconHeight = size;
            newIconWidth = (newIconHeight * originalIconWidth) / originalIconHeight;
            Bitmap scaledIcon = Bitmap.createScaledBitmap(iconB, newIconWidth, newIconHeight, true);
            square = Bitmap.createBitmap(scaledIcon, scaledIcon.getWidth()/2 - scaledIcon.getHeight()/2, 0, scaledIcon.getHeight(), scaledIcon.getHeight());
        } else {
            newIconWidth = size;
            newIconHeight = (newIconWidth * originalIconHeight) / originalIconWidth;
            Bitmap scaledIcon = Bitmap.createScaledBitmap(iconB, newIconWidth, newIconHeight, true);
            square = Bitmap.createBitmap(scaledIcon, 0, scaledIcon.getHeight()/2 - scaledIcon.getWidth()/2, scaledIcon.getWidth(), scaledIcon.getWidth());
        }
        return square;
    }

    public void addTouchListener() {
        final String packageName = app.getName();
        //ToDo: Make listener its own file
        //ToDo: check return types are correct
        setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Log.v("Event", event.toString());
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    return true;
                } else if (event.getAction() == MotionEvent.ACTION_MOVE) {
                    long timeHeld = event.getEventTime() - event.getDownTime();
                    if (!beingMoved && timeHeld > 1500 && withinButton(event.getX(), event.getY())) {
                        ClipData data = ClipData.newPlainText("", "");
                        DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(v);
                        v.startDrag(data, shadowBuilder, v, 0);
                        //v.setVisibility(View.INVISIBLE);

                        ViewFlipper flipper = (ViewFlipper) activity.findViewById(R.id.viewFlipper);
                        flipper.showNext();

                        beingMoved = true;
                        return true;
                    }
                    return false;
                    //ToDo: event cancel
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
}
