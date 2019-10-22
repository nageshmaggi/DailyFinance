package m_fusilsolutions.com.dailyfinance;

import android.Manifest;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Spannable;
import android.text.SpannableString;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.NodeList;

import java.util.ArrayList;
import java.util.List;

import m_fusilsolutions.com.dailyfinance.Adapters.DashBoardAdapter;
import m_fusilsolutions.com.dailyfinance.Adapters.DashBoardTotalsAdapter;
import m_fusilsolutions.com.dailyfinance.Constants.Constants;
import m_fusilsolutions.com.dailyfinance.Constants.SPName;
import m_fusilsolutions.com.dailyfinance.Constants.TransType;
import m_fusilsolutions.com.dailyfinance.CustomControls.CustomToast;
import m_fusilsolutions.com.dailyfinance.CustomControls.CustomTypefaceSpan;
import m_fusilsolutions.com.dailyfinance.Helpers.AsyncResponse;
import m_fusilsolutions.com.dailyfinance.Helpers.ExecuteDataBase;
import m_fusilsolutions.com.dailyfinance.Helpers.NetworkUtils;
import m_fusilsolutions.com.dailyfinance.Helpers.XmlConverter;
import m_fusilsolutions.com.dailyfinance.Models.DFViewModel;
import m_fusilsolutions.com.dailyfinance.Models.DailyFinanceData;
import m_fusilsolutions.com.dailyfinance.Models.DashBoardData;
import m_fusilsolutions.com.dailyfinance.Singletons.FinanceSingleTon;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        View.OnClickListener,AsyncResponse,SwipeRefreshLayout.OnRefreshListener {

    RecyclerView rVHomeDayWise,rVHomeAllTotals;
    List<DashBoardData> _dashBoardList;
    Button btn_daily_finance, btn_collection;
    Typeface typeface, typefaceBold, typefaceBoldItalic;
    TextView tvCompName, tvCompINfo, tvTotStatus,tvTodayStatus;
    ExecuteDataBase _exeDb;
    CustomToast _ct;
    //SwipeRefreshLayout swipeLayout;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if(new NetworkUtils(this).IsNetworkAvailable()) {

            Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);
            rVHomeDayWise = (RecyclerView) findViewById(R.id.recyclerViewDayWise);
            rVHomeAllTotals = (RecyclerView) findViewById(R.id.recyclerViewTotal);
            //swipeLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh);
            btn_daily_finance = (Button) findViewById(R.id.btn_dailyfinance);
            btn_collection = (Button) findViewById(R.id.btn_collection);
            final View headerView = LayoutInflater.from(MainActivity.this).inflate(R.layout.nav_header_main, null);
            tvCompName = (TextView) headerView.findViewById(R.id.tvcompnyname);
            tvCompINfo = (TextView) headerView.findViewById(R.id.tvcompnyinfo);
            tvTotStatus = (TextView) findViewById(R.id.tvTopDashTil);
            tvTodayStatus = (TextView) findViewById(R.id.tvBottomDashTil);
            _ct = new CustomToast(this);
            typeface = Typeface.createFromAsset(getAssets(), "Caviar-Dreams.ttf");
            typefaceBoldItalic = Typeface.createFromAsset(getAssets(), "CaviarDreams_BoldItalic.ttf");
            typefaceBold = Typeface.createFromAsset(getAssets(), "Caviar_Dreams_Bold.ttf");
            tvTotStatus.setTypeface(typefaceBold);
            tvTodayStatus.setTypeface(typefaceBold);
            tvCompName.setTypeface(typefaceBold);
            tvCompINfo.setTypeface(typeface);
            _dashBoardList = new ArrayList<>();
            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                    this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
            drawer.addDrawerListener(toggle);
            toggle.syncState();

            NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
            Menu m = navigationView.getMenu();
            for (int i = 0; i < m.size(); i++) {
                MenuItem mi = m.getItem(i);
                SubMenu subMenu = mi.getSubMenu();
                if (subMenu != null && subMenu.size() > 0) {
                    for (int j = 0; j < subMenu.size(); j++) {
                        MenuItem subMenuItem = subMenu.getItem(j);
                        applyFontToMenuItem(subMenuItem, typeface);
                    }
                }
                applyFontToMenuItem(mi, typefaceBold);
            }

            navigationView.setNavigationItemSelectedListener(this);
            btn_daily_finance.setTypeface(typefaceBold);
            btn_collection.setTypeface(typefaceBold);
            btn_daily_finance.setOnClickListener(this);
            btn_collection.setOnClickListener(this);
            DFViewModel viewModel = ViewModelProviders.of(this).get(DFViewModel.class);
            viewModel.getDailyFinancers().observe(this, new Observer<List<DailyFinanceData>>() {
                @Override
                public void onChanged(@Nullable List<DailyFinanceData> dailyFinanceData) {

                }
            });
            _exeDb = new ExecuteDataBase(this);
            _exeDb.ExecuteResult(SPName.USP_MA_DF_DashBoard.toString(), "<Data Name='abc'/>", TransType.GetDailyFinanceTotals.toString(), "1", Constants.HTTP_URL);
            _exeDb.ExecuteResult(SPName.USP_MA_DF_DashBoard.toString(), "<Data Name='abc'/>", TransType.GetFinanceTotals.toString(), "2", Constants.HTTP_URL);
            //swipeLayout.setOnRefreshListener(this);
            checkPermission();
            checkSelfPermission();
            GetDistinctMemberList();
        }
        else
        {
            startActivity(new Intent(this,NoInternetConnection_Activity.class));
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater mi = getMenuInflater();
        mi.inflate(R.menu.homescreen_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id==R.id.miRefersh)
        {
            startActivity(new Intent(this,MainActivity.class));
        }
        return true;
    }


    private void GetDistinctMemberList(){
        String inpXele = "<Data><StatusData Status='0' /><StatusData Status='0' /></Data>";
        _exeDb.ExecuteResult(SPName.USP_MA_DF_DistinctFinanceMembers.toString(),inpXele,TransType.GetDistinctFinaceUsers.toString(),"0",Constants.HTTP_URL);
    }


    private void checkPermission(){
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS)== PackageManager.PERMISSION_GRANTED){
            return;
        }else{
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.READ_CONTACTS},0);
        }
    }

    private void checkSelfPermission(){
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_EXTERNAL_STORAGE},1);
        }
    }
    //New Change 14102019
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode==1){
            Toast.makeText(this,"Permission Granted",Toast.LENGTH_SHORT).show();
        }
    }



    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onRefresh() {
        if (new NetworkUtils(this).IsNetworkAvailable()) {
            _exeDb.ExecuteResult(SPName.USP_MA_DF_DashBoard.toString(), "<Data Name='abc'/>", TransType.GetDailyFinanceTotals.toString(), "1", Constants.HTTP_URL);
            //swipeLayout.setRefreshing(false);
        }else{
            startActivity(new Intent(this, NoInternetConnection_Activity.class));
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            ShowLogOutDialog();
        }
    }

    private void ShowLogOutDialog() {

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                this);
        alertDialogBuilder.setTitle("Are you sure want to Logout?");
        alertDialogBuilder.setCancelable(false);
        alertDialogBuilder.setPositiveButton("OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id)
                    {
                        startActivity(new Intent(MainActivity.this,UserLoginActivity.class));
                    }
                });
        alertDialogBuilder.setNegativeButton("Cancel",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }



    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (id == R.id.nav_daily_finance) {
            startActivity(new Intent(this, DailyFinance_Activity.class));
            drawer.closeDrawer(GravityCompat.START);
        } else if (id == R.id.nav_collection) {
            startActivity(new Intent(this, Collection_Activity.class));
            drawer.closeDrawer(GravityCompat.START);
        } else if (id == R.id.nav_daily_finance_report) {
            Intent intent = new Intent(this, DailyFinanceReport_Activity.class);
            intent.putExtra("screen", 1);
            startActivity(intent);
            drawer.closeDrawer(GravityCompat.START);
        } else if (id == R.id.nav_collection_report) {
            Intent intent = new Intent(this, DailyFinanceReport_Activity.class);
            intent.putExtra("screen", 2);
            startActivity(intent);
            drawer.closeDrawer(GravityCompat.START);
        } else if (id == R.id.nav_daily_active_finance_report) {
            Intent intent = new Intent(this, DailyFinanceReport_Activity.class);
            intent.putExtra("screen", 3);
            startActivity(intent);
            drawer.closeDrawer(GravityCompat.START);
        } else if (id == R.id.nav_memberwise_daily_finance_report) {
            startActivity(new Intent(this, Memberwise_DailyFinaceReport_Activity.class));
            drawer.closeDrawer(GravityCompat.START);
        } else if (id == R.id.nav_about) {
            startActivity(new Intent(this, AboutUs_Activity.class));
            drawer.closeDrawer(GravityCompat.START);
        } else if (id == R.id.nav_appinfo) {
            startActivity(new Intent(this, AppInfo_Activity.class));
            drawer.closeDrawer(GravityCompat.START);
        } else if(id == R.id.nav_pending_collection_report){
            startActivity(new Intent(this, PendingCollectionsReport_Activity.class));
        }
        return true;
    }

    private void applyFontToMenuItem(MenuItem mi, Typeface typeface) {
        SpannableString mNewTitle = new SpannableString(mi.getTitle());
        mNewTitle.setSpan(new CustomTypefaceSpan("", typeface), 0, mNewTitle.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        mi.setTitle(mNewTitle);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.btn_dailyfinance) {
            startActivity(new Intent(this, DailyFinance_Activity.class));
        } else if (id == R.id.btn_collection) {
            startActivity(new Intent(this, Collection_Activity.class));
        }
    }

    @Override
    public void processFinishing(Object object, String val) {
        //swipeLayout.setRefreshing(false);
        if (object != null) {
            NodeList nodeList = new XmlConverter().StringToXMLformat((String) object);
            if(val.equals("0")) {
                FinanceSingleTon.getInstance().contactList = new ArrayList<>();
                FinanceSingleTon.getInstance().contactList = new XmlConverter().ParseDistinctMemberInfoData(nodeList, FinanceSingleTon.getInstance().contactList);
            } if (val.equals("1")) {
                rVHomeDayWise.setAdapter(null);
                _dashBoardList = new ArrayList<>();
                _dashBoardList = new XmlConverter().ParseDashBoardInfoData(nodeList, _dashBoardList);
                if (_dashBoardList != null && _dashBoardList.size() > 0) {
                    DashBoardAdapter adapter = new DashBoardAdapter(this, _dashBoardList);
                    rVHomeDayWise.setAdapter(adapter);
                    rVHomeDayWise.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
                    rVHomeDayWise.setHasFixedSize(true);
                    rVHomeDayWise.setNestedScrollingEnabled(false);
                }
            }
            else if(val.equals("2")) {
                rVHomeAllTotals.setAdapter(null);
                _dashBoardList = new ArrayList<>();
                _dashBoardList = new XmlConverter().ParseDashBoardTopInfoData(nodeList, _dashBoardList);
                if (_dashBoardList != null && _dashBoardList.size() > 0) {
                    DashBoardTotalsAdapter adapter = new DashBoardTotalsAdapter(this, _dashBoardList);
                    rVHomeAllTotals.setAdapter(adapter);
                    rVHomeAllTotals.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
                    rVHomeAllTotals.setHasFixedSize(true);
                    rVHomeAllTotals.setNestedScrollingEnabled(false);
                }
            }
        } else {
            if (val.equals("1")) {
                _ct.ShowToast("No data found for Today's Status",false);
                _dashBoardList = new ArrayList<>();
                DashBoardData data1 = new DashBoardData("Totals", "0", R.mipmap.totals);
                DashBoardData data2 = new DashBoardData("Collected", "0", R.mipmap.collected);
                DashBoardData data3 = new DashBoardData("Tobe Collected", "0", R.mipmap.tobecollected);
                _dashBoardList.add(data1);
                _dashBoardList.add(data2);
                _dashBoardList.add(data3);
                DashBoardAdapter adapter = new DashBoardAdapter(this, _dashBoardList);
                rVHomeDayWise.setAdapter(adapter);
                rVHomeDayWise.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
                rVHomeDayWise.setHasFixedSize(true);
                rVHomeDayWise.setNestedScrollingEnabled(false);
            }else if(val.equals("2")){
                _ct.ShowToast("No data found for Total Status",false);
                _dashBoardList = new ArrayList<>();
                DashBoardData data1 = new DashBoardData("Total Finance", "0", R.mipmap.total_capital);
                DashBoardData data2 = new DashBoardData("Total Collection", "0", R.mipmap.total_collection);
                DashBoardData data3 = new DashBoardData("Balance","0", R.mipmap.balance);
                _dashBoardList.add(data1);
                _dashBoardList.add(data2);
                _dashBoardList.add(data3);
                DashBoardTotalsAdapter adapter = new DashBoardTotalsAdapter(this, _dashBoardList);
                rVHomeAllTotals.setAdapter(adapter);
                rVHomeAllTotals.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
                rVHomeAllTotals.setHasFixedSize(true);
                rVHomeAllTotals.setNestedScrollingEnabled(false);
            }
//            else
//                _ct.ShowToast("No data found",false);
        }
    }
}
