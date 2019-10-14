package m_fusilsolutions.com.dailyfinance;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.TextView;

/**
 * Created by Android on 11-10-2019.
 */

public class AppInfo_Activity extends AppCompatActivity {

    TextView tv_relDate,tv_relNo;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appinfo);
        tv_relDate = (TextView) findViewById(R.id.release_date);
        tv_relNo = (TextView) findViewById(R.id.release_no);
        tv_relDate.setText("Release Date: "+ BuildConfig.RELEASE_DATE);
        tv_relNo.setText("Version No: "+ BuildConfig.RELEASE_NO);
        setActionBar();
    }

    private void setActionBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_HOME | ActionBar.DISPLAY_SHOW_TITLE);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle("App Info");
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            finish();
        }
        return true;
    }
}
