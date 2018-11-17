package ru.voronezhtsev.serviceslesson;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Сервис генерирующий сообщения вида Text X, где X - целое число от 0 до {@link Integer#MAX_VALUE}
 *
 * @author Воронежцев Игорь on 17.11.2018.
 */
public class MyService extends IntentService {

    public static final int MSG_REGISTER_CLIENT = 1;
    public static final int MSG_UNREGISTER_CLIENT = 2;
    public static final String DATA_KEY = "MyService";

    private List<Messenger> mClients = new ArrayList<>();
    private Messenger mMessenger = new Messenger(new ServiceHandler());
    private static final String TAG = "MyService";

    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     */
    public MyService(String name) {
        super(name);
    }

    public MyService() {
        super("MyService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        int count = 0;
        while (!Thread.currentThread().isInterrupted()) {
            try {
                Thread.sleep(1000l);
                count++;
                Message msg = new Message();
                Bundle bundle = new Bundle();
                bundle.putString(DATA_KEY, "Text " + count);
                msg.setData(bundle);
                for (Messenger messenger : mClients) {
                    messenger.send(msg);
                }
            } catch (Exception e) {
                Log.e(TAG, "Error while generating message for client from service", e);
                Thread.currentThread().interrupt();
            }
        }
    }


    @Override
    public IBinder onBind(Intent intent) {
        return mMessenger.getBinder();
    }

    public static Intent newIntent(Context context) {
        return new Intent(context, MyService.class);
    }

    class ServiceHandler extends Handler {

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_REGISTER_CLIENT:
                    mClients.add(msg.replyTo);
                    break;
                case MSG_UNREGISTER_CLIENT:
                    mClients.remove(msg.replyTo);
                    break;
            }
        }
    }
}
