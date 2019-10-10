package m_fusilsolutions.com.dailyfinance.Models;

public class ReportData {

    private String Date="";
    private String Name="";
    private String MobileNo="";
    private String RefNo="";
    private String Amount="";
    private String NetAmount="";
    private String PerDayAmt="";
    private String Remarks="";
    private String VSNo="";

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

    public String getVSNo() {
        return VSNo;
    }

    public void setVSNo(String VSNo) {
        this.VSNo = VSNo;
    }
}
