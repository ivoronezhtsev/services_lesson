package ru.voronezhtsev.serviceslesson;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.widget.TextView;

/**
 * Активити которая отображает то что сгенерировал сервис {@link MyService} в TextView
 * по центру экрана,биндится к сервису использует Messenger для получения данных от сервиса.
 *
 * @author Воронежцев Игорь on 17.11.2018
 */
public class SecondActivity extends Activity {

    private Messenger mService;
    private Messenger mMessenger = new Messenger(new IncomingHandler());
    private TextView mTextView;
    private ServiceConnection mServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mService = new Messenger(service);
            Message msg = Message.obtain(null, MyService.MSG_REGISTER_CLIENT);
            msg.replyTo = mMessenger;
            try {
                mService.send(msg);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mService = null;
        }
    };

    /**
     * Сгенерировать интент для запуска активити
     *
     * @param context контекст
     * @return интент, для запуска этой активити
     */
    public static Intent newIntent(Context context) {
        return new Intent(context, SecondActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
        mTextView = findViewById(R.id.generated_text_text_view);
    }

    @Override
    protected void onResume() {
        super.onResume();
        bindService();
    }

    @Override
    protected void onPause() {
        super.onPause();
        unbindService();
    }

    private void bindService() {
        bindService(MyService.newIntent(SecondActivity.this),
                mServiceConnection, 0);
    }

    private void unbindService() {
        unbindService(mServiceConnection);
    }

    class IncomingHandler extends Handler {

        @Override
        public void handleMessage(Message msg) {
            mTextView.setText(msg.getData().getString(MyService.DATA_KEY));
        }
    }
}
