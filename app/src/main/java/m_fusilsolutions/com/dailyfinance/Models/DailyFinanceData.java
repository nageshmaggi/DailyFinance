package m_fusilsolutions.com.dailyfinance.Models;

/**
 * Created by Android on 03-10-2019.
 */

public class DailyFinanceData  {
    public String ServerDate="";
    public String ServerTime="";
    public String TransId="";
    public String Date="";
    public String Name="";
    public String MobileNo="";
    public String RefNo="";
    public String Amount="";
    public String NetAmount="";
    public String PerDayAmt="";
    public String Remarks="";
    public String VSNo = "";
    public int CollectionCount=0;
    public int WeekOffDay=0;
    public String TransDate="";
    public int Status=0;
    public boolean IsWeekDaySelected = false;

    public DailyFinanceData(String amt,String date){
        this.Amount = amt;
        this.Date = date;
    }

    public DailyFinanceData(){

    }



    public String getTransId() {
        return TransId;
    }
    public void setTransId(String transId) {
        TransId = transId;
    }

    
    public String getDate() {
        return Date;
    }
    public void setDate(String date) {
        Date = date;
    }

    
    public String getName() {
        return Name;
    }
    public void setName(String name) {
        Name = name;
    }

    
    public String getMobileNo() {
        return MobileNo;
    }
    public void setMobileNo(String mobileNo) {
        MobileNo = mobileNo;
    }

    
    public String getRefNo() {
        return RefNo;
    }
    public void setRefNo(String refNo) {
        RefNo = refNo;
    }

    
    public String getAmount() {
        return Amount;
    }
    public void setAmount(String amount) {
        Amount = amount;
    }

    
    public String getNetAmount() {
        return NetAmount;
    }
    public void setNetAmount(String netAmount) {
        NetAmount = netAmount;
    }

    
    public String getPerDayAmt() {
        return PerDayAmt;
    }
    public void setPerDayAmt(String perDayAmt) {
        PerDayAmt = perDayAmt;
    }

    
    public String getRemarks() {
        return Remarks;
    }
    public void setRemarks(String remarks) {
        Remarks = remarks;
    }

    public String getServerDate() {
        return ServerDate;
    }
    public void setServerDate(String serverDate) {
        ServerDate = serverDate;
    }

    public String getServerTime() {
        return ServerTime;
    }
    public void setServerTime(String serverTime) {
        ServerTime = serverTime;
    }

    public String getVSNo() {
        return VSNo;
    }
    public void setVSNo(String VSNo) {
        this.VSNo = VSNo;
    }

    public int getCollectionCount() {
        return CollectionCount;
    }
    public void setCollectionCount(int collectionCount) {
        CollectionCount = collectionCount;
    }

    public int getWeekOffDay() {
        return WeekOffDay;
    }
    public void setWeekOffDay(int weekOffDay) {
        WeekOffDay = weekOffDay;
    }

    public String getTransDate() {
        return TransDate;
    }
    public void setTransDate(String transDate) {
        TransDate = transDate;
    }

    public boolean isWeekDaySelected() {
        return IsWeekDaySelected;
    }
    public void setWeekDaySelected(boolean weekDaySelected) {
        IsWeekDaySelected = weekDaySelected;
    }

    public int getStatus() {
        return Status;
    }
    public void setStatus(int status) {
        Status = status;
    }

}
