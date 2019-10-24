package m_fusilsolutions.com.dailyfinance;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
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
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import java.util.concurrent.ExecutionException;
import java.util.regex.Pattern;

import m_fusilsolutions.com.dailyfinance.Adapters.CollectionAdapter;
import m_fusilsolutions.com.dailyfinance.Adapters.WeekOffAdapter;
import m_fusilsolutions.com.dailyfinance.Constants.Constants;
import m_fusilsolutions.com.dailyfinance.Constants.LoginUserSession;
import m_fusilsolutions.com.dailyfinance.Constants.SPName;
import m_fusilsolutions.com.dailyfinance.Constants.TransType;
import m_fusilsolutions.com.dailyfinance.CustomControls.CustomToast;
import m_fusilsolutions.com.dailyfinance.Helpers.AsyncResponse;
import m_fusilsolutions.com.dailyfinance.Helpers.DFSmsHelper;
import m_fusilsolutions.com.dailyfinance.Helpers.ExecuteDataBase;
import m_fusilsolutions.com.dailyfinance.Helpers.GetMaxData;
import m_fusilsolutions.com.dailyfinance.Helpers.MessageBoxHelper;
import m_fusilsolutions.com.dailyfinance.Helpers.NetworkUtils;
import m_fusilsolutions.com.dailyfinance.Helpers.SaveMechanism;
import m_fusilsolutions.com.dailyfinance.Helpers.XmlConverter;
import m_fusilsolutions.com.dailyfinance.Models.DailyFinanceData;
import m_fusilsolutions.com.dailyfinance.Models.ResultData;
import m_fusilsolutions.com.dailyfinance.Singletons.FinanceSingleTon;
import m_fusilsolutions.com.dailyfinance.Utils.InputUtils;

/**
 * Created by Android on 02-10-2019.
 */

