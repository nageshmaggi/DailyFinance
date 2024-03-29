package m_fusilsolutions.com.dailyfinance;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import org.w3c.dom.NodeList;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import m_fusilsolutions.com.dailyfinance.Adapters.MemberWiseAdapter;
import m_fusilsolutions.com.dailyfinance.Adapters.PendingsPopUpAdapter;
import m_fusilsolutions.com.dailyfinance.Adapters.ReportAdapter;
import m_fusilsolutions.com.dailyfinance.Constants.Constants;
import m_fusilsolutions.com.dailyfinance.Constants.SPName;
import m_fusilsolutions.com.dailyfinance.Constants.TransType;
import m_fusilsolutions.com.dailyfinance.CustomControls.CustomToast;
import m_fusilsolutions.com.dailyfinance.Helpers.AsyncResponse;
import m_fusilsolutions.com.dailyfinance.Helpers.ExecuteDataBase;
import m_fusilsolutions.com.dailyfinance.Helpers.XmlConverter;
import m_fusilsolutions.com.dailyfinance.Models.DailyFinanceData;
import m_fusilsolutions.com.dailyfinance.Models.ReportData;
import m_fusilsolutions.com.dailyfinance.Singletons.FinanceSingleTon;

public class Memberwise_DailyFinaceReport_Activity extends AppCompatActivity implements View.OnClickListener, AsyncResponse {

    EditText etFrom,etTo,etFilterSearch;
    AutoCompleteTextView etSearch;//new change 18102019
    ImageView ivBack,ivFilter;
    RecyclerView rvReports;
    TextView tvTtlAmt,tvNetAmt,tvPerDayAmt,tvNetTil,tvPDTil,
            tvR2,tvR3,tvReportCount,tvColAmt;
    RadioGroup rgFilterItems;
    CustomToast _ct;//new change 17102019
    public TextView tvWeekDayTotAmt,tvWeekTotCount;//new change 22102019
    RelativeLayout rlSumTotCol;//23100219

    List<ReportData> reportDataList;
    List<DailyFinanceData> weekOffList;//22102019
    ExecuteDataBase _exeDb;
    int flag = 0,screen = 0,selectedId = 0;
    String SearchEle="";
    boolean Clicked = true, flagFrom = false;
    SimpleAdapter mAdapter;

    DatePickerDialog.OnDateSetListener FromdateDialog,ToDateDialog;
    final Calendar myCal = Calendar.getInstance();
    final Calendar myCalt = Calendar.getInstance();

