package m_fusilsolutions.com.dailyfinance.CustomControls;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by Android on 19-10-2019.
 */

public class CustomTextViewBold extends TextView {

    public CustomTextViewBold(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public CustomTextViewBold(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CustomTextViewBold(Context context) {
        super(context);
        init();
    }

    public void init() {
        Typeface tf = Typeface.createFromAsset(getContext().getAssets(), "Caviar_Dreams_Bold.ttf");
        setTypeface(tf ,1);
    }
}