public class Collection_Activity extends AppCompatActivity implements AsyncResponse {
    Typeface typeface,typefaceBold,typefaceBoldItalic;
    EditText etMobileNo, etName, etRefNo, etCollDate,etDate, etAmt, etRemarks;
    Button btnSave, btnGet,btnDelete, btnAdminSave,btnAdminClear;
    ImageView ivSearch;
    ExecuteDataBase _exeDb;
    InputUtils _inputXele;
    DailyFinanceData _collectionData;
    private DatePickerDialog colldatePicker;
    private DatePickerDialog transdatePicker;
    private SimpleDateFormat dtFddMMyyyy;
    CustomToast _ct;
    DailyFinanceData _serverInfoData;
    RelativeLayout layoutNormal;
    LinearLayout layoutAdmin;
    AlertDialog _searchDialog;
    boolean isUpdate = false;
    AutoCompleteTextView actNameOrMobile;
    private SimpleAdapter mAdapter;
    public TextView tvWeekDayTotAmt,tvWeekTotCount;
    List<DailyFinanceData> weekOffList;
    String _financeDate="";
    String intentMobileNo="";


    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collection);
        if (new NetworkUtils(this).IsNetworkAvailable()) {
            intentMobileNo = getIntent().getStringExtra("MobileNo");
            etMobileNo = (EditText) findViewById(R.id.etMobileNo);
            etCollDate = (EditText) findViewById(R.id.etCollDate);
            etDate = (EditText) findViewById(R.id.etDate);
            etName = (EditText) findViewById(R.id.etName);
            etRefNo = (EditText) findViewById(R.id.etRefNo);
            etAmt = (EditText) findViewById(R.id.etAmount);
            etRemarks = (EditText) findViewById(R.id.etRemarks);
            actNameOrMobile = (AutoCompleteTextView) findViewById(R.id.acTNameOrMobile);
            //ivSearch = (ImageView) findViewById(R.id.ivSearch);
            btnSave = (Button) findViewById(R.id.btnSave);
            btnGet = (Button) findViewById(R.id.btnGet);
            btnDelete = (Button) findViewById(R.id.btnDelete);
            btnAdminSave = (Button) findViewById(R.id.btnAdminSave);
            btnAdminClear = (Button) findViewById(R.id.btnAdminClear);
            layoutNormal = (RelativeLayout) findViewById(R.id.layout_normal);
            layoutAdmin = (LinearLayout) findViewById(R.id.layout_Admin);
            typeface = Typeface.createFromAsset(getAssets(), "Caviar-Dreams.ttf");
            typefaceBoldItalic = Typeface.createFromAsset(getAssets(), "CaviarDreams_BoldItalic.ttf");
            typefaceBold = Typeface.createFromAsset(getAssets(), "Caviar_Dreams_Bold.ttf");
            _exeDb = new ExecuteDataBase(this);
            _inputXele = new InputUtils(this);
            _ct = new CustomToast(this);
            _collectionData = new DailyFinanceData();
            _serverInfoData = new DailyFinanceData();
            weekOffList = new ArrayList<>();
            dtFddMMyyyy = new SimpleDateFormat("dd-MM-yyyy", Locale.US);
            CollDatePickerCalling();
            TransDatePickerCalling();
            etName.setEnabled(false);
            etRefNo.setEnabled(false);
            _collectionData.setDate(String.valueOf(dtFddMMyyyy.format(new Date())));
            setActionBar();
            SetScreenConfiguration();
            mAdapter = new SimpleAdapter(this, FinanceSingleTon.getInstance().contactList, R.layout.single_contact,new String[] { "Name", "MobileNo" }, new int[] { R.id.tv_ContactName, R.id.tv_ContactNumber});
            actNameOrMobile.setAdapter(mAdapter);
            actNameOrMobile.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Object object = parent.getItemAtPosition(position);
                    Map<String, String> selecteddata = (Map<String, String>) object;
                    //actNameOrMobile.setText("");
                    actNameOrMobile.setText(selecteddata.get("Name"));
                    if(selecteddata!=null){
                        etName.setText(selecteddata.get("Name"));
                        etMobileNo.setText(selecteddata.get("MobileNo"));
                    }
                    if (!selecteddata.get("MobileNo").isEmpty()) {
                        GetDataByMobileNo(selecteddata.get("MobileNo"));
//                        String xele = _inputXele.getSearchXele(selecteddata.get("MobileNo"));
//                        _exeDb.ExecuteResult(SPName.USP_MA_DF_FinanceSearchData.toString(), xele, TransType.GetFinanceSearchDataInCollection.toString(), "1", Constants.HTTP_URL);
                    }
                }
            });
            actNameOrMobile.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if(s.toString().equals("")){
                        etMobileNo.setText("");
                        ClearProcess();
                    }
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });
            if(intentMobileNo!=null && !intentMobileNo.equals("")){
                GetDataByMobileNo(intentMobileNo);
            }

        } else {
            startActivity(new Intent(this, NoInternetConnection_Activity.class));
        }
    }

    private void GetDataByMobileNo(String mNo){
        String xele = _inputXele.getSearchXele(mNo);
        _exeDb.ExecuteResult(SPName.USP_MA_DF_FinanceSearchData.toString(), xele, TransType.GetFinanceSearchDataInCollection.toString(), "1", Constants.HTTP_URL);
    }

    private void SetScreenConfiguration(){
        if(LoginUserSession._loginUserData.CustomerId.equals("1")){
            layoutNormal.setVisibility(View.GONE);
            layoutAdmin.setVisibility(View.VISIBLE);
            btnDelete.setTypeface(typefaceBold);
            btnAdminSave.setTypeface(typefaceBold);
            btnAdminClear.setTypeface(typefaceBold);
            btnGet.setTypeface(typefaceBold);
        }else{
            layoutNormal.setVisibility(View.VISIBLE);
            layoutAdmin.setVisibility(View.GONE);
        }
    }

    public void OnSaveClick(View view) {
        AssignControlData();
        weekOffList = new ArrayList<>();
        if(!isUpdate) {
            if (!_collectionData.getAmount().isEmpty()) {
                String xele = _inputXele.getWeekOffDaysListByTransId(_collectionData.getTransId());
                _exeDb.ExecuteResult(SPName.USP_MA_DF_FinanceCollectionDates.toString(), xele, TransType.GetFinanceCollectionDates.toString(), "7", Constants.HTTP_URL);
            } else {
                etMobileNo.requestFocus();
                _ct.ShowToast("Enter Required Fields", false);
            }
        }else{
            String xele = _inputXele.getCollectionUpdateXelement(_collectionData);
            _exeDb.ExecuteResult(SPName.USP_MA_DF_OtherCollectionData.toString(),xele,TransType.UpdateCollectionData.toString(),"5",Constants.HTTP_URL);
        }
    }

    private boolean IsTransDateValidation()
    {

        return false;
    }


    private void ShowWeekOffDaysPopUp() {
        final boolean[] flag = {false};
        AlertDialog.Builder adb = new AlertDialog.Builder(this, R.style.MyDialogTheme);
        LayoutInflater li = getLayoutInflater();
        //weekOffList = list();
        View lv = li.inflate(R.layout.week_off_layout_dialog, null);
        RecyclerView rv = lv.findViewById(R.id.recyclerView);
        TextView tvdate = lv.findViewById(R.id.tvDate);
        tvWeekDayTotAmt = lv.findViewById(R.id.tvFtrAmount);
        tvWeekTotCount = lv.findViewById(R.id.tvFtrCount);
        TextView tvAmt = lv.findViewById(R.id.tvAmount);
        TextView tvSelectAll = lv.findViewById(R.id.tvSelectAll);
        CheckBox cb = lv.findViewById(R.id.cbSelectAll);
        ImageView imgclose = lv.findViewById(R.id.imgV_close);
        TextView tvTit = lv.findViewById(R.id.titWeek);
        Button btnOk = lv.findViewById(R.id.btnOk);
        tvdate.setTypeface(typefaceBold);
        tvAmt.setTypeface(typefaceBold);
        tvTit.setTypeface(typefaceBold);
        tvSelectAll.setTypeface(typefaceBold);
        tvWeekDayTotAmt.setTypeface(typefaceBold);
        tvWeekTotCount.setTypeface(typefaceBold);
        btnOk.setTypeface(typefaceBold);
        tvWeekDayTotAmt.setText(String.valueOf(getTotalAmtOfWeekOfList().intValue()));
        tvWeekTotCount.setText(String.valueOf(weekOffList.size()));
        WeekOffAdapter adapter = new WeekOffAdapter(this, weekOffList, flag[0]);
        rv.setAdapter(adapter);
        rv.setLayoutManager(new LinearLayoutManager(this));
        rv.setHasFixedSize(true);
        rv.setNestedScrollingEnabled(false);
        adb.setView(lv);
        adb.setCancelable(false);
        AlertDialog dialog = adb.create();
        dialog.show();
        cb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adapter.selectAll();
            }
        });
        imgclose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final ProgressDialog progDailog = ProgressDialog.show(
                        Collection_Activity.this, "Message", "Please wait...", true);

                new Thread() {
                    public void run() {
                        try {
                            dialog.dismiss();
                            SaveProcess("");
                            //SaveProcess();
                        } catch (Exception e) {

                        }
                        progDailog.dismiss();
                    }
                }.start();
                _ct.ShowToast("Transaction Saved Succesfully",true);
                startActivity(new Intent(Collection_Activity.this,MainActivity.class));
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

    private List<DailyFinanceData> list()
    {
        List<DailyFinanceData> _list = new ArrayList<>();
        _list.add(new DailyFinanceData("100","10-10-2019"));
        _list.add(new DailyFinanceData("100","11-10-2019"));
        _list.add(new DailyFinanceData("100","13-10-2019"));
        _list.add(new DailyFinanceData("100","14-10-2019"));
        _list.add(new DailyFinanceData("100","16-10-2019"));
        return _list;
    }

    private void AssignControlData(){
        _collectionData.setRemarks(etRemarks.getText().toString());
        _collectionData.setDate(etCollDate.getText().toString());
        _collectionData.setTransDate(etDate.getText().toString());
    }

    public void OnEditClick(View view){
        ShowEditDeleteDialog("Edit",true);
    }

    public void OnClearClick(View view) {
        ClearProcess();
    }

    private void SaveProcess() {
        String serXele = _inputXele.getServerInputXele(Constants.DF_COLLECTION_MENUITEM_ID);
        _exeDb.ExecuteResult(SPName.USP_MA_DF_MaxData.toString(), serXele, TransType.GetMaxData.toString(), "2", Constants.HTTP_URL);
    }

    private void SaveProcess(String str) {
        String serXele = _inputXele.getServerInputXele(Constants.DF_COLLECTION_MENUITEM_ID);
        try {
            _serverInfoData = new GetMaxData().execute(SPName.USP_MA_DF_MaxData.toString(), serXele, TransType.GetMaxData.toString(), Constants.HTTP_URL).get();
            if(_serverInfoData!=null){
                SaveMechanism();
            }
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        //_exeDb.ExecuteResult(SPName.USP_MA_DF_MaxData.toString(), serXele, TransType.GetMaxData.toString(), "2", Constants.HTTP_URL);
    }

    private void SaveMechanism()
    {
        if(weekOffList.size() > 1)
        {
            long transId = Long.parseLong(_serverInfoData.getTransId());
            int vn = Integer.parseInt(_serverInfoData.getVSNo());
            for(DailyFinanceData data : weekOffList) {
                if(data.isWeekDaySelected()) {
                    String voucherXele = _inputXele.getVoucherXele( Constants.DF_COLLECTION_MENUITEM_ID, String.valueOf(transId), Constants.DFC_VS, String.valueOf(vn), _serverInfoData.getServerDate(), _serverInfoData.getServerTime());
                    String collectionXele = _inputXele.getCollectionXelement(data, _collectionData, String.valueOf(transId), Constants.DF_COLLECTION_MENUITEM_ID);
                    String inputXele = "<Trans><Voucher>" + voucherXele + collectionXele + "</Voucher></Trans>";
                    new SaveMechanism().execute(SPName.USP_MA_DF_SaveCollection.toString(), inputXele, TransType.SaveDFCData.toString(), Constants.HTTP_URL);
                    vn++;
                    transId++;
                }
            }
            //UpdateSMSProperties();
            //new DFSmsHelper(this,_collectionData).SendSms(LoginUserSession._loginUserData.CollMessage,_collectionData.getMobileNo());
//            _ct.ShowToast("Transaction Saved Succesfully",true);
//            startActivity(new Intent(Collection_Activity.this,MainActivity.class));
            //new MessageBoxHelper(this).ShowMessageBox(true,"Transaction Saved Succesfully");

        }else if(weekOffList.size() ==1){
            String vXele = _inputXele.getVoucherXele(Constants.DF_COLLECTION_MENUITEM_ID, _serverInfoData.getTransId(), Constants.DFC_VS, _serverInfoData.getVSNo(), _serverInfoData.getServerDate(), _serverInfoData.getServerTime());
            String cXele = _inputXele.getCollectionXelement(weekOffList.get(0),_collectionData, _serverInfoData.getTransId(), Constants.DF_COLLECTION_MENUITEM_ID);
            String inputXele = "<Trans><Voucher>" + vXele + cXele + "</Voucher></Trans>";
            new SaveMechanism().execute(SPName.USP_MA_DF_SaveCollection.toString(), inputXele, TransType.SaveDFCData.toString(), Constants.HTTP_URL);
//            _ct.ShowToast("Transaction Saved Succesfully",true);
//            startActivity(new Intent(Collection_Activity.this,MainActivity.class));
            //new MessageBoxHelper(this).ShowMessageBox(true,"Transaction Saved Succesfully");
            //_exeDb.ExecuteResult(SPName.USP_MA_DF_SaveCollection.toString(), inputXele, TransType.SaveDFCData.toString(), "3", Constants.HTTP_URL);
        }
        weekOffList = new ArrayList<>();
    }


    public void CollDatePickerCalling() {
        Calendar delCalendar = Calendar.getInstance();
        colldatePicker = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                etCollDate.setText(dtFddMMyyyy.format(newDate.getTime()));
            }

        }, delCalendar.get(Calendar.YEAR), delCalendar.get(Calendar.MONTH), delCalendar.get(Calendar.DAY_OF_MONTH));
        etCollDate.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(isUpdate) {
                    String[] dateStr = _collectionData.getDate().split("-");
                    String day = dateStr[0];
                    String month = dateStr[1];
                    String year = dateStr[2];
                    colldatePicker.updateDate(Integer.parseInt(year), Integer.parseInt(month) - 1, Integer.parseInt(day));
                }
                colldatePicker.show();
                return true;
            }
        });
        etCollDate.setText(dtFddMMyyyy.format(delCalendar.getTime()));
    }

    public void TransDatePickerCalling() {
        Calendar delCalendar = Calendar.getInstance();
        transdatePicker = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
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
                transdatePicker.show();
                return true;
            }
        });
        etDate.setText(dtFddMMyyyy.format(delCalendar.getTime()));
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
        actionBar.setTitle(getResources().getString(R.string.nav_draw_trans_collections));
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
        CollDatePickerCalling();
        TransDatePickerCalling();
        btnSave.setText("Save");
        btnAdminSave.setText("Save");
        _collectionData = new DailyFinanceData();
        _serverInfoData = new DailyFinanceData();
        isUpdate = false;
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
                String vs = etVs.getText().toString();
                String vn = etVn.getText().toString();
                if(CollectionDataValidation(etVs,etVn,vs,vn))
                if(isEdit) {
                    isUpdate = true;
                    btnSave.setText("Update");
                    btnAdminSave.setText("Update");
                }
                GetCollectionProcess(vs, vn);
            }
        });
    }

    private boolean CollectionDataValidation(EditText etVs, EditText etVn, String vs, String vn) {
        if (vs.isEmpty()) {
            _ct.ShowToast("Enter VS", true);
            etVs.requestFocus();
            return false;
        } else if (vn.isEmpty()) {
            _ct.ShowToast("Enter VN", true);
            etVn.requestFocus();
            return false;
        }
        return true;
    }

    private void GetCollectionProcess(String vs, String vn)
    {
        if(GetValidation(vs,vn)) {
            String xele = _inputXele.getDFinanceDataByMobileNo(Constants.DF_COLLECTION_MENUITEM_ID,vs,vn);
            _exeDb.ExecuteResult(SPName.USP_MA_DF_OtherCollectionData.toString(), xele, TransType.GetCollectionDataByVSNo.toString(), "4", Constants.HTTP_URL);
        }
    }

    public void OnDeleteClick(View view){
        ClearProcess();
        ShowEditDeleteDialog("Delete",false);
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
                if(weekOffList.size() > 0)
                {
                    long transId = Long.parseLong(_serverInfoData.getTransId());
                    int vn = Integer.parseInt(_serverInfoData.getVSNo());
                    for(DailyFinanceData data : weekOffList) {
                        if(data.isWeekDaySelected()) {
                            String voucherXele = _inputXele.getVoucherXele( Constants.DF_COLLECTION_MENUITEM_ID, String.valueOf(transId), Constants.DFC_VS, String.valueOf(vn), _serverInfoData.getServerDate(), _serverInfoData.getServerTime());
                            String collectionXele = _inputXele.getCollectionXelement(data, _collectionData, String.valueOf(transId), Constants.DF_COLLECTION_MENUITEM_ID);
                            String inputXele = "<Trans><Voucher>" + voucherXele + collectionXele + "</Voucher></Trans>";
                            new SaveMechanism().execute(SPName.USP_MA_DF_SaveCollection.toString(), inputXele, TransType.SaveDFCData.toString(), Constants.HTTP_URL);
                            vn++;
                            transId++;
                        }
                    }
                    UpdateSMSProperties();
                    //new DFSmsHelper(this,_collectionData).SendSms(LoginUserSession._loginUserData.CollMessage,_collectionData.getMobileNo());
                    new MessageBoxHelper(this).ShowMessageBox(true,"Transaction Saved Succesfully");
                }else{
                    String vXele = _inputXele.getVoucherXele(Constants.DF_COLLECTION_MENUITEM_ID, _serverInfoData.getTransId(), Constants.DFC_VS, _serverInfoData.getVSNo(), _serverInfoData.getServerDate(), _serverInfoData.getServerTime());
                    String cXele = _inputXele.getCollectionXelement(_collectionData, _serverInfoData.getTransId(), Constants.DF_COLLECTION_MENUITEM_ID);
                    String inputXele = "<Trans><Voucher>" + vXele + cXele + "</Voucher></Trans>";
                    _exeDb.ExecuteResult(SPName.USP_MA_DF_SaveCollection.toString(), inputXele, TransType.SaveDFCData.toString(), "3", Constants.HTTP_URL);
                }
            } else if (val.equals("3")) {
                ResultData res = new ResultData();
                res = new XmlConverter().ParseSaveData(nodeList, res);
                if (res.Result.equals("Success")) {
                    UpdateSMSProperties();
                    //new DFSmsHelper(this,_collectionData).SendSms(LoginUserSession._loginUserData.CollMessage,_collectionData.getMobileNo());
                    new MessageBoxHelper(this).ShowMessageBox(true,"Transaction Saved Succesfully");
                }
            }else if(val.equals("4")){
                hideKeyboard(this);
                List<DailyFinanceData> dfList = new ArrayList<>();
                dfList = new XmlConverter().ParseGetDFCInfoData(nodeList,dfList);// Changes On 21-10-2019
                if (dfList != null && dfList.size() > 0) {
                    DailyFinanceData _resultData = dfList.get(0);
                    if (isUpdate) {
                        etName.setEnabled(false);
                        etMobileNo.setEnabled(false);
                        etRefNo.setEnabled(false);
                        etCollDate.setText(_resultData.getDate());
                        etDate.setText(_resultData.getTransDate());
                        SetUserData(_resultData);
                    } else {
                        String xele = _inputXele.getDFDeleteXele(dfList.get(0).getTransId());
                        _exeDb.ExecuteResult(SPName.USP_MA_DF_OtherCollectionData.toString(), xele, TransType.DeleteCollectionData.toString(), "6", Constants.HTTP_URL);
                    }
                }
            }else if(val.equals("5")){
                ResultData _res = new ResultData();
                _res = new XmlConverter().ParseSaveData(nodeList,_res);
                if(_res!=null){
                    if(_res.Result.equals("Success")){
                        new MessageBoxHelper(this).ShowMessageBox(false,"Transaction Updated Successfully");
                        ClearProcess();
                    }
                }
            }else if(val.equals("6")){
                ResultData _res = new ResultData();
                _res = new XmlConverter().ParseSaveData(nodeList,_res);
                if(_res!=null){
                    if(_res.Result.equals("Success")){
                        //_ct.ShowToast("Delete Successfull",true);
                        new MessageBoxHelper(this).ShowMessageBox(false,"Transaction Deleted Successfully");
                        _searchDialog.dismiss();
                    }
                }
            }else if(val.equals("7")){
                weekOffList = new XmlConverter().ParseWeekOffDaysList(nodeList,weekOffList);
                if(weekOffList!=null && weekOffList.size() > 0) {
                    if (weekOffList.size() == 1) {
                        if (TransDateValidation(_collectionData.getTransDate(), _financeDate)) {
                            if(_collectionData.getDate().equals(weekOffList.get(0).getDate())){
                                SaveProcess("");
                            }else {
                                ShowWeekOffDaysPopUp();
                            }
                        } else {
                            //_ct.ShowToast("Date Must be After Finace Date", true);
                            new MessageBoxHelper(this).ShowOkMessageBox("Date Must be After Finance Date");
                        }
                    } else {
                        ShowWeekOffDaysPopUp();
                    }
                }
            }
        } else {
            if (val.equals("1")) {
                etName.setText("");
                etRefNo.setText("");
                etAmt.setText("");
                etMobileNo.setText("");
                _ct.ShowToast("Invalid MobileNo (Or) Finance Execution Completed",true);
            }else if(val.equals("6")){
                _ct.ShowToast("Status Completed, You Cannot delete this Collection",true);
                _searchDialog.dismiss();
            }else if(val.equals("4")){
                _ct.ShowToast("Status Completed, You Cannot delete this Collection",true);
                _searchDialog.dismiss();
                hideKeyboard(this);
            }else if(val.equals("7")){
                _ct.ShowToast("No Pending Collections For this Member",true);
                ClearProcess();
            }
        }
    }

    private int UpdateSMSProperties()
    {
        int i=0;
        for(DailyFinanceData data : weekOffList){
            if(data.isWeekDaySelected()){
                i++;
            }
        }
        _collectionData.setCollectionCount(i);
        if(i>0){
            _collectionData.setAmount(String.valueOf(getSelectedDaysAmount(_collectionData.getPerDayAmt(),i)));
        }
        return i;
    }

    private int getSelectedDaysAmount(String amount, int count)
    {
        Double value=0.0;
        value = count * (Double.parseDouble(amount));
        return value.intValue();
    }

    private boolean TransDateValidation(String selectedDateStr, String financeDateStr) {
        Date selectedDt = null;
        Date financeDt = null;
        try {
            selectedDt = new SimpleDateFormat("dd-MM-yyyy").parse(selectedDateStr);
            financeDt = new SimpleDateFormat("dd-MM-yyyy").parse(financeDateStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (selectedDt.after(financeDt)) {
            return true;
        }
        return false;
    }


    public static void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public void SetRecyclerViewItem(DailyFinanceData _data){
        SetUserData(_data);
    }

    private void SetUserData(DailyFinanceData _financeData){
        _collectionData = _financeData;
        _financeDate = _collectionData.getDate();
        etName.setText(_collectionData.getName());
        etRefNo.setText(_collectionData.getRefNo());
        etAmt.setText(_collectionData.getPerDayAmt());
        etRemarks.setText(_collectionData.getRemarks());
        etMobileNo.setText(_collectionData.getMobileNo());
        if(_searchDialog!=null) {
            _searchDialog.dismiss();
        }
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
