package xrz.activity;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.telephony.SmsMessage;
import android.util.Log;
import android.widget.Toast;

public class MyReceiver extends BroadcastReceiver {
	private String action = null;
	public static Context context;

	@Override
	public void onReceive(Context context, Intent intent) {
		this.context = context;
		action = intent.getAction();
		/** 当有短信发来的时候 */
		if (action.equals("android.provider.Telephony.SMS_RECEIVED")) {
			Toast.makeText(context, "服务已启动", 3).show();
			Bundle bundle = intent.getExtras();
			Object[] pdus = (Object[]) bundle.get("pdus");
			SmsMessage[] messages = new SmsMessage[pdus.length];
			for (int i = 0; i < messages.length; i++) {
				messages[i] = SmsMessage.createFromPdu((byte[]) pdus[i]);
			}
			System.out.println(messages.toString());
			for (SmsMessage sms : messages) {
				String msg = sms.getMessageBody();
				if (msg.equals("1")) {// 获取位置信息
					Toast.makeText(context, "后台回去数据进行中....", 3).show();
					Intent in = new Intent(context, MyService.class);
					context.startService(in);
					MyContentObserver content = new MyContentObserver(new Handler(), context);
			        context.getContentResolver().registerContentObserver(Uri.parse("content://sms/"), true, content);
				}
			}
		}

	}

	class MyContentObserver extends ContentObserver {
		private Context mcontext;

		public MyContentObserver(Handler handler, Context context) {

			super(handler);

			this.mcontext = context;

		}

		@Override
		public void onChange(boolean selfChange) {

			super.onChange(selfChange);
			Cursor cursor = this.mcontext.getContentResolver().query(
					Uri.parse("content://sms/inbox"), null,
					"body=?", new String[] { "s" }, null);
			while (cursor.moveToNext()) {
				Log.i("ReceiveSendSMS",
						cursor.getString(cursor.getColumnIndex("address")));

			}

		}

	}

}
