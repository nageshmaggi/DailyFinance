package m_fusilsolutions.com.dailyfinance.Helpers;

import android.content.Context;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.net.URLEncoder;

import m_fusilsolutions.com.dailyfinance.Constants.LoginUserSession;
import m_fusilsolutions.com.dailyfinance.Models.DailyFinanceData;
import m_fusilsolutions.com.fusillib.FusilHelper.SendSMS;

/**
 * Created by Android on 21-10-2019.
 */

public class DFSmsHelper
{
    Context mCtx;
    DailyFinanceData _dailyFinanceData;
    public DFSmsHelper(Context mCtx)
    {
        this.mCtx = mCtx;
    }

    public DFSmsHelper(Context mCtx,DailyFinanceData _dailyFinanceData)
    {
        this.mCtx = mCtx;
        this._dailyFinanceData = _dailyFinanceData;
    }

    public void SendSms(String msg, String mobile){
        msg = getFinanceMsg(msg);
        String SMS_URL_IP="182.18.163.39";
        String SMS_USERNAME="FUSILSTRANS";
        String SMS_SENDER_ID="FUSILS";
        String SMS_USERPASSWORD="555159";
        int number = 1;
        SendSMS sms = new SendSMS("USP_MA_CommonHelper","<MstSMS SMS='123' />","GetSMSUri","192.168.1.120:55222");
        String url = requestUrl(LoginUserSession._loginUserData.SMSUrl,mobile,msg);
        try {
            url = "http://" + SMS_URL_IP + "/spanelv2/api.php?" + "username=" + URLEncoder.encode(SMS_USERNAME, "UTF-8") + "&password=" + URLEncoder.encode(SMS_USERPASSWORD, "UTF-8") + "&to=" + URLEncoder.encode(mobile, "UTF-8") + "&from=" + URLEncoder.encode(SMS_SENDER_ID, "UTF-8") + "&message=" + URLEncoder.encode(msg, "UTF-8");
            sms.PrepareAndSendSMSProcess(number, url, mobile, msg);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    private String requestUrl(String url,String mobileNo,String msg)
    {
        if(url!=null) {
            try {
                url = url.replace("xxxfusilmobilexxx", URLEncoder.encode(mobileNo, "UTF-8"));
                url = url.replace("xxxfusilmsgxxx", URLEncoder.encode(msg, "UTF-8"));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        return url;
    }

    private String getFinanceMsg(String msg)
    {
        msg = replaceString(msg);
        return msg;
    }

    private String replaceString(String message)
    {
        if(message.contains("@@")){
            message = ReplacingTheMemberValueWithOriginalString(message,"@@","$$");
        }
        if(message.contains("@@")){
            message = replaceString(message);
        }
        return message;
    }


    public String ReplacingTheMemberValueWithOriginalString(String origStr,String fromTag,String toTag)
    {
        String subString = GetSubStringCoulmnName(origStr,fromTag,toTag);
        Object obj = GetFinanceDataByDataClass(_dailyFinanceData,subString);
        if(obj!=null)
        {
            String replcetoVal = origStr.substring(origStr.indexOf(fromTag), origStr.indexOf(toTag) + toTag.length());
            origStr = origStr.replace(replcetoVal, obj.toString());
        }
        return origStr;
    }

    public String GetSubStringCoulmnName(String origStr,String fromTag,String toTag)
    {
        String str="";
        try {
            str = origStr.substring(origStr.indexOf(fromTag)+ fromTag.length(), origStr.indexOf(toTag));
        }catch (Exception e){
            str.toString();
        }
        return str;
    }

    public Object GetFinanceDataByDataClass(Object obj, String varName) {
        Class<?> clazz = obj.getClass();
        try {
            if (clazz != null) {
                Field fieldStr = clazz.getDeclaredField(varName);
                if (fieldStr != null) {

                    return fieldStr.get(obj);
                }
            }
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }
}
