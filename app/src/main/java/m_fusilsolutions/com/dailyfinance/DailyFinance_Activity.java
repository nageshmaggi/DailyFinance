package m_fusilsolutions.com.dailyfinance;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.design.widget.TextInputLayout;
import android.support.v4.widget.SwipeRefreshLayout;
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
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import org.w3c.dom.NodeList;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import m_fusilsolutions.com.dailyfinance.Adapters.FinanceAdapter;
import m_fusilsolutions.com.dailyfinance.Constants.Constants;
import m_fusilsolutions.com.dailyfinance.Constants.LoginUserSession;
import m_fusilsolutions.com.dailyfinance.Constants.SPName;
import m_fusilsolutions.com.dailyfinance.Constants.TransType;
import m_fusilsolutions.com.dailyfinance.Constants.WeekEnums;
import m_fusilsolutions.com.dailyfinance.CustomControls.CustomToast;
import m_fusilsolutions.com.dailyfinance.Helpers.AsyncResponse;
import m_fusilsolutions.com.dailyfinance.Helpers.DFSmsHelper;
import m_fusilsolutions.com.dailyfinance.Helpers.ExecuteDataBase;
import m_fusilsolutions.com.dailyfinance.Helpers.MessageBoxHelper;
import m_fusilsolutions.com.dailyfinance.Helpers.NetworkUtils;
import m_fusilsolutions.com.dailyfinance.Helpers.XmlConverter;
import m_fusilsolutions.com.dailyfinance.Models.DailyFinanceData;
import m_fusilsolutions.com.dailyfinance.Models.LocalRepoViewModel;
import m_fusilsolutions.com.dailyfinance.Models.ResultData;
import m_fusilsolutions.com.dailyfinance.Singletons.FinanceSingleTon;
import m_fusilsolutions.com.dailyfinance.Utils.InputUtils;

/**
 * Created by Android on 02-10-2019.
 */

