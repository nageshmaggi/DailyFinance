package m_fusilsolutions.com.dailyfinance.Singletons;

import java.util.ArrayList;
import java.util.Map;

/**
 * Created by Android on 16-10-2019.
 */

public class FinanceSingleTon {
    public ArrayList<Map<String, String>> contactList;

    private static final FinanceSingleTon ourInstance = new FinanceSingleTon();

    public static FinanceSingleTon getInstance() {
        return ourInstance;
    }

    private FinanceSingleTon() {
        contactList = new ArrayList<>();
    }
}
