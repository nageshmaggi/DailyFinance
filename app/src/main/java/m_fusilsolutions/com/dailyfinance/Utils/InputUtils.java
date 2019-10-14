package m_fusilsolutions.com.dailyfinance.Utils;

import android.content.Context;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import m_fusilsolutions.com.dailyfinance.Models.DailyFinanceData;

/**
 * Created by Android on 03-10-2019.
 */

public class InputUtils
{
    Context mCtx;
    SimpleDateFormat dtFddMMyyyy,dtFyyyyMMdd;
    public InputUtils(Context mCtx){
        this.mCtx = mCtx;
        dtFddMMyyyy = new SimpleDateFormat("dd-MM-yyyy", Locale.US);
        dtFyyyyMMdd = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
    }

    public String getSearchXele(String mobileNo){
        return "<SearchData MobileNo='"+mobileNo+"' />";
    }

    public String getLoginXele(String uName, String pwd) {
        String Xele = "<LoginData UserName='"+ uName +"' UserPassword='"+ pwd +"' />";
        return Xele;
    }

    public String getDFDeleteXele(String transId) {
        return "<Data TransId='"+ transId +"' />";
    }

    public String getVoucherXele(int menuItemId, String transId,String vs, String vn, String date, String time){
        String voucherXele = "<VoucherData " +
                " TransId='"+transId+
                "' MenuItemId='"+menuItemId+
                "' VS='"+vs+
                "' VN='"+vn+
                "' UserId='"+1+
                "' Status='"+
                "' CreatedDate='"+date+
                "' CreatedTime='"+time+
                "' />";
        return voucherXele;
    }

    public String getDFSaveXelement(DailyFinanceData data, String transid, int menuItemId){

        String xele = "<FinanceData " +
                " TransId='"+transid+
                "' MenuItemId='"+menuItemId+
                "' Date='"+getyyyyDDmmDate(data.getDate())+
                "' Name='"+data.getName()+
                "' MobileNo='"+data.getMobileNo()+
                "' RefNo='"+data.getRefNo()+
                "' Amount='"+data.getAmount()+
                "' NetAmount='"+data.getNetAmount()+
                "' PerDayAmt='"+data.getPerDayAmt()+
                "' Remarks='"+data.getRemarks()+
                "' Status='0' " +
                "/>";
        return xele;
    }

    public String getDFinanceDataByMobileNo(String vs, String vn){
        return "<SearchData VS='"+vs+"' VN='"+vn+"' />";
    }

    public String getDFUpdationXelement(DailyFinanceData data, String transid){

        String xele = "<Data " +
                " TransId='"+transid+
                "' Date='"+getyyyyDDmmDate(data.getDate())+
                "' Name='"+data.getName()+
                "' MobileNo='"+data.getMobileNo()+
                "' RefNo='"+data.getRefNo()+
                "' Amount='"+data.getAmount()+
                "' NetAmount='"+data.getNetAmount()+
                "' PerDayAmt='"+data.getPerDayAmt()+
                "' Remarks='"+data.getRemarks()+
                "' Status='0' " +
                "/>";
        return xele;
    }

    public String getCollectionXelement(DailyFinanceData data, String transid, int menuItemId){

        String xele = "<CollectionData " +
                " TransId='"+transid+
                "' MenuItemId='"+menuItemId+
                "' AgainstId='"+data.getTransId()+
                "' Date='"+getyyyyDDmmDate(data.getDate())+
                "' Name='"+
                "' MobileNo='"+
                "' RefNo='"+
                "' Amount='"+data.getAmount()+
                "' Remarks='"+data.getRemarks()+
                "' />";
        return xele;
    }

    public String getServerInputXele(String vs){
        return "<Data VS='"+vs+"' />";
    }

    public String getyyyyDDmmDate(String dateStr){
        Date date= null;
        try {
            date = dtFddMMyyyy.parse(dateStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return dtFyyyyMMdd.format(date);
    }

    public String getmmDDyyyyDate(String dateStr){
        Date date= null;
        try {
            date = dtFyyyyMMdd.parse(dateStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return dtFddMMyyyy.format(date);
    }
}
