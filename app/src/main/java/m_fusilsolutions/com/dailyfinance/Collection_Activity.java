package m_fusilsolutions.com.dailyfinance;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;

import org.w3c.dom.NodeList;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.regex.Pattern;

import m_fusilsolutions.com.dailyfinance.Adapters.CollectionAdapter;
import m_fusilsolutions.com.dailyfinance.Constants.Constants;
import m_fusilsolutions.com.dailyfinance.Constants.SPName;
import m_fusilsolutions.com.dailyfinance.Constants.TransType;
import m_fusilsolutions.com.dailyfinance.CustomControls.CustomToast;
import m_fusilsolutions.com.dailyfinance.Helpers.AsyncResponse;
import m_fusilsolutions.com.dailyfinance.Helpers.ExecuteDataBase;
import m_fusilsolutions.com.dailyfinance.Helpers.XmlConverter;
import m_fusilsolutions.com.dailyfinance.Models.DailyFinanceData;
import m_fusilsolutions.com.dailyfinance.Models.ResultData;
import m_fusilsolutions.com.dailyfinance.Utils.InputUtils;

/**
 * Created by Android on 02-10-2019.
 */

public class Collection_Activity extends AppCompatActivity implements AsyncResponse {
    EditText etMobileNo, etName, etRefNo, etDate, etAmt, etRemarks;
    ImageView ivSearch;
    ExecuteDataBase _exeDb;
    InputUtils _inputXele;
    DailyFinanceData _collectionData;
    private DatePickerDialog datePicker;
    private SimpleDateFormat dtFddMMyyyy;
    CustomToast _ct;
    DailyFinanceData _serverInfoData;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collection);
        etMobileNo = (EditText) findViewById(R.id.etMobileNo);
        etDate = (EditText) findViewById(R.id.etDate);
        etName = (EditText) findViewById(R.id.etName);
        etRefNo = (EditText) findViewById(R.id.etRefNo);
        etAmt = (EditText) findViewById(R.id.etAmount);
        etRemarks = (EditText) findViewById(R.id.etRemarks);
        ivSearch = (ImageView) findViewById(R.id.ivSearch);
        _exeDb = new ExecuteDataBase(this);
        _inputXele = new InputUtils(this);
        _ct = new CustomToast(this);
        _collectionData = new DailyFinanceData();
        _serverInfoData = new DailyFinanceData();
        dtFddMMyyyy = new SimpleDateFormat("dd-MM-yyyy", Locale.US);
        DatePickerCalling();
        _collectionData.setDate(String.valueOf(dtFddMMyyyy.format(new Date())));
