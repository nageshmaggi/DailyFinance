package m_fusilsolutions.com.dailyfinance;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import org.w3c.dom.NodeList;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import jxl.Workbook;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import m_fusilsolutions.com.dailyfinance.Adapters.PendingsPopUpAdapter;
import m_fusilsolutions.com.dailyfinance.Adapters.ReportAdapter;
import m_fusilsolutions.com.dailyfinance.Constants.Constants;
import m_fusilsolutions.com.dailyfinance.Constants.SPName;
import m_fusilsolutions.com.dailyfinance.Constants.TransType;
import m_fusilsolutions.com.dailyfinance.CustomControls.CustomToast;
import m_fusilsolutions.com.dailyfinance.Helpers.AsyncResponse;
import m_fusilsolutions.com.dailyfinance.Helpers.ExcelHelper;
import m_fusilsolutions.com.dailyfinance.Helpers.ExecuteDataBase;
import m_fusilsolutions.com.dailyfinance.Helpers.XmlConverter;
import m_fusilsolutions.com.dailyfinance.Models.DailyFinanceData;
import m_fusilsolutions.com.dailyfinance.Models.ReportData;

import static m_fusilsolutions.com.dailyfinance.Helpers.FinanceTypefaces.typefaceBold;

/**
 * Created by Android on 05-10-2019.
 */