public class DailyFinance_Activity extends
        AppCompatActivity implements AsyncResponse,
        AdapterView.OnItemSelectedListener,SwipeRefreshLayout.OnRefreshListener
{

    Typeface typeface, typefaceBold, typefaceBoldItalic;
    Button btnSave, btnGet, btnDelete, btnAdminSave, btnAdminClear;
    TextInputLayout tilRefNo;
    EditText etRefNo, etDate, etName, etAmt, etNetAmt, etPerDayAmt, etRemarks;
    EditText etMobileNo;
    SwipeRefreshLayout refreshLayout;
    //ImageView imgContact;
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
   private SimpleAdapter mAdapter;
    AutoCompleteTextView actNameOrMobile;
    List<DailyFinanceData> activeMemberList;
    Spinner spWeek;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daily_finance);
        if (new NetworkUtils(this).IsNetworkAvailable()) {
            etDate = (EditText) findViewById(R.id.etDate);
            etName = (EditText) findViewById(R.id.etName);
            etMobileNo = (EditText) findViewById(R.id.etMobileNo);
            etAmt = (EditText) findViewById(R.id.etAmount);
            etNetAmt = (EditText) findViewById(R.id.etNetAmt);
            etPerDayAmt = (EditText) findViewById(R.id.etPerDayAmt);
            etRemarks = (EditText) findViewById(R.id.etRemarks);
            refreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh);
            actNameOrMobile = (AutoCompleteTextView) findViewById(R.id.acTNameOrMobile);
            //imgContact = (ImageView) findViewById(R.id.img_contact);
            spWeek = (Spinner) findViewById(R.id.spWeek);
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
            _ct = new CustomToast(this);
            _inputXele = new InputUtils(this);
            dtFddMMyyyy = new SimpleDateFormat("dd-MM-yyyy", Locale.US);
            typeface = Typeface.createFromAsset(getAssets(), "Caviar-Dreams.ttf");
            typefaceBoldItalic = Typeface.createFromAsset(getAssets(), "CaviarDreams_BoldItalic.ttf");
            typefaceBold = Typeface.createFromAsset(getAssets(), "Caviar_Dreams_Bold.ttf");
            etRefNo.setTypeface(typeface);
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
                    if (!s.toString().isEmpty()) {
                        String str = s.toString();
                        _financeData.setAmount(str);
                        _localViewModel.CalculateNetAndPerDayAmt(_financeData, etNetAmt, etPerDayAmt);
                    } else {
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
            activeMemberList = new ArrayList<>();
            refreshLayout.setOnRefreshListener(this);
            spWeek.setAdapter(new ArrayAdapter<WeekEnums>(this,
                    android.R.layout.simple_list_item_1, WeekEnums.values()));
            spWeek.setOnItemSelectedListener(this);
            if(FinanceSingleTon.getInstance().contactList!=null && FinanceSingleTon.getInstance().contactList.size() > 0){
                mAdapter = new SimpleAdapter(this, FinanceSingleTon.getInstance().contactList, R.layout.single_contact,new String[] { "Name", "MobileNo" }, new int[] { R.id.tv_ContactName, R.id.tv_ContactNumber});
                actNameOrMobile.setAdapter(mAdapter);
                actNameOrMobile.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        //Map<String,String> selecteddata = contactList.get(position);
                        Object object = parent.getItemAtPosition(position);
                        Map<String,String> selecteddata = (Map<String,String>) object;
                        actNameOrMobile.setText("");
                        actNameOrMobile.setText(selecteddata.get("Name"));
                        if(selecteddata!=null){
                            etName.setText(selecteddata.get("Name"));
                            etMobileNo.setText(selecteddata.get("MobileNo"));
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
                            etName.setText("");
                            etMobileNo.setText("");
                        }
                    }

                    @Override
                    public void afterTextChanged(Editable s) {

                    }
                });
            }
        } else {
            startActivity(new Intent(this, NoInternetConnection_Activity.class));
        }
    }


    private void ShowContactPickDialog() {
        ArrayList<Map<String, String>> contactList = new ArrayList<>();
        contactList = PopulatePeopleList(contactList);
        SimpleAdapter mAdapter = new SimpleAdapter(this, contactList, R.layout.single_contact, new String[]{"Name", "Phone"}, new int[]{R.id.tv_ContactName, R.id.tv_ContactNumber});
        AlertDialog.Builder adb = new AlertDialog.Builder(this, R.style.MyDialogTheme);
        LayoutInflater li = getLayoutInflater();
        View lv = li.inflate(R.layout.contact_pick_dialog, null);
        AutoCompleteTextView etMobileNo = lv.findViewById(R.id.etMobileNo);
        etMobileNo.setAdapter(mAdapter);
        ImageView img_delete = lv.findViewById(R.id.imgV_close);
        adb.setView(lv);
        adb.setCancelable(false);
        AlertDialog _dialog = adb.create();
        _dialog.show();
        img_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                _dialog.dismiss();
            }
        });
    }

    public ArrayList<Map<String, String>> PopulatePeopleList(ArrayList<Map<String, String>> contactList) {
        Cursor people = getContentResolver().query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);
        while (people.moveToNext()) {
            String contactName = people.getString(people.getColumnIndex(
                    ContactsContract.Contacts.DISPLAY_NAME));

            String contactId = people.getString(people.getColumnIndex(
                    ContactsContract.Contacts._ID));
            String hasPhone = people.getString(people.getColumnIndex(
                    ContactsContract.Contacts.HAS_PHONE_NUMBER));

            if ((Integer.parseInt(hasPhone) > 0)) {

                // You know have the number so now query it like this
                Cursor phones = getContentResolver().query(
                        ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                        null,
                        ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = " + contactId,
                        null, null);
                while (phones.moveToNext()) {
                    String phoneNumber = phones.getString(
                            phones.getColumnIndex(
                                    ContactsContract.CommonDataKinds.Phone.NUMBER));

                    Map<String, String> NamePhoneType = new HashMap<String, String>();

                    NamePhoneType.put("Name", contactName);
                    NamePhoneType.put("Phone", phoneNumber);

                    contactList.add(NamePhoneType);
                }
                phones.close();
            }
        }
        people.close();
        startManagingCursor(people);
        return contactList;
    }

    private void SetScreenConfiguration() {
        if (LoginUserSession._loginUserData.CustomerId.equals("1")) {
            layoutNormal.setVisibility(View.GONE);
            layoutAdmin.setVisibility(View.VISIBLE);
            btnDelete.setTypeface(typefaceBold);
            btnAdminSave.setTypeface(typefaceBold);
            btnAdminClear.setTypeface(typefaceBold);
            btnGet.setTypeface(typefaceBold);
        } else {
            layoutNormal.setVisibility(View.VISIBLE);
            layoutAdmin.setVisibility(View.GONE);
        }
    }

    public void OnEditClick(View view) {
        ShowEditDeleteDialog("Edit", true);
    }

    private void ShowEditDeleteDialog(String title, boolean isEdit) {
        AlertDialog.Builder adb = new AlertDialog.Builder(this, R.style.MyDialogTheme);
        LayoutInflater li = getLayoutInflater();
        View lv = li.inflate(R.layout.search_dialog, null);
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
                if (VoucherDataValidation(etVs, etVn, vs, vn)) {
                    if (isEdit) {
                        isUpdate = true;
                        btnSave.setText("Update");
                        btnAdminSave.setText("Update");
                    }
                    GetFinanceUserProcess(vs, vn);
                }
            }
        });
    }

    private boolean VoucherDataValidation(EditText etVs, EditText etVn, String vs, String vn) {
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

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void OnDeleteClick(View view) {
        if (new NetworkUtils(this).IsNetworkAvailable()) {
            ClearProcess();
            ShowEditDeleteDialog("Delete", false);
        } else {
            startActivity(new Intent(this, NoInternetConnection_Activity.class));
        }
    }

    private void GetFinanceUserProcess(String vs, String vn) {
        if (GetValidation(vs, vn)) {
            String xele = _inputXele.getDFinanceDataByMobileNo(Constants.DF_MENUITEM_ID, vs, vn);
            _exeDb.ExecuteResult(SPName.USP_MA_DF_OtherFinanceData.toString(), xele, TransType.GetFinanceDataByVSNo.toString(), "3", Constants.HTTP_URL);
        }
    }

    private boolean GetValidation(String vs, String vn) {
        if (vs.isEmpty()) {
            _ct.ShowToast("Enter VS", true);
            return false;
        } else if (vn.isEmpty()) {
            _ct.ShowToast("Enter VN", true);
            return false;
        }
        return true;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void OnSaveClick(View view) {
        if (new NetworkUtils(this).IsNetworkAvailable()) {
            AssignControlData();
            if (!isUpdate) {
                if (SaveValidation()) {
                    SaveProcess();
                }
            } else {
                String xele = _inputXele.getDFUpdationXelement(_financeData, _financeData.getTransId());
                _exeDb.ExecuteResult(SPName.USP_MA_DF_OtherFinanceData.toString(), xele, TransType.UpdateDFData.toString(), "6", Constants.HTTP_URL);
            }
        } else {
            startActivity(new Intent(this, NoInternetConnection_Activity.class));
        }
    }

    public void OnClearClick(View view) {
        ClearProcess();
    }

    private void AssignControlData() {
        _financeData.setMobileNo(etMobileNo.getText().toString());
        _financeData.setDate(etDate.getText().toString());
        _financeData.setName(etName.getText().toString());
        _financeData.setRefNo(etRefNo.getText().toString());
        _financeData.setAmount(etAmt.getText().toString());
        _financeData.setNetAmount(etNetAmt.getText().toString());
        _financeData.setPerDayAmt(etPerDayAmt.getText().toString());
        _financeData.setRemarks(etRemarks.getText().toString());
    }

    private void SaveProcess() {
        String serXele = _inputXele.getServerInputXele(Constants.DF_MENUITEM_ID);
        _exeDb.ExecuteResult(SPName.USP_MA_DF_MaxData.toString(), serXele, TransType.GetMaxData.toString(), "1", Constants.HTTP_URL);
    }

    private boolean SaveValidation() {
        if (_financeData.getName().isEmpty()) {
            //_ct.ShowToast("Enter Name",false);
            etName.setError("Enter Name");
            etName.requestFocus();
            return false;
        } else if (_financeData.getMobileNo().isEmpty()) {
            //_ct.ShowToast("Enter MobileNo",false);
            etMobileNo.setError("Enter MobileNo");
            etMobileNo.requestFocus();
            return false;
        } else if (_financeData.getAmount().isEmpty()) {
            //_ct.ShowToast("Enter Amount",false);
            etAmt.setError("Enter AMount");
            etAmt.requestFocus();
            return false;
        }
        return true;
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
                String[] dateStr = _financeData.getDate().split("-");
                String day = dateStr[0];
                String month = dateStr[1];
                String year = dateStr[2];

                datePicker.updateDate(Integer.parseInt(year), Integer.parseInt(month) - 1, Integer.parseInt(day));
                datePicker.show();
                return true;
            }
        });
        etDate.setText(_financeData.getDate());
    }

    private void setActionBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_HOME | ActionBar.DISPLAY_SHOW_TITLE);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle(getResources().getString(R.string.nav_draw_trans_finances));
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
        etNetAmt.setText("");
        etPerDayAmt.setText("");
        etName.setText("");
        etMobileNo.setText("");
        etRemarks.setText("");
        etRefNo.setText("");
        _financeData = new DailyFinanceData();
        _serverInfoData = new DailyFinanceData();
        isUpdate = false;
        btnSave.setText("Save");
        btnAdminSave.setText("Save");
        _financeData.setDate(String.valueOf(dtFddMMyyyy.format(new Date())));
        DatePickerCalling();
        spWeek.setSelection(0);

    }

    private ArrayList<Map<String,String>> getList(){
        ArrayList<Map<String,String>> list = new ArrayList<>();
        for(int i=1; i<=200; i++){
            Map<String,String> obj = new HashMap<>();
            obj.put("Name","Nagesh"+i);
            obj.put("MobileNo","889781531"+i);
            list.add(obj);
        }
        return list;
    }

    @Override
    public void processFinishing(Object result, String val) {
        if (result != null) {
            NodeList nodeList = new XmlConverter().StringToXMLformat((String) result);
             if (val.equals("1")) {
                _serverInfoData = new XmlConverter().ParseServerInfoData(nodeList, _serverInfoData);
                if (_serverInfoData != null) {
                    String voucherXele = _inputXele.getVoucherXele(Constants.DF_MENUITEM_ID, _serverInfoData.getTransId(), Constants.DF_VS, _serverInfoData.getVSNo(), _serverInfoData.getServerDate(), _serverInfoData.getServerTime());
                    String dfXele = _inputXele.getDFSaveXelement(_financeData, _serverInfoData.getTransId(), Constants.DF_MENUITEM_ID);
                    String inputXele = "<Data>" + voucherXele + dfXele + "</Data>";
                    _exeDb.ExecuteResult(SPName.USP_MA_DF_SaveFinance.toString(), inputXele, TransType.SaveDFData.toString(), "2", Constants.HTTP_URL);
                }
            } else if (val.equals("2")) {
                ResultData res = new ResultData();
                res = new XmlConverter().ParseSaveData(nodeList, res);
                if (res.Result.equals("Success")) {
                    new DFSmsHelper(this,_financeData).SendSms(LoginUserSession._loginUserData.FinMessage,_financeData.getMobileNo());
                    new MessageBoxHelper(this).ShowMessageBox(true,"Transaction Saved Succesfully");
                } else {
                    _ct.ShowToast("Transaction UnSucessfull", false);
                }
            } else if (val.equals("3")) {
                hideKeyboard(this);
                List<DailyFinanceData> dfList = new ArrayList<>();
                dfList = new XmlConverter().ParseGetDFInfoData(nodeList, dfList);
                if (dfList != null && dfList.size() > 0) {
                    DailyFinanceData _resultData = dfList.get(0);
                    if (isUpdate) {
                        SetUserData(_resultData);
                    } else {
                        if (_resultData.getCollectionCount() == 0) {
                            String xele = _inputXele.getDFDeleteXele(dfList.get(0).getTransId());
                            _exeDb.ExecuteResult(SPName.USP_MA_DF_OtherFinanceData.toString(), xele, TransType.DeleteDFData.toString(), "4", Constants.HTTP_URL);
                        } else {
                            //_ct.ShowToast("Collection Done,You Cannot Perform Delete", true);
                            new MessageBoxHelper(this).ShowOkMessageBox("Collection Done,You Cannot Perform Deletion!!");
                        }
                    }
                }
            } else if (val.equals("4")) {
                ResultData _res = new ResultData();
                _res = new XmlConverter().ParseSaveData(nodeList, _res);
                if (_res != null) {
                    if (_res.Result.equals("Success")) {
                        //_ct.ShowToast("Delete Successfull", true);
                        new MessageBoxHelper(this).ShowMessageBox(false,"Transaction Deleted Successfully");
                        _searchDialog.dismiss();
                    }
                }
            } else if (val.equals("5")) {
                ResultData _res = new ResultData();
                _res = new XmlConverter().ParseSaveData(nodeList, _res);
                if (_res != null) {
                    if (_res.Result.equals("Success")) {
                        new MessageBoxHelper(this).ShowMessageBox(false,"Transaction Deleted Successfully");
                        //_ct.ShowToast("Delete Successfull", true);
                        ClearProcess();
                    }
                }
            } else if (val.equals("6")) {
                ResultData _res = new ResultData();
                _res = new XmlConverter().ParseSaveData(nodeList, _res);
                if (_res != null) {
                    if (_res.Result.equals("Success")) {
                        new MessageBoxHelper(this).ShowMessageBox(false,"Transaction Updated Successfully");
                        ClearProcess();
                    }
                }
            }
        } else {
            if (val.equals("2")) {
                _ct.ShowToast("Transaction UnSucessfull", false);
            } else if (val.equals("3")) {
                isUpdate = false;
                btnSave.setText("Save");
                btnAdminSave.setText("Save");
                _ct.ShowToast("Member Doesn't Exist", false);
                _searchDialog.dismiss();
            } else if (val.equals("6")) {
                _ct.ShowToast("Not Updated, Duplicate Data Exist", true);
            }
        }
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

    public static void showKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(activity);
        }
        imm.showSoftInputFromInputMethod(view.getWindowToken(), 0);
    }

    private void ShowConfirmDialog(DailyFinanceData data) {

        android.support.v7.app.AlertDialog.Builder alertDialogBuilder = new android.support.v7.app.AlertDialog.Builder(
                this);
        alertDialogBuilder.setTitle("Are you sure want to delete?");
        alertDialogBuilder.setCancelable(false);
        alertDialogBuilder.setPositiveButton("OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        String xele = _inputXele.getDFDeleteXele(data.getTransId());
                        _exeDb.ExecuteResult(SPName.USP_MA_DF_OtherFinanceData.toString(), xele, TransType.DeleteDFData.toString(), "5", Constants.HTTP_URL);
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

    public void SetRecyclerViewItem(DailyFinanceData _data) {
        SetUserData(_data);
    }

    private void SetUserData(DailyFinanceData _data) {
       spWeek.setSelection(getWeekOffDayFromWeek(_data.getWeekOffDay()));
        _financeData = _data;
        etName.setText(_data.getName());
        etRefNo.setText(_data.getRefNo());
        try {
            etAmt.setText(_financeData.getAmount());
        } catch (Exception e) {
        }
        if (_financeData.getCollectionCount() > 0) {
            etAmt.setEnabled(false);
        } else {
            etAmt.setEnabled(true);
        }
        etNetAmt.setText(_data.getNetAmount());
        etPerDayAmt.setText(_data.getPerDayAmt());
        etDate.setText(_data.getDate());
        etRemarks.setText(_data.getRemarks());
        etMobileNo.setText(_data.getMobileNo());
        _searchDialog.dismiss();
    }

    private int getWeekOffDayFromWeek(int weekOffDay)
    {
        List<WeekEnums> enumValues = Arrays.asList(WeekEnums.values());
        int position = 0;
        for(WeekEnums week : enumValues){
            if(week.getWeekValue()==weekOffDay){
                return position;
            }
            position++;
        }
        return position;
    }

    private void SearchResultDailog(List<DailyFinanceData> collectionList, String title, boolean isEdit) {
        AlertDialog.Builder adb = new AlertDialog.Builder(this, R.style.MyDialogTheme);
        LayoutInflater li = getLayoutInflater();
        View lv = li.inflate(R.layout.finance_multi_trans_dialog, null);
        ImageView imgClose = lv.findViewById(R.id.imgV_close);
        TextView tv_Dialog_Name = lv.findViewById(R.id.tvDialog_title);
        tv_Dialog_Name.setText(title);
        adb.setView(lv);
        adb.setCancelable(false);
        AlertDialog ad = adb.create();
        RecyclerView rvCollection = (RecyclerView) lv.findViewById(R.id.rvCollection);
        _adapter = new FinanceAdapter(this, collectionList, ad, isEdit);
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



    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String name = ((WeekEnums)spWeek.getSelectedItem()).name();
        int idd = ((WeekEnums)spWeek.getSelectedItem()).getWeekValue();
        _financeData.setWeekOffDay(idd);
    }


    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void onRefresh() {
        ClearProcess();
        refreshLayout.setRefreshing(false);
    }
}
