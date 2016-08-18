package jacksonmarkowski.launcher;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.View;

/**
 * Created by jacksonmarkowski on 8/16/16.
 */
public class Rectangle extends View {

    private Activity activity;
    Paint paint = new Paint();

    public Rectangle(Activity activity) {
        super(activity);
        this.activity = activity;
    }

    @Override
    public void onDraw(Canvas canvas) {
        Preferences prefs = new Preferences(activity);
        int width = prefs.getDefaultAppWidth();
        int height = prefs.getDefaultAppHeight();

        paint.setColor(Color.WHITE);
        paint.setStrokeWidth(8);
        paint.setStyle(Paint.Style.STROKE);
        canvas.drawRect(0, 0, width, height, paint);
    }
}
