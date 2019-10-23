package m_fusilsolutions.com.dailyfinance.Helpers;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;

import m_fusilsolutions.com.dailyfinance.MainActivity;

/**
 * Created by Android on 21-10-2019.
 */

public class MessageBoxHelper {
    Context mCtx;

    public MessageBoxHelper(Context mCtx) {
        this.mCtx = mCtx;
    }

    public void ShowMessageBox(boolean isSave, String message) {
        new AlertDialog.Builder(mCtx)
                .setTitle("Message")
                .setMessage(message)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        if (isSave)
                            mCtx.startActivity(new Intent(mCtx, MainActivity.class));
                    }
                })
                .setIcon(android.R.drawable.ic_menu_save)
                .show();

//        Timer timer = new Timer();
//        timer.schedule(new TimerTask() {
//            @Override
//            public void run() {
//                if (isSave)
//                    mCtx.startActivity(new Intent(mCtx, MainActivity.class));
//            }
//        },5000);
    }

    public void ShowOkMessageBox(String message) {
        new AlertDialog.Builder(mCtx)
                .setTitle("Message")
                .setMessage(message)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }
}
