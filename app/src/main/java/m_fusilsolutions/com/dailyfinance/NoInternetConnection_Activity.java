package m_fusilsolutions.com.dailyfinance;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class NoInternetConnection_Activity extends Activity
{
	Button refresh=null;
	boolean access;
	NetworkInfo activeNetworkInfo;
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_no_internet_connection);
		refresh=(Button) findViewById(R.id.btnrefresh);
		refresh.setOnClickListener(new OnClickListener()
		{	
			@Override
			public void onClick(View v)
			{
				access=isNetworkAvailable();
				if(access==true)
				{
					startActivity(new Intent(NoInternetConnection_Activity.this,MainActivity.class));
				}
			}
		}) ;
	}		
	public boolean isNetworkAvailable() 
	{
		ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		 activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
		return activeNetworkInfo != null && activeNetworkInfo.isConnected();
	}
}
