package com.gcm.app;

import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import com.google.android.gcm.GCMBaseIntentService;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;

public class GCMIntentService extends GCMBaseIntentService {

	public static String KEY_REGISTERID = "KEY_REGISTERID";
	public static String KEY_ERRORID = "KEY_ERRORID";

	public GCMIntentService() {

	}

	// 收到注册Intent后此方法会被调用，GCM分配的注册ID会做为参数传递到设备/应用程序对。通常，你应该发送regid到你的服务器，这样服务器就可以根据这个regid发消息到设备上。
	public void onRegistered(Context context, String regId) {

		Message message = BuildMessage(KEY_REGISTERID, regId);

		if (MainActivity.onRegistered != null)
			MainActivity.onRegistered.sendMessage(message);

		MainActivity.NotifyRemoteOnRegister(regId);
	}

	// 当设备从GCM注销时会被调用。通常你应该发送regid到服务器，这样就可以注销这个设备了。
	public void onUnregistered(Context context, String regId) {
		Message message = BuildMessage(KEY_REGISTERID, regId);

		if (MainActivity.onUnregistered != null)
			MainActivity.onUnregistered.sendMessage(message);
	}

	// 当你的服务器发送了一个消息到GCM后会被调用，并且GCM会把这个消息传送到相应的设备。如果这个消息包含有效负载数据，它们的内容会作为Intent的extras被传送。
	public void onMessage(Context context, Intent intent) {
		Message message = BuildMessage(KEY_REGISTERID, intent);

		MainActivity.showMessage(intent.getStringExtra("msg"));
		// if (MainActivity.onMessage != null)
		// MainActivity.onMessage.sendMessage(message);
//		context.startActivity(new Intent(context, MainActivity.class)
//				.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
	}

	// 当设备试图注册或注销时，但是GCM返回错误时此方法会被调用。通常此方法就是分析错误并修复问题而不会做别的事情。
	protected void onError(Context context, String errorId) {
		Message message = BuildMessage(KEY_REGISTERID, errorId);

		if (MainActivity.onError != null)
			MainActivity.onError.sendMessage(message);
	}

	// 当设备试图注册或注销时，但是GCM服务器无效时。GCM库会使用应急方案重试操作，除非这个方式被重写并返回false。这个方法是可选的并且只有当你想显示信息给用户或想取消重试操作的时候才会被重写。
	protected boolean onRecoverableError(Context context, String errorId) {

		Message message = BuildMessage(KEY_REGISTERID, errorId);

		if (MainActivity.onRecoverableError != null)
			MainActivity.onRecoverableError.sendMessage(message);

		return false;
	}

	public static Message BuildMessage(String key, String value) {
		Bundle bundle = new Bundle();
		bundle.putString(key, value);
		Message message = new Message();
		message.setData(bundle);
		return message;
	}

	public static Message BuildMessage(String key, Intent intent) {
		Message message = new Message();
		// Bundle bundle = intent.getExtras();
		Bundle bundle = new Bundle();
		bundle.putString("message", intent.getStringExtra("msg"));

		message.setData(bundle);
		return message;
	}

}
