package jacksonmarkowski.launcher;

import android.app.Activity;
import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ViewFlipper;

public class AppButtonContainer extends LinearLayout {

    private Activity activity;
    private App app;

    private AppButtonIcon icon;
    private AppButtonText text;

    private int width;
    private int height;

    private boolean beingMoved = false;

    public AppButtonContainer(Activity activity) {
        super(activity);
        this.activity = activity;
    }

    public void setApp(App app) {
        this.app = app;
    }

    public App getApp() {
        return app;
    }

    public void setDefaultParams(int width, int height) {
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(width, height);
        setOrientation(LinearLayout.VERTICAL);
        setLayoutParams(params);

    }

    public void setIcon(AppButtonIcon icon) {
        this.icon = icon;
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.gravity = Gravity.CENTER_HORIZONTAL;
        addView(icon, params);
    }

    public AppButtonIcon getIcon() {
        return icon;
    }

    public void setText(AppButtonText text) {
        this.text = text;
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.gravity = Gravity.CENTER_HORIZONTAL;
        addView(text, params);
    }

    public AppButtonText getText() {
        return text;
    }

    public void addListTouchListener() {
        final String packageName = app.getName();
        //ToDo: Make listener its own file
        //ToDo: check return types are correct
        setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        return true;
                    case MotionEvent.ACTION_MOVE:
                        long timeHeld = event.getEventTime() - event.getDownTime();
                        if (!beingMoved && timeHeld > 1200 && withinButton(event.getX(), event.getY())) {
                            ClipData data = ClipData.newPlainText("", "");
                            DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(v);
                            v.startDrag(data, shadowBuilder, v, 0);
                            //v.setVisibility(View.INVISIBLE);

                            ViewFlipper flipper = (ViewFlipper) activity.findViewById(R.id.viewFlipper);
                            flipper.setInAnimation(activity, R.anim.in_from_bottom);
                            flipper.setOutAnimation(activity, R.anim.out_to_top);
                            flipper.showNext();

                            beingMoved = true;
                            return true;
                        }
                        return false;
                    case MotionEvent.ACTION_CANCEL:
                        beingMoved = false;
                        return true;
                    case MotionEvent.ACTION_UP:
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
                    default:
                        return false;
                }
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
