package m_fusilsolutions.com.dailyfinance.Constants;

/**
 * Created by Android on 15-10-2019.
 */

public enum WeekEnums
{
    None(0),
    Monday(2),
    Friday(6);
//    ,
//    Sunday(1),
//    Tuesday(3),
//    Wednesday(4),
//    Thursday(5),
//    Saturday(7);

    private int weekValue;

    public int getWeekValue() {
        return weekValue;
    }

    private WeekEnums(int val){
        weekValue = val;
    }
}
