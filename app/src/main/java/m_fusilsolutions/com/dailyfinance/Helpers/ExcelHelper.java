package m_fusilsolutions.com.dailyfinance.Helpers;

import java.util.List;

import jxl.format.Colour;
import jxl.write.Label;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WriteException;
import m_fusilsolutions.com.dailyfinance.Models.ReportData;

public class ExcelHelper {

    public WritableSheet FinanceReportSheet(List<ReportData> reportDataList,WritableSheet sheet){
        int i=1,rowSize = reportDataList.size(),tempvalue=1;
        int _total=0,_pdTotal=0,_netTotal=0;
        for(ReportData _data: reportDataList)
        {//new change 16102019 complete method
            _total += Integer.parseInt(_data.getAmount());
            _pdTotal += Integer.parseInt(_data.getPerDayAmt());
            _netTotal += Integer.parseInt(_data.getNetAmount());
        }
        PrepareFinanceReportColumnsData(sheet);
        for(ReportData data: reportDataList)
        {
            try {
                sheet.addCell(new Label(0,i,data.getDate()));
                sheet.addCell(new Label(1,i,data.getName()));
                sheet.addCell(new Label(2,i,data.getMobileNo()));
                sheet.addCell(new Label(3,i,data.getVSNo()));
                sheet.addCell(new Label(4,i,data.getAmount()));
                sheet.addCell(new Label(5,i,data.getNetAmount()));
                sheet.addCell(new Label(6,i,data.getPerDayAmt()));
                sheet.addCell(new Label(7,i,data.getRefNo()));
                sheet.addCell(new Label(8,i,data.getRemarks()));
                sheet.addCell(new Label(9,i,data.getStatus()));
                if(i<rowSize) {
                    i++;
                }
            } catch (WriteException e) {
                e.printStackTrace();
            }
        }
        try {//new change 16102019 complete method
            tempvalue+=rowSize+1;
            WritableFont font = new WritableFont(WritableFont.ARIAL,10, WritableFont.BOLD);
            font.setColour(Colour.BLACK);
            WritableCellFormat format = new WritableCellFormat();
            format.setFont(font);

            sheet.addCell(new Label(4,tempvalue,String.valueOf(_total),format));
            sheet.addCell(new Label(5,tempvalue,String.valueOf(_netTotal),format));
            sheet.addCell(new Label(6,tempvalue,String.valueOf(_pdTotal),format));
        } catch (WriteException e) {
            e.printStackTrace();
        }
        return sheet;
    }

    private void PrepareFinanceReportColumnsData(WritableSheet sheet){
        WritableFont font = new WritableFont(WritableFont.ARIAL,10, WritableFont.BOLD);
        try {
            font.setColour(Colour.RED);
            WritableCellFormat format = new WritableCellFormat();
            format.setFont(font);

            sheet.addCell(new Label(0,0,"Date",format));
            sheet.addCell(new Label(1,0,"Name",format));
            sheet.addCell(new Label(2,0,"MobileNo",format));
            sheet.addCell(new Label(3,0,"VSNo",format));
            sheet.addCell(new Label(4,0,"TotalAmt",format));
            sheet.addCell(new Label(5,0,"NetAmt",format));
            sheet.addCell(new Label(6,0,"PerDayAmt",format));
            sheet.addCell(new Label(7,0,"RefNo",format));
            sheet.addCell(new Label(8,0,"Remarks",format));
            sheet.addCell(new Label(9,0,"Status",format));
        } catch (WriteException e) {
            e.printStackTrace();
        }
    }

    public WritableSheet CollectionReportSheet(List<ReportData> reportDataList,WritableSheet sheet){
        int i=1,rowSize = reportDataList.size(),temp=1;
        int _total=0;
        for(ReportData _data: reportDataList)
        {//new change 16102019 complete method
            _total += Integer.parseInt(_data.getAmount());
        }
        PrepareCollectionReportColumnsData(sheet);
        for(ReportData data: reportDataList)
        {
            try {
                sheet.addCell(new Label(0,i,data.getDate()));
                sheet.addCell(new Label(1,i,data.getName()));
                sheet.addCell(new Label(2,i,data.getMobileNo()));
                sheet.addCell(new Label(3,i,data.getVSNo()));
                sheet.addCell(new Label(4,i,data.getAmount()));
                sheet.addCell(new Label(5,i,data.getRefNo()));
                sheet.addCell(new Label(6,i,data.getRemarks()));
                if(i<rowSize)
                    i++;
            } catch (WriteException e) {
                e.printStackTrace();
            }
        }
        try {//new change 16102019 complete method
            temp+=rowSize+1;
            WritableFont font = new WritableFont(WritableFont.ARIAL,10, WritableFont.BOLD);
            font.setColour(Colour.BLACK);
            WritableCellFormat format = new WritableCellFormat();
            format.setFont(font);
            sheet.addCell(new Label(4,temp,String.valueOf(_total),format));

        } catch (WriteException e) {
            e.printStackTrace();
        }
        return sheet;
    }

    private void PrepareCollectionReportColumnsData(WritableSheet sheet){
        WritableFont font = new WritableFont(WritableFont.ARIAL,10, WritableFont.BOLD);
        try {
            font.setColour(Colour.RED);
            WritableCellFormat format = new WritableCellFormat();
            format.setFont(font);

            sheet.addCell(new Label(0,0,"Date",format));
            sheet.addCell(new Label(1,0,"Name",format));
            sheet.addCell(new Label(2,0,"MobileNo",format));
            sheet.addCell(new Label(3,0,"VSNo",format));
            sheet.addCell(new Label(4,0,"TotalAmt",format));
            sheet.addCell(new Label(5,0,"RefNo",format));
            sheet.addCell(new Label(6,0,"Remarks",format));
        } catch (WriteException e) {
            e.printStackTrace();
        }
    }
}
