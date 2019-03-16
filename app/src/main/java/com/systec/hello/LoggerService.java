package com.systec.hello;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Message;
import android.os.MessageQueue;
import android.util.Log;

import java.io.IOException;

public class LoggerService extends Service {

    private static Handler handler;
    private static Handler mainHandler;
    private HandlerThread handlerThread;
    private final String TAG = "LoggerService";

    public LoggerService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        handlerThread = new HandlerThread("LoggerService");
        handlerThread.start();

        handler = new Handler(handlerThread.getLooper()) {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                Log.i(TAG, "handleMessage: "+String.format("%s", msg.toString()));
//                Exec.Hello();
                Message msg2 = new Message();
                sendMessageRemote(msg2);
            }
        };
    }

    private boolean sendMessageRemote(Message msg) {
        if(null==mainHandler) {
            return false;
        }
        mainHandler.sendMessage(msg);
        return true;
    }

    public static boolean sendMessage(Message msg) {
        if(null==handler) {
            return false;
        }
        handler.sendMessage(msg);
        return true;
    }

    public static void setMainHandler(Handler handler) {
        mainHandler = handler;
    }
}
