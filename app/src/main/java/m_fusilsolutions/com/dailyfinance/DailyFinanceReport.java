package m_fusilsolutions.com.dailyfinance;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import org.w3c.dom.NodeList;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import m_fusilsolutions.com.dailyfinance.Adapters.ReportAdapter;
import m_fusilsolutions.com.dailyfinance.Constants.Constants;
import m_fusilsolutions.com.dailyfinance.Constants.SPName;
import m_fusilsolutions.com.dailyfinance.Constants.TransType;
import m_fusilsolutions.com.dailyfinance.CustomControls.CustomToast;
import m_fusilsolutions.com.dailyfinance.Helpers.AsyncResponse;
import m_fusilsolutions.com.dailyfinance.Helpers.ExecuteDataBase;
import m_fusilsolutions.com.dailyfinance.Helpers.XmlConverter;
import m_fusilsolutions.com.dailyfinance.Models.ReportData;

/**
 * Created by Android on 05-10-2019.
 */

public class DailyFinanceReport extends AppCompatActivity
        implements View.OnClickListener, AsyncResponse {

    RecyclerView rvDfReport;
    Button btnGetDf;
    EditText etFromDate, etToDate;
    TextView tvTotalAmt, tvNetAmt;

    final Calendar myCal = Calendar.getInstance();

    String fromDate, toDate,
            NorPattern = "dd-MM-yyyy",
            DbPattern = "yyyy-MM-dd";
    boolean flagFrom = false;

    DatePickerDialog.OnDateSetListener dateDialog;
    CustomToast _ct;
    ExecuteDataBase _exeDb;
    List<ReportData> reportDataList;
    private double _totAmt, _netAmt;
    int screen = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dailyfinance_layout);
        Intent intent = getIntent();
        screen = intent.getIntExtra("screen",0);
        rvDfReport = (RecyclerView) findViewById(R.id.rvDfReport);
        btnGetDf = (Button) findViewById(R.id.btnGetDf);
        etFromDate = (EditText) findViewById(R.id.etFromDate);
        etToDate = (EditText) findViewById(R.id.etToDate);
        tvTotalAmt = (TextView) findViewById(R.id.tvTtlAmt);
        tvNetAmt = (TextView) findViewById(R.id.tvNtAmt);
        reportDataList = new ArrayList<>();
        _ct = new CustomToast(this);
        _exeDb = new ExecuteDataBase(this);
        etFromDate.setFocusable(false);
        etFromDate.setClickable(true);
        etToDate.setFocusable(false);
        etToDate.setClickable(true);
        setActionBar();

        btnGetDf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!fromDate.equals("") && !toDate.equals("")) {
                    String xele = "<Data FromDate='" + fromDate + "' ToDate='" + toDate + "' />";
                    if(screen==1) {
                        _exeDb.ExecuteResult(SPName.USP_MA_DF_DailyFinance.toString(), xele, TransType.GetDailyFinanceReport.toString(), "1", Constants.HTTP_URL);
                    }else if(screen==2){
                        _exeDb.ExecuteResult(SPName.USP_MA_DF_DailyFinance.toString(), xele, TransType.GetDailyCollectionReport.toString(), "1", Constants.HTTP_URL);
                    }else if(screen==3){
                        _exeDb.ExecuteResult(SPName.USP_MA_DF_DailyFinance.toString(), xele, TransType.GetAllActiveDailyFinanceReport.toString(), "1", Constants.HTTP_URL);
                    }
                }
            }
        });

        dateDialog = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                myCal.set(Calendar.YEAR, i);
                myCal.set(Calendar.MONTH, i1);
                myCal.set(Calendar.DAY_OF_MONTH, i2);
                UpdateDate();
            }
        };

        SetDateProcess();

        etFromDate.setOnClickListener(this);
        etToDate.setOnClickListener(this);
    }

    private void setActionBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_HOME | ActionBar.DISPLAY_SHOW_TITLE);
        actionBar.setDisplayHomeAsUpEnabled(true);
        if(screen==1) {
            actionBar.setTitle("Daily Finance Report");
        }else if(screen==2){
            tvNetAmt.setVisibility(View.INVISIBLE);
            actionBar.setTitle("Collection Report");
        }else if(screen==3){
            actionBar.setTitle("All Active Daily Finance Report");
        }
    }

    private void SetDateProcess() {
        //Calendar c = Calendar.getInstance();
        //c.set(Calendar.DAY_OF_MONTH, 1);

        SimpleDateFormat sdf = new SimpleDateFormat(NorPattern, Locale.UK);
        etFromDate.setText(sdf.format(myCal.getTime()));
        etToDate.setText(sdf.format(myCal.getTime()));

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
        }
    }

    private void CallDatePickerProcess() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            new DatePickerDialog(this, dateDialog,
                    myCal.get(Calendar.YEAR),
                    myCal.get(Calendar.MONTH),
                    myCal.get(Calendar.DAY_OF_MONTH)).show();
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
        String getDate = sdf.format(myCal.getTime());
        et.setText(getDate);

        SimpleDateFormat sdf1 = new SimpleDateFormat(DbPattern, Locale.UK);
        if (flagFrom)
            fromDate = sdf1.format(myCal.getTime());
        else
            toDate = sdf1.format(myCal.getTime());

    }

    @Override
    public void processFinishing(Object object, String val) {
        if (object != null) {
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
                    UpdateFooterData();
                }
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            finish();
        }
        return true;
    }

    private void UpdateFooterData() {
        _totAmt = 0;
        _netAmt = 0;
        if (reportDataList != null && reportDataList.size() > 0) {
            for (ReportData data : reportDataList) {
                if (data != null) {
                    if(screen==1 || screen==3) {
                        _totAmt += (Double.parseDouble(data.getAmount()));
                        _netAmt += (Double.parseDouble(data.getNetAmount()));
                    }else if(screen==2){
                        _totAmt += (Double.parseDouble(data.getAmount()));
                    }
                }
            }
        }
        if(screen==1 || screen==3) {
            tvTotalAmt.setText("Tot Amt: " + String.valueOf(_totAmt));
            tvNetAmt.setText("Net Amt: " + String.valueOf(_netAmt));
        }else if(screen==2){
            tvNetAmt.setVisibility(View.INVISIBLE);
            tvTotalAmt.setText("Tot Amt: " + String.valueOf(_totAmt));
        }
    }
}
