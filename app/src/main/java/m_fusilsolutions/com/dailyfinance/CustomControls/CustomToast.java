package m_fusilsolutions.com.dailyfinance.CustomControls;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by Android on 03-10-2019.
 */

public class CustomToast
{
    Context context;
    public CustomToast(Context context){
        this.context = context;
    }

    public void ShowToast(String msg, boolean isLongTime)
    {
        if(isLongTime)
        Toast.makeText(context,msg,Toast.LENGTH_LONG).show();
        else
            Toast.makeText(context,msg,Toast.LENGTH_SHORT).show();
    }
}
