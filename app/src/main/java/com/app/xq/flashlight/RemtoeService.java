package com.app.xq.flashlight;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.util.Log;

import androidx.annotation.NonNull;

public class RemtoeService extends Service {

    private static final String TAG = "12345";
    private final Messenger mMessenger = new Messenger(new MessengerHandler());

    public RemtoeService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mMessenger.getBinder();
    }

    private static class MessengerHandler extends Handler {

        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            Log.d(TAG, "服务端-handleMessage: " + msg);
            if (msg.what == 100) {
                Messenger msgfromClient = msg.replyTo;
                Message relpyMessage = Message.obtain(null, 200);
                Bundle bundle = new Bundle();
                bundle.putString("reply", "  嗯 ， 你的消息我已经收到，稍后会回复你！");
                relpyMessage.setData(bundle);
                try {
                    msgfromClient.send(relpyMessage);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            } else if (msg.what == 102) {
                //小强开源库
                Messenger replyTo = msg.replyTo;
                Bundle data = msg.getData();
                String string = data.getString("msg");
                Message obtain = Message.obtain(null, 102);
                Bundle bundle = new Bundle();
                bundle.putString("msg", "我收到了你的消息【" + string + "】马上回复");
                obtain.setData(bundle);
                try {
                    replyTo.send(obtain);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }

        }
    }
}
