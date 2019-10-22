package m_fusilsolutions.com.dailyfinance.Helpers;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

/**
 * Created by Android on 01-11-2018.
 */

public class SaveMechanism extends AsyncTask<String,String,Object> {
    Context _context;
    ProgressDialog pd;

    public SaveMechanism(Context context) {
        _context = context;
    }


    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        if (_context != null) {
            pd = new ProgressDialog(_context);
            pd.setTitle("Saving please wait...");
            pd.setCancelable(false);
            pd.show();
        }
    }

    @Override
    protected Object doInBackground(String... strings) {
        String spname = (String) strings[0];
        String inputxele = (String) strings[1];
        String transtype = (String) strings[2];
        String strurl = (String) strings[3];
        String url = "http://" + strurl + "/MobileServices.svc/GetAllReportsMethod?";
        String parameters = "spName=" + URLEncoder.encode(spname) + "&inputxele=" + URLEncoder.encode(inputxele) + "&transType=" + URLEncoder.encode(transtype);
        InputStream instream = null;
        Object resultdata = null;
        String ur = url + parameters;
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
    protected void onPostExecute(Object object) {
        super.onPostExecute(object);
        pd.dismiss();
    }
}
