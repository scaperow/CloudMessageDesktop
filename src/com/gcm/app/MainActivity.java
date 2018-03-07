package com.gcm.app;

import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import com.gcm.app.R;
import com.google.android.gcm.GCMRegistrar;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.StrictMode;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

@SuppressLint({ "NewApi", "NewApi", "NewApi", "NewApi", "NewApi" })
public class MainActivity extends Activity {

	public String GCM_SENDER_ID = "146531582959";
	public static Handler activityHandler = new Handler();
	public TextView textState = null;

	public static Handler onRegistered;
	public static Handler onUnregistered;
	public static Handler onMessage;
	public static Handler onError;
	public static Handler onRecoverableError;
	public static MainActivity instance;

	public static void showMessage(final String msg) {
		activityHandler.post(new Runnable() {
			public void run() {
				Toast.makeText(instance, msg, Toast.LENGTH_LONG).show();
			}
		});
	}

	@SuppressLint("NewApi")
	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		instance = this;
		setContentView(R.layout.activity_main);

		StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
				.detectDiskReads().detectDiskWrites().detectNetwork()
				.penaltyLog().build());
		StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
				.detectLeakedSqlLiteObjects().detectLeakedClosableObjects()
				.penaltyLog().penaltyDeath().build());

		registerEvent();
		textState = (TextView) this.findViewById(R.id.text_statue);

		GCMRegistrar.checkDevice(this);
		GCMRegistrar.checkManifest(this);

		if (checkIsRegistered() == true) {
			textState.setText("Registered");
			NotifyRemoteOnRegister(GCMRegistrar.getRegistrationId(this));
		} else {
			GCMRegistrar.register(this, GCM_SENDER_ID);
		}
	}

	public void registerEvent() {
		if (onError == null) {
			onError = new Handler() {
				public void handleMessage(Message message) {
					activityHandler.post(new Runnable() {
						public void run() {
							textState.setText("Registered");
						}
					});
				}
			};
		}

		if (onMessage == null) {
			onMessage = new Handler() {
				public void handleMessage(final Message message) {
					activityHandler.post(new Runnable() {
						public void run() {
							Bundle bundle = message.getData();
							String m = bundle.getString("message");

							textState.setText(m);
							Toast.makeText(MainActivity.this, m,
									Toast.LENGTH_LONG).show();
						}
					});
				}
			};
		}

		if (onUnregistered == null) {
			onUnregistered = new Handler() {
				public void handleMessage(Message message) {
					activityHandler.post(new Runnable() {
						public void run() {
							textState.setText("Unregistered");
						}
					});
				}
			};
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}

	public boolean checkIsRegistered() {
		final String registerId = GCMRegistrar.getRegistrationId(this);
		if (registerId == null || registerId == "") {
			return false;
		} else {
			activityHandler.post(new Runnable() {
				public void run() {
					textState.setText("Registered");
				}
			});
			return true;
		}
	}

	public static void NotifyRemoteOnRegister(final String registerId) {

		HttpGet request = new HttpGet(
				"http://192.168.1.106/Services/GCM.asmx/Register?registerId="
						+ registerId);
		try {
			HttpResponse response = new DefaultHttpClient().execute(request);
			if (response.getStatusLine().getStatusCode() == 200) {
				Log.d("-------------", "Notify to remote is success");
			}
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	// HttpGet request = new HttpGet(
	// "http://192.168.1.106:8080/CloudMessageServer/servlet/Register?registerid="
	// + registerId);

}