    String fromDate, toDate,mainDate,
            NorPattern = "dd-MM-yyyy",
            DbPattern = "yyyy-MM-dd";
    String btnName="Date",strName="",strMobileNo="";
    Typeface  typefaceBold;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.memberwise_dailyfinace_report);
        setActionBar();
        _exeDb = new ExecuteDataBase(this);
        etSearch = (AutoCompleteTextView) findViewById(R.id.etSearchMD);
        //btnSearch = (Button) findViewById(R.id.btnSearchMD);
        rvReports = (RecyclerView) findViewById(R.id.rvReportMD);
        tvNetAmt = (TextView) findViewById(R.id.tvNtAmtMD);
        tvReportCount = (TextView) findViewById(R.id.tvReportCountMD);
        tvTtlAmt = (TextView) findViewById(R.id.tvTtlAmtMD);
        tvPerDayAmt = (TextView) findViewById(R.id.tvPerDayAmtMD);
        ivBack = (ImageView) findViewById(R.id.ivBackMD);
        rlSumTotCol = (RelativeLayout) findViewById(R.id.rlCollectedSummary);//23102019
        tvColAmt = (TextView) findViewById(R.id.tvCollAmtSum);//23102019
        tvNetTil = (TextView) findViewById(R.id.tvNetTil);
        tvPDTil = (TextView) findViewById(R.id.tvPerDayTil);
        tvR2 = (TextView) findViewById(R.id.tvR2);
        tvR3 = (TextView) findViewById(R.id.tvR3);
        ivFilter = (ImageView) findViewById(R.id.ivFilterMD);
        _ct = new CustomToast(this);//new change 17102019
        ivBack.setEnabled(false);
        ivFilter.setEnabled(false);
        ivFilter.setOnClickListener(this);
        //btnSearch.setOnClickListener(this);
        ivBack.setOnClickListener(this);

        ProcessViews();

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

        GetDistinctMemberList();
        typefaceBold = Typeface.createFromAsset(getAssets(), "Caviar_Dreams_Bold.ttf");
        etSearch.setTypeface(typefaceBold);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater mi = getMenuInflater();
        mi.inflate(R.menu.excel_menu,menu);
        menu.findItem(R.id.miExcel).setVisible(false);
        return true;
    }

    private void ProcessViews() {
        rlSumTotCol.setVisibility(View.VISIBLE);//23102019
        ivBack.setEnabled(false);
        ivFilter.setEnabled(false);
        ivFilter.setVisibility(View.INVISIBLE);
        ivBack.setVisibility(View.INVISIBLE);
        etSearch.setVisibility(View.VISIBLE);
        etSearch.requestFocus();
        //btnSearch.setVisibility(View.INVISIBLE);//new change 181012019
        //btnSearch.setEnabled(false);//new change 18102019
        tvPerDayAmt.setVisibility(View.VISIBLE);
        tvNetAmt.setVisibility(View.VISIBLE);

        tvR2.setVisibility(View.VISIBLE);
        tvR3.setVisibility(View.VISIBLE);
        tvReportCount.setVisibility(View.INVISIBLE);
    }

    private void setActionBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_HOME | ActionBar.DISPLAY_SHOW_TITLE);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle("Member Wise Finance Report");
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            finish();
        }
        else if(id == R.id.miClear){
            if(screen == 1) {
                reportDataList = null;
                rvReports.setAdapter(null);
            }
            else if(screen == 2) {
                reportDataList=null;
                rvReports.setAdapter(null);
                tvReportCount.setText("0");
                tvTtlAmt.setText("00");
            }
            tvTtlAmt.setText("00");
            tvNetAmt.setText("00");
            tvPerDayAmt.setText("00");
            tvColAmt.setText("00");//23102019
        }
        return true;
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.btnSearchMD)
        {
            reportDataList = new ArrayList<>();
            SearchEle = etSearch.getText().toString();
            etSearch.clearFocus();
            //GetDataDF();
        }
        else if(v.getId() == R.id.ivBackMD){
            Clicked = true;
            ProcessViews();
            //rvReports.setAdapter(null);
            GetDataDF(strName,strMobileNo);
        }
        else if(v.getId() == R.id.ivFilterMD) {
            FilterCollectionProcess();
        }
        if (v.getId() == R.id.etFromMW) {
            flagFrom = true;
            CallDatePickerProcess();
        }
        else if (v.getId() == R.id.etToMW) {
            CallDatePickerProcess();
        }
    }

    //new change 18102019
    private void GetDistinctMemberList(){
        String inpXele = "<Data><StatusData Status='0' /><StatusData Status='1' /></Data>";
        _exeDb.ExecuteResult(SPName.USP_MA_DF_DistinctFinanceMembers.toString(),inpXele,TransType.GetDistinctFinaceUsers.toString(),"0",Constants.HTTP_URL);
    }

    private void FilterCollectionProcess() {
        int c = 1;

        AlertDialog.Builder adb = new AlertDialog.Builder(this,R.style.MyDialogTheme);
        LayoutInflater li = getLayoutInflater();
        View lv = li.inflate(R.layout.filter_memwise,null);
        adb.setView(lv);

        rgFilterItems = (RadioGroup) lv.findViewById(R.id.rgFilterRadioGroupMD);
        etFrom = (EditText) lv.findViewById(R.id.etFromMW);
        etTo = (EditText) lv.findViewById(R.id.etToMW);
        etFilterSearch = (EditText) lv.findViewById(R.id.etSearchMDF);
        RelativeLayout rlDates = (RelativeLayout) lv.findViewById(R.id.rlDateFields);
        etFilterSearch.setVisibility(View.INVISIBLE);
        rlDates.setVisibility(View.INVISIBLE);
        if(selectedId == 0)
        {

            rgFilterItems.check(R.id.rbDateMD);
            etFilterSearch.setVisibility(View.INVISIBLE);
            rlDates.setVisibility(View.VISIBLE);
        }
        else {
            rgFilterItems.check(selectedId);
            if(!btnName.equals("Date"))
                etFilterSearch.setVisibility(View.VISIBLE);
            else
                rlDates.setVisibility(View.VISIBLE);
        }

        etFrom.setFocusable(false);
        etFrom.setClickable(true);
        etTo.setFocusable(false);
        etTo.setClickable(true);
        etFrom.setOnClickListener(this);
        etTo.setOnClickListener(this);

        rgFilterItems.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                int id = radioGroup.getCheckedRadioButtonId();
                RadioButton rBtn = (RadioButton) lv.findViewById(id);
                String checkedBtn = rBtn.getText().toString();
                btnName = checkedBtn;
                if(checkedBtn.equals("VN") || checkedBtn.equals("Remarks")) {
                    rlDates.setVisibility(View.INVISIBLE);
                    etFilterSearch.setVisibility(View.VISIBLE);
                    if(checkedBtn.equals("VN"))
                        etFilterSearch.setInputType(InputType.TYPE_CLASS_NUMBER);
                    else
                        etFilterSearch.setInputType(InputType.TYPE_CLASS_TEXT);
                }
                else {
                    etFilterSearch.setVisibility(View.INVISIBLE);
                    rlDates.setVisibility(View.VISIBLE);
                    etFilterSearch.setInputType(InputType.TYPE_CLASS_TEXT);
                }
            }
        });

        if(c<=1){
            SetDateProcess();
            c++;
        }
        adb.setPositiveButton("Get", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                int rgId = rgFilterItems.getCheckedRadioButtonId();
                selectedId = rgId;
                //if(rgId!=-1 && !etSearch.getText().toString().equals(""))
                if(rgId!=-1)
                    if(btnName.equals("Date"))
                        FilterProcess();
                    else {
                        String ele = etFilterSearch.getText().toString();
                        FilterProcessByText(ele,lv);
                    }
            }
        });

        adb.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        AlertDialog ad = adb.create();
        ad.setCancelable(false);
        ad.setCanceledOnTouchOutside(false);
        ad.show();
    }

    private void FilterProcessByText(String ele, View lv) {
        int rgId = rgFilterItems.getCheckedRadioButtonId();
        RadioButton rdBtn = (RadioButton) lv.findViewById(rgId);
        String strFilterBy = rdBtn.getText().toString();

        if(screen==1)
        {
            ProcessFilter(strFilterBy,ele);
        }
        else
        {
            ProcessFilter(strFilterBy,ele);
        }
    }

    private void ProcessFilter(String strFilterBy, String ele) {
        List<ReportData> FilteredReportList = new ArrayList<>();
        for(ReportData data : reportDataList)
        {
            switch(strFilterBy)
            {
                case "VN":
                    if(data.getVSNo().contains(ele))
                        FilteredReportList.add(data);
                    break;
                case "Remarks":
                    if(data.getRemarks().toLowerCase().contains(ele.toLowerCase()))
                        FilteredReportList.add(data);
                    break;
            }
        }
        if (FilteredReportList.size() > 0) {
            ReportAdapter adapter = new ReportAdapter(this, FilteredReportList,screen);
            LinearLayoutManager layoutManager = new LinearLayoutManager(this);
            rvReports.setLayoutManager(layoutManager);
            rvReports.setAdapter(adapter);
            UpdateFooterData(FilteredReportList);
        }
        else
        {
            _ct.ShowToast("No data Found",false);//new change 17102019
        }
    }


    private void FilterProcess() {
        SimpleDateFormat format = new SimpleDateFormat(NorPattern, Locale.UK);
        List<ReportData> FilteredList = new ArrayList<>();
        try {
            Date sDate = format.parse(etFrom.getText().toString()),eDate = format.parse(etTo.getText().toString());
            for(ReportData data: reportDataList)
            {
                Date fDate = format.parse(data.getDate());
                if(fDate.equals(sDate) || fDate.after(sDate) && fDate.before(eDate) || fDate.equals(eDate)){
                    FilteredList.add(data);
                }
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (FilteredList.size() > 0) {
            ReportAdapter adapter = new ReportAdapter(this, FilteredList,screen);
            LinearLayoutManager layoutManager = new LinearLayoutManager(this);
            rvReports.setLayoutManager(layoutManager);
            rvReports.setAdapter(adapter);
            UpdateFooterData(FilteredList);
        }
        else
            _ct.ShowToast("No Data Found",false);//new change 17102019
    }

    private void SetDateProcess() {

        SimpleDateFormat sdf = new SimpleDateFormat(NorPattern, Locale.UK);
        etFrom.setText(sdf.format(myCal.getTime()));
        etTo.setText(sdf.format(myCalt.getTime()));
        //mainDate = (sdf.format(myCal.getTime()));

        SimpleDateFormat sdf1 = new SimpleDateFormat(DbPattern, Locale.UK);
        fromDate = sdf1.format(myCal.getTime());
        toDate = sdf1.format(myCal.getTime());
    }

    private void GetDataDF(String name,String mNo) {//new change 18102019
//        if (SearchEle.matches("[a-zA-Z]+"))
//            flag = 1;

        //if (!SearchEle.equals("")) { new change 18102019
        String xele = "<Data Name='" + name + "' MobileNo='" + mNo + "' />";
        _exeDb.ExecuteResult(SPName.USP_MA_DF_ActiveAndMemwiseReport.toString(), xele, TransType.GetMemWiseReportData.toString(), "1", Constants.HTTP_URL);
        //}
    }

    @Override
    public void processFinishing(Object object, String val) {
        if (object != null) {
            NodeList nodeList = new XmlConverter().StringToXMLformat((String) object);
            if (val.equals("1")) {
                reportDataList = new ArrayList<>();
                rvReports.setAdapter(null);
                reportDataList = new XmlConverter().ParseFinanceReportData(nodeList, reportDataList);
                if (reportDataList.size() > 0) {
                    MemberWiseAdapter adapter = new MemberWiseAdapter(this, reportDataList,1);
                    LinearLayoutManager layoutManager = new LinearLayoutManager(this);
                    rvReports.setLayoutManager(layoutManager);
                    rvReports.setAdapter(adapter);
                    screen =1;
                    UpdateFooterData(reportDataList);
                }
            }
            else if(val.equals("2")){
                reportDataList = new ArrayList<>();
                rvReports.setAdapter(null);
                reportDataList = new XmlConverter().ParseFinanceReportData(nodeList, reportDataList);
                if (reportDataList.size() > 0) {
                    MemberWiseAdapter adapter = new MemberWiseAdapter(this, reportDataList,0);
                    LinearLayoutManager layoutManager = new LinearLayoutManager(this);
                    rvReports.setLayoutManager(layoutManager);
                    rvReports.setAdapter(adapter);
                    screen =2;
                    UpdateFooterData(reportDataList);
                }
            }
            else if(val.equals("0")) {//new change 18102019
                FinanceSingleTon.getInstance().contactList = new ArrayList<>();
                FinanceSingleTon.getInstance().contactList = new XmlConverter().ParseDistinctMemberInfoData(nodeList, FinanceSingleTon.getInstance().contactList);

                if(FinanceSingleTon.getInstance().contactList!=null && FinanceSingleTon.getInstance().contactList.size() > 0) {
                    mAdapter = new SimpleAdapter(this, FinanceSingleTon.getInstance().contactList, R.layout.single_contact, new String[]{"Name", "MobileNo"}, new int[]{R.id.tv_ContactName, R.id.tv_ContactNumber});
                    etSearch.setAdapter(mAdapter);
                    etSearch.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            Object object = parent.getItemAtPosition(position);
                            Map<String, String> selecteddata = (Map<String, String>) object;
                            etSearch.setText("");
                            if (selecteddata != null)//new change 18102019
                            {
                                strName = selecteddata.get("Name");
                                strMobileNo = selecteddata.get("MobileNo");
                                reportDataList = new ArrayList<>();
                                etSearch.clearFocus();
                                GetDataDF(strName,strMobileNo);
                            }
                        }
                    });
                }
                etSearch.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {

                    }

                    @Override
                    public void afterTextChanged(Editable s) {

                    }
                });
            }
            else if(val.equals("7")){
                weekOffList = new ArrayList<>();
                weekOffList = new XmlConverter().ParseWeekOffDaysList(nodeList,weekOffList);
                if(weekOffList!=null && weekOffList.size() > 0) {
                    ShowWeekOffDaysPopUp();
                }
            }
        }
        else {
            if (val.equals("2")) {
                _ct.ShowToast("No Collection For This Member", false);//new change 17102019
                ProcessViews();
                Clicked = true;
            }
        }
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
        Button btnOk = lv.findViewById(R.id.btnOk);//22102019
        btnOk.setVisibility(View.GONE);//2210219
        cb.setVisibility(View.GONE);//22102019
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

    public void SetRecyclerViewItem(ReportData data) {
        if(Clicked) {
            if (data != null) {
                rlSumTotCol.setVisibility(View.GONE);//23102019
                ivBack.setEnabled(true);
                ivFilter.setEnabled(true);
                ivBack.setVisibility(View.VISIBLE);
                ivFilter.setVisibility(View.VISIBLE);
                etSearch.setVisibility(View.INVISIBLE);
                tvReportCount.setVisibility(View.VISIBLE);
                //btnSearch.setVisibility(View.INVISIBLE);
                String xele = "<Data TransId='" + data.getTransId() + "'/>";
                _exeDb.ExecuteResult(SPName.USP_MA_DF_ActiveAndMemwiseReport.toString(), xele, TransType.GetCollectionsAgainstReportData.toString(), "2", Constants.HTTP_URL);
                Clicked = false;
            }
        }
    }

    private int _totAmt,_netAmt,_PdAmt,_totColAmt;

    private void UpdateFooterData(List<ReportData> reportDataList) {
        _totAmt = 0;
        _netAmt = 0;
        _PdAmt = 0;
        _totColAmt = 0;
        if (reportDataList != null && reportDataList.size() > 0) {
            for (ReportData data : reportDataList) {
                if (data != null) {
                    if(screen==1) {
                        _totAmt += (Integer.parseInt(data.getAmount()));
                        _netAmt += (Integer.parseInt(data.getNetAmount()));
                        _PdAmt += (Integer.parseInt(data.getPerDayAmt()));
                        _totColAmt += (Integer.parseInt(data.getCollAmt()));//23102019
                    }else if(screen==2){
                        _totAmt += (Integer.parseInt(data.getAmount()));
                    }
                }
            }
        }
        if(screen==1) {
            tvNetTil.setVisibility(View.VISIBLE);
            tvPDTil.setVisibility(View.VISIBLE);
            tvTtlAmt.setText(String.valueOf(_totAmt));
            tvNetAmt.setText(String.valueOf(_netAmt));
            tvPerDayAmt.setText(String.valueOf(_PdAmt));
            tvColAmt.setText(String.valueOf(_totColAmt));//23102019
            tvR3.setVisibility(View.VISIBLE);
            tvR2.setVisibility(View.VISIBLE);
        }else if(screen==2){
            tvNetTil.setVisibility(View.INVISIBLE);
            tvPDTil.setVisibility(View.INVISIBLE);
            tvPerDayAmt.setVisibility(View.INVISIBLE);
            tvNetAmt.setVisibility(View.INVISIBLE);
            tvTtlAmt.setText(String.valueOf(_totAmt));
            tvReportCount.setVisibility(View.VISIBLE);
            tvReportCount.setText(String.valueOf(reportDataList.size()));
            tvR3.setVisibility(View.INVISIBLE);
            tvR2.setVisibility(View.INVISIBLE);
        }
    }

    private void UpdateDate() {
        if (flagFrom) {
            DateConvertionProcess(etFrom);
            flagFrom = false;
        } else {
            DateConvertionProcess(etTo);
        }
    }
    //New Change 15102019 Method
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
    //New Change 15102019 Method
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

    public void ShowPendingsProcess(ReportData data) {
        String xele =  "<Data TransId='" + data.getTransId() + "' />";
        _exeDb.ExecuteResult(SPName.USP_MA_DF_FinanceCollectionDates.toString(), xele, TransType.GetFinanceCollectionDates.toString(), "7", Constants.HTTP_URL);
    }
}
