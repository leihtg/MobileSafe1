package xrz.utils;

import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import xrz.activity.MyService;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ParseException;
import android.os.Bundle;
import android.util.Log;

public class GetLocation {

	private static LocationManager mLocationManager;
	private static JSONObject object=null;
	private static String mLocation=null;
	
	public static void getLocationMessage(final Context context){
		mLocationManager=(LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
		LocationListener lis=new LocationListener() {
			
			public void onStatusChanged(String provider, int status, Bundle extras) {
				
			}
			
			public void onProviderEnabled(String provider) {
				
			}
			
			public void onProviderDisabled(String provider) {
				
			}
			public void onLocationChanged(final Location location) {
						object=getJSONDate(location);
						mLocation=JxJSONDate(object);
						if(mLocation != null){
							Intent service=new Intent(context,MyService.class);
							service.putExtra("str", mLocation);
							service.putExtra("num", 1);
							context.startService(service);
							mLocationManager.removeUpdates(this);
							
						}
			}

			private JSONObject getJSONDate(Location location) {
				JSONObject object=null;
				HttpGet get=new HttpGet("http://ditu.google.cn/maps/geo?q="+location.getLatitude()+","+location.getLongitude()+"");
				HttpResponse res = null;
				try {
					res = new DefaultHttpClient().execute(get);
				} catch (ClientProtocolException e1) {
					Log.i("ClientProtocolException", e1.toString());
					e1.printStackTrace();
				} catch (IOException e1) {
					Log.i("IOException", e1.toString());
					e1.printStackTrace();
				}
				try {
					if(res.getStatusLine().getStatusCode() == 200){
						try {
							object=new JSONObject(EntityUtils.toString(res.getEntity()));
							return object;
						} catch (ParseException e) {
							e.printStackTrace();
							return null;
						} catch (JSONException e) {
							e.printStackTrace();
							return null;
						}
					}
				} catch (ClientProtocolException e) {
					e.printStackTrace();
					return null;
				} catch (IOException e) {
					e.printStackTrace();
					return null;
				}
				return null;
			}
		};
		mLocationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 5, 5, lis);
	}
	
	
	public static String JxJSONDate(JSONObject object){
		String str;
		JSONArray array;
		try {
			array = object.getJSONArray("Placemark");
			JSONObject j1=array.getJSONObject(0);
			str=j1.getString("address");
		} catch (JSONException e) {
			e.printStackTrace();
			return null;
		}
		return str;
	}
}
