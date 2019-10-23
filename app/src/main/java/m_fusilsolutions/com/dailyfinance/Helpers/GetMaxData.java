package m_fusilsolutions.com.dailyfinance.Helpers;

import android.os.AsyncTask;

import org.w3c.dom.NodeList;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

import m_fusilsolutions.com.dailyfinance.Models.DailyFinanceData;

/**
 * Created by Android on 23-10-2019.
 */

public class GetMaxData extends AsyncTask<String,String,DailyFinanceData> {

    @Override
    protected DailyFinanceData doInBackground(String... strings) {
        String spname=(String) strings[0];
        String inputxele=(String) strings[1];
        String transtype=(String) strings[2];
        String strurl=(String) strings[3];
        //String url="http://"+ strurl +"/ServerServices.svc/fusil/CommonMstForWeb?";
        String url="http://"+ strurl +"/MobileServices.svc/GetAllReportsMethod?";
        String parameters="spName="+ URLEncoder.encode(spname)+"&inputxele="+ URLEncoder.encode(inputxele)+"&transType="+ URLEncoder.encode(transtype);
        InputStream instream = null;
        DailyFinanceData _maxData = new DailyFinanceData();
        String resultdata = null;
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
                _maxData = ParseMaxData(resultdata,_maxData);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return _maxData;
    }

    private DailyFinanceData ParseMaxData(String result, DailyFinanceData infodata)
    {
        NodeList nodeList = new XmlConverter().StringToXMLformat(result);
        if(nodeList!=null){
            infodata = new XmlConverter().ParseServerInfoData(nodeList, infodata);
        }
        return infodata;
    }
}
