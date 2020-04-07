package com.transport.xianxian.utils.jpush;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;

import com.transport.xianxian.activity.NoticeListActivity;
import com.transport.xianxian.activity.OrderDetailsActivity;
import com.transport.xianxian.activity.WebContentActivity;
import com.transport.xianxian.utils.MyLogger;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;

import cn.jpush.android.api.JPushInterface;

/**
 * 自定义接收器
 * <p>
 * 如果不定义这个 Receiver，则：
 * 1) 默认用户会打开主界面
 * 2) 接收不到自定义消息
 */
public class MyReceiver extends BroadcastReceiver {
    private static final String TAG = "收到的消息推送";

    static String model = "";

    @Override
    public void onReceive(Context context, Intent intent) {
        try {
            Bundle bundle = intent.getExtras();
            MyLogger.i(TAG, "[MyReceiver] onReceive - " + intent.getAction() + ", extras: " + printBundle(bundle));

            if (JPushInterface.ACTION_REGISTRATION_ID.equals(intent.getAction())) {
                String regId = bundle.getString(JPushInterface.EXTRA_REGISTRATION_ID);
                MyLogger.i(TAG, "[MyReceiver] 接收Registration Id : " + regId);
                //send the Registration Id to your server...

            } else if (JPushInterface.ACTION_MESSAGE_RECEIVED.equals(intent.getAction())) {
                MyLogger.i(TAG, "[MyReceiver] 接收到推送下来的自定义消息: " + bundle.getString(JPushInterface.EXTRA_MESSAGE));
//				processCustomMessage(context, bundle);

            } else if (JPushInterface.ACTION_NOTIFICATION_RECEIVED.equals(intent.getAction())) {
                MyLogger.i(TAG, "[MyReceiver] 接收到推送下来的通知");
                int notifactionId = bundle.getInt(JPushInterface.EXTRA_NOTIFICATION_ID);
                MyLogger.i(TAG, "[MyReceiver] 接收到推送下来的通知的ID: " + notifactionId);


                JSONObject jObj = new JSONObject(model);
                String sound_type = jObj.getString("sound_type");
                switch (sound_type) {
                    case "1"://附近
                        SoundHelper.get().palyFuJin(context);
                        break;
                    case "2"://新订单
                        SoundHelper.get().palyNewOrder(context);
                        break;
                }

            } else if (JPushInterface.ACTION_NOTIFICATION_OPENED.equals(intent.getAction())) {
                MyLogger.i(TAG, "[MyReceiver] 用户点击打开了通知");
                //解析数据
                JSONObject jObj = new JSONObject(model);
                String type = jObj.getString("type");
                String t_indent_id = jObj.getString("t_indent_id");
                String url = jObj.getString("url");
                MyLogger.i(">>>>>>>>>type:" + type);
                MyLogger.i(">>>>>>>>>t_indent_id:" + t_indent_id);
                MyLogger.i(">>>>>>>>>url:" + url);
                switch (type) {
                    case "1":
                        //订单详情
                        Intent i = new Intent(context, OrderDetailsActivity.class);
                        bundle.putString("id", t_indent_id);
                        i.putExtras(bundle);
//				        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        context.startActivity(i);
                        /*Bundle bundle1 = new Bundle();
                        bundle1.putString("id", t_indent_id);
                        CommonUtil.gotoActivityWithData(context, OrderDetailsActivity.class, bundle1);*/
                        break;
                    case "2":
                    case "4":
                        //网页
                        Intent i2 = new Intent(context, WebContentActivity.class);
                        bundle.putString("url", url);
                        i2.putExtras(bundle);
//				        i2.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        i2.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        context.startActivity(i2);
                        /*Bundle bundle2 = new Bundle();
                        bundle2.putString("url", url);
                        CommonUtil.gotoActivityWithData(context, WebContentActivity.class, bundle2);*/
                        break;
                    case "3":
                        Intent i3 = new Intent(context, NoticeListActivity.class);
//                        bundle.putString("url", url);
                        i3.putExtras(bundle);
//				        i3.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        i3.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        context.startActivity(i3);
//                        CommonUtil.gotoActivity(context, NoticeListActivity.class);
                        break;
                }

            } else if (JPushInterface.ACTION_RICHPUSH_CALLBACK.equals(intent.getAction())) {
                MyLogger.i(TAG, "[MyReceiver] 用户收到到RICH PUSH CALLBACK: " + bundle.getString(JPushInterface.EXTRA_EXTRA));
                //在这里根据 JPushInterface.EXTRA_EXTRA 的内容处理代码，比如打开新的Activity， 打开一个网页等..


            } else if (JPushInterface.ACTION_CONNECTION_CHANGE.equals(intent.getAction())) {
                boolean connected = intent.getBooleanExtra(JPushInterface.EXTRA_CONNECTION_CHANGE, false);
                MyLogger.w(TAG, "[MyReceiver]" + intent.getAction() + " connected state change to " + connected);
            } else {
                MyLogger.i(TAG, "[MyReceiver] Unhandled intent - " + intent.getAction());
            }
        } catch (Exception e) {

        }

    }

    // 打印所有的 intent extra 数据
    private static String printBundle(Bundle bundle) {
        StringBuilder sb = new StringBuilder();
        for (String key : bundle.keySet()) {
            if (key.equals(JPushInterface.EXTRA_NOTIFICATION_ID)) {
                sb.append("\nkey:" + key + ", value:" + bundle.getInt(key));
            } else if (key.equals(JPushInterface.EXTRA_CONNECTION_CHANGE)) {
                sb.append("\nkey:" + key + ", value:" + bundle.getBoolean(key));
            } else if (key.equals(JPushInterface.EXTRA_EXTRA)) {
                if (TextUtils.isEmpty(bundle.getString(JPushInterface.EXTRA_EXTRA))) {
                    MyLogger.i(TAG, "This message has no Extra data");
                    continue;
                }
                try {
                    JSONObject json = new JSONObject(bundle.getString(JPushInterface.EXTRA_EXTRA));
                    Iterator<String> it = json.keys();

                    while (it.hasNext()) {
                        String myKey = it.next();
                        sb.append("\nkey:" + key + ", value: [" +
                                myKey + " - " + json.optString(myKey) + "]");

                        MyLogger.i(">>>>>>>>>" + json.optString(myKey));
                        model = json.optString(myKey) + "";
                    }

                } catch (JSONException e) {
                    MyLogger.e(TAG, "Get message extra JSON error!");
                }

            } else {
                sb.append("\nkey:" + key + ", value:" + bundle.getString(key));
            }
        }
        return sb.toString();
    }
	
	/*//send msg to MainActivity
	private void processCustomMessage(Context context, Bundle bundle) {
		if (MainActivity.isForeground) {
			String message = bundle.getString(JPushInterface.EXTRA_MESSAGE);
			String extras = bundle.getString(JPushInterface.EXTRA_EXTRA);
			Intent msgIntent = new Intent(MainActivity.MESSAGE_RECEIVED_ACTION);
			msgIntent.putExtra(MainActivity.KEY_MESSAGE, message);
			if (!ExampleUtil.isEmpty(extras)) {
				try {
					JSONObject extraJson = new JSONObject(extras);
					if (extraJson.length() > 0) {
						msgIntent.putExtra(MainActivity.KEY_EXTRAS, extras);
					}
				} catch (JSONException e) {

				}

			}
			LocalBroadcastManager.getInstance(context).sendBroadcast(msgIntent);
		}
	}*/
}
