package jacksonmarkowski.launcher;

import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageButton;

public class ApplicationIconButton extends ImageButton {

    private int padding;

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
}
