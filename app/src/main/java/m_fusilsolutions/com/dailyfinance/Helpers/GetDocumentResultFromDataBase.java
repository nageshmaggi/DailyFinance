package m_fusilsolutions.com.dailyfinance.Helpers;

import android.app.AlertDialog;
import android.content.Context;
import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

import dmax.dialog.SpotsDialog;
import m_fusilsolutions.com.dailyfinance.R;

/**
 * Created by Android on 01-11-2018.
 */

public class GetDocumentResultFromDataBase extends AsyncTask<String,String,Object>
{
    Context _context;
    AlertDialog pd;
    public AsyncResponse delegate=null;
    String _value;

    public GetDocumentResultFromDataBase(Context context, String value)
    {
        _context = context;
        _value = value;
    }

    public GetDocumentResultFromDataBase()
    {

    }

    @Override
    protected void onPreExecute()
    {
        super.onPreExecute();
        if(_context!=null) {
            pd = new SpotsDialog(_context, R.style.Custom);
            pd.show();
        }
    }

    @Override
    protected Object doInBackground(String... strings)
    {
        String spname=(String) strings[0];
        String inputxele=(String) strings[1];
        String transtype=(String) strings[2];
        String strurl=(String) strings[3];
        //String url="http://"+ strurl +"/ServerServices.svc/fusil/CommonMstForWeb?";
        String url="http://"+ strurl +"/MobileServices.svc/GetAllReportsMethod?";
        String parameters="spName="+ URLEncoder.encode(spname)+"&inputxele="+ URLEncoder.encode(inputxele)+"&transType="+ URLEncoder.encode(transtype);
        InputStream instream = null;
        Object resultdata = null;
        String ur=url+parameters;
        URL uri;
        HttpURLConnection urlConnection = null;
        try {
            uri = new URL(ur);
            urlConnection = (HttpURLConnection) uri.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.setDoInput(true);

            int code = urlConnection.getResponseCode();
            if (code == 200) {
                resultdata = "";
                instream = urlConnection.getInputStream();
                InputStreamReader isReader = new InputStreamReader(instream);
                BufferedReader br = new BufferedReader(isReader);
                String line;
                while ((line = br.readLine()) != null) {
                    System.out.println(line);
                    resultdata += line;
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultdata;
    }

    @Override
    protected void onPostExecute(Object result)
    {
        super.onPostExecute(result);
        if(pd!=null)
        {
            pd.dismiss();
            if(result!= null)
            {
                delegate.processFinishing(result,_value);
            }
            else
            {
                delegate.processFinishing(null,_value);
            }
        }
    }
}
