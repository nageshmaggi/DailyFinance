package m_fusilsolutions.com.dailyfinance;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.w3c.dom.NodeList;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import m_fusilsolutions.com.dailyfinance.Adapters.FinanceAdapter;
import m_fusilsolutions.com.dailyfinance.Constants.Constants;
import m_fusilsolutions.com.dailyfinance.Constants.LoginUserSession;
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
    Button btnSave, btnGet, btnDelete, btnAdminSave,btnAdminClear;
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
    boolean isUpdate = false;
    RelativeLayout layoutNormal;
    LinearLayout layoutAdmin;
    FinanceAdapter _adapter;
    AlertDialog _searchDialog;

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
        _localViewModel = new LocalRepoViewModel();
        btnSave = (Button) findViewById(R.id.btnSave);
        btnGet = (Button) findViewById(R.id.btnGet);
        btnDelete = (Button) findViewById(R.id.btnDelete);
        btnAdminSave = (Button) findViewById(R.id.btnAdminSave);
        btnAdminClear = (Button) findViewById(R.id.btnAdminClear);
        tilRefNo = (TextInputLayout) findViewById(R.id.tilRefNo);
        etRefNo = (EditText) findViewById(R.id.etRefNo);
        layoutNormal = (RelativeLayout) findViewById(R.id.layout_normal);
        layoutAdmin = (LinearLayout) findViewById(R.id.layout_Admin);
        etRefNo.setTypeface(typeface);
        _ct = new CustomToast(this);
        _inputXele = new InputUtils(this);
        dtFddMMyyyy = new SimpleDateFormat("dd-MM-yyyy", Locale.US);
        typeface = Typeface.createFromAsset(getAssets(),"Caviar-Dreams.ttf");
        typefaceBoldItalic = Typeface.createFromAsset(getAssets(),"CaviarDreams_BoldItalic.ttf");
        typefaceBold = Typeface.createFromAsset(getAssets(),"Caviar_Dreams_Bold.ttf");
        btnSave.setTypeface(typefaceBold);
        btnGet.setTypeface(typefaceBold);
        _exeDb = new ExecuteDataBase(this);
        _financeData.setDate(String.valueOf(dtFddMMyyyy.format(new Date())));
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
                    _localViewModel.CalculateNetAndPerDayAmt(_financeData,etNetAmt,etPerDayAmt);
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
        SetScreenConfiguration();

    }

    private void SetScreenConfiguration(){
        if(LoginUserSession._loginUserData.CustomerId.equals("1")){
            layoutNormal.setVisibility(View.GONE);
            layoutAdmin.setVisibility(View.VISIBLE);
            btnDelete.setTypeface(typefaceBold);
            btnAdminSave.setTypeface(typefaceBold);
            btnAdminClear.setTypeface(typefaceBold);
        }else{
            layoutNormal.setVisibility(View.VISIBLE);
            layoutAdmin.setVisibility(View.GONE);
        }
    }

    public void OnEditClick(View view){
        ShowEditDeleteDialog("Edit",true);
    }

    private void ShowEditDeleteDialog(String title,boolean isEdit){
        AlertDialog.Builder adb  = new AlertDialog.Builder(this,R.style.MyDialogTheme);
        LayoutInflater li = getLayoutInflater();
        View lv = li.inflate(R.layout.search_dialog,null);
        ImageView img_delete = lv.findViewById(R.id.imgV_close);
        TextView tvTitle = lv.findViewById(R.id.tvDialog_title);
        EditText etVs = lv.findViewById(R.id.etVS);
        EditText etVn = lv.findViewById(R.id.etVN);
        Button btnGet = lv.findViewById(R.id.btnget);
        tvTitle.setText(title);
        adb.setView(lv);
        adb.setCancelable(false);
        _searchDialog = adb.create();
        _searchDialog.show();
        img_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                _searchDialog.dismiss();
            }
        });
        btnGet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isEdit) {
                    isUpdate = true;
                    btnSave.setText("Update");
                    btnAdminSave.setText("Update");
                }
                GetFinanceUserProcess(etVs.getText().toString(), etVn.getText().toString());
            }
        });
    }

    public void OnDeleteClick(View view){
        ShowEditDeleteDialog("Delete",false);
//        isUpdate = false;
//        btnSave.setText("Save");
//        btnAdminSave.setText("Save");
//        GetFinanceUserProcess("","");
    }

    private void GetFinanceUserProcess(String vs, String vn)
    {
        if(GetValidation(vs,vn)) {
            String xele = _inputXele.getDFinanceDataByMobileNo(vs,vn);
            _exeDb.ExecuteResult(SPName.USP_MA_DF_DailyFinance.toString(), xele, TransType.GetFinanceDataByVSNo.toString(), "3", Constants.HTTP_URL);
        }
    }

    private boolean GetValidation(String vs, String vn)
    {
        if(vs.isEmpty()){
            _ct.ShowToast("Enter VS",true);
            return false;
        }else if(vn.isEmpty()){
            _ct.ShowToast("Enter VN",true);
            return false;
        }
        return true;
    }

    public void OnSaveClick(View view){
        AssignControlData();
        if(!isUpdate) {
            if (SaveValidation()) {
                SaveProcess();
            }
        }else{
            String xele = _inputXele.getDFUpdationXelement(_financeData,_financeData.getTransId());
            _exeDb.ExecuteResult(SPName.USP_MA_DF_DailyFinance.toString(),xele,TransType.UpdateDFData.toString(),"6",Constants.HTTP_URL);
        }
    }

    public void OnClearClick(View view){
        ClearProcess();
    }

    private void AssignControlData(){
        _financeData.setMobileNo(etMobileNo.getText().toString());
        _financeData.setDate(etDate.getText().toString());
        _financeData.setName(etName.getText().toString());
        _financeData.setRefNo(etRefNo.getText().toString());
        _financeData.setAmount(etAmt.getText().toString());
        _financeData.setNetAmount(etNetAmt.getText().toString());
        _financeData.setPerDayAmt(etPerDayAmt.getText().toString());
        _financeData.setRemarks(etRemarks.getText().toString());
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
                String[] dateStr = _financeData.getDate().split("-");
                String day = dateStr[0];
                String month = dateStr[1];
                String year = dateStr[2];

                datePicker.updateDate(Integer.parseInt(year),Integer.parseInt(month)-1,Integer.parseInt(day));
                datePicker.show();
                return true;
            }
        });
        etDate.setText(_financeData.getDate());
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
        isUpdate = false;
        btnSave.setText("Save");
        btnAdminSave.setText("Save");
        _financeData.setDate(String.valueOf(dtFddMMyyyy.format(new Date())));
    }

    @Override
    public void processFinishing(Object result, String val) {
        if(result!=null) {
            NodeList nodeList = new XmlConverter().StringToXMLformat((String) result);
            if(val.equals("1")){
                _serverInfoData = new XmlConverter().ParseServerInfoData(nodeList,_serverInfoData);
                if(_serverInfoData!=null){
                    String voucherXele = _inputXele.getVoucherXele(Constants.DF_MENUITEM_ID,_serverInfoData.getTransId(),Constants.DF_VS,_serverInfoData.getVSNo(),_serverInfoData.getServerDate(),_serverInfoData.getServerTime());
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
            }else if(val.equals("3")){
                List<DailyFinanceData> dfList = new ArrayList<>();
                dfList = new XmlConverter().ParseGetDFInfoData(nodeList,dfList);
                if (dfList != null && dfList.size() > 0) {
                    DailyFinanceData _resultData = dfList.get(0);
                    if (isUpdate) {
                        SetUserData(_resultData);
                    } else {
                        if(_resultData.getCollectionCount()==0) {
                            String xele = _inputXele.getDFDeleteXele(dfList.get(0).getTransId());
                            _exeDb.ExecuteResult(SPName.USP_MA_DF_DailyFinance.toString(), xele, TransType.DeleteDFData.toString(), "4", Constants.HTTP_URL);
                        }else{
                            _ct.ShowToast("Collection Done,You Cannot Perform Delete",true);
                        }
                    }
                }
            }else if(val.equals("4")){
                ResultData _res = new ResultData();
                _res = new XmlConverter().ParseSaveData(nodeList,_res);
                if(_res!=null){
                    if(_res.Result.equals("Success")){
                        _ct.ShowToast("Delete Successfull",true);
                        //_adapter.notifyDataSetChanged();
                        _searchDialog.dismiss();
                    }
                }
            }else if(val.equals("5")){
                ResultData _res = new ResultData();
                _res = new XmlConverter().ParseSaveData(nodeList,_res);
                if(_res!=null){
                    if(_res.Result.equals("Success")){
                        _ct.ShowToast("Delete Successfull",true);
                        ClearProcess();
                    }
                }
            }else if(val.equals("6")){
                ResultData _res = new ResultData();
                _res = new XmlConverter().ParseSaveData(nodeList,_res);
                if(_res!=null){
                    if(_res.Result.equals("Success")){
                        _ct.ShowToast("Updated Successfully",true);
                        ClearProcess();
                    }
                }
            }
        }else {
            if(val.equals("2")){
                _ct.ShowToast("Transaction UnSucessfull",false);
            }
            else if(val.equals("3")){
                isUpdate = false;
                btnSave.setText("Save");
                btnAdminSave.setText("Save");
                _ct.ShowToast("Member Doesn't Exist",false);
                _searchDialog.dismiss();
            }else if(val.equals("6")){
                _ct.ShowToast("Not Updated, Duplicate Data Exist",true);
            }
        }
    }

    private void ShowConfirmDialog(DailyFinanceData data) {

        android.support.v7.app.AlertDialog.Builder alertDialogBuilder = new android.support.v7.app.AlertDialog.Builder(
                this);
        alertDialogBuilder.setTitle("Are you sure want to delete?");
        alertDialogBuilder.setCancelable(false);
        alertDialogBuilder.setPositiveButton("OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id)
                    {
                        String xele = _inputXele.getDFDeleteXele(data.getTransId());
                        _exeDb.ExecuteResult(SPName.USP_MA_DF_DailyFinance.toString(),xele, TransType.DeleteDFData.toString(),"5", Constants.HTTP_URL);
                        dialog.dismiss();
                    }
                });
        alertDialogBuilder.setNegativeButton("Cancel",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        android.support.v7.app.AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    public void SetRecyclerViewItem(DailyFinanceData _data){
        SetUserData(_data);
    }

    private void SetUserData(DailyFinanceData _data){
        _financeData = _data;
        etName.setText(_data.getName());
        etRefNo.setText(_data.getRefNo());
        try {
            etAmt.setText(_financeData.getAmount());
        }catch (Exception e){
        }
        if(_financeData.getCollectionCount() > 0){
            etAmt.setEnabled(false);
        }else{
            etAmt.setEnabled(true);
        }
        etNetAmt.setText(_data.getNetAmount());
        etPerDayAmt.setText(_data.getPerDayAmt());
        etDate.setText(_data.getDate());
        etRemarks.setText(_data.getRemarks());
        etMobileNo.setText(_data.getMobileNo());
        _searchDialog.dismiss();
    }

    private void SearchResultDailog(List<DailyFinanceData> collectionList, String title, boolean isEdit) {
        AlertDialog.Builder adb  = new AlertDialog.Builder(this,R.style.MyDialogTheme);
        LayoutInflater li = getLayoutInflater();
        View lv = li.inflate(R.layout.finance_multi_trans_dialog,null);
        ImageView imgClose = lv.findViewById(R.id.imgV_close);
        TextView tv_Dialog_Name = lv.findViewById(R.id.tvDialog_title);
        tv_Dialog_Name.setText(title);
        adb.setView(lv);
        adb.setCancelable(false);
        AlertDialog ad = adb.create();
        RecyclerView rvCollection = (RecyclerView) lv.findViewById(R.id.rvCollection);
        _adapter = new FinanceAdapter(this,collectionList,ad, isEdit);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        rvCollection.setLayoutManager(layoutManager);
        rvCollection.setAdapter(_adapter);
        imgClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ad.dismiss();
                btnAdminSave.setText("Save");
                btnSave.setText("Save");
                isUpdate = false;
            }
        });
        ad.show();
    }
}
