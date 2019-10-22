package m_fusilsolutions.com.dailyfinance.Utils;

import android.content.Context;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
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

    public String getCollectionXelement(DailyFinanceData data, String transid, int menuItemId){
        String xe = "<CollectionData " +
                " TransId='" + transid +
                "' MenuItemId='" + menuItemId +
                "' AgainstId='" + data.getTransId() +
                "' Date='" + getyyyyDDmmDate(data.getDate()) +
                "' Coll_Date='" + getyyyyDDmmDate(data.getTransDate()) +
                "' Name='" +
                "' MobileNo='" +
                "' RefNo='" +
                "' Amount='" + data.getPerDayAmt() +
                "' Remarks='" + data.getRemarks() +
                "' />";
        return xe;
    }

    public String getVoucherXele(int menuItemId, String transId,String vs, String vn, String date, String time) {
        String xe = "<VoucherData " +
                " TransId='" + transId +
                "' MenuItemId='" + menuItemId +
                "' VS='" + vs +
                "' VN='" + vn +
                "' UserId='" + 1 +
                "' Status='" +
                "' CreatedDate='" + date +
                "' CreatedTime='" + time +
                "' />";
        return xe;
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
                "' Status='"+ data.getStatus() +
                "' WeekOff='"+data.getWeekOffDay()+
                "' />";
        return xele;
    }

    public String getDFinanceDataByMobileNo(int menuITemId,String vs, String vn){
        return "<SearchData MenuItemId='"+menuITemId+"' VS='"+vs+"' VN='"+vn+"' />";
    }

    public String getWeekOffDaysListByTransId(String transId){
        return "<Data TransId='"+transId+"' />";
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
                "' Status='"+ data.getStatus() +
                "' WeekOff='"+data.getWeekOffDay()+
                "'/>";
        return xele;
    }

    public String getCollectionUpdateXelement(DailyFinanceData data){
        String xele = "<CollectionData " +
                " TransId='"+data.getTransId()+
                "' Date='"+getyyyyDDmmDate(data.getDate())+
                "' Remarks='"+data.getRemarks()+
                "' />";
        return xele;
    }

    public String getCollectionXelement(DailyFinanceData collectionData, DailyFinanceData data, String transid, int menuItemId) {
        String xe = "<CollectionData " +
                " TransId='" + transid +
                "' MenuItemId='" + menuItemId +
                "' AgainstId='" + data.getTransId() +
                "' Date='" + getyyyyDDmmDate(collectionData.getDate()) +
                "' Coll_Date='" + getyyyyDDmmDate(data.getTransDate()) +
                "' Name='" +
                "' MobileNo='" +
                "' RefNo='" +
                "' Amount='" + data.getPerDayAmt() +
                "' Remarks='" + data.getRemarks() +
                "' />";
        return xe;
    }

    public String getServerInputXele(int menuItemId){
        return "<Data MenuItemId='"+menuItemId+"' />";
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

//    public String getCollSaveSMSMessage(String amount,String name, List<DailyFinanceData> list, String date, String mobileNo)
//    {
//        return "Dear "+name+",\nYou Have Done "+getSelectedCount(list)+" Days Collection of Rs."+getSelectedDaysAmount(list,amount)+"/- On "+date;
//    }

    public String getFinanceSaveSMSMessage(String amount,String name, String date)
    {
        return "Dear "+name+",\n You Have Taken DailyFinance Amount of Rs."+amount+" /- On "+date;
    }

//    private double getSelectedDaysAmount(List<DailyFinanceData> weekList, String amount)
//    {
//        Double value=0.0;
//        int count = getSelectedCount(weekList);
//        value = count * (Double.parseDouble(amount));
//        return value.intValue();
//    }

    private int getSelectedCount(DailyFinanceData _colldata,List<DailyFinanceData> weekOffList)
    {
        int i=0;
        for(DailyFinanceData data : weekOffList){
            if(data.isWeekDaySelected()){
                i++;
            }
        }
        _colldata.setCollectionCount(i);
        return i;
    }
}
