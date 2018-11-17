package ru.voronezhtsev.serviceslesson;

import android.app.Activity;
import android.os.Bundle;

/**
 * Стартовый экран приложения, содержит кнопку запуска сервиса {@link MyService}
 * и кнопку отображения второй активити.
 *
 * @author Воронежцев Игорь on 17.11.2018.
 */
public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.start_service_button).setOnClickListener(
                v -> startService(MyService.newIntent(MainActivity.this)));
        findViewById(R.id.start_activity_button).setOnClickListener(
                v -> startActivity(SecondActivity.newIntent(MainActivity.this)));
    }
}
