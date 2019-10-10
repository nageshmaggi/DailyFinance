package m_fusilsolutions.com.dailyfinance.Models;

import android.widget.EditText;

import m_fusilsolutions.com.dailyfinance.Constants.Constants;

/**
 * Created by Android on 03-10-2019.
 */

public class LocalRepoViewModel
{
    public DailyFinanceData _financeData;
    public LocalRepoViewModel(DailyFinanceData financeData){
        this._financeData = financeData;
    }

    public void CalculateNetAndPerDayAmt(EditText etNetAmt, EditText etPerDayAmt){
        long amt = Long.parseLong(_financeData.getAmount());
        Double netAmt = amt - (Constants.COMMISION_PERCENTAGE * amt);
        long perDayAmt = (int) Double.parseDouble(_financeData.getAmount()) / (Constants.NO_OF_FINANCE_DAYS);
        _financeData.setNetAmount(String.valueOf(netAmt.intValue()));
        _financeData.setPerDayAmt(String.valueOf(perDayAmt));
        etNetAmt.setText(_financeData.getNetAmount());
        etPerDayAmt.setText(_financeData.getPerDayAmt());
    }
}