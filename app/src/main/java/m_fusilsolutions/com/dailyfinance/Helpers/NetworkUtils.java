package m_fusilsolutions.com.dailyfinance.Helpers;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.support.annotation.RequiresApi;

/**
 * Created by Android on 15-10-2019.
 */

public class NetworkUtils
{
    Context _ctx;
    public NetworkUtils(Context _ctx){
        this._ctx = _ctx;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public boolean IsNetworkAvailable()
    {
        ConnectivityManager connectivityManager = (ConnectivityManager) _ctx.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}
