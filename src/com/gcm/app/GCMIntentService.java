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

	// �յ�ע��Intent��˷����ᱻ���ã�GCM�����ע��ID����Ϊ�������ݵ��豸/Ӧ�ó���ԡ�ͨ������Ӧ�÷���regid����ķ������������������Ϳ��Ը������regid����Ϣ���豸�ϡ�
	public void onRegistered(Context context, String regId) {

		Message message = BuildMessage(KEY_REGISTERID, regId);

		if (MainActivity.onRegistered != null)
			MainActivity.onRegistered.sendMessage(message);

		MainActivity.NotifyRemoteOnRegister(regId);
	}

	// ���豸��GCMע��ʱ�ᱻ���á�ͨ����Ӧ�÷���regid���������������Ϳ���ע������豸�ˡ�
	public void onUnregistered(Context context, String regId) {
		Message message = BuildMessage(KEY_REGISTERID, regId);

		if (MainActivity.onUnregistered != null)
			MainActivity.onUnregistered.sendMessage(message);
	}

	// ����ķ�����������һ����Ϣ��GCM��ᱻ���ã�����GCM��������Ϣ���͵���Ӧ���豸����������Ϣ������Ч�������ݣ����ǵ����ݻ���ΪIntent��extras�����͡�
	public void onMessage(Context context, Intent intent) {
		Message message = BuildMessage(KEY_REGISTERID, intent);

		MainActivity.showMessage(intent.getStringExtra("msg"));
		// if (MainActivity.onMessage != null)
		// MainActivity.onMessage.sendMessage(message);
//		context.startActivity(new Intent(context, MainActivity.class)
//				.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
	}

	// ���豸��ͼע���ע��ʱ������GCM���ش���ʱ�˷����ᱻ���á�ͨ���˷������Ƿ��������޸������������������顣
	protected void onError(Context context, String errorId) {
		Message message = BuildMessage(KEY_REGISTERID, errorId);

		if (MainActivity.onError != null)
			MainActivity.onError.sendMessage(message);
	}

	// ���豸��ͼע���ע��ʱ������GCM��������Чʱ��GCM���ʹ��Ӧ���������Բ��������������ʽ����д������false����������ǿ�ѡ�Ĳ���ֻ�е�������ʾ��Ϣ���û�����ȡ�����Բ�����ʱ��Żᱻ��д��
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
