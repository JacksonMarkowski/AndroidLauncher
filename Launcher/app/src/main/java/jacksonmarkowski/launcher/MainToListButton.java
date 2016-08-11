package jacksonmarkowski.launcher;

import android.app.Activity;
import android.content.ClipData;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ViewFlipper;

public class MainToListButton extends AppButtonContainer {

    private Activity activity;

    private boolean beingMoved = false;

    public MainToListButton(Activity activity) {
        super(activity);
        this.activity = activity;
    }

    public void addTouchListener() {
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
                            //ToDo: edit mainlayoutdraglistener to allow this button to be moved
                            /*
                            ClipData data = ClipData.newPlainText("", "");
                            DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(v);
                            v.startDrag(data, shadowBuilder, v, 0);
                            v.setVisibility(View.INVISIBLE);

                            beingMoved = true;
                            */
                            return true;
                        }
                        return false;
                    case MotionEvent.ACTION_CANCEL:
                        beingMoved = false;
                        return false;
                    case MotionEvent.ACTION_UP:
                        if (beingMoved) {
                            beingMoved = false;
                            return true;
                        } else {
                            if (withinButton(event.getX(), event.getY())) {
                                ViewFlipper flipper = (ViewFlipper) activity.findViewById(R.id.viewFlipper);
                                flipper.setInAnimation(activity, R.anim.in_from_top);
                                flipper.setOutAnimation(activity, R.anim.out_to_bottom);
                                flipper.showNext();
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

}
