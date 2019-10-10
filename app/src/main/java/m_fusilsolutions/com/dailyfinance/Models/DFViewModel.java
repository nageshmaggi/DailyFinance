package m_fusilsolutions.com.dailyfinance.Models;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import java.util.List;

import m_fusilsolutions.com.dailyfinance.Helpers.AsyncResponse;

/**
 * Created by Android on 03-10-2019.
 */

public class DFViewModel extends ViewModel implements AsyncResponse
{
    private MutableLiveData<List<DailyFinanceData>> dailyFinancers;

    public MutableLiveData<List<DailyFinanceData>> getDailyFinancers() {
        if (dailyFinancers == null) {
            dailyFinancers = new MutableLiveData<>();
            loadFinanceData();
        }
        return dailyFinancers;
    }

    private void loadFinanceData() {

    }

    @Override
    public void processFinishing(Object object, String val) {

    }
}
