package m_fusilsolutions.com.dailyfinance.Helpers;

import android.content.Context;

/**
 * Created by Android on 24-07-2019.
 */

public class ExecuteDataBase {

    Context context;
    public ExecuteDataBase(Context context)
    {
        this.context = context;
    }

    public void ExecuteResult(String spName, String inputXele, String tt, String value, String url)
    {
        GetDocumentResultFromDataBase db = new GetDocumentResultFromDataBase(context,value);
        db.delegate = (AsyncResponse)context;
        db.execute(spName,inputXele,tt,url);
    }
}