//        etMobileNo.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//                if (!s.toString().isEmpty()) {
//                    if (isValidMobile(s.toString())) {
//                        String xele = _inputXele.getSearchXele(etMobileNo.getText().toString());
//                        _exeDb.ExecuteResult(SPName.USP_MA_DF_DailyFinance.toString(), xele, TransType.GetSearchData.toString(), "1", Constants.HTTP_URL);
//                    }
//                } else {
//                    etName.setText("");
//                    etRefNo.setText("");
//                }
//            }
//
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//
//            }
//        });
        setActionBar();
        ivSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!etMobileNo.getText().toString().isEmpty())
                {
                    String xele = _inputXele.getSearchXele(etMobileNo.getText().toString());
                    _exeDb.ExecuteResult(SPName.USP_MA_DF_DailyFinance.toString(), xele, TransType.GetSearchData.toString(), "1", Constants.HTTP_URL);
                }
            }
        });
    }

    public void OnSaveClick(View view) {
        _collectionData.setAmount(etAmt.getText().toString());
        _collectionData.setRemarks(etRemarks.getText().toString());
        _collectionData.setDate(etDate.getText().toString());
        if (!_collectionData.getAmount().isEmpty()) {
            SaveProcess();
        } else {
            etMobileNo.requestFocus();
            _ct.ShowToast("Enter Required Fields", false);
        }
    }

    public void OnClearClick(View view) {
        ClearProcess();
    }

    private void SaveProcess() {
        String serXele = _inputXele.getServerInputXele("DFC");
        _exeDb.ExecuteResult(SPName.USP_MA_DF_DailyFinance.toString(), serXele, TransType.GetMaxData.toString(), "2", Constants.HTTP_URL);
    }


    public void DatePickerCalling() {
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
                String[] dateStr = _collectionData.getDate().split("-");
                String day = dateStr[0];
                String month = dateStr[1];
                String year = dateStr[2];
                datePicker.updateDate(Integer.parseInt(year),Integer.parseInt(month)-1,Integer.parseInt(day));
                datePicker.show();
                return true;
            }
        });
        etDate.setText(_collectionData.getDate());
    }

    private boolean isValidMobile(String phone) {
        if (!Pattern.matches("[a-zA-Z]+", phone)) {
            return phone.length() > 6 && phone.length() <= 13;

        }
        return false;
    }

    private void setActionBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_HOME | ActionBar.DISPLAY_SHOW_TITLE);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle("Collection");
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            finish();
        }
        return true;
    }

    private void ClearProcess() {
        etAmt.setText("");
        etName.setText("");
        etMobileNo.setText("");
        etRemarks.setText("");
        etRefNo.setText("");
        DatePickerCalling();
        _collectionData = new DailyFinanceData();
        _serverInfoData = new DailyFinanceData();
    }

    @Override
    public void processFinishing(Object object, String val) {
        if (object != null) {
            NodeList nodeList = new XmlConverter().StringToXMLformat((String) object);
            if (val.equals("1")) {

                List<DailyFinanceData> collectionList = new ArrayList<>();
                collectionList = new XmlConverter().ParseCollectionSearchData(nodeList, collectionList);
                if (collectionList != null && collectionList.size() > 0) {

                    if(collectionList.size() > 1)
                        SearchResultDailog(collectionList);
                    else
                    {
                        SetUserData(collectionList.get(0));
                    }
                }
            } else if (val.equals("2")) {
                _serverInfoData = new XmlConverter().ParseServerInfoData(nodeList, _serverInfoData);
                String voucherXele = _inputXele.getVoucherXele(Constants.DF_COLLECTION_MENUITEM_ID, _serverInfoData.getTransId(), Constants.DFC_VS, _serverInfoData.getVSNo(), _serverInfoData.getServerDate(), _serverInfoData.getServerTime());
                String collectionXele = _inputXele.getCollectionXelement(_collectionData, _serverInfoData.getTransId(),Constants.DF_COLLECTION_MENUITEM_ID);
                String inputXele = "<Data>" + voucherXele + collectionXele + "</Data>";
                _exeDb.ExecuteResult(SPName.USP_MA_DF_DailyFinance.toString(), inputXele, TransType.SaveDFCData.toString(), "3", Constants.HTTP_URL);
            } else if (val.equals("3")) {
                ResultData res = new ResultData();
                res = new XmlConverter().ParseSaveData(nodeList, res);
                if (res.Result.equals("Success")) {
                    _ct.ShowToast("Save Successfull", false);
                    startActivity(new Intent(this, MainActivity.class));
                    ClearProcess();
                }
            }
        } else {
            if (val.equals("1")) {
                etName.setText("");
                etRefNo.setText("");
                etAmt.setText("");
            }
        }
    }

    public void SetRecyclerViewItem(DailyFinanceData _data){
        SetUserData(_data);
    }

    private void SetUserData(DailyFinanceData _financeData){
        _collectionData = _financeData;
        etName.setText(_collectionData.getName());
        etRefNo.setText(_collectionData.getRefNo());
        etAmt.setText(_collectionData.getPerDayAmt());
    }

    private void SearchResultDailog(List<DailyFinanceData> collectionList) {
        AlertDialog.Builder adb  = new AlertDialog.Builder(this,R.style.MyDialogTheme);
        LayoutInflater li = getLayoutInflater();
        View lv = li.inflate(R.layout.finance_multi_trans_dialog,null);
        ImageView imgClose = lv.findViewById(R.id.imgV_close);
        //adb.setTitle("Choose:");
        adb.setView(lv);
        adb.setCancelable(false);
        AlertDialog ad = adb.create();
        RecyclerView rvCollection = (RecyclerView) lv.findViewById(R.id.rvCollection);
        CollectionAdapter collAdapter = new CollectionAdapter(this,collectionList,ad,false);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        rvCollection.setLayoutManager(layoutManager);
        rvCollection.setAdapter(collAdapter);
        imgClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ad.dismiss();
            }
        });
        ad.show();
    }
}
