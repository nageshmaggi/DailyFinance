package m_fusilsolutions.com.dailyfinance;

import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.AutoCompleteTextView;
import android.widget.SimpleAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Android on 14-10-2019.
 */

public class ContactPickActivity extends AppCompatActivity{

    AutoCompleteTextView etMobileNo;
    private ArrayList<Map<String, String>> contactList;
    private SimpleAdapter mAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.contact_pick_dialog);
        contactList = new ArrayList<>();
        etMobileNo = (AutoCompleteTextView) findViewById(R.id.etMobileNo);
        PopulatePeopleList();
        mAdapter = new SimpleAdapter(this, contactList, R.layout.singile_contact ,new String[] { "Name", "Phone" }, new int[] { R.id.tv_ContactName, R.id.tv_ContactNumber});
        etMobileNo.setAdapter(mAdapter);
    }

    public void PopulatePeopleList() {
        contactList.clear();
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
    }
}
