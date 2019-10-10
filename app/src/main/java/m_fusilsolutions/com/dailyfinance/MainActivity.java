package m_fusilsolutions.com.dailyfinance;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Spannable;
import android.text.SpannableString;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.w3c.dom.NodeList;

import java.util.ArrayList;
import java.util.List;

import m_fusilsolutions.com.dailyfinance.Adapters.DashBoardAdapter;
import m_fusilsolutions.com.dailyfinance.Constants.Constants;
import m_fusilsolutions.com.dailyfinance.Constants.SPName;
import m_fusilsolutions.com.dailyfinance.Constants.TransType;
import m_fusilsolutions.com.dailyfinance.CustomControls.CustomTypefaceSpan;
import m_fusilsolutions.com.dailyfinance.Helpers.AsyncResponse;
import m_fusilsolutions.com.dailyfinance.Helpers.ExecuteDataBase;
import m_fusilsolutions.com.dailyfinance.Helpers.XmlConverter;
import m_fusilsolutions.com.dailyfinance.Models.DFViewModel;
import m_fusilsolutions.com.dailyfinance.Models.DailyFinanceData;
import m_fusilsolutions.com.dailyfinance.Models.DashBoardData;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        View.OnClickListener,AsyncResponse,SwipeRefreshLayout.OnRefreshListener {

    RecyclerView rVHome;
    List<DashBoardData> _dashBoardList;
    Button btn_daily_finance, btn_collection;
    Typeface typeface, typefaceBold, typefaceBoldItalic;
    TextView tvCompName, tvCompINfo;
    ExecuteDataBase _exeDb;
    SwipeRefreshLayout swipeLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        rVHome = (RecyclerView) findViewById(R.id.recyclerView);
        swipeLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh);
        btn_daily_finance = (Button) findViewById(R.id.btn_dailyfinance);
        btn_collection = (Button) findViewById(R.id.btn_collection);
        final View headerView = LayoutInflater.from(MainActivity.this).inflate(R.layout.nav_header_main, null);
        tvCompName = (TextView) headerView.findViewById(R.id.tvcompnyname);
        tvCompINfo = (TextView) headerView.findViewById(R.id.tvcompnyinfo);
        typeface = Typeface.createFromAsset(getAssets(), "Caviar-Dreams.ttf");
        typefaceBoldItalic = Typeface.createFromAsset(getAssets(), "CaviarDreams_BoldItalic.ttf");
        typefaceBold = Typeface.createFromAsset(getAssets(), "Caviar_Dreams_Bold.ttf");
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
        _exeDb.ExecuteResult(SPName.USP_MA_DF_DailyFinance.toString(), "<Data Name='abc'/>", TransType.GetDFTotals.toString(), "1", Constants.HTTP_URL);
        swipeLayout.setOnRefreshListener(this);
    }

    @Override
    public void onRefresh() {
        _exeDb.ExecuteResult(SPName.USP_MA_DF_DailyFinance.toString(), "<Data Name='abc'/>", TransType.GetDFTotals.toString(), "1", Constants.HTTP_URL);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.nav_daily_finance) {
            startActivity(new Intent(this, DailyFinance_Activity.class));
        } else if (id == R.id.nav_collection) {
            startActivity(new Intent(this, Collection_Activity.class));
        }else if (id == R.id.nav_daily_finance_report) {
            Intent intent = new Intent(this, DailyFinanceReport.class);
            intent.putExtra("screen",1);
            startActivity(intent);
        }else if (id == R.id.nav_collection_report) {
            Intent intent = new Intent(this, DailyFinanceReport.class);
            intent.putExtra("screen",2);
            startActivity(intent);
        }else if(id == R.id.nav_daily_active_finance_report){
            Intent intent = new Intent(this, DailyFinanceReport.class);
            intent.putExtra("screen",3);
            startActivity(intent);
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
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
        if (object != null) {
            NodeList nodeList = new XmlConverter().StringToXMLformat((String) object);
            if (val.equals("1")) {
                swipeLayout.setRefreshing(false);
                rVHome.setAdapter(null);
                _dashBoardList = new ArrayList<>();
                _dashBoardList = new XmlConverter().ParseDashBoardInfoData(nodeList, _dashBoardList);
                if (_dashBoardList != null && _dashBoardList.size() > 0) {
                    DashBoardAdapter adapter = new DashBoardAdapter(this, _dashBoardList);
                    rVHome.setAdapter(adapter);
                    rVHome.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
                    rVHome.setHasFixedSize(true);
                    rVHome.setNestedScrollingEnabled(false);
                }
            }
        }else{
            if(val.equals("1")){
                _dashBoardList = new ArrayList<>();
                DashBoardData data1 = new DashBoardData("Totals", "0", R.mipmap.totals);
                DashBoardData data2 = new DashBoardData("Collected", "0", R.mipmap.collected);
                DashBoardData data3 = new DashBoardData("Tobe Collected","0", R.mipmap.tobecollected);
                _dashBoardList.add(data1);
                _dashBoardList.add(data2);
                _dashBoardList.add(data3);
                DashBoardAdapter adapter = new DashBoardAdapter(this, _dashBoardList);
                rVHome.setAdapter(adapter);
                rVHome.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
                rVHome.setHasFixedSize(true);
                rVHome.setNestedScrollingEnabled(false);
            }
        }
    }
}
