package m_fusilsolutions.com.dailyfinance;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.content.ContextCompat;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.NodeList;

import m_fusilsolutions.com.dailyfinance.Constants.Constants;
import m_fusilsolutions.com.dailyfinance.Constants.LoginUserSession;
import m_fusilsolutions.com.dailyfinance.Constants.SPName;
import m_fusilsolutions.com.dailyfinance.Constants.TransType;
import m_fusilsolutions.com.dailyfinance.CustomControls.CustomToast;
import m_fusilsolutions.com.dailyfinance.Helpers.AsyncResponse;
import m_fusilsolutions.com.dailyfinance.Helpers.ExecuteDataBase;
import m_fusilsolutions.com.dailyfinance.Helpers.NetworkUtils;
import m_fusilsolutions.com.dailyfinance.Helpers.XmlConverter;
import m_fusilsolutions.com.dailyfinance.Utils.InputUtils;

public class UserLoginActivity extends Activity
        implements View.OnClickListener, AsyncResponse{

    TextView btnLogin;
    ImageView imageLogo;
    EditText etusername,etuserpassword;
    String username,userpassword;
    ExecuteDataBase db;
    Typeface typeface, typefaceBold, typefaceBoldItalic;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        btnLogin = (TextView) findViewById(R.id.txt_login);
        imageLogo = (ImageView) findViewById(R.id.img_logo);
        etusername = (EditText) findViewById(R.id.email);
        etuserpassword = (EditText) findViewById(R.id.pass);
        typeface = Typeface.createFromAsset(getAssets(), "Caviar-Dreams.ttf");
        typefaceBoldItalic = Typeface.createFromAsset(getAssets(), "CaviarDreams_BoldItalic.ttf");
        typefaceBold = Typeface.createFromAsset(getAssets(), "Caviar_Dreams_Bold.ttf");
        etusername.setTypeface(typefaceBold);
        etuserpassword.setTypeface(typefaceBold);
        btnLogin.setTypeface(typefaceBold);
        db = new ExecuteDataBase(this);
        btnLogin.setOnClickListener(this);
        SetStatusBarColor();
        etusername.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)){
                    etusername.clearFocus();
                    etuserpassword.requestFocus();
                }
                return false;
            }
        });
        //etusername.setText("Admin");
        //etuserpassword.setText("a");
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void SetStatusBarColor()
    {
        Window window = this.getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(ContextCompat.getColor(this,R.color.statusbar_color));
    }

    @Override
    public void onBackPressed()
    {
        Intent a = new Intent(Intent.ACTION_MAIN);
        a.addCategory(Intent.CATEGORY_HOME);
        a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(a);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onClick(View view) {
        if (new NetworkUtils(this).IsNetworkAvailable()) {
            int id = view.getId();
            if (id == R.id.txt_login) {
                username = etusername.getText().toString();
                userpassword = etuserpassword.getText().toString();
                if (LoginValidation()) {
                    LoginProcess();
                }
            }
        } else {
            startActivity(new Intent(this, NoInternetConnection_Activity.class));
        }
    }

    private void LoginProcess()
    {
        String inputxele=new InputUtils(this).getLoginXele(username, userpassword);
        String sp= SPName.USP_MA_DF_Login.toString();
        String tt= TransType.CheckLoginData.toString();
        db.ExecuteResult(sp,inputxele,tt,"0", Constants.HTTP_URL);
    }

    private boolean LoginValidation() {
        if (username.isEmpty() && userpassword.isEmpty()) {
            Toast.makeText(this, "Enter Login Credentials", Toast.LENGTH_SHORT).show();
            etusername.requestFocus();
            return false;
        } else if (username.isEmpty()) {
            Toast.makeText(this, "Enter UserName", Toast.LENGTH_SHORT).show();
            etusername.setError("UserName");
            etusername.requestFocus();
            return false;
        } else if (userpassword.isEmpty()) {
            Toast.makeText(this, "Enter Password", Toast.LENGTH_SHORT).show();
            etuserpassword.setError("Password");
            etuserpassword.requestFocus();
            return false;
        }
        return true;
    }

    @Override
    public void processFinishing(Object result, String val) {
        if(result!=null){
            NodeList nodeList = new XmlConverter().StringToXMLformat((String) result);
            LoginUserSession._loginUserData = new XmlConverter().LoginUserParentToChildIteration(nodeList, LoginUserSession._loginUserData);
            if(LoginUserSession._loginUserData.CustomerId != "0"){
                startActivity(new Intent(this,MainActivity.class));
            }
        }else{
            new CustomToast(this).ShowToast("Invalid Login Credentials",false);
            etuserpassword.setText("");
        }
    }
}
