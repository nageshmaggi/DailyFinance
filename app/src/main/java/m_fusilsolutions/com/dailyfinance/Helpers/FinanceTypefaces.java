package m_fusilsolutions.com.dailyfinance.Helpers;

import android.content.Context;
import android.graphics.Typeface;

/**
 * Created by Android on 17-10-2019.
 */

public class FinanceTypefaces {
    Context mCtx;
    public static Typeface typeface, typefaceBold, typefaceBoldItalic;

    public FinanceTypefaces(Context mCtx) {
        this.mCtx = mCtx;
        typeface = Typeface.createFromAsset(mCtx.getAssets(), "Caviar-Dreams.ttf");
        typefaceBoldItalic = Typeface.createFromAsset(mCtx.getAssets(), "CaviarDreams_BoldItalic.ttf");
        typefaceBold = Typeface.createFromAsset(mCtx.getAssets(), "Caviar_Dreams_Bold.ttf");
    }
}
