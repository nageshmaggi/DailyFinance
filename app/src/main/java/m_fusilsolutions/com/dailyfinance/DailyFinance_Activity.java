package m_fusilsolutions.com.dailyfinance;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;

import org.w3c.dom.NodeList;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import m_fusilsolutions.com.dailyfinance.Constants.Constants;
import m_fusilsolutions.com.dailyfinance.Constants.SPName;
import m_fusilsolutions.com.dailyfinance.Constants.TransType;
import m_fusilsolutions.com.dailyfinance.CustomControls.CustomToast;
import m_fusilsolutions.com.dailyfinance.Helpers.AsyncResponse;
import m_fusilsolutions.com.dailyfinance.Helpers.ExecuteDataBase;
import m_fusilsolutions.com.dailyfinance.Helpers.XmlConverter;
import m_fusilsolutions.com.dailyfinance.Models.DailyFinanceData;
import m_fusilsolutions.com.dailyfinance.Models.LocalRepoViewModel;
import m_fusilsolutions.com.dailyfinance.Models.ResultData;
import m_fusilsolutions.com.dailyfinance.Utils.InputUtils;

/**
 * Created by Android on 02-10-2019.
 */

public class DailyFinance_Activity extends AppCompatActivity implements AsyncResponse
{

    Typeface typeface,typefaceBold,typefaceBoldItalic;
    Button btnSave;
    TextInputLayout tilRefNo;
    EditText etRefNo,etDate,etName,etMobileNo,etAmt,etNetAmt,etPerDayAmt,etRemarks;
    LocalRepoViewModel _localViewModel;
    DailyFinanceData _financeData;
    private DatePickerDialog datePicker;
    private SimpleDateFormat dtFddMMyyyy;
    CustomToast _ct;
    InputUtils _inputXele;
    ExecuteDataBase _exeDb;
    DailyFinanceData _serverInfoData;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daily_finance);
        etDate = (EditText) findViewById(R.id.etDate);
        etName = (EditText) findViewById(R.id.etName);
        etMobileNo = (EditText) findViewById(R.id.etMobileNo);
        etAmt = (EditText) findViewById(R.id.etAmount);
        etNetAmt = (EditText) findViewById(R.id.etNetAmt);
        etPerDayAmt = (EditText) findViewById(R.id.etPerDayAmt);
        etRemarks = (EditText) findViewById(R.id.etRemarks);
        _financeData = new DailyFinanceData();
        _serverInfoData = new DailyFinanceData();
        _localViewModel = new LocalRepoViewModel(_financeData);
        btnSave = (Button) findViewById(R.id.btnSave);
        tilRefNo = (TextInputLayout) findViewById(R.id.tilRefNo);
        etRefNo = (EditText) findViewById(R.id.etRefNo);
        etRefNo.setTypeface(typeface);
        _ct = new CustomToast(this);
        _inputXele = new InputUtils(this);
        dtFddMMyyyy = new SimpleDateFormat("dd-MM-yyyy", Locale.US);
        typeface = Typeface.createFromAsset(getAssets(),"Caviar-Dreams.ttf");
        typefaceBoldItalic = Typeface.createFromAsset(getAssets(),"CaviarDreams_BoldItalic.ttf");
        typefaceBold = Typeface.createFromAsset(getAssets(),"Caviar_Dreams_Bold.ttf");
        btnSave.setTypeface(typefaceBold);
        _exeDb = new ExecuteDataBase(this);
        DatePickerCalling();
        etAmt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(!s.toString().isEmpty()){
                    String str = s.toString();
                    _financeData.setAmount(str);
                    _localViewModel.CalculateNetAndPerDayAmt(etNetAmt,etPerDayAmt);
                }else{
                    etNetAmt.setText("");
                    etPerDayAmt.setText("");
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        setActionBar();
    }

    public void OnSaveClick(View view){
        AssignControlData();
        if(SaveValidation()){
            SaveProcess();
        }
    }

    public void OnClearClick(View view){
        ClearProcess();
    }

    private void AssignControlData(){
        _financeData.setName(etName.getText().toString());
        _financeData.setMobileNo(etMobileNo.getText().toString());
        _financeData.setRefNo(etRefNo.getText().toString());
        _financeData.setRemarks(etRemarks.getText().toString());
        _financeData.setDate(etDate.getText().toString());
    }

    private void SaveProcess(){
        String serXele = _inputXele.getServerInputXele("DF");
        _exeDb.ExecuteResult(SPName.USP_MA_DF_DailyFinance.toString(), serXele, TransType.GetMaxData.toString(),"1", Constants.HTTP_URL);
    }

    private boolean SaveValidation(){
        if(_financeData.getName().isEmpty()){
            //_ct.ShowToast("Enter Name",false);
            etName.setError("Enter Name");
            etName.requestFocus();
            return false;
        }
        else if(_financeData.getMobileNo().isEmpty()){
            //_ct.ShowToast("Enter MobileNo",false);
            etMobileNo.setError("Enter MobileNo");
            etMobileNo.requestFocus();
            return false;
        }
        else if(_financeData.getAmount().isEmpty()){
            //_ct.ShowToast("Enter Amount",false);
            etAmt.setError("Enter AMount");
            etAmt.requestFocus();
            return false;
        }
        return true;
    }

    public void DatePickerCalling()
    {
        Calendar delCalendar = Calendar.getInstance();
        datePicker = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                etDate.setText(dtFddMMyyyy.format(newDate.getTime()));
            }

        }, delCalendar.get(Calendar.YEAR), delCalendar.get(Calendar.MONTH), delCalendar.get(Calendar.DAY_OF_MONTH));
        etDate.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                datePicker.show();
                return true;
            }
        });
        etDate.setText(String.valueOf(dtFddMMyyyy.format(new Date())));
    }

    private void setActionBar()
    {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_HOME|ActionBar.DISPLAY_SHOW_TITLE);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle("Daily Finance");
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        int id = item.getItemId();
        if(id == android.R.id.home){
            finish();
        }
        return true;
    }

    private void ClearProcess(){
        etAmt.setText("");
        etNetAmt.setText("");
        etPerDayAmt.setText("");
        etName.setText("");
        etMobileNo.setText("");
        etRemarks.setText("");
        etRefNo.setText("");
        DatePickerCalling();
        _financeData = new DailyFinanceData();
        _serverInfoData = new DailyFinanceData();
    }

    @Override
    public void processFinishing(Object result, String val) {
        if(result!=null) {
            NodeList nodeList = new XmlConverter().StringToXMLformat((String) result);
            if(val.equals("1")){
                _serverInfoData = new XmlConverter().ParseServerInfoData(nodeList,_serverInfoData);
                if(_serverInfoData!=null){
                    String voucherXele = _inputXele.getVoucherXele(Constants.DF_MENUITEM_ID,_serverInfoData.getTransId(),Constants.DF_VS,_serverInfoData.getVN(),_serverInfoData.getServerDate(),_serverInfoData.getServerTime());
                    String dfXele = _inputXele.getDFSaveXelement(_financeData, _serverInfoData.getTransId(),Constants.DF_MENUITEM_ID);
                    String inputXele = "<Data>" + voucherXele + dfXele + "</Data>";
                    _exeDb.ExecuteResult(SPName.USP_MA_DF_DailyFinance.toString(), inputXele, TransType.SaveDFData.toString(),"2", Constants.HTTP_URL);
                }
            }else if(val.equals("2")){
                ResultData res = new ResultData();
                res = new XmlConverter().ParseSaveData(nodeList,res);
                if(res.Result.equals("Success")){
                    _ct.ShowToast("Save Successfull",false);
                    startActivity(new Intent(this,MainActivity.class));
                }else{
                    _ct.ShowToast("Transaction UnSucessfull",false);
                }
            }
        }else {
            if(val.equals("2")){
                _ct.ShowToast("Transaction UnSucessfull",false);
            }
        }
    }
}
