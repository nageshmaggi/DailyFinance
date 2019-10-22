package m_fusilsolutions.com.dailyfinance;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.w3c.dom.NodeList;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import m_fusilsolutions.com.dailyfinance.Adapters.CollectionReportAdapter;
import m_fusilsolutions.com.dailyfinance.Adapters.ReportAdapter;
import m_fusilsolutions.com.dailyfinance.Constants.Constants;
import m_fusilsolutions.com.dailyfinance.Constants.SPName;
import m_fusilsolutions.com.dailyfinance.Constants.TransType;
import m_fusilsolutions.com.dailyfinance.CustomControls.CustomToast;
import m_fusilsolutions.com.dailyfinance.Helpers.AsyncResponse;
import m_fusilsolutions.com.dailyfinance.Helpers.ExecuteDataBase;
import m_fusilsolutions.com.dailyfinance.Helpers.XmlConverter;
import m_fusilsolutions.com.dailyfinance.Models.ReportData;

public class Memberwise_CollectionReport_Activity extends Activity implements AsyncResponse, View.OnClickListener {

    String transId = "";
    List<ReportData> reportDataList;
    RecyclerView rvReports;
    ImageView ivBack,ivFilter;
    TextView tvTtlAmt,tvReportCount;
    RadioGroup rgFilterItems;
    CustomToast _ct;
    EditText etFrom,etTo,etFilterSearch;

    int selectedId = 0,screen = 0;
    String btnName="Date",fromDate, toDate,
            NorPattern = "dd-MM-yyyy",
            DbPattern = "yyyy-MM-dd";

    boolean flagFrom = false;

    DatePickerDialog.OnDateSetListener FromdateDialog,ToDateDialog;
    final Calendar myCal = Calendar.getInstance();
    final Calendar myCalt = Calendar.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.memberwise_collection_reports_layout);
        transId = getIntent().getStringExtra("TransId");

        rvReports = (RecyclerView) findViewById(R.id.rvReportCol);
        tvReportCount = (TextView) findViewById(R.id.tvReportCountCol);
        tvTtlAmt = (TextView) findViewById(R.id.tvTtlAmtCol);
        ivBack = (ImageView) findViewById(R.id.ivBackCol);
        ivFilter = (ImageView) findViewById(R.id.ivFilterCol);
        _ct = new CustomToast(this);
        ivFilter.setOnClickListener(this);
        ivBack.setOnClickListener(this);

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

        LoadConstructorData();
    }

    private void LoadConstructorData()
    {
        ExecuteDataBase _db = new ExecuteDataBase(this);
        String xele = "<Data TransId='" + transId + "'/>";
        _db.ExecuteResult(SPName.USP_MA_DF_ActiveAndMemwiseReport.toString(), xele, TransType.GetCollectionsAgainstReportData.toString(), "1", Constants.HTTP_URL);
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
                    CollectionReportAdapter adapter = new CollectionReportAdapter(this, reportDataList,0);
                    LinearLayoutManager layoutManager = new LinearLayoutManager(this);
                    rvReports.setLayoutManager(layoutManager);
                    rvReports.setAdapter(adapter);
                    UpdateFooterData(reportDataList);
                }
            }
        }else{
            finish();
        }
    }

    private int _totAmt;

    private void UpdateFooterData(List<ReportData> reportDataList) {
        _totAmt = 0;

        if (reportDataList != null && reportDataList.size() > 0) {
            for (ReportData data : reportDataList) {
                if (data != null) {

                        _totAmt += (Integer.parseInt(data.getAmount()));
                }
            }
        }

            tvTtlAmt.setText(String.valueOf(_totAmt));
            tvReportCount.setVisibility(View.VISIBLE);
            tvReportCount.setText(String.valueOf(reportDataList.size()));
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.ivBackCol){
            this.finish();
        }
        else if(v.getId() == R.id.ivFilterCol) {
            FilterCollectionProcess();
        }
        else if (v.getId() == R.id.etFromMW) {
            flagFrom = true;
            CallDatePickerProcess();
        }
        else if (v.getId() == R.id.etToMW) {
            CallDatePickerProcess();
        }
    }

    private void FilterCollectionProcess() {
        int c = 1;

        AlertDialog.Builder adb = new AlertDialog.Builder(this, R.style.MyDialogTheme);
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
            CollectionReportAdapter adapter = new CollectionReportAdapter(this, FilteredList,0);
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
        if (FilteredReportList.size() > 0 && FilteredReportList!=null) {
            ReportAdapter adapter = new ReportAdapter(this, FilteredReportList,screen);
            LinearLayoutManager layoutManager = new LinearLayoutManager(this);
            rvReports.setLayoutManager(layoutManager);
            rvReports.setAdapter(adapter);
            UpdateFooterData(FilteredReportList);
        }
        else _ct.ShowToast("No Data Found",false);//new change 17102019
    }

    private void UpdateDate() {
        if (flagFrom) {
            DateConvertionProcess(etFrom);
            flagFrom = false;
        } else {
            DateConvertionProcess(etTo);
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
}