public class DailyFinanceReport_Activity extends AppCompatActivity
        implements View.OnClickListener, AsyncResponse {

    RecyclerView rvDfReport;
    Button btnGetDf;
    EditText etFromDate, etToDate;
    TextView tvTotalAmt, tvNetAmt,tvNetTit,tvCount,
            tvR2,tvToTil,tvColAmt,
            tvR3,tvPDTil,tvPDAmt;

    public TextView tvWeekDayTotAmt,tvWeekTotCount;//new change 22102019

    final Calendar myCal = Calendar.getInstance();
    final Calendar myCalt = Calendar.getInstance();

    String fromDate, toDate,mainDate,
            NorPattern = "dd-MM-yyyy",
            DbPattern = "yyyy-MM-dd";
    boolean flagFrom = false;
    DatePickerDialog.OnDateSetListener FromdateDialog,ToDateDialog;
    CustomToast _ct;
    ExecuteDataBase _exeDb;
    List<ReportData> reportDataList;
    List<DailyFinanceData> weekOffList;//22102019
    private int _totAmt, _netAmt,_pdAmt,_totColAmt;
    int screen = 0;
    ImageView ivImgFilter,ivCalendar;
    RelativeLayout rlSumTotCol;
    View filterView;
    RadioGroup rgFilterBy;
    int selectedId = 0;
    String searchEle="";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dailyfinance_report_layout);
        Intent intent = getIntent();
        screen = intent.getIntExtra("screen",0);
        rvDfReport = (RecyclerView) findViewById(R.id.rvDfReport);
        btnGetDf = (Button) findViewById(R.id.btnGetDf);
        etFromDate = (EditText) findViewById(R.id.etFromDate);
        etToDate = (EditText) findViewById(R.id.etToDate);
        tvTotalAmt = (TextView) findViewById(R.id.tvTtlAmt);
        tvNetAmt = (TextView) findViewById(R.id.tvNtAmt);
        tvCount = (TextView) findViewById(R.id.tvReportCount);
        tvNetTit = (TextView) findViewById(R.id.tvNetTitle);
        ivImgFilter = (ImageView) findViewById(R.id.ivImgFilter);
        tvR2 = (TextView) findViewById(R.id.tvR2DF);
        ivCalendar = (ImageView) findViewById(R.id.ivCalendar);
        tvToTil = (TextView) findViewById(R.id.tvToTil);
        rlSumTotCol = (RelativeLayout) findViewById(R.id.rlCollectedSummary);//23102019
        tvR3 = (TextView) findViewById(R.id.tvR3DF);
        tvPDTil = (TextView) findViewById(R.id.tvPerDayTitle);
        tvPDAmt = (TextView) findViewById(R.id.tvPDAmt);
        tvColAmt = (TextView) findViewById(R.id.tvCollAmtSum);//23102019
        ivImgFilter.setOnClickListener(this);
        reportDataList = new ArrayList<>();
        _ct = new CustomToast(this);
        _exeDb = new ExecuteDataBase(this);
        etFromDate.setFocusable(false);
        etFromDate.setClickable(true);
        etToDate.setFocusable(false);
        etToDate.setClickable(true);
        ivImgFilter.setOnClickListener(this);
        if(screen == 2 )
        {
            rlSumTotCol.setVisibility(View.GONE);//23102019
        }
        if(screen == 3)
        {
            etFromDate.setVisibility(View.INVISIBLE);
            etToDate.setVisibility(View.INVISIBLE);
            btnGetDf.setVisibility(View.INVISIBLE);
            ivCalendar.setVisibility(View.INVISIBLE);
            tvToTil.setVisibility(View.INVISIBLE);

        }
        else
        {
            etFromDate.setVisibility(View.VISIBLE);
            etToDate.setVisibility(View.VISIBLE);
            btnGetDf.setVisibility(View.VISIBLE);
            ivCalendar.setVisibility(View.VISIBLE);
            tvToTil.setVisibility(View.VISIBLE);
        }

        setActionBar();

        btnGetDf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!fromDate.equals("") && !toDate.equals("")) {
                    String xele = "<Data FromDate='" + fromDate + "' ToDate='" + toDate + "' />";
                    if(screen==1) {
                        _exeDb.ExecuteResult(SPName.USP_MA_DF_DailyFinanceAndCollReport.toString(), xele, TransType.GetDailyFinanceReport.toString(), "1", Constants.HTTP_URL);
                    }else if(screen==2){
                        _exeDb.ExecuteResult(SPName.USP_MA_DF_DailyFinanceAndCollReport.toString(), xele, TransType.GetDailyCollectionReport.toString(), "1", Constants.HTTP_URL);
                    }
                }
            }
        });

        FromdateDialog = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker fromDatePicker, int i, int i1, int i2) {
                myCal.set(Calendar.YEAR, i);
                myCal.set(Calendar.MONTH, i1);
                myCal.set(Calendar.DAY_OF_MONTH, i2);
                UpdateDate();
            }
        };

        ToDateDialog = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker toDatePicker, int year, int month, int dayOfMonth) {
                myCalt.set(Calendar.YEAR, year);
                myCalt.set(Calendar.MONTH, month);
                myCalt.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                UpdateDate();
            }
        };

        SetDateProcess();

        etFromDate.setOnClickListener(this);
        etToDate.setOnClickListener(this);

        if(screen == 3)
            _exeDb.ExecuteResult(SPName.USP_MA_DF_ActiveAndMemwiseReport.toString(), "<Data Default='hello'/>", TransType.GetAllActiveDailyFinanceReport.toString(), "1", Constants.HTTP_URL);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater mi = getMenuInflater();
        mi.inflate(R.menu.excel_menu,menu);
        if(screen ==1 || screen == 2)
        {
            menu.findItem(R.id.miExcel).setVisible(true);
            menu.findItem(R.id.miRefersh).setVisible(false);//new change 17102019
        }
        else {//new change 17102019
            menu.findItem(R.id.miExcel).setVisible(false);
            menu.findItem(R.id.miRefersh).setVisible(true);
        }
        return true;
    }


    private void setActionBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_HOME | ActionBar.DISPLAY_SHOW_TITLE);
        actionBar.setDisplayHomeAsUpEnabled(true);
        if(screen==1) {
            actionBar.setTitle("Daily Finance Report");
        }else if(screen==2){
            tvNetAmt.setVisibility(View.INVISIBLE);
            tvR2.setVisibility(View.INVISIBLE);
            tvNetTit.setVisibility(View.INVISIBLE);
            tvPDAmt.setVisibility(View.INVISIBLE);
            tvR3.setVisibility(View.INVISIBLE);
            tvPDTil.setVisibility(View.INVISIBLE);

            actionBar.setTitle("Collection Report");
        }else if(screen==3){
            actionBar.setTitle("Active Daily Finance Report");
        }
    }

    private void SetDateProcess() {

        SimpleDateFormat sdf = new SimpleDateFormat(NorPattern, Locale.UK);
        etFromDate.setText(sdf.format(myCal.getTime()));
        etToDate.setText(sdf.format(myCal.getTime()));
        mainDate = (sdf.format(myCal.getTime()));

        SimpleDateFormat sdf1 = new SimpleDateFormat(DbPattern, Locale.UK);
        fromDate = sdf1.format(myCal.getTime());
        toDate = sdf1.format(myCal.getTime());
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.etFromDate) {
            flagFrom = true;
            CallDatePickerProcess();
        } else if (view.getId() == R.id.etToDate) {
            CallDatePickerProcess();
        }else if(view.getId() == R.id.ivImgFilter)
        {
            FilterProcess();
        }
    }

    private void FilterProcess()
    {
        final AlertDialog.Builder filterAdb = new AlertDialog.Builder(this,R.style.MyDialogTheme);
        LayoutInflater li  =  getLayoutInflater();
        filterView = li.inflate(R.layout.filter_main_dfs,null);
        filterAdb.setView(filterView);

        EditText etSearch = (EditText) filterView.findViewById(R.id.etSearch);
        rgFilterBy = (RadioGroup) filterView.findViewById(R.id.rgFilterRadioGroup);
        if(selectedId == 0)
        {
            rgFilterBy.check(R.id.rbName);
        }
        else
            rgFilterBy.check(selectedId);

        etSearch.setText(searchEle);
        rgFilterBy.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                int id = radioGroup.getCheckedRadioButtonId();
                RadioButton rBtn = (RadioButton) filterView.findViewById(id);
                String checkedBtn = rBtn.getText().toString();
                if(checkedBtn.equals("MobileNo") || checkedBtn.equals("VN"))
                    etSearch.setInputType(InputType.TYPE_CLASS_NUMBER);
                else
                    etSearch.setInputType(InputType.TYPE_CLASS_TEXT);
            }
        });
        filterAdb.setPositiveButton("Get", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                int rgId = rgFilterBy.getCheckedRadioButtonId();
                selectedId = rgId;
                if(rgId!=-1 && !etSearch.getText().toString().equals(""))
                    GetFilteredData(etSearch.getText().toString());
            }
        });

        filterAdb.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        AlertDialog ad = filterAdb.create();
        ad.setCancelable(false);
        ad.setCanceledOnTouchOutside(false);
        ad.show();
    }

    private void GetFilteredData(String searchEle) {
        int rgId = rgFilterBy.getCheckedRadioButtonId();
        RadioButton rdBtn = (RadioButton) filterView.findViewById(rgId);
        String strFilterBy = rdBtn.getText().toString();

        if(screen==1)
        {
            ProcessFilter(strFilterBy,searchEle);
        }
        else
        {
            ProcessFilter(strFilterBy,searchEle);
        }
    }

    private void ProcessFilter(String strFilterBy, String searchEle) {
        List<ReportData> FilteredReportList = new ArrayList<>();
        for(ReportData data : reportDataList)
        {
            switch(strFilterBy)
            {
                case "Name":
                    if(data.getName().toLowerCase().contains(searchEle.toLowerCase()))
                        FilteredReportList.add(data);
                    break;
                case "MobileNo":
                    if(data.getMobileNo().contains(searchEle))
                        FilteredReportList.add(data);
                    break;
                case "VN":
                    if(data.getVSNo().contains(searchEle))
                        FilteredReportList.add(data);
                    break;
                case "Remarks":
                    if(data.getRemarks().toLowerCase().contains(searchEle.toLowerCase()))
                        FilteredReportList.add(data);
                    break;
            }
        }
        if (FilteredReportList.size() > 0) {
            ReportAdapter adapter = new ReportAdapter(this, FilteredReportList,screen);
            LinearLayoutManager layoutManager = new LinearLayoutManager(this);
            rvDfReport.setLayoutManager(layoutManager);
            rvDfReport.setAdapter(adapter);
            UpdateFooterData(FilteredReportList);
            tvCount.setText(String.valueOf(FilteredReportList.size()));
        }
        else
            _ct.ShowToast("No Data Found",false);//new change 17102019
    }


    private void CallDatePickerProcess() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            if(flagFrom) {
                new DatePickerDialog(this, FromdateDialog,
                        myCal.get(Calendar.YEAR),
                        myCal.get(Calendar.MONTH),
                        myCal.get(Calendar.DAY_OF_MONTH)).show();
            }
            else{
                new DatePickerDialog(this, ToDateDialog,
                        myCalt.get(Calendar.YEAR),
                        myCalt.get(Calendar.MONTH),
                        myCalt.get(Calendar.DAY_OF_MONTH)).show();
            }
        }
    }

    private void UpdateDate() {
        if (flagFrom) {
            DateConvertionProcess(etFromDate);
            flagFrom = false;
        } else {
            DateConvertionProcess(etToDate);
        }
    }

    private void DateConvertionProcess(EditText et) {
        SimpleDateFormat sdf = new SimpleDateFormat(NorPattern, Locale.UK);
        SimpleDateFormat sdf1 = new SimpleDateFormat(DbPattern, Locale.UK);
        if (flagFrom) {
            String getDate = sdf.format(myCal.getTime());
            fromDate = sdf1.format(myCal.getTime());
            et.setText(getDate);
        }else {
            String getDate = sdf.format(myCalt.getTime());
            toDate = sdf1.format(myCalt.getTime());
            et.setText(getDate);
        }

    }

    @Override
    public void processFinishing(Object object, String val) {
        if (object != null) {
            tvCount.setText("");
            NodeList nodeList = new XmlConverter().StringToXMLformat((String) object);
            if (val.equals("1")) {
                reportDataList = new ArrayList<>();
                rvDfReport.setAdapter(null);
                reportDataList = new XmlConverter().ParseFinanceReportData(nodeList, reportDataList);
                if (reportDataList.size() > 0) {
                    ReportAdapter adapter = new ReportAdapter(this, reportDataList,screen);
                    LinearLayoutManager layoutManager = new LinearLayoutManager(this);
                    rvDfReport.setLayoutManager(layoutManager);
                    rvDfReport.setAdapter(adapter);
                    UpdateFooterData(reportDataList);
                    tvCount.setText(String.valueOf(reportDataList.size()));
                }
            }//22102019
            else if(val.equals("7")){
                weekOffList = new ArrayList<>();
                weekOffList = new XmlConverter().ParseWeekOffDaysList(nodeList,weekOffList);
                if(weekOffList!=null && weekOffList.size() > 0) {
                    ShowWeekOffDaysPopUp();
                }
            }
        }else{
            _ct.ShowToast("No data found",false);
            if(val.equals("1")){
                reportDataList = new ArrayList<>();
                rvDfReport.setAdapter(null);
                if(screen==1 || screen==3) {
                    tvTotalAmt.setText("00");
                    tvNetAmt.setText("00");
                    tvPDAmt.setText("00");
                    tvColAmt.setText("00");//23102019
                }else if(screen==2){
                    rlSumTotCol.setVisibility(View.INVISIBLE);//23102019
                    tvR2.setVisibility(View.INVISIBLE);
                    tvNetAmt.setVisibility(View.INVISIBLE);
                    tvPDAmt.setVisibility(View.INVISIBLE);
                    tvR3.setVisibility(View.INVISIBLE);
                    tvTotalAmt.setText("00");
                }
            }
            else if(val.equals("7")){//new change 22102019
                _ct.ShowToast("No Pendings Found",false);
            }
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            finish();
        }
        else if(id == R.id.miExcel){
            if(reportDataList!=null && reportDataList.size()>0) {//new change 17102019
                PrepareWorkBookProcess();
                _ct.ShowToast("Report Exported Successfully",false);
            }
            else
                _ct.ShowToast("No data found. Export Cancelled",false);
        }
        else if(id == R.id.miClear){
            reportDataList = null;//new change 17102019
            rvDfReport.setAdapter(null);
            if(screen == 1 || screen == 3)
            {
                tvCount.setText("0");
                tvNetAmt.setText("00");
                tvTotalAmt.setText("00");
                tvPDAmt.setText("00");
                tvColAmt.setText("00");//23102019
            }
            else if(screen == 2 ){
                tvCount.setText("00");
                tvTotalAmt.setText("00");
            }
        }
        else if(id == R.id.miRefersh){//new change 17102019
            if(screen == 3)
                _exeDb.ExecuteResult(SPName.USP_MA_DF_ActiveAndMemwiseReport.toString(), "<Data Default='hello'/>", TransType.GetAllActiveDailyFinanceReport.toString(), "1", Constants.HTTP_URL);
        }
        return true;
    }

    private void UpdateFooterData(List<ReportData> reportDataList) {
        _totAmt = 0;
        _netAmt = 0;
        _pdAmt  = 0;
        _totColAmt = 0;
        if (reportDataList != null && reportDataList.size() > 0) {
            for (ReportData data : reportDataList) {
                if (data != null) {
                    if(screen==1 || screen==3) {
                        _totAmt += (Integer.parseInt(data.getAmount()));
                        _netAmt += (Integer.parseInt(data.getNetAmount()));
                        _pdAmt  += (Integer.parseInt(data.getPerDayAmt()));
                        _totColAmt += (Integer.parseInt(data.getCollAmt()));//23102019
                    }else if(screen==2){
                        _totAmt += (Integer.parseInt(data.getAmount()));
                    }
                }
            }
        }
        if(screen==1 || screen==3) {
            tvTotalAmt.setText(String.valueOf(_totAmt));
            tvNetAmt.setText(String.valueOf(_netAmt));
            tvPDAmt.setText(String.valueOf(_pdAmt));
            tvColAmt.setText(String.valueOf(_totColAmt));//23102019
        }else if(screen==2){
            tvNetAmt.setVisibility(View.INVISIBLE);
            tvR2.setVisibility(View.INVISIBLE);
            tvTotalAmt.setText(String.valueOf(_totAmt));
            tvPDAmt.setVisibility(View.INVISIBLE);
            tvR3.setVisibility(View.INVISIBLE);
        }
    }

    private void PrepareWorkBookProcess(){
        Date currentTime = Calendar.getInstance().getTime();
        SimpleDateFormat dateFormat = new SimpleDateFormat("hh.mm.ss aa");
        String time = dateFormat.format(currentTime);

        String fileName="";
        String Name="";
        if(screen == 1)
            Name = "DailyFinanceReport";
        else if(screen == 2)
            Name = "DailyCollectionReport";
        else if(screen == 3)
            Name = "ActiveFinancesReport";

        fileName = Name+"_"+mainDate+"_"+time+".xls";
        File dir = new File(Environment.getExternalStorageDirectory().getPath()+"/DailyFinance/");
        if(!dir.exists())
            dir.mkdir();
        File file = new File(dir,fileName);
        ExcelHelper Xl = new ExcelHelper();
        WritableWorkbook workbook;
        try {
            workbook = Workbook.createWorkbook(file);
            WritableSheet sheet = workbook.createSheet(fileName,0);
            if(screen == 1)
                Xl.FinanceReportSheet(reportDataList,sheet);
            else if(screen == 2 )
                Xl.CollectionReportSheet(reportDataList,sheet);
            workbook.write();
            workbook.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (WriteException e) {
            e.printStackTrace();
        }
    }

    public void SetRecyclerViewItem(ReportData data) {
        if (data != null)
        {
            Intent in = new Intent(this, Memberwise_CollectionReport_Activity.class);
            in.putExtra("TransId",data.getTransId());
            startActivity(in);
        }
    }
//22102019
    public void ShowPendingsProcess(ReportData data) {

        String xele =  "<Data TransId='" + data.getTransId() + "' />";
        _exeDb.ExecuteResult(SPName.USP_MA_DF_FinanceCollectionDates.toString(), xele, TransType.GetFinanceCollectionDates.toString(), "7", Constants.HTTP_URL);
    }

    private void ShowWeekOffDaysPopUp() {
        final boolean[] flag = {false};
        AlertDialog.Builder adb = new AlertDialog.Builder(this,R.style.MyDialogTheme);
        LayoutInflater li = getLayoutInflater();
        View lv = li.inflate(R.layout.week_off_layout_dialog,null);
        adb.setView(lv);
        RecyclerView rv = lv.findViewById(R.id.recyclerView);
        TextView tvdate = lv.findViewById(R.id.tvDate);
        tvWeekDayTotAmt = lv.findViewById(R.id.tvFtrAmount);
        tvWeekTotCount = lv.findViewById(R.id.tvFtrCount);
        TextView tvAmt = lv.findViewById(R.id.tvAmount);
        TextView tvSelectAll = lv.findViewById(R.id.tvSelectAll);
        tvSelectAll.setVisibility(View.GONE);//22102019
        CheckBox cb = lv.findViewById(R.id.cbSelectAll);
        cb.setVisibility(View.GONE);//22102019
        Button btnOk = lv.findViewById(R.id.btnOk);//22102019
        btnOk.setVisibility(View.GONE);//2210219
        ImageView imgclose = lv.findViewById(R.id.imgV_close);
        TextView tvTit = lv.findViewById(R.id.titWeek);
        tvdate.setTypeface(typefaceBold);
        tvAmt.setTypeface(typefaceBold);
        tvTit.setTypeface(typefaceBold);
        tvSelectAll.setTypeface(typefaceBold);
        tvWeekDayTotAmt.setTypeface(typefaceBold);
        tvWeekTotCount.setTypeface(typefaceBold);
        tvWeekDayTotAmt.setText(String.valueOf(getTotalAmtOfWeekOfList().intValue()));
        tvWeekTotCount.setText(String.valueOf(weekOffList.size()));
        PendingsPopUpAdapter adapter = new PendingsPopUpAdapter(this,weekOffList);
        rv.setAdapter(adapter);
        rv.setLayoutManager(new LinearLayoutManager(this));
        rv.setHasFixedSize(true);
        rv.setNestedScrollingEnabled(false);
        adb.setView(lv);
        adb.setCancelable(false);
        AlertDialog dialog = adb.create();
        dialog.show();
        imgclose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }
    private Double getTotalAmtOfWeekOfList()
    {
        double val =0;
        for(DailyFinanceData data : weekOffList)
        {
            val = val+Double.parseDouble(data.getAmount());
        }
        return val;
    }
}
